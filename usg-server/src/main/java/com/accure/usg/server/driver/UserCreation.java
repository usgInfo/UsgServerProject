/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.server.driver;

import com.accure.usg.common.dto.OrgRole;
import com.accure.usg.common.dto.Role;
import com.accure.user.dto.User;
import com.accure.user.manager.RoleManager;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author deepak2310
 */
public class UserCreation {

    public static void main(String[] args) throws Exception {
        HashMap locationMap = new HashMap();
        String[] locations = {""};
        String[] roles = {"SuperAdmin"};

        ArrayList<OrgRole> or = new UserManager().getOrgRole(locations, roles);
        new UserCreation().createUser(or, "");
    }

    

    public String createUser(ArrayList<OrgRole> orList, String ddoId) throws Exception {
        //Setting the value to create user
        User user = new User();
        user.setFname("Super");
        user.setLname("Admin");
        user.setGender("Male");
        user.setMaritialstatus("Single");
        user.setMobile("9878833303");
        user.setEmail("user@superadmin.com");
        user.setLoginid("user@superadmin.com");
        user.setAddress("Bannerghatta road");
        user.setCity("Banglore");
        user.setState("Karnataka");
        user.setCountry("India");
        user.setCreatedDate(System.currentTimeMillis());
        user.setUpdatedDate(System.currentTimeMillis());
        user.setStatus(ApplicationConstants.ACTIVE);
        user.setPassword("lClyuY5DMRZg4C2S2/3SHNePylcY7/jWHeabRRKr1mM=");//accureone
        user.setZipcode("66213");
        user.setCreatedBy("Accure");
        user.setOrgRole(orList);
        user.setDdoId(ddoId);

        String userId = null;
        userId = DBManager.getDbConnection().insert(ApplicationConstants.USER_TABLE, new Gson().toJson(user));
        //System.out.println("User : \"" + user.getLoginid() + "\" is Created - " + userId);
        return userId;
    }

}
