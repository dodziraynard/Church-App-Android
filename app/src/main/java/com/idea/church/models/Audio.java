package com.idea.church.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Audio implements Parcelable {
    @SerializedName("file")
    private String data;

    @SerializedName("title")
    private String title;

    @SerializedName("desc")
    private String desc;

    @SerializedName("preacher")
    private String artist;

    public Audio(String data, String title, String desc, String artist) {
        this.data = data;
        this.title = title;
        this.desc = desc;
        this.artist = artist;
    }

    public String getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getArtist() {
        return artist;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return title;
    }

    private Audio(Parcel in) {
        data = in.readString();
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

    public static final Parcelable.Creator<Audio> CREATOR = new Parcelable.Creator<Audio>() {
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };
}