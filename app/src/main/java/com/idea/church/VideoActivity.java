package com.idea.church;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.idea.church.Adapters.VideoItemAdapter;
import com.idea.church.Models.Video;
import java.io.File;
import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    // Debugging tag
    private static final String TAG = "HRD";
    private static final String VIDEO_STATE = "VIDEOS";
    private ArrayList<Video> videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        videos = new ArrayList<>();

        if(savedInstanceState != null) {
            // Load saved audioList
            if (savedInstanceState.containsKey(VIDEO_STATE)) {
                videos = savedInstanceState.getParcelableArrayList(VIDEO_STATE);
            }
        } else{
            loadVideo(null);
        }

        renderAdapter();
    }


    public void renderAdapter(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        VideoItemAdapter videoItemAdapter = new VideoItemAdapter(this, videos);

        videoItemAdapter.setOnItemClickListener(new VideoItemAdapter.OnItemClickListener() {
            @Override
            public void onDownloadClickListener(View itemView, int position) {
                final Video currentVideo = videos.get(position);
                String path = "/Church/Videos";

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
                                                    currentVideo.getTitle()+"-2"));
                        }
                    });
                    builder.show();

                } else{
                    startService(DownloadService.getDownloadService(VideoActivity.this,
                                                    currentVideo.getData(),
                                                    folder.getPath(),
                                                    currentVideo.getTitle()));
                }
            }

            @Override
            public void onImageClickListener(View itemView, int position) {
                Video currentVideo = videos.get(position);
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentVideo.getData()));
                videoIntent.setDataAndType(Uri.parse(currentVideo.getData()), "video/*");
                startActivity(videoIntent);
            }

        });

        recyclerView.setAdapter(videoItemAdapter);
    }

    private void loadVideo(String query){
//        for (int i = 0; i < 10; i++) {
//            String data = Integer.toString(i*100);
//            String title = Integer.toString(i*100);
//            String desc = Integer.toString(i*100);;
//            String artist = Integer.toString(i*100);;
//            String image = Integer.toString(i*100);;
//
//            videos.add(new Video(data, title, desc, artist, image));
//        }

        File folder = new File(Environment.getExternalStorageDirectory() + "/Church");
        File [] files = folder.listFiles();

        for (int i = 0; i < files.length; i++) {
            Log.i("HRD", "Filename "+ files[i].getName());
        }

        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DURATION + ">= 0";
        String sortOrder = MediaStore.Video.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        videos.add(new Video("https://static.videezy.com/system/resources/previews/000/008/443/original/Dark_Haired_Girl_confused_-what--_2.mp4",
                "Dark_Haired_Girl_confused_-what--_2.mp4", "DES", "ART", "01/2/2010"));
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));

                // Save to videos
                videos.add(new Video(data, title, desc, artist, date));
            }

        }
        cursor.close();
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadVideo(query);
        Toast.makeText(this, "SUBMITEd", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
