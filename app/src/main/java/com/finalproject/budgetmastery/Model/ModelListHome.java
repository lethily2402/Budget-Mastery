package com.finalproject.budgetmastery.Model;

import java.io.Serializable;

public class ModelListHome implements Serializable {
    private String tvDate;
    private String tvDay;
    private String tvTitle;
    private String tvAmount;

    // Default constructor required for calls to DataSnapshot.getValue(ModelListHome.class)
    public ModelListHome() {
    }

    public ModelListHome(String tvDate, String tvDay, String tvTitle, String tvAmount) {
        this.tvDate = tvDate;
        this.tvDay = tvDay;
        this.tvTitle = tvTitle;
        this.tvAmount = tvAmount;
    }

    public String getTvDate() {
        return tvDate;
    }

    public void setTvDate(String tvDate) {
        this.tvDate = tvDate;
    }

    public String getTvDay() {
        return tvDay;
    }

    public void setTvDay(String tvDay) {
        this.tvDay = tvDay;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public String getTvAmount() {
        return tvAmount;
    }

    public void setTvAmount(String tvAmount) {
        this.tvAmount = tvAmount;
    }
}
