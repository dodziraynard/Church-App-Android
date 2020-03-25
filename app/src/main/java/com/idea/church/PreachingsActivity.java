package com.idea.church;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.idea.church.Adapters.AudioItemsAdapter;
import com.idea.church.Models.Audio;
import com.idea.church.R;

import java.util.ArrayList;

public class PreachingsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    // Debugging tag
    private static final String TAG = "HRD";

    private static final String STATE_AUDIOS = "AUDIOS";
    private ArrayList<Audio> audioList;
    Audio currentAudio;

    private MediaPlayerService player;
    private MediaPlayerReceiver receiver;
    boolean serviceBound = false;
    public static boolean serviceAudioPaused = false;
    public static final String Broadcast_PLAY_NEW_AUDIO = "PLAY";
    public static final String Broadcast_PAUSE_AUDIO = "PAUSE";
    public static final String Broadcast_RESUME_AUDIO = "RESUME";

    Button buttonPlay;
    Button buttonPause;
    TextView duration;
    TextView currentTitle;
    TextView elapsed;
    SeekBar seekBar;

    private void playAudio(String media) {
        StorageUtil storage = new StorageUtil(getApplicationContext());
        storage.storeMediaSource(media);

        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }

    private void loadAudio(String query) {
        if (query != null){
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }
        // Setting up the adapter
        this.audioList = new ArrayList<>();
        Log.i(TAG, "Sleeping");
        for (int i = 0; i < 10; i++) {
            String data = "https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg";
            String title = Integer.toString(i*100);;
            String desc = Integer.toString(i*100);;
            String artist = Integer.toString(i*100);;
            String image = Integer.toString(i*100);;

            // Save to audioList
            audioList.add(new Audio(data, title, desc, artist, image));
        }
    }

    public void renderAdapter(){
        // Setting up the recycler view
        RecyclerView recyclerView  = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AudioItemsAdapter audioItemsAdapter = new AudioItemsAdapter(this, audioList);

        audioItemsAdapter.setOnItemClickListener(new AudioItemsAdapter.OnItemClickListener(){

            @Override
            public void onCardClickListener(View itemView, int position) {
                currentAudio = audioList.get(position);
                currentTitle.setText(currentAudio.getTitle());
                playAudio(currentAudio.getData());
                buttonPause.setVisibility(View.VISIBLE);
            }

            @Override
            public void setOnItemDownloadListener(View buttonDownload, int position) {
                Toast.makeText(PreachingsActivity.this, "DOWNLOAD " + position, Toast.LENGTH_SHORT).show();
            }
        });

        // Attach adapter to the recycler view
        recyclerView.setAdapter(audioItemsAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preachings);

        //Receive data from MediaPlayerService
        receiver = new MediaPlayerReceiver();
        registerReceiver(receiver, new IntentFilter("MEDIA_PLAYER_INFO"));

        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Load saved audioList
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_AUDIOS)) {
                serviceBound = savedInstanceState.getBoolean("ServiceState");
            }
            if (savedInstanceState.containsKey(STATE_AUDIOS)){
                audioList = savedInstanceState.getParcelableArrayList(STATE_AUDIOS);
            }
        } else{
            // Load audioList
            loadAudio(null);
        }

        // Find views
        buttonPause = findViewById(R.id.btnPause);
        buttonPlay = findViewById(R.id.btnPlay);
        seekBar = findViewById(R.id.seekBar);
        duration = findViewById(R.id.duration);
        elapsed = findViewById(R.id.elapsed);
        currentTitle = findViewById(R.id.currentTitle);

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPlay.setVisibility(View.VISIBLE);
                buttonPause.setVisibility(View.GONE);
                Intent broadcastIntent = new Intent(Broadcast_PAUSE_AUDIO);
                sendBroadcast(broadcastIntent);
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPlay.setVisibility(View.GONE);
                buttonPause.setVisibility(View.VISIBLE);
                if(serviceAudioPaused){
                    Intent broadcastIntent = new Intent(Broadcast_RESUME_AUDIO);
                    sendBroadcast(broadcastIntent);
                } else if (currentAudio != null) {
                    playAudio(currentAudio.getData());
                }
            }
        });

        //Display items
        renderAdapter();
    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // Saving audioList in the state
        outState.putParcelableArrayList(STATE_AUDIOS, audioList);
        outState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        loadAudio(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);

            //service is active
            player.stopSelf();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    class MediaPlayerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("MEDIA_PLAYER_INFO"))
            {
                int dur = intent.getIntExtra("DURATION",0);
                int el = intent.getIntExtra("ELAPSED",0);
                String duText = dur / 60 +":"+ dur%60;
                String elText = el / 60 +":"+ el%60;
                Log.i("HRD", duText);
                duration.setText(duText);
                elapsed.setText(elText);
            }
        }

    }

}