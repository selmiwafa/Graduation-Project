package com.example.pfe;


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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText edName;
    private EditText edEmail;
    private EditText edBirthdate;
    private EditText edPassword;
    private EditText edConfirmPass;
    private EditText edAdress;
    private Button btnSignin;
    boolean verify;
    DatePickerDialog.OnDateSetListener setListener;
    ProgressDialog dialog;
    JSONParser parser=new JSONParser();
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_signup);

        this.initView();
        edBirthdate.setOnClickListener(v -> {
            int y = Calendar.getInstance().get(Calendar.YEAR)-18;
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
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (year>=(y-18) && month>=m && day>=d){
                Toast.makeText(SignupActivity.this, "User should be older than 18 years old!", Toast.LENGTH_LONG).show();
                edBirthdate.setText("");
            }
        };
        btnSignin.setOnClickListener(v -> addUser());
    }

    void addUser() {
        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String birthdate= edBirthdate.getText().toString();
        String password = edPassword.getText().toString();
        String confirmPassword = edConfirmPass.getText().toString();
        String adress = edAdress.getText().toString();

        confirmPassword(password, confirmPassword);

        if (name.isEmpty() || email.isEmpty() || birthdate.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || adress.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
        }
        else if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Invalid e-mail format!", Toast.LENGTH_LONG).show();
        }
        else if (password.length()<5) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Password must be at least 5 characters!", Toast.LENGTH_LONG).show();
        }
        else if (confirmPassword(password, confirmPassword)) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Confirm Password doesn't match password!!", Toast.LENGTH_LONG).show();
        }
        else {
            new Add().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class Add extends AsyncTask<String,String,String>
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(SignupActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();


        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPrefManager.getInstance(getApplicationContext()).getUser().setCode();
            SharedPrefManager.getInstance(getApplicationContext()).getUser().getCode();
            SendEmail verification = new SendEmail();
            verify = verification.sendEmail(SharedPrefManager.getInstance(getApplicationContext()).getUser());
            if (verify) {

                HashMap<String, String> map = new HashMap<>();
                map.put("email", edEmail.getText().toString());
                map.put("name", edName.getText().toString());
                map.put("birthdate", edBirthdate.getText().toString());
                map.put("password", edPassword.getText().toString());
                map.put("adress", edAdress.getText().toString());

                JSONObject object = parser.makeHttpRequest("http://10.0.2.2/healthbuddy/user/add.php", "GET", map);
                try {
                    success = object.getInt("success");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(SignupActivity.this, "Verification mail not sent.", Toast.LENGTH_SHORT).show();
            }


            return null;

        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            dialog.cancel();

            if(success==1)
            {
                Toast.makeText(SignupActivity.this, "Sign up successfull", Toast.LENGTH_LONG).show();
                startActivity(new Intent(SignupActivity.this, VerifyActivity.class));
            }
            else
            {
                Toast.makeText(SignupActivity.this,"Error signing up!",Toast.LENGTH_LONG).show();
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
        btnSignin = findViewById(R.id.btnSignin);

    }

    public boolean isValidEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public boolean confirmPassword(String password, String confirm){
        return !confirm.matches(password);
    }


    public void OpenLoginPage() {
        startActivity(new Intent( SignupActivity.this, LoginActivity.class));
    }
}
