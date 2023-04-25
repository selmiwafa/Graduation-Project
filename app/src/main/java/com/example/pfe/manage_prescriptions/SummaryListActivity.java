package com.example.pfe.manage_prescriptions;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.manage_medicine.Medicine;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SummaryListActivity extends AppCompatActivity {

    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success;
    TextView edPres_name, start_date, end_date;
    ArrayList<Medicine> medicineList;
    private RecyclerView mRecyclerView;
    SummaryAdapter adapter;
    LinearLayoutManager linearlayoutmanager;
    Prescription prescription;
    ArrayList<PresMedicine> summaryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_summary_list);
        setSummary();


        summaryList = SharedPrefManager.getInstance(getApplicationContext()).getSummaryList();

        mRecyclerView = findViewById(R.id.listSummaryMedicine);


        medicineList = SharedPrefManager.getInstance(getApplicationContext()).getMedicineList();
        adapter = new SummaryAdapter(SummaryListActivity.this, summaryList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(medicine -> {
            Toast.makeText(SummaryListActivity.this, "Item selected", Toast.LENGTH_SHORT).show();
        });
        linearlayoutmanager = new LinearLayoutManager(SummaryListActivity.this);
        mRecyclerView.setLayoutManager(linearlayoutmanager);
    }
    public void setSummary(){
        edPres_name = findViewById(R.id.pres_name);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);

        prescription = SharedPrefManager.getInstance(getApplicationContext()).getCurrentPres();
        edPres_name.setText(prescription.getName());
        start_date.setText(prescription.getStart());
        end_date.setText(prescription.getEnd());

    }

    public void save(View view) {
        new Add().execute();
    }
    public void openAddPage(View view){
        startActivity(new Intent(SummaryListActivity.this, SelectMedicineActivity.class));
    }

    @SuppressLint("StaticFieldLeak")
    class Add extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SummaryListActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, Object> params = new HashMap<>();

            HashMap<String, String> map = new HashMap<>();
            map.put("pres_id", prescription.getId());
            map.put("pres_name", prescription.getName());
            map.put("pres_start", prescription.getStart());
            map.put("pres_end", prescription.getEnd());
            map.put("owner_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            map.put("owner_type", "user");

            Gson gson = new Gson();
            String summaryListJson = gson.toJson(summaryList);
            params.put("summarylist", summaryListJson);

            params.putAll(map);
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/addMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/prescription/addPrescription.php", "GET", params);
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
                SharedPrefManager.getInstance(getApplicationContext()).deleteSummary();
                SharedPrefManager.getInstance(getApplicationContext()).deleteCurrentPres();
                Toast.makeText(SummaryListActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(SummaryListActivity.this, MyPrescriptionsActivity.class));
            } else {
                Toast.makeText(SummaryListActivity.this, "Error adding prescription!", Toast.LENGTH_LONG).show();
            }
        }
    }
}