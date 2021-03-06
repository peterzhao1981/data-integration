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
import com.mode.checkProduct.fileutils.FileUtils;
import com.mode.checkProduct.threadcontrol.Thread1688Task;
import com.mode.checkProduct.threadcontrol.ThreadCountTask;
import com.mode.checkProduct.threadcontrol.ThreadOtherTask;
import com.mode.checkProduct.userinfo.CurrentUserService;
import com.mode.checkProduct.userinfo.User;
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
    // private static final Logger logger =
    // LoggerFactory.getLogger(CheckProductStatusService.class);

    // 自动注入dao层
    @Autowired
    private CheckProductStatusRepository checkProductRepository;

    private CheckProductStatus checkProductEntity;

    // public static String input =
    // "C:\\Users\\Administrator\\excel\\checkProductStatus\\orignExcel";
    private static String[] skuColumnName = { "库存SKU", "sku" };
    private static String[] urlColumnName = { "采购链接", "product_url", "url" };
    private static String[] product_idColumnName = { "product_id" };
    private int skuColumnIndex, urlColumnIndex, product_idColumnIndex;
    private static String skuKey = "sku";
    private static String urlKey = "product_url";
    private static String productIDKey = "product_id";

    // 第一次使用时需要清库,已经在前台控制。点击“导入数据库”即执行清库
    public String process() throws Exception {
        String result = "error";
        System.out.println("开始爬虫");
        crawlerHtml();
        System.out.println("第1次爬虫完成");
        crawlerHtml();
        System.out.println("第2次爬虫完成");
        result = crawlerHtml();// 执行两次
        System.out.println("爬虫结束");
        return result;
    }

    // 在上传excel的时候清理数据库？，在导出之后，清库？
    // 将excel导入数据库中
    public void excelProcess() throws IOException {
        // clearTable();
        File excelPathFile;
        Sheet sheet;
        String input = ConfigInfo.rootUplaodPath
                + CurrentUserService.getCurrentUser().getUserName();
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
        FileUtils.clearDirectory(input);// 清理文件
    }

    public void importExcelToDB(Sheet sheet, Map<String, Integer> indexMap) {
        User user = CurrentUserService.getCurrentUser();
        Iterator<Row> iterator = sheet.iterator();// 行迭代器
        while (iterator.hasNext()) {
            String spu = null;
            String sku = null;
            String url = null;
            Long productId = 0L;
            // 之所以这样写，是为了保证线程安全，如果将checkProductEntity设置为static将会导致所有线程共享同一份对象
            checkProductEntity = new CheckProductStatus();
            checkProductEntity.setUsername(user.getUserName());
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

    // 将得到的待爬网页list分段，分为1688和非1688
    private String crawlerHtml() {
        String userName = CurrentUserService.getCurrentUser().getUserName();
        List<String> unCrawlerList = new ArrayList<>();
        unCrawlerList.add(Common.RES_PRODUCT_EXIST);
        unCrawlerList.add(Common.RES_PRODUCT_INVALID);
        unCrawlerList.add(Common.RES_PRODUCT_LACK);

        // 1688集合
        List<String> noCrawlerResult1688 = new ArrayList<>();
        noCrawlerResult1688 = checkProductRepository.findCrawler1688Result(userName, unCrawlerList);// 商品存在与不存在的都不验证了，省的浪费次数

        // 非1688网址集合
        List<String> noCrawlerResultOther = new ArrayList<>();
        noCrawlerResultOther = checkProductRepository.findCrawlerNot1688Result(userName,
                unCrawlerList);

        ConfigInfo.unDealProductNumber = noCrawlerResult1688.size() + noCrawlerResultOther.size();
        Thread1688Task thread1688Task = new Thread1688Task(noCrawlerResult1688,
                ConfigInfo.threadNum1688);// 默认线程是10个，可以开个线程池，后续优化
        ThreadOtherTask threadOtherTask = new ThreadOtherTask(noCrawlerResultOther,
                ConfigInfo.threadNumOther);// 默认线程是10个，可以开个线程池，后续优化
        // 启动线程
        ThreadCountTask threadCountTask = ThreadCountTask.getInstance();

        System.out.println(ConfigInfo.unDealProductNumber);
        thread1688Task.handleList();
        threadOtherTask.handleList();
        try {
            threadCountTask.endGateCountAwait();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "error";
        }
        System.out.println("执行完毕");
        return "success";
    }

    // 导出结果到excel中
    public String exportDBtoExcel(int resultKind) {
        String userName = CurrentUserService.getCurrentUser().getUserName();
        String downFilePath = null;
        try {
            System.out.println("开始导出结果到excel中");
            if (resultKind == 1) {
                downFilePath = ExcelWrite.process(getResult(userName), resultKind);
            } else if (resultKind == 2) {
                downFilePath = ExcelWrite.process(getResultContainLackInfo(userName), resultKind);
            } else {
                System.out.println("请定义要导出的excel类型");
            }
            System.out.println("导出完成");
        } catch (Exception e) {
            System.out.println("导出失败");
            return null;
        }
        return downFilePath;

    }

    // 得到导出的结果
    public List<CheckProductStatus> getResult(String userName) {
        return checkProductRepository.getResult(Common.RES_PRODUCT_EXIST, userName);
    }

    // 得到导出的结果，包含缺货信息
    public List<CheckProductStatus> getResultContainLackInfo(String userName) {
        return checkProductRepository.getResultContainLackInfo(Common.RES_PRODUCT_EXIST, userName);
    }

    public void deleteUserRecord(String userName) throws Exception {
        checkProductRepository.deleteByUsername(userName);
    }

    public List<CheckProductStatus> get1688APIUseCurrency(String userName) {
        return checkProductRepository.findByStatusAndUsername(Common.API_MAX, userName);
    }

}
