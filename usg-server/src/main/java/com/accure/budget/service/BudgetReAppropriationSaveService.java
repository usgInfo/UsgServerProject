/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.service;

import com.accure.budget.dto.CreateBudgetExpense;
import com.accure.budget.dto.DeptWiseExpBudgetAllocation;
import com.accure.budget.manager.BudgetReAppropriationManager;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
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
 * @author Asif
 */
public class BudgetReAppropriationSaveService extends HttpServlet {

    Logger logger = Logger.getLogger(BudgetReAppropriationSaveService.class);
    String privilege = ApplicationConstants.PV_BUDGET_REAPPROPRIATION_VIEW;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {

                User currentUser = (User) session.getAttribute("user");
//                boolean authorized = UserManager.checkUserPrivilege(currentUser, privilege);
//                if (authorized) {
                    String Obj = request.getParameter("obj");
                    String loginUserId = request.getParameter("userId");
                    User user = new UserManager().fetch(loginUserId);
                    String userName = user.getFname() + " " + user.getLname();
                    List<DeptWiseExpBudgetAllocation> list = new Gson().fromJson(Obj, new TypeToken<List<DeptWiseExpBudgetAllocation>>() {
                    }.getType());
                    int count = list.size();
                    int resultCount = 0;
//                for (Iterator<ExpenseBudgetApproval> iterator = list.iterator(); iterator.hasNext();) {
//                    ExpenseBudgetApproval next = iterator.next();
//                    if (new BudgetReAppropriationManager().updateAppropriationAmount(next, userName)) {
//                        resultCount++;
//                    }
//                }
                    for (DeptWiseExpBudgetAllocation expenseBudgetApproval : list) {
                        boolean result1 = new BudgetReAppropriationManager().updateAppropriationAmount(expenseBudgetApproval, userName);
                        resultCount++;
                    }
                    String result = "false";
                    if (resultCount == count) {
                        result = "true";
                    }
                    if (result != null && !result.isEmpty()) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        out.write(new Gson().toJson(result));
                        logger.info(Common.getLogMsg("BudgetReAppropriationSaveService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                        logger.info(Common.getLogMsg("BudgetReAppropriationSaveService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                    }
//                } else {
//                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
//                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
//                    logger.info(Common.getLogMsg("BudgetReAppropriationSaveService", ApplicationConstants.FAIL, ApplicationConstants.UNAUTHORIZED_ACCESS));
//                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("BudgetReAppropriationSaveService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_EXCEPTION));
            logger.info(Common.getLogMsg("BudgetReAppropriationSaveService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.HTTP_STATUS_EXCEPTION));
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
