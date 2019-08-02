package com.unikom.islamedia.model;

import com.google.gson.annotations.SerializedName;

public class UstadzModel {
    @SerializedName("nama_ustadz")
    private String nama_ustadz;
    @SerializedName("foto_ustadz")
    private String foto_ustadz;
    @SerializedName("alamat_ustadz")
    private String alamat_ustadz;
    @SerializedName("no_kontak_ustadz")
    private String no_kontak_ustadz;
    @SerializedName("nama_masjid")
    private String nama_masjid;
    @SerializedName("kompetensi")
    private String kompetensi;

    public String getNama_ustadz() {
        return nama_ustadz;
    }

    public void setNama_ustadz(String nama_ustadz) {
        this.nama_ustadz = nama_ustadz;
    }

    public String getFoto_ustadz() {
        return foto_ustadz;
    }

    public void setFoto_ustadz(String foto_ustadz) {
        this.foto_ustadz = foto_ustadz;
    }

    public String getAlamat_ustadz() {
        return alamat_ustadz;
    }

    public void setAlamat_ustadz(String alamat_ustadz) {
        this.alamat_ustadz = alamat_ustadz;
    }

    public String getNo_kontak_ustadz() {
        return no_kontak_ustadz;
    }

    public void setNo_kontak_ustadz(String no_kontak_ustadz) {
        this.no_kontak_ustadz = no_kontak_ustadz;
    }

    public String getNama_masjid() {
        return nama_masjid;
    }

    public void setNama_masjid(String nama_masjid) {
        this.nama_masjid = nama_masjid;
    }

    public String getKompetensi() {
        return kompetensi;
    }

    public void setKompetensi(String kompetensi) {
        this.kompetensi = kompetensi;
    }
}
