package com.idea.church.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.idea.church.models.Audio;
import com.idea.church.R;

import java.util.ArrayList;

public class DownloadedPreachingItemAdapter extends RecyclerView.Adapter<DownloadedPreachingItemAdapter.PreachingViewHolder> {
    private ArrayList<Audio> audios;
    private Context context;
    private OnItemClickListener listener;

    public DownloadedPreachingItemAdapter(ArrayList<Audio> audios, Context context) {
        this.audios = audios;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClickListener(View view, int position);
        void onDeleteClickListener(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public PreachingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.downloaded_preaching_item, null);

        return new PreachingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadedPreachingItemAdapter.PreachingViewHolder holder, int position) {
        Audio video = audios.get(position);
        holder.title.setText(video.getTitle());
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    class PreachingViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageButton deleteBtn;
        CardView cardView;

        PreachingViewHolder(@NonNull final View itemView) {
            super(itemView);
            title  = itemView.findViewById(R.id.title);
            cardView  = itemView.findViewById(R.id.card);
            deleteBtn  = itemView.findViewById(R.id.deleteBtn);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onItemClickListener(itemView, getLayoutPosition());
                    }
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onDeleteClickListener(itemView, getLayoutPosition());
                    }
                }
            });
        }
    }
}
