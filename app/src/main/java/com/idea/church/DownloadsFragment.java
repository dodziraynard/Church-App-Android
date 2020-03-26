package com.idea.church;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.idea.church.Adapters.TabAdapter;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadsFragment extends Fragment {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public DownloadsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloads, container, false);

        // Change title on action bar.
        Objects.requireNonNull(getActivity()).setTitle("Downloads");

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new DownloadedVideo(), "VIDEOS");
        adapter.addFragment(new DownloadedPreachings(), "PREACHINGS");
        adapter.addFragment(new DownloadedMaterials(), "MATERIALS");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
