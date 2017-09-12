/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.service;

import com.accure.payroll.manager.AutoSalaryProcessManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
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
 * @author accure
 */
public class ProcessSalaryService extends HttpServlet {

    Logger logger = Logger.getLogger(ProcessSalaryService.class);
    String privilege = ApplicationConstants.PV_AUTO_SALARY_PROCESS_CREATE;
    String privilege1 = ApplicationConstants.PV_AUTO_SALARY_PROCESS_UPDATE;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
                User currentUser = (User) session.getAttribute("user");
                boolean authorized = UserManager.checkUserPrivilege(currentUser, privilege);
                boolean authorized1 = UserManager.checkUserPrivilege(currentUser, privilege1);
                if (authorized || authorized1) {
                    String operationType = request.getParameter("operationType");
                    String loginUserId = request.getParameter("loginId");
                    String month = request.getParameter("month");
                    String year = request.getParameter("year");
                    String payMonth = request.getParameter("payMonth");
                    String payYear = request.getParameter("payYear");
                    String salaryProcessType = request.getParameter("salaryProcessType");

                    HashMap result = null;
                    if ("process".equals(operationType)) {
                        String[] employeeCode = request.getParameterValues("processSalary[]");
                        result = new AutoSalaryProcessManager().processSalary(employeeCode, month, year, loginUserId, salaryProcessType, payMonth, payYear);
                        if (result != null && result.size() > 0) {
                            out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.SALARY_SUCCESSFULLY_PROCESSED, result)));
                            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                            logger.info(Common.getLogMsg("ProcessSalaryService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                        } else {
                            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                            out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.SALARY_PROCESS_FAIL, result)));
                            logger.info(Common.getLogMsg("ProcessSalaryService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                        }
                    } else if ("unprocess".equals(operationType)) {
                        String[] unprocessCode = request.getParameterValues("unprocessSalary[]");
                        result = new AutoSalaryProcessManager().unprocessSalary(unprocessCode, month, year, loginUserId, salaryProcessType, payMonth, payYear);
                        if (result != null && result.size() > 0) {
                            out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.SALARY_SUCCESSFULLY_UNPROCESSED, result)));
                            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                            logger.info(Common.getLogMsg("ProcessSalaryService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                        } else {
                            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                            out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.SALARY_UNPROCESS_FAIL, result)));
                            logger.info(Common.getLogMsg("ProcessSalaryService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                        }
                    }
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
                    logger.error(Common.getLogMsg("ProcessSalaryService", ApplicationConstants.FAIL, ApplicationConstants.UNAUTHORIZED_ACCESS));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_INVALID_SESSION, ApplicationConstants.INVALID_SESSION, null)));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_EXCEPTION, ApplicationConstants.EXCEPTION_MESSAGE, "")));
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("ProcessSalaryService", ApplicationConstants.ERROR, stack.toString()));
            //System.out.println(stack.toString());
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
