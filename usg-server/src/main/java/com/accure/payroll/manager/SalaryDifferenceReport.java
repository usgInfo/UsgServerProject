/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.FinancialYear;
import com.accure.payroll.dto.Deductions;
import com.accure.payroll.dto.Earnings;
import com.accure.payroll.dto.IncomeTax;
import com.accure.payroll.dto.InsuranceTransactions;
import com.accure.payroll.dto.LoanPayment;
import com.accure.payroll.dto.SalarySlipRegisterReport;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.getMonthString;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.round;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class SalaryDifferenceReport {

    public static ByteArrayOutputStream generateSalaryReport(List<SalarySlipRegisterReport> preSalarySlipList, List<SalarySlipRegisterReport> salarySlipList, String month, String year, String path, String fin) throws DocumentException, FileNotFoundException, BadElementException, IOException, Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        double totalEarnings = 0.00;
        double totalDeductions = 0.00;
        double netpayment = 0.00;
        String employeeCode = "";
        String employeeName = "";
        String scale = "";
        String basic = "";
        String pan = "";
        String designation = "";
        String accountNo = "";
        String fundType = "";
        int currentMonthDays = 0;
        String payStopSalary = "";
        int preMonthDays = 0;

        LinkedHashMap<String, String> deductions = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> Earnings = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> predeductions = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> preEarnings = new LinkedHashMap<String, String>();

        ArrayList<InsuranceTransactions> insuranceobj = null;
        IncomeTax incomeTaxobj = null;
        ArrayList<LoanPayment> loanObj = null;

        if (salarySlipList != null && salarySlipList.size() > 0) {
            for (SalarySlipRegisterReport salarylist : salarySlipList) {
                employeeCode = salarylist.getEmployeeCode();
                employeeName = salarylist.getEmployeeName();
                payStopSalary = salarylist.getSalaryProcessType();
                //System.out.println("payStopSalary  " + payStopSalary);
                scale = salarylist.getSalaryType();
                accountNo = salarylist.getAcnumber();
                designation = salarylist.getDesignationName();
                pan = salarylist.getPanNo();
                Earnings earingInfo = salarylist.getEarningsInfo();
                Deductions deductionInfo = salarylist.getDeductionsInfo();
                fundType = salarylist.getFundTypeName();
                currentMonthDays = salarylist.getAttendance().getTotalDays();

                ArrayList<EarningHeadsDetails> earningList = earingInfo.getEarningHeads();
                ArrayList<EarningHeadsDetails> deductionList = deductionInfo.getDeductionHeads();
                if (earingInfo != null) {
                    if (earingInfo.isIsEarningHeads()) {
                        if (earningList != null && earningList.size() > 0) {
                            Collections.sort(earningList, new Comparator<EarningHeadsDetails>() {
                                public int compare(EarningHeadsDetails s1, EarningHeadsDetails s2) {
                                    return s1.getDescriptionInfo().getDisplayLevel() - s1.getDescriptionInfo().getDisplayLevel();
                                }
                            });
                            for (int i = 0; i < earningList.size(); i++) {
                                String showonSlip = earningList.get(i).getDescriptionInfo().getShowOnSalarySlip();
                                String rounding = earningList.get(i).getDescriptionInfo().getRounding();
                                if (showonSlip.equalsIgnoreCase("Yes")) {
                                    totalEarnings += round(rounding, earningList.get(i).getAmount());
                                    Earnings.put(earningList.get(i).getDescription(), String.valueOf(round(rounding, earningList.get(i).getAmount())));
                                }

                            }
                        }
                    }
                }
                if (deductionInfo != null) {

                    if (deductionInfo.isIsDeductionHeads()) {
                        if (deductionList != null && deductionList.size() > 0) {

                            Collections.sort(deductionList, new Comparator<EarningHeadsDetails>() {
                                public int compare(EarningHeadsDetails s1, EarningHeadsDetails s2) {
                                    return s1.getDescriptionInfo().getDisplayLevel() - s1.getDescriptionInfo().getDisplayLevel();
                                }
                            });

                            for (int i = 0; i < deductionList.size(); i++) {
                                String showonSlip = deductionList.get(i).getDescriptionInfo().getShowOnSalarySlip();
                                if (showonSlip.equalsIgnoreCase("Yes")) {
                                    String rounding = earningList.get(i).getDescriptionInfo().getRounding();
                                    totalDeductions += round(rounding, deductionList.get(i).getAmount());
                                    deductions.put(deductionList.get(i).getDescription(), String.valueOf(round(rounding, deductionList.get(i).getAmount())));
                                }
                            }
                            if (deductionInfo.isIsInsurance()) {
                                insuranceobj = deductionInfo.getInsurance();
                                for (int i = 0; i < insuranceobj.size(); i++) {
                                    if (insuranceobj.get(i).getInstallmentAmount() != 0.0) {
                                        totalDeductions += insuranceobj.get(i).getInstallmentAmount();
                                        double insurance = insuranceobj.get(i).getInstallmentAmount();
                                        deductions.put(insuranceobj.get(i).getInsInfo().getShortDescription(), String.valueOf(insurance));
                                    }
                                }
                            }
                            if (deductionInfo.isIsIncomeTax()) {
                                incomeTaxobj = deductionInfo.getIncomeTax();
//                                totalDeductions += incomeTaxobj.getIt();
//                                double incomeTax = incomeTaxobj.getIt();
                                totalDeductions += incomeTaxobj.getTotal();
                                double incomeTax = incomeTaxobj.getTotal();
                                deductions.put("INCOME TAX", String.valueOf(incomeTax));
                            }
                            if (deductionInfo.isIsLoan()) {
                                loanObj = deductionInfo.getLoan();
                                for (int i = 0; i < loanObj.size(); i++) {
                                    if (loanObj.get(i).getInterestInstallmentAmount() != 0.0) {
                                        totalDeductions += loanObj.get(i).getInterestInstallmentAmount();
                                        deductions.put("LOAN INTEREST", String.valueOf(loanObj.get(i).getInterestInstallmentAmount()));
                                    }
                                    totalDeductions += loanObj.get(i).getInstallmentAmount();
                                    deductions.put("LOAN", String.valueOf(loanObj.get(i).getInstallmentAmount()));
                                }
                            }
                        }
                    }
                }
            }
        }

        if (preSalarySlipList != null && preSalarySlipList.size() > 0) {
            for (SalarySlipRegisterReport salarylist : preSalarySlipList) {
                Earnings earingInfo = salarylist.getEarningsInfo();
                Deductions deductionInfo = salarylist.getDeductionsInfo();
                ArrayList<EarningHeadsDetails> earningList = earingInfo.getEarningHeads();
                ArrayList<EarningHeadsDetails> deductionList = deductionInfo.getDeductionHeads();
                preMonthDays = salarylist.getAttendance().getTotalDays();

                if (earingInfo != null) {
                    if (earingInfo.isIsEarningHeads()) {
                        if (earningList != null && earningList.size() > 0) {
                            Collections.sort(earningList, new Comparator<EarningHeadsDetails>() {
                                public int compare(EarningHeadsDetails s1, EarningHeadsDetails s2) {
                                    return s1.getDescriptionInfo().getDisplayLevel() - s1.getDescriptionInfo().getDisplayLevel();
                                }
                            });
                            for (int i = 0; i < earningList.size(); i++) {
                                String showonSlip = earningList.get(i).getDescriptionInfo().getShowOnSalarySlip();
                                String rounding = earningList.get(i).getDescriptionInfo().getRounding();
                                if (earningList.get(i).getAmount() != 0 && showonSlip.equalsIgnoreCase("Yes")) {
                                    totalEarnings += round(rounding, earningList.get(i).getAmount());
                                    preEarnings.put(earningList.get(i).getDescription(), String.valueOf(round(rounding, earningList.get(i).getAmount())));
                                }

                            }
                        }
                    }
                }
                if (deductionInfo != null) {

                    if (deductionInfo.isIsDeductionHeads()) {
                        if (deductionList != null && deductionList.size() > 0) {

                            Collections.sort(deductionList, new Comparator<EarningHeadsDetails>() {
                                public int compare(EarningHeadsDetails s1, EarningHeadsDetails s2) {
                                    return s1.getDescriptionInfo().getDisplayLevel() - s1.getDescriptionInfo().getDisplayLevel();
                                }
                            });

                            for (int i = 0; i < deductionList.size(); i++) {
                                String showonSlip = deductionList.get(i).getDescriptionInfo().getShowOnSalarySlip();
                                if (deductionList.get(i).getAmount() != 0 && showonSlip.equalsIgnoreCase("Yes")) {
                                    String rounding = earningList.get(i).getDescriptionInfo().getRounding();
                                    totalDeductions += round(rounding, deductionList.get(i).getAmount());
                                    predeductions.put(deductionList.get(i).getDescription(), String.valueOf(round(rounding, deductionList.get(i).getAmount())));
                                }
                            }
                            if (deductionInfo.isIsInsurance()) {
                                insuranceobj = deductionInfo.getInsurance();
                                for (int i = 0; i < insuranceobj.size(); i++) {
                                    if (insuranceobj.get(i).getInstallmentAmount() != 0.0) {
                                        totalDeductions += insuranceobj.get(i).getInstallmentAmount();
                                        double insurance = insuranceobj.get(i).getInstallmentAmount();
                                        predeductions.put(insuranceobj.get(i).getInsInfo().getShortDescription(), String.valueOf(insurance));
                                    }

                                }
                            }
                                if (deductionInfo.isIsIncomeTax()) {
                                    incomeTaxobj = deductionInfo.getIncomeTax();
//                                totalDeductions += incomeTaxobj.getIt();
//                                double incomeTax = incomeTaxobj.getIt();
                                    totalDeductions += incomeTaxobj.getTotal();
                                    double incomeTax = incomeTaxobj.getTotal();
                                    predeductions.put("INCOME TAX", String.valueOf(incomeTax));
                                }
                                if (deductionInfo.isIsLoan()) {
                                    loanObj = deductionInfo.getLoan();
                                    for (int i = 0; i < loanObj.size(); i++) {
                                        if (loanObj.get(i).getInterestInstallmentAmount() != 0.0) {
                                            totalDeductions += loanObj.get(i).getInterestInstallmentAmount();
                                            predeductions.put("LOAN INTEREST", String.valueOf(loanObj.get(i).getInterestInstallmentAmount()));
                                        }
                                        totalDeductions += loanObj.get(i).getInstallmentAmount();
                                        predeductions.put("LOAN", String.valueOf(loanObj.get(i).getInstallmentAmount()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Rectangle pageSize = new Rectangle(1000f, 1000f);
            Document document = new Document(pageSize);
            PdfWriter.getInstance(document, bos);
            document.open();

            PdfPTable outerTable = new PdfPTable(1);
            outerTable.setWidthPercentage(100);

            PdfPCell outercell = new PdfPCell();

            PdfPTable header = new PdfPTable(3);
            header.setWidthPercentage(100);
            header.setWidths(new int[]{30, 200, 50});
            header.setSpacingAfter(10);

            PdfPCell imagecell = new PdfPCell();

            Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
            image1.setAlignment(Image.LEFT);
            image1.scaleAbsolute(100.0f, 100.0f);

            imagecell.addElement(image1);
            imagecell.setBorderWidthBottom(1);
            imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            imagecell.setBorderColor(BaseColor.WHITE);
            imagecell.setBorderColor(BaseColor.WHITE);

            String monthString = getMonthString(Integer.parseInt(month));
            String payStopSalaryStr = "";
            if (payStopSalary.equalsIgnoreCase("paystopsalary")) {
                payStopSalaryStr = "(Pay Stopped)";
            }

            Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 25, Font.BOLD, BaseColor.BLACK)));
            headerPhrase.add(new Phrase(new Chunk("\n")));
            headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.NORMAL, BaseColor.BLACK))));
            headerPhrase.add(new Phrase(new Chunk("\n")));
            headerPhrase.add(new Phrase(new Chunk("\n")));
            headerPhrase.add(new Phrase(new Chunk("  Salary Difference Register For " + monthString + "-" + year + payStopSalaryStr, FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD, BaseColor.BLACK))));
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

            Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk("\n")));
            timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk("\n")));
            timePhrase.add(new Phrase(new Chunk("\n")));
            timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk(fin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK))));

            PdfPCell timecell = new PdfPCell(timePhrase);
            timecell.setBorderWidthBottom(1);
            timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            timecell.setBorderColor(BaseColor.WHITE);
            header.addCell(imagecell);
            header.addCell(headercell);
            header.addCell(timecell);
            outercell.addElement(header);

            PdfPTable table8 = new PdfPTable(2);
            table8.setSpacingBefore(5f);
            table8.setWidthPercentage(100.0f);

            Phrase datePhrase81 = new Phrase(new Chunk("Emp Name : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            datePhrase81.add(new Phrase(new Chunk(employeeName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
            PdfPCell cell81 = new PdfPCell(datePhrase81);
            cell81.setBorder(Rectangle.NO_BORDER);
            cell81.setBorderWidthBottom(0.5f);
            cell81.setBorderColorTop(BaseColor.BLACK);
            cell81.setBorderWidthTop(0.5f);
            cell81.setBorderColorBottom(BaseColor.BLACK);
            cell81.setBorderColorRight(BaseColor.BLACK);
            cell81.setBorderWidthRight(0.5f);
            cell81.setPaddingBottom(4.0f);

            Phrase datePhrase82 = new Phrase(new Chunk("Scale : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            datePhrase82.add(new Phrase(new Chunk(scale, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
            PdfPCell cell82 = new PdfPCell(datePhrase82);
            cell82.setBorder(Rectangle.NO_BORDER);
            cell82.setBorderWidthBottom(0.5f);
            cell82.setBorderColorTop(BaseColor.BLACK);
            cell82.setBorderWidthTop(0.5f);
            cell82.setBorderColorBottom(BaseColor.BLACK);
            cell82.setPaddingBottom(4.0f);

            Phrase datePhrase83 = new Phrase(new Chunk("Emp.Code : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            datePhrase83.add(new Phrase(new Chunk(employeeCode, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
            PdfPCell cell83 = new PdfPCell(datePhrase83);
            cell83.setBorder(Rectangle.NO_BORDER);
            cell83.setBorderWidthBottom(0.5f);
            cell83.setBorderColorBottom(BaseColor.BLACK);
            cell83.setBorderColorRight(BaseColor.BLACK);
            cell83.setBorderWidthRight(0.5f);
            cell83.setPaddingBottom(4.0f);

            Phrase datePhrase84 = new Phrase(new Chunk("PAN : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
//        datePhrase84.add(new Phrase(new Chunk(basic, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
//        datePhrase84.add(new Phrase(new Chunk("   ")));
            // datePhrase84.add(new Phrase(new Chunk("PAN:", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
            datePhrase84.add(new Phrase(new Chunk(pan, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
            PdfPCell cell84 = new PdfPCell(datePhrase84);
            cell84.setBorder(Rectangle.NO_BORDER);
            cell84.setBorderWidthBottom(0.5f);
            cell84.setBorderColorBottom(BaseColor.BLACK);
            cell84.setPaddingBottom(4.0f);

            Phrase datePhrase85 = new Phrase(new Chunk("Designation : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            datePhrase85.add(new Phrase(new Chunk(designation, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
            PdfPCell cell85 = new PdfPCell(datePhrase85);
            cell85.setBorder(Rectangle.NO_BORDER);
            cell85.setBorderWidthBottom(0.5f);
            cell85.setBorderColorBottom(BaseColor.BLACK);
            cell85.setPaddingBottom(4.0f);
            cell85.setBorderColorRight(BaseColor.BLACK);
            cell85.setBorderWidthRight(0.5f);

//        Phrase datePhrase86 = new Phrase(new Chunk("Bank A/C No:", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
//        datePhrase86.add(new Phrase(new Chunk(accountNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
//        PdfPCell cell86 = new PdfPCell(datePhrase86);
//        cell86.setBorder(Rectangle.NO_BORDER);
//        cell86.setBorderWidthBottom(0.5f);
//        cell86.setBorderColorBottom(BaseColor.BLACK);
//        cell86.setPaddingBottom(4.0f);
            Phrase datePhrase86 = new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            datePhrase86.add(new Phrase(new Chunk("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
            PdfPCell cell86 = new PdfPCell(datePhrase86);
            cell86.setBorder(Rectangle.NO_BORDER);
            cell86.setBorderWidthBottom(0.5f);
            cell86.setBorderColorBottom(BaseColor.BLACK);
            cell86.setPaddingBottom(4.0f);

            Phrase datePhrase87 = new Phrase(new Chunk("Fund Type : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            datePhrase87.add(new Phrase(new Chunk(fundType, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
            PdfPCell cell87 = new PdfPCell(datePhrase87);
            cell87.setBorder(Rectangle.NO_BORDER);
            cell87.setBorderWidthBottom(0.5f);
            cell87.setBorderColorBottom(BaseColor.BLACK);
            cell87.setPaddingBottom(4.0f);
            cell87.setBorderColorRight(BaseColor.BLACK);
            cell87.setBorderWidthRight(0.5f);

            String incDate = "21/07/2017";
//                if (salarylist.getPfNumber() != null && salarylist.getPfNumber() != "" && !salarylist.getPfNumber().isEmpty()) {
//                    PFNumber = salarylist.getPfNumber();
//                }

            Phrase datePhrase88 = new Phrase(new Chunk("Days : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
//        datePhrase88.add(new Phrase(new Chunk(incDate, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
//        datePhrase88.add(new Phrase(new Chunk("    ")));
//        datePhrase88.add(new Phrase(new Chunk("Days", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
//        datePhrase88.add(new Phrase(new Chunk("    ")));
            datePhrase88.add(new Phrase(new Chunk(Integer.toString(preMonthDays), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));
            datePhrase88.add(new Phrase(new Chunk("   ")));
            datePhrase88.add(new Phrase(new Chunk(Integer.toString(currentMonthDays), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK))));

            PdfPCell cell88 = new PdfPCell(datePhrase88);
            cell88.setBorder(Rectangle.NO_BORDER);
            cell88.setBorderWidthBottom(0.5f);
            cell88.setBorderColorBottom(BaseColor.BLACK);
            cell88.setPaddingBottom(4.0f);

            table8.addCell(cell81);
            table8.addCell(cell82);
            table8.addCell(cell83);
            table8.addCell(cell84);
            table8.addCell(cell85);
            table8.addCell(cell88);
            table8.addCell(cell87);
            table8.addCell(cell86);
            outercell.addElement(table8);

            PdfPTable salaryDetailsTable = new PdfPTable(2);
            salaryDetailsTable.setWidthPercentage(100.0f);

            PdfPCell earningsDetailsTable = new PdfPCell(new Phrase("Earnings", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
            earningsDetailsTable.setBorder(Rectangle.NO_BORDER);
            earningsDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            earningsDetailsTable.setBorderColor(BaseColor.WHITE);
            earningsDetailsTable.setBorderWidthBottom(0.5f);
            earningsDetailsTable.setPaddingBottom(5f);
            earningsDetailsTable.setBorderColorBottom(BaseColor.BLACK);
            earningsDetailsTable.setBorderColorRight(BaseColor.BLACK);
            earningsDetailsTable.setBorderWidthRight(0.5f);

            PdfPCell deductionDetailsTable = new PdfPCell(new Phrase("Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK)));
            deductionDetailsTable.setBorder(Rectangle.NO_BORDER);
            deductionDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            deductionDetailsTable.setBorderColor(BaseColor.WHITE);
            deductionDetailsTable.setBorderWidthBottom(0.5f);
            deductionDetailsTable.setPaddingBottom(5f);
            deductionDetailsTable.setBorderColorBottom(BaseColor.BLACK);

            salaryDetailsTable.addCell(earningsDetailsTable);
            salaryDetailsTable.addCell(deductionDetailsTable);

            outercell.addElement(salaryDetailsTable);

            PdfPTable paragraph = new PdfPTable(1);
            paragraph.setWidthPercentage(100.0f);

            PdfPCell paragraphCell = new PdfPCell();
            paragraphCell.setBorder(Rectangle.NO_BORDER);
            paragraphCell.setBorderColor(BaseColor.WHITE);

            PdfPTable earningTable = new PdfPTable(2);
            earningTable.setWidthPercentage(100.0f);
            earningTable.setSpacingAfter(5.0f);

            PdfPTable firstHeaderTable = new PdfPTable(4);
            firstHeaderTable.setWidthPercentage(100.0f);
            PdfPTable secondHeaderTeable = new PdfPTable(4);
            secondHeaderTeable.setWidthPercentage(100.0f);
            secondHeaderTeable.setSpacingAfter(5.0f);

            PdfPCell firstheadercell1 = new PdfPCell(new Phrase(("Particulars"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            firstheadercell1.setBorder(Rectangle.NO_BORDER);
            firstheadercell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell1.setBorderColor(BaseColor.WHITE);
            firstHeaderTable.addCell(firstheadercell1);
            PdfPCell firstheadercell2 = new PdfPCell(new Phrase(("Pre.Month"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            firstheadercell2.setBorder(Rectangle.NO_BORDER);
            firstheadercell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell2.setBorderColor(BaseColor.WHITE);
            firstHeaderTable.addCell(firstheadercell2);
            PdfPCell firstheadercell3 = new PdfPCell(new Phrase(("Current Month"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            firstheadercell3.setBorder(Rectangle.NO_BORDER);
            firstheadercell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell3.setBorderColor(BaseColor.WHITE);
            firstHeaderTable.addCell(firstheadercell3);
            PdfPCell firstheadercell4 = new PdfPCell(new Phrase(("Difference"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            firstheadercell4.setBorder(Rectangle.NO_BORDER);
            firstheadercell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell4.setBorderColor(BaseColor.WHITE);
            firstHeaderTable.addCell(firstheadercell4);

            paragraphCell = new PdfPCell();
            paragraphCell.setBorder(Rectangle.NO_BORDER);
            paragraphCell.setBorderColor(BaseColor.WHITE);
            paragraphCell.setBorderColorRight(BaseColor.BLACK);
            paragraphCell.setBorderWidthRight(0.5f);
            paragraphCell.addElement(firstHeaderTable);
            earningTable.addCell(paragraphCell);

            PdfPCell firstheadercell5 = new PdfPCell(new Phrase(("Particulars"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            firstheadercell5.setBorder(Rectangle.NO_BORDER);
            firstheadercell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell5.setBorderColor(BaseColor.WHITE);
            secondHeaderTeable.addCell(firstheadercell5);
            PdfPCell firstheadercell6 = new PdfPCell(new Phrase(("Pre.Month"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            firstheadercell6.setBorder(Rectangle.NO_BORDER);
            firstheadercell6.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell6.setBorderColor(BaseColor.WHITE);
            secondHeaderTeable.addCell(firstheadercell6);
            PdfPCell firstheadercell7 = new PdfPCell(new Phrase(("Current Month"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            firstheadercell7.setBorder(Rectangle.NO_BORDER);
            firstheadercell7.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell7.setBorderColor(BaseColor.WHITE);
            secondHeaderTeable.addCell(firstheadercell7);
            PdfPCell firstheadercell8 = new PdfPCell(new Phrase(("Difference"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            firstheadercell8.setBorder(Rectangle.NO_BORDER);
            firstheadercell8.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell8.setBorderColor(BaseColor.WHITE);
            secondHeaderTeable.addCell(firstheadercell8);

            paragraphCell = new PdfPCell();
            paragraphCell.setBorder(Rectangle.NO_BORDER);
            paragraphCell.setBorderColor(BaseColor.WHITE);
            paragraphCell.addElement(secondHeaderTeable);
            earningTable.addCell(paragraphCell);
            paragraphCell = new PdfPCell(earningTable);
            paragraphCell.setBorder(Rectangle.NO_BORDER);
            paragraphCell.setBorderColor(BaseColor.WHITE);
            paragraphCell.setBorderColorBottom(BaseColor.BLACK);
            paragraphCell.setBorderWidthBottom(0.5f);
            paragraph.addCell(paragraphCell);
            outercell.addElement(paragraph);

            PdfPTable paragraph1 = new PdfPTable(1);
            paragraph1.setWidthPercentage(100.0f);

            PdfPCell paragraphCell1 = new PdfPCell();
            PdfPTable earningbodyTable = new PdfPTable(2);
            earningbodyTable.setWidthPercentage(100.0f);

            PdfPTable firstTable1 = new PdfPTable(4);
            firstTable1.setWidthPercentage(100.0f);
            PdfPTable secondTable1 = new PdfPTable(4);
            secondTable1.setWidthPercentage(100.0f);

            paragraphCell1.setBorder(Rectangle.NO_BORDER);
            paragraphCell1.setBorderColor(BaseColor.WHITE);

            Set<String> previouskeys = null;
            Set<String> currentkeys = null;
            Set<String> previousDeductionkeys = null;
            Set<String> currentDeductionskeys = null;
            previouskeys = preEarnings.keySet();
            currentkeys = Earnings.keySet();
            currentDeductionskeys = deductions.keySet();
            previousDeductionkeys = predeductions.keySet();
            int previousLength = previouskeys.size();
            int currentLength = currentkeys.size();
            double totalpreEarningAmount = 0.0;
            double totalcurrentEarningAmount = 0.0;
            double totalpreDeductionamount = 0.0;
            double totalcurrentDeductionAmount = 0.0;

            if (Earnings.size() == preEarnings.size()) {
            } else if (Earnings.size() > preEarnings.size()) {
                for (String str : Earnings.keySet()) {
                    if (!preEarnings.containsKey(str)) {
                        preEarnings.put(str, "0.00");
                    }
                }
            } else if ((Earnings.size() < preEarnings.size())) {
                for (String str : preEarnings.keySet()) {
                    if (!Earnings.containsKey(str)) {
                        Earnings.put(str, "0.00");
                    }
                }
            } else {

            }

            if (deductions.size() == predeductions.size()) {
            } else if (deductions.size() > predeductions.size()) {
                for (String str : deductions.keySet()) {
                    if (!predeductions.containsKey(str)) {
                        predeductions.put(str, "0.00");
                    }
                }
            } else if ((deductions.size() < predeductions.size())) {
                for (String str : predeductions.keySet()) {
                    if (!deductions.containsKey(str)) {
                        deductions.put(str, "0.00");
                    }
                }
            } else {

            }
            totalpreEarningAmount = TotalCount(preEarnings, totalpreEarningAmount);
            totalcurrentEarningAmount = TotalCount(Earnings, totalcurrentEarningAmount);
            totalpreDeductionamount = TotalCount(predeductions, totalpreDeductionamount);
            totalcurrentDeductionAmount = TotalCount(deductions, totalcurrentDeductionAmount);

            if (previousLength > currentLength) {
                firstTable1 = mapItration(preEarnings, Earnings, firstTable1);
            }
            if (previousDeductionkeys.size() > currentDeductionskeys.size()) {
                secondTable1 = mapItration(predeductions, deductions, secondTable1);
            }

            if (previousLength <= currentLength) {
                firstTable1 = mapItrationLessthan(Earnings, preEarnings, firstTable1);
            }
            if (previousDeductionkeys.size() <= currentDeductionskeys.size()) {
                secondTable1 = mapItrationLessthan(deductions, predeductions, secondTable1);
            }

            if (!preEarnings.isEmpty()) {
                firstTable1 = mapItrationpreviousMonth(preEarnings, firstTable1);

            }
            if (!Earnings.isEmpty()) {
                firstTable1 = mapItrationCurrentMonth(Earnings, firstTable1);
            }

            if (!predeductions.isEmpty()) {
                secondTable1 = mapItrationpreviousMonth(predeductions, secondTable1);
            }
            if (!deductions.isEmpty()) {
                secondTable1 = mapItrationCurrentMonth(deductions, secondTable1);
            }

            paragraphCell1 = new PdfPCell();
            paragraphCell1.setBottom(0.0f);
            paragraphCell1.setBorder(Rectangle.NO_BORDER);
            paragraphCell1.setBorderColor(BaseColor.WHITE);
            paragraphCell1.setBorderColorRight(BaseColor.BLACK);
            paragraphCell1.setBorderWidthRight(0.5f);
            paragraphCell1.addElement(firstTable1);
            earningbodyTable.addCell(paragraphCell1);
            paragraphCell1 = new PdfPCell();
            paragraphCell1.setBorder(Rectangle.NO_BORDER);
            paragraphCell1.setBorderColor(BaseColor.WHITE);
            paragraphCell1.addElement(secondTable1);
            earningbodyTable.addCell(paragraphCell1);

            paragraphCell1 = new PdfPCell(earningbodyTable);

            paragraphCell1.setBorder(Rectangle.NO_BORDER);
            paragraphCell1.setBorderColor(BaseColor.WHITE);
            paragraphCell1.setBorderWidthBottom(0.5f);
            paragraphCell1.setBorderColorBottom(BaseColor.BLACK);

            paragraph1.addCell(paragraphCell1);

            outercell.addElement(paragraph1);

            PdfPTable Totalparagraph1 = new PdfPTable(1);
            Totalparagraph1.setWidthPercentage(100.0f);
            Totalparagraph1.setSpacingAfter(5.0f);

            PdfPCell totalparagraphCell1 = new PdfPCell();
            PdfPTable totalearningbodyTable = new PdfPTable(2);
            totalearningbodyTable.setWidthPercentage(100.0f);

            PdfPTable totalDeductionTable = new PdfPTable(4);
            PdfPTable TotalEarningTable = new PdfPTable(4);
            totalDeductionTable.setSpacingAfter(5.0f);
            totalparagraphCell1.setBorder(Rectangle.NO_BORDER);
            totalparagraphCell1.setBorderColor(BaseColor.WHITE);
            TotalEarningTable.setSpacingAfter(5.0f);

            TotalEarningTable.setWidthPercentage(100.0f);
            totalDeductionTable.setWidthPercentage(100.0f);
            TotalEarningTable.setWidths(new float[]{1, 1, 1, 1});
            totalDeductionTable.setWidths(new float[]{1, 1, 1, 1});

            PdfPCell keycell = null;
            PdfPCell valuecell = null;
            PdfPCell currentcell = null;
            PdfPCell differentcell = null;

            keycell = new PdfPCell(new Phrase(("Total Earnings"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            keycell.setBorder(Rectangle.NO_BORDER);
            keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
            keycell.setBorderColor(BaseColor.WHITE);
            TotalEarningTable.addCell(keycell);

            valuecell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalpreEarningAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            valuecell.setBorder(Rectangle.NO_BORDER);
            valuecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            valuecell.setBorderColor(BaseColor.WHITE);
            TotalEarningTable.addCell(valuecell);

            currentcell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalcurrentEarningAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            currentcell.setBorder(Rectangle.NO_BORDER);
            currentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            currentcell.setBorderColor(BaseColor.WHITE);
            TotalEarningTable.addCell(currentcell);
            Double different = totalpreEarningAmount - totalcurrentEarningAmount;
            differentcell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(different), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            differentcell.setBorder(Rectangle.NO_BORDER);
            differentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            differentcell.setBorderColor(BaseColor.WHITE);
            TotalEarningTable.addCell(differentcell);

            totalparagraphCell1 = new PdfPCell();
            totalparagraphCell1.setBorder(Rectangle.NO_BORDER);
            totalparagraphCell1.setBorderColor(BaseColor.WHITE);
            totalparagraphCell1.setBorderColorRight(BaseColor.BLACK);
            totalparagraphCell1.setBorderWidthRight(0.5f);
            totalparagraphCell1.addElement(TotalEarningTable);
            totalearningbodyTable.addCell(totalparagraphCell1);

            firstheadercell5 = new PdfPCell(new Phrase(("Total Deductions"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            firstheadercell5.setBorder(Rectangle.NO_BORDER);
            firstheadercell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell5.setBorderColor(BaseColor.WHITE);
            totalDeductionTable.addCell(firstheadercell5);

            firstheadercell6 = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalpreDeductionamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            firstheadercell6.setBorder(Rectangle.NO_BORDER);
            firstheadercell6.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell6.setBorderColor(BaseColor.WHITE);
            totalDeductionTable.addCell(firstheadercell6);

            firstheadercell7 = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalcurrentDeductionAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            firstheadercell7.setBorder(Rectangle.NO_BORDER);
            firstheadercell7.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell7.setBorderColor(BaseColor.WHITE);
            totalDeductionTable.addCell(firstheadercell7);
            Double differencedeductions = totalpreDeductionamount - totalcurrentDeductionAmount;
            firstheadercell8 = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(differencedeductions), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            firstheadercell8.setBorder(Rectangle.NO_BORDER);
            firstheadercell8.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstheadercell8.setBorderColor(BaseColor.WHITE);
            totalDeductionTable.addCell(firstheadercell8);

            totalparagraphCell1 = new PdfPCell();
            totalparagraphCell1.setBorder(Rectangle.NO_BORDER);
            totalparagraphCell1.setBorderColor(BaseColor.WHITE);
            totalparagraphCell1.addElement(totalDeductionTable);
            totalearningbodyTable.addCell(totalparagraphCell1);
            totalparagraphCell1 = new PdfPCell(totalearningbodyTable);
            totalparagraphCell1.setBorder(Rectangle.NO_BORDER);
            totalparagraphCell1.setBorderColor(BaseColor.WHITE);
            totalparagraphCell1.setBorderColorBottom(BaseColor.BLACK);
            totalparagraphCell1.setBorderWidthBottom(0.5f);
            Totalparagraph1.addCell(totalparagraphCell1);
            outercell.addElement(Totalparagraph1);

            PdfPTable netparagraph1 = new PdfPTable(1);
            netparagraph1.setWidthPercentage(100.0f);
            netparagraph1.setSpacingAfter(5.0f);

            PdfPCell netparagraphCell1 = new PdfPCell();

            PdfPTable netearningbodyTable = new PdfPTable(2);
            netearningbodyTable.setWidthPercentage(100.0f);
            netearningbodyTable.setSpacingAfter(5.0f);

            PdfPTable netDeductionTable = new PdfPTable(4);
            TotalEarningTable.setSpacingAfter(5.0f);
            PdfPTable netEarningTable = new PdfPTable(4);
            netEarningTable.setSpacingAfter(5.0f);
            netparagraphCell1.setBorder(Rectangle.NO_BORDER);
            netparagraphCell1.setBorderColor(BaseColor.WHITE);

            netDeductionTable.setWidthPercentage(100.0f);
            netEarningTable.setWidthPercentage(100.0f);

            PdfPCell netkeycell = new PdfPCell(new Phrase((""), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            netkeycell.setBorder(Rectangle.NO_BORDER);
            netkeycell.setHorizontalAlignment(Element.ALIGN_LEFT);
            netkeycell.setBorderColor(BaseColor.WHITE);
            netEarningTable.addCell(netkeycell);
            PdfPCell netvaluecell = new PdfPCell(new Phrase((""), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            netvaluecell.setBorder(Rectangle.NO_BORDER);
            netvaluecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            netvaluecell.setBorderColor(BaseColor.WHITE);
            netEarningTable.addCell(netvaluecell);
            PdfPCell netcurrentcell = new PdfPCell(new Phrase((""), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            netcurrentcell.setBorder(Rectangle.NO_BORDER);
            netcurrentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            netcurrentcell.setBorderColor(BaseColor.WHITE);
            netEarningTable.addCell(netcurrentcell);
            PdfPCell netdifferentcell = new PdfPCell(new Phrase((""), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            netdifferentcell.setBorder(Rectangle.NO_BORDER);
            netdifferentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            netdifferentcell.setBorderColor(BaseColor.WHITE);
            netEarningTable.addCell(netdifferentcell);

            netparagraphCell1 = new PdfPCell();
            netparagraphCell1.setBorder(Rectangle.NO_BORDER);
            netparagraphCell1.setBorderColor(BaseColor.WHITE);
            netparagraphCell1.setBorderColorRight(BaseColor.BLACK);
            //netparagraphCell1.setBorderWidthRight(0.5f);
            netparagraphCell1.setBorderColorBottom(BaseColor.BLACK);
            netparagraphCell1.setBorderWidthBottom(0.5f);
            netparagraphCell1.addElement(netEarningTable);
            netearningbodyTable.addCell(netparagraphCell1);

            PdfPCell dedKeyCell = new PdfPCell(new Phrase(("Net Payable"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            dedKeyCell.setBorder(Rectangle.NO_BORDER);
            dedKeyCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            dedKeyCell.setBorderColor(BaseColor.WHITE);
            netDeductionTable.addCell(dedKeyCell);
            double netPayablePre = totalpreEarningAmount - totalpreDeductionamount;
            PdfPCell dedValueCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(netPayablePre), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            dedValueCell.setBorder(Rectangle.NO_BORDER);
            dedValueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            dedValueCell.setBorderColor(BaseColor.WHITE);
            netDeductionTable.addCell(dedValueCell);
            double netPayablecurr = totalcurrentEarningAmount - totalcurrentDeductionAmount;
            PdfPCell dedCurrentCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(netPayablecurr), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            dedCurrentCell.setBorder(Rectangle.NO_BORDER);
            dedCurrentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            dedCurrentCell.setBorderColor(BaseColor.WHITE);
            netDeductionTable.addCell(dedCurrentCell);
            double diff = netPayablePre - netPayablecurr;

            PdfPCell dedDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(diff), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            dedDifferenceCell.setBorder(Rectangle.NO_BORDER);
            dedDifferenceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            dedDifferenceCell.setBorderColor(BaseColor.WHITE);
            netDeductionTable.addCell(dedDifferenceCell);

            netparagraphCell1 = new PdfPCell();
            netparagraphCell1.setBorder(Rectangle.NO_BORDER);
            netparagraphCell1.setBorderColor(BaseColor.WHITE);
            netparagraphCell1.addElement(netDeductionTable);
            netearningbodyTable.addCell(netparagraphCell1);
            netparagraphCell1 = new PdfPCell(netearningbodyTable);
            netparagraphCell1.setBorder(Rectangle.NO_BORDER);
            netparagraphCell1.setBorderColorBottom(BaseColor.BLACK);
            netparagraphCell1.setBorderWidthBottom(0.5f);
            netparagraph1.addCell(netparagraphCell1);

            outercell.addElement(netparagraph1);

            PdfPTable TotalSalaryDifferenceSalary = new PdfPTable(1);
            TotalSalaryDifferenceSalary.setWidthPercentage(100.0f);

            PdfPCell totalDifferenceCell = new PdfPCell(new Phrase(("Total Salary Difference"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            totalDifferenceCell.setBorderColorBottom(BaseColor.BLACK);
            totalDifferenceCell.setPaddingBottom(6f);
            TotalSalaryDifferenceSalary.addCell(totalDifferenceCell);
            outercell.addElement(TotalSalaryDifferenceSalary);

            PdfPTable SalaryDifferenceSalaryTotal = new PdfPTable(6);
            SalaryDifferenceSalaryTotal.setWidthPercentage(100.0f);

            totalDifferenceCell = new PdfPCell(new Phrase(("Pre.Gross Total"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalpreEarningAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(("Pre.Deduction Total"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalpreDeductionamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(("Pre.Net Pay"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(netPayablePre), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(("Curr.Gross Total"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalcurrentEarningAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(("Curr.Deduction Total"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(totalcurrentDeductionAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(("Curr.Net Pay"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(netPayablecurr), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(("Difference"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            totalDifferenceCell.setBorderColorBottom(BaseColor.BLACK);
            totalDifferenceCell.setBorderWidth(0.5f);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);
            double grossTotalDiff = totalpreEarningAmount - totalcurrentEarningAmount;

            totalDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(grossTotalDiff), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            totalDifferenceCell.setBorderColorBottom(BaseColor.BLACK);
            totalDifferenceCell.setBorderWidth(0.5f);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(("Difference"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            totalDifferenceCell.setBorderColorBottom(BaseColor.BLACK);
            totalDifferenceCell.setBorderWidth(0.5f);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            double DeductionTotalDiff = totalpreDeductionamount - totalcurrentDeductionAmount;
            totalDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(DeductionTotalDiff), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            totalDifferenceCell.setBorderColorBottom(BaseColor.BLACK);
            totalDifferenceCell.setBorderWidth(0.5f);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            double netpayementTotalDiff = netPayablePre - netPayablecurr;
            totalDifferenceCell = new PdfPCell(new Phrase(("Difference"), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            totalDifferenceCell.setBorderColorBottom(BaseColor.BLACK);
            totalDifferenceCell.setBorderWidth(0.5f);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            totalDifferenceCell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(netpayementTotalDiff), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            totalDifferenceCell.setBorder(Rectangle.NO_BORDER);
            totalDifferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalDifferenceCell.setBorderColor(BaseColor.WHITE);
            totalDifferenceCell.setBorderColorBottom(BaseColor.BLACK);
            totalDifferenceCell.setBorderWidth(0.5f);
            SalaryDifferenceSalaryTotal.addCell(totalDifferenceCell);

            outercell.addElement(SalaryDifferenceSalaryTotal);

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

    

    public static PdfPTable mapItration(LinkedHashMap currentMap, LinkedHashMap previousMap, PdfPTable firstTable1) {

        PdfPCell keycell = null;
        PdfPCell valuecell = null;
        PdfPCell currentcell = null;
        PdfPCell differentcell = null;
        Iterator<Map.Entry<String, String>> iter = currentMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            Iterator<Map.Entry<String, String>> iter1 = previousMap.entrySet().iterator();
            while (iter1.hasNext()) {
                Map.Entry<String, String> entry1 = iter1.next();
                if (entry1.getKey().equalsIgnoreCase(entry.getKey())) {
                    keycell = new PdfPCell(new Phrase((entry1.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    keycell.setBorder(Rectangle.NO_BORDER);
                    keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    keycell.setBorderColor(BaseColor.WHITE);
                    firstTable1.addCell(keycell);
                    String preAmount = entry.getValue();
                    valuecell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(entry.getValue())), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    valuecell.setBorder(Rectangle.NO_BORDER);
                    valuecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    valuecell.setBorderColor(BaseColor.WHITE);
                    firstTable1.addCell(valuecell);
                    String currentAmount = entry1.getValue();
                    currentcell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(entry1.getValue())), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    currentcell.setBorder(Rectangle.NO_BORDER);
                    currentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    currentcell.setBorderColor(BaseColor.WHITE);
                    firstTable1.addCell(currentcell);
                    double difference = Double.parseDouble(preAmount) - Double.parseDouble(currentAmount);
                    differentcell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(difference), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    differentcell.setBorder(Rectangle.NO_BORDER);
                    differentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    differentcell.setBorderColor(BaseColor.WHITE);
                    firstTable1.addCell(differentcell);
                    iter1.remove();
                    iter.remove();
                    break;
                }

            }

        }

        return firstTable1;
    }

    public static PdfPTable mapItrationLessthan(LinkedHashMap currentMap, LinkedHashMap previousMap, PdfPTable firstTable1) {

        PdfPCell keycell = null;
        PdfPCell valuecell = null;
        PdfPCell currentcell = null;
        PdfPCell differentcell = null;
        //System.out.println("previousMap" + previousMap.toString());
        //System.out.println("currentMap" + currentMap.toString());
        Iterator<Map.Entry<String, String>> iter = currentMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            Iterator<Map.Entry<String, String>> iter1 = previousMap.entrySet().iterator();
            while (iter1.hasNext()) {
                Map.Entry<String, String> entry1 = iter1.next();
                if (entry1.getKey().equalsIgnoreCase(entry.getKey())) {
                    keycell = new PdfPCell(new Phrase((entry.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    keycell.setBorder(Rectangle.NO_BORDER);
                    keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    keycell.setBorderColor(BaseColor.WHITE);
                    firstTable1.addCell(keycell);
                    String preAmount = entry1.getValue();
                    valuecell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(preAmount)), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    valuecell.setBorder(Rectangle.NO_BORDER);
                    valuecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    valuecell.setBorderColor(BaseColor.WHITE);
                    firstTable1.addCell(valuecell);
                    String currentAmount = entry.getValue();
                    currentcell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.valueOf(currentAmount)), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    currentcell.setBorder(Rectangle.NO_BORDER);
                    currentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    currentcell.setBorderColor(BaseColor.WHITE);
                    firstTable1.addCell(currentcell);
                    double difference = Double.parseDouble(preAmount) - Double.parseDouble(currentAmount);
                    differentcell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(difference), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                    differentcell.setBorder(Rectangle.NO_BORDER);
                    differentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    differentcell.setBorderColor(BaseColor.WHITE);
                    firstTable1.addCell(differentcell);
                    iter1.remove();
                    iter.remove();
                    break;
                }

            }

        }
        return firstTable1;
    }

    public static PdfPTable mapItrationpreviousMonth(LinkedHashMap currentMap, PdfPTable firstTable1) {
        PdfPCell keycell = null;
        PdfPCell valuecell = null;
        PdfPCell currentcell = null;
        PdfPCell differentcell = null;
        Iterator<Map.Entry<String, String>> iter = currentMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();

            keycell = new PdfPCell(new Phrase((entry.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            keycell.setBorder(Rectangle.NO_BORDER);
            keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
            keycell.setBorderColor(BaseColor.WHITE);
            firstTable1.addCell(keycell);
            String preAmount = entry.getValue();
            valuecell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.parseDouble(preAmount)), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            valuecell.setBorder(Rectangle.NO_BORDER);
            valuecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            valuecell.setBorderColor(BaseColor.WHITE);
            firstTable1.addCell(valuecell);
            String currentAmount = "0.00";
            currentcell = new PdfPCell(new Phrase(currentAmount, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            currentcell.setBorder(Rectangle.NO_BORDER);
            currentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            currentcell.setBorderColor(BaseColor.WHITE);
            firstTable1.addCell(currentcell);
            double difference = Double.parseDouble(preAmount) - Double.parseDouble(currentAmount);
            differentcell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(difference), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            differentcell.setBorder(Rectangle.NO_BORDER);
            differentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            differentcell.setBorderColor(BaseColor.WHITE);
            firstTable1.addCell(differentcell);
            iter.remove();
            break;
        }

        return firstTable1;
    }

    public static PdfPTable mapItrationCurrentMonth(LinkedHashMap currentMap, PdfPTable firstTable1) {
        PdfPCell keycell = null;
        PdfPCell valuecell = null;
        PdfPCell currentcell = null;
        PdfPCell differentcell = null;
        Iterator<Map.Entry<String, String>> iter = currentMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            keycell = new PdfPCell(new Phrase((entry.getKey()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            keycell.setBorder(Rectangle.NO_BORDER);
            keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
            keycell.setBorderColor(BaseColor.WHITE);
            firstTable1.addCell(keycell);
            String preAmount = "0.00";
            valuecell = new PdfPCell(new Phrase(preAmount, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            valuecell.setBorder(Rectangle.NO_BORDER);
            valuecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            valuecell.setBorderColor(BaseColor.WHITE);
            firstTable1.addCell(valuecell);
            String currentAmount = entry.getValue();
            currentcell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Double.parseDouble(currentAmount)), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            currentcell.setBorder(Rectangle.NO_BORDER);
            currentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            currentcell.setBorderColor(BaseColor.WHITE);
            firstTable1.addCell(currentcell);
            double difference = Double.parseDouble(preAmount) - Double.parseDouble(currentAmount);
            differentcell = new PdfPCell(new Phrase(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(difference), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
            differentcell.setBorder(Rectangle.NO_BORDER);
            differentcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            differentcell.setBorderColor(BaseColor.WHITE);
            firstTable1.addCell(differentcell);
            iter.remove();
            break;
        }

        return firstTable1;
    }

    public static double TotalCount(LinkedHashMap table, double count) {
        Iterator<Map.Entry<String, String>> iter = table.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            count += Double.parseDouble(entry.getValue());
        }
        return count;
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
