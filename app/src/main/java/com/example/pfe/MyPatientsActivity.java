package com.example.pfe;

import static com.example.pfe.R.layout.user_details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pfe.manageMedicine.BarcodeActivity;
import com.example.pfe.manage_patients.AddPatientActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MyPatientsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    private AlertDialog dialog;
    NavigationView navigationView;
    Toolbar toolbar;
    RelativeLayout card1, card2;
    TextView username, detail_email, patient1, patient2, rel1, rel2, text;
    ImageButton addBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_my_patients);
        createNavbar();
        text = findViewById(R.id.text);
        text.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getKeyNumberPatients()));
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

    public void cancel(View view) {
        dialog.dismiss();
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
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
}