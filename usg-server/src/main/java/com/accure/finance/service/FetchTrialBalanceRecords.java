/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.service;

import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.manager.TrialBalanceManager;
import com.accure.finance.reportformat.CreatePDFFile;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TreeMap;
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
public class FetchTrialBalanceRecords extends HttpServlet {

    Logger logger = Logger.getLogger(FetchTrialBalanceRecords.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
                String trialBalanceCriteria = request.getParameter("trialBalanceCriteria");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");
                String suppressZero = request.getParameter("suppressZero");
                PaymentVoucher paymentVoucher = new Gson().fromJson(trialBalanceCriteria, new TypeToken<PaymentVoucher>() {
                }.getType());
                TreeMap<String, Object> trialBalanceResult = new TrialBalanceManager().getTrialBalanceReocrds(
                        paymentVoucher.getDDO(), paymentVoucher.getLocation(), paymentVoucher.getFundType(),
                        paymentVoucher.getBudgetHead(), suppressZero, fromDate, toDate);
                ServletContext servletContext = getServletContext();
                String path = File.separator + "usg" + File.separator + "images";
                String contextPath = servletContext.getRealPath("/" + path);
                ByteArrayOutputStream bos = new CreatePDFFile().generateTrialBalanceReport(trialBalanceResult, contextPath);
                if (bos != null) {
                    response.setContentType("application/pdf;charset=UTF-8;name='TrialBalanceReport.pdf'");
                    PDFMergerUtility ut = new PDFMergerUtility();
                    ut.addSource(new ByteArrayInputStream(bos.toByteArray()));
                    ut.setDestinationStream(out);
                    ut.mergeDocuments();
                    ut.setDestinationFileName("TrialBalanceReport.pdf");
                    ut.getDestinationStream().flush();
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    logger.info(Common.getLogMsg("TrialBalanceReport",
                            ApplicationConstants.VIEW, ApplicationConstants.SUCCESS));
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                    out.print(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, "TrialBalanceReport Failed", "")));
                    logger.info(Common.getLogMsg("TrialBalanceReport", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                out.print(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, "TrialBalanceReport Failed", "")));
                logger.info(Common.getLogMsg("TrialBalanceReport", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
            }
        } catch (JsonSyntaxException ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("TrialBalanceReport",
                    ApplicationConstants.ERROR, stack.toString()));
        } catch (IOException ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("TrialBalanceReport",
                    ApplicationConstants.ERROR, stack.toString()));
        } catch (COSVisitorException ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("TrialBalanceReport",
                    ApplicationConstants.ERROR, stack.toString()));
        } finally {
            out.flush();
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
