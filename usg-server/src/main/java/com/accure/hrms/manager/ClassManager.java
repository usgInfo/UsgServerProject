/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Class;
import com.accure.hrms.dto.DAMaster;
import com.accure.hrms.dto.Gazetted;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author user
 */
public class ClassManager {

    public String saveClass(Class cla,String userid ) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        String cid=null;
        if(duplicate(cla))
        {
           cid = ApplicationConstants.DUPLICATE_MESSAGE;
            return cid; 
        }
        else
        {
        cla.setCreatedBy(userName);
        cla.setCreateDate(System.currentTimeMillis() + "");
        cla.setUpdateDate(System.currentTimeMillis() + "");
        cla.setStatus(ApplicationConstants.ACTIVE);

        String claJson = new Gson().toJson(cla);

         cid= DBManager.getDbConnection().insert(ApplicationConstants.CLASS_TABLE, claJson);
        }
        return cid;
    }

    public String fetchClass(String cId) throws Exception {
        if (cId == null || cId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CLASS_TABLE, cId);
        List<Class> classList = new Gson().fromJson(result, new TypeToken<List<Class>>() {
        }.getType());
        if (classList == null || classList.size() < 1) {
            return null;
        }
        return new Gson().toJson(classList.get(0));
    }

    /*public String deleteClass(String cId) throws Exception {
        if (cId == null || cId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<Class>() {
        }.getType();
        String cla = new ClassManager().fetchClass(cId);
        if (cla == null || cla.isEmpty()) {
            return false;
        }
        Class classJson = new Gson().fromJson(cla, type);
        String status = "";
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.DESIGNATION_TABLE, "clas", cId) || DeleteDependencyManager.hasDependency(ApplicationConstants.HEAD_SLAB_TABLE, "clas", cId)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        }
//        classJson.setStatus(ApplicationConstants.INACTIVE);
        else{
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CLASS_TABLE, cId, new Gson().toJson(classJson));
        if (result) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }*/

    public String deleteClass(String rid,String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        Class cla = fetchClass1(rid);

        cla.setUpdatedBy(userName);
        cla.setUpdateDate(System.currentTimeMillis() + "");
        cla.setStatus(ApplicationConstants.DELETE);
         String classJson = new Gson().toJson(cla);
        String status = "";
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.DESIGNATION_TABLE, "clas", rid)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.HEAD_SLAB_TABLE, "clas", rid))||(DeleteDependencyManager.hasDependency(ApplicationConstants.LEAVE_TYPE_MASTER, "leaveTypeDetails.employeeCategory", rid))) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.CLASS_TABLE, rid, classJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }
    public Class fetchClass1(String rid) throws Exception {
        if (rid == null) {
            return null;
        }
        String classJson = DBManager.getDbConnection().fetch(ApplicationConstants.CLASS_TABLE, rid);
        List<Class> classList = new Gson().fromJson(classJson, new TypeToken<List<Class>>() {
        }.getType());
        Class class1 = classList.get(0);
        return class1;
    }
    public String updateClass(Class cla, String cId,String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        cla.setUpdatedBy(userName);
        cla.setUpdateDate(System.currentTimeMillis() + "");
        cla.setStatus(ApplicationConstants.ACTIVE);
        HashMap duplicateCheckMap = new HashMap();
         String status = null;
          duplicateCheckMap.put("name", cla.getName());
        //duplicateCheckMap.put("gadNonGad", cla.getGadNonGad());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.CLASS_TABLE, duplicateCheckMap,cId))
        {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        }
            else
            {
        String classJson = new Gson().toJson(cla);
        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.CLASS_TABLE, cId, classJson);
          if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else 
            {
                status = ApplicationConstants.FAIL;
            }
           }
        return status;
    }

    public String fetchAllClass() throws Exception 
    {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CLASS_TABLE, conditionMap);

        List<Class> classList = new Gson().fromJson(result1, new TypeToken<List<Class>>() {
        }.getType());
        GAManager ga = new GAManager();

        for (Class cl : classList) {
            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.GAZETTED_NATURE_TABLE, cl.getGadNonGad());
            List<Gazetted> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Gazetted>>() {
            }.getType());
            Gazetted gal = gaList.get(0);
            cl.setGadNonGad(gal.getGazitted());
        }
        return new Gson().toJson(classList);

    }
    public boolean duplicate(Class clas) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("name", clas.getName());
            //conditionMap.put("gadNonGad", clas.getGadNonGad());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CLASS_TABLE, conditionMap);
            if (result != null) {
//                List<DAMaster> list = new Gson().fromJson(result, new TypeToken<List<DAMaster>>() {
//                }.getType());
//
//                for (DAMaster li : list) {
//                    if (damaster.getDefinedRate().equals(li.getDefinedRate()) && damaster.getPaidRate() == li.getPaidRate()) {
//                        res = true;
//                    }
//                }
                     res = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

}
