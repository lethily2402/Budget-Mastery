package com.finalproject.budgetmastery.Model;

import java.io.Serializable;

public class ModelListKhoanThu implements Serializable {

    private int image_icon;
    private String txt_title;

    public ModelListKhoanThu(int image_icon, String txt_title) {
        this.image_icon = image_icon;
        this.txt_title = txt_title;
    }

    public int getImage_icon() {
        return image_icon;
    }

    public void setImage_icon(int image_icon) {
        this.image_icon = image_icon;
    }

    public String getTxt_title() {
        return txt_title;
    }

    public void setTxt_title(String txt_title) {
        this.txt_title = txt_title;
    }
}