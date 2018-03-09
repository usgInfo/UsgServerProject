/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.service;

import com.accure.budget.dto.BudgetAtAGlance;
import com.accure.budget.dto.DeptWiseExpBudgetAllocation;
import com.accure.budget.dto.ExpenseBudgetApproval;
import com.accure.budget.manager.BudgetReAppropriationReportManager;
import com.accure.finance.reportformat.CreatePDFFile;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.pdfbox.util.PDFMergerUtility;

/**
 *
 * @author user
 */
public class BudgerReapropriationReportService extends HttpServlet {
Logger logger = Logger.getLogger(BudgetAtAGlanceService.class);
    String privilege = ApplicationConstants.PV_BUDGET_REPORT_VIEW;

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
            /* TODO output your page here. You may use following sample code. */
            HttpSession session = request.getSession(false);
            String budgetExpenseStr = request.getParameter("budgetsearchDetails");
              String finYear = URLDecoder.decode(request.getParameter("finYear"));
            if (SessionManager.checkUserSession(session)) {

//                User currentUser = (User) session.getAttribute("user");
//                boolean authorized = UserManager.checkUserPrivilege(currentUser, privilege);
//                if (authorized) {
                    ServletContext servletContext = getServletContext();
                    String path = File.separator + "usg" + File.separator + "images";
                    String contextPath = servletContext.getRealPath("/" + path); 
                    List<DeptWiseExpBudgetAllocation> budExpenseBudgetApprovalList = null;
                   budExpenseBudgetApprovalList = new BudgetReAppropriationReportManager().
                            generateReApropriationBudgetReport(budgetExpenseStr);
                    ByteArrayOutputStream bos = new BudgetReAppropriationReportManager().generateBudgetReport(budExpenseBudgetApprovalList,
                            contextPath,finYear);
                    if (bos != null) {
                        response.setContentType("application/pdf;charset=UTF-8;name='BudgetReappropriationReport.pdf'");
                        PDFMergerUtility ut = new PDFMergerUtility();
                        ut.addSource(new ByteArrayInputStream(bos.toByteArray()));
                        ut.setDestinationStream(out);
                        ut.mergeDocuments();
                        ut.setDestinationFileName("BudgetReappropriationReport.pdf");
                        ut.getDestinationStream().flush();
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        logger.info(Common.getLogMsg("BudgetAtAGlanceService",
                                ApplicationConstants.VIEW, ApplicationConstants.SUCCESS));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.print(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.BANK_LIST_fAILED, null)));
                        logger.info(Common.getLogMsg("BudgetAtAGlanceService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                    }

//                } else {
//                    request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_UNAUTHORIZED);
////                    out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_UNAUTHORIZED, "Unauthorized access", null)));
//                    logger.info(Common.getLogMsg("BudgetAtAGlanceService", ApplicationConstants.FAIL, ApplicationConstants.UNAUTHORIZED_ACCESS));
//                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                out.print(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.BANK_LIST_fAILED, null)));
                logger.info(Common.getLogMsg("BudgetAtAGlanceService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("BudgetAtAGlanceService",
                    ApplicationConstants.ERROR, stack.toString()));
        } finally {
            out.flush();
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
