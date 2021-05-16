package com.app.vipsaffinity.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.activities.AboutVSITActivity;
import com.app.vipsaffinity.activities.EventsActivity;
import com.app.vipsaffinity.activities.MyFacultyActivity;
import com.app.vipsaffinity.activities.ProfessorsActivity;
import com.app.vipsaffinity.adapters.ImageSliderAdapter;
import com.app.vipsaffinity.databinding.FragmentHomeBinding;
import com.app.vipsaffinity.models.CampusUpdate;
import com.app.vipsaffinity.utils.Helper;
import com.app.vipsaffinity.utils.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    //field declaration
    FragmentHomeBinding binding;
    List<CampusUpdate> campusUpdates;
    Context mContext;
    Activity mActivity;
    int numberOfCampusUpdates;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mContext != null)
            return;

        mContext = getContext();
        mActivity = getActivity();
        //setting onClickListener
        binding.containerMyFaculty.setOnClickListener(this);
        binding.containerProfessors.setOnClickListener(this);
        binding.containerVSIT.setOnClickListener(this);
        binding.containerEvents.setOnClickListener(this);
        //get campusImages
        binding.imageSlider.setVisibility(View.INVISIBLE);
        numberOfCampusUpdates = 0;
        campusUpdates = new ArrayList<>();
        //setting profile data
        if (SessionManager.getGender(mContext).matches(getString(R.string.male)))
            binding.imgProfile.setImageResource(R.drawable.img_boy);
        else
            binding.imgProfile.setImageResource(R.drawable.img_girl);
        binding.userName.setText(SessionManager.getFullName(mContext));
        binding.userCetRank.setText(String.valueOf(SessionManager.getRankCET(mContext)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (campusUpdates.size() != 0)
            return;
        if (Helper.isConnected(mContext)) {
            getCampusUpdates();
        } else {
            Toast.makeText(mContext, "No internet connection", Toast.LENGTH_LONG).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.noInternetPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    private void getCampusUpdates() {
        // Get a reference to our posts
        DatabaseReference ref = Helper.getFirebaseDB().getReference("campus_updates");
        // Attach a listener to read the data at our posts reference
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                campusUpdates.add(snapshot.getValue(CampusUpdate.class));
                numberOfCampusUpdates++;
                setImageSlider();
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
                Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setImageSlider() {
        if (campusUpdates.size() == numberOfCampusUpdates) {
            binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
            binding.imageSlider.setSliderTransformAnimation(SliderAnimations.CUBEOUTDEPTHTRANSFORMATION);
            binding.imageSlider.setSliderAdapter(new ImageSliderAdapter(mContext, campusUpdates));
            binding.imageSlider.startAutoCycle();
            binding.progressBar.setVisibility(View.GONE);
            binding.noInternetPlaceHolder.setVisibility(View.GONE);
            binding.imageSlider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.container_my_faculty)
            startActivity(new Intent(mActivity, MyFacultyActivity.class));
        else if (id == R.id.container_professors)
            startActivity(new Intent(mActivity, ProfessorsActivity.class));
        else if (id == R.id.container_VSIT)
            startActivity(new Intent(mActivity, AboutVSITActivity.class));
        else if (id == R.id.container_events)
            startActivity(new Intent(mActivity, EventsActivity.class));

        mActivity.overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }
}
