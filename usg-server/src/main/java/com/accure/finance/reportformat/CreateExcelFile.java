/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.reportformat;

import com.accure.finance.dto.BankReconcilation;
import com.accure.finance.manager.BankReconcilationManager;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author User
 */
public class CreateExcelFile {
  
          public static String exceltoData(String data, String path) throws FileNotFoundException, IOException, Exception {
        String  fileName;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Calendar cal = Calendar.getInstance();
        String time = dateFormat.format(cal.getTime());
        fileName = path+ File.separator+"BankDetails_" + time + ".xls";
        
        String[] headerData = {"FromDate", "ToDate", "Location", "Ledger", "ReconcilationStatus"};
        Workbook workbook = null;
        
        if (fileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new Exception("invalid file name, should be xls or xlsx");
        }

        Sheet sheet = workbook.createSheet("EmployeeList");
        HSSFCellStyle style = (HSSFCellStyle) workbook.createCellStyle();
        HSSFFont font = (HSSFFont) workbook.createFont();

        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFillBackgroundColor(IndexedColors.RED.getIndex());

        sheet.setColumnWidth(2, 7500);
        style.setFont(font);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headerData.length; i++) {
            Cell cell = headerRow.createCell(i);
            sheet.autoSizeColumn(i);
            cell.setCellValue(headerData[i]);
            cell.setCellStyle(style);
        }
        JSONArray arrayJson = new JSONArray(data);
        CDL.toString(arrayJson);

        int dataRow = 1;

        for (int i = 0; i < arrayJson.length(); i++) {
            Cell cell1;
            Row row = sheet.createRow(dataRow++);
            JSONObject obj = arrayJson.getJSONObject(i);
            cell1 = row.createCell(0);
            cell1.setCellValue((String) obj.get("fromDate"));
            sheet.autoSizeColumn(0);
            cell1 = row.createCell(1);
            cell1.setCellValue((String) obj.get("toDate"));
            sheet.autoSizeColumn(1);
             cell1 = row.createCell(2);
            cell1.setCellValue((String) obj.get("locationName"));
           
            sheet.autoSizeColumn(2);
              cell1 = row.createCell(3);
            cell1.setCellValue((String) obj.getString("ledgerName"));
          
            sheet.autoSizeColumn(3);
              cell1 = row.createCell(4);
            cell1.setCellValue((String) obj.get("reconcilationStatus"));
            sheet.autoSizeColumn(4);

        }

        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
        return fileName;

    }
}