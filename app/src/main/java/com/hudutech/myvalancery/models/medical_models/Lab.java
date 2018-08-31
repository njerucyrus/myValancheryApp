package com.hudutech.myvalancery.models.medical_models;

import java.io.Serializable;

public class Lab implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String photoUrl;
    private String labName;
    private String contactNo;
    private String place;
    private String address;
    private double lat;
    private double lng;
    private boolean isValidated;

    public Lab() {

    }

    public Lab(String docKey, String photoUrl, String labName, String contactNo, String place, String address, double lat, double lng, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrl = photoUrl;
        this.labName = labName;
        this.contactNo = contactNo;
        this.place = place;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
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

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Lab{" +
                "docKey='" + docKey + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", labName='" + labName + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", place='" + place + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", isValidated=" + isValidated +
                '}';
    }
}
