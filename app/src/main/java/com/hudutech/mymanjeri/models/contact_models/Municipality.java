package com.hudutech.mymanjeri.models.contact_models;

import java.io.Serializable;

public class Municipality implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String photoUrl;
    private String name;
    private String phoneNumber;
    private String designation;
    private String wardNo;
    private boolean isValidated;

    public Municipality() {
    }

    public Municipality(String docKey, String photoUrl, String name, String phoneNumber, String designation, String wardNo, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrl = photoUrl;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.designation = designation;
        this.wardNo = wardNo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Municipality{" +
                "docKey='" + docKey + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", name='" + name + '\'' +
                ", designation='" + designation + '\'' +
                ", wardNo='" + wardNo + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
