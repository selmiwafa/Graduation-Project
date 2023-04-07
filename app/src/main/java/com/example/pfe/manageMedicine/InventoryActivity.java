package com.example.pfe.manageMedicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfe.HomepageActivity;
import com.example.pfe.JSONParser;
import com.example.pfe.Medicine;
import com.example.pfe.MyPatientsActivity;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_inventory);

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
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
    public void OpenAddMedicine(View view){
        Intent intent = new Intent(InventoryActivity.this, AddMedicineActivity.class);
        startActivity(intent);
    }

}