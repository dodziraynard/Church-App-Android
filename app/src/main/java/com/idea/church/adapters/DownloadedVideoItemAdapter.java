package com.idea.church.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idea.church.models.Video;
import com.idea.church.R;

import java.util.ArrayList;

public class DownloadedVideoItemAdapter extends RecyclerView.Adapter<DownloadedVideoItemAdapter.VideoViewHolder> {

    private Context context;
    private ArrayList<Video> videos;

    private  static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClickListener(View itemView, int position);
        void onDeleteItemClickListener(View itemView, int position);
    }

    public void setOnItemClickListener(DownloadedVideoItemAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    public DownloadedVideoItemAdapter(Context context, ArrayList<Video> videos){
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.downloaded_video_item, null);

        return new DownloadedVideoItemAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.title.setText(video.getTitle());
        Glide.with(context)
                .load(videos.get(position).getData())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        CardView cardView;
        ImageButton deleteBtn;

        public VideoViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            title  = itemView.findViewById(R.id.title);
            cardView  = itemView.findViewById(R.id.card);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            cardView.setOnClickListener(new View.OnClickListener() {
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
                        listener.onDeleteItemClickListener(itemView, getLayoutPosition());
                    }
                }
            });
        }
    }
}
