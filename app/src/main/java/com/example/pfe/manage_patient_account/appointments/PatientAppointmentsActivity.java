package com.example.pfe.manage_patient_account.appointments;

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
import com.example.pfe.appointments.Appointment;

import java.util.ArrayList;
import java.util.Objects;

public class PatientAppointmentsActivity extends AppCompatActivity {
    ArrayList<Appointment> appointments;
    PatientAppointmentAdapter adapter;
    int patient_num;
    String patient_id;
    LinearLayoutManager linearlayoutmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_patient_appointments);

        RecyclerView mRecyclerView = findViewById(R.id.patient_appointments_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patient_num = extras.getInt("patient_num");
        }
        appointments = SharedPrefManager.getInstance(getApplicationContext()).getAppointmentList();
        if (patient_num==1){
            patient_id=SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getId();
            appointments.removeIf(appointment -> !Objects.equals(appointment.getOwner(), patient_id));
        } else {
            patient_id=SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getId();
            appointments.removeIf(appointment -> !Objects.equals(appointment.getOwner(), patient_id));
        }

        adapter = new PatientAppointmentAdapter(PatientAppointmentsActivity.this, appointments);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(medicine -> {
            Toast.makeText(PatientAppointmentsActivity.this, "Item selected", Toast.LENGTH_SHORT).show();
        });
        linearlayoutmanager = new LinearLayoutManager(PatientAppointmentsActivity.this);
        mRecyclerView.setLayoutManager(linearlayoutmanager);
    }
    public void back(View view) {
        finish();
    }
}