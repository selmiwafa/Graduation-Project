package com.example.pfe.manage_prescriptions;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import java.util.ArrayList;

public class AddPresMedDetails extends AppCompatActivity {
    EditText edDose,edFrequency,edOther;
    TextView edPeriod,edTpw;
    String barcode,name,description,exp_date;
    int quantity;
    int period, tpw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pres_med_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            barcode = extras.getString("barcode");
            name = extras.getString("med_name");
        }
        init();

    }

    public void savePresMed(View view){
        if (edDose.getText().toString().isEmpty() ||
                edFrequency.getText().toString().isEmpty() ||
                edPeriod.getText().toString().isEmpty() ||
                edTpw.getText().toString().isEmpty() ) {
            Toast.makeText(this, "Fill required fields!", Toast.LENGTH_LONG).show();
        } else
        {
            int dose = Integer.parseInt(edDose.getText().toString());
            int frequency = Integer.parseInt(edFrequency.getText().toString());
            int period = Integer.parseInt(edPeriod.getText().toString());
            int tpw = Integer.parseInt(edTpw.getText().toString());
            String other = edOther.getText().toString();
            if (TextUtils.isEmpty(other)) {
                other = "";
            }
            ArrayList<PresMedicine> presMedicines;
            PresMedicine presMedicine = new PresMedicine(barcode,name,dose,frequency,period,tpw,other);
            if (SharedPrefManager.getInstance(getApplicationContext()).getSummaryList() == null) {
                presMedicines = new ArrayList<>();
            } else {
                presMedicines = SharedPrefManager.getInstance(getApplicationContext()).getSummaryList();
            }
            presMedicines.add(presMedicine);
            SharedPrefManager.saveSummaryList(presMedicines);

            Intent intent = new Intent(this, SummaryListActivity.class);
            this.startActivity(intent);
        }
    }
    public void  init(){
        edDose = findViewById(R.id.edDose);
        edFrequency = findViewById(R.id.edFrequency);
        edOther =findViewById(R.id.edOther);
    }
    public void increasePeriod(View view) {
        period = period + 1;
        displayPeriod(period);

    }
    public void decreasePeriod(View view) {
        if (period>0) {
            period = period - 1;
        }
        displayPeriod(period);
    }
    private void displayPeriod(int number) {
        edPeriod = (TextView) findViewById(R.id.edPeriod);
        edPeriod.setText("" + number);
    }
    public void increaseTpw(View view) {
        tpw = tpw + 1;
        displayTpw(tpw);

    }
    public void decreaseTpw(View view) {
        if (tpw>0) {
            tpw = tpw - 1;
        }
        displayTpw(tpw);
    }
    private void displayTpw(int number) {
        edTpw = (TextView) findViewById(R.id.edtpw);
        edTpw.setText("" + number);
    }
    public void back(View view){
        finish();
    }
}