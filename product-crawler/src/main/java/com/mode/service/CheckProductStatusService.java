package com.mode.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mode.checkProduct.Common;
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

    // 主方法入口
    public void process() throws Exception {
        checkProductRepository.deleteAll();
        excelProcess();
        crawlerHtml();
    }

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

    private void crawlerHtml() {
        List<CheckProductStatus> noCrawlerResult = new ArrayList<>();
        noCrawlerResult = checkProductRepository.findCrawlerResult();

        for (int i = 0; i < noCrawlerResult.size(); i++) {
            String url = noCrawlerResult.get(i).getProductUrl().trim();
            Long id = noCrawlerResult.get(i).getId();
            url = url.replaceAll("：", ":").replaceAll(" ", "");// 去掉URL中错误的字符
            String domainStr = getDomainStr(url);// 得到待爬网页的主域名
            if (domainStr.equals("error url")) {
                // 将url格式错误的信息，插入到数据库中。
                checkProductRepository.setCheckProductStatusStatus(domainStr, id);
            } else {
                crawlerMain(domainStr, url, id);// 开始爬虫
            }
        }
    }

    // 输出供应商名单，从数据库中读取供应商。用处不大，只是为了分析有哪些网页需要爬取
    private void printSupplierList() {

        List<CheckProductStatus> noCrawlerResult = new ArrayList<>();
        noCrawlerResult = checkProductRepository.findCrawlerResult();
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

    // 得到product_url的域名，用于判断需要使用哪一种爬虫方法，抓取网页.使用该方法之前需要将url的异常字符去掉
    private String getDomainStr(String url) {
        String regexStr = "[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)";
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url.trim().replaceAll("：", ":").replaceAll(" ", ""));
        if (matcher.find()) {
            return matcher.group();
        } else {
            // url有问题，直接跳过爬取，将status设置为"error url"
            System.out.println("url有问题，请检查url，或者正则表达式代码");
            return "error url";
        }
    }

    /*
     * @param domainStr:域名，url:完整url，id:是url对应的id
     * 
     * @describe 爬取网页的主入口
     */
    private void crawlerMain(String domainStr, String url, Long id) {
        Common common = new Common();
        common.process(domainStr, url, id);
    }

    public static void main(String[] args) {
        String string = "http：//us.memebox.com/product/5887?showColorOptions=false"
                .replaceAll("：", ":").replaceAll(" ", "");
        System.out.println(string);
        String regexStr = "[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)";
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

}
