package com.example.pfe.manage_analyses;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.manage_medicine.InventoryActivity;
import com.example.pfe.manage_medicine.UpdateMedicineActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

public class UpdateAnalysisActivity extends AppCompatActivity {
    String name, date, result;
    EditText edAnalysisName, edAnalysisDate, edResult;
    ProgressDialog dialog;
    Button saveBtn;
    ImageButton topSaveBtn;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    DatePickerDialog.OnDateSetListener setListener;
    int success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_update_analysis);
        initView();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("analysis_name");
            date = extras.getString("analysis_date");
            result = extras.getString("result");
        }
        edAnalysisDate.setText(date);
        edAnalysisName.setText(name);
        edResult.setText(result);
        edAnalysisDate.setOnClickListener(v -> {
            int y = Calendar.getInstance().get(Calendar.YEAR) ;
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    UpdateAnalysisActivity.this
                    , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    , setListener, y, m, d);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener = (view, year, month, day) -> {
            String date = day + "/" + month + "/" + year;
            edAnalysisDate.setText(date);
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (year >= y && month >= m && day >= d) {
                Toast.makeText(UpdateAnalysisActivity.this, "Analysis should be already done!", Toast.LENGTH_LONG).show();
                edAnalysisDate.setText("");
            }
        };
        saveBtn.setOnClickListener(v -> updateAnalysis());
        topSaveBtn.setOnClickListener(v -> updateAnalysis());
    }
    public void initView(){
        edAnalysisDate = findViewById(R.id.edAnalysisDate);
        edAnalysisName=findViewById(R.id.edAnalysisName);
        edResult=findViewById(R.id.edAnalysisResult);
        saveBtn=findViewById(R.id.saveBtn);
        topSaveBtn=findViewById(R.id.topSaveBtn);
    }
    void updateAnalysis() {
        String a_name = edAnalysisName.getText().toString();
        String a_date = edAnalysisDate.getText().toString();
        String result = edResult.getText().toString();
        if (a_name.isEmpty() || a_date.isEmpty() || result.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Enter all required fields!", Toast.LENGTH_LONG).show();
        }
        new Update().execute();
    }
    public void back(View view){
        startActivity(new Intent(UpdateAnalysisActivity.this, MyAnalysesActivity.class));
    }

    @SuppressLint("StaticFieldLeak")
    class Update extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UpdateAnalysisActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("old_analysis_name", name);
            map.put("new_analysis_name", edAnalysisName.getText().toString());
            map.put("analysis_date", edAnalysisDate.getText().toString());
            map.put("result", edResult.getText().toString());
            map.put("user", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/updateMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/analysis/updateAnalysis.php", "GET", map);
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
                Toast.makeText(UpdateAnalysisActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateAnalysisActivity.this, MyAnalysesActivity.class));
            } else {
                Toast.makeText(UpdateAnalysisActivity.this, "Error adding analysis!", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void delete(View view) {
        new Delete().execute();
    }
    @SuppressLint("StaticFieldLeak")
    class Delete extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UpdateAnalysisActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> map = new HashMap<>();
            map.put("analysis_name", name);
            map.put("user", SharedPrefManager.getInstance(UpdateAnalysisActivity.this).getUser().getEmail());

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/deleteMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/analysis/deleteAnalysis.php", "GET", map);
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
                Toast.makeText(UpdateAnalysisActivity.this, "deleted successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UpdateAnalysisActivity.this, InventoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(UpdateAnalysisActivity.this, "Error deleting analysis!", Toast.LENGTH_LONG).show();
            }
        }
    }
}