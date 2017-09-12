/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.service;

import com.accure.finance.dto.LedgerCategory;
import com.accure.finance.manager.LedgerManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;

/**
 *
 * @author deepak2310
 */
public class LedgerCategoryService extends HttpServlet {

    Logger logger = Logger.getLogger(LedgerCategoryService.class);
    String privilege = ApplicationConstants.PV_LEDGER_CATEGORY_CREATE;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
                User currentUser = (User) session.getAttribute("user");
                boolean authorized = UserManager.checkUserPrivilege(currentUser, privilege);
                if (authorized) {
                    String ledgerCategoryJson = request.getParameter("ledgerCategoryJson");
                    String loginUserId = request.getParameter("userid");

                    Type type = new TypeToken<LedgerCategory>() {
                    }.getType();
                    LedgerCategory ledgerCategory = new Gson().fromJson(ledgerCategoryJson, type);

                    String ledgerCategoryId = new LedgerManager().createLedgerCategory(ledgerCategory, loginUserId);
                    if (ledgerCategoryId.equalsIgnoreCase(ApplicationConstants.DUPLICATE_MESSAGE)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_DUPLICATE);
                        out.write(new Gson().toJson(ledgerCategoryId));
                    } else if (ledgerCategoryId.equalsIgnoreCase(ApplicationConstants.UNAUTHORIZED_ACCESS)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
                        out.write(new Gson().toJson(ledgerCategoryId));
                    } else if (ledgerCategoryId.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        out.write(new Gson().toJson(ledgerCategoryId));
                    } else if (ledgerCategoryId.equalsIgnoreCase(ApplicationConstants.FAIL)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(ledgerCategoryId));
                    }
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
                    logger.info(Common.getLogMsg("LedgerCategoryService", ApplicationConstants.FAIL, ApplicationConstants.UNAUTHORIZED_ACCESS));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_EXCEPTION, ApplicationConstants.LEDGER_CATEGORY_CREATION_FAILED, "")));
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("LedgerCategoryService", ApplicationConstants.ERROR, stack.toString()));
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
