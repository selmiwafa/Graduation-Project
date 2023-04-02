package com.example.pfe.manage_patients;

import static com.example.pfe.LoginActivity.restartActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.JSONParser;
import com.example.pfe.MyPatientsActivity;
import com.example.pfe.Patient;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddPatientActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btnAddPatient;
    EditText edName, edAge;
    Spinner edRelationship;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    int success, number;
    String relationship, message;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    String user = "root";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_patient);
        initView();
        createSpinner();
        btnAddPatient.setOnClickListener(v -> addPatient());

    }

    public void createSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.relationship, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edRelationship.setAdapter(adapter);
        edRelationship.setOnItemSelectedListener(this);
        edRelationship.setPrompt("Relationship");
    }

    void addPatient() {
        String name = edName.getText().toString();
        String age = edAge.getText().toString();
        String relationship = edRelationship.toString();

        if (name.isEmpty() || age.isEmpty() || relationship.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
        } else {
            new AddPatient().execute();
        }
    }

    private void initView() {
        edName = findViewById(R.id.patient_name);
        edAge = findViewById(R.id.patient_age);
        edRelationship = findViewById(R.id.relationship);
        btnAddPatient = findViewById(R.id.addPatientBtn);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                break;
            case 1:
                relationship = "Daughter";
                break;
            case 2:
                relationship = "Son";
                break;
            case 3:
                relationship = "Father";
                break;
            case 4:
                relationship = "Mother";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("StaticFieldLeak")
    class AddPatient extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddPatientActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> map = new HashMap<>();
            map.put("patient_name", edName.getText().toString());
            map.put("relationship", relationship);
            map.put("patient_age", edAge.getText().toString());
            map.put("user", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/patient/addPatient.php", "GET", map);
                message = object.getString("message");
                success = object.getInt("success");
                while (success == 1) {
                    number = object.getInt("number");
                    SharedPrefManager.getInstance(getApplicationContext()).setKeyNumberPatients(number);
                    if (number == 1) {
                        JSONArray patientsJson = object.getJSONArray("patients");
                        JSONObject patientJson = patientsJson.getJSONObject(0);
                        Patient patient = new Patient(
                                patientJson.getString("patient_name"),
                                patientJson.getInt("patient_age"),
                                patientJson.getString("relationship")
                        );
                        ArrayList<Patient> Patients = new ArrayList<>();
                        Patients.add(patient);
                        SharedPrefManager.getInstance(getApplicationContext()).getUser().setArray(Patients);
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient1(patient);
                    } else if (number == 2) {
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
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient1(patient1);
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient2(patient2);
                    }

                    break;
                }
                connection.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if (success == 1) {
                Toast.makeText(AddPatientActivity.this, "Patient added successfully", Toast.LENGTH_LONG).show();
                restartActivity(AddPatientActivity.this);

            } else {
                Toast.makeText(AddPatientActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backMypatients(View view) {
        startActivity(new Intent(AddPatientActivity.this, MyPatientsActivity.class));
    }
}