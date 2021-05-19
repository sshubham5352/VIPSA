package com.app.vipsaffinity.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.ActivityIDCardModuleBinding;
import com.app.vipsaffinity.utils.OnSwipeTouchListener;

public class IDCardModuleActivity extends AppCompatActivity {
    //final Variables
    final int FRONT_FACE = 1, REAR_FACE = 0, ANIM_TIME = 400;
    //class variables
    int currentFaceSide = FRONT_FACE;
    float angleInOnePixel;
    Runnable mPositionRunnable;
    AnimatorListenerAdapter animatorSetAdapter;
    AnimatorSet animatorSet;
    float previousX, actionDownX;
    ActivityIDCardModuleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_i_d_card_module);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlueGrotto));

        //calculating the angle by which layout would move for move in every 1 pixel
        angleInOnePixel = Math.round((90 / (float) getResources().getDisplayMetrics().widthPixels + 0.007f) * 1000) / 1000f;
        //Runnable
        mPositionRunnable = new Runnable() {
            @Override
            public void run() {
                binding.containerIdCard.setRotationY(getAngleInRange(binding.containerIdCard.getRotationY()));
                adjustScalingAndIDCardFace();
                if (animatorSet.isRunning()) {
                    binding.containerIdCard.postDelayed(mPositionRunnable, 5);
                } else {
                    binding.containerIdCard.removeCallbacks(mPositionRunnable);
                    animatorSet.addListener(animatorSetAdapter);
                }
            }
        };
        //rotate Anim
        animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(binding.containerIdCard, "rotationY", 0, 0));

        animatorSetAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                adjustScalingAndIDCardFace();
            }
        };
        animatorSet.addListener(animatorSetAdapter);
//        rotateAnim =
//        rotateAnim.end();
        //setting onCLickListeners
        binding.idCardListenerLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                super.onTouch(view, event);
                if (animatorSet.isRunning())
                    return true;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        binding.containerIdCard.clearAnimation();
                        actionDownX = previousX = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        binding.containerIdCard.setRotationY(getAngleInRange(binding.containerIdCard.getRotationY()));
                        float changeInAngle = pixelsToAngle(previousX, event.getX());
                        animateView(changeInAngle + binding.containerIdCard.getRotationY(), 0);
                        previousX = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        break;
                    }
                }
                return true;
            }


            @Override
            public void onSwipeLeft(float diffX) {
                super.onSwipeLeft(diffX);
                animateView(binding.containerIdCard.getRotationY() + diffX / 4, (long) (ANIM_TIME + (diffX * -1) / 6));
                binding.containerIdCard.post(mPositionRunnable);
                animatorSet.removeAllListeners();
            }


            @Override
            public void onSwipeRight(float diffX) {
                super.onSwipeRight(diffX);
                animateView(binding.containerIdCard.getRotationY() + diffX / 4, (long) (ANIM_TIME + diffX / 6));
                binding.containerIdCard.post(mPositionRunnable);
                animatorSet.removeAllListeners();
            }

            @Override
            public void onLongClick() {
                super.onLongClick();
                binding.containerIdCard.post(mPositionRunnable);
                animateView(0, 800);
                animatorSet.removeAllListeners();
            }
        });
        setData();
    }


    private void animateView(float rotationAngle, long duration) {
        if ((rotationAngle > 88 && rotationAngle < 91) || (rotationAngle > 268 && rotationAngle < 271) ||
                (rotationAngle < -88 && rotationAngle > -91) || (rotationAngle < -268 && rotationAngle > -271)) {
            if (rotationAngle > binding.containerIdCard.getRotationY())
                rotationAngle += 4;
            else
                rotationAngle -= 4;
        }
        ((ObjectAnimator) animatorSet.getChildAnimations().get(0)).setFloatValues(binding.containerIdCard.getRotationY(), rotationAngle);
        if (duration != 0)
            animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(duration);
        animatorSet.start();
    }

    // return angle in range -360 to 360
    private float getAngleInRange(float angle) {
        boolean isNegative = false;
        if (angle < 0) {
            angle *= -1;
            isNegative = true;
        }
        while (angle >= 360)
            angle -= 360;

        if (isNegative)
            return (angle * -1);
        else
            return angle;
    }

    private float pixelsToAngle(float x1, float x2) {
        return (x2 - x1) * angleInOnePixel;
    }

    private void adjustScalingAndIDCardFace() {
        float currentAngle = binding.containerIdCard.getRotationY();
        float scale = 1;
        if (currentAngle == 0 || currentAngle == 180)
            scale = 1;
        else if ((currentAngle > -90 && currentAngle < 90) || (currentAngle > 180 && currentAngle < 270) || (currentAngle < -180 && currentAngle > -270)) {
            scale = 1 - getCurrentAngleUnder90() * 0.0045f;
        } else if ((currentAngle > 90 && currentAngle < 180) || (currentAngle > 270 && currentAngle < 360) || (currentAngle < -90 && currentAngle > -180) || (currentAngle < -270 && currentAngle > -360))
            scale = 0.595f + getCurrentAngleUnder90() * 0.0045f;

        if ((currentAngle > 90 && currentAngle < 270) || (currentAngle < -90 && currentAngle > -270))
            changeIDCardLayout(REAR_FACE);
        else
            changeIDCardLayout(FRONT_FACE);

        Log.d("sshubham", "currentAngle = " + currentAngle);
        binding.containerIdCard.setScaleY(scale);
        binding.containerIdCard.setScaleX(scale);
    }

    private float getCurrentAngleUnder90() {
        float angle = Math.abs(binding.containerIdCard.getRotationY());
        while (angle > 90)
            angle -= 90;
        return angle;
    }

    private void setData() {

    }


    private void changeIDCardLayout(int face) {
        Log.d("sshubham", "changeIDCardLayout: was called with face = " + face);
        if (face == currentFaceSide)
            return;

        switch (face) {
            case FRONT_FACE: {
                binding.frontFace.rootLayout.setVisibility(View.VISIBLE);
                binding.rearFace.rootLayout.setVisibility(View.GONE);
                break;
            }
            case REAR_FACE: {
                Log.d("sshubham", "face changed");
                binding.rearFace.rootLayout.setVisibility(View.VISIBLE);
                binding.rearFace.rootLayout.setScaleX(-1);
                binding.frontFace.rootLayout.setVisibility(View.GONE);
                break;
            }
        }
        currentFaceSide = face;
    }
}