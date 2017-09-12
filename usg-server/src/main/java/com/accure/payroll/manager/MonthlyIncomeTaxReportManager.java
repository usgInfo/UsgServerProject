/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.FinancialYear;
import com.accure.payroll.dto.IncomeTax;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.getMonthString;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class MonthlyIncomeTaxReportManager {

    public ByteArrayOutputStream getSearchResult(String EmpAttendanceJsonString, String month, String year, String reporttype, String path,String fin) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<IncomeTax> salarySlipRegisterList = null;

        RestClient aql = new RestClient();

        String incometaxTable = ApplicationConstants.USG_DB1 + ApplicationConstants.INCOMETAX_TABLE + "`";

        IncomeTax incomeTaxObj = new Gson().fromJson(EmpAttendanceJsonString, new TypeToken<IncomeTax>() {
        }.getType());
        String autoSalaryQuery = "";
        if (incomeTaxObj.getEmployeeCode() != null && incomeTaxObj.getEmployeeCode() != "" && !incomeTaxObj.getEmployeeCode().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.employeeCode=\"" + incomeTaxObj.getEmployeeCode() + "\"";
        }
        if (incomeTaxObj.getEmployeeName() != null && incomeTaxObj.getEmployeeName() != "" && !incomeTaxObj.getEmployeeName().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.employeeName=\"" + incomeTaxObj.getEmployeeName() + "\"";
        }
        if (incomeTaxObj.getEmployeeCodeM() != null && incomeTaxObj.getEmployeeCodeM() != "" && !incomeTaxObj.getEmployeeCodeM().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.employeeCodeM=\"" + incomeTaxObj.getEmployeeCodeM() + "\"";
        }
        if (incomeTaxObj.getLocation() != null && !incomeTaxObj.getLocation().isEmpty() && !incomeTaxObj.getLocation().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.location=\"" + incomeTaxObj.getLocation() + "\"";
        }
        if (incomeTaxObj.getDepartment() != null && !incomeTaxObj.getDepartment().isEmpty() && !incomeTaxObj.getDepartment().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.department=\"" + incomeTaxObj.getDepartment() + "\"";
        }
        if (incomeTaxObj.getDesignation() != null && !incomeTaxObj.getDesignation().isEmpty() && !incomeTaxObj.getDesignation().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.designation=\"" + incomeTaxObj.getDesignation() + "\"";
        }
        if (incomeTaxObj.getNatureType() != null && incomeTaxObj.getNatureType().isEmpty() && !incomeTaxObj.getNatureType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.natureType=\"" + incomeTaxObj.getNatureType() + "\"";
        }
        if (incomeTaxObj.getPostingCity() != null && incomeTaxObj.getPostingCity().isEmpty() && !incomeTaxObj.getPostingCity().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.postingCity=\"" + incomeTaxObj.getPostingCity() + "\"";
        }
        if (incomeTaxObj.getPfType() != null && incomeTaxObj.getPfType().isEmpty() && !incomeTaxObj.getPfType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.pfType=\"" + incomeTaxObj.getPfType() + "\"";
        }
        if (incomeTaxObj.getFundType() != null && incomeTaxObj.getFundType().isEmpty() && !incomeTaxObj.getFundType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.fundType=\"" + incomeTaxObj.getFundType() + "\"";
        }
        if (incomeTaxObj.getBudgetHead() != null && incomeTaxObj.getBudgetHead().isEmpty() && !incomeTaxObj.getBudgetHead().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and incomeTax.budgetHead=\"" + incomeTaxObj.getBudgetHead() + "\"";
        }

        String autoSalaryProcessQuery = "select incomeTax.employeeName,incomeTax.it,incomeTax.panNo,incomeTax.educationcess,incomeTax.total from " + incometaxTable + ""
                + " as incomeTax where incomeTax.month=" + month + " and incomeTax.year=" + year + " and ddo=\"" + incomeTaxObj.getDdo() + "\"" + autoSalaryQuery;

        //System.out.println("" + autoSalaryProcessQuery);

        String incomeTaxOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
        //System.out.println("ouput" + incomeTaxOutput);

        if (incomeTaxOutput != null && !incomeTaxOutput.isEmpty() && !incomeTaxOutput.equals("[]")) {
            salarySlipRegisterList = new Gson().fromJson(incomeTaxOutput, new TypeToken< ArrayList<IncomeTax>>() {
            }.getType());

        }
        if (reporttype.equalsIgnoreCase("MonthlyITRegister")) {
            bos = generateIncomeTaxReport(salarySlipRegisterList, month, year, path,fin);
        }

        return bos;
    }

    public ByteArrayOutputStream generateIncomeTaxReport(List<IncomeTax> incomeTaxList, String month, String year, String path,String fin) throws DocumentException, FileNotFoundException, BadElementException, IOException, Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        double totalITAmount = 0.00;
        double cessAmount = 0.00;
        double TtotalAmount = 0.00;
        Document document = new Document();
        PdfWriter.getInstance(document, bos);
        document.open();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100.0f);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100.0f);
        header.setWidths(new int[]{60, 150, 100});

        PdfPCell imagecell = new PdfPCell();
//        //System.out.println("path ------------- "+path);

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(70.0f, 70.0f);

        imagecell.addElement(image1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorder(Rectangle.NO_BORDER);
        imagecell.setBorderColor(BaseColor.WHITE);

        String monthString = getMonthString(Integer.parseInt(month));
        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("  IT Register For  " + monthString + "-" + year, FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase("\n"));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);
        headercell.setBorder(Rectangle.NO_BORDER);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        DateFormat dateFormatTime = new SimpleDateFormat("hh:mm:ss");
        String financialYearJson = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
        List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
        }.getType());
        FinancialYear fyObj = fyList.get(0);
        String fyId = fyObj.getYear();
        String strmin = fyId.substring(2, 4);
        int instrmin = Integer.parseInt(strmin);
        instrmin = instrmin + 1;
        strmin = Integer.toString(instrmin);

        Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(fin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));

        PdfPCell timecell = new PdfPCell(timePhrase);
        timecell.setPaddingTop(2.0f);
        timecell.setBorder(Rectangle.NO_BORDER);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);

        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        header.setSpacingAfter(10);

        outercell.addElement(header);

        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100.0f);
        PdfPCell linecell = new PdfPCell();
        linecell.setBorder(Rectangle.NO_BORDER);
        linecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        linecell.setBorderColor(BaseColor.WHITE);
        linecell.setBorderColorBottom(BaseColor.BLACK);
        line.setSpacingBefore(5f);
        line.setSpacingAfter(5f);
        linecell.setBorderWidthBottom(1f);
        line.addCell(linecell);

        outercell.addElement(line);

//        PdfPTable incomeHeadingline = new PdfPTable(1);
//        incomeHeadingline.setWidthPercentage(100.0f);
//        PdfPCell incomeHeadingcell = new PdfPCell(new Phrase(new Chunk("Income Tax Deduction For The Month Of " + monthString + "  " + year, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
//        incomeHeadingcell.setBorder(Rectangle.NO_BORDER);
//        incomeHeadingcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        incomeHeadingcell.setBorderColor(BaseColor.WHITE);
//        incomeHeadingcell.setBorderColorBottom(BaseColor.BLACK);
//
//        incomeHeadingline.addCell(incomeHeadingcell);
        //    outercell.addElement(incomeHeadingline);
        // outercell.addElement(line);
        PdfPTable incomeHeadingNameline = new PdfPTable(6);
        incomeHeadingNameline.setWidthPercentage(100.0f);

        PdfPCell snoCell = new PdfPCell(new Phrase(new Chunk("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        snoCell.setBorder(Rectangle.NO_BORDER);
        snoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        snoCell.setBorderColor(BaseColor.WHITE);
        snoCell.setBorderColorBottom(BaseColor.BLACK);

        PdfPCell namecell = new PdfPCell(new Phrase(new Chunk("Employee Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        namecell.setBorder(Rectangle.NO_BORDER);
        namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        namecell.setBorderColor(BaseColor.WHITE);
        namecell.setBorderColorBottom(BaseColor.BLACK);

        PdfPCell panNocell = new PdfPCell(new Phrase(new Chunk("PAN NO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        panNocell.setBorder(Rectangle.NO_BORDER);
        panNocell.setHorizontalAlignment(Element.ALIGN_CENTER);
        panNocell.setBorderColor(BaseColor.WHITE);
        panNocell.setBorderColorBottom(BaseColor.BLACK);

        PdfPCell itAmountcell = new PdfPCell(new Phrase(new Chunk("IT", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        itAmountcell.setBorder(Rectangle.NO_BORDER);
        itAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        itAmountcell.setBorderColor(BaseColor.WHITE);
        itAmountcell.setBorderColorBottom(BaseColor.BLACK);

        PdfPCell cessAmountcell = new PdfPCell(new Phrase(new Chunk("Cess", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        cessAmountcell.setBorder(Rectangle.NO_BORDER);
        cessAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cessAmountcell.setBorderColor(BaseColor.WHITE);
        cessAmountcell.setBorderColorBottom(BaseColor.BLACK);

        PdfPCell totalAmountcell = new PdfPCell(new Phrase(new Chunk("Amount", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        totalAmountcell.setBorder(Rectangle.NO_BORDER);
        totalAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalAmountcell.setBorderColor(BaseColor.WHITE);
        totalAmountcell.setBorderColorBottom(BaseColor.BLACK);

        incomeHeadingNameline.addCell(snoCell);
        incomeHeadingNameline.addCell(namecell);
        incomeHeadingNameline.addCell(panNocell);
        incomeHeadingNameline.addCell(itAmountcell);
        incomeHeadingNameline.addCell(cessAmountcell);
        incomeHeadingNameline.addCell(totalAmountcell);

        outercell.addElement(incomeHeadingNameline);
        outercell.addElement(line);

//        PdfPTable BudgetHeadline = new PdfPTable(1);
//        BudgetHeadline.setWidthPercentage(100.0f);
//        PdfPCell BudgetHeadcell = new PdfPCell(new Phrase(new Chunk("Budget Head : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
//        BudgetHeadcell.addElement(new PdfPCell(new Phrase(new Chunk(BudgetName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)))));
//        BudgetHeadcell.setBorder(Rectangle.NO_BORDER);
//        BudgetHeadcell.setHorizontalAlignment(Element.ALIGN_LEFT);
//        BudgetHeadcell.setBorderColor(BaseColor.WHITE);
//        BudgetHeadcell.setBorderColorBottom(BaseColor.BLACK);
//
//        BudgetHeadline.addCell(BudgetHeadcell);
//
//        outercell.addElement(BudgetHeadline);
//        outercell.addElement(line);
        int count = 1;
        if (incomeTaxList != null && incomeTaxList.size() > 0) {

            for (IncomeTax income : incomeTaxList) {
                incomeHeadingNameline = new PdfPTable(6);
                incomeHeadingNameline.setWidthPercentage(100.0f);

                snoCell = new PdfPCell(new Phrase(new Chunk(Integer.toString(count++), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
                snoCell.setBorder(Rectangle.NO_BORDER);
                snoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                snoCell.setBorderColor(BaseColor.WHITE);
                snoCell.setBorderColorBottom(BaseColor.BLACK);

                namecell = new PdfPCell(new Phrase(new Chunk(income.getEmployeeName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
                namecell.setBorder(Rectangle.NO_BORDER);
                namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
                namecell.setBorderColor(BaseColor.WHITE);
                namecell.setBorderColorBottom(BaseColor.BLACK);
                String panno = "";
                if (income.getPanNo() != null && income.getPanNo() != "" && !income.getPanNo().isEmpty()) {
                    panno = income.getPanNo();
                }
                panNocell = new PdfPCell(new Phrase(new Chunk(panno, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
                panNocell.setBorder(Rectangle.NO_BORDER);
                panNocell.setHorizontalAlignment(Element.ALIGN_CENTER);
                panNocell.setBorderColor(BaseColor.WHITE);
                panNocell.setBorderColorBottom(BaseColor.BLACK);
                double amount = 0.00;
                if (income.getIt() != 0) {
                    amount = income.getIt();
                    totalITAmount = totalITAmount + amount;
                }

                itAmountcell = new PdfPCell(new Phrase(new Chunk(String.format("%.2f", amount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
                itAmountcell.setBorder(Rectangle.NO_BORDER);
                itAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                itAmountcell.setBorderColor(BaseColor.WHITE);
                itAmountcell.setBorderColorBottom(BaseColor.BLACK);

                double cesamount = 0.00;
                if (income.getEducationcess() != 0) {
                    cesamount = income.getEducationcess();
                    cessAmount = cessAmount + cesamount;
                }
                cessAmountcell = new PdfPCell(new Phrase(new Chunk(String.format("%.2f", cesamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
                cessAmountcell.setBorder(Rectangle.NO_BORDER);
                cessAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cessAmountcell.setBorderColor(BaseColor.WHITE);
                cessAmountcell.setBorderColorBottom(BaseColor.BLACK);

                double TTamount = 0.00;
                if (income.getTotal() != 0) {
                    TTamount = income.getTotal();
                    TtotalAmount = TtotalAmount + TTamount;
                }
                totalAmountcell = new PdfPCell(new Phrase(new Chunk(String.format("%.2f", TTamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
                totalAmountcell.setBorder(Rectangle.NO_BORDER);
                totalAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                totalAmountcell.setBorderColor(BaseColor.WHITE);
                totalAmountcell.setBorderColorBottom(BaseColor.BLACK);

                incomeHeadingNameline.addCell(snoCell);
                incomeHeadingNameline.addCell(namecell);
                incomeHeadingNameline.addCell(panNocell);
                incomeHeadingNameline.addCell(itAmountcell);
                incomeHeadingNameline.addCell(cessAmountcell);
                incomeHeadingNameline.addCell(totalAmountcell);

                outercell.addElement(incomeHeadingNameline);
                outercell.addElement(line);
            }

            incomeHeadingNameline = new PdfPTable(6);
            incomeHeadingNameline.setWidthPercentage(100.0f);

            snoCell = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            snoCell.setBorder(Rectangle.NO_BORDER);
            snoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            snoCell.setBorderColor(BaseColor.WHITE);
            snoCell.setBorderColorBottom(BaseColor.BLACK);

            namecell = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            namecell.setBorder(Rectangle.NO_BORDER);
            namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            namecell.setBorderColor(BaseColor.WHITE);
            namecell.setBorderColorBottom(BaseColor.BLACK);

            panNocell = new PdfPCell(new Phrase(new Chunk("Grand Total", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            panNocell.setBorder(Rectangle.NO_BORDER);
            panNocell.setHorizontalAlignment(Element.ALIGN_CENTER);
            panNocell.setBorderColor(BaseColor.WHITE);
            panNocell.setBorderColorBottom(BaseColor.BLACK);

            itAmountcell = new PdfPCell(new Phrase(new Chunk(String.format("%.2f", totalITAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            itAmountcell.setBorder(Rectangle.NO_BORDER);
            itAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            itAmountcell.setBorderColor(BaseColor.WHITE);
            itAmountcell.setBorderColorBottom(BaseColor.BLACK);

            cessAmountcell = new PdfPCell(new Phrase(new Chunk(String.format("%.2f", cessAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            cessAmountcell.setBorder(Rectangle.NO_BORDER);
            cessAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cessAmountcell.setBorderColor(BaseColor.WHITE);
            cessAmountcell.setBorderColorBottom(BaseColor.BLACK);

            totalAmountcell = new PdfPCell(new Phrase(new Chunk(String.format("%.2f", TtotalAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            totalAmountcell.setBorder(Rectangle.NO_BORDER);
            totalAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalAmountcell.setBorderColor(BaseColor.WHITE);
            totalAmountcell.setBorderColorBottom(BaseColor.BLACK);

            incomeHeadingNameline.addCell(snoCell);
            incomeHeadingNameline.addCell(namecell);
            incomeHeadingNameline.addCell(panNocell);
            incomeHeadingNameline.addCell(itAmountcell);
            incomeHeadingNameline.addCell(cessAmountcell);
            incomeHeadingNameline.addCell(totalAmountcell);

            outercell.addElement(incomeHeadingNameline);
            outercell.addElement(line);

        }

        RoundRectangle roundRectangle = new RoundRectangle();
        outercell.setCellEvent(roundRectangle);
        outercell.setBorder(Rectangle.NO_BORDER);
        outercell.setBorderWidth(2);
        outercell.setPadding(8);

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
