package com.hudutech.myvalanchery.models;

public class User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String name;
    private String phoneNumber;
    private boolean isMainAdmin;
    private boolean isSBAdmin;

    public User() {
    }

    public User(String username, String name, String phoneNumber, boolean isMainAdmin, boolean isSBAdmin) {
        this.username = username;
        this.isMainAdmin = isMainAdmin;
        this.isSBAdmin = isSBAdmin;
        this.name = name;
        this.phoneNumber = phoneNumber;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isMainAdmin() {
        return isMainAdmin;
    }

    public void setMainAdmin(boolean mainAdmin) {
        isMainAdmin = mainAdmin;
    }

    public boolean isSBAdmin() {
        return isSBAdmin;
    }

    public void setSBAdmin(boolean SBAdmin) {
        isSBAdmin = SBAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", isMainAdmin=" + isMainAdmin +
                ", isSBAdmin=" + isSBAdmin +
                '}';
    }
}
