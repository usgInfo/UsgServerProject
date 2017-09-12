/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.hrms.dto.Employee;
import com.accure.hrms.manager.EmployeeManager;
import com.accure.payroll.dto.EmployeeSuspension;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

/**
 *
 * @author upendra
 */
public class EmployeeSuspensionManager {

    boolean status = false;

    public boolean saveEmpSuspension(EmployeeSuspension empsuspen, String userid, String empid, String empsuspenId) throws Exception {
        User user = new UserManager().fetch(userid);
        String fName = user.getFname();
        if (empsuspen.getIsSuspended()) {
            empsuspen.setCreatedBy(fName);
            empsuspen.setCreateDate(System.currentTimeMillis() + "");
            empsuspen.setStatus(ApplicationConstants.ACTIVE);
            empsuspen.setSuspendDate(empsuspen.getDated());
            String empsuspenJson = new Gson().toJson(empsuspen);

            String empsusId = DBManager.getDbConnection().insert(ApplicationConstants.EMP_SUSPENSION_TABLE, empsuspenJson);

            Employee emp = new EmployeeManager().fetchEmployee(empid);
            emp.setIsSuspended(empsuspen.getIsSuspended());
            emp.setEmpSuspendedId(empsusId);

            status = new EmployeeSuspensionManager().EmpupdateinEmpSuspension(emp, empid, userid);

        } else {
            empsuspen.setUpdatedBy(fName);
            empsuspen.setStatus(ApplicationConstants.ACTIVE);
            empsuspen.setUnSuspendDate(empsuspen.getDated());
            empsuspen.setIsSuspended(empsuspen.getIsSuspended());

            boolean empsusstatus = new EmployeeSuspensionManager().updateEmployeeSuspension(empsuspen, userid, empsuspenId);
            Employee emp = new EmployeeManager().fetchEmployee(empid);
            emp.setIsSuspended(empsuspen.getIsSuspended());
            emp.setEmpSuspendedId("");
            status = new EmployeeSuspensionManager().EmpupdateinEmpSuspension(emp, empid, userid);
        }
        return status;
    }

    public EmployeeSuspension fetchEmployeeSuspension(String rid) throws Exception {
        if (rid == null) {
            return null;
        }
        String EmployeeSuspensionJson = DBManager.getDbConnection().fetch(ApplicationConstants.EMP_SUSPENSION_TABLE, rid);
        List<EmployeeSuspension> EmployeeSuspensionlist = new Gson().fromJson(EmployeeSuspensionJson, new TypeToken<List<EmployeeSuspension>>() {
        }.getType());
        EmployeeSuspension relation = EmployeeSuspensionlist.get(0);
        return relation;
    }

    public boolean updateEmpSuspension(EmployeeSuspension dbRelObj, String pKey) throws Exception {
        String relationjson = new Gson().toJson(dbRelObj);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.EMP_SUSPENSION_TABLE, pKey, relationjson);
        return status;
    }

    public boolean updateEmployeeSuspension(EmployeeSuspension empsus, String userId, String rid) throws Exception {
        EmployeeSuspension dbempObj = fetchEmployeeSuspension(rid);

        dbempObj.setDdo(empsus.getDdo());
        dbempObj.setDated(empsus.getDated());
        dbempObj.setDepartment(empsus.getDepartment());
        dbempObj.setDesignation(empsus.getDesignation());
        dbempObj.setEmployeeCode(empsus.getEmployeeCode());
        dbempObj.setEmployeeCodeM(empsus.getEmployeeCodeM());
        dbempObj.setEmployeeName(empsus.getEmployeeName());
        dbempObj.setLocation(empsus.getLocation());
        dbempObj.setRemarks(empsus.getRemarks());
        dbempObj.setIsSuspended(empsus.getIsSuspended());
        dbempObj.setSuspendDate(dbempObj.getSuspendDate());
        dbempObj.setUnSuspendDate(empsus.getUnSuspendDate());
        dbempObj.setCreateDate(dbempObj.getCreateDate());
        dbempObj.setCreatedBy(dbempObj.getCreatedBy());
        dbempObj.setUpdateDate(System.currentTimeMillis() + "");
        dbempObj.setUpdatedBy(empsus.getUpdatedBy());
        boolean status = updateEmpSuspension(dbempObj, rid);
        return status;
    }

    public boolean EmpupdateinEmpSuspension(Employee newEmployee, String employeeId, String userId) throws Exception {
        Employee previousEmp = new Gson().fromJson(new EmployeeManager().fetch(employeeId), new TypeToken<Employee>() {
        }.getType());
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        previousEmp.setIsSuspended(newEmployee.getIsSuspended());
        previousEmp.setEmpSuspendedId(newEmployee.getEmpSuspendedId());
        previousEmp.setUpdatedBy(userName);
        previousEmp.setUpdateDate(System.currentTimeMillis() + "");
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_TABLE, employeeId, new Gson().toJson(previousEmp));
        if (result) {
            new EmployeeManager().employeeHistory(previousEmp, employeeId);
        }
        return result;
    }
}
