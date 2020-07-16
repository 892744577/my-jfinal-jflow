package com.kakarote.crm9.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class CreateExcel {

    //创建有数据的工作表，并返回表对象
    public static boolean CreateSheet(List<List<String>> sheetdata,OutputStream os) throws Exception {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        //sheetdata.add(0, titlerow);
        for(int row = 0;row < sheetdata.size();row++) {
            Row rows = sheet.createRow(row);
            for(int col = 0;col < sheetdata.get(row).size();col++) {
                Object temp = sheetdata.get(row).get(col);
                String value = temp!=null?temp.toString():"";
                rows.createCell(col).setCellValue(value);
            }
        }
        workbook.write(os);
        os.flush();// 刷新此输出流并强制将所有缓冲的输出字节写出
        return true;
    }
}
