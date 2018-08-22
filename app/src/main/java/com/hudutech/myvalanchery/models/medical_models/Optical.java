package com.hudutech.myvalanchery.models.medical_models;

import java.io.Serializable;

public class Optical implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String photoUrl;
    private String shopName;
    private String phoneNumber;
    private String place;
    private String address;
    private double lat;
    private double lng;
    private boolean isValidated;

    public Optical() {
    }

    public Optical(String docKey, String photoUrl, String shopName, String phoneNumber, String place, String address, double lat, double lng, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrl = photoUrl;
        this.shopName = shopName;
        this.phoneNumber = phoneNumber;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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
}
