/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.finance.dto.ContraVoucher;
import com.accure.finance.dto.Ledger;
import com.accure.finance.dto.LedgerList;
import com.accure.finance.dto.Location;
import com.accure.finance.dto.ManageOpeningBalance;
import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.dto.ReceiptVoucher;
import com.accure.finance.dto.TrialBalance;
import com.accure.hrms.dto.FinancialYear;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
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
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author User
 */
public class BankReconcilationReportManager {

    public ByteArrayOutputStream bankReconcilationReport(String fromDate, String toDate, String voucherdata, String ledger, String path, String fin, String fromDateStr, String ddo, String location, String finYear) throws DocumentException, FileNotFoundException, Exception {
        Long fromVD = 0L;
        Long toVD = 0L;
        Long finStart = 0L;
        fromVD = saveInMilliSecondDate(fromDate);
        toVD = saveInMilliSecondDate(toDate);
        finStart = saveInMilliSecondDate(fromDateStr);
        double openingBalance = 0.00;
        double closingBalance = 0.0;
        double totalChequeAmount = 0.0;
        double totalCrAmount = 0.0;
        double totalDrAmount = 0.0;
        double transactionCrAmount = 0.00;
        double transactionDrAmount = 0.00;
        double cbl = 0.0;
        String cbl1 = "";
        String drtext = "";
        double drvalue = 0.00;
        String crtext = "";
        double crvalue = 0.00;

        HashMap<String, String> conditionMap1 = new HashMap<String, String>();
        conditionMap1.put("ledgerId", ledger);
        conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String ledgerType = null;
        String drCrType = null;
        String ledgerName = "";
        String ledgerJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, conditionMap1);
        if (ledgerJson == null || ledgerJson.isEmpty()) {

            openingBalance = 0.00;
//            HashMap<String, String> conditionMap2 = new HashMap<String, String>();
//            conditionMap2.put("ledgerId", ledger);
//            conditionMap2.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//            String ledgerNameJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_TABLE, conditionMap2);
            String ledgerNameJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_TABLE, ledger);
            List<Ledger> ledgerNameList = new Gson().fromJson(ledgerNameJson, new TypeToken<List<Ledger>>() {
            }.getType());
            Ledger ledgerNameListObj = ledgerNameList.get(0);
            ledgerName = ledgerNameListObj.getLedgerName();
        } else {
            List<ManageOpeningBalance> openingBalanceList = new Gson().fromJson(ledgerJson, new TypeToken<List<ManageOpeningBalance>>() {
            }.getType());
            ManageOpeningBalance openingBalanceObj = openingBalanceList.get(0);
            ledgerType = openingBalanceObj.getLedgerId();
            drCrType = openingBalanceObj.getAmountType();
            ledgerName = openingBalanceObj.getLedgerName();

            if (ledgerType.equalsIgnoreCase(ledger));
            String obal = openingBalanceObj.getAmount();
            if(obal != null){
            openingBalance = roundTwoDecimals(Double.parseDouble(obal));
            }

//        if(openingBalanceObj.getAmountType().equalsIgnoreCase("Dr"))
//          {
//              openingBalance = + roundTwoDecimals(openingBalance); 
//          }
//        if(openingBalanceObj.getAmountType().equalsIgnoreCase("Cr"))
//          {
//              openingBalance = - openingBalance; 
//          }
            openingBalance = +roundTwoDecimals(openingBalance);
//             Map<String, Object> manageOpeningBalanceMap = null;
//                manageOpeningBalanceMap = new CarryForwardLedgerBalanceManager().search(ledger, ddo, location, fromDate, toDate);
        }

        PaymentVoucher pvobj = new Gson().fromJson(voucherdata, new TypeToken<PaymentVoucher>() {
        }.getType());

        RestClient aql1 = new RestClient();
        String Active1 = "\"Active\"";

        String pvTable = ApplicationConstants.USG_DB1 + ApplicationConstants.PAYMENT_VOUCHER_TABLE + "`";

        String paymentSearchQuery = "";

//        if (fromDate != null && fromDate != "" && !fromDate.isEmpty()) {
//          
//            paymentSearchQuery = paymentSearchQuery + " pv.voucherDateInMilliSecond>=" + fromVD;
//
//        }
//        if (toDate != null && toDate != "" && !toDate.isEmpty()) {
//
//            paymentSearchQuery = paymentSearchQuery + " and pv.voucherDateInMilliSecond<=" + toVD;
//
//        }
        String paymentVoucherQuery = "select * from " + pvTable + ""
                + " as pv where " + paymentSearchQuery + " pv.status=\"Active\"";

        String paymentVoucherOutput = aql1.getRestData(ApplicationConstants.END_POINT, paymentVoucherQuery);

        List<PaymentVoucher> payVocList = null;
        if (paymentVoucherOutput != null && !paymentVoucherOutput.isEmpty() && !paymentVoucherOutput.equals("[]")) {
            payVocList = new Gson().fromJson(paymentVoucherOutput, new TypeToken< ArrayList<PaymentVoucher>>() {
            }.getType());
        }

        List<LedgerList> payVocFinalList = new ArrayList<LedgerList>();
        if (paymentVoucherOutput != null && !paymentVoucherOutput.isEmpty() && !paymentVoucherOutput.equals("[]")) {
            for (PaymentVoucher payVou : payVocList) {
                String entryStatusRpt1 = payVou.getEntryStatus();
                for (int k1 = 0; k1 < payVou.getLedgerList().size(); k1++) {
                    long vdms = payVou.getVoucherDateInMilliSecond();
                    if ((payVou.getLedgerList().get(k1).getLedger().equals(ledger)) && (payVou.getLedgerList().get(k1).getDrCr().equals("Dr")) && (vdms >= finStart) && (vdms <= fromVD)) {
                        drtext = payVou.getLedgerList().get(k1).getDrAmount();
                        drvalue = roundTwoDecimals(Double.parseDouble(drtext));
                        totalDrAmount = roundTwoDecimals(totalDrAmount + drvalue);

                    }
                    if ((payVou.getLedgerList().get(k1).getLedger().equals(ledger)) && (payVou.getLedgerList().get(k1).getDrCr().equals("Cr")) && (vdms >= finStart) && (vdms <= fromVD)) {
                        crtext = payVou.getLedgerList().get(k1).getCrAmount();
                        crvalue = roundTwoDecimals(Double.parseDouble(crtext));
                        totalCrAmount = roundTwoDecimals(totalCrAmount + crvalue);

                    }
                    if ((entryStatusRpt1 != null)) {
//                        if ((payVou.getLedgerList().get(k1).getLedger().equals(ledger)) && (payVou.getPaymentMode().equals("Cheque")) && (payVou.getLedgerList().get(k1).getDrCr().equals("Cr")) && (payVou.getEntryStatus().equals("Uncleared Entries")) && (vdms >= fromVD) && (vdms <= toVD) && ((payVou.getPostingStatus().equals("Posted")))) {
                         if ((payVou.getLedgerList().get(k1).getLedger().equals(ledger)) && (payVou.getPaymentMode().equals("Cheque")) && (payVou.getLedgerList().get(k1).getDrCr().equals("Cr")) && (payVou.getEntryStatus().equals("Uncleared Entries")) && (vdms >= finStart) && (vdms <= fromVD) && ((payVou.getPostingStatus().equals("Posted")))) {
                            for (int cn = 0; cn < payVou.getChequeList().size(); cn++) {
                                payVou.getLedgerList().get(cn).setVoucherDtReport(payVou.getVoucherDate());
                                payVou.getLedgerList().get(cn).setVoucherNoReport(payVou.getVoucherNo());
                                payVou.getLedgerList().get(cn).setLocationReport(payVou.getLocation());
                                payVou.getLedgerList().get(cn).setVoucherDateInMilliSecondReport(payVou.getVoucherDateInMilliSecond());
                                payVou.getLedgerList().get(cn).setPaymentModeReport(payVou.getPaymentMode());
//                            payVou.getLedgerList().get(cn).setLedger(payVou.getLedgerList().get(k1).getLedger());
//                            payVou.getLedgerList().get(k1).setInFavourOfReport(payVou.getInFavorOf());
//                            payVou.getLedgerList().get(k1).setChequeNoReport(payVou.getChequeNo());
//                            payVou.getLedgerList().get(k1).setChequeDtReport(payVou.getChequeDate());
//                            payVou.getLedgerList().get(k1).setChequeAmtReport(payVou.getChequeAmount());
//                            payVou.getLedgerList().get(cn).setDrCr(payVou.getLedgerList().get(k1).getDrCr());
                                payVou.getLedgerList().get(cn).setDrCr(payVou.getLedgerList().get(k1).getDrCr());
                                payVou.getLedgerList().get(cn).setInFavourOfReport(payVou.getChequeList().get(cn).getInFavorOf());
                                payVou.getLedgerList().get(cn).setChequeNoReport(payVou.getChequeList().get(cn).getChequeNo());
                                payVou.getLedgerList().get(cn).setChequeDtReport(payVou.getChequeList().get(cn).getChequeDate());
                                payVou.getLedgerList().get(cn).setChequeAmtReport(payVou.getChequeList().get(cn).getChequeAmount());
                                payVou.getLedgerList().get(cn).setVoucherTyReport(payVou.getVoucherName());
                                payVocFinalList.add(payVou.getLedgerList().get(cn));
                            }

                        }
                    }
                }

            }
        }

        ReceiptVoucher rvobj = new Gson().fromJson(voucherdata, new TypeToken<ReceiptVoucher>() {
        }.getType());

        RestClient aql3 = new RestClient();
        String Active3 = "\"Active\"";

        String rvTable = ApplicationConstants.USG_DB1 + ApplicationConstants.RECEIPT_VOUCHER_TABLE + "`";

        String receiptSearchQuery = "";

//        if (fromDate != null && fromDate != "" && !fromDate.isEmpty()) {
//          
//            receiptSearchQuery = receiptSearchQuery + " rv.voucherDateInMilliSecond>=" + fromVD;
//
//        }
//        if (toDate != null && toDate != "" && !toDate.isEmpty()) {
//
//            receiptSearchQuery = receiptSearchQuery + " and rv.voucherDateInMilliSecond<=" + toVD;
//
//        }
        String receiptVoucherQuery = "select * from " + rvTable + ""
                + " as rv where " + receiptSearchQuery + " rv.status=\"Active\"";

        String receiptVoucherOutput = aql3.getRestData(ApplicationConstants.END_POINT, receiptVoucherQuery);

        List<ReceiptVoucher> recVocList = null;
        if (receiptVoucherOutput != null && !receiptVoucherOutput.isEmpty() && !receiptVoucherOutput.equals("[]")) {
            recVocList = new Gson().fromJson(receiptVoucherOutput, new TypeToken< ArrayList<ReceiptVoucher>>() {
            }.getType());
        }

        List<LedgerList> recVocFinalList = new ArrayList<LedgerList>();
        if (receiptVoucherOutput != null && !receiptVoucherOutput.isEmpty() && !receiptVoucherOutput.equals("[]")) {
            for (ReceiptVoucher recVou : recVocList) {
                String entryStatusRpt1 = recVou.getEntryStatus();
                for (int k3 = 0; k3 < recVou.getLedgerList().size(); k3++) {
                    long vdms = recVou.getVoucherDateInMilliSecond();
                    if ((recVou.getLedgerList().get(k3).getLedger().equals(ledger)) && (recVou.getLedgerList().get(k3).getDrCr().equals("Dr")) && (vdms >= finStart) && (vdms <= fromVD)) {
                        drtext = recVou.getLedgerList().get(k3).getDrAmount();
                        drvalue = roundTwoDecimals(Double.parseDouble(drtext));
                        totalDrAmount = roundTwoDecimals(totalDrAmount + drvalue);

                    }
                    if ((recVou.getLedgerList().get(k3).getLedger().equals(ledger)) && (recVou.getLedgerList().get(k3).getDrCr().equals("Cr")) && (vdms >= finStart) && (vdms <= fromVD)) {
                        crtext = recVou.getLedgerList().get(k3).getCrAmount();
                        crvalue = roundTwoDecimals(Double.parseDouble(crtext));
                        totalCrAmount = roundTwoDecimals(totalCrAmount + crvalue);

                    }
//                    closingBalance = roundTwoDecimals(openingBalance + totalCrAmount - totalDrAmount);
                    if ((entryStatusRpt1 != null)) {

                        if ((recVou.getLedgerList().get(k3).getLedger().equals(ledger)) && (recVou.getPaymentMode().equals("Cheque")) && (recVou.getLedgerList().get(k3).getDrCr().equals("Dr")) && (recVou.getEntryStatus().equals("Uncleared Entries")) && (vdms >= finStart) && (vdms <= fromVD) && ((recVou.getPostingStatus().equals("Posted")))) {
                            for (int cn = 0; cn < recVou.getChequeList().size(); cn++) {
                                recVou.getLedgerList().get(cn).setVoucherDtReport(recVou.getVoucherDate());
                                recVou.getLedgerList().get(cn).setVoucherNoReport(recVou.getVoucherNo());
                                recVou.getLedgerList().get(cn).setLocationReport(recVou.getLocation());
                                recVou.getLedgerList().get(cn).setVoucherDateInMilliSecondReport(recVou.getVoucherDateInMilliSecond());
                                recVou.getLedgerList().get(cn).setPaymentModeReport(recVou.getPaymentMode());
//                                recVou.getLedgerList().get(cn).setLedger(recVou.getLedgerList().get(k3).getLedger());
//                            recVou.getLedgerList().get(k3).setInFavourOfReport(recVou.getInFavorOf());
//                            recVou.getLedgerList().get(k3).setChequeNoReport(recVou.getChequeNo());
//                            recVou.getLedgerList().get(k3).setChequeDtReport(recVou.getChequeDate());
//                            recVou.getLedgerList().get(k3).setChequeAmtReport(recVou.getChequeAmount());
                                recVou.getLedgerList().get(cn).setDrCr(recVou.getLedgerList().get(k3).getDrCr());
                                recVou.getLedgerList().get(cn).setInFavourOfReport(recVou.getChequeList().get(cn).getInFavorOf());
                                recVou.getLedgerList().get(cn).setChequeNoReport(recVou.getChequeList().get(cn).getChequeNo());
                                recVou.getLedgerList().get(cn).setChequeDtReport(recVou.getChequeList().get(cn).getChequeDate());
                                recVou.getLedgerList().get(cn).setChequeAmtReport(recVou.getChequeList().get(cn).getChequeAmount());
                                recVou.getLedgerList().get(cn).setVoucherTyReport(recVou.getVoucherName());
                                recVocFinalList.add(recVou.getLedgerList().get(cn));
                            }
                        }
                    }
                }

            }
        }

        ContraVoucher cvobj = new Gson().fromJson(voucherdata, new TypeToken<ContraVoucher>() {
        }.getType());

        RestClient aql2 = new RestClient();

        String cvTable = ApplicationConstants.USG_DB1 + ApplicationConstants.CONTRA_VOUCHER_TABLE + "`";

        String contraSearchQuery = "";

//        if (fromDate != null && fromDate != "" && !fromDate.isEmpty()) {
//          
//            contraSearchQuery = contraSearchQuery + " cv.voucherDateInMilliSecond>=" + fromVD;
//
//        }
//        if (toDate != null && toDate != "" && !toDate.isEmpty()) {
//
//            contraSearchQuery = contraSearchQuery + " and cv.voucherDateInMilliSecond<=" + toVD;
//
//        }
        String contraVoucherQuery = "select * from " + cvTable + ""
                + " as cv where " + contraSearchQuery + " cv.status=\"Active\"";

        String contraVoucherOutput = aql2.getRestData(ApplicationConstants.END_POINT, contraVoucherQuery);

        List<ContraVoucher> conVocList = null;
        if (contraVoucherOutput != null && !contraVoucherOutput.isEmpty() && !contraVoucherOutput.equals("[]")) {
            conVocList = new Gson().fromJson(contraVoucherOutput, new TypeToken< ArrayList<ContraVoucher>>() {
            }.getType());
        }

        List<LedgerList> conVocFinalList = new ArrayList<LedgerList>();
        if (contraVoucherOutput != null && !contraVoucherOutput.isEmpty() && !contraVoucherOutput.equals("[]")) {
            for (ContraVoucher conVou : conVocList) {
                long vdms = conVou.getVoucherDateInMilliSecond();
                String entryStatusRpt1 = conVou.getEntryStatus();
                for (int k2 = 0; k2 < conVou.getLedgerList().size(); k2++) {
                    if ((conVou.getLedgerList().get(k2).getLedger().equals(ledger)) && (conVou.getLedgerList().get(k2).getDrCr().equals("Dr")) && (vdms >= finStart) && (vdms <= fromVD)) {
                        drtext = conVou.getLedgerList().get(k2).getDrAmount();
                        drvalue = roundTwoDecimals(Double.parseDouble(drtext));
                        totalDrAmount = roundTwoDecimals(totalDrAmount + drvalue);

                    }
                    if ((conVou.getLedgerList().get(k2).getLedger().equals(ledger)) && (conVou.getLedgerList().get(k2).getDrCr().equals("Cr")) && (vdms >= finStart) && (vdms <= fromVD)) {
                        crtext = conVou.getLedgerList().get(k2).getCrAmount();
                        crvalue = roundTwoDecimals(Double.parseDouble(crtext));
                        totalCrAmount = roundTwoDecimals(totalCrAmount + crvalue);

                    }

                    if ((entryStatusRpt1 != null)) {
                        if ((conVou.getLedgerList().get(k2).getLedger().equals(ledger)) && (conVou.getPaymentMode().equals("Cheque")) && (conVou.getEntryStatus().equals("Uncleared Entries")) && (vdms >= finStart) && (vdms <= fromVD) && ((conVou.getPostingStatus().equals("Posted")))) {
                            for (int cn = 0; cn < conVou.getChequeList().size(); cn++) {
                                conVou.getLedgerList().get(cn).setVoucherDtReport(conVou.getVoucherDate());
                                conVou.getLedgerList().get(cn).setVoucherNoReport(conVou.getVoucherNo());
                                conVou.getLedgerList().get(cn).setLocationReport(conVou.getLocation());
                                conVou.getLedgerList().get(cn).setVoucherDateInMilliSecondReport(conVou.getVoucherDateInMilliSecond());
                                conVou.getLedgerList().get(cn).setPaymentModeReport(conVou.getPaymentMode());
//                            conVou.getLedgerList().get(cn).setLedger(conVou.getLedgerList().get(k2).getLedger());
//                            conVou.getLedgerList().get(k2).setInFavourOfReport(conVou.getInFavorOf());
//                            conVou.getLedgerList().get(k2).setChequeNoReport(conVou.getChequeNo());
//                            conVou.getLedgerList().get(k2).setChequeDtReport(conVou.getChequeDate());
//                            conVou.getLedgerList().get(k2).setChequeAmtReport(conVou.getChequeAmount());
                                conVou.getLedgerList().get(cn).setDrCr(conVou.getLedgerList().get(k2).getDrCr());
                                conVou.getLedgerList().get(cn).setInFavourOfReport(conVou.getChequeList().get(cn).getInFavorOf());
                                conVou.getLedgerList().get(cn).setChequeNoReport(conVou.getChequeList().get(cn).getChequeNo());
                                conVou.getLedgerList().get(cn).setChequeDtReport(conVou.getChequeList().get(cn).getChequeDate());
                                conVou.getLedgerList().get(cn).setChequeAmtReport(conVou.getChequeList().get(cn).getChequeAmount());
                                conVou.getLedgerList().get(cn).setVoucherTyReport(conVou.getVoucherName());
                                conVocFinalList.add(conVou.getLedgerList().get(cn));
                            }

                        }
                    }

                }

            }
        }
//        closingBalance = roundTwoDecimals(openingBalance + totalCrAmount - totalDrAmount);

        List<LedgerList> merged = new ArrayList(payVocFinalList);
        merged.addAll(conVocFinalList);
        merged.addAll(recVocFinalList);
        List<LedgerList> lL = new ArrayList<LedgerList>();

        if (merged != null) {
            Collections.sort(merged, new VoucherDatesSortBy());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, bos);

        Font font1 = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
        Font font3 = new Font(Font.FontFamily.HELVETICA, 9);

        float[] tencolumnwidth = {1.2f, 2.2f, 2.2f, 2f, 2f, 2f, 1.7f, 2.2f, 3.9f, 3.9f};
        float[] columnWidth = {1f, 1f, 1f};
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
//        headerPhrase.add(new Phrase(new Chunk("Bank Book", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
        headercell.setPaddingBottom(6f);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);
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

        Phrase timePhrase = new Phrase(new Chunk("Date :" + dateFormat.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time:" + dateFormatTime.format(date), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("FY : ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        timePhrase.add(new Phrase(new Chunk(fin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));

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
        PdfPCell cell = new PdfPCell(new Phrase("BANK RECONCILIATION REPORT ", font1));
        cell.setColspan(4);
        cell.setRowspan(5);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(8f);
        cell.setPaddingLeft(85f);
        cell.setBorder(Rectangle.NO_BORDER);

        headertable.addCell(cell);
        document.add(headertable);

        PdfPTable table0 = new PdfPTable(1);
        table0.setWidthPercentage(100);
        table0.setWidths(onecolumnwidth);

        PdfPCell table0cella = null;
        String drCrval = drCrType;
//        String opBal = roundTwoDecimalPoints(openingBalance);
//        String cloBal = roundTwoDecimalPoints(closingBalance);
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
        if (drCrval != null) {
            /*if (drCrval.equals("Dr")) {
                closingBalance = roundTwoDecimals(openingBalance - totalCrAmount + totalDrAmount);
            } else if (drCrval.equals("Cr")) {
                closingBalance = roundTwoDecimals(openingBalance + totalCrAmount - totalDrAmount);
            } else {
                closingBalance = 0.00;
            }*/

            TreeMap<String, Object> trialBalanceResult = new TrialBalanceManager().getTrialBalanceReocrds1(
                    ddo, location, "",
                    "", "", fromDateStr, fromDate);
            Set<String> sets = trialBalanceResult.keySet();
            for (String key : sets) {
                if (key.equals("result")) {
                    ArrayList<TrialBalance> resultList = (ArrayList) trialBalanceResult
                            .get("result");
                    String ledgerNameStr;
                    String closingCrAmount;
                    String closingDrAmount;
                    for (TrialBalance groupEntries : resultList) {
                        ledgerNameStr = groupEntries.getLedgerName();
//                        int lb_ind = ledgerNameStr.indexOf("[\\(\\)]");
//                        if (lb_ind != -1) {
                        ledgerNameStr = groupEntries.getLedgerName();
                        String lederval = "no";
                        //String[] ledgerarr = ledgerName.split("(");
                        String tempBankName = null;
                        if (ledgerName.contains("(")) {
                            tempBankName = ledgerName.substring(0, ledgerName.indexOf("("));
                        }

                        //lederval = ledgerarr[0];
                        if (ledgerNameStr.equalsIgnoreCase(tempBankName)) {
                            transactionCrAmount = Double.parseDouble(groupEntries.getTransactionCrAmount());
                            transactionDrAmount = Double.parseDouble(groupEntries.getTransactionDrAmount());
                            if (drCrval.equalsIgnoreCase("Cr")) {
                                closingCrAmount = groupEntries.getClosingBalanceCrAmount();
//                                openingBalance = Double.parseDouble(groupEntries.getOpeningBalanceCrAmount());
                                closingBalance = openingBalance + transactionCrAmount - transactionDrAmount;
                            } else if (drCrval.equalsIgnoreCase("Dr")) {
                                closingDrAmount = groupEntries.getClosingBalanceDrAmount();
//                                openingBalance = Double.parseDouble(groupEntries.getOpeningBalanceCrAmount());
                                closingBalance = openingBalance + transactionDrAmount - transactionCrAmount;
                            }
                        }
                        else{
                            closingBalance = openingBalance + transactionDrAmount - transactionCrAmount;
                        }
                        if (resultList.size() == 0) {
                            closingBalance = openingBalance;
                        }

//                        }
                    }
                    if (resultList.size() == 0) {
                        closingBalance = openingBalance;
                    }
                }
//                else
//                {
//                    closingBalance = openingBalance;
//                }

            }

        } else {
            TreeMap<String, Object> trialBalanceResult = new TrialBalanceManager().getTrialBalanceReocrds1(
                    ddo, location, "",
                    "", "", fromDateStr, fromDate);
            Set<String> sets = trialBalanceResult.keySet();
            for (String key : sets) {
                if (key.equals("result")) {
                    ArrayList<TrialBalance> resultList = (ArrayList) trialBalanceResult
                            .get("result");
                    String ledgerNameStr;

                    for (TrialBalance groupEntries : resultList) {
                        ledgerNameStr = groupEntries.getLedgerName();
                        if (ledgerNameStr.equalsIgnoreCase(ledgerName)) {
                            transactionCrAmount = Double.parseDouble(groupEntries.getTransactionCrAmount());
                            transactionDrAmount = Double.parseDouble(groupEntries.getTransactionDrAmount());
//                            closingBalance = openingBalance + transactionCrAmount - transactionDrAmount;
                            closingBalance = openingBalance;
                        }
                    }

                }

            }
            closingBalance = openingBalance;
        }
        TrialBalanceManager tbm = new TrialBalanceManager();
        String opBal = roundTwoDecimalPoints(closingBalance);
//<<<<<<<<<<<<<<<<<<<<<<<<<<,,
        if (drCrval != null) {
//            if (drCrval.equals("Dr")) {
////                table0cella = new PdfPCell(new Paragraph("Closing Balance :  " + opBal + " " + drCrval, font2));
//                table0cella = new PdfPCell(new Paragraph("Closing Balance :  " + opBal , font2));
//            } else if (drCrval.equals("Cr")) {
////                String removeMinus = opBal.replace("-", " ");
////                table0cella = new PdfPCell(new Paragraph("Closing Balance :  " + removeMinus + " " + drCrval, font2));
////                  table0cella = new PdfPCell(new Paragraph("Closing Balance :  " + removeMinus , font2));
//                  table0cella = new PdfPCell(new Paragraph("Closing Balance :  " + opBal , font2));
//            } else {
//                table0cella = new PdfPCell(new Paragraph("Closing Balance :  ", font2));
//            }
            if (closingBalance >= 0.00) {
                table0cella = new PdfPCell(new Paragraph("Closing Balance :  " + opBal, font2));
            } else if (closingBalance < 0.00) {
                String removeMinus = opBal.replace("-", " ");
                table0cella = new PdfPCell(new Paragraph("Closing Balance :  " + removeMinus, font2));
            } else {
                table0cella = new PdfPCell(new Paragraph("Closing Balance :  ", font2));
            }
        } else {
            table0cella = new PdfPCell(new Paragraph("Closing Balance :  " + opBal + " ", font2));
        }
        table0cella.setBorderColor(BaseColor.BLACK);
        table0cella.setBackgroundColor(BaseColor.WHITE);
        table0cella.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table0cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table0cella.setBorder(Rectangle.NO_BORDER);
        table0.addCell(table0cella);
        document.add(table0);

        if (merged == null || merged.size() > 0) {
            PdfPTable table1 = new PdfPTable(10); //
            table1.setWidthPercentage(100); //Width 100%
            table1.setWidths(tencolumnwidth);

            PdfPCell table1cella = new PdfPCell(new Paragraph("S.No", font2));
            table1cella.setBorderColor(BaseColor.BLACK);
            table1cella.setBackgroundColor(BaseColor.WHITE);
            table1cella.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1cella.setBorder(Rectangle.NO_BORDER);
            table1cella.setBorderWidthTop(1f);
            table1cella.setBorderWidthBottom(1f);

            PdfPCell table1cellb = new PdfPCell(new Paragraph("Date", font2));
            table1cellb.setBorderColor(BaseColor.BLACK);
            table1cellb.setBackgroundColor(BaseColor.WHITE);
            table1cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1cellb.setBorder(Rectangle.NO_BORDER);
            table1cellb.setBorderWidthTop(1f);
            table1cellb.setBorderWidthBottom(1f);

            PdfPCell table1cellc = new PdfPCell(new Paragraph("Bill NO.", font2));
            table1cellc.setBorderColor(BaseColor.BLACK);
            table1cellc.setBackgroundColor(BaseColor.WHITE);
            table1cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1cellc.setBorder(Rectangle.NO_BORDER);
            table1cellc.setBorderWidthTop(1f);
            table1cellc.setBorderWidthBottom(1f);

            PdfPCell table1celld = new PdfPCell(new Paragraph("In Favour Of", font2));
            table1celld.setBorderColor(BaseColor.BLACK);
            table1celld.setBackgroundColor(BaseColor.WHITE);
            table1celld.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1celld.setBorder(Rectangle.NO_BORDER);
            table1celld.setBorderWidthTop(1f);
            table1celld.setBorderWidthBottom(1f);

            PdfPCell table1celle = new PdfPCell(new Paragraph("Location", font2));
            table1celle.setBorderColor(BaseColor.BLACK);
            table1celle.setBackgroundColor(BaseColor.WHITE);
            table1celle.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1celle.setBorder(Rectangle.NO_BORDER);
            table1celle.setBorderWidthTop(1f);
            table1celle.setBorderWidthBottom(1f);

            PdfPCell table1cellf = new PdfPCell(new Paragraph("Ledger", font2));
            table1cellf.setBorderColor(BaseColor.BLACK);
            table1cellf.setBackgroundColor(BaseColor.WHITE);
            table1cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1cellf.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1cellf.setBorder(Rectangle.NO_BORDER);
            table1cellf.setBorderWidthTop(1f);
            table1cellf.setBorderWidthBottom(1f);

            PdfPCell table1cellg = new PdfPCell(new Paragraph("Cheque NO.", font2));
            table1cellg.setBorderColor(BaseColor.BLACK);
            table1cellg.setBackgroundColor(BaseColor.WHITE);
            table1cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1cellg.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1cellg.setBorder(Rectangle.NO_BORDER);
            table1cellg.setBorderWidthTop(1f);
            table1cellg.setBorderWidthBottom(1f);

            PdfPCell table1cellh = new PdfPCell(new Paragraph("Cheque Date", font2));
            table1cellh.setBorderColor(BaseColor.BLACK);
            table1cellh.setBackgroundColor(BaseColor.WHITE);
            table1cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1cellh.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1cellh.setBorder(Rectangle.NO_BORDER);
            table1cellh.setBorderWidthTop(1f);
            table1cellh.setBorderWidthBottom(1f);

            PdfPCell table1celli = new PdfPCell(new Paragraph("Cheque Amount", font2));
            table1celli.setBorderColor(BaseColor.BLACK);
            table1celli.setBackgroundColor(BaseColor.WHITE);
            // celle.setPaddingLeft(15);
            table1celli.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1celli.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1celli.setBorder(Rectangle.NO_BORDER);
            table1celli.setBorderWidthTop(1f);
            table1celli.setBorderWidthBottom(1f);

            PdfPCell table1cellj = new PdfPCell(new Paragraph("Closing Balance", font2));
            table1cellj.setBorderColor(BaseColor.BLACK);
            table1cellj.setBackgroundColor(BaseColor.WHITE);
            // celle.setPaddingLeft(15);
            table1cellj.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1cellj.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table1cellj.setBorder(Rectangle.NO_BORDER);
            table1cellj.setBorderWidthTop(1f);
            table1cellj.setBorderWidthBottom(1f);

            table1.addCell(table1cella);
            table1.addCell(table1cellb);
            table1.addCell(table1cellc);
            table1.addCell(table1celld);
            table1.addCell(table1celle);
            table1.addCell(table1cellf);
            table1.addCell(table1cellg);
            table1.addCell(table1cellh);
            table1.addCell(table1celli);
            table1.addCell(table1cellj);
            document.add(table1);

            int sNo = 1;

            PdfPTable table2 = new PdfPTable(10);
            table2.setWidthPercentage(100); //Width 100%
            table2.setWidths(tencolumnwidth);

            for (int i = 0; i < merged.size(); i++) {
                table2.flushContent();
                table2.setWidthPercentage(100); //Width 100%

                PdfPCell table2cell1 = new PdfPCell(new Paragraph(Integer.toString(sNo), font3));
                table2cell1.setBorderColor(BaseColor.BLACK);
                table2cell1.setBackgroundColor(BaseColor.WHITE);
                table2cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table2cell1.setPaddingTop(15f);
//                table2cell1.setBorder(Rectangle.NO_BORDER);
                table2cell1.setPaddingLeft(2f);

                PdfPCell table2cell2 = null;

                table2cell2 = new PdfPCell(new Paragraph(merged.get(i).getVoucherDtReport(), font3));
                table2cell2.setBorderColor(BaseColor.BLACK);
                table2cell2.setBackgroundColor(BaseColor.WHITE);
                table2cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table2cell2.setPaddingTop(15f);
//                table2cell2.setBorder(Rectangle.NO_BORDER);
                table2cell2.setPaddingTop(15f);

                PdfPCell table2cell3 = new PdfPCell(new Paragraph(merged.get(i).getVoucherNoReport(), font3));
                table2cell3.setBorderColor(BaseColor.BLACK);
                table2cell3.setBackgroundColor(BaseColor.WHITE);
                table2cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                table2cell3.setBorder(Rectangle.NO_BORDER);
                table2cell3.setPaddingTop(15f);
                table2cell3.setPaddingLeft(2f);

                PdfPCell table2cell4 = new PdfPCell(new Paragraph(merged.get(i).getInFavourOfReport(), font3));
                table2cell4.setBorderColor(BaseColor.BLACK);
                table2cell4.setBackgroundColor(BaseColor.WHITE);
                table2cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                table2cell4.setBorder(Rectangle.NO_BORDER);
                table2cell4.setPaddingTop(15f);
                table2cell4.setPaddingLeft(2f);

                PdfPCell table2cell5 = null;
                if (merged.get(i).getLocationReport() != null) {
                    String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, merged.get(i).getLocationReport());

                    List<Location> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Location>>() {
                    }.getType());
                    Location gal = gaList.get(0);
                    merged.get(i).setLocationReport(gal.getLocationName());
                    table2cell5 = new PdfPCell(new Paragraph(gal.getLocationName(), font3));
//            table2cell5 = new PdfPCell(new Paragraph(merged.get(i).getLocationReport(), font3));
                    table2cell5.setBorderColor(BaseColor.BLACK);
                    table2cell5.setBackgroundColor(BaseColor.WHITE);
                    table2cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table2cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    table2cell5.setBorder(Rectangle.NO_BORDER);
                    table2cell5.setPaddingTop(15f);
                    table2cell5.setPaddingLeft(2f);
                }

                PdfPCell table2cell6 = null;
                if (merged.get(i).getLedger() != null) {
//                    String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_TABLE, merged.get(i).getLedger());
                    String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_TABLE, ledger);

                    List<Ledger> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Ledger>>() {
                    }.getType());
                    Ledger gal = gaList.get(0);
                    String lName = new CashBookReportManager().getLedgerCodeAndLedgerName(((LinkedTreeMap<String, String>) gal.getId()).get("$oid"), gal.getLedgerName());

                    merged.get(i).setLocationReport(lName);
                    table2cell6 = new PdfPCell(new Paragraph(lName, font3));
                    table2cell6.setBorderColor(BaseColor.BLACK);
                    table2cell6.setBackgroundColor(BaseColor.WHITE);
                    table2cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table2cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    table2cell6.setBorder(Rectangle.NO_BORDER);
                    table2cell6.setPaddingTop(15f);
                    table2cell6.setPaddingLeft(2f);
                }
                PdfPCell table2cell7 = new PdfPCell(new Paragraph(merged.get(i).getChequeNoReport(), font3));
                table2cell7.setBorderColor(BaseColor.BLACK);
                table2cell7.setBackgroundColor(BaseColor.WHITE);
                table2cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                table2cell7.setBorder(Rectangle.NO_BORDER);
                table2cell7.setPaddingTop(15f);
                table2cell7.setPaddingLeft(2f);

                PdfPCell table2cell8 = new PdfPCell(new Paragraph(merged.get(i).getChequeDtReport(), font3));
                table2cell8.setBorderColor(BaseColor.BLACK);
                table2cell8.setBackgroundColor(BaseColor.WHITE);
                table2cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                table2cell8.setBorder(Rectangle.NO_BORDER);
                table2cell8.setPaddingTop(15f);
                table2cell8.setPaddingLeft(2f);

                PdfPCell table2cell9 = null;
                String text = merged.get(i).getChequeAmtReport();
                double value = Double.parseDouble(text);
                String chequeAmountDouble = roundTwoDecimalPoints(value);
                String drCrText = merged.get(i).getDrCr();
//                if(i % 2 != 0)
//                {
//                    drCrText = merged.get(i-1).getDrCr();
//                    table2cell9 = new PdfPCell(new Paragraph(chequeAmountDouble + " " + drCrText, font3));
//                }
                if (i > 0) {
                    String drcrs = merged.get(i).getVoucherNoReport();
                    String drcrs1 = merged.get(i - 1).getVoucherNoReport();
                    if (drcrs.equalsIgnoreCase(drcrs1)) {
                        drCrText = merged.get(i - 1).getDrCr();
                        table2cell9 = new PdfPCell(new Paragraph(chequeAmountDouble + " " + drCrText, font3));
                    } else {
                        table2cell9 = new PdfPCell(new Paragraph(chequeAmountDouble + " " + drCrText, font3));
                    }
                } else {
                    table2cell9 = new PdfPCell(new Paragraph(chequeAmountDouble + " " + drCrText, font3));
                }
                table2cell9.setBorderColor(BaseColor.BLACK);
                table2cell9.setBackgroundColor(BaseColor.WHITE);
                table2cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                table2cell9.setBorder(Rectangle.NO_BORDER);
                table2cell9.setPaddingTop(15f);
                table2cell9.setPaddingLeft(2f);
                String drCrValue = merged.get(i).getDrCr();
                String text1 = roundTwoDecimalPoints(value);
                totalChequeAmount = roundTwoDecimals(totalChequeAmount) + value;

//            if(drCrValue.equalsIgnoreCase("Cr")){
//            cbl = roundTwoDecimals(openingBalance + value);
//            }
//            if(drCrValue.equalsIgnoreCase("Dr")){
//            cbl = roundTwoDecimals(openingBalance - value);
//            }
//            openingBalance = roundTwoDecimals(cbl);
//            cbl1 = roundTwoDecimalPoints(cbl);
                if (drCrText.equalsIgnoreCase("Cr")) {
                    cbl = roundTwoDecimals(closingBalance + value);
                }
                if (drCrText.equalsIgnoreCase("Dr")) {
                    cbl = roundTwoDecimals(closingBalance - value);
                }
                closingBalance = roundTwoDecimals(cbl);
                cbl1 = roundTwoDecimalPoints(cbl);

                PdfPCell table2cell10 = null;
//            if(openingBalance >= 0.00){
//            table2cell10 = new PdfPCell(new Paragraph(cbl1 + " Cr", font3));
//            }
//            
//            if(openingBalance < 0.00){
//                String str = cbl1;
//                String str1 = str.replace("-", " ");
//            table2cell10 = new PdfPCell(new Paragraph(str1 + " Dr", font3));
//            }
                table2cell10 = new PdfPCell(new Paragraph(cbl1, font3));
                table2cell10.setBorderColor(BaseColor.BLACK);
                table2cell10.setBackgroundColor(BaseColor.WHITE);
                table2cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                table2cell10.setBorder(Rectangle.NO_BORDER);
                table2cell10.setPaddingTop(15f);
                table2cell10.setPaddingLeft(2f);

                table2.addCell(table2cell1);
                table2.addCell(table2cell2);
                table2.addCell(table2cell3);
                table2.addCell(table2cell4);
                table2.addCell(table2cell5);
                table2.addCell(table2cell6);
                table2.addCell(table2cell7);
                table2.addCell(table2cell8);
                table2.addCell(table2cell9);
                table2.addCell(table2cell10);

                sNo++;
                document.add(table2);
            }

            PdfPTable table10 = new PdfPTable(10); //
            table10.setWidthPercentage(100); //Width 100%
            table10.setWidths(tencolumnwidth);

            PdfPCell table10cella = new PdfPCell(new Paragraph("", font2));
            table10cella.setBorderColor(BaseColor.BLACK);
            table10cella.setBackgroundColor(BaseColor.WHITE);
            table10cella.setHorizontalAlignment(Element.ALIGN_LEFT);
            table10cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10cella.setBorder(Rectangle.LEFT);
            table10cella.setBorderWidthTop(1f);

            PdfPCell table10cellb = new PdfPCell(new Paragraph("", font2));
            table10cellb.setBorderColor(BaseColor.BLACK);
            table10cellb.setBackgroundColor(BaseColor.WHITE);
            table10cellb.setHorizontalAlignment(Element.ALIGN_LEFT);
            table10cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10cellb.setBorder(Rectangle.NO_BORDER);
            table10cellb.setBorderWidthTop(1f);

            PdfPCell table10cellc = new PdfPCell(new Paragraph("", font2));
            table10cellc.setBorderColor(BaseColor.BLACK);
            table10cellc.setBackgroundColor(BaseColor.WHITE);
            table10cellc.setHorizontalAlignment(Element.ALIGN_LEFT);
            table10cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10cellc.setBorder(Rectangle.NO_BORDER);
            table10cellc.setBorderWidthTop(1f);

            PdfPCell table10celld = new PdfPCell(new Paragraph("", font2));
            table10celld.setBorderColor(BaseColor.BLACK);
            table10celld.setBackgroundColor(BaseColor.WHITE);
            table10celld.setHorizontalAlignment(Element.ALIGN_LEFT);
            table10celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10celld.setBorder(Rectangle.NO_BORDER);
            table10celld.setBorderWidthTop(1f);

            PdfPCell table10celle = new PdfPCell(new Paragraph("", font2));
            table10celle.setBorderColor(BaseColor.BLACK);
            table10celle.setBackgroundColor(BaseColor.WHITE);
            table10celle.setHorizontalAlignment(Element.ALIGN_LEFT);
            table10celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10celle.setBorder(Rectangle.NO_BORDER);
            table10celle.setBorderWidthTop(1f);

            PdfPCell table10cellf = new PdfPCell(new Paragraph("", font2));
            table10cellf.setBorderColor(BaseColor.BLACK);
            table10cellf.setBackgroundColor(BaseColor.WHITE);
            table10cellf.setHorizontalAlignment(Element.ALIGN_LEFT);
            table10cellf.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10cellf.setBorder(Rectangle.NO_BORDER);
            table10cellf.setBorderWidthTop(1f);

            PdfPCell table10cellg = new PdfPCell(new Paragraph("", font2));
            table10cellg.setBorderColor(BaseColor.BLACK);
            table10cellg.setBackgroundColor(BaseColor.WHITE);
            table10cellg.setHorizontalAlignment(Element.ALIGN_LEFT);
            table10cellg.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10cellg.setBorder(Rectangle.NO_BORDER);
            table10cellg.setBorderWidthTop(1f);

            PdfPCell table10cellh = new PdfPCell(new Paragraph("Total :", font2));
            table10cellh.setBorderColor(BaseColor.BLACK);
            table10cellh.setBackgroundColor(BaseColor.WHITE);
            table10cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
            table10cellh.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10cellh.setBorder(Rectangle.NO_BORDER);
            table10cellh.setBorderWidthTop(1f);

            String totalChequeAmount1 = roundTwoDecimalPoints(totalChequeAmount);
            PdfPCell table10celli = new PdfPCell(new Paragraph(totalChequeAmount1, font3));
            table10celli.setBorderColor(BaseColor.BLACK);
            table10celli.setBackgroundColor(BaseColor.WHITE);
            // celle.setPaddingLeft(15);
            table10celli.setHorizontalAlignment(Element.ALIGN_CENTER);
            table10celli.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10celli.setBorder(Rectangle.NO_BORDER);
            table10celli.setBorderWidthTop(1f);

            PdfPCell table10cellj = new PdfPCell(new Paragraph("", font2));
            table10cellj.setBorderColor(BaseColor.BLACK);
            table10cellj.setBackgroundColor(BaseColor.WHITE);
            // celle.setPaddingLeft(15);
            table10cellj.setHorizontalAlignment(Element.ALIGN_LEFT);
            table10cellj.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table10cellj.setBorder(Rectangle.RIGHT);
            table10cellj.setBorderWidthTop(1f);

            table10.addCell(table10cella);
            table10.addCell(table10cellb);
            table10.addCell(table10cellc);
            table10.addCell(table10celld);
            table10.addCell(table10celle);
            table10.addCell(table10cellf);
            table10.addCell(table10cellg);
            table10.addCell(table10cellh);
            table10.addCell(table10celli);
            table10.addCell(table10cellj);
            document.add(table10);

            PdfPTable table11 = new PdfPTable(1);
            table11.setWidthPercentage(100);
            table11.setWidths(onecolumnwidth);

            PdfPCell table11cella = null;
            table11cella = new PdfPCell(new Paragraph("Closing Balance : " + cbl1, font3));
            table11cella.setBorderColor(BaseColor.BLACK);
            table11cella.setBackgroundColor(BaseColor.WHITE);
            table11cella.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table11cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table8cella.setBorder(Rectangle.RIGHT);
            table11cella.setBorderWidthLeft(1f);
            table11.addCell(table11cella);
            document.add(table11);
        } else {
            LineSeparator line14 = new LineSeparator();
            line14.setOffset(-1);
            document.add(line14);

            PdfPTable table9 = new PdfPTable(1);
            table9.setWidthPercentage(100);
            table9.setWidths(onecolumnwidth);

            PdfPCell table9cella = new PdfPCell(new Paragraph("No Records Exist ", font2));
            table9cella.setBorderColor(BaseColor.BLACK);
            table9cella.setBackgroundColor(BaseColor.WHITE);
            table9cella.setHorizontalAlignment(Element.ALIGN_CENTER);
            table9cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table9cella.setBorder(Rectangle.NO_BORDER);
            table9cella.setBorderWidthTop(1f);
            table9cella.setBorderWidthBottom(1f);

            table9.addCell(table9cella);
            document.add(table9);
        }

        document.close();
        return bos;
    }

    public long saveInMilliSecondDate(String str) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateInString = str;
            Date date = sdf.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            return 0;
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

    public static double roundTwoDecimals(double amount) {
        if (amount == 0 || amount == -1) {
            return amount;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        double output = Double.parseDouble(df.format(amount) + "");
        return output;
    }

    public static String roundTwoDecimalPoints(double amount) {
        //System.out.println(amount);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        String output = nf.format(amount).replaceAll(",", "");
        return output;
    }

}
