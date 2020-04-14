package com.idea.church.models;

import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("feedback")
    private String feedback;

    public String getFeedback() {
        return feedback;
    }

    public Feedback(String feedback) {
        this.feedback = feedback;
    }
}
