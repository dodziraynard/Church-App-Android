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

public class RegisterActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextInputEditText passwordText;
    TextInputEditText usernameText;
    TextInputEditText mobile;
    TextInputEditText churchId;
    TextInputEditText fullName;
    TextInputEditText confPasswordText;
    Button submit;
    TextView logInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Views
        submit = findViewById(R.id.submitBtn);
        progressBar = findViewById(R.id.progressBar);
        passwordText = findViewById(R.id.password);
        confPasswordText = findViewById(R.id.confPassword);
        usernameText = findViewById(R.id.username);
        fullName = findViewById(R.id.fullName);
        mobile = findViewById(R.id.fullName);
        churchId = findViewById(R.id.churchId);

        logInText = findViewById(R.id.logInText);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setVisibility(View.GONE);
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String confPassword = confPasswordText.getText().toString();
                String fn = fullName.getText().toString();
                String ch = churchId.getText().toString();
                String m = mobile.getText().toString();

                if (confPassword.equals(password)){
                    register(username, password, fn, ch, m);
                } else{
                    Toast toast = Toast.makeText(getApplicationContext(), "PASSWORDS DO NOT MATCH", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_IN);
                    toast.show();
                    submit.setVisibility(View.VISIBLE);
                }
            }
        });

        logInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(loginIntent);
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

    public void register(final String username, final String password, String fullName, String churchId, String mobile){
        progressBar.setVisibility(View.VISIBLE);
        //Retrofit login or register to get auth token
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);
        Call<LoginResponse> call = apiService.register(username, password, churchId, fullName, mobile);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    String token = response.body().getToken();
                    String usernameError = response.body().getUsername();
                    if(token != null){
                        createAccount(username, password, token);
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        mainActivity.putExtra("fragment", R.id.devotion);
                        startActivity(mainActivity);
                    } else if(usernameError != null){
                        Toast toast = Toast.makeText(getApplicationContext(), usernameError, Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_IN);
                        toast.show();
                    }
                } else{
                    Toast toast = Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG);
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
