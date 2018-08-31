package com.hudutech.myvalancery.models.digital_models;

public class Partner implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String photoUrl;
    private String gender;
    private String name;
    private String place;
    private String religion;
    private int age;
    private String phoneNumber;
    private String moreDetails;
    private String userUid;
    private String docKey;
    private boolean isValidated;

    public Partner() {
    }

    public Partner(String photoUrl, String gender, String name, String place, String religion, int age, String phoneNumber, String moreDetails, String userUid, String docKey, boolean isValidated) {
        this.photoUrl = photoUrl;
        this.gender = gender;
        this.name = name;
        this.place = place;
        this.religion = religion;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.moreDetails = moreDetails;
        this.userUid = userUid;
        this.docKey = docKey;
        this.isValidated = isValidated;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Partner{" +
                "photoUrl='" + photoUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", religion='" + religion + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", moreDetails='" + moreDetails + '\'' +
                ", userUid='" + userUid + '\'' +
                ", docKey='" + docKey + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
