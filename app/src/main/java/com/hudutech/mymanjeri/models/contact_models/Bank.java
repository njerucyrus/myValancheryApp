package com.hudutech.mymanjeri.models.contact_models;

import java.io.Serializable;

public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String photoUrl;
    private String bankName;
    private String phoneNumber;
    private String place;
    private boolean isValidated;

    public Bank() {

    }

    public Bank(String docKey, String photoUrl, String bankName, String phoneNumber, String place, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrl = photoUrl;
        this.bankName = bankName;
        this.phoneNumber = phoneNumber;
        this.place = place;
        this.isValidated = isValidated;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "docKey='" + docKey + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", place='" + place + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
