/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.budget.dto.FundType;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.DesignationFundTypeMapping;
import com.accure.hrms.dto.Discipline;
import com.accure.hrms.dto.Grade;
import com.accure.hrms.dto.Nature;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class DesignationFundTypeMappingManager {

    public String save(DesignationFundTypeMapping dft, String userid) throws Exception {

        String result;
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        hMap.put("ddo", dft.getDdo());
        hMap.put("designation", dft.getDesignation());
        hMap.put("grade", dft.getGrade());
        hMap.put("fundType", dft.getFundType());
        hMap.put("budgetHead", dft.getBudgetHead());
        hMap.put("natureType", dft.getNatureType());
        hMap.put("desciplineName", dft.getDesciplineName());

        if (userid == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, hMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userid);
            String userName = user.getFname() + user.getLname();

            dft.setCreateDate(System.currentTimeMillis() + "");
            dft.setUpdateDate(System.currentTimeMillis() + "");
            dft.setStatus(ApplicationConstants.ACTIVE);
            dft.setUpdatedBy(userName);
            dft.setCreatedBy(userName);
            String dftJson = new Gson().toJson(dft);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, dftJson);
            if (fResult != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    public String fetch() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);

        List<DesignationFundTypeMapping> DFTMList = new Gson().fromJson(result, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        for (DesignationFundTypeMapping li : DFTMList) {
            if ((li.getDdo() != null)) {

                String ddoStr = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, li.getDdo());
                if (ddoStr != null) {
                    List<DDO> list = new Gson().fromJson(ddoStr, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO ddo = list.get(0);
                    li.setDdo(ddo.getDdoName());
                }

            }
            if ((li.getDesignation() != null)) {

                String degnStr = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, li.getDesignation());
                if (degnStr != null) {
                    List<Designation> degnList = new Gson().fromJson(degnStr, new TypeToken<List<Designation>>() {
                    }.getType());
                    Designation degn = degnList.get(0);
                    li.setDesignation(degn.getDesignation());
                }

            }
            if (li.getGrade() != null) {

                String grdStr = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE, li.getGrade());
                if (grdStr != null) {
                    List<Grade> grdList = new Gson().fromJson(grdStr, new TypeToken<List<Grade>>() {
                    }.getType());
                    Grade grd = grdList.get(0);
                    li.setGrade(grd.getGradeName());
                }

            }

            if (li.getFundType() != null) {

                String fTStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, li.getFundType());
                if (fTStr != null) {
                    List<FundType> fTList = new Gson().fromJson(fTStr, new TypeToken<List<FundType>>() {
                    }.getType());
                    FundType ft = fTList.get(0);
                    li.setFundType(ft.getDescription());
                }

            }
            if (li.getBudgetHead() != null) {

                String bhStr = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, li.getBudgetHead());
                if (bhStr != null) {
                    List<BudgetHeadMaster> bhList = new Gson().fromJson(bhStr, new TypeToken<List<BudgetHeadMaster>>() {
                    }.getType());
                    BudgetHeadMaster bh = bhList.get(0);
                    li.setBudgetHead(bh.getBudgetHead());
                }

            }
            if (li.getNatureType() != null) {

                String ntStr = DBManager.getDbConnection().fetch(ApplicationConstants.NATURE_TABLE, li.getNatureType());
                if (ntStr != null) {
                    List<Nature> ntList = new Gson().fromJson(ntStr, new TypeToken<List<Nature>>() {
                    }.getType());
                    Nature nt = ntList.get(0);
                    li.setNatureType(nt.getNatureName());
                }

            }
            if (li.getDesciplineName() != null) {

                String discStr = DBManager.getDbConnection().fetch(ApplicationConstants.DISCIPLINE_TABLE, li.getDesciplineName());
                if (discStr != null) {
                    List<Discipline> descList = new Gson().fromJson(discStr, new TypeToken<List<Discipline>>() {
                    }.getType());
                    Discipline disc = descList.get(0);
                    li.setDesciplineName(disc.getDisciplineName());
                }

            }
        }
        return new Gson().toJson(DFTMList);
    }

    public String fetchAll(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);

        List<DesignationFundTypeMapping> DFTMList = new Gson().fromJson(result, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        for (DesignationFundTypeMapping li : DFTMList) {
            if ((li.getDdo() != null)) {
                try {
                    String ddoStr = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, li.getDdo());
                    if (ddoStr != null) {
                        List<DDO> ddoList = new Gson().fromJson(ddoStr, new TypeToken<List<DDO>>() {
                        }.getType());
                        DDO Obj = ddoList.get(0);
                        li.setDdo(Obj.getDdoName());
                    }
                } catch (Exception e) {
                }
            }
            if ((li.getDesignation() != null)) {
                try {
                    String desgStr = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, li.getDesignation());
                    if (desgStr != null) {
                        List<Designation> desgList = new Gson().fromJson(desgStr, new TypeToken<List<Designation>>() {
                        }.getType());
                        Designation desg = desgList.get(0);
                        li.setDesignation(desg.getDesignation());
                    }
                } catch (Exception e) {
                }
            }
            if (li.getGrade() != null) {
                try {
                    String gradeStr = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE, li.getGrade());
                    if (gradeStr != null) {
                        List<Grade> gradeList = new Gson().fromJson(gradeStr, new TypeToken<List<Grade>>() {
                        }.getType());
                        Grade grade = gradeList.get(0);
                        li.setGrade(grade.getGradeName());
                    }
                } catch (Exception e) {

                }
            }

            if (li.getFundType() != null) {
                try {
                    String ftStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, li.getFundType());
                    if (ftStr != null) {
                        List<FundType> ftList = new Gson().fromJson(ftStr, new TypeToken<List<FundType>>() {
                        }.getType());
                        FundType ft = ftList.get(0);
                        li.setFundType(ft.getDescription());
                    }
                } catch (Exception e) {
                }
            }
            if (li.getBudgetHead() != null) {
                try {
                    String bhStr = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, li.getBudgetHead());
                    if (bhStr != null) {
                        List<BudgetHeadMaster> bhList = new Gson().fromJson(bhStr, new TypeToken<List<BudgetHeadMaster>>() {
                        }.getType());
                        BudgetHeadMaster bh = bhList.get(0);
                        li.setBudgetHead(bh.getBudgetHead());
                    }
                } catch (Exception e) {
                }
            }
            if (li.getNatureType() != null) {
                try {
                    String ntStr = DBManager.getDbConnection().fetch(ApplicationConstants.NATURE_TABLE, li.getNatureType());
                    if (ntStr != null) {
                        List<Nature> ntList = new Gson().fromJson(ntStr, new TypeToken<List<Nature>>() {
                        }.getType());
                        Nature nt = ntList.get(0);
                        li.setNatureType(nt.getNatureName());
                    }
                } catch (Exception e) {
                }
            }
            if (li.getDesciplineName() != null) {
                try {
                    String discStr = DBManager.getDbConnection().fetch(ApplicationConstants.DISCIPLINE_TABLE, li.getDesciplineName());
                    if (discStr != null) {
                        List<Discipline> discList = new Gson().fromJson(discStr, new TypeToken<List<Discipline>>() {
                        }.getType());
                        Discipline disc = discList.get(0);
                        li.setDesciplineName(disc.getDisciplineName());
                    }
                } catch (Exception e) {
                }
            }
        }
        return new Gson().toJson(DFTMList);
    }

    public boolean delete(String id) throws Exception {

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, id);
        List<DesignationFundTypeMapping> dbList = new Gson().fromJson(dbStr, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        DesignationFundTypeMapping dbObj = dbList.get(0);
        DesignationFundTypeMapping ftUpdate = new DesignationFundTypeMapping();

        ftUpdate.setDdo(dbObj.getDdo());
        ftUpdate.setBudgetHead(dbObj.getBudgetHead());
        ftUpdate.setDesignation(dbObj.getDesignation());
        ftUpdate.setDesciplineName(dbObj.getDesciplineName());
        ftUpdate.setFundType(dbObj.getFundType());
        ftUpdate.setNatureType(dbObj.getNatureType());
        ftUpdate.setGrade(dbObj.getGrade());
        ftUpdate.setCreateDate(dbObj.getCreateDate());
        ftUpdate.setUpdateDate(System.currentTimeMillis() + "");
        ftUpdate.setStatus(ApplicationConstants.DELETE);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, id, new Gson().toJson(ftUpdate));

        return result;
    }

    public String Update(DesignationFundTypeMapping dft, String id, String userid) throws Exception {

        String result;
        HashMap<String, String> saveconditionMap = new HashMap<String, String>();
        saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        saveconditionMap.put("ddo", dft.getDdo());
        saveconditionMap.put("designation", dft.getDesignation());
        saveconditionMap.put("grade", dft.getGrade());
        saveconditionMap.put("fundType", dft.getFundType());
        saveconditionMap.put("budgetHead", dft.getBudgetHead());
        saveconditionMap.put("natureType", dft.getNatureType());
        saveconditionMap.put("desciplineName", dft.getDesciplineName());

        if (userid == null) {
            result = null;
        } else if (isDuplicateforUpdate(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, saveconditionMap, id)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userid);
            String userName = user.getFname() + user.getLname();
            String ftype = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, id);
            List<DesignationFundTypeMapping> dftmList = new Gson().fromJson(ftype, new TypeToken<List<DesignationFundTypeMapping>>() {
            }.getType());
            DesignationFundTypeMapping dftm = dftmList.get(0);
            dft.setCreateDate(dftm.getCreateDate());
            dft.setCreatedBy(dftm.getCreatedBy());
            dft.setUpdateDate(System.currentTimeMillis() + "");
            dft.setStatus(ApplicationConstants.ACTIVE);
            dft.setUpdatedBy(userName);
            String dftJson = new Gson().toJson(dft);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, id, dftJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }

        return result;

    }

    public String search(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);

        List<DesignationFundTypeMapping> desigFundHeadTypelist = new Gson().fromJson(result, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        List<Designation> designationList = new ArrayList<Designation>();
        List<FundType> fundtypelist = new ArrayList<FundType>();
        for (DesignationFundTypeMapping dfm : desigFundHeadTypelist) {
            try {
                String desgStr = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION, dfm.getDesignation());
                List<Designation> desgList = new Gson().fromJson(desgStr, new TypeToken<List<Designation>>() {
                }.getType());
                designationList.add(desgList.get(0));

            } catch (Exception e) {
            }
            try {
                String fnStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_NATURE_TABLE, dfm.getFundType());
                if (fnStr != null) {
                    List<FundType> fnList = new Gson().fromJson(fnStr, new TypeToken<List<FundType>>() {
                    }.getType());
                    fundtypelist.add(fnList.get(0));
                }
            } catch (Exception e) {
            }
        }

        HashMap<String, String> resultList = new HashMap<String, String>();
        resultList.put("designationList", new Gson().toJson(designationList));
        resultList.put("fundTypeList", new Gson().toJson(fundtypelist));
        return new Gson().toJson(resultList);
    }

    public String fetchGrade(String designation) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("designation", designation);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_TABLE, conditionMap);

        if (result == null || result.isEmpty()) {
            return null;
        }
        List<Designation> desiglist = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());

        desiglist = getGrade(desiglist);

        return new Gson().toJson(desiglist);
    }

    private List<Designation> getGrade(List<Designation> designationList) throws Exception {
        Map<String, String> GradeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.GRADE_TABLE);
        List<Grade> religionList = new Gson().fromJson(result, new TypeToken<List<Grade>>() {
        }.getType());
        for (Iterator<Grade> iterator = religionList.iterator(); iterator.hasNext();) {
            Grade next = iterator.next();
            GradeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getGradeName());
        }
        for (int i = 0; i < designationList.size(); i++) {
            for (Map.Entry<String, String> entry : GradeMap.entrySet()) {
                if (entry.getKey().equals(designationList.get(i).getGrade())) {
                    designationList.get(i).setGrade(entry.getKey());
                    designationList.get(i).setGradeName(entry.getValue());

                }
            }
        }
        return designationList;
    }

}
