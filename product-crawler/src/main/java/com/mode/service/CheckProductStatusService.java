package com.mode.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mode.checkProduct.commoninfo.Common;
import com.mode.checkProduct.commoninfo.ConfigInfo;
import com.mode.checkProduct.excelProcess.ExcelWrite;
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

    public static String input = "C:\\Users\\Administrator\\excel\\checkProductStatus\\orignExcel";
    private static String sheetName = "Sheet1";
    private static String[] skuColumnName = { "库存SKU", "sku" };
    private static String[] urlColumnName = { "采购链接", "product_url" };
    private static String[] product_idColumnName = { "product_id" };
    private int skuColumnIndex, urlColumnIndex, product_idColumnIndex;
    private static String skuKey = "sku";
    private static String urlKey = "product_url";
    private static String productIDKey = "product_id";

    // 第一次使用时需要清库check_status
    public synchronized void process() throws Exception {
        System.out.println("开始爬虫");
        crawlerHtml();
        System.out.println("爬虫结束");
    }

    // 一般开始的时候不需要第1、2步的方法，可以注释掉。每一次开始新的爬虫的时候可以将注释去掉
    // 将excel导入数据库中
    public void excelProcess() throws IOException {
        clearTable();
        File excelPathFile;
        Sheet sheet;
        List<String> excelFileLsit = new ArrayList<String>();

        excelFileLsit = ExcelUtils.getAllExcelFileName(input);
        for (String excelName : excelFileLsit) {
            excelPathFile = new File(input + "\\" + excelName);
            Workbook workbook = ExcelUtils.getWorkbook(excelPathFile);
            sheet = workbook.getSheetAt(0);// (sheetName);
            Map<String, Integer> indexMap = new HashMap<>();
            // 得到spu列的index
            skuColumnIndex = ExcelUtils.getColumNumberByName(sheet, skuColumnName);
            // 得到url列的index
            urlColumnIndex = ExcelUtils.getColumNumberByName(sheet, urlColumnName);

            product_idColumnIndex = ExcelUtils.getColumNumberByName(sheet, product_idColumnName);
            indexMap.put(skuKey, skuColumnIndex);
            indexMap.put(urlKey, urlColumnIndex);
            indexMap.put(productIDKey, product_idColumnIndex);
            importExcelToDB(sheet, indexMap);

        }
        System.out.println("导入完成");
        // 导入成功之后，立即删除表中的重复记录,由于需要导出sku，所以删除这个操作。修改日期0523
        // deleteRepeatRecord();
        // System.out.println("删除重复记录完成");
    }

    public void importExcelToDB(Sheet sheet, Map<String, Integer> indexMap) {

        Iterator<Row> iterator = sheet.iterator();// 行迭代器
        while (iterator.hasNext()) {
            String spu = null;
            String sku = null;
            String url = null;
            Long productId = 0L;
            // 之所以这样写，是为了保证线程安全，如果将checkProductEntity设置为static将会导致所有线程共享同一份对象
            checkProductEntity = new CheckProductStatus();
            int skuColumnIndex = indexMap.get("sku");
            int urlColumnIndex = indexMap.get("product_url");
            int product_idColumnIndex = indexMap.get("product_id");
            Row nextRow = iterator.next();
            if (nextRow.getRowNum() < 1) {
                continue;// 如果是第一行就跳过
            }
            if (skuColumnIndex != -1 && nextRow.getCell(skuColumnIndex) != null) {
                try {
                    sku = nextRow.getCell(skuColumnIndex).getStringCellValue().toString();
                } catch (Exception e) {
                    sku = null;
                }
                try {
                    spu = sku.split("-")[0];
                } catch (Exception e) {
                    spu = sku;
                }

                checkProductEntity.setSku(sku);
                checkProductEntity.setSpu(spu);
            }
            // 此时就应将错误的url进行处理
            if (urlColumnIndex != -1 && nextRow.getCell(urlColumnIndex) != null) {
                try {
                    url = nextRow.getCell(urlColumnIndex).getStringCellValue().toString();
                } catch (Exception e) {
                    url = null;
                }
                url = Common.getRightURL(url);
                if (url.length() == 0 || url.equals("null"))
                    url = null;
                checkProductEntity.setProductUrl(url);
            }

            if (product_idColumnIndex != -1 && nextRow.getCell(product_idColumnIndex) != null) {
                productId = (long) (nextRow.getCell(product_idColumnIndex).getNumericCellValue());
                checkProductEntity.setProductId(productId);
            }
            checkProductRepository.save(checkProductEntity);
        }
    }

    // 将excel导入数据库
    private void importExcelToDB(Sheet sheet, int spuColumnIndex, int urlColumnIndex) {

        Iterator<Row> iterator = sheet.iterator();// 行迭代器
        while (iterator.hasNext()) {
            String spu = null;
            String sku = null;
            String url = null;
            Row nextRow = iterator.next();
            if (nextRow.getRowNum() < 1) {
                continue;// 如果是第一行就跳过
            }
            sku = nextRow.getCell(spuColumnIndex).getStringCellValue().toString().trim();
            if (sku != null && sku.length() > 0 && !sku.equals("null") && sku.contains("-")) {
                spu = sku.split("-")[0];// 只取前面的spu码
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
            checkProductEntity.setSku(sku);
            checkProductRepository.save(checkProductEntity);
        }
    }

    // 将得到的待爬网页list分段，分为1688和非1688
    private void crawlerHtml() {
        // 1688集合
        List<String> noCrawlerResult1688 = new ArrayList<>();
        noCrawlerResult1688 = checkProductRepository
                .findCrawler1688Result_1(Common.RES_PRODUCT_EXIST, Common.RES_PRODUCT_INVALID);// 商品存在与不存在的都不验证了，省的浪费次数

        // 非1688网址集合
        List<String> noCrawlerResultOther = new ArrayList<>();
        noCrawlerResultOther = checkProductRepository.findCrawlerNot1688Result_1(
                Common.RES_PRODUCT_EXIST, Common.RES_PRODUCT_LACK, Common.RES_PRODUCT_INVALID);

        Thread1688Task thread1688Task = new Thread1688Task(noCrawlerResult1688,
                ConfigInfo.threadNum1688);// 默认线程是10个，可以开个线程池，后续优化
        ThreadOtherTask threadOtherTask = new ThreadOtherTask(noCrawlerResultOther,
                ConfigInfo.threadNumOther);// 默认线程是10个，可以开个线程池，后续优化
        // 启动线程
        thread1688Task.handleList();
        threadOtherTask.handleList();
    }

    // 每一次开始新的爬虫之前，需要清库，或者手动将表删掉。启动程序即可重新建表
    private void clearTable() {
        checkProductRepository.deleteAll();
    }

    // 导出结果到excel中
    public void exportDBtoExcel() {
        try {
            System.out.println("开始导出结果到excel中");
            ExcelWrite.process(checkProductRepository.getResult(Common.RES_PRODUCT_EXIST));
            System.out.println("导出完成");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("导出失败");
        }

    }

    // 删除数据库中的重复记录,在从Excel导入数据库完成之后执行。
    private void deleteRepeatRecord() {
        List<Integer> unionList = new ArrayList<Integer>();
        unionList = checkProductRepository.findListID1();
        unionList.addAll(checkProductRepository.findListID2());
        checkProductRepository.deleteCheckProductStatus(unionList);
    }

    public static void main(String[] args) {

    }

}
