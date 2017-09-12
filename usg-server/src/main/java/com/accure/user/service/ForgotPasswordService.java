/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.service;

import com.accure.user.manager.UserManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author deepak2310
 */
public class ForgotPasswordService extends HttpServlet {

    Logger logger = Logger.getLogger(ForgotPasswordService.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String emailId = request.getParameter("email");
            boolean bStatus = new UserManager().forgotPassword(emailId);;

            if (bStatus) {
                out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.FORGOT_PASSWORD_GENERATED, bStatus)));
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                logger.info(Common.getLogMsg("ForgotPasswordService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.FORGOT_PASSWORD_NOTGENERATED)));
                logger.info(Common.getLogMsg("ForgotPasswordService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_EXCEPTION, ApplicationConstants.FORGOT_PASSWORD_NOTGENERATED)));
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("ForgotPasswordService", ApplicationConstants.ERROR, stack.toString()));
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
