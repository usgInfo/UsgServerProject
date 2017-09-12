/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.service;

import com.accure.finance.dto.PartyMaster;
import com.accure.finance.manager.PartyMasterManager;
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
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class PartyMasterSave extends HttpServlet {

    Logger logger = Logger.getLogger(PartyMasterSave.class);
    //  String privilege = ApplicationConstants.PV_CREATE_PARTY_MASTER;

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

            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
//                User currentUser = (User) session.getAttribute("user");
//                boolean authorized = UserManager.checkUserPrivilege(currentUser, privilege);
//                if (authorized) {
                String partyMasterStr = request.getParameter("partyMasterStr");
                String userid = request.getParameter("userId");
                Type type = new TypeToken<PartyMaster>() {
                }.getType();

                PartyMaster cla = new Gson().fromJson(partyMasterStr, type);
                String result = new PartyMasterManager().save(cla, userid);

                if (result.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    out.write(new Gson().toJson(result));
                    logger.info(Common.getLogMsg("PartyMasterSave", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                } else if (result.equalsIgnoreCase(ApplicationConstants.DUPLICATE)) {
                    request.setAttribute("statuscode", ApplicationConstants.DUPLICATE);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_DUPLICATE));
                    logger.info(Common.getLogMsg("PartyMasterSave", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                } else if (result.equalsIgnoreCase(ApplicationConstants.FAIL)) {
                    request.setAttribute("statuscode", ApplicationConstants.FAIL);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                    logger.info(Common.getLogMsg("PartyMasterSave", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                } else if (result.equalsIgnoreCase("null")) {
                    request.setAttribute("statuscode", ApplicationConstants.FAIL);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_NULL));
                    logger.info(Common.getLogMsg("PartyMasterSave", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                }
//                } else {
//                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
//                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
                //logger.info(Common.getLogMsg("PartyMasterSave", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));

//                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("PartyMasterSave", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
            }

        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("PartyMasterSave", ApplicationConstants.ERROR, stack.toString()));
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
