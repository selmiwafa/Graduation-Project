package com.example.pfe.manage_patient_account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.manage_patient_account.appointments.PatientAppointmentsActivity;

public class Patient1Activity extends AppCompatActivity {
    int patient = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_patient1);
        TextView edName = findViewById(R.id.edName);
        TextView relationship = findViewById(R.id.relationship);
        edName.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getName()));
        relationship.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getRelationship()));
    }

    public void OpenMypatients(View view) {
        finish();
    }
   /* public void OpenAnalyses(View view) {
        startActivity(new Intent(Patient1Activity.this, PatientAnalyses.class));
    }
    public void OpenPrescriptions(View view) {
        startActivity(new Intent(Patient1Activity.this, PatientPrescriptions.class));
    }*/
    public void OpenAppointments1(View view) {
        Intent intent = new Intent(Patient1Activity.this, PatientAppointmentsActivity.class);
        intent.putExtra("patient_num",1);
        startActivity(intent);
    }

    public void OpenUpdatePatient1(View view) {
        startActivity(new Intent(Patient1Activity.this, UpdatePatient1Activity.class));
    }

}