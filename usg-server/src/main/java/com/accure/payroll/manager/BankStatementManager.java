/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.FinancialYear;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.usg.common.manager.ConvertMoneyToNumberMain;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author upendra
 */
public class BankStatementManager {

    public ByteArrayOutputStream bankStatementReport(String month, String year, String empdata, String path, String bank,String fin) throws DocumentException, FileNotFoundException, Exception {

        Type type = new TypeToken<AutoSalaryProcess>() {
        }.getType();
        AutoSalaryProcess employeeSearch = new Gson().fromJson(empdata, type);

        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        if (employeeSearch.getDdo() != "") {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", employeeSearch.getDdo()));
        }

        if (employeeSearch.getLocation() != "") {
            regexQuery.put("location",
                    new BasicDBObject("$regex", employeeSearch.getLocation()));

        }
        if (employeeSearch.getDepartment() != "") {
            regexQuery.put("department",
                    new BasicDBObject("$regex", employeeSearch.getDepartment()));

        }
        if (employeeSearch.getDesignation() != "") {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", employeeSearch.getDesignation()));

        }
        if (employeeSearch.getNatureType() != "") {
            regexQuery.put("natureType",
                    new BasicDBObject("$regex", employeeSearch.getNatureType()));

        }
        if (employeeSearch.getFundType() != "") {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", employeeSearch.getFundType()));

        }
        if (employeeSearch.getEmployeeName() != "") {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", employeeSearch.getEmployeeName()));

        }
        if (employeeSearch.getEmployeeCode() != "") {
            regexQuery.put("employeeCode",
                    new BasicDBObject("$regex", employeeSearch.getEmployeeCode()));

        }
        int chmonth = Integer.parseInt(month);
        int chyear = Integer.parseInt(year);
        if (month != null) {
            regexQuery.put("month",
                    new BasicDBObject("$eq", chmonth));

        }
        if (year != null) {
            regexQuery.put("year",
                    new BasicDBObject("$eq", chyear));

        }

        DBCursor cursor = collection.find(regexQuery);
        List<AutoSalaryProcess> employeeList = new ArrayList<AutoSalaryProcess>();
        while (cursor.hasNext()) {
            DBObject ob = cursor.next();
            Type type1 = new TypeToken<AutoSalaryProcess>() {
            }.getType();
            AutoSalaryProcess em = new Gson().fromJson(ob.toString(), type1);
            employeeList.add(em);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, bos);

        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        document.open();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");

        image1.setAlignment(Image.ALIGN_JUSTIFIED_ALL);
        image1.scaleAbsolute(70.0f, 70.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1f);
        imagecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        imagecell.setBorderColor(BaseColor.BLACK);
        imagecell.setBorderColor(BaseColor.WHITE);

        String conMonth = getMonthString(Integer.parseInt(month));
        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("    Bank  Statement for  Month of - " + conMonth + "-" + year, FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1f);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.BLACK);
        headercell.setBorderColor(BaseColor.WHITE);

        headercell.setPaddingBottom(12f);
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
        timecell.setBorderColor(BaseColor.BLACK);
        timecell.setBorderColor(BaseColor.WHITE);

        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        outercell.addElement(header);

        double banktotalAmount = 0;
        float[] onecolumnwidth = {1f};
        Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

        PdfPTable table1 = new PdfPTable(1); // 3 columns.
        table1.setWidthPercentage(100); //Width 100%
        table1.setWidths(onecolumnwidth);

        PdfPCell cella = new PdfPCell(new Paragraph("To", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        cella.setBorder(Rectangle.NO_BORDER);
        cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cella.setBorderColor(BaseColor.WHITE);

        table1.addCell(cella);

        PdfPCell table2cella = new PdfPCell(new Paragraph("Sr.Branch Manager", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
        table2cella.setBorder(Rectangle.NO_BORDER);
        table2cella.setBorderColor(BaseColor.WHITE);
        table2cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table2cella.setBorderWidthLeft(1f);
        table2cella.setBorderWidthRight(1f);
        table1.addCell(table2cella);

        if (!bank.equals("null")) {
            PdfPCell table3cella = new PdfPCell(new Paragraph(bank, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            table3cella.setBorder(Rectangle.NO_BORDER);
            table3cella.setBorderColor(BaseColor.WHITE);
            table3cella.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table3cella.setBorderWidthLeft(1f);
            table3cella.setBorderWidthRight(1f);
            table1.addCell(table3cella);
        } else {
            PdfPCell table3cella = new PdfPCell(new Paragraph("Bank Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
            table3cella.setBorder(Rectangle.NO_BORDER);
            table3cella.setBorderColor(BaseColor.WHITE);
            table3cella.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table3cella.setBorderWidthLeft(1f);
            table3cella.setBorderWidthRight(1f);
            table1.addCell(table3cella);
        }

        PdfPCell table4cella = new PdfPCell(new Paragraph("Saurashtra University,Rajkot", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
        table4cella.setBorder(Rectangle.NO_BORDER);
        table4cella.setBorderColor(BaseColor.WHITE);
        table4cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        table4cella.setPaddingBottom(12f);
        table4cella.setBorderWidthLeft(1f);
        table4cella.setBorderWidthRight(1f);
        table4cella.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table1.addCell(table4cella);

        PdfPCell table5cella = new PdfPCell(new Paragraph("Sir,", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table5cella.setBorder(Rectangle.NO_BORDER);
        table5cella.setBorderColor(BaseColor.WHITE);
        table5cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        table5cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table5cella.setBorderWidthLeft(1f);
        table5cella.setBorderWidthRight(1f);
        table1.addCell(table5cella);

        
        
        double bankAmount = getTotal(employeeList);
        ConvertMoneyToNumberMain a = new ConvertMoneyToNumberMain();
        String word = a.convertAmountToWord(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(bankAmount).replaceAll(",",""));

        PdfPCell table6cella = new PdfPCell(new Paragraph("  You are requested to transfer sum of Rs." + SalarySlipRportPDFGeneration.roundTwoDecimalPoints(bankAmount) + " (" + word + " Only) to the below bank A/C by Debiting  our A/C.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        table6cella.setBorder(Rectangle.NO_BORDER);
        table6cella.setBorderColor(BaseColor.WHITE);
        table6cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        table6cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table6cella.setPaddingBottom(12f);
        table6cella.setBorderWidthLeft(1f);
        table6cella.setBorderWidthBottom(1f);
        table6cella.setBorderWidthRight(1f);
        table1.addCell(table6cella);
        outercell.addElement(table1);

        float[] fourcolumnwidth = {1f, 3f, 3f, 3f};
        PdfPTable menu = new PdfPTable(4); // 3 columns.
        menu.setWidthPercentage(100); //Width 100%
        menu.setWidths(fourcolumnwidth);

        PdfPCell menucella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        menucella.setHorizontalAlignment(Element.ALIGN_CENTER);
        menucella.setPaddingBottom(5f);
        menucella.setPaddingTop(5f);

        PdfPCell menucellb = new PdfPCell(new Paragraph("Employee Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        menucellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        menucellb.setPaddingBottom(5f);
        menucellb.setPaddingTop(5f);

        PdfPCell menucellc = new PdfPCell(new Paragraph("A/C Number", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        menucellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        menucellc.setPaddingBottom(5f);
        menucellc.setPaddingTop(5f);

        PdfPCell menucelld = new PdfPCell(new Paragraph("Amount", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        menucelld.setHorizontalAlignment(Element.ALIGN_CENTER);
        menucelld.setPaddingBottom(5f);
        menucelld.setPaddingTop(5f);

        menu.addCell(menucella);
        menu.addCell(menucellb);
        menu.addCell(menucellc);
        menu.addCell(menucelld);
        outercell.addElement(menu);

        //list of employees     
        int sNo = 1;
        PdfPTable emplist = new PdfPTable(4); // 3 columns.
        emplist.setWidthPercentage(100); //Width 100%
        emplist.setWidths(fourcolumnwidth);

        for (AutoSalaryProcess key : employeeList) {
            emplist = new PdfPTable(4);
            emplist.setWidthPercentage(100); //Width 100%
            emplist.setWidths(fourcolumnwidth);
            emplist.setWidthPercentage(100);
            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5f);
            table5cell1.setPaddingTop(5f);

            PdfPCell cell2 = new PdfPCell(new Paragraph(key.getEmployeeName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5f);
            cell2.setPaddingTop(5f);

            PdfPCell cell3 = new PdfPCell(new Paragraph(key.getAcnumber(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5f);
            cell2.setPaddingTop(5f);


            banktotalAmount = banktotalAmount + key.getFinalPayment();
            PdfPCell cell4 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(key.getFinalPayment()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5f);
            cell4.setPaddingTop(5f);

            emplist.addCell(table5cell1);
            emplist.addCell(cell2);
            emplist.addCell(cell3);
            emplist.addCell(cell4);
            outercell.addElement(emplist);
            sNo++;
        }

        PdfPTable total = new PdfPTable(4); // 3 columns.
        total.setWidthPercentage(100); //Width 100%
        total.setWidths(fourcolumnwidth);

        PdfPCell totalcella = new PdfPCell(new Paragraph("", font2));
        totalcella.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalcella.setPaddingBottom(5f);
        totalcella.setPaddingTop(5f);

        PdfPCell totalcellb = new PdfPCell(new Paragraph("", font2));
        totalcellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalcellb.setPaddingBottom(5f);
        totalcellb.setPaddingTop(5f);

        PdfPCell totalcellc = new PdfPCell(new Paragraph("Grand Total", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        totalcellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalcellc.setPaddingBottom(5f);
        totalcellc.setPaddingTop(5f);
        PdfPCell totalcelld = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(banktotalAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
        totalcelld.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalcelld.setPaddingBottom(5f);
        totalcelld.setPaddingTop(5f);

        total.addCell(totalcella);
        total.addCell(totalcellb);
        total.addCell(totalcellc);
        total.addCell(totalcelld);
        outercell.addElement(total);

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

    public static double getTotal(List<AutoSalaryProcess> empList) {
        double amount = 0;
        for (AutoSalaryProcess key : empList) {
            amount = amount + key.getFinalPayment();
        }
        return amount;
    }

    public static String getMonthString(int month) {
        String monthString = "";
        switch (month) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
            default:
                monthString = "Invalid month";
                break;
        }
        return monthString;
    }

//    public static void main(String args[]) throws FileNotFoundException, Exception {
//        //System.out.println("final resule" + new BankStatementManager().bankStatementReport("8", "2016", "", ""));
//    }
}
