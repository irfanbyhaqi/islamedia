package com.unikom.islamedia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResLokasiModel {
    List<LokasiModel> result;
    @SerializedName("status_code")
    private String status_code;
    @SerializedName("message")
    private String message;
    @SerializedName("jumlah_masjid")
    private String jumlah_masjid;
    @SerializedName("jumlah_ustadz")
    private String jumlah_ustadz;

    public List<LokasiModel> getResult() {
        return result;
    }

    public void setResult(List<LokasiModel> result) {
        this.result = result;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJumlah_masjid() {
        return jumlah_masjid;
    }

    public void setJumlah_masjid(String jumlah_masjid) {
        this.jumlah_masjid = jumlah_masjid;
    }

    public String getJumlah_ustadz() {
        return jumlah_ustadz;
    }

    public void setJumlah_ustadz(String jumlah_ustadz) {
        this.jumlah_ustadz = jumlah_ustadz;
    }
}
