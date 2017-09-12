package com.accure.payroll.manager;

import com.accure.payroll.dto.ArrearProcess;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

/**
 *
 * @author chaitu
 */
public class AutoSalaryOtherManager {

    final static RestClient aql = new RestClient();
    final static String arrearProcessTable = ApplicationConstants.USG_DB1 + ApplicationConstants.ARREAR_PROCESS_TABLE + "`";

    /**
     * @author chaitu
     * @description getEmployeeArrearProcess() method will get the employee
     * arrear
     * @table arrearprocess
     * @conditions 1.)month=1 2.)year=2016 3.)empcode=123
     * @param String empCode
     * @param int month
     * @param int year
     * @return ArrearProcess
     * @throws Exception
     */
    public static ArrearProcess getEmployeeArrearProcess(String empCode, int month, int year, String stopSalary) throws Exception {
        ArrearProcess ap = null;
        if (empCode == null || month == 0 || year == 0) {
            return ap;
        }

        String arrearProcessTableQuery = "select employeeCode,payMonth,payYear,arrearDate,arrearType,fromDate,toDate,sum(totalEarnings) as totalEarnings,sum(totalDeductions) as totalDeductions,sum(netPay) as netPay"
                + " from " + arrearProcessTable
                + " where payMonth=" + month
                + " and payYear=" + year
                + " and employeeCode=\"" + empCode + "\""
                + " and status=\"" + ApplicationConstants.ACTIVE + "\""
                + " group by employeeCode,payMonth,payYear,arrearDate,arrearType,fromDate,toDate";

        //get data from arrearprocess
        String arrearProcessTableQueryOutput = aql.getRestData(ApplicationConstants.END_POINT, arrearProcessTableQuery);
        if (arrearProcessTableQueryOutput != null && !arrearProcessTableQueryOutput.isEmpty() && !arrearProcessTableQueryOutput.equals("[]")) {
            ArrayList<ArrearProcess> list = new Gson().fromJson(arrearProcessTableQueryOutput, new TypeToken<ArrayList<ArrearProcess>>() {
            }.getType());
            ap = list.get(0);

            //verify the stop salary flag
            if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.YES)) {
                ap.setTotalEarnings(0.0);
                ap.setTotalDeductions(0.0);
            }
        }
        return ap;
    }
}
