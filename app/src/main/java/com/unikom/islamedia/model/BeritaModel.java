package com.unikom.islamedia.model;

import com.google.gson.annotations.SerializedName;

public class BeritaModel {
    @SerializedName("gambar")
    private String gambar;
    @SerializedName("nama_admin")
    private String nama_admin;
    @SerializedName("judul")
    private String judul;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("ket")
    private String ket;

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNama_admin() {
        return nama_admin;
    }

    public void setNama_admin(String nama_admin) {
        this.nama_admin = nama_admin;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }
}
