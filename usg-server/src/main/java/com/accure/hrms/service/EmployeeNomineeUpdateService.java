/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.service;

import com.accure.hrms.dto.EmployeeNominee;
import com.accure.hrms.manager.EmployeeNomineeManager;
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
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;

/**
 *
 * @author user
 */
public class EmployeeNomineeUpdateService extends HttpServlet {

    Logger logger = Logger.getLogger(EmployeeNomineeUpdateService.class);
    String privilege = ApplicationConstants.PV_EMPLOYEE_NOMINEE_DETAILS_UPDATE;

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
        HttpSession session = request.getSession(false);
        try {
            /* TODO output your page here. You may use following sample code. */

            if (SessionManager.checkUserSession(session)) {
                User currentUser = (User) session.getAttribute("user");
                boolean authorized = UserManager.checkUserPrivilege(currentUser, privilege);
                if (authorized) {

                    String newcityCategory = request.getParameter("nomineeJson");
                    String id = request.getParameter("id");
                    String userid = request.getParameter("userid");

                    Type type = new TypeToken<EmployeeNominee>() {
                    }.getType();
                    EmployeeNominee nomineeDTO = new Gson().fromJson(newcityCategory, type);
                    EmployeeNomineeManager disManObj = new EmployeeNomineeManager();

                String status = disManObj.update(id,nomineeDTO,userid);

                    if (status.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_SUCCESS));
                        logger.info(Common.getLogMsg("EmployeeNomineeUpdateService", ApplicationConstants.UPDATE, ApplicationConstants.SUCCESS));
                    } else if (status.equalsIgnoreCase(ApplicationConstants.DUPLICATE_EMP_CODE)) {
                        request.setAttribute("statuscode", ApplicationConstants.DUPLICATE_EMP_CODE);
                        out.write(new Gson().toJson(ApplicationConstants.DUPLICATE_EMP_CODE));
                        logger.info(Common.getLogMsg("EmployeeNomineeUpdateService", ApplicationConstants.UPDATE, ApplicationConstants.SUCCESS));
                    } else if (status.equalsIgnoreCase(ApplicationConstants.DUPLICATE_NOMINEES)) {
                        request.setAttribute("statuscode", ApplicationConstants.DUPLICATE_NOMINEES);
                        out.write(new Gson().toJson(ApplicationConstants.DUPLICATE_NOMINEES));
                        logger.info(Common.getLogMsg("EmployeeNomineeUpdateService", ApplicationConstants.UPDATE, ApplicationConstants.SUCCESS));
                }  else if (status.equalsIgnoreCase(ApplicationConstants.DUPLICATE_PRIMARIES)) {
                        request.setAttribute("statuscode", ApplicationConstants.DUPLICATE_PRIMARIES);
                        out.write(new Gson().toJson(ApplicationConstants.DUPLICATE_PRIMARIES));
                        logger.info(Common.getLogMsg("EmployeeNomineeUpdateService", ApplicationConstants.UPDATE, ApplicationConstants.SUCCESS));
                } 
                
                
                
                
                else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                        logger.info(Common.getLogMsg("EmployeeNomineeUpdateService", ApplicationConstants.UPDATE, ApplicationConstants.FAIL));
                    }
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
                    logger.info(Common.getLogMsg("EmployeeNomineeUpdateService", ApplicationConstants.FAIL, ApplicationConstants.UNAUTHORIZED_ACCESS));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("EmployeeNomineeUpdateService", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("EmployeeNomineeUpdateService", ApplicationConstants.ERROR, stack.toString()));
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
