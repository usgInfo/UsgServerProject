/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.service;

import com.accure.payroll.manager.LoanOrderStatementManager;
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
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.pdfbox.util.PDFMergerUtility;

/**
 *
 * @author Upendra
 */
public class FetchEmpDetailsLoanOrderStatement extends HttpServlet {

    Logger logger = Logger.getLogger(FetchEmpDetailsLoanOrderStatement.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setContentType("text/json;charset=UTF-8");
        //   PrintWriter out = response.getWriter();
//        response.setContentType("application/pdf;charset=UTF-8");
        response.setContentType("application/pdf;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
//        OutputStream out = null;
        try {

//            HttpSession session = request.getSession(false);
            ServletContext servletContext = getServletContext();
            String path = File.separator + "usg" + File.separator + "images";
            String contextPath = servletContext.getRealPath("/" + path);
            String order = request.getParameter("orderid");
            String fin = URLDecoder.decode(request.getParameter("fin"));
            ByteArrayOutputStream baos = new LoanOrderStatementManager().loanOrderPdfStatement(order, contextPath, fin);
            if (baos != null) {
                PDFMergerUtility ut = new PDFMergerUtility();
                ut.addSource(new ByteArrayInputStream(baos.toByteArray()));
                ut.setDestinationStream(out);
                ut.mergeDocuments();
                ut.setDestinationFileName("LoanOrderStatement.pdf");
                ut.getDestinationStream().flush();
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
            }
//            } else {
//                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
//            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("FetchEmpDetailsLoanOrderStatement", ApplicationConstants.ERROR, stack.toString()));
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
