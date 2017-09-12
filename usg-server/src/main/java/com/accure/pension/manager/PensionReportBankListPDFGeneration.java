/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.FinancialYear;
import com.accure.leave.dto.LeaveEncashment;
import com.accure.pension.manager.PensionBankManager;
import com.accure.payroll.dto.ArrearProcess;
import com.accure.payroll.dto.Deductions;
import com.accure.payroll.dto.Earnings;
import com.accure.payroll.dto.IncomeTax;
import com.accure.payroll.dto.InsuranceTransactions;
import com.accure.payroll.dto.LoanPayment;
import com.accure.payroll.dto.SalarySlipRegisterReport;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.getMonthString;
import com.accure.pension.dto.PensionEmployee;
import com.accure.usg.common.manager.ConvertMoneyToNumberMain;
import com.accure.usg.common.manager.DBManager;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.File;
import com.itextpdf.text.Image;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONObject;

/**
 *
 * @author ankur
 */
public class PensionReportBankListPDFGeneration {

    final static LinkedHashMap<String, Double> Cumalativedeductions = new LinkedHashMap<String, Double>();
    final static LinkedHashMap<String, Double> CumalativeEarnings = new LinkedHashMap<String, Double>();
    final static double totalCumulativeEarnings = 0.00;
    final static double totalCumulativeDeductions = 0.00;
    final static String months[] = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public ByteArrayOutputStream generatePensionBankListReport(List<PensionEmployee> PensionList, String path, String month, String year, String bName) throws DocumentException, FileNotFoundException, BadElementException, IOException, Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        month = months[Integer.parseInt(month)];
        Document document = new Document();
        PdfWriter.getInstance(document, bos);
        document.open();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();
        outerTable.setWidthPercentage(100);

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{40, 200, 50});

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(70.0f, 70.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderWidthBottom(0.5f);
        imagecell.setBorderColorBottom(BaseColor.BLACK);
        imagecell.setPaddingBottom(10);

        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("    BANK TRANSFER LIST OF PENSIONER'S ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("    MONTH of " + month + ",  " + year, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");

        Phrase timePhrase = new Phrase(new Chunk("Date : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("\n")));
        //timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        // timePhrase.add(new Phrase(new Chunk(fin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setPaddingLeft(10.0f);
        headercell.setBorderColor(BaseColor.WHITE);
        headercell.setBorderWidthBottom(0.5f);
        headercell.setBorderColorBottom(BaseColor.BLACK);
        headercell.setPaddingBottom(10);

        PdfPCell timecell = new PdfPCell(timePhrase);
        timecell.setBorderWidthBottom(1);
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //timecell.setBorderColor(BaseColor.BLACK);
        timecell.setBorderColor(BaseColor.WHITE);
        timecell.setBorderWidthBottom(0.5f);
        timecell.setBorderColorBottom(BaseColor.BLACK);
        timecell.setPaddingBottom(10);

        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        PdfPTable table1 = new PdfPTable(4);
        table1.setWidthPercentage(100.0f);
        table1.setSpacingBefore(5f);
        table1.setSpacingAfter(5f);
        table1.setWidths(new int[]{1, 2, 2, 2});

        PdfPCell snoValueCell = new PdfPCell(new Phrase("S.No", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        snoValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        snoValueCell.setPaddingBottom(5);
        snoValueCell.setPaddingTop(5);

        PdfPCell nameCell = new PdfPCell(new Phrase("NAME OF PENSIONER's", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setPaddingBottom(5);
        nameCell.setPaddingTop(5);

        PdfPCell accountno = new PdfPCell(new Phrase("BANK_ACCNO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        accountno.setHorizontalAlignment(Element.ALIGN_CENTER);
        accountno.setPaddingBottom(5);
        accountno.setPaddingTop(5);

        PdfPCell netAmount = new PdfPCell(new Phrase("NET PAYABLE ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        netAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
        netAmount.setPaddingBottom(5);
        netAmount.setPaddingTop(5);

        table1.addCell(snoValueCell);
        table1.addCell(nameCell);
        table1.addCell(accountno);
        table1.addCell(netAmount);

        PdfPCell bankname = new PdfPCell(new Phrase("Bank Name   :", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        bankname.setHorizontalAlignment(Element.ALIGN_CENTER);
        bankname.setPaddingBottom(5);
        bankname.setPaddingTop(5);
        bankname.setBorderColor(BaseColor.WHITE);
        bankname.setColspan(2);
        bankname.setBorderColorTop(BaseColor.BLACK);
        bankname.setBorderColorLeft(BaseColor.BLACK);
        bankname.setBorderWidthLeft(0.3f);
        bankname.setBorderWidthTop(0.3f);

        PdfPCell bankValue = new PdfPCell(new Phrase(bName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        bankValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        bankValue.setPaddingBottom(5);
        bankValue.setPaddingTop(5);
        bankValue.setColspan(2);
        bankValue.setBorderColor(BaseColor.WHITE);
        bankValue.setBorderColorTop(BaseColor.BLACK);
        bankValue.setBorderColorRight(BaseColor.BLACK);
        bankValue.setBorderWidthRight(0.3f);
        bankValue.setBorderWidthTop(0.3f);

        table1.addCell(bankname);
        table1.addCell(bankValue);
        Double netTotalAmount = 0d;

        if (PensionList != null && PensionList.size() > 0) {
            int count = 1;
            for (PensionEmployee pensionObject : PensionList) {
                PdfPCell snoValue = new PdfPCell(new Phrase(String.valueOf(count), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                snoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                snoValue.setPaddingBottom(5);
                snoValue.setPaddingTop(5);
                snoValue.setBorderWidthBottom(1);
                snoValue.setPaddingTop(3.0f);
                snoValue.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell nameValue = new PdfPCell(new Phrase(pensionObject.getEmployeeName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                nameValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                nameValue.setPaddingBottom(5);
                nameValue.setPaddingTop(5);
                nameValue.setBorderWidthBottom(1);
                nameValue.setPaddingTop(3.0f);
                nameValue.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell accountValue = new PdfPCell(new Phrase(pensionObject.getAccountNumber(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                accountValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                accountValue.setPaddingBottom(5);
                accountValue.setPaddingTop(5);
                accountValue.setBorderWidthBottom(1);
                accountValue.setPaddingTop(3.0f);
                accountValue.setHorizontalAlignment(Element.ALIGN_CENTER);

                //For Family Pension
                if (!pensionObject.getFamilyPension().equals("")) {
                    netTotalAmount = netTotalAmount + Double.parseDouble(pensionObject.getFamilyPension());
                    PdfPCell netValue = new PdfPCell(new Phrase(String.valueOf(pensionObject.getFamilyPension()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                    netValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                    netValue.setPaddingBottom(5);
                    netValue.setPaddingTop(5);
                    netValue.setBorderWidthBottom(1);
                    netValue.setPaddingTop(3.0f);
                    netValue.setHorizontalAlignment(Element.ALIGN_CENTER);

                    table1.addCell(snoValue);
                    table1.addCell(nameValue);
                    table1.addCell(accountValue);
                    table1.addCell(netValue);
                } //For Pension
                else if (!pensionObject.getPension().equals("")) {
                    netTotalAmount = netTotalAmount + Double.parseDouble(pensionObject.getPension());
                    PdfPCell netValue = new PdfPCell(new Phrase(String.valueOf(pensionObject.getPension()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                    netValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                    netValue.setPaddingBottom(5);
                    netValue.setPaddingTop(5);
                    netValue.setBorderWidthBottom(1);
                    netValue.setPaddingTop(3.0f);
                    netValue.setHorizontalAlignment(Element.ALIGN_CENTER);

                    table1.addCell(snoValue);
                    table1.addCell(nameValue);
                    table1.addCell(accountValue);
                    table1.addCell(netValue);
                }

                count++;
            }

            PdfPCell emptyCell = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
            emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            emptyCell.setPaddingBottom(5);
            emptyCell.setPaddingTop(5);
            emptyCell.setBorderColor(BaseColor.WHITE);
            emptyCell.setBorderColorTop(BaseColor.BLACK);

            PdfPCell Totalname = new PdfPCell(new Phrase("Total   :", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
            Totalname.setHorizontalAlignment(Element.ALIGN_CENTER);
            Totalname.setPaddingBottom(5);
            Totalname.setPaddingTop(5);
            Totalname.setBorderColor(BaseColor.WHITE);
            Totalname.setBorderColorTop(BaseColor.BLACK);

            PdfPCell TotalValue = new PdfPCell(new Phrase(String.valueOf(netTotalAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
            TotalValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            TotalValue.setPaddingBottom(5);
            TotalValue.setPaddingTop(5);
            TotalValue.setBorderColor(BaseColor.WHITE);
            TotalValue.setBorderColorTop(BaseColor.BLACK);

            table1.addCell(emptyCell);
            table1.addCell(emptyCell);
            table1.addCell(Totalname);
            table1.addCell(TotalValue);

        }

        outercell.addElement(table1);

        //add code
        outerTable.addCell(outercell);

        document.add(outerTable);
        document.close();

        return bos;
    }
}
