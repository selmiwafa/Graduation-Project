package com.example.pfe.manage_patients;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pfe.HomepageActivity;
import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddPatientActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button btnAddPatient;
    EditText edName, edAge;
    Spinner edRelationship;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    int success;
    String relationship;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_patient);
        initView();
        createSpinner();
        createNavbar();
        btnAddPatient.setOnClickListener(v -> addUser());

        // Hide/Show Items
        menu = navigationView.getMenu();
    }

    public void createSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.relationship, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edRelationship.setAdapter(adapter);
        edRelationship.setOnItemSelectedListener(this);
        edRelationship.setPrompt("Relationship");
    }

    public void createNavbar() {
        drawerLayout = findViewById(R.id.drawerlayout2);
        navigationView = findViewById(R.id.nav_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    void addUser() {
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.home):
                Intent intent = new Intent(AddPatientActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_patient):
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                relationship = edRelationship.toString();
            case 1:
                relationship = edRelationship.toString();
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
            map.put("name", edName.getText().toString());
            map.put("relationship", edRelationship.toString());
            map.put("age", edAge.getText().toString());
            map.put("user", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            JSONObject object = parser.makeHttpRequest("http://10.0.2.2/healthbuddy/patient/addPatient.php", "GET", map);
            try {
                success = object.getInt("success");
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
                menu.findItem(R.id.patient1).setVisible(true);
            } else {
                Toast.makeText(AddPatientActivity.this, "Error adding  patient!", Toast.LENGTH_LONG).show();
            }
        }

    }
}