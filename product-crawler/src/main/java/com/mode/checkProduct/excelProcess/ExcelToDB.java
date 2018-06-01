package com.mode.checkProduct.excelProcess;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    // 将excel的指定列导入导入数据库
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
        // test.setTest = new setTest();
        // test.setTest.setA = 1;
        // test.setTest.setB = 1;
        // System.out.println(test.setTest.setA + ";" + test.setTest.setB);
        // test.setTest = new setTest();
        // System.out.println(test.setTest.setA + ";" + test.setTest.setB);
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread() {
                public void run() {
                    test test = new test();
                    test.setTest = new setTest();
                    test.setTest.setA++;
                    Random random = new Random();
                    int jj = random.nextInt(2) % 2;
                    if (jj == 0) {
                        test.setTest.setB = 2;
                    }
                    System.out.println(Thread.currentThread().getName() + ";jj=" + jj + ";A="
                            + test.setTest.setA + ";B=" + test.setTest.setB + ";"
                            + test.setTest.hashCode());
                }
            };
            thread.start();
        }
    }

    public static void main1(String[] args)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // TODO Auto-generated method stub
        test test = new test();
        test.setId(100);
        test.setName("ma");
        Method[] methods = test.getClass().getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
            if (method.getName().contains("setId")) {
                method.invoke(test, 12);
            }
            if (method.getName().contains("setName")) {
                method.invoke(test, "nb");
            }
        }
        Field[] fields = test.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> class1 = field.getType();
            if (class1.getName() == "int") {
                field.set(test, 1);
            }
            if (class1.getName().equals("java.lang.String")) {
                field.set(test, null);
            }
            System.out.println(field.getType());
            System.out.println(field.getName());
        }
        List<Field> fields2 = Arrays.asList(test.getClass().getFields());
        fields2.stream().forEach(obj -> obj.setAccessible(true));
    }

}

class test {
    private int id;
    String name;
    public setTest setTest;
    public static int bb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class setTest {
    public int setA = 0;
    public int setB = 0;
}
