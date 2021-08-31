package com.app.vipsaffinity.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.app.vipsaffinity.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class Helper {
    // class variables
    public static final FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
    public static final FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    public static final AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
    public static final Calendar calendar = Calendar.getInstance();
    public static GoogleSignInClient mGoogleSignInClient;

    static {
        alphaAnimation.setDuration(1000);
    }

    public static void setText(String text, TextView textView, boolean setNA) {
        if (text != null && text.length() != 0)
            textView.setText(text);
        else if (setNA)
            textView.setText(R.string.na);
    }

    public static FirebaseDatabase getFirebaseDB() {
        return firebaseDB;
    }

    public static FirebaseFirestore getFirestoreDB() {
        return firestoreDB;
    }

    public static AlphaAnimation getAlphaAnimation1000() {
        return alphaAnimation;
    }

    public static AlphaAnimation getAlphaAnimation(int millis) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(millis);
        return alphaAnimation;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static boolean isFieldEmpty(EditText field) {
        if (field.getText().toString().trim().length() == 0) {
            field.getText().clear();
            field.setError("Field can't be empty");
            return true;
        }
        return false;
    }

    public static long getDateInMillis(int y, int m, int d) {
        calendar.clear();
        calendar.set(y, m - 1, d); // date format : Year, Month - 1, Day
        return calendar.getTimeInMillis();
    }

    public static GoogleSignInClient getGoogleSignInClient(Activity activity) {
        if (mGoogleSignInClient == null)
            mGoogleSignInClient = GoogleSignIn.getClient(activity,
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail().build());
        return mGoogleSignInClient;
    }
}
