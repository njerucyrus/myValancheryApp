package com.hudutech.mymanjeri.models.contact_models;

public class Professional implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String name;
    private String phoneNumber;
    private String place;
    private String category;
    private String photoUrl;
    private boolean isValidated;


    public Professional() {
    }

    public Professional(String docKey, String name, String phoneNumber, String place, String category, String photoUrl, boolean isValidated) {
        this.docKey = docKey;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.place = place;
        this.category = category;
        this.photoUrl = photoUrl;
        this.isValidated = isValidated;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Professional{" +
                "docKey='" + docKey + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", place='" + place + '\'' +
                ", category='" + category + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
