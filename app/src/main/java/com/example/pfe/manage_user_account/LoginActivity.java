package com.example.pfe.manage_user_account;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.HomepageActivity;
import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.appointments.Appointment;
import com.example.pfe.donations.Donation;
import com.example.pfe.manage_analyses.Analysis;
import com.example.pfe.manage_medicine.Medicine;
import com.example.pfe.manage_patient_account.Patient;
import com.example.pfe.manage_prescriptions.PresMedicine;
import com.example.pfe.manage_prescriptions.Prescription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmailLogin;
    private EditText edPasswordLogin;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    int success, number;
    String message;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(LoginActivity.this, HomepageActivity.class));
            return;
        }
        this.initView();

    }

    public void login(View view) {
        String email = edEmailLogin.getText().toString();
        String password = edPasswordLogin.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Invalid e-mail format!", Toast.LENGTH_LONG).show();
        } else {
            new LoginActivity.Log().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class Log extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            number = 0;
            HashMap<String, String> map = new HashMap<>();
            map.put("email", edEmailLogin.getText().toString());
            map.put("password", edPasswordLogin.getText().toString());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/user/log.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/user/log.php", "GET", map);
                success = object.getInt("success");
                message = object.getString("message");
                number = object.getInt("number_patients");
                while (success == 1) {
                    JSONArray userJson = object.getJSONArray("user");
                    JSONObject jsonObject = userJson.getJSONObject(0);
                    User user = new User(
                            jsonObject.getString("email"),
                            jsonObject.getString("name"),
                            jsonObject.getString("birthdate"),
                            jsonObject.getString("password"),
                            jsonObject.getString("adress"),
                            jsonObject.getString("number")
                    );
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                    if (number == 1) {
                        SharedPrefManager.getInstance(getApplicationContext()).setKeyNumberPatients(1);
                        JSONArray patientsJson = object.getJSONArray("patients");
                        JSONObject patientJson = patientsJson.getJSONObject(0);
                        Patient patient = new Patient(
                                patientJson.getString("patient_name"),
                                patientJson.getInt("patient_age"),
                                patientJson.getString("relationship")
                        );
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient1(patient);
                    } else if (number >= 2) {
                        SharedPrefManager.getInstance(getApplicationContext()).setKeyNumberPatients(2);
                        JSONArray patientsJson = object.getJSONArray("patients");
                        JSONObject patient1Json = patientsJson.getJSONObject(0);
                        Patient patient1 = new Patient(
                                patient1Json.getString("patient_name"),
                                patient1Json.getInt("patient_age"),
                                patient1Json.getString("relationship")
                        );
                        JSONObject patient2Json = patientsJson.getJSONObject(1);
                        Patient patient2 = new Patient(
                                patient2Json.getString("patient_name"),
                                patient2Json.getInt("patient_age"),
                                patient2Json.getString("relationship")
                        );
                        int number2 = object.getInt("number_medicine");
                        ArrayList<Medicine> medicineList = new ArrayList<>();
                        JSONArray medicineJson = object.getJSONArray("medicine");
                        for (int i = 0; i < number2; i++) {
                            JSONObject jsonObject2 = medicineJson.getJSONObject(i);
                            Medicine medicine = new Medicine(
                                    jsonObject2.getString("barcode"),
                                    jsonObject2.getString("med_name"),
                                    jsonObject2.getInt("quantity"),
                                    jsonObject2.getString("description"),
                                    jsonObject2.getString("exp_date")
                            );
                            medicineList.add(medicine);
                        }
                        int number3 = object.getInt("number_analyses");
                        ArrayList<Analysis> analysisArrayList = new ArrayList<>();
                        JSONArray analysisJson = object.getJSONArray("user_analyses");
                        for (int i = 0; i < number3; i++) {
                            JSONObject jsonObject2 = analysisJson.getJSONObject(i);
                            Analysis analysis = new Analysis(
                                    jsonObject2.getString("analysis_name"),
                                    jsonObject2.getString("analysis_date"),
                                    jsonObject2.getString("result"),
                                    jsonObject2.getString("owner")
                                    );
                            analysisArrayList.add(analysis);
                        }
                        int number4 = object.getInt("number_donations");
                        ArrayList<Donation> donations = new ArrayList<>();
                        JSONArray donationJson = object.getJSONArray("donations");
                        for (int i = 0; i < number4; i++) {
                            JSONObject jsonObject3 = donationJson.getJSONObject(i);
                            Donation donation = new Donation(
                                    jsonObject3.getString("id"),
                                    jsonObject3.getString("barcode"),
                                    Integer.parseInt(jsonObject3.getString("quantity")),
                                    jsonObject3.getString("date"));
                            donations.add(donation);
                        }

                        int number5 = object.getInt("number_apps");
                        ArrayList<Appointment> appointments = new ArrayList<>();
                        JSONArray appointmentJson = object.getJSONArray("appointments");
                        for (int i = 0; i < number5; i++) {
                            JSONObject jsonObject4 = appointmentJson.getJSONObject(i);
                            Appointment appointment = new Appointment(
                                    jsonObject4.getString("app_name"),
                                    jsonObject4.getString("app_date"),
                                    jsonObject4.getString("app_time"),
                                    jsonObject4.getString("app_type"),
                                    jsonObject4.getString("owner")
                            );
                            appointments.add(appointment);
                        }

                        int number6 = object.getInt("number_prescriptions");
                        ArrayList<Prescription> prescriptions = new ArrayList<>();
                        JSONArray prescriptionJson = object.getJSONArray("prescriptions");

                        HashSet<String> uniquePresIds = new HashSet<>();
                        HashSet<String> uniqueBarcodes = new HashSet<>();

                        for (int i = 0; i < number6; i++) {
                            JSONObject jsonObject4 = prescriptionJson.getJSONObject(i);
                            String presId = jsonObject4.getString("pres_name");
                            ArrayList<PresMedicine> presMedicines = new ArrayList<>();

                            for (int j = 0; j < number6; j++) {
                                JSONObject presMed = prescriptionJson.getJSONObject(j);
                                if (presMed.getString("pres_name").equals(presId)) {
                                    String barcode = presMed.getString("barcode");

                                    // Check if barcode has already been added
                                    if (uniqueBarcodes.contains(barcode)) {
                                        continue;  // Skip this PresMedicine object
                                    } else {
                                        uniqueBarcodes.add(barcode);  // Add new barcode to set
                                    }

                                    PresMedicine presMedecine = new PresMedicine(
                                            barcode,
                                            Integer.parseInt(presMed.getString("dose")),
                                            Integer.parseInt(presMed.getString("frequency")),
                                            Integer.parseInt(presMed.getString("period")),
                                            Integer.parseInt(presMed.getString("times_per_week")),
                                            presMed.getString("other_details")
                                    );
                                    presMedicines.add(presMedecine);
                                }
                            }

                            // Check if prescription ID has already been added
                            if (uniquePresIds.contains(presId)) {
                                continue;  // Skip this Prescription object
                            } else {
                                uniquePresIds.add(presId);  // Add new prescription ID to set
                            }

                            Prescription prescription = new Prescription(
                                    presId,
                                    jsonObject4.getString("start_date"),
                                    jsonObject4.getString("end_date"),
                                    jsonObject4.getString("owner"),
                                    presMedicines
                            );

                            prescriptions.add(prescription);
                        }




                        SharedPrefManager.savePrescriptionList(prescriptions);
                        SharedPrefManager.saveAppointmentList(appointments);
                        SharedPrefManager.saveDonationList(donations);
                        SharedPrefManager.saveAnalysisList(analysisArrayList);
                        SharedPrefManager.saveMedicineList(medicineList);
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient1(patient1);
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient2(patient2);
                    }
                    break;
                }
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();
            if (success == 1) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, HomepageActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                restartActivity(LoginActivity.this);
            }
        }

    }

    private void initView() {
        edEmailLogin = findViewById(R.id.edEmailLogin);
        edPasswordLogin = findViewById(R.id.edPasswordLogin);
    }

    public boolean isValidEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public void OpenSignupPage(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    public void OpenForgetPage(View view) {
        startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
    }

    public void OpenHomePage(View view) {
        startActivity(new Intent(LoginActivity.this, HomepageActivity.class));
    }

    public static void restartActivity(Activity activity) {
        activity.recreate();
    }

}
