package com.accure.user.manager;

import com.accure.db.in.DAO;
import com.accure.user.dto.User;
import com.accure.usg.common.dto.OrgRole;
import com.accure.usg.common.dto.Role;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author accure
 */
public class RoleManager {

    public Role fetch(String roleName) throws Exception {
        Role dbRole = null;
        if (roleName == null) {
            return dbRole;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put(ApplicationConstants.ROLE_NAME, roleName);
        DAO dao = DBManager.getDbConnection();
        String roleJson = dao.fetchAllRowsByConditions(ApplicationConstants.ROLE_TABLE, map);
//        dao.close();

        if (roleJson != null) {
            List<Role> roles = new Gson().fromJson(roleJson, new TypeToken<List<Role>>() {
            }.getType());
            dbRole = roles.get(0);
        }
        return dbRole;
    }

    public List<Role> fetchAllRoles() throws Exception {
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String rolesJson = dao.fetchAllRowsByConditions(ApplicationConstants.ROLE_TABLE, conditionMap);
//        dao.close();
        List<Role> rolesLists = new Gson().fromJson(rolesJson, new TypeToken<List<Role>>() {
        }.getType());
        return rolesLists;
    }

    public List<Role> fetchAllRolesByUser(String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        List<OrgRole> listOfRole = user.getOrgRole();
        List<Role> listofrole = new ArrayList<Role>();
        for (Iterator<OrgRole> iterator = listOfRole.iterator(); iterator.hasNext();) {
            OrgRole next = iterator.next();
            List<Role> listofroleDemo = next.getRole();
            if (listofroleDemo.size() > 0) {
                for (Iterator<Role> iterator1 = listofroleDemo.iterator(); iterator1.hasNext();) {
                    Role next1 = iterator1.next();
                    try {
                        listofrole.add(next1);
                    } catch (Exception e) {
                    }
                }
            }
//            listofrole.addAll(next.getRole());
        }
        List<String> userRoleListName = new ArrayList<String>();
        for (Iterator<Role> iterator = listofrole.iterator(); iterator.hasNext();) {
            Role next = iterator.next();
            userRoleListName.add(next.getRoleName());
        }
        Set<String> rolesThatLoggedInUserCanCreate = new HashSet<String>();
        for (Iterator<String> iterator = userRoleListName.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            if (next.equals(ApplicationConstants.ROLE_SUPER_ADMIN)) {
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_HRMS_ADMIN);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_HRMS_USER);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_PAYROLL_ADMIN);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_PAYROLL_USER);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_FINANCIAL_ADMIN);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_FINANCIAL_USER);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_BUDGET_ADMIN);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_BUDGET_USER);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_PENSION_ADMIN);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_LEAVE_ADMIN);
                break;
            } else if (next.equals(ApplicationConstants.ROLE_HRMS_ADMIN)) {
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_HRMS_ADMIN);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_HRMS_USER);
            } else if (next.equals(ApplicationConstants.ROLE_PAYROLL_ADMIN)) {
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_PAYROLL_ADMIN);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_PAYROLL_USER);
            } else if (next.equals(ApplicationConstants.ROLE_FINANCIAL_ADMIN)) {
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_FINANCIAL_ADMIN);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_FINANCIAL_USER);
            } else if (next.equals(ApplicationConstants.ROLE_BUDGET_ADMIN)) {
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_BUDGET_ADMIN);
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_BUDGET_USER);
            } else if (next.equals(ApplicationConstants.ROLE_PENSION_ADMIN)) {
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_PENSION_ADMIN);
            } else if (next.equals(ApplicationConstants.ROLE_LEAVE_ADMIN)) {
                rolesThatLoggedInUserCanCreate.add(ApplicationConstants.ROLE_LEAVE_ADMIN);
            }
        }

        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String rolesJson = dao.fetchAllRowsByConditions(ApplicationConstants.ROLE_TABLE, conditionMap);
//        dao.close();
        List<Role> rolesLists = new Gson().fromJson(rolesJson, new TypeToken<List<Role>>() {
        }.getType());
        for (Iterator<Role> iterator = rolesLists.iterator(); iterator.hasNext();) {
            Role next = iterator.next();
            if (!rolesThatLoggedInUserCanCreate.contains(next.getRoleName())) {
                iterator.remove();
            }
        }
        return rolesLists;
    }

    public static void main(String[] args) throws Exception {

        //System.out.println("-------------------------\n" + new RoleManager().fetchAllRolesByUser("585a43e9ff098b13bc4cf28a"));
    }
}
