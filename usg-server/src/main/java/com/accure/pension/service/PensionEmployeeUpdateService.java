/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.service;

import com.accure.pension.manager.PensionEmployeeManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.pension.dto.PensionEmployee;
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

/**
 *
 * @author user
 */
public class PensionEmployeeUpdateService extends HttpServlet {

    Logger logger = Logger.getLogger(PensionEmployeeUpdateService.class);

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
                String classJson = request.getParameter("pensionEmployeeJson");
                Type type = new TypeToken<PensionEmployee>() {
                }.getType();
                PensionEmployee cla = new Gson().fromJson(classJson, type);
                //String association = request.getParameter("assJson");
                String id = request.getParameter("id");
                String userId = request.getParameter("userId");
                String status = new PensionEmployeeManager().updatePensionEmployeeMaster(cla, id, userId);

                if (status.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_SUCCESS));
                    logger.info(Common.getLogMsg("PensionEmployeeUpdateService", ApplicationConstants.UPDATE, ApplicationConstants.SUCCESS));
                } else if (status.equalsIgnoreCase(ApplicationConstants.FAIL)) {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                    logger.info(Common.getLogMsg("PensionEmployeeUpdateService", ApplicationConstants.UPDATE, ApplicationConstants.FAIL));
                } else if (status.equalsIgnoreCase(ApplicationConstants.DUPLICATE)) {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_DUPLICATE);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                    logger.info(Common.getLogMsg("PensionEmployeeUpdateService", ApplicationConstants.UPDATE, ApplicationConstants.FAIL));
                } 

            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("PensionEmployeeUpdateService", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
            }

        } catch (Exception e) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("PensionEmployeeUpdateService", ApplicationConstants.ERROR, stack.toString()));
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
