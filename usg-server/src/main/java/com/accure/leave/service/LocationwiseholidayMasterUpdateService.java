/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.service;

import com.accure.leave.dto.HolidayLocationMaster;
import com.accure.leave.dto.LocationWiseHolidayMaster;
import com.accure.leave.dto.LocationWiseHolidayMasterList;
import com.accure.leave.manager.HolidayLocationMasterManager;
import com.accure.leave.manager.LocationwiseHolidayMasterManager;
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
public class LocationwiseholidayMasterUpdateService extends HttpServlet {
Logger logger = Logger.getLogger(HolidayLocationMasterUpdateService.class);
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
                String objJson = request.getParameter("obj");
                Type type = new TypeToken<LocationWiseHolidayMasterList>() {
                }.getType();
                LocationWiseHolidayMasterList obj = new Gson().fromJson(objJson, type);
                String objid = request.getParameter("objId");
                String location = request.getParameter("locationWiseHoliday");
                 String userId = request.getParameter("userId");
                String year = request.getParameter("year");
                String result = new LocationwiseHolidayMasterManager().updateLocationWiseHolidaysMaster1(obj, objid, location, year,userId);
               if (result.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    out.write(new Gson().toJson(result));
                    logger.info(Common.getLogMsg("LocationwiseholidayMasterUpdateService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                } else if (result.equalsIgnoreCase(ApplicationConstants.DUPLICATE)) {
                    request.setAttribute("statuscode", ApplicationConstants.DUPLICATE);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_DUPLICATE));
                    logger.info(Common.getLogMsg("LocationwiseholidayMasterUpdateService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                } else if (result.equalsIgnoreCase(ApplicationConstants.FAIL)) {
                    request.setAttribute("statuscode", ApplicationConstants.FAIL);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                    logger.info(Common.getLogMsg("LocationwiseholidayMasterUpdateService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                } else if (result.equalsIgnoreCase("null")) {
                    request.setAttribute("statuscode", ApplicationConstants.FAIL);
                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_NULL));
                    logger.info(Common.getLogMsg("LocationwiseholidayMasterUpdateService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));

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
