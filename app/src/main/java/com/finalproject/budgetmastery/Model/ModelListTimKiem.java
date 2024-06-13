package com.finalproject.budgetmastery.Model;

import java.io.Serializable;

public class ModelListTimKiem implements Serializable {
    private String svSearch;
    private String lvThuchi;

    public ModelListTimKiem(String svSearch, String lvThuchi) {
        this.svSearch = svSearch;
        this.lvThuchi = lvThuchi;
    }

    public String getSvSearch() {
        return svSearch;
    }

    public void setSvSearch(String svSearch) {
        this.svSearch = svSearch;
    }

    public String getLvThuchi() {
        return lvThuchi;
    }

    public void setLvThuchi(String lvThuchi) {
        this.lvThuchi = lvThuchi;
    }
}
