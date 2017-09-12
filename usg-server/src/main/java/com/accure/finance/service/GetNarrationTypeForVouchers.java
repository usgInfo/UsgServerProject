/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.service;

import com.accure.finance.dto.BudgetNature;
import com.accure.finance.manager.BudgetNatureManager;
import com.accure.finance.manager.PaymentVoucherManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class GetNarrationTypeForVouchers extends HttpServlet {

    Logger logger = Logger.getLogger(GetNarrationTypeForVouchers.class);

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
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */

            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
                String voucher = request.getParameter("VocherName");
                String result = "";
                
                if (voucher.equalsIgnoreCase(ApplicationConstants.RECEIPT_VOUCHERS)) {
                    result = new PaymentVoucherManager().getNarrationTypeForReceiptVoucher();
                    //System.out.println("resiept voucher"+result);
                } else if (voucher.equalsIgnoreCase(ApplicationConstants.PAYMENT_VOUCHERS)) {
                    result = new PaymentVoucherManager().getNarrationTypeForPaymentVoucher();
                    //System.out.println("payment voucher"+result);
                } else if (voucher.equalsIgnoreCase(ApplicationConstants.JOURNAL_VOUCHERS)) {
                    result = new PaymentVoucherManager().getNarrationTypeForJournalVoucher();
                    //System.out.println("journal voucher"+result);
                } else if (voucher.equalsIgnoreCase(ApplicationConstants.CONTRA_VOUCHERS)) {
                    result = new PaymentVoucherManager().getNarrationTypeForContraVoucher();
                    //System.out.println("contra voucher"+result);
                }

                if (result != null) {
                    out.write(result);
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    logger.info(Common.getLogMsg("GetNarrationTypeForVouchers", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                } else if (result == null || result.isEmpty()) {
                    out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_NODATA, ApplicationConstants.NO_DATA_FOUND)));
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.BUDGET_NATURE_LIST_FAILED, result)));
                    logger.info(Common.getLogMsg("GetNarrationTypeForVouchers", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_EXCEPTION, ApplicationConstants.BUDGET_NATURE_LIST_FAILED, false)));
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("GetNarrationTypeForVouchers", ApplicationConstants.ERROR, stack.toString()));
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
