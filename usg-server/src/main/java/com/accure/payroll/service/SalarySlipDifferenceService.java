/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.service;

import com.accure.payroll.manager.SalarySlipRegisterReportManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

/**
 *
 * @author user
 */
public class SalarySlipDifferenceService extends HttpServlet {

    Logger logger = Logger.getLogger(SalarySlipReport.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DocumentException, COSVisitorException {
//        response.setContentType("text/json;charset=UTF-8");
        //   PrintWriter out = response.getWriter();
        response.setContentType("application/pdf;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
//        OutputStream out = null;
        try {

            HttpSession session = request.getSession(false);
//            if (SessionManager.checkUserSession(session)) {
            ServletContext servletContext = getServletContext();
            String path = File.separator + "usg" + File.separator + "images";
            String contextPath = servletContext.getRealPath("/"+path);

            String id = request.getParameter("id");
            String ddo = request.getParameter("ddo");
            String month = request.getParameter("month");
            String year = request.getParameter("year");
            String fin = URLDecoder.decode(request.getParameter("fin"));
            String processType=URLDecoder.decode(request.getParameter("processType"));
           //System.out.println("processType"+processType);
            ByteArrayOutputStream outputJson = new SalarySlipRegisterReportManager().generationPdfDifferenceSalarySlip(id, ddo, month, year, contextPath,fin,processType);
            if (outputJson != null) {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                ByteArrayOutputStream baos = outputJson;
                byte b[] = baos.toByteArray();

                PDFMergerUtility ut = new PDFMergerUtility();
                ut.addSource(new ByteArrayInputStream(baos.toByteArray()));
                ut.setDestinationStream(out);
                ut.mergeDocuments();
                ut.setDestinationFileName("SalaryReport.pdf");
                ut.getDestinationStream().flush();
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Document document = new Document();
                PdfWriter.getInstance(document, bos);
                document.open();
                Paragraph paragraph = new Paragraph();
                paragraph.add("NO Data");
                document.add(paragraph);
                PDFMergerUtility ut = new PDFMergerUtility();
                ut.addSource(new ByteArrayInputStream(bos.toByteArray()));
                ut.setDestinationStream(out);
                ut.mergeDocuments();
                ut.setDestinationFileName("SalaryReport.pdf");
                ut.getDestinationStream().flush();
            }

        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, bos);
            document.open();
            Paragraph paragraph = new Paragraph();
            paragraph.add("NO Data");
            document.add(paragraph);
            PDFMergerUtility ut = new PDFMergerUtility();
            ut.addSource(new ByteArrayInputStream(bos.toByteArray()));
            ut.setDestinationStream(out);
            ut.mergeDocuments();
            ut.setDestinationFileName("SalaryReport.pdf");
            ut.getDestinationStream().flush();

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DocumentException ex) {
            java.util.logging.Logger.getLogger(SalarySlipDifferenceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (COSVisitorException ex) {
            java.util.logging.Logger.getLogger(SalarySlipDifferenceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DocumentException ex) {
            java.util.logging.Logger.getLogger(SalarySlipDifferenceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (COSVisitorException ex) {
            java.util.logging.Logger.getLogger(SalarySlipDifferenceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
