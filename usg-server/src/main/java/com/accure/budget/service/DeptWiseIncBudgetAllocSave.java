/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.service;

import com.accure.budget.dto.DeptWiseIncBudgetAllocation;
import com.accure.budget.manager.DepartmentWiseIncAlloManager;
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

/**
 *
 * @author user
 */
public class DeptWiseIncBudgetAllocSave extends HttpServlet {

    Logger logger = Logger.getLogger(HeadwiseIncomeBudgetSaveService.class);
    String privilege = ApplicationConstants.PV_LOCATION_INCOME_BUDGET_ALLOCATION_CREATE;

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

            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {

//                User currentUser = (User) session.getAttribute("user");
//                boolean authorized = UserManager.checkUserPrivilege(currentUser, privilege);
//                if (authorized) {
                String searchObj = request.getParameter("objJson");
                // String sanction = request.getParameter("employeeDetails");
                String loginUserId = request.getParameter("userid");
                List<DeptWiseIncBudgetAllocation> list = new Gson().fromJson(searchObj, new TypeToken<List<DeptWiseIncBudgetAllocation>>() {
                }.getType());
                int count = list.size();
                int resultCount = 0;
                String result = "false";

                for (Iterator<DeptWiseIncBudgetAllocation> iterator = list.iterator(); iterator.hasNext();) {
                    DeptWiseIncBudgetAllocation next = iterator.next();
                    String checkDuplicate = new DepartmentWiseIncAlloManager().checkDuplicateCon(next);
                    if (checkDuplicate.equalsIgnoreCase(ApplicationConstants.DUPLICATE_MESSAGE)) {
                        result = ApplicationConstants.DUPLICATE_MESSAGE;
                    } else {
                        if (new DepartmentWiseIncAlloManager().save(next, loginUserId) != "") {
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
                    logger.info(Common.getLogMsg("HeadwiseIncomeBudgetSaveService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                    logger.info(Common.getLogMsg("HeadwiseIncomeBudgetSaveService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                }
//                } else {
//                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
//                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
//                    logger.info(Common.getLogMsg("HeadwiseIncomeBudgetSaveService", ApplicationConstants.FAIL, ApplicationConstants.UNAUTHORIZED_ACCESS));
//                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("HeadwiseIncomeBudgetSaveService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.INVALID_SESSION));
            }

        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("HeadwiseIncomeBudgetSaveService", ApplicationConstants.ERROR, stack.toString()));
            out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_EXCEPTION));
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
