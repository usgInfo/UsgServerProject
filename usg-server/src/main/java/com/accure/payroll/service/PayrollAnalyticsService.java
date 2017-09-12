package com.accure.payroll.service;

import com.accure.payroll.manager.PayrollAnalyticsManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author chaitu
 */
public class PayrollAnalyticsService extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
                String type = request.getParameter("type");
                String ddo = request.getParameter("ddoId");
                //System.out.println(ddo);
                session.setAttribute("selectedddoid", ddo);
                LinkedHashMap result = null;
                if ("processedsalary".equals(type)) {
                    result = PayrollAnalyticsManager.getSalaryStatusAnalytics(session, true);
                } else if ("unprocessedsalary".equals(type)) {
                    result = PayrollAnalyticsManager.getSalaryStatusAnalytics(session, false);
                } else if ("salarypaid".equals(type)) {
                    result = PayrollAnalyticsManager.getSalaryAmountAnalytics(session);
                }
                session.removeAttribute("selectedddoid");
                if (result != null && result.size() > 0) {
                    out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.SUCCESS, result)));
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.NO_DATA_AVAILABLE, result)));
                }
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_EXCEPTION, ApplicationConstants.EXCEPTION_MESSAGE, "")));
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
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
