
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class ListOfEmployeeReport {

    public static void main(String[] args) throws DocumentException, FileNotFoundException, BadElementException, IOException {
        ListOfEmployeeReport cs = new ListOfEmployeeReport();
        cs.generateSalaryReport();
    }

    public void generateSalaryReport() throws DocumentException, FileNotFoundException, BadElementException, IOException {

        Document document = new Document();

        String[] headerNames = {"SNO", "Name", "Designation", "Department", "Joining Date", "Address", "ddo", "SalaryType"};
        String[] headerValues={"1","mano","Software Engineer","CS","23/10/2016","Tamilnadu","mano@gmail.com","PayScale"};
        PdfPTable outerTable = new PdfPTable(1);

        outerTable.setTotalWidth(555f);
        outerTable.setLockedWidth(true);

//        outerTable.setTotalWidth((pageSize.getWidth() - document.leftMargin()
//                - document.rightMargin()) * outerTable.getWidthPercentage() / 100);
        outerTable.setWidthPercentage(100);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\user\\Desktop\\salaryreport\\sample.pdf"));
        document.open();

        float[] columnWidth = {1f};

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{20, 200, 40});

        PdfPCell imagecell = new PdfPCell();

        Image image1 = Image.getInstance("C:\\Users\\user\\Desktop\\Capture.png");
        image1.setAlignment(Image.LEFT);
        image1.scaleAbsolute(50.0f, 50.0f);

        imagecell.addElement(image1);
        imagecell.setBorderWidthBottom(1);
        imagecell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imagecell.setBorderColor(BaseColor.WHITE);

        Phrase headerPhrase = new Phrase(new Chunk("RAJASTHAN TECHNICAL UNIVERSITY, KOTA", FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD, BaseColor.BLACK)));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("        Akelgarh,Rawatbhata Road,Kota", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));
        headerPhrase.add(new Phrase(new Chunk("    Salary Statement for the Month of SEPTEMBER 2015", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK))));
        headerPhrase.add(new Phrase(new Chunk("\n")));

        PdfPCell headercell = new PdfPCell(headerPhrase);
        headercell.setBorderWidthBottom(1);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setBorderColor(BaseColor.WHITE);

        Phrase timePhrase = new Phrase(new Chunk("Date:23/09/2016", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)));
        timePhrase.add(new Phrase(new Chunk("\n")));
        timePhrase.add(new Phrase(new Chunk("Time:12:00AM", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK))));
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


        PdfPTable employeeDetailsTable = new PdfPTable(8);
        employeeDetailsTable.setWidthPercentage(100);
        PdfPCell employeecell = null;
        employeeDetailsTable.setHeaderRows(1);

        for (int i = 0; i < headerNames.length; i++) {
            String headerName = headerNames[i];
            employeecell = new PdfPCell(new Phrase(headerName,FontFactory.getFont(FontFactory.TIMES_ITALIC, 10, Font.BOLD, BaseColor.BLACK)));
            employeeDetailsTable.addCell(employeecell);
        }
        for(int k=0;k<40;k++){
         for (int i = 0; i < headerValues.length; i++) {
            String headerValue = headerValues[i];
            employeecell = new PdfPCell(new Phrase(headerValue,FontFactory.getFont(FontFactory.TIMES_ITALIC, 10, Font.NORMAL, BaseColor.BLACK)));
          
            employeeDetailsTable.addCell(employeecell);
        }
        }

        outercell.addElement(employeeDetailsTable);
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
