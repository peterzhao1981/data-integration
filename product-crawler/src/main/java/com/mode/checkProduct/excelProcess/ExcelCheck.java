package com.mode.checkProduct.excelProcess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.mode.util.ExcelUtils;

/*
 * Excel首行检查，在上传excel之后将立即进行
 */
public class ExcelCheck {

    public static List<String> checkExcels(String path) {
        List<String> resultList = new ArrayList<>();
        List<String> excelNameList = new ArrayList<>();
        int count = 0;
        try {
            excelNameList = ExcelUtils.getAllExcelFileName(path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (String excelName : excelNameList) {
            String result = "";
            if ((result = checkTitleRow(excelName, path)) != "") {
                count++;
                resultList.add(count + "、" + excelName + ":" + result);
            }
        }
        return resultList.size() > 0 ? resultList : null;
    }

    // 检查首行，是否包含url，sku
    private static String checkTitleRow(String excelName, String path) {
        int countSKU = 0;
        int countURL = 0;
        String result = "";
        String excelFilePath = path + "\\" + excelName;
        try (Workbook workBook = ExcelUtils.getWorkbook(new File(excelFilePath))) {
            Sheet sheet = workBook.getSheet("Sheet1");
            if (sheet == null) {
                sheet = workBook.getSheetAt(0);
            }
            Row row = sheet.getRow(0);
            for (Cell cell : row) {
                String cellValue = "";
                try {
                    cellValue = cell.getStringCellValue();
                } finally {
                    if (cellValue.contains("SKU") || cellValue.contains("sku")
                            || cellValue.contains("Sku")) {
                        countSKU++;
                    } else if (cellValue.contains("URL") || cellValue.contains("url")
                            || cellValue.contains("Url") || cellValue.contains("采购链接")) {
                        countURL++;
                    }
                }

            }
        } catch (Exception e) {
            return "error";// 说明Excel文件有误，不添加
        }
        if (countSKU == 0) {
            result += "不存在sku列 ";
        } else if (countSKU > 1) {
            result += "存在多个sku列 ";
        }

        if (countURL == 0) {
            result += "不存在url列 ";
        } else if (countURL > 1) {
            result += "存在多个url列 ";
        }

        return result;
    }

    public static void main(String[] args) {
        String result = ",";
        System.out.println(result.substring(1, result.lastIndexOf(",")));
        ExcelCheck.checkExcels(null);

    }

}
