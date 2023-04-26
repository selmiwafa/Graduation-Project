package com.example.pfe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.pfe.appointments.Appointment;
import com.example.pfe.donations.Donation;
import com.example.pfe.manage_analyses.Analysis;
import com.example.pfe.manage_medicine.Medicine;
import com.example.pfe.manage_patient_account.Patient;
import com.example.pfe.manage_prescriptions.PresMedicine;
import com.example.pfe.manage_prescriptions.Prescription;
import com.example.pfe.manage_user_account.LoginActivity;
import com.example.pfe.manage_user_account.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "healthbuddypref";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_BIRTHDATE = "keybirthdate";
    private static final String KEY_ADRESS = "keyadress";
    private static final String KEY_NUMBER = "keynumber";
    private static final String KEY_PASSWORD = "keypassword";
    private static final String KEY_P1_NAME = "keyp1name";
    private static final String KEY_P1_AGE = "keyp1age";
    private static final String KEY_P1_RELATIONSHIP = "keyp1relationship";

    private static final String KEY_P2_NAME = "keyp2name";
    private static final String KEY_P2_AGE = "keyp2age";
    private static final String KEY_P2_RELATIONSHIP = "keyp2relationship";
    private static final int KEY_NUMBER_PATIENTS = 0;
    private static final String ANALYSIS_LIST_KEY = "analysisarray";
    private static final String DONATION_LIST_KEY = "donationarray";
    private static final String MEDICINE_LIST_KEY = "medicinearray";

    private static final String SUMMARY_LIST_KEY = "summaryarray";
    private static final String APPOINTMENT_LIST_KEY = "appointmentarray";
    private static final String PRES_ID = "presid",PRES_NAME = "presname",PRES_START = "start",PRES_END = "end";

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
    public static void saveSummaryList(ArrayList<PresMedicine> medicine){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(medicine);
        editor.putString(SUMMARY_LIST_KEY, json);
        editor.apply();
    }
    public ArrayList<PresMedicine> getSummaryList() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SUMMARY_LIST_KEY, "");
        Type type = new TypeToken<ArrayList<PresMedicine>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void deleteSummaryItem(String barcode) {
        ArrayList<PresMedicine> presMedicines = getSummaryList();
        for (int i = 0; i < presMedicines.size(); i++) {
            PresMedicine medicine = presMedicines.get(i);
            if (medicine.getBarcode().equals(barcode)) {
                presMedicines.remove(i);
                saveSummaryList(presMedicines);
                return;
            }
        }
    }
    public void deleteSummary() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SUMMARY_LIST_KEY);
        editor.apply();
    }
    public static void saveAppointmentList(ArrayList<Appointment> appointments) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(appointments);
        editor.putString(APPOINTMENT_LIST_KEY, json);
        editor.apply();
    }
    public void deleteAppointment(String id) {
        ArrayList<Appointment> appointments = getAppointmentList();
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getId().equals(id)) {
                appointments.remove(i);
                saveAppointmentList(appointments);
                return;
            }
        }
    }
    public ArrayList<Appointment> getAppointmentList() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(APPOINTMENT_LIST_KEY, "");
        Type type = new TypeToken<ArrayList<Appointment>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void setCurrentPres(String id, String name, String start, String end){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRES_ID, id);
        editor.putString(PRES_NAME, name);
        editor.putString(PRES_START, start);
        editor.putString(PRES_END, end);
        editor.apply();
    }
    public Prescription getCurrentPres () {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Prescription(
                sharedPreferences.getString(PRES_NAME, null),
                sharedPreferences.getString(PRES_START, null),
                sharedPreferences.getString(PRES_END, null));
    }
    public void deleteCurrentPres() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PRES_ID);
        editor.remove(PRES_NAME);
        editor.remove(PRES_START);
        editor.remove(PRES_END);
        editor.apply();
    }
    public static void saveAnalysisList(ArrayList<Analysis> analysis) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(analysis);
        editor.putString(ANALYSIS_LIST_KEY, json);
        editor.apply();
    }

    public ArrayList<Analysis> getAnalysisList() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(ANALYSIS_LIST_KEY, "");
        Type type = new TypeToken<ArrayList<Analysis>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void deleteAnalysis(String name) {
        ArrayList<Analysis> analysisList = getAnalysisList();
        for (int i = 0; i < analysisList.size(); i++) {
            Analysis analysis = analysisList.get(i);
            if (analysis.getAnalysis_name().equals(name)) {
                analysisList.remove(i);
                saveAnalysisList(analysisList);
                return;
            }
        }
    }

    public static void saveMedicineList(ArrayList<Medicine> medicine) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(medicine);
        editor.putString(MEDICINE_LIST_KEY, json);
        editor.apply();
    }
    public ArrayList<Donation> getDonationList() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(DONATION_LIST_KEY, "");
        Type type = new TypeToken<ArrayList<Donation>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void deleteDonation(String id) {
        ArrayList<Donation> donations = getDonationList();
        for (int i = 0; i < donations.size(); i++) {
            Donation donation = donations.get(i);
            if (donation.getId().equals(id)) {
                donations.remove(i);
                saveDonationList(donations);
                return;
            }
        }
    }
    public static void saveDonationList(ArrayList<Donation> donations) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(donations);
        editor.putString(DONATION_LIST_KEY, json);
        editor.apply();
    }
    public ArrayList<Medicine> getMedicineList() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(MEDICINE_LIST_KEY, "");
        Type type = new TypeToken<ArrayList<Medicine>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void deleteMedicine(String barcode) {
        ArrayList<Medicine> medicineList = getMedicineList();
        for (int i = 0; i < medicineList.size(); i++) {
            Medicine medicine = medicineList.get(i);
            if (medicine.getBarcode().equals(barcode)) {
                medicineList.remove(i);
                saveMedicineList(medicineList);
                return;
            }
        }
    }
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_BIRTHDATE, user.getBirthdate());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_ADRESS, user.getAdress());
        editor.putString(KEY_NUMBER, user.getNumber());
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

    public Patient getPatient2() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Patient(
                sharedPreferences.getString(KEY_P2_NAME, null),
                sharedPreferences.getInt(KEY_P2_AGE, 0),
                sharedPreferences.getString(KEY_P2_RELATIONSHIP, null)
        );
    }

    public void addPatient1(Patient patient) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_P1_NAME, patient.getName());
        editor.putInt(KEY_P1_AGE, patient.getAge());
        editor.putString(KEY_P1_RELATIONSHIP, patient.getRelationship());
        editor.apply();
    }

    public void addPatient2(Patient patient) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_P2_NAME, patient.getName());
        editor.putInt(KEY_P2_AGE, patient.getAge());
        editor.putString(KEY_P2_RELATIONSHIP, patient.getRelationship());
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
                sharedPreferences.getString(KEY_ADRESS, null),
                sharedPreferences.getString(KEY_NUMBER, null)
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