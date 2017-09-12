/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.FinancialYear;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.LoanPayment;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
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
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author upendra
 */
public class DeductionReportManager {

    public String getDeductionHeadsforDeduction(String deduction) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("deductionType", deduction);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> deductionList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        return new Gson().toJson(deductionList);
    }

    public String getDedHeadsforLoanAdvanced(String deduction, String empCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("deductionType", deduction);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> deductionList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        ArrayList<SalaryHead> list1 = new ArrayList<SalaryHead>();
        if (result != null) {
            for (SalaryHead salaryHead : deductionList) {
                String LoanTypeId = ((LinkedTreeMap<String, String>) salaryHead.getId()).get("$oid");
                if (getDedHeadFromLoan(empCode, LoanTypeId)) {
                    list1.add(salaryHead);
                }
            }
        }
        return new Gson().toJson(list1);
    }

    public boolean getDedHeadFromLoan(String empCode, String LoanTypeId) throws Exception {
        {
            boolean status = false;
            HashMap<String, String> conditionMap1 = new HashMap<String, String>();
            conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap1.put("loanType", LoanTypeId);
            conditionMap1.put("empCode", empCode);
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, conditionMap1);
            if (result1 != null) {
                status = true;
            }
            return status;
        }
    }

    public ByteArrayOutputStream deductionReport(String month, String year, String empdata, String deductionType, String deductionHead, String path, String fin) throws DocumentException, FileNotFoundException, Exception {
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
        if (employeeSearch.getLocation() != null || !employeeSearch.getLocation().equalsIgnoreCase("null") || !employeeSearch.getLocation().equalsIgnoreCase("")) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", employeeSearch.getLocation()));

        }
        if (!employeeSearch.getDepartment().equalsIgnoreCase("null") || !employeeSearch.getDepartment().equalsIgnoreCase("")) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", employeeSearch.getDepartment()));

        }
        if (!employeeSearch.getDesignation().equalsIgnoreCase("null") || !employeeSearch.getDesignation().equalsIgnoreCase("")) {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", employeeSearch.getDesignation()));

        }
        if (!employeeSearch.getNatureType().equalsIgnoreCase("null") || !employeeSearch.getNatureType().equalsIgnoreCase("")) {
            regexQuery.put("natureType",
                    new BasicDBObject("$regex", employeeSearch.getNatureType()));

        }
//        if (!employeeSearch.getFundType().equalsIgnoreCase("null") || !employeeSearch.getFundType().equalsIgnoreCase("")) {
//            regexQuery.put("fundType",
//                    new BasicDBObject("$regex", employeeSearch.getFundType()));
//
//        }
        if (!employeeSearch.getEmployeeName().equalsIgnoreCase("null") || !employeeSearch.getEmployeeName().equalsIgnoreCase("")) {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", employeeSearch.getEmployeeName()));

        }
        if (!employeeSearch.getEmployeeCode().equalsIgnoreCase("null") || !employeeSearch.getEmployeeCode().equalsIgnoreCase("")) {
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
        try {
            while (cursor.hasNext()) {
                DBObject ob = cursor.next();
                Type type1 = new TypeToken<AutoSalaryProcess>() {
                }.getType();
                try {
                    AutoSalaryProcess em = new Gson().fromJson(ob.toString(), type1);
                    employeeList.add(em);
                } catch (Exception ee) {
                }
            }
        } catch (Exception e) {

        }
        List<AutoSalaryProcess> finalList = new ArrayList<AutoSalaryProcess>();
        try {
            for (AutoSalaryProcess key : employeeList) {
                if (deductionType.equalsIgnoreCase("Others")) {
                    if (key.getDeductionsInfo().isIsDeductionHeads()) {
                        for (int i = 0; i < key.getDeductionsInfo().getDeductionHeads().size(); i++) {
                            try {
                                if (((LinkedTreeMap<String, String>) key.getDeductionsInfo().getDeductionHeads().get(i).getDescriptionInfo().getId()).get("$oid").equals(deductionHead)) {
                                    finalList.add(key);
                                }
                            } catch (Exception ee) {
                            }

                        }

                    }
                } else if (deductionType.equalsIgnoreCase("Insurance")) {
                    if (key.getDeductionsInfo().isIsInsurance()) {
                        for (int i = 0; i < key.getDeductionsInfo().getInsurance().size(); i++) {
                        try {
                            if (key.getDeductionsInfo().getInsurance().get(i).getInscName().equals(deductionHead)) {
                                finalList.add(key);
                            }
                        } catch (Exception ee) {
                        }
                    }
                        }
                } else if (deductionType.equalsIgnoreCase("Loan")) {
                    if (key.getDeductionsInfo().isIsLoan()) {
                        for (int i = 0; i < key.getDeductionsInfo().getLoan().size(); i++) {
                            try {
                                if (key.getDeductionsInfo().getLoan().get(i).getLoanType().equals(deductionHead)) {
                                    finalList.add(key);
                                }
                            } catch (Exception ee) {
                            }
                        }

                    }
                } 
                 else if (deductionType.equalsIgnoreCase("Advance")) {
                    if (key.getDeductionsInfo().isIsLoan()) {
                        for (int i = 0; i < key.getDeductionsInfo().getLoan().size(); i++) {
                            try {
                                if (key.getDeductionsInfo().getLoan().get(i).getLoanType().equals(deductionHead)) {
                                    finalList.add(key);
                                }
                            } catch (Exception ee) {
                            }
                        }

                    }
                }else if (deductionType.equalsIgnoreCase("IncomeTax")) {
                    if (key.getDeductionsInfo().isIsIncomeTax()) {
                        finalList.add(key);
                    }
                }
            }
        } catch (Exception e) {

        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, bos);

        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
        Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        document.open();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100.0f);

        PdfPCell outercell = new PdfPCell();

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});

        PdfPCell imagecell = new PdfPCell();

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
        headerPhrase.add(new Phrase(new Chunk("Deduction Details of " + deductionType + " - " + monthString + " " + year, FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK))));
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
        timecell.setPaddingTop(2.0f);
        timecell.setBorder(Rectangle.NO_BORDER);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);

        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        header.setSpacingAfter(10);

        outercell.addElement(header);

        //
        float[] fourcolumnwidth = {1f, 3f, 3f, 3f, 3f};
        PdfPTable menu = new PdfPTable(5); // 3 columns.
        menu.setWidthPercentage(100); //Width 100%
        menu.setWidths(fourcolumnwidth);

        PdfPCell menucella = new PdfPCell(new Paragraph("S.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));

        menucella.setHorizontalAlignment(Element.ALIGN_CENTER);
        menucella.setPaddingBottom(5);

        PdfPCell menucellb = new PdfPCell(new Paragraph("EMPLOYEE NAME", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
        menucellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        menucellb.setPaddingBottom(5);

        PdfPCell menucelld = new PdfPCell(new Paragraph("PAN NO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
        menucelld.setHorizontalAlignment(Element.ALIGN_CENTER);
        menucelld.setPaddingBottom(5);

        PdfPCell menucelle = null;
        if (deductionHead.equalsIgnoreCase("IT")) {
            menucelle = new PdfPCell(new Paragraph("INCOME TAX", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            menucelle.setHorizontalAlignment(Element.ALIGN_CENTER);
            menucelle.setPaddingBottom(5);
        } else {
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, deductionHead);
            List<SalaryHead> list = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
            }.getType());

            SalaryHead salList = list.get(0);

            menucelle = new PdfPCell(new Paragraph(salList.getShortDescription(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            menucelle.setHorizontalAlignment(Element.ALIGN_CENTER);
            menucelle.setPaddingBottom(5);
        }
        PdfPCell menucellg = new PdfPCell(new Paragraph("TOTAL", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
        menucellg.setHorizontalAlignment(Element.ALIGN_CENTER);
        menucellg.setPaddingBottom(5);

        menu.addCell(menucella);
        menu.addCell(menucellb);
        menu.addCell(menucelld);
        menu.addCell(menucelle);
        menu.addCell(menucellg);
        outercell.addElement(menu);

        int sNo = 1;
        PdfPTable emplist = new PdfPTable(5); // 7 columns.
        emplist.setWidthPercentage(100); //Width 100%
        emplist.setWidths(fourcolumnwidth);
        double gtotal = 0.0;
        for (AutoSalaryProcess ausalryList : finalList) {
            emplist = new PdfPTable(5); // 7 columns.
            emplist.setWidthPercentage(100); //Width 100%
            emplist.setWidths(fourcolumnwidth);
            String SerNo = Integer.toString(sNo);

            PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cell1.setPaddingBottom(5);

            PdfPCell cell2 = new PdfPCell(new Paragraph(ausalryList.getEmployeeName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setPaddingBottom(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(ausalryList.getPanNo(), font3));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setPaddingBottom(5);

            PdfPCell cell4 = new PdfPCell(new Paragraph("", font3));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setPaddingBottom(5);

            double itamount = 0.0;
            PdfPCell cell5 = null;

            if (deductionType.equalsIgnoreCase("Others")) {
                for (int i = 0; i < ausalryList.getDeductionsInfo().getDeductionHeads().size(); i++) {
                    if (((LinkedTreeMap<String, String>) ausalryList.getDeductionsInfo().getDeductionHeads().get(i).getDescriptionInfo().getId()).get("$oid").equals(deductionHead)) {
                        itamount = ausalryList.getDeductionsInfo().getDeductionHeads().get(i).getAmount();
                        cell5 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(ausalryList.getDeductionsInfo().getDeductionHeads().get(i).getAmount()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell5.setPaddingBottom(5);
                    }
                }
            } else if (deductionType.equalsIgnoreCase("Insurance")) {
                    if (ausalryList.getDeductionsInfo().isIsInsurance()) {
                        for (int i = 0; i < ausalryList.getDeductionsInfo().getInsurance().size(); i++) {
                        try {
                            if (ausalryList.getDeductionsInfo().getInsurance().get(i).getInscName().equals(deductionHead)) {
                                  itamount = ausalryList.getDeductionsInfo().getInsurance().get(i).getInstallmentAmount();
                            }
                        } catch (Exception ee) {
                        }
                    }
                 
                    cell5 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(itamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                    cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell5.setPaddingBottom(5);
                }

            } else if (deductionType.equalsIgnoreCase("IncomeTax")) {
//                itamount = ausalryList.getDeductionsInfo().getIncomeTax().getIt();
                itamount = ausalryList.getDeductionsInfo().getIncomeTax().getTotal();
                cell5 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(itamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setPaddingBottom(5);
            } else if (deductionType.equalsIgnoreCase("Loan")) {
                for (int i = 0; i < ausalryList.getDeductionsInfo().getLoan().size(); i++) {
                    if ((ausalryList.getDeductionsInfo().getLoan().get(i).getLoanType()).equals(deductionHead)) {
                        itamount = ausalryList.getDeductionsInfo().getLoan().get(i).getPaidAmount();
                        cell5 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(itamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell5.setPaddingBottom(5);
                    }
                }

            }
            else if (deductionType.equalsIgnoreCase("Advance")) {
                for (int i = 0; i < ausalryList.getDeductionsInfo().getLoan().size(); i++) {
                    if ((ausalryList.getDeductionsInfo().getLoan().get(i).getLoanType()).equals(deductionHead)) {
                        itamount = ausalryList.getDeductionsInfo().getLoan().get(i).getPaidAmount();
                        cell5 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(itamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell5.setPaddingBottom(5);
                    }
                }

            }
            gtotal = gtotal + itamount;
            PdfPCell cell7 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(itamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell7.setPaddingBottom(5);

            emplist.addCell(table5cell1);
            emplist.addCell(cell2);
            emplist.addCell(cell3);
            emplist.addCell(cell5);
            emplist.addCell(cell7);
            outercell.addElement(emplist);
            sNo++;
        }

        //Last Line
        PdfPTable total = new PdfPTable(5); // 3 columns.
        total.setWidthPercentage(100); //Width 100%
        total.setWidths(fourcolumnwidth);

        PdfPCell totalcella = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        totalcella.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalcella.setPaddingBottom(5);
        totalcella.setBorder(Rectangle.NO_BORDER);
        totalcella.setBorderColor(BaseColor.WHITE);

        PdfPCell totalcellc = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        totalcellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalcellc.setBorder(Rectangle.NO_BORDER);
        totalcellc.setBorderColor(BaseColor.WHITE);

        totalcellc.setPaddingBottom(5);

        PdfPCell totalcelld = new PdfPCell(new Paragraph("Grand Total", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        totalcelld.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalcelld.setPaddingBottom(5);
        totalcelld.setBorder(Rectangle.NO_BORDER);
        totalcelld.setBorderColor(BaseColor.WHITE);

        PdfPCell totalcelle = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(gtotal), FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        totalcelle.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalcelle.setPaddingBottom(5);
        totalcelle.setBorder(Rectangle.NO_BORDER);
        totalcelle.setBorderColor(BaseColor.WHITE);

        PdfPCell totalcellg = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(gtotal), FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
        totalcellg.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalcellg.setPaddingBottom(5);
        totalcellg.setBorder(Rectangle.NO_BORDER);
        totalcellg.setBorderColor(BaseColor.WHITE);
        total.addCell(totalcella);

        total.addCell(totalcellc);
        total.addCell(totalcelld);
        total.addCell(totalcelle);
        total.addCell(totalcellg);
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
//        //System.out.println("final result" + new DeductionReportManager().deductionReport("9", "2016", "57ea0649e4b0f97866d150b0", "Others", "57ecdc6ae4b0fab8c93bcf75", ""));
//    }
}
