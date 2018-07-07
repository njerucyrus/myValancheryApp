package com.hudutech.mymanjeri.models.contact_models;

import java.io.Serializable;

public class Politics implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String partyPhotoUrl;
    private String name;
    private String phoneNumber;
    private String designation;
    private String place;
    private String party;
    private boolean isValidated;

    public Politics() {
    }

    public Politics(String docKey, String partyPhotoUrl, String name, String phoneNumber, String designation, String place, String party, boolean isValidated) {
        this.docKey = docKey;
        this.partyPhotoUrl = partyPhotoUrl;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.designation = designation;
        this.place = place;
        this.party = party;
        this.isValidated = isValidated;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getPartyPhotoUrl() {
        return partyPhotoUrl;
    }

    public void setPartyPhotoUrl(String partyPhotoUrl) {
        this.partyPhotoUrl = partyPhotoUrl;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Politics{" +
                "docKey='" + docKey + '\'' +
                ", partyPhotoUrl='" + partyPhotoUrl + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", designation='" + designation + '\'' +
                ", place='" + place + '\'' +
                ", party='" + party + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}


