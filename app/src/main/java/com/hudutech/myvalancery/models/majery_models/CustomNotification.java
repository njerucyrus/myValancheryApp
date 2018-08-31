package com.hudutech.myvalancery.models.majery_models;

import java.util.Date;

public class CustomNotification implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String notification;
    private boolean isValidated;
    private Date date;

    public CustomNotification() {

    }

    public CustomNotification(String docKey, String notification, boolean isValidated, Date date) {
        this.docKey = docKey;
        this.notification = notification;
        this.isValidated = isValidated;
        this.date = date;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
