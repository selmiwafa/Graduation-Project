package com.example.pfe.manageMedicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.pfe.HomepageActivity;
import com.example.pfe.Medicine;
import com.example.pfe.MyPatientsActivity;
import com.example.pfe.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private RecyclerView medicineRecyclerView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_inventory);

        List<Medicine> medicineList = new ArrayList<>();
        // Add medicine to the list
        medicineList.add(new Medicine("000000000", "Inflamil",5,"","12/3/2025"));
        medicineList.add(new Medicine("000000000", "Clamoxyl",5,"","12/3/2025"));
        medicineList.add(new Medicine("000000000", "Augmentin",5,"","12/3/2025"));
        medicineList.add(new Medicine("000000000", "Aspégic",5,"","12/3/2025"));

        medicineRecyclerView = findViewById(R.id.medicine_list);
        medicineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineRecyclerView.setAdapter(new MedicineAdapter(medicineList));
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