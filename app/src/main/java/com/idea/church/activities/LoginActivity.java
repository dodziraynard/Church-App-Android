package com.idea.church.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.idea.church.MainActivity;
import com.idea.church.R;
import com.idea.church.commons.APIService;
import com.idea.church.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.ACCOUNT_TYPE;
import static com.idea.church.commons.Constants.BASE_URL;


public class LoginActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextInputEditText passwordText;
    TextInputEditText usernameText;
    Button submit;
    TextView registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Views
        submit = findViewById(R.id.submitBtn);
        registerText = findViewById(R.id.registerText);
        progressBar = findViewById(R.id.progressBar);
        passwordText = findViewById(R.id.password);
        usernameText = findViewById(R.id.username);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setVisibility(View.GONE);
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                logIn(username, password);
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    public void createAccount(String username, String password, String authToken) {
        AccountManager accountManager = AccountManager.get(this);
        Account account = new Account(username, ACCOUNT_TYPE);
        if(accountManager.getAccountsByType(ACCOUNT_TYPE).length > 0){
            Toast.makeText(this, "Accounts added already", Toast.LENGTH_SHORT).show();
            finish();
        } else{
            accountManager.addAccountExplicitly(account, password, null);
            accountManager.setAuthToken(account, "full_access", authToken);
        }
    }

    public void logIn(final String username, final String password){
        progressBar.setVisibility(View.VISIBLE);
        //Retrofit login or register to get auth token
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);
        Call<LoginResponse> call = apiService.logIn(username, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    String token = response.body().getToken();
                    createAccount(username, password, token);
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    mainActivity.putExtra("fragment", R.id.devotion);
                    startActivity(mainActivity);
                } else{
                    Toast toast = Toast.makeText(getApplicationContext(), "INCORRECT CREDENTIALS", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_IN);
                    toast.show();
                    submit.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), "CAN'T ACCESS THE SERVER", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_IN);
                toast.show();
                progressBar.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
            }
        });
    }
}
