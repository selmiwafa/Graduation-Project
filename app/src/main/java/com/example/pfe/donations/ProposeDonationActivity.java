package com.example.pfe.donations;

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
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.pfe.appointments.analysis_appointments.AnalysisAppointmentActivity;
import com.example.pfe.appointments.doctor_appointments.DoctorAppointmentActivity;
import com.example.pfe.diet.DietActivity;
import com.example.pfe.localisation.LocateDoctorsActivity;
import com.example.pfe.localisation.LocatePharmaciesActivity;
import com.example.pfe.manage_analyses.AddAnalysisActivity;
import com.example.pfe.manage_analyses.MyAnalysesActivity;
import com.example.pfe.manage_medicine.AddMedicineActivity;
import com.example.pfe.manage_medicine.BarcodeActivity;
import com.example.pfe.manage_medicine.InventoryActivity;
import com.example.pfe.manage_patient_account.MyPatientsActivity;
import com.example.pfe.manage_prescriptions.AddPrescriptionActivity;
import com.example.pfe.manage_prescriptions.MyPrescriptionsActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProposeDonationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int minteger;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success;
    String barcode;
    Button BtnPropose;
    EditText edBarcode;
    TextView edQuantity;
    ArrayList<Donation> donations;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String date, id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_propose_donation);
        initView();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            barcode = extras.getString("barcode");
        }
        edBarcode.setText(barcode);
        int y = Calendar.getInstance().get(Calendar.YEAR) ;
        int m = Calendar.getInstance().get(Calendar.MONTH);
        int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        id = d + "/" + m + "/" + y + "-" + hour + ":" + minute;
        date = d + "/" + m + "/" + y ;
        createNavbar();
        BtnPropose.setOnClickListener(v -> propose());
    }

    void propose() {
        String code = String.valueOf(edBarcode.getText());
        if (code.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Enter required field!", Toast.LENGTH_LONG).show();
        } else if (minteger == 0) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Quantity should be over 0!!", Toast.LENGTH_LONG).show();
        }
        new Add().execute();
    }


    @SuppressLint("StaticFieldLeak")
    class Add extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProposeDonationActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", id);
            map.put("barcode", barcode);
            map.put("quantity", String.valueOf(minteger));
            map.put("date", date);
            map.put("user", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/analysis/addAnalysis.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/donations/addDonation.php", "GET", map);
                success = object.getInt("success");
                connection.close();
                if (SharedPrefManager.getInstance(getApplicationContext()).getDonationList() == null){
                    donations = new ArrayList<>();
                }
                else {
                    donations = SharedPrefManager.getInstance(getApplicationContext()).getDonationList();
                }
                donations.add(new Donation(id,
                        barcode,
                        minteger,
                        date));
                SharedPrefManager.saveDonationList(donations);
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
                Toast.makeText(ProposeDonationActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProposeDonationActivity.this, MyDonationsActivity.class));
            } else {
                Toast.makeText(ProposeDonationActivity.this, "Error adding donation!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void createNavbar() {
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.home):
                Intent intent = new Intent(ProposeDonationActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_patients):
                intent = new Intent(ProposeDonationActivity.this, MyPatientsActivity.class);
                startActivity(intent);
                break;
            case (R.id.scan):
                intent = new Intent(ProposeDonationActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            case (R.id.inventory):
                intent = new Intent(ProposeDonationActivity.this, InventoryActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_med):
                intent = new Intent(ProposeDonationActivity.this, AddMedicineActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_prescriptions):
                intent = new Intent(ProposeDonationActivity.this, MyPrescriptionsActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_prescription):
                intent = new Intent(ProposeDonationActivity.this, AddPrescriptionActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_analyses):
                intent = new Intent(ProposeDonationActivity.this, MyAnalysesActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_analysis):
                intent = new Intent(ProposeDonationActivity.this, AddAnalysisActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_doctors):
                intent = new Intent(ProposeDonationActivity.this, LocateDoctorsActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_pharmacies):
                intent = new Intent(ProposeDonationActivity.this, LocatePharmaciesActivity.class);
                startActivity(intent);
                break;

            case (R.id.make_doctor_appointment):
                intent = new Intent(ProposeDonationActivity.this, DoctorAppointmentActivity.class);
                startActivity(intent);
                break;
            case (R.id.make_analysis_appointment):
                intent = new Intent(ProposeDonationActivity.this, AnalysisAppointmentActivity.class);
                startActivity(intent);
                break;
            case (R.id.request_donation):
                intent = new Intent(ProposeDonationActivity.this, RequestDonationActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_donations):
                intent = new Intent(ProposeDonationActivity.this, MyDonationsActivity.class);
                startActivity(intent);
                break;
            case (R.id.propose_donation):
                break;
            case (R.id.propose_diet):
                intent = new Intent(ProposeDonationActivity.this, DietActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
    public void initView(){
        edBarcode = findViewById(R.id.barcode);
        edQuantity=findViewById(R.id.edQuantity);
        BtnPropose=findViewById(R.id.propose);
    }
    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);

    }public void decreaseInteger(View view) {
        if (minteger>0) {
            minteger = minteger - 1;
        }
        display(minteger);
    }
    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(R.id.edQuantity);
        displayInteger.setText("" + number);
    }
    public void openSelectProposal(View view) {
        startActivity(new Intent(ProposeDonationActivity.this,SelectProposalActivity.class));
    }
}