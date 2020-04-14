package com.idea.church.fragments;

import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.idea.church.DB.DevotionDBHandler;
import com.idea.church.R;
import com.idea.church.commons.APIService;
import com.idea.church.commons.HeaderInterceptor;
import com.idea.church.models.Devotion;
import com.idea.church.models.DevotionResponse;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.idea.church.commons.Constants.BASE_URL;
import static com.idea.church.commons.Constants.BASE_URL_TEST;
import static com.idea.church.commons.HelperFunctions.isOnline;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevotionFragment extends Fragment {
    private TextView title;
    private TextView detail;
    private ImageView imageView;
    private ProgressBar progressBar;
    private View view;
    private Devotion devotion;

    public DevotionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_devotion, container, false);

        //Find views

        WebView browser = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        TextView offline = view.findViewById(R.id.offline);

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

        browser.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if(progress == 100)
                    progressBar.setVisibility(View.GONE);
            }
        });
        browser.loadUrl(BASE_URL+"/daily_devotion");
        return view;
    }
}
