package com.example.pfe.manageMedicine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfe.R;
import com.example.pfe.SignupActivity;

import java.util.Calendar;

public class AddMedicineActivity extends AppCompatActivity {
    int barcode;
    EditText edBarcode, edExpDate;
    int minteger;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_medicine);

        edBarcode = findViewById(R.id.edBarcode);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            barcode = extras.getInt("key");
        }
        edBarcode.setText(barcode);
        edExpDate=findViewById(R.id.edExpDate);
        edExpDate.setOnClickListener(v -> {
            int y = Calendar.getInstance().get(Calendar.YEAR) - 18;
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddMedicineActivity.this
                    , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    , setListener, y, m, d);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener = (view, year, month, day) -> {
            month = month + 1;
            String date = day + "/" + month + "/" + year;
            edExpDate.setText(date);
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (year >= (y - 18) && month >= m && day >= d) {
                Toast.makeText(AddMedicineActivity.this, "User should be older than 18 years old!", Toast.LENGTH_LONG).show();
                edExpDate.setText("");
            }
        };
    }
    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);

    }public void decreaseInteger(View view) {
        minteger = minteger - 1;
        display(minteger);
    }
    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.edQuantity);
        displayInteger.setText("" + number);
    }
}