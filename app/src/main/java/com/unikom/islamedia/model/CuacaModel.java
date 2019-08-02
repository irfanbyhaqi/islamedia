package com.unikom.islamedia.model;

import com.google.gson.annotations.SerializedName;

public class CuacaModel {

    @SerializedName("temp")
    private Double temp;

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}
