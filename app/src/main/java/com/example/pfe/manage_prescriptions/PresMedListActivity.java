package com.example.pfe.manage_prescriptions;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PresMedListActivity extends AppCompatActivity {

    ProgressDialog dialog;
    Button saveBtn;
    ImageButton topSaveBtn;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success;
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
    int tpw;
    String other;
    List<String> list;
    ArrayList<PresMedicine> summaryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_pres_med_list);
        setSummary();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            barcode = extras.getString("barcode");
            name = extras.getString("med_name");

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
        // Get the existing summary list from the fragment
        FragmentSummaryList fragmentSummaryList = (FragmentSummaryList) getSupportFragmentManager().findFragmentById(R.id.fragment_container_summary);
         summaryList = fragmentSummaryList.summaryList;

        // Add the new PresMedicine object to the end of the list
        PresMedicine presMed = new PresMedicine(barcode, name, dose, frequency, period, tpw, other);
        summaryList.add(presMed);
        if (fragmentSummaryList.adapter != null) {
            fragmentSummaryList.adapter.notifyDataSetChanged();
        }
    }
    public void setSummary(){
        edPres_name = findViewById(R.id.pres_name);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);

        list = SharedPrefManager.getInstance(getApplicationContext()).getCurrentPres();
        edPres_name.setText(list.get(1));
        start_date.setText(list.get(2));
        end_date.setText(list.get(3));

    }

    public void save(View view) {
        new Add().execute();
    }
    public void openAddPage(View view){
        startActivity(new Intent(PresMedListActivity.this, AddPresMedicineActivity.class));
    }

    @SuppressLint("StaticFieldLeak")
    class Add extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PresMedListActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            //for (int i = 0; i< summaryList.size(); i++) {
            int i = 0;
                map.put("barcode", summaryList.get(i).getBarcode());
                map.put("dose", String.valueOf(summaryList.get(i).getDose()));
                map.put("frequency", String.valueOf(summaryList.get(i).getFrequency()));
                map.put("period", String.valueOf(summaryList.get(i).getPeriod()));
                map.put("tpw", String.valueOf(summaryList.get(i).getTpw()));
                map.put("other", summaryList.get(i).getOther());
            //}

            map.put("pres_id", list.get(0));
            map.put("pres_name", list.get(1));
            map.put("pres_start", list.get(2));
            map.put("pres_end", list.get(3));

            map.put("user", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/addMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/prescription/addPrescription.php", "GET", map);
                success = object.getInt("success");
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if (success == 1) {
                Toast.makeText(PresMedListActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PresMedListActivity.this, MyPrescriptionsActivity.class));
            } else {
                Toast.makeText(PresMedListActivity.this, "Error adding prescription!", Toast.LENGTH_LONG).show();
            }
        }
    }
}