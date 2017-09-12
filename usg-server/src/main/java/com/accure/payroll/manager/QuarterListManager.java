/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.FinancialYear;
import com.accure.hrms.dto.Quarter;
import com.accure.hrms.dto.QuarterCategory;
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
 * @author accure
 */
public class QuarterListManager {
    public ByteArrayOutputStream quarterListPdfStatement(String quarterCategory, String condition, String city, String path, String fin) throws DocumentException, FileNotFoundException, Exception {
        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);

        Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
        document.open();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{40, 200, 50});
        header.setSpacingAfter(18);

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
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360 005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Quarter List Details ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));

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
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");

        Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
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

        float[] sixthcolumn = {1f, 2f, 2f, 2f, 2f, 2f};
        float[] columnWidth = {1f, 1f, 1f, 1f, 1f};

        PdfPTable table1 = new PdfPTable(5); // 3 columns.
        table1.setWidthPercentage(100); //Width 100%
        table1.setWidths(columnWidth);

        PdfPCell table1cella = new PdfPCell(new Paragraph("", font2));
        table1cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cella.setPaddingBottom(5);
        table1cella.setPaddingTop(5);

        PdfPCell table1cellb = new PdfPCell(new Paragraph("", font2));
        table1cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cellb.setPaddingBottom(5);
        table1cellb.setPaddingTop(5);

        table1.addCell(table1cella);
        table1.addCell(table1cellb);

        outercell.addElement(table1);

        PdfPTable table2 = new PdfPTable(6);
        table2.setWidths(new float[]{1f, 4f, 4f, 4f, 1f, 1f});
        table2.setWidthPercentage(100); //Width 100%
        table2.setWidths(sixthcolumn);

        PdfPCell table2cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2cella.setPaddingBottom(5);
        table2cella.setPaddingTop(5);

        PdfPCell table2cellb = new PdfPCell(new Paragraph("Category", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2cellb.setPaddingBottom(5);
        table2cellb.setPaddingTop(5);

        PdfPCell table2cellc = new PdfPCell(new Paragraph("City", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2cellc.setPaddingBottom(5);
        table2cellc.setPaddingTop(5);

        PdfPCell table2celld = new PdfPCell(new Paragraph("Quarter No", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2celld.setPaddingBottom(5);
        table2celld.setPaddingTop(5);

        PdfPCell table2celle = new PdfPCell(new Paragraph("Carpet Area", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2celle.setPaddingBottom(5);
        table2celle.setPaddingTop(5);

        PdfPCell table2cellf = new PdfPCell(new Paragraph("Condition", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2cellf.setPaddingBottom(5);
        table2cellf.setPaddingTop(5);

        table2.addCell(table2cella);
        table2.addCell(table2cellb);
        table2.addCell(table2cellc);
        table2.addCell(table2celld);
        table2.addCell(table2celle);
        table2.addCell(table2cellf);
        outercell.addElement(table2);

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("category", quarterCategory);
        conditionMap.put("condition", condition);
        conditionMap.put("city", city);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_TABLE, conditionMap);
        if(result != null){
        List<Quarter> quarterList = new Gson().fromJson(result, new TypeToken<List<Quarter>>() {
        }.getType());

//    
        int sNo = 1;

        PdfPTable table3 = new PdfPTable(6);
        table3.setWidths(new float[]{1f, 2f, 3f, 2f, 2f, 2f});
        table3.setWidthPercentage(100); //Width 100%
        table3.setWidths(sixthcolumn);
        for (int i = 0; i < quarterList.size(); i++) {
            table3 = new PdfPTable(6);
            table3.setWidths(new float[]{1f, 2f, 3f, 2f, 2f, 2f});
            table3.setWidthPercentage(100); //Width 100%
            table3.setWidths(sixthcolumn);

            String SerNo = Integer.toString(sNo);

            Quarter quarter = quarterList.get(i);
            PdfPCell table3cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK)));
            table3cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3cell1.setPaddingBottom(5);
            table3cell1.setPaddingTop(5);

            PdfPCell cell2 = null;

            if (quarter.getCategory() != null) {

                String quarterCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.QUARTER_CATEGORY_TABLE, quarter.getCategory());

                List<QuarterCategory> quarterCategoryList = new Gson().fromJson(quarterCategoryJson, new TypeToken<List<QuarterCategory>>() {
                }.getType());
                QuarterCategory qc = quarterCategoryList.get(0);
                quarter.setCategory(qc.getCategory());

                cell2 = new PdfPCell(new Paragraph(quarter.getCategory(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK)));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setPaddingBottom(5);
                cell2.setPaddingTop(5);

            }

            PdfPCell cell3 = null;

            if (quarter.getCity() != null) {

                String cityJson = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_TABLE, quarter.getCity());

                List<CityMaster> cityList = new Gson().fromJson(cityJson, new TypeToken<List<CityMaster>>() {
                }.getType());
                CityMaster cm = cityList.get(0);
                quarter.setCity(cm.getCityName());

                cell3 = new PdfPCell(new Paragraph(quarter.getCity(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK)));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setPaddingBottom(5);
                cell3.setPaddingTop(5);
            }

            PdfPCell cell4 = new PdfPCell(new Paragraph(quarter.getQuarterNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);
            cell4.setPaddingTop(5);

            PdfPCell cell5 = new PdfPCell(new Paragraph(quarter.getCarpetArea(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK)));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setPaddingBottom(5);
            cell5.setPaddingTop(5);

            PdfPCell cell6 = new PdfPCell(new Paragraph(quarter.getCondition(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setPaddingBottom(5);
            cell6.setPaddingTop(5);

            table3.addCell(table3cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);
            table3.addCell(cell5);
            table3.addCell(cell6);
            outercell.addElement(table3);

            sNo++;
        }
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
