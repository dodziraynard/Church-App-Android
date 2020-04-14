package com.idea.church.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.idea.church.R;
import com.idea.church.adapters.LeaderItemAdapter;
import com.idea.church.adapters.NotificationItemAdapter;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.models.NotificationResponse;
import com.idea.church.models.Notification;

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
public class NotificationsFragment extends Fragment {
    View view;
    ProgressBar progressBar;
    ArrayList<Notification> notifications;
    TextView infoText;
    private Retrofit retrofit;
    private RecyclerView recyclerView;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        infoText = view.findViewById(R.id.infoText);

        loadNotifications();
        return view;
    }

    private void loadNotifications(){
        progressBar.setVisibility(View.VISIBLE);
        infoText.setVisibility(View.GONE);
        notifications = new ArrayList<>();

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
        Call<NotificationResponse> call = apiService.getNotifications();
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    notifications = response.body().getResults();
                    //Display items
                    renderAdapter(view);
                } else{
                    notifications = new ArrayList<>();
                    renderAdapter(view);
                }
            }
            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable throwable) {
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
        if(notifications.size() == 0){
            infoText.setVisibility(View.VISIBLE);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Adapter
        NotificationItemAdapter notificationItemAdapter = new NotificationItemAdapter(getContext(), notifications);
        recyclerView.setAdapter(notificationItemAdapter);
    }
}
