/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.common.duplicate;

import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.types.ObjectId;
import java.util.List;
import com.accure.usg.common.manager.DBManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.map.CaseInsensitiveMap;

/**
 *
 * @author user
 */
public class Duplicate {

    public static boolean hasDuplicateforSave(String tableName, HashMap columns) {
        boolean status = false;
        DBCursor cursor = null;
        PropertiesConfiguration config = getConfig();
        DBCollection collection = null;
        BasicDBObject duplicatecheckMap = null;
        try {
            DB db = DBManager.getDB();
            collection = db.getCollection(tableName);
            duplicatecheckMap = new BasicDBObject();
            if (!columns.isEmpty()) {
                Iterator entries = columns.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                  //  String searchKey=(String) entry.getValue();
                    
                    
                    
                   //  "(^" + searchKey + "|\\W" + searchKey + ")";
//                    duplicatecheckMap.put((String) entry.getKey(), Pattern.compile("^" + ((String) entry.getValue()).replace("\\s", "") + "$", (Pattern.CASE_INSENSITIVE | Pattern.COMMENTS)));
                    duplicatecheckMap.put((String) entry.getKey(), Pattern.compile("^" + (String) entry.getValue() + "$", (Pattern.CASE_INSENSITIVE)));

                  //  duplicatecheckMap.put((String) entry.getKey(), Pattern.compile( "(^" + searchKey + "|\\W" + searchKey + ")", (Pattern.CASE_INSENSITIVE|Pattern.COMMENTS)));
                }
                duplicatecheckMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                System.out.println(duplicatecheckMap);
                cursor = collection.find(duplicatecheckMap);
                status = cursor.hasNext();
            }

        } catch (Exception e) {
            status = false;
        } finally {
            System.out.println(status);
            return status;
        }
    }

    public static boolean hasDuplicateforUpdate(String tableName, HashMap columns, String id) {
        boolean status = false;
        String result = null;
        DBCursor cursor = null;
        PropertiesConfiguration config = getConfig();
        DBCollection collection = null;
        BasicDBObject duplicatecheckMap = null;
        try {
            DB db = DBManager.getDB();
            collection = db.getCollection(tableName);
            duplicatecheckMap = new BasicDBObject();
            if (!columns.isEmpty()) {
                Iterator entries = columns.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    duplicatecheckMap.put((String) entry.getKey(), Pattern.compile("^" + (String) entry.getValue() + "$", (Pattern.CASE_INSENSITIVE)));

                    //duplicatecheckMap.put((String) entry.getKey(), Pattern.compile("^" + ((String) entry.getValue()).replace("\\s", "") + "$", (Pattern.CASE_INSENSITIVE | Pattern.COMMENTS)));
                }
                duplicatecheckMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                cursor = collection.find(duplicatecheckMap);
                status = cursor.hasNext();
                if (status) {
                    duplicatecheckMap.put("_id", new ObjectId(id));
                    cursor = collection.find(duplicatecheckMap);
                    status = cursor.hasNext();
                }

            }
        } catch (Exception e) {
            status = false;
        } finally {
            return status;
        }
    }

    public static void main(String[] args) {
        HashMap map = new HashMap();
        String name = "checkcpf";
        map.put("PFType", Pattern.compile(name, Pattern.COMMENTS | Pattern.CASE_INSENSITIVE));
        map.put("status", ApplicationConstants.ACTIVE);
        hasDuplicateforSave("pftype", map);
    }

    public static boolean isDuplicateforUpdate(String tableName, HashMap columns, String id) {
        boolean status = false;
        String result = null;
        HashMap duplicatecheckMap = new HashMap();
        try {
            if (!columns.isEmpty()) {
                Iterator entries = columns.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    // duplicatecheckMap.put((String) entry.getKey(), Pattern.compile("^" + ((String) entry.getValue()).replace("\\s", "") + "$", (Pattern.CASE_INSENSITIVE|Pattern.COMMENTS)));

                    duplicatecheckMap.put((String) entry.getKey(), Pattern.compile("^" + (String) entry.getValue() + "$", (Pattern.CASE_INSENSITIVE)));
                }
                duplicatecheckMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(tableName, duplicatecheckMap);
                if (result1 == null) {
                    status = false;
                    return status;
                } else {
                    List<Map> employeeList = new Gson().fromJson(result1, new TypeToken<List<Map>>() {
                    }.getType());
                    for (Map city : employeeList) {
                        String obid = ((Map<String, String>) city.get("_id")).get("$oid");

                        if (!id.equals(obid)) {
                            status = true;
                            break;
                        } else {
                            continue;

                        }
                    }
                }
            }
        } catch (Exception e) {
            status = false;
        } finally {
            return status;
        }
    }
}
