package com.example.pfe.manage_user_account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

public class VerifyActivity extends AppCompatActivity {
    EditText edCode;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_verify);

        edCode = findViewById(R.id.edCode);
        String code = edCode.getText().toString();

        if (code == SharedPrefManager.getInstance(getApplicationContext()).getUser().getCode()) {
            startActivity(new Intent(VerifyActivity.this, LoginActivity.class));
        } else {
            Toast.makeText(this, "Wrong verification code!", Toast.LENGTH_SHORT).show();
        }

    }
}