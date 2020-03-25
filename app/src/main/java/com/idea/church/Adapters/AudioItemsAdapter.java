package com.idea.church.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.idea.church.Models.Audio;
import com.idea.church.R;
import java.util.ArrayList;


public class AudioItemsAdapter extends RecyclerView.Adapter<AudioItemsAdapter.AudioViewHolder> {

    private Context context;
    private ArrayList<Audio> audios;

    //Listener for items
    private static OnItemClickListener listener;

    // Listener interface
    public interface OnItemClickListener{
        void onCardClickListener(View itemView, int position);
        void setOnItemDownloadListener(View buttonDownload, int layoutPosition);
    }

    // method for setting listener to activity
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public AudioItemsAdapter(Context context, ArrayList<Audio> audios){
        this.context  = context;
        this.audios   = audios;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.audio_item_layout, null);

        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder viewHolder, final int i){
        String title = audios.get(i).getTitle();
        viewHolder.title.setText(title);
    }

    // Get the number of view items
    @Override
    public int getItemCount(){
        return audios.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder {
        Button buttonDownload;
        TextView title;
        TextView preacher;
        TextView date;
        TextView desc;
        CardView card;

        AudioViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.title);
            preacher = itemView.findViewById(R.id.preacher);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);

            buttonDownload = itemView.findViewById(R.id.btnDownload);
            card = itemView.findViewById(R.id.audio_item);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onCardClickListener(card, getLayoutPosition());
                }
            });

            buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.setOnItemDownloadListener(buttonDownload, getLayoutPosition());
                }
            });

        }
    }
}


