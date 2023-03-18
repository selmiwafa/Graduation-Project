package com.example.pfe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

}