package com.idea.church.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Leader{
    @SerializedName("full_name")
    private String fullName;

    @SerializedName("position")
    private String position;

    @SerializedName("contacts")
    private String contact;

    @SerializedName("image")
    private String image;

    public Leader(String fullName, String position, String image, String contact) {
        this.fullName = fullName;
        this.position = position;
        this.image = image;
        this.contact = contact;
    }

    public String getFullName() {
        return fullName;
    }

    public String getContact(){
        return contact;
    }
    public String getPosition() {
        return position;
    }

    public String getImage() {
        return this.image;
    }
}