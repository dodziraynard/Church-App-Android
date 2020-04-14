package com.idea.church.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Material implements Parcelable {
    @SerializedName("file")
    private String data;

    @SerializedName("title")
    private String title;

    @SerializedName("desc")
    private String desc;

    @SerializedName("date")
    private String date;

    @SerializedName("image")
    private String image;

    public Material(String data, String title, String desc, String date, String image) {
        this.data = data;
        this.date = date;
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public String getImage(){
        return image;
    }
    public String getDate(){
        return date;
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

    public String getDesc() {
        return desc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return title;
    }

    private Material(Parcel in) {
        data = in.readString();
        date = in.readString();
        desc = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeString(title);
        dest.writeString(desc);
    }

    public static final Parcelable.Creator<Material> CREATOR = new Parcelable.Creator<Material>() {
        public Material createFromParcel(Parcel in) {
            return new Material(in);
        }

        public Material[] newArray(int size) {
            return new Material[size];
        }
    };
}