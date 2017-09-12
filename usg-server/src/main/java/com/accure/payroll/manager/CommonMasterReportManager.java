/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.budget.dto.FundType;
import com.accure.budget.manager.FundTypeManager;
import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.Bank;
import com.accure.hrms.dto.DdoDepartmentMap;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.DesignationFundTypeMapping;
import com.accure.hrms.dto.Formula;
import com.accure.hrms.manager.DDODepartmentManager;
import com.accure.hrms.manager.DesignationFundTypeMappingManager;
import com.accure.hrms.manager.FormulaManager;
import com.accure.hrms.manager.FundHeadMappingManager;
import com.accure.hrms.manager.PFTypeManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;
import com.accure.hrms.dto.PFType;
import com.accure.hrms.manager.NatureManager;
import com.accure.hrms.dto.Nature;
import com.accure.hrms.dto.DAMaster;
import com.accure.hrms.dto.FinancialYear;
import com.accure.hrms.dto.FundHeadMapping;
import com.accure.hrms.dto.GISGroup;
import com.accure.hrms.dto.Grade;
import com.accure.hrms.dto.GradeDetails;
import com.accure.hrms.dto.HeadSlab;
import com.accure.hrms.dto.SalaryHead;
import com.accure.hrms.manager.BankManager;
import com.accure.hrms.manager.ClassManager;
import com.accure.hrms.manager.DAManager;
import com.accure.hrms.manager.DesignationManager;
import com.accure.hrms.manager.GISGroupMasterManager;
import com.accure.hrms.manager.GradeManager;
import com.accure.hrms.manager.HeadSlabManager;
import com.accure.hrms.manager.SalaryHeadManager;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author user
 */
public class CommonMasterReportManager {

    public ByteArrayOutputStream getFormulaMasterReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {
        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String formulJson = new FormulaManager().viewFormulaList();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(70.0f, 70.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColor(BaseColor.WHITE);

        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 22, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("    Formula Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        float[] eightcolumn = {1f, 3f, 3f, 1f};

        PdfPTable table3 = new PdfPTable(4);
        table3.setWidthPercentage(100);
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        table3.setHeaderRows(1);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);
        PdfPCell table3cellb = new PdfPCell(new Phrase("Description", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);
        PdfPCell table3cellc = new PdfPCell(new Phrase("Formula", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);
        PdfPCell table3celld = new PdfPCell(new Phrase("Order", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);
        table3.addCell(table3celld);

        int sNo = 1;

        List<Formula> formulalist = new Gson().fromJson(formulJson, new TypeToken<List<Formula>>() {
        }.getType());
        for (Formula key : formulalist) {
            String descrption = key.getDescription();
            String formula = key.getFormula();
            int orderNo = key.getOrder();
            String replacefromulahashSymbol = formula.replaceAll("#", "");

            PdfPCell table5cell1 = new PdfPCell(new Phrase(String.valueOf(sNo), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);
            PdfPCell cell2 = new PdfPCell(new Phrase(descrption, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);
            PdfPCell cell3 = new PdfPCell(new Phrase(replacefromulahashSymbol, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);
            PdfPCell cell4 = new PdfPCell(new Phrase(String.valueOf(orderNo), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);

            sNo++;
        }

        outercell.addElement(table3);

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

    public ByteArrayOutputStream getBankReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {
        Document document = new Document();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);

        document.open();

        String formulJson = new BankManager().fetchAllBank();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    Bank Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        float[] eightcolumn = {2f, 3f, 3f, 3f, 3f, 3f};

        PdfPTable table3 = new PdfPTable(6);
        table3.setWidthPercentage(100);
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Bank Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Branch Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("City", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celle = new PdfPCell(new Paragraph("State", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3cellf = new PdfPCell(new Paragraph("A/C No", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);
        table3.addCell(table3celld);
        table3.addCell(table3celle);
        table3.addCell(table3cellf);

        table3.setHeaderRows(1);
        int sNo = 1;

        List<Bank> bankList = new Gson().fromJson(formulJson, new TypeToken<List<Bank>>() {
        }.getType());
        for (Bank key : bankList) {
            //PDF Document
            String bankName = key.getBankname();
            String brName = key.getBranchname();
            String city = key.getCity();
            String state = key.getState();
            String accNo = key.getAcnumber();

            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(bankName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(brName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph(city, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            PdfPCell cell5 = new PdfPCell(new Paragraph(state, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setPaddingBottom(5);
            cell5.setPaddingTop(5);

            PdfPCell cell6 = new PdfPCell(new Paragraph(accNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setPaddingBottom(5);
            cell6.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);
            table3.addCell(cell5);
            table3.addCell(cell6);
            sNo++;
        }
        outercell.addElement(table3);
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

    public ByteArrayOutputStream getDesignationReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {
        Document document = new Document();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);

        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
        Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        document.open();

        String designationJson = new DesignationManager().fetchAll();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    Designation Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        float[] eightcolumn = {1f, 2f, 2f, 2f, 2f, 2f};
        float[] onecolumn = {1f};

        PdfPTable table3 = new PdfPTable(6);
        table3.setWidthPercentage(100);
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Designation", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Class", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("Grade", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        PdfPCell table3celle = new PdfPCell(new Paragraph("Retirement Age", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celle.setPaddingBottom(5);
        table3celle.setPaddingTop(5);

        PdfPCell table3cellf = new PdfPCell(new Paragraph("Seniority Level", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellf.setPaddingBottom(5);
        table3cellf.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);
        table3.addCell(table3celld);
        table3.addCell(table3celle);
        table3.addCell(table3cellf);

        int sNo = 1;

        List<Designation> designationlist = new Gson().fromJson(designationJson, new TypeToken<List<Designation>>() {
        }.getType());
        for (Designation key : designationlist) {

            String designation = key.getDesignation();
            String clas = key.getClas();
            String retirementAge = key.getRetirementAge();
            String seniorityLevel = key.getSeniorityLevel();
            String grade = key.getGradeName();
            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(designation, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(clas, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = null;
            Grade finResult = null;
//            try {
//                grade = grade.replace("\"", "");
//                String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE_TABLE, grade);
//                List<Grade> GradeList = new Gson().fromJson(result1, new TypeToken<List<Grade>>() {
//                }.getType());
//                finResult = GradeList.get(0);
//
//            } catch (Exception e) {
//            }
//            if (finResult == null) {
//                cell4 = new PdfPCell(new Paragraph("", font3));
//            } else {
            cell4 = new PdfPCell(new Paragraph(grade, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            // }
            PdfPCell cell5 = new PdfPCell(new Paragraph(retirementAge, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setPaddingBottom(5);
            cell5.setPaddingTop(5);

            PdfPCell cell6 = new PdfPCell(new Paragraph(seniorityLevel, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setPaddingBottom(5);
            cell6.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);
            table3.addCell(cell5);
            table3.addCell(cell6);
            sNo++;
        }
        outercell.addElement(table3);
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

    public ByteArrayOutputStream getDaMaReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {
        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String damaJson = new DAManager().viewDAMaster();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    DA/MA Rate(s) Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        float[] eightcolumn = {1f, 2f, 2f, 2f, 2f, 2f};

        PdfPTable table3 = new PdfPTable(6);
        table3.setWidthPercentage(100);
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Rate Defined For", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Paid Rate", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("Actual Rate", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        PdfPCell table3celle = new PdfPCell(new Paragraph("Effective From Date ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celle.setPaddingBottom(5);
        table3celle.setPaddingTop(5);

        PdfPCell table3cellf = new PdfPCell(new Paragraph("Effective To Date", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellf.setPaddingBottom(5);
        table3cellf.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);
        table3.addCell(table3celld);
        table3.addCell(table3celle);
        table3.addCell(table3cellf);

        int sNo = 1;

        List<DAMaster> damaList = new Gson().fromJson(damaJson, new TypeToken<List<DAMaster>>() {
        }.getType());
        for (DAMaster key : damaList) {

            String rateDefinedFor = key.getDefinedRate();
            int paidRate = key.getPaidRate();
            int actualRate = key.getActualRate();
            String efffromDate = key.getEffFromDate();
            String effectiveString = key.getEffToDate();

            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell table5cell2 = new PdfPCell(new Paragraph(rateDefinedFor, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell2.setPaddingBottom(5);
            table5cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(paidRate), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph(String.valueOf(actualRate), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            PdfPCell cell5 = new PdfPCell(new Paragraph(efffromDate, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setPaddingBottom(5);
            cell5.setPaddingTop(5);
            PdfPCell cell6 = new PdfPCell(new Paragraph(effectiveString, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setPaddingBottom(5);
            cell6.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(table5cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);
            table3.addCell(cell5);
            table3.addCell(cell6);

            sNo++;

        }
        outercell.addElement(table3);
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

    public ByteArrayOutputStream getDesiFundTypeMappingReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String mappingList = new DesignationFundTypeMappingManager().fetch();

        float[] eightcolumn = {1f, 2f, 2f, 2f, 2f, 2f};

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("Designation FundType Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        PdfPTable table3 = new PdfPTable(6);
        table3.setWidthPercentage(100);
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("DDO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Designation", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("Fund Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        PdfPCell table3celle = new PdfPCell(new Paragraph("Nature Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celle.setPaddingBottom(5);
        table3celle.setPaddingTop(5);

        PdfPCell table3cellf = new PdfPCell(new Paragraph("Budget Head", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);
        table3.addCell(table3celld);
        table3.addCell(table3celle);
        table3.addCell(table3cellf);

        int sNo = 1;

        List<DesignationFundTypeMapping> fundmMppingList = new Gson().fromJson(mappingList, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        for (DesignationFundTypeMapping key : fundmMppingList) {
            String ddo = key.getDdo();
            String designation = key.getDesignation();
            String fundType = key.getFundType();
            String naturetype = key.getNatureType();
            String budgetHead = key.getBudgetHead();

            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(ddo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(designation, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph(fundType, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            PdfPCell cell5 = new PdfPCell(new Paragraph(naturetype, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setPaddingBottom(5);
            cell5.setPaddingTop(5);

            PdfPCell cell6 = new PdfPCell(new Paragraph(budgetHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setPaddingBottom(5);
            cell6.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);
            table3.addCell(cell5);
            table3.addCell(cell6);

            sNo++;

        }
        outercell.addElement(table3);
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

    public ByteArrayOutputStream getFundHeadMappingReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String formulJson = new FundHeadMappingManager().viewFHMList();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    Fund Head Mapping Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        float[] eightcolumn = {1f, 3f, 3f};

        //Heads
        PdfPTable table3 = new PdfPTable(3); // 3 columns.
        table3.setWidthPercentage(100); //Width 100%
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Fund Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Sector", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);

        int sNo = 1;

        List<FundHeadMapping> formulalist = new Gson().fromJson(formulJson, new TypeToken<List<FundHeadMapping>>() {
        }.getType());
        for (FundHeadMapping key : formulalist) {
            //PDF Document
            String fundType = key.getFundType();
            String sector = key.getSector();

            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(fundType, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(sector, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            sNo++;
        }
        outercell.addElement(table3);
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

    public ByteArrayOutputStream getDdoDepartmentMappingReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String formulJson = new DDODepartmentManager().fetch();

        float[] eightcolumn = {1f, 3f, 3f};

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("DDO Department Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        PdfPTable table3 = new PdfPTable(3);
        table3.setWidthPercentage(100);
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("DDO Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Department", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);

        int sNo = 1;

        List<DdoDepartmentMap> ddodepartmentlist = new Gson().fromJson(formulJson, new TypeToken<List<DdoDepartmentMap>>() {
        }.getType());

        for (DdoDepartmentMap key : ddodepartmentlist) {
            String ddo = key.getDdo();
            List<String> dept = key.getDepartmentList();

            String SerNo = Integer.toString(sNo);

            String department = "";
            int countlist = 0;
            for (int deptCount = 0; deptCount < dept.size(); deptCount++) {
                countlist++;
                department += countlist + "." + dept.get(deptCount) + "\n";
            }

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(ddo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(department, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            sNo++;
        }
        outercell.addElement(table3);
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

    public ByteArrayOutputStream getPfTypeReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String formulJson = new PFTypeManager().fetchAll();

        float[] eightcolumn = {1f, 3f};

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("PF Type Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        PdfPTable table3 = new PdfPTable(2);
        table3.setWidthPercentage(100);
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("PF Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        int sNo = 1;

        List<PFType> formulalist = new Gson().fromJson(formulJson, new TypeToken<List<PFType>>() {
        }.getType());
        for (PFType key : formulalist) {

            String pfType = key.getPFType();

            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(pfType, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);

            sNo++;
        }
        outercell.addElement(table3);
        RoundRectangle roundRectangle = new RoundRectangle();
        outercell.setCellEvent(roundRectangle);
        outercell.setBorder(Rectangle.NO_BORDER);
        outercell.setBorderWidth(2);
        outercell.setPadding(8);

        outerTable.addCell(outercell);
        document.add(outerTable);
        document.close();

        document.close();
        return bos;
    }

    public ByteArrayOutputStream getFundTypeReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    Fund Type Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        List<FundType> fundTypeList = null;
        fundTypeList = new FundTypeManager().view();

        float[] eightcolumn = {1f, 3f, 3f, 1f};

        PdfPTable table3 = new PdfPTable(4);
        table3.setWidthPercentage(100);
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Description", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Under Fund Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("Order", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);
        table3.addCell(table3celld);
        int sNo = 1;

        for (FundType key : fundTypeList) {
            String description = key.getDescription();
            String underFundType = key.getFundTypeName();
            String order = key.getOrder();

            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(description, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(underFundType, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph(order, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);

            sNo++;
        }
        outercell.addElement(table3);
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

    public ByteArrayOutputStream getNatureTypeReports(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String formulJson = new NatureManager().fetchAll();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    Nature Type Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);

        outercell.addElement(header);

        float[] eightcolumn = {1f, 3f};

        PdfPTable table3 = new PdfPTable(2);
        table3.setWidthPercentage(100);
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Nature Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);

        int sNo = 1;
        PdfPTable emplist = new PdfPTable(2);

        List<Nature> naturelist = new Gson().fromJson(formulJson, new TypeToken<List<Nature>>() {
        }.getType());
        for (Nature key : naturelist) {

            String nature = key.getNatureName();
            emplist.flushContent();
            emplist.setWidthPercentage(100);
            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(nature, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);

            sNo++;
        }
        outercell.addElement(table3);
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

    //Salary Head
    public ByteArrayOutputStream getSalaryHeadReport(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String salaryHeadList = new SalaryHeadManager().fetchAll();

        float[] sevencolumn = {1f, 2f, 2f, 2f, 2f, 1f, 2f};
        float[] onecolumn = {1f};

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    Salary Head Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        //Heads
        PdfPTable table3 = new PdfPTable(7);
        table3.setWidthPercentage(100); //Width 100%
        table3.setWidths(sevencolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Description", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Short Description", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("Head Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        PdfPCell table3celle = new PdfPCell(new Paragraph("Mapping", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celle.setPaddingBottom(5);
        table3celle.setPaddingTop(5);

        PdfPCell table3cellf = new PdfPCell(new Paragraph("Order Level", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellf.setPaddingBottom(5);
        table3cellf.setPaddingTop(5);

        PdfPCell table3cellg = new PdfPCell(new Paragraph("Display Level", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellg.setPaddingBottom(5);
        table3cellg.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);
        table3.addCell(table3celld);
        table3.addCell(table3celle);
        table3.addCell(table3cellf);
        table3.addCell(table3cellg);

        int sNo = 1;

        List<SalaryHead> headlist = new Gson().fromJson(salaryHeadList, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (SalaryHead key : headlist) {

            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(key.getDescription(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(key.getShortDescription(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph(key.getHeadType(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            PdfPCell cell5 = new PdfPCell(new Paragraph(key.getMapping(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setPaddingBottom(5);
            cell5.setPaddingTop(5);

            PdfPCell cell6 = new PdfPCell(new Paragraph(Integer.toString(key.getOrderLevel()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setPaddingBottom(5);
            cell6.setPaddingTop(5);

            PdfPCell cell7 = new PdfPCell(new Paragraph(Integer.toString(key.getDisplayLevel()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell7.setPaddingBottom(5);
            cell7.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);
            table3.addCell(cell5);
            table3.addCell(cell6);
            table3.addCell(cell7);
            sNo++;
        }
        outercell.addElement(table3);
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
//Head Slab

    public ByteArrayOutputStream getHeadSlabReport(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Rectangle pageSize = new Rectangle(1000f, 1000f);
        Document document = new Document(pageSize);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String headslabList = new HeadSlabManager().fetchAll();

        float[] thritencolumn = {1f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f};
        float[] onecolumn = {1f};

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
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

        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("    Head Slab Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        //Heads
        PdfPTable table = new PdfPTable(13);
        table.setWidthPercentage(100); //Width 100%
        table.setWidths(thritencolumn);
        table.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Head Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("DDO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("Salary Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        PdfPCell table3celle = new PdfPCell(new Paragraph("City Category", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celle.setPaddingBottom(5);
        table3celle.setPaddingTop(5);

        PdfPCell table3cellf = new PdfPCell(new Paragraph("City", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellf.setPaddingBottom(5);
        table3cellf.setPaddingTop(5);

        PdfPCell table3celllsd = new PdfPCell(new Paragraph("Class", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celllsd.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celllsd.setPaddingBottom(5);
        table3celllsd.setPaddingTop(5);

        PdfPCell table3cellg = new PdfPCell(new Paragraph("Nature", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellg.setPaddingBottom(5);
        table3cellg.setPaddingTop(5);

        PdfPCell table3cellh = new PdfPCell(new Paragraph("Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellh.setPaddingBottom(5);
        table3cellh.setPaddingTop(5);

        PdfPCell table3celli = new PdfPCell(new Paragraph("From GP", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celli.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celli.setPaddingBottom(5);
        table3celli.setPaddingTop(5);

        PdfPCell table3cellj = new PdfPCell(new Paragraph("To GP", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellj.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellj.setPaddingBottom(5);
        table3cellj.setPaddingTop(5);

        PdfPCell table3cellk = new PdfPCell(new Paragraph("Amount", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellk.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellk.setPaddingBottom(5);
        table3cellk.setPaddingTop(5);

        PdfPCell table3celll = new PdfPCell(new Paragraph("Formula", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celll.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celll.setPaddingBottom(5);
        table3celll.setPaddingTop(5);

        table.addCell(table3cella);
        table.addCell(table3cellb);
        table.addCell(table3cellc);
        table.addCell(table3celld);
        table.addCell(table3celle);
        table.addCell(table3cellf);
        table.addCell(table3celllsd);
        table.addCell(table3cellg);
        table.addCell(table3cellh);
        table.addCell(table3celli);
        table.addCell(table3cellj);
        table.addCell(table3cellk);
        table.addCell(table3celll);

        int sNo = 0;

        //get the data from employee'
        List<HeadSlab> headlist = new Gson().fromJson(headslabList, new TypeToken<List<HeadSlab>>() {
        }.getType());
        for (HeadSlab key : headlist) {

            sNo++;
            String SerNo = Integer.toString(sNo);

            PdfPCell cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setPaddingBottom(5);
            cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(key.getHeadName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(key.getDdo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph(key.getSalaryType(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            PdfPCell cell5 = new PdfPCell(new Paragraph(key.getCityCategory(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setPaddingBottom(5);
            cell5.setPaddingTop(5);

            PdfPCell cell6 = new PdfPCell(new Paragraph(key.getCity(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setPaddingBottom(5);
            cell6.setPaddingTop(5);

            PdfPCell cell7 = new PdfPCell(new Paragraph(key.getClas(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell7.setPaddingBottom(5);
            cell7.setPaddingTop(5);

            PdfPCell cell8 = new PdfPCell(new Paragraph(key.getNatureType(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell8.setPaddingBottom(5);
            cell8.setPaddingTop(5);

            PdfPCell cell9 = new PdfPCell(new Paragraph(key.getType(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell9.setPaddingBottom(5);
            cell9.setPaddingTop(5);

            PdfPCell cell10 = new PdfPCell(new Paragraph(Long.toString(key.getFromGPLong()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell10.setPaddingBottom(5);
            cell10.setPaddingTop(5);

            PdfPCell cell11 = new PdfPCell(new Paragraph(Long.toString(key.getToGPLong()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell11.setPaddingBottom(5);
            cell11.setPaddingTop(5);

            PdfPCell cell12 = new PdfPCell(new Paragraph(Long.toString(key.getAmountLong()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell12.setPaddingBottom(5);
            cell12.setPaddingTop(5);

            PdfPCell cell13 = new PdfPCell(new Paragraph(key.getFormulaTwo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell13.setPaddingBottom(5);
            cell13.setPaddingTop(5);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            table.addCell(cell7);
            table.addCell(cell8);
            table.addCell(cell9);
            table.addCell(cell10);
            table.addCell(cell11);
            table.addCell(cell12);
            table.addCell(cell13);

        }
        outercell.addElement(table);
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

    public ByteArrayOutputStream getGradeMasterReport(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String gradeList = new GradeManager().fetchAll();

        float[] fourcolumn = {1f, 2f, 2f, 2f};
        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    Grade Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(fourcolumn);
        table.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Grade Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Grade Pay", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("Increment Type", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        table.addCell(table3cella);
        table.addCell(table3cellb);
        table.addCell(table3cellc);
        table.addCell(table3celld);
        int sNo = 1;

        //System.out.println("gradeList" + gradeList);
        List<Grade> headlist = new Gson().fromJson(gradeList, new TypeToken<List<Grade>>() {
        }.getType());
        for (Grade key : headlist) {
            String SerNo = Integer.toString(sNo);
            String gradepay = "";
            gradepay = key.getGradePay();
            String incrementtype = "";
            incrementtype = key.getIncrementType();

            String details = "";
            List<GradeDetails> li = key.getGradeDetailsList();
            GradeDetails data = li.get(0);
            details = data.getGradeOne() + "-" + data.getGradeTwo() + "-" + data.getGradeThree() + "-" + data.getOrder();

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(details, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(gradepay, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph(incrementtype, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            table.addCell(table5cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            sNo++;
        }
        outercell.addElement(table);
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

    //Class Master
    public ByteArrayOutputStream getClassMasterReport(String path,String fin) throws DocumentException, FileNotFoundException, Exception {
        Document document = new Document();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String gradeList = new ClassManager().fetchAllClass();

        float[] fivecolumn = {1f, 2f, 2f, 2f, 2f};
        float[] onecolumn = {1f};

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    Class Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        //Heads
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100); //Width 100%
        table.setWidths(fivecolumn);
        table.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Class Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Gad-Nongad", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("Is Teaching", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        PdfPCell table3celle = new PdfPCell(new Paragraph("Is Non-Teaching", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celle.setPaddingBottom(5);
        table3celle.setPaddingTop(5);

        table.addCell(table3cella);
        table.addCell(table3cellb);
        table.addCell(table3cellc);
        table.addCell(table3celld);
        table.addCell(table3celle);
        int sNo = 1;

        List<com.accure.hrms.dto.Class> headlist = new Gson().fromJson(gradeList, new TypeToken<List<com.accure.hrms.dto.Class>>() {
        }.getType());
        for (com.accure.hrms.dto.Class key : headlist) {
            //PDF Document
            String SerNo = Integer.toString(sNo);
            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell table3cell = new PdfPCell(new Paragraph(key.getName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table3cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3cell.setPaddingBottom(5);
            table3cell.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(key.getGadNonGad(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(key.getTeaching(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph(key.getNonTeaching(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            table.addCell(table5cell1);
            table.addCell(table3cell);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);

            sNo++;
        }
        outercell.addElement(table);
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

    public ByteArrayOutputStream getGisGroupReport(String path,String fin) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        String gradeList = new GISGroupMasterManager().viewGISGroupMaster();

        float[] fourcolumn = {1f, 2f, 2f, 2f};
        float[] onecolumn = {1f};
        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(10);

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("    GIS Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

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
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);
        //Heads
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100); //Width 100%
        table.setWidths(fourcolumn);
        table.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("Group Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Grade Pay", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("Rate of Subscription", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        table.addCell(table3cella);
        table.addCell(table3cellb);
        table.addCell(table3cellc);
        table.addCell(table3celld);

        int sNo = 1;

        //get the data from employee'
        List<GISGroup> headlist = new Gson().fromJson(gradeList, new TypeToken<List<GISGroup>>() {
        }.getType());
        for (GISGroup key : headlist) {
            //PDF Document
            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(key.getGroupName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);
            cell2.setPaddingTop(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(key.getGradePayFrom(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph(key.getRateOfSub(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            table.addCell(table5cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            sNo++;
        }
        outercell.addElement(table);
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

    public static void main(String args[]) throws FileNotFoundException, Exception {
        ////System.out.println("final resule" + new CommonMasterReportManager().getDesignationReports());
    }
}
