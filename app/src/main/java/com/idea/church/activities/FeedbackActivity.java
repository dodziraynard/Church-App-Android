package com.idea.church.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.idea.church.R;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.models.FeedbackResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.BASE_URL;

public class FeedbackActivity extends AppCompatActivity {
    ProgressBar progressBar;
    private static Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle("Feedback");

        Button submitBtn = findViewById(R.id.submitBtn);
        progressBar = findViewById(R.id.progressBar);
        final EditText editText = findViewById(R.id.editText);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedBack = editText.getText().toString();
                if (!TextUtils.isEmpty(feedBack)){
                    //Submit

                    //Interceptor
                    HeaderInterceptor headerInterceptor = new HeaderInterceptor(FeedbackActivity.this);
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
                    progressBar.setVisibility(View.VISIBLE);
                    APIService apiService = retrofit.create(APIService.class);
                    Call<FeedbackResponse> call = apiService.postFeedback(feedBack);
                    call.enqueue(new Callback<FeedbackResponse>() {
                        @Override
                        public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.body() != null) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Thanks for your feedback", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
                                snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                                snackbar.show();
                                editText.setText("");
                            } else{
                                Toast.makeText(FeedbackActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FeedbackResponse> call, Throwable throwable) {
                            Log.i("HRD", throwable.getMessage());
                            progressBar.setVisibility(View.GONE);
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Unable to communicate with the server",
                                    Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(getResources().getColor(R.color.colorAccent));
                            snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
                            snackbar.show();
                        }
                    });
                }
            }
        });
    }
}
