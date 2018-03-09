/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.DeptWiseExpBudgetAllocation;
import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class BudgetReAppropriationReportManager {

    public ByteArrayOutputStream generateBudgetReport(List<DeptWiseExpBudgetAllocation> budgetAtGlanceList, String path, String finYear) throws DocumentException, FileNotFoundException, Exception {

        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);
        document.open();

        //String formulJson = new com.accure.hrms.manager.FundHeadMappingManager().viewFHMList();
        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();
        PdfPCell outercell2 = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("Budget ReAppropriation Details", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);

        //financial year
        String financialYearJson = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
        List<com.accure.hrms.dto.FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<com.accure.hrms.dto.FinancialYear>>() {
        }.getType());
        com.accure.hrms.dto.FinancialYear fyObj = fyList.get(0);
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
        timePhrase.add(new Phrase(new Chunk(finYear, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));

        PdfPCell timecell = new PdfPCell(timePhrase);
        timecell.setBorderWidthBottom(1);
        timecell.setPaddingTop(3.0f);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        float[] eightcolumn = {1f, 2f, 2f, 2f, 2f};

        //Heads
        PdfPTable table3 = new PdfPTable(5); // 3 columns.
        table3.setWidthPercentage(100); //Width 100%
        table3.setWidths(eightcolumn);
        table3.setSpacingBefore(5.0f);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellb = new PdfPCell(new Paragraph("From Ledger", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(5);
        table3cellb.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("Amount Before ReAppropriation", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celld = new PdfPCell(new Paragraph("ReAppropriation Amount", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(5);
        table3celld.setPaddingTop(5);

        PdfPCell table3celle = new PdfPCell(new Paragraph("Amount After ReAppropriation", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celle.setPaddingBottom(5);
        table3celle.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);
        table3.addCell(table3celld);
        table3.addCell(table3celle);

        int sNo = 1;

//        List<FundHeadMapping> formulalist = new Gson().fromJson(formulJson, new TypeToken<List<FundHeadMapping>>() {
//        }.getType());
        if (budgetAtGlanceList.size() != 0) {
            for (DeptWiseExpBudgetAllocation key : budgetAtGlanceList) {
                //PDF Document
                String ledgerName = key.getLedgerName();
                double sanctionedAmount = Double.parseDouble(key.getSanctionedAmount());
                try {
                    sanctionedAmount = sanctionedAmount + Double.parseDouble(key.getExtraProvisionAmount());
                } catch (Exception ex) {

                }
                String appropriationValueStr = key.getAppropriationValue();
                if (appropriationValueStr != null) {
                    if (appropriationValueStr.contains("-")) {
                        double reapproprationvalue = Double.parseDouble(key.getAppropriationValue());
                        double amountaftrReapro = sanctionedAmount + reapproprationvalue;
////
                        String SerNo = Integer.toString(sNo);
//
                        PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table5cell1.setPaddingBottom(5);
                        table5cell1.setPaddingTop(5);

                        PdfPCell cell2 = new PdfPCell(new Paragraph(ledgerName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell2.setPaddingBottom(5);
                        cell2.setPaddingTop(5);

                        PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(sanctionedAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell3.setPaddingBottom(5);
                        cell3.setPaddingTop(5);

                        PdfPCell cell4 = new PdfPCell(new Paragraph(String.valueOf(reapproprationvalue), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell4.setPaddingBottom(5);
                        cell4.setPaddingTop(5);

                        PdfPCell cell5 = new PdfPCell(new Paragraph(String.valueOf(amountaftrReapro), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell5.setPaddingBottom(5);
                        cell5.setPaddingTop(5);

                        table3.addCell(table5cell1);
                        table3.addCell(cell2);
                        table3.addCell(cell3);
                        table3.addCell(cell4);
                        table3.addCell(cell5);
                        sNo++;
                    }
                }
            }
        }
        outercell.addElement(table3);
        RoundRectangle roundRectangle = new RoundRectangle();
        outercell.setCellEvent(roundRectangle);
        outercell.setBorder(Rectangle.NO_BORDER);
        outercell.setBorderWidth(2);
        outercell.setPadding(8);
        //Heads
        PdfPTable table4 = new PdfPTable(5); // 3 columns.
        table4.setWidthPercentage(100); //Width 100%
        table4.setWidths(eightcolumn);
        table4.setSpacingBefore(5.0f);

        PdfPCell table4cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table4cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table4cella.setPaddingBottom(5);
        table4cella.setPaddingTop(5);

        PdfPCell table4cellb = new PdfPCell(new Paragraph("To Ledger", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table4cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table4cellb.setPaddingBottom(5);
        table4cellb.setPaddingTop(5);

        PdfPCell table4cellc = new PdfPCell(new Paragraph("Amount Before ReAppropriation", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table4cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table4cellc.setPaddingBottom(5);
        table4cellc.setPaddingTop(5);

        PdfPCell table4celld = new PdfPCell(new Paragraph("ReAppropriation Amount", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table4celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table4celld.setPaddingBottom(5);
        table4celld.setPaddingTop(5);

        PdfPCell table4celle = new PdfPCell(new Paragraph("Amount After ReAppropriation", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table4celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table4celle.setPaddingBottom(5);
        table4celle.setPaddingTop(5);

        table4.addCell(table4cella);
        table4.addCell(table4cellb);
        table4.addCell(table4cellc);
        table4.addCell(table4celld);
        table4.addCell(table4celle);

        sNo = 1;

//        List<FundHeadMapping> formulalist = new Gson().fromJson(formulJson, new TypeToken<List<FundHeadMapping>>() {
//        }.getType());
        if (budgetAtGlanceList.size() != 0) {
            for (DeptWiseExpBudgetAllocation key : budgetAtGlanceList) {
                //PDF Document
                String ledgerName = key.getLedgerName();
                double sanctionedAmount = Double.parseDouble(key.getSanctionedAmount());
                try {
                    sanctionedAmount = sanctionedAmount + Double.parseDouble(key.getExtraProvisionAmount());
                } catch (Exception ex) {

                }
                String appropriationValueStr = key.getAppropriationValue();
                if (appropriationValueStr != null) {
                    if (!appropriationValueStr.contains("-")) {
                        double reapproprationvalue = Double.parseDouble(key.getAppropriationValue());
                        double amountaftrReapro = sanctionedAmount + reapproprationvalue;
////
                        String SerNo = Integer.toString(sNo);
//
                        PdfPCell table5cella = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        table5cella.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table5cella.setPaddingBottom(5);
                        table5cella.setPaddingTop(5);

                        PdfPCell cellb = new PdfPCell(new Paragraph(ledgerName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellb.setPaddingBottom(5);
                        cellb.setPaddingTop(5);

                        PdfPCell cellc = new PdfPCell(new Paragraph(String.valueOf(sanctionedAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellc.setPaddingBottom(5);
                        cellc.setPaddingTop(5);

                        PdfPCell celld = new PdfPCell(new Paragraph(String.valueOf(reapproprationvalue), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        celld.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celld.setPaddingBottom(5);
                        celld.setPaddingTop(5);

                        PdfPCell celle = new PdfPCell(new Paragraph(String.valueOf(amountaftrReapro), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        celle.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celle.setPaddingBottom(5);
                        celle.setPaddingTop(5);

                        table4.addCell(table5cella);
                        table4.addCell(cellb);
                        table4.addCell(cellc);
                        table4.addCell(celld);
                        table4.addCell(celle);
                        sNo++;
                    }
                }
            }
        }

        outercell.addElement(table4);
        RoundRectangle roundRectangle1 = new RoundRectangle();
        outercell.setCellEvent(roundRectangle1);
        outercell.setBorder(Rectangle.NO_BORDER);
        outercell.setBorderWidth(2);
        outercell.setPadding(8);
        outerTable.addCell(outercell);
        // outerTable.addCell(outercell2);
        document.add(outerTable);
        document.close();
        return bos;
    }

    public List<DeptWiseExpBudgetAllocation> generateReApropriationBudgetReport(String obj1) throws Exception {
        DeptWiseExpBudgetAllocation obj = new Gson().fromJson(obj1, new TypeToken<DeptWiseExpBudgetAllocation>() {
        }.getType());
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (obj.getBudgetType().isEmpty() == false) {
            conditionMap.put("budgetType", obj.getBudgetType());
        }
        if (obj.getFundtype().isEmpty() == false) {
            conditionMap.put("fundtype", obj.getFundtype());
        }
        if (obj.getFinYear().isEmpty() == false) {
            conditionMap.put("finYear", obj.getFinYear());
        }
        if (obj.getSector().isEmpty() == false) {
            conditionMap.put("sector", obj.getSector());
        }
        if (obj.getDdo().isEmpty() == false) {
            conditionMap.put("ddo", obj.getDdo());
        }
        if (obj.getBudgetHead().isEmpty() == false) {
            conditionMap.put("budgetHead", obj.getBudgetHead());
        }
        if (obj.getLocation().isEmpty() == false) {
            conditionMap.put("location", obj.getLocation());
        }
        String result1 = "";
        // conditionMap.put("isSanctioned", "true");
        //System.out.println("-------conditionMap----"+conditionMap.toString());
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DEPTWISE_EXP_BUDGET_ALLOC_TABLE, conditionMap);
        if (result != null) {
            List<DeptWiseExpBudgetAllocation> list = new Gson().fromJson(result, new TypeToken<List<DeptWiseExpBudgetAllocation>>() {
            }.getType());

            //list = getBudgetHeadAndDescription(list);
            return list;
        }
        return null;
    }
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
