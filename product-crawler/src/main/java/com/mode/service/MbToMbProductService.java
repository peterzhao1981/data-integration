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

import com.mode.entity.MaBangProduct;
import com.mode.util.CommonUtils;
import com.mode.util.ExcelUtils;

/**
 * Created by zhaoweiwei on 2018/2/26.
 */
public class MbToMbProductService {

    public static String inputPath = "/Users/zhaoweiwei/Documents/peter/马帮/product/input";
//    public static String inputPath = "D:/excel/product/input";
    public static String outputPath = "/Users/zhaoweiwei/Documents/peter/马帮/product/output/product.xls";
//    public static String outputPath = "D:/excel/product/output/product.xls";

    public void load() throws Exception {
        List<MaBangProduct> maBangProducts = new ArrayList<>();
        inputDaily(maBangProducts);
        output(maBangProducts);
    }

    private void inputDaily(List<MaBangProduct> maBangProducts) throws Exception {
        File input = new File(inputPath);

        if (input.isDirectory()) {
            File[] files = input.listFiles();
            for (File file : files) {
                System.out.println(file.getName());
                if ((!file.getName().endsWith("xlsx") && !file.getName().endsWith("xls"))
                        || file.isHidden()) {
                    continue;
                }
                System.out.println(file.getName());
                try (Workbook workbook = ExcelUtils.getWorkbook(file)) {

                    Sheet sheet = workbook.getSheetAt(0);
                    int rows = sheet.getPhysicalNumberOfRows();
                    System.out.println(rows);
                    for (int i = 1; i < rows; i++) {
                        Row row = sheet.getRow(i);
                        if (row != null) {
                            int cells = row.getLastCellNum();
                            MaBangProduct maBangProduct = new MaBangProduct();
                            for (int j = 0; j < cells; j++) {
                                Cell cell = row.getCell(j);
                                if (cell != null) {
                                    String value = null;
                                    int type = cell.getCellType();
                                    if (type == XSSFCell.CELL_TYPE_NUMERIC) {
                                        value = String.valueOf(cell.getNumericCellValue());
                                    } else if (type == XSSFCell.CELL_TYPE_STRING) {
                                        value = cell.getStringCellValue();
                                    } else if (type == XSSFCell.CELL_TYPE_FORMULA) {
                                        try {
                                            value = String.valueOf(cell.getStringCellValue());
                                        } catch (IllegalStateException e) {
                                            value = String.valueOf(cell.getNumericCellValue());
                                        }
                                    } else {
                                        System.out.println("j -> " + j);
                                        System.out.println("type -> " + type);
                                        continue;
                                    }
                                    if (value != null) {
                                        value = value.trim();
                                    }
                                    switch (j) {
                                        case 0:
                                            maBangProduct.setSku(CommonUtils.skuConverter(value));
                                            int index = value.indexOf("-");
                                            if (index > -1) {
                                                maBangProduct.setPrimarySku(value.substring(0, index));
                                            } else {
                                                maBangProduct.setPrimarySku(value);
                                            }
                                            break;
                                        case 1:
                                            maBangProduct.setTitle(value);
                                            break;
                                        case 31:
                                            maBangProduct.setImageUrl(value);
                                            break;
                                        case 18:
                                            maBangProduct.setPurchaseUrl(value);
                                            break;
                                        case 32:
                                            maBangProduct.setComment(value);
                                            break;
                                    }
                                }
                            }
                            if (!StringUtils.isEmpty(maBangProduct.getSku())) {
                                maBangProducts.add(maBangProduct);
                            }
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            }

        }
    }

    private void output(List<MaBangProduct> maBangProducts) throws Exception {
        if (maBangProducts.size() == 0) {
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
            Cell cell8;
            Cell cell9;
            Cell cell10;
            Cell cell11;
            Cell cell12;
            Cell cell13;
            Cell cell14;
            Cell cell15;
            Cell cell16;
            Cell cell17;
            Cell cell18;
            Cell cell19;
            Cell cell20;
            Cell cell21;


            row = sheet.createRow(0);
            cell0 = row.createCell(0);
            cell0.setCellValue("*商品编号");
            cell1 = row.createCell(1);
            cell1.setCellValue("*商品名称");
            cell2 = row.createCell(2);
            cell2.setCellValue("*成本价(格式:1234.50)");
            cell3 = row.createCell(3);
            cell3.setCellValue("*商品目录");
            cell4 = row.createCell(4);
            cell4.setCellValue("*商品仓位");
            cell5 = row.createCell(5);
            cell5.setCellValue("*商品仓库");
            cell6 = row.createCell(6);
            cell6.setCellValue("*商品库存");
            cell7 = row.createCell(7);
            cell7.setCellValue("供应商");
            cell8 = row.createCell(8);
            cell8.setCellValue("品牌");
            cell9 = row.createCell(9);
            cell9.setCellValue("销售员");
            cell10 = row.createCell(10);
            cell10.setCellValue("商品重量");
            cell11 = row.createCell(11);
            cell11.setCellValue("采购员");
            cell12 = row.createCell(12);
            cell12.setCellValue("包材");
            cell13 = row.createCell(13);
            cell13.setCellValue("售价");
            cell14 = row.createCell(14);
            cell14.setCellValue("备注");
            cell15 = row.createCell(15);
            cell15.setCellValue("*申报价值(格式:1234.50)");
            cell16 = row.createCell(16);
            cell16.setCellValue("原厂编号");
            cell17 = row.createCell(17);
            cell17.setCellValue("主sku");
            cell18 = row.createCell(18);
            cell18.setCellValue("配货员");
            cell19 = row.createCell(19);
            cell19.setCellValue("库存名称");
            cell20 = row.createCell(20);
            cell20.setCellValue("采购链接");
            cell21 = row.createCell(21);
            cell21.setCellValue("图片链接");
            for (int j = 0; j < maBangProducts.size(); j++) {
                MaBangProduct maBangProduct = maBangProducts.get(j);
                row = sheet.createRow(j + 1);
                cell0 = row.createCell(0);
                cell0.setCellValue(maBangProduct.getSku());
                cell1 = row.createCell(1);
                cell1.setCellValue(maBangProduct.getTitle());
                cell2 = row.createCell(2);
                cell2.setCellValue(maBangProduct.getCost());
                cell3 = row.createCell(3);
                cell3.setCellValue(maBangProduct.getCatelog());
                cell4 = row.createCell(4);
                cell4.setCellValue(maBangProduct.getStock());
                cell5 = row.createCell(5);
                cell5.setCellValue(maBangProduct.getWarehouse());
                cell6 = row.createCell(6);
                cell6.setCellValue(maBangProduct.getInventory());
                cell7 = row.createCell(7);
                cell7.setCellValue(maBangProduct.getVendor());
                cell8 = row.createCell(8);
                cell8.setCellValue(maBangProduct.getBrand());
                cell9 = row.createCell(9);
                cell9.setCellValue(maBangProduct.getSeller());
                cell10 = row.createCell(10);
                cell10.setCellValue(maBangProduct.getWeight());
                cell11 = row.createCell(11);
                cell11.setCellValue(maBangProduct.getPurhaser());
                cell12 = row.createCell(12);
                cell12.setCellValue(maBangProduct.getStuff());
                cell13 = row.createCell(13);
                cell13.setCellValue(maBangProduct.getListPrice());
                cell14 = row.createCell(14);
                cell14.setCellValue(maBangProduct.getComment());
                cell15 = row.createCell(15);
                cell15.setCellValue(maBangProduct.getDeclarePrice());
                cell16 = row.createCell(16);
                cell16.setCellValue(maBangProduct.getOriginId());
                cell17 = row.createCell(17);
                cell17.setCellValue(maBangProduct.getPrimarySku());
                cell18 = row.createCell(18);
                cell18.setCellValue(maBangProduct.getDispatcher());
                cell19 = row.createCell(19);
                cell19.setCellValue(maBangProduct.getInventoryName());
                cell20 = row.createCell(20);
                cell20.setCellValue(maBangProduct.getPurchaseUrl());
                cell21 = row.createCell(21);
                cell21.setCellValue(maBangProduct.getImageUrl());
            }
            workBook.write(out);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        MbToMbProductService service = new MbToMbProductService();
        service.load();
    }
}
