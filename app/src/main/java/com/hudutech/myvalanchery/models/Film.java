package com.hudutech.myvalanchery.models;

import java.io.Serializable;
import java.util.List;

public class Film implements Serializable {
    private final static long serialVersionUID = 1L;
    private String docKey;
    private String photoUrl;
    private String title;
    private String place;
    private String address;
    private String phoneNumber;
    private int shows;
    private List<String> times;
    private double lat;
    private double lng;
    private boolean isValidated;

    public Film() {
    }

    public Film(String docKey, String photoUrl, String title, String place, String address, String phoneNumber, int shows, List<String> times, double lat, double lng, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrl = photoUrl;
        this.title = title;
        this.place = place;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.shows = shows;
        this.times = times;
        this.lat = lat;
        this.lng = lng;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getShows() {
        return shows;
    }

    public void setShows(int shows) {
        this.shows = shows;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
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
        return "Film{" +
                "docKey='" + docKey + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", title='" + title + '\'' +
                ", place='" + place + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", shows=" + shows +
                ", times=" + times +
                ", lat=" + lat +
                ", lng=" + lng +
                ", isValidated=" + isValidated +
                '}';
    }
}
