/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.FinancialYear;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.Deductions;
import com.accure.payroll.dto.Earnings;
import com.accure.payroll.dto.IncomeTax;
import com.accure.payroll.dto.InsuranceTransactions;
import com.accure.payroll.dto.LoanPayment;
import com.accure.payroll.dto.SalarySlipRegisterReport;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.getMonthString;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.roundTwoDecimalPoints;
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
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class MontlySalaryRegisterReport {

    public ByteArrayOutputStream generateSalaryRegisterReport(List<SalarySlipRegisterReport> salarySlipList, String path, int monthValue, int yearValue, String fin) throws DocumentException, FileNotFoundException, BadElementException, IOException, Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Document document = null;

        Rectangle pageSize = new Rectangle(1000f, 1000f);
        document = new Document(PageSize.A3.rotate());
        PdfWriter.getInstance(document, bos);
        document.open();
        PdfPTable outerTable = new PdfPTable(1);

        PdfPCell outercell = new PdfPCell();
        outerTable.setWidthPercentage(100f);

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        header.setSpacingAfter(20);

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(50.0f, 50.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColor(BaseColor.WHITE);
        ArrayList<InsuranceTransactions> insuranceobj = null;
        IncomeTax incomeTaxobj = null;
        ArrayList<LoanPayment> loanObj = null;
        String monthString = getMonthString(monthValue);

        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 30, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 17, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("  Monthly Salary Register : " + monthString + "-" + yearValue, FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD, BaseColor.BLACK))));
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

        Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 17, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(fin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 17, Font.BOLD, BaseColor.BLACK))));

        PdfPCell timecell = new PdfPCell(timePhrase);
        timecell.setBorderWidthBottom(1);
        timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        timecell.setBorderColor(BaseColor.WHITE);
        header.addCell(imagecell);
        header.addCell(headercell);
        header.addCell(timecell);

        outercell.addElement(header);
        SalarySlipRegisterReportManager srr = new SalarySlipRegisterReportManager();
        List<SalaryHead> earningsHead = srr.getAllEarningHeads();
        List<SalaryHead> deductionHeads = srr.getAllDeductionHeads();
        int earningsize = 0;
        int deductionsize = 0;
        int size = 0;
        if (earningsHead != null) {
            earningsize = earningsHead.size() / 3;
            if ((earningsHead.size() % 3) == 0) {
                earningsize = earningsize;
            } else {
                earningsize = earningsize + 1;
            }

        }
        if (deductionHeads != null) {
            deductionsize = (deductionHeads.size() / 3);
            if ((deductionHeads.size() % 3) == 0) {
                deductionsize = deductionsize;
            } else {
                deductionsize = deductionsize + 1;
            }
        }
        size = earningsize + deductionsize + 6;
        //System.out.println(size);

        PdfPTable columnHeading = new PdfPTable(size);
        columnHeading.setSpacingBefore(5.0f);
        int columns[] = new int[size];
        columns[0] = 1;
        for (int k = 1; k < size; k++) {
            columns[k] = 2;
        }
        columnHeading.setWidths(columns);
        columnHeading.setWidthPercentage(100);

        PdfPCell emptyCell = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        emptyCell.setPaddingLeft(1f);
        emptyCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell earningCellhead = new PdfPCell(new Phrase(new Chunk("Earnings", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        earningCellhead.setHorizontalAlignment(Element.ALIGN_CENTER);

        earningCellhead.setColspan(earningsize);

        PdfPCell deductionsCell = new PdfPCell(new Phrase(new Chunk("Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        deductionsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        deductionsCell.setPaddingLeft(1f);
        deductionsCell.setColspan(deductionsize + 1);

        columnHeading.addCell(emptyCell);
        columnHeading.addCell(emptyCell);
        columnHeading.addCell(earningCellhead);
        columnHeading.addCell(emptyCell);
        columnHeading.addCell(deductionsCell);
        columnHeading.addCell(emptyCell);
        columnHeading.addCell(emptyCell);
//        outercell.addElement(columnHeading);
//
//        columnHeading = new PdfPTable(size);
//        columnHeading.setSpacingBefore(5.0f);
//        columnHeading.setWidths(columns);
//        columnHeading.setWidthPercentage(100);

        PdfPCell snoCell = new PdfPCell(new Phrase(new Chunk("\nS.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        snoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        snoCell.setPaddingLeft(1f);
        PdfPCell namecell = new PdfPCell(new Phrase(new Chunk("Employee Code\n\n Employee Name\n\n Designation", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        namecell.setHorizontalAlignment(Element.ALIGN_CENTER);

        columnHeading.addCell(snoCell);
        columnHeading.addCell(namecell);

        PdfPCell earningCell = null;
        LinkedHashMap earningMap = new LinkedHashMap();
        if (earningsHead != null) {
            for (int earningHeadCount = 0; earningHeadCount < earningsHead.size();) {
                SalaryHead salaryHead1 = earningsHead.get(earningHeadCount);
                String head1 = "";
                if (salaryHead1.getDescription() != null && salaryHead1.getShortDescription() != "" && !salaryHead1.getShortDescription().isEmpty()) {
                    head1 = salaryHead1.getShortDescription();
                }
                earningMap.put(earningHeadCount, head1);
                ++earningHeadCount;
                String head2 = "";
                if (earningHeadCount < earningsHead.size()) {
                    SalaryHead salaryHead2 = earningsHead.get(earningHeadCount);
                    if (salaryHead2.getDescription() != null && salaryHead2.getShortDescription() != "" && !salaryHead2.getShortDescription().isEmpty()) {
                        head2 = salaryHead2.getShortDescription();
                    }
                }
                earningMap.put(earningHeadCount, head2);
                ++earningHeadCount;
                String head3 = "";
                if (earningHeadCount < earningsHead.size()) {
                    SalaryHead salaryHead3 = earningsHead.get(earningHeadCount);
                    if (salaryHead3.getDescription() != null && salaryHead3.getShortDescription() != "" && !salaryHead3.getShortDescription().isEmpty()) {
                        head3 = salaryHead3.getShortDescription();
                    }
                }
                earningMap.put(earningHeadCount, head3);

                ++earningHeadCount;

                String totalHead = "";
                if (head1 != null && head1 != "" && !head1.isEmpty()) {
                    totalHead += head1 + "\n\n";
                }
                if (head2 != null && head2 != "" && !head2.isEmpty()) {
                    totalHead += head2 + "\n\n";
                }
                if (head3 != null && head3 != "" && !head3.isEmpty()) {
                    totalHead += head3 + "\n\n";
                }

                earningCell = new PdfPCell(new Phrase(new Chunk(totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
                earningCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                columnHeading.addCell(earningCell);
            }
        }
        PdfPCell grossSalarycell = new PdfPCell(new Phrase(new Chunk("Gross Salary", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        grossSalarycell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        PdfPCell arrearcell = new PdfPCell(new Phrase(new Chunk("Arrear", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
//        arrearcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        columnHeading.addCell(grossSalarycell);
        //    columnHeading.addCell(arrearcell);

        PdfPCell DeductionsCell = null;

        LinkedHashMap deductionMap = new LinkedHashMap();
        int countdec = 0;
        if (deductionHeads != null) {

            for (int deductionHeadsCount = 0; deductionHeadsCount < deductionHeads.size();) {

                SalaryHead earningHead1 = deductionHeads.get(deductionHeadsCount);
                String deductionhead1 = "";
                if (earningHead1 != null) {
                    if (earningHead1.getShortDescription() != null && earningHead1.getShortDescription() != "" && !earningHead1.getShortDescription().isEmpty()) {
                        deductionhead1 = earningHead1.getShortDescription();
                    }
                }
                deductionMap.put(deductionHeadsCount, deductionhead1);
                ++deductionHeadsCount;
                String deductionhead2 = "";
                if (deductionHeadsCount < deductionHeads.size()) {
                    SalaryHead earningHead2 = deductionHeads.get(deductionHeadsCount);
                    if (earningHead2 != null) {
                        if (earningHead2.getShortDescription() != null && earningHead2.getShortDescription() != "" && !earningHead2.getShortDescription().isEmpty()) {
                            deductionhead2 = earningHead2.getShortDescription();
                        }
                    }
                }
                deductionMap.put(deductionHeadsCount, deductionhead2);
                ++deductionHeadsCount;
                String deductionhead3 = "";
                if (deductionHeadsCount < deductionHeads.size()) {
                    SalaryHead earningHead3 = deductionHeads.get(deductionHeadsCount);
                    if (earningHead3 != null) {
                        if (earningHead3.getShortDescription() != null && earningHead3.getShortDescription() != "" && !earningHead3.getShortDescription().isEmpty()) {
                            deductionhead3 = earningHead3.getShortDescription();
                        }
                    }
                }
                deductionMap.put(deductionHeadsCount, deductionhead3);
                ++deductionHeadsCount;

                String totalHead = "";
                if (deductionhead1 != null && deductionhead1 != "" && !deductionhead1.isEmpty()) {
                    totalHead += deductionhead1 + "\n\n";
                }
                if (deductionhead2 != null && deductionhead2 != "" && !deductionhead2.isEmpty()) {
                    totalHead += deductionhead2 + "\n\n";
                }
                if (deductionhead3 != null && deductionhead3 != "" && !deductionhead3.isEmpty()) {
                    totalHead += deductionhead3 + "\n\n";
                }
                countdec = deductionHeadsCount;

                DeductionsCell = new PdfPCell(new Phrase(new Chunk(totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
                DeductionsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                columnHeading.addCell(DeductionsCell);
            }

        }
//        deductionMap.put(countdec , "INCOME TAX");
        //System.out.println("deductionMap" + deductionMap);
        //System.out.println("earningMap" + earningMap);

        PdfPCell TaxCell = new PdfPCell(new Phrase(new Chunk("INCOME TAX", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        TaxCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell totalDeductionCell = new PdfPCell(new Phrase(new Chunk("Total Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        totalDeductionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell totalNetSalaryCell = new PdfPCell(new Phrase(new Chunk("Total Net Amount", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
        totalNetSalaryCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        columnHeading.addCell(TaxCell);
        columnHeading.addCell(totalDeductionCell);
        columnHeading.addCell(totalNetSalaryCell);

        outercell.addElement(columnHeading);
        int month = 0;
        int year = 0;
        String name = "";
        String code = "";
        int attendacne = 0;
        String designation = "";
        int count = 0;
        if (salarySlipList != null && salarySlipList.size() > 0) {

            for (SalarySlipRegisterReport salarylist : salarySlipList) {
                count++;
                double totalEarnings = 0.00;
                double totalDeductions = 0.00;
                double netpayment = 0.00;
                double incomeTax = 0.00;
                name = salarylist.getEmployeeName();
                year = salarylist.getYear();
                month = salarylist.getMonth();
                designation = salarylist.getDesignationName();
                attendacne = salarylist.getAttendance().getPresent();
                code = salarylist.getEmployeeCode();
                LinkedHashMap<String, String> deductions = new LinkedHashMap<String, String>();
                LinkedHashMap<String, String> Earnings = new LinkedHashMap<String, String>();
                Earnings earingInfo = salarylist.getEarningsInfo();
                Deductions deductionInfo = salarylist.getDeductionsInfo();

                ArrayList<EarningHeadsDetails> earningList = earingInfo.getEarningHeads();
                ArrayList<EarningHeadsDetails> deductionList = deductionInfo.getDeductionHeads();

                if (earingInfo != null) {
                    if (earingInfo.isIsEarningHeads()) {
                        if (earningList != null && earningList.size() > 0) {
                            for (int i = 0; i < earningList.size(); i++) {
                                String showonRegister = earningList.get(i).getDescriptionInfo().getShowOnRegister();

                                if (earningList.get(i).getAmount() != 0.0 && showonRegister.equalsIgnoreCase("Yes")) {
                                    totalEarnings += earningList.get(i).getAmount();
                                    Earnings.put(earningList.get(i).getDescriptionInfo().getShortDescription(), roundTwoDecimalPoints(earningList.get(i).getAmount()));
                                }

                            }
                        }
                    }

                }

                if (deductionInfo != null) {
                    if (deductionInfo.isIsDeductionHeads()) {
                        if (deductionList != null && deductionList.size() > 0) {
                            for (int i = 0; i < deductionList.size(); i++) {
                                String showonRegister = deductionList.get(i).getDescriptionInfo().getShowOnRegister();
                                if (deductionList.get(i).getAmount() != 0.0 && showonRegister.equalsIgnoreCase("Yes")) {
                                    totalDeductions += deductionList.get(i).getAmount();
                                    deductions.put(deductionList.get(i).getDescriptionInfo().getShortDescription(), roundTwoDecimalPoints(deductionList.get(i).getAmount()));
                                }
                            }
                        }
                        if (deductionInfo.isIsInsurance()) {
                            insuranceobj = deductionInfo.getInsurance();
                            for (int i = 0; i < insuranceobj.size(); i++) {
                                    if (insuranceobj.get(i).getInstallmentAmount() != 0.0) {
                                        totalDeductions += insuranceobj.get(i).getInstallmentAmount();
                                        double insurance = insuranceobj.get(i).getInstallmentAmount();
                                        deductions.put(insuranceobj.get(i).getInsInfo().getShortDescription(), roundTwoDecimalPoints(insurance));
                                    }

                                }
                        }

                        if (deductionInfo.isIsIncomeTax()) {
                            incomeTaxobj = deductionInfo.getIncomeTax();
//                            totalDeductions += incomeTaxobj.getIt();
//                            incomeTax = incomeTaxobj.getIt();
                            totalDeductions += incomeTaxobj.getTotal();
                            incomeTax = incomeTaxobj.getTotal();
                            deductions.put("INCOME TAX", roundTwoDecimalPoints(incomeTax));
                        }
                        if (deductionInfo.isIsLoan()) {
                            loanObj = deductionInfo.getLoan();
                            double interest = 0.00;
                            for (int i = 0; i < loanObj.size(); i++) {
                                if (loanObj.get(i).getInterestPaid() != 0.0) {
                                    totalDeductions += loanObj.get(i).getInterestPaid();
                                    interest = loanObj.get(i).getInterestPaid();
                                }
                                totalDeductions += loanObj.get(i).getPaidAmount();
                                deductions.put(loanObj.get(i).getLoanInfo().getShortDescription(), roundTwoDecimalPoints(loanObj.get(i).getPaidAmount() + interest));
                            }
                        }
                    }
                }

                PdfPTable employeeData = new PdfPTable(size);
                employeeData.setWidths(columns);
                employeeData.setWidthPercentage(100);

                PdfPCell Cell1 = new PdfPCell(new Phrase(new Chunk("\n" + String.valueOf(count), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                snoCell.setPaddingLeft(1f);
                PdfPCell Cell2 = new PdfPCell(new Phrase(new Chunk(code + "\n\n" + name + "\n\n" + designation, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                Cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

                employeeData.addCell(Cell1);
                employeeData.addCell(Cell2);

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
                        String des1 = (String) earningMap.get(list.get(k));
                        String value1 = (String) Earnings.get(des1);
                        if (value1 == null) {
                            value1 = "";
                        }
                        k++;
                        String value2 = "";
                        if (k < list.size()) {
                            String des2 = (String) earningMap.get(list.get(k));
                            value2 = (String) Earnings.get(des2);
                            if (value2 == null) {
                                value2 = "";
                            }
                        }
                        k++;
                        String value3 = "";
                        if (k < list.size()) {
                            String des3 = (String) earningMap.get(list.get(k));
                            value3 = (String) Earnings.get(des3);
                            if (value3 == null) {
                                value3 = "";
                            }
                        }
                        k++;

                        String totalHead = "";
                        if (value1 != null && value1 != "" && !value1.isEmpty()) {
                            totalHead += value1;
                        } else {
                            totalHead += "0.00";
                        }
                        totalHead += "\n\n";
                        if (value2 != null && value2 != "" && !value2.isEmpty()) {
                            totalHead += value2;
                        } else {
                            totalHead += "0.00";
                        }
                        totalHead += "\n\n";
                        if (value2 != null && value3 != "" && !value3.isEmpty()) {
                            totalHead += value3;
                        } else {
                            totalHead += "0.00";
                        }
                        totalHead += "\n\n";

                        Cell1 = new PdfPCell(new Phrase(new Chunk(totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        employeeData.addCell(Cell1);

                    }
                }
                PdfPCell grosscell = new PdfPCell(new Phrase(new Chunk("\n\n" + roundTwoDecimalPoints(totalEarnings) + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                grosscell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                PdfPCell arrearcellData = new PdfPCell(new Phrase(new Chunk("Arrear", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
//                  arrearcellData.setHorizontalAlignment(Element.ALIGN_CENTER);
                employeeData.addCell(grosscell);
                //     employeeData.addCell(arrearcellData);

                Set<Integer> deductionkey = null;
                if (deductions.size() > 0) {
                    if (deductionMap != null && !deductionMap.isEmpty() && !deductionMap.equals("{}")) {
                        deductionkey = deductionMap.keySet();
                    }
                    if (deductionkey != null && !deductionkey.equals("[]") && deductionkey.size() > 0) {
                        ArrayList list1 = new ArrayList();
                        for (Integer key : deductionkey) {
                            list1.add(key);
                        }
                        for (int k = 0; k < list1.size();) {
                            String des1 = (String) deductionMap.get(list1.get(k));
                            String value1 = (String) deductions.get(des1);
                            if (value1 == null) {
                                value1 = "";
                            }
                            k++;
                            String value2 = "";
                            if (k < list1.size()) {
                                String des2 = (String) deductionMap.get(list1.get(k));
                                value2 = (String) deductions.get(des2);
                                if (value2 == null) {
                                    value2 = "";
                                }
                            }
                            k++;
                            String value3 = "";
                            if (k < list1.size()) {
                                String des3 = (String) deductionMap.get(list1.get(k));
                                value3 = (String) deductions.get(des3);
                                if (value3 == null) {
                                    value3 = "";
                                }
                            }
                            k++;

                            String totalHead = "";
                            if (value1 != null && value1 != "" && !value1.isEmpty()) {
                                totalHead += value1;
                            } else {
                                totalHead += "0.00";
                            }
                            totalHead += "\n\n";
                            if (value2 != null && value2 != "" && !value2.isEmpty()) {
                                totalHead += value2;
                            } else {
                                totalHead += "0.00";
                            }
                            totalHead += "\n\n";
                            if (value3 != null && value3 != "" && !value3.isEmpty()) {
                                totalHead += value3;
                            } else {
                                totalHead += "0.00";
                            }
                            totalHead += "\n\n";
                            Cell1 = new PdfPCell(new Phrase(new Chunk(totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                            Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            employeeData.addCell(Cell1);

                        }
                    }
                } else {
                    for (int i = 0; i < deductionsize; i++) {
                        Cell1 = new PdfPCell(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        employeeData.addCell(Cell1);
                    }
                }
                String incomeTaxStr = "";
                if (incomeTax != 0) {
                    incomeTaxStr = roundTwoDecimalPoints(incomeTax);
                }
                Cell1 = new PdfPCell(new Phrase(new Chunk("\n\n" + incomeTaxStr + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                //System.out.println("totalDeductions" + totalDeductions);
                netpayment = totalEarnings - totalDeductions;
                PdfPCell totalDeductionCellData = new PdfPCell(new Phrase(new Chunk("\n\n" + roundTwoDecimalPoints(totalDeductions) + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                totalDeductionCellData.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell totalNetSalaryCellData = new PdfPCell(new Phrase(new Chunk("\n\n" + roundTwoDecimalPoints(netpayment) + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
                totalNetSalaryCellData.setHorizontalAlignment(Element.ALIGN_CENTER);
                employeeData.addCell(Cell1);
                employeeData.addCell(totalDeductionCellData);
                employeeData.addCell(totalNetSalaryCellData);
                outercell.addElement(employeeData);

            }

        }
        com.accure.payroll.manager.RoundRectangle roundRectangle = new com.accure.payroll.manager.RoundRectangle();
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
