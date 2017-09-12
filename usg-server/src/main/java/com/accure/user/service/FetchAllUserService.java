/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.service;

import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author deepak2310
 */
public class FetchAllUserService extends HttpServlet {

    Logger logger = Logger.getLogger(FetchAllUserService.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            List<User> userList = null;
            userList = new UserManager().fetchAllUser();
            if (userList != null && userList.size() > 0) {
                out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.USER_LIST, userList)));
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                logger.info(Common.getLogMsg("FetchAllUserService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
            } else if (userList == null || userList.isEmpty()) {
                out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_NODATA, ApplicationConstants.NO_DATA_FOUND)));
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.USER_LIST_FAILED, userList)));
                logger.info(Common.getLogMsg("FetchAllUserService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_EXCEPTION, ApplicationConstants.USER_LIST_FAILED, false)));
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("FetchAllUserService", ApplicationConstants.ERROR, stack.toString()));
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
            java.util.logging.Logger.getLogger(FetchAllUserService.class.getName()).log(Level.SEVERE, null, ex);
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
            java.util.logging.Logger.getLogger(FetchAllUserService.class.getName()).log(Level.SEVERE, null, ex);
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
