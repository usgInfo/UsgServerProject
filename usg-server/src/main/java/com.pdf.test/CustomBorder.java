package com.pdf;

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
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;

public class CustomBorder {

    public static void main(String[] args) throws DocumentException, FileNotFoundException, BadElementException, IOException {
        CustomBorder cs = new CustomBorder();
        cs.generateSalaryReport();
    }

    public void generateSalaryReport() throws DocumentException, FileNotFoundException, BadElementException, IOException {

        Hashtable<String, String> deductions = new Hashtable<String, String>();
        Hashtable<String, String> Earnings = new Hashtable<String, String>();
        deductions.put("Prof.Tax", "0.0");
        deductions.put("Income Tax", "0.0");
        deductions.put("GPF", "0.0");
        deductions.put("FBS", "0.0");
        deductions.put("WFS", "0.0");

        Earnings.put("Basic Pay", "0.0");
        Earnings.put("GP", "0.0");
        Earnings.put("DA", "0.0");
        Earnings.put("HRA", "0.0");
        Earnings.put("CCA", "0.0");

        Document document = new Document();

        PdfPTable outerTable = new PdfPTable(1);

        outerTable.setTotalWidth(555f);
        outerTable.setLockedWidth(true);

//        outerTable.setTotalWidth((pageSize.getWidth() - document.leftMargin()
//                - document.rightMargin()) * outerTable.getWidthPercentage() / 100);
        outerTable.setWidthPercentage(100);

        PdfPCell outercell = new PdfPCell();
//        outercell.setBorderColor(BaseColor.WHITE);
//        outercell.setBorder(Rectangle.NO_BORDER);
//        outercell.setPaddingTop(40.0f);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\user\\Desktop\\salaryreport\\sample.pdf"));
        document.open();

        float[] columnWidth = {1f};

        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{20, 200});

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance("C:\\Users\\user\\Desktop\\Capture.png");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(50.0f, 50.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(0.5f);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);
        imagecell.setBorderColorBottom(BaseColor.BLACK);

        Phrase headerPhrase = new Phrase(new Chunk("RAJASTHAN TECHNICAL UNIVERSITY, KOTA", FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("        Akelgarh,Rawatbhata Road,Kota", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("    Salary Statement for the Month of SEPTEMBER 2015", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

        PdfPCell headercell = new PdfPCell(headerPhrase);

        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);
        headercell.setBorderWidthBottom(0.5f);
        headercell.setBorderColorBottom(BaseColor.BLACK);
        header.addCell(imagecell);
        header.addCell(headercell);

        outercell.addElement(header);

        // outerTable.addCell(header);
        PdfPTable table8 = new PdfPTable(2);
        table8.setWidthPercentage(100.0f);
        table8.setSpacingBefore(2f);
        table8.setSpacingAfter(2f);

        Phrase datePhrase81 = new Phrase(new Chunk("Emp No:", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        datePhrase81.add(new Phrase(new Chunk("12345", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
        PdfPCell cell81 = new PdfPCell(datePhrase81);
        cell81.setBorder(Rectangle.NO_BORDER);

        Phrase datePhrase82 = new Phrase(new Chunk("Days:", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        datePhrase82.add(new Phrase(new Chunk("30", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
        PdfPCell cell82 = new PdfPCell(datePhrase82);
        cell82.setBorder(Rectangle.NO_BORDER);

        Phrase datePhrase83 = new Phrase(new Chunk("Name:", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        datePhrase83.add(new Phrase(new Chunk("Manonmani A", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
        PdfPCell cell83 = new PdfPCell(datePhrase83);
        cell83.setBorder(Rectangle.NO_BORDER);

        Phrase datePhrase84 = new Phrase(new Chunk("PAN No:", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        datePhrase84.add(new Phrase(new Chunk("136526CZXXSD", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
        PdfPCell cell84 = new PdfPCell(datePhrase84);
        cell84.setBorder(Rectangle.NO_BORDER);

        Phrase datePhrase85 = new Phrase(new Chunk("Designation:", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        datePhrase85.add(new Phrase(new Chunk("Software Engineer", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
        PdfPCell cell85 = new PdfPCell(datePhrase85);
        cell85.setBorder(Rectangle.NO_BORDER);

        Phrase datePhrase86 = new Phrase(new Chunk("Department:", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        datePhrase86.add(new Phrase(new Chunk("Computer", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
        PdfPCell cell86 = new PdfPCell(datePhrase86);
        cell86.setBorder(Rectangle.NO_BORDER);

        Phrase datePhrase87 = new Phrase(new Chunk("Salary Type :", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        datePhrase87.add(new Phrase(new Chunk("Pay Sacle", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
        PdfPCell cell87 = new PdfPCell(datePhrase87);
        cell87.setBorder(Rectangle.NO_BORDER);
        cell87.setBorderWidthBottom(0.5f);
        cell87.setBorderColorBottom(BaseColor.BLACK);
        cell87.setPaddingBottom(4.0f);

        Phrase datePhrase88 = new Phrase(new Chunk("CPF/NPS/GPF:", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        datePhrase88.add(new Phrase(new Chunk("37456DFGFT", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK))));
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
        table8.addCell(cell86);
        table8.addCell(cell87);
        table8.addCell(cell88);
        table8.spacingAfter();

        outercell.addElement(table8);

        PdfPTable salaryDetailsTable = new PdfPTable(2);
        salaryDetailsTable.setWidthPercentage(100.0f);
        salaryDetailsTable.setSpacingBefore(2f);
        salaryDetailsTable.setSpacingAfter(2f);

        PdfPCell earningsDetailsTable = new PdfPCell(new Phrase("EMOLUMENTS", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        earningsDetailsTable.setBorder(Rectangle.NO_BORDER);
        earningsDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        earningsDetailsTable.setBorderColor(BaseColor.WHITE);
        earningsDetailsTable.setBorderWidthBottom(0.5f);
        earningsDetailsTable.setBorderColorBottom(BaseColor.BLACK);
        earningsDetailsTable.setPaddingBottom(5f);

        PdfPCell deductionDetailsTable = new PdfPCell(new Phrase("DEDUCTIONS", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        deductionDetailsTable.setBorder(Rectangle.NO_BORDER);
        deductionDetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        deductionDetailsTable.setBorderColor(BaseColor.WHITE);
        deductionDetailsTable.setBorderWidthBottom(0.5f);
        deductionDetailsTable.setBorderColorBottom(BaseColor.BLACK);
        deductionDetailsTable.setPaddingBottom(5f);

        salaryDetailsTable.addCell(earningsDetailsTable);
        salaryDetailsTable.addCell(deductionDetailsTable);

        outercell.addElement(salaryDetailsTable);

        PdfPTable paragraph = new PdfPTable(1);
        paragraph.setWidthPercentage(100.0f);

        PdfPCell paragraphCell = new PdfPCell();
        paragraphCell.setBorder(Rectangle.NO_BORDER);
        paragraphCell.setBorderColor(BaseColor.WHITE);
        paragraph.addCell(paragraphCell);

        PdfPTable earningTable = new PdfPTable(2);

        earningTable.setWidthPercentage(100.0f);
        paragraphCell = new PdfPCell();
        PdfPTable firstTable = new PdfPTable(2);
        paragraphCell.setBorder(Rectangle.NO_BORDER);
        paragraphCell.setBorderColor(BaseColor.WHITE);
        Set<String> keys = Earnings.keySet();
        PdfPCell keycell = null;
        PdfPCell valuecell = null;
        for (String key : keys) {
            firstTable.setWidthPercentage(100.0f);
            keycell = new PdfPCell(new Phrase((key), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            keycell.setBorder(Rectangle.NO_BORDER);
            keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
            keycell.setBorderColor(BaseColor.WHITE);
            firstTable.addCell(keycell);
            valuecell = new PdfPCell(new Phrase(Earnings.get(key), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            valuecell.setBorder(Rectangle.NO_BORDER);
            valuecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            valuecell.setBorderColor(BaseColor.WHITE);
            firstTable.addCell(valuecell);
        }

        keycell = new PdfPCell(new Phrase("Total Earnings", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        keycell.setBorder(Rectangle.NO_BORDER);
        keycell.setHorizontalAlignment(Element.ALIGN_LEFT);
        keycell.setBorderColor(BaseColor.WHITE);
        firstTable.addCell(keycell);
        valuecell = new PdfPCell(new Phrase("1234567", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
        valuecell.setBorder(Rectangle.NO_BORDER);
        valuecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        valuecell.setBorderColor(BaseColor.WHITE);
        firstTable.addCell(valuecell);

        paragraphCell.addElement(firstTable);
        earningTable.addCell(paragraphCell);

        paragraphCell = new PdfPCell();
        paragraphCell.setBorder(PdfPCell.NO_BORDER);
        paragraphCell.setBorderColor(BaseColor.WHITE);

        PdfPTable secondTable = new PdfPTable(2);
        secondTable.setWidthPercentage(100.0f);
        Set<String> deductionkey = deductions.keySet();
        PdfPCell deductionKeycell = null;
        PdfPCell deductionValuecell = null;
        for (String key : deductionkey) {
            deductionKeycell = new PdfPCell(new Phrase((key), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            deductionKeycell.setBorder(Rectangle.NO_BORDER);
            deductionKeycell.setHorizontalAlignment(Element.ALIGN_LEFT);
            deductionKeycell.setBorderColor(BaseColor.WHITE);
            secondTable.addCell(deductionKeycell);
            deductionValuecell = new PdfPCell(new Phrase(deductions.get(key), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            deductionValuecell.setBorder(Rectangle.NO_BORDER);
            deductionValuecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            deductionValuecell.setBorderColor(BaseColor.WHITE);
            secondTable.addCell(deductionValuecell);

        }

        deductionKeycell = new PdfPCell(new Phrase("Total Deductions", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
        deductionKeycell.setBorder(Rectangle.NO_BORDER);
        deductionKeycell.setHorizontalAlignment(Element.ALIGN_LEFT);
        deductionKeycell.setBorderColor(BaseColor.WHITE);
        secondTable.addCell(deductionKeycell);
        deductionValuecell = new PdfPCell(new Phrase("26345364", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
        deductionValuecell.setBorder(Rectangle.NO_BORDER);
        deductionValuecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        deductionValuecell.setBorderColor(BaseColor.WHITE);
        secondTable.addCell(deductionValuecell);

        paragraphCell.addElement(secondTable);
        earningTable.addCell(paragraphCell);

        paragraphCell = new PdfPCell(earningTable);

        paragraphCell.setBorder(Rectangle.NO_BORDER);
        paragraphCell.setBorderColor(BaseColor.WHITE);
        

        paragraph.addCell(paragraphCell);

        outercell.addElement(paragraph);

        DottedLineSeparator separator = new DottedLineSeparator();
        separator.setPercentage(59500f / 523f);
        Chunk linebreak = new Chunk(separator);

        outercell.addElement(linebreak);

        float[] columnWidths = {3f, 1f};

        PdfPTable table11 = new PdfPTable(2);
        table11.setWidthPercentage(100);
        table11.setWidths(columnWidths);

        PdfPCell transferString = new PdfPCell(new Phrase("* Transferrred to Bank Account No.6009000400000568", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
        earningsDetailsTable.setBorder(Rectangle.NO_BORDER);
        transferString.setBorderColor(BaseColor.WHITE);

        PdfPCell transferAmount = new PdfPCell(new Phrase("Rs.21790.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
        deductionDetailsTable.setBorder(Rectangle.NO_BORDER);
        transferAmount.setBorderColor(BaseColor.WHITE);

        table11.addCell(transferString);
        table11.addCell(transferAmount);

        PdfPCell contributionTableAmount = new PdfPCell(new Phrase("* University Contribution towards NPS  Rs.0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
        contributionTableAmount.setBorder(Rectangle.NO_BORDER);
        contributionTableAmount.setBorderColor(BaseColor.WHITE);

        PdfPCell contributionTableAmount1 = new PdfPCell();
        contributionTableAmount1.setBorder(Rectangle.NO_BORDER);
        contributionTableAmount1.setBorderColor(BaseColor.WHITE);
        table11.addCell(contributionTableAmount);
        table11.addCell(contributionTableAmount1);

        PdfPCell netPayableCell = new PdfPCell(new Phrase("  Net Payable (in words): Rs. TWENTY-ONE THOUSAND SEVEN HUNDRED NINETY ONLY", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK)));
        netPayableCell.setBorder(Rectangle.NO_BORDER);
        netPayableCell.setBorderColor(BaseColor.WHITE);
        netPayableCell.setPaddingRight(5.0f);
        netPayableCell.setBorderWidthBottom(0.5f);
        netPayableCell.setBorderColorBottom(BaseColor.BLACK);
        netPayableCell.setPaddingBottom(5f);

        PdfPCell netPayableCell1 = new PdfPCell();
        netPayableCell1.setBorder(Rectangle.NO_BORDER);
        netPayableCell1.setBorderColor(BaseColor.WHITE);
        netPayableCell1.setBorderWidthBottom(0.5f);
        netPayableCell1.setBorderColorBottom(BaseColor.BLACK);
        netPayableCell.setPaddingBottom(5f);

        paragraphCell.setPaddingBottom(5f);
        table11.addCell(netPayableCell);
        table11.addCell(netPayableCell1);

        outercell.addElement(table11);

        PdfPTable SignatureTable = new PdfPTable(1);
        SignatureTable.setWidthPercentage(100);

        PdfPCell signatureCell = new PdfPCell(new Phrase("It is a computer generated slip and doesn't require a signature.", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK)));
        signatureCell.setBorder(Rectangle.NO_BORDER);
        signatureCell.setBorderColor(BaseColor.WHITE);
        signatureCell.setPaddingRight(5.0f);
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
        writer.close();
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
