/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.service;

import com.accure.finance.dto.Group;
import com.accure.finance.manager.GroupManager;
import com.accure.usg.common.manager.SessionManager;
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
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class FinancialGroupService extends HttpServlet {

    String privilege = ApplicationConstants.PV_GROUP_CREATE;
    Logger logger = Logger.getLogger(FinancialGroupService.class);

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

                User currentUser = (User) session.getAttribute("user");
                boolean authorized = UserManager.checkUserPrivilege(currentUser, privilege);
                if (authorized) {

                    String groupJson = request.getParameter("groupJson");
                    String userId = request.getParameter("userId");
                    Type type = new TypeToken<Group>() {
                    }.getType();
                    Group fc = new Gson().fromJson(groupJson, type);
                    String result = new GroupManager().save(fc, userId);
                    if (result.equalsIgnoreCase(ApplicationConstants.DUPLICATE_MESSAGE)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_DUPLICATE);
                        out.write(new Gson().toJson(result));
                    } else if (result.equalsIgnoreCase(ApplicationConstants.UNAUTHORIZED_ACCESS)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
                        out.write(new Gson().toJson(result));
                    } else if (result.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        out.write(new Gson().toJson(result));
                    } else if (result.equalsIgnoreCase(ApplicationConstants.FAIL)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(result));
                        logger.info(Common.getLogMsg("FinancialGroupService", ApplicationConstants.FAIL, ApplicationConstants.FAIL));
                    }
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
                    logger.info(Common.getLogMsg("FinancialGroupService", ApplicationConstants.FAIL, ApplicationConstants.UNAUTHORIZED_ACCESS));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("FinancialGroupService", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.info(Common.getLogMsg("FinancialGroupService", ApplicationConstants.FAIL, ApplicationConstants.EXCEPTION_MESSAGE));

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
