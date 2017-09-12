/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.server.utils;

import com.accure.accure.db.Query;
import com.accure.finance.dto.PaymentVoucher;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;
//import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author amit
 */
public class Common {

    public static String getLogMsg(String strProcessName, String strCurrentStatus, String strErrorMsg) {
        String processId = UUID.randomUUID().toString();
        String strLogMsg = "process_id:" + processId + ",process_name:" + strProcessName + ","
                + "status:" + strCurrentStatus + ",error:" + strErrorMsg;
        return strLogMsg;
    }

    /**
     *
     * @return PropertiesConfiguration object for config. file
     */
    public static PropertiesConfiguration getConfig() {
        PropertiesConfiguration config = null;
        try {
            config = new PropertiesConfiguration("conf.properties");
        } catch (ConfigurationException ex) {
        }
        return config;
    }

    public static String getLogMsg(String orgId, String userId, String data, String action, String ip) {
        if ((orgId == null) || orgId.isEmpty() || userId == null || userId.isEmpty() || data == null || data.isEmpty()
                || action == null || action.isEmpty()) {
            return null;
        }

        String processId = UUID.randomUUID().toString();
        String strLogMsg = "process_id:" + processId + ",orgId:" + orgId + ",userId:" + userId + ",data:" + data + ",action:" + action + ",clientIp:" + ip;
        return strLogMsg;
    }

    public static String getDatefromMillis(String millis) {

        long currentDateTime = Long.parseLong(millis);
        Date outDate = new Date(currentDateTime);
        DateFormat df = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT);
        return df.format(outDate);
    }

    public Map<String, Object> onSuccess(String statusCode, String message, Object result) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("status", "success");
        returnMap.put("msg", message);
        returnMap.put("statuscode", statusCode);
        returnMap.put("result", result);
        return returnMap;
    }

    public Map<String, Object> onFailure(String statusCode, String message, Object result) {

        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("status", "failure");
        returnMap.put("msg", message);
        returnMap.put("statuscode", statusCode);
        returnMap.put("result", result);

        return returnMap;
    }

    public Map<String, Object> LoginonFailure(String statusCode, String message) {

        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("status", "failure");
        returnMap.put("msg", message);
        returnMap.put("statuscode", statusCode);

        return returnMap;
    }

    public String generateReferralCode() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890@#$&".toCharArray();
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

    public static double daysBetween(Date one, Date two) {
        long difference = (one.getTime() - two.getTime()) / 86400000;
        return Math.ceil(difference);
    }

    public static Long getMillis(String inputDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(inputDate);
        return date.getTime();
    }

    public static long daysBetween(long one, long two) {
        long difference = (one - two) / 86400000;
        return Math.abs(difference);
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    public static int daysBetweenTheDates(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String getShortMonthString(int month) {
        String monthString = "";
        switch (month) {
            case 1:
                monthString = "Jan";
                break;
            case 2:
                monthString = "Feb";
                break;
            case 3:
                monthString = "Mar";
                break;
            case 4:
                monthString = "Apr";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "Jun";
                break;
            case 7:
                monthString = "Jul";
                break;
            case 8:
                monthString = "Aug";
                break;
            case 9:
                monthString = "Sep";
                break;
            case 10:
                monthString = "Oct";
                break;
            case 11:
                monthString = "Nov";
                break;
            case 12:
                monthString = "Dec";
                break;
            default:
                monthString = "Invalid month";
                break;
        }
        return monthString;
    }

//    public int getNoOfDays(String strOrderDate, String strDeliveryDate) {
//        int days = (int) (Long.valueOf(strDeliveryDate) - Long.valueOf(strOrderDate)) / (1000 * 60 * 60 * 24);
//        return days;
//    }
//
//    public long getDayCount(String start, String end, String dateFormat) {
//        long diff = -1;
//        try {
//            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//            Date dateStart = formatter.parse(start);
//            Date dateEnd = formatter.parse(end);
//
//            //time is always 00:00:00 so rounding should help to ignore the missing hour when going from winter to summer time as well as the extra hour in the other direction
//            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
//        } catch (Exception e) {
//            e.getMessage();
//            //handle the exception according to your own situation
//        }
//        return diff;
//    }
    public static long calculateHoardingPrice(long existingPrice, long existingDays, long howManyDays) {
        long price = ((existingPrice * howManyDays) / existingDays);
        return price;
    }

    public static long getMillisFromDate(String inputDate) throws Exception {
        if (inputDate == null) {
            return 0;
        }
        //input date format must be in dd/MM/yyyy
        SimpleDateFormat sdf = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT);
        Date date = sdf.parse(inputDate);
        return date.getTime();
    }

    public static Date getDateFromString(String inputDate) throws Exception {
        if (inputDate == null) {
            return null;
        }
        //input date format must be in dd/MM/yyyy
        SimpleDateFormat sdf = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT);
        Date date = sdf.parse(inputDate);
        return date;
    }

    public static Long getMilliseconds(String inputDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date date = sdf.parse(inputDate);
        //System.out.println(date.getTime());
        return date.getTime();
    }

    public static Query createNameValueQuery(String name, String value) {
        Query q = new Query();
        q.setColumnName(name);
        q.setColumnValue(value);
        return q;
    }

    public static Query createNameValueConditionQuery(String name, String value, String condition) {
        Query q = new Query();
        q.setColumnName(name);
        q.setColumnValue(value);
        q.setCondition(condition);
        return q;
    }

    public static Query createNameMatchValuesConditionQuery(String org, String name, String[] matchValues, String condition) {
        Query q = new Query();
        q.setColumnName(org);
        q.setColumnName(name);
        q.setMatchValues(matchValues);
        q.setCondition(condition);
        return q;
    }

    public static Query createNameMatchValuesConditionQuery(String name, String[] matchValues, String condition) {
        Query q = new Query();
        q.setColumnName(name);
        q.setMatchValues(matchValues);
        q.setCondition(condition);
        return q;
    }

    public static int getDaysFromMonthAndYear(int year, int month) throws Exception {
        if (month == 0 || year == 0) {
            return -1;
        }
        //getting number of days based on month & year
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, (month - 1)); //in calendar month starts with 0
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    public static MongoCollection getCollection(String collectionName) throws Exception {
        MongoDatabase db = DBManager.getMongoDatabase();
        MongoCollection<Document> collection = db.getCollection(collectionName);
        return collection;
    }

    public static Document getFilterDocumentForFilterList(List<HashMap> filterList) {

        Document filterDocument = new Document();
        boolean isFilterAdded = false;
        for (HashMap<String, String> filterMap : filterList) {
            Set<Map.Entry<String, String>> filterSet = filterMap.entrySet();
            for (Map.Entry<String, String> filter : filterSet) {
                if (filter.getValue() != null && !filter.getValue().equalsIgnoreCase("")) {
                    if (filterDocument.containsKey(filter.getKey())) {
                        String value = (String) filterDocument.get(filter.getKey());
                        filterDocument.put("$or", Arrays.asList(new Document(filter.getKey(), new Document("$regex",
                                Pattern.quote(value)).append("$options", "i")),
                                new Document(filter.getKey(), new Document("$regex",
                                                Pattern.quote(filter.getValue())).append("$options", "i"))));
                    } else if (filterDocument.containsKey("$or")) {
                        List<Document> orDocument = (List) filterDocument.get("$or");
                        for (Document or : orDocument) {
                            if (or.containsKey(filter.getKey())) {
                                orDocument.add(new Document(filter.getKey(), new Document("$regex",
                                        Pattern.quote(filter.getValue())).append("$options", "i")));
                                isFilterAdded = true;
                            }
                        }
                        if (isFilterAdded) {
                            filterDocument.append("$or", orDocument);
                        }
                    }
                    if (!isFilterAdded) {
                        filterDocument.put(filter.getKey(), new Document("$regex",
                                Pattern.quote(filter.getValue())).append("$options", "i"));
                    }
                }
            }

        }

        filterDocument.put("status", ApplicationConstants.ACTIVE);

        return filterDocument;
    }

    public static String checkIfFieldExists(String collection, String fieldName, String fieldValue) {

        String result = null;
        try {
            Map<String, String> statusActiveMap = new HashMap();
            statusActiveMap.put(fieldName, fieldValue);
            statusActiveMap.put("status", "Active");

            result = DBManager.getDbConnection().fetchAllRowsByConditions(collection,
                    statusActiveMap);

        } catch (Exception exception) {
        }

        return result;

    }

    public static Object queryDbCollectionWithQuasar(List<String> fieldsList, String dbTableCollection,
            List<String> queryFilterList, List<String> groupByList, List<String> orderByList, Type type) {

        Object object = null;

        try {
            String query = "select ";

            if (fieldsList != null && !fieldsList.isEmpty()) {
                query += fieldsList.toString().replace("[", "").replace("]", "") + " ";
            } else {
                query += "* ";
            }

            if (dbTableCollection != null) {
                query += "from " + dbTableCollection + " ";
            }

            if (queryFilterList != null && !queryFilterList.isEmpty()) {
                query += "where " + queryFilterList.toString().replace("[", "").replace("]", "")
                        .replace(",", " and") + " ";
            }

            if (groupByList != null && !groupByList.isEmpty()) {
                query += "group by " + groupByList.toString().replace("[", "").replace("]", "") + " ";
            }

            if (orderByList != null && !orderByList.isEmpty()) {
                query += "order by " + orderByList.toString().replace("[", "").replace("]", "");
            }

            //System.out.println("query: " + query);

            RestClient restClient = new RestClient();
            String resultData = restClient.getRestData(ApplicationConstants.END_POINT, query);
            object = new Gson().fromJson(resultData, type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;

    }

    public static String removeNullAndEmptyValuesInMapAndBuildQuery(HashMap map, String query) {
        if (map != null) {
            Set<Map.Entry<String, String>> set = map.entrySet();
            Iterator<Map.Entry<String, String>> setIterator = set.iterator();
            while (setIterator.hasNext()) {
                Map.Entry<String, String> entry = setIterator.next();
                if (entry.getValue() == null || entry.getValue().equalsIgnoreCase("")) {
                    setIterator.remove();
                    continue;
                }

                Object value = entry.getValue() instanceof String
                        ? "\"" + entry.getValue() + "\"" : entry.getValue();

                if (query != null) {
                    if (query.contains("where")) {

                        query += " and " + entry.getKey() + "=" + value;
                    } else {
                        query += " where " + entry.getKey() + "=" + value;
                    }
                }

            }

        }

        return query;
    }

    public static void main(String[] args) {

        List<String> queryFList = new ArrayList();
        queryFList.add("DDO");
        queryFList.add("SUM(totalDrAmount)");

        List<String> groupByList = new ArrayList();
        groupByList.add("DDO");

//        List<String> queryFilterList = new ArrayList<String>();
//        queryFilterList.add("DDO=\"582f34960c92ec57796a1e13\"");
        List<String> orderByList = new ArrayList();
        orderByList.add("DDO");

        List<PaymentVoucher> paymentVoucherList = (List<PaymentVoucher>) Common.queryDbCollectionWithQuasar(
                queryFList, ApplicationConstants.USG_DB1 + "paymentvoucher`",
                null, groupByList, orderByList, new TypeToken<List<HashMap>>() {
                }.getType());

        //System.out.println("result: " + new Gson().toJson(paymentVoucherList));

    }
}
