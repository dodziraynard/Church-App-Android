package com.idea.church.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idea.church.adapters.DownloadedVideoItemAdapter;
import com.idea.church.models.Video;
import com.idea.church.R;

import java.io.File;
import java.util.ArrayList;

import static com.idea.church.activities.VideoActivity.FULL_VIDEO_DOWNLOAD_PATH;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadedVideo extends Fragment {
    private ArrayList<Video> videos;

    public DownloadedVideo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloaded_video, container, false);


        loadVideos();
        renderAdapter(view);
        return view;
    }

    private void loadVideos() {
        videos = new ArrayList<>();
        File folder = new File(Environment.getExternalStorageDirectory() + FULL_VIDEO_DOWNLOAD_PATH);

        if (!folder.exists()){
            return;
        }

        File [] files = folder.listFiles();

        for (File file : files) {
            String data = file.getPath();
            String title = file.getName();
            videos.add(new Video(data, title, "", "", ""));
        }
    }

    private void renderAdapter(final View view) {
        if (videos.size() == 0){
            TextView infoText = view.findViewById(R.id.infoText);
            infoText.setText("You've not downloaded any video yet!");;
            infoText.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DownloadedVideoItemAdapter downloadedVideoItemAdapter
                = new DownloadedVideoItemAdapter(getContext(), videos);

        downloadedVideoItemAdapter.setOnItemClickListener(new DownloadedVideoItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View itemView, int position) {
                Video currentVideo = videos.get(position);
                Intent videoIntent = new Intent(getContext(), VideoPlayerActivity.class);
                videoIntent.putExtra("videoUri", currentVideo.getData());
                startActivity(videoIntent);
            }

            @Override
            public void onDeleteItemClickListener(View itemView, int position) {
                final Video currentVideo = videos.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete");
                builder.setMessage("Sure you want to delete the video from internal storage?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Delete video
                       File file = new File(Environment.getExternalStorageDirectory() + FULL_VIDEO_DOWNLOAD_PATH+"/"+currentVideo.getTitle());
                       if (file.exists()){
                           boolean result = file.delete();

                           if (result){
                               // file is deleted
                               loadVideos();
                               renderAdapter(view);
                           }
                       }
                    }
                });
                builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.show();
            }
        });
        recyclerView.setAdapter(downloadedVideoItemAdapter);
    }
}