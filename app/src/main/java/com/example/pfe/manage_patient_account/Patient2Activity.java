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

public class Patient2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_patient2);
        TextView edName = findViewById(R.id.edName2);
        TextView relationship = findViewById(R.id.relationship2);
        edName.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getName()));
        relationship.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getRelationship()));
    }

    /*public void OpenAnalyses2(View view) {
        startActivity(new Intent(Patient2Activity.this, PatientAnalyses.class));
    }
    public void OpenPrescriptions2(View view) {
        startActivity(new Intent(Patient2Activity.this, PatientPrescriptions.class));
    }*/
    public void OpenAppointments2(View view) {
        Intent intent = new Intent(Patient2Activity.this, PatientAppointmentsActivity.class);
        intent.putExtra("patient_num",2);
        startActivity(intent);
    }
    public void OpenMypatients(View view) {
        startActivity(new Intent(Patient2Activity.this, MyPatientsActivity.class));
    }
    public void OpenUpdatePatient1(View view) {
        startActivity(new Intent(Patient2Activity.this, UpdatePatient2Activity.class));
    }
}