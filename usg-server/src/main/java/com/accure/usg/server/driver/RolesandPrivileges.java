package com.accure.usg.server.driver;

import com.accure.usg.common.dto.Privilege;
import com.accure.usg.common.dto.Role;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author deepak2310
 */
public class RolesandPrivileges {

    public static void main(String[] args) throws Exception {

        createSuperAdminPrivileges();
        createHRMSAdminPrivileges();
        createHRAUserPrivileges();
        createFinancialAdminPrivileges();
        createFinancialUserPrivileges();
        createPayrollAdminPrivileges();
        createPayrollUserPrivileges();
        createBudgetAdminPrivileges();
        createBudgetUserPrivileges();
        createLeaveAdminPrivileges();
        createPensionAdminPrivileges();
    }

    public static void createSuperAdminPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> SuperAdminPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateUser", "UpdateUser", "CreateFinancialYear", "DeleteExistingUser", "ViewFinancialYear", "UpdateFinancialYear", "DeleteFinancialYear",
            "CreateFormula", "ViewFormula", "UpdateFormula", "DeleteFormula", "CreateCategory", "ViewCategory", "UpdateCategory", "DeleteCategory", "CreateRelation", "ViewRelation",
            "UpdateRelation", "DeleteRelation", "CreateMarital", "ViewMarital", "UpdateMarital", "DeleteMarital", "CreateSalutation", "ViewSalutation", "UpdateSalutation", "DeleteSalutation",
            "CreateReligion", "ViewReligion", "UpdateReligion", "DeleteReligion", "CreateNature", "ViewNature", "UpdateNature", "DeleteNature", "CreateLoanNature", "ViewLoanNature",
            "UpdateLoanNature", "DeleteLoanNature", "CreateFundType", "ViewFundType", "UpdateFundType", "DeleteFundType", "CreateBudgetHead", "ViewBudgetHead", "UpdateBudgetHead",
            "DeleteBudgetHead", "CreateFundHeadMapping", "ViewFundHeadMapping", "UpdateFundHeadMapping", "DeleteFundHeadMapping", "CreateDesgFundTypeMapping", "ViewDesgFundTypeMapping",
            "UpdateDesgFundTypeMapping", "DeleteDesgFundTypeMapping", "CreateGadNongad", "ViewGadNongad", "UpdateGadNongad", "DeleteGadNongad", "CreateDDODepartmentMapping",
            "ViewDDODepartmentMapping", "UpdateDDODepartmentMapping", "DeleteDDODepartmentMapping", "CreateChapterVIMaster", "ViewChapterVIMaster", "DeleteChapterVIMaster",
            "UpdateChapterVIMaster", "CreateDeductionType", "ViewDeductionType", "UpdateDeductionType", "DeleteDeductionType", "CreateFixedHead", "ViewFixedHead", "UpdateFixedHead",
            "DeleteFixedHead", "CreateHeadName", "ViewHeadName", "UpdateHeadName", "DeleteHeadName", "CreatePFType", "ViewPFType", "UpdatePFType", "DeletePfType", "CreateBasedOn",
            "ViewBasedOn", "DeleteBasedOn", "UpdateBasedOn", "CreateEmployeeLeftStatus", "ViewEmployeeLeftStatus", "UpdateEmployeeLeftStatus", "DeleteEmployeeLeftStatus",
            "CreateSalaryBillType", "UpdateSalaryBillType", "DeleteSalaryBillType", "ViewSalaryBillType", "CreateParentHead", "ViewParentHead", "UpdateParentHead", "DeleteParentHead",
            "CreateBank", "ViewBank", "UpdateBank", "DeleteBank", "CreateDepartment", "ViewDepartment", "UpdateDepartment", "DeleteDepartment", "CreateSection", "ViewSection",
            "UpdateSection", "DeleteSection", "CreateDiscipline", "ViewDiscipline", "UpdateDiscipline", "DeleteDiscipline", "CreateAssociation", "ViewAssociation", "UpdateAssociation",
            "DeleteAssociation", "CreateCityCategory", "ViewCityCategory", "UpdateCityCtaegory", "DeleteCityCategory", "CreateCity", "ViewCity", "UpdateCity", "DeleteCity",
            "CreateDaMaRate", "ViewDaMaRate", "UpdateDaMaRate", "DeleteDaMaRate", "CreateGISGroup", "ViewGISGroup", "UpdateGISGroup", "DeleteGISGroup", "CreateGrade", "ViewGrade",
            "UpdateGrade", "DeleteGrade", "CreateClass", "ViewClass", "UpdateClass", "DeleteClass", "CreateDesignation", "ViewDesignation", "UpdateDesignation", "DeleteDesignation",
            "CreateSalaryHead", "ViewSalaryHead", "UpdateSalaryHead", "DeleteSalaryHead", "CreateHeadSlab", "ViewHeadSlab", "UpdateHeadSlab", "DeleteHeadSlab", "CreateQuarterCategory",
            "ViewQuarterCategory", "UpdateQuarterCategory", "DeleteQuarterCategory", "CreateQuarter", "ViewQuarter", "UpdateQuarter", "DeleteQuarter", "CreateEmployee", "ViewEmployee",
            "UpdateEmployee", "DeleteEmployee", "CreateEmployeeDemographics", "ViewEmployeeDemographics", "UpdateEmployeeDemographics", "DeleteEmployeeDemographics", "CreateEmployeeJob",
            "ViewEmployeeJob", "UpdateEmployeeJob", "DeleteEmployeeJob", "CreateEmployeeAssign", "ViewEmployeeAsign", "UpdateEmployeeAsign", "DeleteEmployeeAsign", "CreateEmployeeDetails",
            "ViewEmployeeDetails", "UpdateEmployeeDetails", "DeleteEmployeeDetails", "CreateEmployeeSalary", "ViewEmployeeSalary", "DeleteEmployeeSalary", "UpdateEmployeeSalary",
            "ViewSalaryHeadAssign", "CreateFundCategory", "ViewFundCategory", "UpdateFundCategory", "DeleteFundCategory", "CreateGroup", "ViewGroup", "UpdateGroup", "DeleteGroup",
            "CreateMajorCode", "ViewMajorCode", "UpdateMajorCode", "DeleteMajorCode", "CreateMinorCode", "UpdateMinorCode", "ViewMinorCode", "DeleteMinorCode", "CreateSubMajorCode",
            "ViewSubMajorCode", "UpdateSubMajorCode", "DeleteSubMajorCode", "CreateSubMinorCode", "ViewSubMinorCode", "UpdateSubMinorCode", "DeleteSubMinorCode", "CreateGovernmentBudgetHead",
            "ViewGovernmentBudgetHead", "UpdateGovernmentBudgetHead", "DeleteGovernmentBudgetHead", "CreateChequeConfiguation", "ViewChequeConfiguation", "UpdateChequeConfiguation",
            "DeleteChequeConfiguation", "CreateLocation", "ViewLocation", "UpdateLocation", "DeleteLocation", "CreateDDO", "ViewDDO", "UpdateDDO", "DeleteDDO", "CreateBudgetNature",
            "ViewBudgetNature", "UpdateBudgetNature", "DeleteBudgetNature", "CreateUnderGroup", "ViewUnderGroup", "UpdateUnderGroup", "DeleteUnderGroup", "CreateParentLedgerCategory",
            "ViewParentLedgerCategory", "UpdateParentLedgerCategory", "DeleteParentLedgerCategory", "CreateLedgerCategory", "UpdateLedgerCategory", "DeleteLedgerCategory",
            "ViewLedgerCategory", "CreateLedger", "ViewLedger", "UpdateLedger", "DeleteLedger", "CreateOpenBalance", "ViewOpenBalance", "UpdateOpenBalance", "DeleteOpenBalance",
            "CreateStandardNarration", "ViewStandardNarration", "UpdateStandardNarration", "DeleteStandardNarration", "CreateHeadCodeLocMapping", "ViewHeadCodeLocMapping",
            "UpdateHeadCodeLocMapping", "DeleteHeadCodeLocMapping", "ChangeFinancialYear", "CreateFDRProcess", "ViewFDRProcess", "DeleteFDRProcess", "UpdateFDRProcess",
            "CreateFixedDepositDetails", "ViewFixedDepositDetails", "UpdateFixedDepositDetails", "DeleteFixedDepositDetails", "CreateBankReconcilationEntry", "ViewBankReconcilationEntry",
            "UpdateBankReconcilationEntry", "DeleteBankReconcilationEntry", "ViewBankReconcilationReport", "CreateReceiptVoucher", "ViewReceiptVoucher", "UpdateReceiptVoucher",
            "DeleteReceiptVoucher", "CreatePaymentVoucher", "ViewPaymentVoucher", "UpdatePaymentVoucher", "DeletePaymentVoucher", "CreateContraVoucher", "ViewContraVoucher",
            "UpdateContraVoucher", "DeleteContraVoucher", "CreateJournalVoucher", "ViewJournalVoucher", "UpdateJournalVoucher", "DeleteJournalVoucher", "ViewVoucherPosting",
            "VoucherPostingDetails", "SaveVoucherPosting", "ViewVoucherUnPosting", "VoucherUnPostingDetails", "SaveVoucherUnPosting", "SearchBankCheque", "PrintBankCheque",
            "SearchPostSalaryVoucher", "PostSalaryVoucherDetails", "PostSalaryVoucher", "ViewBankBookReport", "PrintBankBookReport", "ViewCashBookReport", "PrintCashBookReport",
            "ViewLedgerBookReport", "PrintLedgerBookReport", "ViewTrialBalanceReport", "PrintTrialBalanceReport", "ViewChequeIssueRegisterReport", "PrintChequeIssueRegisterReport",
            "ViewBudgetRegisterReport", "PrintBudgetRegisterReport", "ViewFDRStatementReport", "PrintFDRStatementReport", "ViewChequeBudgetExpenditureRegisterReport",
            "PrintChequeBudgetExpenditureRegisterReport", "CreateLoanApply", "ViewLoanApply", "UpdateLoanApply", "DeleteLoanApply", "CreateLoanOrder", "ViewLoanOrder", "UpdateLoanOrder",
            "DeleteLoanOrder", "CreateLoanAllotment", "ViewLoanAllotment", "UpdateLoanAllotment", "DeleteLoanAllotment", "CreateLoanRecovery", "ViewLoanRecovery", "UpdateLoanRecovery",
            "DeleteLoanRecovery", "CreateLoanTransaction", "ViewLoanTransaction", "UpdateLoanTransaction", "DeleteLoanTransaction", "CreateInsuranceTransaction", "ViewInsuranceTransaction",
            "UpdateInsuranceTransaction", "DeleteInsuranceTransaction", "CreateEmployeeAttendance", "ViewEmployeeAttendence", "UpdateEmployeeAttendence", "DeleteEmployeeAttendance",
            "CreateAttendanceAdjustment", "ViewAttendanceAdjustment", "UpdateAttendanceAdjustment", "DeleteAttendanceAdjustment", "CreateAutoSalary", "ViewAutoSalary", "UpdateAutoSalary",
            "DeleteAutoSalary", "CreateIncomeTax", "ViewIncomeTax", "UpdateIncomeTax", "DeleteIncomeTax", "CreateArrear", "ViewArrear", "UpdateArrear", "DeleteArrear",
            "CreateArrearConfiguration", "ViewArrearConfiguration", "UpdateArrearConfiguration", "DeleteArrearConfiguration", "CreateArrearAdjustment", "ViewArrearAdjustment",
            "UpdateArrearAdjustment", "DeleteArrearAdjustment", "CreateEmployeePromotion", "ViewEmployeePromotion", "UpdateEmployeePromotion", "DeleteEmployeePromotion", "CreatePayStopped", "ViewPayStopped", "UpdatePayStopped",
            "DeletePayStopped", "CreateQuarterTransaction", "ViewQuarterTransaction", "UpdateQuarterTransaction", "DeleteQuarterTransaction", "CreateSalaryIncrement", "ViewSalaryIncrement",
            "UpdateSalaryIncrement", "DeleteSalaryIncrement", "CreateSalaryBill", "ViewSalaryBill", "UpdateSalaryBill", "DeleteSalaryBill", "CreateArrearBill", "ViewArrearBill",
            "UpdateArrearBill", "DeleteArrearBill", "CreateTransferForm", "ViewTransferForm", "UpdateTransferForm", "DeleteTransferForm", "ViewSalarySlip", "ViewDeductionView",
            "ViewBankStatement", "ViewLoanOrderStatement", "ViewEmployeeList", "ViewSalaryRegisterDiff", "ViewSalarySlipMail", "ViewMonthWiseRegister", "ViewEmployeeDeduction",
            "ViewSalaryStatus", "ViewQuarterList", "ViewQuarterTransactionList", "ViewMastersList", "ViewSalarySummary", "ViewArrearReport", "EmployeeView", "EmployeeImport", "EmployeeExport",
            "SalaryView", "SalaryExport", "HeadView", "HeadExport", "HeadImport", "CreateBudgetType", "ViewBudgetType", "UpdateBudgetType", "DeleteBudgetType", "CreateSector", "ViewSector",
            "UpdateSector", "DeleteSector", "CreateIncomeBudget", "ViewIncomeBudget", "UpdateIncomeBudget", "DeleteIncomeBudget", "CreateBudgetConsolidatedIncome",
            "ViewBudgetConsolidatedIncome", "UpdateBudgetConsolidatedIncome", "DeleteBudgetConsolidateIncome", "CreateIncomeBudgetUniversity", "ViewIncomeBudgetUniversity",
            "UpdateIncomeBudgetUniversity", "DeleteIncomeBudgetUniversity", "CreateLocationBudgetAllocation", "ViewLocationBudgetAllocation", "UpdateLocationBudgetAllocation",
            "DeleteLocationBudgetAllocation", "CreateBudgetExpenses", "ViewBudgetExpenses", "UpdateBudgetExpenses", "DeleteBudgetExpenses", "CreateBudgetConsolidatedExpenses",
            "ViewBudgetConsolidatedExpenses", "UpdateBudgetConsolidatedExpenses", "DeleteBudgetConsolidatedExpenses", "CreateBugetExpensesUniversity", "ViewBugetExpensesUniversity",
            "UpdateBugetExpensesUniversity", "DeleteBugetExpensesUniversity", "ViewLocationWiseExpenseBudget", "ViewBudgetApprovalDDO", "ViewBudgetReAppropriation", "BudgetReportView",
            "CreateCommonHolidays", "ViewCommonHolidays", "UpdateCommonHolidays", "DeleteCommonHolidays", "CreateHolidays", "ViewHolidays", "UpdateHoliday", "DeleteHoliday",
            "CreateLocationHolidays", "ViewLocationHolidays", "UpdateLocationHolidays", "DeleteLocationHolidays", "CreateWeekOffMaster", "ViewWeekOffMaster", "UpdateWeekOffMaster",
            "DeleteWeekOffMaster", "CreateLeaveTypeMasters", "ViewLeaveTypeMasters", "UpdateLeaveTypeMasters", "DeleteLeaveTypeMaster", "CreateLeaveAssignment", "ViewLeaveAssignment",
            "UpdateLeaveAssignment", "DeleteLeaveAssignment", "CreateLeaveTransaction", "ViewLeaveTransaction", "UpdateLeaveTransaction", "DeleteLeaveTransaction", "CreateLeaveEncashment",
            "ViewLeaveEncashment", "UpdateLeaveEncashment", "DeleteLeaveEncashment", "CreateLeaveForward", "ViewLeaveForward", "UpdateLeaveForward", "DeleteLeaveForward",
            "CreateLeaveWorkFlow", "ViewLeaveWorkFlow", "UpdateLeaveWorkFlow", "DeleteLeaveWorkFlow", "CreateLeaveRequest", "ViewLeaveRequest", "UpdateLeaveRequest",
            "DeleteLeaveRequest", "CreateLeaveApproval", "ViewLeaveApproval", "UpdateLeaveApproval", "DeleteLeaveApproval", "CreateLeaveAdjustmentRequest", "ViewLeaveAdjustmentRequest",
            "UpdateLeaveAdjustmentRequest", "DeleteLeaveAdjustmentRequest", "CreateLeaveAdjustmentApproval", "ViewLeaveAdjustmentApproval", "UpdateLeaveAdjustmentApproval",
            "DeleteLeaveAdjustmentApproval", "ViewLeaveTransactionReport", "CreatePensionBank", "ViewPensionBank", "UpdatePensionBank", "DeletePensionBank", "CreatePensionConfiguration",
            "ViewPensionConfiguration", "UpdatePensionConfiguration", "DeletePensionConfiguration", "CreatePensionFormula", "ViewPensionFormula", "UpdatePensionFormula",
            "DeletePensionFormula", "CreateAssignPensionHead", "ViewAssignPensionHead", "UpdateAssignPensionHead", "DeleteAssignPensionHead", "CreatePensionArrearConf",
            "ViewPensionArrearConf", "UpdatePensionArrearConf", "DeletePensionArrearConf", "CreatePensionEmployee", "ViewPensionEmployee", "UpdatePensionEmployee",
            "DeletePensionEmployee", "ViewProcessPension", "ProcessPension", "UnProcessPension", "ViewPensionSalaryDetails", "SavePensionSalaryDetails", "ViewPensionArrear",
            "ProcessPensionArrear", "UnProcessPensionArrear", "ViewPensionArrearAdjustment", "UpdatePensionArrearAdjustment", "UpdatePensionRevision", "SavePensionRevision",
            "EditPensionRevision", "DeletePensionRevision", "ViewPensionBankReport", "ViewPensionBankList", "ViewPensionYear", "ViewGovernmentLedgerCode",
            "UpdateGovernmentLedgerCode", "DeleteGovernmentLedgerCode", "GovernmentLedgerCode", "LedgerCode", "ViewLedgerCode", "UpdateLedgerCode", "DeleteLedgerCode", "ViewLocationWiseHolidaysMaster", "CreateLocationWiseHolidaysMaster", "DeleteLocationWiseHolidaysMaster", "UpdateLocationWiseHolidaysMaster", "CreateDDOLocationMapping", "UpdateDDOLocationMapping", "DeleteDDOLocationMapping", "ViewDDOLocationMapping", "ViewUserList","CreateBankName","ViewBankName","UpdateBankName","DeleteBankName","DeleteEmployeePromotion","UpdateEmployeePromotion"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            SuperAdminPrivilege.add(privilege);
        }

        //Role will create here
        Role SuperAdminRole = new Role();
        SuperAdminRole.setRoleName(ApplicationConstants.ROLE_SUPER_ADMIN);
        SuperAdminRole.setStatus(ApplicationConstants.ACTIVE);
        SuperAdminRole.setPrivilege(SuperAdminPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(SuperAdminRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + SuperAdminRole.getRoleName() + "\" Role\n");
        }
    }

    public static void createHRMSAdminPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> AdminPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateUser", "UpdateUser", "DeleteExistingUser", "CreateFinancialYear", "ViewFinancialYear", "UpdateFinancialYear", "DeleteFinancialYear",
            "CreateFormula", "ViewFormula", "UpdateFormula", "DeleteFormula", "CreateCategory", "ViewCategory", "UpdateCategory", "DeleteCategory", "CreateRelation", "ViewRelation",
            "UpdateRelation", "DeleteRelation", "CreateMarital", "ViewMarital", "UpdateMarital", "DeleteMarital", "CreateSalutation", "ViewSalutation", "UpdateSalutation", "DeleteSalutation",
            "CreateReligion", "ViewReligion", "UpdateReligion", "DeleteReligion", "CreateNature", "ViewNature", "UpdateNature", "DeleteNature", "CreateLoanNature", "ViewLoanNature",
            "UpdateLoanNature", "DeleteLoanNature", "CreateFundType", "ViewFundType", "UpdateFundType", "DeleteFundType", "CreateBudgetHead", "ViewBudgetHead", "UpdateBudgetHead",
            "DeleteBudgetHead", "CreateFundHeadMapping", "ViewFundHeadMapping", "UpdateFundHeadMapping", "DeleteFundHeadMapping", "CreateDesgFundTypeMapping", "ViewDesgFundTypeMapping",
            "UpdateDesgFundTypeMapping", "DeleteDesgFundTypeMapping", "CreateGadNongad", "ViewGadNongad", "UpdateGadNongad", "DeleteGadNongad", "CreateDDODepartmentMapping",
            "ViewDDODepartmentMapping", "UpdateDDODepartmentMapping", "DeleteDDODepartmentMapping", "CreateChapterVIMaster", "ViewChapterVIMaster", "DeleteChapterVIMaster",
            "UpdateChapterVIMaster", "CreateDeductionType", "ViewDeductionType", "UpdateDeductionType", "DeleteDeductionType", "CreateFixedHead", "ViewFixedHead", "UpdateFixedHead",
            "DeleteFixedHead", "CreateHeadName", "ViewHeadName", "UpdateHeadName", "DeleteHeadName", "CreatePFType", "ViewPFType", "UpdatePFType", "DeletePfType", "CreateBasedOn",
            "ViewBasedOn", "DeleteBasedOn", "UpdateBasedOn", "CreateEmployeeLeftStatus", "ViewEmployeeLeftStatus", "UpdateEmployeeLeftStatus", "DeleteEmployeeLeftStatus",
            "CreateSalaryBillType", "UpdateSalaryBillType", "DeleteSalaryBillType", "ViewSalaryBillType", "CreateParentHead", "ViewParentHead", "UpdateParentHead", "DeleteParentHead",
            "CreateBank", "ViewBank", "UpdateBank", "DeleteBank", "CreateDepartment", "ViewDepartment", "UpdateDepartment", "DeleteDepartment", "CreateSection", "ViewSection",
            "UpdateSection", "DeleteSection", "CreateDiscipline", "ViewDiscipline", "UpdateDiscipline", "DeleteDiscipline", "CreateAssociation", "ViewAssociation", "UpdateAssociation",
            "DeleteAssociation", "CreateCityCategory", "ViewCityCategory", "UpdateCityCtaegory", "DeleteCityCategory", "CreateCity", "ViewCity", "UpdateCity", "DeleteCity",
            "CreateDaMaRate", "ViewDaMaRate", "UpdateDaMaRate", "DeleteDaMaRate", "CreateGISGroup", "ViewGISGroup", "UpdateGISGroup", "DeleteGISGroup", "CreateGrade", "ViewGrade",
            "UpdateGrade", "DeleteGrade", "CreateClass", "ViewClass", "UpdateClass", "DeleteClass", "CreateDesignation", "ViewDesignation", "UpdateDesignation", "DeleteDesignation",
            "CreateSalaryHead", "ViewSalaryHead", "UpdateSalaryHead", "DeleteSalaryHead", "CreateHeadSlab", "ViewHeadSlab", "UpdateHeadSlab", "DeleteHeadSlab", "CreateQuarterCategory",
            "ViewQuarterCategory", "UpdateQuarterCategory", "DeleteQuarterCategory", "CreateQuarter", "ViewQuarter", "UpdateQuarter", "DeleteQuarter", "CreateEmployee", "ViewEmployee",
            "UpdateEmployee", "DeleteEmployee", "CreateEmployeeDemographics", "ViewEmployeeDemographics", "UpdateEmployeeDemographics", "DeleteEmployeeDemographics", "CreateEmployeeJob",
            "ViewEmployeeJob", "UpdateEmployeeJob", "DeleteEmployeeJob", "CreateEmployeeAssign", "ViewEmployeeAsign", "UpdateEmployeeAsign", "DeleteEmployeeAsign", "CreateEmployeeDetails",
            "ViewEmployeeDetails", "UpdateEmployeeDetails", "DeleteEmployeeDetails", "CreateEmployeeSalary", "ViewEmployeeSalary", "DeleteEmployeeSalary", "UpdateEmployeeSalary",
            "ViewSalaryHeadAssign", "ChangeFinancialYear", "CreateLocation", "ViewLocation", "ViewUserList", "UpdateLocation", "DeleteLocation", "CreateDDO", "ViewDDO", "UpdateDDO", "DeleteDDO", "CreateDDOLocationMapping", "UpdateDDOLocationMapping", "DeleteDDOLocationMapping", "ViewDDOLocationMapping","CreateGovernmentBudgetHead"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            AdminPrivilege.add(privilege);
        }

        //Role will create here
        Role AdminRole = new Role();
        AdminRole.setRoleName(ApplicationConstants.ROLE_HRMS_ADMIN);
        AdminRole.setStatus(ApplicationConstants.ACTIVE);
        AdminRole.setPrivilege(AdminPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(AdminRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + AdminRole.getRoleName() + "\" Role\n");
        }
    }

    public static void createHRAUserPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> HRAdminPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateEmployee", "ViewEmployee", "UpdateEmployee", "DeleteEmployee", "CreateEmployeeDemographics",
            "ViewEmployeeDemographics", "UpdateEmployeeDemographics", "DeleteEmployeeDemographics", "CreateEmployeeJob", "ViewEmployeeJob", "UpdateEmployeeJob",
            "DeleteEmployeeJob", "CreateEmployeeAssign", "ViewEmployeeAsign", "UpdateEmployeeAsign", "DeleteEmployeeAsign", "CreateEmployeeDetails", "ViewEmployeeDetails",
            "UpdateEmployeeDetails", "DeleteEmployeeDetails", "CreateEmployeeSalary", "ViewEmployeeSalary", "DeleteEmployeeSalary", "UpdateEmployeeSalary",
            "ViewSalaryHeadAssign", "ChangeFinancialYear", "ViewUserList"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            HRAdminPrivilege.add(privilege);
        }

        //Role will create here
        Role HRAdminRole = new Role();
        HRAdminRole.setRoleName(ApplicationConstants.ROLE_HRMS_USER);
        HRAdminRole.setStatus(ApplicationConstants.ACTIVE);
        HRAdminRole.setPrivilege(HRAdminPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(HRAdminRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + HRAdminRole.getRoleName() + "\" Role\n");
        }
    }

    public static void createFinancialAdminPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> HRUserPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateUser", "ViewUserList", "UpdateUser", "DeleteExistingUser", "CreateFinancialYear", "ViewFinancialYear", "UpdateFinancialYear",
            "DeleteFinancialYear", "CreateFundType", "ViewFundTyper", "UpdateFundType", "DeleteFundType", "CreateFundCategory", "ViewFundCategory", "UpdateFundCategory",
            "DeleteFundCategory", "CreateFundHeadMapping", "ViewFundHeadMapping", "UpdateFundHeadMapping", "DeleteFundHeadMapping", "CreateGroup", "ViewGroup", "UpdateGroup",
            "DeleteGroup", "CreateMajorCode", "ViewMajorCode", "UpdateMajorCode", "DeleteMajorCode", "CreateMinorCode", "UpdateMinorCode", "ViewMinorCode", "DeleteMinorCode",
            "CreateSubMajorCode", "ViewSubMajorCode", "UpdateSubMajorCode", "DeleteSubMajorCode", "CreateSubMinorCode", "ViewSubMinorCode", "UpdateSubMinorCode", "DeleteSubMinorCode",
            "CreateGovernmentBudgetHead", "ViewGovernmentBudgetHead", "UpdateGovernmentBudgetHead", "DeleteGovernmentBudgetHead", "CreateBudgetHead", "ViewBudgetHead",
            "UpdateBudgetHead", "DeleteBudgetHead", "CreateChequeConfiguation", "ViewChequeConfiguation", "UpdateChequeConfiguation", "DeleteChequeConfiguation", "CreateBank",
            "ViewBank", "UpdateBank", "DeleteBank", "CreateLocation", "ViewLocation", "UpdateLocation", "DeleteLocation", "CreateDDO", "ViewDDO", "UpdateDDO", "DeleteDDO",
            "CreateBudgetNature", "ViewBudgetNature", "UpdateBudgetNature", "DeleteBudgetNature", "CreateUnderGroup", "ViewUnderGroup", "UpdateUnderGroup", "DeleteUnderGroup",
            "CreateParentLedgerCategory", "ViewParentLedgerCategory", "UpdateParentLedgerCategory", "DeleteParentLedgerCategory", "CreateLedgerCategory", "UpdateLedgerCategory",
            "DeleteLedgerCategory", "ViewLedgerCategory", "CreateLedger", "ViewLedger", "UpdateLedger", "DeleteLedger", "CreateOpenBalance", "ViewOpenBalance", "UpdateOpenBalance",
            "DeleteOpenBalance", "CreateStandardNarration", "ViewStandardNarration", "UpdateStandardNarration", "DeleteStandardNarration", "CreateHeadCodeLocMapping",
            "ViewHeadCodeLocMapping", "UpdateHeadCodeLocMapping", "DeleteHeadCodeLocMapping", "ChangeFinancialYear", "CreateFDRProcess", "ViewFDRProcess", "DeleteFDRProcess",
            "UpdateFDRProcess", "CreateFixedDepositDetails", "ViewFixedDepositDetails", "UpdateFixedDepositDetails", "DeleteFixedDepositDetails", "CreateBankReconcilationEntry",
            "ViewBankReconcilationEntry", "UpdateBankReconcilationEntry", "DeleteBankReconcilationEntry", "ViewBankReconcilationReport", "CreateReceiptVoucher", "ViewReceiptVoucher",
            "UpdateReceiptVoucher", "DeleteReceiptVoucher", "CreatePaymentVoucher", "ViewPaymentVoucher", "UpdatePaymentVoucher", "DeletePaymentVoucher", "CreateContraVoucher",
            "ViewContraVoucher", "UpdateContraVoucher", "DeleteContraVoucher", "CreateJournalVoucher", "ViewJournalVoucher", "UpdateJournalVoucher", "DeleteJournalVoucher",
            "ViewVoucherPosting", "VoucherPostingDetails", "SaveVoucherPosting", "ViewVoucherUnPosting", "VoucherUnPostingDetails", "SaveVoucherUnPosting", "SearchBankCheque",
            "PrintBankCheque", "SearchPostSalaryVoucher", "PostSalaryVoucherDetails", "PostSalaryVoucher", "ViewBankBookReport", "PrintBankBookReport", "ViewCashBookReport",
            "PrintCashBookReport", "ViewLedgerBookReport", "PrintLedgerBookReport", "ViewTrialBalanceReport", "PrintTrialBalanceReport", "ViewChequeIssueRegisterReport",
            "PrintChequeIssueRegisterReport", "ViewBudgetRegisterReport", "PrintBudgetRegisterReport", "ViewFDRStatementReport", "PrintFDRStatementReport",
            "ViewChequeBudgetExpenditureRegisterReport", "PrintChequeBudgetExpenditureRegisterReport", "ViewGovernmentLedgerCode", "UpdateGovernmentLedgerCode",
            "DeleteGovernmentLedgerCode", "GovernmentLedgerCode", "LedgerCode", "ViewLedgerCode", "UpdateLedgerCode", "DeleteLedgerCode", "CreateDDOLocationMapping", "UpdateDDOLocationMapping", "DeleteDDOLocationMapping", "ViewDDOLocationMapping","CreateBankName","ViewBankName","UpdateBankName","DeleteBankName"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            HRUserPrivilege.add(privilege);
        }

        //Role will create here
        Role HRUserRole = new Role();
        HRUserRole.setRoleName(ApplicationConstants.ROLE_FINANCIAL_ADMIN);
        HRUserRole.setStatus(ApplicationConstants.ACTIVE);
        HRUserRole.setPrivilege(HRUserPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(HRUserRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + HRUserRole.getRoleName() + "\" Role\n");
        }

    }
//

    public static void createFinancialUserPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> HRAuthorizedUserPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateBank", "ViewBank", "UpdateBank", "DeleteBank", "ChangeFinancialYear", "CreateFDRProcess", "ViewFDRProcess",
            "DeleteFDRProcess", "UpdateFDRProcess", "CreateFixedDepositDetails", "ViewFixedDepositDetails", "UpdateFixedDepositDetails", "DeleteFixedDepositDetails",
            "CreateBankReconcilationEntry", "ViewBankReconcilationEntry", "UpdateBankReconcilationEntry", "DeleteBankReconcilationEntry", "ViewBankReconcilationReport",
            "CreateReceiptVoucher", "ViewReceiptVoucher", "UpdateReceiptVoucher", "DeleteReceiptVoucher", "CreatePaymentVoucher", "ViewPaymentVoucher", "UpdatePaymentVoucher",
            "DeletePaymentVoucher", "CreateContraVoucher", "ViewContraVoucher", "UpdateContraVoucher", "DeleteContraVoucher", "CreateJournalVoucher", "ViewJournalVoucher",
            "UpdateJournalVoucher", "DeleteJournalVoucher", "ViewVoucherPosting", "VoucherPostingDetails", "SaveVoucherPosting", "ViewVoucherUnPosting", "VoucherUnPostingDetails",
            "SaveVoucherUnPosting", "SearchBankCheque", "PrintBankCheque", "SearchPostSalaryVoucher", "PostSalaryVoucherDetails", "PostSalaryVoucher", "ViewBankBookReport",
            "PrintBankBookReport", "ViewCashBookReport", "PrintCashBookReport", "ViewLedgerBookReport", "PrintLedgerBookReport", "ViewTrialBalanceReport", "PrintTrialBalanceReport",
            "ViewChequeIssueRegisterReport", "PrintChequeIssueRegisterReport", "ViewBudgetRegisterReport", "PrintBudgetRegisterReport", "ViewFDRStatementReport", "PrintFDRStatementReport",
            "ViewChequeBudgetExpenditureRegisterReport", "PrintChequeBudgetExpenditureRegisterReport", "ViewUserList"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            HRAuthorizedUserPrivilege.add(privilege);
        }

        //Role will create here
        Role HRAuthorizedUserRole = new Role();
        HRAuthorizedUserRole.setRoleName(ApplicationConstants.ROLE_FINANCIAL_USER);
        HRAuthorizedUserRole.setStatus(ApplicationConstants.ACTIVE);
        HRAuthorizedUserRole.setPrivilege(HRAuthorizedUserPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(HRAuthorizedUserRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + HRAuthorizedUserRole.getRoleName() + "\" Role\n");
        }

    }
//

    public static void createPayrollAdminPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> FinancialAdminPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateUser", "ViewUserList", "UpdateUser", "DeleteExistingUser", "CreateFinancialYear", "ViewFinancialYear", "UpdateFinancialYear", "DeleteFinancialYear",
            "CreateLoanApply", "ViewLoanApply", "UpdateLoanApply", "DeleteLoanApply", "CreateLoanOrder", "ViewLoanOrder", "UpdateLoanOrder", "DeleteLoanOrder",
            "CreateLoanAllotment", "ViewLoanAllotment", "UpdateLoanAllotment", "DeleteLoanAllotment", "CreateLoanRecovery", "ViewLoanRecovery", "UpdateLoanRecovery",
            "DeleteLoanRecovery", "CreateLoanTransaction", "ViewLoanTransaction", "UpdateLoanTransaction", "DeleteLoanTransaction", "CreateInsuranceTransaction",
            "ViewInsuranceTransaction", "UpdateInsuranceTransaction", "DeleteInsuranceTransaction", "CreateEmployeeAttendance", "ViewEmployeeAttendence", "UpdateEmployeeAttendence",
            "DeleteEmployeeAttendance", "CreateAttendanceAdjustment", "ViewAttendanceAdjustment", "UpdateAttendanceAdjustment", "DeleteAttendanceAdjustment", "CreateAutoSalary",
            "ViewAutoSalary", "UpdateAutoSalary", "DeleteAutoSalary", "CreateIncomeTax", "ViewIncomeTax", "UpdateIncomeTax", "DeleteIncomeTax", "CreateArrear", "ViewArrear",
            "UpdateArrear", "DeleteArrear", "CreateArrearConfiguration", "ViewArrearConfiguration", "UpdateArrearConfiguration", "DeleteArrearConfiguration", "CreateArrearAdjustment",
            "ViewArrearAdjustment", "UpdateArrearAdjustment", "DeleteArrearAdjustment", "CreateEmployeePromotion", "ViewEmployeePromotion", "DeleteEmployeePromotion", "UpdateEmployeePromotion", "CreatePayStopped", "ViewPayStopped",
            "UpdatePayStopped", "DeletePayStopped", "CreateQuarterTransaction", "ViewQuarterTransaction", "UpdateQuarterTransaction", "DeleteQuarterTransaction",
            "CreateSalaryIncrement", "ViewSalaryIncrement", "UpdateSalaryIncrement", "DeleteSalaryIncrement", "CreateSalaryBill", "ViewSalaryBill", "UpdateSalaryBill",
            "DeleteSalaryBill", "CreateArrearBill", "ViewArrearBill", "UpdateArrearBill", "DeleteArrearBill", "CreateTransferForm", "ViewTransferForm", "UpdateTransferForm",
            "DeleteTransferForm", "ViewSalarySlip", "ViewDeductionView", "ViewBankStatement", "ViewLoanOrderStatement", "ViewEmployeeList", "ViewSalaryRegisterDiff",
            "ViewSalarySlipMail", "ViewMonthWiseRegister", "ViewEmployeeDeduction", "ViewSalaryStatus", "ViewQuarterList", "ViewQuarterTransactionList", "ViewMastersList",
            "ViewSalarySummary", "ViewArrearReport", "ViewGISGroup", "EmployeeView", "EmployeeImport", "EmployeeExport", "SalaryView", "SalaryExport", "HeadView", "HeadExport",
            "HeadImport", "ChangeFinancialYear","DeleteEmployeePromotion","UpdateEmployeePromotion"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            FinancialAdminPrivilege.add(privilege);
        }

        //Role will create here
        Role FinancialAdminRole = new Role();
        FinancialAdminRole.setRoleName(ApplicationConstants.ROLE_PAYROLL_ADMIN);
        FinancialAdminRole.setStatus(ApplicationConstants.ACTIVE);
        FinancialAdminRole.setPrivilege(FinancialAdminPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(FinancialAdminRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + FinancialAdminRole.getRoleName() + "\" Role\n");
        }

    }
//

    public static void createPayrollUserPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> FinancialUserPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateLoanApply", "ViewLoanApply", "UpdateLoanApply", "DeleteLoanApply", "CreateLoanOrder", "ViewLoanOrder",
            "UpdateLoanOrder", "DeleteLoanOrder", "CreateLoanAllotment", "ViewLoanAllotment", "UpdateLoanAllotment", "DeleteLoanAllotment", "CreateLoanRecovery", "ViewLoanRecovery",
            "UpdateLoanRecovery", "DeleteLoanRecovery", "CreateLoanTransaction", "ViewLoanTransaction", "UpdateLoanTransaction", "DeleteLoanTransaction", "CreateInsuranceTransaction",
            "ViewInsuranceTransaction", "UpdateInsuranceTransaction", "DeleteInsuranceTransaction", "CreateEmployeeAttendance", "ViewEmployeeAttendence", "UpdateEmployeeAttendence",
            "DeleteEmployeeAttendance", "CreateAttendanceAdjustment", "ViewAttendanceAdjustment", "UpdateAttendanceAdjustment", "DeleteAttendanceAdjustment", "CreateAutoSalary",
            "ViewAutoSalary", "UpdateAutoSalary", "DeleteAutoSalary", "CreateIncomeTax", "ViewIncomeTax", "UpdateIncomeTax", "DeleteIncomeTax", "CreateArrear", "ViewArrear", "UpdateArrear",
            "DeleteArrear", "CreateArrearConfiguration", "ViewArrearConfiguration", "UpdateArrearConfiguration", "DeleteArrearConfiguration", "CreateArrearAdjustment",
            "ViewArrearAdjustment", "UpdateArrearAdjustment", "DeleteArrearAdjustment", "CreateEmployeePromotion", "ViewEmployeePromotion", "DeleteEmployeePromotion", "UpdateEmployeePromotion", "CreatePayStopped", "ViewPayStopped",
            "UpdatePayStopped", "DeletePayStopped", "CreateQuarterTransaction", "ViewQuarterTransaction", "UpdateQuarterTransaction", "DeleteQuarterTransaction", "CreateSalaryIncrement",
            "ViewSalaryIncrement", "UpdateSalaryIncrement", "DeleteSalaryIncrement", "CreateSalaryBill", "ViewSalaryBill", "UpdateSalaryBill", "DeleteSalaryBill", "CreateArrearBill",
            "ViewArrearBill", "UpdateArrearBill", "DeleteArrearBill", "CreateTransferForm", "ViewTransferForm", "UpdateTransferForm", "DeleteTransferForm", "ViewSalarySlip",
            "ViewDeductionView", "ViewBankStatement", "ViewLoanOrderStatement", "ViewEmployeeList", "ViewSalaryRegisterDiff", "ViewSalarySlipMail", "ViewMonthWiseRegister",
            "ViewEmployeeDeduction", "ViewSalaryStatus", "ViewQuarterList", "ViewQuarterTransactionList", "ViewMastersList", "ViewSalarySummary", "ViewArrearReport", "ViewGISGroup", "ChangeFinancialYear", "ViewUserList"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            FinancialUserPrivilege.add(privilege);
        }

        //Role will create here
        Role FinancialUserRole = new Role();
        FinancialUserRole.setRoleName(ApplicationConstants.ROLE_PAYROLL_USER);
        FinancialUserRole.setStatus(ApplicationConstants.ACTIVE);
        FinancialUserRole.setPrivilege(FinancialUserPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(FinancialUserRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + FinancialUserRole.getRoleName() + "\" Role\n");
        }
    }

    public static void createBudgetAdminPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> FinancialUserPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateUser", "ViewUserList", "UpdateUser", "DeleteExistingUser", "CreateFinancialYear", "ViewFinancialYear", "UpdateFinancialYear", "DeleteFinancialYear",
            "CreateFundType", "ViewFundTyper", "UpdateFundType", "DeleteFundType", "CreateFundCategory", "ViewFundCategory", "UpdateFundCategory", "DeleteFundCategory",
            "CreateFundHeadMapping", "ViewFundHeadMapping", "UpdateFundHeadMapping", "DeleteFundHeadMapping", "CreateMajorCode", "ViewMajorCode", "UpdateMajorCode", "DeleteMajorCode",
            "CreateMinorCode", "UpdateMinorCode", "ViewMinorCode", "DeleteMinorCode", "CreateSubMajorCode", "ViewSubMajorCode", "UpdateSubMajorCode", "DeleteSubMajorCode",
            "CreateSubMinorCode", "ViewSubMinorCode", "UpdateSubMinorCode", "DeleteSubMinorCode", "CreateGovernmentHeadCode", "ViewGovernmentHeadCode", "UpdateGovernmentHeadCode",
            "DeleteGovernmentHeadCode", "CreateHeadCode", "ViewHeadCode", "UpdateHeadCode", "DeleteHeadCode", "CreateBudgetType", "ViewBudgetType", "UpdateBudgetType",
            "DeleteBudgetType", "CreateSector", "ViewSector", "UpdateSector", "DeleteSector", "CreateIncomeBudget", "ViewIncomeBudget", "UpdateIncomeBudget", "DeleteIncomeBudget",
            "CreateBudgetConsolidatedIncome", "ViewBudgetConsolidatedIncome", "UpdateBudgetConsolidatedIncome", "DeleteBudgetConsolidateIncome", "CreateIncomeBudgetUniversity",
            "ViewIncomeBudgetUniversity", "UpdateIncomeBudgetUniversity", "DeleteIncomeBudgetUniversity", "CreateLocationBudgetAllocation", "ViewLocationBudgetAllocation",
            "UpdateLocationBudgetAllocation", "DeleteLocationBudgetAllocation", "CreateBudgetExpenses", "ViewBudgetExpenses", "UpdateBudgetExpenses", "DeleteBudgetExpenses",
            "CreateBudgetConsolidatedExpenses", "ViewBudgetConsolidatedExpenses", "UpdateBudgetConsolidatedExpenses", "DeleteBudgetConsolidatedExpenses", "CreateBugetExpensesUniversity",
            "ViewBugetExpensesUniversity", "UpdateBugetExpensesUniversity", "DeleteBugetExpensesUniversity", "ViewLocationWiseExpenseBudget", "ViewBudgetApprovalDDO",
            "ViewBudgetReAppropriation", "BudgetReportView", "ChangeFinancialYear","CreateGovernmentBudgetHead"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            FinancialUserPrivilege.add(privilege);
        }

        //Role will create here
        Role FinancialUserRole = new Role();
        FinancialUserRole.setRoleName(ApplicationConstants.ROLE_BUDGET_ADMIN);
        FinancialUserRole.setStatus(ApplicationConstants.ACTIVE);
        FinancialUserRole.setPrivilege(FinancialUserPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(FinancialUserRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + FinancialUserRole.getRoleName() + "\" Role\n");
        }
    }

    public static void createBudgetUserPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> FinancialUserPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateIncomeBudget", "ViewIncomeBudget", "UpdateIncomeBudget", "DeleteIncomeBudget", "CreateBudgetConsolidatedIncome",
            "ViewBudgetConsolidatedIncome", "UpdateBudgetConsolidatedIncome", "DeleteBudgetConsolidateIncome", "CreateIncomeBudgetUniversity", "ViewIncomeBudgetUniversity",
            "UpdateIncomeBudgetUniversity", "DeleteIncomeBudgetUniversity", "CreateLocationBudgetAllocation", "ViewLocationBudgetAllocation", "UpdateLocationBudgetAllocation",
            "DeleteLocationBudgetAllocation", "CreateBudgetExpenses", "ViewBudgetExpenses", "UpdateBudgetExpenses", "DeleteBudgetExpenses", "CreateBudgetConsolidatedExpenses",
            "ViewBudgetConsolidatedExpenses", "UpdateBudgetConsolidatedExpenses", "DeleteBudgetConsolidatedExpenses", "CreateBugetExpensesUniversity", "ViewBugetExpensesUniversity",
            "UpdateBugetExpensesUniversity", "DeleteBugetExpensesUniversity", "ViewLocationWiseExpenseBudget", "ViewBudgetApprovalDDO", "ViewBudgetReAppropriation", "BudgetReportView", "ChangeFinancialYear", "ViewUserList"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            FinancialUserPrivilege.add(privilege);
        }

        //Role will create here
        Role FinancialUserRole = new Role();
        FinancialUserRole.setRoleName(ApplicationConstants.ROLE_BUDGET_USER);
        FinancialUserRole.setStatus(ApplicationConstants.ACTIVE);
        FinancialUserRole.setPrivilege(FinancialUserPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(FinancialUserRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + FinancialUserRole.getRoleName() + "\" Role\n");
        }
    }

    public static void createLeaveAdminPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> FinancialUserPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateUser", "ViewUserList", "UpdateUser", "DeleteExistingUser", "CreateFinancialYear", "ViewFinancialYear", "UpdateFinancialYear", "DeleteFinancialYear",
            "CreateCommonHolidays", "ViewCommonHolidays", "UpdateCommonHolidays", "DeleteCommonHolidays", "CreateHolidays", "ViewHolidays", "UpdateHoliday", "DeleteHoliday",
            "CreateLocationHolidays", "ViewLocationHolidays", "UpdateLocationHolidays", "DeleteLocationHolidays", "CreateWeekOffMaster", "ViewWeekOffMaster", "UpdateWeekOffMaster",
            "DeleteWeekOffMaster", "CreateLeaveTypeMasters", "ViewLeaveTypeMasters", "UpdateLeaveTypeMasters", "DeleteLeaveTypeMaster", "CreateLeaveAssignment", "ViewLeaveAssignment",
            "UpdateLeaveAssignment", "DeleteLeaveAssignment", "CreateLeaveTransaction", "ViewLeaveTransaction", "UpdateLeaveTransaction", "DeleteLeaveTransaction", "CreateLeaveTransaction",
            "ViewLeaveTransaction", "UpdateLeaveTransaction", "DeleteLeaveTransaction", "CreateLeaveEncashment", "ViewLeaveEncashment", "UpdateLeaveEncashment", "DeleteLeaveEncashment",
            "CreateLeaveForward", "ViewLeaveForward", "UpdateLeaveForward", "DeleteLeaveForward", "CreateLeaveWorkFlow", "ViewLeaveWorkFlow", "UpdateLeaveWorkFlow", "DeleteLeaveWorkFlow",
            "CreateLeaveRequest", "ViewLeaveRequest", "UpdateLeaveRequest", "DeleteLeaveRequest", "CreateLeaveApproval", "ViewLeaveApproval", "UpdateLeaveApproval", "DeleteLeaveApproval",
            "CreateLeaveAdjustmentRequest", "ViewLeaveAdjustmentRequest", "UpdateLeaveAdjustmentRequest", "DeleteLeaveAdjustmentRequest", "CreateLeaveAdjustmentApproval",
            "ViewLeaveAdjustmentApproval", "UpdateLeaveAdjustmentApproval", "DeleteLeaveAdjustmentApproval", "ViewLeaveTransactionReport", "ViewLocationWiseHolidaysMaster", "CreateLocationWiseHolidaysMaster", "DeleteLocationWiseHolidaysMaster", "UpdateLocationWiseHolidaysMaster", "ChangeFinancialYear"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            FinancialUserPrivilege.add(privilege);
        }

        //Role will create here
        Role FinancialUserRole = new Role();
        FinancialUserRole.setRoleName(ApplicationConstants.ROLE_LEAVE_ADMIN);
        FinancialUserRole.setStatus(ApplicationConstants.ACTIVE);
        FinancialUserRole.setPrivilege(FinancialUserPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(FinancialUserRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + FinancialUserRole.getRoleName() + "\" Role\n");
        }
    }

    public static void createPensionAdminPrivileges() throws Exception {
        //Admin privileges will create here
        List<Privilege> FinancialUserPrivilege = new ArrayList<Privilege>();
        String[] privilegeNames = {
            "ProfileView", "UpdateProfile", "ChangePassword", "CreateUser", "ViewUserList", "UpdateUser", "DeleteExistingUser", "CreateFinancialYear", "ViewFinancialYear", "UpdateFinancialYear", "DeleteFinancialYear",
            "CreatePensionBank", "ViewPensionBank", "UpdatePensionBank", "DeletePensionBank", "CreatePensionConfiguration", "ViewPensionConfiguration", "UpdatePensionConfiguration",
            "DeletePensionConfiguration", "CreatePensionFormula", "ViewPensionFormula", "UpdatePensionFormula", "DeletePensionFormula", "CreateAssociation", "ViewAssociation",
            "UpdateAssociation", "DeleteAssociation", "CreateAssignPensionHead", "ViewAssignPensionHead", "UpdateAssignPensionHead", "DeleteAssignPensionHead", "CreatePensionArrearConf",
            "ViewPensionArrearConf", "UpdatePensionArrearConf", "DeletePensionArrearConf", "CreatePensionEmployee", "ViewPensionEmployee", "UpdatePensionEmployee", "DeletePensionEmployee",
            "ViewProcessPension", "ProcessPension", "UnProcessPension", "ViewPensionSalaryDetails", "SavePensionSalaryDetails", "ViewPensionArrear", "ProcessPensionArrear",
            "UnProcessPensionArrear", "ViewPensionArrearAdjustment", "UpdatePensionArrearAdjustment", "UpdatePensionRevision", "SavePensionRevision", "EditPensionRevision",
            "DeletePensionRevision", "ViewPensionBankReport", "ViewPensionBankList", "ViewPensionYear", "ChangeFinancialYear"};

        for (String privilegeName : privilegeNames) {
            //System.out.println("Privilege - " + privilegeName);
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilege.setStatus(ApplicationConstants.ACTIVE);
            FinancialUserPrivilege.add(privilege);
        }

        //Role will create here
        Role FinancialUserRole = new Role();
        FinancialUserRole.setRoleName(ApplicationConstants.ROLE_PENSION_ADMIN);
        FinancialUserRole.setStatus(ApplicationConstants.ACTIVE);
        FinancialUserRole.setPrivilege(FinancialUserPrivilege);

        //DB Role insertion in Role Table
        String adminPrimaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ROLE_TABLE, new Gson().toJson(FinancialUserRole));
        if (adminPrimaryKey != null) {
            //System.out.println("Above Privileges are Added for : \"" + FinancialUserRole.getRoleName() + "\" Role\n");
        }
    }

}
