package com.mode.checkProduct.excelProcess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.mode.checkProduct.commoninfo.Common;
import com.mode.checkProduct.commoninfo.ConfigInfo;
import com.mode.checkProduct.fileutils.DateUtils;
import com.mode.checkProduct.fileutils.FileUtils;
import com.mode.checkProduct.userinfo.CurrentUserService;
import com.mode.entity.CheckProductStatus;
import com.mode.util.ExcelUtils;

public class ExcelWrite {

    public static String process(List<CheckProductStatus> resultList, int kind) {
        String sheetName = "sheet1";
        String filePath = ConfigInfo.rootUplaodPath
                + CurrentUserService.getCurrentUser().getUserName() + "\\" + "output";
        FileUtils.deltAndCreatFile(filePath);
        File outFilePath = new File(filePath);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        filePath = filePath + "\\" + df.format(date) + "日_商品链接失效列表" + DateUtils.getCurrentTime()
                + ".xls";
        try {
            ExcelWrite.createExcel(sheetName, filePath);
            outFilePath = new File(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Excel文件创建失败");
        }

        try (Workbook workBook = ExcelUtils.getWorkbook(outFilePath);
                OutputStream out = new FileOutputStream(outFilePath)) {
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheet("sheet1");
            Row row;
            // Write header
            Cell cell0;
            Cell cell1;
            Cell cell2;
            Cell cell3;
            Cell cell4;
            row = sheet.createRow(0);
            cell0 = row.createCell(0);
            cell0.setCellValue("采购链接");
            cell1 = row.createCell(1);
            cell1.setCellValue("sku");
            cell2 = row.createCell(2);
            cell2.setCellValue("spu");
            cell3 = row.createCell(3);
            cell3.setCellValue("状态");
            // 只有当kind为2的时候才输出缺货信息
            if (kind == 2) {
                cell4 = row.createCell(4);
                cell4.setCellValue("缺货信息");
            }
            for (int i = 0; i < resultList.size(); i++) {
                row = sheet.createRow(i + 1);
                cell0 = row.createCell(0);
                cell0.setCellValue(resultList.get(i).getProductUrl());
                cell1 = row.createCell(1);
                cell1.setCellValue(resultList.get(i).getSku());
                cell2 = row.createCell(2);
                cell2.setCellValue(resultList.get(i).getSpu());
                cell3 = row.createCell(3);
                try {
                    if (resultList.get(i).getStatus().equals(Common.RES_PRODUCT_EXIST)) {
                        cell3.setCellValue("商品存在");
                    } else {
                        cell3.setCellValue("商品链接失效");
                    }
                } catch (Exception e) {
                    cell3.setCellValue("商品链接失效");
                }
                if (kind == 2) {
                    cell4 = row.createCell(4);
                    cell4.setCellValue(resultList.get(i).getLackInfo());
                }
            }
            workBook.write(out);
            out.close();
            workBook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return filePath;
    }

    // 创建excel
    public static void createExcel(String sheetName, String filePath) throws Exception {
        FileOutputStream outputStream = new FileOutputStream(filePath);// 完整的文件路径
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        workbook.createSheet(sheetName);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }

}
