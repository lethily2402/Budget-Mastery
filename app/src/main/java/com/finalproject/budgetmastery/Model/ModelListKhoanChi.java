package com.finalproject.budgetmastery.Model;

public class ModelListKhoanChi {
    private String imageUri;
    private String txt_title;
    private String key;
    private String ngayThang;
    private String ghiChu;
    private String soTien;

    public ModelListKhoanChi(String imageUri, String txt_title, String key, String ngayThang, String ghiChu, String soTien) {
        this.imageUri = imageUri;
        this.txt_title = txt_title;
        this.key = key;
        this.ngayThang = ngayThang;
        this.ghiChu = ghiChu;
        this.soTien = soTien;
    }

    public ModelListKhoanChi(String imageUri, String txt_title, String key) {
        this.imageUri = imageUri;
        this.txt_title = txt_title;
        this.key = key;
    }
    public ModelListKhoanChi() {
    }

    public String getNgayThang() {
        return ngayThang;
    }

    public void setNgayThang(String ngayThang) {
        this.ngayThang = ngayThang;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getSoTien() {
        return soTien;
    }

    public void setSoTien(String soTien) {
        this.soTien = soTien;
    }

    // Getter và Setter cho key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    // Getter và Setter khác
    public String getImageUri() {
        return imageUri;
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