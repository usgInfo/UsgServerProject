/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.finance.manager.VoucherDatesSortBy;
import com.accure.leave.dto.LocationWiseHolidayMaster;
import com.accure.leave.dto.LocationWiseHolidayMasterList;
import com.accure.usg.common.manager.RestClient;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author accure
 */
public class LocationWiseHolidayReportManager {

    public ByteArrayOutputStream locationWiseHolidayPdfStatement(String id, String location, String year, String path, String financialyearstart, String financialyearEnd) throws DocumentException, FileNotFoundException, Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        RestClient aql = new RestClient();
        String Active = "\"Active\"";
//
        String lwhmTable = ApplicationConstants.USG_DB1 + ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER + "`";

        String locationWiseHolidaySearchQuery = "select lhm._id,lhm.locationWiseHolidayFormList,lhm.locationWiseHoliday,lhm.year from " + lwhmTable + ""
                + " as lhm where lhm._id= OID(\"" + id + "\") and lhm.status=\"Active\"";

        String locationWiseHolidayOutput = aql.getRestData(ApplicationConstants.END_POINT, locationWiseHolidaySearchQuery);

        List<LocationWiseHolidayMasterList> lwhmList = null;
        if (locationWiseHolidayOutput != null && !locationWiseHolidayOutput.isEmpty() && !locationWiseHolidayOutput.equals("[]")) {
            lwhmList = new Gson().fromJson(locationWiseHolidayOutput, new TypeToken< ArrayList<LocationWiseHolidayMasterList>>() {
            }.getType());
        }
//        List<LocationWiseHolidayMasterList> lwhmFinalList = new ArrayList<LocationWiseHolidayMasterList>();
        List<LocationWiseHolidayMaster> lwhmFinalList = new ArrayList<LocationWiseHolidayMaster>();
        if (locationWiseHolidayOutput != null && !locationWiseHolidayOutput.isEmpty() && !locationWiseHolidayOutput.equals("[]")) {
            int lwho = 0;
            for (LocationWiseHolidayMasterList lwhm : lwhmList) {
                for (lwho = 0; lwho < lwhm.getLocationWiseHolidayFormList().size(); lwho++) {
//                            lwhm.getCommonHoliday();
//                            lwhm.getHolidayType();
//                            lwhm.getLocation();
//                            lwhm.getYear();
//                    lwhm.setCommonHolidayReport(lwhm.getLocationWiseHolidayFormList().get(l2).getCommonHoliday());
//                    lwhm.setHolidayTypeReport(lwhm.getLocationWiseHolidayFormList().get(l2).getHolidayType());
//                    lwhm.setLocationReport(lwhm.getLocationWiseHolidayFormList().get(l2).getLocation());
////                    lwhm.setYearReport(lwhm.getLocationWiseHolidayFormList().get(l2).get);
//                    lwhm.setFromDateReport(lwhm.getLocationWiseHolidayFormList().get(l2).getFromDate());
//                    lwhm.setFromDateInMilliSecondReport(saveInMilliSecond(lwhm.getLocationWiseHolidayFormList().get(l2).getFromDate()));
                    lwhm.getLocationWiseHolidayFormList().get(lwho).setFromDateInMilliSecond(saveInMilliSecond(lwhm.getLocationWiseHolidayFormList().get(lwho).getFromDate()));
//                    lwhm.setToDateReport(lwhm.getLocationWiseHolidayFormList().get(l2).getToDate());
                    lwhmFinalList.add(lwhm.getLocationWiseHolidayFormList().get(lwho));
//                    lwhmFinalList.add(lwhm);
                }
            }
        }
        
List<LocationWiseHolidayMaster> merged = new ArrayList(lwhmFinalList);
if (merged != null) {
            Collections.sort(merged, new LocationWiseHolidaysSortBy());
        }
        Document document = new Document();
        PdfWriter.getInstance(document, bos);

        Font font1 = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
        Font font3 = new Font(Font.FontFamily.HELVETICA, 9);

        float[] sevencolumnwidth = {1f, 1f, 1f, 1f, 1f, 1f, 1f};
        float[] onecolumnwidth = {1f};
        float[] twocolumnwidth = {1f, 1f};

        document.open();
        PdfPTable outerTable = new PdfPTable(1);

        outerTable.setTotalWidth(555f);
        outerTable.setLockedWidth(true);

        outerTable.setWidthPercentage(100);

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{20, 200, 40});

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(50.0f, 50.0f);

        imagecell.addElement(image1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColorBottom(BaseColor.BLACK);

        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
        headercell.setPaddingBottom(6f);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);
//financial year
//        String financialYearJson = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
//        FinancialYear financialYearJson =new FinancialYearManager().fetchActiveLeaveFinancialYear();
//        System.out.println("Financial Year for Location wise" + fy);
        /*List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
        }.getType());
        FinancialYear fyObj = fyList.get(0);
        String fyId = fyObj.getYear();
        String strmin = fyId.substring(2, 4);
        int instrmin = Integer.parseInt(strmin);
        instrmin = instrmin + 1;
        strmin = Integer.toString(instrmin);*/
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = new Date();
        DateFormat dateFormatTime = new SimpleDateFormat("HH : mm : ss");

        Phrase timePhrase = new Phrase(new Chunk("Date :" + dateFormat.format(date1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time:" + dateFormatTime.format(date1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("\n")));
//        timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
//        timePhrase.add(new Phrase(new Chunk(fin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));

        PdfPCell timecell = new PdfPCell(timePhrase);
        timecell.setBorderWidthBottom(1);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);

        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        RoundRectangle roundRectangle = new RoundRectangle();
        PdfPCell outercell = new PdfPCell();
        outercell.setCellEvent(roundRectangle);
        outercell.setBorder(Rectangle.NO_BORDER);
        outercell.setPadding(8);
        outercell.addElement(header);

        outerTable.addCell(outercell);
        document.add(outerTable);

        PdfPTable headertable = new PdfPTable(new float[]{5});
        PdfPCell cell = new PdfPCell(new Phrase("Location Wise Holiday Master", font1));
        cell.setColspan(4);
        cell.setRowspan(5);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(8f);
        cell.setPaddingLeft(120f);
        cell.setBorder(Rectangle.NO_BORDER);

        headertable.addCell(cell);
        document.add(headertable);

        PdfPTable table1 = new PdfPTable(7);
        table1.setWidthPercentage(100); //Width 100%
        table1.setWidths(sevencolumnwidth);

        PdfPCell table1cella = new PdfPCell(new Paragraph("S.No", font2));
        table1cella.setBorderColor(BaseColor.BLACK);
        table1cella.setBackgroundColor(BaseColor.WHITE);
        table1cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1cella.setBorder(Rectangle.NO_BORDER);
        table1cella.setBorderWidthTop(1f);
        table1cella.setBorderWidthBottom(1f);
        table1cella.setBorderWidthLeft(1f);

        PdfPCell table1cellb = new PdfPCell(new Paragraph("Common Holiday", font2));
        table1cellb.setBorderColor(BaseColor.BLACK);
        table1cellb.setBackgroundColor(BaseColor.WHITE);
        table1cellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1cellb.setBorder(Rectangle.NO_BORDER);
        table1cellb.setBorderWidthTop(1f);
        table1cellb.setBorderWidthBottom(1f);
        table1cellb.setBorderWidthLeft(1f);

        PdfPCell table1cellc = new PdfPCell(new Paragraph("Holiday Type", font2));
        table1cellc.setBorderColor(BaseColor.BLACK);
        table1cellc.setBackgroundColor(BaseColor.WHITE);
        table1cellc.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1cellc.setBorder(Rectangle.NO_BORDER);
        table1cellc.setBorderWidthTop(1f);
        table1cellc.setBorderWidthBottom(1f);
        table1cellc.setBorderWidthLeft(1f);

        PdfPCell table1celld = new PdfPCell(new Paragraph("Location", font2));
        table1celld.setBorderColor(BaseColor.BLACK);
        table1celld.setBackgroundColor(BaseColor.WHITE);
        table1celld.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1celld.setBorder(Rectangle.NO_BORDER);
        table1celld.setBorderWidthTop(1f);
        table1celld.setBorderWidthBottom(1f);
        table1celld.setBorderWidthLeft(1f);

        PdfPCell table1celle = new PdfPCell(new Paragraph("Year", font2));
        table1celle.setBorderColor(BaseColor.BLACK);
        table1celle.setBackgroundColor(BaseColor.WHITE);
        table1celle.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1celle.setBorder(Rectangle.NO_BORDER);
        table1celle.setBorderWidthTop(1f);
        table1celle.setBorderWidthBottom(1f);
        table1celle.setBorderWidthLeft(1f);

        PdfPCell table1cellf = new PdfPCell(new Paragraph("From Date", font2));
        table1cellf.setBorderColor(BaseColor.BLACK);
        table1cellf.setBackgroundColor(BaseColor.WHITE);
        table1cellf.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1cellf.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1cellf.setBorder(Rectangle.NO_BORDER);
        table1cellf.setBorderWidthTop(1f);
        table1cellf.setBorderWidthBottom(1f);
        table1cellf.setBorderWidthLeft(1f);

        PdfPCell table1cellg = new PdfPCell(new Paragraph("To Date", font2));
        table1cellg.setBorderColor(BaseColor.BLACK);
        table1cellg.setBackgroundColor(BaseColor.WHITE);
        table1cellg.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1cellg.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1cellg.setBorder(Rectangle.NO_BORDER);
        table1cellg.setBorderWidthTop(1f);
        table1cellg.setBorderWidthBottom(1f);
        table1cellg.setBorderWidthLeft(1f);
        table1cellg.setBorderWidthRight(1f);

        table1.addCell(table1cella);
        table1.addCell(table1cellb);
        table1.addCell(table1cellc);
        table1.addCell(table1celld);
        table1.addCell(table1celle);
        table1.addCell(table1cellf);
        table1.addCell(table1cellg);
        document.add(table1);

        int sNo = 1;

        PdfPTable table2 = new PdfPTable(7);
        table2.setWidthPercentage(100); //Width 100%
        table2.setWidths(sevencolumnwidth);
        for (int i = 0; i < lwhmFinalList.size(); i++) {

            PdfPCell table2cella = new PdfPCell(new Paragraph(Integer.toString(sNo), font3));
            table2cella.setBorderColor(BaseColor.BLACK);
            table2cella.setBackgroundColor(BaseColor.WHITE);
            table2cella.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2cella.setBorder(Rectangle.NO_BORDER);
//            table2cella.setBorderWidthTop(1f);
            table2cella.setBorderWidthBottom(1f);
            table2cella.setBorderWidthLeft(1f);

            PdfPCell table2cellb = new PdfPCell(new Paragraph(merged.get(i).getCommonHoliday(), font3));
            table2cellb.setBorderColor(BaseColor.BLACK);
            table2cellb.setBackgroundColor(BaseColor.WHITE);
            table2cellb.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2cellb.setBorder(Rectangle.NO_BORDER);
//            table2cellb.setBorderWidthTop(1f);
            table2cellb.setBorderWidthBottom(1f);
            table2cellb.setBorderWidthLeft(1f);

            PdfPCell table2cellc = new PdfPCell(new Paragraph(merged.get(i).getHolidayType(), font3));
            table2cellc.setBorderColor(BaseColor.BLACK);
            table2cellc.setBackgroundColor(BaseColor.WHITE);
            table2cellc.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2cellc.setBorder(Rectangle.NO_BORDER);
//            table2cellc.setBorderWidthTop(1f);
            table2cellc.setBorderWidthBottom(1f);
            table2cellc.setBorderWidthLeft(1f);

            PdfPCell table2celld = new PdfPCell(new Paragraph(merged.get(i).getLocation(), font3));
            table2celld.setBorderColor(BaseColor.BLACK);
            table2celld.setBackgroundColor(BaseColor.WHITE);
            table2celld.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2celld.setBorder(Rectangle.NO_BORDER);
//            table2celld.setBorderWidthTop(1f);
            table2celld.setBorderWidthBottom(1f);
            table2celld.setBorderWidthLeft(1f);

            String locVal = Integer.toString(merged.get(i).getYear());
//             String locVal = Integer.toString(lwhmFinalList.get(i).getYear);
            PdfPCell table2celle = new PdfPCell(new Paragraph(locVal, font3));
            table2celle.setBorderColor(BaseColor.BLACK);
            table2celle.setBackgroundColor(BaseColor.WHITE);
            table2celle.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2celle.setBorder(Rectangle.NO_BORDER);
//            table2celle.setBorderWidthTop(1f);
            table2celle.setBorderWidthBottom(1f);
            table2celle.setBorderWidthLeft(1f);

            PdfPCell table2cellf = null;
            String fDate = merged.get(i).getFromDate();
//            if (!fDate.isEmpty() || !fDate.equalsIgnoreCase("")) {
                table2cellf = new PdfPCell(new Paragraph(merged.get(i).getFromDate(), font3));
                table2cellf.setBorderColor(BaseColor.BLACK);
                table2cellf.setBackgroundColor(BaseColor.WHITE);
                table2cellf.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2cellf.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table2cellf.setBorder(Rectangle.NO_BORDER);
//                table2cellf.setBorderWidthTop(1f);
                table2cellf.setBorderWidthBottom(1f);
                table2cellf.setBorderWidthLeft(1f);
//            }

            PdfPCell table2cellg = new PdfPCell(new Paragraph(merged.get(i).getToDate(), font3));
            table2cellg.setBorderColor(BaseColor.BLACK);
            table2cellg.setBackgroundColor(BaseColor.WHITE);
            table2cellg.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2cellg.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2cellg.setBorder(Rectangle.NO_BORDER);
//            table2cellg.setBorderWidthTop(1f);
            table2cellg.setBorderWidthBottom(1f);
            table2cellg.setBorderWidthLeft(1f);
            table2cellg.setBorderWidthRight(1f);

            table2.addCell(table2cella);
            table2.addCell(table2cellb);
            table2.addCell(table2cellc);
            table2.addCell(table2celld);
            table2.addCell(table2celle);
            table2.addCell(table2cellf);
            table2.addCell(table2cellg);
            sNo++;
//        document.add(table2);
        }
        document.add(table2);
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
    
    public Long saveInMilliSecond(String str) throws ParseException {
   
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);
        return date.getTime();
    }

}
