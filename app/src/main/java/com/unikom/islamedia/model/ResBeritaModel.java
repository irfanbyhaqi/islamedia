package com.unikom.islamedia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResBeritaModel {
    List<BeritaModel> result;
    @SerializedName("status_code")
    private String status_code;
    @SerializedName("message")
    private String message;
    @SerializedName("autocomplete")
    private String[] autocomplete;

    public List<BeritaModel> getResult(){
        return  result;
    }

    public void setResult(List<BeritaModel> result){
        this.result = result;
    }

    public ResBeritaModel(String status_code, String message) {
        this.status_code = status_code;
        this.message = message;
    }

    public String getStatus_code() {
        return status_code;
    }

    public String getMessage() {
        return message;
    }

    public String[] getAutocomplete() {
        return autocomplete;
    }

    public void setAutocomplete(String[] autocomplete) {
        this.autocomplete = autocomplete;
    }
}
