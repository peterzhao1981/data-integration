package com.mode.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.util.StringUtils;

import com.mode.entity.MaBangPurchase;
import com.mode.util.ExcelUtils;
import com.mode.util.RawDataUtil;

/**
 * Created by zhaoweiwei on 2018/1/8.
 */
public class DxmToMbPurchaseService {

    public static String inputPath = "/Users/zhaoweiwei/Documents/peter/马帮/purchase/input";
	//public static String inputPath = "D:/excel/purchase/input";
    public static String outputPath = "/Users/zhaoweiwei/Documents/peter/马帮/purchase/output/purchase.xls";
	//public static String outputPath = "D:/excel/purchase/output/purchase.xls";

    private List<String> uploadedPurchases = new ArrayList<>();


    public void load() throws Exception {
        List<MaBangPurchase> maBangPurchases = new ArrayList<>();
        loadExculdePurchases();
        input(maBangPurchases);
        output(maBangPurchases);
    }

    private void loadExculdePurchases() {
        RawDataUtil.processLine("exclude_purchase.txt", line -> {
            if (StringUtils.isEmpty(line)) {
                return;
            }
            uploadedPurchases.add(line);
        });
        System.out.println(uploadedPurchases.size());
    }

    private void input(List<MaBangPurchase> maBangPurchases) throws Exception {
        File input = new File(inputPath);

        if (input.isDirectory()) {
            File[] files = input.listFiles();
            for (File file : files) {
                System.out.println(file.getName());
                if ((!file.getName().endsWith("xlsx") && !file.getName().endsWith("xls"))
                         || file.isHidden()) {
                    continue;
                }
                try (Workbook workbook = ExcelUtils.getWorkbook(file)) {

                    Sheet sheet = workbook.getSheetAt(0);
                    int rows = sheet.getPhysicalNumberOfRows();
                    System.out.println(rows);
                    for (int i = 1; i < rows; i++) {
                        Row row = sheet.getRow(i);
                        if (row != null) {
                            int cells = row.getPhysicalNumberOfCells();
                            MaBangPurchase maBangPurchase = new MaBangPurchase();
                            for (int j = 0; j < cells; j++) {
                                Cell cell = row.getCell(j);
                                if (cell != null) {
                                    String value = null;
                                    int type = cell.getCellType();
                                    if (type == XSSFCell.CELL_TYPE_NUMERIC) {
                                        value = String.valueOf(cell.getNumericCellValue());
                                    } else if (type == XSSFCell.CELL_TYPE_STRING) {
                                        value = cell.getStringCellValue();
                                    } else {
                                        System.out.println("type -> " + type);
                                        continue;
                                    }
                                    if (value != null) {
                                        value = value.trim();
                                    }
                                    switch (j) {
                                        case 0:
                                            value = "2018" + value;
                                            if (uploadedPurchases.contains(value)) {
                                                System.out.println(value);
                                                maBangPurchase.setBatchNo("");
                                                break;
                                            }
                                            maBangPurchase.setBatchNo(value);
                                            break;
                                        case 2:
                                            if (!StringUtils.isEmpty(value)) {
                                                value = value.replaceAll(" ", "");
                                                if (value.endsWith("#")) {
                                                    value = value.substring(0, value.length() - 1);
                                                }
                                            }
                                            maBangPurchase.setSku(value);
                                            break;
                                        case 4:
                                            maBangPurchase.setQuantity(value);
                                            break;
                                        case 11:
                                            if (!"上海马帮合作仓".equals(value)) {
                                                maBangPurchase.setBatchNo("");
                                            }
                                            break;
                                        case 16:
                                            if (StringUtils.isEmpty(value)) {
                                                maBangPurchase.setBatchNo("");
                                                break;
                                            }
                                            maBangPurchase.setTrackNo(value);
                                            break;

                                    }
                                }
                            }
                            if (!StringUtils.isEmpty(maBangPurchase.getBatchNo())) {
                                maBangPurchases.add(maBangPurchase);
                            }
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            }

        }
    }


    private void output(List<MaBangPurchase> maBangPurchases) throws Exception {
        if (maBangPurchases.size() == 0) {
            return;
        }

        File output = new File(outputPath);
        try (Workbook workBook = ExcelUtils.getWorkbook(output); OutputStream out = new FileOutputStream(output)) {

            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);


            Row row;
            // Write header
            Cell cell0;
            Cell cell1;
            Cell cell2;
            Cell cell3;
            Cell cell4;
            Cell cell5;
            Cell cell6;
            Cell cell7;

            row = sheet.createRow(0);
            cell0 = row.createCell(0);
            cell0.setCellValue("*采购批次");
            cell1 = row.createCell(1);
            cell1.setCellValue("*供货商");
            cell2 = row.createCell(2);
            cell2.setCellValue("*运费(格式:1234.50)");
            cell3 = row.createCell(3);
            cell3.setCellValue("*店铺");
            cell4 = row.createCell(4);
            cell4.setCellValue("*商品编号");
            cell5 = row.createCell(5);
            cell5.setCellValue("*订购量");
            cell6 = row.createCell(6);
            cell6.setCellValue("*订购价格");
            cell7 = row.createCell(7);
            cell7.setCellValue("*运单号");

            for (int j = 0; j < maBangPurchases.size(); j++) {
                MaBangPurchase maBangPurchase = maBangPurchases.get(j);
                row = sheet.createRow(j + 1);
                cell0 = row.createCell(0);
                cell0.setCellValue(maBangPurchase.getBatchNo());
                cell1 = row.createCell(1);
                cell1.setCellValue(maBangPurchase.getVendor());
                cell2 = row.createCell(2);
                cell2.setCellValue(maBangPurchase.getFreight());
                cell3 = row.createCell(3);
                cell3.setCellValue(maBangPurchase.getStore());
                cell4 = row.createCell(4);
                cell4.setCellValue(maBangPurchase.getSku());
                cell5 = row.createCell(5);
                cell5.setCellValue(maBangPurchase.getQuantity());
                cell6 = row.createCell(6);
                cell6.setCellValue(maBangPurchase.getCost());
                cell7 = row.createCell(7);
                cell7.setCellValue(maBangPurchase.getTrackNo());
            }
            workBook.write(out);
        } catch (Exception e) {
            throw e;
        }
    }


    public static void main(String[] args) throws Exception {
        DxmToMbPurchaseService service = new DxmToMbPurchaseService();
        service.load();
    }
}


