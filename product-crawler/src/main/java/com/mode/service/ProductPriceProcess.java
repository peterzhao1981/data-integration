package com.mode.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mode.checkProduct.Check1688;
import com.mode.checkProduct.Common;
import com.mode.repository.ProductPriceStore;
import com.mode.util.ExcelUtils;

/*
 * @author maxiaodong on 2018/05/10
 * @version 0.0.1 根据peter的几个1688的url，爬取了几个价格。插入数据库
 * @describe 关于商品的价格处理service
 */
@Transactional
@Service
public class ProductPriceProcess {
    // 自动注入dao层
    @Autowired
    private ProductPriceStore productPriceStore;

    /*
     * @param excelPath:输入excel所在的文件路径。
     */
    public void getProductPrice(String excelPath) throws Exception {
        // to do ，希望增加一个处理excel的通用方法，给定一个excel，可以读取出他的所有记录到一个list中。
        Workbook workbook = ExcelUtils.getWorkbook(new File(excelPath));
        Sheet sheet = workbook.getSheet("");
        String url = null;
        BigDecimal price = new BigDecimal(0);
        Check1688 check1688 = null;
        int spuIndex = ExcelUtils.getColumNumberByName(sheet, "");
        int urlIndex = ExcelUtils.getColumNumberByName(sheet, "");
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            String spu = null;
            Row nextRow = rowIterator.next();
            if (nextRow.getRowNum() < 1) {
                continue;
            }
            if (nextRow.getCell(urlIndex) != null) {
                url = nextRow.getCell(urlIndex).getStringCellValue();
                check1688 = new Check1688(null, Common.getDocument(url), null);
                price = check1688.getPriceListFrom1688().get(0);// 只需要一个值
            }
            if (nextRow.getCell(spuIndex) != null) {
                spu = nextRow.getCell(spuIndex).getStringCellValue();
            }
            productPriceStore.setCheckProductStatusStatus(spu, price);
        }
    }
}
