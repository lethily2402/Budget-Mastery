package com.finalproject.budgetmastery.Model;

public class ModelListKhoanChi {

        private String imageUri;
        private String txt_title;

        public ModelListKhoanChi() {
            // Default constructor required for calls to DataSnapshot.getValue(ModelListKhoanChi.class)
        }

        public ModelListKhoanChi(String imageUri, String txt_title) {
            this.imageUri = imageUri;
            this.txt_title = txt_title;
        }

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

