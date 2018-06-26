package com.mode.checkProduct.excelProcess;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mode.checkProduct.commoninfo.Common;
import com.mode.entity.CheckProductStatus;
import com.mode.repository.CheckProductStatusRepository;

@Component
public class ExcelToDB {

    // 设置自动注入
    public static ExcelToDB excelToDBUtil;

    @Autowired
    private CheckProductStatusRepository checkProductRepository;

    private CheckProductStatus checkProductEntity;

    @PostConstruct
    public void init() {
        excelToDBUtil = this;
        excelToDBUtil.checkProductRepository = this.checkProductRepository;
    }

    // 将excel的指定列导入数据库
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
                sku = nextRow.getCell(skuColumnIndex).getStringCellValue().toString().trim();
                if (sku.contains("-")) {
                    spu = sku.split("-")[0];// 只取前面的spu码
                } else {
                    spu = sku;
                }
                checkProductEntity.setSku(sku);
                checkProductEntity.setSpu(spu);
            }
            // 此时就应将错误的url进行处理
            if (urlColumnIndex != -1 && nextRow.getCell(urlColumnIndex) != null) {
                url = nextRow.getCell(urlColumnIndex).getStringCellValue().trim();
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

    public CheckProductStatus setValueToDBFiled(HashMap<String, Object> param,
            CheckProductStatus checkProductStatus)
            throws IllegalArgumentException, ReflectiveOperationException {
        List<Field> reflectList = Arrays.asList(checkProductStatus.getClass().getDeclaredFields());
        for (String key : param.keySet()) {
            for (Field field : reflectList) {
                String fieldName = field.getName();
                if (key.contains("_")) {
                    String first = key.split("_")[0];
                    String second = key.split("_")[1];
                    if (fieldName.contains(first) && fieldName.contains(second)) {
                        field.set(checkProductStatus, param.get(key));
                    }
                }
                if (key.equalsIgnoreCase(fieldName)) {
                    field.set(checkProductStatus, param.get(key));
                }
            }
        }
        return checkProductStatus;
    }

    public static void main(String[] args) {

    }
}
