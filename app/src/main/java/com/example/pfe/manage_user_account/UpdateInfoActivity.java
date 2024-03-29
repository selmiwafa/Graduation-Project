package com.example.pfe.manage_user_account;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

public class UpdateInfoActivity extends AppCompatActivity {
    ProgressDialog dialog;
    EditText edName, edPassword, edBirthdate, edAdress, edNumber;
    Button btnUpdate;
    DatePickerDialog.OnDateSetListener setListener;
    JSONParser parser = new JSONParser();
    int success;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_update_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.initView();
        edBirthdate.setOnClickListener(v -> {
            int y = (Calendar.getInstance().get(Calendar.YEAR)) - 18;
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    UpdateInfoActivity.this
                    , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    , setListener, y, m, d);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener = (view, year, month, day) -> {
            month = month + 1;
            String date = day + "/" + month + "/" + year;
            edBirthdate.setText(date);
            int y = Calendar.getInstance().get(Calendar.YEAR);
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (year >= (y - 18) && month >= m && day >= d) {
                Toast.makeText(UpdateInfoActivity.this, "User should be older than 18 years old!", Toast.LENGTH_LONG).show();
                edBirthdate.setText("");
            }
        };
        btnUpdate.setOnClickListener(v -> updateUser());
    }
    void updateUser() {
        String name = edName.getText().toString();
        String birthdate = edBirthdate.getText().toString();
        String password = edPassword.getText().toString();
        String adress = edAdress.getText().toString();
        if (name.isEmpty() || birthdate.isEmpty() || password.isEmpty() || adress.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
        } else if (password.length() < 5) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Password must be at least 5 characters!", Toast.LENGTH_LONG).show();
        } else {
            new Update().execute();
        }
    }
    @SuppressLint("StaticFieldLeak")
    class Update extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UpdateInfoActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("email", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            map.put("name", edName.getText().toString());
            map.put("birthdate", edBirthdate.getText().toString());
            map.put("password", edPassword.getText().toString());
            map.put("adress", edAdress.getText().toString());
            map.put("number", edNumber.getText().toString());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/user/update.php", "GET", map);
                success = object.getInt("success");
                JSONArray userJson = object.getJSONArray("user");
                JSONObject jsonObject = userJson.getJSONObject(0);
                User user = new User(
                        jsonObject.getString("email"),
                        jsonObject.getString("name"),
                        jsonObject.getString("birthdate"),
                        jsonObject.getString("password"),
                        jsonObject.getString("adress"),
                        jsonObject.getString("number")
                );
                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if (success == 1) {
                Toast.makeText(UpdateInfoActivity.this, "Update account successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateInfoActivity.this, ManageAccountActivity.class));
            } else {
                Toast.makeText(UpdateInfoActivity.this, "Error updating!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void initView() {
        edName = findViewById(R.id.username);
        edBirthdate = findViewById(R.id.birthdate);
        edPassword = findViewById(R.id.password);
        edAdress = findViewById(R.id.adress);
        edNumber = findViewById(R.id.number);
        btnUpdate = findViewById(R.id.update_infoBtn);
        edName.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName());
        edAdress.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getAdress());
        edBirthdate.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getBirthdate());
        edNumber.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getNumber());
    }
    public void backManage(View view) {
        startActivity(new Intent(UpdateInfoActivity.this, ManageAccountActivity.class));
    }
}