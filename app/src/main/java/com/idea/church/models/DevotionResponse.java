package com.idea.church.models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;


public class DevotionResponse {
    @SerializedName("devotions")
    private ArrayList<Devotion> results;
    public ArrayList<Devotion> getResults(){
        return  results;
    }
}
