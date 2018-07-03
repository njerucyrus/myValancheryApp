package com.hudutech.mymanjeri.models;

import java.util.Arrays;
import java.util.List;

public class Hotel implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private List<String> photoUrls;
    private String name;
    private float starRating;
    private double lat;
    private double lng;
    private String placeName;
    private String address;
    private String phoneNumber;
    private String moreInfo;
    private boolean isValidated;

    public Hotel() {
    }

    public Hotel(String docKey, List<String> photoUrls, String name, float starRating, double lat, double lng, String placeName, String address, String phoneNumber, String moreInfo, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrls = photoUrls;
        this.name = name;
        this.starRating = starRating;
        this.lat = lat;
        this.lng = lng;
        this.placeName = placeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.moreInfo = moreInfo;
        this.isValidated = isValidated;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }
}
