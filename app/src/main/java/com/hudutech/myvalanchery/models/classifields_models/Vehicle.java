package com.hudutech.myvalanchery.models.classifields_models;

import java.util.List;

public class Vehicle implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private List<String> photoUrls;
    private double amount;
    private String brand;
    private String model;
    private int year;
    private double kmDriven;
    private String fuel;
    private String phoneNumber;
    private String location;
    private String heading;
    private String description;
    private boolean isValidated;

    public Vehicle() {
    }

    public Vehicle(String docKey, List<String> photoUrls, double amount, String brand, String model, int year, double kmDriven, String fuel, String phoneNumber, String location, String heading, String description, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrls = photoUrls;
        this.amount = amount;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.kmDriven = kmDriven;
        this.fuel = fuel;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getKmDriven() {
        return kmDriven;
    }

    public void setKmDriven(double kmDriven) {
        this.kmDriven = kmDriven;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
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

    @Override
    public String toString() {
        return "Vehicle{" +
                "docKey='" + docKey + '\'' +
                ", photoUrls=" + photoUrls +
                ", amount=" + amount +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", kmDriven=" + kmDriven +
                ", fuel='" + fuel + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", location='" + location + '\'' +
                ", heading='" + heading + '\'' +
                ", description='" + description + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
