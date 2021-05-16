package com.app.vipsaffinity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.ActivitySplashBinding;
import com.app.vipsaffinity.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    //global variables
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Hooks
        binding.icIcon.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_down_slide));
        binding.txtVips.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_right_slide));
        binding.txtAffinity.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_left_slide));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SessionManager.isLoggedIn(SplashActivity.this)) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2500);
    }

}