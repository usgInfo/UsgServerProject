/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.service;

import com.accure.budget.dto.ConsolidateExpenseBudget;
import com.accure.budget.manager.ConsolidateExpenseBudgetManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
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
public class ConsolidateExpenseService extends HttpServlet {

    Logger logger = Logger.getLogger(ConsolidateExpenseService.class);
    String privilege = ApplicationConstants.PV_BUDGET_CONSOLIDATED_EXPENSES_CREATE;

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
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {

                String objJson = request.getParameter("objJson");
                String loginUserId = request.getParameter("userid");
                User user = new UserManager().fetch(loginUserId);
                String userName = user.getFname() + " " + user.getLname();
                String finyear = request.getParameter("financialYear");
                String fundType = request.getParameter("fundType");
                String sector = request.getParameter("sector");
                String budgetType = request.getParameter("budgetType");
                //
                List<ConsolidateExpenseBudget> list = new Gson().fromJson(objJson, new TypeToken<List<ConsolidateExpenseBudget>>() {
                }.getType());
                int count = list.size();
                String result = "false";
                int resultCount = 0;
                String newSlno = new ConsolidateExpenseBudgetManager().getSlNumber(finyear, fundType, sector, budgetType);

                for (Iterator<ConsolidateExpenseBudget> iterator = list.iterator(); iterator.hasNext();) {
                    ConsolidateExpenseBudget next = iterator.next();
                    if (new ConsolidateExpenseBudgetManager().checkDuplicate(next).equalsIgnoreCase(ApplicationConstants.DUPLICATE_MESSAGE)) {
                        result = ApplicationConstants.DUPLICATE_MESSAGE;
                    } else {
                        if (new ConsolidateExpenseBudgetManager().save(next, loginUserId, newSlno) != "") {
                            ArrayList<String> li = (ArrayList<String>) next.getIncomeBudgetIdList();
                            for (Iterator<String> iterator1 = li.iterator(); iterator1.hasNext();) {
                                String next1 = iterator1.next();
                                new ConsolidateExpenseBudgetManager().updateIsConsolidateFlagOfExpenseBudget(next1, userName);
                            }
                            resultCount++;
                        }
                    }
                }

                if (resultCount == count) {
                    result = "true";
                }
                if (result != null && !result.isEmpty()) {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    out.write(new Gson().toJson(result));
                    logger.info(Common.getLogMsg("ConsolidateExpenseBudgetService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                    logger.info(Common.getLogMsg("ConsolidateExpenseBudgetService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                }

            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("ConsolidateExpenseBudgetService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_EXCEPTION));
            logger.info(Common.getLogMsg("ConsolidateExpenseBudgetService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.HTTP_STATUS_EXCEPTION));
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
