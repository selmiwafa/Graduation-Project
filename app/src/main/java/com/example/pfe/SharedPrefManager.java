package com.example.pfe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "healthbuddypref";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_BIRTHDATE = "keybirthdate";
    private static final String KEY_ADRESS = "keyadress";
    private static final String KEY_PASSWORD = "keypassword";
    private static final String KEY_P1_NAME = "keyp1name";
    private static final String KEY_P1_AGE = "keyp1age";
    private static final String KEY_P1_RELATIONSHIP = "keyp1relationship";

    private static final String KEY_P2_NAME = "keyp2name";
    private static final String KEY_P2_AGE = "keyp2age";
    private static final String KEY_P2_RELATIONSHIP = "keyp2relationship";
    private static final int KEY_NUMBER_PATIENTS = 0;

    @SuppressLint("StaticFieldLeak")
    private static SharedPrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_BIRTHDATE, user.getBirthdate());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_ADRESS, user.getAdress());
        editor.apply();
    }

    public Patient getPatient1() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Patient(
                sharedPreferences.getString(KEY_P1_NAME, null),
                sharedPreferences.getInt(KEY_P1_AGE, 0),
                sharedPreferences.getString(KEY_P1_RELATIONSHIP, null)
        );
    }

    public void addPatient1(Patient patient) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_P1_NAME, patient.getName());
        editor.putInt(KEY_P1_AGE, patient.getAge());
        editor.putString(KEY_P1_RELATIONSHIP, patient.getRelationship());
        setKeyNumberPatients(1);
        editor.apply();
    }

    public void addPatient2(Patient patient) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_P2_NAME, patient.getName());
        editor.putInt(KEY_P2_AGE, patient.getAge());
        editor.putString(KEY_P2_RELATIONSHIP, patient.getRelationship());
        setKeyNumberPatients(2);
        editor.apply();
    }

    public void setKeyNumberPatients(int a) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(KEY_NUMBER_PATIENTS), a);
        editor.apply();
    }
    public int getKeyNumberPatients() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(String.valueOf(KEY_NUMBER_PATIENTS), 0);
    }
    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(KEY_EMAIL, null) != null);
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_BIRTHDATE, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_ADRESS, null)
        );
    }

    // this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}