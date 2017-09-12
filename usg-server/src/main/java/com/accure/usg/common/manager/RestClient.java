/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.common.manager;

/**
 *
 * @author deepak2310
 */
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author sansari
 */
public class RestClient {

    public String getRestData(String endpoint, String query) {
        String json = null;
        try {

            String formatterQuery = URLEncoder.encode(query, "UTF-8");
            String formatedEndpoint = endpoint + formatterQuery;
            URL url = new URL(formatedEndpoint);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            json = output.toString();

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return json;
    }

    public static void main(String[] s) throws Exception {

        PropertiesConfiguration config = getConfig();
//        String fromDate = "01/09/2016";
//        String toDate = "30/09/2016";
//        Long fdate = getMillis(fromDate);
//        Long tdate = getMillis(toDate);
//        String Active = "\"Active\"";
//        String LeaveType = "\'57ceaef269b8ddec97e0a8fb\'";
//        String mongoCollectionName = "`" + (String) config.getProperty("aql_db") + "/" + (String) config.getProperty("db-schema") + "/employee`";
//        String mongoCollectionName1 = "`" + (String) config.getProperty("aql_db") + "/" + (String) config.getProperty("db-schema") + "/empattendance`";
//        String mongoCollectionName2 = "`" + (String) config.getProperty("aql_db") + "/" + (String) config.getProperty("db-schema") + "/leaveTransaction`";
//
////        String sql = "select emp.employeeCodeM"
//                + " from " + mongoCollectionName +" as emp ,"+mongoCollectionName1 +" as attendance ,"+mongoCollectionName2+" "
//                + "as transaction where emp.employeeCode = attendance.employeeCode and transaction.employeeCode = emp.employeeCode";
      //  String sql = "select  SUM(totalLeaveDays) from " + mongoCollectionName2 + " where  fromDateInMilliSecond>= "+fdate+" and  toDateInMilliSecond<= "+tdate+" and leaveTypeDescription = \"SL\" and status =" + Active;
      
      //String sql="select  *from " + mongoCollectionName1 + "where lockStatus=true and month=9 and year=2016 and ddo=\"5795c2b88631dc75fa51cff3\"";
      String sql="select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from `/mano/usgtest/empattendance` as emp";
        //System.out.println(sql);

        String json = new RestClient().getRestData("http://localhost:8085/query/fs?q=", sql);
        //System.out.println(json);
    }

//    public static Long getMillis(String inputDate) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
//        Date date = sdf.parse(inputDate);
//        //System.out.println( date.getTime());
//        return date.getTime();
//    }
}
