package com.example.pfe.manage_account;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;
import com.example.pfe.SignupActivity;

public class UpdateInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_update_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    void updateUser() {
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
            Toast.makeText(getApplicationContext().getApplicationContext(), "Password must be at least 8 characters!", Toast.LENGTH_LONG).show();
        }
        else if (confirmPassword(password, confirmPassword)) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Confirm Password doesn't match password!!", Toast.LENGTH_LONG).show();
        }
        else {
            new SignupActivity.Add().execute();
        }
    }
    private void initView() {
        edName = findViewById(R.id.username);
        edBirthdate = findViewById(R.id.birthdate);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPass = findViewById(R.id.edConfirmPass);
        edAdress = findViewById(R.id.edAdress);
        btnSignin = findViewById(R.id.btnSignin);

    }
}