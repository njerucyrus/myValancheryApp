package com.hudutech.myvalancery.models.contact_models;

import java.io.Serializable;

public class Mp implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String photoUrl;
    private String name;
    private String constituency;
    private String party;
    private String mobile;
    private String phone;
    private String email;
    private String address;
    private boolean isValidated;

    public Mp() {
    }

    public Mp(String docKey, String photoUrl, String name, String constituency, String party, String mobile, String phone, String email, String address, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrl = photoUrl;
        this.name = name;
        this.constituency = constituency;
        this.party = party;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
        this.address = address;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Mp{" +
                "docKey='" + docKey + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", name='" + name + '\'' +
                ", constituency='" + constituency + '\'' +
                ", party='" + party + '\'' +
                ", mobile='" + mobile + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
