/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.finance.dto.LedgerCodeMaster;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class LedgerOptionsManager {


    public static String getLedgerOtherThanGroups(String group, String group1) throws Exception {
        RestClient aql = new RestClient();
        String Active = "\"Active\"";

        String ledgerTable = ApplicationConstants.USG_DB1 + ApplicationConstants.LEDGER_TABLE + "`";
        String searchQuery = "";
        searchQuery = searchQuery + " underGroup!=" + group;
        searchQuery = searchQuery + " and underGroup!=" + group1;

        String ledgerQuery = "SELECT _id as id, ledgerName FROM " + ledgerTable + ""
                + " WHERE `underGroup` NOT IN (\"" + group + "\"" + ",\"" + group1 + "\")" + " and status=\"Active\"";

        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, ledgerQuery);
        

        List<Map<String, String>> list = new Gson().fromJson(dbOutput, new TypeToken<List<Map<String, String>>>() {
        }.getType());

        List<Map<String, String>> outlist = new ArrayList<Map<String, String>>();

        for (Map<String, String> li : list) {

            HashMap<String, String> hmap = new HashMap<String, String>();

            hmap.put("governmentLedgerCode", li.get("id"));
            hmap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

            String lcStr = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_CODE_TABLE, hmap);

            if (lcStr != null) {
                List<LedgerCodeMaster> lcmList = new Gson().fromJson(lcStr, new TypeToken<List<LedgerCodeMaster>>() {
                }.getType());
                String ledgerCode = lcmList.get(0).getLedgerCode();

                HashMap<String, String> map = new HashMap<String, String>();

                map.put("idStr", li.get("id"));
                map.put("ledgerCode", ledgerCode);
                map.put("ledgerName", li.get("ledgerName"));

                outlist.add(map);

            }

        }

        return new Gson().toJson(outlist);

    }
    
    
}
