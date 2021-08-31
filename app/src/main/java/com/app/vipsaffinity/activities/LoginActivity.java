package com.app.vipsaffinity.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.ActivityLoginBinding;
import com.app.vipsaffinity.models.Undergraduate;
import com.app.vipsaffinity.utils.Constants;
import com.app.vipsaffinity.utils.Helper;
import com.app.vipsaffinity.utils.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.app.vipsaffinity.utils.Constants.RC_GOOGLE_SIGNUP;

public class LoginActivity extends AppCompatActivity {
    //class Variables
    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    CollectionReference undergraduatesRef;
    GoogleSignInClient mGoogleSignInClient;
    Undergraduate user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        // this lines ensure only the status-bar to become transparent without affecting the nav-bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        //setting calender for DOB
        setCalender();
        //setting on click listener
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areFieldsValid();
            }
        });
        binding.loginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail().build());
        //getting collection reference
        undergraduatesRef = Helper.getFirestoreDB().collection(Constants.COLLECTION_UNDERGRADUATES);
    }

    private void setCalender() {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                binding.dob.setText(sdf.format(myCalendar.getTime()));
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(Helper.getDateInMillis(2005, 12, 31));
        datePickerDialog.getDatePicker().setMinDate(Helper.getDateInMillis(1995, 1, 1));
        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void areFieldsValid() {
        if (Helper.isFieldEmpty(binding.enrollmentNo) || Helper.isFieldEmpty(binding.dob))
            return;

        if (binding.enrollmentNo.getText().toString().contains(" ")) {
            binding.enrollmentNo.setError("Enrollment ID can't contain spaces");
            return;
        }

        if (binding.enrollmentNo.length() != 11) {
            binding.enrollmentNo.setError("Enter a valid enrollment no.");
            return;
        }
        progressDialog.show();
        doesUserExist(Long.parseLong(binding.enrollmentNo.getText().toString()), binding.dob.getText().toString());
    }

    private void loginWithGoogle() {
        progressDialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGNUP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGNUP) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                doesUserExist(account.getEmail());
            } catch (ApiException e) {
                if (e.getStatusCode() == 12501)
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                else if (e.getStatusCode() == 7)
                    Toast.makeText(this, "Internet not available", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "Something went wrong.Failure code : " + e.getStatusCode(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }

    private void doesUserExist(String gmailID) {
        undergraduatesRef.whereEqualTo(Constants.GMAIL_ID, gmailID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() == 0) {
                            binding.textViewMsg.setText(R.string.user_does_not_exist);
                            progressDialog.dismiss();
                            return;
                        }
                        user = queryDocumentSnapshots.getDocuments().get(0).toObject(Undergraduate.class);
                        loginUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Constants.LOGIN, "error : " + e.toString());
                        Toast.makeText(LoginActivity.this, "Something went wrong.\n Error : " + e.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void doesUserExist(long enrollment_no, final String dob) {
        undergraduatesRef.whereEqualTo(Constants.ENROLLMENT_NO, enrollment_no)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() == 0) {
                            binding.textViewMsg.setText(R.string.user_does_not_exist);
                            progressDialog.dismiss();
                            return;
                        }
                        user = queryDocumentSnapshots.getDocuments().get(0).toObject(Undergraduate.class);
                        Log.d("bella ciao", "-> " + user.getDob());
                        if (!user.getDob().matches(dob)) {
                            binding.textViewMsg.setText(R.string.dob_does_not_match);
                            progressDialog.dismiss();
                            return;
                        }
                        loginUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Constants.LOGIN, "error : " + e.toString());
                        Toast.makeText(LoginActivity.this, "Something went wrong.\n Error : " + e.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    public void loginUser() {
        SessionManager.createLoginSessionViaManualLogin(this, user);
        startMainActivity(user.getFirstName() + " " + user.getLastName());
    }

    private void startMainActivity(final String userName) {
        //hiding soft keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.enrollmentNo.getWindowToken(), 0);
        //starting MainActivity
        startActivity(new Intent(this, MainActivity.class).putExtra(Constants.KEY_GENDER, user.getGmailID()));
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        progressDialog.dismiss();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "Welcome " + userName, Toast.LENGTH_LONG).show();
                finishAffinity();
            }
        }, 1000);
    }
}
