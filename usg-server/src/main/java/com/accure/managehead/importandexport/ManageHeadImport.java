/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.managehead.importandexport;

import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.HeadAssign;
import com.accure.hrms.dto.HeadName;
import com.accure.hrms.manager.HeadAssignManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class ManageHeadImport {
    
    public static String  manageHeadImport(File path){
        String result=null;
        
        String failure="";
         List<String> headAssignIDList = new ArrayList<String>();
        
         try {
            FileInputStream inp = new FileInputStream(path);
            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();
            //System.out.println(rowCount);

            JSONObject json = new JSONObject();
            JSONArray rows = new JSONArray();;
            XSSFRow row;
            XSSFRow Headerrow = (XSSFRow) sheet.getRow(0);
            int headerColCount=sheet.getRow(0).getLastCellNum();
            for (int i = 1; i <= rowCount; i++) {
                int colCount = sheet.getRow(i).getLastCellNum();
                JSONArray cells = new JSONArray();
                JSONObject jRow = new JSONObject();
                if(headerColCount!=colCount){
                    return null;
                }else{
                  row = (XSSFRow) sheet.getRow(i);
                  for (int j = 0; j < colCount; j++) {
                    XSSFCell cell = row.getCell(j);
                    String cellValue = "";
                    XSSFCell Headercell = Headerrow.getCell(j);
                    if (cell != null) {
                        int type = cell.getCellType();
                        switch (type) {
                            case Cell.CELL_TYPE_STRING:
                                cellValue = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                cellValue = String.valueOf((int) cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case Cell.CELL_TYPE_BLANK:
                               return null;
                            case Cell.CELL_TYPE_ERROR:
                            default:
                                return null;
                        }

                    }
                
                   
                    jRow.put(Headercell.getStringCellValue(), cellValue);
                    cells.put(jRow);
                }
                }
                rows.put(jRow);
                json.put("rows", rows);
             }
              
              JSONArray array = json.getJSONArray("rows");
              Map employeeMap=getemployeecode();
              Map headTypeNameMap=getHeadTypeName();
              int count=0;
              for (int i = 0; i < array.length(); i++) {
              JSONObject obj= array.getJSONObject(i);
              if(employeeMap.containsKey(obj.get("EmployeeCode"))&& headTypeNameMap.containsKey(obj.get("HeadName"))){
              String objectId=  HeadAssignManager.AssignImport((String)employeeMap.get(obj.get("EmployeeCode")),(String)headTypeNameMap.get(obj.get("HeadName")));
              headAssignIDList.add(objectId);
              //System.out.println(objectId);
              count++;
             }else{
                  failure=obj.get("EmployeeCode")+",";
              }
              }
              if(failure!=null && failure.isEmpty()){
                  result="success";
              }else{
                  for (int k = 0; k< headAssignIDList.size(); k++) {
                  DBManager.getDbConnection().deleteDocument(ApplicationConstants.HEAD_ASSIGN_TABLE, headAssignIDList.get(k));
                }
                  result=failure;
              }
        
            
    
    
          
}catch(Exception e){
    e.printStackTrace();
}
           return result;
}
    public static void main(String[] args) throws Exception{
     File file= new File("C:\\Users\\user\\Desktop\\ManageHeadAssign.xlsx");
     manageHeadImport(file);
     

            
}
    public static Map<String,String> getemployeecode() throws Exception{
        HashMap<String, String> conditionMap = new HashMap<String, String>();

        Map<String, String> map = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
       List<Employee> list = new Gson().fromJson(result1, new TypeToken<List<Employee>>() {
        }.getType());
        for (Iterator<Employee> iterator = list.iterator(); iterator.hasNext();) {
            Employee next = iterator.next();
            map.put( next.getEmployeeCode(),((LinkedTreeMap<String, String>) next.getId()).get("$oid"));
        }
        return map;
    
    
    }
    
    public static Map<String,String> getHeadTypeName() throws Exception{
        HashMap<String, String> conditionMap = new HashMap<String, String>();

        Map<String, String> map = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HEAD_NAME_MASTER, conditionMap);
       List<HeadName> list = new Gson().fromJson(result1, new TypeToken<List<HeadName>>() {
        }.getType());
        for (Iterator<HeadName> iterator = list.iterator(); iterator.hasNext();) {
            HeadName next = iterator.next();
            map.put(next.getHeadName(),((LinkedTreeMap<String, String>) next.getId()).get("$oid"));
        }
        return map;
    }
}
