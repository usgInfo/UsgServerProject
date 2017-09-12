/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.service;

import com.accure.accure.db.MongoFile;
import com.accure.hrms.manager.EmployeeManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
//import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Asif
 */
public class EmployeeImageSearchService extends HttpServlet {

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
                String employeeId = request.getParameter("employeeId");
                MongoFile mf = new EmployeeManager().FetchImage(employeeId);

                if (mf != null) {
                    byte[] bytes = mf.getBaos().toByteArray();
                    response.setContentType("image/*");
                    String based64String = Base64.encodeBase64String(bytes);
                    out.write(based64String);
                } else {
                    out.write("No Data Found");
                }

//                Base64InputStream bi=(Base64InputStream) result.getInputStream();
//                InputStream ba = result.getInputStream();
//                final byte[] bytes64bytes = Base64.getEncoder(ba);
//                final String content = new String(bytes64bytes);
//                Base64InputStream b64is = new Base64InputStream(ba, true, -1, null);
//                response.setContentType(new Gson().toJson(b64is));
//                //System.out.println(b64is);
//                out.write(new Gson().toJson(b64is));

//                final byte[] bytes64bytes = Base64.encodeBase64(IOUtils.toByteArray(ba));
//                final String content = (String) (bytes64bytes.toString());
//                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
//                out.write(new Gson().toJson(bytes64bytes));

//                val bytes = IOUtils.toByteArray(ba);
//                val bytes64 = Base64.encodeBase64(bytes)
//                val content = new String(bytes64) //                byte[] data = new byte[contentLength];
                //
                //                final byte[] bytes64bytes = Base64.encodeBase64(IOUtils.toByteArray(ba));
                //                final String content = new String(bytes64bytes);
                //                if (result == true) {
                //                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                //                    out.write(new Gson().toJson(result));
                ////                      logger.info(Common.getLogMsg("EmployeeManagerViewAllService", ApplicationConstants.VIEW, ApplicationConstants.SUCCESS));
                //                } else {
                //                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                //                    out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                ////                    logger.info(Common.getLogMsg("ViewReligionService", ApplicationConstants.VIEW, ApplicationConstants.FAIL));
                //                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
//                logger.info(Common.getLogMsg("ViewReligionService", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
//            logger.error(Common.getLogMsg("ViewReligionService", ApplicationConstants.ERROR, stack.toString()));
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
