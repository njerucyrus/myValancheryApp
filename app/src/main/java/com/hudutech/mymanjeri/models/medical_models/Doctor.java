package com.hudutech.mymanjeri.models.medical_models;

import java.io.Serializable;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String photoUrl;
    private String doctorName;
    private String phoneNumber;
    private String department;
    private boolean isValidate;

    public Doctor() {

    }

    public Doctor(String docKey, String photoUrl, String doctorName, String phoneNumber, String department, boolean isValidate) {
        this.docKey = docKey;
        this.photoUrl = photoUrl;
        this.doctorName = doctorName;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.isValidate = isValidate;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isValidate() {
        return isValidate;
    }

    public void setValidate(boolean validate) {
        isValidate = validate;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "docKey='" + docKey + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", department='" + department + '\'' +
                ", isValidate=" + isValidate +
                '}';
    }
}
