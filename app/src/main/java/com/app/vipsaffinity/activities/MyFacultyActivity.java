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
import com.app.vipsaffinity.adapters.MyFacultyAdapter;
import com.app.vipsaffinity.databinding.ActivityMyFacultyBinding;
import com.app.vipsaffinity.interfaces.MyFacultyListener;
import com.app.vipsaffinity.models.FacultyMember;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyFacultyActivity extends AppCompatActivity implements MyFacultyListener {
    // class variables
    ActivityMyFacultyBinding binding;
    List<FacultyMember> facultyMembers;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_faculty);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorLightBlue));
        //creating loader
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        binding.recyclerViewMyFaculty.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        facultyMembers = new ArrayList<>();
        if (Helper.isConnected(this))
            fetchFacultyMembers();
        else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            binding.noInternetPlaceHolder.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        }
    }

    private void fetchFacultyMembers() {
        // Get a reference to our posts
        DatabaseReference ref = Helper.getFirebaseDB().getReference("faculty");
        // Attach a listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.recyclerViewMyFaculty.setAdapter(new MyFacultyAdapter(MyFacultyActivity.this, MyFacultyActivity.this, facultyMembers));
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyFacultyActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                facultyMembers.add(snapshot.getValue(FacultyMember.class));
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
                Toast.makeText(MyFacultyActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void showFacultyDetailsActivity(int position) {
        Intent intent = new Intent(this, FacultyDetailsActivity.class);
        intent.putExtra(Constants.FACULTY_MEMBER_DATA, facultyMembers.get(position));
        startActivity(intent);

    }
}