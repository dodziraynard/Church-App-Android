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

import com.idea.church.adapters.DownloadedPreachingItemAdapter;
import com.idea.church.models.Audio;
import com.idea.church.R;

import java.io.File;
import java.util.ArrayList;

import static com.idea.church.activities.PreachingsActivity.FULL_PREACHING_DOWNLOAD_PATH;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadedPreachings extends Fragment {
    private ArrayList<Audio> audios;

    public DownloadedPreachings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloaded_preachings, container, false);

        loadPreachings();
        renderAdapter(view);
        return view;
    }

    private void loadPreachings() {
        audios = new ArrayList<>();
        File folder = new File(Environment.getExternalStorageDirectory() + FULL_PREACHING_DOWNLOAD_PATH);

        if (!folder.exists()){
            return;
        }

        File [] files = folder.listFiles();

        for (File file : files) {
            String data = file.getPath();
            String title = file.getName();
            audios.add(new Audio(data, title, "", "", ""));
        }
    }

    private void renderAdapter(final View view) {
        if (audios.size() == 0){
            TextView infoText = view.findViewById(R.id.infoText);
            infoText.setVisibility(View.VISIBLE);
        }
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DownloadedPreachingItemAdapter downloadedPreachingItemAdapter =
                new DownloadedPreachingItemAdapter(audios, getContext());

        downloadedPreachingItemAdapter.setOnItemClickListener(new DownloadedPreachingItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Audio audio = audios.get(position);
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(audio.getData()), "audio/*");
                startActivity(intent);
            }

            @Override
            public void onDeleteClickListener(final View itemView, int position) {
                final Audio audio = audios.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete");
                builder.setMessage("Sure you want to delete the video from internal storage?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Delete video
                        File file = new File(Environment.getExternalStorageDirectory() + FULL_PREACHING_DOWNLOAD_PATH+"/"+audio.getTitle());
                        if (file.exists()){
                            boolean result = file.delete();

                            if (result){
                                // file is deleted
                                loadPreachings();
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
        recyclerView.setAdapter(downloadedPreachingItemAdapter);
    }
}
