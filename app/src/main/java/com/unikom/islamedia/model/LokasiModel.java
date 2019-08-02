package com.unikom.islamedia.model;

import com.google.gson.annotations.SerializedName;

public class LokasiModel {
    @SerializedName("logo_masjid")
    private String logo_masjid;
    @SerializedName("nama_masjid")
    private String nama_masjid;
    @SerializedName("alamat_masjid")
    private String alamat_masjid;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    @SerializedName("jarak")
    private double jarak;

    public String getLogo_masjid() {
        return logo_masjid;
    }

    public void setLogo_masjid(String logo_masjid) {
        this.logo_masjid = logo_masjid;
    }

    public String getNama_masjid() {
        return nama_masjid;
    }

    public void setNama_masjid(String nama_masjid) {
        this.nama_masjid = nama_masjid;
    }

    public String getAlamat_masjid() {
        return alamat_masjid;
    }

    public void setAlamat_masjid(String alamat_masjid) {
        this.alamat_masjid = alamat_masjid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getJarak() {
        return jarak;
    }

    public void setJarak(double jarak) {
        this.jarak = jarak;
    }
}
