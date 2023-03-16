package com.example.pfe;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText edName;
    private EditText edEmail;
    private EditText edBirthdate;
    private EditText edPassword;
    private EditText edConfirmPassword;
    private EditText edAdress;
    private Button btnSignin;
    DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_signup);

        this.initView();
        edBirthdate.setOnClickListener(v -> {
            int y = Calendar.getInstance().get(Calendar.YEAR);
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SignupActivity.this
                    , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    , setListener, y, m, d);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener= (view, year, month, day) -> {
            month = month+1;
            String date = day+"/"+month+"/"+year;
            edBirthdate.setText(date);
            int y = Calendar.getInstance().get(Calendar.YEAR);
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (year>=(y-18) && month>=m && day>=d){
                Toast.makeText(SignupActivity.this, "User should be older than 18 years old!", Toast.LENGTH_LONG).show();
                edBirthdate.setText("");
            }
        };
        btnSignin.setOnClickListener(v -> addUser());
    }

    private void addUser() {
        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String birthdate= edBirthdate.getText().toString();
        String password = edPassword.getText().toString();
        String confirmPassword = edConfirmPassword.getText().toString();
        String adress = edAdress.getText().toString();

        confirmPassword(password, confirmPassword);

        if (name.isEmpty() || email.isEmpty() || birthdate.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || adress.isEmpty()) {
            Toast.makeText(this, "All fields required!", Toast.LENGTH_LONG).show();
        }
        else if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid e-mail format!", Toast.LENGTH_LONG).show();
        }
        else if (password.length()<8) {
            Toast.makeText(this, "Password must be at least 8 characters!", Toast.LENGTH_LONG).show();
        }
        else if (confirmPassword(password, confirmPassword)) {
            Toast.makeText(this, "Confirm Password doesn't match password!!", Toast.LENGTH_LONG).show();
        }

        @SuppressLint("StaticFieldLeak")
        class AddUser extends AsyncTask<Void, Void, String> {
            private ProgressBar progressBar;
            private LinearLayout PB;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("name", name);
                params.put("password", password);
                params.put("birthdate", birthdate);
                params.put("adress", adress);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);


            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                LinearLayout PB = (LinearLayout) findViewById(R.id.progressBarLayout);
                PB.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getString("email"),
                                userJson.getString("name"),
                                userJson.getString("adress"),
                                userJson.getString("birthdate"),
                                userJson.getString("password")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        AddUser ru = new AddUser();
        ru.execute();



    }

    private void initView() {
        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edBirthdate = findViewById(R.id.edBirthdate);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        edAdress = findViewById(R.id.edAdress);
        btnSignin = findViewById(R.id.btnSignin);
    }

    public boolean isValidEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public boolean confirmPassword(String password, String confirm){
        return !confirm.matches(password);
    }


    public void OpenLoginPage(View view) {
        startActivity(new Intent( SignupActivity.this, LoginActivity.class));
    }
    public void OpenHomePage(View view) {
        startActivity(new Intent( SignupActivity.this, HomepageActivity.class));
    }
}
