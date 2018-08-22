package com.hudutech.myvalanchery.models.majery_models;

import java.util.List;

public class Shop implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String place;
    private List<String> photoUrls;
    private double rating;
    private String shopName;
    private String shopContact;
    private String shopLocation;
    private String shopType;
    private String docKey;
    private double lat;
    private double lng;
    private boolean isValidated;

    public Shop() {
    }

    public Shop(String place, List<String> photoUrls, double rating, String shopName, String shopContact, String shopLocation, String shopType, String docKey, double lat, double lng, boolean isValidated) {
        this.place = place;
        this.photoUrls = photoUrls;
        this.rating = rating;
        this.shopName = shopName;
        this.shopContact = shopContact;
        this.shopLocation = shopLocation;
        this.shopType = shopType;
        this.docKey = docKey;
        this.lat = lat;
        this.lng = lng;
        this.isValidated = isValidated;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopContact() {
        return shopContact;
    }

    public void setShopContact(String shopContact) {
        this.shopContact = shopContact;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
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
        return "Shop{" +
                "shopName='" + shopName + '\'' +
                ", shopContact='" + shopContact + '\'' +
                ", shopLocation='" + shopLocation + '\'' +
                ", shopType='" + shopType + '\'' +
                ", docKey='" + docKey + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
