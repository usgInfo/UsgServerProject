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
import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.dto.ReceiptVoucher;
import com.accure.hrms.dto.FinancialYear;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
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
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author accure
 */
public class ChequeIssueRegisterReportManager {
    
    public ByteArrayOutputStream chequeIssueRegisterReport(String fromDate, String toDate, String voucherdata, String ledger, String path, String fin) throws DocumentException, FileNotFoundException, Exception
            
    {
        Long fromCD = 0L;
        Long toCD = 0L;
        fromCD = saveInMilliSecondDate(fromDate);
        toCD = saveInMilliSecondDate(toDate);
        
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

        if (fromDate != null && fromDate != "" && !fromDate.isEmpty()) {
          
//            paymentSearchQuery = paymentSearchQuery + " and pv.ChequeDateInMilliSecond>=" + fromCD;
            paymentSearchQuery = paymentSearchQuery + " and pv.voucherDateInMilliSecond>=" + fromCD;

        }
        if (toDate != null && toDate != "" && !toDate.isEmpty()) {

//            paymentSearchQuery = paymentSearchQuery + " and pv.ChequeDateInMilliSecond<=" + toCD;
            paymentSearchQuery = paymentSearchQuery + " and pv.voucherDateInMilliSecond<=" + toCD;

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
            for (PaymentVoucher payVou : payVocList) {
                for (int k1 = 0; k1 < payVou.getLedgerList().size(); k1++) {

              if (ledger != null && ledger != "")  {
                    if (((payVou.getLedgerList().get(k1).getLedger().equals(ledger))) && ((payVou.getPaymentMode().equals("Cheque"))) && ((payVou.getLedgerList().get(k1).getDrCr().equals("Cr"))) && ((payVou.getPostingStatus().equals("Posted")))) {
                            for (int cn = 0; cn < payVou.getChequeList().size(); cn++) {
                            payVou.getLedgerList().get(cn).setVoucherDtReport(payVou.getVoucherDate());
                            payVou.getLedgerList().get(cn).setChequeDtReport(payVou.getChequeList().get(cn).getChequeDate());
                            payVou.getLedgerList().get(cn).setChequeNoReport(payVou.getChequeList().get(cn).getChequeNo());
                            payVou.getLedgerList().get(cn).setVoucherNoReport(payVou.getVoucherNo());
                            payVou.getLedgerList().get(cn).setInFavourOfReport(payVou.getChequeList().get(cn).getInFavorOf());
                            payVou.getLedgerList().get(cn).setLocationReport(payVou.getLocation());
                            payVou.getLedgerList().get(cn).setChequeAmtReport(payVou.getChequeList().get(cn).getChequeAmount());
                            payVou.getLedgerList().get(cn).setVoucherDateInMilliSecondReport(payVou.getVoucherDateInMilliSecond());
//                            payVou.getLedgerList().get(k1).setChequeDateInMilliSecondReport(payVou.getChequeDateInMilliSecond());
                            payVou.getLedgerList().get(cn).setPaymentModeReport(payVou.getPaymentMode());
                            payVocFinalList.add(payVou.getLedgerList().get(cn));
                            }

                    }
            }
            if (ledger == null || ledger == "" || ledger.equalsIgnoreCase("null"))
            {
                            if (((payVou.getPaymentMode().equals("Cheque"))) && ((payVou.getLedgerList().get(k1).getDrCr().equals("Cr"))) && ((payVou.getPostingStatus().equals("Posted")))) {
                            for (int cn = 0; cn < payVou.getChequeList().size(); cn++) {
                            payVou.getLedgerList().get(cn).setVoucherDtReport(payVou.getVoucherDate());
                            payVou.getLedgerList().get(cn).setChequeDtReport(payVou.getChequeList().get(cn).getChequeDate());
                            payVou.getLedgerList().get(cn).setChequeNoReport(payVou.getChequeList().get(cn).getChequeNo());
                            payVou.getLedgerList().get(cn).setVoucherNoReport(payVou.getVoucherNo());
                            payVou.getLedgerList().get(cn).setInFavourOfReport(payVou.getChequeList().get(cn).getInFavorOf());
                            payVou.getLedgerList().get(cn).setLocationReport(payVou.getLocation());
                            payVou.getLedgerList().get(cn).setChequeAmtReport(payVou.getChequeList().get(cn).getChequeAmount());
                            payVou.getLedgerList().get(cn).setVoucherDateInMilliSecondReport(payVou.getVoucherDateInMilliSecond());
//                            payVou.getLedgerList().get(k1).setChequeDateInMilliSecondReport(payVou.getChequeDateInMilliSecond());
                            payVou.getLedgerList().get(cn).setPaymentModeReport(payVou.getPaymentMode());
                            payVocFinalList.add(payVou.getLedgerList().get(cn));
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

        if (fromDate != null && fromDate != "" && !fromDate.isEmpty()) {
          
            contraSearchQuery = contraSearchQuery + " and cv.voucherDateInMilliSecond>=" + fromCD;

        }
        if (toDate != null && toDate != "" && !toDate.isEmpty()) {

            contraSearchQuery = contraSearchQuery + " and cv.voucherDateInMilliSecond<=" + toCD;

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
            for (ContraVoucher conVou : conVocList) {
                for (int k2 = 0; k2 < conVou.getLedgerList().size(); k2++) {

            if (ledger != null && ledger != "")  {                  
                    if (((conVou.getLedgerList().get(k2).getLedger().equals(ledger))) && ((conVou.getPaymentMode().equals("Cheque"))) && ((conVou.getPostingStatus().equals("Posted")))) {
                            for (int cn = 0; cn < conVou.getChequeList().size(); cn++) {
                            conVou.getLedgerList().get(cn).setVoucherDtReport(conVou.getVoucherDate());
                            conVou.getLedgerList().get(cn).setChequeDtReport(conVou.getChequeList().get(cn).getChequeDate());
                            conVou.getLedgerList().get(cn).setChequeNoReport(conVou.getChequeList().get(cn).getChequeNo());
                            conVou.getLedgerList().get(cn).setVoucherNoReport(conVou.getVoucherNo());
                            conVou.getLedgerList().get(cn).setInFavourOfReport(conVou.getChequeList().get(cn).getInFavorOf());
                            conVou.getLedgerList().get(cn).setLocationReport(conVou.getLocation());
                            conVou.getLedgerList().get(cn).setChequeAmtReport(conVou.getChequeList().get(cn).getChequeAmount());
                            conVou.getLedgerList().get(cn).setVoucherDateInMilliSecondReport(conVou.getVoucherDateInMilliSecond());
//                            conVou.getLedgerList().get(cn).setChequeDateInMilliSecondReport(conVou.getChequeDateInMilliSecond());
                            conVou.getLedgerList().get(cn).setPaymentModeReport(conVou.getPaymentMode());
                            conVocFinalList.add(conVou.getLedgerList().get(cn));
                            }

                    }
            }
            if (ledger == null || ledger == "" || ledger.equalsIgnoreCase("null"))
            {
                            if (((conVou.getPaymentMode().equals("Cheque"))) && ((conVou.getPostingStatus().equals("Posted"))) && ((conVou.getLedgerList().get(k2).getDrCr().equals("Cr")))) {
                            for (int cn = 0; cn < conVou.getChequeList().size(); cn++) {
                            conVou.getLedgerList().get(cn).setVoucherDtReport(conVou.getVoucherDate());
                            conVou.getLedgerList().get(cn).setChequeDtReport(conVou.getChequeList().get(cn).getChequeDate());
                            conVou.getLedgerList().get(cn).setChequeNoReport(conVou.getChequeList().get(cn).getChequeNo());
                            conVou.getLedgerList().get(cn).setVoucherNoReport(conVou.getVoucherNo());
                            conVou.getLedgerList().get(cn).setInFavourOfReport(conVou.getChequeList().get(cn).getInFavorOf());
                            conVou.getLedgerList().get(cn).setLocationReport(conVou.getLocation());
                            conVou.getLedgerList().get(cn).setChequeAmtReport(conVou.getChequeList().get(cn).getChequeAmount());
                            conVou.getLedgerList().get(cn).setVoucherDateInMilliSecondReport(conVou.getVoucherDateInMilliSecond());
//                            conVou.getLedgerList().get(cn).setChequeDateInMilliSecondReport(conVou.getChequeDateInMilliSecond());
                            conVou.getLedgerList().get(cn).setPaymentModeReport(conVou.getPaymentMode());
                            conVocFinalList.add(conVou.getLedgerList().get(cn));
                            }
                        }
                          
            }
                }

            }
        }

List<LedgerList> merged = new ArrayList(payVocFinalList);
        merged.addAll(conVocFinalList);
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

        float[] elevencolumnwidth = {2f, 3f, 3f, 3f, 3f, 3f, 3f, 5.6f, 2f, 2f, 2.3f};
        float[] columnWidth = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
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
        timePhrase.add(new Phrase(new Chunk(fin , FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));

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
        PdfPCell cell = new PdfPCell(new Phrase("CHEQUE ISSUE REGISTER ", font1));
        cell.setColspan(4);
        cell.setRowspan(5);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(8f);
        cell.setPaddingLeft(125f);
        cell.setBorder(Rectangle.NO_BORDER);

        headertable.addCell(cell);
        document.add(headertable);
    
        if (merged == null || merged.size() > 0){
        PdfPTable table1 = new PdfPTable(11); 
        table1.setWidthPercentage(100); //Width 100%
        table1.setWidths(elevencolumnwidth);

        PdfPCell table1cella = new PdfPCell(new Paragraph("S.No", font2));
        table1cella.setBorderColor(BaseColor.BLACK);
        table1cella.setBackgroundColor(BaseColor.WHITE);
        table1cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1cella.setBorder(Rectangle.NO_BORDER);
        table1cella.setBorderWidthTop(1f);
        table1cella.setBorderWidthBottom(1f);

        PdfPCell table1cellb = new PdfPCell(new Paragraph("Date", font2));
        table1cellb.setBorderColor(BaseColor.BLACK);
        table1cellb.setBackgroundColor(BaseColor.WHITE);
        table1cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1cellb.setBorder(Rectangle.NO_BORDER);
        table1cellb.setBorderWidthTop(1f);
        table1cellb.setBorderWidthBottom(1f);

        PdfPCell table1cellc = new PdfPCell(new Paragraph("Bill NO.", font2));
        table1cellc.setBorderColor(BaseColor.BLACK);
        table1cellc.setBackgroundColor(BaseColor.WHITE);
        table1cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1cellc.setBorder(Rectangle.NO_BORDER);
        table1cellc.setBorderWidthTop(1f);
        table1cellc.setBorderWidthBottom(1f);

        PdfPCell table1celld = new PdfPCell(new Paragraph("In Favour Of", font2));
        table1celld.setBorderColor(BaseColor.BLACK);
        table1celld.setBackgroundColor(BaseColor.WHITE);
        table1celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1celld.setBorder(Rectangle.NO_BORDER);
        table1celld.setBorderWidthTop(1f);
        table1celld.setBorderWidthBottom(1f);

        PdfPCell table1celle = new PdfPCell(new Paragraph("Place", font2));
        table1celle.setBorderColor(BaseColor.BLACK);
        table1celle.setBackgroundColor(BaseColor.WHITE);
        table1celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1celle.setBorder(Rectangle.NO_BORDER);
        table1celle.setBorderWidthTop(1f);
        table1celle.setBorderWidthBottom(1f);

        PdfPCell table1cellf = new PdfPCell(new Paragraph("Cheque NO.", font2));
        table1cellf.setBorderColor(BaseColor.BLACK);
        table1cellf.setBackgroundColor(BaseColor.WHITE);
        table1cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cellf.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1cellf.setBorder(Rectangle.NO_BORDER);
        table1cellf.setBorderWidthTop(1f);
        table1cellf.setBorderWidthBottom(1f);

        PdfPCell table1cellg = new PdfPCell(new Paragraph("Cheque Date", font2));
        table1cellg.setBorderColor(BaseColor.BLACK);
        table1cellg.setBackgroundColor(BaseColor.WHITE);
        table1cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cellg.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1cellg.setBorder(Rectangle.NO_BORDER);
        table1cellg.setBorderWidthTop(1f);
        table1cellg.setBorderWidthBottom(1f);

        PdfPCell table1cellh = new PdfPCell(new Paragraph("Amount", font2));
        table1cellh.setBorderColor(BaseColor.BLACK);
        table1cellh.setBackgroundColor(BaseColor.WHITE);
        table1cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cellh.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1cellh.setBorder(Rectangle.NO_BORDER);
        table1cellh.setBorderWidthTop(1f);
        table1cellh.setBorderWidthBottom(1f);

        PdfPCell table1celli = new PdfPCell(new Paragraph("Clerk sign", font2));
        table1celli.setBorderColor(BaseColor.BLACK);
        table1celli.setBackgroundColor(BaseColor.WHITE);
        table1celli.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1celli.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1celli.setBorder(Rectangle.NO_BORDER);
        table1celli.setBorderWidthTop(1f);
        table1celli.setBorderWidthBottom(1f);

        PdfPCell table1cellj = new PdfPCell(new Paragraph("To Sign", font2));
        table1cellj.setBorderColor(BaseColor.BLACK);
        table1cellj.setBackgroundColor(BaseColor.WHITE);
        table1cellj.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cellj.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1cellj.setBorder(Rectangle.NO_BORDER);
        table1cellj.setBorderWidthTop(1f);
        table1cellj.setBorderWidthBottom(1f);

        PdfPCell table1cellk = new PdfPCell(new Paragraph("Remark", font2));
        table1cellk.setBorderColor(BaseColor.BLACK);
        table1cellk.setBackgroundColor(BaseColor.WHITE);
        table1cellk.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1cellk.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table1cellk.setBorder(Rectangle.NO_BORDER);
        table1cellk.setBorderWidthTop(1f);
        table1cellk.setBorderWidthBottom(1f);
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
        table1.addCell(table1cellk);
        document.add(table1);
    
        int sNo = 1;

        PdfPTable table2 = new PdfPTable(11);
        table2.setWidthPercentage(100); //Width 100%
        table2.setWidths(elevencolumnwidth);

        for (int i = 0; i < merged.size(); i++) {
            table2.flushContent();
            table2.setWidthPercentage(100); //Width 100%

            PdfPCell table2cell1 = new PdfPCell(new Paragraph(Integer.toString(sNo), font3));
            table2cell1.setBorderColor(BaseColor.BLACK);
            table2cell1.setBackgroundColor(BaseColor.WHITE);
            table2cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2cell1.setPaddingTop(15f);
//            table2cell1.setBorder(Rectangle.NO_BORDER);
            table2cell1.setPaddingLeft(2f);

            PdfPCell table2cell2 = null;

            table2cell2 = new PdfPCell(new Paragraph(merged.get(i).getVoucherDtReport(), font3));
            table2cell2.setBorderColor(BaseColor.BLACK);
            table2cell2.setBackgroundColor(BaseColor.WHITE);
            table2cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2cell2.setPaddingTop(15f);
//            table2cell2.setBorder(Rectangle.NO_BORDER);
            table2cell2.setPaddingTop(15f);
            
            PdfPCell table2cell3 = new PdfPCell(new Paragraph(merged.get(i).getVoucherNoReport(), font3));
            table2cell3.setBorderColor(BaseColor.BLACK);
            table2cell3.setBackgroundColor(BaseColor.WHITE);
            table2cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table2cell3.setBorder(Rectangle.NO_BORDER);
            table2cell3.setPaddingTop(15f);
            table2cell3.setPaddingLeft(2f);
            
            PdfPCell table2cell4 = new PdfPCell(new Paragraph(merged.get(i).getInFavourOfReport(), font3));
            table2cell4.setBorderColor(BaseColor.BLACK);
            table2cell4.setBackgroundColor(BaseColor.WHITE);
            table2cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table2cell4.setBorder(Rectangle.NO_BORDER);
            table2cell4.setPaddingTop(15f);
            table2cell4.setPaddingLeft(2f);

            PdfPCell table2cell5 = null;
        if (merged.get(i).getLocationReport() != null) {
            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, merged.get(i).getLocationReport());
//              String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, location);

            List<Location> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Location>>() {
            }.getType());
            Location gal = gaList.get(0);
            merged.get(i).setLocationReport(gal.getLocationName());
            table2cell5 = new PdfPCell(new Paragraph(gal.getLocationName(), font3));
            table2cell5.setBorderColor(BaseColor.BLACK);
            table2cell5.setBackgroundColor(BaseColor.WHITE);
            table2cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table2cell5.setBorder(Rectangle.NO_BORDER);
            table2cell5.setPaddingTop(15f);
            table2cell5.setPaddingLeft(2f);
        }

            PdfPCell table2cell6 = new PdfPCell(new Paragraph(merged.get(i).getChequeNoReport(), font3));
            table2cell6.setBorderColor(BaseColor.BLACK);
            table2cell6.setBackgroundColor(BaseColor.WHITE);
            table2cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table2cell6.setBorder(Rectangle.NO_BORDER);
            table2cell6.setPaddingTop(15f);
            table2cell6.setPaddingLeft(2f);

            PdfPCell table2cell7 = new PdfPCell(new Paragraph(merged.get(i).getChequeDtReport(), font3));
            table2cell7.setBorderColor(BaseColor.BLACK);
            table2cell7.setBackgroundColor(BaseColor.WHITE);
            table2cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table2cell7.setBorder(Rectangle.NO_BORDER);
            table2cell7.setPaddingTop(15f);
            table2cell7.setPaddingLeft(2f);
            
            PdfPCell table2cell8 = null;
            String text = merged.get(i).getChequeAmtReport();
            double value = Double.parseDouble(text);
            String chequeAmountDouble = roundTwoDecimalPoints(value);
            table2cell8 = new PdfPCell(new Paragraph(chequeAmountDouble, font3));
            table2cell8.setBorderColor(BaseColor.BLACK);
            table2cell8.setBackgroundColor(BaseColor.WHITE);
            table2cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table2cell8.setBorder(Rectangle.NO_BORDER);
            table2cell8.setPaddingTop(15f);
            table2cell8.setPaddingLeft(2f);
            
            PdfPCell table2cell9 = new PdfPCell(new Paragraph("", font3));
            table2cell9.setBorderColor(BaseColor.BLACK);
            table2cell9.setBackgroundColor(BaseColor.WHITE);
            table2cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table2cell9.setBorder(Rectangle.NO_BORDER);
            table2cell9.setPaddingTop(15f);
            table2cell9.setPaddingLeft(2f);
            
            PdfPCell table2cell10 = new PdfPCell(new Paragraph("", font3));
            table2cell10.setBorderColor(BaseColor.BLACK);
            table2cell10.setBackgroundColor(BaseColor.WHITE);
            table2cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table2cell10.setBorder(Rectangle.NO_BORDER);
            table2cell10.setPaddingTop(15f);
            table2cell10.setPaddingLeft(2f);
            
            PdfPCell table2cell11 = new PdfPCell(new Paragraph("", font3));
            table2cell11.setBorderColor(BaseColor.BLACK);
            table2cell11.setBackgroundColor(BaseColor.WHITE);
            table2cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table2cell11.setBorder(Rectangle.NO_BORDER);
            table2cell11.setPaddingTop(15f);
            table2cell11.setPaddingLeft(2f);

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
            table2.addCell(table2cell11);

            sNo++;
            //   }
            document.add(table2);
        }
        }
        else{
        LineSeparator line14 = new LineSeparator();
        line14.setOffset(-1);
        document.add(line14);
        
        PdfPTable table9 = new PdfPTable(1);
        table9.setWidthPercentage(100);
        table9.setWidths(onecolumnwidth);

            PdfPCell table9cella = new PdfPCell(new Paragraph("No Records Exist " , font2));
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
