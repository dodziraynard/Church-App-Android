package com.idea.church.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.idea.church.adapters.TabAdapter;
import com.idea.church.activities.DownloadedMaterials;
import com.idea.church.activities.DownloadedPreachings;
import com.idea.church.activities.DownloadedVideo;
import com.idea.church.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadsFragment extends Fragment {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    View view;

    public DownloadsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_downloads, container, false);
        init();

        // Change title on action bar.
        Objects.requireNonNull(getActivity()).setTitle("Downloads");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void init(){
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new DownloadedPreachings(), "AUDIOS");
        adapter.addFragment(new DownloadedVideo(), "VIDEOS");
        adapter.addFragment(new DownloadedMaterials(), "MATERIALS");

    }
}
