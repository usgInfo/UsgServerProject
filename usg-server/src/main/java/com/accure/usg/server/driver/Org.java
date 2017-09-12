/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.server.driver;

import com.accure.usg.common.dto.Organization;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author deepak2310
 */
public class Org {

    public static void main(String[] args) throws Exception {

        Organization org = new Organization();
        org.setName("Accure University");
        org.setPhone("9999999999");
        org.setAlternatePhone("8888888888");
        org.setEmail("accure@accureanalytics.com");
        org.setAlternateEmail("accure2@gmail.com");
        org.setAddress("Cranes building,Bannerghatta main road");
        org.setAlternateAddress("Cranes building,Bannerghatta main road");
        org.setCity("Banglore");
        org.setState("Karnataka");
        org.setCountry("India");
        org.setZip("143001");
        org.setFax("1234567890");
        org.setType("University");
        org.setCategory("Government");
        org.setStatus(ApplicationConstants.ACTIVE);
        org.setCreateDate(System.currentTimeMillis() + "");
        org.setUpdateDate(System.currentTimeMillis() + "");

        String orgId = DBManager.getDbConnection().insert(ApplicationConstants.ORGANIZATION_TABLE, new Gson().toJson(org));
        //System.out.println(orgId);

        //For fetching the data
//        String Id = "5741c03644ae3e94fc47899b";
//        String json = DBManager.getDbConnection().fetch(ApplicationConstants.ORGANIZATION_TABLE, Id);
//        Type type = new TypeToken<List<Organization>>() {
//        }.getType();
//        List<Organization> org = new Gson().fromJson(json, type);
//        //System.out.println("");
    }

}
