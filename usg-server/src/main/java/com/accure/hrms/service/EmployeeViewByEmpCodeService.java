/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.service;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
public class EmployeeViewByEmpCodeService extends HttpServlet {

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
//            HttpSession session = request.getSession(false);
//            if (SessionManager.checkUserSession(session)) {
//                
//                String cId=request.getParameter("employeeCode");
//               / String result = new PensionHeadAssignManager().getEmployeeByEmployeeCode(cId);
//                if (result !=null ) {
//                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
//                    out.write(result);
//                } else {
//                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
//                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
//                }
//            } else {
//                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
//                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
//            }
//        } catch (Exception ex) {
//            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
//            StringWriter stack = new StringWriter();
//            ex.printStackTrace(new PrintWriter(stack));
//
//            out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_EXCEPTION));
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
