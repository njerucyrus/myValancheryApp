package com.hudutech.mymanjeri.models.contact_models;

import java.io.Serializable;

public class Institution implements Serializable {
    private static final long serialVersionUID = 1L;
    private String photoUrl;
    private String docKey;
    private String name;
    private String contactNo;
    private String place;
    private String category;
    private String location;
    private boolean isValidated;

    public Institution() {
    }

    public Institution(String photoUrl, String docKey, String name, String contactNo, String place, String category, String location, boolean isValidated) {
        this.photoUrl = photoUrl;
        this.docKey = docKey;
        this.name = name;
        this.contactNo = contactNo;
        this.place = place;
        this.category = category;
        this.location = location;
        this.isValidated = isValidated;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Institution{" +
                "photoUrl='" + photoUrl + '\'' +
                ", docKey='" + docKey + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", place='" + place + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
