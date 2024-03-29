package com.example.pfe.manage_medicine;

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
import java.util.Calendar;
import java.util.HashMap;

public class AddMedicineActivity extends AppCompatActivity {
    int minteger;
    String barcode;
    EditText edBarcode, edExpDate, edMedName, edDesc;
    TextView edQuantity;
    ProgressDialog dialog;
    Button saveBtn;
    ImageButton topSaveBtn;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success;

    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_medicine);
        initView();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            barcode = extras.getString("key");
        }
        edBarcode.setText(barcode);
        edExpDate.setOnClickListener(v -> {
            int y = Calendar.getInstance().get(Calendar.YEAR) ;
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
            String date = day + "/" + month + "/" + year;
            edExpDate.setText(date);
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (year < y && month < m && day < d) {
                Toast.makeText(AddMedicineActivity.this, "Medicine Expired! Throw it away!", Toast.LENGTH_LONG).show();
                edExpDate.setText("");
            }
        };
        saveBtn.setOnClickListener(v -> addMedicine());
        topSaveBtn.setOnClickListener(v -> addMedicine());
    }
    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);

    }public void decreaseInteger(View view) {
        if (minteger>0) {
            minteger = minteger - 1;
        }
        display(minteger);
    }
    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(R.id.edQuantity);
        displayInteger.setText("" + number);
    }
    public void initView(){
        edBarcode = findViewById(R.id.edBarcode);
        edExpDate=findViewById(R.id.edExpDate);
        edQuantity=findViewById(R.id.edQuantity);
        edMedName=findViewById(R.id.edMedName);
        edDesc=findViewById(R.id.edDesc);
        saveBtn=findViewById(R.id.saveBtn);
        topSaveBtn=findViewById(R.id.topSaveBtn);
    }
    void addMedicine() {
        String barcode = edBarcode.getText().toString();
        String med_name = edMedName.getText().toString();
        String quantity = edQuantity.getText().toString();
        String exp_date = edExpDate.getText().toString();
        if (barcode.isEmpty() || med_name.isEmpty() || quantity.isEmpty() || exp_date.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Enter all required fields!", Toast.LENGTH_LONG).show();
        }
        else if (quantity == "0") {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Quantity should be more than 0!", Toast.LENGTH_LONG).show();
        }
        new Add().execute();
    }


    @SuppressLint("StaticFieldLeak")
    class Add extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddMedicineActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("barcode", edBarcode.getText().toString());
            map.put("med_name", edMedName.getText().toString());
            map.put("quantity", edQuantity.getText().toString());
            map.put("description", edDesc.getText().toString());
            map.put("exp_date", edExpDate.getText().toString());
            map.put("user", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/addMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/medicine/addMedicine.php", "GET", map);
                success = object.getInt("success");
                connection.close();
                ArrayList<Medicine> medicineArrayList = SharedPrefManager.getInstance(getApplicationContext()).getMedicineList();
                medicineArrayList.add(new Medicine(edBarcode.getText().toString(),
                        edMedName.getText().toString(),
                        Integer.parseInt(edQuantity.getText().toString()),
                        edDesc.getText().toString(),
                        edExpDate.getText().toString()));
                SharedPrefManager.saveMedicineList(medicineArrayList);
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
                Toast.makeText(AddMedicineActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddMedicineActivity.this, InventoryActivity.class));
            } else {
                Toast.makeText(AddMedicineActivity.this, "Error adding medicine!", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void back(View view){
        finish();
    }
}
