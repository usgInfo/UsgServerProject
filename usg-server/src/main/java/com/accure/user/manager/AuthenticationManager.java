package com.accure.user.manager;

import com.accure.accure.security.Security;
import com.accure.user.manager.UserManager;
import com.accure.user.dto.User;
import com.accure.usg.server.utils.ApplicationConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deepak
 */
public class AuthenticationManager {

    public User authenticate(String userId, String password) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.USER_LOGIN_ID, userId);
        conditionMap.put(ApplicationConstants.USER_PASSWORD, Security.encryptPassword(password));
//        conditionMap.put(ApplicationConstants.USER_PASSWORD, password);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        List<User> users = new UserManager().getUserByCondition(conditionMap);
        if (users != null && users.size() > 0) {
            User user = users.get(0);
            user.setPassword(null);
            user.setId(((Map<String, String>) user.getId()).get("$oid"));

            return user;
        } else {
            return null;
        }
    }

    public static void main(String args[]) throws Exception {
        
//        //System.out.println("password: "+Security.decrypt("thQwyfkkoPGjbhxW6rZlPNwiqqgn/M5q4r9SrbBPz0U="));
        
    }
}
