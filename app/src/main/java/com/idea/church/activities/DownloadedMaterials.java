package com.idea.church.activities;


import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.idea.church.adapters.DownloadedMaterialItemAdapter;
import com.idea.church.models.Material;
import com.idea.church.R;
import com.idea.church.models.Video;

import java.io.File;
import java.util.ArrayList;

import static com.idea.church.activities.MaterialsActivity.FULL_MATERIAL_DOWNLOAD_PATH;
import static com.idea.church.activities.VideoActivity.FULL_VIDEO_DOWNLOAD_PATH;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadedMaterials extends Fragment {
    ArrayList<Material> materials;

    public DownloadedMaterials() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloaded_materials, container, false);

        loadMaterials();
        renderAdapter(view);
        return view;
    }

    private void renderAdapter(final View view) {
        if (materials.size() == 0){
            TextView infoText = view.findViewById(R.id.infoText);
            infoText.setText("You've not downloaded any material yet!");
            infoText.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView  = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DownloadedMaterialItemAdapter downloadedMaterialItemAdapter =
                new DownloadedMaterialItemAdapter(materials, getContext());

        downloadedMaterialItemAdapter.setOnItemClickListener(new DownloadedMaterialItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View itemView, int position) {
                Material material = materials.get(position);
                Intent videoIntent = new Intent(getContext(), PDFViewerActivity.class);
                videoIntent.putExtra("pdfUri", material.getData());
                startActivity(videoIntent);
            }

            @Override
            public void onDeleteClickListener(View itemView, int position) {
                final Material material = materials.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete");
                builder.setMessage("Sure you want to delete the pdf from internal storage?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Delete video
                        File file = new File(Environment.getExternalStorageDirectory() + FULL_MATERIAL_DOWNLOAD_PATH+"/"+material.getTitle());
                        if (file.exists()){
                            boolean result = file.delete();
                            if (result){
                                // file is deleted
                                Toast.makeText(getContext(), "DELETED", Toast.LENGTH_LONG).show();
                                loadMaterials();
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

        recyclerView.setAdapter(downloadedMaterialItemAdapter);
    }

    private void loadMaterials() {
        materials = new ArrayList<>();
        File folder = new File(Environment.getExternalStorageDirectory() + FULL_MATERIAL_DOWNLOAD_PATH);

        if(!folder.exists()){
            return;
        }

        File [] files = folder.listFiles();

        for (File file : files) {
            String data = file.getPath();
            String title = file.getName();
            materials.add(new Material(data, title, "", "", ""));
        }
    }


}
