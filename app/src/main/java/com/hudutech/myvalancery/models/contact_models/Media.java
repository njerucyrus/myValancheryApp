package com.hudutech.myvalancery.models.contact_models;

import java.io.Serializable;

public class Media implements Serializable {
    private static final long serialVersionUID = 1L;
    private String logoUrl;
    private String docKey;
    private String name;
    private String phoneNumber;
    private String mediaName;
    private String mediaType;
    private boolean isValidated;

    public Media() {

    }

    public Media(String logoUrl, String docKey, String name, String phoneNumber, String mediaName, String mediaType, boolean isValidated) {
        this.logoUrl = logoUrl;
        this.docKey = docKey;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mediaName = mediaName;
        this.mediaType = mediaType;
        this.isValidated = isValidated;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Media{" +
                "logoUrl='" + logoUrl + '\'' +
                ", docKey='" + docKey + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", mediaName='" + mediaName + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
