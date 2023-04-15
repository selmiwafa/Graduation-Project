package com.example.pfe;

import static com.example.pfe.R.layout.user_details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.example.pfe.manage_user_account.ManageAccountActivity;
import com.google.android.material.navigation.NavigationView;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AlertDialog dialog;
    TextView username, detail_email;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_home_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        createNavbar();
    }

    public void logout(View view) {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
    }

    public void OpenManageAccount(View view) {
        startActivity(new Intent(HomepageActivity.this, ManageAccountActivity.class));
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

    public void cancel(View view) {
        dialog.dismiss();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.home):
                break;
            case (R.id.my_patients):
                Intent intent = new Intent(HomepageActivity.this, MyPatientsActivity.class);
                startActivity(intent);
                break;
            case (R.id.scan):
                intent = new Intent(HomepageActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            case (R.id.inventory):
                intent = new Intent(HomepageActivity.this, InventoryActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_med):
                intent = new Intent(HomepageActivity.this, AddMedicineActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_prescriptions):
                intent = new Intent(HomepageActivity.this, MyPrescriptionsActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_prescription):
                intent = new Intent(HomepageActivity.this, AddPrescriptionActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_analyses):
                intent = new Intent(HomepageActivity.this, MyAnalysesActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_analysis):
                intent = new Intent(HomepageActivity.this, AddAnalysisActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_doctors):
                intent = new Intent(HomepageActivity.this, LocateDoctorsActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_pharmacies):
                intent = new Intent(HomepageActivity.this, LocatePharmaciesActivity.class);
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

}
