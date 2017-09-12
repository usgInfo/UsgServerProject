/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.usg.common.manager;

import javax.servlet.http.HttpSession;

/**
 *
 * @author Upendra
 */
public class SessionManager {

    public static boolean checkUserSession(HttpSession loginSession) {
        if ((loginSession == null) || (loginSession.toString().isEmpty())) {
            return false;
        } else if (loginSession.isNew()) {
            return false;
        } else {
            return true;
        }
    }
}
