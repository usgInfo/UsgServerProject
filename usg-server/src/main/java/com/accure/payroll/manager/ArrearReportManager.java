/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.FinancialYear;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.ArrearConfig;
import com.accure.payroll.dto.ArrearProcess;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.Deductions;
import com.accure.payroll.dto.Earnings;
import com.accure.payroll.dto.IncomeTax;
import com.accure.payroll.dto.InsuranceTransactions;
import com.accure.payroll.dto.LoanPayment;

import com.accure.payroll.dto.SalarySlipRegisterReport;
import com.itextpdf.text.pdf.PdfPage;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.getMonthString;
import com.accure.usg.common.manager.DBManager;
import com.google.gson.internal.LinkedTreeMap;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class ArrearReportManager {

    public String searchItFinally(ArrearProcess emp, String sortBy) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.ARREAR_PROCESS_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        BasicDBObject sortQuery = new BasicDBObject();

        if (emp.getEmployeeName() != null) {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", emp.getEmployeeName()));
        }
        if (emp.getEmployeeCode() != null) {
            regexQuery.put("employeeCode",
                    new BasicDBObject("$regex", emp.getEmployeeCode()));
        }
        if (emp.getPayMonth() != 0) {
            regexQuery.put("payMonth",
                    new BasicDBObject(ApplicationConstants.EQUAL_OPERATOR, emp.getPayMonth()));
        }
        if (emp.getPayYear() != 0) {
            regexQuery.put("payYear",
                    new BasicDBObject(ApplicationConstants.EQUAL_OPERATOR, emp.getPayYear()));
        }
        if (emp.getDdo() != null) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", emp.getDdo()));
        }
//       
        if (emp.getDesignation() != null) {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", emp.getDesignation()));
        }
        if (emp.getLocation() != null) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", emp.getLocation()));
        }
        if (emp.getDepartment() != null) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", emp.getDepartment()));
        }
        if (emp.getNatureType() != null) {
            regexQuery.put("natureType",
                    new BasicDBObject("$regex", emp.getNatureType()));
        }
        if (emp.getPostingCity() != null) {
            regexQuery.put("postingCity",
                    new BasicDBObject("$regex", emp.getPostingCity()));
        }

//        if (emp.getLeaveType() != null) {
//            regexQuery.put("leaveType",
//                    new BasicDBObject("$regex", emp.getLeaveType()));
//        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
//        regexQuery.put("$sort",
//                new BasicDBObject("employeeName", -1));
//          regexQuery.put("fromDateinMillisecond",
//                new BasicDBObject("$gt", "716927400000"));

        //System.out.println(regexQuery);

        DBCursor cursor2 = collection.find(regexQuery);
        // cursor2.sort();
        List<ArrearProcess> employeeList = new ArrayList<ArrearProcess>();

        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            //System.out.println(ob.get("employeeName"));

//            String empJson = new Gson().toJson(cursor2.next());
//            //System.out.println(empJson);
            Type type = new TypeToken<ArrearProcess>() {
            }.getType();
            ArrearProcess em = new Gson().fromJson(ob.toString(), type);
            employeeList.add(em);
        }

        if (employeeList.size() > 0) {

            employeeList = new EmployeeArrearManager().getLocationForArrearProcess(employeeList);
            employeeList = new EmployeeArrearManager().getSalaryTypeforArrearProces(employeeList);
            employeeList = new EmployeeArrearManager().getDDOforArrearProcess(employeeList);
            employeeList = new EmployeeArrearManager().getDepartmentforArrear(employeeList);
            employeeList = new EmployeeArrearManager().getDesignationforArrearprocess(employeeList);
            employeeList = new EmployeeArrearManager().getBudgetHeadName(employeeList);
            employeeList = new EmployeeArrearManager().getFundTypeArrearprocess(employeeList);

        }
        if (sortBy.equals("EmployeeCodeM")) {
            Collections.sort(employeeList, new ArrearSortManager());
        }
//        } else if (sortBy.equals("EmployeeName")) {
//            Collections.sort(employeeList, new ArrearSortByEmployeeNameManager());
//        }

        String finalresult = new Gson().toJson(employeeList);
        return finalresult;
    }

    public ByteArrayOutputStream generateArrearReport(String ddo, List<SalarySlipRegisterReport> salarySlipList, String path, int monthValue, int yearValue) throws DocumentException, FileNotFoundException, BadElementException, IOException, Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Document document = null;
        PdfDestination pdfDest = null;
        Rectangle pageSize = new Rectangle(3500f, 2500f);
        document = new Document(PageSize.A3.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, bos);
        pdfDest = new PdfDestination(PdfDestination.XYZ, 0, PageSize.A3.getHeight(), 0.75f);
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_CONFIG_TABLE, conditionMap);
        List<ArrearConfig> ArrearConfigResult = new Gson().fromJson(result, new TypeToken<List<ArrearConfig>>() {
        }.getType());
        List<String> headIds = new ArrayList<String>();
        if (result != null) {
            for (ArrearConfig arrearConfig : ArrearConfigResult) {
                headIds.add(arrearConfig.getHead());
            }
        }
        document.open();
        PdfPTable outerTable = new PdfPTable(1);

        PdfPCell outercell = new PdfPCell();
        outerTable.setWidthPercentage(100f);

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(50.0f, 50.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColor(BaseColor.WHITE);
        InsuranceTransactions insuranceobj = null;
        IncomeTax incomeTaxobj = null;
        ArrayList<LoanPayment> loanObj = null;
        String monthString = getMonthString(monthValue);

        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 30, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 17, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("  Arrear  Report: " + monthString + "-" + yearValue, FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        DateFormat dateFormatTime = new SimpleDateFormat("HH : mm : ss");

        String financialYearJson = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
        List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
        }.getType());
        FinancialYear fyObj = fyList.get(0);
        String fyId = fyObj.getYear();
        String strmin = fyId.substring(2, 4);
        int instrmin = Integer.parseInt(strmin);
        instrmin = instrmin + 1;
        strmin = Integer.toString(instrmin);

        Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(fyId + "-" + strmin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK))));

        PdfPCell timecell = new PdfPCell(timePhrase);
        timecell.setBorderWidthBottom(1);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);
        header.setSpacingAfter(5.0f);
        outercell.addElement(header);
        List<SalaryHead> earningsHead = getAllEarningHeads();
        List<SalaryHead> deductionHead = getAllDeductionHeads();
        List<SalaryHead> earningsHeads = new ArrayList<SalaryHead>();
        List<SalaryHead> deductionHeads = new ArrayList<SalaryHead>();

        if (result != null) {
            for (ArrearConfig arrearConfig : ArrearConfigResult) {
                headIds.add(arrearConfig.getHead());
            }
            //System.out.println("----config headIds----" + headIds);
            for (SalaryHead earnings : earningsHead) {
                String headId = (String) ((LinkedTreeMap) earnings.getId()).get("$oid");
                if (headIds.contains(headId)) {
                    earningsHeads.add(earnings);
                }

            }
            for (SalaryHead deductions : deductionHead) {
                String headId = (String) ((LinkedTreeMap) deductions.getId()).get("$oid");
                if (headIds.contains(headId)) {
                    deductionHeads.add(deductions);
                }

            }

            deductionHead.clear();
            earningsHead.clear();
            deductionHead = deductionHeads;
            earningsHead = earningsHeads;
        }
        //System.out.println("----config earningsHeads----" + earningsHeads.size());
        //System.out.println("----config deductionHeads----" + deductionHeads.size());
//        //System.out.println("----config earningsHeads----"+earningsHeads.size());
//        //System.out.println("----config deductionHeads----"+deductionHeads.size());
        int earningsize = 0;
        int deductionsize = 0;
        int size = 0;
        if (earningsHead != null) {
            if (earningsHead.size() == 0) {
                earningsize = 0;
            } else if (earningsHead.size() == 1) {
                earningsize = 0;
            } else {
                earningsize = (earningsHead.size() / 2);
                if ((earningsHead.size() % 2) == 0) {
                    earningsize = earningsize;
                } else {
                    earningsize = earningsize + 1;
                }
            }
        }
        if (deductionHead != null) {
            if (deductionHead.size() == 0) {
                deductionsize = 0;
            } else if (deductionHead.size() == 1) {
                deductionsize = 1;
            } else {
                deductionsize = (deductionHead.size() / 2);
                if ((deductionHead.size() % 2) == 0) {
                    deductionsize = deductionsize;
                } else {
                    deductionsize = deductionsize + 1;
                }
            }
        }
        size = (earningsize * 3) + (deductionsize * 3) + 6;
        //System.out.println("size" + size);

        PdfPTable columnHeading = new PdfPTable(size);
        columnHeading.setSpacingBefore(3.0f);
        float columns[] = new float[size];
        columns[0] = .5f;
        columns[1] = .8f;
        columns[2] = .6f;
        for (int k = 3; k < size; k++) {
            columns[k] = .5f;
        }
        //System.out.println(columns);
        columnHeading.setWidths(columns);
        columnHeading.setWidthPercentage(100);

        PdfPCell emptyCell0 = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        emptyCell0.setHorizontalAlignment(Element.ALIGN_CENTER);
        emptyCell0.setPaddingLeft(1f);
        emptyCell0.setBorder(Rectangle.NO_BORDER);
        emptyCell0.setBorderWidthLeft(.5f);
        emptyCell0.setBorderWidthTop(.5f);

        PdfPCell emptyCell01 = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        emptyCell01.setHorizontalAlignment(Element.ALIGN_CENTER);
        emptyCell01.setPaddingLeft(1f);
        emptyCell01.setBorder(Rectangle.NO_BORDER);
        emptyCell01.setBorderWidthTop(.5f);

        PdfPCell emptyCell02 = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        emptyCell02.setHorizontalAlignment(Element.ALIGN_CENTER);
        emptyCell02.setPaddingLeft(1f);
        emptyCell02.setBorder(Rectangle.NO_BORDER);
        emptyCell02.setBorderWidthTop(.5f);
        emptyCell02.setColspan(1);

        PdfPCell emptyCell = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        emptyCell.setPaddingLeft(1f);
        emptyCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell earningCellhead = new PdfPCell(new Phrase(new Chunk("To Be Drawn", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        earningCellhead.setHorizontalAlignment(Element.ALIGN_CENTER);
        earningCellhead.setColspan(earningsize + deductionsize + 1);

        PdfPCell earningCellheadprev = new PdfPCell(new Phrase(new Chunk("Amount Drawn", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        earningCellheadprev.setHorizontalAlignment(Element.ALIGN_CENTER);
        earningCellheadprev.setColspan(earningsize + deductionsize + 1);

        PdfPCell earningCellheaddiff = new PdfPCell(new Phrase(new Chunk("Balance", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        earningCellheaddiff.setHorizontalAlignment(Element.ALIGN_CENTER);
        earningCellheaddiff.setColspan(earningsize + deductionsize + 1);

        columnHeading.addCell(emptyCell0);
        columnHeading.addCell(emptyCell01);
        columnHeading.addCell(emptyCell02);
        columnHeading.addCell(earningCellhead);
        columnHeading.addCell(earningCellheadprev);
        columnHeading.addCell(earningCellheaddiff);

        PdfPCell snoCell = new PdfPCell(new Phrase(new Chunk("\n\nS.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        snoCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        //snoCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        snoCell.setPaddingLeft(10f);
        snoCell.setBorder(Rectangle.NO_BORDER);
        snoCell.setBorderWidthTop(.5f);
        snoCell.setBorderWidthLeft(.5f);
        snoCell.setBorderWidthRight(.5f);
        snoCell.setBorderWidthBottom(.2f);
        PdfPCell namecell = new PdfPCell(new Phrase(new Chunk("\n\nEmployee Code\n\n Employee Name", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        namecell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        namecell.setPaddingLeft(10f);
        namecell.setBorder(Rectangle.NO_BORDER);
        namecell.setBorderWidthTop(.5f);
        namecell.setBorderWidthLeft(.5f);
        namecell.setBorderWidthRight(.5f);
        namecell.setBorderWidthBottom(.2f);
        PdfPCell monthYearcell = new PdfPCell(new Phrase(new Chunk("\n\nPeriod", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        monthYearcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        monthYearcell.setPaddingLeft(10f);
        columnHeading.addCell(snoCell);
        columnHeading.addCell(namecell);
        columnHeading.addCell(monthYearcell);
        PdfPCell earningCell = null;
        LinkedHashMap earningMap = new LinkedHashMap();
        if (earningsHead != null) {
            for (int earningHeadCount = 0; earningHeadCount < earningsHead.size();) {
                SalaryHead salaryHead1 = earningsHead.get(earningHeadCount);
                String head1 = "";
                if (salaryHead1.getShortDescription() != null && salaryHead1.getShortDescription() != "" && !salaryHead1.getShortDescription().isEmpty()) {
                    head1 = salaryHead1.getShortDescription();
                }
                earningMap.put(earningHeadCount, head1);
                ++earningHeadCount;
                String head2 = "";
                if (earningHeadCount < earningsHead.size()) {
                    SalaryHead salaryHead2 = earningsHead.get(earningHeadCount);
                    if (salaryHead2.getShortDescription() != null && salaryHead2.getShortDescription() != "" && !salaryHead2.getShortDescription().isEmpty()) {
                        head2 = salaryHead2.getShortDescription();
                    }
                }
                earningMap.put(earningHeadCount, head2);
                ++earningHeadCount;
                String totalHead = "";
                if (head1 != null && head1 != "" && !head1.isEmpty()) {
                    totalHead += head1 + "\n\n";
                }
                if (head2 != null && head2 != "" && !head2.isEmpty()) {
                    totalHead += head2 + "\n\n";
                }
                earningCell = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                earningCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                earningCell.setPaddingLeft(10f);
                columnHeading.addCell(earningCell);
            }
        }

        PdfPCell DeductionsCell = null;
        LinkedHashMap deductionMap = new LinkedHashMap();
        if (deductionHead != null) {
            for (int deductionHeadCount = 0; deductionHeadCount < deductionHead.size();) {
                SalaryHead earningHead1 = deductionHead.get(deductionHeadCount);
                String deductionhead1 = "";
                if (earningHead1 != null) {
                    if (earningHead1.getShortDescription() != null && earningHead1.getShortDescription() != "" && !earningHead1.getShortDescription().isEmpty()) {
                        deductionhead1 = earningHead1.getShortDescription();
                    }
                }
                deductionMap.put(deductionHeadCount, deductionhead1);
                ++deductionHeadCount;
                String deductionhead2 = "";
                if (deductionHeadCount < deductionHead.size()) {
                    SalaryHead earningHead2 = deductionHead.get(deductionHeadCount);
                    if (earningHead2 != null) {
                        if (earningHead2.getShortDescription() != null && earningHead2.getShortDescription() != "" && !earningHead2.getShortDescription().isEmpty()) {
                            deductionhead2 = earningHead2.getShortDescription();
                        }
                    }
                }
                deductionMap.put(deductionHeadCount, deductionhead2);
                ++deductionHeadCount;
                String totalHead = "";
                if (deductionhead1 != null && deductionhead1 != "" && !deductionhead1.isEmpty()) {
                    totalHead += deductionhead1 + "\n\n";
                }
                if (deductionhead2 != null && deductionhead2 != "" && !deductionhead2.isEmpty()) {
                    totalHead += deductionhead2 + "\n\n";
                }
                DeductionsCell = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                DeductionsCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                DeductionsCell.setPaddingLeft(10f);
                columnHeading.addCell(DeductionsCell);
            }
        }
        PdfPCell totalDeductionCell = new PdfPCell(new Phrase(new Chunk("\n\nTotal Earnings\n\nTotal Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        totalDeductionCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        totalDeductionCell.setPaddingLeft(8f);
        columnHeading.addCell(totalDeductionCell);

        PdfPCell earningCell2 = null;
        if (earningsHead != null) {
            for (int earningHeadCount = 0; earningHeadCount < earningsHead.size();) {
                SalaryHead salaryHead1 = earningsHead.get(earningHeadCount);
                String head1 = "";
                if (salaryHead1.getShortDescription() != null && salaryHead1.getShortDescription() != "" && !salaryHead1.getShortDescription().isEmpty()) {
                    head1 = salaryHead1.getShortDescription();
                }
                ++earningHeadCount;
                String head2 = "";
                if (earningHeadCount < earningsHead.size()) {
                    SalaryHead salaryHead2 = earningsHead.get(earningHeadCount);
                    if (salaryHead2.getShortDescription() != null && salaryHead2.getShortDescription() != "" && !salaryHead2.getShortDescription().isEmpty()) {
                        head2 = salaryHead2.getShortDescription();
                    }
                }
                ++earningHeadCount;
                String totalHead = "";
                if (head1 != null && head1 != "" && !head1.isEmpty()) {
                    totalHead += head1 + "\n\n";
                }
                if (head2 != null && head2 != "" && !head2.isEmpty()) {
                    totalHead += head2 + "\n\n";
                }
                earningCell2 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                earningCell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                earningCell2.setPaddingLeft(10f);
                columnHeading.addCell(earningCell2);
            }
        }

        PdfPCell DeductionsCell2 = null;
        if (deductionHead != null) {
            for (int deductionHeadCount = 0; deductionHeadCount < deductionHead.size();) {
                SalaryHead earningHead1 = deductionHead.get(deductionHeadCount);
                String deductionhead1 = "";
                if (earningHead1 != null) {
                    if (earningHead1.getShortDescription() != null && earningHead1.getShortDescription() != "" && !earningHead1.getShortDescription().isEmpty()) {
                        deductionhead1 = earningHead1.getShortDescription();
                    }
                }
                ++deductionHeadCount;
                String deductionhead2 = "";
                if (deductionHeadCount < deductionHead.size()) {
                    SalaryHead earningHead2 = deductionHead.get(deductionHeadCount);
                    if (earningHead2 != null) {
                        if (earningHead2.getShortDescription() != null && earningHead2.getShortDescription() != "" && !earningHead2.getShortDescription().isEmpty()) {
                            deductionhead2 = earningHead2.getShortDescription();
                        }
                    }
                }
                deductionMap.put(deductionHeadCount, deductionhead2);
                ++deductionHeadCount;
                String totalHead = "";
                if (deductionhead1 != null && deductionhead1 != "" && !deductionhead1.isEmpty()) {
                    totalHead += deductionhead1 + "\n\n";
                }
                if (deductionhead2 != null && deductionhead2 != "" && !deductionhead2.isEmpty()) {
                    totalHead += deductionhead2 + "\n\n";
                }
                DeductionsCell2 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                DeductionsCell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                DeductionsCell2.setPaddingLeft(10f);
                columnHeading.addCell(DeductionsCell2);
            }
        }
        PdfPCell totalDeductionCell2 = new PdfPCell(new Phrase(new Chunk("\n\n Total Earnings\n\nTotal Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        totalDeductionCell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        totalDeductionCell2.setPaddingLeft(8f);

        columnHeading.addCell(totalDeductionCell2);

        PdfPCell earningCell3 = null;
        if (earningsHead != null) {
            for (int earningHeadCount = 0; earningHeadCount < earningsHead.size();) {
                SalaryHead salaryHead1 = earningsHead.get(earningHeadCount);
                String head1 = "";
                if (salaryHead1.getShortDescription() != null && salaryHead1.getShortDescription() != "" && !salaryHead1.getShortDescription().isEmpty()) {
                    head1 = salaryHead1.getShortDescription();
                }
                ++earningHeadCount;
                String head2 = "";
                if (earningHeadCount < earningsHead.size()) {
                    SalaryHead salaryHead2 = earningsHead.get(earningHeadCount);
                    if (salaryHead2.getShortDescription() != null && salaryHead2.getShortDescription() != "" && !salaryHead2.getShortDescription().isEmpty()) {
                        head2 = salaryHead2.getShortDescription();
                    }
                }
                ++earningHeadCount;
                String totalHead = "";
                if (head1 != null && head1 != "" && !head1.isEmpty()) {
                    totalHead += head1 + "\n\n";
                }
                if (head2 != null && head2 != "" && !head2.isEmpty()) {
                    totalHead += head2 + "\n\n";
                }
                earningCell3 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                earningCell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                earningCell3.setPaddingLeft(10f);
                columnHeading.addCell(earningCell3);
            }
        }

        PdfPCell DeductionsCell3 = null;
        if (deductionHead != null) {
            for (int deductionHeadCount = 0; deductionHeadCount < deductionHead.size();) {
                SalaryHead earningHead1 = deductionHead.get(deductionHeadCount);
                String deductionhead1 = "";
                if (earningHead1 != null) {
                    if (earningHead1.getShortDescription() != null && earningHead1.getShortDescription() != "" && !earningHead1.getShortDescription().isEmpty()) {
                        deductionhead1 = earningHead1.getShortDescription();
                    }
                }
                ++deductionHeadCount;
                String deductionhead2 = "";
                if (deductionHeadCount < deductionHead.size()) {
                    SalaryHead earningHead2 = deductionHead.get(deductionHeadCount);
                    if (earningHead2 != null) {
                        if (earningHead2.getShortDescription() != null && earningHead2.getShortDescription() != "" && !earningHead2.getShortDescription().isEmpty()) {
                            deductionhead2 = earningHead2.getShortDescription();
                        }
                    }
                }
                deductionMap.put(deductionHeadCount, deductionhead2);
                ++deductionHeadCount;
                String totalHead = "";
                if (deductionhead1 != null && deductionhead1 != "" && !deductionhead1.isEmpty()) {
                    totalHead += deductionhead1 + "\n\n";
                }
                if (deductionhead2 != null && deductionhead2 != "" && !deductionhead2.isEmpty()) {
                    totalHead += deductionhead2 + "\n\n";
                }
                DeductionsCell3 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                DeductionsCell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                DeductionsCell3.setPaddingLeft(10f);
                columnHeading.addCell(DeductionsCell3);
            }
        }
        PdfPCell totalDeductionCell3 = new PdfPCell(new Phrase(new Chunk("\n\nTotal Earnings\n\nTotal Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        totalDeductionCell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        totalDeductionCell3.setPaddingLeft(8f);
        columnHeading.addCell(totalDeductionCell3);

        List<ArrearProcess> arrearList = new ArrearReportManager().getAlldetailsFromArrearForReport(salarySlipList);
        //System.out.println("-------------arrearListsize----------" + arrearList.size());
        outercell.addElement(columnHeading);
        int month = 0;
        int year = 0;
        String name = "";
        String code = "";
        int attendacne = 0;
        String designation = "";
        int count = 0;
        Map<String, Double> totalHeadMap = new HashMap<String, Double>();
        Map<String, Double> totalHeadMap1 = new HashMap<String, Double>();

        Map<String, Double> totalHeadMapprev = new HashMap<String, Double>();
        Map<String, Double> totalHeadMapprev1 = new HashMap<String, Double>();

        Map<String, Double> totalHeadMapDiff = new HashMap<String, Double>();
        Map<String, Double> totalHeadMapDiff1 = new HashMap<String, Double>();

        Map totalHeadAmount = new HashMap();
        Map empNameMap = new HashMap();
        Double totArrearHeads = 0.00;
        Double totArrearEarningHeads = 0.00;
        Double totArrearDeductionHeads = 0.00;
        Double totPrevHeads = 0.00;
        Double totPrevEarningHeads = 0.00;
        Double totPrevDeductionHeads = 0.00;
        Double totDeffHeads = 0.00;
        Double totDeffEarningHeads = 0.00;
        Double totDeffDeductionHeads = 0.00;

        if (arrearList != null && arrearList.size() > 0) {
            PdfPTable employeeData = new PdfPTable(size);
            employeeData.setWidths(columns);
            employeeData.setWidthPercentage(100);
            for (ArrearProcess salarylist : arrearList) {

                double totalEarnings = 0.00;
                double totalDeductions = 0.00;
                double totalPrevEarnings = 0.00;
                double totalPrevDeductions = 0.00;
                double totalDiffEarnings = 0.00;
                double totalDiffDeductions = 0.00;
                name = salarylist.getEmployeeName();
                year = salarylist.getYear();
                month = salarylist.getMonth();
                code = salarylist.getEmployeeCode();
                LinkedHashMap<String, String> deductions = new LinkedHashMap<String, String>();
                LinkedHashMap<String, String> Earnings = new LinkedHashMap<String, String>();

                LinkedHashMap<String, String> prevDeductions = new LinkedHashMap<String, String>();
                LinkedHashMap<String, String> prevEarnings = new LinkedHashMap<String, String>();

                LinkedHashMap<String, String> diffDeductions = new LinkedHashMap<String, String>();
                LinkedHashMap<String, String> diffEarnings = new LinkedHashMap<String, String>();

                List<EarningHeadsDetails> earningList = salarylist.getEarningHeads();
                List<EarningHeadsDetails> deductionList = salarylist.getDeductionHeads();

                List<EarningHeadsDetails> prevearningList = salarylist.getPrevEarningHeads();
                List<EarningHeadsDetails> prevdeductionList = salarylist.getPrevDeductionHeads();

                List<EarningHeadsDetails> diffEarningList = salarylist.getDiffEarningHeads();
                List<EarningHeadsDetails> diffDeductionList = salarylist.getDiffDeductionHeads();

//                //System.out.println("------earningList------"+earningList.toString());
//                //System.out.println("------deductionList------"+deductionList.toString());
//                //System.out.println("------prevearningList------"+prevearningList.toString());
//                //System.out.println("------prevdeductionList------"+prevdeductionList.toString());
//                //System.out.println("------diffEarningList------"+diffEarningList.toString());
//                //System.out.println("------diffDeductionList------"+diffDeductionList.toString());
                if (earningList != null && earningList.size() > 0) {
                    for (int i = 0; i < earningList.size(); i++) {
                        String shortDescrption = null;
                        String id = earningList.get(i).getDescription();
                        if (headIds.contains(id)) {
                            totalEarnings += earningList.get(i).getAmount();
                            if (id != null) {
                                String resultAS = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, id);
                                List<SalaryHead> religionList = new Gson().fromJson(resultAS, new TypeToken<List<SalaryHead>>() {
                                }.getType());
                                if (resultAS != null) {
                                    shortDescrption = religionList.get(0).getShortDescription();
                                }
                            }
                            Earnings.put(shortDescrption, String.valueOf(earningList.get(i).getAmount()));
                        }
                    }
                }
                if (prevearningList != null && prevearningList.size() > 0) {
                    for (int i = 0; i < prevearningList.size(); i++) {
                        SalaryHead salHead = prevearningList.get(i).getDescriptionInfo();
                        String id = (String) ((LinkedTreeMap) salHead.getId()).get("$oid");
                        if (headIds.contains(id)) {
                            totalPrevEarnings += prevearningList.get(i).getAmount();
                            String shortDescrption = "";
                            if (id != null) {
                                String resultAS = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, id);
                                List<SalaryHead> religionList = new Gson().fromJson(resultAS, new TypeToken<List<SalaryHead>>() {
                                }.getType());
                                if (resultAS != null) {
                                    shortDescrption = religionList.get(0).getShortDescription();
                                }
                            }
                            prevEarnings.put(shortDescrption, String.valueOf(prevearningList.get(i).getAmount()));
                        }
                    }
                }
                if (diffEarningList != null && diffEarningList.size() > 0) {
                    for (int i = 0; i < diffEarningList.size(); i++) {

                        SalaryHead salHead = diffEarningList.get(i).getDescriptionInfo();
                        String id = (String) ((LinkedTreeMap) salHead.getId()).get("$oid");
                        //System.out.println("------_head_id-----" + id);
                        if (headIds.contains(id)) {
                            totalDiffEarnings += diffEarningList.get(i).getAmount();
                            //System.out.println("------totalDiffEarnings-----" + totalDiffEarnings);
                            String shortDescrption = "";
                            if (id != null) {
                                String resultAS = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, id);
                                List<SalaryHead> religionList = new Gson().fromJson(resultAS, new TypeToken<List<SalaryHead>>() {
                                }.getType());
                                if (resultAS != null) {
                                    shortDescrption = religionList.get(0).getShortDescription();
                                }
                            }
                            diffEarnings.put(shortDescrption, String.valueOf(diffEarningList.get(i).getAmount()));
                        }
                    }
                }
                //System.out.println("------diffEarnings-----" + diffEarnings);
                //System.out.println("------totalDiffEarnings-----" + totalDiffEarnings);
                if (deductionList != null && deductionList.size() > 0) {
                    for (int i = 0; i < deductionList.size(); i++) {

                        String shortDescrption = null;
                        String id = deductionList.get(i).getDescription();
                        if (headIds.contains(id)) {
                            totalDeductions += deductionList.get(i).getAmount();
                            if (id != null) {
                                String resultAS = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, id);
                                List<SalaryHead> religionList = new Gson().fromJson(resultAS, new TypeToken<List<SalaryHead>>() {
                                }.getType());
                                if (resultAS != null) {
                                    shortDescrption = religionList.get(0).getShortDescription();
                                }
                            }
                            deductions.put(shortDescrption, String.valueOf(deductionList.get(i).getAmount()));
                        }
                    }
                }

                if (prevdeductionList != null && prevdeductionList.size() > 0) {
                    for (int i = 0; i < prevdeductionList.size(); i++) {

                        SalaryHead salHead = prevdeductionList.get(i).getDescriptionInfo();
                        String id = (String) ((LinkedTreeMap) salHead.getId()).get("$oid");
                        if (headIds.contains(id)) {
                            totalPrevDeductions += prevdeductionList.get(i).getAmount();
                            String shortDescrption = "";
                            if (id != null) {
                                String resultAS = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, id);
                                List<SalaryHead> religionList = new Gson().fromJson(resultAS, new TypeToken<List<SalaryHead>>() {
                                }.getType());
                                if (resultAS != null) {
                                    shortDescrption = religionList.get(0).getShortDescription();
                                }
                            }
                            prevDeductions.put(shortDescrption, String.valueOf(prevdeductionList.get(i).getAmount()));
                        }
                    }
                }

                if (diffDeductionList != null && diffDeductionList.size() > 0) {
                    for (int i = 0; i < diffDeductionList.size(); i++) {
                        SalaryHead salHead = diffDeductionList.get(i).getDescriptionInfo();
                        String id = (String) ((LinkedTreeMap) salHead.getId()).get("$oid");
                        if (headIds.contains(id)) {
                            totalDiffDeductions += diffDeductionList.get(i).getAmount();
                            String shortDescrption = "";
                            if (id != null) {
                                String resultAS = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, id);
                                List<SalaryHead> religionList = new Gson().fromJson(resultAS, new TypeToken<List<SalaryHead>>() {
                                }.getType());
                                if (resultAS != null) {
                                    shortDescrption = religionList.get(0).getShortDescription();
                                }
                            }
                            diffDeductions.put(shortDescrption, String.valueOf(diffDeductionList.get(i).getAmount()));
                        }
                    }
                }
                PdfPCell Cell1 = null;
                PdfPCell Cell2 = null;
                if (empNameMap.containsKey(code)) {
                    Cell1 = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                    Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    Cell1.setBorder(Rectangle.NO_BORDER);
                    Cell1.setBorderWidthLeft(.5f);
                    //Cell1.setBorderWidthRight(.5f);
                    Cell1.setBorderWidthBottom(.5f);
                    snoCell.setPaddingLeft(1f);
                    Cell2 = new PdfPCell(new Phrase(new Chunk(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                    Cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    Cell2.setBorder(Rectangle.NO_BORDER);
                    Cell2.setBorderWidthLeft(.5f);
                    Cell2.setBorderWidthRight(.5f);
                    Cell2.setBorderWidthBottom(.5f);
                } else {
                    count++;
                    empNameMap.put(code, code);
                    Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + String.valueOf(count), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                    Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    Cell1.setBorder(Rectangle.NO_BORDER);
                    Cell1.setBorderWidthTop(.5f);
                    Cell1.setBorderWidthLeft(.5f);
                    //Cell1.setBorderWidthRight(.5f);
                    snoCell.setPaddingLeft(1f);
                    Cell2 = new PdfPCell(new Phrase(new Chunk("\n\n" + code + "\n\n" + name + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                    Cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    Cell2.setBorder(Rectangle.NO_BORDER);
                    Cell2.setBorderWidthLeft(.5f);
                    Cell2.setBorderWidthRight(.5f);
                    Cell2.setBorderWidthTop(.5f);
                }
                PdfPCell cell4 = new PdfPCell(new Phrase(new Chunk("\n\n" + getMonthString(month) + "\n\n" + year + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                employeeData.addCell(Cell1);
                employeeData.addCell(Cell2);
                employeeData.addCell(cell4);
                Set<Integer> earingkey = null;

                if (earningMap != null && !earningMap.isEmpty() && !earningMap.equals("{}")) {
                    earingkey = earningMap.keySet();
                }
                if (earingkey != null && !earingkey.equals("[]") && earingkey.size() > 0) {
                    ArrayList list = new ArrayList();
                    for (Integer key : earingkey) {
                        list.add(key);
                    }
                    for (int k = 0; k < list.size();) {
                        double value11 = 0.00;
                        String des1 = (String) earningMap.get(list.get(k));
                        String value1 = (String) Earnings.get(des1);
                        //System.out.println("---value1value1---" + value1 + k);
                        if (value1 == null || value1 == "0.00" || value1 == "0.0") {
                            value1 = "0.00";
                        } else {
                            value11 = Double.parseDouble(value1);
                            value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                        }

                        if (totalHeadMap.containsKey(des1)) {
                            double currentVal = totalHeadMap.get(des1);
                            totalHeadMap.put(des1, currentVal + value11);

                        } else {
                            totalHeadMap.put(des1, value11);
                        }

                        k++;
                        String value2 = "";
                        double value12 = 0.00;
                        if (k < list.size()) {
                            String des2 = (String) earningMap.get(list.get(k));
                            value2 = (String) Earnings.get(des2);

                            if (value2 == null) {
                                value2 = "0.00";
                            } else {
                                value12 = Double.parseDouble(value2);
                                value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value2));
                            }
                            if (totalHeadMap.containsKey(des2)) {
                                double currentVal = totalHeadMap.get(des2);
                                totalHeadMap.put(des2, currentVal + value12);

                            } else {
                                totalHeadMap.put(des2, value12);
                            }
                        }
                        k++;
                        //System.out.println("-------value1----" + value1);
                        //System.out.println("-------value1----" + value1);
                        String totalHead = "";
                        if (value1 != null && value1 != "" && !value1.isEmpty()) {
                            totalHead += value1 + "\n\n";
                        }
                        if (value2 != null && value2 != "" && !value2.isEmpty()) {
                            totalHead += value2 + "\n\n";
                        }
                        Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        employeeData.addCell(Cell1);

                    }
                }
                Set<Integer> deductionkey = null;
                if (deductionMap != null && !deductionMap.isEmpty() && !deductionMap.equals("{}")) {
                    deductionkey = deductionMap.keySet();
                }

                if (deductionkey != null && !deductionkey.equals("[]") && deductionkey.size() > 0) {
                    ArrayList list1 = new ArrayList();
                    for (Integer key : deductionkey) {
                        list1.add(key);
                    }
                    for (int k = 0; k < list1.size();) {
                        double value11 = 0.00;
                        String des1 = (String) deductionMap.get(list1.get(k));
                        String value1 = (String) deductions.get(des1);
                        if (value1 == null) {
                            value1 = "0.00";
                        } else {
                            value11 = Double.parseDouble(value1);
                            value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                        }
                        if (totalHeadMap1.containsKey(des1)) {

                            double currentVal = totalHeadMap1.get(des1);
                            totalHeadMap1.put(des1, currentVal + value11);

                        } else {
                            totalHeadMap1.put(des1, value11);
                        }
                        k++;
                        String value2 = "";
                        double value12 = 0.00;
                        if (k < list1.size()) {
                            String des2 = (String) deductionMap.get(list1.get(k));
                            value2 = (String) deductions.get(des2);
                            if (value2 == null) {
                                value2 = "0.00";
                            } else {
                                value12 = Double.parseDouble(value2);
                                value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value2));
                            }
                            if (totalHeadMap1.containsKey(des2)) {
                                double currentVal = totalHeadMap1.get(des2);
                                totalHeadMap1.put(des2, currentVal + value12);

                            } else {
                                totalHeadMap1.put(des2, value12);
                            }
                        }
                        k++;
                        String totalHead = "";
                        if (value1 != null && value1 != "" && !value1.isEmpty()) {
                            totalHead += value1 + "\n\n";
                        }
                        if (value2 != null && value2 != "" && !value2.isEmpty()) {
                            totalHead += value2 + "\n\n";
                        }
                        Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        employeeData.addCell(Cell1);

                    }
                }
                totArrearHeads += totalDeductions + totalEarnings;
                totArrearEarningHeads += totalEarnings;
                totArrearDeductionHeads += totalDeductions;
                totalHeadAmount.put("totalArrearHeads", SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totArrearHeads));
                totalHeadAmount.put("totalArrearEarningHeads", SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totArrearEarningHeads));
                totalHeadAmount.put("totalArrearDeductionsHeads", SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totArrearDeductionHeads));
                PdfPCell totalDeductionCellData = new PdfPCell(new Phrase(new Chunk("\n\n" + SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalEarnings) + "\n\n" + SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalDeductions), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                totalDeductionCellData.setHorizontalAlignment(Element.ALIGN_CENTER);
                employeeData.addCell(totalDeductionCellData);
                if (earingkey != null && !earingkey.equals("[]") && earingkey.size() > 0) {
                    ArrayList list = new ArrayList();
                    for (Integer key : earingkey) {
                        list.add(key);
                    }
                    for (int k = 0; k < list.size();) {
                        double value11 = 0.00;
                        String des1 = (String) earningMap.get(list.get(k));
                        String value1 = (String) prevEarnings.get(des1);
                        if (value1 == null) {
                            value1 = "0.00";
                        } else {
                            value11 = Double.parseDouble(value1);
                            value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                        }
                        if (totalHeadMapprev.containsKey(des1)) {

                            double currentVal = totalHeadMapprev.get(des1);
                            totalHeadMapprev.put(des1, currentVal + value11);

                        } else {
                            totalHeadMapprev.put(des1, value11);
                        }
                        k++;
                        String value2 = "";
                        double value12 = 0.00;
                        if (k < list.size()) {
                            String des2 = (String) earningMap.get(list.get(k));
                            value2 = (String) prevEarnings.get(des2);

                            if (value2 == null) {
                                value2 = "0.00";
                            } else {
                                value12 = Double.parseDouble(value2);
                                value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value2));
                            }

                            if (totalHeadMapprev.containsKey(des2)) {

                                double currentVal = totalHeadMapprev.get(des2);
                                totalHeadMapprev.put(des2, currentVal + value12);

                            } else {
                                totalHeadMapprev.put(des2, value12);
                            }
                        }
                        k++;
                        String totalHead = "";
                        if (value1 != null && value1 != "" && !value1.isEmpty()) {
                            totalHead += value1 + "\n\n";
                        }
                        if (value2 != null && value2 != "" && !value2.isEmpty()) {
                            totalHead += value2 + "\n\n";
                        }
                        Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        employeeData.addCell(Cell1);

                    }
                }
                if (deductionkey != null && !deductionkey.equals("[]") && deductionkey.size() > 0) {
                    ArrayList list1 = new ArrayList();
                    for (Integer key : deductionkey) {
                        list1.add(key);
                    }
                    for (int k = 0; k < list1.size();) {
                        double value11 = 0.00;
                        String des1 = (String) deductionMap.get(list1.get(k));
                        String value1 = (String) prevDeductions.get(des1);
                        if (value1 == null) {
                            value1 = "0.00";
                        } else {
                            value11 = Double.parseDouble(value1);
                            value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                        }
                        if (totalHeadMapprev1.containsKey(des1)) {

                            double currentVal = totalHeadMapprev1.get(des1);
                            totalHeadMapprev1.put(des1, currentVal + value11);

                        } else {
                            totalHeadMapprev1.put(des1, value11);
                        }
                        k++;
                        String value2 = "";
                        double value12 = 0.00;
                        if (k < list1.size()) {
                            String des2 = (String) deductionMap.get(list1.get(k));
                            value2 = (String) prevDeductions.get(des2);

                            if (value2 == null) {
                                value2 = "0.00";
                            } else {
                                value12 = Double.parseDouble(value2);
                                value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value2));
                            }
                            if (totalHeadMapprev1.containsKey(des2)) {

                                double currentVal = totalHeadMapprev1.get(des2);
                                totalHeadMapprev1.put(des2, currentVal + value12);

                            } else {
                                totalHeadMapprev1.put(des2, value12);
                            }
                        }
                        k++;
                        String totalHead = "";
                        if (value1 != null && value1 != "" && !value1.isEmpty()) {
                            totalHead += value1 + "\n\n";
                        }
                        if (value2 != null && value2 != "" && !value2.isEmpty()) {
                            totalHead += value2 + "\n\n";
                        }
                        Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        employeeData.addCell(Cell1);

                    }
                }
                totPrevHeads += totalPrevDeductions + totalPrevEarnings;
                totPrevEarningHeads += totalPrevEarnings;
                totPrevDeductionHeads += totalPrevDeductions;
                totalHeadAmount.put("totalPrevHeads", SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totPrevHeads));
                totalHeadAmount.put("totalPrevEarningHeads", SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totPrevEarningHeads));
                totalHeadAmount.put("totalPrevDeductionHeads", SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totPrevDeductionHeads));
                PdfPCell totalDeductionCellData1 = new PdfPCell(new Phrase(new Chunk("\n\n" + SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalPrevEarnings) + "\n\n" + SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalPrevDeductions), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                totalDeductionCellData1.setHorizontalAlignment(Element.ALIGN_CENTER);
                employeeData.addCell(totalDeductionCellData1);

                if (earingkey != null && !earingkey.equals("[]") && earingkey.size() > 0) {
                    ArrayList list = new ArrayList();
                    for (Integer key : earingkey) {
                        list.add(key);
                    }
                    for (int k = 0; k < list.size();) {
                        double value11 = 0.00;
                        String des1 = (String) earningMap.get(list.get(k));
                        String value1 = (String) diffEarnings.get(des1);
                        if (value1 == null) {
                            value1 = "0.00";
                        } else {
                            value11 = Double.parseDouble(value1);
                            value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                        }
                        if (totalHeadMapDiff.containsKey(des1)) {

                            double currentVal = totalHeadMapDiff.get(des1);
                            totalHeadMapDiff.put(des1, currentVal + value11);

                        } else {
                            totalHeadMapDiff.put(des1, value11);
                        }
                        k++;
                        String value2 = "";
                        double value12 = 0.00;
                        if (k < list.size()) {
                            String des2 = (String) earningMap.get(list.get(k));
                            value2 = (String) diffEarnings.get(des2);
                            if (value2 == null) {
                                value2 = "0.00";
                            } else {
                                value12 = Double.parseDouble(value2);
                                value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value2));
                            }
                            if (totalHeadMapDiff.containsKey(des2)) {

                                double currentVal = totalHeadMapDiff.get(des2);
                                totalHeadMapDiff.put(des2, currentVal + value12);

                            } else {
                                totalHeadMapDiff.put(des2, value12);
                            }
                        }
                        k++;
                        String totalHead = "";
                        if (value1 != null && value1 != "" && !value1.isEmpty()) {
                            totalHead += value1 + "\n\n";
                        }
                        if (value2 != null && value2 != "" && !value2.isEmpty()) {
                            totalHead += value2 + "\n\n";
                        }
                        Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        employeeData.addCell(Cell1);

                    }
                }
                if (deductionkey != null && !deductionkey.equals("[]") && deductionkey.size() > 0) {
                    ArrayList list1 = new ArrayList();
                    for (Integer key : deductionkey) {
                        list1.add(key);
                    }
                    for (int k = 0; k < list1.size();) {
                        double value11 = 0.00;
                        String des1 = (String) deductionMap.get(list1.get(k));
                        String value1 = (String) diffDeductions.get(des1);
                        if (value1 == null) {
                            value1 = "0.00";
                        } else {
                            value11 = Double.parseDouble(value1);
                            value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                        }
                        if (totalHeadMapDiff1.containsKey(des1)) {

                            double currentVal = totalHeadMapDiff1.get(des1);
                            totalHeadMapDiff1.put(des1, currentVal + value11);

                        } else {
                            totalHeadMapDiff1.put(des1, value11);
                        }
                        k++;
                        String value2 = "";
                        double value12 = 0.00;
                        if (k < list1.size()) {
                            String des2 = (String) deductionMap.get(list1.get(k));
                            value2 = (String) diffDeductions.get(des2);
                            if (value2 == null) {
                                value2 = "0.00";
                            } else {
                                value12 = Double.parseDouble(value2);
                                value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value2));
                            }
                            if (totalHeadMapDiff1.containsKey(des2)) {

                                double currentVal = totalHeadMapDiff1.get(des2);
                                totalHeadMapDiff1.put(des2, currentVal + value12);

                            } else {
                                totalHeadMapDiff1.put(des2, value12);
                            }
                        }
                        k++;
                        String totalHead = "";
                        if (value1 != null && value1 != "" && !value1.isEmpty()) {
                            totalHead += value1 + "\n\n";
                        }
                        if (value2 != null && value2 != "" && !value2.isEmpty()) {
                            totalHead += value2 + "\n\n";
                        }
                        Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        employeeData.addCell(Cell1);

                    }
                }
                totDeffHeads += totalDiffEarnings + totalDiffDeductions;
                totDeffEarningHeads += totalDiffEarnings;
                totDeffDeductionHeads += totalDiffDeductions;
                totalHeadAmount.put("totalDeffHeads", SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totDeffHeads));
                totalHeadAmount.put("totalDeffEarningHeads", SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totDeffEarningHeads));
                totalHeadAmount.put("totalDeffDeductionHeads", SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totDeffDeductionHeads));
                PdfPCell totalDeductionCellData2 = new PdfPCell(new Phrase(new Chunk("\n" + SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalDiffEarnings) + "\n\n" + SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalDiffDeductions), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                totalDeductionCellData2.setHorizontalAlignment(Element.ALIGN_CENTER);
                employeeData.addCell(totalDeductionCellData2);
            }
            // code of getting individual some of heads 
            // first arrear heads
            PdfPCell Cell1a = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
            Cell1a.setHorizontalAlignment(Element.ALIGN_CENTER);
            snoCell.setPaddingLeft(1f);
            Cell1a.setBorder(Rectangle.NO_BORDER);
            Cell1a.setBorderWidthLeft(.5f);
            Cell1a.setBorderWidthBottom(.5f);
            Cell1a.setBorderWidthTop(.5f);
            PdfPCell Cell2a = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
            Cell2a.setHorizontalAlignment(Element.ALIGN_CENTER);
            Cell2a.setBorder(Rectangle.NO_BORDER);
            Cell2a.setBorderWidthRight(.5f);
            Cell2a.setBorderWidthBottom(.5f);
            Cell2a.setBorderWidthTop(.5f);
            PdfPCell cell4a = new PdfPCell(new Phrase(new Chunk("\n\n" + "TOTAL", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
            cell4a.setHorizontalAlignment(Element.ALIGN_CENTER);

            employeeData.addCell(Cell1a);
            employeeData.addCell(Cell2a);
            employeeData.addCell(cell4a);
            Set<Integer> earingkey = null;

            if (earningMap != null && !earningMap.isEmpty() && !earningMap.equals("{}")) {
                earingkey = earningMap.keySet();
            }
            if (earingkey != null && !earingkey.equals("[]") && earingkey.size() > 0) {
                ArrayList list = new ArrayList();
                for (Integer key : earingkey) {
                    list.add(key);
                }
                for (int k = 0; k < list.size();) {
                    String value1value1 = "";
                    String des1 = (String) earningMap.get(list.get(k));
                    Double value1 = (Double) totalHeadMap.get(des1);

                    if (value1 == null) {
                        value1 = 0.00;
                        value1value1 = "0.00";
                    } else {
                        value1value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(value1);
                    }
                    k++;
                    Double value2 = 0.00;
                    String value2value2 = "";
                    if (k < list.size()) {
                        String des2 = (String) earningMap.get(list.get(k));
                        value2 = (Double) totalHeadMap.get(des2);
                        if (value2 == null) {
                            value2 = 0.00;
                            value2value2 = "0.00";
                        } else {
                            value2value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(value2);
                        }
                    }
                    k++;
                    String totalHead = "";
                    if (value1 != null) {
                        totalHead += value1value1 + "\n\n";
                    }
                    if (value2 != null) {
                        totalHead += value2value2 + "\n\n";
                    }
                    PdfPCell Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                    Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    employeeData.addCell(Cell1);

                }
            }
            Set<Integer> deductionkey = null;
            if (deductionMap != null && !deductionMap.isEmpty() && !deductionMap.equals("{}")) {
                deductionkey = deductionMap.keySet();
            }
            if (deductionkey != null && !deductionkey.equals("[]") && deductionkey.size() > 0) {
                ArrayList list1 = new ArrayList();
                for (Integer key : deductionkey) {
                    list1.add(key);
                }
                for (int k = 0; k < list1.size();) {
                    String value1value1 = "";
                    String des1 = (String) deductionMap.get(list1.get(k));
                    Double value1 = (Double) totalHeadMap1.get(des1);

                    if (value1 == null) {
                        value1 = 0.00;
                        value1value1 = "0.00";
                    } else {
                        value1value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                    }
                    k++;
                    Double value2 = 0.00;
                    String value2value2 = "";
                    if (k < list1.size()) {
                        String des2 = (String) deductionMap.get(list1.get(k));
                        value2 = (Double) totalHeadMap1.get(des2);

                        if (value2 == null) {
                            value2 = 0.00;
                            value2value2 = "0.00";
                        } else {
                            value2value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(value2);
                        }
                    }
                    k++;
                    String totalHead = "";
                    if (value1 != null) {
                        totalHead += value1value1 + "\n\n";
                    }
                    if (value2 != null) {
                        totalHead += value2value2 + "\n\n";
                    }
                    PdfPCell Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                    Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    employeeData.addCell(Cell1);

                }
            }
            PdfPCell cell5a = new PdfPCell(new Phrase(new Chunk(String.valueOf("\n\n" + totalHeadAmount.get("totalArrearEarningHeads")) + "\n\n" + totalHeadAmount.get("totalArrearDeductionsHeads"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
            cell5a.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeeData.addCell(cell5a);
            // prev total heads
            if (earingkey != null && !earingkey.equals("[]") && earingkey.size() > 0) {
                ArrayList list = new ArrayList();
                for (Integer key : earingkey) {
                    list.add(key);
                }
                for (int k = 0; k < list.size();) {
                    String value1value1 = "";
                    String des1 = (String) earningMap.get(list.get(k));
                    Double value1 = (Double) totalHeadMapprev.get(des1);

                    if (value1 == null) {
                        value1 = 0.00;
                        value1value1 = "0.00";
                    } else {
                        value1value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                    }
                    k++;
                    Double value2 = 0.00;
                    String value2value2 = "";
                    if (k < list.size()) {
                        String des2 = (String) earningMap.get(list.get(k));
                        value2 = (Double) totalHeadMapprev.get(des2);

                        if (value2 == null) {
                            value2 = 0.00;
                            value2value2 = "0.00";
                        } else {
                            value2value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(value2);
                        }
                    }
                    k++;
                    String totalHead = "";
                    if (value1 != null) {
                        totalHead += value1value1 + "\n\n";
                    }
                    if (value2 != null) {
                        totalHead += value2value2 + "\n\n";
                    }
                    PdfPCell Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                    Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    employeeData.addCell(Cell1);

                }
            }
            if (deductionkey != null && !deductionkey.equals("[]") && deductionkey.size() > 0) {
                ArrayList list1 = new ArrayList();
                for (Integer key : deductionkey) {
                    list1.add(key);
                }
                for (int k = 0; k < list1.size();) {
                    String value1value1 = "";
                    String des1 = (String) deductionMap.get(list1.get(k));
                    Double value1 = (Double) totalHeadMapprev1.get(des1);
                    if (value1 == null) {
                        value1 = 0.00;
                        value1value1 = "0.00";
                    } else {
                        value1value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                    }
                    k++;
                    Double value2 = 0.00;
                    String value2value2 = "";
                    if (k < list1.size()) {
                        String des2 = (String) deductionMap.get(list1.get(k));
                        value2 = (Double) totalHeadMapprev1.get(des2);

                        if (value2 == null) {
                            value2 = 0.00;
                            value2value2 = "0.00";
                        } else {
                            value2value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(value2);
                        }
                    }
                    k++;
                    String totalHead = "";
                    if (value1 != null) {
                        totalHead += value1value1 + "\n\n";
                    }
                    if (value2 != null) {
                        totalHead += value2value2 + "\n\n";
                    }
                    PdfPCell Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                    Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    employeeData.addCell(Cell1);

                }
            }
            PdfPCell cell6a = new PdfPCell(new Phrase(new Chunk(String.valueOf("\n\n" + totalHeadAmount.get("totalPrevEarningHeads")) + "\n\n" + totalHeadAmount.get("totalPrevDeductionHeads"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
            cell6a.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeeData.addCell(cell6a);
            // diff total heads
            if (earingkey != null && !earingkey.equals("[]") && earingkey.size() > 0) {
                ArrayList list = new ArrayList();
                for (Integer key : earingkey) {
                    list.add(key);
                }
                for (int k = 0; k < list.size();) {
                    String value1value1 = "";
                    String des1 = (String) earningMap.get(list.get(k));
                    Double value1 = (Double) totalHeadMapDiff.get(des1);

                    if (value1 == null) {
                        value1 = 0.00;
                        value1value1 = "0.00";
                    } else {
                        value1value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                    }
                    k++;
                    Double value2 = 0.00;
                    String value2value2 = "";
                    if (k < list.size()) {
                        String des2 = (String) earningMap.get(list.get(k));
                        value2 = (Double) totalHeadMapDiff.get(des2);

                        if (value2 == null) {
                            value2 = 0.00;
                            value2value2 = "0.00";
                        } else {
                            value2value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(value2);
                        }
                    }
                    k++;
                    String totalHead = "";
                    if (value1 != null) {
                        totalHead += value1value1 + "\n\n";
                    }
                    if (value2 != null) {
                        totalHead += value2value2 + "\n\n";
                    }
                    PdfPCell Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                    Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    employeeData.addCell(Cell1);

                }
            }
            if (deductionkey != null && !deductionkey.equals("[]") && deductionkey.size() > 0) {
                ArrayList list1 = new ArrayList();
                for (Integer key : deductionkey) {
                    list1.add(key);
                }
                for (int k = 0; k < list1.size();) {
                    String value1value1 = "";
                    String des1 = (String) deductionMap.get(list1.get(k));
                    Double value1 = (Double) totalHeadMapDiff1.get(des1);
                    if (value1 == null) {
                        value1 = 0.00;
                        value1value1 = "0.00";
                    } else {
                        value1value1 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(value1));
                    }
                    k++;
                    Double value2 = 0.00;
                    String value2value2 = "";
                    if (k < list1.size()) {
                        String des2 = (String) deductionMap.get(list1.get(k));
                        value2 = (Double) totalHeadMapDiff1.get(des2);

                        if (value2 == null) {
                            value2 = 0.00;
                            value2value2 = "0.00";
                        } else {
                            value2value2 = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(value2);
                        }
                    }
                    k++;
                    String totalHead = "";
                    if (value1 != null) {
                        totalHead += value1value1 + "\n\n";
                    }
                    if (value2 != null) {
                        totalHead += value2value2 + "\n\n";
                    }
                    PdfPCell Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
                    Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    employeeData.addCell(Cell1);

                }
            }

            PdfPCell cell7a = new PdfPCell(new Phrase(new Chunk(String.valueOf("\n\n" + totalHeadAmount.get("totalDeffEarningHeads")) + "\n\n" + totalHeadAmount.get("totalDeffDeductionHeads"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
            cell7a.setHorizontalAlignment(Element.ALIGN_CENTER);
            employeeData.addCell(cell7a);

            outercell.addElement(employeeData);
        }

        //System.out.println("------diff ded------" + totalHeadMapDiff1);
//        //System.out.println("------diff ear------"+totalHeadMapDiff);
//        //System.out.println("------arre ear------"+totalHeadMap);
//        //System.out.println("-------arre ded-----"+totalHeadMap1);
//        //System.out.println("------prev ern------"+totalHeadMapprev);
//        //System.out.println("------prev ded------"+totalHeadMapprev1);
//         //System.out.println("------totalHeadAmount------"+totalHeadAmount);

        com.accure.payroll.manager.RoundRectangle roundRectangle = new com.accure.payroll.manager.RoundRectangle();
        outercell.setCellEvent(roundRectangle);
        outercell.setBorder(Rectangle.NO_BORDER);
        outercell.setBorderWidth(2);
        outercell.setPadding(8);

        outerTable.addCell(outercell);

        document.add(outerTable);
        //create a new action to send the document to our new destination.
        PdfAction action = PdfAction.gotoLocalPage(1, pdfDest, writer);

//set the open action for our writer object
        writer.setOpenAction(action);

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

    public List<ArrearProcess> getAlldetailsFromArrearForReport(List salarySlipList) throws DocumentException, FileNotFoundException, BadElementException, IOException, Exception {
        List<ArrearProcess> arrearList = new ArrayList<ArrearProcess>();
        List<ArrearProcess> newSortedList = new ArrayList<ArrearProcess>();
        if (salarySlipList != null && salarySlipList.size() > 0) {
            for (int j = 0; j < salarySlipList.size(); j++) {
                //get new heads from arrears
                String id = (String) salarySlipList.get(j);
                ArrearProcess arrearProcessObj = EmployeeArrearManager.fetch(id);
                if (arrearProcessObj != null) {
                    String autoSalId = arrearProcessObj.getIdStr();
                    //System.out.println("------autoSalId---" + autoSalId);
                    if (autoSalId != null) {
                        //get old heads from salary
                        AutoSalaryProcess autosalaryObj = AutoSalaryProcessManager.fetch(autoSalId);
                        if (autosalaryObj != null) {
                            Earnings salheadLists = autosalaryObj.getEarningsInfo();
                            if (salheadLists.isIsEarningHeads()) {
                                arrearProcessObj.setPrevEarningHeads(salheadLists.getEarningHeads());
                            }

                            Deductions dedheadLists = autosalaryObj.getDeductionsInfo();
                            if (dedheadLists.isIsDeductionHeads()) {
                                arrearProcessObj.setPrevDeductionHeads(dedheadLists.getDeductionHeads());
                            }
                            arrearList.add(arrearProcessObj);
                        }
                    }
                }
            }

            if (arrearList != null && arrearList.size() > 0) {
                for (ArrearProcess arrearProcess : arrearList) {
                    //creating new diff list
                    List<EarningHeadsDetails> diffEarHeadList = new ArrayList<EarningHeadsDetails>();
                    List<EarningHeadsDetails> diffDedHeadList = new ArrayList<EarningHeadsDetails>();
                    //new heads means  employyee heads
                    List<EarningHeadsDetails> earHeadList = new ArrayList<EarningHeadsDetails>();
                    List<EarningHeadsDetails> dedHeadList = new ArrayList<EarningHeadsDetails>();
                    HashMap<String, String> empcondMap = new HashMap<String, String>();
                    empcondMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                    empcondMap.put("ddo", arrearProcess.getDdo());
                    empcondMap.put("employeeCode", arrearProcess.getEmployeeCode());
                    empcondMap.put("status", ApplicationConstants.ACTIVE);
                    Employee emp = null;
                    String empResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, empcondMap);
                    if (empResult != null) {
                        List<Employee> emplists = new Gson().fromJson(empResult, new TypeToken<List<Employee>>() {
                        }.getType());
                        emp = emplists.get(0);
                        earHeadList = emp.getEarningHeads();
                        dedHeadList = emp.getDeductionHeads();
                    }
                    //prev heads
                    List<EarningHeadsDetails> prevEarHeadList = arrearProcess.getPrevEarningHeads();
                    List<EarningHeadsDetails> prevDedHeadList = arrearProcess.getPrevDeductionHeads();
                    //System.out.println("---------prevEarHeadList---" + new Gson().toJson(prevEarHeadList));
                    //System.out.println("---------prevDedHeadList---" + new Gson().toJson(prevDedHeadList));
                    //loop new heads
                    for (int ij = 0; ij < earHeadList.size(); ij++) {
                        EarningHeadsDetails aarningHeadsDetails = earHeadList.get(ij);
                        String earninHeadid = aarningHeadsDetails.getDescription();
                        double earninHeadVal = aarningHeadsDetails.getAmount();

                        //loop prev heads
                        for (EarningHeadsDetails tempObj : prevEarHeadList) {
                            EarningHeadsDetails diffErningHeadObj = new Gson().fromJson(new Gson().toJson(tempObj), new TypeToken<EarningHeadsDetails>() {
                            }.getType());

                            SalaryHead salHead = diffErningHeadObj.getDescriptionInfo();
                            String prevearninHeadid = (String) ((LinkedTreeMap) salHead.getId()).get("$oid");
                            double prevEarninHeadVal = diffErningHeadObj.getAmount();

                            if (earninHeadid.equals(prevearninHeadid)) {
                                double diffEarninHeadValue = (earninHeadVal - prevEarninHeadVal);
                                diffErningHeadObj.setAmount(diffEarninHeadValue);
                                diffEarHeadList.add(diffErningHeadObj);
                            }
                        }
                    }
                    arrearProcess.setDiffEarningHeads(diffEarHeadList);

                    for (int ij = 0; ij < dedHeadList.size(); ij++) {
                        EarningHeadsDetails dedHeadsDetail = dedHeadList.get(ij);
                        String dedHeadid = dedHeadsDetail.getDescription();
                        double dedHeadVal = dedHeadsDetail.getAmount();

                        for (EarningHeadsDetails tempObj : prevDedHeadList) {
                            EarningHeadsDetails diffDedHeadObj = new Gson().fromJson(new Gson().toJson(tempObj), new TypeToken<EarningHeadsDetails>() {
                            }.getType());

                            SalaryHead salHead = diffDedHeadObj.getDescriptionInfo();
                            String prevDedHeadid = (String) ((LinkedTreeMap) salHead.getId()).get("$oid");
                            double prevDedHeadVal = diffDedHeadObj.getAmount();

                            if (dedHeadid.equals(prevDedHeadid)) {
                                double diffDedHeadValue = dedHeadVal - prevDedHeadVal;
                                diffDedHeadObj.setAmount(diffDedHeadValue);
                                diffDedHeadList.add(diffDedHeadObj);
                            }
                        }
                    }
                    arrearProcess.setDiffDeductionHeads(diffDedHeadList);
                    arrearProcess.setEarningHeads(earHeadList);
                    arrearProcess.setDeductionHeads(dedHeadList);

                }
                //System.out.println("---------beffore---" + new Gson().toJson(newSortedList));
                Collections.sort(newSortedList, new ArrearSortManager());
                //System.out.println("---------aftr---" + new Gson().toJson(newSortedList.size()));
                Map<String, String> sortMap = new HashMap<String, String>();
                List<String> empCodeList = new ArrayList<String>();

                for (int i = 0; i < arrearList.size(); i++) {
                    String empCode = arrearList.get(i).getEmployeeCode();
                    if (empCodeList.contains(empCode)) {
                        continue;
                    } else {
                        empCodeList.add(empCode);
                    }
                }
                //System.out.println("---empCodeList---" + empCodeList);
                for (int ik = 0; ik < empCodeList.size(); ik++) {

                    String employeCode = empCodeList.get(ik);
                    List<ArrearProcess> monthwiseSortedList = new ArrayList<ArrearProcess>();
                    monthwiseSortedList.clear();
                    for (int i = 0; i < arrearList.size(); i++) {

                        String empCodenew = arrearList.get(i).getEmployeeCode();
                        if (employeCode.equals(empCodenew)) {
                            //System.out.println("---inside---");
                            monthwiseSortedList.add(arrearList.get(i));
                        }

                    }
                    //System.out.println("---outSide---");
                    Collections.sort(monthwiseSortedList, new ArrearSortByMonth());
                    //System.out.println("---outSide1---");

                    newSortedList.addAll(monthwiseSortedList);

                }

            }
        }
        return newSortedList;
    }

    public List<SalaryHead> getAllEarningHeads() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", ApplicationConstants.ACTIVE);
        map.put("active", "Yes");
        map.put("headType", "Earnings");
        //map.put("showOnRegister", "Yes");
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, map);
        List<SalaryHead> religionList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        return religionList;
    }

    public List<SalaryHead> getAllDeductionHeads() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", ApplicationConstants.ACTIVE);
        map.put("active", "Yes");
        map.put("headType", "Deductions");
        //map.put("showOnRegister", "Yes");
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, map);
        List<SalaryHead> religionList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        return religionList;
    }
}

class Rotate extends PdfPageEventHelper {

    protected PdfNumber orientation = PdfPage.PORTRAIT;

    public void setOrientation(PdfNumber orientation) {
        this.orientation = orientation;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        writer.addPageDictEntry(PdfName.ROTATE, orientation);
    }
}
