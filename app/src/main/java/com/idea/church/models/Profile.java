package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

public class Profile{
    @SerializedName("full_name")
    private String fullName;

    @SerializedName("church")
    private String church;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    @SerializedName("image")
    private String image;

    public Profile(String fullName, String church, String mobile, String email, String username, String image) {
        this.fullName = fullName;
        this.church = church;
        this.mobile = mobile;
        this.email = email;
        this.username = username;
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public String getChurch() {
        return church;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }
}