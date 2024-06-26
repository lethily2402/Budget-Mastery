package com.finalproject.budgetmastery.Model;

public class ModelListKhoanChi {

    private String imageUri;
    private String tvTitle;
    private String tvAmount;

    private String tvDate;

    public ModelListKhoanChi() {
        // Default constructor required for calls to DataSnapshot.getValue(ModelListKhoanChi.class)
    }

    public ModelListKhoanChi(String imageUri, String tvTitle) {
        this.imageUri = imageUri;
        this.tvTitle = tvTitle;
    }

    public String getTvAmount() {
        return tvAmount;
    }

    public void setTvAmount(String tvAmount) {
        this.tvAmount = tvAmount;
    }

    public String getTvDate() {
        return tvDate;
    }

    public void setTvDate(String tvDate) {
        this.tvDate = tvDate;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }
}

