package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PrayerRequestResponse {

    @SerializedName("requests")
    private ArrayList<PrayerRequest> results;

    public ArrayList<PrayerRequest> getResults(){
        return  results;
    }
}