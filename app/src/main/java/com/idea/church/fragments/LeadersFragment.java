package com.idea.church.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.idea.church.R;
import com.idea.church.adapters.LeaderItemAdapter;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.models.Leader;
import com.idea.church.models.LeaderResponse;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeadersFragment extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArrayList<Leader> leaders;
    private Retrofit retrofit;
    private View view;
    TextView infoText;

    public LeadersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leaders, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        infoText = view.findViewById(R.id.infoText);

        loadLeaders();
        return view;
    }

    private void loadLeaders(){
        progressBar.setVisibility(View.VISIBLE);
        infoText.setVisibility(View.GONE);
        leaders = new ArrayList<>();

        //Interceptor
        HeaderInterceptor headerInterceptor = new HeaderInterceptor(getContext());
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
        Call<LeaderResponse> call = apiService.getLeaders();
        call.enqueue(new Callback<LeaderResponse>() {
            @Override
            public void onResponse(Call<LeaderResponse> call, Response<LeaderResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    leaders = response.body().getResults();
                    //Display items
                    renderAdapter(view);
                } else{
                    leaders = new ArrayList<>();
                    renderAdapter(view);
                }
            }
            @Override
            public void onFailure(Call<LeaderResponse> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                try{
                    view.findViewById(R.id.frameLayout).setVisibility(View.VISIBLE);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new OfflineFragment());
                    ft.commit();
                } catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void renderAdapter(View view){
        if(leaders.size() == 0){
            infoText.setVisibility(View.VISIBLE);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Adapter
        LeaderItemAdapter leaderItemAdapter = new LeaderItemAdapter(leaders, getContext());
        leaderItemAdapter.setOnItemClickListener(new LeaderItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View itemView, int position) {
                TextView contact = itemView.findViewById(R.id.contact);
                if (!TextUtils.isEmpty(contact.getText())){
                    String dial = "tel:" + contact.getText();
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                } else{
                    Toast.makeText(getContext(), "Can't Call this number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCallClickListener(View itemView, int position) {
                TextView contact = itemView.findViewById(R.id.contact);
                if (!TextUtils.isEmpty(contact.getText())){
                    String dial = "tel:" + contact.getText();
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                } else{
                    Toast.makeText(getContext(), "Can't Call this number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(leaderItemAdapter);
    }
}
