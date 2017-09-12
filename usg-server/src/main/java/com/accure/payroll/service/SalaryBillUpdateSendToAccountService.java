/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.service;

import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.manager.SalaryBillCreationManager;
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
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author ankur
 */
public class SalaryBillUpdateSendToAccountService extends HttpServlet {

    Logger logger = Logger.getLogger(SalaryBillUpdateSendToAccountService.class);
    String privilege = ApplicationConstants.PV_SALARY_BILL_TYPE_UPDATE;

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

//                String empListJson = request.getParameter("empListJson");
//
//                String userid = request.getParameter("userid");
//
//                //This list has key, value pair of Object Id and all other values
//                List<Map> list = new Gson().fromJson(empListJson, new TypeToken<List<Map>>() {
//                }.getType());
//                String empObjId[] = new String[list.size()];
//               //This i is used to increment the index of empObjId
//                int i = 0;
//                String manualBill[] = new String[list.size()];
//                //we will get one by one Object Id's & then Store in empObjId Array
//                for (Map li : list) {
//                    String str = (String) li.get("emprowid");
//                    empObjId[i] = str;
//                    manualBill[i] = (String)li.get("maualBillNumber");
//                    i++;
//                }
                    String empListJson = request.getParameter("empListJson");

                    String loginUserId = request.getParameter("userid");
                    List<Map> list = new Gson().fromJson(empListJson, new TypeToken<List<Map>>() {
                    }.getType());
                    String empObjId[] = new String[list.size()];
                    //This i is used to increment the index of empObjId
                    int i = 0;
                    boolean status = false;
                    //we will get one by one Object Id's & then Store in empObjId Array
                    for (Map li : list) {
                        String str = (String) li.get("emprowid");

                        String objJson = new SalaryBillCreationManager().fetchData(str);
                        Type type = new TypeToken<AutoSalaryProcess>() {
                        }.getType();
                        AutoSalaryProcess obj = new Gson().fromJson(objJson, type);
                        obj.setManualBillNumber((String) li.get("manualBillNo"));
                        obj.setBillDate((String) li.get("billDate"));
                        if (obj.isSendToAccounts() == false) {
                            obj.setSendToAccounts(true);
                        }

                        status = new SalaryBillCreationManager().update(obj, str, loginUserId);
                        i++;
                    }

                    //Using this String array (empObjId) of Object Id we will call method
                    if (status) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_SUCCESS));

                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                        logger.info(Common.getLogMsg("SalaryBillUpdateSendToAccountService", ApplicationConstants.DELETE, ApplicationConstants.FAIL));
                    }
                } else {
                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
                    logger.info(Common.getLogMsg("SalaryBillUpdateSendToAccountService", ApplicationConstants.FAIL, ApplicationConstants.UNAUTHORIZED_ACCESS));
                }

            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("SalaryBillUpdateSendToAccountService", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("SalaryBillUpdateSendToAccountService", ApplicationConstants.ERROR, stack.toString()));
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
