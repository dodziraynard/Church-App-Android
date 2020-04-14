package com.idea.church;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.idea.church.activities.LoginActivity;
import com.idea.church.activities.RegisterActivity;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.fragments.DashboardFragment;
import com.idea.church.fragments.DevotionFragment;
import com.idea.church.fragments.DownloadsFragment;
import com.idea.church.fragments.LeadersFragment;
import com.idea.church.fragments.NotificationsFragment;
import com.idea.church.models.DevotionResponse;
import com.idea.church.models.LoginResponse;
import com.pusher.pushnotifications.PushNotifications;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.ACCOUNT_TYPE;
import static com.idea.church.commons.Constants.BASE_URL;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "HRD";
    private static int CURRENT_FRAGMENT_ID = 0;
    private static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

    DashboardFragment dashboardFragment;
    DownloadsFragment downloadsFragment;
    DevotionFragment devotionFragment;
    NotificationsFragment notificationFragment;
    LeadersFragment leadersFragment;
    ProgressBar progressBar;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Views
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        progressBar = findViewById(R.id.progressBar);
        FirebaseMessaging.getInstance().subscribeToTopic("notifications");

        PushNotifications.start(getApplicationContext(), "e07bd2ab-31f4-4600-b28d-546ec7d29fa2");
        PushNotifications.addDeviceInterest("notifications");

        //Bottom Navigation item handling
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigateBottomNavigationBar(item.getItemId());
                return true;
            }
        });
        // Authenticating user
        final AccountManager accountManager = AccountManager.get(this);
        final Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length<1){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else if(getIntent().getIntExtra("fragment", 0) == 0) {
            progressBar.setVisibility(View.VISIBLE);
            HeaderInterceptor headerInterceptor = new HeaderInterceptor(getApplicationContext());
            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder().addInterceptor(headerInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            APIService apiService = retrofit.create(APIService.class);
            Call<DevotionResponse> call = apiService.getDevotions();
            call.enqueue(new Callback<DevotionResponse>() {
                @Override
                public void onResponse(Call<DevotionResponse> call, final Response<DevotionResponse> response) {
                    if (response.code() == 401){
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        APIService apiService = retrofit.create(APIService.class);
                        Call<LoginResponse> callLogin = apiService.logIn(accounts[0].name, accountManager.getPassword(accounts[0]));
                        callLogin.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                                progressBar.setVisibility(View.GONE);
                                if(response.body() != null){
                                    String token = response.body().getToken();
                                    accountManager.addAccountExplicitly(accounts[0], accountManager.getPassword(accounts[0]), null);
                                    accountManager.setAuthToken(accounts[0], "full_access", token);
                                    displayHomeFragment(savedInstanceState);
                                } else{
                                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                                }
                            }
                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                                progressBar.setVisibility(View.GONE);
                                displayHomeFragment(savedInstanceState);
                            }
                        });
                    } else{
                        progressBar.setVisibility(View.GONE);
                        displayHomeFragment(savedInstanceState);
                    }
                }
                @Override
                public void onFailure(Call<DevotionResponse> call, Throwable throwable) {
                    progressBar.setVisibility(View.GONE);
                    displayHomeFragment(savedInstanceState);
                }
            });
        } else{
            displayHomeFragment(savedInstanceState);
        }
    }

    private void displayHomeFragment(Bundle savedInstanceState){
        int fragment = getIntent().getIntExtra("fragment", 0);
        if(fragment != 0){
            navigateBottomNavigationBar(fragment);
            bottomNavigationView.setSelectedItemId(fragment);
        } else {
            // Remembering the current fragment on activity restart
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(CURRENT_FRAGMENT)) {
                    CURRENT_FRAGMENT_ID = savedInstanceState.getInt(CURRENT_FRAGMENT);
                    navigateBottomNavigationBar(CURRENT_FRAGMENT_ID);
                }
            } else {
                navigateBottomNavigationBar(R.id.devotion);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT, CURRENT_FRAGMENT_ID);
        super.onSaveInstanceState(outState);
    }

    void navigateBottomNavigationBar(int id){
        dashboardFragment = new DashboardFragment();
        downloadsFragment = new DownloadsFragment();
        devotionFragment = new DevotionFragment();
        notificationFragment = new NotificationsFragment();
        leadersFragment = new LeadersFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        CURRENT_FRAGMENT_ID = id;
        switch (id){
            case R.id.downloads:
                ft.replace(R.id.mainFrame, downloadsFragment);
                ft.commitAllowingStateLoss();
                break;
            case R.id.dashboard:
                ft.replace(R.id.mainFrame, dashboardFragment);
                ft.commitAllowingStateLoss();
                break;
            case R.id.notifications:
                ft.replace(R.id.mainFrame, notificationFragment);
                ft.commitAllowingStateLoss();
                break;
            case R.id.leadership:
                ft.replace(R.id.mainFrame, leadersFragment);
                ft.commitAllowingStateLoss();
                break;
            default:
                ft.replace(R.id.mainFrame, devotionFragment);
                ft.commitAllowingStateLoss();
                break;
        }
    }

    // PERMISSIONS HANDLING
    private static final String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CALL_PHONE
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
