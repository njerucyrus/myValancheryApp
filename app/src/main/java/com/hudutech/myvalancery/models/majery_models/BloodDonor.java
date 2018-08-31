package com.hudutech.myvalancery.models.majery_models;

public class BloodDonor implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String fullName;
    private String phoneNumber;
    private String bloodGroup;
    private String otherDetails;
    private String address;
    private String avator;
    private boolean isValidated;


    public BloodDonor() {
    }

    public BloodDonor(String docKey, String fullName, String phoneNumber, String bloodGroup, String otherDetails, String address, String avator, boolean isValidated) {
        this.docKey = docKey;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.bloodGroup = bloodGroup;
        this.otherDetails = otherDetails;
        this.address = address;
        this.avator = avator;
        this.isValidated = isValidated;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
