/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.leave.dto.LeaveEncashment;
import com.accure.payroll.dto.ArrearProcess;
import com.accure.payroll.dto.Deductions;
import com.accure.payroll.dto.Earnings;
import com.accure.payroll.dto.IncomeTax;
import com.accure.payroll.dto.InsuranceTransactions;
import com.accure.payroll.dto.LoanPayment;
import com.accure.payroll.dto.SalarySlipRegisterReport;
import com.accure.usg.common.manager.ConvertMoneyToNumberMain;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.File;
import com.itextpdf.text.Image;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class SalarySlipRportPDFGeneration {

    static LinkedHashMap<String, Double> Cumalativedeductions = new LinkedHashMap<String, Double>();
    static LinkedHashMap<String, Double> CumalativeEarnings = new LinkedHashMap<String, Double>();
    static double totalCumulativeEarnings = 0.00;
    static double totalCumulativeDeductions = 0.00;

    public ByteArrayOutputStream generateSalaryReport(List<SalarySlipRegisterReport> salarySlipList, String path, String financialyearStart, String financialyearEnd) throws DocumentException, FileNotFoundException, BadElementException, IOException, Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        double totalEarnings = 0.00;
        double totalDeductions = 0.00;
        double netpayment = 0.00;
        String salaryProcessType = "";

        Document document = new Document();
        PdfWriter.getInstance(document, bos);
        document.open();

        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();
        outerTable.setWidthPercentage(100);

        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{40, 200});

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(70.0f, 70.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderWidthBottom(0.5f);
        imagecell.setBorderColorBottom(BaseColor.BLACK);
        imagecell.setPaddingBottom(10);

        ArrayList<InsuranceTransactions> insuranceobj = null;
        IncomeTax incomeTaxobj = null;
        ArrayList<LoanPayment> loanObj = null;
        String employeeCodes = "";
        int months = 0;
        int eyear = 0;

        if (salarySlipList != null && salarySlipList.size() > 0) {

            for (SalarySlipRegisterReport salarylist : salarySlipList) {
                LinkedHashMap<String, Double> deductions = new LinkedHashMap<String, Double>();
                LinkedHashMap<String, Double> Earnings = new LinkedHashMap<String, Double>();
                Earnings earingInfo = salarylist.getEarningsInfo();
                Deductions deductionInfo = salarylist.getDeductionsInfo();
                ArrearProcess arrearprocess = salarylist.getArrear();

                employeeCodes = salarylist.getEmployeeCode();
                months = salarylist.getMonth();
                eyear = salarylist.getYear();
                salaryProcessType = salarylist.getSalaryProcessType();
                String ddo = "";
                ddo = salarylist.getDdo();
                //System.out.println("ddo" + ddo);
                SalarySlipRportPDFGeneration srg = new SalarySlipRportPDFGeneration();
                srg.calculateCumulative(employeeCodes, months, eyear, financialyearStart, financialyearEnd, ddo);

                ArrayList<EarningHeadsDetails> earningList = earingInfo.getEarningHeads();
                ArrayList<EarningHeadsDetails> deductionList = deductionInfo.getDeductionHeads();
                LeaveEncashment leaveEnhancementList = earingInfo.getLeaveEncashment();

                if (earingInfo != null) {
                    if (earingInfo.isIsEarningHeads()) {
                        if (earningList != null && earningList.size() > 0) {
                            Collections.sort(earningList, new Comparator<EarningHeadsDetails>() {
                                public int compare(EarningHeadsDetails s1, EarningHeadsDetails s2) {
                                    return s1.getDescriptionInfo().getDisplayLevel() - s2.getDescriptionInfo().getDisplayLevel();
                                }
                            });
                            for (int i = 0; i < earningList.size(); i++) {
                                String showonSlip = earningList.get(i).getDescriptionInfo().getShowOnSalarySlip();

                                if (earningList.get(i).getAmount() != 0 && showonSlip.equalsIgnoreCase("Yes")) {
                                    totalEarnings += earningList.get(i).getAmount();
                                    Earnings.put(earningList.get(i).getDescription(), earningList.get(i).getAmount());
                                }

                            }
                        }
                    }
                    if (earingInfo.isIsLeaveEncashment()) {
                        totalEarnings += leaveEnhancementList.getTotalAmount();
                        Earnings.put(leaveEnhancementList.getLeInfo().getShortDescription(), leaveEnhancementList.getTotalAmount());
                    }
                }
                if (salarylist.isIsArrear()) {
                    totalEarnings += arrearprocess.getTotalEarnings();
                    Earnings.put("ARREAR", (arrearprocess.getTotalEarnings()));
                }

                if (deductionInfo != null) {
                    if (deductionInfo.isIsDeductionHeads()) {
                        if (deductionList != null && deductionList.size() > 0) {
                            Collections.sort(deductionList, new Comparator<EarningHeadsDetails>() {
                                public int compare(EarningHeadsDetails s1, EarningHeadsDetails s2) {
                                    return s1.getDescriptionInfo().getDisplayLevel() - s2.getDescriptionInfo().getDisplayLevel();
                                }
                            });

                            for (int i = 0; i < deductionList.size(); i++) {
                                String showonSlip = deductionList.get(i).getDescriptionInfo().getShowOnSalarySlip();
                                if (deductionList.get(i).getAmount() != 0 && showonSlip.equalsIgnoreCase("Yes")) {
                                    totalDeductions += deductionList.get(i).getAmount();
                                    deductions.put(deductionList.get(i).getDescription(), (deductionList.get(i).getAmount()));
                                }
                            }
                        }
                    }
                    if (deductionInfo.isIsInsurance()) {
                        insuranceobj = deductionInfo.getInsurance();
                        for (int i = 0; i < insuranceobj.size(); i++) {
                            if (insuranceobj.get(i).getInstallmentAmount() != 0.0) {
                                totalDeductions += insuranceobj.get(i).getInstallmentAmount();
                                double insurance = insuranceobj.get(i).getInstallmentAmount();
                                deductions.put(insuranceobj.get(i).getInsInfo().getShortDescription(), (insurance));
                            }
                        }
                    }
                    if (deductionInfo.isIsIncomeTax()) {
                        incomeTaxobj = deductionInfo.getIncomeTax();
//                        totalDeductions += incomeTaxobj.getIt();
//                        double incomeTax = incomeTaxobj.getIt();
                        totalDeductions += incomeTaxobj.getTotal();
                        double incomeTax = incomeTaxobj.getTotal();
                        deductions.put("INCOME TAX", (incomeTax));
                    }
                    if (deductionInfo.isIsLoan()) {
                        loanObj = deductionInfo.getLoan();
                        for (int i = 0; i < loanObj.size(); i++) {
                            if (loanObj.get(i).getInterestPaid() != 0.0) {
                                totalDeductions += loanObj.get(i).getInterestPaid();
                                deductions.put(loanObj.get(i).getLoanInfo().getShortDescription() + " INTEREST", (loanObj.get(i).getInterestPaid()));
                            }
                            totalDeductions += loanObj.get(i).getPaidAmount();
                            deductions.put(loanObj.get(i).getLoanInfo().getShortDescription(), (loanObj.get(i).getPaidAmount()));
                        }
                    }
                    if (salarylist.isIsArrear()) {
                        totalDeductions += arrearprocess.getTotalDeductions();
                        deductions.put("ARREAR", (arrearprocess.getTotalDeductions()));
                    }

                }

                int month = salarylist.getMonth();
                //System.out.println("1" + Earnings.toString());
                //System.out.println("2" + deductions.toString());
                String payStopValue = "";
                if (salaryProcessType.equalsIgnoreCase("paystopsalary")) {
                    payStopValue = "(Pay Stopped)";
                }

                String monthString = getMonthString(month);
                Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
                headerPhrase.add(new Phrase(new Chunk("\n")));
                headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
                headerPhrase.add(new Phrase(new Chunk("\n")));
                headerPhrase.add(new Phrase(new Chunk("    Salary Statement for the Month of " + monthString + " " + salarylist.getYear() + payStopValue, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD, BaseColor.BLACK))));
                headerPhrase.add(new Phrase(new Chunk("\n")));

                PdfPCell headercell = new PdfPCell(headerPhrase);
                headercell.setBorderWidthBottom(1);
                headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headercell.setPaddingLeft(10.0f);
                headercell.setBorderColor(BaseColor.WHITE);
                headercell.setBorderWidthBottom(0.5f);
                headercell.setBorderColorBottom(BaseColor.BLACK);
                headercell.setPaddingBottom(10);

                header.addCell(imagecell);
                header.addCell(headercell);
                //header.addCell(headercell);

                outercell.addElement(header);

                // outerTable.addCell(header);
                PdfPTable table8 = new PdfPTable(2);
                table8.setWidthPercentage(100.0f);
                table8.setSpacingBefore(5f);
                table8.setSpacingAfter(5f);

                String employeeCode = "";
                if (salarylist.getEmployeeCode() != null && salarylist.getEmployeeCode() != "" && !salarylist.getEmployeeCode().isEmpty()) {
                    employeeCode = salarylist.getEmployeeCode();
                }

                Phrase datePhrase81 = new Phrase(new Chunk("Employee No : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                datePhrase81.add(new Phrase(new Chunk(employeeCode, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
                PdfPCell cell81 = new PdfPCell(datePhrase81);
                cell81.setBorder(Rectangle.NO_BORDER);
                String Designation = "";
                if (salarylist.getDesignationName() != null && salarylist.getDesignationName() != "" && !salarylist.getDesignationName().isEmpty()) {
                    Designation = salarylist.getDesignationName();
                }

                Phrase datePhrase82 = new Phrase(new Chunk("Designation : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                datePhrase82.add(new Phrase(new Chunk(Designation, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
                PdfPCell cell82 = new PdfPCell(datePhrase82);
                cell82.setBorder(Rectangle.NO_BORDER);

                String employeeName = "";
                if (salarylist.getEmployeeName() != null && salarylist.getEmployeeName() != "" && !salarylist.getEmployeeName().isEmpty()) {
                    employeeName = salarylist.getEmployeeName();
                }

                Phrase datePhrase83 = new Phrase(new Chunk("Employee Name : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                datePhrase83.add(new Phrase(new Chunk(employeeName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
                PdfPCell cell83 = new PdfPCell(datePhrase83);
                cell83.setBorder(Rectangle.NO_BORDER);

                String panno = "";
                if (salarylist.getPanNo() != null && salarylist.getPanNo() != "" && !salarylist.getPanNo().isEmpty()) {
                    panno = salarylist.getPanNo();
                }
                String Department = "";
                if (salarylist.getDepartmentName() != null && salarylist.getDepartmentName() != "" && !salarylist.getDepartmentName().isEmpty()) {
                    Department = salarylist.getDepartmentName();
                }

                Phrase datePhrase84 = new Phrase(new Chunk("Department : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                datePhrase84.add(new Phrase(new Chunk(Department, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
                PdfPCell cell84 = new PdfPCell(datePhrase84);
                cell84.setBorder(Rectangle.NO_BORDER);

                Phrase datePhrase85 = new Phrase(new Chunk("PAN : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                datePhrase85.add(new Phrase(new Chunk(panno, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
                PdfPCell cell85 = new PdfPCell(datePhrase85);
                cell85.setBorder(Rectangle.NO_BORDER);

                Phrase datePhrase86 = new Phrase(new Chunk("Division : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                datePhrase86.add(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
                PdfPCell cell86 = new PdfPCell(datePhrase86);
                cell86.setBorder(Rectangle.NO_BORDER);

                String salarytype = "";
                if (salarylist.getSalaryType() != null && salarylist.getSalaryType() != "" && !salarylist.getSalaryType().isEmpty()) {
                    salarytype = salarylist.getSalaryType();
                }
                String Grade = "";
                if (salarylist.getGradeName() != null && salarylist.getGradeName() != "" && !salarylist.getGradeName().isEmpty()) {
                    Grade = salarylist.getGradeName();
                }

                Phrase datePhrase87 = new Phrase(new Chunk("Grade : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                datePhrase87.add(new Phrase(new Chunk(Grade, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
                PdfPCell cell87 = new PdfPCell(datePhrase87);
                cell87.setBorder(Rectangle.NO_BORDER);

                String PFScheme = "";
                if (salarylist.getPfTypeName() != null && salarylist.getPfTypeName() != "" && !salarylist.getPfTypeName().isEmpty()) {
                    PFScheme = salarylist.getPfTypeName();
                }
                String accountno = "";
                if (salarylist.getAcnumber() != null && salarylist.getAcnumber() != "" && !salarylist.getAcnumber().isEmpty()) {
                    accountno = salarylist.getAcnumber();
                }

                Phrase datePhrase89 = new Phrase(new Chunk("BANK A/C  No : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                datePhrase89.add(new Phrase(new Chunk(accountno, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
                PdfPCell cell89 = new PdfPCell(datePhrase89);
                cell89.setBorder(Rectangle.NO_BORDER);
                //cell88.setBorderWidthBottom(0.5f);
                //  cell88.setBorderColorBottom(BaseColor.BLACK);

                Phrase datePhrase810 = new Phrase(new Chunk("PF Scheme  : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                datePhrase810.add(new Phrase(new Chunk(PFScheme, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
                PdfPCell cell10 = new PdfPCell(datePhrase810);
                cell10.setBorder(Rectangle.NO_BORDER);
                cell10.setPaddingBottom(10.0f);

                Phrase datePhrase811 = new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));

                datePhrase811.add(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));

                PdfPCell cell11 = new PdfPCell(datePhrase811);
                cell11.setBorder(Rectangle.NO_BORDER);

                cell11.setPaddingBottom(10.0f);

                table8.addCell(cell81);
                table8.addCell(cell82);
                table8.addCell(cell83);
                table8.addCell(cell84);
                table8.addCell(cell85);
                table8.addCell(cell86);
                table8.addCell(cell87);
                table8.addCell(cell89);
                table8.addCell(cell10);
                table8.addCell(cell11);
                outercell.addElement(table8);

                PdfPTable salaryDetailsTable = new PdfPTable(6);
                salaryDetailsTable.setWidthPercentage(100.0f);
                salaryDetailsTable.setSpacingBefore(2f);
                salaryDetailsTable.setSpacingAfter(2f);

                PdfPCell earningsDetailsTable = new PdfPCell(new Phrase("Earnings", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                earningsDetailsTable.setBorder(Rectangle.NO_BORDER);
                earningsDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                earningsDetailsTable.setBorderColor(BaseColor.WHITE);
                earningsDetailsTable.setBorderWidthBottom(0.5f);
                earningsDetailsTable.setBorderColorBottom(BaseColor.BLACK);
                earningsDetailsTable.setPaddingBottom(5f);
                earningsDetailsTable.setBorderColorTop(BaseColor.BLACK);
                earningsDetailsTable.setPaddingTop(5f);
                earningsDetailsTable.setBackgroundColor(BaseColor.LIGHT_GRAY);
                earningsDetailsTable.setBorderWidthTop(0.5f);
                earningsDetailsTable.setBorderWidthRight(0.5f);
                earningsDetailsTable.setBorderColorRight(BaseColor.BLACK);
                earningsDetailsTable.setBorderWidthLeft(0.5f);
                earningsDetailsTable.setBorderColorLeft(BaseColor.BLACK);

                PdfPCell deductionDetailsTable = new PdfPCell(new Phrase("Amount", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                deductionDetailsTable.setBorder(Rectangle.NO_BORDER);
                deductionDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                deductionDetailsTable.setBorderColor(BaseColor.WHITE);
                deductionDetailsTable.setBorderWidthBottom(0.5f);
                deductionDetailsTable.setBorderColorBottom(BaseColor.BLACK);
                deductionDetailsTable.setPaddingBottom(5f);
                deductionDetailsTable.setBorderColorTop(BaseColor.BLACK);
                deductionDetailsTable.setPaddingTop(5f);
                deductionDetailsTable.setBorderWidthTop(0.5f);
                deductionDetailsTable.setBorderWidthRight(0.5f);
                deductionDetailsTable.setBorderColorRight(BaseColor.BLACK);
                deductionDetailsTable.setBackgroundColor(BaseColor.LIGHT_GRAY);

                PdfPCell cumulativeDetailsTable = new PdfPCell(new Phrase("Cumulative", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                cumulativeDetailsTable.setBorder(Rectangle.NO_BORDER);
                cumulativeDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                cumulativeDetailsTable.setBorderColor(BaseColor.WHITE);
                cumulativeDetailsTable.setBorderWidthBottom(0.5f);
                cumulativeDetailsTable.setBorderColorBottom(BaseColor.BLACK);
                cumulativeDetailsTable.setPaddingBottom(5f);
                cumulativeDetailsTable.setBorderColorTop(BaseColor.BLACK);
                cumulativeDetailsTable.setPaddingTop(5f);
                cumulativeDetailsTable.setBorderWidthTop(0.5f);
                cumulativeDetailsTable.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cumulativeDetailsTable.setBorderWidthRight(0.5f);
                cumulativeDetailsTable.setBorderColorRight(BaseColor.BLACK);

                PdfPCell deductionsDetailsTable = new PdfPCell(new Phrase("Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                deductionsDetailsTable.setBorder(Rectangle.NO_BORDER);
                deductionsDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                deductionsDetailsTable.setBorderColor(BaseColor.WHITE);
                deductionsDetailsTable.setBorderWidthBottom(0.5f);
                deductionsDetailsTable.setBorderColorBottom(BaseColor.BLACK);
                deductionsDetailsTable.setPaddingBottom(5f);
                deductionsDetailsTable.setBorderColorTop(BaseColor.BLACK);
                deductionsDetailsTable.setPaddingTop(5f);
                deductionsDetailsTable.setBorderWidthTop(0.5f);
                deductionsDetailsTable.setBackgroundColor(BaseColor.LIGHT_GRAY);
                deductionsDetailsTable.setBorderWidthRight(0.5f);
                deductionsDetailsTable.setBorderColorRight(BaseColor.BLACK);

                PdfPCell amountDetailsTable = new PdfPCell(new Phrase("Amount", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                amountDetailsTable.setBorder(Rectangle.NO_BORDER);
                amountDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                amountDetailsTable.setBorderColor(BaseColor.WHITE);
                amountDetailsTable.setBorderWidthBottom(0.5f);
                amountDetailsTable.setBorderColorBottom(BaseColor.BLACK);
                amountDetailsTable.setPaddingBottom(5f);
                amountDetailsTable.setBorderColorTop(BaseColor.BLACK);
                amountDetailsTable.setPaddingTop(5f);
                amountDetailsTable.setBorderWidthTop(0.5f);
                amountDetailsTable.setBackgroundColor(BaseColor.LIGHT_GRAY);
                amountDetailsTable.setBorderWidthRight(0.5f);
                amountDetailsTable.setBorderColorRight(BaseColor.BLACK);

                PdfPCell deductioncumulativeDetailsTable = new PdfPCell(new Phrase("Cumulative", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                deductioncumulativeDetailsTable.setBorder(Rectangle.NO_BORDER);
                deductioncumulativeDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                deductioncumulativeDetailsTable.setBorderColor(BaseColor.WHITE);
                deductioncumulativeDetailsTable.setBorderWidthBottom(0.5f);
                deductioncumulativeDetailsTable.setBorderColorBottom(BaseColor.BLACK);
                deductioncumulativeDetailsTable.setPaddingBottom(5f);
                deductioncumulativeDetailsTable.setBorderColorTop(BaseColor.BLACK);
                deductioncumulativeDetailsTable.setPaddingTop(5f);
                deductioncumulativeDetailsTable.setBorderWidthTop(0.5f);
                deductioncumulativeDetailsTable.setBackgroundColor(BaseColor.LIGHT_GRAY);
                deductioncumulativeDetailsTable.setBorderWidthRight(0.5f);
                deductioncumulativeDetailsTable.setBorderColorRight(BaseColor.BLACK);

                salaryDetailsTable.addCell(earningsDetailsTable);
                salaryDetailsTable.addCell(deductionDetailsTable);
                salaryDetailsTable.addCell(cumulativeDetailsTable);
                salaryDetailsTable.addCell(deductionsDetailsTable);
                salaryDetailsTable.addCell(amountDetailsTable);
                salaryDetailsTable.addCell(deductioncumulativeDetailsTable);
                //outercell.addElement(salaryDetailsTable);
                PdfPCell keycell = null;
                PdfPCell valuecell = null;
                PdfPCell cumulativecell = null;
                String earningAmount = "";

                if (deductions.size() == Cumalativedeductions.size()) {
                } else if (deductions.size() > Cumalativedeductions.size()) {

                } else if ((deductions.size() < Cumalativedeductions.size())) {
                    for (String str : Cumalativedeductions.keySet()) {
                        if (!deductions.containsKey(str)) {
                            deductions.put(str, 0.00);
                        }
                    }
                }

                if (Earnings.size() == CumalativeEarnings.size()) {
                } else if (Earnings.size() > CumalativeEarnings.size()) {

                } else if ((Earnings.size() < CumalativeEarnings.size())) {
                    for (String str : CumalativeEarnings.keySet()) {
                        if (!Earnings.containsKey(str)) {
                            Earnings.put(str, 0.00);
                        }
                    }
                }

                if (Earnings.size() > deductions.size()) {
                    Iterator<Map.Entry<String, Double>> iter = Earnings.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Double> entry = iter.next();
                        if (deductions.size() > 0) {
                            Iterator<Map.Entry<String, Double>> iter1 = deductions.entrySet().iterator();
                            while (iter1.hasNext()) {
                                Map.Entry<String, Double> entry1 = iter1.next();
                                keycell = new PdfPCell(new Phrase((entry.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                keycell.setBorder(Rectangle.NO_BORDER);
                                keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                keycell.setBorderColor(BaseColor.WHITE);
                                keycell.setBorderWidthRight(0.5f);
                                keycell.setBorderColorRight(BaseColor.BLACK);
                                keycell.setBorderWidthLeft(0.5f);
                                keycell.setBorderColorLeft(BaseColor.BLACK);
                                salaryDetailsTable.addCell(keycell);
                                earningAmount = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Earnings.get(entry.getKey()));
                                valuecell = new PdfPCell(new Phrase(earningAmount, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                valuecell.setBorder(Rectangle.NO_BORDER);
                                valuecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                valuecell.setBorderColor(BaseColor.WHITE);
                                valuecell.setBorderWidthRight(0.5f);
                                valuecell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(valuecell);

                                String cumulativeearnings = "";
                                if (CumalativeEarnings.containsKey(entry.getKey())) {
                                    cumulativeearnings = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(CumalativeEarnings.get(entry.getKey()));
                                }
                                // String earningsValue = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(CumalativeEarnings.get(entry.getKey()));
                                cumulativecell = new PdfPCell(new Phrase(cumulativeearnings, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                cumulativecell.setBorder(Rectangle.NO_BORDER);
                                cumulativecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cumulativecell.setBorderColor(BaseColor.WHITE);
                                cumulativecell.setBorderWidthRight(0.5f);
                                cumulativecell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(cumulativecell);
                                earningAmount = "";
                                PdfPCell deductionKeycell = null;
                                PdfPCell deductionValuecell = null;
                                String deductionAmount = "";
                                deductionKeycell = new PdfPCell(new Phrase((entry1.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                deductionKeycell.setBorder(Rectangle.NO_BORDER);
                                deductionKeycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                deductionKeycell.setBorderColor(BaseColor.WHITE);
                                deductionKeycell.setBorderWidthRight(0.5f);
                                deductionKeycell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(deductionKeycell);
                                deductionAmount = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(deductions.get(entry1.getKey()));
                                deductionValuecell = new PdfPCell(new Phrase(deductionAmount, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                deductionValuecell.setBorder(Rectangle.NO_BORDER);
                                deductionValuecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                deductionValuecell.setBorderColor(BaseColor.WHITE);
                                deductionValuecell.setBorderWidthRight(0.5f);
                                deductionValuecell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(deductionValuecell);
                                String cumulativeDedutions = "";
                                if (Cumalativedeductions.containsKey(entry1.getKey())) {
                                    cumulativeDedutions = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Cumalativedeductions.get(entry1.getKey()));
                                }
                                cumulativecell = new PdfPCell(new Phrase(cumulativeDedutions, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                cumulativecell.setBorder(Rectangle.NO_BORDER);
                                cumulativecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cumulativecell.setBorderColor(BaseColor.WHITE);
                                cumulativecell.setBorderWidthRight(0.5f);
                                cumulativecell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(cumulativecell);
                                deductionAmount = "";
                                iter.remove();
                                iter1.remove();
                                break;
                            }
                        } else {
                            keycell = new PdfPCell(new Phrase((entry.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            keycell.setBorder(Rectangle.NO_BORDER);
                            keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            keycell.setBorderColor(BaseColor.WHITE);
                            keycell.setBorderWidthRight(0.5f);
                            keycell.setBorderColorRight(BaseColor.BLACK);
                            keycell.setBorderWidthLeft(0.5f);
                            keycell.setBorderColorLeft(BaseColor.BLACK);
                            salaryDetailsTable.addCell(keycell);
                            earningAmount = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Earnings.get(entry.getKey()));
                            valuecell = new PdfPCell(new Phrase(earningAmount, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            valuecell.setBorder(Rectangle.NO_BORDER);
                            valuecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            valuecell.setBorderColor(BaseColor.WHITE);
                            valuecell.setBorderWidthRight(0.5f);
                            valuecell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(valuecell);

                            String cumulativeearnings = "";
                            if (CumalativeEarnings.containsKey(entry.getKey())) {
                                cumulativeearnings = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(CumalativeEarnings.get(entry.getKey()));
                            }
                            // String earningsValue = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(CumalativeEarnings.get(entry.getKey()));
                            cumulativecell = new PdfPCell(new Phrase(cumulativeearnings, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            cumulativecell.setBorder(Rectangle.NO_BORDER);
                            cumulativecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cumulativecell.setBorderColor(BaseColor.WHITE);
                            cumulativecell.setBorderWidthRight(0.5f);
                            cumulativecell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(cumulativecell);
                            earningAmount = "";

                            PdfPCell deductionKeycell = null;
                            PdfPCell deductionValuecell = null;
                            String deductionAmount = "";

                            deductionKeycell = new PdfPCell(new Phrase((""), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            deductionKeycell.setBorder(Rectangle.NO_BORDER);
                            deductionKeycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            deductionKeycell.setBorderColor(BaseColor.WHITE);
                            deductionKeycell.setBorderWidthRight(0.5f);
                            deductionKeycell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(deductionKeycell);

                            deductionValuecell = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            deductionValuecell.setBorder(Rectangle.NO_BORDER);
                            deductionValuecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            deductionValuecell.setBorderColor(BaseColor.WHITE);
                            deductionValuecell.setBorderWidthRight(0.5f);
                            deductionValuecell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(deductionValuecell);
                            cumulativecell = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            cumulativecell.setBorder(Rectangle.NO_BORDER);
                            cumulativecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cumulativecell.setBorderColor(BaseColor.WHITE);
                            cumulativecell.setBorderWidthRight(0.5f);
                            cumulativecell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(cumulativecell);
                            deductionAmount = "";

                        }
                    }

                }
                if (deductions.size() > Earnings.size()) {
                    Iterator<Map.Entry<String, Double>> iter = deductions.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Double> entry = iter.next();
                        if (deductions.size() > 0) {
                            Iterator<Map.Entry<String, Double>> iter1 = Earnings.entrySet().iterator();
                            while (iter1.hasNext()) {
                                Map.Entry<String, Double> entry1 = iter1.next();
                                keycell = new PdfPCell(new Phrase((entry.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                keycell.setBorder(Rectangle.NO_BORDER);
                                keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                keycell.setBorderColor(BaseColor.WHITE);
                                keycell.setBorderWidthRight(0.5f);
                                keycell.setBorderColorRight(BaseColor.BLACK);
                                keycell.setBorderWidthLeft(0.5f);
                                keycell.setBorderColorLeft(BaseColor.BLACK);
                                salaryDetailsTable.addCell(keycell);
                                earningAmount = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Earnings.get(entry.getKey()));
                                valuecell = new PdfPCell(new Phrase(earningAmount, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                valuecell.setBorder(Rectangle.NO_BORDER);
                                valuecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                valuecell.setBorderColor(BaseColor.WHITE);
                                valuecell.setBorderWidthRight(0.5f);
                                valuecell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(valuecell);
                                String cumulativeearnings = "";
                                if (CumalativeEarnings.containsKey(entry.getKey())) {
                                    cumulativeearnings = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(CumalativeEarnings.get(entry.getKey()));
                                }
                                //String earningsValue = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(CumalativeEarnings.get(entry.getKey()));
                                cumulativecell = new PdfPCell(new Phrase(cumulativeearnings, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                cumulativecell.setBorder(Rectangle.NO_BORDER);
                                cumulativecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cumulativecell.setBorderColor(BaseColor.WHITE);
                                cumulativecell.setBorderWidthRight(0.5f);
                                cumulativecell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(cumulativecell);
                                earningAmount = "";
                                PdfPCell deductionKeycell = null;
                                PdfPCell deductionValuecell = null;
                                String deductionAmount = "";
                                deductionKeycell = new PdfPCell(new Phrase((entry1.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                deductionKeycell.setBorder(Rectangle.NO_BORDER);
                                deductionKeycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                deductionKeycell.setBorderColor(BaseColor.WHITE);
                                deductionKeycell.setBorderWidthRight(0.5f);
                                deductionKeycell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(deductionKeycell);
                                deductionAmount = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(deductions.get(entry1.getKey()));
                                deductionValuecell = new PdfPCell(new Phrase(deductionAmount, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                deductionValuecell.setBorder(Rectangle.NO_BORDER);
                                deductionValuecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                deductionValuecell.setBorderColor(BaseColor.WHITE);
                                deductionValuecell.setBorderWidthRight(0.5f);
                                deductionValuecell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(deductionValuecell);

                                String cumulativedeductions = "";
                                if (Cumalativedeductions.containsKey(entry.getKey())) {
                                    cumulativedeductions = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Cumalativedeductions.get(entry.getKey()));
                                }
                                //String cumulativeDedutions = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Cumalativedeductions.get(entry1.getKey()));
                                cumulativecell = new PdfPCell(new Phrase(cumulativedeductions, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                                cumulativecell.setBorder(Rectangle.NO_BORDER);
                                cumulativecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cumulativecell.setBorderColor(BaseColor.WHITE);
                                cumulativecell.setBorderWidthRight(0.5f);
                                cumulativecell.setBorderColorRight(BaseColor.BLACK);
                                salaryDetailsTable.addCell(cumulativecell);
                                deductionAmount = "";
                                iter.remove();
                                iter1.remove();
                                break;
                            }
                        } else {
                            keycell = new PdfPCell(new Phrase((entry.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            keycell.setBorder(Rectangle.NO_BORDER);
                            keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            keycell.setBorderColor(BaseColor.WHITE);
                            keycell.setBorderWidthRight(0.5f);
                            keycell.setBorderColorRight(BaseColor.BLACK);
                            keycell.setBorderWidthLeft(0.5f);
                            keycell.setBorderColorLeft(BaseColor.BLACK);
                            salaryDetailsTable.addCell(keycell);
                            earningAmount = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Earnings.get(entry.getKey()));
                            valuecell = new PdfPCell(new Phrase(earningAmount, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            valuecell.setBorder(Rectangle.NO_BORDER);
                            valuecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            valuecell.setBorderColor(BaseColor.WHITE);
                            valuecell.setBorderWidthRight(0.5f);
                            valuecell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(valuecell);
                            String cumulativeearnings = "";
                            if (CumalativeEarnings.containsKey(entry.getKey())) {
                                cumulativeearnings = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(CumalativeEarnings.get(entry.getKey()));
                            }

                            //  String earningsValue = SalarySlipRportPDFGeneration.roundTwoDecimalPoints(CumalativeEarnings.get(entry.getKey()));
                            cumulativecell = new PdfPCell(new Phrase(cumulativeearnings, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            cumulativecell.setBorder(Rectangle.NO_BORDER);
                            cumulativecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cumulativecell.setBorderColor(BaseColor.WHITE);
                            cumulativecell.setBorderWidthRight(0.5f);
                            cumulativecell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(cumulativecell);
                            earningAmount = "";

                            PdfPCell deductionKeycell = null;
                            PdfPCell deductionValuecell = null;
                            String deductionAmount = "";

                            deductionKeycell = new PdfPCell(new Phrase((""), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            deductionKeycell.setBorder(Rectangle.NO_BORDER);
                            deductionKeycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            deductionKeycell.setBorderColor(BaseColor.WHITE);
                            deductionKeycell.setBorderWidthRight(0.5f);
                            deductionKeycell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(deductionKeycell);

                            deductionValuecell = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            deductionValuecell.setBorder(Rectangle.NO_BORDER);
                            deductionValuecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            deductionValuecell.setBorderColor(BaseColor.WHITE);
                            deductionValuecell.setBorderWidthRight(0.5f);
                            deductionValuecell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(deductionValuecell);
                            cumulativecell = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                            cumulativecell.setBorder(Rectangle.NO_BORDER);
                            cumulativecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cumulativecell.setBorderColor(BaseColor.WHITE);
                            cumulativecell.setBorderWidthRight(0.5f);
                            cumulativecell.setBorderColorRight(BaseColor.BLACK);
                            salaryDetailsTable.addCell(cumulativecell);
                            deductionAmount = "";

                        }
                    }

                }

                PdfPCell totalparagraphCell1 = null;
                PdfPCell totalparagraphCell2 = null;
                PdfPCell totalparagraphCell3 = null;

                totalparagraphCell1 = new PdfPCell(new Phrase("Gross Pay", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                totalparagraphCell1.setBorder(Rectangle.NO_BORDER);
                totalparagraphCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                totalparagraphCell1.setBorderColor(BaseColor.WHITE);
                totalparagraphCell1.setBorderWidthBottom(1f);
                totalparagraphCell1.setBorderWidthTop(1f);
                totalparagraphCell1.setBorderColorBottom(BaseColor.BLACK);
                totalparagraphCell1.setBorderColorTop(BaseColor.BLACK);
                totalparagraphCell1.setPaddingBottom(5f);
                totalparagraphCell1.setPaddingTop(5f);
                totalparagraphCell1.setBorderWidthLeft(0.5f);
                totalparagraphCell1.setBorderColorLeft(BaseColor.BLACK);
                String totEarnings = "0.00";
                if (totalEarnings != 0.0) {
                    totEarnings = roundTwoDecimalPoints(totalEarnings);
                }
                totalparagraphCell2 = new PdfPCell(new Phrase(totEarnings, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                totalparagraphCell2.setBorder(Rectangle.NO_BORDER);
                totalparagraphCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalparagraphCell2.setBorderColor(BaseColor.WHITE);
                totalparagraphCell2.setBorderWidthBottom(1f);
                totalparagraphCell2.setBorderWidthTop(1f);
                totalparagraphCell2.setBorderColorBottom(BaseColor.BLACK);
                totalparagraphCell2.setBorderColorTop(BaseColor.BLACK);
                totalparagraphCell2.setPaddingBottom(5f);
                totalparagraphCell2.setPaddingTop(5f);

                SalarySlipRportPDFGeneration s = new SalarySlipRportPDFGeneration();
                totalparagraphCell3 = new PdfPCell(new Phrase(roundTwoDecimalPoints(totalCumulativeEarnings), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                totalparagraphCell3.setBorder(Rectangle.NO_BORDER);
                totalparagraphCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalparagraphCell3.setBorderColor(BaseColor.WHITE);

                totalparagraphCell3.setBorderWidthBottom(1f);
                totalparagraphCell3.setBorderWidthTop(1f);
                totalparagraphCell3.setBorderColorBottom(BaseColor.BLACK);
                totalparagraphCell3.setBorderColorTop(BaseColor.BLACK);
                totalparagraphCell3.setPaddingBottom(5f);
                totalparagraphCell3.setPaddingTop(5f);

                PdfPCell totalparagraphCell4 = null;
                PdfPCell totalparagraphCell5 = null;
                PdfPCell totalparagraphCell6 = null;

                totalparagraphCell4 = new PdfPCell(new Phrase("Total Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                totalparagraphCell4.setBorder(Rectangle.NO_BORDER);
                totalparagraphCell4.setHorizontalAlignment(Element.ALIGN_LEFT);
                totalparagraphCell4.setBorderColor(BaseColor.WHITE);
                totalparagraphCell4.setBorderWidthBottom(1f);
                totalparagraphCell4.setBorderWidthTop(1f);
                totalparagraphCell4.setBorderColorTop(BaseColor.BLACK);
                totalparagraphCell4.setBorderColorBottom(BaseColor.BLACK);
                totalparagraphCell4.setPaddingBottom(5f);
                totalparagraphCell4.setPaddingTop(5f);

                String totdeductionss = "0.00";
                if (totalDeductions != 0.0) {
                    totdeductionss = roundTwoDecimalPoints(totalDeductions);
                }
                totalparagraphCell5 = new PdfPCell(new Phrase(totdeductionss, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                totalparagraphCell5.setBorder(Rectangle.NO_BORDER);
                totalparagraphCell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalparagraphCell5.setBorderColor(BaseColor.WHITE);
                totalparagraphCell5.setBorderWidthBottom(1f);
                totalparagraphCell5.setBorderWidthTop(1f);
                totalparagraphCell5.setBorderColorTop(BaseColor.BLACK);
                totalparagraphCell5.setBorderColorBottom(BaseColor.BLACK);
                totalparagraphCell5.setPaddingBottom(5f);
                totalparagraphCell5.setPaddingTop(5f);

                totalparagraphCell6 = new PdfPCell(new Phrase(roundTwoDecimalPoints(totalCumulativeDeductions), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                totalparagraphCell6.setBorder(Rectangle.NO_BORDER);
                totalparagraphCell6.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalparagraphCell6.setBorderColor(BaseColor.WHITE);
                totalparagraphCell6.setBorderWidthBottom(1f);
                totalparagraphCell6.setBorderWidthTop(1f);
                totalparagraphCell6.setBorderColorTop(BaseColor.BLACK);
                totalparagraphCell6.setBorderColorBottom(BaseColor.BLACK);
                totalparagraphCell6.setPaddingBottom(5f);
                totalparagraphCell6.setPaddingTop(5f);
                totalparagraphCell6.setBorderWidthRight(0.5f);
                totalparagraphCell6.setBorderColorRight(BaseColor.BLACK);

                salaryDetailsTable.addCell(totalparagraphCell1);
                salaryDetailsTable.addCell(totalparagraphCell2);
                salaryDetailsTable.addCell(totalparagraphCell3);
                salaryDetailsTable.addCell(totalparagraphCell4);
                salaryDetailsTable.addCell(totalparagraphCell5);
                salaryDetailsTable.addCell(totalparagraphCell6);

                outercell.addElement(salaryDetailsTable);

                float[] columnWidths = {3f, 1f};

                PdfPTable table11 = new PdfPTable(6);
                table11.setWidthPercentage(100);
                //  table11.setWidths(columnWidths);

                PdfPCell transferString = new PdfPCell(new Phrase("Net Salary ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                transferString.setBorder(Rectangle.NO_BORDER);
                transferString.setBorderColor(BaseColor.WHITE);
                transferString.setHorizontalAlignment(Element.ALIGN_LEFT);

                netpayment = totalEarnings - totalDeductions;
                String totnet = "0.00";
                if (netpayment != 0.0) {
                    totnet = roundTwoDecimalPoints(netpayment);
                }
                PdfPCell transferAmount = new PdfPCell(new Phrase(totnet, FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                transferAmount.setBorder(Rectangle.NO_BORDER);
                transferAmount.setBorderColor(BaseColor.WHITE);
                transferAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);

                PdfPCell emptyCell = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                emptyCell.setBorder(Rectangle.NO_BORDER);
                emptyCell.setBorderColor(BaseColor.WHITE);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                table11.addCell(transferString);
                table11.addCell(transferAmount);
                table11.addCell(emptyCell);
                table11.addCell(emptyCell);
                table11.addCell(emptyCell);
                table11.addCell(emptyCell);

                String amountword = "";
                if (netpayment != 0.0) {
                    ConvertMoneyToNumberMain a = new ConvertMoneyToNumberMain();
                    String word = "0";
                    if (netpayment > -1) {
                        word = a.convertAmountToWord(roundTwoDecimalPoints(netpayment).replaceAll(",", ""));
                    } else {
                        netpayment = -1 * netpayment;
                        word = a.convertAmountToWord(roundTwoDecimalPoints(netpayment).replaceAll(",", ""));
                        amountword = "  Amount (in words): Rs. Minus " + word + " Only";
                    }

                }
                PdfPCell netPayableCell = new PdfPCell(new Phrase(amountword, FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK)));
                netPayableCell.setBorder(Rectangle.NO_BORDER);
                netPayableCell.setBorderColor(BaseColor.WHITE);
                netPayableCell.setPaddingRight(5.0f);
                netPayableCell.setBorderWidthBottom(0.5f);
                netPayableCell.setBorderColorBottom(BaseColor.BLACK);
                netPayableCell.setPaddingBottom(5f);

                // netPayableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell netPayableCell1 = new PdfPCell();
                netPayableCell1.setBorder(Rectangle.NO_BORDER);
                netPayableCell1.setBorderColor(BaseColor.WHITE);
                netPayableCell1.setBorderWidthBottom(0.5f);
                netPayableCell1.setBorderColorBottom(BaseColor.BLACK);
                netPayableCell.setPaddingBottom(5f);
                netPayableCell.setColspan(5);

                //paragraphCell.setPaddingBottom(5f);
                table11.addCell(netPayableCell);
                table11.addCell(netPayableCell1);
                table11.addCell(emptyCell);
                table11.addCell(emptyCell);
                table11.addCell(emptyCell);
                table11.addCell(emptyCell);

                outercell.addElement(table11);

                PdfPTable advancecEll = new PdfPTable(6);
                advancecEll.setWidthPercentage(100);

                PdfPCell emptyCell1 = new PdfPCell(new Phrase("Advance", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                emptyCell1.setBorder(Rectangle.NO_BORDER);
                emptyCell1.setBorderColor(BaseColor.WHITE);
                emptyCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell1.setBorderWidthBottom(0.5f);
                emptyCell1.setBorderColorBottom(BaseColor.BLACK);

                PdfPCell emptyCell2 = new PdfPCell(new Phrase("Taken", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                emptyCell2.setBorder(Rectangle.NO_BORDER);
                emptyCell2.setBorderColor(BaseColor.WHITE);
                emptyCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell2.setBorderWidthBottom(0.5f);
                emptyCell2.setBorderColorBottom(BaseColor.BLACK);

                PdfPCell emptyCell3 = new PdfPCell(new Phrase("Op.Bal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                emptyCell3.setBorder(Rectangle.NO_BORDER);
                emptyCell3.setBorderColor(BaseColor.WHITE);
                emptyCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell3.setBorderWidthBottom(0.5f);
                emptyCell3.setBorderColorBottom(BaseColor.BLACK);

                PdfPCell emptyCell4 = new PdfPCell(new Phrase("EMI", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                emptyCell4.setBorder(Rectangle.NO_BORDER);
                emptyCell4.setBorderColor(BaseColor.WHITE);
                emptyCell4.setBorderWidthBottom(0.5f);
                emptyCell4.setBorderColorBottom(BaseColor.BLACK);
                emptyCell4.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell emptyCell5 = new PdfPCell(new Phrase("Cl.Bal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                emptyCell5.setBorder(Rectangle.NO_BORDER);
                emptyCell5.setBorderColor(BaseColor.WHITE);
                emptyCell5.setBorderWidthBottom(0.5f);
                emptyCell5.setBorderColorBottom(BaseColor.BLACK);
                emptyCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell5.setColspan(2);

                PdfPCell emptyCell6 = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                emptyCell6.setBorder(Rectangle.NO_BORDER);
                emptyCell6.setBorderColor(BaseColor.WHITE);
                emptyCell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell6.setBorderWidthBottom(0.5f);
                emptyCell6.setBorderColorBottom(BaseColor.BLACK);

                advancecEll.addCell(emptyCell1);
                advancecEll.addCell(emptyCell2);
                advancecEll.addCell(emptyCell3);
                advancecEll.addCell(emptyCell4);
                advancecEll.addCell(emptyCell5);
                advancecEll.addCell(emptyCell6);

                outercell.addElement(advancecEll);

                PdfPTable SignatureTable = new PdfPTable(1);
                SignatureTable.setWidthPercentage(100);

                PdfPCell signatureCell = new PdfPCell(new Phrase("This is computer generated pay slip,Hence Signature is not Required", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK)));
                signatureCell.setBorder(Rectangle.NO_BORDER);
                signatureCell.setBorderColor(BaseColor.WHITE);
                signatureCell.setPaddingRight(5.0f);
                signatureCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                SignatureTable.addCell(signatureCell);
                outercell.addElement(SignatureTable);

                RoundRectangle roundRectangle = new RoundRectangle();
                outercell.setCellEvent(roundRectangle);
                outercell.setBorder(Rectangle.NO_BORDER);
                outercell.setBorderWidth(2);
                outercell.setPadding(8);

                outerTable.addCell(outercell);

                document.add(outerTable);
                document.close();
                Cumalativedeductions.clear();
                CumalativeEarnings.clear();
                totalCumulativeEarnings = 0.00;
                totalCumulativeDeductions = 0.00;
            }

        }
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

    public static double round(String fo, double num) {
        double multipleOf = 0.0;
        if (fo.equalsIgnoreCase(ApplicationConstants.ROUNDING_NONE)) {
            return roundTwoDecimals(num);
//            DecimalFormat df = new DecimalFormat("#.00");
//            String dx = df.format(num);
//            num = Double.valueOf(dx);
//            //System.out.println("1" + num);
//            return num;
        } else if (fo.equalsIgnoreCase(ApplicationConstants.ROUNDING_50_PAISE)) {
            multipleOf = 0.5;
        } else if (fo.equalsIgnoreCase(ApplicationConstants.ROUNDING_1_RUPEE)) {
            multipleOf = 1.0;
        } else if (fo.equalsIgnoreCase(ApplicationConstants.ROUNDING_10_RUPEE)) {
            multipleOf = 10.0;
        } else {
//            DecimalFormat df = new DecimalFormat("#.##");
//            String dx = df.format(num);
//            num = Double.valueOf(dx);
//            //System.out.println("2" + num);
            return num;
        }
        double dd = Math.floor((num + multipleOf / 2) / multipleOf) * multipleOf;
        return dd;
    }

    public static double roundTwoDecimals(double amount) {
        if (amount == 0) {
            return amount;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        double output = Double.parseDouble(df.format(amount) + "");
        return output;
    }

    public static String roundTwoDecimalPoints(double amount) {
        if (amount == 0) {
            return String.valueOf(0.00);
        }
//        //System.out.println(amount);
//        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("en", "IN"));
//        nf.setMaximumFractionDigits(340);
//        nf.setMinimumFractionDigits(2);
//        nf.setRoundingMode(RoundingMode.HALF_UP);
//        String output = nf.format(amount).toString();
        String s = String.format(Locale.UK, "%1.2f", Math.abs(amount));
        s = s.replaceAll("(.+)(...\\...)", "$1,$2");
        while (s.matches("\\d{3,},.+")) {
            s = s.replaceAll("(\\d+)(\\d{2},.+)", "$1,$2");
        }
        return amount < 0 ? ("-" + s) : s;
    }

    public void calculateCumulative(String employeeCode, int month, int year, String financialYearstart, String financialyearEnd, String ddo) throws Exception {
        //System.out.println("123" + financialYearstart);
        String finanicialyearend = financialyearEnd.substring(financialyearEnd.lastIndexOf("/") + 1);
        //System.out.println("finanicialyearend" + finanicialyearend);
        // int financialmonth = Integer.parseInt(financialYearstart.substring(3, 5));

        RestClient aql = new RestClient();
        // int endMonth=Integer.parseInt(financialyearEnd.substring(3, 5));

        SalarySlipRegisterReport salarylist1 = null;

        String autoSalaryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";
        DBCursor cursor2 = null;

        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE);

        BasicDBObject regexQuery = null;
        if (year == Integer.parseInt(finanicialyearend)) {
            if (month == 3) {
                regexQuery = new BasicDBObject();
                regexQuery.put("year", Integer.parseInt(finanicialyearend));
                regexQuery.put("month", month);
                regexQuery.put("employeeCode", employeeCode);
                regexQuery.put("ddo", ddo);
                regexQuery.put("status", "Processed");
                DBCursor cursor3 = collection.find(regexQuery);
                calculateCumulativeCursorValue(cursor3);
                regexQuery.clear();
                cursor3.close();
            } else {
                regexQuery = new BasicDBObject();
                int preyear = year - 1;
                int premonth = 12;
                regexQuery.put("year", preyear);
                regexQuery.put("month", new BasicDBObject("$gte", 3).append("$lte", premonth));
                regexQuery.put("employeeCode", employeeCode);
                regexQuery.put("ddo", ddo);
                regexQuery.put("status", "Processed");
                cursor2 = collection.find(regexQuery);
                calculateCumulativeCursorValue(cursor2);
                regexQuery.clear();
                regexQuery = new BasicDBObject();
                regexQuery.put("year", Integer.parseInt(finanicialyearend));
                regexQuery.put("month", new BasicDBObject("$gte", 1).append("$lte", 2));
                regexQuery.put("employeeCode", employeeCode);
                regexQuery.put("ddo", ddo);
                regexQuery.put("status", "Processed");
                //System.out.println(regexQuery.toString());
                DBCursor cursor3 = collection.find(regexQuery);
                calculateCumulativeCursorValue(cursor3);
                regexQuery.clear();
                cursor3.close();
            }
        } else {
            regexQuery = new BasicDBObject();
            regexQuery.put("year", year);
            regexQuery.put("month", new BasicDBObject("$gte", 3).append("$lte", month));
            regexQuery.put("employeeCode", employeeCode);
            regexQuery.put("ddo", ddo);
            regexQuery.put("status", "Processed");
            cursor2 = collection.find(regexQuery);
            calculateCumulativeCursorValue(cursor2);
            regexQuery.clear();
            cursor2.close();
        }

        //System.out.println("Totaldeductions" + Cumalativedeductions);
        //System.out.println("Totalearnings" + CumalativeEarnings);

    }

    public static void calculateCumulativeCursorValue(DBCursor cursor2) {

        double totalDeductions = 0.00;
        double totalEarnings = 0.00;
        ArrayList<InsuranceTransactions> insuranceobj = null;
        IncomeTax incomeTaxobj = null;
        ArrayList<LoanPayment> loanObj = null;

        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            //System.out.println("ob" + ob.toString());
            Type type = new TypeToken<SalarySlipRegisterReport>() {
            }.getType();
            SalarySlipRegisterReport salarylist = new Gson().fromJson(ob.toString(), type);

            Earnings earingInfo = salarylist.getEarningsInfo();
            Deductions deductionInfo = salarylist.getDeductionsInfo();
            ArrearProcess arrearprocess = salarylist.getArrear();

            ArrayList<EarningHeadsDetails> earningList = earingInfo.getEarningHeads();
            ArrayList<EarningHeadsDetails> deductionList = deductionInfo.getDeductionHeads();
            LeaveEncashment leaveEnhancementList = earingInfo.getLeaveEncashment();

            if (earingInfo != null) {
                if (earingInfo.isIsEarningHeads()) {
                    if (earningList != null && earningList.size() > 0) {
                        Collections.sort(earningList, new Comparator<EarningHeadsDetails>() {
                            public int compare(EarningHeadsDetails s1, EarningHeadsDetails s2) {
                                return s1.getDescriptionInfo().getDisplayLevel() - s2.getDescriptionInfo().getDisplayLevel();
                            }
                        });
                        for (int i = 0; i < earningList.size(); i++) {
                            String showonSlip = earningList.get(i).getDescriptionInfo().getShowOnSalarySlip();

                            if (earningList.get(i).getAmount() != 0 && showonSlip.equalsIgnoreCase("Yes")) {
                                totalCumulativeEarnings += earningList.get(i).getAmount();
                                if (CumalativeEarnings != null && CumalativeEarnings.size() > 0) {
                                    if (!CumalativeEarnings.containsKey(earningList.get(i).getDescription())) {
                                        CumalativeEarnings.put(earningList.get(i).getDescription(), (earningList.get(i).getAmount()));

                                    } else {
                                        CumalativeEarnings.put(earningList.get(i).getDescription(), CumalativeEarnings.get(earningList.get(i).getDescription()) + earningList.get(i).getAmount());
                                    }
                                } else {
                                    CumalativeEarnings.put(earningList.get(i).getDescription(), (earningList.get(i).getAmount()));
                                }

                            }

                        }
                    }
                }
            }
            if (earingInfo.isIsLeaveEncashment()) {
                totalCumulativeEarnings += leaveEnhancementList.getTotalAmount();
                if (CumalativeEarnings != null && CumalativeEarnings.size() > 0) {
                    if (!CumalativeEarnings.containsKey(leaveEnhancementList.getLeInfo().getShortDescription())) {
                        CumalativeEarnings.put(leaveEnhancementList.getLeInfo().getShortDescription(), (leaveEnhancementList.getTotalAmount()));
                    } else {
                        CumalativeEarnings.put(leaveEnhancementList.getLeInfo().getShortDescription(), CumalativeEarnings.get(leaveEnhancementList.getLeInfo().getShortDescription()) + leaveEnhancementList.getTotalAmount());
                    }
                } else {
                    CumalativeEarnings.put(leaveEnhancementList.getLeInfo().getShortDescription(), (leaveEnhancementList.getTotalAmount()));
                }
            }

            if (salarylist.isIsArrear()) {
                totalCumulativeEarnings += arrearprocess.getTotalEarnings();
                if (CumalativeEarnings != null && CumalativeEarnings.size() > 0) {
                    if (!CumalativeEarnings.containsKey("ARREAR")) {
                        Cumalativedeductions.put("ARREAR", (arrearprocess.getTotalEarnings()));
                    } else {
                        Cumalativedeductions.put("ARREAR", CumalativeEarnings.get("ARREAR") + arrearprocess.getTotalEarnings());
                    }
                } else {
                    Cumalativedeductions.put("ARREAR", (arrearprocess.getTotalEarnings()));
                }
            }

            if (deductionInfo != null) {
                if (deductionInfo.isIsDeductionHeads()) {
                    //System.out.println(deductionList.size() + "deductionList.size()");
                    if (deductionList != null && deductionList.size() > 0) {
                        Collections.sort(deductionList, new Comparator<EarningHeadsDetails>() {
                            public int compare(EarningHeadsDetails s1, EarningHeadsDetails s2) {
                                //System.out.println("s1.getDescriptionInfo().getDisplayLevel()" + s1.getDescriptionInfo().getDisplayLevel());
                                return s1.getDescriptionInfo().getDisplayLevel() - s2.getDescriptionInfo().getDisplayLevel();
                            }
                        });

                        for (int i = 0; i < deductionList.size(); i++) {
                            String showonSlip = deductionList.get(i).getDescriptionInfo().getShowOnSalarySlip();
                            if (deductionList.get(i).getAmount() != 0 && showonSlip.equalsIgnoreCase("Yes")) {
                                totalCumulativeDeductions += deductionList.get(i).getAmount();
                                if (Cumalativedeductions != null && Cumalativedeductions.size() > 0) {
                                    if (!Cumalativedeductions.containsKey(deductionList.get(i).getDescription())) {

                                        Cumalativedeductions.put(deductionList.get(i).getDescription(), (deductionList.get(i).getAmount()));
                                    } else {
                                        Cumalativedeductions.put(deductionList.get(i).getDescription(), Cumalativedeductions.get(deductionList.get(i).getDescription()) + deductionList.get(i).getAmount());
                                    }
                                } else {
                                    Cumalativedeductions.put(deductionList.get(i).getDescription(), (deductionList.get(i).getAmount()));
                                }
                            }
                        }
                    }
                    if (deductionInfo.isIsInsurance()) {
                        insuranceobj = deductionInfo.getInsurance();
                        for (int i = 0; i < insuranceobj.size(); i++) {
                            if (insuranceobj.get(i).getInstallmentAmount() != 0.0) {
                                totalCumulativeDeductions += insuranceobj.get(i).getInstallmentAmount();
                                double insurance = insuranceobj.get(i).getInstallmentAmount();
                                if (Cumalativedeductions != null && Cumalativedeductions.size() > 0) {
                                    if (!Cumalativedeductions.containsKey(insuranceobj.get(i).getInsInfo().getShortDescription())) {

                                        Cumalativedeductions.put(insuranceobj.get(i).getInsInfo().getShortDescription(), (insurance));
                                    } else {
                                        Cumalativedeductions.put(insuranceobj.get(i).getInsInfo().getShortDescription(), Cumalativedeductions.get(insuranceobj.get(i).getInsInfo().getShortDescription()) + (insurance));
                                    }
                                } else {
                                    Cumalativedeductions.put(insuranceobj.get(i).getInsInfo().getShortDescription(), (insurance));
                                }
                            }
                        }
                    }
                    if (deductionInfo.isIsIncomeTax()) {
                        incomeTaxobj = deductionInfo.getIncomeTax();
//                        totalCumulativeDeductions += incomeTaxobj.getIt();
//                        double incomeTax = incomeTaxobj.getIt();
                        totalCumulativeDeductions += incomeTaxobj.getTotal();
                        double incomeTax = incomeTaxobj.getTotal();
                        if (Cumalativedeductions != null && Cumalativedeductions.size() > 0) {
                            if (!Cumalativedeductions.containsKey("INCOME TAX")) {
                                Cumalativedeductions.put("INCOME TAX", (incomeTax));
                            } else {
                                Cumalativedeductions.put("INCOME TAX", Cumalativedeductions.get("INCOME TAX") + (incomeTax));
                            }
                        } else {
                            Cumalativedeductions.put("INCOME TAX", (incomeTax));
                        }
                    }

                    if (deductionInfo.isIsLoan()) {
                        loanObj = deductionInfo.getLoan();
                        for (int i = 0; i < loanObj.size(); i++) {
                            totalCumulativeDeductions += loanObj.get(i).getInterestPaid();
                            if (loanObj.get(i).getInterestPaid() != 0.0) {
                                if (Cumalativedeductions != null && Cumalativedeductions.size() > 0) {
                                    if (!Cumalativedeductions.containsKey(loanObj.get(i).getLoanInfo().getShortDescription() + " INTEREST")) {
                                        Cumalativedeductions.put(loanObj.get(i).getLoanInfo().getShortDescription() + " INTEREST", (loanObj.get(i).getInterestPaid()));
                                    } else {
                                        double keyValue = Cumalativedeductions.get(loanObj.get(i).getLoanInfo().getShortDescription() + " INTEREST");
                                        //System.out.println(1);
                                        //System.out.println(keyValue + loanObj.get(i).getInterestPaid());
                                        Cumalativedeductions.put(loanObj.get(i).getLoanInfo().getShortDescription() + " INTEREST", (keyValue + loanObj.get(i).getInterestPaid()));
                                    }
                                } else {
                                    Cumalativedeductions.put(loanObj.get(i).getLoanInfo().getShortDescription() + " INTEREST", (loanObj.get(i).getInterestPaid()));
                                }
                            }
                            totalCumulativeDeductions += loanObj.get(i).getPaidAmount();
                            if (Cumalativedeductions != null && Cumalativedeductions.size() > 0) {
                                if (!Cumalativedeductions.containsKey(loanObj.get(i).getLoanInfo().getShortDescription())) {
                                    Cumalativedeductions.put(loanObj.get(i).getLoanInfo().getShortDescription(), (loanObj.get(i).getPaidAmount()));
                                } else {
                                    Cumalativedeductions.put(loanObj.get(i).getLoanInfo().getShortDescription(), Cumalativedeductions.get(loanObj.get(i).getLoanInfo().getShortDescription()) + loanObj.get(i).getPaidAmount());
                                }
                            } else {
                                Cumalativedeductions.put(loanObj.get(i).getLoanInfo().getShortDescription(), (loanObj.get(i).getPaidAmount()));
                            }
                        }
                    }
                    if (salarylist.isIsArrear()) {
                        totalCumulativeDeductions += arrearprocess.getTotalDeductions();
                        if (Cumalativedeductions != null && Cumalativedeductions.size() > 0) {
                            if (!Cumalativedeductions.containsKey("ARREAR")) {
                                Cumalativedeductions.put("ARREAR", (arrearprocess.getTotalDeductions()));
                            } else {
                                Cumalativedeductions.put("ARREAR", Cumalativedeductions.get("ARREAR") + (arrearprocess.getTotalDeductions()));
                            }
                        } else {
                            Cumalativedeductions.put("ARREAR", (arrearprocess.getTotalDeductions()));
                        }
                    }
                }
            }

        }

    }

}
