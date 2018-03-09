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

import com.mode.entity.MaBangOrder;
import com.mode.util.CommonUtils;
import com.mode.util.ExcelUtils;
import com.mode.util.RawDataUtil;

/**
 * Created by zhaoweiwei on 2018/2/9.
 */
public class MbToMbOrderService {

    public static String inputPath = "/Users/zhaoweiwei/Documents/peter/马帮/order/input";
    //	public static String inputPath = "C:/Users/Administrator/order/input";
    public static String outputPath = "/Users/zhaoweiwei/Documents/peter/马帮/order/output/order.xls";
//	public static String outputPath = "C:/Users/Administrator/order/output/order1.xls";


    private List<String> excludeOrders = new ArrayList<>();

    private List<String> includeOrders = new ArrayList<>();


    public void load() throws Exception {
        List<MaBangOrder> maBangOrders = new ArrayList<>();
        loadExculdeOrders();
        input(maBangOrders);
        output(maBangOrders);
    }

    private void loadExculdeOrders() {
        RawDataUtil.processLine("mabang_exclude_order.txt", line -> {
            if (StringUtils.isEmpty(line)) {
                return;
            }
            excludeOrders.add(line);
        });
        System.out.println("excludeOrders -> " + excludeOrders.size());
        RawDataUtil.processLine("mabang_include_order.txt", line -> {
            if (StringUtils.isEmpty(line)) {
                return;
            }
            includeOrders.add(line);
        });
        System.out.println("includeOrders -> " + includeOrders.size());

    }

    private void input(List<MaBangOrder> maBangOrders) throws Exception {
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
                        String orderNo = "";
                        String buyerName = "";
                        String buyerPhone = "";
                        String buyerAddress1 = "";
                        String buyerAddress2 = "";
                        String buyerCity = "";
                        String buyerState = "";
                        String buyerZip = "";
                        String buyerCountry = "";
                        String listPrice = "";
                        String sku = "";
                        String productTitle = "";
                        String quantity = "";
                        String paidDate = "";
                        if (row != null) {
                            int cells = row.getPhysicalNumberOfCells();
                            MaBangOrder maBangOrder = new MaBangOrder();
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
//                                        System.out.println("type -> " + type);
                                        continue;
                                    }
                                    if (value != null) {
                                        value = value.trim();
                                    }
                                    switch (j) {
                                        case 1:
                                            if (StringUtils.isEmpty(value)) {
                                                throw new Exception("Order No is empty");
                                            }
                                            if (excludeOrders.contains(value)) {
//                                                System.out.println("ignore " + value);
                                                orderNo = "";
                                                break;
                                            }
                                            orderNo = value;
                                            break;
                                        case 2:
                                            if (!"马帮合作仓".equals(value)) {
                                                orderNo = "";
                                            }
                                            break;
                                        case 4:
                                            buyerName = value;
                                            break;
                                        case 5:
                                            buyerPhone = value;
                                            break;
                                        case 12:
                                            buyerAddress1 = value;
                                            break;
                                        case 13:
                                            buyerAddress2 = value;
                                            break;
                                        case 10:
                                            buyerCity = value;
                                            break;
                                        case 9:
                                            buyerState = value;
                                            break;
                                        case 11:
                                            buyerZip = value;
                                            break;
                                        case 7:
                                            buyerCountry = StringUtils.isEmpty(value) ? value : value.toUpperCase();
                                            break;
                                        case 15:
                                            sku = CommonUtils.skuConverter(value);;
                                            break;
                                        case 16:
                                            productTitle = value;
                                            break;
                                        case 17:
                                            quantity = String.valueOf(Double.valueOf(value).intValue());
                                            break;
                                        case 18:
                                            if (!StringUtils.isEmpty(value)) {
                                                listPrice = "RMB" + value;
                                            }
                                            break;
                                        case 19:
                                            if (!StringUtils.isEmpty(value)) {
                                                int index = value.indexOf(" ");
                                                if (index != -1) {
                                                    value = value.substring(0, index);
                                                    if (Integer.parseInt(value.replaceAll("-", "")) <= 20180130) {
                                                        orderNo = "";
                                                        break;
                                                    }
                                                }
                                            }
                                            paidDate = value;
                                            break;
                                        case 22:
                                            if (!StringUtils.isEmpty(value)) {
                                                if (includeOrders.contains(orderNo)) {
                                                    break;
                                                }
                                                System.out.println("ignore contains shippmentNo " + value + " for " +
                                                        "orderNo " + orderNo);
                                                orderNo = "";
                                            }
                                            break;
                                    }
                                }
                            }
                            if (!StringUtils.isEmpty(orderNo)) {
                                maBangOrder.setOrderNo(orderNo);
                                maBangOrder.setBuyerName(buyerName);
                                maBangOrder.setBuyerPhone(buyerPhone);
                                maBangOrder.setBuyerAddress1(buyerAddress1);
                                maBangOrder.setBuyerAddress2(buyerAddress2);
                                maBangOrder.setBuyerCity(buyerCity);
                                maBangOrder.setBuyerState(buyerState);
                                maBangOrder.setBuyerZip(buyerZip);
                                maBangOrder.setBuyerCountry(buyerCountry);
                                maBangOrder.setSku(sku);
                                maBangOrder.setProductTitle(productTitle);
                                maBangOrder.setQuantity(quantity);
                                maBangOrder.setPaidDate(paidDate);
                                maBangOrder.setListPrice(listPrice);
                                maBangOrders.add(maBangOrder);
                            }
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            }

        }
    }

    private void output(List<MaBangOrder> maBangOrders) throws Exception {
        if (maBangOrders.size() == 0) {
            return;
        }
        File output = new File(outputPath);
        try (Workbook workBook = ExcelUtils.getWorkbook(output); OutputStream out = new FileOutputStream(output)){

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

            row = sheet.createRow(0);
            cell0 = row.createCell(0);
            cell0.setCellValue("交易单号");
            cell1 = row.createCell(1);
            cell1.setCellValue("买家ID");
            cell2 = row.createCell(2);
            cell2.setCellValue("买家姓名");
            cell3 = row.createCell(3);
            cell3.setCellValue("买家电话");
            cell4 = row.createCell(4);
            cell4.setCellValue("买家邮箱");
            cell5 = row.createCell(5);
            cell5.setCellValue("买家地址1");
            cell6 = row.createCell(6);
            cell6.setCellValue("买家地址2");
            cell7 = row.createCell(7);
            cell7.setCellValue("买家城市");
            cell8 = row.createCell(8);
            cell8.setCellValue("买家地区");
            cell9 = row.createCell(9);
            cell9.setCellValue("买家邮编");
            cell10 = row.createCell(10);
            cell10.setCellValue("买家国家");
            cell11 = row.createCell(11);
            cell11.setCellValue("Sku");
            cell12 = row.createCell(12);
            cell12.setCellValue("商品名称");
            cell13 = row.createCell(13);
            cell13.setCellValue("购买数量");
            cell14 = row.createCell(14);
            cell14.setCellValue("卖出金额");
            cell15 = row.createCell(15);
            cell15.setCellValue("运费");
            cell16 = row.createCell(16);
            cell16.setCellValue("交易编号");
            cell17 = row.createCell(17);
            cell17.setCellValue("平台");
            cell18 = row.createCell(18);
            cell18.setCellValue("付款日期");
            cell19 = row.createCell(19);
            cell19.setCellValue("网站运输方式");
            cell20 = row.createCell(20);
            cell20.setCellValue("店铺");
            for (int j = 0; j < maBangOrders.size(); j++) {
                MaBangOrder maBangOrder = maBangOrders.get(j);
                row = sheet.createRow(j + 1);
                cell0 = row.createCell(0);
                cell0.setCellValue(maBangOrder.getOrderNo());
                cell1 = row.createCell(1);
                cell1.setCellValue(maBangOrder.getBuyerId());
                cell2 = row.createCell(2);
                cell2.setCellValue(maBangOrder.getBuyerName());
                cell3 = row.createCell(3);
                cell3.setCellValue(maBangOrder.getBuyerPhone());
                cell4 = row.createCell(4);
                cell4.setCellValue(maBangOrder.getBuyerEmail());
                cell5 = row.createCell(5);
                cell5.setCellValue(maBangOrder.getBuyerAddress1());
                cell6 = row.createCell(6);
                cell6.setCellValue(maBangOrder.getBuyerAddress2());
                cell7 = row.createCell(7);
                cell7.setCellValue(maBangOrder.getBuyerCity());
                cell8 = row.createCell(8);
                cell8.setCellValue(maBangOrder.getBuyerState());
                cell9 = row.createCell(9);
                cell9.setCellValue(maBangOrder.getBuyerZip());
                cell10 = row.createCell(10);
                cell10.setCellValue(maBangOrder.getBuyerCountry());
                cell11 = row.createCell(11);
                cell11.setCellValue(maBangOrder.getSku());
                cell12 = row.createCell(12);
                cell12.setCellValue(maBangOrder.getProductTitle());
                cell13 = row.createCell(13);
                cell13.setCellValue(maBangOrder.getQuantity());
                cell14 = row.createCell(14);
                cell14.setCellValue(maBangOrder.getListPrice());
                cell15 = row.createCell(15);
                cell15.setCellValue(maBangOrder.getFreight());
                cell16 = row.createCell(16);
                cell16.setCellValue(maBangOrder.getTransactionNo());
                cell17 = row.createCell(17);
                cell17.setCellValue(maBangOrder.getPlatform());
                cell18 = row.createCell(18);
                cell18.setCellValue(maBangOrder.getPaidDate());
                cell19 = row.createCell(19);
                cell19.setCellValue(maBangOrder.getShippingChannel());
                cell20 = row.createCell(20);
                cell20.setCellValue(maBangOrder.getStore());
            }
            workBook.write(out);
        } catch (Exception e) {
            throw e;
        }
    }


    public static void main(String[] args) throws Exception {
        MbToMbOrderService service = new MbToMbOrderService();
        service.load();
    }
}
