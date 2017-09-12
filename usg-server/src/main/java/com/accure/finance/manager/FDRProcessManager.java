/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.db.in.DAO;
import com.accure.finance.dto.FDRProcess;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author deepak2310
 */
public class FDRProcessManager {

    public String createFDRProcess(FDRProcess fdrProcess, String loginUserId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        fdrProcess.setCreateDate(System.currentTimeMillis() + "");
        fdrProcess.setUpdateDate(System.currentTimeMillis() + "");
        fdrProcess.setStatus(ApplicationConstants.ACTIVE);
        fdrProcess.setCreatedBy(userName);
//        fdrProcess.setFdMaturityAmount(String.format("%.2f", Double.parseDouble(fdrProcess.getFdMaturityAmount() + "")));
        String fdDate = fdrProcess.getFdDate();
        fdrProcess.setFdDateInMillis(sdf.parse(fdDate).getTime() + "");
        String enCashmentDate = fdrProcess.getFdEncashmentDate();
        fdrProcess.setFdEncashmentDateInMillis(sdf.parse(enCashmentDate).getTime() + "");
        String fdMaturityDate = fdrProcess.getFdMaturityDate();
        fdrProcess.setFdMaturityDateInMillis(sdf.parse(fdMaturityDate).getTime() + "");

        String fdrProcessJson = new Gson().toJson(fdrProcess);

        String fdrProcessId = DBManager.getDbConnection().insert(ApplicationConstants.FDR_PROCESS_TABLE, fdrProcessJson);
        if (fdrProcessId != null) {
            return fdrProcessId;
        }
        return null;
    }

    public List<FDRProcess> fetchAllFDRProcess() throws Exception {
        HashMap<String, String> bankName = new FixedDepositsManager().fetchAllBank();
        HashMap<String, String> paymentMode = new FixedDepositsManager().fetchAllBank();
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String fdrProcessJson = dao.fetchAllRowsByConditions(ApplicationConstants.FDR_PROCESS_TABLE, conditionMap);
//        dao.close();
        List<FDRProcess> fdrProcessList = new Gson().fromJson(fdrProcessJson, new TypeToken<List<FDRProcess>>() {
        }.getType());
        List<FDRProcess> returnList = new ArrayList<FDRProcess>();
        if (fdrProcessList != null) {
            for (FDRProcess fdrProcess : fdrProcessList) {
                if (fdrProcess.getPaymentMode() != null) {
                    String paymentModeId = fdrProcess.getPaymentMode();
                    if (paymentMode.containsKey(paymentModeId) && paymentMode.get(paymentModeId) != null) {
                        fdrProcess.setPaymentModeName(paymentMode.get(paymentModeId));
                    }
                }
                if (fdrProcess.getBank() != null) {
                    String bankId = fdrProcess.getBank();
                    if (bankName.containsKey(bankId) && bankName.get(bankId) != null) {
                        fdrProcess.setBankName(bankName.get(bankId));
                    }
                }
                returnList.add(fdrProcess);
            }
        }

        return returnList;
    }

    public boolean deleteFDRProcess(String fdrProcessId, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        FDRProcess fdrProcess = fetch(fdrProcessId);
        fdrProcess.setStatus(ApplicationConstants.DELETE);
        fdrProcess.setUpdatedBy(userName);

        String fdrProcessJson = new Gson().toJson(fdrProcess);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FDR_PROCESS_TABLE, fdrProcessId, fdrProcessJson);
        if (status) {
            return true;
        }
        return false;
    }

    public FDRProcess fetch(String fdrProcessId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();

        String fdrProcessJson = DBManager.getDbConnection().fetch(ApplicationConstants.FDR_PROCESS_TABLE, fdrProcessId);

        if (fdrProcessJson == null || fdrProcessJson.isEmpty()) {
            return null;
        }
        List<FDRProcess> fdrProcessList = new Gson().fromJson(fdrProcessJson, new TypeToken<List<FDRProcess>>() {
        }.getType());
        if (fdrProcessList == null || fdrProcessList.isEmpty()) {
            return null;
        }
        return fdrProcessList.get(0);

    }

    public boolean updateFDRProcess(FDRProcess fdrProcess, String userId, String fdrProcessId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        FDRProcess fdrProcessDB = fetch(fdrProcessId);
        if (fdrProcessDB.getFdType() != null) {
            fdrProcessDB.setFdType(fdrProcess.getFdType());
        }
        if (fdrProcessDB.getFdNature() != null) {
            fdrProcessDB.setFdNature(fdrProcess.getFdNature());
        }
        if (fdrProcessDB.getPaymentMode() != null) {
            fdrProcessDB.setPaymentMode(fdrProcess.getPaymentMode());
        }
        if (fdrProcessDB.getBank() != null) {
            fdrProcessDB.setBank(fdrProcess.getBank());
        }
        if (fdrProcessDB.getFdDate() != null) {
            fdrProcessDB.setFdDate(fdrProcess.getFdDate());
        }
        if (fdrProcessDB.getFdEncashmentDate()!= null) {
            fdrProcessDB.setFdEncashmentDate(fdrProcess.getFdEncashmentDate());
        }
        if (fdrProcessDB.getFdNumber() != null) {
            fdrProcessDB.setFdNumber(fdrProcess.getFdNumber());
        }
        if (fdrProcessDB.getFdAmount() != null) {
            fdrProcessDB.setFdAmount(fdrProcess.getFdAmount());
        }
        if (fdrProcessDB.getFdPeriod() != null) {
            fdrProcessDB.setFdPeriod(fdrProcess.getFdPeriod());
        }
        if (fdrProcessDB.getFdMaturityAmount() != null) {
            fdrProcessDB.setFdMaturityAmount(fdrProcess.getFdMaturityAmount());
        }
        if (fdrProcessDB.getFdEncashmentAmount() != null) {
            fdrProcessDB.setFdEncashmentAmount(fdrProcess.getFdEncashmentAmount());
        }
        if (fdrProcessDB.getFdMaturityDate() != null) {
            fdrProcessDB.setFdMaturityDate(fdrProcess.getFdMaturityDate());
        }
        if (fdrProcessDB.getFdRemarks() != null) {
            fdrProcessDB.setFdRemarks(fdrProcess.getFdRemarks());
        }

        fdrProcessDB.setUpdateDate(System.currentTimeMillis() + "");
        fdrProcessDB.setStatus(ApplicationConstants.ACTIVE);
        fdrProcessDB.setUpdatedBy(userName);

        String fdDate = fdrProcess.getFdDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fdrProcessDB.setFdDateInMillis(sdf.parse(fdDate).getTime() + "");

        String fdMaturityDate = fdrProcess.getFdMaturityDate();
        fdrProcessDB.setFdMaturityDateInMillis(sdf.parse(fdMaturityDate).getTime() + "");
        String fdEncashmentDate = fdrProcess.getFdEncashmentDate();
        fdrProcessDB.setFdEncashmentDateInMillis(sdf.parse(fdEncashmentDate).getTime() + "");

        String fdrProcessJson = new Gson().toJson(fdrProcessDB);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FDR_PROCESS_TABLE, fdrProcessId, fdrProcessJson);
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    public String getFdrProcess(FDRProcess fdrProcess) throws Exception {

        String fdrProcessResult = null;
        try {

            MongoDatabase db = DBManager.getMongoDatabase();
            MongoCollection<Document> collection = db.getCollection(ApplicationConstants.FDR_PROCESS_TABLE);

            MongoCollection<Document> bankCollection = db.getCollection(ApplicationConstants.BANK_NAME_TABLE);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Document document = new Document();
            if (fdrProcess.getFdType() != null && !fdrProcess.getFdType().equalsIgnoreCase("")) {
                document.put("fdType", fdrProcess.getFdType());
            }
            if (fdrProcess.getFdNature() != null && !fdrProcess.getFdNature().equalsIgnoreCase("")) {
                document.put("fdNature", fdrProcess.getFdNature());
            }
            if (fdrProcess.getFdFromDate() != null && !fdrProcess.getFdFromDate().equalsIgnoreCase("")) {
                ArrayList doc = (ArrayList) document.get("$and");
                if (doc == null) {
                    doc = new ArrayList();
                }
                doc.add(new Document("fdDateInMillis", new Document("$gte",
                        sdf.parse(fdrProcess.getFdFromDate()).getTime() + "")));
                document.put("$and", doc);
            }
            if (fdrProcess.getFdToDate() != null && !fdrProcess.getFdToDate().equalsIgnoreCase("")) {
                ArrayList doc = (ArrayList) document.get("$and");
                if (doc == null) {
                    doc = new ArrayList();
                }
                doc.add(new Document("fdDateInMillis", new Document("$lte",
                        sdf.parse(fdrProcess.getFdToDate()).getTime() + "")));
                document.put("$and", doc);
            }
            if (fdrProcess.getFdMaturityFromDate() != null && !fdrProcess.getFdMaturityFromDate().equalsIgnoreCase("")) {
                ArrayList doc = (ArrayList) document.get("$and");
                if (doc == null) {
                    doc = new ArrayList();
                }
                doc.add(new Document("fdMaturityDateInMillis", new Document("$gte",
                        sdf.parse(fdrProcess.getFdMaturityFromDate()).getTime() + "")));
                document.put("$and", doc);
            }
            if (fdrProcess.getFdMaturityToDate() != null && !fdrProcess.getFdMaturityToDate().equalsIgnoreCase("")) {
                ArrayList doc = (ArrayList) document.get("$and");
                if (doc == null) {
                    doc = new ArrayList();
                }
                doc.add(new Document("fdMaturityDateInMillis", new Document("$lte",
                        sdf.parse(fdrProcess.getFdMaturityToDate()).getTime() + "")));
                document.put("$and", doc);
            }

            document.put("status", "Active");

            FindIterable<Document> fdrProcessIterator = collection.find(document);
            if (fdrProcessIterator != null) {

                ArrayList<FDRProcess> resultList = new ArrayList();

                MongoCursor<Document> cursor = fdrProcessIterator.iterator();
                if (cursor != null) {
                    while (cursor.hasNext()) {
                        Document resultDocument = cursor.next();

                        FindIterable<Document> fItr = bankCollection.find(
                                new Document("_id", new ObjectId(resultDocument.get("bank") + "")));
                        MongoCursor<Document> bankCursor = fItr.iterator();
                        if (bankCursor != null) {
                            while (bankCursor.hasNext()) {
                                Document bankDocument = bankCursor.next();
                                resultDocument.put("bankName", bankDocument.get("bankName"));
                            }
                            bankCursor.close();
                        }

//                        resultDocument.put("fdInterestAmount", String.format("%.2f",
//                                Double.parseDouble(resultDocument.get("fdInterestAmount") + "")));
//
//                        resultDocument.put("fdMaturityAmount", String.format("%.2f",
//                                Double.parseDouble(resultDocument.get("fdMaturityAmount") + "")));

                        resultList.add((FDRProcess) new Gson().fromJson(resultDocument.toJson(),
                                new TypeToken<FDRProcess>() {
                                }.getType()));
                    }
                    cursor.close();
                }
                fdrProcessResult = new Gson().toJson(resultList);
            }
        } catch (NumberFormatException numberFormatException) {
        } catch (JsonSyntaxException ex) {

        }

        return fdrProcessResult;
    }

}
