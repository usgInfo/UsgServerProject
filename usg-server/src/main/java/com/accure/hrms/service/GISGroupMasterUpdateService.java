/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.service;

import com.accure.hrms.dto.GISGroup;
import com.accure.hrms.manager.GISGroupMasterManager;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
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
public class GISGroupMasterUpdateService extends HttpServlet {
    Logger logger = Logger.getLogger(GISGroupMasterUpdateService.class);
    String privilege = ApplicationConstants.PV_UPDATE_GIS_GROUP;
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
                if (authorized) 
                {
                    String classJson = request.getParameter("updategisgroupmaster");
                String userid=request.getParameter("userid");
                    Type type = new TypeToken<GISGroup>() {
                    }.getType();
                    GISGroup cla = new Gson().fromJson(classJson, type);
                String cId=request.getParameter("id");
                String status = new GISGroupMasterManager().update(cla, cId,userid);
                  if (status.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_SUCCESS));
                    } else if (status.equalsIgnoreCase(ApplicationConstants.FAIL)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                        logger.info(Common.getLogMsg("GISGroupMasterUpdateService", ApplicationConstants.DELETE, ApplicationConstants.FAIL));
                    } else if (status.equalsIgnoreCase(ApplicationConstants.DUPLICATE_MESSAGE)) {
                        request.setAttribute("statuscode", ApplicationConstants.DUPLICATE_MESSAGE);
                        out.write(new Gson().toJson(ApplicationConstants.DUPLICATE_MESSAGE));
                        logger.info(Common.getLogMsg("GISGroupMasterUpdateService", ApplicationConstants.DELETE, ApplicationConstants.FAIL));

                    } else 
                    {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                        logger.info(Common.getLogMsg("GISGroupMasterUpdateService", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
                    }

                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("GISGroupMasterUpdateService", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
            }
        } 
        catch(Exception e){
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("GISGroupMasterUpdateService", ApplicationConstants.ERROR, stack.toString()));
            out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_EXCEPTION));
        }finally {
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
