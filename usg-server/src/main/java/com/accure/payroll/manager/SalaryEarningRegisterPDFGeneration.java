/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
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
import com.accure.usg.common.manager.DBManager;
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
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
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
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class SalaryEarningRegisterPDFGeneration {

    public ByteArrayOutputStream generateSalaryRegisterReport(List<SalarySlipRegisterReport> salarySlipList, String path, int monthValue, int yearValue,String fin) throws DocumentException, FileNotFoundException, BadElementException, IOException, Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Document document = null;

        SalarySlipRegisterReportManager srr = new SalarySlipRegisterReportManager();
        List<SalaryHead> earningsHead = srr.getAllEarningHeads();
        int earningsize = 0;
        int size = 0;
        if (earningsHead != null) {
            earningsize = earningsHead.size() / 3;
            if ((earningsHead.size() % 3) == 0) {
                earningsize = earningsize;
            } else {
                earningsize = earningsize + 1;
            }

        }

        size = earningsize + 3;
        if (size > 5) {
            document = new Document(PageSize.A1.rotate());
            PdfWriter.getInstance(document, bos);
        } else {
            document = new Document(PageSize.A3.rotate());
            PdfWriter.getInstance(document, bos);
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
        image1.scaleAbsolute(100.0f, 100.0f);

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
        headerPhrase.add(new Phrase(new Chunk("  Monthly Salary Earning Register For " + monthString + "-" + yearValue, FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD, BaseColor.BLACK))));
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
        header.setSpacingAfter(5.0f);
        outercell.addElement(header);

        PdfPTable columnHeading = new PdfPTable(size);
        columnHeading.setSpacingBefore(5.0f);
        int columns[] = new int[size];
        columns[0] = 1;
        for (int k = 1; k < size; k++) {
            columns[k] = 2;
        }
        //System.out.println(columns);
        columnHeading.setWidths(columns);
        columnHeading.setWidthPercentage(100);

        PdfPCell snoCell = new PdfPCell(new Phrase(new Chunk("\nS.No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK))));
        snoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        snoCell.setPaddingLeft(1f);
        PdfPCell namecell = new PdfPCell(new Phrase(new Chunk("Employee Code\n\n Employee Name\n\nDesignation\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK))));
        namecell.setHorizontalAlignment(Element.ALIGN_CENTER);

        columnHeading.addCell(snoCell);
        columnHeading.addCell(namecell);

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
                String head3 = "";
                if (earningHeadCount < earningsHead.size()) {
                    SalaryHead salaryHead3 = earningsHead.get(earningHeadCount);
                    if (salaryHead3.getShortDescription() != null && salaryHead3.getShortDescription() != "" && !salaryHead3.getShortDescription().isEmpty()) {
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

                earningCell = new PdfPCell(new Phrase(new Chunk(totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK))));
                earningCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                columnHeading.addCell(earningCell);
            }
            PdfPCell totalEarnings = new PdfPCell(new Phrase(new Chunk("\n\nTotal Earnings\n", FontFactory.getFont(FontFactory.TIMES_ROMAN,16, Font.BOLD, BaseColor.BLACK))));
            totalEarnings.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeading.addCell(totalEarnings);
        }

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
                name = salarylist.getEmployeeName();
                year = salarylist.getYear();
                month = salarylist.getMonth();
                designation = salarylist.getDesignationName();
                attendacne = salarylist.getAttendance().getPresent();
                code = salarylist.getEmployeeCode();
                LinkedHashMap<String, String> Earnings = new LinkedHashMap<String, String>();
                Earnings earingInfo = salarylist.getEarningsInfo();
                Deductions deductionInfo = salarylist.getDeductionsInfo();

                ArrayList<EarningHeadsDetails> earningList = earingInfo.getEarningHeads();

                if (earingInfo != null) {
                    if (earingInfo.isIsEarningHeads()) {
                        if (earningList != null && earningList.size() > 0) {
                            for (int i = 0; i < earningList.size(); i++) {
                                String showonSlip = earningList.get(i).getDescriptionInfo().getShowOnRegister();

                                if (earningList.get(i).getAmount()!=0.0 &&  showonSlip.equalsIgnoreCase("Yes")) {
                                    totalEarnings += earningList.get(i).getAmount();
                                    Earnings.put(earningList.get(i).getDescriptionInfo().getShortDescription(), roundTwoDecimalPoints(earningList.get(i).getAmount()));
                                }

                            }
                        }
                    }

                }

                //System.out.println("earinngs" + Earnings);
                //System.out.println("map" + earningMap);
                PdfPTable employeeData = new PdfPTable(size);
                employeeData.setWidths(columns);
                employeeData.setWidthPercentage(100);

                PdfPCell Cell1 = new PdfPCell(new Phrase(new Chunk("\n" + (count), FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK))));
                Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                snoCell.setPaddingLeft(1f);
                PdfPCell Cell2 = new PdfPCell(new Phrase(new Chunk(code + "\n\n" + name + "\n\n" + designation + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK))));
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
                        }else{
                            totalHead+="0.00";
                        }
                        totalHead += "\n\n";
                        if (value2 != null && value2 != "" && !value2.isEmpty()) {
                            totalHead += value2;
                        }else{
                            totalHead+="0.00";
                        }
                        totalHead += "\n\n";
                        if (value3 != null && value3 != "" && !value3.isEmpty()) {
                            totalHead += value3;
                        }else{
                            totalHead+="0.00";
                        }
                        totalHead += "\n\n";
                        Cell1 = new PdfPCell(new Phrase(new Chunk(totalHead, FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK))));
                        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        employeeData.addCell(Cell1);

                    }
                    PdfPCell totalEarningsData = new PdfPCell(new Phrase(new Chunk("\n\n" + roundTwoDecimalPoints(totalEarnings) + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK))));
                    totalEarningsData.setHorizontalAlignment(Element.ALIGN_CENTER);
                    employeeData.addCell(totalEarningsData);
                }
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
