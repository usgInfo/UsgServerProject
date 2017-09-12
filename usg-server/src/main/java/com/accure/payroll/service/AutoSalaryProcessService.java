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
import java.util.logging.Level;
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
public class AutoSalaryProcessService extends HttpServlet {

    Logger logger = Logger.getLogger(AutoSalaryProcessService.class);
    String privilege = ApplicationConstants.PV_AUTO_SALARY_PROCESS_VIEW;
    String privilege1 = ApplicationConstants.PV_AUTO_SALARY_PROCESS_CREATE;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
                User currentUser = (User) session.getAttribute("user");
                boolean authorized = UserManager.checkUserPrivilege(currentUser, privilege);
                boolean authorized1 = UserManager.checkUserPrivilege(currentUser, privilege1);
                if (authorized || authorized1) {
                    String action = request.getParameter("action");
                    String salaryProcessType = request.getParameter("salaryProcessType");
                    if (ApplicationConstants.EMPLOYEE_VIEW.equals(action)) {
                        String searchJson = request.getParameter("searchJson");
                        HashMap outputJson = new AutoSalaryProcessManager().viewSalary(searchJson, salaryProcessType);
                        if (outputJson != null && outputJson.size() > 0) {
                            out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.RECORDS_FOUND, outputJson)));
                            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        } else {
                            out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_FAIL, "search fail", outputJson)));
                            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        }
                    }
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
                    logger.error(Common.getLogMsg("AutoSalaryProcessService", ApplicationConstants.ERROR, ApplicationConstants.UNAUTHORIZED_ACCESS));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_INVALID_SESSION, ApplicationConstants.INVALID_SESSION, false)));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_EXCEPTION, ApplicationConstants.EXCEPTION_MESSAGE, false)));
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("AutoSalaryProcessService", ApplicationConstants.ERROR, stack.toString()));
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(AutoSalaryProcessService.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(AutoSalaryProcessService.class.getName()).log(Level.SEVERE, null, ex);
        }
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
