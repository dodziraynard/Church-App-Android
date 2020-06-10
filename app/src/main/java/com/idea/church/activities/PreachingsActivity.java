package com.idea.church.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.idea.church.MainActivity;
import com.idea.church.adapters.AudioItemsAdapter;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.fragments.DashboardFragment;
import com.idea.church.fragments.OfflineFragment;
import com.idea.church.models.Audio;
import com.idea.church.models.AudioResponse;
import com.idea.church.R;
import com.idea.church.commons.APIService;
import com.idea.church.services.DownloadService;
import com.idea.church.services.MediaPlayerService;
import com.idea.church.commons.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.ACCOUNT_TYPE;
import static com.idea.church.commons.Constants.API_KEY;
import static com.idea.church.commons.Constants.AUTH_TOKEN_TYPE;
import static com.idea.church.commons.Constants.BASE_URL;

public class PreachingsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "HRD";
    private static Retrofit retrofit = null;
    private static final String STATE_AUDIOS = "AUDIOS";
    public static final String FULL_PREACHING_DOWNLOAD_PATH = "/Church/Preaching";
    private ArrayList<Audio> audioList;
    Audio currentAudio;

    private MediaPlayerService player;
    private MediaPlayerReceiver receiver;
    boolean serviceBound = false;
    boolean receiverRegistered = false;
    public static boolean serviceAudioPaused = false;
    public static final String Broadcast_PLAY_NEW_AUDIO = "PLAY";
    public static final String Broadcast_PAUSE_AUDIO = "PAUSE";
    public static final String Broadcast_RESUME_AUDIO = "RESUME";
    public static final String Broadcast_SEEK_AUDIO = "Broadcast_SEEK_AUDIO";

    Button buttonPlay;
    Button buttonPause;
    TextView buttonDownload;
    TextView duration;
    TextView currentTitle;
    TextView elapsed;
    SeekBar seekBar;
    ProgressBar progressBar;
    ProgressBar progress_circular;
    RecyclerView recyclerView;
    String authToken;
    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preachings);

        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle("Audios");

        // Find views
        buttonPause = findViewById(R.id.btnPause);
        buttonPlay = findViewById(R.id.btnPlay);
        seekBar = findViewById(R.id.seekBar);
        progressBar = findViewById(R.id.loading);
        duration = findViewById(R.id.duration);
        elapsed = findViewById(R.id.elapsed);
        currentTitle = findViewById(R.id.currentTitle);
        progress_circular = findViewById(R.id.progress_circular);
        recyclerView  = findViewById(R.id.recyclerView);
        infoText = findViewById(R.id.infoText);

        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey("ServiceState")) {
                serviceBound = savedInstanceState.getBoolean("ServiceState");
            }

            if (savedInstanceState.containsKey("ReceiverState")) {
                receiverRegistered = savedInstanceState.getBoolean("ReceiverState");
            }

            // Load saved audioList
            if (savedInstanceState.containsKey(STATE_AUDIOS)){
                audioList = savedInstanceState.getParcelableArrayList(STATE_AUDIOS);
                renderAdapter();
            }
        } else{
            // Load audioList
            loadAudio(null);
        }

        if (!receiverRegistered){
            //Receive data from MediaPlayerService
            receiver = new MediaPlayerReceiver();
            registerReceiver(receiver, new IntentFilter("MEDIA_PLAYER_INFO"));
            receiverRegistered = true;
        }

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
                    playAudio(currentAudio);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    StorageUtil storage = new StorageUtil(getApplicationContext());
                    storage.storeSeekPosition(progress*1000);
                    Intent broadcastIntent = new Intent(Broadcast_SEEK_AUDIO);
                    sendBroadcast(broadcastIntent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void playAudio(Audio audio) {
        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        StorageUtil storage = new StorageUtil(getApplicationContext());
        storage.storeAudio(audio);

        //Check if service is active
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
        progress_circular.setVisibility(View.VISIBLE);
        infoText.setVisibility(View.GONE);
        audioList = new ArrayList<>();

//        Interceptor
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Account account = accounts[0];
            authToken = accountManager.peekAuthToken(account, AUTH_TOKEN_TYPE);
        }
        HeaderInterceptor headerInterceptor = new HeaderInterceptor(this);
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder().addInterceptor(headerInterceptor)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        APIService apiService = retrofit.create(APIService.class);
        Call<AudioResponse> call = apiService.getPreachings(query);
        call.enqueue(new Callback<AudioResponse>() {
            @Override
            public void onResponse(Call<AudioResponse> call, Response<AudioResponse> response) {
                if (response.body() != null) {
                    audioList = response.body().getResults();
                }
                //Display items
                renderAdapter();
                progress_circular.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<AudioResponse> call, Throwable throwable) {
                progress_circular.setVisibility(View.GONE);
                try{
                    findViewById(R.id.frameLayout).setVisibility(View.VISIBLE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new OfflineFragment());
                    ft.commit();
                } catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void renderAdapter(){
        if (audioList.size() == 0){
            infoText.setText(getString(R.string.no_preaching_found));
            infoText.setVisibility(View.VISIBLE);
        }

        // Setting up the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AudioItemsAdapter audioItemsAdapter = new AudioItemsAdapter(this, audioList);
        audioItemsAdapter.setOnItemClickListener(new AudioItemsAdapter.OnItemClickListener(){

            @Override
            public void onCardClickListener(View itemView, int position) {
                currentAudio = audioList.get(position);
                currentTitle.setText(currentAudio.getTitle());
                playAudio(currentAudio);
                buttonPause.setVisibility(View.VISIBLE);
            }

            @Override
            public void setOnItemDownloadListener(final View itemView, int position) {
                final Audio currentAudio = audioList.get(position);
                String path = FULL_PREACHING_DOWNLOAD_PATH;
                final File folder = new File(path);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                final String fullPath = Environment.getExternalStorageDirectory()+path+"/"+currentAudio.getTitle();

                if(new File(fullPath).exists()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PreachingsActivity.this);

                    builder.setTitle("File already exists");
                    builder.setMessage("Do you want to open " +"'" + currentAudio.getTitle() + "'?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(fullPath), "audio/*");
                            startActivity(intent);
                            buttonDownload = itemView.findViewById(R.id.btnDownload);
                            buttonDownload.setVisibility(View.VISIBLE);
                        }
                    });

                    builder.setNegativeButton("Nope - Download Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startService(DownloadService.getDownloadService(PreachingsActivity.this,
                                    currentAudio.getData(),
                                    folder.getPath(),
                                    currentAudio.getTitle()));
                            buttonDownload = itemView.findViewById(R.id.btnDownload);
                            buttonDownload.setVisibility(View.INVISIBLE);
                            Toast.makeText(PreachingsActivity.this, "Downloading", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();

                } else{
                    startService(DownloadService.getDownloadService(PreachingsActivity.this,
                            currentAudio.getData(),
                            folder.getPath(),
                            currentAudio.getTitle()));
                    buttonDownload = itemView.findViewById(R.id.btnDownload);
                    buttonDownload.setVisibility(View.INVISIBLE);
                    Toast.makeText(PreachingsActivity.this, "Downloading", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Attach adapter to the recycler view
        recyclerView.setAdapter(audioItemsAdapter);
    }

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
        outState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        MenuItem download = menu.findItem(R.id.download);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        download.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainActivityIntent.putExtra("fragment", R.id.downloads);
                startActivity(mainActivityIntent);
                return false;
            }
        });
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
        if (receiverRegistered){
            unregisterReceiver(receiver);
            receiverRegistered = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("MEDIA_PLAYER_INFO"));
        receiverRegistered = true;
    }

    class MediaPlayerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(Objects.equals(intent.getAction(), "MEDIA_PLAYER_INFO"))
            {
                int dur = intent.getIntExtra("DURATION",0);
                int el = intent.getIntExtra("ELAPSED",0);
                String title = intent.getStringExtra("title");

                String duText = dur / 60 +":"+ dur%60;
                String elText = el / 60 +":"+ el%60;
                duration.setText(duText);
                elapsed.setText(elText);

                currentTitle.setText(title);
                seekBar.setMax(dur);
                seekBar.setProgress(el);

                boolean prepared = intent.getBooleanExtra("LOADING_PERCENT", false);
                if(prepared){
                    progressBar.setVisibility(View.GONE);
                    buttonPause.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                }
            }
        }

    }
}