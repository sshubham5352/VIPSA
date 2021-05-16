package com.app.vipsaffinity.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.vipsaffinity.utils.Helper;
import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.ActivityAboutVSITBinding;
import com.app.vipsaffinity.models.Event;

import java.util.ArrayList;
import java.util.List;

public class AboutVSITActivity extends AppCompatActivity {
    //class variables
    ActivityAboutVSITBinding binding;
    ProgressDialog progressDialog;
    List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_v_s_i_t);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBrown));
        //creating loader
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        progressDialog.show();

        //       binding.recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        events = new ArrayList<>();
        if (Helper.isConnected(this)) {

            //fetchEvents();
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            binding.noInternetPlaceHolder.setVisibility(View.VISIBLE);
            binding.webView.setVisibility(View.GONE);
            progressDialog.dismiss();
        }

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setAppCacheEnabled(true);
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.loadUrl("https://vsit.vips.edu/about-vsit/#:~:text=VSIT%20offers%20the%20students%20to,Computer%20Application%20(MCA)%20programme.");
    }
}


