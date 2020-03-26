package com.idea.church.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Audio implements Parcelable {

    private String data;
    private String title;
    private String desc;
    private String artist;
    private String image;

    public Audio(String data, String title, String desc, String artist, String image) {
        this.data = data;
        this.title = title;
        this.desc = desc;
        this.artist = artist;
        this.image = image;
    }

    public String getData() {
        return data;
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

    public String getAlbum() {
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

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
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
        image = in.readString();
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