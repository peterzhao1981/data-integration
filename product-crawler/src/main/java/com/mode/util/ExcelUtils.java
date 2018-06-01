package com.mode.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by zhaoweiwei on 2018/1/17.
 * 
 * @author maxiadong Add method [get all excel name under file path]on
 *         2018/05/07
 */
public class ExcelUtils {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static Workbook getWorkbook(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if (file.getName().endsWith(EXCEL_XLS)) { // Excel 2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) { // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /*
     * 遍历得到filepath路径下的所有excel文件，create by maxiaodong on 2018/05/07
     */
    public static List<String> getAllExcelFileName(String filePath) throws IOException {
        List<String> excelNameList = new ArrayList<>();
        File dir = new File(filePath);
        File[] fileArr = dir.listFiles();
        for (File file : fileArr) {
            String fileName = file.getName();
            if (fileName.endsWith(EXCEL_XLS) || fileName.endsWith(EXCEL_XLSX)) {
                excelNameList.add(fileName);
            }
        }
        return excelNameList;
    }

    /*
     * 根据名称获取sheet表中的列index,create by maxiaodong on 2018/05/07
     */
    public static int getColumNumberByName(Sheet sheet, String[] columnName) {
        int index = -1;
        Row firstRow = sheet.getRow(0);// 只读表头
        Iterator<Cell> cellIterator = firstRow.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            for (String name : columnName) {
                if (name.equals(cell.getStringCellValue()))
                    return cell.getColumnIndex();
            }
        }
        return index;// 说明不存在该列，返回-1
    }
}
