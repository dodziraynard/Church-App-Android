package com.idea.church.models;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Devotion  implements Parcelable {
    @SerializedName("image")
    private String imageUrl;

    private Bitmap bitmap;

    @SerializedName("verse")
    private String verse_title;

    @SerializedName("content")
    private String verse_detail;

    private String date;

    @SerializedName("id")
    private String serverID;

    public Devotion(Bitmap image, String imageUrl, String verse_title, String verse_detail, String date, String id) {
        this.bitmap = image;
        this.verse_title = verse_title;
        this.verse_detail = verse_detail;
        this.date = date;
        this.serverID = id;
        this.imageUrl = imageUrl;
    }

    public Bitmap getImage() {
        return bitmap;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public String getVerse_title() {
        return verse_title;
    }

    public String getVerse_detail() {
        return verse_detail;
    }
    public String getDate() {
        return date;
    }
    public String getServerID() {
        return serverID;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return verse_title;
    }

    private Devotion(Parcel in) {
        verse_title = in.readString();
        verse_detail = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(verse_title);
        dest.writeString(verse_detail);
        dest.writeString(date);
    }

    public static final Parcelable.Creator<Devotion> CREATOR = new Parcelable.Creator<Devotion>() {
        public Devotion createFromParcel(Parcel in) {
            return new Devotion(in);
        }

        public Devotion[] newArray(int size) {
            return new Devotion[size];
        }
    };
}