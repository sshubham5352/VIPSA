package com.app.vipsaffinity.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.vipsaffinity.utils.Constants;
import com.app.vipsaffinity.utils.Helper;
import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.ActivityProfessorDetailsBinding;
import com.app.vipsaffinity.models.Professor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ProfessorDetailsActivity extends AppCompatActivity {
    //class variables
    ActivityProfessorDetailsBinding binding;
    Professor mProfessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        // this lines ensure only the status-bar to become transparent without affecting the nav-bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_professor_details);
        mProfessor = (Professor) getIntent().getSerializableExtra(Constants.PROFESSOR_DATA);
        setAlphaAnimations();
        setData();
    }

    private void setAlphaAnimations() {
        //setting fadeInAnimation
        binding.rootLayout.startAnimation(Helper.getAlphaAnimation(600));
        binding.imgProfessor.startAnimation(Helper.getAlphaAnimation1000());
    }

    private void setData() {
        //setting images via Glide
        Glide.with(this).setDefaultRequestOptions(new RequestOptions().
                placeholder(R.drawable.icon_professor).error(R.drawable.icon_professor))
                .load(Constants.FIREBASE_BASE_URL + mProfessor.getImage_url()).into(binding.imgProfessor);

        Helper.setText(mProfessor.getName(), binding.nameProfessor, true);
        Helper.setText(mProfessor.getDepartment(), binding.departmentProfessor, true);
        Helper.setText(mProfessor.getBio(), binding.bioProfessor, true);
    }
}