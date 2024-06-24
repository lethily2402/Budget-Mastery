
package com.finalproject.budgetmastery.Model;

import java.io.Serializable;

public class ModelListHome implements Serializable {
    private String tvDate;
    private String tvDay;
    private String tvTitle;
    private String tvAmount;
    private String imageUri;
    private String key;


    public ModelListHome() {
        // Default constructor required for calls to DataSnapshot.getValue(ModelListHome.class)
    }

    public ModelListHome(String tvDate, String tvDay, String tvTitle, String tvAmount, String imageUri) {
        this.tvDate = tvDate;
        this.tvDay = tvDay;
        this.tvTitle = tvTitle;
        this.tvAmount = tvAmount;
        this.imageUri = imageUri;
        this.key = key;
    }

    // Getters và Setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getTvDate() { return tvDate; }
    public void setTvDate(String tvDate) { this.tvDate = tvDate; }

    public String getTvDay() { return tvDay; }
    public void setTvDay(String tvDay) { this.tvDay = tvDay; }

    public String getTvTitle() { return tvTitle; }
    public void setTvTitle(String tvTitle) { this.tvTitle = tvTitle; }

    public String getTvAmount() { return tvAmount; }
    public void setTvAmount(String tvAmount) { this.tvAmount = tvAmount; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }


}
