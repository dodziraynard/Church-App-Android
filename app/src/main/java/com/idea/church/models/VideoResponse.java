package com.idea.church.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideoResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("videos")
    private ArrayList<Video> results;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public ArrayList<Video> getResults() {
        return results;
    }
    public void setResults(ArrayList<Video> results) {
        this.results = results;
    }
    public int getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
