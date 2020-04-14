package com.idea.church.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.idea.church.MainActivity;
import com.idea.church.adapters.VideoItemAdapter;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.commons.HelperFunctions;
import com.idea.church.fragments.OfflineFragment;
import com.idea.church.models.Video;
import com.idea.church.R;
import com.idea.church.models.VideoResponse;
import com.idea.church.services.DownloadService;

import java.io.File;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.BASE_URL;

public class VideoActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    // Debugging tag
    private static final String TAG = "HRD";
    private static Retrofit retrofit = null;
    private static final String VIDEO_STATE = "VIDEOS";
    public static final String FULL_VIDEO_DOWNLOAD_PATH = "/Church/Videos";
    private ArrayList<Video> videos;
    TextView infoText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle("Videos");

        progressBar = findViewById(R.id.progressBar);
        infoText = findViewById(R.id.infoText);

        if(savedInstanceState != null) {
            // Load saved audioList
            if (savedInstanceState.containsKey(VIDEO_STATE)) {
                videos = savedInstanceState.getParcelableArrayList(VIDEO_STATE);
                renderAdapter();
            }
        } else{
            loadVideo(null);
        }
    }

    void renderAdapter(){
        if (videos.size() == 0){
            infoText.setText(getString(R.string.no_videos_found));
            infoText.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VideoItemAdapter videoItemAdapter = new VideoItemAdapter(this, videos);
        videoItemAdapter.setOnItemClickListener(new VideoItemAdapter.OnItemClickListener() {
            @Override
            public void onDownloadClickListener(View itemView, int position) {
                final Video currentVideo = videos.get(position);
                String path = FULL_VIDEO_DOWNLOAD_PATH;

                final File folder = new File(path);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                final String fullPath = Environment.getExternalStorageDirectory()+path+"/"+currentVideo.getTitle();

                if(new File(fullPath).exists()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                    builder.setTitle("File already exists");
                    builder.setMessage("Do you want to open " +"'" + currentVideo.getTitle() + "'?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullPath));
                            videoIntent.setDataAndType(Uri.parse(fullPath), "video/*");
                            startActivity(videoIntent);
                        }
                    });
                    builder.setNegativeButton("Nope - Download Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startService(DownloadService.getDownloadService(VideoActivity.this,
                                    currentVideo.getData(),
                                    folder.getPath(),
                                    currentVideo.getTitle()));
                        }
                    });
                    builder.show();

                } else{
                    startService(DownloadService.getDownloadService(VideoActivity.this,
                            currentVideo.getData(),
                            folder.getPath(),
                            currentVideo.getTitle()));
                    Toast.makeText(getApplicationContext(), "DOWNLOAD STARTED", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onImageClickListener(View itemView, int position) {
                Video currentVideo = videos.get(position);
                Intent videoIntent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                videoIntent.putExtra("videoUri", currentVideo.getData());
                startActivity(videoIntent);
            }

        });

        recyclerView.setAdapter(videoItemAdapter);
    }

    private void loadVideo(String query){
        progressBar.setVisibility(View.VISIBLE);
        infoText.setVisibility(View.GONE);
        videos = new ArrayList<>();

        //Interceptor
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
        Call<VideoResponse> call = apiService.getVideos(query);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.body() != null) {
                    videos = response.body().getResults();
                    //Display items
                    progressBar.setVisibility(View.GONE);
                    renderAdapter();
                } else{
                    videos = new ArrayList<>();
                    renderAdapter();
                }
            }
            @Override
            public void onFailure(Call<VideoResponse> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(VIDEO_STATE, videos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        MenuItem download = menu.findItem(R.id.download);

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
        loadVideo(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
