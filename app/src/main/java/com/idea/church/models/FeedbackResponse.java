package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FeedbackResponse {
    @SerializedName("feedbacks")
    private ArrayList<Feedback> results;

    public ArrayList<Feedback> getResults(){
        return  results;
    }
}
