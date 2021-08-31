package com.app.vipsaffinity.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.ActivityIDCardModuleBinding;
import com.app.vipsaffinity.models.Undergraduate;
import com.app.vipsaffinity.utils.Constants;
import com.app.vipsaffinity.utils.Helper;
import com.app.vipsaffinity.utils.OnSwipeTouchListener;
import com.app.vipsaffinity.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

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
    ProgressDialog progressDialog;
    CollectionReference undergraduatesRef;
    Undergraduate undergradData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_i_d_card_module);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlueGrotto));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //getting collection reference
        undergraduatesRef = Helper.getFirestoreDB().collection(Constants.COLLECTION_UNDERGRADUATES);

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

        if (SessionManager.getGender(this).matches("F"))
            binding.frontFace.imgStudent.setImageDrawable(getResources().getDrawable(R.drawable.img_girl));

        fetchData();
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

    private void fetchData() {
        progressDialog.setTitle("Generating Your ID-Card...");
        progressDialog.show();

        undergraduatesRef.whereEqualTo(Constants.ENROLLMENT_NO, SessionManager.getEnrollmentNo(this))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        undergradData = queryDocumentSnapshots.getDocuments().get(0).toObject(Undergraduate.class);
                        Log.d("bella ciao", "-> " + undergradData.getDob());
                        setData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Constants.LOGIN, "error : " + e.toString());
                        Toast.makeText(IDCardModuleActivity.this, "Something went wrong.\n Error : " + e.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    void setData() {
        binding.frontFace.departmentStudent.setText(getResources().getString(R.string.vsit));
        binding.frontFace.courseStudent.setText(getResources().getString(R.string.bca));
        binding.frontFace.semester.setText("2");
        binding.frontFace.hodSignature.setImageDrawable(getResources().getDrawable(R.drawable.img_hod_signature));
        //setting undergrad data
        if (undergradData.getProfilePic() != null && undergradData.getProfilePic().length() != 0)
            Glide.with(this).load(undergradData.getProfilePic()).into(binding.frontFace.imgStudent);
        binding.frontFace.nameStudent.setText(SessionManager.getFullName(this));
        binding.rearFace.enrollmentNo.setText(undergradData.getEnrollmentNo() + "");
        Helper.setText(undergradData.getFatherName(), binding.rearFace.fatherName, true);
        Helper.setText(undergradData.getDob(), binding.rearFace.dob, true);
        Helper.setText(undergradData.getPhoneNo() + "", binding.rearFace.phoneNo, true);
        binding.rearFace.residenceStudent.setText(undergradData.getResidence().getAddress());
        progressDialog.dismiss();
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