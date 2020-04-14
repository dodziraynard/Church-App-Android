package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

public class Testimony {
    @SerializedName("testimony")
    String testimony;

    public Testimony(String testimony) {
        this.testimony = testimony;
    }

    public String getTestimony() {
        return testimony;
    }
}
