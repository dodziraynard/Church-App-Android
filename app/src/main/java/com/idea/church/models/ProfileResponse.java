package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProfileResponse {

    @SerializedName("profile")
    private ArrayList<Profile> results;

    public Profile getResults(){
        if (results.size() > 0)
        return  results.get(0);
        return  null;
    }
}
