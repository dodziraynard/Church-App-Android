package com.idea.church.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {
    @SerializedName("file")
    private String data;

    @SerializedName("title")
    private String title;

    @SerializedName("desc")
    private String desc;

    @SerializedName("image")
    private String image;
    private String artist;
    private String date;

    public Video(String data, String title, String desc, String artist, String date) {
        this.data = data;
        this.date = date;
        this.title = title;
        this.desc = desc;
        this.artist = artist;
    }

    public String getDate(){
        return date;
    }

    public String getData() {
        return data;
    }

    public String getImage(){
        return image;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setAlbum(String desc) {
        this.desc = desc;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return title;
    }

    private Video(Parcel in) {
        data = in.readString();
        date = in.readString();
        desc = in.readString();
        artist = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(artist);
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}