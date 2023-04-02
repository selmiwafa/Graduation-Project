package com.example.pfe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmailLogin;
    private EditText edPasswordLogin;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    int success, number = 0;
    String message;
   // String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent( LoginActivity.this, HomepageActivity.class));
            return;
        }
        this.initView();

    }
    public void login(View view) {
        String email = edEmailLogin.getText().toString();
        String password = edPasswordLogin.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
        }
        else if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Invalid e-mail format!", Toast.LENGTH_LONG).show();
        }
        else {
            new LoginActivity.Log().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class Log extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {

            HashMap<String,String> map= new HashMap<>();
            map.put("email",edEmailLogin.getText().toString());
            map.put("password",edPasswordLogin.getText().toString());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
               // JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/user/log.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/user/log.php", "GET", map);
                success = object.getInt("success");
                message = object.getString("message");
                while (success == 1) {
                    JSONArray userJson = object.getJSONArray("user");
                    JSONObject jsonObject = userJson.getJSONObject(0);
                    User user = new User(
                            jsonObject.getString("email"),
                            jsonObject.getString("name"),
                            jsonObject.getString("birthdate"),
                            jsonObject.getString("password"),
                            jsonObject.getString("adress")
                    );
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                    number = object.getInt("number");
                    if (number == 1) {
                        SharedPrefManager.getInstance(getApplicationContext()).setKeyNumberPatients(1);
                        JSONArray patientsJson = object.getJSONArray("patients");
                        JSONObject patientJson = patientsJson.getJSONObject(0);
                        Patient patient = new Patient(
                                patientJson.getString("patient_name"),
                                patientJson.getInt("patient_age"),
                                patientJson.getString("relationship")
                        );
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient1(patient);
                    } else if (number >= 2) {
                        SharedPrefManager.getInstance(getApplicationContext()).setKeyNumberPatients(2);
                        JSONArray patientsJson = object.getJSONArray("patients");
                        JSONObject patient1Json = patientsJson.getJSONObject(0);
                        Patient patient1 = new Patient(
                                patient1Json.getString("patient_name"),
                                patient1Json.getInt("patient_age"),
                                patient1Json.getString("relationship")
                        );
                        JSONObject patient2Json = patientsJson.getJSONObject(1);
                        Patient patient2 = new Patient(
                                patient2Json.getString("patient_name"),
                                patient2Json.getInt("patient_age"),
                                patient2Json.getString("relationship")
                        );
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient1(patient1);
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient2(patient2);
                    }
                    break;
                }
                connection.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            dialog.cancel();
            if(success == 1)
            {
                Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_LONG).show();
                startActivity(new Intent( LoginActivity.this, HomepageActivity.class));
            }
            else {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                restartActivity(LoginActivity.this);
            }
        }

    }

    private void initView() {
        edEmailLogin = findViewById(R.id.edEmailLogin);
        edPasswordLogin = findViewById(R.id.edPasswordLogin);
    }

    public boolean isValidEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }
    public void OpenSignupPage(View view) {
        startActivity(new Intent( LoginActivity.this, SignupActivity.class));
    }
    public void OpenForgetPage(View view) {
        startActivity(new Intent( LoginActivity.this, ForgetActivity.class));
    }
    public void OpenHomePage(View view) {
        startActivity(new Intent( LoginActivity.this, HomepageActivity.class));
    }
    public static void restartActivity(Activity activity){
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }

}
