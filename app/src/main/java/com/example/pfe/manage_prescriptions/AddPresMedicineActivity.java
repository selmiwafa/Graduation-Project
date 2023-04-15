package com.example.pfe.manage_prescriptions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;

public class AddPresMedicineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_pres_medicine);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.select_fragment_container, new SelectMedicineFragment())
                .commit();
    }
    public void back (View view) {
        finish();
    }
    public void openPresMedDetails(View view) {
        startActivity(new Intent(AddPresMedicineActivity.this, AddPresMedDetails.class));
    }
}