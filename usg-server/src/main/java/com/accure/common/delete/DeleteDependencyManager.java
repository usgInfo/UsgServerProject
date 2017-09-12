/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.common.delete;

import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author mano
 */
public class DeleteDependencyManager {

    public static boolean hasDependency(String dependencyTable, String dependencyColumn, String dependencyColumnValue) {
        PropertiesConfiguration config = getConfig();
        boolean status = false;
        DBCursor cursor = null;
        DBCollection collection = null;
        BasicDBObject query = null;
        try {
            DB db = DBManager.getDB();
            collection = db.getCollection(dependencyTable);
            query = new BasicDBObject();
            query.put(dependencyColumn, dependencyColumnValue);
            query.put("status", ApplicationConstants.ACTIVE);
            cursor = collection.find(query);
            status = cursor.hasNext();
        } catch (Exception e) {
            status = false;
        } finally {
            cursor = null;
            query = null;
            return status;
        }
    }

    public static boolean hasDependencyFinancialYear(String dependencyTable, int fromMonth, int fromYear, int toMonth, int toyear) {
        boolean status = false;
        String Active = "\"Active\"";
        try {
            String mongoCollectionName = ApplicationConstants.USG_DB1 + dependencyTable + "`";
            int fromYeartoMonth = 12 - fromMonth;
            String fromYearQuery = "select  *from  " + mongoCollectionName + " where month BETWEEN " + fromMonth + " and " + (fromMonth + fromYeartoMonth) + " and year =" + fromYear + " and status =" + Active;
            String fromObjectSL = new RestClient().getRestData(ApplicationConstants.END_POINT, fromYearQuery);
            if (fromObjectSL != null && !fromObjectSL.isEmpty() && !fromObjectSL.equals("[]")) {
                status = true;
                return status;
            } else {
                int toYeartoMonth = toMonth;
                String toYearQuery = "select  *from  " + mongoCollectionName + " where month BETWEEN " + 1 + " and " + (toYeartoMonth) + " and year =" + toyear + " and status =" + Active;
                String toObjectSL = new RestClient().getRestData(ApplicationConstants.END_POINT, toYearQuery);
                if (toObjectSL != null && !toObjectSL.isEmpty() && !toObjectSL.equals("[]")) {
                    status = true;
                }
            }
        } catch (Exception e) {
            status = false;
            return status;
        } finally {
            return status;

        }
    }

  
}
