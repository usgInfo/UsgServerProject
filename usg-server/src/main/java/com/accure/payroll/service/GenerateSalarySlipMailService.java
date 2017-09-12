/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.service;

import com.accure.payroll.manager.EmployeeListManager;
import com.accure.payroll.manager.GenerateSalarySlipMailManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
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
public class GenerateSalarySlipMailService extends HttpServlet {

    Logger logger = Logger.getLogger(GenerateSalarySlipMailService.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/pdf;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
                ServletContext servletContext = getServletContext();
               String path=File.separator+"usg"+File.separator+"images";
                String contextPath = servletContext.getRealPath(path);
                String employeeList = request.getParameter("employeeCodeList");
                String ddo = request.getParameter("ddo");
                String month = request.getParameter("month");
                String year = request.getParameter("year");
                String finalcialYearStart=request.getParameter("financialyearStart");
             String finalcialYearEnd=request.getParameter("financialyearEnd");
                HashMap outputJson = new GenerateSalarySlipMailManager().employeeSalarySlipMail(employeeList, ddo, month, year, contextPath,finalcialYearStart,finalcialYearEnd);
                if (outputJson != null) {
                    out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.EMPLOYEE_LIST_FOR_EMPLOYEE_ATTENDACE, outputJson)));
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    logger.info(Common.getLogMsg("GenerateSalarySlipMailService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                } else if (outputJson == null) {
                    out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_NODATA, ApplicationConstants.NO_DATA_FOUND, "")));
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.EMPLOYEE_LIST_FOR_EMPLOYEE_ATTENDACE_FAILED, outputJson)));
                    logger.info(Common.getLogMsg("GenerateSalarySlipMailService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                }
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("GenerateSalarySlipMailService", ApplicationConstants.ERROR, stack.toString()));
            // out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_EXCEPTION));
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
