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

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "HRD";
    DashboardFragment dashboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Set dashboard on main screen
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dashboardFragment = new DashboardFragment();
        ft.replace(R.id.mainFrame, dashboardFragment);
        ft.commit();

        //Bottom Navigation item handling
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                dashboardFragment = new DashboardFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()){
                    case R.id.dashboard:
                        break;
                    default:
                        ft.replace(R.id.mainFrame, dashboardFragment);
                        ft.commit();
                        break;
                }
                return true;
            }
        });
    }

    // PERMISSIONS HANDLING
    private static final String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
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
