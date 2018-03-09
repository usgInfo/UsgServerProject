/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.dto;

import com.accure.usg.common.dto.Common;

/**
 * @author ankur
 */
public class ApplicationForm extends Common {
    
    private String candidateName;
    private String motherName;
    private String fatherName;
    private String dob;
    private Long dobInMilliSecond;
    private String programmeName;
    private String collegeName;
    private String formNumber;
    private String phoneNumber;
    private String gender;
//    private String category;
    private String specialClaim;
    private String emailAddress;
    private String nationality;
    private String address;
    //For Class 10th details
    private String board_X;
    private String passingYear_X;
    private Integer marksX;
    private Integer marksOutOfX;
    //For Class 12th details
    private String board_XII;
    private String passingYear_XII;
    private Integer marksXII;
    private Integer marksOutOfXII; 
    //For Graduation Details
    private String nameOfDegree_grad;
    private String specialization_grad;
    private String duration_grad;
    private String medium_grad;
    private String percentage_grad;
    private String mode_grad;
    private String nameOfUniversity_grad;
    private String nameOfCollege_grad;
    private String univ_rollNum_grad;
    private String city_grad;
    //For Post-Graduation Details
    private String nameOfDegree_pg;
    private String specialization_pg;
    private String duration_pg;
    private String medium_pg;
    private String percentage_pg;
    private String mode_pg;
    private String nameOfUniversity_pg;
    private String nameOfCollege_pg;
    private String univ_rollNum_pg;
    private String city_pg;

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }

    public String getSpecialClaim() {
        return specialClaim;
    }

    public void setSpecialClaim(String specialClaim) {
        this.specialClaim = specialClaim;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBoard_X() {
        return board_X;
    }

    public void setBoard_X(String board_X) {
        this.board_X = board_X;
    }

    public String getPassingYear_X() {
        return passingYear_X;
    }

    public void setPassingYear_X(String passingYear_X) {
        this.passingYear_X = passingYear_X;
    }

    public String getBoard_XII() {
        return board_XII;
    }

    public void setBoard_XII(String board_XII) {
        this.board_XII = board_XII;
    }

    public String getPassingYear_XII() {
        return passingYear_XII;
    }

    public void setPassingYear_XII(String passingYear_XII) {
        this.passingYear_XII = passingYear_XII;
    }

    public String getNameOfDegree_grad() {
        return nameOfDegree_grad;
    }

    public void setNameOfDegree_grad(String nameOfDegree_grad) {
        this.nameOfDegree_grad = nameOfDegree_grad;
    }

    public String getSpecialization_grad() {
        return specialization_grad;
    }

    public void setSpecialization_grad(String specialization_grad) {
        this.specialization_grad = specialization_grad;
    }

    public String getDuration_grad() {
        return duration_grad;
    }

    public void setDuration_grad(String duration_grad) {
        this.duration_grad = duration_grad;
    }

    public String getMedium_grad() {
        return medium_grad;
    }

    public void setMedium_grad(String medium_grad) {
        this.medium_grad = medium_grad;
    }

    public String getPercentage_grad() {
        return percentage_grad;
    }

    public void setPercentage_grad(String percentage_grad) {
        this.percentage_grad = percentage_grad;
    }

    public String getMode_grad() {
        return mode_grad;
    }

    public void setMode_grad(String mode_grad) {
        this.mode_grad = mode_grad;
    }

    public String getNameOfUniversity_grad() {
        return nameOfUniversity_grad;
    }

    public void setNameOfUniversity_grad(String nameOfUniversity_grad) {
        this.nameOfUniversity_grad = nameOfUniversity_grad;
    }

    public String getNameOfCollege_grad() {
        return nameOfCollege_grad;
    }

    public void setNameOfCollege_grad(String nameOfCollege_grad) {
        this.nameOfCollege_grad = nameOfCollege_grad;
    }

    public String getUniv_rollNum_grad() {
        return univ_rollNum_grad;
    }

    public void setUniv_rollNum_grad(String univ_rollNum_grad) {
        this.univ_rollNum_grad = univ_rollNum_grad;
    }

    public String getCity_grad() {
        return city_grad;
    }

    public void setCity_grad(String city_grad) {
        this.city_grad = city_grad;
    }

    public String getNameOfDegree_pg() {
        return nameOfDegree_pg;
    }

    public void setNameOfDegree_pg(String nameOfDegree_pg) {
        this.nameOfDegree_pg = nameOfDegree_pg;
    }

    public String getSpecialization_pg() {
        return specialization_pg;
    }

    public void setSpecialization_pg(String specialization_pg) {
        this.specialization_pg = specialization_pg;
    }

    public String getDuration_pg() {
        return duration_pg;
    }

    public void setDuration_pg(String duration_pg) {
        this.duration_pg = duration_pg;
    }

    public String getMedium_pg() {
        return medium_pg;
    }

    public void setMedium_pg(String medium_pg) {
        this.medium_pg = medium_pg;
    }

    public String getPercentage_pg() {
        return percentage_pg;
    }

    public void setPercentage_pg(String percentage_pg) {
        this.percentage_pg = percentage_pg;
    }

    public String getMode_pg() {
        return mode_pg;
    }

    public void setMode_pg(String mode_pg) {
        this.mode_pg = mode_pg;
    }

    public String getNameOfUniversity_pg() {
        return nameOfUniversity_pg;
    }

    public void setNameOfUniversity_pg(String nameOfUniversity_pg) {
        this.nameOfUniversity_pg = nameOfUniversity_pg;
    }

    public String getNameOfCollege_pg() {
        return nameOfCollege_pg;
    }

    public void setNameOfCollege_pg(String nameOfCollege_pg) {
        this.nameOfCollege_pg = nameOfCollege_pg;
    }

    public String getUniv_rollNum_pg() {
        return univ_rollNum_pg;
    }

    public void setUniv_rollNum_pg(String univ_rollNum_pg) {
        this.univ_rollNum_pg = univ_rollNum_pg;
    }

    public String getCity_pg() {
        return city_pg;
    }

    public void setCity_pg(String city_pg) {
        this.city_pg = city_pg;
    }

    public Long getDobInMilliSecond() {
        return dobInMilliSecond;
    }

    public void setDobInMilliSecond(Long dobInMilliSecond) {
        this.dobInMilliSecond = dobInMilliSecond;
    }

    public Integer getMarksX() {
        return marksX;
    }

    public void setMarksX(Integer marksX) {
        this.marksX = marksX;
    }

    public Integer getMarksOutOfX() {
        return marksOutOfX;
    }

    public void setMarksOutOfX(Integer marksOutOfX) {
        this.marksOutOfX = marksOutOfX;
    }

    public Integer getMarksXII() {
        return marksXII;
    }

    public void setMarksXII(Integer marksXII) {
        this.marksXII = marksXII;
    }

    public Integer getMarksOutOfXII() {
        return marksOutOfXII;
    }

    public void setMarksOutOfXII(Integer marksOutOfXII) {
        this.marksOutOfXII = marksOutOfXII;
    }
    
    
    
}
