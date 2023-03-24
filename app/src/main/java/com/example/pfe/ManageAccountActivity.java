package com.example.pfe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ManageAccountActivity extends AppCompatActivity {
    TextView username, detail_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        username = findViewById(R.id.username);
        detail_email = findViewById(R.id.detail_email);
        username.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName()));
        detail_email.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail()));
    }
    public void back (View view) {
        startActivity(new Intent( ManageAccountActivity.this, HomepageActivity.class));
    }

}