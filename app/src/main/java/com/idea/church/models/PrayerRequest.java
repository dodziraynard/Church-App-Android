package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

public class PrayerRequest{
    @SerializedName("request")
    private String request;

    public PrayerRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}