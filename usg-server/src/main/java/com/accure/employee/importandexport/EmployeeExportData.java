/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.employee.importandexport;

/**
 *
 * @author user
 */
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

public class EmployeeExportData {

    public String exceltoData(String data, String path) throws FileNotFoundException, IOException, Exception {
        String fileName;
        
       
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Calendar cal = Calendar.getInstance();
        String time = dateFormat.format(cal.getTime());
        fileName = path+ File.separator+"EmployeeDetails_" + time + ".xls";
        
        String[] headerData = {"SNO", "employeeCodeM", "EMPLOYEE NAME", "LOCATION", "DEPARTMENT", "SALARYTYPE", "DESIGNATION"};
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
            int sno = (Integer) obj.get("sno");
            String employeeCode = (String) obj.get("employeeCodeM");
            String employeeName = (String) obj.get("employeeName");
            String location = (String) obj.get("location");
            String department = (String) obj.getString("department");
            String designation = (String) obj.getString("designation");
            String salaryType = (String) obj.getString("salaryType");

            cell1 = row.createCell(0);
            cell1.setCellValue(sno);
            sheet.autoSizeColumn(0);
            cell1 = row.createCell(1);
            cell1.setCellValue(employeeCode);
            sheet.autoSizeColumn(1);
            cell1 = row.createCell(2);
            cell1.setCellValue(employeeName);
            cell1 = row.createCell(3);
            sheet.autoSizeColumn(3);
            cell1.setCellValue(location);
            cell1 = row.createCell(4);
            sheet.autoSizeColumn(4);
            cell1.setCellValue(department);
            cell1 = row.createCell(5);
            sheet.autoSizeColumn(5);
            cell1.setCellValue(salaryType);
            cell1 = row.createCell(6);
            sheet.autoSizeColumn(6);
            cell1.setCellValue(designation);

        }

        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
        return fileName;

    }

    public static void main(String[] args) throws IOException, Exception {
        // String data = "[{\"sno\":1,\"employeeCodeM\":\"DDO\",\"employeeName\":\"DDO\",\"location\":\"Location 1 \",\"department\":\"point\",\"salaryType\":\"TEST\",\"designation\":\"Designation\"},{\"sno\":2,\"employeeCodeM\":\"A\",\"employeeName\":\"A\",\"location\":\"Location 2\",\"department\":\"point\",\"salaryType\":\"TEST\",\"designation\":\"Designation\"}]";
        //exceltoData(data,null);
    }
}
