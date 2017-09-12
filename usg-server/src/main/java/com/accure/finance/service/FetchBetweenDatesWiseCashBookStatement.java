/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.service;

import com.accure.finance.manager.BetweenDatesCashBookManager;
import com.accure.finance.manager.CashBookReportManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
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
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.pdfbox.util.PDFMergerUtility;

/**
 *
 * @author accure
 */
public class FetchBetweenDatesWiseCashBookStatement extends HttpServlet {

    Logger logger = Logger.getLogger(FetchBetweenDatesWiseCashBookStatement.class);
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/pdf;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
//        OutputStream out = null;
        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            String fin = URLDecoder.decode(request.getParameter("fin"));
//            String voucherdata = request.getParameter("voucherList");
           String voucherdata = URLDecoder.decode(request.getParameter("voucherList"));
            String ledger = request.getParameter("ledger");
           ServletContext servletContext = getServletContext();
            String path=File.separator+"usg"+File.separator+"images";
            String contextPath = servletContext.getRealPath("/"+path);
            //ByteArrayOutputStream outputJson = new QuarterListManager().quarterListPdfStatement(quarterCategory,condition);
            ByteArrayOutputStream outputJson = new BetweenDatesCashBookManager().betweenDatesCashBookPdfStatement(fromDate, toDate, voucherdata, ledger, contextPath, fin);

            if (outputJson != null) {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                ByteArrayOutputStream baos = outputJson;
                byte b[] = baos.toByteArray();

                PDFMergerUtility ut = new PDFMergerUtility();
                ut.addSource(new ByteArrayInputStream(baos.toByteArray()));
                ut.setDestinationStream(out);
                ut.mergeDocuments();
                ut.setDestinationFileName("CashBookReportStatement.pdf");
                ut.getDestinationStream().flush();
                //System.out.println(ut);
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
            }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("FetchBetweenDatesWiseCashBookStatement", ApplicationConstants.ERROR, stack.toString()));
            // out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_EXCEPTION));
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
