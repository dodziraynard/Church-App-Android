package com.idea.church.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.idea.church.R;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.models.ProfileResponse;

import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.BASE_URL;

public class ProfileEditActivity extends AppCompatActivity {
    ProgressBar progressBar;
    Button submitBtn;
    TextInputEditText fullNameView;
    TextInputEditText emailView;
    TextInputEditText mobileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Intent editIntent = getIntent();

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle("Edit Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final String fullName = editIntent.getStringExtra("fullName");
        final String email = editIntent.getStringExtra("email");
        final String mobile = editIntent.getStringExtra("mobile");

        // Views
        submitBtn = findViewById(R.id.submitBtn);
        fullNameView = findViewById(R.id.fullName);
        emailView = findViewById(R.id.email);
        mobileView = findViewById(R.id.mobile);
        progressBar = findViewById(R.id.progressBar);

        fullNameView.setText(fullName);
        emailView.setText(email);
        mobileView.setText(mobile);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String fullName =  Objects.requireNonNull(fullNameView.getText()).toString();
                    String email =  Objects.requireNonNull(emailView.getText()).toString();
                    String mobile =  Objects.requireNonNull(mobileView.getText()).toString();

                    updateProfile(fullName, email, mobile);
                } catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void updateProfile(String fullName, String email, String mobile){
        progressBar.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.GONE);

        //Interceptor
        HeaderInterceptor headerInterceptor = new HeaderInterceptor(this);
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder().addInterceptor(headerInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);
        Call<ProfileResponse> call = apiService.updateProfile(email, fullName, mobile);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, final Response<ProfileResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.code() >= 200 && response.code() < 300){
                    onBackPressed();
                } else{
                    Toast toast = Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_IN);
                    toast.show();
                    submitBtn.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), "CAN'T ACCESS THE SERVER", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_IN);
                toast.show();
                progressBar.setVisibility(View.GONE);
                submitBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
