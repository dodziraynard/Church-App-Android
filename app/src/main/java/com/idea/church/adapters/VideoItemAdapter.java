package com.idea.church.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.idea.church.models.Video;
import com.idea.church.R;

import java.util.ArrayList;

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.VideoViewHolder> {
    private Context context;
    private ArrayList<Video> videos;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDownloadClickListener(View itemView, int position);
        void onImageClickListener(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public VideoItemAdapter(Context context, ArrayList<Video> videos){
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_item_layout, null);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.title.setText(video.getTitle());
        holder.desc.setText(video.getDesc());
        holder.date.setText(video.getDate());

        Glide.with(context)
                .asBitmap()
                .load(videos.get(position).getImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView desc;
        TextView date;
        Button buttonDownload;
        Button cancelDownload;

        VideoViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            title  = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
            buttonDownload = itemView.findViewById(R.id.download);
            cancelDownload = itemView.findViewById(R.id.cancelDownload);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onImageClickListener(imageView, getLayoutPosition());
                    }
                }
            });

            buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onDownloadClickListener(itemView, getLayoutPosition());
                    }
                }
            });
        }
    }
}
