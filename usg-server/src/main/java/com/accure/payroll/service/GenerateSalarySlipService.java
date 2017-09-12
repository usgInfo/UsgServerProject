/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.service;

import com.accure.budget.dto.FundType;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.PFType;
import com.accure.payroll.manager.SalarySlipManager;
import com.accure.usg.common.manager.SessionManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
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
public class GenerateSalarySlipService extends HttpServlet {

    Logger logger = Logger.getLogger(GenerateSalarySlipService.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(false);
            if (SessionManager.checkUserSession(session)) {
                String action = request.getParameter("action");
                List<Department> departmentList = null;
                List<Designation> designationList = null;
                List<CityMaster> postingCityList = null;
                List<PFType> pfTypeList = null;
                List<FundType> fundTypeList = null;
                List<BudgetHeadMaster> budgetHeadList = null;
                FundType fundType;
                if (ApplicationConstants.DEPARTMENT.equals(action)) {
                    departmentList = new SalarySlipManager().fetchAllDapartment();
                    if (departmentList != null) {
                        Department department = new Department();
                        department.setDepartment(departmentList.get(0).getDepartment());
                        out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.DEPARTMENT_LIST, departmentList)));
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                    } else if (departmentList == null) {
                        out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_NODATA, ApplicationConstants.NO_DATA_FOUND)));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.DEPARTMENT_LIST_FAILED, departmentList)));
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                    }
                } else if (ApplicationConstants.DESIGNATION.equals(action)) {
                    designationList = new SalarySlipManager().fetchAllDesignation();
                    if (designationList != null) {
                        Designation designation = new Designation();
                        designation.setDesignation(designationList.get(0).getDesignation());
                        out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.DESIGNATION_LIST, designationList)));
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                    } else if (designationList == null) {
                        out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_NODATA, ApplicationConstants.NO_DATA_FOUND)));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.DESIGNATION_LIST_FAILED, designationList)));
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                    }

                } else if (ApplicationConstants.PF_TYPE.equals(action)) {
                    pfTypeList = new SalarySlipManager().fetchAllPFType();
                    if (pfTypeList != null) {
//                        Employee employee = new Employee();
//                        employee.setPfType(pfTypeList.get(0).getPfType());
                        out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.PF_TYPE_LIST, pfTypeList)));
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                    } else if (pfTypeList == null) {
                        out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_NODATA, ApplicationConstants.NO_DATA_FOUND)));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.PF_TYPE_LIST_FAILED, pfTypeList)));
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                    }
                } else if (ApplicationConstants.POSTING_CITY.equals(action)) {
                    postingCityList = new SalarySlipManager().fetchAllPostingCity();
                    if (postingCityList != null) {
                        out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.POSTING_CITY_LIST, postingCityList)));
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                    } else if (postingCityList == null) {
                        out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_NODATA, ApplicationConstants.NO_DATA_FOUND)));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.POSTING_CITY_LIST_FAILED, postingCityList)));
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                    }
                }else if (ApplicationConstants.FUND_TYPE.equals(action)) {
                    fundTypeList = new SalarySlipManager().fetchAllFundType();
                    if (fundTypeList != null) {
                        fundType = new FundType();
                        fundType.setDescription(fundTypeList.get(0).getDescription());
                        out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.FUND_TYPE_LIST, fundTypeList)));
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                    } else if (fundTypeList == null) {
                        out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_NODATA, ApplicationConstants.NO_DATA_FOUND)));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.FUND_TYPE_LIST_FAILED, fundTypeList)));
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                    }
                }else if (ApplicationConstants.BUDGET_HEAD.equals(action)) {
                    budgetHeadList = new SalarySlipManager().fetchAllBudgetHead();
                    if (budgetHeadList != null) {
                        BudgetHeadMaster budgetHead = new BudgetHeadMaster();
                        budgetHead.setBudgetHead(budgetHeadList.get(0).getBudgetHead());
                        out.write(new Gson().toJson(new Common().onSuccess(ApplicationConstants.HTTP_STATUS_SUCCESS, ApplicationConstants.BUDGET_HEAD_LIST, budgetHeadList)));
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_SUCCESS);
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.SUCCESS));
                    } else if (fundTypeList == null) {
                        out.write(new Gson().toJson(new Common().LoginonFailure(ApplicationConstants.HTTP_STATUS_NODATA, ApplicationConstants.NO_DATA_FOUND)));
                    } else {
                        request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_FAIL);
                        out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_FAIL, ApplicationConstants.BUDGET_HEAD_LIST_FAILED, budgetHeadList)));
                        logger.info(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.AUTHENTICATION, ApplicationConstants.FAIL));
                    }
                }
            } else {
                request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(new Gson().toJson(ApplicationConstants.HTTP_STATUS_INVALID_SESSION));
            }
        } catch (Exception ex) {
            request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_EXCEPTION);
            out.write(new Gson().toJson(new Common().onFailure(ApplicationConstants.HTTP_STATUS_EXCEPTION, ApplicationConstants.DEPARTMENT_LIST_FAILED, false)));
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            logger.error(Common.getLogMsg("GenerateSalarySlipService", ApplicationConstants.ERROR, stack.toString()));
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
