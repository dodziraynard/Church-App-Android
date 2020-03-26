package com.idea.church;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.Manifest;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "HRD";
    private static int CURRENT_FRAGMENT_ID;
    private static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

    DashboardFragment dashboardFragment;
    DownloadsFragment downloadsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null){
            if (savedInstanceState.containsKey(CURRENT_FRAGMENT)) {
                CURRENT_FRAGMENT_ID = savedInstanceState.getInt(CURRENT_FRAGMENT);
                navigateBottomNavigationBar(CURRENT_FRAGMENT_ID);
            }
        } else{
            navigateBottomNavigationBar(R.id.dashboard);
        }

        //Bottom Navigation item handling
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                CURRENT_FRAGMENT_ID = id;
                navigateBottomNavigationBar(id);
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT, CURRENT_FRAGMENT_ID);
        super.onSaveInstanceState(outState);
    }

    void navigateBottomNavigationBar(int id){
        dashboardFragment = new DashboardFragment();
        downloadsFragment = new DownloadsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (id){
            case R.id.downloads:
                ft.replace(R.id.mainFrame, downloadsFragment);
                ft.commit();
                break;
            default:
                ft.replace(R.id.mainFrame, dashboardFragment);
                ft.commit();
                break;
        }
    }




    // PERMISSIONS HANDLING
    private static final String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int REQUEST_PERMISSIONS_CODE = 14;
    private static final int PERMISSIONS_COUNT = PERMISSIONS.length;

    @SuppressLint("NewApi")
    private boolean permissionsDenied(){
        for (int i = 0; i < PERMISSIONS_COUNT; i++) {
            if(checkSelfPermission(PERMISSIONS[i])!= PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissionsDenied()){
            Toast.makeText(this, "Perm Denied", Toast.LENGTH_LONG).show();
            ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
            recreate();
        }
        else{
            onResume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsDenied()){
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS_CODE);
        }
    }
}
