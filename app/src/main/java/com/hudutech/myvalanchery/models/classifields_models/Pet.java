package com.hudutech.myvalanchery.models.classifields_models;

import java.io.Serializable;
import java.util.List;

public class Pet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private List<String> photoUrls;
    private double amount;
    private String breed;
    private int age;
    private String phoneNumber;
    private String location;
    private String heading;
    private String description;
    private boolean isValidated;

    public Pet() {
    }

    public Pet(String docKey, List<String> photoUrls, double amount, String breed, int age, String phoneNumber, String location, String heading, String description, boolean isValidated) {
        this.docKey = docKey;
        this.photoUrls = photoUrls;
        this.amount = amount;
        this.breed = breed;
        this.age = age;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
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
        return "Pet{" +
                "docKey='" + docKey + '\'' +
                ", amount=" + amount +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", location='" + location + '\'' +
                ", heading='" + heading + '\'' +
                ", description='" + description + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
