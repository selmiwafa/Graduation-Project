package com.example.pfe.manage_patient_account.analysis;

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
import com.example.pfe.manage_analyses.Analysis;

import java.util.ArrayList;
import java.util.Objects;

public class PatientAnalysisActivity extends AppCompatActivity {
    ArrayList<Analysis> analyses;
    PatientAnalysisAdapter adapter;
    int patient_num;
    String patient_id;
    LinearLayoutManager linearlayoutmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_patient_analysis);

        RecyclerView mRecyclerView = findViewById(R.id.patient_analyses_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patient_num = extras.getInt("patient_num");
        }
        analyses = SharedPrefManager.getInstance(getApplicationContext()).getAnalysisList();
        if (patient_num==1){
            patient_id=SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getId();
            analyses.removeIf(analysis -> !Objects.equals(analysis.getOwner(), patient_id));
        } else {
            patient_id=SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getId();
            analyses.removeIf(analysis -> !Objects.equals(analysis.getOwner(), patient_id));
        }

        adapter = new PatientAnalysisAdapter(PatientAnalysisActivity.this, analyses);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(analysis -> Toast.makeText(PatientAnalysisActivity.this, "Item selected", Toast.LENGTH_SHORT).show());
        linearlayoutmanager = new LinearLayoutManager(PatientAnalysisActivity.this);
        mRecyclerView.setLayoutManager(linearlayoutmanager);
    }
    public void back(View view) {
        finish();
    }
}