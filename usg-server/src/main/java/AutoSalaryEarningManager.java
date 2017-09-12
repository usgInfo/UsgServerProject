
import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.Employee;
import com.accure.payroll.dto.EmpAttendance;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author ankur
 */
public class AutoSalaryEarningManager {

    /**
     * This method takes all details of Employee from table and return as
     * ArrayList
     *
     * @param empcode is the String input employee code
     * @return details of employee as ArrayList
     */
    public static ArrayList<Employee> getEmployee(String empcode) {

        ArrayList<Employee> list = new ArrayList<Employee>();
        RestClient aql = new RestClient();
        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";
        ////System.out.println(empTable);

        String empQuery = "select * from " + empTable + " where employeeCode=" + empcode + "";
        // //System.out.println(empQuery);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, empQuery);
        ////System.out.println(dbOutput);
        list = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<EmpAttendance>>() {
        }.getType());

        return list;
    }

    public static ArrayList<EmpAttendance> getEmpAttendance(String empcode) {

        ArrayList<EmpAttendance> list = new ArrayList<EmpAttendance>();
        RestClient aql = new RestClient();
        String attTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMP_ATTENDANCE_TABLE + "`";
        ////System.out.println(attTable);

        String attQuery = "select * from " + attTable + " where employeeCode=" + empcode + "";
        // //System.out.println(attQuery);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
        ////System.out.println(dbOutput);
        list = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<EmpAttendance>>() {
        }.getType());

        return list;
    }

    public long getLWPSalary(String empcode, String table) {

        long salary = 0;
        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";
        ArrayList<Employee> emplist = getEmployee(empcode);
        ArrayList<EmpAttendance> attendancelist = getEmpAttendance(empcode);

        if (attendancelist.size() > 0) {
            int lwp = attendancelist.get(0).getLeaveWithoutPay();

            if (lwp > 0) {
                long basic = emplist.get(0).getBasic();
                Date d = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                int iMonth = cal.get(Calendar.MONTH);
                int iYear = cal.get(Calendar.YEAR);
                int iDay = cal.get(Calendar.DAY_OF_MONTH);

                // Create a calendar object and set year and month
                Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);

                // Get the number of days in that month
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

                salary = daysInMonth * lwp;
            }
        }

        return salary;
    }

}
