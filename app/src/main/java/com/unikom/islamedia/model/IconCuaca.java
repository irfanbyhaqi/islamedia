package com.unikom.islamedia.model;

import com.google.gson.annotations.SerializedName;

public class IconCuaca {
    @SerializedName("icon")
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
