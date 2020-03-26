package com.idea.church;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class DashboardFragment extends Fragment {
    ImageButton preachingBtn;
    ImageButton videoBtn;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        preachingBtn  = view.findViewById(R.id.preachingBtn);
        videoBtn  = view.findViewById(R.id.videoBtn);

        preachingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preachingIntent  = new Intent(getContext(), PreachingsActivity.class);
                startActivity(preachingIntent);
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent  = new Intent(getContext(), VideoActivity.class);
                startActivity(videoIntent);
            }
        });
        return view;
    }

}
