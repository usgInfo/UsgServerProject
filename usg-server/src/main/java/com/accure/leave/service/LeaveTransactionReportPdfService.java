/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.service;

import com.accure.hrms.service.EmployeeSearchService;
import com.accure.leave.dto.LeaveTransaction;
import com.accure.leave.manager.LeaveTransactionReportPdfManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.List;
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
 * @author user
 */
public class LeaveTransactionReportPdfService extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    Logger logger = Logger.getLogger(EmployeeSearchService.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        try {

            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
                String leaveTransReportJson = URLDecoder.decode(request.getParameter("leaveTransReportJson"));
                String fromDate = request.getParameter("fromDateVal");
                String toDate = request.getParameter("toDateVal");
                ServletContext servletContext = getServletContext();
                String path = File.separator + "usg" + File.separator + "images";
                String contextPath = servletContext.getRealPath("/" + path);
                List<LeaveTransaction> employeeList = new Gson().fromJson(leaveTransReportJson,
                        new TypeToken<List<LeaveTransaction>>() {
                }.getType());
                ByteArrayOutputStream baos = new LeaveTransactionReportPdfManager().
                        createPdfforLeaveReport(employeeList, fromDate, toDate,
                                contextPath);

                if (baos != null) {
                    response.setContentType("application/pdf;charset=UTF-8;name='LeaveTransactionDetails.pdf'");
                    PDFMergerUtility ut = new PDFMergerUtility();
                    ut.addSource(new ByteArrayInputStream(baos.toByteArray()));
                    ut.setDestinationStream(out);
                    ut.mergeDocuments();
                    ut.setDestinationFileName("LeaveTransactionDetails.pdf");
                    ut.getDestinationStream().flush();
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    logger.info(Common.getLogMsg("LeaveTransactionReportPdfService",
                            ApplicationConstants.VIEW, ApplicationConstants.SUCCESS));
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                    logger.info(Common.getLogMsg("LeaveTransactionReportPdfService",
                            ApplicationConstants.VIEW, ApplicationConstants.FAIL));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                logger.info(Common.getLogMsg("LeaveTransactionReportPdfService",
                        ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("LeaveTransactionReportPdfService",
                    ApplicationConstants.ERROR, stack.toString()));

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
