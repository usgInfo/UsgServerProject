/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.service;

import com.accure.payroll.manager.CommonMasterReportManager;
import com.accure.usg.server.utils.ApplicationConstants;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.pdfbox.util.PDFMergerUtility;

/**
 *
 * @author user
 */
public class CommonMasterReportService extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        ServletOutputStream out = response.getOutputStream();
        try {
            ServletContext servletContext = getServletContext();
            String path = File.separator + "usg" + File.separator + "images";
            String contextPath = servletContext.getRealPath("/" + path);
            String reportType = request.getParameter("reportType");
            String fin = URLDecoder.decode(request.getParameter("fin"));
            ByteArrayOutputStream baos = null;
            if (reportType.equalsIgnoreCase("FORMULA")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getFormulaMasterReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("DESIGNATION_FUND_TYPE_MAPPING")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getDesiFundTypeMappingReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("FUND_HEAD_MAPPING")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getFundHeadMappingReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("DDO_DEPARTMENT_MASTER")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getDdoDepartmentMappingReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("PF_TYPE")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getPfTypeReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("FUND_TYPE")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getFundTypeReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("NATURE_TYPE")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getNatureTypeReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("BANK")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getBankReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("DESIGNATION")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getDesignationReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("DA_MA")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getDaMaReports(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("SALARY_HEAD_MASTER")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getSalaryHeadReport(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("HEAD_SLAB_MASTER")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getHeadSlabReport(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("GRADE_MASTER")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getGradeMasterReport(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("CLASS_MASTER")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getClassMasterReport(contextPath,fin);
            } else if (reportType.equalsIgnoreCase("GIS_GROUP_MASTER")) {
                response.setHeader("Content-Disposition", "inline; filename=" + reportType + ".pdf");
                response.setContentType("application/pdf;charset=UTF-8;name=" + reportType + ".pdf");
                baos = new CommonMasterReportManager().getGisGroupReport(contextPath,fin);
            }
            if (baos != null) {
                PDFMergerUtility ut = new PDFMergerUtility();
                ut.addSource(new ByteArrayInputStream(baos.toByteArray()));
                ut.setDestinationStream(out);
                ut.mergeDocuments();
                ut.setDestinationFileName("SalaryStatusReport.pdf");
                ut.getDestinationStream().flush();
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
            }
//            } else {
//                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
//            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            //logger.error(Common.getLogMsg("MonthlySalaryStatusReport", ApplicationConstants.ERROR, stack.toString()));
            // out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_EXCEPTION));
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
