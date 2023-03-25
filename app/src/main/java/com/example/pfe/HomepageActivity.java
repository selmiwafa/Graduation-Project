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

import com.google.android.material.navigation.NavigationView;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AlertDialog.Builder dialogBuilder;
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

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void logout(View view) {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
    }

    public void OpenManageAccount(View view) {
        startActivity(new Intent(HomepageActivity.this, ManageAccountActivity.class));
    }

    public void createUserDialog(View view) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView=getLayoutInflater().inflate(user_details,null);
        dialogBuilder.setView(contactPopupView);
        dialog=dialogBuilder.create();
        dialog.show();
        username = dialog.findViewById(R.id.username);
        detail_email = dialog.findViewById(R.id.detail_email);
        username.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName()));
        detail_email.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail()));
    }

    public void cancel(View view) {
        dialog.dismiss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
