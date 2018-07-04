package com.hudutech.mymanjeri.models.contact_models;

public class Vehicle implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String name;
    private String place;
    private String phoneNumber;
    private String vehicleType;
    private String photoUrl;
    private boolean isValidated;


    public Vehicle() {
    }

    public Vehicle(String docKey, String name, String place, String phoneNumber, String vehicleType, String photoUrl, boolean isValidated) {
        this.docKey = docKey;
        this.name = name;
        this.place = place;
        this.phoneNumber = phoneNumber;
        this.vehicleType = vehicleType;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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
        return "Vehicle{" +
                "docKey='" + docKey + '\'' +
                ", name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
