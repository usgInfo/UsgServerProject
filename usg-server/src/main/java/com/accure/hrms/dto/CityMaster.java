/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class CityMaster extends Common{
    String categoryCity;
    String cityName;
    String addressOne;
    String addressTwo;
    String isMetro;
    String isOther;
    String categoryCityName;

    public String getCategoryCityName() {
        return categoryCityName;
    }

    public void setCategoryCityName(String categoryCityName) {
        this.categoryCityName = categoryCityName;
    }
    

    public String getCityCategory() {
        return categoryCity;
    }

    public void setCategoryCity(String cityCategory) {
        this.categoryCity = cityCategory;
    }
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddressOne() {
        return addressOne;
    }

    public void setAddressOne(String addressOne) {
        this.addressOne = addressOne;
    }

    public String getAddressTwo() {
        return addressTwo;
    }

    public void setAddressTwo(String addressTwo) {
        this.addressTwo = addressTwo;
    }

    public String getIsMetro() {
        return isMetro;
    }

    public void setIsMetro(String isMetro) {
        this.isMetro = isMetro;
    }

    public String getIsOther() {
        return isOther;
    }

    public void setIsOther(String isOther) {
        this.isOther = isOther;
    }

}
