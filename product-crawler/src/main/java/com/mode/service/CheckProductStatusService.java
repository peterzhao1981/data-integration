package com.mode.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mode.checkProduct.Common;
import com.mode.checkProduct.commoninfo.ConfigInfo;
import com.mode.checkProduct.threadcontrol.Thread1688Task;
import com.mode.checkProduct.threadcontrol.ThreadOtherTask;
import com.mode.entity.CheckProductStatus;
import com.mode.repository.CheckProductStatusRepository;
import com.mode.util.ExcelUtils;

/*
 * @author maxiaodong on 2018/05/07
 * @version 0.0.1
 * @describe 验证采购链接的有效性爬虫，本类包含：excel数据与数据库的交互，url去重。
 */
@Transactional
@Service
public class CheckProductStatusService {
    // 自动注入dao层
    @Autowired
    private CheckProductStatusRepository checkProductRepository;

    private CheckProductStatus checkProductEntity;

    public static String input = "D:\\mxd\\workspace2\\ExcelData";
    private static String sheetName = "库存SKU";
    private static String spuColumnName = "库存SKU";
    private static String urlColumnName = "采购链接";
    private int spuColumnIndex, urlColumnIndex;

    // 主方法入口,一般开始的时候不需要第1、2步的方法，可以注释掉。每一次开始新的爬虫的时候可以将注释去掉
    public void process() throws Exception {
        System.out.println("开始处理excel");
        excelProcess();
        deleteRepeatRecord();// 本方法是清库
        System.out.println("开始爬虫");
        crawlerHtml();
    }

    // 一般开始的时候不需要第1、2步的方法，可以注释掉。每一次开始新的爬虫的时候可以将注释去掉

    // 将excel导入数据库中
    private void excelProcess() throws IOException {
        File excelPathFile;
        Sheet sheet;
        List<String> excelFileLsit = new ArrayList<String>();

        excelFileLsit = ExcelUtils.getAllExcelFileName(input);

        for (String excelName : excelFileLsit) {
            excelPathFile = new File(input + "\\" + excelName);
            Workbook workbook = ExcelUtils.getWorkbook(excelPathFile);
            sheet = workbook.getSheet(sheetName);
            // 得到spu列的index
            spuColumnIndex = ExcelUtils.getColumNumberByName(sheet, spuColumnName);
            // 得到url列的index
            urlColumnIndex = ExcelUtils.getColumNumberByName(sheet, urlColumnName);
            if (spuColumnIndex == -1 || urlColumnIndex == -1) {
                System.out.printf("%s文件不存在必须的spu或url列，请检查下载的excel", excelName);
            } else {
                importExcelToDB(sheet, spuColumnIndex, urlColumnIndex);
            }
        }
        System.out.println("导入完成");
        // 导入成功之后，立即删除表中的重复记录
        deleteRepeatRecord();
        System.out.println("删除重复记录完成");
    }

    // 将excel导入数据库
    private void importExcelToDB(Sheet sheet, int spuColumnIndex, int urlColumnIndex) {

        Iterator<Row> iterator = sheet.iterator();// 行迭代器
        while (iterator.hasNext()) {
            String spu = null;
            String url = null;
            Row nextRow = iterator.next();
            if (nextRow.getRowNum() < 1) {
                continue;// 如果是第一行就跳过
            }
            spu = nextRow.getCell(spuColumnIndex).getStringCellValue().toString().trim();
            if (spu.length() > 0 && spu != null && !spu.equals("null") && spu.contains("-")) {
                spu = spu.split("-")[0];// 只取前面的spu码
            }
            if (nextRow.getCell(urlColumnIndex) != null) {
                url = nextRow.getCell(urlColumnIndex).getStringCellValue().trim();
                if (url.length() == 0 && url.equals("")) {
                    url = null;
                }
            }
            checkProductEntity = new CheckProductStatus();
            checkProductEntity.setProductUrl(url);
            checkProductEntity.setSpu(spu);
            checkProductRepository.save(checkProductEntity);
        }
    }

    // 删除数据库中的重复记录,在从Excel导入数据库完成之后执行。
    private void deleteRepeatRecord() {
        List<Integer> unionList = new ArrayList<Integer>();
        unionList = checkProductRepository.findListID1();
        unionList.addAll(checkProductRepository.findListID2());
        checkProductRepository.deleteCheckProductStatus(unionList);
    }

    // 将得到的待爬网页list分段，分为1688和非1688
    private void crawlerHtml() {
        // 1688集合
        List<CheckProductStatus> noCrawlerResult1688 = new ArrayList<>();
        noCrawlerResult1688 = checkProductRepository.findCrawler1688Result(Common.RES_PRODUCT_EXIST,
                Common.RES_PRODUCT_INVALID, Common.URL_ERROR);// 商品存在与不存在的都不验证了，省的浪费次数

        // 非1688网址集合
        List<CheckProductStatus> noCrawlerResultOther = new ArrayList<>();
        noCrawlerResultOther = checkProductRepository.findCrawlerNot1688Result(
                Common.RES_PRODUCT_EXIST, Common.URL_ERROR, Common.URL_ERROR);// 本来应该是缺货

        Thread1688Task thread1688Task = new Thread1688Task(noCrawlerResult1688,
                ConfigInfo.threadNum1688);// 默认线程是10个，可以开个线程池，后续优化
        ThreadOtherTask threadOtherTask = new ThreadOtherTask(noCrawlerResultOther,
                ConfigInfo.threadNumOther);// 默认线程是5个，可以开个线程池，后续优化
        // 启动线程
        thread1688Task.handleList();
        threadOtherTask.handleList();
    }

    // 输出供应商名单，从数据库中读取供应商。用处不大，只是为了分析有哪些网页需要爬取
    private void printSupplierList() {

        List<CheckProductStatus> noCrawlerResult = new ArrayList<>();
        noCrawlerResult = checkProductRepository.findCrawlerResult(Common.RES_PRODUCT_EXIST);
        List<String> agent = new ArrayList<>();
        for (int i = 0; i < noCrawlerResult.size(); i++) {
            String tmp = noCrawlerResult.get(i).getProductUrl();
            if (tmp.contains(".com")) {
                String tmpSplit = tmp.split(".com")[0].toString();
                if (!agent.contains(tmpSplit)) {
                    agent.add(tmpSplit);
                    System.out.println(tmp);
                }
            }
        }
    }

    public synchronized void test() {
        Thread thread1 = new Thread() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Thread thread2 = new Thread() {
                        public void run() {
                            for (int j = 0; j < 5; j++) {
                                System.out.println(Thread.currentThread().getName() + "打印" + j);
                            }

                        }
                    };
                    thread2.start();
                }
            }
        };
        thread1.start();

    }

    public static void main(String[] args) {
        CheckProductStatusService aa = new CheckProductStatusService();
        aa.test();
    }

}
