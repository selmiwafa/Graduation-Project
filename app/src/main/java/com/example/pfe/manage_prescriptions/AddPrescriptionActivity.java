package com.example.pfe.manage_prescriptions;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import java.util.Calendar;

public class AddPrescriptionActivity extends AppCompatActivity {
    EditText edStartDate, edEndDate, edPresName;
    String id;
    DatePickerDialog.OnDateSetListener setListener, setListener2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_prescription);

        edStartDate = findViewById(R.id.edStartDate);
        edStartDate.setOnClickListener(v -> pickDate());
        setListener = (view, year, month, day) -> {
            String date = day + "/" + month + "/" + year;
            edStartDate.setText(date);
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        };
        edEndDate = findViewById(R.id.edEndDate);
        edEndDate.setOnClickListener(v -> pickDate2());
        setListener2 = (view, year, month, day) -> {
            String date = day + "/" + month + "/" + year;
            edEndDate.setText(date);
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        };
        edPresName = findViewById(R.id.prescription_name_edit_text);
        if (edEndDate.getText().toString().isEmpty()) {
            edEndDate.setText("");
        }
    }
    public void back(View view){
        this.finish();
    }
    public void next(View view){
        if (edPresName.getText().toString().isEmpty() ||
                edStartDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill all required fields!", Toast.LENGTH_SHORT).show();
        } else
        {
            SharedPrefManager.getInstance(getApplicationContext()).setCurrentPres(edPresName.getText().toString()+edStartDate.getText().toString(),edPresName.getText().toString(),edStartDate.getText().toString(),edEndDate.getText().toString());
            Intent intent = new Intent(AddPrescriptionActivity.this, SummaryListActivity.class);
            startActivity(intent);


        }
    }
    public void pickDate(){
        int y = Calendar.getInstance().get(Calendar.YEAR) ;
        int m = Calendar.getInstance().get(Calendar.MONTH);
        int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddPrescriptionActivity.this
                , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                , setListener, y, m, d);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }
    public void pickDate2(){
        int y = Calendar.getInstance().get(Calendar.YEAR) ;
        int m = Calendar.getInstance().get(Calendar.MONTH);
        int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddPrescriptionActivity.this
                , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                , setListener2, y, m, d);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }
}