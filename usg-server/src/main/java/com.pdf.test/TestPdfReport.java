
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import static java.awt.Color.white;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.poi.ss.usermodel.Font;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class TestPdfReport {

    public static void main(String[] args) throws Exception {

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\user\\Desktop\\salaryreport\\sample.pdf"));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        CMYKColor magentaColor = new CMYKColor(0.f, 1.f, 0.f, 0.f);
        canvas.setColorStroke(magentaColor);
        canvas.moveTo(36,36);
        canvas.lineTo(36,806);
        canvas.lineTo(559,36);
        canvas.lineTo(559,806);
        canvas.closePathStroke();

        document.close();
    }
}
