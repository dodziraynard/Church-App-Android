package com.idea.church.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.idea.church.R;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.fragments.OfflineFragment;
import com.idea.church.models.Profile;
import com.idea.church.models.ProfileResponse;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.BASE_URL;

public class ProfileActivity extends AppCompatActivity {
    Retrofit retrofit;
    Profile profile;
    ProgressBar progressBar;

    TextView fullName;
    TextView email;
    TextView mobile;
    TextView username;
    TextView church;
    ImageView imageView;
    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle("Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //views
        editBtn = findViewById(R.id.editBtn);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        church = findViewById(R.id.church);

        editBtn.setVisibility(View.GONE);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                editIntent.putExtra("fullName", fullName.getText().toString());
                editIntent.putExtra("email", email.getText().toString());
                editIntent.putExtra("mobile", mobile.getText().toString());
                startActivity(editIntent);
            }
        });
        
        // Getting user profile
        progressBar.setVisibility(View.VISIBLE);
        //Interceptor
        HeaderInterceptor headerInterceptor = new HeaderInterceptor(ProfileActivity.this);
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
        Call<ProfileResponse> call = apiService.getProfile();
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    profile = response.body().getResults();
                    //Display items
                    renderView();
                } else{
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                            "Unable load profile", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(getResources().getColor(R.color.colorError));
                    snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Unable load profile", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(getResources().getColor(R.color.colorError));
                snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                snackbar.show();
            }
        });
    }

    private void renderView() {
        progressBar.setVisibility(View.GONE);
        fullName.setText(profile.getFullName());
        email.setText(profile.getEmail());
        mobile.setText(profile.getMobile());
        username.setText(profile.getUsername());
        church.setText(profile.getChurch());
        editBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onRestart() {
        APIService apiService = retrofit.create(APIService.class);
        Call<ProfileResponse> call = apiService.getProfile();
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    profile = response.body().getResults();
                    //Display items
                    renderView();
                } else{
                    Toast.makeText(ProfileActivity.this, "Can't load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, "Can't load profile", Toast.LENGTH_SHORT).show();
            }
        });
        super.onRestart();
    }
}
