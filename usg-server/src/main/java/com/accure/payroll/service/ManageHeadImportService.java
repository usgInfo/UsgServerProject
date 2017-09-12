/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.service;

import com.accure.employee.importandexport.EmployeeImportData;
import com.accure.managehead.importandexport.ManageHeadImport;
import com.accure.usg.server.utils.ApplicationConstants;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author user
 */
public class ManageHeadImportService extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 50 * 1024;
    private int maxMemSize = 4 * 1024;
   

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ServletContext servletContext = getServletContext();
        String contextPath = servletContext.getRealPath(File.separator);
        //System.out.println("data"+contextPath);

        filePath = contextPath + "HeadImportFiles";
        File file = new File(filePath);
        if (!file.exists()) {
            if (file.mkdir()) {
                //System.out.println("Directory is created!");
            } else {
                //System.out.println("Failed to create directory!");
            }
        }

        try {
            isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                return;
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();

            factory.setSizeThreshold(maxMemSize);
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(maxFileSize);
            try {
                List fileItems = upload.parseRequest(request);
                Iterator i = fileItems.iterator();
                while (i.hasNext()) {
                    FileItem fi = (FileItem) i.next();
                    if (!fi.isFormField()) {

                        String fileName = fi.getName();
                        if (fileName.lastIndexOf("\\") >= 0) {
                            file = new File(file.getAbsolutePath() + File.separator + fileName.substring(fileName.lastIndexOf("\\")));
                        } else {
                            file = new File(file.getAbsolutePath() + File.separator + fileName.substring(fileName.lastIndexOf("\\") + 1));
                        }
                        fi.write(file);
                        String result = ManageHeadImport.manageHeadImport(file.getAbsoluteFile());
                        if(result!=null){
                        if (result.equalsIgnoreCase("success")) {
                            file.delete();
                            response.setContentType("text/plain");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(ApplicationConstants.HTTP_STATUS_SUCCESS);

                        } else{
                            file.delete();
                            response.setContentType("text/plain");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(result);
                        }
                        }else{
                             file.delete();
                            response.setContentType("text/plain");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(ApplicationConstants.HTTP_STATUS_NODATA);
                        }

                    }
                }

            } catch (Exception ex) {
                //System.out.println(ex);
            }
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
