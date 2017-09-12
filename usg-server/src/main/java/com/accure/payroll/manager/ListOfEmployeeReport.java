/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.FinancialYear;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.getMonthString;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class ListOfEmployeeReport {

    public ByteArrayOutputStream generateEmployeeReport(List<Employee> employeeList, String path,String fin) throws DocumentException, FileNotFoundException, BadElementException, IOException, Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        String[] headerNames = {"S.No.", "Employee Code", "Employee Name", "Department", "Location", "Designation", "SalaryType", "Gender"};

//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        Date date = new Date();
//        String dateStr = dateFormat.format(date);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
//        String currentTime = sdf.format(date);
        Rectangle pageSize = new Rectangle(700, 700);
        Document document = new Document(pageSize);
        PdfWriter.getInstance(document, bos);
        document.open();

        PdfPTable outerTable = new PdfPTable(1);
//
//        outerTable.setTotalWidth(555f);
//        outerTable.setLockedWidth(true);

        outerTable.setWidthPercentage(100);

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{30, 200, 50});
        header.setSpacingAfter(15);

        PdfPCell imagecell = new PdfPCell();
        PdfPCell outercell = new PdfPCell();
        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(70.0f, 70.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColor(BaseColor.WHITE);

        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk(" Employee(s) Details ", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        DateFormat dateFormatTime = new SimpleDateFormat("HH : mm : ss");

        String financialYearJson = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
        List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
        }.getType());
        FinancialYear fyObj = fyList.get(0);
        String fyId = fyObj.getYear();
        String strmin = fyId.substring(2, 4);
        int instrmin = Integer.parseInt(strmin);
        instrmin = instrmin + 1;
        strmin = Integer.toString(instrmin);

        Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(fin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));

        PdfPCell timecell = new PdfPCell(timePhrase);
        timecell.setBorderWidthBottom(1);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);
//financial year
//        String financialYearJson = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
//        List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
//        }.getType());
//        FinancialYear fyObj = fyList.get(0);
//        String fyId = fyObj.getYear();
//        String strmin = fyId.substring(2, 4);
//        int instrmin = Integer.parseInt(strmin);
//        instrmin = instrmin + 1;
//        strmin = Integer.toString(instrmin);
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = new Date();
//        DateFormat dateFormatTime = new SimpleDateFormat("HH : mm : ss");
//
//        Phrase timePhrase = new Phrase(new Chunk("Date :" + dateFormat.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
//        timePhrase.add(new Phrase(new Chunk("\n")));
//        timePhrase.add(new Phrase(new Chunk("Time:" + dateFormatTime.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
//        timePhrase.add(new Phrase(new Chunk("\n")));
//        timePhrase.add(new Phrase(new Chunk("\n")));
//        timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
//        timePhrase.add(new Phrase(new Chunk(fyId + "-" + strmin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
//
//        PdfPCell timecell = new PdfPCell(timePhrase);
//        timecell.setBorderWidthBottom(1);
//        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        timecell.setBorderColor(BaseColor.WHITE);
//
//        header.addCell(imagecell);
//        header.addCell(headercell);
//        header.addCell(timecell);
        RoundRectangle roundRectangle = new RoundRectangle();
        outercell = new PdfPCell();
        outercell.setCellEvent(roundRectangle);
        outercell.setBorder(Rectangle.NO_BORDER);
        outercell.setPadding(10);
        outercell.addElement(header);

        PdfPTable employeeDetailsTable = new PdfPTable(8);
        //  employeeDetailsTable.setSpacingBefore(10);

        employeeDetailsTable.setWidths(new float[]{2f, 3f, 3f, 3f, 3f, 3f, 3f, 2f});
        employeeDetailsTable.setWidthPercentage(100);
        PdfPCell employeecell = null;
        employeeDetailsTable.setHeaderRows(1);

        for (int i = 0; i < headerNames.length; i++) {
            String headerName = headerNames[i];
            employeecell = new PdfPCell(new Phrase(headerName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            employeecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeecell.setPaddingBottom(5);
            employeecell.setPaddingTop(5);
            employeeDetailsTable.addCell(employeecell);

        }
        int count = 0;
        String employeecode = "";
        String employeename = "";
        String location = "";
        String designation = "";
        String salarytype = "";
        String gender = "";
        String department = "";

        for (Employee employee : employeeList) {
            count++;
            employeecell = new PdfPCell(new Phrase(String.valueOf(count), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            employeecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeecell.setPaddingBottom(5);
            employeecell.setPaddingTop(5);
            employeecode = employee.getEmployeeCode();
            employeeDetailsTable.addCell(employeecell);

            employeecell = new PdfPCell(new Phrase(employeecode, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            employeecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeecell.setPaddingBottom(5);
            employeecell.setPaddingTop(5);
            employeeDetailsTable.addCell(employeecell);
            employeename = employee.getEmployeeName();
            employeecell = new PdfPCell(new Phrase(employeename, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            employeecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeecell.setPaddingBottom(5);
            employeecell.setPaddingTop(5);
            employeeDetailsTable.addCell(employeecell);
            department = employee.getDepartment();
            employeecell = new PdfPCell(new Phrase(department, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            employeecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeecell.setPaddingBottom(5);
            employeecell.setPaddingTop(5);
            employeeDetailsTable.addCell(employeecell);
            location = employee.getLocation();
            employeecell = new PdfPCell(new Phrase(location, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            employeecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeecell.setPaddingBottom(5);
            employeecell.setPaddingTop(5);
            employeeDetailsTable.addCell(employeecell);
            designation = employee.getDesignation();
            employeecell = new PdfPCell(new Phrase(designation, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            employeecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeecell.setPaddingBottom(5);
            employeecell.setPaddingTop(5);
            employeeDetailsTable.addCell(employeecell);
            salarytype = employee.getSalaryType();
            //System.out.println("salarytype" + salarytype);
            employeecell = new PdfPCell(new Phrase(salarytype, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            employeecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeecell.setPaddingBottom(5);
            employeecell.setPaddingTop(5);
            employeeDetailsTable.addCell(employeecell);
            gender = employee.getGender();
            employeecell = new PdfPCell(new Phrase(gender, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            employeecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeecell.setPaddingBottom(5);
            employeecell.setPaddingTop(5);
            employeeDetailsTable.addCell(employeecell);

            employeecode = "";
            employeename = "";
            location = "";
            designation = "";
            salarytype = "";
            gender = "";
            department = "";

        }
        outercell.addElement(employeeDetailsTable);
        outerTable.addCell(outercell);

        document.add(outerTable);
        document.close();

        return bos;
    }

    class RoundRectangle implements PdfPCellEvent {

        public void cellLayout(PdfPCell cell, Rectangle rect,
                PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                    rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                    rect.getHeight() - 3, 4);
            cb.stroke();
        }
    }

}
