package com.hudutech.myvalanchery.models;

import android.net.Uri;

public class SelectedImage {
    private Uri imageUri;

    public SelectedImage() {
    }

    public SelectedImage(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
