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
import com.accure.payroll.dto.LoanAllotment;
import com.accure.payroll.dto.LoanOrder;
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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author upendra
 */
public class LoanOrderStatementManager {
    
    public ByteArrayOutputStream loanOrderPdfStatement(String orderId, String path,String fin) throws DocumentException, FileNotFoundException, Exception {
        Rectangle pageSize = new Rectangle(700f, 700f);
        Document document = new Document(pageSize);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        PdfWriter.getInstance(document, bos);
        
        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        document.open();
        
        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{50, 200, 50});
        
        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);
        
        PdfPCell outercell = new PdfPCell();
        
        PdfPCell imagecell = new PdfPCell();
        
        Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(70.0f, 70.0f);
        
        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColor(BaseColor.WHITE);
        
        Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("LOAN ORDER STATEMENT FOR ORDER -  " + orderId + "", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        
        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
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
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
        
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
        
        PdfPTable headertable = new PdfPTable(new float[]{5});
        PdfPCell cell = new PdfPCell(new Phrase());
        cell.setColspan(4);
        cell.setRowspan(5);
        //   cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingBottom(8f);
        cell.setPaddingLeft(180f);
        cell.setBorder(Rectangle.NO_BORDER);
        
        headertable.addCell(cell);
        outercell.addElement(headertable);
        
        float[] eightcolumn = {1f, 3f, 3f, 3f, 3f, 3f, 3f, 3f};
        float[] columnWidth = {1f, 3f, 3f, 3f, 3f, 3f, 3f};
        float[] onecolumnwidth = {1f};
        float[] twocolumnwidth = {1f, 1f};
        PdfPTable table1 = new PdfPTable(1); // 3 columns.
        table1.setWidthPercentage(100); //Width 100%
        table1.setWidths(onecolumnwidth);
        
        PdfPCell cella = new PdfPCell(new Paragraph("The Sanction is here by accorded to draw an advance amount to the following employees of this University out of their own subscriptions towards the provident Fund noted against each.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        cella.setBorderColor(BaseColor.BLACK);
        cella.setBackgroundColor(BaseColor.WHITE);
//        cella.setPaddingLeft(15);
        cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cella.setBorder(Rectangle.NO_BORDER);
        //   cella.setBorderWidthBottom(1f);
        table1.addCell(cella);
        outercell.addElement(table1);
        
        PdfPTable headDescr = new PdfPTable(1); // 3 columns.
        headDescr.setWidthPercentage(100); //Width 100%
        headDescr.setWidths(onecolumnwidth);
        PdfPCell headDescrcella = new PdfPCell(new Paragraph("The recovery is to be made in the installments mentioned against their names from the next month in which the advance is drawn.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
        headDescrcella.setBorderColor(BaseColor.BLACK);
        headDescrcella.setBackgroundColor(BaseColor.WHITE);
//        headDescrcella.setPaddingLeft(15);
        headDescrcella.setHorizontalAlignment(Element.ALIGN_LEFT);
        headDescrcella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headDescrcella.setPaddingBottom(6f);
        headDescrcella.setBorder(Rectangle.NO_BORDER);
        headDescr.addCell(headDescrcella);
        outercell.addElement(headDescr);
        
        PdfPTable table4 = new PdfPTable(1); // 3 columns.
        table4.setWidthPercentage(100); //Width 100%
        table4.setWidths(onecolumnwidth);
        
        HashMap<String, String> loanOrderCondition = new HashMap<String, String>();
        loanOrderCondition.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        loanOrderCondition.put("orderNo", orderId);
        //System.out.println("condition:" + loanOrderCondition);
        String loanOrderresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ORDER_TABLE, loanOrderCondition);
        List<LoanOrder> loanOrderempList = new Gson().fromJson(loanOrderresult, new TypeToken<List<LoanOrder>>() {
        }.getType());
        //System.out.println("loan Order List" + loanOrderempList);
        // DDO
        String ddoName = new Gson().toJson(loanOrderempList.get(0).getDdo());
        ddoName = ddoName.replace("\"", "");
        String ddodata = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddoName);
        List<DDO> ddolist = new Gson().fromJson(ddodata, new TypeToken<List<DDO>>() {
        }.getType());
        DDO ddoval = ddolist.get(0);
        
        PdfPCell table4cella = new PdfPCell(new Paragraph("DDO Name:" + ddoval.getDdoName(), font2));
        table4cella.setBorder(Rectangle.NO_BORDER);
        table4cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table4cella.setPaddingBottom(10);
        table4.addCell(table4cella);
        outercell.addElement(table4);
//Table started
        PdfPTable table2 = new PdfPTable(8); // 3 columns.
        table2.setWidthPercentage(100); //Width 100%
        table2.setWidths(eightcolumn);
        
        PdfPCell table2cell = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table2cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2cell.setPaddingBottom(10);
        
        PdfPCell table2cell1 = new PdfPCell(new Paragraph("Recovery", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table2cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2cell1.setPaddingBottom(10);
        table2cell1.setColspan(2);
        
        table2.addCell(table2cell);
        table2.addCell(table2cell);
        table2.addCell(table2cell);
        table2.addCell(table2cell);
        table2.addCell(table2cell1);
        table2.addCell(table2cell);
        table2.addCell(table2cell);
        table2.addCell(table2cell);
        table2.addCell(table2cell);
        outercell.addElement(table2);
        
        PdfPTable table3 = new PdfPTable(8); // 3 columns.
        table3.setWidthPercentage(100); //Width 100%
        table3.setWidths(eightcolumn);
        
        PdfPCell table3cella = new PdfPCell(new Paragraph("S.No.", font2));
        table3cella.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cella.setPaddingBottom(10);
        
        PdfPCell table3cellb = new PdfPCell(new Paragraph("Employee Name", font2));
        table3cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellb.setPaddingBottom(10);
        
        PdfPCell table3cellc = new PdfPCell(new Paragraph("Employee Code", font2));
        table3cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellc.setPaddingBottom(10);
        
        PdfPCell table3celld = new PdfPCell(new Paragraph("Amount Sanctioned", font2));
        table3celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celld.setPaddingBottom(10);
        
        PdfPCell table3celle = new PdfPCell(new Paragraph("No.of Installment", font2));
        table3celle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3celle.setPaddingBottom(10);
        
        PdfPCell table3cellf = new PdfPCell(new Paragraph("Amount of Installment P.M", font2));
        table3cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellf.setPaddingBottom(10);
        
        PdfPCell table3cellg = new PdfPCell(new Paragraph("Amount adjust against Previous Loan", font2));
        table3cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellg.setPaddingBottom(10);
        
        PdfPCell table3cellh = new PdfPCell(new Paragraph("Total Of Net Loan Payable", font2));
        table3cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3cellh.setPaddingBottom(10);
        
        table3.addCell(table3cella);
        table3.addCell(table3cellb);
        table3.addCell(table3cellc);
        table3.addCell(table3celld);
        table3.addCell(table3celle);
        table3.addCell(table3cellf);
        table3.addCell(table3cellg);
        table3.addCell(table3cellh);
        outercell.addElement(table3);
        
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("orderNo", orderId);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ALLOTMENT_TABLE, conditionMap);
        List<LoanAllotment> empList = new Gson().fromJson(result, new TypeToken<List<LoanAllotment>>() {
        }.getType());
        int sNo = 1;
        PdfPTable table5 = new PdfPTable(8); // 3 columns.
        table5.setWidthPercentage(100); //Width 100%
        table5.setWidths(eightcolumn);
        double Sanamount = 0;
        if (empList != null) {
            for (int i = 0; i < empList.size(); i++) {
                table5 = new PdfPTable(8); // 3 columns.
                table5.setWidthPercentage(100); //Width 100%
                table5.setWidths(eightcolumn);
                
                String SerNo = Integer.toString(sNo);
                
                LoanAllotment lt = empList.get(i);
                PdfPCell table5cell1 = new PdfPCell(new Paragraph(SerNo, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                table5cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table5cell1.setPaddingBottom(10);
                table5cell1.setPaddingTop(10);
                
                PdfPCell cell2 = new PdfPCell(new Paragraph(lt.getEmpName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setPaddingBottom(10);
                cell2.setPaddingTop(10);
                
                PdfPCell cell3 = new PdfPCell(new Paragraph(lt.getEmpCode(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setPaddingBottom(10);
                cell3.setPaddingTop(10);
                
                Sanamount = Sanamount + lt.getAllotAmount();
                Sanamount = Math.round(Sanamount * 100.00) / 100.00;
                String sanamount = Double.toString(lt.getAllotAmount());
                
                PdfPCell cell4 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(lt.getSanctionedAmount()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setPaddingBottom(10);
                cell4.setPaddingTop(10);
                
                int totalInsta = 0;
                totalInsta = (int) lt.getTotalInstallment();
                // totalInsta = Math.round(totalInsta * 100) / 100;
                String totalStrInsta = Integer.toString(totalInsta);
                
                PdfPCell cell5 = new PdfPCell(new Paragraph(totalStrInsta, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setPaddingBottom(10);
                cell5.setPaddingTop(10);
                
                double instAmount = (int) lt.getInstallmentAmount();
                instAmount = Math.round(instAmount * 100) / 100;
                String insStrAmount = Double.toString(instAmount);
                PdfPCell cell6 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(instAmount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setPaddingBottom(10);
                cell6.setPaddingTop(10);
                
                double adjamount = lt.getAdjustAmount();
                adjamount = Math.round(adjamount * 100.00) / 100.00;
                String strAdjamount = Double.toString(adjamount);
                PdfPCell cell7 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(adjamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell7.setPaddingBottom(10);
                cell7.setPaddingTop(10);
                String strnetLoan = null;
                double netLoan = 0.00;
                if (lt.getInterestPercentage() != null) {
                    double interestPer = Double.parseDouble(lt.getInterestPercentage());
                    netLoan = (int) lt.getInstallmentAmount() * (int) lt.getTotalInstallment();
                    netLoan = Math.round(netLoan * 100.00) / 100.00;
                    strnetLoan = Double.toString(netLoan);
                }
                PdfPCell cell8 = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(lt.getAllotAmount()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
                cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell8.setPaddingBottom(10);
                cell8.setPaddingTop(10);
                
                table5.addCell(table5cell1);
                table5.addCell(cell2);
                table5.addCell(cell3);
                table5.addCell(cell4);
                table5.addCell(cell5);
                table5.addCell(cell6);
                table5.addCell(cell7);
                table5.addCell(cell8);
                outercell.addElement(table5);
                
                sNo++;
            }
        }
        PdfPTable table6 = new PdfPTable(8); // 3 columns.
        table6.setWidthPercentage(100); //Width 100%
        table6.setWidths(eightcolumn);
        
        PdfPCell table6cellaa = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        //  table6cellaa.setBorderWidthLeft(0);
        table6cellaa.setBorderWidthRight(0);
        table6cellaa.setPaddingBottom(5);
        table6cellaa.setPaddingTop(5);
        
        PdfPCell table6cella3 = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        table6cella3.setBorderWidthRight(0);
        table6cella3.setBorderWidthLeft(0);
        table6cella3.setPaddingBottom(5);
        table6cella3.setPaddingTop(5);
        
        PdfPCell table7cellg = new PdfPCell(new Paragraph("Total Amount", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        table7cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table7cellg.setPaddingBottom(5);
        table7cellg.setBorderWidthRight(0);
        table7cellg.setBorderWidthLeft(0);
        table7cellg.setPaddingTop(5);
        
        String totalsanAmount = Double.toString(Sanamount);
        PdfPCell table6cellb = new PdfPCell(new Paragraph(SalarySlipRportPDFGeneration.roundTwoDecimalPoints(Sanamount), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK)));
        table6cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table6cellb.setPaddingRight(5);
        table6cellb.setPaddingBottom(5);
        table6cellb.setPaddingTop(5);
        //table6cellb.setBorderWidthRight(0);
        table6cellb.setBorderWidthLeft(0);
        
        table6.addCell(table6cellaa);
        table6.addCell(table6cella3);
        table6.addCell(table6cella3);
        table6.addCell(table6cella3);
        table6.addCell(table6cella3);
        table6.addCell(table6cella3);
        table6.addCell(table7cellg);
        table6.addCell(table6cellb);
        outercell.addElement(table6);
//table 7

        PdfPTable comptroller = new PdfPTable(1); // 3 columns.
        comptroller.setWidthPercentage(100); //Width 100%
        comptroller.setWidths(onecolumnwidth);
        
        String ComproName = new Gson().toJson(loanOrderempList.get(0).getComptroller());
        
        ComproName = ComproName.replace("\"", "");
        
        String empdata = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_TABLE, ComproName);
        List<Employee> emplist = new Gson().fromJson(empdata, new TypeToken<List<Employee>>() {
        }.getType());
        if (emplist != null) {
            Employee li = emplist.get(0);
            PdfPCell comptrollercella = new PdfPCell(new Paragraph(li.getEmployeeName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK)));
            comptrollercella.setBorderColor(BaseColor.BLACK);
            comptrollercella.setBackgroundColor(BaseColor.WHITE);
            comptrollercella.setHorizontalAlignment(Element.ALIGN_RIGHT);
            comptrollercella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            comptrollercella.setPaddingBottom(8f);
            comptrollercella.setPaddingRight(5f);
            comptrollercella.setBorder(Rectangle.NO_BORDER);
            comptroller.addCell(comptrollercella);
            outercell.addElement(comptroller);
        } else {
            PdfPCell comptrollercella = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK)));
            comptrollercella.setBorderColor(BaseColor.BLACK);
            comptrollercella.setBackgroundColor(BaseColor.WHITE);
            comptrollercella.setHorizontalAlignment(Element.ALIGN_RIGHT);
            comptrollercella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            comptrollercella.setPaddingBottom(8f);
            comptrollercella.setPaddingRight(5f);
            comptrollercella.setBorder(Rectangle.NO_BORDER);
            comptroller.addCell(comptrollercella);
            outercell.addElement(comptroller);
        }
        PdfPTable table7 = new PdfPTable(1); // 3 columns.
        table7.setWidthPercentage(100); //Width 100%
        table7.setWidths(onecolumnwidth);
        
        PdfPCell table7cella = new PdfPCell(new Paragraph("COMPTROLLER", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        table7cella.setBorderColor(BaseColor.BLACK);
        table7cella.setBackgroundColor(BaseColor.WHITE);
        table7cella.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table7cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table7cella.setBorder(Rectangle.NO_BORDER);
        table7cella.setPaddingBottom(20f);
        table7.addCell(table7cella);
        outercell.addElement(table7);

        // copyto     
        PdfPTable table8 = new PdfPTable(1);
        table8.setWidthPercentage(100);
        table8.setWidths(onecolumnwidth);
        
        PdfPCell table8cella = new PdfPCell(new Paragraph("Copy to:-", font2));
        table8cella.setBorderColor(BaseColor.BLACK);
        table8cella.setBackgroundColor(BaseColor.WHITE);
        table8cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        table8cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table8cella.setBorder(Rectangle.NO_BORDER);
        table8.addCell(table8cella);
        outercell.addElement(table8);
//copy to description
        PdfPTable table9 = new PdfPTable(1);
        table9.setWidthPercentage(100);
        table9.setWidths(onecolumnwidth);
        
        PdfPCell table9cella = new PdfPCell(new Paragraph("1.The concerned DDO with the request to deduct the said amount from the salary of the person concerned every month and to mentioned the sanction No. and Emp No.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        table9cella.setBorderColor(BaseColor.BLACK);
        table9cella.setBackgroundColor(BaseColor.WHITE);
        table9cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        table9cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table9cella.setBorder(Rectangle.NO_BORDER);
        table9.addCell(table9cella);
        outercell.addElement(table9);
        
        PdfPTable table10 = new PdfPTable(1);
        table10.setWidthPercentage(100);
        table10.setWidths(onecolumnwidth);
        PdfPCell table10cella = new PdfPCell(new Paragraph("2.Person Concerned", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        table10cella.setBorderColor(BaseColor.BLACK);
        table10cella.setBackgroundColor(BaseColor.WHITE);
        table10cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        table10cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table10cella.setBorder(Rectangle.NO_BORDER);
        table10.addCell(table10cella);
        outercell.addElement(table10);
//Asst Account Office

        PdfPTable assistofficerSign = new PdfPTable(1); // 3 columns.
        assistofficerSign.setWidthPercentage(100); //Width 100%
        assistofficerSign.setWidths(onecolumnwidth);
        
        String accountOfficerName = new Gson().toJson(loanOrderempList.get(0).getSanctionedBy());
        
        accountOfficerName = accountOfficerName.replace("\"", "");
        //System.out.println("sanctioned By::::::" + accountOfficerName);
        String accountOfficerdata = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_TABLE, accountOfficerName);
        List<Employee> accountOfficerlist = new Gson().fromJson(accountOfficerdata, new TypeToken<List<Employee>>() {
        }.getType());
        if (accountOfficerlist != null) {
            Employee accountOffic = accountOfficerlist.get(0);
            PdfPCell assistofficerSigncella = new PdfPCell(new Paragraph(accountOffic.getEmployeeName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK)));
            assistofficerSigncella.setBorderColor(BaseColor.BLACK);
            assistofficerSigncella.setBackgroundColor(BaseColor.WHITE);
            assistofficerSigncella.setHorizontalAlignment(Element.ALIGN_RIGHT);
            assistofficerSigncella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            assistofficerSigncella.setPaddingBottom(8f);
            assistofficerSigncella.setPaddingRight(5f);
            assistofficerSigncella.setBorder(Rectangle.NO_BORDER);
            assistofficerSign.addCell(assistofficerSigncella);
            outercell.addElement(assistofficerSign);
        } else {
            
            PdfPCell assistofficerSigncella = new PdfPCell(new Paragraph("", font3));
            assistofficerSigncella.setBorderColor(BaseColor.BLACK);
            assistofficerSigncella.setBackgroundColor(BaseColor.WHITE);
            assistofficerSigncella.setHorizontalAlignment(Element.ALIGN_RIGHT);
            assistofficerSigncella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            assistofficerSigncella.setPaddingBottom(8f);
            assistofficerSigncella.setPaddingRight(5f);
            assistofficerSigncella.setBorder(Rectangle.NO_BORDER);
            assistofficerSign.addCell(assistofficerSigncella);
            outercell.addElement(assistofficerSign);
        }
        PdfPTable assistofficer = new PdfPTable(1); // 3 columns.
        assistofficer.setWidthPercentage(100); //Width 100%
        assistofficer.setWidths(onecolumnwidth);
        
        PdfPCell assistofficercell = new PdfPCell(new Paragraph("ASST.ACCOUNTS OFFICER", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK)));
        assistofficercell.setBorderColor(BaseColor.BLACK);
        assistofficercell.setBackgroundColor(BaseColor.WHITE);
        assistofficercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        assistofficercell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        assistofficercell.setBorder(Rectangle.NO_BORDER);
        assistofficercell.setPaddingBottom(30f);
        assistofficer.addCell(assistofficercell);
        outercell.addElement(assistofficer);
        
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
    
    public static void main(String[] args) throws Exception {
//        LoanOrderStatementManager manager = new LoanOrderStatementManager();
//        ByteArrayOutputStream result = manager.loanOrderPdfStatement("1");
//        //System.out.println("final resiult---------------------" + result);

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
