package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LeaderResponse {

    @SerializedName("leaders")
    private ArrayList<Leader> results;
    public ArrayList<Leader> getResults(){
        return  results;
    }
}
