package com.example.pfe.manage_medicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.pfe.HomepageActivity;
import com.example.pfe.localisation.LocateDoctorsActivity;
import com.example.pfe.localisation.LocatePharmaciesActivity;
import com.example.pfe.manage_analyses.AddAnalysisActivity;
import com.example.pfe.manage_analyses.MyAnalysesActivity;
import com.example.pfe.manage_patient_account.AddPatientActivity;
import com.example.pfe.manage_patient_account.MyPatientsActivity;
import com.example.pfe.R;
import com.example.pfe.manage_prescriptions.AddPrescriptionActivity;
import com.example.pfe.manage_prescriptions.MyPrescriptionsActivity;
import com.google.android.material.navigation.NavigationView;

public class InventoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_inventory);
        createNavbar();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new MedicineList())
                .commit();
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
                Intent intent = new Intent(InventoryActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_patients):
                intent = new Intent(InventoryActivity.this, MyPatientsActivity.class);
                startActivity(intent);
                break;
            case (R.id.scan):
                intent = new Intent(InventoryActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            case (R.id.inventory):
                break;
            case (R.id.add_med):
                intent = new Intent(InventoryActivity.this, AddMedicineActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_prescriptions):
                intent = new Intent(InventoryActivity.this, MyPrescriptionsActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_prescription):
                intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_analyses):
                intent = new Intent(InventoryActivity.this, MyAnalysesActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_analysis):
                intent = new Intent(InventoryActivity.this, AddAnalysisActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_doctors):
                intent = new Intent(InventoryActivity.this, LocateDoctorsActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_pharmacies):
                intent = new Intent(InventoryActivity.this, LocatePharmaciesActivity.class);
                startActivity(intent);
                break;

            case (R.id.make_doctor_appointment):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
            case (R.id.make_analysis_appointment):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
            case (R.id.request_donation):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
            case (R.id.propose_donation):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
            case (R.id.propose_diet):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
    public void OpenAddMedicine(View view){
        Intent intent = new Intent(InventoryActivity.this, AddMedicineActivity.class);
        startActivity(intent);
    }
    public void OpenBarcodeScanner(View view){
        Intent intent = new Intent(InventoryActivity.this, BarcodeActivity.class);
        startActivity(intent);
    }
}