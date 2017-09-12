/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.analytics;

import com.accure.finance.dto.ContraVoucher;
import com.accure.finance.dto.FinancialAccounting_FinancialYear;
import com.accure.finance.dto.JournalVoucher;
import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.dto.ReceiptVoucher;
import static com.accure.user.analytics.AnalyticsManager.convertDataToMillsec;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class FinancialAnalyticsManager {

    public String getVoucherCountByFY(String fy, String ddo, String location) throws Exception {
        if (null == fy || fy.isEmpty()) {
            return null;
        }
        int recCount = 0;
        int payCount = 0;
        int conCount = 0;
        int jouCount = 0;
        HashMap<Integer, Long> result = new HashMap<Integer, Long>();

        HashMap<Integer, HashMap<Long, Long>> fyMap = new HashMap<Integer, HashMap<Long, Long>>();
        // get all available FY from fy collection
        String strFy = DBManager.getDbConnection().fetchAll(ApplicationConstants.FINANCIAL_YEAR_TABLE);
        if (null != strFy && !strFy.isEmpty()) {
            List<FinancialAccounting_FinancialYear> financialYear = new Gson().fromJson(strFy, new TypeToken<List<FinancialAccounting_FinancialYear>>() {
            }.getType());
            for (FinancialAccounting_FinancialYear fYear : financialYear) {
                HashMap<Long, Long> map = new HashMap<Long, Long>();
                map.put(convertDataToMillsec(fYear.getFromDate()), convertDataToMillsec(fYear.getToDate()));
                fyMap.put(Integer.parseInt(fYear.getYear()), map);
                result.put(Integer.parseInt(fYear.getYear()), 0l);
            }

//            HashMap<String, String> cond = new HashMap<String, String>();
//            cond.put("ddoName", "Gujarat");
//            String strDdoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, cond);
            if (null != ddo && !ddo.isEmpty()) {
//                List<DDO> ddoList = new Gson().fromJson(strDdoJson, new TypeToken<List<DDO>>() {
//                }.getType());
                HashMap<String, String> cond = new HashMap<String, String>();
                cond.put("DDO", ddo);
                cond.put("location", location);
                cond.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                // get all employee collection
                String reciptList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.RECEIPT_VOUCHER_TABLE, cond);
                if (null != reciptList && !reciptList.isEmpty()) {
                    List<ReceiptVoucher> employee = new Gson().fromJson(reciptList, new TypeToken<List<ReceiptVoucher>>() {
                    }.getType());
                    for (ReceiptVoucher emp : employee) {
                        if (null != emp.getVoucherDate() && !emp.getVoucherDate().isEmpty()) {
                            // //System.out.println(emp.getDateOfJoining());
                            Long voucherDate = convertDataToMillsec(emp.getVoucherDate());
                            int year = Integer.parseInt(fy);
                            HashMap<Long, Long> value = fyMap.get(year);
                            for (Map.Entry<Long, Long> m : value.entrySet()) {
                                Long fyrFromdate = m.getKey();
                                Long fyrTodate = m.getValue();
                                if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
                                    recCount++;
                                }

                            }
                        }
                    }
                }
                //       -------------Payment Voucher-----------------       
                String paymentList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PAYMENT_VOUCHER_TABLE, cond);
                if (null != paymentList && !paymentList.isEmpty()) {
                    List<PaymentVoucher> payVouList = new Gson().fromJson(paymentList, new TypeToken<List<PaymentVoucher>>() {
                    }.getType());
                    for (PaymentVoucher paymentVouc : payVouList) {
                        if (null != paymentVouc.getVoucherDate() && !paymentVouc.getVoucherDate().isEmpty()) {
                            // //System.out.println(emp.getDateOfJoining());
                            Long voucherDate = convertDataToMillsec(paymentVouc.getVoucherDate());
                            int year = Integer.parseInt(fy);
                            HashMap<Long, Long> value = fyMap.get(year);
                            for (Map.Entry<Long, Long> m : value.entrySet()) {
                                Long fyrFromdate = m.getKey();
                                Long fyrTodate = m.getValue();
                                if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
                                    payCount++;
                                }

                            }
                        }
                    }
                }

                // ----------------------------Contravoucher----------------------   
                String contraList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONTRA_VOUCHER_TABLE, cond);
                if (null != contraList && !contraList.isEmpty()) {
                    List<ContraVoucher> conVouList = new Gson().fromJson(contraList, new TypeToken<List<ContraVoucher>>() {
                    }.getType());
                    if (conVouList != null) {
                        for (ContraVoucher contra : conVouList) {
                            if (null != contra.getVoucherDate() && !contra.getVoucherDate().isEmpty()) {
                                // //System.out.println(emp.getDateOfJoining());
                                Long voucherDate = convertDataToMillsec(contra.getVoucherDate());
                                int year = Integer.parseInt(fy);
                                HashMap<Long, Long> value = fyMap.get(year);
                                for (Map.Entry<Long, Long> m : value.entrySet()) {
                                    Long fyrFromdate = m.getKey();
                                    Long fyrTodate = m.getValue();
                                    if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
                                        conCount++;
                                    }

                                }
                            }
                        }
                    }
                }

                // ------------------------------------Journal Voucher------------------------   
                String journalList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.JOURNAL_VOUCHER_TABLE, cond);
                if (null != journalList && !journalList.isEmpty()) {
                    List<JournalVoucher> jouVouList = new Gson().fromJson(journalList, new TypeToken<List<JournalVoucher>>() {
                    }.getType());
                    if (jouVouList != null) {
                        for (JournalVoucher jour : jouVouList) {
                            if (null != jour.getVoucherDate() && !jour.getVoucherDate().isEmpty()) {
                                // //System.out.println(emp.getDateOfJoining());
                                Long voucherDate = convertDataToMillsec(jour.getVoucherDate());
                                int year = Integer.parseInt(fy);
                                HashMap<Long, Long> value = fyMap.get(year);
                                for (Map.Entry<Long, Long> m : value.entrySet()) {
                                    Long fyrFromdate = m.getKey();
                                    Long fyrTodate = m.getValue();
                                    if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
                                        jouCount++;
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        HashMap<String, Integer> resultList = new HashMap<String, Integer>();

        resultList.put(ApplicationConstants.RECEIPT_VOUCHER, recCount);

        resultList.put(ApplicationConstants.PAYMENT_VOUCHER, payCount);

        resultList.put(ApplicationConstants.CONTRA_VOUCHER, conCount);

        resultList.put(ApplicationConstants.JOURNAL_VOUCHER, jouCount);

        //System.out.println(new Gson().toJson(result));
        return new Gson().toJson(resultList);
    }

    ///////////////////////////////////
    public String getVoucherStatusCount(String fy, String ddo, String location) throws Exception {
        if (null == fy || fy.isEmpty()) {
            return null;
        }
        int recCount = 0;
        int recpostCount = 0;
        int payCount = 0;
        int paypostCount = 0;
        int conCount = 0;
        int conpostCount = 0;
        int jouCount = 0;
        int joupostCount = 0;
        HashMap<Integer, Long> result = new HashMap<Integer, Long>();
        HashMap<String, HashMap<String, Integer>> finalresult = new HashMap<String, HashMap<String, Integer>>();
        HashMap<Integer, HashMap<Long, Long>> fyMap = new HashMap<Integer, HashMap<Long, Long>>();
        // get all available FY from fy collection
        String strFy = DBManager.getDbConnection().fetchAll(ApplicationConstants.FINANCIAL_YEAR_TABLE);
        if (null != strFy && !strFy.isEmpty()) {
            List<FinancialAccounting_FinancialYear> financialYear = new Gson().fromJson(strFy, new TypeToken<List<FinancialAccounting_FinancialYear>>() {
            }.getType());
            for (FinancialAccounting_FinancialYear fYear : financialYear) {
                HashMap<Long, Long> map = new HashMap<Long, Long>();
                map.put(convertDataToMillsec(fYear.getFromDate()), convertDataToMillsec(fYear.getToDate()));
                fyMap.put(Integer.parseInt(fYear.getYear()), map);
                result.put(Integer.parseInt(fYear.getYear()), 0l);
            }

//            HashMap<String, String> cond = new HashMap<String, String>();
//            cond.put("ddoName", "Gujarat");
//            String strDdoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, cond);
            if (null != ddo && !ddo.isEmpty()) {
//                List<DDO> ddoList = new Gson().fromJson(strDdoJson, new TypeToken<List<DDO>>() {
//                }.getType());
                HashMap<String, String> cond = new HashMap<String, String>();
                cond.put("DDO", ddo);
                cond.put("location", location);
                cond.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                // get all employee collection
                HashMap<String, Integer> recvounewList = new HashMap<String, Integer>();
                String reciptList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.RECEIPT_VOUCHER_TABLE, cond);
                if (null != reciptList && !reciptList.isEmpty()) {
                    List<ReceiptVoucher> employee = new Gson().fromJson(reciptList, new TypeToken<List<ReceiptVoucher>>() {
                    }.getType());
                    for (ReceiptVoucher emp : employee) {
                        if (null != emp.getVoucherDate() && !emp.getVoucherDate().isEmpty()) {
                            // //System.out.println(emp.getDateOfJoining());
                            Long voucherDate = convertDataToMillsec(emp.getVoucherDate());
                            int year = Integer.parseInt(fy);
                            HashMap<Long, Long> value = fyMap.get(year);
                            for (Map.Entry<Long, Long> m : value.entrySet()) {
                                Long fyrFromdate = m.getKey();
                                Long fyrTodate = m.getValue();
                                if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
                                    if (emp.getPostingStatus().equalsIgnoreCase("Posted")) {
                                        recpostCount++;
                                    } else {
                                        recCount++;
                                    }
                                }

                            }
                        }
                    }
                }
                recvounewList.put("new", recCount);
                recvounewList.put("posted", recpostCount);
                finalresult.put(ApplicationConstants.RECEIPT_VOUCHER, recvounewList);
                //       -------------Payment Voucher-----------------  

                HashMap<String, Integer> payvounewList = new HashMap<String, Integer>();
                String paymentList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PAYMENT_VOUCHER_TABLE, cond);
                if (null != paymentList && !paymentList.isEmpty()) {
                    List<PaymentVoucher> payVouList = new Gson().fromJson(paymentList, new TypeToken<List<PaymentVoucher>>() {
                    }.getType());
                    for (PaymentVoucher paymentVouc : payVouList) {
                        if (null != paymentVouc.getVoucherDate() && !paymentVouc.getVoucherDate().isEmpty()) {
                            // //System.out.println(emp.getDateOfJoining());
                            Long voucherDate = convertDataToMillsec(paymentVouc.getVoucherDate());
                            int year = Integer.parseInt(fy);
                            HashMap<Long, Long> value = fyMap.get(year);
                            for (Map.Entry<Long, Long> m : value.entrySet()) {
                                Long fyrFromdate = m.getKey();
                                Long fyrTodate = m.getValue();
                                if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
                                    if (paymentVouc.getPostingStatus().equalsIgnoreCase("Posted")) {
                                        paypostCount++;
                                    } else {
                                        payCount++;
                                    }
                                }

                            }
                        }
                    }
                }
                payvounewList.put("new", payCount);
                payvounewList.put("posted", paypostCount);
                finalresult.put(ApplicationConstants.PAYMENT_VOUCHER, payvounewList);

                // ----------------------------Contravoucher----------------------   
                HashMap<String, Integer> convounewList = new HashMap<String, Integer>();
                String contraList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONTRA_VOUCHER_TABLE, cond);
                if (null != contraList && !contraList.isEmpty()) {
                    List<ContraVoucher> conVouList = new Gson().fromJson(contraList, new TypeToken<List<ContraVoucher>>() {
                    }.getType());
                    if (conVouList != null) {
                        for (ContraVoucher contra : conVouList) {
                            if (null != contra.getVoucherDate() && !contra.getVoucherDate().isEmpty()) {
                                // //System.out.println(emp.getDateOfJoining());
                                Long voucherDate = convertDataToMillsec(contra.getVoucherDate());
                                int year = Integer.parseInt(fy);
                                HashMap<Long, Long> value = fyMap.get(year);
                                for (Map.Entry<Long, Long> m : value.entrySet()) {
                                    Long fyrFromdate = m.getKey();
                                    Long fyrTodate = m.getValue();
                                    if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
                                        if (contra.getPostingStatus().equalsIgnoreCase("Posted")) {
                                            conpostCount++;
                                        } else {
                                            conCount++;
                                        }
                                    }

                                }
                            }
                        }
                    }
                }

                convounewList.put("new", conCount);
                convounewList.put("posted", conpostCount);
                finalresult.put(ApplicationConstants.CONTRA_VOUCHER, convounewList);
                // ------------------------------------Journal Voucher------------------------   
                HashMap<String, Integer> jourvounewList = new HashMap<String, Integer>();
                String journalList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.JOURNAL_VOUCHER_TABLE, cond);
                if (null != journalList && !journalList.isEmpty()) {
                    List<JournalVoucher> jouVouList = new Gson().fromJson(journalList, new TypeToken<List<JournalVoucher>>() {
                    }.getType());
                    if (jouVouList != null) {
                        for (JournalVoucher jour : jouVouList) {
                            if (null != jour.getVoucherDate() && !jour.getVoucherDate().isEmpty()) {
                                // //System.out.println(emp.getDateOfJoining());
                                Long voucherDate = convertDataToMillsec(jour.getVoucherDate());
                                int year = Integer.parseInt(fy);
                                HashMap<Long, Long> value = fyMap.get(year);
                                for (Map.Entry<Long, Long> m : value.entrySet()) {
                                    Long fyrFromdate = m.getKey();
                                    Long fyrTodate = m.getValue();
                                    if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
                                        if (jour.getPostingStatus().equalsIgnoreCase("Posted")) {
                                            joupostCount++;
                                        } else {
                                            jouCount++;
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                jourvounewList.put("new", jouCount);
                jourvounewList.put("posted", joupostCount);
                finalresult.put(ApplicationConstants.JOURNAL_VOUCHER, jourvounewList);
            }
        }
//        HashMap<String, Integer> resultList = new HashMap<String, Integer>();
//
//        resultList.put("posted", recpostCount);
//
//        resultList.put(ApplicationConstants.PAYMENT_VOUCHER, payCount);
//
//        resultList.put(ApplicationConstants.CONTRA_VOUCHER, conCount);
//
//        resultList.put(ApplicationConstants.JOURNAL_VOUCHER, jouCount);
//        finalresult.put(ApplicationConstants.RECEIPT_VOUCHER, resultList.put("new", recCount));
//        //System.out.println(
//                new Gson().toJson(result));
        return new Gson()
                .toJson(finalresult);
    }

    //////////////////////
    public String getChequeStatus(String fy, String ddo, String location) throws Exception {
        if (null == fy || fy.isEmpty()) {
            return null;
        }

        int payCount = 0;
        int paypostCount = 0;
        int conCount = 0;
        int conpostCount = 0;
        int finalCount = 0;
        int finalpostCount = 0;

        HashMap<Integer, Long> result = new HashMap<Integer, Long>();
        HashMap<String, HashMap<String, Integer>> finalresult = new HashMap<String, HashMap<String, Integer>>();
        HashMap<Integer, HashMap<Long, Long>> fyMap = new HashMap<Integer, HashMap<Long, Long>>();
        // get all available FY from fy collection
        String strFy = DBManager.getDbConnection().fetchAll(ApplicationConstants.FINANCIAL_YEAR_TABLE);
        if (null != strFy && !strFy.isEmpty()) {
            List<FinancialAccounting_FinancialYear> financialYear = new Gson().fromJson(strFy, new TypeToken<List<FinancialAccounting_FinancialYear>>() {
            }.getType());
            for (FinancialAccounting_FinancialYear fYear : financialYear) {
                HashMap<Long, Long> map = new HashMap<Long, Long>();
                map.put(convertDataToMillsec(fYear.getFromDate()), convertDataToMillsec(fYear.getToDate()));
                fyMap.put(Integer.parseInt(fYear.getYear()), map);
                result.put(Integer.parseInt(fYear.getYear()), 0l);
            }

//            HashMap<String, String> cond = new HashMap<String, String>();
//            cond.put("ddoName", "Gujarat");
//            String strDdoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, cond);
            if (null != ddo && !ddo.isEmpty()) {
//                List<DDO> ddoList = new Gson().fromJson(strDdoJson, new TypeToken<List<DDO>>() {
//                }.getType());
                HashMap<String, String> cond = new HashMap<String, String>();
                cond.put("DDO", ddo);
                cond.put("location", location);
                cond.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

                //       -------------Payment Voucher-----------------  
                String paymentList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PAYMENT_VOUCHER_TABLE, cond);
                if (null != paymentList && !paymentList.isEmpty()) {
                    List<PaymentVoucher> payVouList = new Gson().fromJson(paymentList, new TypeToken<List<PaymentVoucher>>() {
                    }.getType());
                    for (PaymentVoucher paymentVouc : payVouList) {
                        if (null != paymentVouc.getVoucherDate() && !paymentVouc.getVoucherDate().isEmpty()) {
                            // //System.out.println(emp.getDateOfJoining());
                            Long voucherDate = convertDataToMillsec(paymentVouc.getVoucherDate());
                            int year = Integer.parseInt(fy);
                            HashMap<Long, Long> value = fyMap.get(year);
                            for (Map.Entry<Long, Long> m : value.entrySet()) {
                                Long fyrFromdate = m.getKey();
                                Long fyrTodate = m.getValue();
                                if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
                                    if (paymentVouc.getPostingStatus().equalsIgnoreCase("Posted")) {
                                        paypostCount++;
                                    } else {
                                        payCount++;
                                    }
                                }

                            }
                        }
                    }
                }

                // ----------------------------Contravoucher----------------------   
                HashMap<String, Integer> convounewList = new HashMap<String, Integer>();
                String contraList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONTRA_VOUCHER_TABLE, cond);
                if (null != contraList && !contraList.isEmpty()) {
                    List<ContraVoucher> conVouList = new Gson().fromJson(contraList, new TypeToken<List<ContraVoucher>>() {
                    }.getType());
                    if (conVouList != null) {
                        for (ContraVoucher contra : conVouList) {
                            if (null != contra.getVoucherDate() && !contra.getVoucherDate().isEmpty()) {
                                // //System.out.println(emp.getDateOfJoining());
                                Long voucherDate = convertDataToMillsec(contra.getVoucherDate());
                                int year = Integer.parseInt(fy);
                                HashMap<Long, Long> value = fyMap.get(year);
                                for (Map.Entry<Long, Long> m : value.entrySet()) {
                                    Long fyrFromdate = m.getKey();
                                    Long fyrTodate = m.getValue();
                                    if (fyrFromdate <= voucherDate && voucherDate <= fyrTodate) {
//                                        
                                        if (contra.getPaymentMode().equalsIgnoreCase("Cheque")) {
                                            for (int i = 0; i < contra.getLedgerList().size(); i++) {
                                                if (contra.getLedgerList().get(i).getDrCr().equalsIgnoreCase("Cr") && contra.getLedgerList().get(i).getGroupName().equalsIgnoreCase("Bank Group")) {
                                                    if (contra.getPostingStatus().equalsIgnoreCase("Posted")) {
                                                        conpostCount++;
                                                    } else {
                                                        conCount++;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                HashMap<String, Integer> finalList = new HashMap<String, Integer>();
                finalCount = conCount + payCount;
                finalpostCount = conpostCount + paypostCount;
                finalList.put("new", finalCount);
                finalList.put("posted", finalpostCount);
                finalresult.put(ApplicationConstants.CHEQUE_STATUS, finalList);

            }
        }

        return new Gson().toJson(finalresult);
    }

    public static void main(String args[]) throws Exception {
        //System.out.println("$$$$$$$$$$$$$$$$"+new FinancialAnalyticsManager().getVoucherCountByFY("2017", "582f34960c92ec57796a1e13", "582f34a70c92ec57796a1e17"));
    }
}
