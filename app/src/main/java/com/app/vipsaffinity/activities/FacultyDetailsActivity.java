package com.app.vipsaffinity.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.databinding.DataBindingUtil;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.ActivityFacultyDetailsBinding;
import com.app.vipsaffinity.models.FacultyMember;
import com.app.vipsaffinity.utils.Constants;
import com.app.vipsaffinity.utils.Helper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class FacultyDetailsActivity extends AppCompatActivity {

    // class variables
    ActivityFacultyDetailsBinding binding;
    FacultyMember mFaculty;
    int themeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faculty_details);
        mFaculty = (FacultyMember) getIntent().getSerializableExtra(Constants.FACULTY_MEMBER_DATA);

        themeColor = Color.parseColor(mFaculty.getColor_theme());
        getWindow().setStatusBarColor(themeColor);
        setBtnBackListener();
        setAlphaAnimations();
        setData();
        changeTextColors();
        setGradientLayers(themeColor);
    }


    private void setBtnBackListener() {
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
            }
        });
    }

    private void setAlphaAnimations() {
        //setting fadeInAnimation
        binding.rootLayout.startAnimation(Helper.getAlphaAnimation(600));
        binding.btnHome.startAnimation(Helper.getAlphaAnimation(800));
        binding.imgFaculty.startAnimation(Helper.getAlphaAnimation(800));
    }

    private void setData() {
        //setting images via Glide
        Glide.with(this).setDefaultRequestOptions(new RequestOptions().
                placeholder(R.drawable.icon_my_faculty).error(R.drawable.icon_my_faculty))
                .load(Constants.FIREBASE_BASE_URL + mFaculty.getImage_url()).into(binding.imgFaculty);

        Glide.with(this).setDefaultRequestOptions(new RequestOptions().
                placeholder(R.color.colorGray).error(R.color.colorGray))
                .load(Constants.FIREBASE_BASE_URL + mFaculty.getBg_image())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.bgImg.startAnimation(Helper.getAlphaAnimation1000());
                        return false;
                    }
                }).into(binding.bgImg);

        //setting textColor
        binding.departmentFaculty.setTextColor(themeColor);
        binding.bioFaculty.setTextColor(themeColor);
        binding.proficiencyFaculty.setTextColor(themeColor);
        binding.experienceFaculty.setTextColor(themeColor);
        binding.phoneFaculty.setTextColor(themeColor);
        binding.gmailFaculty.setTextColor(themeColor);

        Helper.setText(mFaculty.getName(), binding.nameFaculty, true);
        Helper.setText(mFaculty.getDepartment_short(), binding.departmentShortFaculty, true);
        Helper.setText(mFaculty.getDepartment(), binding.departmentFaculty, true);
        Helper.setText(mFaculty.getBio(), binding.bioFaculty, true);
        Helper.setText(mFaculty.getProficiency(), binding.proficiencyFaculty, true);
        Helper.setText(mFaculty.getExperience(), binding.experienceFaculty, true);
        Helper.setText(String.valueOf(mFaculty.getPhone_number()), binding.phoneFaculty, true);
        Helper.setText(String.valueOf(mFaculty.getEmail_id()), binding.gmailFaculty, true);
    }

    private void changeTextColors() {
    }

    private void setGradientLayers(int backgroundLayersColor) {
        GradientDrawable gd1 = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{backgroundLayersColor,
                        ColorUtils.setAlphaComponent(backgroundLayersColor, 200),
                        ColorUtils.setAlphaComponent(backgroundLayersColor, 220)});
        GradientDrawable gd2 = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{backgroundLayersColor,
                        ColorUtils.setAlphaComponent(backgroundLayersColor, 1),
                        ColorUtils.setAlphaComponent(backgroundLayersColor, 1),
                        ColorUtils.setAlphaComponent(backgroundLayersColor, 1)});

        binding.gradientLayer1.setBackground(gd1);
        binding.gradientLayer2.setBackground(gd2);
    }
}