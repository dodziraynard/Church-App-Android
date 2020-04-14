package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TestimonyResponse {
    @SerializedName("testimonies")
    private ArrayList<Testimony> results;

    public ArrayList<Testimony> getResults(){
        return  results;
    }
}
