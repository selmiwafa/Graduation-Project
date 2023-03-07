package com.example.pfe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (name.isEmpty() || email.isEmpty() || birthdate.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || adress.isEmpty()) {
            Toast.makeText(this, "All fields required!", Toast.LENGTH_SHORT).show();
        }
        else if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid e-mail format!", Toast.LENGTH_SHORT).show();
        }
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

    public void OpenLoginPage(View view) {
        startActivity(new Intent( SignupActivity.this, LoginActivity.class));
    }
}
