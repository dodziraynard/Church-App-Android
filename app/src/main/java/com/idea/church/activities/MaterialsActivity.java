package com.idea.church.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.idea.church.MainActivity;
import com.idea.church.adapters.MaterialItemAdapter;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.fragments.OfflineFragment;
import com.idea.church.models.Material;
import com.idea.church.R;
import com.idea.church.models.MaterialResponse;
import com.idea.church.services.DownloadService;

import java.io.File;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.BASE_URL;

public class MaterialsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ArrayList<Material> materials;
    public static final String FULL_MATERIAL_DOWNLOAD_PATH = "/Church/Materials";
    private static Retrofit retrofit = null;
    ProgressBar progressBar;
    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials);

        progressBar = findViewById(R.id.progress_circular);
        infoText = findViewById(R.id.infoText);

        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle("Library");

        loadMaterials(null);
    }

    private void loadMaterials(String query){
        progressBar.setVisibility(View.VISIBLE);
        infoText.setVisibility(View.GONE);
        materials = new ArrayList<>();

        //Interceptor
        HeaderInterceptor headerInterceptor = new HeaderInterceptor(this);
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
        Call<MaterialResponse> call = apiService.getMaterials(query);
        call.enqueue(new Callback<MaterialResponse>() {
            @Override
            public void onResponse(Call<MaterialResponse> call, Response<MaterialResponse> response) {
                if (response.body() != null) {
                    materials = response.body().getResults();
                    //Display items
                    progressBar.setVisibility(View.GONE);
                    renderAdapter();
                } else{
                    materials = new ArrayList<>();
                    renderAdapter();
                }
            }
            @Override
            public void onFailure(Call<MaterialResponse> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                try{
                    findViewById(R.id.frameLayout).setVisibility(View.VISIBLE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new OfflineFragment());
                    ft.commit();
                } catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void renderAdapter() {
        if(materials.size() == 0){
            infoText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        RecyclerView recyclerView  = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MaterialItemAdapter materialItemAdapter =
                new MaterialItemAdapter(this, materials);


        materialItemAdapter.setOnItemClickListener(new MaterialItemAdapter.OnItemClickListener() {
            @Override
            public void setOnItemDownloadListener(View buttonDownload, int layoutPosition) {
                String path = FULL_MATERIAL_DOWNLOAD_PATH;
                Material material = materials.get(layoutPosition);

                final File folder = new File(path);
                if (!folder.exists()) {
                    folder.mkdir();
                }
                final String fullPath = Environment.getExternalStorageDirectory()+path+"/"+material.getTitle();
                if(!new File(fullPath).exists()){
                    startService(DownloadService.getDownloadService(MaterialsActivity.this,
                            material.getData(),
                            folder.getPath(),
                            material.getTitle()));
                    Toast.makeText(getApplicationContext(), "DOWNLOAD STARTED", Toast.LENGTH_LONG).show();
                } else {
                    Intent videoIntent = new Intent(getApplicationContext(), PDFViewerActivity.class);
                    videoIntent.putExtra("pdfUri", fullPath);
                    startActivity(videoIntent);
                }
            }

            @Override
            public void setOnViewPdfListener(View buttonDownload, int layoutPosition) {
                Material material = materials.get(layoutPosition);
                String path = FULL_MATERIAL_DOWNLOAD_PATH;
                final File folder = new File(path);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                final String fullPath = Environment.getExternalStorageDirectory()+path+"/"+material.getTitle();
                if(!new File(fullPath).exists()){
                    startService(DownloadService.getDownloadService(MaterialsActivity.this,
                            material.getData(),
                            folder.getPath(),
                            material.getTitle()));
                    Toast.makeText(getApplicationContext(), "DOWNLOAD STARTED", Toast.LENGTH_LONG).show();

                } else {
                    Intent videoIntent = new Intent(getApplicationContext(), PDFViewerActivity.class);
                    videoIntent.putExtra("pdfUri", fullPath);
                    startActivity(videoIntent);
                }

            }
        });

        recyclerView.setAdapter(materialItemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        MenuItem download = menu.findItem(R.id.download);
        download.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainActivityIntent.putExtra("fragment", R.id.downloads);
                startActivity(mainActivityIntent);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadMaterials(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
