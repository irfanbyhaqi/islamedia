package com.unikom.islamedia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResUstadzModel {
    @SerializedName("status_code")
    private String status_code;
    @SerializedName("message")
    private String message;
    List<UstadzModel> result;

    public ResUstadzModel(String status_code, String message) {
        this.status_code = status_code;
        this.message = message;
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

    public List<UstadzModel> getResult() {
        return result;
    }

    public void setResult(List<UstadzModel> result) {
        this.result = result;
    }
}
