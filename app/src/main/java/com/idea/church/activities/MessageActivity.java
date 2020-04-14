package com.idea.church.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.idea.church.R;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.models.PrayerRequestResponse;
import com.idea.church.models.TestimonyResponse;

import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.BASE_URL;

public class MessageActivity extends AppCompatActivity {
    TextInputEditText message;
    Button submitBtn;
    String messageType;
    private Retrofit retrofit = null;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messageType = getIntent().getStringExtra("messageType");

        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle(messageType);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        message = findViewById(R.id.message);
        submitBtn = findViewById(R.id.submitBtn);
        progressBar = findViewById(R.id.progressBar);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = Objects.requireNonNull(message.getText()).toString();
                HeaderInterceptor headerInterceptor = new HeaderInterceptor(getApplicationContext());
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

                if (messageType.equals("Testimony")){
                    progressBar.setVisibility(View.VISIBLE);
                    APIService apiService = retrofit.create(APIService.class);
                    Call<TestimonyResponse> call = apiService.postTestimony(m);
                    call.enqueue(new Callback<TestimonyResponse>() {
                        @Override
                        public void onResponse(Call<TestimonyResponse> call, Response<TestimonyResponse> response) {
                            if(response.code() < 200 || response.code() >= 300){
                                progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                        "["+ response.code() +"] " +"Unable to submit testimony", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(getResources().getColor(R.color.colorError));
                                snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                                snackbar.show();
                            } else{
                                progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Testimony Submitted", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
                                snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                                snackbar.show();
                                message.setText("");
                            }
                        }
                        @Override
                        public void onFailure(Call<TestimonyResponse> call, Throwable throwable) {
                            progressBar.setVisibility(View.GONE);
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                    "Unable to communicate with the server", Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(getResources().getColor(R.color.colorError));
                            snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                            snackbar.show();

                        }
                    });
                } else if (messageType.equals("Prayer Request")){
                    progressBar.setVisibility(View.VISIBLE);
                    APIService apiService = retrofit.create(APIService.class);
                    Call<PrayerRequestResponse> call = apiService.postPrayerRequest(m);
                    call.enqueue(new Callback<PrayerRequestResponse>() {
                        @Override
                        public void onResponse(Call<PrayerRequestResponse> call, Response<PrayerRequestResponse> response) {
                            if(response.code() < 200 || response.code() >= 300){
                                progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Unable to submit request", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(getResources().getColor(R.color.colorAccent));
                                snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                                snackbar.show();
                            } else{
                                progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                        "["+ response.code() +"] " +"Unable to submit request", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(getResources().getColor(R.color.colorError));
                                snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                                snackbar.show();
                                message.setText("");
                            }
                        }
                        @Override
                        public void onFailure(Call<PrayerRequestResponse> call, Throwable throwable) {
                            progressBar.setVisibility(View.GONE);
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                    "Unable to communicate with the server", Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(getResources().getColor(R.color.colorError));
                            snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                            snackbar.show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
