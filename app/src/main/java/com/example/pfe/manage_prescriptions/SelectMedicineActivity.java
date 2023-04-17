package com.example.pfe.manage_prescriptions;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.manage_medicine.Medicine;

import java.util.ArrayList;

public class SelectMedicineActivity extends AppCompatActivity {
    ArrayList<Medicine> medicineList;
    private RecyclerView mRecyclerView;
    SelectMedicineAdapter adapter;
    LinearLayoutManager linearlayoutmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_pres_medicine);

        mRecyclerView = findViewById(R.id.selectlistMedicine);


        medicineList = SharedPrefManager.getInstance(getApplicationContext()).getMedicineList();
        adapter = new SelectMedicineAdapter(SelectMedicineActivity.this, medicineList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(medicine -> {
            Toast.makeText(SelectMedicineActivity.this, "Item selected", Toast.LENGTH_SHORT).show();
        });
        linearlayoutmanager = new LinearLayoutManager(SelectMedicineActivity.this);
        mRecyclerView.setLayoutManager(linearlayoutmanager);
    }
    public void back (View view) {
        finish();
    }
}