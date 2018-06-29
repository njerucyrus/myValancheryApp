package com.hudutech.mymanjeri.models;

public class User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private boolean isMainAdmin;
    private boolean isSBAdmin;

    public User() {
    }

    public User(String username, boolean isMainAdmin, boolean isSBAdmin) {
        this.username = username;
        this.isMainAdmin = isMainAdmin;
        this.isSBAdmin = isSBAdmin;
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
