/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;
import java.util.Date;

/**
 *
 * @author Asif
 */
public class EmployeeFileHolder extends Common {

    private Object _id;
    private String createdate;
    private String updatedate;
    private String category;
    private String pathURL;
    private String status;
    private String keyword;
    private String Description;
    private String srcfilepath;
    private Date uploadDate;
    private String idStr;   

    public Object getId() {
        return _id;
    }

    public void setId(Object _id) {
        this._id = _id;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPathURL() {
        return pathURL;
    }

    public void setPathURL(String pathURL) {
        this.pathURL = pathURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getSrcfilepath() {
        return srcfilepath;
    }

    public void setSrcfilepath(String srcfilepath) {
        this.srcfilepath = srcfilepath;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

}
