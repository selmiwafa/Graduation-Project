package com.example.pfe.manage_prescriptions;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;

public class PresMedListActivity extends AppCompatActivity {
    String pres_id, pres_name, pres_start, pres_end;
    TextView edPres_name, start_date, end_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_pres_med_list);
        getValues();

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
}