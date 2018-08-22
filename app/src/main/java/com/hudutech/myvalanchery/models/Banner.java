package com.hudutech.myvalanchery.models;

public class Banner implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String bannerUrl;
    private String docKey;
    //for demo
    private int resId;
    private boolean start;

    public Banner() {
    }

    public Banner(String bannerUrl, String docKey, int resId, boolean start) {
        this.bannerUrl = bannerUrl;
        this.resId = resId;
        this.docKey = docKey;
        this.start = start;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
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

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
