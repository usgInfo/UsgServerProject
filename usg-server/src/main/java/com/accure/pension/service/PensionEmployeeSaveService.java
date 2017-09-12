/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.service;

import com.accure.hrms.manager.EmployeeManager;
import com.accure.pension.manager.PensionManager;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class PensionEmployeeSaveService extends HttpServlet {

    Logger logger = Logger.getLogger(PensionEmployeeSaveService.class);
    String privilege = ApplicationConstants.PV_EMPLOYEE_CREATE;

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
                //if (authorized) {
//                String employeeJson = request.getParameter("employeeJson");
                    String employeeJson = request.getHeader("employeeJson");
                    String userId = request.getHeader("userId");
                    boolean isImageAVailable = ServletFileUpload.isMultipartContent(request);
                    //System.out.println("employeeJson" + employeeJson);
                    Type type = new TypeToken<com.accure.pension.dto.PensionEmployeeDetails>() {
                    }.getType();
                    com.accure.pension.dto.PensionEmployeeDetails emp = new Gson().fromJson(employeeJson, type);
                    emp.setImageAvailable(isImageAVailable);
                    String resultid = new PensionManager().save(emp, userId);

                    List<String> fileIdList = new ArrayList<String>();
                    if (resultid != null && !resultid.isEmpty()) {

                        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                        if (!isMultipart) {
                        } else {
                            FileItemFactory factory = new DiskFileItemFactory();
                            ServletFileUpload upload = new ServletFileUpload(factory);
                            List items = null;
                            try {
                                items = upload.parseRequest(request);
                            } catch (FileUploadException e) {
                            }
                            Iterator itr = items.iterator();
                            List<String> fileIds = new ArrayList<String>();
                            while (itr.hasNext()) {
                                FileItem item = (FileItem) itr.next();
                                if (!item.isFormField()) {
                                    try {
                                        String imageid = new PensionManager().storeFile(item, resultid);
                                        fileIdList.add(imageid);
                                    } catch (Exception e) {
                                    }
                                }
                            }
//                        employee.setImagelist(fileIdList);
//                        updateStatus = new EmployeeManager().update(employee, resultid);
                        }
//
//                    if (updateStatus = true) {
//                        out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.HOARDINGINFO_SAVED, fileIdList)));
//                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
//                        logger.info(Common.getLogMsg("CreateHoardingService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
//                    } else {
//                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
//                        out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.HOARDINGINFO_UNSAVED, status)));
//                        logger.info(Common.getLogMsg("CreateHoardingService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
//                    }
                    }

                    if (resultid != null && !resultid.isEmpty()) {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        out.write(new Gson().toJson(resultid));
                        logger.info(Common.getLogMsg("EmployeeService", ApplicationConstants.VIEW, ApplicationConstants.SUCCESS));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_FAIL));
                        logger.info(Common.getLogMsg("EmployeeService", ApplicationConstants.VIEW, ApplicationConstants.FAIL));
                    }
//                } else {
//                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
//                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
//                    logger.info(Common.getLogMsg("EmployeeService", ApplicationConstants.FAIL, ApplicationConstants.UNAUTHORIZED_ACCESS));
//                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
                logger.info(Common.getLogMsg("EmployeeService", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("EmployeeService", ApplicationConstants.ERROR, stack.toString()));
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
