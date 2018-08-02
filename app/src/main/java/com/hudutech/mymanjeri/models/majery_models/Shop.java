package com.hudutech.mymanjeri.models.majery_models;

public class Shop implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
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

    public Shop(String shopName, String shopContact, String shopLocation, String shopType, String docKey, double lat, double lng, boolean isValidated) {
        this.shopName = shopName;
        this.shopContact = shopContact;
        this.shopLocation = shopLocation;
        this.shopType = shopType;
        this.docKey = docKey;
        this.lat = lat;
        this.lng = lng;
        this.isValidated = isValidated;
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
