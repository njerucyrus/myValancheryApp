package com.hudutech.myvalancery.models.majery_models;

public class Education implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String imageUrl;
    private String placeName;
    private String description;
    private String docKey;
    private boolean isValidated;

    public Education() {
    }

    public Education(String imageUrl, String placeName, String description, String docKey, boolean isValidated) {
        this.imageUrl = imageUrl;
        this.placeName = placeName;
        this.description = description;
        this.docKey = docKey;
        this.isValidated = isValidated;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Education{" +
                "imageUrl='" + imageUrl + '\'' +
                ", placeName='" + placeName + '\'' +
                ", description='" + description + '\'' +
                ", docKey='" + docKey + '\'' +
                '}';
    }
}
