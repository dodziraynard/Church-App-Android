package com.idea.church.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.idea.church.R;
import com.idea.church.commons.HelperFunctions;
import com.pusher.client.channel.User;

import static com.idea.church.commons.Constants.ACCOUNT_TYPE;
import static com.idea.church.commons.Constants.AUTH_TOKEN_TYPE;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle("User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final AccountManager accountManager = AccountManager.get(this);
        final Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);

        final Account account;
        account = accounts[0];

        Button profileBtn = findViewById(R.id.profile);
        Button testimony = findViewById(R.id.testimony);
        Button  prayerRequest = findViewById(R.id.prayerRequest);
        Button  tithe_donations = findViewById(R.id.tithe_donations);
        Button logout = findViewById(R.id.logout);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        testimony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(getBaseContext(), MessageActivity.class);
                messageIntent.putExtra("messageType", "Testimony");
                startActivity(messageIntent);
            }
        });

        prayerRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(getBaseContext(), MessageActivity.class);
                messageIntent.putExtra("messageType", "Prayer Request");
                startActivity(messageIntent);
            }
        });

        tithe_donations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transactionActivity = new Intent(getBaseContext(), TransactionActivity.class);
                startActivity(transactionActivity);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               accountManager.removeAccount(account, null, null);
               startActivity(new Intent(UserActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
