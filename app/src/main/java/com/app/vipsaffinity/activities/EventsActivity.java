package com.app.vipsaffinity.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.vipsaffinity.utils.Constants;
import com.app.vipsaffinity.utils.Helper;
import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.ActivityEventsBinding;
import com.app.vipsaffinity.interfaces.EventListener;
import com.app.vipsaffinity.models.Event;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity implements EventListener {
    //class variables
    ActivityEventsBinding binding;
    ProgressDialog progressDialog;
    List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_events);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPurple));
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
        binding.webView.loadUrl("https://vsjmc.vips.edu/events/");
    }

    private void fetchEvents() {
        // Get a reference to our posts
        DatabaseReference ref = Helper.getFirebaseDB().getReference("events");
        // Attach a listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //     binding.recyclerViewEvents.setAdapter(new EventAdapter(EventsActivity.this, EventsActivity.this, events));
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventsActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                events.add(snapshot.getValue(Event.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void showEventDetailsActivity(int position) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra(Constants.EVENT_DATA, events.get(position));
        startActivity(intent);

    }
}


