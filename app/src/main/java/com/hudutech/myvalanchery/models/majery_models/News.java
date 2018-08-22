package com.hudutech.myvalanchery.models.majery_models;

import java.util.Date;

public class News implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String imageUrl;
    private String newsHeading;
    private String news;
    private String docKey;
    private boolean isValidated;
    private Date date;

    public News() {
    }

    public News(String imageUrl, String newsHeading, String news, String docKey, Date date, boolean isValidated) {
        this.imageUrl = imageUrl;
        this.newsHeading = newsHeading;
        this.news = news;
        this.docKey = docKey;
        this.date = date;
        this.isValidated = isValidated;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "News{" +
                "imageUrl='" + imageUrl + '\'' +
                ", newsHeading='" + newsHeading + '\'' +
                ", news='" + news + '\'' +
                ", docKey='" + docKey + '\'' +
                '}';
    }
}
