package com.idea.church.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idea.church.Models.Audio;
import com.idea.church.R;
import com.idea.church.ViewHolders.AudioViewHolder;

import java.util.ArrayList;

public class AudioItemsAdapter extends RecyclerView.Adapter<AudioViewHolder> {
    private Context context;
    private ArrayList<Audio> audios;

    //Listener for items
    private static OnItemClickListener listener;

    // Listener interface
    public interface OnItemClickListener{
        void onItemClickListener(View itemView, int position);
    }

    // method for setting listener to activity
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public AudioItemsAdapter(Context context, ArrayList<Audio> audios){
        this.context  = context;
        this.audios  = audios;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.audio_item_layout, null);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder viewHolder, int i){
        String title = audios.get(i).getTitle();

    }

    // Get the number of view items
    @Override
    public int getItemCount(){
        return audios.size();
    }
}
