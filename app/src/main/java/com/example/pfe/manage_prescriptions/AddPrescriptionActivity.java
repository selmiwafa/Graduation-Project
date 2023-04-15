package com.example.pfe.manage_prescriptions;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;

import java.util.Calendar;

public class AddPrescriptionActivity extends AppCompatActivity {
    EditText edStartDate, edEndDate, edPresName;
    String id;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_prescription);
        edStartDate.setOnClickListener(v -> pickDate());
        setListener = (view, year, month, day) -> {
            String date = day + "/" + month + "/" + year;
            edStartDate.setText(date);
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        };
        edEndDate.setOnClickListener(v -> pickDate());
        setListener = (view, year, month, day) -> {
            String date = day + "/" + month + "/" + year;
            edEndDate.setText(date);
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        };
        edPresName = findViewById(R.id.prescription_name_edit_text);
        id = edPresName.getText().toString()+edStartDate.getText().toString();
    }
    public void back(View view){
        this.finish();
    }
    public void next(View view){
        Intent intent = new Intent(AddPrescriptionActivity.this, PresMedListActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",edPresName.getText().toString());
        intent.putExtra("start_date",edStartDate.getText().toString());
        intent.putExtra("end_date",edEndDate.getText().toString());
        startActivity(intent);
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
}