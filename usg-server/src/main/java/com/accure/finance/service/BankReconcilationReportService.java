/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.service;

import com.accure.finance.manager.BankReconcilationReportManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.pdfbox.util.PDFMergerUtility;

/**
 *
 * @author User
 */
public class BankReconcilationReportService extends HttpServlet {

    Logger logger = Logger.getLogger(BankReconcilationReportService.class);
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Content-Disposition", "inline; filename=\"BankReconcilationReport.pdf\"");
        response.setContentType("application/pdf;charset=UTF-8;name=\"BankReconcilationReport.pdf\"");
        ServletOutputStream out = response.getOutputStream();

        try {

            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            String finYear = request.getParameter("finYear");
            String fin = URLDecoder.decode(request.getParameter("fin"));
            String voucherdata = URLDecoder.decode(request.getParameter("voucherList"));
            String ledger = request.getParameter("ledger");
            String fromDateStr = request.getParameter("fromDateStr");
            String toDateStr = request.getParameter("toDateStr");
            String ddo = request.getParameter("ddo");
            String location = request.getParameter("location");
            ServletContext servletContext = getServletContext();
            String path=File.separator+"usg"+File.separator+"images";
            String contextPath = servletContext.getRealPath("/"+path);
//            ByteArrayOutputStream baos = new BankReconcilationReportManager().bankReconcilationReport(fromDate, toDate, voucherdata, ledger, contextPath, fin, fromDateStr, ddo, location, finYear);
            ByteArrayOutputStream baos = new BankReconcilationReportManager().bankReconcilationReport(fromDateStr, fromDate, voucherdata, ledger, contextPath, fin, fromDateStr, ddo, location, finYear);
            if (baos != null) {
                PDFMergerUtility ut = new PDFMergerUtility();
                ut.addSource(new ByteArrayInputStream(baos.toByteArray()));
                ut.setDestinationStream(out);
                ut.mergeDocuments();
                ut.setDestinationFileName("BankReconcilationReport.pdf");
                ut.getDestinationStream().flush();
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("BankReconcilationReportService", ApplicationConstants.ERROR, stack.toString()));
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
        processRequest(request, response);
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
        processRequest(request, response);
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
