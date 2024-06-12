package com.finalproject.budgetmastery.Model;

import android.net.Uri;

import java.io.Serializable;

public class ModelListKhoanChi implements Serializable {
    private String imageUri;
    private String txt_title;

    public ModelListKhoanChi(String imageUri, String txt_title) {
        this.imageUri = imageUri;
        this.txt_title = txt_title;
    }

    public ModelListKhoanChi() {
    }

    public Uri getImageUri() {
        return Uri.parse(imageUri);
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTxt_title() {
        return txt_title;
    }

    public void setTxt_title(String txt_title) {
        this.txt_title = txt_title;
    }
}