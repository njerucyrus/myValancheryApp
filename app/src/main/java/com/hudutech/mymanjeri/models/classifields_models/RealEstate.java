package com.hudutech.mymanjeri.models.classifields_models;

import java.util.List;

public class RealEstate implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private List<String> photoUrls;
    private double amount;
    private boolean hasWater;
    private boolean hasElectricity;
    private String phoneNumber;
    private String location;
    private String heading;
    private String description;
    private boolean isValidated;

    public RealEstate() {
    }

    public RealEstate(String docKey, List<String> photoUrls, double amount, boolean hasWater, boolean hasElectricity, String phoneNumber, String location, String heading, String description, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrls = photoUrls;
        this.amount = amount;
        this.hasWater = hasWater;
        this.hasElectricity = hasElectricity;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.heading = heading;
        this.description = description;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isHasWater() {
        return hasWater;
    }

    public void setHasWater(boolean hasWater) {
        this.hasWater = hasWater;
    }

    public boolean isHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }
}
