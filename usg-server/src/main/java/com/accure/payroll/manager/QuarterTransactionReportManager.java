/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.FinancialYear;
import com.accure.payroll.dto.QuaterTransaction;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author accure
 */
public class QuarterTransactionReportManager {

    public ByteArrayOutputStream quarterTransactionPdfStatement(String city1, String quarterCategory, String fromAllocationDate, String toAllocationDate, String fromLeftDate, String toLeftDate, String path, String fin) throws DocumentException, FileNotFoundException, Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = fromAllocationDate;
        String dateInString1 = toAllocationDate;
        String dateInString2 = null;

        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString3 = fromLeftDate;
        String dateInString4 = toLeftDate;
        String dateInString5 = null;

        Date date = new Date();
        Date date1 = new Date();
        Date date2 = new Date();

        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();

        Date date3 = new Date();
        Date date4 = new Date();
        Date date5 = new Date();
        try {

            date = formatter.parse(dateInString);
            date1 = formatter.parse(dateInString1);
            c.setTime(date);
            c.add(Calendar.DATE, -1);
            date = c.getTime();

            c1.setTime(date1);
            c1.add(Calendar.DATE, 1);
            date1 = c1.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {

            date3 = formatter1.parse(dateInString3);
            date4 = formatter1.parse(dateInString4);
            c2.setTime(date3);
            c2.add(Calendar.DATE, -1);
            date3 = c2.getTime();

            c3.setTime(date4);
            c3.add(Calendar.DATE, 1);
            date4 = c3.getTime();

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        Rectangle pageSize = new Rectangle(800f, 800f);
        Document document = new Document(pageSize);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        document.open();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{30, 200, 50});
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
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360 005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Quarter Transaction Details ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);
        headercell.setPaddingBottom(6f);
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
        Date dates = new Date();
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");

        Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk(dateFormat.format(dates), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(dates), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
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

        float[] sixthcolumn = {1f, 3f, 2f, 2f, 2f, 2f};

        PdfPTable table2 = new PdfPTable(6); // 6 columns.
        table2.setWidthPercentage(100); //Width 100%
        table2.setWidths(sixthcolumn);

        PdfPCell table2cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2cella.setPaddingBottom(5);
        table2cella.setPaddingTop(5);

        PdfPCell table2cellb = new PdfPCell(new Paragraph("DDO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2cellb.setPaddingBottom(5);
        table2cellb.setPaddingTop(5);

        PdfPCell table2cellc = new PdfPCell(new Paragraph("Employee Code", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2cellc.setPaddingBottom(5);
        table2cellc.setPaddingTop(5);

        PdfPCell table2celld = new PdfPCell(new Paragraph("Employee Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2celld.setPaddingBottom(5);
        table2celld.setPaddingTop(5);

        PdfPCell table2celle = new PdfPCell(new Paragraph("Allocation Date", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        table2celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2celle.setPaddingBottom(5);
        table2celle.setPaddingTop(5);

        PdfPCell table2cellf = new PdfPCell(new Paragraph("Leave date", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
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
        conditionMap.put("city", city1);
        conditionMap.put("quaterCategory", quarterCategory);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_TRANSACTION_TABLE, conditionMap);
        List<QuaterTransaction> qTList = new Gson().fromJson(result, new TypeToken<List<QuaterTransaction>>() {
        }.getType());

        int sNo = 1;
        PdfPTable table3 = new PdfPTable(6); // 6 columns
        table3.setWidthPercentage(100); //Width 100%
        table3.setWidths(sixthcolumn);
        if (result != null) {
            qTList = getDDO(qTList);
            for (int i = 0; i < qTList.size(); i++) {
                if(qTList.size() > 0){
                table3.setWidthPercentage(100); //Width 100%

                String SerNo = Integer.toString(sNo);

                QuaterTransaction qT = qTList.get(i);
                PdfPCell table3cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                table3cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3cell1.setPaddingBottom(5);
                table3cell1.setPaddingTop(5);

                PdfPCell cell2 = new PdfPCell(new Paragraph(qT.getDdo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setPaddingBottom(5);
                cell2.setPaddingTop(5);

                PdfPCell cell3 = new PdfPCell(new Paragraph(qT.getEmployeeCode(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setPaddingBottom(5);
                cell3.setPaddingTop(5);

                PdfPCell cell4 = new PdfPCell(new Paragraph(qT.getEmployeeName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setPaddingBottom(5);
                cell4.setPaddingTop(5);

                PdfPCell cell5 = null;
                dateInString2 = qT.getAllowcationDate();
                try {
                    date2 = formatter.parse(dateInString2);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date.compareTo(date2) * date2.compareTo(date1) > 0) {
                    cell5 = new PdfPCell(new Paragraph(dateInString2, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell5.setPaddingBottom(5);
                    cell5.setPaddingTop(5);
                }
                PdfPCell cell6 = null;
                dateInString5 = qT.getLeaveDate();
                if(dateInString5 != ""){
                try {
                    date5 = formatter1.parse(dateInString5);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                
                if (date3.compareTo(date5) * date5.compareTo(date4) > 0) {
                    cell6 = new PdfPCell(new Paragraph(dateInString5, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell6.setPaddingBottom(5);
                    cell6.setPaddingTop(5);
                }
                }

                table3.addCell(table3cell1);
                table3.addCell(cell2);
                table3.addCell(cell3);
                table3.addCell(cell4);
                table3.addCell(cell5);
                table3.addCell(cell6);
                sNo++;
                }
            }
            outercell.addElement(table3);
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

    private List<QuaterTransaction> getDDO(List<QuaterTransaction> employeeList) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDdo())) {
                    employeeList.get(i).setDdo(entry.getValue());
                }
            }
        }
        return employeeList;
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
