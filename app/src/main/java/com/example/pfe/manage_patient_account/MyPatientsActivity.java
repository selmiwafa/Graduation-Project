package com.example.pfe.manage_patient_account;

import static com.example.pfe.R.layout.delete_dialog;
import static com.example.pfe.R.layout.user_details;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pfe.HomepageActivity;
import com.example.pfe.JSONParser;
import com.example.pfe.manage_user_account.LoginActivity;
import com.example.pfe.manage_user_account.ManageAccountActivity;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.manageMedicine.BarcodeActivity;
import com.example.pfe.manageMedicine.InventoryActivity;
import com.example.pfe.manage_user_account.User;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public class MyPatientsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    private AlertDialog dialog;
    NavigationView navigationView;
    Toolbar toolbar;
    RelativeLayout card1, card2;
    TextView username, detail_email, patient1, patient2, rel1, rel2, text;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    ImageButton addBtn;
    ProgressDialog progressDialog, dialog2;
    int success, number;
    String message;
    JSONParser parser = new JSONParser();

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog delDialog;
    int patient = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_my_patients);
        createNavbar();

        new Show().execute();
        addBtn = findViewById(R.id.addBtn);
        addBtn.setVisibility(View.VISIBLE);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        if (SharedPrefManager.getInstance(getApplicationContext()).getKeyNumberPatients() == 0) {
            card1.setVisibility(View.INVISIBLE);
            card2.setVisibility(View.INVISIBLE);
        } else if (SharedPrefManager.getInstance(getApplicationContext()).getKeyNumberPatients() == 1) {
            card1.setVisibility(View.VISIBLE);
            patient1 = findViewById(R.id.pat1);
            patient1.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getName()));
            rel1 = findViewById(R.id.rel1);
            rel1.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getRelationship()));
        } else {
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
            patient1 = findViewById(R.id.pat1);
            patient1.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getName()));
            rel1 = findViewById(R.id.rel1);
            rel1.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getRelationship()));
            patient2 = findViewById(R.id.pat2);
            patient2.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getName()));
            rel2 = findViewById(R.id.rel2);
            rel2.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getRelationship()));
            addBtn.setVisibility(View.INVISIBLE);
        }
    }
    public void createNavbar() {
        drawerLayout = findViewById(R.id.drawerlayout2);
        navigationView = findViewById(R.id.nav_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void createUserDialog(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(user_details, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        username = dialog.findViewById(R.id.username);
        detail_email = dialog.findViewById(R.id.detail_email);
        showInfo(username, detail_email);
    }
    public void deletePatientDialog1(View view) {
        patient = 1;
        AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(delete_dialog, null);
        dialogBuilder2.setView(contactPopupView);
        delDialog = dialogBuilder2.create();
        delDialog.show();
    }
    public void deletePatientDialog2(View view) {
        patient = 2;
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(delete_dialog, null);
        dialogBuilder.setView(contactPopupView);
        delDialog = dialogBuilder.create();
        delDialog.show();
    }
    public void deletePatient(View view) {
        new Delete().execute();
    }
    public void cancel(View view) {
        delDialog.dismiss();
    }
    public void logout(View view) {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
    }
    public void OpenManageAccount(View view) {
        startActivity(new Intent(MyPatientsActivity.this, ManageAccountActivity.class));
    }
    public void OpenAddPatient(View view) {
        startActivity(new Intent(MyPatientsActivity.this, AddPatientActivity.class));
    }
    public void showInfo(TextView username, TextView email) {
        username.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName()));
        email.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail()));
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void OpenPatient1(View view) {
        Intent intent = new Intent(MyPatientsActivity.this, Patient1Activity.class);
        startActivity(intent);
    }
    public void OpenPatient2(View view) {
        Intent intent = new Intent(MyPatientsActivity.this, Patient2Activity.class);
        startActivity(intent);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.home):
                Intent intent = new Intent(MyPatientsActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_patients):
                break;
            case (R.id.scan):
                intent = new Intent(MyPatientsActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            case (R.id.inventory):
                intent = new Intent(MyPatientsActivity.this, InventoryActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
    @SuppressLint("StaticFieldLeak")
    class Delete extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MyPatientsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("user", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            if (patient == 1) {
                map.put("patient_name", SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getName());
            }
            else {
                map.put("patient_name", SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getName());
            }
            System.out.println("patient = " + patient);
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/patient/deletePatient.php", "GET", map);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/patient/deletePatient.php", "GET", map);
                success = object.getInt("success");
                if (patient == 1) {
                    String name = SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getName();
                    int age = SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getAge();
                    String relationship = SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getRelationship();
                    SharedPrefManager.getInstance(getApplicationContext()).getPatient1().setName(name);
                    SharedPrefManager.getInstance(getApplicationContext()).getPatient1().setAge(age);
                    SharedPrefManager.getInstance(getApplicationContext()).getPatient1().setRelationship(relationship);
                    card2 = findViewById(R.id.card2);
                    card2.setVisibility(View.INVISIBLE);
                    SharedPrefManager.getInstance(getApplicationContext()).getPatient2().setName("");
                    SharedPrefManager.getInstance(getApplicationContext()).getPatient2().setAge(0);
                    SharedPrefManager.getInstance(getApplicationContext()).getPatient2().setRelationship("");
                }
                else {
                    card2 = findViewById(R.id.card2);
                    card2.setVisibility(View.INVISIBLE);
                    SharedPrefManager.getInstance(getApplicationContext()).getPatient2().setName("");
                    SharedPrefManager.getInstance(getApplicationContext()).getPatient2().setAge(0);
                    SharedPrefManager.getInstance(getApplicationContext()).getPatient2().setRelationship("");
                }
                connection.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();

            if (success == 1) {
                Toast.makeText(MyPatientsActivity.this, "Patient deleted successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MyPatientsActivity.this, MyPatientsActivity.class));
            } else {
                Toast.makeText(MyPatientsActivity.this, "Error deleting!", Toast.LENGTH_LONG).show();
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    class Show extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog2 = new ProgressDialog(MyPatientsActivity.this);
            dialog2.setMessage("Please wait");
            dialog2.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> map = new HashMap<>();
            map.put("user", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/patient/selectPatient.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/patient/selectPatient.php", "GET", map);
                success = object.getInt("success");
                message = object.getString("message");
                while (success == 1) {
                    number = object.getInt("number");
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
            dialog2.cancel();
            if (success == 1) {
                Toast.makeText(MyPatientsActivity.this, "Patients refreshed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MyPatientsActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

    }
}