package com.hudutech.myvalanchery.models.contact_models;

public class General implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String name;
    private String place;
    private String phoneNumber;
    private boolean isValidated;

    public General() {

    }

    public General(String docKey, String name, String place, String phoneNumber, boolean isValidated) {
        this.docKey = docKey;
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
        return "General{" +
                "docKey='" + docKey + '\'' +
                ", name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
