package com.example.pfe.manage_patients;

import static com.example.pfe.LoginActivity.restartActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.example.pfe.Patient;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddPatientActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btnAddPatient;
    EditText edName, edAge;
    Spinner edRelationship;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    int success, number;
    String relationship, message;

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
            JSONObject object = parser.makeHttpRequest("http://10.0.2.2/healthbuddy/patient/addPatient.php", "GET", map);
            try {
                message = object.getString("message");
                success = object.getInt("success");
                while (success == 1) {
                    JSONArray userJson = object.getJSONArray("patient");
                    JSONObject jsonObject = userJson.getJSONObject(0);
                    Patient patient = new Patient(
                            jsonObject.getString("patient_name"),
                            jsonObject.getInt("patient_age"),
                            jsonObject.getString("relationship")
                    );
                    SharedPrefManager.getInstance(getApplicationContext()).getUser().addUserPatient(patient);
                    break;
                }
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
}