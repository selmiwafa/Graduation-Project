package com.example.pfe.manage_user_account;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.R;

public class ForgetActivity extends AppCompatActivity {
    EditText edEmail;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_forget);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        edEmail= findViewById(R.id.email);
        btn = findViewById(R.id.forget);
        //btn.setOnClickListener(v -> {
            /*try {
                ParseUser.requestPasswordReset(edEmail.getText().toString());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(ForgetActivity.this, "Reset link sent!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ForgetActivity.this, UpdateInfoActivity.class));
        });*/

    }



}