package com.idea.church.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("username")
    private String username;

    public String getToken(){
        return  token;
    }

    public String getUsername(){
        return username;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
