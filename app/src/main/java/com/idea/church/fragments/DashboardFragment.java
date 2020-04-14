package com.idea.church.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.idea.church.activities.FeedbackActivity;
import com.idea.church.activities.HelpActivity;
import com.idea.church.activities.LoginActivity;
import com.idea.church.activities.MaterialsActivity;
import com.idea.church.activities.PreachingsActivity;
import com.idea.church.R;
import com.idea.church.activities.UserActivity;
import com.idea.church.activities.VideoActivity;

import static com.idea.church.commons.Constants.ACCOUNT_TYPE;
import static com.idea.church.commons.Constants.BASE_URL;


public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ImageButton preachingBtn;
        ImageButton materialsBtn;
        ImageButton videoBtn;
        ImageButton adminBtn;
        ImageButton meBtn;
        ImageButton helpBtn;
        Button feedBackBtn;

        preachingBtn  = view.findViewById(R.id.preachingBtn);
        materialsBtn  = view.findViewById(R.id.materialsBtn);
        videoBtn  = view.findViewById(R.id.videoBtn);
        adminBtn  = view.findViewById(R.id.adminBtn);
        meBtn     = view.findViewById(R.id.meBtn);
        helpBtn     = view.findViewById(R.id.helpBtn);
        feedBackBtn     = view.findViewById(R.id.feedBackBtn);


        materialsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preachingIntent  = new Intent(getContext(), MaterialsActivity.class);
                startActivity(preachingIntent);
            }
        });

        preachingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preachingIntent = new Intent(getContext(), PreachingsActivity.class);
                startActivity(preachingIntent);
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(getContext(), VideoActivity.class);
                startActivity(videoIntent);
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(BASE_URL));
                startActivity(browserIntent);
            }
        });

        meBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountManager accountManager = AccountManager.get(getContext());
                Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);

                if (accounts.length > 0){
                    Intent userIntent  = new Intent(getContext(), UserActivity.class);
                    startActivity(userIntent);
                } else{
                    Intent accountsIntent = new Intent(getContext(), LoginActivity.class);
                    startActivity(accountsIntent);
                }
            }
        });

        feedBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedBackIntent  = new Intent(getContext(), FeedbackActivity.class);
                startActivity(feedBackIntent);
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent helpIntent = new Intent(getContext(), HelpActivity.class);
                startActivity(helpIntent);
            }
        });
        return view;
    }

}
