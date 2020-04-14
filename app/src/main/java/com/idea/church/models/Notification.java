package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Notification {

    @SerializedName("message")
    private String message;

    @SerializedName("title")
    private String title;

    @SerializedName("date")
    private String date;

    public Notification(String message, String title, String date) {
        this.message = message;
        this.title = title;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}