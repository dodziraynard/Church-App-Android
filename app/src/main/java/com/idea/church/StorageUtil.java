package com.idea.church;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idea.church.Models.Audio;

import java.lang.reflect.Type;

public class StorageUtil {
    private final String STORAGE = "com.idea.church.STORAGE";
    private SharedPreferences preferences;
    private Context context;

    private Audio audio;

    public StorageUtil(Context context) {
        this.context = context;
    }


    public void clearCachedAudioPlaylist() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void storeMediaSource(String media) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("media", media);
        editor.apply();
    }

    public String getMediaSource() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getString("media", null);
    }
}
