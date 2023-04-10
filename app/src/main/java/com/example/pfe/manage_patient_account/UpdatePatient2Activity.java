package com.example.pfe.manage_patient_account;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class UpdatePatient2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btnUpdatePatient;
    EditText edName, edAge;
    Spinner edRelationship;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    int success, number;
    String relationship, message;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_update_patient2);
        initView();
        createSpinner();
        btnUpdatePatient.setOnClickListener(v -> updatePatient());
    }
    public void createSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.relationship, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edRelationship.setAdapter(adapter);
        edRelationship.setOnItemSelectedListener(this);
        edRelationship.setPrompt("Relationship");
    }

    void updatePatient() {
        String name = edName.getText().toString();
        String age = edAge.getText().toString();
        String relationship = edRelationship.toString();

        if (name.isEmpty() || age.isEmpty() || relationship.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
        } else {
            new UpdatePatient().execute();
        }
    }

    private void initView() {
        edName = findViewById(R.id.new_patient2_name);
        edAge = findViewById(R.id.new_patient2_age);
        edRelationship = findViewById(R.id.new_relationship2);
        btnUpdatePatient = findViewById(R.id.updatePatientBtn2);
        edName.setText(SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getName());
        String age = String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getAge());
        edAge.setText(age);
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
    class UpdatePatient extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UpdatePatient2Activity.this);
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
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/patient/updatePatient.php", "GET", map);
                // JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/patient/updatePatient.php", "GET", map);
                message = object.getString("message");
                success = object.getInt("success");
                while (success == 1) {
                    JSONArray patientsJson = object.getJSONArray("patient");
                    JSONObject patient2Json = patientsJson.getJSONObject(0);
                    Patient patient2 = new Patient(
                            patient2Json.getString("patient_name"),
                            patient2Json.getInt("patient_age"),
                            patient2Json.getString("relationship")
                    );
                    SharedPrefManager.getInstance(getApplicationContext()).addPatient2(patient2);
                    break;
                }
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
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
                Toast.makeText(UpdatePatient2Activity.this, "Patient updated successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdatePatient2Activity.this, MyPatientsActivity.class));

            } else {
                Toast.makeText(UpdatePatient2Activity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backMypatients(View view) {
        finish();
    }
}