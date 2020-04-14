package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationResponse {

    @SerializedName("notifications")
    private ArrayList<Notification> results;
    public ArrayList<Notification> getResults(){
        return  results;
    }
}
