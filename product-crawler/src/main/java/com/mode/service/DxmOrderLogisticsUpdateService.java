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

import com.mode.entity.DxmOrderLogistics;
import com.mode.util.ExcelUtils;
import com.mode.util.RawDataUtil;

/**
 * Update Dxm order logistics from MaBang shipped orders.
 *
 * Created by zhaoweiwei on 2018/1/17.
 */
public class DxmOrderLogisticsUpdateService {

    public static String inputPath = "/Users/zhaoweiwei/Documents/peter/马帮/dxm_order_logistics/input";

    public static String outputPath = "/Users/zhaoweiwei/Documents/peter/马帮/dxm_order_logistics/output/logistics.xls";

    private String trackingUrlPrefix = "https://t.17track.net/en#nums=";


    private List<String> updatedOrders = new ArrayList<>();


    public void load() throws Exception {
        List<DxmOrderLogistics> dxmOrderLogisticses = new ArrayList<DxmOrderLogistics>();
        loadExculdeOrders();
        input(dxmOrderLogisticses);
        output(dxmOrderLogisticses);
    }

    private void loadExculdeOrders() {
        RawDataUtil.processLine("dxm_exclude_order.txt", line -> {
            if (StringUtils.isEmpty(line)) {
                return;
            }
            updatedOrders.add(line);
        });
        System.out.println(updatedOrders.size());
    }


    private void input(List<DxmOrderLogistics> dxmOrderLogisticses) throws Exception {
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
                            DxmOrderLogistics dxmOrderLogistics = new DxmOrderLogistics();
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
                                    if (j == 2) {
                                        if (updatedOrders.contains(value)) {
                                            System.out.println(value);
                                            dxmOrderLogistics.setOrderNo("");
                                            continue;
                                        }
                                        dxmOrderLogistics.setOrderNo(value);
                                    } else if (j == 15) {
                                        dxmOrderLogistics.setLogisticsMethod(value);
                                    } else if (j == 16) {
                                        dxmOrderLogistics.setTrackingNo(value);
                                        if (!StringUtils.isEmpty(value)) {
                                            dxmOrderLogistics.setTrackingUrl(trackingUrlPrefix + value);
                                        }
                                    }

                                }
                            }
                            if (!StringUtils.isEmpty(dxmOrderLogistics.getOrderNo())) {
                                dxmOrderLogisticses.add(dxmOrderLogistics);
                            }
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            }

        }
    }

    private void output(List<DxmOrderLogistics> dxmOrderLogisticses) throws Exception {
        if (dxmOrderLogisticses.size() == 0) {
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

            row = sheet.createRow(0);
            cell0 = row.createCell(0);
            cell0.setCellValue("*订单号");
            cell1 = row.createCell(1);
            cell1.setCellValue("*物流方式");
            cell2 = row.createCell(2);
            cell2.setCellValue("*跟踪号");
            cell3 = row.createCell(3);
            cell3.setCellValue("*发货类型");
            cell4 = row.createCell(4);
            cell4.setCellValue("追踪网址");

            for (int j = 0; j < dxmOrderLogisticses.size(); j++) {
                DxmOrderLogistics dxmOrderLogistics = dxmOrderLogisticses.get(j);
                row = sheet.createRow(j + 1);
                cell0 = row.createCell(0);
                cell0.setCellValue(dxmOrderLogistics.getOrderNo());
                cell1 = row.createCell(1);
                cell1.setCellValue(dxmOrderLogistics.getLogisticsMethod());
                cell2 = row.createCell(2);
                cell2.setCellValue(dxmOrderLogistics.getTrackingNo());
                cell3 = row.createCell(3);
                cell3.setCellValue(dxmOrderLogistics.getShippingType());
                cell4 = row.createCell(4);
                cell4.setCellValue(dxmOrderLogistics.getTrackingUrl());
            }
            workBook.write(out);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        DxmOrderLogisticsUpdateService service = new DxmOrderLogisticsUpdateService();
        service.load();
    }
}
