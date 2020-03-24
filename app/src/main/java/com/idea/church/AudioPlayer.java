//package com.idea.church;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.ComponentName;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.provider.MediaStore;
//import android.util.Log;
//
//import com.idea.church.Models.Audio;
//
//import java.util.ArrayList;
//
//public class AudioPlayer extends AppCompatActivity {
//    private MediaPlayerService player;
//    boolean serviceBound = false;
//    ArrayList<Audio> audioList;
//
//    public static final String Broadcast_PLAY_NEW_AUDIO = "com.idea.church";
//
//
//    private void playAudio(int audioIndex) {
//        //Check is service is active
//        if (!serviceBound) {
//            //Store Serializable audioList to SharedPreferences
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            storage.storeAudio(audioList);
//            storage.storeAudioIndex(audioIndex);
//
//            Intent playerIntent = new Intent(this, MediaPlayerService.class);
//            startService(playerIntent);
//            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        } else {
//            //Store the new audioIndex to SharedPreferences
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            storage.storeAudioIndex(audioIndex);
//
//            //Service is active
//            //Send a broadcast to the service -> PLAY_NEW_AUDIO
//            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
//            sendBroadcast(broadcastIntent);
//        }
//    }
//
//    private void loadAudio() {
//        ContentResolver contentResolver = getContentResolver();
//
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Log.i("MUSIC", uri.toString());
//
//        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
//
//        if (cursor != null && cursor.getCount() > 0) {
//            audioList = new ArrayList<>();
//            while (cursor.moveToNext()) {
//                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//
//                Log.i("MUSIC", "------------------------------");
//
//                Log.i("MUSIC", title);
//
//                Log.i("MUSIC", "------------------------------");
//                // Save to audioList
//                audioList.add(new Audio(data, title, album, artist));
//            }
//        }
//        cursor.close();
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_audio_player);
//
//        loadAudio();
//        //play the first audio in the ArrayList
//        playAudio(5);
//
////        playAudio("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");
//
//    }
//
//    //Binding this Client to the AudioPlayer Service
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
//            player = binder.getService();
//            serviceBound = true;
//
////            Toast.makeText(AudioPlayer.this, "Service Bound", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            serviceBound = false;
//        }
//    };
//
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putBoolean("ServiceState", serviceBound);
//        super.onSaveInstanceState(savedInstanceState);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        onRestoreInstanceState(savedInstanceState);
//        serviceBound = savedInstanceState.getBoolean("ServiceState");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (serviceBound) {
//            unbindService(serviceConnection);
//
//            //service is active
//            player.stopSelf();
//        }
//    }
//}
//
