/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.reportformat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.accure.budget.dto.BudgetAtAGlance;
import com.accure.budget.dto.BudgetAtAGlance.LedgerWiseEstimate;
import com.accure.budget.dto.FinancialYear;
import com.accure.finance.dto.BankReconcilation;
import com.accure.finance.dto.FDRProcess;
import com.accure.finance.dto.GovtBudgetHead;
import com.accure.finance.dto.MajorHead;
import com.accure.finance.dto.MinorHead;
import com.accure.finance.dto.SubMajorHead;
import com.accure.finance.dto.SubMinorHead;
import com.accure.finance.dto.TrialBalance;
import com.accure.finance.manager.BankReconcilationManager;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class CreatePDFFileNew {

    public static void main(String[] args) throws Exception {
        BankReconcilationManager manager = new BankReconcilationManager();
        List<BankReconcilation> bankDetails = manager.fetchAllBankReconcilation();
        /*for(int i=0;i<bankDetails.size();i++){
         //System.out.println("BANKDETAILS:"+bankDetails);
         }
         Document document = new Document();
         PdfPTable table = new PdfPTable(new float[] { 2, 1, 2 });
         table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
         table.addCell("Name");
         table.addCell("Age");
         table.addCell("Location");
         table.setHeaderRows(1);
         PdfPCell[] cells = table.getRow(0).getCells(); 
         for (int j=0;j<cells.length;j++){
         cells[j].setBackgroundColor(BaseColor.GRAY);
         }
         for (int i=1;i<5;i++){
         table.addCell("Name:"+i);
         table.addCell("Age:"+i);
         table.addCell("Location:"+i);
         }
         PdfWriter.getInstance(document, new FileOutputStream("D:/reconcilationBankPDF.pdf"));
         document.open();
         document.add(table);
         document.close();
         //System.out.println("reconcilationBankPDF.pdf file created in 'D' drive");
         }*/
    }

    public static String createPdfforReconcilation(String data, String filePath) {
        try {

            JSONArray arrayJson = new JSONArray(data);
            CDL.toString(arrayJson);
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath + File.separator + "BankReconcilationReport.pdf"));

            document.open();

            PdfPTable table = new PdfPTable(5); // 3 columns.
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            table.setSpacingAfter(10f); //Space after table

            //Set Column widths
            float[] columnWidths = {1f, 1f, 1f, 1f, 1f};
            table.setWidths(columnWidths);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FromDate"));
            cell1.setBorderColor(BaseColor.BLACK);
            cell1.setBackgroundColor(BaseColor.GRAY);
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell2 = new PdfPCell(new Paragraph("ToDate"));
            cell2.setBorderColor(BaseColor.BLACK);
            cell2.setBackgroundColor(BaseColor.GRAY);
            cell2.setPaddingLeft(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell3 = new PdfPCell(new Paragraph("Location"));
            cell3.setBorderColor(BaseColor.BLACK);
            cell3.setBackgroundColor(BaseColor.GRAY);
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell4 = new PdfPCell(new Paragraph("Ledger"));
            cell4.setBorderColor(BaseColor.BLACK);
            cell4.setBackgroundColor(BaseColor.GRAY);
            cell4.setPaddingLeft(10);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell5 = new PdfPCell(new Paragraph("ReconcilationStatus"));
            cell5.setBorderColor(BaseColor.BLACK);
            cell5.setBackgroundColor(BaseColor.GRAY);
            cell5.setPaddingLeft(10);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

            for (int i = 0; i < arrayJson.length(); i++) {
                JSONObject obj = arrayJson.getJSONObject(i);
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                cell1 = new PdfPCell(new Paragraph(obj.getString("fromDate")));
                cell1.setBorderColor(BaseColor.BLACK);
                cell1.setPaddingLeft(10);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell2 = new PdfPCell(new Paragraph(obj.getString("toDate")));
                cell2.setBorderColor(BaseColor.BLACK);
                cell2.setPaddingLeft(10);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell3 = new PdfPCell(new Paragraph(obj.getString("locationName")));
                cell3.setBorderColor(BaseColor.BLACK);
                cell3.setPaddingLeft(10);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell4 = new PdfPCell(new Paragraph(obj.getString("ledgerName")));
                cell4.setBorderColor(BaseColor.BLACK);
                cell4.setPaddingLeft(10);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell5 = new PdfPCell(new Paragraph(obj.getString("reconcilationStatus")));
                cell5.setBorderColor(BaseColor.BLACK);
                cell5.setPaddingLeft(10);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
            }
            document.add(table);

            document.close();
            writer.close();
            return filePath + File.separator + "BankReconcilationReport.pdf";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ByteArrayOutputStream generateFdrStatementReport(List<FDRProcess> fdrProcessList,
            String path) {
        try {
            Document document = new Document();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, bos);
            Font font1 = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font font2 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
            Font font3 = new Font(Font.FontFamily.HELVETICA, 9);
            document.open();

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

            Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
            headerPhrase.add(new Phrase(new Chunk("\n")));
            headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
            headerPhrase.add(new Phrase(new Chunk("\n")));
            headerPhrase.add(new Phrase(new Chunk("\n")));
            headerPhrase.add(new Phrase("FDR Statement Report"));
            headerPhrase.add(new Phrase(new Chunk("\n")));

            PdfPCell headercell = new PdfPCell(headerPhrase);
            headercell.setBorderWidthBottom(1);
            headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headercell.setBorderColor(BaseColor.WHITE);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            DateFormat dateFormatTime = new SimpleDateFormat("HH : mm : ss");

            Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK)));
            timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk("\n")));
            timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));

            PdfPCell timecell = new PdfPCell(timePhrase);
            timecell.setBorderWidthBottom(1);
            timecell.setPaddingTop(3.0f);
            timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            timecell.setBorderColor(BaseColor.WHITE);
            header.addCell(imagecell);
            header.addCell(headercell);
            header.addCell(timecell);

            document.add(header);

            float[] columnWidth = {0.7f, 1.7f, 1.5f, 1.5f, 2f, 1f, 1.5f, 2f, 2f, 2f, 2f, 2.3f};

            PdfPTable table1 = new PdfPTable(12); // 12 columns.
            table1.setWidthPercentage(100); //Width 100%

            table1.setWidths(columnWidth);

            if (!(fdrProcessList.size() > 0)) {
                PdfPCell cell0 = new PdfPCell(new Paragraph("No Records Exist", font2));
                cell0.setBorderColor(BaseColor.BLACK);
                cell0.setBackgroundColor(BaseColor.WHITE);
                cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell0.setBorder(Rectangle.BOX);
                cell0.setColspan(12);

                table1.addCell(cell0);
                document.add(table1);
                document.close();
                writer.close();
                return bos;
            }

            PdfPCell cella = new PdfPCell(new Paragraph("Sl No", font2));
            cella.setBorderColor(BaseColor.BLACK);
            cella.setBackgroundColor(BaseColor.WHITE);
            cella.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella.setVerticalAlignment(Element.ALIGN_CENTER);
            cella.setBorder(Rectangle.BOX);

            PdfPCell cellb = new PdfPCell(new Paragraph("Payment Mode", font2));
            cellb.setBorderColor(BaseColor.BLACK);
            cellb.setBackgroundColor(BaseColor.WHITE);
            cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellb.setVerticalAlignment(Element.ALIGN_CENTER);
            cellb.setBorder(Rectangle.BOX);

            PdfPCell cellc = new PdfPCell(new Paragraph("Bank Name", font2));
            cellc.setBorderColor(BaseColor.BLACK);
            cellc.setBackgroundColor(BaseColor.WHITE);
            cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellc.setVerticalAlignment(Element.ALIGN_CENTER);
            cellc.setBorder(Rectangle.BOX);

            PdfPCell celld = new PdfPCell(new Paragraph("FDR Number", font2));
            celld.setBorderColor(BaseColor.BLACK);
            celld.setBackgroundColor(BaseColor.WHITE);
            celld.setHorizontalAlignment(Element.ALIGN_CENTER);
            celld.setVerticalAlignment(Element.ALIGN_CENTER);
            celld.setBorder(Rectangle.BOX);

            PdfPCell celle = new PdfPCell(new Paragraph("Amount", font2));
            celle.setBorderColor(BaseColor.BLACK);
            celle.setBackgroundColor(BaseColor.WHITE);
            celle.setHorizontalAlignment(Element.ALIGN_CENTER);
            celle.setVerticalAlignment(Element.ALIGN_CENTER);
            celle.setBorder(Rectangle.BOX);

            PdfPCell cellf = new PdfPCell(new Paragraph("Rate", font2));
            cellf.setBorderColor(BaseColor.BLACK);
            cellf.setBackgroundColor(BaseColor.WHITE);
            cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellf.setVerticalAlignment(Element.ALIGN_CENTER);
            cellf.setBorder(Rectangle.BOX);

            PdfPCell cellg = new PdfPCell(new Paragraph("Periods", font2));
            cellg.setBorderColor(BaseColor.BLACK);
            cellg.setBackgroundColor(BaseColor.WHITE);
            cellg.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellg.setVerticalAlignment(Element.ALIGN_CENTER);
            cellg.setBorder(Rectangle.BOX);

            PdfPCell cellh = new PdfPCell(new Paragraph("Interest Amount", font2));
            cellh.setBorderColor(BaseColor.BLACK);
            cellh.setBackgroundColor(BaseColor.WHITE);
            cellh.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellh.setVerticalAlignment(Element.ALIGN_CENTER);
            cellh.setBorder(Rectangle.BOX);

            PdfPCell celli = new PdfPCell(new Paragraph("Maturity Amount", font2));
            celli.setBorderColor(BaseColor.BLACK);
            celli.setBackgroundColor(BaseColor.WHITE);
            celli.setHorizontalAlignment(Element.ALIGN_CENTER);
            celli.setVerticalAlignment(Element.ALIGN_CENTER);
            celli.setBorder(Rectangle.BOX);

            PdfPCell cellj = new PdfPCell(new Paragraph("FD Date", font2));
            cellj.setBorderColor(BaseColor.BLACK);
            cellj.setBackgroundColor(BaseColor.WHITE);
            cellj.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellj.setVerticalAlignment(Element.ALIGN_CENTER);
            cellj.setBorder(Rectangle.BOX);

            PdfPCell cellk = new PdfPCell(new Paragraph("Maturity Date", font2));
            cellk.setBorderColor(BaseColor.BLACK);
            cellk.setBackgroundColor(BaseColor.WHITE);
            cellk.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellk.setVerticalAlignment(Element.ALIGN_CENTER);
            cellk.setBorder(Rectangle.BOX);

            PdfPCell celll = new PdfPCell(new Paragraph("Encashment Date", font2));
            celll.setBorderColor(BaseColor.BLACK);
            celll.setBackgroundColor(BaseColor.WHITE);
            celll.setHorizontalAlignment(Element.ALIGN_CENTER);
            celll.setVerticalAlignment(Element.ALIGN_CENTER);
            celll.setBorder(Rectangle.BOX);

            table1.addCell(cella);
            table1.addCell(cellb);
            table1.addCell(cellc);
            table1.addCell(celld);
            table1.addCell(celle);
            table1.addCell(cellf);
            table1.addCell(cellg);
            table1.addCell(cellh);
            table1.addCell(celli);
            table1.addCell(cellj);
            table1.addCell(cellk);
            table1.addCell(celll);
//            document.add(table1);

            int sNo = 1;

            NumberFormat numberFormat = NumberFormat.getInstance();

            //Set Column widths
            for (int i = 0; i < fdrProcessList.size(); i++) {
                String SerNo = Integer.toString(sNo);

                FDRProcess lt = fdrProcessList.get(i);
                cella = new PdfPCell(new Paragraph(SerNo, font3));
                cella.setBorderColor(BaseColor.BLACK);
                cella.setBackgroundColor(BaseColor.WHITE);
                cella.setHorizontalAlignment(Element.ALIGN_LEFT);
                cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cella.setBorder(Rectangle.BOX);

                cellb = new PdfPCell(new Paragraph(lt.getPaymentMode(), font3));
                cellb.setBorderColor(BaseColor.BLACK);
                cellb.setBackgroundColor(BaseColor.WHITE);
                cellb.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellb.setBorder(Rectangle.BOX);

                cellc = new PdfPCell(new Paragraph(lt.getBankName(), font3));
                cellc.setBorderColor(BaseColor.BLACK);
                cellc.setBackgroundColor(BaseColor.WHITE);
                cellc.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellc.setBorder(Rectangle.BOX);

                celld = new PdfPCell(new Paragraph(lt.getFdNumber(), font3));
                celld.setBorderColor(BaseColor.BLACK);
                celld.setBackgroundColor(BaseColor.WHITE);
                celld.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celld.setBorder(Rectangle.BOX);

                String text = lt.getFdAmount();
                double amount = numberFormat.parse(text).doubleValue();
                celle = new PdfPCell(new Paragraph(String.format("%.2f", amount), font3));
                celle.setBorderColor(BaseColor.BLACK);
                celle.setBackgroundColor(BaseColor.WHITE);
                celle.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celle.setBorder(Rectangle.BOX);

//                cellf = new PdfPCell(new Paragraph(lt.getFdRate(), font3));
//                cellf.setBorderColor(BaseColor.BLACK);
//                cellf.setBackgroundColor(BaseColor.WHITE);
//                cellf.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                cellf.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cellf.setBorder(Rectangle.BOX);
                cellg = new PdfPCell(new Paragraph(lt.getFdPeriod(), font3));
                cellg.setBorderColor(BaseColor.BLACK);
                cellg.setBackgroundColor(BaseColor.WHITE);
                cellg.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellg.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellg.setBorder(Rectangle.BOX);

//                String text2 = lt.getFdInterestAmount();
//                double value2 = numberFormat.parse(text2).doubleValue();
//                cellh = new PdfPCell(new Paragraph(String.format("%.2f", value2), font3));
//                cellh.setBorderColor(BaseColor.BLACK);
//                cellh.setBackgroundColor(BaseColor.WHITE);
//                cellh.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                cellh.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cellh.setBorder(Rectangle.BOX);
                String text3 = lt.getFdMaturityAmount();
                double value3 = numberFormat.parse(text3).doubleValue();
                celli = new PdfPCell(new Paragraph(String.format("%.2f", value3), font3));
                celli.setBorderColor(BaseColor.BLACK);
                celli.setBackgroundColor(BaseColor.WHITE);
                celli.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celli.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celli.setBorder(Rectangle.BOX);

                cellj = new PdfPCell(new Paragraph(lt.getFdDate(), font3));
                cellj.setBorderColor(BaseColor.BLACK);
                cellj.setBackgroundColor(BaseColor.WHITE);
                cellj.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellj.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellj.setBorder(Rectangle.BOX);

                cellk = new PdfPCell(new Paragraph(lt.getFdMaturityDate(), font3));
                cellk.setBorderColor(BaseColor.BLACK);
                cellk.setBackgroundColor(BaseColor.WHITE);
                cellk.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellk.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellk.setBorder(Rectangle.BOX);

                celll = new PdfPCell(new Paragraph(lt.getFdEncashmentDate(), font3));
                celll.setBorderColor(BaseColor.BLACK);
                celll.setBackgroundColor(BaseColor.WHITE);
                celll.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celll.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celll.setBorder(Rectangle.BOX);

                table1.addCell(cella);
                table1.addCell(cellb);
                table1.addCell(cellc);
                table1.addCell(celld);
                table1.addCell(celle);
                table1.addCell(cellf);
                table1.addCell(cellg);
                table1.addCell(cellh);
                table1.addCell(celli);
                table1.addCell(cellj);
                table1.addCell(cellk);
                table1.addCell(celll);

                sNo++;
            }
            document.add(table1);
            document.close();
            writer.close();
            return bos;

        } catch (Exception e) {
            return null;
        }//To change body of generated methods, choose Tools | Templates.

    }

    public ByteArrayOutputStream generateTrialBalanceReport(TreeMap<String, Object> trialBalanceResult,
            String path) {
        try {
            Document document = new Document();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, bos);
            Font font1 = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font font2 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
            Font font3 = new Font(Font.FontFamily.HELVETICA, 9);
            document.open();

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

            Phrase headerPhrase = new Phrase(new Chunk("Saurashtra University", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK)));
            headerPhrase.add(new Phrase(new Chunk("\n")));
            headerPhrase.add(new Phrase(new Chunk("   Rajkot - 360005.Gujarat, India. ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK))));
            headerPhrase.add(new Phrase(new Chunk("\n")));
            headerPhrase.add(new Phrase(new Chunk("\n")));
            headerPhrase.add(new Phrase(new Chunk("Trial Balance (Ledger Wise)", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD, BaseColor.BLACK))));
            headerPhrase.add(new Phrase(new Chunk("\n")));

            String locationName = (String) trialBalanceResult.get("location");

            headerPhrase.add(new Phrase(new Chunk("Location : " + locationName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));

            PdfPCell headercell = new PdfPCell(headerPhrase);
            headercell.setBorderWidthBottom(1);
            headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headercell.setBorderColor(BaseColor.WHITE);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            DateFormat dateFormatTime = new SimpleDateFormat("HH : mm : ss");

            Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK)));
            timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk("\n")));
            timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));

            PdfPCell timecell = new PdfPCell(timePhrase);
            timecell.setBorderWidthBottom(1);
            timecell.setPaddingTop(3.0f);
            timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            timecell.setBorderColor(BaseColor.WHITE);
            header.addCell(imagecell);
            header.addCell(headercell);
            header.addCell(timecell);

            document.add(header);

            float[] columnWidth = {0.7f, 1.2f, 1.5f, 1.5f, 1f, 1f, 1f, 1f, 1f};

            PdfPTable table1 = new PdfPTable(9); // 9 columns.
            table1.setWidthPercentage(100); //Width 100%

            table1.setWidths(columnWidth);

            ArrayList<TrialBalance> result = (ArrayList) trialBalanceResult
                    .get("result");

            if (!(result.size() > 0)) {
                PdfPCell cell0 = new PdfPCell(new Paragraph("No Records Exist", font2));
                cell0.setBorderColor(BaseColor.BLACK);
                cell0.setBackgroundColor(BaseColor.WHITE);
                cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell0.setBorder(Rectangle.BOX);
                cell0.setColspan(12);

                table1.addCell(cell0);
                document.add(table1);
                document.close();
                writer.close();
                return bos;
            }

            PdfPCell cell0 = new PdfPCell(new Paragraph("Date As On : " + trialBalanceResult.get("asOnDate"), font2));
            cell0.setBorderColor(BaseColor.BLACK);
            cell0.setBackgroundColor(BaseColor.WHITE);
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell0.setBorder(Rectangle.NO_BORDER);
            cell0.setColspan(9);

            PdfPCell cella = new PdfPCell(new Paragraph("Sl No", font2));
            cella.setBorderColor(BaseColor.BLACK);
            cella.setBackgroundColor(BaseColor.WHITE);
            cella.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cella.setBorder(Rectangle.BOX);

            PdfPCell cellb = new PdfPCell(new Paragraph("UnderGroup", font2));
            cellb.setBorderColor(BaseColor.BLACK);
            cellb.setBackgroundColor(BaseColor.WHITE);
            cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellb.setBorder(Rectangle.BOX);

            PdfPCell cellc = new PdfPCell(new Paragraph("Ledger Name", font2));
            cellc.setBorderColor(BaseColor.BLACK);
            cellc.setBackgroundColor(BaseColor.WHITE);
            cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellc.setBorder(Rectangle.BOX);

            PdfPCell celld = new PdfPCell(new Paragraph("Opening Balance", font2));
            celld.setBorderColor(BaseColor.BLACK);
            celld.setBackgroundColor(BaseColor.WHITE);
            celld.setHorizontalAlignment(Element.ALIGN_CENTER);
            celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celld.setBorder(Rectangle.BOX);
            celld.setColspan(2);

            PdfPCell celle = new PdfPCell(new Paragraph("Transaction", font2));
            celle.setBorderColor(BaseColor.BLACK);
            celle.setBackgroundColor(BaseColor.WHITE);
            celle.setHorizontalAlignment(Element.ALIGN_CENTER);
            celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celle.setBorder(Rectangle.BOX);
            celle.setColspan(2);

            PdfPCell cellf = new PdfPCell(new Paragraph("Closing Balance", font2));
            cellf.setBorderColor(BaseColor.BLACK);
            cellf.setBackgroundColor(BaseColor.WHITE);
            cellf.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellf.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellf.setBorder(Rectangle.BOX);
            cellf.setColspan(2);

            PdfPCell cella1 = new PdfPCell(new Paragraph("", font2));
            cella1.setBorderColor(BaseColor.BLACK);
            cella1.setBackgroundColor(BaseColor.WHITE);
            cella1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cella1.setBorder(Rectangle.LEFT);
            cella1.setColspan(3);

            PdfPCell cella2 = new PdfPCell(new Paragraph("Dr", font2));
            cella2.setBorderColor(BaseColor.BLACK);
            cella2.setBackgroundColor(BaseColor.WHITE);
            cella2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cella2.setBorder(Rectangle.BOX);

            PdfPCell cella3 = new PdfPCell(new Paragraph("Cr", font2));
            cella3.setBorderColor(BaseColor.BLACK);
            cella3.setBackgroundColor(BaseColor.WHITE);
            cella3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cella3.setBorder(Rectangle.BOX);

            PdfPCell cella4 = new PdfPCell(new Paragraph("Dr", font2));
            cella4.setBorderColor(BaseColor.BLACK);
            cella4.setBackgroundColor(BaseColor.WHITE);
            cella4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cella4.setBorder(Rectangle.BOX);

            PdfPCell cella5 = new PdfPCell(new Paragraph("Cr", font2));
            cella5.setBorderColor(BaseColor.BLACK);
            cella5.setBackgroundColor(BaseColor.WHITE);
            cella5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cella5.setBorder(Rectangle.BOX);

            PdfPCell cella6 = new PdfPCell(new Paragraph("Dr", font2));
            cella6.setBorderColor(BaseColor.BLACK);
            cella6.setBackgroundColor(BaseColor.WHITE);
            cella6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cella6.setBorder(Rectangle.BOX);

            PdfPCell cella7 = new PdfPCell(new Paragraph("Cr", font2));
            cella7.setBorderColor(BaseColor.BLACK);
            cella7.setBackgroundColor(BaseColor.WHITE);
            cella7.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella7.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cella7.setBorder(Rectangle.BOX);

            table1.addCell(cell0);
            table1.addCell(cella);
            table1.addCell(cellb);
            table1.addCell(cellc);
            table1.addCell(celld);
            table1.addCell(celle);
            table1.addCell(cellf);
            table1.addCell(cella1);
            table1.addCell(cella2);
            table1.addCell(cella3);
            table1.addCell(cella4);
            table1.addCell(cella5);
            table1.addCell(cella6);
            table1.addCell(cella7);

//            document.add(table1);
            int sNo = 1;

            //Set Column widths
            for (TrialBalance groupEntries : result) {

                String groupName = groupEntries.getGroupName();

                String SerNo = Integer.toString(sNo);

                cella = new PdfPCell(new Paragraph(SerNo, font3));
                cella.setBorderColor(BaseColor.BLACK);
                cella.setBackgroundColor(BaseColor.WHITE);
                cella.setHorizontalAlignment(Element.ALIGN_CENTER);
                cella.setVerticalAlignment(Element.ALIGN_CENTER);
                cella.setPaddingTop(15f);
                cella.setBorder(Rectangle.BOX);

                cellb = new PdfPCell(new Paragraph(groupName, font3));
                cellb.setBorderColor(BaseColor.BLACK);
                cellb.setBackgroundColor(BaseColor.WHITE);
                cellb.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellb.setVerticalAlignment(Element.ALIGN_CENTER);
                cellb.setBorder(Rectangle.BOX);
                cellb.setPaddingTop(15f);

                cellc = new PdfPCell(new Paragraph(groupEntries.getLedgerName(), font3));
                cellc.setBorderColor(BaseColor.BLACK);
                cellc.setBackgroundColor(BaseColor.WHITE);
                cellc.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellc.setVerticalAlignment(Element.ALIGN_CENTER);
                cellc.setBorder(Rectangle.BOX);
                cellc.setPaddingTop(15f);

                String text = groupEntries.getOpeningBalanceDrAmount();
                double value = Double.parseDouble(text);
                String doubToStr = roundTwoDecimalPoints(value);
//                celld = new PdfPCell(new Paragraph(groupEntries.getOpeningBalanceDrAmount(), font3));
                celld = new PdfPCell(new Paragraph(doubToStr, font3));
                celld.setBorderColor(BaseColor.BLACK);
                celld.setBackgroundColor(BaseColor.WHITE);
                celld.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celld.setVerticalAlignment(Element.ALIGN_CENTER);
                celld.setBorder(Rectangle.BOX);
                celld.setPaddingTop(15f);

                String text1 = groupEntries.getOpeningBalanceCrAmount();
                double value1 = Double.parseDouble(text1);
                String doubToStr1 = roundTwoDecimalPoints(value1);
//                celle = new PdfPCell(new Paragraph(groupEntries.getOpeningBalanceCrAmount(), font3));
                celle = new PdfPCell(new Paragraph(doubToStr1, font3));
                celle.setBorderColor(BaseColor.BLACK);
                celle.setBackgroundColor(BaseColor.WHITE);
                celle.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celle.setVerticalAlignment(Element.ALIGN_CENTER);
                celle.setBorder(Rectangle.BOX);
                celle.setPaddingTop(15f);

                String text2 = groupEntries.getTransactionDrAmount();
                double value2 = Double.parseDouble(text2);
                String doubToStr2 = roundTwoDecimalPoints(value2);

//                cellf = new PdfPCell(new Paragraph(groupEntries.getTransactionDrAmount(), font3));
                cellf = new PdfPCell(new Paragraph(doubToStr2, font3));
                cellf.setBorderColor(BaseColor.BLACK);
                cellf.setBackgroundColor(BaseColor.WHITE);
                cellf.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellf.setVerticalAlignment(Element.ALIGN_CENTER);
                cellf.setBorder(Rectangle.BOX);
                cellf.setPaddingTop(15f);

                String text3 = groupEntries.getTransactionCrAmount();
                double value3 = Double.parseDouble(text3);
                String doubToStr3 = roundTwoDecimalPoints(value3);
                PdfPCell cellg = new PdfPCell(new Paragraph(doubToStr3, font3));
//                PdfPCell cellg = new PdfPCell(new Paragraph(groupEntries.getTransactionCrAmount(), font3));
                cellg.setBorderColor(BaseColor.BLACK);
                cellg.setBackgroundColor(BaseColor.WHITE);
                cellg.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellg.setVerticalAlignment(Element.ALIGN_CENTER);
                cellg.setBorder(Rectangle.BOX);
                cellg.setPaddingTop(15f);

                String text4 = groupEntries.getClosingBalanceDrAmount();
                double value4 = Double.parseDouble(text4);
                String doubToStr4 = roundTwoDecimalPoints(value4);
                PdfPCell cellh = new PdfPCell(new Paragraph(doubToStr4, font3));
//                PdfPCell cellh = new PdfPCell(new Paragraph(groupEntries.getClosingBalanceDrAmount(), font3));
                cellh.setBorderColor(BaseColor.BLACK);
                cellh.setBackgroundColor(BaseColor.WHITE);
                cellh.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellh.setVerticalAlignment(Element.ALIGN_CENTER);
                cellh.setBorder(Rectangle.BOX);
                cellh.setPaddingTop(15f);

                String text5 = groupEntries.getClosingBalanceCrAmount();
                double value5 = Double.parseDouble(text5);
                String doubToStr5 = roundTwoDecimalPoints(value5);
                PdfPCell celli = new PdfPCell(new Paragraph(doubToStr5, font3));
//                PdfPCell celli = new PdfPCell(new Paragraph(groupEntries.getClosingBalanceCrAmount(), font3));
                celli.setBorderColor(BaseColor.BLACK);
                celli.setBackgroundColor(BaseColor.WHITE);
                celli.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celli.setVerticalAlignment(Element.ALIGN_CENTER);
                celli.setBorder(Rectangle.BOX);
                celli.setPaddingTop(15f);

                table1.addCell(cella);
                table1.addCell(cellb);
                table1.addCell(cellc);
                table1.addCell(celld);
                table1.addCell(celle);
                table1.addCell(cellf);
                table1.addCell(cellg);
                table1.addCell(cellh);
                table1.addCell(celli);

                sNo++;

            }
            document.add(table1);
            document.close();
            writer.close();
            return bos;

        } catch (Exception e) {
            return null;
        }//To change body of generated methods, choose Tools | Templates.

    }

    public ByteArrayOutputStream generateBudgetReport(HashMap<String, Object> budgetAtGlanceMap,
            String path) {

        try {
            Document document = new Document();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, bos);

            document.open();

            PdfPTable header = new PdfPTable(5);
            header.setWidthPercentage(100);
            header.setWidths(new int[]{40, 12, 12, 12, 12});
            header.setHeaderRows(3);

            Font font2 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);

            PdfPTable headerLogo = new PdfPTable(3);
            headerLogo.setWidthPercentage(100);
            headerLogo.setWidths(new int[]{50, 200, 50});

            PdfPCell imagecell = new PdfPCell();

            Image image1 = Image.getInstance(path + File.separator + "/images.jpg");
            image1.setAlignment(Image.LEFT);
            image1.scaleAbsolute(50.0f, 50.0f);

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

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            DateFormat dateFormatTime = new SimpleDateFormat("HH : mm : ss");

            PdfPCell headercell = new PdfPCell(headerPhrase);
            headercell.setBorderWidthBottom(1);
            headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headercell.setBorderColor(BaseColor.WHITE);

            Phrase timePhrase = new Phrase(new Chunk("Date ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK)));
            timePhrase.add(new Phrase(new Chunk(dateFormat.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk("\n")));
            timePhrase.add(new Phrase(new Chunk("Time ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));
            timePhrase.add(new Phrase(new Chunk(dateFormatTime.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK))));

            PdfPCell timecell = new PdfPCell(timePhrase);
            timecell.setBorderWidthBottom(1);
            timecell.setPaddingTop(3.0f);
            timecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            timecell.setBorderColor(BaseColor.WHITE);
            headerLogo.addCell(imagecell);
            headerLogo.addCell(headercell);
            headerLogo.addCell(timecell);

            document.add(headerLogo);

            PdfPCell cella = new PdfPCell(new Paragraph("SAURASHTRA UNIVERSITY RAJKOT", font2));
            cella.setBorderColor(BaseColor.BLACK);
            cella.setBackgroundColor(BaseColor.WHITE);
            cella.setHorizontalAlignment(Element.ALIGN_CENTER);
            cella.setVerticalAlignment(Element.ALIGN_CENTER);
            cella.setBorder(Rectangle.BOX);
            cella.setColspan(5);

            header.addCell(cella);
            String currentFinancialYear = budgetAtGlanceMap.get("financialYear") + "";

            FinancialYear financialYear = ((FinancialYear) (new Gson().fromJson(currentFinancialYear,
                    new TypeToken<FinancialYear>() {
            }.getType())));

            List<BudgetAtAGlance> budgetAtGlanceList = (List<BudgetAtAGlance>) budgetAtGlanceMap.get("result");

            if (financialYear != null && budgetAtGlanceList != null) {

                PdfPCell cellb = new PdfPCell(new Paragraph("BUDGET ESTIMATE FOR THE YEAR "
                        + financialYear.getFromDate().split("/")[2] + "-"
                        + financialYear.getToDate().split("/")[2] + " (IN LACS)", font2));
                cellb.setBorderColor(BaseColor.BLACK);
                cellb.setBackgroundColor(BaseColor.WHITE);
                cellb.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellb.setVerticalAlignment(Element.ALIGN_CENTER);
                cellb.setBorder(Rectangle.BOX);
                cellb.setColspan(5);

                PdfPCell cellc = new PdfPCell(new Paragraph("BUDGET AT A GLANCE", font2));
                cellc.setBorderColor(BaseColor.BLACK);
                cellc.setBackgroundColor(BaseColor.WHITE);
                cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellc.setVerticalAlignment(Element.ALIGN_CENTER);
                cellc.setBorder(Rectangle.BOX);
                cellc.setColspan(5);

                header.addCell(cellb);
                header.addCell(cellc);

                if (!(budgetAtGlanceList.size() > 0)) {
                    PdfPCell cell0 = new PdfPCell(new Paragraph("No Records Exist", font2));
                    cell0.setBorderColor(BaseColor.BLACK);
                    cell0.setBackgroundColor(BaseColor.WHITE);
                    cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell0.setBorder(Rectangle.BOX);
                    cell0.setColspan(5);

                    header.addCell(cell0);
                    document.add(header);

                    try {
                        document.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    writer.close();
                    return bos;
                }

                List<String> headerList = (List<String>) budgetAtGlanceMap.get("header");

                for (String headerStr : headerList) {

                    PdfPCell celld = new PdfPCell(new Paragraph(headerStr, font2));
                    celld.setBorderColor(BaseColor.BLACK);
                    celld.setBackgroundColor(BaseColor.WHITE);
                    celld.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celld.setVerticalAlignment(Element.ALIGN_CENTER);
                    celld.setBorder(Rectangle.BOX);

                    header.addCell(celld);
                }
                LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>>>> majorfinalData
                        = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>>>>();
                for (BudgetAtAGlance budgetAtAGlance : budgetAtGlanceList) {

//                    PdfPCell celle = new PdfPCell(new Paragraph(budgetAtAGlance.getBudgetHeadName(), font2));
//                    celle.setBorderColor(BaseColor.BLACK);
//                    celle.setBackgroundColor(BaseColor.WHITE);
//                    celle.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    celle.setVerticalAlignment(Element.ALIGN_CENTER);
//                    celle.setBorder(Rectangle.BOX);
//                    celle.setBorderWidthBottom(0f);
//                    celle.setBorderWidthTop(1f);
//                    celle.setColspan(5);
//                    header.addCell(celle);
                    HashMap<String, String> budgetCon = new HashMap<String, String>();
                    budgetCon.put("budgetHead", budgetAtAGlance.getBudgetHeadName());
                    budgetCon.put("status", "Active");
                    String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, budgetCon);
                    List<BudgetHeadMaster> budgetList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
                    }.getType());
                    if (budgetList != null) {
                        ArrayList<String> headsList = new ArrayList<String>();

                        String majorHead = null;
                        String subMajorHead = null;
                        String minorHead = null;
                        String subMinorHead = null;
                        Double actualAmt = 0d;
                        Double estimateAmt = 0d;
                        Double revisedAmt = 0d;
                        Double featureAmt = 0d;
                        BudgetHeadMaster budgetData = budgetList.get(0);
                        String govtBudgetHead = DBManager.getDbConnection().fetch(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, budgetData.getGovtBudgetHead());
                        List<GovtBudgetHead> govtDataList = new Gson().fromJson(govtBudgetHead, new TypeToken<List<GovtBudgetHead>>() {
                        }.getType());
                        GovtBudgetHead govtBudgetHeadData = govtDataList.get(0);

                        String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.MAJORHEAD_TABLE, govtBudgetHeadData.getMajorHead());
                        List<MajorHead> gaList = new Gson().fromJson(gaJson, new TypeToken<List<MajorHead>>() {
                        }.getType());
                        MajorHead gal = gaList.get(0);
                        majorHead = gal.getMajorHead();
                        String minorid = govtBudgetHeadData.getMinorHead();
                        String minorjson = DBManager.getDbConnection().fetch(ApplicationConstants.MINORHEAD_TABLE, minorid);
                        List<MinorHead> gaList1 = new Gson().fromJson(minorjson, new TypeToken<List<MinorHead>>() {
                        }.getType());
                        MinorHead gal1 = gaList1.get(0);
                        minorHead = gal1.getMinorHead();
                        String gaJson2 = DBManager.getDbConnection().fetch(ApplicationConstants.SUB_MAJORHEAD_TABLE, govtBudgetHeadData.getSubMajorHead());
                        List<SubMajorHead> gaList2 = new Gson().fromJson(gaJson2, new TypeToken<List<SubMajorHead>>() {
                        }.getType());
                        SubMajorHead gal2 = gaList2.get(0);
                        subMajorHead = gal2.getSubMajorHead();
                        String gaJson3 = DBManager.getDbConnection().fetch(ApplicationConstants.SUB_MINORHEAD_TABLE, govtBudgetHeadData.getSubMinorHead());
                        List<SubMinorHead> gaList3 = new Gson().fromJson(gaJson3, new TypeToken<List<SubMinorHead>>() {
                        }.getType());
                        SubMinorHead gal3 = gaList3.get(0);
                        subMinorHead = gal3.getSubMinorHead();

                        List<LedgerWiseEstimate> ledgerWiseEstimateList
                                = (List<LedgerWiseEstimate>) budgetAtAGlance.getLedgerWiseEstimate();

                        for (LedgerWiseEstimate ledgerWiseEstimate : ledgerWiseEstimateList) {
                            actualAmt = actualAmt + Double.parseDouble(ledgerWiseEstimate.getAcutalFor());
                            estimateAmt = estimateAmt + Double.parseDouble(ledgerWiseEstimate.getBudgetEstimate());
                            revisedAmt = revisedAmt + Double.parseDouble(ledgerWiseEstimate.getRevisedEstimate());
                            featureAmt = featureAmt + Double.parseDouble(ledgerWiseEstimate.getFutureBudgetEstimate());
                        }

                        if (majorfinalData.containsKey(majorHead)) {
                            LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>>> subMajorDataId = new LinkedHashMap();
                            subMajorDataId = majorfinalData.get(majorHead);
                            if (subMajorDataId.containsKey(subMajorHead)) {
                                LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> submajorData = new LinkedHashMap();
                                submajorData = subMajorDataId.get(subMajorHead);
                                LinkedHashMap<String, LinkedHashMap<String, Double>> MinordataId = new LinkedHashMap();
                                LinkedHashMap<String, LinkedHashMap<String, Double>> SUbMinordataId = new LinkedHashMap();
                                MinordataId = submajorData.get(minorHead);
                                SUbMinordataId = submajorData.get(subMinorHead);
                                if (MinordataId != null && MinordataId.containsKey(minorHead)) {
                                    Double TotactualAmt = 0d;
                                    Double TotestimateAmt = 0d;
                                    Double TotrevisedAmt = 0d;
                                    Double TotfeatureAmt = 0d;

                                    LinkedHashMap<String, Double> minorData = new LinkedHashMap();
                                    minorData = MinordataId.get(minorHead);
                                    TotactualAmt = minorData.get("Actual") + actualAmt;
                                    TotestimateAmt = minorData.get("EstimateBefore") + estimateAmt;
                                    TotrevisedAmt = minorData.get("Revised") + revisedAmt;
                                    TotfeatureAmt = minorData.get("EstimateAfter") + featureAmt;
                                    minorData.put("Actual", TotactualAmt);
                                    minorData.put("EstimateBefore", TotestimateAmt);
                                    minorData.put("Revised", TotrevisedAmt);
                                    minorData.put("EstimateAfter", TotfeatureAmt);
                                    MinordataId.put(minorHead, minorData);
                                    submajorData.put(minorHead, MinordataId);
                                    subMajorDataId.put(subMajorHead, submajorData);
                                    majorfinalData.put(majorHead, subMajorDataId);

                                } else {
                                    LinkedHashMap<String, Double> minorData = new LinkedHashMap();
                                    minorData.put("Actual", actualAmt);
                                    minorData.put("EstimateBefore", estimateAmt);
                                    minorData.put("Revised", revisedAmt);
                                    minorData.put("EstimateAfter", featureAmt);
                                    LinkedHashMap<String, LinkedHashMap<String, Double>> MinordataIdnew = new LinkedHashMap();
                                    MinordataIdnew.put(minorHead, minorData);
                                    submajorData.put(minorHead, MinordataIdnew);
                                    subMajorDataId.put(subMajorHead, submajorData);
                                    majorfinalData.put(majorHead, subMajorDataId);
                                }
                                if (SUbMinordataId != null && SUbMinordataId.containsKey(subMinorHead)) {

                                    Double TotactualAmt = 0d;
                                    Double TotestimateAmt = 0d;
                                    Double TotrevisedAmt = 0d;
                                    Double TotfeatureAmt = 0d;

                                    LinkedHashMap<String, Double> subminorData = new LinkedHashMap();
                                    subminorData = SUbMinordataId.get(subMinorHead);
                                    TotactualAmt = subminorData.get("Actual") + actualAmt;
                                    TotestimateAmt = subminorData.get("EstimateBefore") + estimateAmt;
                                    TotrevisedAmt = subminorData.get("Revised") + revisedAmt;
                                    TotfeatureAmt = subminorData.get("EstimateAfter") + featureAmt;
                                    subminorData.put("Actual", TotactualAmt);
                                    subminorData.put("EstimateBefore", TotestimateAmt);
                                    subminorData.put("Revised", TotrevisedAmt);
                                    subminorData.put("EstimateAfter", TotfeatureAmt);
                                    subminorData.put("isSubMinor", 1d);
                                    SUbMinordataId.put(subMinorHead, subminorData);
                                    submajorData.put(subMinorHead, SUbMinordataId);
                                    subMajorDataId.put(subMajorHead, submajorData);
                                    majorfinalData.put(majorHead, subMajorDataId);

                                } else {
                                    LinkedHashMap<String, Double> subminorData = new LinkedHashMap();
                                    subminorData.put("Actual", actualAmt);
                                    subminorData.put("EstimateBefore", estimateAmt);
                                    subminorData.put("Revised", revisedAmt);
                                    subminorData.put("EstimateAfter", featureAmt);
                                    subminorData.put("isSubMinor", 1d);

                                    LinkedHashMap<String, LinkedHashMap<String, Double>> SUbMinordataIdnew = new LinkedHashMap();
                                    SUbMinordataIdnew.put(subMinorHead, subminorData);
                                    submajorData.put(subMinorHead, SUbMinordataIdnew);
                                    subMajorDataId.put(subMajorHead, submajorData);
                                    majorfinalData.put(majorHead, subMajorDataId);
                                }

                            } else {
                                LinkedHashMap<String, Double> minorData = new LinkedHashMap();
                                LinkedHashMap<String, LinkedHashMap<String, Double>> minorDataId = new LinkedHashMap();
                                minorData.put("Actual", actualAmt);
                                minorData.put("EstimateBefore", estimateAmt);
                                minorData.put("Revised", revisedAmt);
                                minorData.put("EstimateAfter", featureAmt);
                                minorDataId.put(minorHead, minorData);

                                LinkedHashMap<String, Double> subminorData = new LinkedHashMap();
                                LinkedHashMap<String, LinkedHashMap<String, Double>> subminorDataId = new LinkedHashMap();
                                subminorData.put("Actual", actualAmt);
                                subminorData.put("EstimateBefore", estimateAmt);
                                subminorData.put("Revised", revisedAmt);
                                subminorData.put("EstimateAfter", featureAmt);
                                subminorDataId.put(subMinorHead, subminorData);

                                LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> submajorData = new LinkedHashMap();
                                //     HashMap<String, HashMap<String, HashMap<String, HashMap<String, Double>>>> submajorDataId = new HashMap();
                                submajorData.put(minorHead, minorDataId);
                                submajorData.put(subMinorHead, subminorDataId);
                                subMajorDataId.put(subMajorHead, submajorData);
                                majorfinalData.put(majorHead, subMajorDataId);
                            }

                        } else {
                            LinkedHashMap<String, Double> minorData = new LinkedHashMap();
                            LinkedHashMap<String, LinkedHashMap<String, Double>> minorDataId = new LinkedHashMap();
                            minorData.put("Actual", actualAmt);
                            minorData.put("EstimateBefore", estimateAmt);
                            minorData.put("Revised", revisedAmt);
                            minorData.put("EstimateAfter", featureAmt);
                            minorDataId.put(minorHead, minorData);

                            LinkedHashMap<String, Double> subminorData = new LinkedHashMap();
                            LinkedHashMap<String, LinkedHashMap<String, Double>> subminorDataId = new LinkedHashMap();
                            subminorData.put("Actual", actualAmt);
                            subminorData.put("EstimateBefore", estimateAmt);
                            subminorData.put("Revised", revisedAmt);
                            subminorData.put("EstimateAfter", featureAmt);
                            subminorData.put("isSubMinor", 1d);
                            subminorDataId.put(subMinorHead, subminorData);

                            LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> submajorData = new LinkedHashMap();
                            LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>>> submajorDataId = new LinkedHashMap();
                            submajorData.put(minorHead, minorDataId);
                            submajorData.put(subMinorHead, subminorDataId);
                            submajorDataId.put(subMajorHead, submajorData);

                            majorfinalData.put(majorHead, submajorDataId);

                        }

                    }

//                    List<LedgerWiseEstimate> ledgerWiseEstimateList
//                            = (List<LedgerWiseEstimate>) budgetAtAGlance.getLedgerWiseEstimate();
//
//                    for (LedgerWiseEstimate ledgerWiseEstimate : ledgerWiseEstimateList) {
//
//                        PdfPCell celll = new PdfPCell(new Paragraph(ledgerWiseEstimate.getLedgerName() + "", font2));
//                        celll.setBorderColor(BaseColor.BLACK);
//                        celll.setBackgroundColor(BaseColor.WHITE);
//                        celll.setHorizontalAlignment(Element.ALIGN_LEFT);
//                        celll.setVerticalAlignment(Element.ALIGN_CENTER);
//                        celll.setBorder(Rectangle.BOX);
//
//                        header.addCell(celll);
//
//                        PdfPCell cellh = new PdfPCell(new Paragraph(ledgerWiseEstimate.getAcutalFor() + "", font2));
//                        cellh.setBorderColor(BaseColor.BLACK);
//                        cellh.setBackgroundColor(BaseColor.WHITE);
//                        cellh.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        cellh.setVerticalAlignment(Element.ALIGN_CENTER);
//                        cellh.setBorder(Rectangle.BOX);
//
//                        header.addCell(cellh);
//
//                        PdfPCell celli = new PdfPCell(new Paragraph(ledgerWiseEstimate.getBudgetEstimate() + "", font2));
//                        celli.setBorderColor(BaseColor.BLACK);
//                        celli.setBackgroundColor(BaseColor.WHITE);
//                        celli.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        celli.setVerticalAlignment(Element.ALIGN_CENTER);
//                        celli.setBorder(Rectangle.BOX);
//
//                        header.addCell(celli);
//
//                        PdfPCell cellj = new PdfPCell(new Paragraph(ledgerWiseEstimate.getRevisedEstimate() + "", font2));
//                        cellj.setBorderColor(BaseColor.BLACK);
//                        cellj.setBackgroundColor(BaseColor.WHITE);
//                        cellj.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        cellj.setVerticalAlignment(Element.ALIGN_CENTER);
//                        cellj.setBorder(Rectangle.BOX);
//
//                        header.addCell(cellj);
//
//                        PdfPCell cellk = new PdfPCell(new Paragraph(ledgerWiseEstimate.getFutureBudgetEstimate() + "", font2));
//                        cellk.setBorderColor(BaseColor.BLACK);
//                        cellk.setBackgroundColor(BaseColor.WHITE);
//                        cellk.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                        cellk.setVerticalAlignment(Element.ALIGN_CENTER);
//                        cellk.setBorder(Rectangle.BOX);
//
//                        header.addCell(cellk);
//
//                    }
                }

                if (majorfinalData != null) {
                    Set<String> keys = majorfinalData.keySet();
                    for (String key : keys) {
                        PdfPCell celle = new PdfPCell(new Paragraph(key, font2));
                        celle.setBorderColor(BaseColor.BLACK);
                        celle.setBackgroundColor(BaseColor.WHITE);
                        celle.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celle.setVerticalAlignment(Element.ALIGN_CENTER);
                        celle.setBorder(Rectangle.BOX);
                        celle.setBorderWidthBottom(0f);
                        celle.setBorderWidthTop(1f);
                        celle.setColspan(5);
                        header.addCell(celle);

                        //
                        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>>> subMajorDataId = new LinkedHashMap();
                        subMajorDataId = majorfinalData.get(key);
                        Set<String> submjaorkeys = subMajorDataId.keySet();
                        for (String subMajorkey : submjaorkeys) {
                            PdfPCell cellee = new PdfPCell(new Paragraph(subMajorkey, font2));
                            cellee.setBorderColor(BaseColor.BLACK);
                            cellee.setBackgroundColor(BaseColor.WHITE);
                            cellee.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cellee.setVerticalAlignment(Element.ALIGN_LEFT);
                            cellee.setBorder(Rectangle.BOX);
                            cellee.setBorderWidthBottom(0f);
                            cellee.setBorderWidthTop(1f);
                            cellee.setPaddingLeft(50f);
                            cellee.setColspan(5);
                            header.addCell(cellee);

                            //
                            LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> MinorandSuMinordata = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>>();

                            MinorandSuMinordata = subMajorDataId.get(subMajorkey);
                            Set<String> minorkeys = MinorandSuMinordata.keySet();
                            for (String minorkey : minorkeys) {
                                PdfPCell celll = new PdfPCell(new Paragraph(minorkey + "", font2));
                                celll.setBorderColor(BaseColor.BLACK);
                                celll.setBackgroundColor(BaseColor.WHITE);
                                celll.setHorizontalAlignment(Element.ALIGN_LEFT);
                                celll.setVerticalAlignment(Element.ALIGN_CENTER);
                                celll.setBorder(Rectangle.BOX);

                                LinkedHashMap<String, LinkedHashMap<String, Double>> minorDataId = new LinkedHashMap();
                                minorDataId = MinorandSuMinordata.get(minorkey);
                                HashMap<String, Double> minorData = new HashMap();
                                minorData = minorDataId.get(minorkey);

                                for (LinkedHashMap<String, Double> map : minorDataId.values()) {
                                    if (map.containsKey("isSubMinor")) {
                                        celll.setIndent(15.0f);
                                    }
                                }
                                header.addCell(celll);
                                PdfPCell cellh = new PdfPCell(new Paragraph(String.format("%.2f", minorData.get("Actual")) + "", font2));
                                cellh.setBorderColor(BaseColor.BLACK);
                                cellh.setBackgroundColor(BaseColor.WHITE);
                                cellh.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cellh.setVerticalAlignment(Element.ALIGN_CENTER);
                                cellh.setBorder(Rectangle.BOX);

                                header.addCell(cellh);

                                PdfPCell celli = new PdfPCell(new Paragraph(String.format("%.2f", minorData.get("EstimateBefore")) + "", font2));
                                celli.setBorderColor(BaseColor.BLACK);
                                celli.setBackgroundColor(BaseColor.WHITE);
                                celli.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                celli.setVerticalAlignment(Element.ALIGN_CENTER);
                                celli.setBorder(Rectangle.BOX);

                                header.addCell(celli);

                                PdfPCell cellj = new PdfPCell(new Paragraph(String.format("%.2f", minorData.get("Revised")) + "", font2));
                                cellj.setBorderColor(BaseColor.BLACK);
                                cellj.setBackgroundColor(BaseColor.WHITE);
                                cellj.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cellj.setVerticalAlignment(Element.ALIGN_CENTER);
                                cellj.setBorder(Rectangle.BOX);

                                header.addCell(cellj);

                                PdfPCell cellk = new PdfPCell(new Paragraph(String.format("%.2f", minorData.get("EstimateAfter")) + "", font2));
                                cellk.setBorderColor(BaseColor.BLACK);
                                cellk.setBackgroundColor(BaseColor.WHITE);
                                cellk.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cellk.setVerticalAlignment(Element.ALIGN_CENTER);
                                cellk.setBorder(Rectangle.BOX);

                                header.addCell(cellk);

                            }

                        }

                    }
                }

            } else {
                header = new PdfPTable(1);
                header.setWidthPercentage(100);
                PdfPCell cellxyz = new PdfPCell(new Paragraph("No Records Exist", font2));
                cellxyz.setBorderColor(BaseColor.BLACK);
                cellxyz.setBackgroundColor(BaseColor.WHITE);
                cellxyz.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellxyz.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellxyz.setBorder(Rectangle.BOX);

                header.addCell(cellxyz);
                document.add(header);
                document.close();
                writer.close();
                return bos;
            }

            document.add(header);

            document.close();
            writer.close();

            return bos;
        } catch (Exception ex) {
            return null;
        }

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
