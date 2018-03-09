/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.budget.dto.CreateIncomeBudget;
import com.accure.budget.dto.ExpenseBudget;
import com.accure.finance.dto.ContraVoucher;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Ledger;
import com.accure.finance.dto.LedgerCategory;
import com.accure.finance.dto.LedgerList;
import com.accure.finance.dto.Location;
import com.accure.finance.dto.ManageOpeningBalance;
import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.dto.ReceiptVoucher;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author accure
 */
public class BankBookReportManager {

    public ByteArrayOutputStream bankBookPdfStatement(String date, String voucherdata, String ledger, String path, String fin) throws DocumentException, FileNotFoundException, Exception {
        double openingBalance = 0.00;
        double closingBalance = 0.0;
        Long dateVD = 0L;
        dateVD = saveInMilliSecondDate(date);
        double totalDrAmount = 0.0;
        double totalCrAmount = 0.0;
        double cbl = 0.0;
        String cbl1 = "";
        String financialYearId = "";

        HashMap<String, String> conditionMap1 = new HashMap<String, String>();
        conditionMap1.put("ledgerId", ledger);
        conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String ledgerType = null;
        String drCrType = null;
        String ledgerJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, conditionMap1);
        if (ledgerJson == null || ledgerJson.isEmpty()) {

//            openingBalance = 0.00;
            String finArr[] = fin.split("-");
            String finYear = finArr[0];
             HashMap<String, String> finConditionMap = new HashMap<String, String>();
        finConditionMap.put("year", finYear);
        finConditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String finIdJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, finConditionMap);
        if (finIdJson != null && !finIdJson.isEmpty()) {
            List<FinancialYear> finIdList = new Gson().fromJson(finIdJson, new TypeToken<List<FinancialYear>>() {
            }.getType());
            FinancialYear finIdListObj = finIdList.get(0);
            financialYearId = ((LinkedTreeMap<String, String>) finIdListObj.getId()).get("$oid");
        }
            Ledger led = new ManageOpeningBalanceManager().checkLedgerIsBudgetTypeOrNot(ledger);
        
        String budType = led.getBudgetType();
        if(budType.equalsIgnoreCase("Yes")){
        List<LedgerCategory> lc = new ManageOpeningBalanceManager().checkLedgerIsIncomeOrBudgetCategory(ledger);
        if(lc != null){
            
            String ledgerCategory = lc.get(0).getLedgerCategory();
            
            if(ledgerCategory.equalsIgnoreCase("Income")){
              List<CreateIncomeBudget>  cib = new ManageOpeningBalanceManager().getSanctionedAndExtraProvisionAmount(ledger,financialYearId);
              if(cib != null){
                  for(int ib = 0; ib <cib.size(); ib++){
                  if(cib.get(ib).getIsSanctioned().equalsIgnoreCase("true")){
                      openingBalance = openingBalance + (Double.parseDouble(cib.get(ib).getSanctionedAmount()))*100000;
                  }
                  if(cib.get(ib).getIsExtraProvisioned().equalsIgnoreCase("true")){
                      openingBalance = openingBalance + (Double.parseDouble(cib.get(ib).getExtraProvisionAmount()))*100000;
                  }
                  }
                  
              }else{
            openingBalance = 0.00;
        }
            openingBalance = - openingBalance;
            }
            else if(ledgerCategory.equalsIgnoreCase("Expense")){
             
                List<ExpenseBudget>  ceb = new ManageOpeningBalanceManager().getSanctionedAndExtraProvisionAmountExpense(ledger,financialYearId);
              if(ceb != null){
                  for(int eb = 0; eb <ceb.size(); eb++){
                  if(ceb.get(eb).getIsSanctioned().equalsIgnoreCase("true")){
                      openingBalance = openingBalance + (Double.parseDouble(ceb.get(eb).getSanctionedAmount()))*100000;
                  }
                  if(ceb.get(eb).getIsExtraProvisioned().equalsIgnoreCase("true")){
                      openingBalance = openingBalance + (Double.parseDouble(ceb.get(eb).getExtraProvisionAmount()))*100000;
                  }

                  }
              }else{
            openingBalance = 0.00;
        }
            openingBalance = + openingBalance;
            }else{
                openingBalance = 0.00;
            }
        }else{
            openingBalance = 0.00;
        }

        }else{
            openingBalance = 0.00;
        }
        } else {
            List<ManageOpeningBalance> openingBalanceList = new Gson().fromJson(ledgerJson, new TypeToken<List<ManageOpeningBalance>>() {
            }.getType());
            ManageOpeningBalance openingBalanceObj = openingBalanceList.get(0);
            ledgerType = openingBalanceObj.getLedgerId();
            drCrType = openingBalanceObj.getAmountType();

            if (ledgerType.equalsIgnoreCase(ledger));
            String obal = openingBalanceObj.getAmount();
            openingBalance = roundTwoDecimals(Double.parseDouble(obal));

            if (openingBalanceObj.getAmountType().equalsIgnoreCase("Dr")) {
                openingBalance = +openingBalance;
            }
            if (openingBalanceObj.getAmountType().equalsIgnoreCase("Cr")) {
                openingBalance = -openingBalance;
            }
        }
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        ReceiptVoucher rvobj = new Gson().fromJson(voucherdata, new TypeToken<ReceiptVoucher>() {
        }.getType());

        LedgerList lLobj = new Gson().fromJson(voucherdata, new TypeToken<LedgerList>() {
        }.getType());

        RestClient aql = new RestClient();
        String Active = "\"Active\"";

        String rvTable = ApplicationConstants.USG_DB1 + ApplicationConstants.RECEIPT_VOUCHER_TABLE + "`";

        String receiptSearchQuery = "";

        if (rvobj.getDDO() != null && !rvobj.getDDO().isEmpty() && !rvobj.getDDO().equals("0")) {
            receiptSearchQuery = receiptSearchQuery + " rv.DDO=\"" + rvobj.getDDO() + "\"";
        }
        if (rvobj.getLocation() != null && !rvobj.getLocation().isEmpty() && !rvobj.getLocation().equals("0")) {
            receiptSearchQuery = receiptSearchQuery + " and rv.location=\"" + rvobj.getLocation() + "\"";
        }

        if (rvobj.getFundType() != null && !rvobj.getFundType().isEmpty() && !rvobj.getFundType().equals("0")) {
            receiptSearchQuery = receiptSearchQuery + " and rv.fundType=\"" + rvobj.getFundType() + "\"";
        }

        if (rvobj.getBudgetHead() != null && !rvobj.getBudgetHead().isEmpty() && !rvobj.getBudgetHead().equals("0")) {
            receiptSearchQuery = receiptSearchQuery + " and rv.budgetHead=\"" + rvobj.getBudgetHead() + "\"";
        }

        if (date != null && date != "" && !date.isEmpty()) {

            receiptSearchQuery = receiptSearchQuery + " and rv.voucherDateInMilliSecond=" + dateVD;

        }

        String receiptVoucherQuery = "select * from " + rvTable + ""
                + " as rv where " + receiptSearchQuery + " and rv.status=\"Active\"";

        String receiptVoucherOutput = aql.getRestData(ApplicationConstants.END_POINT, receiptVoucherQuery);

        List<ReceiptVoucher> recVocList = null;
        if (receiptVoucherOutput != null && !receiptVoucherOutput.isEmpty() && !receiptVoucherOutput.equals("[]")) {
            recVocList = new Gson().fromJson(receiptVoucherOutput, new TypeToken< ArrayList<ReceiptVoucher>>() {
            }.getType());
        }

        List<LedgerList> recVocFinalList = new ArrayList<LedgerList>();
        HashMap<String, String> test = new HashMap<String, String>();
        if (receiptVoucherOutput != null && !receiptVoucherOutput.isEmpty() && !receiptVoucherOutput.equals("[]")) {
            int k = 0;
            int l = 0;
            for (ReceiptVoucher recVou : recVocList) {
                for (k = 0; k < recVou.getLedgerList().size(); k++) {
                    if ((recVou.getLedgerList().get(k).getLedger()) != null) {
                        if (((recVou.getLedgerList().get(k).getLedger()).equals(ledger)) && ((recVou.getPostingStatus().equals("Posted")))) {

                            if (((recVou.getLedgerList().get(k).getDrCr()).equals("Dr"))) {
                                for (l = 0; l < recVou.getLedgerList().size(); l++) {
                                    if (((recVou.getLedgerList().get(l).getDrCr()).equals("Cr"))) {
                                        recVou.getLedgerList().get(l).setVoucherDtReport(recVou.getVoucherDate());
                                        recVou.getLedgerList().get(l).setVoucherNoReport(recVou.getVoucherNo());
                                        recVou.getLedgerList().get(l).setVoucherTyReport(recVou.getVoucherName());
                                        recVou.getLedgerList().get(l).setVoucherDateInMilliSecondReport(recVou.getVoucherDateInMilliSecond());
                                        recVocFinalList.add(recVou.getLedgerList().get(l));

                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        PaymentVoucher pvobj = new Gson().fromJson(voucherdata, new TypeToken<PaymentVoucher>() {
        }.getType());

        RestClient aql1 = new RestClient();
        String Active1 = "\"Active\"";

        String pvTable = ApplicationConstants.USG_DB1 + ApplicationConstants.PAYMENT_VOUCHER_TABLE + "`";

        String paymentSearchQuery = "";

        if (pvobj.getDDO() != null && !pvobj.getDDO().isEmpty() && !pvobj.getDDO().equals("0")) {
            paymentSearchQuery = paymentSearchQuery + " pv.DDO=\"" + pvobj.getDDO() + "\"";
        }
        if (pvobj.getLocation() != null && !pvobj.getLocation().isEmpty() && !pvobj.getLocation().equals("0")) {
            paymentSearchQuery = paymentSearchQuery + " and pv.location=\"" + pvobj.getLocation() + "\"";
        }

        if (pvobj.getFundType() != null && !pvobj.getFundType().isEmpty() && !pvobj.getFundType().equals("0")) {
            paymentSearchQuery = paymentSearchQuery + " and pv.fundType=\"" + pvobj.getFundType() + "\"";
        }

        if (pvobj.getBudgetHead() != null && !pvobj.getBudgetHead().isEmpty() && !pvobj.getBudgetHead().equals("0")) {
            paymentSearchQuery = paymentSearchQuery + " and pv.budgetHead=\"" + pvobj.getBudgetHead() + "\"";
        }

        if (date != null && date != "" && !date.isEmpty()) {

            paymentSearchQuery = paymentSearchQuery + " and pv.voucherDateInMilliSecond=" + dateVD;

        }
        String paymentVoucherQuery = "select * from " + pvTable + ""
                + " as pv where " + paymentSearchQuery + " and pv.status=\"Active\"";

        String paymentVoucherOutput = aql1.getRestData(ApplicationConstants.END_POINT, paymentVoucherQuery);

        List<PaymentVoucher> payVocList = null;
        if (paymentVoucherOutput != null && !paymentVoucherOutput.isEmpty() && !paymentVoucherOutput.equals("[]")) {
            payVocList = new Gson().fromJson(paymentVoucherOutput, new TypeToken< ArrayList<PaymentVoucher>>() {
            }.getType());
        }

        List<LedgerList> payVocFinalList = new ArrayList<LedgerList>();
        if (paymentVoucherOutput != null && !paymentVoucherOutput.isEmpty() && !paymentVoucherOutput.equals("[]")) {
            int k1 = 0;
            int l1 = 0;
            for (PaymentVoucher payVou : payVocList) {
                for (k1 = 0; k1 < payVou.getLedgerList().size(); k1++) {
                    if ((payVou.getLedgerList().get(k1).getLedger()) != null) {
                        if (((payVou.getLedgerList().get(k1).getLedger()).equals(ledger)) && ((payVou.getPostingStatus().equals("Posted")))) {

                            if (((payVou.getLedgerList().get(k1).getDrCr()).equals("Cr"))) {
                                for (l1 = 0; l1 < payVou.getLedgerList().size(); l1++) {
                                    if (((payVou.getLedgerList().get(l1).getDrCr()).equals("Dr"))) {
                                        payVou.getLedgerList().get(l1).setVoucherDtReport(payVou.getVoucherDate());
                                        payVou.getLedgerList().get(l1).setVoucherNoReport(payVou.getVoucherNo());
                                        payVou.getLedgerList().get(l1).setVoucherTyReport(payVou.getVoucherName());
                                        payVou.getLedgerList().get(l1).setVoucherDateInMilliSecondReport(payVou.getVoucherDateInMilliSecond());
                                        payVocFinalList.add(payVou.getLedgerList().get(l1));

                                    }
                                }
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

        if (cvobj.getDDO() != null && !cvobj.getDDO().isEmpty() && !cvobj.getDDO().equals("0")) {
            contraSearchQuery = contraSearchQuery + " cv.DDO=\"" + cvobj.getDDO() + "\"";
        }
        if (cvobj.getLocation() != null && !cvobj.getLocation().isEmpty() && !cvobj.getLocation().equals("0")) {
            contraSearchQuery = contraSearchQuery + " and cv.location=\"" + cvobj.getLocation() + "\"";
        }

        if (cvobj.getFundType() != null && !cvobj.getFundType().isEmpty() && !cvobj.getFundType().equals("0")) {
            contraSearchQuery = contraSearchQuery + " and cv.fundType=\"" + cvobj.getFundType() + "\"";
        }

        if (cvobj.getBudgetHead() != null && !cvobj.getBudgetHead().isEmpty() && !cvobj.getBudgetHead().equals("0")) {
            contraSearchQuery = contraSearchQuery + " and cv.budgetHead=\"" + cvobj.getBudgetHead() + "\"";
        }

        if (date != null && date != "" && !date.isEmpty()) {

            contraSearchQuery = contraSearchQuery + " and cv.voucherDateInMilliSecond=" + dateVD;

        }

        String contraVoucherQuery = "select * from " + cvTable + ""
                + " as cv where " + contraSearchQuery + " and cv.status=\"Active\"";

        String contraVoucherOutput = aql2.getRestData(ApplicationConstants.END_POINT, contraVoucherQuery);

        List<ContraVoucher> conVocList = null;
        if (contraVoucherOutput != null && !contraVoucherOutput.isEmpty() && !contraVoucherOutput.equals("[]")) {
            conVocList = new Gson().fromJson(contraVoucherOutput, new TypeToken< ArrayList<ContraVoucher>>() {
            }.getType());
        }

        List<LedgerList> conVocFinalList = new ArrayList<LedgerList>();
        if (contraVoucherOutput != null && !contraVoucherOutput.isEmpty() && !contraVoucherOutput.equals("[]")) {
            int k2 = 0;
            int l2 = 0;
            for (ContraVoucher conVou : conVocList) {
                for (k2 = 0; k2 < conVou.getLedgerList().size(); k2++) {
                    if ((conVou.getLedgerList().get(k2).getLedger()) != null) {
                        if (((conVou.getLedgerList().get(k2).getLedger()).equals(ledger)) && ((conVou.getPostingStatus().equals("Posted")))) {

                            if (((conVou.getLedgerList().get(k2).getDrCr()).equals("Cr"))) {
                                for (l2 = 0; l2 < conVou.getLedgerList().size(); l2++) {
                                    if (((conVou.getLedgerList().get(l2).getDrCr()).equals("Dr"))) {
                                        conVou.getLedgerList().get(l2).setVoucherDtReport(conVou.getVoucherDate());
                                        conVou.getLedgerList().get(l2).setVoucherNoReport(conVou.getVoucherNo());
                                        conVou.getLedgerList().get(l2).setVoucherTyReport(conVou.getVoucherName());
                                        conVou.getLedgerList().get(l2).setVoucherDateInMilliSecondReport(conVou.getVoucherDateInMilliSecond());
                                        conVocFinalList.add(conVou.getLedgerList().get(l2));

                                    }
                                }
                            } else if (((conVou.getLedgerList().get(k2).getDrCr()).equals("Dr"))) {
                                for (l2 = 0; l2 < conVou.getLedgerList().size(); l2++) {
                                    if (((conVou.getLedgerList().get(l2).getDrCr()).equals("Cr"))) {
                                        conVou.getLedgerList().get(l2).setVoucherDtReport(conVou.getVoucherDate());
                                        conVou.getLedgerList().get(l2).setVoucherNoReport(conVou.getVoucherNo());
                                        conVou.getLedgerList().get(l2).setVoucherTyReport(conVou.getVoucherName());
                                        conVou.getLedgerList().get(l2).setVoucherDateInMilliSecondReport(conVou.getVoucherDateInMilliSecond());
                                        conVocFinalList.add(conVou.getLedgerList().get(l2));

                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        List<LedgerList> merged = new ArrayList(recVocFinalList);
        merged.addAll(payVocFinalList);
        merged.addAll(conVocFinalList);

        if (merged != null && date.equals("") && merged.size() > 0) {
            Collections.sort(merged, new VoucherDatesSortBy());

        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, bos);

        Font font1 = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
        Font font3 = new Font(Font.FontFamily.HELVETICA, 9);

        float[] ninthcolumnwidth = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
        float[] columnWidth = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
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
        Date date1 = new Date();
        DateFormat dateFormatTime = new SimpleDateFormat("HH : mm : ss");

        Phrase timePhrase = new Phrase(new Chunk("Date :" + dateFormat.format(date1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time:" + dateFormatTime.format(date1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
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
        BankBookReportManager.RoundRectangle roundRectangle = new BankBookReportManager.RoundRectangle();
        PdfPCell outercell = new PdfPCell();
        outercell.setCellEvent(roundRectangle);
        outercell.setBorder(Rectangle.NO_BORDER);
        outercell.setPadding(8);
        outercell.addElement(header);

        outerTable.addCell(outercell);
        document.add(outerTable);

        PdfPTable headertable = new PdfPTable(new float[]{5});
        PdfPCell cell = new PdfPCell(new Phrase("Bank Book", font1));
        cell.setColspan(4);
        cell.setRowspan(5);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(8f);
        cell.setPaddingLeft(165f);
        cell.setBorder(Rectangle.NO_BORDER);

        headertable.addCell(cell);
        document.add(headertable);

        PdfPTable table1 = new PdfPTable(2);
        table1.setWidthPercentage(100);
        table1.setWidths(twocolumnwidth);

        PdfPCell table1cella = null;
        if (rvobj.getDDO() != null) {
            String ddoJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, rvobj.getDDO());

            List<DDO> ddoList = new Gson().fromJson(ddoJson, new TypeToken<List<DDO>>() {
            }.getType());
            DDO ddo = ddoList.get(0);
            rvobj.setDdoName(ddo.getDdoName());
            table1cella = new PdfPCell(new Paragraph("DDO :" + rvobj.getDdoName(), font2));
            table1cella.setBorderColor(BaseColor.BLACK);
            table1cella.setBackgroundColor(BaseColor.WHITE);
            table1cella.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table1cella.setBorder(Rectangle.NO_BORDER);
        }

        PdfPCell table1cellb = null;
        if (rvobj.getLocation() != null) {
            String locationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, rvobj.getLocation());

            List<Location> locationList = new Gson().fromJson(locationJson, new TypeToken<List<Location>>() {
            }.getType());
            Location loc = locationList.get(0);
            rvobj.setLocationName(loc.getLocationName());
            table1cellb = new PdfPCell(new Paragraph("Location :" + rvobj.getLocationName(), font2));
            table1cellb.setBorderColor(BaseColor.BLACK);
            table1cellb.setBackgroundColor(BaseColor.WHITE);
            table1cellb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table1cellb.setBorder(Rectangle.NO_BORDER);
        }
        table1.addCell(table1cella);
        table1.addCell(table1cellb);
        document.add(table1);

        PdfPTable table2 = new PdfPTable(1);
        table2.setWidthPercentage(100);
        table2.setWidths(onecolumnwidth);

        PdfPCell table2cella = null;
        if (lLobj.getLedger() != null) {
            String ledJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_TABLE, lLobj.getLedger());

            List<Ledger> ledgerList = new Gson().fromJson(ledJson, new TypeToken<List<Ledger>>() {
            }.getType());
            Ledger led = ledgerList.get(0);
            String ledgerName = new CashBookReportManager().getLedgerCodeAndLedgerName(((LinkedTreeMap<String, String>) led.getId()).get("$oid"), led.getLedgerName());

            lLobj.setLedgerName(ledgerName);
            table2cella = new PdfPCell(new Paragraph("Ledger Name :" + lLobj.getLedgerName(), font2));
            table2cella.setBorderColor(BaseColor.BLACK);
            table2cella.setBackgroundColor(BaseColor.WHITE);
            table2cella.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2cella.setBorder(Rectangle.NO_BORDER);
        }
        table2.addCell(table2cella);
        document.add(table2);

        PdfPTable table3 = new PdfPTable(2);
        table3.setWidthPercentage(100);
        table3.setWidths(twocolumnwidth);

        PdfPCell table3cella = null;

        if ((date != null && !date.isEmpty())) {
            table3cella = new PdfPCell(new Paragraph("Date is :" + date, font2));
        } else {
            table3cella = new PdfPCell(new Paragraph("Date is :" + "  -  ", font2));
        }
        table3cella.setBorderColor(BaseColor.BLACK);
        table3cella.setBackgroundColor(BaseColor.WHITE);
        table3cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        table3cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table3cella.setBorder(Rectangle.NO_BORDER);

        PdfPCell table3cellb = null;
        String drCrval = drCrType;
        String opBal = roundTwoDecimalPoints(openingBalance);

        if (drCrval != null) {
            if (drCrval.equals("Dr")) {
                table3cellb = new PdfPCell(new Paragraph("Opening Balance :  " + opBal + " " + drCrval, font2));
            } else if (drCrval.equals("Cr")) {
                String removeMinus = opBal.replace("-", " ");
                table3cellb = new PdfPCell(new Paragraph("Opening Balance :  " + removeMinus + " " + drCrval, font2));
            } else {
                table3cellb = new PdfPCell(new Paragraph("Opening Balance :  ", font2));
            }
        } else {
            table3cellb = new PdfPCell(new Paragraph("Opening Balance :  " + opBal + " ", font2));
        }

        table3cellb.setBorderColor(BaseColor.BLACK);
        table3cellb.setBackgroundColor(BaseColor.WHITE);
        table3cellb.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table3cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table3cellb.setBorder(Rectangle.NO_BORDER);
        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        document.add(table3);

        if (merged == null || merged.size() > 0) {
            PdfPTable table5 = new PdfPTable(9);
            table5.setWidthPercentage(100); //Width 100%
            table5.setWidths(ninthcolumnwidth);

            PdfPCell table5cella = new PdfPCell(new Paragraph("S.No", font2));
            table5cella.setBorderColor(BaseColor.BLACK);
            table5cella.setBackgroundColor(BaseColor.WHITE);
            table5cella.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table5cella.setBorder(Rectangle.NO_BORDER);
            table5cella.setBorderWidthTop(1f);
            table5cella.setBorderWidthBottom(1f);

            PdfPCell table5cellb = new PdfPCell(new Paragraph("Date", font2));
            table5cellb.setBorderColor(BaseColor.BLACK);
            table5cellb.setBackgroundColor(BaseColor.WHITE);
            table5cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table5cellb.setBorder(Rectangle.NO_BORDER);
            table5cellb.setBorderWidthTop(1f);
            table5cellb.setBorderWidthBottom(1f);

            PdfPCell table5cellc = new PdfPCell(new Paragraph("Ledger Name", font2));
            table5cellc.setBorderColor(BaseColor.BLACK);
            table5cellc.setBackgroundColor(BaseColor.WHITE);
            table5cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table5cellc.setBorder(Rectangle.NO_BORDER);
            table5cellc.setBorderWidthTop(1f);
            table5cellc.setBorderWidthBottom(1f);

            PdfPCell table5celld = new PdfPCell(new Paragraph("Narration", font2));
            table5celld.setBorderColor(BaseColor.BLACK);
            table5celld.setBackgroundColor(BaseColor.WHITE);
            table5celld.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table5celld.setBorder(Rectangle.NO_BORDER);
            table5celld.setBorderWidthTop(1f);
            table5celld.setBorderWidthBottom(1f);

            PdfPCell table5celle = new PdfPCell(new Paragraph("Voucher No.", font2));
            table5celle.setBorderColor(BaseColor.BLACK);
            table5celle.setBackgroundColor(BaseColor.WHITE);
            table5celle.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table5celle.setBorder(Rectangle.NO_BORDER);
            table5celle.setBorderWidthTop(1f);
            table5celle.setBorderWidthBottom(1f);

            PdfPCell table5cellf = new PdfPCell(new Paragraph("Voucher Type", font2));
            table5cellf.setBorderColor(BaseColor.BLACK);
            table5cellf.setBackgroundColor(BaseColor.WHITE);
            table5cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cellf.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table5cellf.setBorder(Rectangle.NO_BORDER);
            table5cellf.setBorderWidthTop(1f);
            table5cellf.setBorderWidthBottom(1f);

            PdfPCell table5cellg = new PdfPCell(new Paragraph("Dr (Rs)", font2));
            table5cellg.setBorderColor(BaseColor.BLACK);
            table5cellg.setBackgroundColor(BaseColor.WHITE);
            table5cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cellg.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table5cellg.setBorder(Rectangle.NO_BORDER);
            table5cellg.setBorderWidthTop(1f);
            table5cellg.setBorderWidthBottom(1f);

            PdfPCell table5cellh = new PdfPCell(new Paragraph("Cr (Rs)", font2));
            table5cellh.setBorderColor(BaseColor.BLACK);
            table5cellh.setBackgroundColor(BaseColor.WHITE);
            table5cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5cellh.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table5cellh.setBorder(Rectangle.NO_BORDER);
            table5cellh.setBorderWidthTop(1f);
            table5cellh.setBorderWidthBottom(1f);

            PdfPCell table5celli = new PdfPCell(new Paragraph("Closing (Rs)", font2));
            table5celli.setBorderColor(BaseColor.BLACK);
            table5celli.setBackgroundColor(BaseColor.WHITE);
            table5celli.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5celli.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table5celli.setBorder(Rectangle.NO_BORDER);
            table5celli.setBorderWidthTop(1f);
            table5celli.setBorderWidthBottom(1f);

            table5.addCell(table5cella);
            table5.addCell(table5cellb);
            table5.addCell(table5cellc);
            table5.addCell(table5celld);
            table5.addCell(table5celle);
            table5.addCell(table5cellf);
            table5.addCell(table5cellg);
            table5.addCell(table5cellh);
            table5.addCell(table5celli);
            document.add(table5);

            int sNo = 1;

            PdfPTable table6 = new PdfPTable(9);
            table6.setWidthPercentage(100); //Width 100%
            table6.setWidths(ninthcolumnwidth);

            for (int i = 0; i < merged.size(); i++) {
                table6.flushContent();
                table6.setWidthPercentage(100); //Width 100%

                PdfPCell table6cell1 = new PdfPCell(new Paragraph(Integer.toString(sNo), font3));
                table6cell1.setBorderColor(BaseColor.BLACK);
                table6cell1.setBackgroundColor(BaseColor.WHITE);
                table6cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table6cell1.setPaddingTop(15f);
//            table6cell1.setBorder(Rectangle.NO_BORDER);
                table6cell1.setPaddingLeft(2f);

                PdfPCell table6cell2 = null;

                table6cell2 = new PdfPCell(new Paragraph(merged.get(i).getVoucherDtReport(), font3));
                table6cell2.setBorderColor(BaseColor.BLACK);
                table6cell2.setBackgroundColor(BaseColor.WHITE);
                table6cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table6cell2.setBorder(Rectangle.NO_BORDER);
                table6cell2.setPaddingTop(15f);

                PdfPCell table6cell3 = null;

                if (merged.get(i).getLedger() != null) {
                    String ledJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_TABLE, merged.get(i).getLedger());

                    List<Ledger> ledgerList = new Gson().fromJson(ledJson, new TypeToken<List<Ledger>>() {
                    }.getType());
                    Ledger led = ledgerList.get(0);
                    String ledgerName = new CashBookReportManager().getLedgerCodeAndLedgerName(((LinkedTreeMap<String, String>) led.getId()).get("$oid"), led.getLedgerName());

                    merged.get(i).setLedgerName(ledgerName);
                    table6cell3 = new PdfPCell(new Paragraph(merged.get(i).getLedgerName(), font3));
                    table6cell3.setBorderColor(BaseColor.BLACK);
                    table6cell3.setBackgroundColor(BaseColor.WHITE);
                    table6cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table6cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                table6cell3.setBorder(Rectangle.NO_BORDER);
                    table6cell3.setPaddingTop(15f);
                    table6cell3.setPaddingLeft(2f);
                }
                PdfPCell table6cell4 = new PdfPCell(new Paragraph(merged.get(i).getShortNarration(), font3));
                table6cell4.setBorderColor(BaseColor.BLACK);
                table6cell4.setBackgroundColor(BaseColor.WHITE);
                table6cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table6cell4.setBorder(Rectangle.NO_BORDER);
                table6cell4.setPaddingTop(15f);
                table6cell4.setPaddingLeft(2f);

                PdfPCell table6cell5 = new PdfPCell(new Paragraph(merged.get(i).getVoucherNoReport(), font3));
                table6cell5.setBorderColor(BaseColor.BLACK);
                table6cell5.setBackgroundColor(BaseColor.WHITE);
                table6cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table6cell5.setBorder(Rectangle.NO_BORDER);
                table6cell5.setPaddingTop(15f);
                table6cell5.setPaddingLeft(2f);

                PdfPCell table6cell6 = new PdfPCell(new Paragraph(merged.get(i).getVoucherTyReport(), font3));
                table6cell6.setBorderColor(BaseColor.BLACK);
                table6cell6.setBackgroundColor(BaseColor.WHITE);
                table6cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table6cell6.setBorder(Rectangle.NO_BORDER);
                table6cell6.setPaddingTop(15f);
                table6cell6.setPaddingLeft(2f);

                PdfPCell table6cell7 = null;
                String text = merged.get(i).getDrAmount();
                double value = Double.parseDouble(text);
                String textx = roundTwoDecimalPoints(value);
                totalDrAmount = totalDrAmount + value;

                table6cell7 = new PdfPCell(new Paragraph(textx, font3));
                table6cell7.setBorderColor(BaseColor.BLACK);
                table6cell7.setBackgroundColor(BaseColor.WHITE);
                table6cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table6cell7.setBorder(Rectangle.NO_BORDER);
                table6cell7.setPaddingTop(15f);
                table6cell7.setPaddingLeft(2f);

                String text1 = merged.get(i).getCrAmount();
                double value1 = Double.parseDouble(text1);
                String texty = roundTwoDecimalPoints(value1);
                totalCrAmount = totalCrAmount + value1;

                PdfPCell table6cell8 = new PdfPCell(new Paragraph(texty, font3));
                table6cell8.setBorderColor(BaseColor.BLACK);
                table6cell8.setBackgroundColor(BaseColor.WHITE);
                table6cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table6cell8.setBorder(Rectangle.NO_BORDER);
                table6cell8.setPaddingTop(15f);
                table6cell8.setPaddingLeft(2f);

                cbl = roundTwoDecimals(openingBalance + value1 - value);
                openingBalance = roundTwoDecimals(cbl);
                cbl1 = roundTwoDecimalPoints(cbl);

                PdfPCell table6cell9 = null;
                if (openingBalance >= 0.00) {
                    table6cell9 = new PdfPCell(new Paragraph(cbl1 + " Dr", font3));
                }

                if (openingBalance < 0.00) {
                    String str = cbl1;
                    String str1 = str.replace("-", " ");
                    table6cell9 = new PdfPCell(new Paragraph(str1 + " Cr", font3));
                }
                table6cell9.setBorderColor(BaseColor.BLACK);
                table6cell9.setBackgroundColor(BaseColor.WHITE);
                table6cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table6cell9.setBorder(Rectangle.NO_BORDER);
                table6cell9.setPaddingTop(15f);
                table6cell9.setPaddingLeft(2f);

                table6.addCell(table6cell1);
                table6.addCell(table6cell2);
                table6.addCell(table6cell3);
                table6.addCell(table6cell4);
                table6.addCell(table6cell5);
                table6.addCell(table6cell6);
                table6.addCell(table6cell7);
                table6.addCell(table6cell8);
                table6.addCell(table6cell9);

                sNo++;
                document.add(table6);

            }
            PdfPTable table7 = new PdfPTable(9);
            table7.setWidthPercentage(100); //Width 100%
            table7.setWidths(ninthcolumnwidth);

            PdfPCell table7cella = new PdfPCell(new Paragraph("", font2));
            table7cella.setBorderColor(BaseColor.BLACK);
            table7cella.setBackgroundColor(BaseColor.WHITE);
            table7cella.setHorizontalAlignment(Element.ALIGN_LEFT);
            table7cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7cella.setBorder(Rectangle.LEFT);
            table7cella.setBorderWidthTop(1f);

            PdfPCell table7cellb = new PdfPCell(new Paragraph("", font2));
            table7cellb.setBorderColor(BaseColor.BLACK);
            table7cellb.setBackgroundColor(BaseColor.WHITE);
            table7cellb.setHorizontalAlignment(Element.ALIGN_LEFT);
            table7cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7cellb.setBorder(Rectangle.NO_BORDER);
            table7cellb.setBorderWidthTop(1f);

            PdfPCell table7cellc = new PdfPCell(new Paragraph("", font2));
            table7cellc.setBorderColor(BaseColor.BLACK);
            table7cellc.setBackgroundColor(BaseColor.WHITE);
            table7cellc.setHorizontalAlignment(Element.ALIGN_LEFT);
            table7cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7cellc.setBorder(Rectangle.NO_BORDER);
            table7cellc.setBorderWidthTop(1f);

            PdfPCell table7celld = new PdfPCell(new Paragraph("", font2));
            table7celld.setBorderColor(BaseColor.BLACK);
            table7celld.setBackgroundColor(BaseColor.WHITE);
            table7celld.setHorizontalAlignment(Element.ALIGN_LEFT);
            table7celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7celld.setBorder(Rectangle.NO_BORDER);
            table7celld.setBorderWidthTop(1f);

            PdfPCell table7celle = new PdfPCell(new Paragraph("", font2));
            table7celle.setBorderColor(BaseColor.BLACK);
            table7celle.setBackgroundColor(BaseColor.WHITE);
            table7celle.setHorizontalAlignment(Element.ALIGN_LEFT);
            table7celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7celle.setBorder(Rectangle.NO_BORDER);
            table7celle.setBorderWidthTop(1f);

            PdfPCell table7cellf = new PdfPCell(new Paragraph("Total :", font2));
            table7cellf.setBorderColor(BaseColor.BLACK);
            table7cellf.setBackgroundColor(BaseColor.WHITE);
            table7cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
            table7cellf.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7cellf.setBorder(Rectangle.NO_BORDER);
            table7cellf.setBorderWidthTop(1f);

            totalDrAmount = roundTwoDecimals(totalDrAmount);
            String totalDrAmount1 = roundTwoDecimalPoints(totalDrAmount);

            PdfPCell table7cellg = new PdfPCell(new Paragraph(totalDrAmount1, font3));
            table7cellg.setBorderColor(BaseColor.BLACK);
            table7cellg.setBackgroundColor(BaseColor.WHITE);
            table7cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
            table7cellg.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7cellg.setBorder(Rectangle.NO_BORDER);
            table7cellg.setBorderWidthTop(1f);

            totalCrAmount = roundTwoDecimals(totalCrAmount);
            String totalCrAmount1 = roundTwoDecimalPoints(totalCrAmount);

            PdfPCell table7cellh = new PdfPCell(new Paragraph(totalCrAmount1, font3));
            table7cellh.setBorderColor(BaseColor.BLACK);
            table7cellh.setBackgroundColor(BaseColor.WHITE);
            table7cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
            table7cellh.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7cellh.setBorder(Rectangle.NO_BORDER);
            table7cellh.setBorderWidthTop(1f);

            PdfPCell table7celli = new PdfPCell(new Paragraph("", font2));
            table7celli.setBorderColor(BaseColor.BLACK);
            table7celli.setBackgroundColor(BaseColor.WHITE);
            table7celli.setHorizontalAlignment(Element.ALIGN_LEFT);
            table7celli.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7celli.setBorder(Rectangle.RIGHT);
            table7celli.setBorderWidthTop(1f);
            table7celli.setBorderWidthRight(1f);

            table7.addCell(table7cella);
            table7.addCell(table7cellb);
            table7.addCell(table7cellc);
            table7.addCell(table7celld);
            table7.addCell(table7celle);
            table7.addCell(table7cellf);
            table7.addCell(table7cellg);
            table7.addCell(table7cellh);
            table7.addCell(table7celli);
            document.add(table7);

            PdfPTable table8 = new PdfPTable(1);
            table8.setWidthPercentage(100);
            table8.setWidths(onecolumnwidth);

            PdfPCell table8cella = null;
            if (merged == null || merged.size() < 1) {
                if (drCrval != null) {
                    String str1 = opBal.replace("-", " ");
                    table8cella = new PdfPCell(new Paragraph("Closing Balance : " + str1 + " " + drCrval, font3));
                } else {
                    table8cella = new PdfPCell(new Paragraph("Closing Balance : " + opBal + " ", font3));
                }
            } else if (drCrval != null) {
                if (openingBalance > 0.00) {
                    table8cella = new PdfPCell(new Paragraph("Closing Balance : " + cbl1 + " Dr", font3));
                }

                if (openingBalance < 0.00) {
                    String str = cbl1;
                    String str1 = str.replace("-", " ");
                    table8cella = new PdfPCell(new Paragraph("Closing Balance : " + str1 + " Cr", font3));
                }
            } else {
                if (openingBalance > 0.00) {
                    table8cella = new PdfPCell(new Paragraph("Closing Balance : " + cbl1 + " Dr", font3));
                }
                if (openingBalance == 0.00) {
                    table8cella = new PdfPCell(new Paragraph("Closing Balance : " + openingBalance, font3));
                }
                if (openingBalance < 0.00) {
                    String str = cbl1;
                    String str1 = str.replace("-", " ");
                    table8cella = new PdfPCell(new Paragraph("Closing Balance : " + str1 + " Cr", font3));
                }

            }
            table8cella.setBorderColor(BaseColor.BLACK);
            table8cella.setBackgroundColor(BaseColor.WHITE);
            table8cella.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table8cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table8cella.setBorder(Rectangle.NO_BORDER);
            table8cella.setBorderWidthLeft(1f);
            table8cella.setBorderWidthRight(1f);
            table8.addCell(table8cella);
            document.add(table8);

//        LineSeparator line12 = new LineSeparator();
//        line12.setOffset(-1);
//        document.add(line12);
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
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        String output = nf.format(amount).replaceAll(",", "");
        return output;
    }
}
