/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.payroll.manager;

import com.accure.payroll.dto.AttendanceAdj;
import com.accure.payroll.dto.EmpAttendance;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 *
 * @author upendra
 */
public class EmpAttendanceAdjManager {
     public String save(String attendance) throws Exception {
        Type type = new TypeToken<AttendanceAdj>() {
        }.getType();
        AttendanceAdj ddoDTO = new Gson().fromJson(attendance, type);
        ddoDTO.setCreateDate(System.currentTimeMillis() + "");
        ddoDTO.setUpdateDate(System.currentTimeMillis() + "");
        ddoDTO.setStatus(ApplicationConstants.ACTIVE);

        String ddoJson = new Gson().toJson(ddoDTO);

        String ddoresult = DBManager.getDbConnection().insert(ApplicationConstants.EMP_ATTENDANCE_ADJ_TABLE, ddoJson);
        return ddoresult;

    }
}
