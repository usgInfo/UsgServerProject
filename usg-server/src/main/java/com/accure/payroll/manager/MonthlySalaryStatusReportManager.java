/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.FinancialYear;
import com.accure.payroll.dto.MonthlySalaryStatus;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.getMonthString;
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
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author upendra
 */
public class MonthlySalaryStatusReportManager {

    public ByteArrayOutputStream mothlySalaryStatusStatement(String month, String year, String path, String fin) throws DocumentException, FileNotFoundException, Exception {
        Document document = new Document();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);

        // Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
        Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        document.open();

       DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE);

        DBObject groupFields = new BasicDBObject("_id", "$ddo");
        groupFields.put("count", new BasicDBObject("$sum", 1));
        groupFields.put("salary", new BasicDBObject("$sum", "$earnings"));

        DBObject group = new BasicDBObject("$group", groupFields);

        // You can add a sort to order by count descending
        DBObject monthFields = new BasicDBObject("month", Integer.parseInt(month));
        DBObject monthsort = new BasicDBObject("$match", monthFields);

        DBObject yearFields = new BasicDBObject("year", Integer.parseInt(year));
        DBObject yearsort = new BasicDBObject("$match", yearFields);

        AggregationOutput output = collection.aggregate(monthsort, yearsort, group);
        //System.out.println("result" + output.results());
        List<MonthlySalaryStatus> salaryincreList = new ArrayList<MonthlySalaryStatus>();
        for (DBObject doc : output.results()) {
            MonthlySalaryStatus monthlist = new MonthlySalaryStatus();
            monthlist.setDdo(doc.get("_id").toString());
            monthlist.setEmpCount(doc.get("count").toString());
            monthlist.setTotalAmount(doc.get("salary").toString());
            //System.out.println(" monthly Report" + monthlist);

            salaryincreList.add(monthlist);

        }

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(20);

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(70.0f, 70.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColor(BaseColor.WHITE);

        String monthString = getMonthString(Integer.parseInt(month));

        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("Monthly Salary Status Details -  " + monthString + " " + year, FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK))));
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

        float[] eightcolumn = {1f, 2f, 2f, 2f, 2f, 2f, 2f, 2f};
        float[] sevencolumn = {1f, 2f, 1f, 1f, 2f, 1f};
        //Heads
        PdfPTable table3 = new PdfPTable(6); // 3 columns.
        table3.setWidthPercentage(100); //Width 100%
        table3.setWidths(sevencolumn);

        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(5);
        table3cella.setPaddingTop(5);

        PdfPCell table3cellc = new PdfPCell(new Paragraph("DDO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(5);
        table3cellc.setPaddingTop(5);

        PdfPCell table3celle = new PdfPCell(new Paragraph("Total Employee", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celle.setPaddingBottom(5);
        table3celle.setPaddingTop(5);

        PdfPCell table3cellf = new PdfPCell(new Paragraph("Salary Processed", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellf.setPaddingBottom(5);
        table3cellf.setPaddingTop(5);

        PdfPCell table3cellg = new PdfPCell(new Paragraph("Amount Paid", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellg.setPaddingBottom(5);
        table3cellg.setPaddingTop(5);

        PdfPCell table3cellh = new PdfPCell(new Paragraph("Pending", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table3cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellh.setPaddingBottom(5);
        table3cellh.setPaddingTop(5);

        table3.addCell(table3cella);
        table3.addCell(table3cellc);
        table3.addCell(table3celle);
        table3.addCell(table3cellf);
        table3.addCell(table3cellg);
        table3.addCell(table3cellh);

        int sNo = 1;
        int totalempCount = 0;
        int totalSalaryProcessedCount = 0;
        Double totalAmountPaidCount = 0.0;
        int totalPendingCount = 0;

        //get the data from employee
        for (MonthlySalaryStatus key : salaryincreList) {

            String ddo = key.getDdo();
            int salaryProcessed = Integer.parseInt(key.getEmpCount());
            totalSalaryProcessedCount = totalSalaryProcessedCount + salaryProcessed;
            String totalSalaryProcessed = Integer.toString(salaryProcessed);
            String totalSalary = key.getTotalAmount().replaceAll("", "");

            Double totalPaidAmount = Double.parseDouble(totalSalary);
            String totlStr = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalPaidAmount);
            totalAmountPaidCount = totalAmountPaidCount + totalPaidAmount;
            HashMap<String, String> empCondition = new HashMap<String, String>();
            empCondition.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

            empCondition.put("ddo", ddo);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, empCondition);
            List<Employee> empList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
            }.getType());
            String totalEmpStr = "";
            String salaryPendingStr = "";

            int total_employees = empList.size();
            totalempCount = totalempCount + total_employees;
            totalEmpStr = Integer.toString(total_employees);
            int salary_pending = total_employees - salaryProcessed;
            totalPendingCount = totalPendingCount + salary_pending;
            salaryPendingStr = Integer.toString(salary_pending);
            //System.out.println("EmpSize:" + empList.size());

            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);
            table5cell1.setPaddingTop(5);

            String ddoName = new Gson().toJson(ddo);
            ddoName = ddoName.replace("\"", "");
            String ddodata = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddoName);
            List<DDO> ddolist = new Gson().fromJson(ddodata, new TypeToken<List<DDO>>() {
            }.getType());
            DDO ddoval = ddolist.get(0);
            PdfPCell cell3 = new PdfPCell(new Paragraph(ddoval.getDdoName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);
            cell3.setPaddingTop(5);

            PdfPCell cell5 = new PdfPCell(new Paragraph(totalEmpStr, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setPaddingBottom(5);
            cell5.setPaddingTop(5);
            PdfPCell cell6 = new PdfPCell(new Paragraph(totalSalaryProcessed, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setPaddingBottom(5);
            cell6.setPaddingTop(5);
            PdfPCell cell7 = new PdfPCell(new Paragraph(totlStr, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell7.setPaddingBottom(5);
            cell7.setPaddingTop(5);

            PdfPCell cell8 = new PdfPCell(new Paragraph(salaryPendingStr, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell8.setPaddingBottom(5);
            cell8.setPaddingTop(5);

            table3.addCell(table5cell1);
            table3.addCell(cell3);
            table3.addCell(cell5);
            table3.addCell(cell6);
            table3.addCell(cell7);
            table3.addCell(cell8);

            sNo++;

        }
        outercell.addElement(table3);

        PdfPTable totalTable = new PdfPTable(6); // 3 columns.
        totalTable.setWidthPercentage(100); //Width 100%
        totalTable.setWidths(sevencolumn);

        PdfPCell table6Empty = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
        table6Empty.setBorderColor(BaseColor.WHITE);
        table6Empty.setHorizontalAlignment(Element.ALIGN_CENTER);
        table6Empty.setPaddingBottom(8f);
        table6Empty.setBorderWidthTop(1);
        table6Empty.setBorderColorTop(BaseColor.BLACK);
        table6Empty.setBorderWidthBottom(1);
        table6Empty.setBorderColorBottom(BaseColor.BLACK);

        PdfPCell table6celld = new PdfPCell(new Paragraph("Grand Total", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table6celld.setBorderColor(BaseColor.WHITE);
        table6celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table6celld.setPaddingBottom(8f);
        table6celld.setBorderWidthTop(1);
        table6celld.setBorderColorTop(BaseColor.BLACK);
        table6celld.setBorderWidthTop(1);
        table6celld.setBorderColorTop(BaseColor.BLACK);
        table6celld.setBorderWidthBottom(1);
        table6celld.setBorderColorBottom(BaseColor.BLACK);

        PdfPCell table6celle = new PdfPCell(new Paragraph(Integer.toString(totalempCount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
        table6celle.setBorderColor(BaseColor.WHITE);
        table6celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table6celle.setPaddingBottom(8f);
        table6celle.setBorderWidthTop(1);
        table6celle.setBorderColorTop(BaseColor.BLACK);
        table6celle.setBorderWidthBottom(1);
        table6celle.setBorderColorBottom(BaseColor.BLACK);

        PdfPCell table6cellf = new PdfPCell(new Paragraph(Integer.toString(totalSalaryProcessedCount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
        table6cellf.setBorderColor(BaseColor.WHITE);
        table6cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table6cellf.setPaddingBottom(8f);
        table6cellf.setBorderWidthTop(1);
        table6cellf.setBorderColorTop(BaseColor.BLACK);
        table6cellf.setBorderWidthBottom(1);
        table6cellf.setBorderColorBottom(BaseColor.BLACK);

        String totlStr = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalAmountPaidCount);
        PdfPCell table6cellg = new PdfPCell(new Paragraph(totlStr, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
        table6cellg.setBorderColor(BaseColor.WHITE);
        table6cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table6cellg.setPaddingBottom(8f);
        table6cellg.setBorderWidthTop(1);
        table6cellg.setBorderColorTop(BaseColor.BLACK);
        table6cellg.setBorderWidthBottom(1);
        table6cellg.setBorderColorBottom(BaseColor.BLACK);

        PdfPCell table6cellh = new PdfPCell(new Paragraph(Integer.toString(totalPendingCount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
        table6cellh.setBorderColor(BaseColor.WHITE);
        table6cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
        table6cellh.setPaddingBottom(8f);
        table6cellh.setBorderWidthTop(1);
        table6cellh.setBorderColorTop(BaseColor.BLACK);
        table6cellh.setBorderWidthBottom(1);
        table6cellh.setBorderColorBottom(BaseColor.BLACK);

        totalTable.addCell(table6Empty);
        totalTable.addCell(table6celld);
        totalTable.addCell(table6celle);
        totalTable.addCell(table6cellf);
        totalTable.addCell(table6cellg);
        totalTable.addCell(table6cellh);
        outercell.addElement(totalTable);

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
        //     //System.out.println("final resule" + new MonthlySalaryStatusReportManager().mothlySalaryStatusStatement("9", "2016"));
    }
}
