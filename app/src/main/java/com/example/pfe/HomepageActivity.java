package com.example.pfe;

import static com.example.pfe.R.layout.user_details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomepageActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    TextView username, detail_email;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_home_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_menu);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) item -> {
            switch (item.getItemId()) {
                case R.id.patient1:
                {
                    Toast.makeText(HomepageActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                }
                case R.id.home:
                {
                    Toast.makeText(HomepageActivity.this, "Home", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            return false;
        });
    }

    public void setSupportActionBar(Toolbar myToolbar) {
    }

    public void logout(View view) {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
    }
    public void OpenManageAccount (View view) {
        startActivity(new Intent( HomepageActivity.this, ManageAccountActivity.class));
    }
    public void createUserDialog(View view) {
        dialogBuilder=new AlertDialog.Builder(this);
        final View contactPopupView=getLayoutInflater().inflate(user_details,null);
        dialogBuilder.setView(contactPopupView);
        dialog=dialogBuilder.create();
        dialog.show();
        username = dialog.findViewById(R.id.username);
        detail_email = dialog.findViewById(R.id.detail_email);
        username.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName()));
        detail_email.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail()));
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void cancel (View view){
        dialog.dismiss();
    }
}
