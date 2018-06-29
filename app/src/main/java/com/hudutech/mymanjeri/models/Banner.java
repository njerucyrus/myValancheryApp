package com.hudutech.mymanjeri.models;

public class Banner implements java.io.Serializable {
    private static final long serialVersionUID=1L;

    private String barnerUrl;
    private String docKey;
    //for demo
    private int resId;

    public Banner() {}

    public Banner(String barnerUrl, String docKey, int resId) {
        this.barnerUrl = barnerUrl;
        this.resId = resId;
        this.docKey = docKey;
    }

    public String getBarnerUrl() {
        return barnerUrl;
    }

    public void setBarnerUrl(String barnerUrl) {
        this.barnerUrl = barnerUrl;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }
}
