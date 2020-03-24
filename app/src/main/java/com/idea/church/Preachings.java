//package com.idea.church;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.provider.MediaStore;
//import android.transition.ArcMotion;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//import com.idea.church.Adapters.AudioItemsAdapter;
//import com.idea.church.Models.Audio;
//
//import java.lang.reflect.Array;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class Preachings extends Fragment {
//    // Debugging tag
//    private static final String TAG = "HRD";
//
//    private static final String STATE_AUDIOS = "AUDIOS";
//    private ArrayList<Audio> audios;
//    private String query;
//
//    public Preachings() {
//        // Required empty public constructor
//    }
//    private void loadAudio() {
//        // Setting up the adapter
//        audios = new ArrayList<>();
//        Log.i(TAG, "Sleeping");
//        for (int i = 0; i < 10; i++) {
//            String data = Integer.toString(i*100);
//            String title = Integer.toString(i*100);;
//            String desc = Integer.toString(i*100);;
//            String artist = Integer.toString(i*100);;
//            String image = Integer.toString(i*100);;
//
//            // Save to audioList
//            audios.add(new Audio(data, title, desc, artist, image));
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        // Saving audios in the state
//        outState.putParcelableArrayList(STATE_AUDIOS, audios);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Get the state of the application if any
//        Log.i(TAG, "CREATED AUDIO");
//        if(savedInstanceState != null) {
//            if (savedInstanceState.containsKey(STATE_AUDIOS)){
//                Log.i(TAG, "RETRIVED AUDIO");
//                audios = savedInstanceState.getParcelableArrayList(STATE_AUDIOS);
//            }
//        } else if (audios == null){
//            // Load audios
//            loadAudio();
//        }
//
//        View view = inflater.inflate(R.layout.fragment_preachings, container, false);
//
//        // Setting up the recycler view
//        RecyclerView recyclerView  = view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        AudioItemsAdapter audioItemsAdapter = new AudioItemsAdapter(getContext(), audios);
//
//        audioItemsAdapter.setOnItemClickListener(new AudioItemsAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClickListener(View itemView, int position) {
//                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Attach adapter to the recycler view
//        recyclerView.setAdapter(audioItemsAdapter);
//
//        // Inflate the layout for this fragment
//        return view;
//    }
//}
