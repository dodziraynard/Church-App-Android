package com.idea.church.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.idea.church.R;

import java.io.File;

public class PDFViewerActivity extends AppCompatActivity {
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        String path = getIntent().getStringExtra("pdfUri");
        uri = Uri.parse(path);
        File file = new File(path);

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromFile(file)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        page++;
                        Toast.makeText(getApplicationContext(), page+"/"+pageCount, Toast.LENGTH_LONG).show();
                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        onErrorListener();
                        Log.i("HRD",  t.getMessage());
                    }
                })
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        onPageErrorListener();
                    }
                })
                .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load();
    }

    private  void onErrorListener(){
        Toast.makeText(this, "Can't load PDF", Toast.LENGTH_SHORT).show();
    }

    private  void onPageErrorListener(){
        Toast.makeText(this, "Page Error", Toast.LENGTH_SHORT).show();
    }
}
