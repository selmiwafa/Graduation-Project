package com.example.pfe.manage_prescriptions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;

public class PresMedListActivity extends AppCompatActivity {
    String pres_id, pres_name, pres_start, pres_end;
    TextView edPres_name, start_date, end_date;
    String barcode ;
    String name;
    int quantity;
    String exp_date;
    String description;

    int dose;
    int frequency;
    int period;
    PresMedicine presMed;
    int tpw;
    String other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_pres_med_list);
        getValues();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            barcode = extras.getString("barcode");
            name = extras.getString("med_name");
            quantity = extras.getInt("quantity");
            exp_date = extras.getString("exp_date");
            description = extras.getString("description");

            String doseString = extras.getString("dose");
            if (doseString != null) {
                dose = Integer.parseInt(doseString);
            }

            String frequencyString = extras.getString("frequency");
            if (frequencyString != null) {
                frequency = Integer.parseInt(frequencyString);
            }

            String periodString = extras.getString("period");
            if (periodString != null) {
                period = Integer.parseInt(periodString);
            }

            String tpwString = extras.getString("tpw");
            if (tpwString != null) {
                tpw = Integer.parseInt(tpwString);
            }

            other = extras.getString("other");
        }
        // Add some items to the list
        presMed = new PresMedicine(barcode,name,dose,frequency,period,tpw,other);
        // Pass the itemList to the fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_summary, new FragmentSummaryList())
                .commit();
    }
    public PresMedicine getPresMedicine(){
        return this.presMed;
    }
    public void getValues(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pres_id = extras.getString("id");
            pres_name = extras.getString("name");
            pres_start = extras.getString("start_date");
            pres_end = extras.getString("end_date");
        }
        edPres_name = findViewById(R.id.pres_name);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);

        edPres_name.setText(pres_name);
        start_date.setText(pres_start);
        end_date.setText(pres_end);
    }
    public void openAddPage(View view){
        startActivity(new Intent(PresMedListActivity.this, AddPresMedicineActivity.class));
    }
}