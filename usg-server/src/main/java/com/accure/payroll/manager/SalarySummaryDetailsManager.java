/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.FinancialYear;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.SalarySlipRegisterReport;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.getMonthString;
import com.accure.pdf.common.CustomDashedLineSeparators;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class SalarySummaryDetailsManager {

    public ByteArrayOutputStream getSearchResult(String EmpAttendanceJsonString, String month, String year, String ddo, String path, String fin) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int count = 0;
        double totalEarnings = 0.0;
        double totalDeductions = 0.0;
        double totalnetAmount = 0.0;

        CustomDashedLineSeparators separator = new CustomDashedLineSeparators();
        separator.setDash(7);
        separator.setGap(1);
        separator.setLineWidth(1);
        separator.setPhase(1.0f);
        separator.setAlignment(Element.ALIGN_TOP);
        separator.setOffset(-2);
        Chunk linebreak = new Chunk(separator);
        Rectangle pageSize = new Rectangle(800f, 800f);
        Document document = new Document(pageSize);
        PdfWriter.getInstance(document, bos);
        document.open();

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{30, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(100.0f, 100.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColor(BaseColor.WHITE);

        String monthString = getMonthString(Integer.parseInt(month));

        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360 005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("      Salary Summary Details For " + monthString + "-" + year, FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK))));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);
//financial year
        String financialYearJson = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
        List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
        }.getType());
        FinancialYear fyObj = fyList.get(0);
        String fyId = fyObj.getYear();
        String strmin = fyId.substring(2, 4);
        int instrmin = Integer.parseInt(strmin);
        instrmin = instrmin + 1;
        strmin = Integer.toString(instrmin);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        DateFormat dateFormatTime = new SimpleDateFormat("HH : mm : ss");

        Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(fin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));

        PdfPCell timecell = new PdfPCell(timePhrase);
        timecell.setBorderWidthBottom(1);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);

        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);

        cell.addElement(header);
        // cell.addElement(linebreak);

        PdfPTable bodyTable = new PdfPTable(5);
        bodyTable.setWidthPercentage(100);
        bodyTable.setWidths(new int[]{1, 2, 2, 2, 2});

        PdfPCell snoCell = new PdfPCell(new Phrase("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        snoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        snoCell.setPaddingBottom(5);
        snoCell.setPaddingTop(5);

        PdfPCell DDOCell = new PdfPCell(new Phrase("DDO Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        DDOCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        DDOCell.setPaddingBottom(5);
        DDOCell.setPaddingTop(5);

        PdfPCell earningsCell = new PdfPCell(new Phrase("Earnings", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        earningsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        earningsCell.setPaddingBottom(5);
        earningsCell.setPaddingTop(5);

        PdfPCell deductionsCell = new PdfPCell(new Phrase("Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        deductionsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        deductionsCell.setPaddingBottom(5);
        deductionsCell.setPaddingTop(5);

        PdfPCell netCell = new PdfPCell(new Phrase("Net Pay", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        netCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        netCell.setPaddingBottom(5);
        netCell.setPaddingTop(5);

        bodyTable.addCell(snoCell);
        bodyTable.addCell(DDOCell);
        bodyTable.addCell(earningsCell);
        bodyTable.addCell(deductionsCell);
        bodyTable.addCell(netCell);

        cell.addElement(bodyTable);
        //cell.addElement(linebreak);

        RestClient aql = new RestClient();

        String autoSalaryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";

        AutoSalaryProcess autosalaryprocessObj = new Gson().fromJson(EmpAttendanceJsonString, new TypeToken<AutoSalaryProcess>() {
        }.getType());

        String autoSalaryQuery = "";

        String[] ddoArray = ddo.split(",");

        for (int i = 0; i < ddoArray.length; i++) {
            String ddoStr = "";

            if (ddoArray[i].equalsIgnoreCase("All")) {
                ddoStr = "";
            } else {
                ddoStr = ddoStr + " and autosalary.ddo=\"" + ddoArray[i] + "\"";
            }
            //System.out.println("ddoArray[i]" + ddoStr);
            //System.out.println("autosalaryprocessObj.getEmployeeCode()" + autosalaryprocessObj.getEmployeeCode());
            if (autosalaryprocessObj.getEmployeeCode() != null && autosalaryprocessObj.getEmployeeCode() != "" && !autosalaryprocessObj.getEmployeeCode().isEmpty()) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.employeeCode=\"" + autosalaryprocessObj.getEmployeeCode() + "\"";
            }
            if (autosalaryprocessObj.getEmployeeName() != null && autosalaryprocessObj.getEmployeeName() != "" && !autosalaryprocessObj.getEmployeeName().isEmpty()) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.employeeName=\"" + autosalaryprocessObj.getEmployeeName() + "\"";
            }
            if (autosalaryprocessObj.getEmployeeCodeM() != null && autosalaryprocessObj.getEmployeeCodeM() != "" && !autosalaryprocessObj.getEmployeeCodeM().isEmpty()) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.employeeCodeM=\"" + autosalaryprocessObj.getEmployeeCodeM() + "\"";
            }
            if (autosalaryprocessObj.getLocation() != null && !autosalaryprocessObj.getLocation().isEmpty() && !autosalaryprocessObj.getLocation().equals("0")) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.location=\"" + autosalaryprocessObj.getLocation() + "\"";
            }
            if (autosalaryprocessObj.getDepartment() != null && !autosalaryprocessObj.getDepartment().isEmpty() && !autosalaryprocessObj.getDepartment().equals("0")) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.department=\"" + autosalaryprocessObj.getDepartment() + "\"";
            }
            if (autosalaryprocessObj.getDesignation() != null && !autosalaryprocessObj.getDesignation().isEmpty() && !autosalaryprocessObj.getDesignation().equals("0")) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.designation=\"" + autosalaryprocessObj.getDesignation() + "\"";
            }
            if (autosalaryprocessObj.getNatureType() != null && !autosalaryprocessObj.getNatureType().isEmpty() && !autosalaryprocessObj.getNatureType().equals("0")) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.natureType=\"" + autosalaryprocessObj.getNatureType() + "\"";
            }
            if (autosalaryprocessObj.getPostingCity() != null && !autosalaryprocessObj.getPostingCity().isEmpty() && !autosalaryprocessObj.getPostingCity().equals("0")) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.postingCity=\"" + autosalaryprocessObj.getPostingCity() + "\"";
            }
            if (autosalaryprocessObj.getPfType() != null && !autosalaryprocessObj.getPfType().isEmpty() && !autosalaryprocessObj.getPfType().equals("0")) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.pfType=\"" + autosalaryprocessObj.getPfType() + "\"";
            }
            if (autosalaryprocessObj.getFundType() != null && !autosalaryprocessObj.getFundType().isEmpty() && !autosalaryprocessObj.getFundType().equals("0")) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.fundType=\"" + autosalaryprocessObj.getFundType() + "\"";
            }
            if (autosalaryprocessObj.getBudgetHead() != null && !autosalaryprocessObj.getBudgetHead().isEmpty() && !autosalaryprocessObj.getBudgetHead().equals("0")) {
                autoSalaryQuery = autoSalaryQuery + " and autosalary.budgetHead=\"" + autosalaryprocessObj.getBudgetHead() + "\"";
            }

//            String autoSalaryProcessQuery = "select autosalary.deductions,autosalary.earnings, autosalary.ddoName from " + autoSalaryTable + ""
//                    + " as autosalary where autosalary.month= " + month + " and autosalary.year=" + year + " " + autoSalaryQuery + "" + ddoStr;
            String autoSalaryProcessQuery = "select SUM(autosalary.earnings),SUM(autosalary.deductions),autosalary.ddoName from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.month= " + month + " and autosalary.year=" + year + " " + autoSalaryQuery + "" + ddoStr + " GROUP BY autosalary.ddoName";

            //System.out.println("" + autoSalaryProcessQuery);

            String autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
            if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
                JSONArray arraySL = new JSONArray(autosalaryOutput);
                if (arraySL.length() > 0) {
                    for (int outputcount = 0; outputcount < arraySL.length(); outputcount++) {
                        JSONObject slObj = arraySL.getJSONObject(outputcount);
                        Double Earnings = (Double) slObj.get("0");
                        Double Deduction = (Double) slObj.get("1");
                        String ddoName = (String) slObj.get("ddoName");

                        bodyTable = new PdfPTable(5);
                        bodyTable.setWidthPercentage(100);
                        bodyTable.setWidths(new int[]{1, 2, 2, 2, 2});

                        count++;
                        PdfPCell snoValueCell = new PdfPCell(new Phrase(Integer.toString(count), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                        snoValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        snoValueCell.setPaddingBottom(5);
                        snoValueCell.setPaddingTop(5);

                        PdfPCell DDOValueCell = new PdfPCell(new Phrase(ddoName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                        DDOValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        DDOValueCell.setPaddingBottom(5);
                        DDOValueCell.setPaddingTop(5);
                        totalEarnings = totalEarnings + Earnings;

                        Earnings = Math.round(Earnings * 100.00) / 100.00;
                        //  SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Earnings);
                        // String earningStr = Double.toString(Earnings);

                        PdfPCell earningsValueCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Earnings), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                        earningsValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        earningsValueCell.setPaddingBottom(5);
                        earningsValueCell.setPaddingTop(5);

                        totalDeductions = totalDeductions + Deduction;
                        Deduction = Math.round(Deduction * 100.00) / 100.00;
                        // String deductionsStr = Double.toString(Deduction);

                        PdfPCell deductionsValueCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Deduction), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                        deductionsValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        deductionsValueCell.setPaddingBottom(5);
                        deductionsValueCell.setPaddingTop(5);
                        double netpay = Earnings - Deduction;
                        netpay = Math.round(netpay * 100.00) / 100.00;
                        totalnetAmount = totalnetAmount + netpay;
                        String netpayStr = Double.toString(netpay);

                        PdfPCell netValueCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(netpay), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                        netValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        netValueCell.setPaddingBottom(5);
                        netValueCell.setPaddingTop(5);
                        bodyTable.addCell(snoValueCell);
                        bodyTable.addCell(DDOValueCell);
                        bodyTable.addCell(earningsValueCell);
                        bodyTable.addCell(deductionsValueCell);
                        bodyTable.addCell(netValueCell);
                        cell.addElement(bodyTable);

                    }
                }
            }
        }
        //cell.addElement(linebreak);

        bodyTable = new PdfPTable(5);
        bodyTable.setWidthPercentage(100);
        bodyTable.setWidths(new int[]{1, 2, 2, 2, 2});

        PdfPCell snoValueCell = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
        snoValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        snoValueCell.setPaddingBottom(5);
        snoValueCell.setPaddingTop(5);

        PdfPCell DDOValueCell = new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        DDOValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        DDOValueCell.setPaddingBottom(5);
        DDOValueCell.setPaddingTop(5);

        totalEarnings = Math.round(totalEarnings * 100.00) / 100.00;

        PdfPCell earningsValueCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalEarnings), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        earningsValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        earningsValueCell.setPaddingBottom(5);
        earningsValueCell.setPaddingTop(5);

        totalDeductions = Math.round(totalDeductions * 100.00) / 100.00;
        PdfPCell deductionsValueCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalDeductions), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        deductionsValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        deductionsValueCell.setPaddingBottom(5);
        deductionsValueCell.setPaddingTop(5);
        totalnetAmount = Math.round(totalnetAmount * 100.00) / 100.00;
        PdfPCell netValueCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalnetAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        netValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        netValueCell.setPaddingBottom(5);
        netValueCell.setPaddingTop(5);
        bodyTable.addCell(snoValueCell);
        bodyTable.addCell(DDOValueCell);
        bodyTable.addCell(earningsValueCell);
        bodyTable.addCell(deductionsValueCell);
        bodyTable.addCell(netValueCell);
        cell.addElement(bodyTable);
        // cell.addElement(linebreak);

        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorderColor(BaseColor.WHITE);
        PdfPTable outerTable = new PdfPTable(1);

        outerTable.setWidthPercentage(100f);

        com.accure.payroll.manager.RoundRectangle roundRectangle = new com.accure.payroll.manager.RoundRectangle();
        cell.setCellEvent(roundRectangle);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorderWidth(2);
        cell.setPadding(8);

        table.addCell(cell);
        document.add(table);
        document.close();
        return bos;
    }
}
