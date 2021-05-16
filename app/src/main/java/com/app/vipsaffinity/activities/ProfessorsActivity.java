package com.app.vipsaffinity.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.vipsaffinity.utils.Constants;
import com.app.vipsaffinity.utils.Helper;
import com.app.vipsaffinity.R;
import com.app.vipsaffinity.adapters.ProfessorAdapter;
import com.app.vipsaffinity.databinding.ActivityProfessorsBinding;
import com.app.vipsaffinity.interfaces.ProfessorListener;
import com.app.vipsaffinity.models.Professor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfessorsActivity extends AppCompatActivity implements ProfessorListener {
    //class variables
    ActivityProfessorsBinding binding;
    List<Professor> professors;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_professors);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorOlive));
        //creating loader
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        binding.recyclerViewProfessors.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        professors = new ArrayList<>();
        if (Helper.isConnected(this))
            fetchProfessors();
        else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            binding.noInternetPlaceHolder.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        }
    }

    private void fetchProfessors() {
        // Get a reference to our posts
        DatabaseReference ref = Helper.getFirebaseDB().getReference("professors");
        // Attach a listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.recyclerViewProfessors.setAdapter(new ProfessorAdapter(ProfessorsActivity.this, ProfessorsActivity.this, professors));
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfessorsActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                professors.add(snapshot.getValue(Professor.class));
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
                Toast.makeText(ProfessorsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void showProfessorDetailsActivity(int position) {
        Intent intent = new Intent(this, ProfessorDetailsActivity.class);
        intent.putExtra(Constants.PROFESSOR_DATA, professors.get(position));
        startActivity(intent);
    }
}