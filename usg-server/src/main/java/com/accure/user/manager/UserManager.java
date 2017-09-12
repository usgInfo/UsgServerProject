/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.manager;

import com.accure.accure.security.Security;
import com.accure.db.in.DAO;
import com.accure.finance.dto.Location;
import com.accure.finance.manager.LocationManager;
import com.accure.hrms.dto.Employee;
import com.accure.usg.common.dto.OrgRole;
import com.accure.usg.common.dto.Role;
import com.accure.user.dto.User;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.EmailManager;
import com.accure.usg.common.manager.SearchManager;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author deepak2310
 */
public class UserManager {

    public List<User> getUserByCondition(HashMap<String, String> columnCondition) throws Exception {
        Map<String, Map<String, String>> condition = new HashMap<String, Map<String, String>>();
        Set<String> keys = columnCondition.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            Map<String, String> cond = new HashMap<String, String>();
            String key = (String) it.next();
            cond.put(ApplicationConstants.EQUAL, columnCondition.get(key));
            condition.put(key, cond);
        }
        String result = DBManager.getDbConnection().fetchRowsByConditions(ApplicationConstants.USER_TABLE, condition);
        if (result != null && !result.equals("")) {
            Type type = new TypeToken<List<User>>() {
            }.getType();
            List<User> users = new Gson().fromJson(result, type);
            return users;
        } else {
            return null;
        }
    }

    public static boolean checkUserPrivilege(User user, String privilege) {
        Type type = new TypeToken<User>() {
        }.getType();
        String result = new Gson().toJson(user, type);
        return result.contains(privilege);
    }

    public boolean checkAvailability(String key, String value) throws Exception {
        //returns true if available
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(key, value);
        List<User> users = new UserManager().getUserByCondition(conditionMap);
        return users != null && !users.isEmpty();
    }

    public List<User> fetchAllUser() throws Exception {
        HashMap<String, String> ddo = new SearchManager().fetchallDDO();
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        List<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String userJson = dao.fetchAllRowsByConditions(ApplicationConstants.USER_TABLE, conditionMap);
//        dao.close();
        List<User> userList = new Gson().fromJson(userJson, new TypeToken<List<User>>() {
        }.getType());
        List<User> returnLists = new ArrayList<User>();
        if (userList != null) {
            for (User user : userList) {
                if (user.getDdoId() != null) {
                    String id = user.getDdoId();
                    if (ddo.containsKey(id) && ddo.get(id) != null) {
                        user.setDdoName(ddo.get(id));
                    }
                }
                user = updateLocationNames(user);

                returnLists.add(user);
            }
        }

        return returnLists;
    }

    public User fetch(String userId) throws Exception {
        if (userId == null || userId.isEmpty()) {
            return null;
        }
        String userJson = DBManager.getDbConnection().fetch(ApplicationConstants.USER_TABLE, userId);
        if (userJson == null || userJson.isEmpty()) {
            return null;
        }
        List<User> userList = new Gson().fromJson(userJson, new TypeToken<List<User>>() {
        }.getType());
        if (userList == null || userList.isEmpty()) {
            return null;
        }
        return userList.get(0);

    }

    public boolean updateUserProfile(User user, String UserId) throws Exception {
        if (user == null || UserId == null || UserId.isEmpty()) {
            return false;
        }

        User dbUser = fetch(UserId);

        String userData;
        String latestUserData;

        userData = dbUser.getFname();
        latestUserData = user.getFname();

        if (userData != null) {
            if (userData.compareTo(latestUserData) != 0) {
                dbUser.setFname(latestUserData);
            }
        }

        userData = dbUser.getLname();
        latestUserData = user.getLname();

        if (userData != null) {
            if (userData.compareTo(latestUserData) != 0) {
                dbUser.setLname(latestUserData);
            }
        }

        userData = dbUser.getMobile();
        latestUserData = user.getMobile();

        if (userData != null) {
            if (userData.compareTo(latestUserData) != 0) {
                dbUser.setMobile(latestUserData);
            }
        }

        userData = dbUser.getEmail();
        latestUserData = user.getEmail();

        if (userData != null) {
            if (userData.compareTo(latestUserData) != 0) {
                dbUser.setEmail(latestUserData);
                dbUser.setLoginid(latestUserData);
            }
        }

        dbUser.setUpdateDate(System.currentTimeMillis() + "");
        boolean status = update(dbUser, UserId);

        if (status) {
            return true;
        }
        return false;

    }

    public boolean update(User user, String userId) throws Exception {
        Type type = new TypeToken<User>() {
        }.getType();
        String userString = new Gson().toJson(user, type);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.USER_TABLE, userId, userString);
        return status;
    }

    public boolean changePassword(String oldPass, String newPass, User user) throws Exception {
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put(ApplicationConstants.USER_PASSWORD, Security.encryptPassword(oldPass));
        condition.put(ApplicationConstants.USER_LOGIN_ID, user.getLoginid());
        List<User> users = getUserByCondition(condition);
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
            user.setPassword(Security.encryptPassword(newPass));
            Type type = new TypeToken<User>() {
            }.getType();
            String userJson = new Gson().toJson(user, type);
            String userId = ((Map<String, String>) user.getId()).get("$oid");
            boolean status = update(user, userId);
            user.setPassword("");
            return status;
        } else {
            return false;
        }
    }

    public boolean forgotPassword(String emailId) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.EMAIL_ID, emailId);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        List<User> users = getUserByCondition(conditionMap);
        if (users != null && !users.isEmpty()) {
            SecureRandom random = new SecureRandom();
            String randomPassword = new BigInteger(130, random).toString(32);
            String subject = (String) getConfig().getProperty("new-password-subject");
            new EmailManager().sendEmail(emailId, subject, randomPassword);
            User user = users.get(0);
            user.setPassword(Security.encryptPassword(randomPassword));
            Map<String, String> idMap = (Map<String, String>) user.getId();
            boolean status = update(user, idMap.get("$oid"));
            return status;
        } else {
            return false;
        }

    }

    public String generateRandom() {
        List<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        String result = "";
        for (int i = 0; i < 5; i++) {
            result += numbers.get(i).toString();
        }
        return result;
    }

    public boolean deleteUser(String userId, String currentUserLogin) throws Exception {

        User dbUser = fetch(userId);
        dbUser.setStatus(ApplicationConstants.DELETE);
        dbUser.setUpdatedBy(currentUserLogin);
        dbUser.setUpdateDate(System.currentTimeMillis() + "");

        boolean status = update(dbUser, userId);

        if (status) {
            return true;
        }
        return false;
    }

    public boolean saveUserInformation(String userJson, String userId, String userRoles, String loginUserId) throws Exception {

//        Type userType = new TypeToken<User>() {
//        }.getType();
//        User userJsonUi = new Gson().fromJson(userJson, userType);
//
//        User loginUser = fetch(loginUserId);
//        String loginUserName = loginUser.getFname();
//
//        User userdb = new UserManager().fetch(userId);
//
//        userdb.setLoginid(userJsonUi.getEmail());
//        userdb.setFname(userJsonUi.getFname());
//        userdb.setLname(userJsonUi.getLname());
//        userdb.setGender(userJsonUi.getGender());
//        userdb.setMobile(userJsonUi.getMobile());
//        userdb.setEmail(userJsonUi.getEmail());
//        userdb.setAddress(userJsonUi.getAddress());
//        userdb.setCity(userJsonUi.getCity());
//        userdb.setState(userJsonUi.getState());
//        userdb.setCountry(userJsonUi.getCountry());
//        userdb.setZipcode(userJsonUi.getZipcode());
//
//        String[] roles = userRoles.split(",");
//        List<Role> roleList = new ArrayList<Role>();
//        for (String rolename : roles) {
//            Map<String, String> conditionMap = new HashMap<String, String>();
//            conditionMap.put(ApplicationConstants.ROLE_NAME, rolename);
//            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ROLE_TABLE, conditionMap);
//            if (result != null && !result.equals("")) {
//                Type type = new TypeToken<List<Role>>() {
//                }.getType();
//                List<Role> rolelist = new Gson().fromJson(result, type);
//                Role availRole = rolelist.get(0);
//                roleList.add(availRole);
//            }
//        }
//
//        OrgRole orgrole = new OrgRole();
//        orgrole.setOrg(org);
//        orgrole.setId(UUID.randomUUID());
//        orgrole.setRole(roleList);
//
//        List<OrgRole> orgRoleList = userdb.getOrgRole();
//        List<OrgRole> newOrgRoleList = new ArrayList<OrgRole>();
//        newOrgRoleList.add(orgrole);
//        for (int i = 0; i < orgRoleList.size(); i++) {
//            if (orgRoleList.get(i).getOrg().getName().equals(org.getName())) {
//            } else {
//                newOrgRoleList.add(orgRoleList.get(i));
//            }
//        }
//        userdb.setOrgRole(newOrgRoleList);
//        userdb.setUpdateDate(System.currentTimeMillis() + "");
//        userdb.setUpdatedBy(loginUserName);
//
//        boolean status = update(userdb, userId);
//        if (status) {
//            return true;
//        }
        return false;
    }

    public boolean resetPassword(String strId) throws Exception {
        SecureRandom random = new SecureRandom();
        String randomPassword = new BigInteger(130, random).toString(32);
        User user = fetch(strId);
        if (user != null) {
            user.setPassword(Security.encryptPassword(randomPassword));
            boolean status = update(user, strId);
            if (status) {
                String subject = (String) getConfig().getProperty("new-password-subject");
                new EmailManager().sendEmail(user.getEmail(), subject, randomPassword);
            }
            return status;
        } else {
            return false;
        }
    }

//    public Boolean registerEmployIntoUser(Employee employData, String employRoles, String userId) throws Exception {
//
//        User UserDB = fetch(userId);
//        String name = UserDB.getFname();
//
//        User userData = new User();
//        userData.setFname(employData.getEmployeeName());
//        userData.setLname(employData.getEmployeeName());
//        userData.setEmail(employData.getEmail());
//        userData.setLoginid(employData.getEmail());
//        userData.setGender(employData.getGender());
//
//        String[] roles = employRoles.split(",");
//        List<Role> roleList = new ArrayList<Role>();
//        for (String rolename : roles) {
//            Map<String, String> conditionMap = new HashMap<String, String>();
//            conditionMap.put(ApplicationConstants.ROLE_NAME, rolename);
//            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ROLE_TABLE, conditionMap);
//            if (result != null && !result.equals("")) {
//                Type type = new TypeToken<List<Role>>() {
//                }.getType();
//                List<Role> rolelist = new Gson().fromJson(result, type);
//                Role availRole = rolelist.get(0);
//                roleList.add(availRole);
//            }
//        }
//
//        String orgJson = DBManager.getDbConnection().fetchAll(ApplicationConstants.ORGANIZATION_TABLE);
//        Type type = new TypeToken<List<Organization>>() {
//        }.getType();
//        List<Organization> org = new Gson().fromJson(orgJson, type);
//        Organization orgData = org.get(0);
//
//        OrgRole orgrole = new OrgRole();
//        orgrole.setId(UUID.randomUUID());
//        orgrole.setRole(roleList);
//        orgrole.setOrg(orgData);
//        List<OrgRole> orgRoleList = new ArrayList<OrgRole>();
//        orgRoleList.add(orgrole);
//
//        userData.setOrgRole(orgRoleList);
//        userData.setCreateDate(System.currentTimeMillis() + "");
//        userData.setUpdateDate(System.currentTimeMillis() + "");
//        userData.setCreatedBy(name);
//        userData.setStatus(ApplicationConstants.ACTIVE);
//        String userJson = new Gson().toJson(userData);
//
//        String employId = DBManager.getDbConnection().insert(ApplicationConstants.USER_TABLE, userJson);
//
//        boolean status = resetPassword(employId);
//        if (status) {
//            return true;
//        }
//        return false;
//    }
    public ArrayList<OrgRole> getOrgRole(String[] locations, String[] roles) throws Exception {
        if (roles != null && roles.length > 0) {
            OrgRole or = new OrgRole();
            //get & set the location
            List ll = new ArrayList();
            for (String loc : locations) {
                ll.add(loc);
            }

            //get & set the roles
            List<Role> rolesList = new ArrayList<Role>();
            for (String roleName : roles) {
                Role dbRole = new RoleManager().fetch(roleName);
                rolesList.add(dbRole);
            }

            or.setLocation(ll);
            or.setRole(rolesList);
            ArrayList<OrgRole> orList = new ArrayList<OrgRole>();
            orList.add(or);
            return orList;
        } else {
            return null;
        }
    }

    public String createEmployeeAsUser(Employee employData, String[] locations, String[] roles, String loginUserId, String loginId, String password) throws Exception {
        if (employData != null && locations != null && locations.length > 0 && roles != null && roles.length > 0 && loginUserId != null && loginId != null && password != null) {

            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            User userData = new User();
            //credentials
            userData.setLoginid(loginId);
            userData.setPassword(Security.encryptPassword(password));
            //demographics
            userData.setEmployeeId(employData.getpKey());
            userData.setFname(employData.getEmployeeName());
            userData.setLname(employData.getEmployeeName());
            userData.setEmail(employData.getEmail());
            userData.setGender(employData.getGender());
            userData.setDdoId(employData.getDdo());

            //Locations & Roles
            ArrayList<OrgRole> orgRole = getOrgRole(locations, roles);
            userData.setOrgRole(orgRole);

            //meta data
            userData.setCreatedDate(System.currentTimeMillis());
            userData.setUpdatedDate(System.currentTimeMillis());
            userData.setCreatedBy(userName);
            userData.setStatus(ApplicationConstants.ACTIVE);

            String employId = DBManager.getDbConnection().insert(ApplicationConstants.USER_TABLE, new Gson().toJson(userData));
            return employId;
        } else {
            return null;
        }
    }

    public boolean updateUserInformation(String userJson, String userId, String[] userRoles, String[] locations, String loginUserId) throws Exception {
        if (userJson != null && userId != null && userRoles.length > 0 && userRoles != null && locations.length > 0 && locations != null && loginUserId != null) {
            //ui user json
            Type userType = new TypeToken<User>() {
            }.getType();
            User userJsonUi = new Gson().fromJson(userJson, userType);

            //db user
            User userdb = new UserManager().fetch(userId);
            userdb.setFname(userJsonUi.getFname());
            userdb.setLname(userJsonUi.getLname());
            userdb.setGender(userJsonUi.getGender());
            userdb.setMobile(userJsonUi.getMobile());
            userdb.setEmail(userJsonUi.getEmail());
            userdb.setAddress(userJsonUi.getAddress());
            userdb.setCity(userJsonUi.getCity());
            userdb.setState(userJsonUi.getState());
            userdb.setCountry(userJsonUi.getCountry());
            userdb.setZipcode(userJsonUi.getZipcode());

            //Locations & Roles
            ArrayList<OrgRole> orgRole = getOrgRole(locations, userRoles);
            userdb.setOrgRole(orgRole);

            //meta data
            userdb.setUpdatedDate(System.currentTimeMillis());
            userdb.setUpdatedBy(loginUserId);

            boolean status = update(userdb, userId);
            return status;
        } else {
            return false;
        }
    }

    public User updateLocationNames(User user) throws Exception {
        if (user != null) {
            List<OrgRole> listOfOrgRole = user.getOrgRole();
            List<String> locationIdList = new ArrayList<String>();
            if (listOfOrgRole != null) {
                for (Iterator<OrgRole> iterator = listOfOrgRole.iterator(); iterator.hasNext();) {
                    OrgRole next = iterator.next();
                    List<String> locationIdsList = next.getLocation();
                    for (Iterator<String> iterator1 = locationIdsList.iterator(); iterator1.hasNext();) {
                        String next1 = iterator1.next();
                        if (next1 != null && !next1.isEmpty()) {
                            locationIdList.add(next1);
                        }

                    }
                }
            }
            List<String> returnLocationListNames = new ArrayList<String>();
            if (locationIdList != null) {
                for (Iterator<String> iterator = locationIdList.iterator(); iterator.hasNext();) {
                    String next = iterator.next();
                    Location loc = new LocationManager().fetch(next);
                    if (loc != null) {
                        returnLocationListNames.add(loc.getLocationName());
                    }
                }
            }
            user.setLocationNames(returnLocationListNames);
            return user;
        } else {
            return null;
        }
    }
}
