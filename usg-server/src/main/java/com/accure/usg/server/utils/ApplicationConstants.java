/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.server.utils;

import static com.accure.usg.server.utils.Common.getConfig;

/**
 *
 * @author kalyan
 */
public class ApplicationConstants {

    private ApplicationConstants() {

        throw new AssertionError();
    }

    //Table used
    //this table is not used in application
    public static final String VALIDATE_DATES = "validateDateValue";

    //
    public static final String PREVIOUS_BUDGET_AMOUNT_DETAILS = "previousYearActualBudgetAmount";
    public static final String PARTY_MASTER = "partyMaster";

    public static final String CONSOLIDATE_DEPARTMENT_INCOME = "consolidateDeptIncome";
    public static final String CONSOLIDATE_DEPARTMENT_EXPENSE = "consolidateDeptExpense";
    public static final String EMPLOYEE_DEPARTMENT_MAPPING = "employeeDepartmentMapping";
    public static final String LEAVE_FINACIAL_YEAR_TABLE = "leaveFinanicialYear";
    public static final String RETIREMENT_DATE_VALIDATE_FOR_INSC_TRANS = "dateValidation";
    public static final String SALARY_VALIDATION = "salryAlreadyProcessedForInsc";
    public static final String DUPLICATE = "duplicate";
    public static final String DATA_EXISTED = "Already Existed";
    public static final String DUPLICATE_ASSOCIATION = "duplicateAssociation";
    public static final String DUPLICATE_EMP_CODE = "duplicateEmpCode";
    public static final String DUPLICATE_NOMINEES = "duplicateNominees";
    public static final String DUPLICATE_PRIMARIES = "duplicatePrimaries";
    public static final String USER_TABLE = "user";
    public static final String PENSION_HEAD_ASSIGN_TABLE = "pensionHeadAssign";
    public static final String GIS_GROUP_TABLE = "gisgrouptable";
    public static final String DISCIPLINE_TABLE = "disciplinetable";
    public static final String SECTION_TABLE = "sectiontable";
    public static final String CITY_CATEGORY_TABLE = "cityCategoryTable";
    public static final String DEPARTMENT_TABLE = "departmentTable";
    public static final String ASSOCIATION_TABLE = "associationTable";
    public static final String CITY_TABLE = "cityTable";
    public static final String DA_TABLE = "daTable";
    public static final String EMPLOYEE_PREVIOUS_JOB_TABLE = "employeePreviousJobDetails";
    public static final String ORGANIZATION_TABLE = "organization";
    public static final String ROLE_TABLE = "role";
    public static final String BANK_TABLE = "bank";
    public static final String QUARTER_CATEGORY_TABLE = "quartercategory";
    public static final String QUARTER_TABLE = "quarter";
    public static final String CLASS_TABLE = "class";
    public static final String GRADE_TABLE = "grade";
    public static final String DESIGNATION_TABLE = "designation";
    public static final String SALARY_HEAD_TABLE = "salaryhead";
    public static final String HEAD_SLAB_TABLE = "headslab";
    public static final String EMPLOYEE_TABLE = "employee";
    public static final String FUND_CATEGORY_TABLE = "fundcategory";
    public static final String FINANCIAL_FUND_TYPE_TABLE = "financialfundtype";
    public static final String GROUP_TABLE = "group";
    public static final String GROUP_MASTER_TABLE = "groupMaster";
    public static final String RECEIPT_VOUCHER_TABLE = "receiptvoucher";
    public static final String PAYMENT_VOUCHER_TABLE = "paymentvoucher";
    public static final String RECEIPT_VOUCHER = "ReceiptVoucher";
    public static final String PAYMENT_VOUCHER = "PaymentVoucher";
    public static final String CONTRA_VOUCHER = "ContraVoucher";
    public static final String CHEQUE_STATUS = "Cheque Status";
    public static final String BUDGET_SANCTION_CHART = "Cheque Status";
    public static final String JOURNAL_VOUCHER = "JournalVoucher";
    public static final String LOAN_PAYMENT_MODE_LOAN_RECOVERY = "LoanRecovery";
    public static final String LOAN_ALLOTMENT_MODE = "loanAllotment";
    public static final String CONTRA_VOUCHER_TABLE = "contravoucher";
    public static final String JOURNAL_VOUCHER_TABLE = "journalvoucher";
    public static final String ASSETS = "Assets";
    public static final String LIABILITY = "Liability";
    public static final String INCOME = "Income";
    public static final String EXPENDITURE = "Expenditure";
    public static final String LEAVE_TYPE_MASTER = "leaveType";
    public static final String PARENT_HEAD_MASTER = "parenthead";
    public static final String DEDUCTION_TYPE_MASTER = "deductiontype";
    public static final String FIXED_HEAD_MASTER = "fixedhead";
    public static final String CHAPTERVI_TYPE_MASTER = "chapterVItype";
    public static final String HEAD_NAME_MASTER = "headnamemaster";
    public static final String BASED_ON_MASTER = "basedon";
    public static final String PF_TYPE_MASTER = "pftype";
    public static final String EMPLOYEE_LEFT_STATUS_MASTER = "employeeLeftStatus";
    public static final String UNDER_GROUP_MASTER = "undergroup";
    public static final String UNDER_BUDGET_HEAD_MASTER = "underbudgethead";
    public static final String SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER = "salarybilltypeOrEmpCategory";
    public static final String EMPLOYEE_LEAVE_ASSIGNMENT = "empLeaveAssignment";
    public static final String LEAVE_TRANSACTION = "leaveTransaction";
    public static final String LEAVE_ENCASHMENT = "leaveEncashment";
    public static final String LEAVE_CARRY_FORWARD = "leaveCarryForward";
    public static final String CONSOLIDATE_INCOME_BUDGET = "consolidateIncomeBudget";
    public static final String CONSOLIDATE_DEPT_INCOME = "consolidateDeptIncome";
    public static final String CONSOLIDATE_SACTION_INCOME = "incomeBudgetSanction";
    public static final String EXTRA_PROVISION_INCOME = "extraProvisionIncome";
    public static final String EXTRA_PROVISION_EXPENSE = "extraProvisionExpense";
    public static final String SACTION_UNIVERSITY_EXPENSE = "expensebudgetsanctionuniversity";
    public static final String CONSOLIDATE_ = "consolidateExpenseBudget";
    public static final String LOAN_APPLY_TABLE = "loanapply";
    public static final String PENSION_FORMULA_TABLE = "pensionFormula";
    public static final String RELIGION_TABLE = "religion";
    public static final String SALUTATION_TABLE = "salutation";
    public static final String MARITAL_STATUS_TABLE = "maritalstatus";
    public static final String CATEGORY_TABLE = "category";
    public static final String RELATION_TABLE = "relation";
    public static final String FINANCIAL_YEAR_TABLE = "budgetFinacialYear";
    public static final String FORMULA_TABLE = "formula";
    public static final String NOMINEE_TABLE = "nominee";
    public static final String MAJORHEAD_TABLE = "majorhead";
    public static final String MINORHEAD_TABLE = "minorhead";
    public static final String SUB_MINORHEAD_TABLE = "subminorhead";
    public static final String SUB_MAJORHEAD_TABLE = "submajorhead";
    public static final String BANK_NAME_TABLE = "bankname";
    public static final String GOVT_BUDGET_HEAD_TABLE = "govtbudgethead";
    public static final String EMP_DEMPGRAPHIC_TABLE = "empdemographic";
    public static final String LOAN_ORDER_TABLE = "loanorder";
    public static final String FUND_HEAD_MAPPING_TABLE = "fundheadmapping";
    public static final String FINANCIAL_FUND_HEAD_MAPPING_TABLE = "financialfundheadmapping";
    public static final String BANK_CHEQUE_CONFIGURATION_TABLE = "bankchequeconfiguration";
    public static final String FINANCIAL_BUDGET_HEAD_CODE_TABLE = "financialbudgetheadcode";
    public static final String EMPLOYEE_PROMOTION_TABLE = "employeepromotion";
    public static final String FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE = "budgetFinacialYear";
    public static final String LOAN_ALLOTMENT_TABLE = "loanallotment";
    public static final String LOAN_TRANSACTION_TABLE = "loanSupress";
    public static final String LOAN_RECOVERY_TABLE = "loanrecovery";
    public static final String LOAN_PAYMENT_TABLE = "loanpayment";
    public static final String LEDGER_TABLE = "ledger";
    public static final String LEDGER_CATEGORY_TABLE = "ledgerCategory";
    public static final String PARENT_LEDGER_TABLE = "parentLedger";
    public static final String GOVERNMENT_LEDGER_CODE_TABLE = "governmentLedgerCode";
    public static final String LEDGER_CODE_TABLE = "ledgerCode";
    public static final String HEAD_CODE_LOCATION_MAPPING_TABLE = "headcodelocation";
    public static final String NARATION_TABLE = "naration";
    public static final String DDO_TABLE = "ddo";
    public static final String LOCATION_TABLE = "location";
    public static final String EMP_ATTENDANCE_TABLE = "empattendance";
    public static final String EMP_ATTENDANCE_ADJ_TABLE = "empAttendanceAdjustment";
    public static final String BANK_RECONCILATION_TABLE = "bankReconcilationEntry";
    public static final String BANK_RECONCILATION_REPORT_TABLE = "bankReconcilationReport";
    public static final String PENSION_EMPLOYEE_TABLE = "pensionEmploy";
    public static final String PENSION_EMPLOYEE_DETAILS = "pensionEmployeeDetails";
    public static final String AUTO_SALARY_PROCESS_TABLE = "autosalaryprocess";
    public static final String AUTO_SALARY_UNPROCESS_TABLE = "autosalaryunprocess";
    public static final String PAY_STOPED_SALARY_TABLE = "paystopedsalary";
    public static final String ARREAR_CONFIG_TABLE = "arrearconfig";
    public static final String ARREAR_PROCESS_TABLE = "arrearprocess";
    public static final String ARREAR_UN_PROCESS_TABLE = "arrearunprocess";
    public static final String ADJ_INCOMETAX_TABLE = "adjincometax";
    public static final String INCOMETAX_TABLE = "incometaxprocess";
    public static final String ARREAR_ADJUSTMENT_TABLE = "arrearAdjustment";
    public static final String BUDGET_TYPE_TABLE = "budgetType";
    public static final String BUDGET_SECTOR_TABLE = "budgetSector";
    public static final String PENSION_REVISION_TABLE = "pensionRevision";
    public static final String HEAD_CODE_TABLE = "headCode";
    public static final String DDO_HEAD_CODE_TABLE = "ddoheadCode";
    public static final String EXPENSE_BUDGET_TABLE = "expenseBudget";
    public static final String EXPENSE_BUDGET_APPROVAL = "expenseBudgetApproval";
    public static final String BUDGET_MAJOR_HEAD_TABLE = "budgetMajorHead";
    public static final String BUDGET_MINOR_HEAD_TABLE = "budgetMinorHead";
    public static final String BUDGET_SUB_MAJOR_HEAD_TABLE = "budgetSubMajorHead";
    public static final String BUDGET_SUB_MINOR_HEAD_TABLE = "budgetSubMinorHead";
    public static final String BUDGET_GOVT_BUDGETHEAD_TABLE = "budgetGovtHead";
    public static final String PENSION_CLASSIFICATION_TABLE = "pensionClassification";
    public static final String PENSION_TYPE_TABLE = "pensionType";
    public static final String BUDGET_FUND_CATEGORY_TABLE = "budgetFundCategory";
//    public static final String BUDGET_FUND_TYPE_TABLE = "budgetFundType";
    public static final String FUND_TYPE_TABLE = "fundType";
    public static final String BUDGET_FINACIAL_YEAR_TABLE = "budgetFinacialYear";
    public static final String BUDGET_FUND_HEAD_MAPPING_TABLE = "budgetfundHeadMapping";
    public static final String BUDGET_NATURE_TABLE = "budgetNature";
    public static final String LOAN_TYPE_TABLE = "LoanType";
    public static final String EMP_TRANSFER_JOINING_TABLE = "emptransferjoiningtable";
    public static final String FIXED_DEPOSIT_TABLE = "fixedDeposit";
    public static final String MANAGE_OPENING_BALANCE_TABLE = "manageOpeningBalance";
    public static final String FDR_PROCESS_TABLE = "fdrProcess";
    public static final String EMP_SALARY_TABLE = "empSalary";
    public static final String HEAD_ASSIGN_TABLE = "headAssignTable";
    public static final String LEAVE_ADJUSTMENT_TABLE = "leaveAdjustment";
    public static final String BUDGET_EXPENSE_TABLE = "budgetExpenseTable";
    public static final String MANAGE_PENSION_SALARY_DETAILS_TABLE = "managePensionSalaryDetails";
    public static final String INSURANCE_TRANSACTIONS_TABLE = "insuranceTransactionTable";
    public static final String INSURANCE_TRANSACTIONS_HISTORY_TABLE = "insuranceTransactionHistory";
    public static final String SALARY_INCREMENT_DATE_WISE = "salary-increment-date-wise";
    public static final String EMP_SUSPENSION_TABLE = "employee-suspension";

    //For Insurance Transactions
    public static final String LIC = "LIC";
    public static final String GS_LIS = "GS LIS";

//    For User Management //Profile Management
    public static final String USER_LIST = "List of User for User Management";
    public static final String USER_LIST_FAILED = "Retrieving of user list is failed";
    public static final String PROFILE_LIST = "User Profile List";
    public static final String PROFILE_LIST_FAILED = "Retrieving of profile list is failed";
    public static final String USER_INFO_UPDATED = "User Information Updated";
    public static final String USER_INFO_UPDATION_FAILED = "User Information Updation failed";
    public static final String PASSWORD_UPDATE = "Password Updation";
    public static final String PASSWORD_SUCCESSFULLY_CHANGED = "Your Password is successfully changed";
    public static final String PASSWORD_CHANGE_FAILED = "Password Updation failed";
    public static final String USER_DELETED_SUCCESSFULLY = "User is successfully delted";
    public static final String USER_DELETION_FAILED = "User deletion failed";
    public static final String USER_CREATED_SUCCESSFULLY = "User is successfully created";
    public static final String USER_CREATION_FAILED = "User creation failed";
    public static final String DDO_LOCATION_TABLE = "ddoLocationMapping";
    public static final String VALIDATE_QUARTER_NUMBER = "duplicateQuarterNumber";
    public static final String PENSION_HEAD_SLAB_TABLE = "pensionHeadSlab";
    public static final String PENSION_ASSOCIATION_TABLE = "pensionAssociationTable";
    public static final String PROCESS_PENSION_TABLE = "processPension";
    public static final String UNPROCESS_PENSION_TABLE = "unProcessPension";
    public static final String PENSION_ARREAR_TABLE = "pensionArrearTable";
    public static final String PENSION_ARREAR_NOT_PROCESS_TABLE = "pensionArrearNotProcess";
    public static final String QUARTER_TRANSACTION_TABLE = "quarterTransaction";
    public static final String CREATE_INCOME_BUDGET_TABLE = "IncomeBudget";
    public static final String DEPTWISE_INC_BUDGET_ALLOC_TABLE = "deptWiseIncomeBudgetAllocation";
    public static final String DEPTWISE_EXP_BUDGET_ALLOC_TABLE = "deptWiseExpenseBudgetAllocation";

    public static final String USER_LOGIN_ID = "loginid";
    public static final String USER_PASSWORD = "password";
    public static final String EMAIL_ID = "email";
    public static final String MOBILE_NO = "mobile";
    public static final String ID = "_id";
    public static final String ROLE_NAME = "roleName";
    public static final String ACTIVE_FINANCIAL_YEAR = "active";

    //Status
    public static final String ACTIVE = "Active";
    public static final String ISACTIVE = "isActive";
    public static final String TRUE = "True";
    public static final String FALSE = "False";
    public static final String FINANCIAL_ACTIVE = "active";
    public static final String SUBMIT = "Submit";
    public static final String STATUS = "status";
    public static final String INACTIVE = "Inactive";
    public static final String DELETE = "Delete";
    public static final String PROCESSED = "Processed";
    public static final String UNPROCESSED = "Unprocessed";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String VALUE = "value";
    public static final String REQUEST = "Request";
    public static final String SALARY = "salary";
    public static final String PAY_STOP_SALARY = "paystopsalary";

    //System Messages
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String ERROR = "error";
    public static final String FATAL_ERROR = "fatal error";
    public static final String WARNING = "warning";
    public static final String AUTHORIZED = "authorized";
    public static final String UNAUTHORIZED_ACCESS = "unauthorized access";
    public static final String INVALID_INPUT = "invalid input";
    public static final String INVALID_SESSION = "Invalid Session";
    public static final String INSUFFICIENT_PRIVILEGE = "Insufficient Privilege";
    public static final String EXCEPTION_MESSAGE = "Internal error occured please contact admin";
    public static final String MISSING_PARAMETERS = "Missing input parameters";
    public static final String OPERATION_FAIL = "operation fail";
    public static final String NO_DATA_AVAILABLE = "no data available";
    public static final String RECORDS_FOUND = "Records found";
    public static final String RECORDS_NOT_FOUND = "Records not found";
    public static final String RECORD_MAPPED = "RecordMapped";
    public static final String REGISTER = "Successfully Registered";
    public static final String SIGNIN = "Sign in successfully";
    public static final String UNSIGNIN = "Sign in unsuccessfully";
    public static final String USERCREATED = "User Created successfully";
    public static final String USERCREATIONFAILED = "User Creation Failed";
    public static final String EMAIL_EXIST = "Email Already exist";
    public static final String MOBILENUMBER_EXIT = "Mobile Number Already exist";
    public static final String FORGOT_PASSWORD_GENERATED = "Forgot Password, your new password generated successfully and sent to your email.";
    public static final String FORGOT_PASSWORD_NOTGENERATED = "Forgot Password, sorry unable to generate password.";

    // audit log actions
    public static final String VIEW = "view";
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String NEW = "new";
    public static final String POSTED = "Posted";
//    public static final String DELETE = "Delete";
    public static final String SEND_EMAIL = "sendEmail";
    public static final String AUTHENTICATION = "authenticate";
    public static final String LOGOUT = "logout";
    public static final String USER = "user";
    public static final String ADDYOURDEPT = "ADD";
    //HTTP status codes
    public static final String HTTP_STATUS_SUCCESS = "200";
    public static final String HTTP_STATUS_FAIL = "501";
    public static final String HTTP_STATUS_DELETE_MAP = "502";
    public static final String HTTP_STATUS_UNAUTHORIZED = "401";
    public static final String HTTP_STATUS_NODATA = "404";
    public static final String HTTP_STATUS_INVALID_SESSION = "403";
    public static final String HTTP_STATUS_EXCEPTION = "500";
    public static final String HTTP_STATUS_DUPLICATE = "duplicate";
    // public static final String HTTP_STATUS_DUPLICATE = "duplicate";
    public static final String HTTP_STATUS_NULL = "null";
    public static final String HTTP_STATUS_NOT_ALLOWED = "405";

    //Common Util constants
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String PROVIDER = "provider";

    //generic status constants
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_CANCEL = "cancelled";
    public static final String STATUS_RE_SCHEDULE = "rescheduled";
    public static final String STATUS_REVISE = "revised";
    public static final String STATUS_COMPLETE = "completed";

    public static final String CARRY_FORWARD = "CarryForward";
    public static final String LAPSE = "Lapse";
    public static final String CARRY_FORWARD_LAPSE = "This Ledger is either Forwarded or Lapsed";

    public static final String EQUAL = "equal";
    public static final String HOARDING_LATITUDE = "latitude";
    public static final String HOARDING_LONGITUDE = "longitude";
    public static final String LOGICAL_OR = "or";
    public static final String LOGICAL_AND = "and";
    public static final String LOGICAL_LT = "lt";
    public static final String LOGICAL_GT = "gt";
    public static final String LOGICAL_LTE = "lte";
    public static final String LOGICAL_GTE = "gte";
    public static final String PASSWORD_PREFIX = "acc";
    public static final String LOGICAL_IN = "in";

    //Nature
    public static final String NATURE_TABLE = "naturemaster";
    public static final String FUND_NATURE_TABLE = "fundnaturemaster";
    public static final String LOAN_NATURE_TABLE = "loannaturemaster";
    public static final String GAZETTED_NATURE_TABLE = "gazettedmaster";
    public static final String DESIGNATION_FUND_TYPE_TABLE = "designationfundtypemaster";
    public static final String DDO_DEPARTMENT_TABLE = "ddodepartmentmapping";
    public static final String BUDGET_HEAD_MAPPING_TABLE = "budgetheadmapping";

    public static final String PENSION_BANK_TABLE = "pensionBank";

    public static final String PENSION_CONFIG_TABLE = "pensionconfiguration";

    public static final String PENSION_HEAD_TABLE = "pensionhead";

    public static final String SECTOR_TABLE = "sectorTable";

//         for employee table
    public static final String NAME = "employeeName";
    public static final String SORTKEY = "SORTKEY";
    public static final String SORTORDER = "SORTORDER";
    public static final String SORTORDER_TYPE = "1";

    public static final String NO_DATA_FOUND = "No Data Found";

//    for Fund Type
    public static final String DESCRIPTION = "description";
    public static final String ORDER = "order";
    public static final String DESCRIPTION_DUPLICATE = "The Given Description already exist";
    public static final String ORDER_DUPLICATE = "Order Can't be duplicate";

    //for Finance
    public static final String LEDGER_CREATED = "Ledger created successfully";
    public static final String LEDGER_CREATION_FAILED = "Ledger creation failed";
    public static final String LEDGER_LIST = "Ledger List";
    public static final String LEDGER_LIST_FAILED = "Ledger List failed";
    public static final String LEDGER_UPDATION = "Updation of ledger done";
    public static final String LEDGER_UPDATION_FAILED = "Updation of ledger failed";
    public static final String LEDGER_DELETED = "Ledger is successfully deleted";
    public static final String LEDGER_DELETED_FAILED = "Ledger deletion failed";

    //for ledger Category
    public static final String LEDGER_CATEGORY_CREATED = "Ledger Created created successfully";
    public static final String LEDGER_CATEGORY_CREATION_FAILED = "Ledger Category creation failed";
    public static final String LEDGER_CATEGORY_LIST = "Ledger Category List";
    public static final String LEDGER_CATEGORY_LIST_FAILED = "Ledger Category List failed";
    public static final String LEDGER_CATEGORY_UPDATION = "Updation of ledger category done";
    public static final String LEDGER_CATEGORY_UPDATION_FAILED = "Updation of ledger category failed";
    public static final String LEDGER_CATEGORY_DELETED = "Ledger Category is successfully deleted";
    public static final String LEDGER_CATEGORY_DELETED_FAILED = "Ledger Category deletion failed";

//    for headCodeLocationMapping
    public static final String BUDGET_HEAD_LIST = "Budget Head List";
    public static final String BUDGET_HEAD_LIST_FAILED = "Budget Head List failed";
    public static final String HEAD_CODE_CREATED = "Head Code Creation";
    public static final String HEAD_CODE_CREATION_FAILED = "Head Code Creation Failed";
    public static final String HEAD_CODE_LIST = "Head Code List";
    public static final String HEAD_CODE_LIST_FAILED = "Head Code List failed";
    public static final String HEAD_CODE_UPDATION = "Head Code Updation Done";
    public static final String HEAD_CODE_UPDATION_FAILED = "Head Code Updation failed";
    public static final String HEAD_CODE_DELETED = "Head Code is successfully deleted";
    public static final String HEAD_CODE_DELETED_FAILED = "Head Code deletion failed";

    //For opening Balance
    public static final String GROUP_LIST = "Group List";
    public static final String GROUP_LIST_fAILED = "Group List Failed";
    public static final String CURRENT_FINANCIAL_YEAR = "Current Year";
    public static final String CURRENT_FINANCIAL_YEAR_FAILED = "Current Year Failed";
    public static final String YEAR_CHANGED = "Current Year Changed";
    public static final String YEAR_CHANGED_FAILED = "Current Year Changed Failed";

    //for Fixed Deposits details
    public static final String BANK_LIST = "Bank List";
    public static final String BANK_LIST_fAILED = "Bank List Failed";
    public static final String FUND_TYPE_LIST = "Fund Type List";
    public static final String FUND_TYPE_LIST_FAILED = "Fund Type List Failed";

//    for naration
    public static final String NARATION_CREATED = "Naration is successfully Created";
    public static final String NARATION_CREATION_FAILED = "Naration creation failed";
    public static final String NARATION_LIST = "Naration List";
    public static final String NARATION_LIST_FAILED = "Naration List Failed";
    public static final String NARATION_INFO_UPDATED = "Naration Updation Done";
    public static final String NARATION_UPDATION_FAILED = "Naration Updation Failed";
    public static final String NARATION_DELETED_SUCCESSFULLY = "Naration successfully deleted";
    public static final String NARATION_DELETION_FAILED = "Naration deletion failed";

//    for Sector
    public static final String SECTOR_LIST = "Sector List";
    public static final String SECTOR_LIST_FAILED = "Sector List Failed";

//    Carry Forward
    public static final String CARRY_FORWARD_SUCCESSFULLY_UPDATED = "Carry Forward Successfully updated into Manage Opening Balance";
    public static final String CARRY_FORWARD_UPDATION_FAILES = "Carry Forward Updation Failed";

//    for DDO
    public static final String DDO_CREATED = "DDO is successfully Created";
    public static final String DDO_CREATION_FAILED = "DDO creation failed";
    public static final String DDO_LIST = "DDO List";
    public static final String DDO_LIST_FAILED = "DDO List Failed";
    public static final String DDO_INFO_UPDATED = "DDO Updated Successfully";
    public static final String DDO_UPDATION_FAILED = "DDO Updation Failed";
    public static final String DDO_DELETED_SUCCESSFULLY = "DDO successfully deleted";
    public static final String DDO_DELETION_FAILED = "DDO deletion failed";

//    for Fixed Deposits
    public static final String FIXED_DEPOSIT_CREATED = "Fixed Deposit is successfully Created";
    public static final String FIXED_DEPOSIT_CREATION_FAILED = "Fixed Deposit creation failed";
    public static final String FIXED_DEPOSIT_LIST = "Fixed Deposit List";
    public static final String FIXED_DEPOSIT_LIST_FAILED = "Fixed Deposit List Failed";
    public static final String FIXED_DEPOSIT_INFO_UPDATED = "Fixed Deposit Updated Successfully";
    public static final String FIXED_DEPOSIT_UPDATION_FAILED = "Fixed Deposit Updation Failed";
    public static final String FIXED_DEPOSIT_DELETED_SUCCESSFULLY = "Fixed Deposit successfully deleted";
    public static final String FIXED_DEPOSIT_DELETION_FAILED = "Fixed Deposit deletion failed";

//    for manage Opening Balance
    public static final String MANAGE_OPENING_BALANCE = "Manage Opening balance is successfully Created";
    public static final String MANAGE_OPENING_BALANCE_FAILED = "Manage Opening balance creation failed";
    public static final String MANAGE_OPENING_BALANCE_LIST = "Manage Opening balance List";
    public static final String MANAGE_OPENING_BALANCE_LIST_FAILED = "Manage Opening balance List Failed";
    public static final String MANAGE_OPENING_BALANCE_DELETED_SUCCESSFULLY = "Manage Opening balance successfully deleted";
    public static final String MANAGE_OPENING_BALANCE_DELETION_FAILED = "Manage Opening balance deletion failed";
    public static final String LEDGER_USED_IN_VOUCHERS = "Ledger is used in vouchers";
    public static final String LEDGER_NOT_USED_IN_VOUCHERS = "Ledger is not used in vouchers";

//    for Fixed Deposits
    public static final String FDR_PROCESS_CREATED = "FDR Process is successfully Created";
    public static final String FDR_PROCESS_CREATION_FAILED = "FDR Process creation failed";
    public static final String FDR_PROCESS_LIST = "FDR Process List";
    public static final String FDR_PROCESS_LIST_FAILED = "FDR Process List Failed";
    public static final String FDR_PROCESS_INFO_UPDATED = "FDR Process Updated Successfully";
    public static final String FDR_PROCESS_UPDATION_FAILED = "FDR Process Updation Failed";
    public static final String FDR_PROCESS_DELETED_SUCCESSFULLY = "FDR Process successfully deleted";
    public static final String FDR_PROCESS_DELETION_FAILED = "FDR Process deletion failed";

//    for fund Type
//    public static final String FUND_TYPE_LIST = "Fund Type List";
//    public static final String FUND_TYPE_LIST_FAILED = "FDR Process List Failed";
    //for LOcation
    public static final String LOCATION_CREATED = "Location is successfully Created";
    public static final String LOCATION_CREATION_FAILED = "Location creation failed";
    public static final String LOCATION_LIST = "Location List";
    public static final String LOCATION_LIST_FAILED = "Location List Failed";
    public static final String LOCATION_INFO_UPDATED = "Location Updated Successfully";
    public static final String LOCATION_UPDATION_FAILED = "Location Updation Failed";
    public static final String LOCATION_DELETED_SUCCESSFULLY = "Location successfully deleted";
    public static final String LOCATION_DELETION_FAILED = "Location deletion failed";

    //For Budget Nature
    public static final String BUDGET_NATURE_CREATED = "Budget Nature is successfully Created";
    public static final String BUDGET_NATURE_CREATION_FAILED = "Budget Nature creation failed";
    public static final String BUDGET_NATURE_LIST = "Budget Nature List";
    public static final String BUDGET_NATURE_LIST_FAILED = "Budget Nature List Failed";
    public static final String BUDGET_NATURE_INFO_UPDATED = "Budget Nature Updated Successfully";
    public static final String BUDGET_NATURE_UPDATION_FAILED = "Budget Nature Updation Failed";
    public static final String BUDGET_NATURE_DELETED_SUCCESSFULLY = "Budget Nature successfully deleted";
    public static final String BUDGET_NATURE_DELETION_FAILED = "Budget Nature deletion failed";

//    for parent Ledger
    public static final String PARENT_LEDGER_CREATED = "Parent Ledger is successfully Created";
    public static final String PARENT_LEDGER_CREATION_FAILED = "Parent Ledger creation failed";
    public static final String PARENT_LEDGER_LIST = "Parent Ledger List";
    public static final String PARENT_LEDGER_LIST_FAILED = "Parent Ledger List Failed";
    public static final String PARENT_LEDGER_DELETED_SUCCESSFULLY = "Parent Ledger successfully deleted";
    public static final String PARENT_LEDGER_DELETION_FAILED = "Parent Ledger deletion failed";
    public static final String PARENT_LEDGER_UPDATED = "Parent Ledger Updated Successfully";
    public static final String PARENT_LEDGER_UPDATION_FAILED = "Parent Ledger Updation failed";

//    for bank Reconcilation
    public static final String BANK_RECONCILATION_CREATED = "Bank Reconcilation is successfully Created";
    public static final String BANK_RECONCILATION_FAILED = "Bank Reconcilation creation failed";
    public static final String BANK_RECONCILATION_LIST = "Bank Reconcilation List";
    public static final String BANK_RECONCILATION_LIST_FAILED = "Bank Reconcilation List Failed";
    public static final String BANKRECONCILATION_INFO_UPDATED = "Bank Reconcilation Updation Done";
    public static final String BANKRECONCILATION_UPDATION_FAILED = "Bank Reconcilation Updation Failed";
    public static final String BANKRECONCILATION_DELETED_SUCCESSFULLY = "Bank Reconcilation successfully deleted";
    public static final String BANKRECONCILATION_DELETION_FAILED = "Bank Reconcilation deletion failed";

//    for bank Reconcilation Report
    public static final String BANK_RECONCILATION_REPORT_CREATED = "Bank Reconcilation Report is successfully Created";
    public static final String BANK_RECONCILATION_REPORT_FAILED = "Bank Reconcilation Report creation failed";
    public static final String BANK_RECONCILATION_REPORT_LIST = "Bank Reconcilation Report List";
    public static final String BANK_RECONCILATION_REPORT_LIST_FAILED = "Bank Reconcilation Report List Failed";

//   for Government Ledger Code Master    
    public static final String GOVERNMENT_LEDGER_CODE_CREATED = "Government Ledger Code is successfully Created";
    public static final String GOVERNMENT_LEDGER_CODE_CREATION_FAILED = "Government Ledger Code creation failed";
    public static final String GOVERNMENT_LEDGER_CODE_LIST = "Government Ledger Code List";
    public static final String GOVERNMENT_LEDGER_CODE_LIST_FAILED = "Government Ledger Code List Failed";
    public static final String GOVERNMENT_LEDGER_CODE_INFO_UPDATED = "Government Ledger Code Updated Successfully";
    public static final String GOVERNMENT_LEDGER_CODE_UPDATION_FAILED = "Government Ledger Code Updation Failed";
    public static final String GOVERNMENT_LEDGER_CODE_DELETED_SUCCESSFULLY = "Government Ledger Code successfully deleted";
    public static final String GOVERNMENT_LEDGER_CODE_DELETION_FAILED = "Government Ledger Code deletion failed";

//   for Ledger Code Master
    public static final String LEDGER_CODE_CREATED = "Ledger Code created successfully";
    public static final String LEDGER_CODE_CREATION_FAILED = "Ledger Code creation failed";
    public static final String LEDGER_CODE_LIST = "Ledger Code List";
    public static final String LEDGER_CODE_LIST_FAILED = "Ledger Code List failed";
    public static final String LEDGER_CODE_UPDATION = "Updation of Ledger Code done";
    public static final String LEDGER_CODE_UPDATION_FAILED = "Updation of Ledger Code failed";
    public static final String LEDGER_CODE_DELETED = "Ledger Code is successfully deleted";
    public static final String LEDGER_CODE_DELETED_FAILED = "Ledger Code deletion failed";

    //for pension Employee 
    public static final String PENSION_EMPLOYEE_LIST = "List of pension Employee";
    public static final String PENSION_EMPLOYEE_LIST_FAILED = "List of pension Employee failed";
    public static final String PENSION = "Pension";
    public static final String FAMILY_PENSION = "Family Pension";

    //for leave Adjustment
    public static final String LEAVE_ADJUSTMENT_CREATED = "Leave Adjustment created successfully";
    public static final String LEAVE_ADJUSTMENT_CREATION_FAILED = "Leave Adjustment creation failed";
    public static final String LEAVE_ADJUSTMENT_LIST = "List of leave adjustment Employee";
    public static final String LEAVE_ADJUSTMENT_LIST_FAILED = "List of leave adjustment failed";
    public static final String LEAVE_ADJUSTMENT_UPDATED = "leave adjustment Master Updated Successfully";
    public static final String LEAVE_ADJUSTMENT_UPDATION_FAILED = "leave adjustment Master Updation Failed";
    public static final String LEAVE_TRANSACTION_UPDATED = "leave Transaction Updated Successfully";
    public static final String LEAVE_TRANSACTION_UPDATION_FAILED = "leave Transaction Updation Failed";
    public static final String LEAVE_ADJUSTMENT_DELETED_SUCCESSFULLY = "leave adjustment successfully deleted";
    public static final String LEAVE_ADJUSTMENT_DELETION_FAILED = "leave adjustment deletion failed";

    //for group master
    public static final String GROUP_MASTER_CREATED = "Group is successfully Created";
    public static final String GROUP_MASTER_FAILED = "Group creation failed";
    public static final String GROUP_MASTER_LIST = "Group Master List";
    public static final String GROUP_MASTER_LIST_FAILED = "Group Master List Failed";
    public static final String GROUP_DELETED_SUCCESSFULLY = "Group successfully deleted";
    public static final String GROUP_DELETION_FAILED = "Group deletion failed";
    public static final String GROUP_INFO_UPDATED = "Group Master Updated Successfully";
    public static final String GROUP_UPDATION_FAILED = "Group Master Updation Failed";

    //Roles List
    public static final String ROLES_LIST = "Roles List";
    public static final String ROLES_LIST_FAILED = "Roles List Failed";

//    for columndepartment
    public static final String DEPARTMENT = "department";
    public static final String DESIGNATION = "designation";
    public static final String POSTING_CITY = "postingCity";
    public static final String PF_TYPE = "pfType";
    public static final String FUND_TYPE = "fundType";
    public static final String BUDGET_HEAD = "budgetHead";
    public static final String BANK_NAME = "bankName";
    public static final String NATURE = "nature";
    public static final String DDO = "ddo";
    public static final String LOCATION = "location";
    public static final String GRADE = "grade";
    public static final String Nature = "nature";
    public static final String EMPLOYEE_VIEW = "view";
    public static final String EMPLOYEE_DELETE = "delete";

//    for salary slip
    public static final String DEPARTMENT_LIST = "Department List";
    public static final String DEPARTMENT_LIST_FAILED = "Department List Failed";
    public static final String DESIGNATION_LIST = "Department List Failed";
    public static final String DESIGNATION_LIST_FAILED = "Department List Failed";
    public static final String POSTING_CITY_LIST = "Posting City List";
    public static final String POSTING_CITY_LIST_FAILED = "Posting City List Failed";
    public static final String PF_TYPE_LIST = "Pf Type List";
    public static final String PF_TYPE_LIST_FAILED = "Pf Type List Failed";
    public static final String EMPLOYEE_LIST = "Employee List";
    public static final String EMPLOYEE_LIST_FAILED = "Employee List Failed";

    //for Vouchers
    public static final String CONTRA_VOUCHERS = "Contra Voucher";
    public static final String RECEIPT_VOUCHERS = "Receipt Voucher";
    public static final String PAYMENT_VOUCHERS = "Payment Voucher";
    public static final String JOURNAL_VOUCHERS = "Journal Voucher";
    public static final String COMMON = "Common";

    //for Leave Management Table
    public static final String COMMON_HOLIDAYS_MASTER = "Common_Holidays_Master";
    public static final String HOLIDAYS_LOCATION_MASTER = "holidaysLocationMaster";
    public static final String LOCATION_WISE_HOLIDAY_MASTER = "locationwiseholidaymaster";
    public static final String WEEKLY_OFF_MASTER = "weeklyoffmaster";
    public static final String LEAVE_WORK_FLOW_MASTER = "leaveWorkFlowmaster";
    public static final String LEAVE_REQUEST = "leaveRequest";
    public static final String LEAVE_ADJUSTMENT_REQUEST = "leaveAdjustmentRequest";
    public static final String LEAVE_APPROVAL = "leaveApproval";
    public static final String LEAVE_ADJUSTMENT_APPROVAL = "leaveAdjustmentApproval";
    public static final String HOLIDAY_TYPE_MASTER = "holidaytypemaster";
    public static final String HEADWISE_INCOME_BUDGET_MASTER = "headwiseIncomeBudgetMaster";

    //AQL config
    public static final String END_POINT = (String) getConfig().getProperty("aql_url");
    public static final String USG_DB1 = "`" + (String) getConfig().getProperty("aql_db");

    //Common privileges
    public static final String PV_VIEW_PROFILE = "ProfileView";
    //  DDO
    public static final String PV_CREATE_DDO = "CreateDDO";
    public static final String PV_DELETE_DDO = "DeleteDDO";
    public static final String PV_UPDATE_DDO = "UpdateDDO";
    public static final String PV_VIEW_DDO = "ViewDDO";
    //  Location
    public static final String PV_CREATE_LOCATION = "CreateLocation";
    public static final String PV_DELETE_LOCATION = "DeleteLocation";
    public static final String PV_UPDATE_LOCATION = "UpdateLocation";
    public static final String PV_VIEW_LOCATION = "ViewLocation";
    //  DDO Location Mapping
    public static final String PV_CREATE_DDO_LOCATION_MAPPING = "CreateDDOLocationMapping";
    public static final String PV_DELETE_DDO_LOCATION_MAPPING = "DeleteDDOLocationMapping";
    public static final String PV_UPDATE_DDO_LOCATION_MAPPING = "UpdateDDOLocationMapping";
    public static final String PV_VIEW_DDO_LOCATION_MAPPING = "ViewDDOLocationMapping";
    //Fund Type Master
    public static final String PV_CREATE_FUND_TYPE = "CreateFundType";
    public static final String PV_DELETE_FUND_TYPE = "DeleteFundType";
    public static final String PV_UPDATE_FUND_TYPE = "UpdateFundType";
    public static final String PV_VIEW_FUND_TYPE = "ViewFundType";
    //Formula Master
    public static final String PV_CREATE_FORMULA = "CreateFormula";
    public static final String PV_DELETE_FORMULA = "DeleteFormula";
    public static final String PV_UPDATE_FORMULA = "UpdateFormula";
    public static final String PV_VIEW_FORMULA = "ViewFormula";

    //HRMS Start Previleges End
    //    Category
    public static final String PV_CREATE_CATEGORY = "CreateCategory";
    public static final String PV_DELETE_CATEGORY = "DeleteCategory";
    public static final String PV_UPDATE_CATEGORY = "UpdateCategory";
    public static final String PV_VIEW_CATEGORY = "ViewCategory";
    //    Relation
    public static final String PV_CREATE_RELATION = "CreateRelation";
    public static final String PV_DELETE_RELATION = "DeleteRelation";
    public static final String PV_UPDATE_RELATION = "UpdateRelation";
    public static final String PV_VIEW_RELATION = "ViewRelation";
    //    Maritial
    public static final String PV_CREATE_MARITIAL = "CreateMarital";
    public static final String PV_DELETE_MARITIAL = "DeleteMarital";
    public static final String PV_UPDATE_MARITIAL = "UpdateMarital";
    public static final String PV_VIEW_MARITIAL = "ViewMarital";
    //    Salutation
    public static final String PV_CREATE_SALUTATION = "CreateSalutation";
    public static final String PV_DELETE_SALUTATION = "DeleteSalutation";
    public static final String PV_UPDATE_SALUTATION = "UpdateSalutation";
    public static final String PV_VIEW_SALUTATION = "ViewSalutation";
    //    Buget Head Master
    public static final String PV_CREATE_BUDGET_HEAD = "CreateBudgetHead";
    public static final String PV_DELETE_BUDGET_HEAD = "ViewBudgetHead";
    public static final String PV_UPDATE_BUDGET_HEAD = "UpdateBudgetHead";
    public static final String PV_VIEW_BUDGET_HEAD = "DeleteBudgetHead";
    //    Religion Master
    public static final String PV_CREATE_RELIGION = "CreateReligion";
    public static final String PV_DELETE_RELIGION = "DeleteReligion";
    public static final String PV_UPDATE_RELIGION = "UpdateReligion";
    public static final String PV_VIEW_RELIGION = "ViewReligion";
    //    Nature Master
    public static final String PV_CREATE_NATURE = "CreateNature";
    public static final String PV_DELETE_NATURE = "DeleteNature";
    public static final String PV_UPDATE_NATURE = "UpdateNature";
    public static final String PV_VIEW_NATURE = "ViewNature";
    //    Loan Type

    //Loan Nature Master
    public static final String PV_CREATE_LOAN_NATURE = "CreateLoanNature";
    public static final String PV_DELETE_LOAN_NATURE = "DeleteLoanNature";
    public static final String PV_UPDATE_LOAN_NATURE = "UpdateLoanNature";
    public static final String PV_VIEW_LOAN_NATURE = "ViewLoanNature";
    //Gad Nongad Master
    public static final String PV_CREATE_GAD_NONGAD = "CreateGadNongad";
    public static final String PV_DELETE_GAD_NONGAD = "DeleteGadNongad";
    public static final String PV_UPDATE_GAD_NONGAD = "UpdateGadNongad";
    public static final String PV_VIEW_GAD_NONGAD = "ViewGadNongad";
    //Fund Head Mapping
    public static final String PV_CREATE_FUND_HEAD_MAPPING = "CreateFundHeadMapping";
    public static final String PV_DELETE_FUND_HEAD_MAPPING = "DeleteFundHeadMapping";
    public static final String PV_UPDATE_FUND_HEAD_MAPPING = "UpdateFundHeadMapping";
    public static final String PV_VIEW_FUND_HEAD_MAPPING = "ViewFundHeadMapping";
    //Designation Fund Type Mapping
    public static final String PV_CREATE_DESIGNATION_FUND_TYPE_MAPPING = "CreateDesgFundTypeMapping";
    public static final String PV_DELETE_DESIGNATION_FUND_TYPE_MAPPING = "DeleteDesgFundTypeMapping";
    public static final String PV_UPDATE_DESIGNATION_FUND_TYPE_MAPPING = "UpdateDesgFundTypeMapping";
    public static final String PV_VIEW_DESIGNATION_FUND_TYPE_MAPPING = "ViewDesgFundTypeMapping";

    //Chapter vi Master
    public static final String PV_CREATE_CHAPTER_VI = "CreateChapterVIMaster";
    public static final String PV_DELETE_CHAPTER_VI = "UpdateChapterVIMaster";
    public static final String PV_UPDATE_CHAPTER_VI = "DeleteChapterVIMaster";
    public static final String PV_VIEW_CHAPTER_VI = "ViewChapterVIMaster";
    //Fixed Head Master
    public static final String PV_CREATE_FIXED_HEAD = "CreateFixedHead";
    public static final String PV_DELETE_FIXED_HEAD = "DeleteFixedHead";
    public static final String PV_UPDATE_FIXED_HEAD = "UpdateFixedHead";
    public static final String PV_VIEW_FIXED_HEAD = "ViewFixedHead";
    //Financial Year Master
    public static final String PV_CREATE_FINANCIAL_YEAR = "CreateFinancialYear";
    public static final String PV_DELETE_FINANCIAL_YEAR = "DeleteFinancialYear";
    public static final String PV_UPDATE_FINANCIAL_YEAR = "UpdateFinancialYear";
    public static final String PV_VIEW_FINANCIAL_YEAR = "ViewFinancialYear";
    //PF Type Master
    public static final String PV_CREATE_PF_TYPE = "CreatePFType";
    public static final String PV_DELETE_PF_TYPE = "DeletePfType";
    public static final String PV_UPDATE_PF_TYPE = "UpdatePFType";
    public static final String PV_VIEW_PF_TYPE = "ViewPFType";
    //DDO Department Master
    public static final String PV_CREATE_DDO_DEPARTMENT_MAPPING = "CreateDDODepartmentMapping";
    public static final String PV_DELETE_DDO_DEPARTMENT_MAPPING = "DeleteDDODepartmentMapping";
    public static final String PV_UPDATE_DDO_DEPARTMENT_MAPPING = "UpdateDDODepartmentMapping";
    public static final String PV_VIEW_DDO_DEPARTMENT_MAPPING = "ViewDDODepartmentMapping";
    //Salary Bill Type
    public static final String PV_SALARY_BILL_TYPE_CREATE = "CreateSalaryBillType";
    public static final String PV_SALARY_BILL_TYPE_DELETE = "DeleteSalaryBillType";
    public static final String PV_SALARY_BILL_TYPE_UPDATE = "UpdateSalaryBillType";
    public static final String PV_SALARY_BILL_TYPE_VIEW = "ViewSalaryBillType";
    //HRMS Common Previleges End

    //HRMS Account Previleges End
    //for bank
    public static final String PV_CREATE_BANK = "CreateBank";
    public static final String PV_VIEW_BANK = "ViewBank";
    public static final String PV_UPDATE_BANK = "UpdateBank";
    public static final String PV_DELETE_BANK = "DeleteBank";
    //for Department
    public static final String PV_CREATE_DEPARTMENT = "CreateDepartment";
    public static final String PV_VIEW_DEPARTMENT = "ViewDepartment";
    public static final String PV_UPDATE_DEPARTMENT = "UpdateDepartment";
    public static final String PV_DELETE_DEPARTMENT = "DeleteDepartment";
    //For Section
    public static final String PV_CREATE_SECTION = "CreateSection";
    public static final String PV_VIEW_SECTION = "ViewSection";
    public static final String PV_UPDATE_SECTION = "UpdateSection";
    public static final String PV_DELETE_SECTION = "DeleteSection";
    //For Discipline
    public static final String PV_CREATE_DISCIPLINE = "CreateDiscipline";
    public static final String PV_VIEW_DISCIPLINE = "ViewDiscipline";
    public static final String PV_UPDATE_DISCIPLINE = "UpdateDiscipline";
    public static final String PV_DELETE_DISCIPLINE = "DeleteDiscipline";
    //City Category
    public static final String PV_CREATE_CITY_CATEGORY = "CreateCityCategory";
    public static final String PV_VIEW_CITY_CATEGORY = "ViewCityCategory";
    public static final String PV_UPDATE_CITY_CATEGORY = "UpdateCityCtaegory";
    public static final String PV_DELETE_CITY_CATEGORY = "DeleteCityCategory";
    //City Master
    public static final String PV_CREATE_CITY = "CreateCity";
    public static final String PV_VIEW_CITY = "ViewCity";
    public static final String PV_UPDATE_CITY = "UpdateCity";
    public static final String PV_DELETE_CITY = "DeleteCity";
    //For DA/MA Rate
    public static final String PV_CREATE_DA_MA_RATE = "CreateDaMaRate";
    public static final String PV_VIEW_DA_MA_RATE = "ViewDaMaRate";
    public static final String PV_UPDATE_DA_MA_RATE = "UpdateDaMaRate";
    public static final String PV_DELETE_DA_MA_RATE = "DeleteDaMaRate";
    //Grade Master
    public static final String PV_CREATE_GRADE = "CreateGrade";
    public static final String PV_VIEW_GRADE = "ViewGrade";
    public static final String PV_UPDATE_GRADE = "UpdateGrade";
    public static final String PV_DELETE_GRADE = "DeleteGrade";
    //Class Master
    public static final String PV_CREATE_CLASS = "CreateClass";
    public static final String PV_VIEW_CLASS = "ViewClass";
    public static final String PV_UPDATE_CLASS = "UpdateClass";
    public static final String PV_DELETE_CLASS = "DeleteClass";
    //Designation Master
    public static final String PV_CREATE_DESIGNATION = "CreateDesignation";
    public static final String PV_VIEW_DESIGNATION = "ViewDesignation";
    public static final String PV_UPDATE_DESIGNATION = "UpdateDesignation";
    public static final String PV_DELETE_DESIGNATION = "DeleteDesignation";
    //Salary Head Master
    public static final String PV_CREATE_SALARY_HEAD = "CreateSalaryHead";
    public static final String PV_VIEW_SALARY_HEAD = "ViewSalaryHead";
    public static final String PV_UPDATE_SALARY_HEAD = "UpdateSalaryHead";
    public static final String PV_DELETE_SALARY_HEAD = "DeleteSalaryHead";
    //Head Slab Master
    public static final String PV_CREATE_HEAD_SLAB = "CreateHeadSlab";
    public static final String PV_VIEW_HEAD_SLAB = "ViewHeadSlab";
    public static final String PV_UPDATE_HEAD_SLAB = "UpdateHeadSlab";
    public static final String PV_DELETE_HEAD_SLAB = "DeleteHeadSlab";
    //Quarter Category Master
    public static final String PV_CREATE_QUARTER_CATEGORY = "CreateQuarterCategory";
    public static final String PV_VIEW_QUARTER_CATEGORY = "ViewQuarterCategory";
    public static final String PV_UPDATE_QUARTER_CATEGORY = "UpdateQuarterCategory";
    public static final String PV_DELETE_QUARTER_CATEGORY = "DeleteQuarterCategory";
    //Quarter Master
    public static final String PV_CREATE_QUARTER = "CreateQuarter";
    public static final String PV_VIEW_QUARTER = "ViewQuarter";
    public static final String PV_UPDATE_QUARTER = "UpdateQuarter";
    public static final String PV_DELETE_QUARTER = "DeleteQuarter";
    //Association Master
    public static final String PV_CREATE_ASSOCIATION = "CreateAssociation";
    public static final String PV_VIEW_ASSOCIATION = "ViewAssociation";
    public static final String PV_UPDATE_ASSOCIATION = "UpdateAssociation";
    public static final String PV_DELETE_ASSOCIATION = "DeleteAssociation";
    //GIS group Master
    public static final String PV_CREATE_GIS_GROUP = "CreateGISGroup";
    public static final String PV_VIEW_GIS_GROUP = "ViewGISGroup";
    public static final String PV_UPDATE_GIS_GROUP = "UpdateGISGroup";
    public static final String PV_DELETE_GIS_GROUP = "DeleteGISGroup";
    //HRMS Account Previleges End

//    payroll report start
    public static final String PV_VIEW_SALARY_SLIP = "ViewSalarySlip";
    public static final String PV_VIEW_DEDUCTION_VIEW = "ViewDeductionView";
    public static final String PV_VIEW_BANK_STATEMENT = "ViewBankStatement";
    public static final String PV_VIEW_LOAN_ORDER_STATEMENT = "ViewLoanOrderStatement";
    public static final String PV_VIEW_EMPLOYEE_LIST = "ViewEmployeeList";
    public static final String PV_VIEW_SALARY_REGISTER_DIFF = "ViewSalaryRegisterDiff";
    public static final String PV_VIEW_SALARY_SLIP_MAIL = "ViewSalarySlipMail";
    public static final String PV_VIEW_MONTHLY_WISE_REGISTER = "ViewMonthWiseRegister";
    public static final String PV_VIEW_EMPLOYEE_DEDUCTION = "ViewEmployeeDeduction";
    public static final String PV_VIEW_SALARY_STATUS = "ViewSalaryStatus";
    public static final String PV_VIEW_QUARTER_LIST = "ViewQuarterList";
    public static final String PV_VIEW_QUARTER_TRANSACTION_LIST = "ViewQuarterTransactionList";
    public static final String PV_VIEW_MASTER_LIST = "ViewMastersList";
    public static final String PV_VIEW_SALARY_SUMMARY = "ViewSalarySummary";
    public static final String PV_VIEW_ARREAR_REPORT = "ViewArrearReport";
    public static final String PV_VIEW_EMPLOYEE_IMPORT = "ViewEmployeeImport";
    public static final String PV_VIEW_EMPLOYEE_EXPORT = "ViewEmployeeExport";
//    payroll report end

    //Privilages for Payroll
    public static final String PV_CREATE_LOAN_APPLY = "CreateLoanApply";
    public static final String PV_UPDATE_LOAN_APPLY = "UpdateLoanApply";
    public static final String PV_VIEW_LOAN_APPLY = "ViewLoanApply";
    public static final String PV_DELETE_LOAN_APPLY = "DeleteLoanApply";

    //Leave Management
    public static final String LEAVE_APPLIED = "Applied";
    public static final String LEAVE_APPROVED = "Approved";
    public static final String LEAVE_ADJUSTMENT_REQUEST_STATUS = "AdjustmentRequested";
    public static final String LEAVE_ADJUSTMENT_APPROVED_STATUS = "AdjustmentApproved";
    public static final String INCOME_BUDGET_STATUS = "sentStatus";
    public static final String INCOME_BUDGET_STATUS_MEAASGE = "true";
    public static final String INCOME_BUDGET_SACTION_STATUS_MEAASGE = "true";
    public static final String IS_CONSOLIDATED_TRUE = "true";
    public static final String IS_SANCTIONED_TRUE = "true";
    public static final String IS_SANCTIONED_FALSE = "false";
    public static final String CONSOLIDATE_EXPENSE_TABLE = "consolidateExpenseBudget";
    public static final String DELETED = "Deleted";
    public static final String EXISTED = "existed";
    public static final String APPROVED = "Approved";
    public static final String APPLIED = "Applied";
    public static final String ADJUSTMENT_REQUESTED = "Adjustment_Requested";
    public static final String ADJUSTMENT_APPROVED = "Adjustment_Approved";
    public static final String LEAVE_REJECTED = "rejected";
    public static final String GET_DESIGNATION_BASED_ON_DDO = "GetDesignationBasedOnDdo";
    public static final String GET_FUND_TYPE_GRADE_N_CLASS_BASED_ON_DDO_DESI = "getFTGradeClassBasedOnDdoDesignation";
    public static final String GET_BH_BASED_ON_DDO_DESI_FT = "GetBudgetHeadBasedOnDdoDesignationFT";
    public static final String GET_NATURE_BASED_ON_DDO_DESI_FT_BH = "GetNatureBasedOnDdoDesignationFT_BH";
    public static final String GET_DESCIPLINE_BASED_ON_DDO_DESI_FT_BH_NATURE = "GetDesciplineBasedOnDdoDesignationFT_BH_Nature";
    public static final String GET_LOCATION_BASED_ON_DDO = "getLocationBasedOnDdo";
    public static final String GET_DEPARTMENT_BASED_ON_DDO = "GetDepartmentBasedOnDdo";
    public static final String GET_GRADE_BASED_ON_DESIGNATION = "getGradeBssedOnDesignation";
    public static final String EMPLOYEE_HISTORY_TABLE = "employee_history";

//    for Auto Salary Process
    public static final String EMPLOYEE_LIST_FOR_AUTO_SALARY = "SearchEmployeeList";
    public static final String EMPLOYEE_LIST_FOR_AUTO_SALARY_FAILED = "SearchEmployeeListFailed";
    public static final String SALARY_SUCCESSFULLY_PROCESSED = "Salary is successfully processsed";
    public static final String SALARY_PROCESS_FAIL = "Salary process failed";
    public static final String SALARY_ALREADY_PROCESSED = "Salary already processed";
    public static final String SALARY_SUCCESSFULLY_UNPROCESSED = "Salary is successfully unprocesssed";
    public static final String SALARY_UNPROCESS_FAIL = "Salary unprocess failed";
    public static final String SALARY_ALREADY_UNPROCESSED = "Salary already unprocessed";

    //for Employee Attendance
    public static final String EMPLOYEE_LIST_FOR_EMPLOYEE_ATTENDACE = "SearchEmployeeList";
    public static final String EMPLOYEE_LIST_FOR_EMPLOYEE_ATTENDACE_FAILED = "SearchEmployeeListFailed";
    public static final String EMPLOYEE_ATTENDACE_SUCCESSFULLY_PROCESSED = "Salary is successfully processsed";
    public static final String EMPLOYEE_ATTENDACE_PROCESS_FAIL = "Salary process failed";

    public static final String SICK_LEAVE = "sick leave";
    public static final String LEAVE_WITHOOUT_PAY = "LWP";
    public static final String EMP_ATTENDANCE_UNPROCESSTABLE = "empAttendanceUnProcess";

    public static final String REGEX = "$regex";
    public static final String DDO_LOCATION_PATTERN_DIVIDER = "/";
    public static final String DDO_LOCATION_AUTOINCREMENT_VALUE_PATTERN_DIVIDER = "-";
    public static final String EMPLOYEE_CODE = "employeeCode";
    public static final String DESIGNATION_FUNDTYPE_MAPPING_CATEGORYPOST = "categoryposts";
    public static final String POSTS = "posts";
    public static final String EMP_DDO = "ddo";
    public static final String EMP_DESIGNATION = "designation";
    public static final String EMP_GRADE = "grade";
    public static final String EMP_FUNDTYPE = "fundType";
    public static final String EMP_BUDGETHEAD = "budgetHead";
    public static final String EMP_NATURETYPE = "natureType";
    public static final String EMP_DISCIPLINE = "discipline";
    public static final String NO_POST_AVAILABLE = "NoPostAvailableForThisCategory";
    public static final String EMAIL_AVAILABLE = "Email Already Existed";
    public static final String PAN_AVAILABLE = "PAN Already Existed";
    public static final String EARNING_HEADS = "Earnings";
    public static final String DEDUCTION_HEADS = "Deductions";
    public static final String DEDUCTION_TYPE_OTHERS = "Others";
    public static final String HEAD_TYPE = "headType";
    public static final String DEDUCTION_TYPE = "deductionType";
    public static final String IS_BASIC = "isBasic";
    public static final String GRADE_PAY = "Grade Pay";
    public static final String ROUNDING_NONE = "None";
    public static final String ROUNDING_50_PAISE = "50 Paise";
    public static final String ROUNDING_1_RUPEE = "1 Rupees";
    public static final String ROUNDING_10_RUPEE = "10 Rupees";

    public static final String MONTHLY = "monthly";
    public static final String QUARTERLY = "quarterly";
    public static final String YEARLY = "yearly";
    public static final String OTHER = "other";
    public static final String OTHERS = "others";
    public static final String HEAD_SLAB_HEAD_NAME = "headName";
    public static final String HEAD_SLAB_TYPE_TWO = "typeTwo";
    public static final String IS_FORMULA = "formula";
    public static final String EQUAL_OPERATOR = "$eq";
    public static final String GTE_OPERATOR = "$gte";
    public static final String LTE_OPERATOR = "$lte";
    public static final String IS_AMOUNT = "amount";
    public static final String PT = "PT";
    public static final String GPF = "GPF";
    public static final String ASSOCIATION_FEE = "ASSOCIATION FEE";
    public static final String EMPLOYEE_CODE_IS_UPDATED = "Updated";
    public static final String DEDUCTION_TYPE_LOAN = "Loan";
    public static final String UPDATED = "Updated";
    public static final String ADD_BASIC_SALARY_HEAD_FIRST = "Add Basic Salary Heads  First";
    public static final String USER_SESSION_LOCATION_ID = "userSessionLocationId";
    public static final String LOCATION_UPDATED_IN_SESSION_SUCCESFULLY = "locationUpdatedSuccesfully";
    public static final String LOCATION_UPDATED_IN_SESSION_FAILED = "locationUpdateFailed";

    //Constants for Roles
    public static final String ROLE_SUPER_ADMIN = "SuperAdmin";
    public static final String ROLE_HRMS_ADMIN = "HRMSAdmin";
    public static final String ROLE_HRMS_USER = "HRMSUser";
    public static final String ROLE_FINANCIAL_ADMIN = "FinancialAdmin";
    public static final String ROLE_FINANCIAL_USER = "FinancialUser";
    public static final String ROLE_PAYROLL_ADMIN = "PayrollAdmin";
    public static final String ROLE_PAYROLL_USER = "PayrollUser";
    public static final String ROLE_BUDGET_ADMIN = "BudgetAdmin";
    public static final String ROLE_BUDGET_USER = "BudgetUser";
    public static final String ROLE_LEAVE_ADMIN = "LeaveAdmin";
    public static final String ROLE_PENSION_ADMIN = "PensionAdmin";

    public static final String LEAVE_ASSIGNED = "leaveassigned";
    public static final String LEAVE_UNASSIGNED = "leaveunassigned";
    public static final String EMPLOYEE_LEAVE_ASSGNMNT_ID = "employeeId";
    public static final String DELETE_MESSAGE = "This is mapped to other places";

    //for check salary head
    public static final String DATA_EXIST = "Salary Head is mapped in Employee master";
    public static final String DATA_DOESNOT_EXIST = "Salary Head Doesnot Exist in Employee master";
    public static final String DUPLICATE_MESSAGE = "Duplicate";

    //Start of Previlages for Leave Management
    //Leave Type
    public static final String PV_LEAVE_TYPE_CREATE = "CreateLeaveType";
    public static final String PV_LEAVE_TYPE_UPDATE = "UpdateLeaveType";
    public static final String PV_LEAVE_TYPE_DELETE = "DeleteLeaveType";
    public static final String PV_LEAVE_TYPE_VIEW = "ViewLeaveType";

    //Holiday Type
    public static final String PV_HOLIDAY_TYPE_DELETE = "DeleteHoliday";
    public static final String PV_HOLIDAY_TYPE_UPDATE = "UpdateHoliday";
    public static final String PV_HOLIDAY_TYPE_VIEW = "ViewHolidays";
    public static final String PV_HOLIDAY_TYPE_CREATE = "CreateHolidays";

    //Common Holidays Master
    public static final String PV_COMMON_HOLIDAY_DELETE = "DeleteCommonHolidays";
    public static final String PV_COMMON_HOLIDAY_UPDATE = "UpdateCommonHolidays";
    public static final String PV_COMMON_HOLIDAY_VIEW = "ViewCommonHolidays";
    public static final String PV_COMMON_HOLIDAY_CREATE = "CreateCommonHolidays";

    //Holiday Location
    public static final String PV_HOLIDAY_LOCATION_DELETE = "DeleteHoliday";
    public static final String PV_HOLIDAY_LOCATION_UPDATE = "UpdateHoliday";
    public static final String PV_HOLIDAY_LOCATION_VIEW = "ViewHolidays";
    public static final String PV_HOLIDAY_LOCATION_CREATE = "CreateHolidays";

    //Weekly Off Master
    public static final String PV_CREATE_WEEKLY_OFF_MASTER = "CreateWeekOffMaster";
    public static final String PV_VIEW_WEEKLY_OFF_MASTER = "ViewWeekOffMaster";
    public static final String PV_UPDATE_WEEKLY_OFF_MASTER = "UpdateWeekOffMaster";
    public static final String PV_DELETE_WEEKLY_OFF_MASTER = "DeleteWeekOffMaster";

    //Employee Leave Assignment
    public static final String PV_CREATE_EMPLOYEE_LEAVE_ASSIGNMENT_MASTER = "CreateLeaveAssignment";
    public static final String PV_VIEW_EMPLOYEE_LEAVE_ASSIGNMENT_MASTER = "ViewLeaveAssignment";
    public static final String PV_UPDATEE_MPLOYEE_LEAVE_ASSIGNMENT_MASTER = "UpdateLeaveAssignment";
    public static final String PV_DELETE_EMPLOYEE_LEAVE_ASSIGNMENT_MASTER = "DeleteLeaveAssignment";

    //Employee Leave Transaction
    public static final String PV_CREATE_EMPLOYEE_LEAVE_TRANSACTION_MASTER = "CreateLeaveTransaction";
    public static final String PV_VIEW_EMPLOYEE_LEAVE_TRANSACTION_MASTER = "ViewLeaveTransaction";
    public static final String PV_UPDATE_EMPLOYEE_LEAVE_TRANSACTION_MASTER = "UpdateLeaveTransaction";
    public static final String PV_DELETE_EMPLOYEE_LEAVE_TRANSACTION_MASTER = "DeleteLeaveTransaction";

    //Employee Encashment
    public static final String PVCREATEEMPLOYEEENCASHMENTMASTER = "CreateLeaveEncashment";
    public static final String PVVIEWEMPLOYEEENCASHMENTMASTER = "ViewLeaveEncashment";
    public static final String PVUPDATEEMPLOYEEENCASHMENTMASTER = "UpdateLeaveEncashment";
    public static final String PVDELETEEMPLOYEEENCASHMENTMASTER = "DeleteLeaveEncashment";
    //End of Previlages for Leave Management

    //HRMS Employee Privelage will goes here
    public static final String PV_EMPLOYEE_CREATE = "CreateEmployee";
    public static final String PV_EMPLOYEE_UPDATE = "UpdateEmployee";
    public static final String PV_EMPLOYEE_DELETE = "DeleteEmployee";
    public static final String PV_EMPLOYEE_VIEW = "ViewEmployee";
    public static final String PV_EMPLOYEE_DEMOGRAPHIC_CREATE = "CreateEmployeeDemographics";
    public static final String PV_EMPLOYEE_DEMOGRAPHIC_VIEW = "ViewEmployeeDemographics";
    public static final String PV_EMPLOYEE_DEMOGRAPHIC_UPDATE = "UpdateEmployeeDemographics";
    public static final String PV_EMPLOYEE_DEMOGRAPHIC_DELETE = "DeleteEmployeeDemographics";
    public static final String PV_EMPLOYEE_PREVIOUS_JOB_CREATE = "CreateEmployeeJob";
    public static final String PV_EMPLOYEE_PREVIOUS_JOB_DELETE = "DeleteEmployeeJob";
    public static final String PV_EMPLOYEE_PREVIOUS_JOB_UPDATE = "UpdateEmployeeJob";
    public static final String PV_EMPLOYEE_PREVIOUS_JOB_VIEW = "ViewEmployeeJob";
    public static final String PV_EMPLOYEE_NOMINEE_DETAILS_CREATE = "CreateEmployeeDetails";
    public static final String PV_EMPLOYEE_NOMINEE_DETAILS_UPDATE = "UpdateEmployeeDetails";
    public static final String PV_EMPLOYEE_NOMINEE_DETAILS_DELETE = "DeleteEmployeeDetails";
    public static final String PV_EMPLOYEE_NOMINEE_DETAILS_VIEW = "ViewEmployeeDetails";
    public static final String PV_EMPLOYEE_HEAD_ASSIGN_CREATE = "CreateEmployeeAssign";
    public static final String PV_EMPLOYEE_HEAD_ASSIGN_DELETE = "DeleteEmployeeAsign";
    public static final String PV_EMPLOYEE_HEAD_ASSIGN_UPDATE = "UpdateEmployeeAsign";
    public static final String PV_EMPLOYEE_HEAD_ASSIGN_VIEW = "ViewEmployeeAsign";

    //HRMS Employee Privelage will ends here 
    public static final String PV_LOAN_ORDER_CREATE = "CreateLoanOrder";
    public static final String PV_LOAN_ORDER_VIEW = "ViewLoanOrder";
    public static final String PV_LOAN_ORDER_UPDATE = "UpdateLoanOrder";
    public static final String PV_LOAN_ORDER_DELETE = "DeleteLoanOrder";
    public static final String PV_LOAN_ALLOTMENT_CREATE = "CreateLoanAllotment";
    public static final String PV_LOAN_ALLOTMENT_VIEW = "ViewLoanAllotment";
    public static final String PV_LOAN_ALLOTMENT_UPDATE = "UpdateLoanAllotment";
    public static final String PV_LOAN_ALLOTMENT_DELETE = "DeleteLoanAllotment";
    public static final String PV_LOAN_TRANSACTION_CREATE = "CreateLoanTransaction";
    public static final String PV_LOAN_TRANSACTION_VIEW = "ViewLoanTransaction";
    public static final String PV_LOAN_TRANSACTION_UPDATE = "UpdateLoanTransaction";
    public static final String PV_LOAN_TRANSACTION_DELETE = "DeleteLoanTransaction";
    public static final String PV_LOAN_RECOVERY_CREATE = "CreateLoanRecovery";
    public static final String PV_LOAN_RECOVERY_VIEW = "ViewLoanRecovery";
    public static final String PV_LOAN_RECOVERY_UPDATE = "UpdateLoanRecovery";
    public static final String PV_LOAN_RECOVERY_DELETE = "DeleteLoanRecovery";
    public static final String PV_EMPLOYEE_PROMOTION_CREATE = "CreateEmployeePromotion";
    public static final String PV_EMPLOYEE_PROMOTION_VIEW = "ViewEmployeePromotion";
    public static final String PV_EMPLOYEE_PROMOTION_DELETE = "DeleteEmployeePromotion";
    public static final String PV_EMPLOYEE_PROMOTION_UPDATE = "UpdateEmployeePromotion";
    public static final String PV_SALARY_INCREMENT_DATE_WISE_CREATE = "CreateSalaryIncrement";
    public static final String PV_SALARY_INCREMENT_DATE_WISE_DELETE = "DeleteSalaryIncrement";
    public static final String PV_SALARY_INCREMENT_DATE_WISE_UPDATE = "UpdateSalaryIncrement";
    public static final String PV_SALARY_INCREMENT_DATE_WISE_VIEW = "ViewSalaryIncrement";
    public static final String PV_INSURANCE_TRANSACTION_CREATE = "CreateInsuranceTransaction";
    public static final String PV_INSURANCE_TRANSACTION_VIEW = "ViewInsuranceTransaction";
    public static final String PV_INSURANCE_TRANSACTION_UPDATE = "UpdateInsuranceTransaction";
    public static final String PV_INSURANCE_TRANSACTION_DELETE = "DeleteInsuranceTransaction";
    public static final String PV_ARREAR_CONFIGURATION_CREATE = "CreateArrearConfiguration";
    public static final String PV_ARREAR_CONFIGURATION_DELETE = "DeleteArrearConfiguration";
    public static final String PV_ARREAR_CONFIGURATION_UPDATE = "UpdateArrearConfiguration";
    public static final String PV_ARREAR_CONFIGURATION_VIEW = "ViewArrearConfiguration";
    public static final String PV_EMPLOYEE_ATTENDANCE_CREATE = "CreateEmployeeAttendance";
    public static final String PV_EMPLOYEE_ATTENDANCE_VIEW = "ViewEmployeeAttendence";
    public static final String PV_EMPLOYEE_ATTENDANCE_UPDATE = "UpdateEmployeeAttendence";
    public static final String PV_EMPLOYEE_ATTENDANCE_DELETE = "DeleteEmployeeAttendance";
    public static final String PV_EMPLOYEE_ATTENDANCE_ADJUSTMENT_CREATE = "CreateAttendanceAdjustment";
    public static final String PV_EMPLOYEE_ATTENDANCE_ADJUSTMENT_VIEW = "ViewAttendanceAdjustment";
    public static final String PV_EMPLOYEE_ATTENDANCE_ADJUSTMENT_UPDATE = "UpdateAttendanceAdjustment";
    public static final String PV_EMPLOYEE_ATTENDANCE_ADJUSTMENT_DELETE = "DeleteAttendanceAdjustment";
    public static final String PV_ARREAR_CREATE = "CreateArrear";
    public static final String PV_ARREAR_DELETE = "DeleteArrear";
    public static final String PV_ARREAR_UPDATE = "UpdateArrear";
    public static final String PV_ARREAR_VIEW = "ViewArrear";
    public static final String PV_ARREAR_ADJUSTMENT_CREATE = "CreateArrearAdjustment";
    public static final String PV_ARREAR_ADJUSTMENT_DELETE = "DeleteArrearAdjustment";
    public static final String PV_ARREAR_ADJUSTMENT_UPDATE = "UpdateArrearAdjustment";
    public static final String PV_ARREAR_ADJUSTMENT_VIEW = "ViewArrearAdjustment";
    public static final String PV_QUARTER_TRANSACTION_CREATE = "CreateQuarterTransaction";
    public static final String PV_QUARTER_TRANSACTION_VIEW = "ViewQuarterTransaction";
    public static final String PV_QUARTER_TRANSACTION_UPDATE = "UpdateQuarterTransaction";
    public static final String PV_QUARTER_TRANSACTION_DELETE = "DeleteQuarterTransaction";
    public static final String PV_TRANSAFER_JOINING_FORM_CREATE = "CreateTransferForm";
    public static final String PV_TRANSAFER_JOINING_FORM_VIEW = "ViewTransferForm";
    public static final String PV_TRANSAFER_JOINING_FORM_UPDATE = "UpdateTransferForm";
    public static final String PV_TRANSAFER_JOINING_FORM_DELETE = "DeleteTransferForm";
    public static final String PV_INCOME_TAX_CREATE = "CreateIncomeTax";
    public static final String PV_INCOME_TAX_VIEW = "ViewIncomeTax";
    public static final String PV_INCOME_TAX_UPDATE = "UpdateIncomeTax";
    public static final String PV_INCOME_TAX_DELETE = "DeleteIncomeTax";
    public static final String PV_ARREAR_BILL_CREATE = "CreateArrearBill";
    public static final String PV_ARREAR_BILL_VIEW = "ViewArrearBill";
    public static final String PV_ARREAR_BILL_DELETE = "UpdateArrearBill";
    public static final String PV_ARREAR_BILL_UPDATE = "DeleteArrearBill";
    public static final String PV_AUTO_SALARY_PROCESS_CREATE = "CreateAutoSalary";
    public static final String PV_AUTO_SALARY_PROCESS_DELETE = "DeleteAutoSalary";
    public static final String PV_AUTO_SALARY_PROCESS_UPDATE = "UpdateAutoSalary";
    public static final String PV_AUTO_SALARY_PROCESS_VIEW = "ViewAutoSalary";
    //Privelages for Payroll Ends Here
    public static final String DDO_LOCATION_ARE_NOT_ACTIVE = "DDO/Location is not Active .";
    public static final String LOCATION_IS_NOT_MAPPED = "Location is not Activly mapped with DDO.";
    public static final String EMPLOYEE_CODE_NOT_GENERATED = "Problem in generating Employee Code.";
    public static final String DDO_NAME = "ddoName";
    public static final String LOCATION_NAME = "locationName";
    public static final String DDO_ID = "ddoId";
    public static final String LOCATION_ID = "locationId";
    public static final String IFSC_CODE_EXISTED = "IFSC_CODE_EXISTED";
    public static final String MICR_CODE_EXISTED = "MICR_CODE_EXISTED";
    public static final String ACCOUNT_NUMBER_EXISTED = "ACCOUNT_NUMBER_EXISTED";
    public static final String INVALID_ACCOUNT_NUMBER = "INVALID_ACCOUNT_NUMBER";

    //Finance module Privelages starte here
    public static final String PV_BANK_NAME_CREATE = "CreateBankName";
    public static final String PV_BANK_NAME_VIEW = "ViewBankName";
    public static final String PV_BANK_NAME_DELETE = "UpdateBankName";
    public static final String PV_BANK_NAME_UPDATE = "DeleteBankName";
    //Bank Cheque Configuration
    public static final String PV_CHEQUE_CONFIGUATION_CREATE = "CreateChequeConfiguation";
    public static final String PV_CHEQUE_CONFIGUATION_VIEW = "ViewChequeConfiguation";
    public static final String PV_CHEQUE_CONFIGUATION_DELETE = "UpdateChequeConfiguation";
    public static final String PV_CHEQUE_CONFIGUATION_UPDATE = "DeleteChequeConfiguation";

    //Bank Cheque Configuration
    public static final String PV_LEDGER_CREATE = "CreateLedger";
    public static final String PV_LEDGER_VIEW = "ViewLedger";
    public static final String PV_LEDGER_DELETE = "DeleteLedger";
    public static final String PV_LEDGER_UPDATE = "UpdateLedger";
    //Ledger Code
    public static final String PV_LEDGER_CODE_CREATE = "LedgerCode";
    public static final String PV_LEDGER_CODE_VIEW = "ViewLedgerCode";
    public static final String PV_LEDGER_CODE_DELETE = "DeleteLedgerCode";
    public static final String PV_LEDGER_CODE_UPDATE = "UpdateLedgerCode";
    // Ledger Category
    public static final String PV_LEDGER_CATEGORY_CREATE = "CreateLedgerCategory";
    public static final String PV_LEDGER_CATEGORY_VIEW = "ViewLedgerCategory";
    public static final String PV_LEDGER_CATEGORY_DELETE = "DeleteLedgerCategory";
    public static final String PV_LEDGER_CATEGORY_UPDATE = "UpdateLedgerCategory";
    //Open Balance
    public static final String PV_OPENING_BALANCE_CREATE = "CreateOpenBalance";
    public static final String PV_OPENING_BALANCE_VIEW = "ViewOpenBalance";
    public static final String PV_OPENING_BALANCE_DELETE = "DeleteOpenBalance";
    public static final String PV_OPENING_BALANCE_UPDATE = "UpdateOpenBalance";
    //Narration
    public static final String PV_NARRATION_CREATE = "CreateStandardNarration";
    public static final String PV_NARRATION_VIEW = "ViewStandardNarration";
    public static final String PV_NARRATION_DELETE = "DeleteStandardNarration";
    public static final String PV_NARRATION_UPDATE = "UpdateStandardNarration";
    //Head Code Location Mapping
    public static final String PV_HEAD_CODE_LOCACTION_MAPPING_CREATE = "CreateHeadCodeLocMapping";
    public static final String PV_HEAD_CODE_LOCACTION_MAPPING_VIEW = "ViewHeadCodeLocMapping";
    public static final String PV_HEAD_CODE_LOCACTION_MAPPING_UPDATE = "UpdateHeadCodeLocMapping";
    public static final String PV_HEAD_CODE_LOCACTION_MAPPING_DELETE = "DeleteHeadCodeLocMapping";
    //FDR Process
    public static final String PV_FDR_PROCESS_CREATE = "CreateFDRProcess";
    public static final String PV_FDR_PROCESS_VIEW = "ViewFDRProcess";
    public static final String PV_FDR_PROCESS_UPDATE = "UpdateFDRProcess";
    public static final String PV_FDR_PROCESS_DELETE = "DeleteFDRProcess";
    //Bank Reconcilation Entry
    public static final String PV_BANK_RECONCILATION_ENTRY_CREATE = "CreateBankReconcilationEntry";
    public static final String PV_BANK_RECONCILATION_ENTRY_VIEW = "ViewBankReconcilationEntry";
    public static final String PV_BANK_RECONCILATION_ENTRY_UPDATE = "UpdateBankReconcilationEntry";
    public static final String PV_BANK_RECONCILATION_ENTRY_DELETE = "DeleteBankReconcilationEntry";
    //Bank Reconcilation Entry Report
    public static final String PV_BANK_RECONCILATION_ENTRY_REPORT_CREATE = "ViewBankReconcilationReport";
    //Change Financial Year
    public static final String PV_CHANGE_FINANCIAL_YEAR = "ChangeFinancialYear";
    //Fund Category
    public static final String PV_FUND_CATEGORY_CREATE = "CreateFundCategory";
    public static final String PV_FUND_CATEGORY_VIEW = "ViewFundCategory";
    public static final String PV_FUND_CATEGORY_UPDATE = "UpdateFundCategory";
    public static final String PV_FUND_CATEGORY_DELETE = "DeleteFundCategory";
    //Group
    public static final String PV_GROUP_CREATE = "CreateGroup";
    public static final String PV_GROUP_VIEW = "ViewGroup";
    public static final String PV_GROUP_UPDATE = "UpdateGroup";
    public static final String PV_GROUP_DELETE = "DeleteGroup";
    //   Major Code
    public static final String PV_MAJOR_CODE_CREATE = "CreateHeadCodeLocMapping";
    public static final String PV_MAJOR_CODE_VIEW = "ViewMajorCode";
    public static final String PV_MAJOR_CODE_UPDATE = "UpdateMajorCode";
    public static final String PV_MAJOR_CODE_DELETE = "DeleteMajorCode";
    //Minor Code
    public static final String PV_MINOR_CODE_CREATE = "CreateMinorCode";
    public static final String PV_MINOR_CODE_VIEW = "ViewMinorCode";
    public static final String PV_MINOR_CODE_UPDATE = "UpdateMinorCode";
    public static final String PV_MINOR_CODE_DELETE = "DeleteMinorCode";
    //Sub Major Code
    public static final String PV_SUB_MAJOR_CODE_CREATE = "CreateSubMajorCode";
    public static final String PV_SUB_MAJOR_CODE_VIEW = "ViewSubMajorCode";
    public static final String PV_SUB_MAJOR_CODE_UPDATE = "UpdateSubMajorCode";
    public static final String PV_SUB_MAJOR_CODE_DELETE = "DeleteSubMajorCode";
    //Sub Minor Code
    public static final String PV_SUB_MINOR_CODE_CREATE = "CreateSubMinorCode";
    public static final String PV_SUB_MINOR_CODE_VIEW = "ViewSubMinorCode";
    public static final String PV_SUB_MINOR_CODE_UPDATE = "UpdateSubMinorCode";
    public static final String PV_SUB_MINOR_CODE_DELETE = "DeleteSubMinorCode";
    //Government Budget Head
    public static final String PV_GOVERNMENT_BUDGET_HEAD_CREATE = "CreateGovernmentBudgetHead";
    public static final String PV_GOVERNMENT_BUDGET_HEAD_VIEW = "ViewGovernmentBudgetHead";
    public static final String PV_GOVERNMENT_BUDGET_HEAD_UPDATE = "UpdateGovernmentBudgetHead";
    public static final String PV_GOVERNMENT_BUDGET_HEAD_DELETE = "DeleteGovernmentBudgetHead";
    //Receipt Voucher
    public static final String PV_RECEIPT_VOUCHER_CREATE = "CreateReceiptVoucher";
    public static final String PV_RECEIPT_VOUCHER_VIEW = "ViewReceiptVoucher";
    public static final String PV_RECEIPT_VOUCHER_UPDATE = "UpdateReceiptVoucher";
    public static final String PV_RECEIPT_VOUCHER_DELETE = "DeleteReceiptVoucher";
    //Payment Voucher
    public static final String PV_PAYMENT_VOUCHER_CREATE = "CreatePaymentVoucher";
    public static final String PV_PAYMENT_VOUCHER_VIEW = "ViewPaymentVoucher";
    public static final String PV_PAYMENT_VOUCHER_UPDATE = "UpdatePaymentVoucher";
    public static final String PV_PAYMENT_VOUCHER_DELETE = "DeletePaymentVoucher";
    //Contra Voucher
    public static final String PV_CONTRA_VOUCHER_CREATE = "CreateContraVoucher";
    public static final String PV_CONTRA_VOUCHER_VIEW = "ViewContraVoucher";
    public static final String PV_CONTRA_VOUCHER_UPDATE = "UpdateContraVoucher";
    public static final String PV_CONTRA_VOUCHER_DELETE = "DeleteContraVoucher";
    //Journal Voucher
    public static final String PV_JOURNAL_VOUCHER_CREATE = "CreateJournalVoucher";
    public static final String PV_JOURNAL_VOUCHER_VIEW = "ViewJournalVoucher";
    public static final String PV_JOURNAL_VOUCHER_UPDATE = "UpdateJournalVoucher";
    public static final String PV_JOURNAL_VOUCHER_DELETE = "DeleteHeadCodeLocMapping";
    //Voucher Posting
    public static final String PV_VOUCHER_POSTING_CREATE = "SaveVoucherPosting";
    public static final String PV_VOUCHER_POSTING_DETAILS = "VoucherPostingDetails";
    public static final String PV_VOUCHER_POSTING_VIEW = "ViewVoucherPosting";
    //Voucher UnPosting
    public static final String PV_VOUCHER_UNPOSTING_CREATE = "SaveVoucherUnPosting";
    public static final String PV_VOUCHER_UNPOSTING_DETAILS = "VoucherUnPostingDetails";
    public static final String PV_VOUCHER_UNPOSTING_VIEW = "ViewVoucherUnPosting";
    //Sub Minor Code
    public static final String PV_BANK_CHEQUE_SEARCH = "SearchBankCheque";
    public static final String PV_BANK_CHEQUE_PRINT = "PrintBankCheque";
    //Post Salary Voucher
    public static final String PV_POST_SALARY_VOUCHER_SEARCH = "SearchPostSalaryVoucher";
    public static final String PV_POST_SALARY_VOUCHER_CREATE = "PostSalaryVoucherDetails";

    //Start of Finance Module Transaction Privilege
    public static final String pvViewReceiptVoucher = "ViewReceiptVoucher";
    public static final String pvCreateReceiptVoucher = "CreateReceiptVoucher";
    public static final String pvUpdateReceiptVoucher = "UpdateReceiptVoucher";
    public static final String pvDeleteReceiptVoucher = "DeleteReceiptVoucher";

    public static final String pvViewPaymentVoucher = "ViewPaymentVoucher";
    public static final String pvCreatePaymentVoucher = "CreatePaymentVoucher";
    public static final String pvUpdatePaymentVoucher = "UpdatePaymentVoucher";
    public static final String pvDeletePaymentVoucher = "DeletePaymentVoucher";
    //End of Finance Module Transaction  Privilege

    //Finance module Privelages ends HERE
    //Budget module Privelages goes HERE
    //DDO HEAD CODE
    public static final String PV_HEAD_CODE_CREATE = "CreateHeadCode";
    public static final String PV_HEAD_CODE_VIEW = "ViewHeadCode";
    public static final String PV_HEAD_CODE_UPDATE = "UpdateHeadCode";
    public static final String PV_HEAD_CODE_DELETE = "DeleteHeadCode";
    //BUDGET TYPE
    public static final String PV_BUDGET_TYPE_CREATE = "CreateBudgetType";
    public static final String PV_BUDGET_TYPE_VIEW = "ViewBudgetType";
    public static final String PV_BUDGET_TYPE_UPDATE = "UpdateBudgetType";
    public static final String PV_BUDGET_TYPE_DELETE = "UpdateBudgetType";
//    //Sector Category -------------------------------------No more using this master and privelages
//    public static final String PV_SECTOR_CATEGORY_CREATE = "CreateSectorCategory";
//    public static final String PV_SECTOR_CATEGORY_VIEW = "ViewSectorCategory";
//    public static final String PV_SECTOR_CATEGORY_UPDATE = "UpdateSectorCategory";
//    public static final String PV_SECTOR_CATEGORY_DELETE = "DeleteSectorCategory";
    //Sector
    public static final String PV_SECTOR_CREATE = "CreateSector";
    public static final String PV_SECTOR_VIEW = "ViewSector";
    public static final String PV_SECTOR_UPDATE = "UpdateSector";
    public static final String PV_SECTOR_DELETE = "DeleteSector";
    //INCOME BUDGET
    public static final String PV_INCOME_BUDGET_CREATE = "CreateIncomeBudget";
    public static final String PV_INCOME_BUDGET_VIEW = "ViewIncomeBudget";
    public static final String PV_INCOME_BUDGET_UPDATE = "UpdateIncomeBudget";
    public static final String PV_INCOME_BUDGET_DELETE = "DeleteIncomeBudget";
    //Budget Consolidated Income
    public static final String PV_CONSOLIDATED_INCOME_BUDGET_CREATE = "CreateBudgetConsolidatedIncome";
    public static final String PV_CONSOLIDATED_INCOME_BUDGET_VIEW = "ViewBudgetConsolidatedIncome";
    public static final String PV_CONSOLIDATED_INCOME_BUDGET_UPDATE = "UpdateBudgetConsolidatedIncome";
    public static final String PV_CONSOLIDATED_INCOME_BUDGET_DELETE = "DeleteBudgetConsolidateIncome";
    //INCOME BUDGET UNIVERSITY
    public static final String PV_INCOME_BUDGET_UNIVERSITY_CREATE = "CreateIncomeBudgetUniversity";
    public static final String PV_INCOME_BUDGET_UNIVERSITY_VIEW = "ViewIncomeBudgetUniversity";
    public static final String PV_INCOME_BUDGET_UNIVERSITY_UPDATE = "UpdateIncomeBudgetUniversity";
    public static final String PV_INCOME_BUDGET_UNIVERSITY_DELETE = "DeleteIncomeBudgetUniversity";
    //LOCATION BUDGET ALLOCATION
    public static final String PV_LOCATION_INCOME_BUDGET_ALLOCATION_CREATE = "CreateLocationBudgetAllocation";
    public static final String PV_LOCATION_INCOME_BUDGET_ALLOCATION_VIEW = "ViewLocationBudgetAllocation";
    public static final String PV_LOCATION_INCOME_BUDGET_ALLOCATION_UPDATE = "UpdateLocationBudgetAllocation";
    public static final String PV_LOCATION_INCOME_BUDGET_ALLOCATION_DELETE = "DeleteLocationBudgetAllocation";
    //BUDGET EXPENSES
    public static final String PV_BUDGET_EXPENSES_CREATE = "CreateBudgetExpenses";
    public static final String PV_BUDGET_EXPENSES_VIEW = "ViewBudgetExpenses";
    public static final String PV_BUDGET_EXPENSES_UPDATE = "UpdateBudgetExpenses";
    public static final String PV_BUDGET_EXPENSES_DELETE = "DeleteBudgetExpenses";
    //BUDGET CONSOLIDATED EXPENSES
    public static final String PV_BUDGET_CONSOLIDATED_EXPENSES_CREATE = "CreateBudgetConsolidatedExpenses";
    public static final String PV_BUDGET_CONSOLIDATED_EXPENSES_VIEW = "ViewBudgetConsolidatedExpenses";
    public static final String PV_BUDGET_CONSOLIDATED_EXPENSES_UPDATE = "UpdateBudgetConsolidatedExpenses";
    public static final String PV_BUDGET_CONSOLIDATED_EXPENSES_DELETE = "DeleteBudgetConsolidatedExpenses";
    //BUGET_EXPENSES_UNIVERSITY
    public static final String PV_BUGET_EXPENSES_UNIVERSITY_CREATE = "CreateBugetExpensesUniversity";
    public static final String PV_BUGET_EXPENSES_UNIVERSITY_VIEW = "ViewBugetExpensesUniversity";
    public static final String PV_BUGET_EXPENSES_UNIVERSITY_UPDATE = "UpdateBugetExpensesUniversity";
    public static final String PV_BUGET_EXPENSES_UNIVERSITY_DELETE = "DeleteBugetExpensesUniversity";
    //LOCATION WISE EXPENSE BUDGET
    public static final String PV_LOCATION_WISE_EXPENSE_BUDGET_VIEW = "ViewLocationWiseExpenseBudget";
    //BUDGET APPROVAL
    public static final String PV_EXPENSE_BUDGET_APPROVAL_DDO_VIEW = "ViewBudgetApprovalDDO";
    //BUDGET REAPPROPRIATION
    public static final String PV_BUDGET_REAPPROPRIATION_VIEW = "ViewBudgetReAppropriation";
    //BUDGET REPORT
    public static final String PV_BUDGET_REPORT_VIEW = "BudgetReportView";
    //Budget module Privelages ends HERE
    public static final String YEAR = "year";
    public static final String LEAVE_TYPE = "leaveType";
    public static final String GENDER_BOTH = "Both";
    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";
    public static final String EMPLOYEE_ID = "employeeId";
    //This two Constants used for tracking , weather carry forward for that emoloyee is done before assignment or after assignment
    //While Unprocess of the carry forward, if After_assignment is there , then current year leaves=0
    //While Unprocess of the carry forward, if Before_assignment is there , delete the record in assignment with assignment id
    public static final String AFTER_ASSIGNMENT = "AfterAssignment";
    public static final String BEFORE_ASSIGNMENT = "BeforeAssignment";

    //for Application Form
    public static final String APPLICATION_FORM_SAVE = "SaveApplicationForm";
    public static final String APPLICATION_FORM_VIEW = "ViewApplicationForm";
    public static final String APPLICATION_FORM_UPDATE = "UpdateApplicationForm";
    public static final String APPLICATION_FORM_DELETE = "DeleteApplicationForm";

    //ADMISSION 
    //course Master
    public static final String COURSE_MASTER_TABLE = "course_master";
    //course ategeory
    public static final String PV_CREATE_COURSE = "CreateCourse";
    public static final String PV_VIEW_COURSE = "ViewCourse";
    public static final String PV_UPDATE_COURSE = "UpdateCourse";
    public static final String PV_DELETE_COURSE = "DeleteCourse";
    public static final String PROGRAM_TYPE_TABLE = "program_type_master";
    public static final String PERIOD_TABLE = "period_master";
    public static final String FACULTY_TABLE = "faculty_master";
    public static final String FACULTY_DEGREE_MAPPING_TABLE = "faculty_degree_mapping_master";
    public static final String SUBJECT_TABLE = "subject_master";
    public static final String PROGRAM_TABLE = "program_master";
    public static final String BOARD_TABLE = "board_master";
    public static final String COLLEGE_TABLE = "college_master";
    public static final String SPECIAL_CLAIM_TABLE = "special_claim_master";
    public static final String LEARNING_CENTRE_TABLE = "learning_centre_master";

//    for Board Master
    public static final String BOARD_CREATION_FAILED = "Board creation failed";

//    for College Master
    public static final String COLLEGE_CREATION_FAILED = "College creation failed";
//    for Board Master
    public static final String SPECIAL_CLAIM_CREATION_FAILED = "Special Claim creation failed";
//    for Learning Centre Master
    public static final String LEARNING_CENTRE_CREATION_FAILED = "Learning Centre creation failed";
    public static final String LEARNING_CENTRE_LIST = "Learning Centre List";
    public static final String APPLICATION_FORM = "applicationForm";

}
