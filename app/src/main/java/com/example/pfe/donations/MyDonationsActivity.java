package com.example.pfe.donations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.HomepageActivity;
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

import java.util.ArrayList;

public class MyDonationsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Donation> donations;
    private RecyclerView mRecyclerView;
    MyDonationsAdapter adapter;
    LinearLayoutManager linearlayoutmanager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_my_donations);
        createNavbar();

        mRecyclerView = findViewById(R.id.my_donations_list);


        donations = SharedPrefManager.getInstance(getApplicationContext()).getDonationList();
        adapter = new MyDonationsAdapter(MyDonationsActivity.this, donations);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(medicine -> {
            Toast.makeText(MyDonationsActivity.this, "Item selected", Toast.LENGTH_SHORT).show();
        });
        linearlayoutmanager = new LinearLayoutManager(MyDonationsActivity.this);
        mRecyclerView.setLayoutManager(linearlayoutmanager);
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
                Intent intent = new Intent(MyDonationsActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_patients):
                intent = new Intent(MyDonationsActivity.this, MyPatientsActivity.class);
                startActivity(intent);
                break;
            case (R.id.scan):
                intent = new Intent(MyDonationsActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            case (R.id.inventory):
                intent = new Intent(MyDonationsActivity.this, InventoryActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_med):
                intent = new Intent(MyDonationsActivity.this, AddMedicineActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_prescriptions):
                intent = new Intent(MyDonationsActivity.this, MyPrescriptionsActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_prescription):
                intent = new Intent(MyDonationsActivity.this, AddPrescriptionActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_analyses):
                intent = new Intent(MyDonationsActivity.this, MyAnalysesActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_analysis):
                intent = new Intent(MyDonationsActivity.this, AddAnalysisActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_doctors):
                intent = new Intent(MyDonationsActivity.this, LocateDoctorsActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_pharmacies):
                intent = new Intent(MyDonationsActivity.this, LocatePharmaciesActivity.class);
                startActivity(intent);
                break;

            case (R.id.make_doctor_appointment):
                intent = new Intent(MyDonationsActivity.this, DoctorAppointmentActivity.class);
                startActivity(intent);
                break;
            case (R.id.make_analysis_appointment):
                intent = new Intent(MyDonationsActivity.this, AnalysisAppointmentActivity.class);
                startActivity(intent);
                break;
            case (R.id.request_donation):
                intent = new Intent(MyDonationsActivity.this, RequestDonationActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_donations):
                break;
            case (R.id.propose_donation):
                intent = new Intent(MyDonationsActivity.this, ProposeDonationActivity.class);
                startActivity(intent);
                break;
            case (R.id.propose_diet):
                intent = new Intent(MyDonationsActivity.this, DietActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
}