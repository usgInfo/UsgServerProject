/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.service;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
public class DownloadServlet extends HttpServlet {

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
      
        try {
            ServletOutputStream sout = response.getOutputStream();
            /* TODO output your page here. You may use following sample code. */
         
            String fileName=request.getParameter("fileName");
             
            fileName=fileName.replaceAll("\"", "");
            String fileFormat=fileName.substring(fileName.indexOf("."));
            //System.out.println(fileFormat);
             //System.out.println(fileName);
             File f = new File(fileName);
            //System.out.println(fileName.toString());
            if(fileFormat.equalsIgnoreCase("pdf")){
            response.setContentType("application/pdf");
            }
            if(fileFormat.equalsIgnoreCase("xls")||fileFormat.equalsIgnoreCase("xlsx") ){
            response.setContentType("application/vnd.ms-excel");
            }
             if(fileFormat.equalsIgnoreCase("doc")||fileFormat.equalsIgnoreCase("docx") ){
            response.setContentType("application/msword");
            }
            response.setHeader("Content-Disposition", "attachment;filename="+f.getName()+"."+fileFormat);
                    byte[] arBytes = new byte[(int)f.length()];
                    //System.out.println(arBytes.length);
                    FileInputStream is = new FileInputStream( f );
                    is.read(arBytes);
                    sout.write(arBytes);
                    is.close();
                   
        }catch(Exception e){
            e.printStackTrace();
            
        } finally {
          //  sout.close();
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

}