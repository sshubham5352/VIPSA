package com.app.vipsaffinity.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.activities.LoginActivity;
import com.app.vipsaffinity.models.Undergraduate;

public class SessionManager {
    //class Variables
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String PREFERENCE_NAME = "com.app.vipsaffinity.undergraduate";

    // Constructor
    private SessionManager() {
        //empty private constructor to make it a singleton class
    }

    private static void getSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public static void createLoginSessionViaManualLogin(Context context, Undergraduate user) {
        if (sharedPreferences == null)
            getSharedPreferences(context);

        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.putLong(Constants.KEY_ENROLLMENT_NO, user.getEnrollmentNo());
        editor.putInt(Constants.KEY_ROLL_NO, user.getRollNo());
        editor.putInt(Constants.KEY_RANK_CET, user.getRankCET());
        editor.putString(Constants.KEY_FIRST_NAME, user.getFirstName());
        editor.putString(Constants.KEY_LAST_NAME, user.getLastName());
        editor.putString(Constants.KEY_DOB, user.getDob());
        editor.putString(Constants.KEY_GENDER, user.getGender());
        editor.putString(Constants.KEY_GMAIL_ID, user.getGmailID());
        editor.putString(Constants.KEY_HOUSE_NO, user.getResidence().getHouseNo());
        editor.putString(Constants.KEY_AREA, user.getResidence().getArea());
        editor.putString(Constants.KEY_CITY, user.getResidence().getCity());
        editor.putString(Constants.KEY_DISTRICT, user.getResidence().getDistrict());
        editor.putString(Constants.KEY_STATE, user.getResidence().getState());
        editor.putInt(Constants.KEY_PIN_CODE, user.getResidence().getPinCode());
        // commit changes
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public static long getEnrollmentNo(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getLong(Constants.KEY_ENROLLMENT_NO, 0);
    }

    public static int getRollNo(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getInt(Constants.KEY_ROLL_NO, 0);
    }

    public static int getRankCET(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getInt(Constants.KEY_RANK_CET, 0);
    }

    public static String getFirstName(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getString(Constants.KEY_FIRST_NAME, context.getString(R.string.na));
    }

    public static String getFullName(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getString(Constants.KEY_FIRST_NAME, context.getString(R.string.na)) + " " +
                sharedPreferences.getString(Constants.KEY_LAST_NAME, "");
    }


    public static String getLastName(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getString(Constants.KEY_LAST_NAME, "");
    }

    public static String getDOB(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getString(Constants.KEY_DOB, "");
    }

    public static String getGender(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getString(Constants.KEY_GENDER, "");
    }

    public static String getGmailID(Context context) {
        if (sharedPreferences == null)
            getSharedPreferences(context);
        return sharedPreferences.getString(Constants.KEY_GMAIL_ID, "");
    }


    // Clear session details
    public static void logoutUser(Context context) {
        // Clearing all data from Shared Preferences
        if (sharedPreferences == null)
            getSharedPreferences(context);
        try {
            editor.clear();
            editor.commit();
            redirectToSignInActivity(context);
        } catch (Exception e) {
            Toast.makeText(context, "something went wrong.", Toast.LENGTH_SHORT).show();
            Log.d("errors", "error : " + e.toString());
        }
    }

    private static void redirectToSignInActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        context.startActivity(intent);
    }
}
