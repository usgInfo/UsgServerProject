/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.service;

import com.accure.budget.dto.FinancialYear;
import com.accure.budget.manager.FinancialYearManager;
import com.accure.user.dto.User;
import com.accure.user.manager.AuthenticationManager;
import com.accure.user.manager.UserLocationManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author deepak2310
 */
public class LoginService extends HttpServlet {

    Logger logger = Logger.getLogger(LoginService.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        try {
            logger.info(Common.getLogMsg("LoginService", ApplicationConstants.AUTHENTICATION, "started"));

            String loginid = request.getParameter("username");
            String password = request.getParameter("password");

//            String data = "loginid=" + loginid + "password=" + Security.encryptPassword(password);
            User user = new AuthenticationManager().authenticate(loginid, password);
            if (user != null) {
                HashMap<String, String> userObj = new HashMap<String, String>();
                User userInfo = new User();
                userInfo.setId(user.getId());
                userInfo.setEmployeeId(user.getEmployeeId());
                userInfo.setFname(user.getFname());
                userInfo.setLname(user.getLname());
                userInfo.setMobile(user.getMobile());
                userInfo.setEmail(user.getEmail());
                userInfo.setLoginid(user.getLoginid());
                userInfo.setDdoId(user.getDdoId());
                userInfo.setOrgRole(user.getOrgRole());
                userObj.put("userObject", new Gson().toJson(userInfo));
                userObj.put("locationObjects", new UserLocationManager().getLocationOfUser(user.getOrgRole()));
                FinancialYear financialYear = new FinancialYearManager().getActiveFinancialYear();
                String currentFinancialYear = "";
                if (financialYear != null) {
                    currentFinancialYear = financialYear.getFromDate() + "~" + financialYear.getToDate();
                    userObj.put("currentFinancialYear", currentFinancialYear);
                    userObj.put("currentFinancialYearId", ((Map<String, String>) financialYear.getId()).get("$oid"));
                }
                if (userInfo != null) {
                    session.setAttribute("user", user);
                    session.setAttribute("currentFinancialYear", currentFinancialYear);
                    session.setAttribute("SessionTime", "" + System.currentTimeMillis());
                    out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.SIGNIN, userObj)));
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                    logger.info(Common.getLogMsg("LoginService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.UNSIGNIN)));
                logger.info(Common.getLogMsg("LoginService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_EXCEPTION, ApplicationConstants.UNSIGNIN)));
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("LoginService", ApplicationConstants.ERROR, stack.toString()));
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
            java.util.logging.Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
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
            java.util.logging.Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
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
