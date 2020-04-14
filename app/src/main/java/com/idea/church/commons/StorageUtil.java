package com.idea.church.commons;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idea.church.models.Audio;

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

    public void storeAudio(Audio audio) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(audio);
        editor.putString("audio", json);
        editor.apply();
    }

    public void storeSeekPosition(int progress) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("progress", progress);
        editor.apply();
    }

    public int getSeekPosition() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getInt("progress", 0);
    }

    public Audio getAudio() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("audio", null);
        Type type = new TypeToken<Audio>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
