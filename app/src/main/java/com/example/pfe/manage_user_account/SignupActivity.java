package com.example.pfe.manage_user_account;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText edName;
    private EditText edEmail;
    private EditText edBirthdate;
    private EditText edPassword;
    private EditText edConfirmPass;
    private EditText edAdress, edNumber;
    private Button btnSignin;
    boolean verify;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    DatePickerDialog.OnDateSetListener setListener;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_signup);

        this.initView();
        edBirthdate.setOnClickListener(v -> {
            int y = Calendar.getInstance().get(Calendar.YEAR) - 18;
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SignupActivity.this
                    , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    , setListener, y, m, d);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener = (view, year, month, day) -> {
            month = month + 1;
            String date = day + "/" + month + "/" + year;
            edBirthdate.setText(date);
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (year >= (y - 18) && month >= m && day >= d) {
                Toast.makeText(SignupActivity.this, "User should be older than 18 years old!", Toast.LENGTH_LONG).show();
                edBirthdate.setText("");
            }
        };
        btnSignin.setOnClickListener(v -> addUser());
    }

    void addUser() {
        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String birthdate = edBirthdate.getText().toString();
        String password = edPassword.getText().toString();
        String confirmPassword = edConfirmPass.getText().toString();
        String adress = edAdress.getText().toString();
        String number = edNumber.getText().toString();

        confirmPassword(password, confirmPassword);

        if (name.isEmpty() || email.isEmpty() || birthdate.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || adress.isEmpty()  || number.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Invalid e-mail format!", Toast.LENGTH_LONG).show();
        } else if (password.length() < 5) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Password must be at least 5 characters!", Toast.LENGTH_LONG).show();
        } else if (confirmPassword(password, confirmPassword)) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Confirm Password doesn't match password!!", Toast.LENGTH_LONG).show();
        } else {
            loginUser(email,password);
        }
    }
    private void loginUser(String userName, String password) {
        // calling a method to login a user.
        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setEmail(userName);
        user.setPassword(password);
        // after login checking if the user is null or not.
        user.signUpInBackground((SignUpCallback) e -> {
            if (e == null) {
                ParseUser.logOut();
                Toast.makeText(SignupActivity.this, "Sign up sucessful! Please verify mail and login!", Toast.LENGTH_LONG).show();
                new Add().execute();
                }
            else
            {
                Toast.makeText(SignupActivity.this, "Can't create your account", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class Add extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SignupActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            /*if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(SignupActivity.this));
            }
            Object py = Python.getInstance();*/

            HashMap<String, String> map = new HashMap<>();
            map.put("email", edEmail.getText().toString());
            map.put("name", edName.getText().toString());
            map.put("birthdate", edBirthdate.getText().toString());
            map.put("password", edPassword.getText().toString());
            map.put("adress", edAdress.getText().toString());
            map.put("number", edNumber.getText().toString());

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/user/add.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/user/add.php", "GET", map);
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
                //startActivity(new Intent(SignupActivity.this, VerifyActivity.class));
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            } else {
                Toast.makeText(SignupActivity.this, "Error signing up!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initView() {
        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edBirthdate = findViewById(R.id.edBirthdate);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPass = findViewById(R.id.edConfirmPass);
        edAdress = findViewById(R.id.edAdress);
        edNumber = findViewById(R.id.edNumber);
        btnSignin = findViewById(R.id.btnSignin);

    }

    public boolean isValidEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public boolean confirmPassword(String password, String confirm) {
        return !confirm.matches(password);
    }


    public void OpenLoginPage() {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
    }
}
