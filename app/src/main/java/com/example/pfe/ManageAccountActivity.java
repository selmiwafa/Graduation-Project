package com.example.pfe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.manage_account.UpdateInfoActivity;

public class ManageAccountActivity extends AppCompatActivity {
    TextView username, detail_email;
    Button Update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        username = findViewById(R.id.username);
        detail_email = findViewById(R.id.detail_email);
        Update = findViewById(R.id.update_account_information);
        username.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName()));
        detail_email.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail()));
    }
    public void backHomepage (View view) {
        startActivity(new Intent( ManageAccountActivity.this, HomepageActivity.class));
    }
    public void openPageUpdate (View view) {
        startActivity(new Intent( ManageAccountActivity.this, UpdateInfoActivity.class));
    }


}