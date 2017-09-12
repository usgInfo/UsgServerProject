/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.finance.dto.BankReconcilation;
import com.accure.finance.manager.BankReconcilationManager;
import com.accure.leave.dto.LeaveTransaction;
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
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.FileOutputStream;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class LeaveTransactionReportPdfManager {

    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");

    public static void main(String[] args) throws Exception {
        BankReconcilationManager manager = new BankReconcilationManager();
        List<BankReconcilation> bankDetails = manager.fetchAllBankReconcilation();

    }

    public ByteArrayOutputStream createPdfforLeaveReport(List<LeaveTransaction> employeeList,
            String fromDate, String toDate, String path) {
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
            headerPhrase.add(new Phrase("Leave Transaction Details from " + fromDate + " to " + toDate + "", font1));
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

            float[] columnWidths = {1f, 1f, 1f, 1f, 1f, 1f};
            float[] columnWidth = {1f, 1f, 1f, 1f, 1f};

            PdfPTable table1 = new PdfPTable(5); // 3 columns.
            table1.setWidthPercentage(100); //Width 100%

            table1.setWidths(columnWidth);

            PdfPCell cella = new PdfPCell(new Paragraph("Leave Type", font2));
            cella.setBorderColor(BaseColor.BLACK);
            cella.setBackgroundColor(BaseColor.WHITE);
            cella.setHorizontalAlignment(Element.ALIGN_LEFT);
            cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cella.setBorder(Rectangle.NO_BORDER);
            cella.setBorderWidthBottom(1f);
            cella.setBorderWidthTop(1f);

            PdfPCell cellb = new PdfPCell(new Paragraph("Dated", font2));
            cellb.setBorderColor(BaseColor.BLACK);
            cellb.setBackgroundColor(BaseColor.WHITE);
            cellb.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellb.setBorder(Rectangle.NO_BORDER);
            cellb.setBorderWidthBottom(1f);
            cellb.setBorderWidthTop(1f);

            PdfPCell cellc = new PdfPCell(new Paragraph("Day", font2));
            cellc.setBorderColor(BaseColor.BLACK);
            cellc.setBackgroundColor(BaseColor.WHITE);
            cellc.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellc.setBorder(Rectangle.NO_BORDER);
            cellc.setBorderWidthBottom(1f);
            cellc.setBorderWidthTop(1f);

            PdfPCell celld = new PdfPCell(new Paragraph("Half Day", font2));
            celld.setBorderColor(BaseColor.BLACK);
            celld.setBackgroundColor(BaseColor.WHITE);
            celld.setHorizontalAlignment(Element.ALIGN_LEFT);
            celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celld.setBorder(Rectangle.NO_BORDER);
            celld.setBorderWidthBottom(1f);
            celld.setBorderWidthTop(1f);

            PdfPCell celle = new PdfPCell(new Paragraph("Remarks", font2));
            celle.setBorderColor(BaseColor.BLACK);
            celle.setBackgroundColor(BaseColor.WHITE);
            celle.setHorizontalAlignment(Element.ALIGN_LEFT);
            celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celle.setBorder(Rectangle.NO_BORDER);
            celle.setBorderWidthBottom(1f);
            celle.setBorderWidthTop(1f);
            table1.addCell(cella);
            table1.addCell(cellb);
            table1.addCell(cellc);
            table1.addCell(celld);
            table1.addCell(celle);
            document.add(table1);

            PdfPTable table = new PdfPTable(6); // 3 columns.
            table.setWidthPercentage(100); //Width 100%

            //Set Column widths
            table.setWidths(columnWidths);

            PdfPCell cell1 = new PdfPCell(new Paragraph("S.No", font2));
            cell1.setBorderColor(BaseColor.BLACK);
            cell1.setBackgroundColor(BaseColor.WHITE);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell2 = new PdfPCell(new Paragraph("Employee Code(M)", font2));
            cell2.setBorderColor(BaseColor.BLACK);
            cell2.setBackgroundColor(BaseColor.WHITE);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell3 = new PdfPCell(new Paragraph("Employee Name", font2));
            cell3.setBorderColor(BaseColor.BLACK);
            cell3.setBackgroundColor(BaseColor.WHITE);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell4 = new PdfPCell(new Paragraph("Location", font2));
            cell4.setBorderColor(BaseColor.BLACK);
            cell4.setBackgroundColor(BaseColor.WHITE);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell5 = new PdfPCell(new Paragraph("Department", font2));
            cell5.setBorderColor(BaseColor.BLACK);
            cell5.setBackgroundColor(BaseColor.WHITE);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell6 = new PdfPCell(new Paragraph("Designation", font2));
            cell6.setBorderColor(BaseColor.BLACK);
            cell6.setBackgroundColor(BaseColor.WHITE);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            document.add(table);
            int sNo = 1;
            PdfPTable table3 = new PdfPTable(6); // 3 columns.
            table3.setWidthPercentage(100); //Width 100%
            //table.setSpacingBefore(10f); //Space before table
            // table.setSpacingAfter(10f); //Space after table
            //Set Column widths
            table3.setWidths(columnWidths);
            for (int i = 0; i < employeeList.size(); i++) {
                table3.flushContent();
                table3.setWidthPercentage(100); //Width 100%

                String SerNo = Integer.toString(sNo);

                LeaveTransaction lt = employeeList.get(i);
                cell1 = new PdfPCell(new Paragraph(SerNo, font3));
                cell1.setBorderColor(BaseColor.BLACK);
                cell1.setBackgroundColor(BaseColor.WHITE);
//        cell1.setPaddingLeft(15);cell1.setPaddingBottom(15);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell1.setPaddingTop(15f);
                cell1.setBorder(Rectangle.NO_BORDER);

                cell2 = new PdfPCell(new Paragraph(lt.getEmployeeCodeM(), font3));
                cell2.setBorderColor(BaseColor.BLACK);
                cell2.setBackgroundColor(BaseColor.WHITE);
//        cell2.setPaddingLeft(15);cell2.setPaddingBottom(15);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setPaddingTop(15f);

                cell3 = new PdfPCell(new Paragraph(lt.getEmployeeName(), font3));
                cell3.setBorderColor(BaseColor.BLACK);
                cell3.setBackgroundColor(BaseColor.WHITE);
//        cell3.setPaddingLeft(15);cell3.setPaddingBottom(15);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell3.setBorder(Rectangle.NO_BORDER);
                cell3.setPaddingTop(15f);

                cell4 = new PdfPCell(new Paragraph(lt.getLocation(), font3));
                cell4.setBorderColor(BaseColor.BLACK);
                cell4.setBackgroundColor(BaseColor.WHITE);
//        cell4.setPaddingLeft(15);cell4.setPaddingBottom(15);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell4.setBorder(Rectangle.NO_BORDER);
                cell4.setPaddingTop(15f);

                cell5 = new PdfPCell(new Paragraph(lt.getDepartment(), font3));
                cell5.setBorderColor(BaseColor.BLACK);
                cell5.setBackgroundColor(BaseColor.WHITE);
//        cell5.setPaddingLeft(15);cell5.setPaddingBottom(15);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell5.setBorder(Rectangle.NO_BORDER);
                cell5.setPaddingTop(15f);

                cell6 = new PdfPCell(new Paragraph(lt.getDesignation(), font3));
                cell6.setBorderColor(BaseColor.BLACK);
                cell6.setBackgroundColor(BaseColor.WHITE);
//        cell6.setPaddingLeft(15);cell6.setPaddingBottom(15);
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell6.setBorder(Rectangle.NO_BORDER);
                cell6.setPaddingTop(15f);

                table3.addCell(cell1);
                table3.addCell(cell2);
                table3.addCell(cell3);
                table3.addCell(cell4);
                table3.addCell(cell5);
                table3.addCell(cell6);
                document.add(table3);
                LineSeparator line11 = new LineSeparator();
                line11.setOffset(-1);
                document.add(line11);
                String fromDateStr = lt.getFromDate();
                String toDateStr = lt.getToDate();
                SimpleDateFormat dates = new SimpleDateFormat("dd/mm/yyyy");
                Date date1 = (Date) dates.parse(fromDateStr);
                Date date2 = (Date) dates.parse(toDateStr);
                long frDate = saveInMilliSecond(fromDateStr);
                long tDate = saveInMilliSecond(toDateStr);
                long diffInMilis = tDate - frDate;
                long diffInDays = diffInMilis / (24 * 60 * 60 * 1000);
                diffInDays = diffInDays + 1;
                long ff = 1;
                Paragraph p = new Paragraph(sNo + " " + lt.getLeaveTypeDescription()
                        + " for " + diffInDays + " day(s) from " + fromDateStr + " to " + toDateStr + "", font2);
                document.add(p);

                DottedLineSeparator dottedline = new DottedLineSeparator();
                dottedline.setPercentage(100);
                Chunk linebreak = new Chunk(dottedline);
                document.add(linebreak);

                PdfPTable table2 = new PdfPTable(5); // 3 columns.
                table2.setWidthPercentage(100); //Width 100%
//                table2.//Width 100%
                // table1.setSpacingBefore(10f); //Space before table
                // table1.setSpacingAfter(10f); //Space after table
                table2.setWidths(columnWidth);
                // DottedLineCell app = new DottedLineCell();
                Calendar c = Calendar.getInstance();
//                while (ff <= diffInDays) {
                List<Map<String, String>> dateRemarksAndHalfday = lt.getDateRemarksAndIsHalfDay();
                for (Map<String, String> map : dateRemarksAndHalfday) {
                    dottedline = new DottedLineSeparator();
                    dottedline.setPercentage(59500f / 523f);
                    cella = new PdfPCell(new Paragraph(lt.getLeaveTypeDescription(), font3));
                    cella.setBorderColor(BaseColor.BLACK);
                    cella.setBackgroundColor(BaseColor.WHITE);
                    cella.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cella.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                  cella.setCellEvent(app.new DottedCell());
                    cella.setBorder(PdfPCell.NO_BORDER);
                    cella.setBorderWidthBottom(.5f);
                        Date demoDate = new Date();
                        Date demoDate1 = new Date();
                        String date11=map.get("date");
                        String[] d = date11.split("/");
                        String newDate=d[0]+"-"+d[1]+"-"+d[2];
                    c.setTime(format1.parse(newDate));
//                    //System.out.println("-----------------------" + date1.toLocaleString());
//                    //System.out.println(date1.getDate() + "-" + date1.getMonth() + "-" + date1.getYear());

                    date1 = (Date) c.getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
                    String day = formatter.format(date1);
                    long utilDate = c.getTimeInMillis();
                    String utilDateinStr = MilliSecondToDDMMYYY(Long.toString(utilDate));
//                    //System.out.println(utilDateinStr + "********");

                    cellb = new PdfPCell(new Paragraph(utilDateinStr, font3));
                    cellb.setBorderColor(BaseColor.BLACK);
                    cellb.setBackgroundColor(BaseColor.WHITE);
                    cellb.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellb.setBorder(Rectangle.NO_BORDER);
                    cellb.setBorderWidthBottom(.5f);
//                    cellb.setBorderWidthTop(1f);
                    c.add(Calendar.DATE, 1);
                    date1 = (Date) c.getTime();
                    ff++;
                    cellc = new PdfPCell(new Paragraph(day, font3));
                    cellc.setBorderColor(BaseColor.BLACK);
                    cellc.setBackgroundColor(BaseColor.WHITE);
                    //cellc.setPaddingLeft(15);
                    cellc.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellc.setBorder(Rectangle.NO_BORDER);
                    cellc.setBorderWidthBottom(.5f);
//                    cellc.setBorderWidthTop(1f);

                    String isHalfDay = map.get("isHalfDay");
                    if (isHalfDay != null && isHalfDay.equalsIgnoreCase("true")) {
                        isHalfDay = "Yes";
                    } else {
                        isHalfDay = "No";
                    }
                    celld = new PdfPCell(new Paragraph(isHalfDay, font3));
                    celld.setBorderColor(BaseColor.BLACK);
                    celld.setBackgroundColor(BaseColor.WHITE);
                    //celld.setPaddingLeft(15);
                    celld.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celld.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celld.setBorder(Rectangle.NO_BORDER);
                    celld.setBorderWidthBottom(.5f);
//                    celld.setBorderWidthTop(1f);

                    celle = new PdfPCell(new Paragraph(map.get("reason"), font3));
                    celle.setBorderColor(BaseColor.BLACK);
                    celle.setBackgroundColor(BaseColor.WHITE);
                    // celle.setPaddingLeft(15);
                    celle.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celle.setBorder(Rectangle.NO_BORDER);
                    celle.setBorderWidthBottom(.5f);
//                    celle.setBorderWidthTop(1f);

                    table2.addCell(cella);
                    table2.addCell(cellb);
                    table2.addCell(cellc);
                    table2.addCell(celld);
                    table2.addCell(celle);

                    DottedLineSeparator dottedlineaa = new DottedLineSeparator();
                    dottedlineaa.setPercentage(100);
                    Chunk linebreakaa = new Chunk(dottedlineaa);
                    document.add(linebreakaa);

                }
                document.add(table2);
                sNo++;
            }
            document.close();
            writer.close();
            return bos;

        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }//To change body of generated methods, choose Tools | Templates.
    }

    public Long saveInMilliSecond(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = str;
        java.util.Date date = sdf.parse(dateInString);
        return date.getTime();
    }

    public String MilliSecondToDDMMYYY(String str) {
        long foo = Long.parseLong(str);
        Date date = new Date(foo);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

}
