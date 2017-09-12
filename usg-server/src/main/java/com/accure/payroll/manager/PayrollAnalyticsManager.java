package com.accure.payroll.manager;

import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.EmpAttendance;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpSession;

/**
 *
 * @author chaitu
 */
public class PayrollAnalyticsManager {

    final static String autoSalaryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";
    static String attendanceTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMP_ATTENDANCE_TABLE + "`";
    static String ddoTable = ApplicationConstants.USG_DB1 + ApplicationConstants.DDO + "`";

    public static String[] getCurrentFinancialYears(HttpSession session) throws Exception {
        String[] financialYears = null;
        if (session == null) {
            return financialYears;
        }
        String currentFinancialYear = (String) session.getAttribute("currentFinancialYear");
        if (currentFinancialYear != null) {
            if (currentFinancialYear.contains("~")) {
                String[] dates = currentFinancialYear.split("~");
                financialYears = new String[dates.length];
                if (dates != null && dates.length > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    for (int i = 0; i < dates.length; i++) {
                        Date temp = sdf.parse(dates[i]);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(temp);
                        financialYears[i] = cal.get(Calendar.YEAR) + "";
                    }
                }
            }
        }
        return financialYears;
    }

    public static LinkedHashMap getSalaryStatusAnalytics(HttpSession session, boolean lockStatus) throws Exception {
        LinkedHashMap map = new LinkedHashMap();
        ArrayList<EmpAttendance> attenList = null;
        String[] finacialYears = getCurrentFinancialYears(session);
        if (session == null || finacialYears == null || finacialYears.length == 0) {
            return map;
        }
        String ddo = (String) session.getAttribute("selectedddoid");

        String attendanceQuery = "select atten.employeeCode,atten.year,atten.month"
                + " from " + attendanceTable + " as atten left join " + ddoTable + " as ddoT on (OID(atten.ddo)=ddoT._id)"
                + " where ddoT._id=OID(\"" + ddo + "\")"
                + " and atten.lockStatus=" + lockStatus
                + " and atten.status=\"" + ApplicationConstants.ACTIVE + "\""
                + " and (atten.year in (" + Integer.parseInt(finacialYears[0]) + ") and atten.month in (4,5,6,7,8,9,10,11,12) or atten.year in (" + Integer.parseInt(finacialYears[1]) + ") and atten.month in (1,2,3))"
                + " order by atten.year asc";

        String attendanceQueryOutput = new RestClient().getRestData(ApplicationConstants.END_POINT, attendanceQuery);
        if (attendanceQueryOutput != null && !attendanceQueryOutput.isEmpty() && !attendanceQueryOutput.equals("[]")) {
            attenList = new Gson().fromJson(attendanceQueryOutput, new TypeToken<ArrayList<EmpAttendance>>() {
            }.getType());
            for (EmpAttendance att : attenList) {
                String name = att.getMonth() + "";
                if (map.containsKey(name)) {
                    map.put(name, (Integer.parseInt(map.get(name) + "") + 1));
                } else {
                    map.put(name, 1);
                }
            }
        }
        return map;
    }

    public static LinkedHashMap getSalaryAmountAnalytics(HttpSession session) throws Exception {
        LinkedHashMap map = new LinkedHashMap();
        ArrayList<AutoSalaryProcess> list = null;
        String[] finacialYears = getCurrentFinancialYears(session);
        if (session == null || finacialYears == null || finacialYears.length == 0) {
            return map;
        }
        String ddo = (String) session.getAttribute("selectedddoid");
        String query = "select autos.employeeCode,autos.year,autos.month,autos.finalPayment"
                + " from " + autoSalaryTable + " as autos left join " + ddoTable + " as ddoT on (OID(autos.ddo)=ddoT._id)"
                + " where ddoT._id=OID(\"" + ddo + "\")"
                + " and autos.status=\"" + ApplicationConstants.PROCESSED + "\""
                + " and (autos.year in (" + Integer.parseInt(finacialYears[0]) + ") and autos.month in (4,5,6,7,8,9,10,11,12) or autos.year in (" + Integer.parseInt(finacialYears[1]) + ") and autos.month in (1,2,3))"
                + " order by autos.year asc";

        String queryOutput = new RestClient().getRestData(ApplicationConstants.END_POINT, query);
        if (queryOutput != null && !queryOutput.isEmpty() && !queryOutput.equals("[]")) {
            list = new Gson().fromJson(queryOutput, new TypeToken<ArrayList<AutoSalaryProcess>>() {
            }.getType());
            for (AutoSalaryProcess auto : list) {
                String name = auto.getMonth() + "";
                if (map.containsKey(name)) {
                    map.put(name, (Double.parseDouble(map.get(name) + "") + auto.getFinalPayment()));
                } else {
                    map.put(name, auto.getFinalPayment());
                }
            }
        }
        return map;
    }

}
