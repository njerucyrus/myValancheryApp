package com.hudutech.myvalanchery.models.contact_models;

import java.io.Serializable;

public class ArtAndCulture implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String photoUrl;
    private String name;
    private String place;
    private String phoneNumber;
    private boolean isValidated;

    public ArtAndCulture() {

    }

    public ArtAndCulture(String docKey, String photoUrl, String name, String place, String phoneNumber, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrl = photoUrl;
        this.name = name;
        this.place = place;
        this.phoneNumber = phoneNumber;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "ArtAndCulture{" +
                "docKey='" + docKey + '\'' +
                ", name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
