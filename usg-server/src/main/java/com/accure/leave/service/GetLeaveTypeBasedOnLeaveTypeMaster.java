/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.service;

import com.accure.leave.dto.LeaveTypeMaster;
import com.accure.leave.manager.LeaveTypeManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author user
 */
public class GetLeaveTypeBasedOnLeaveTypeMaster extends HttpServlet {

    Logger logger = Logger.getLogger(GetLeaveTypeBasedOnLeaveTypeMaster.class);

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
            try {
                HttpSession session = request.getSession(false);
                if (SessionManager.checkUserSession(session)) {
                    String natureType = request.getParameter("natureType");
                    String employeeCategory = request.getParameter("employeeCategory");

                    String result = new LeaveTypeManager().getLeaveTypeForLeaveCarryForward(natureType, employeeCategory);

                    if (result != null) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        out.write(new Gson().toJson(result));
                        logger.info(Common.getLogMsg("GetLeaveTypeBasedOnLeaveTypeMaster", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                        logger.info(Common.getLogMsg("GetLeaveTypeBasedOnLeaveTypeMaster", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                    }
                }
            } catch (Exception ex) {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
                StringWriter stack = new StringWriter();
                ex.printStackTrace(new PrintWriter(stack));
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_EXCEPTION));
                logger.info(Common.getLogMsg("GetLeaveTypeBasedOnLeaveTypeMaster", ApplicationConstants.AUTHENTICATION, ApplicationConstants.HTTP_STATUS_EXCEPTION));
            }
        } finally {
            out.flush();
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
