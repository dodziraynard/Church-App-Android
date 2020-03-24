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

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        preachingBtn  = view.findViewById(R.id.preachingBtn);
        preachingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preachingIntent  = new Intent(getContext(), PreachingsActivity.class);
                startActivity(preachingIntent);
            }
        });
        return view;
    }

}
