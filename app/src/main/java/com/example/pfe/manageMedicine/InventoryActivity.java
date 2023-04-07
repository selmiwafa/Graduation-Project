package com.example.pfe.manageMedicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfe.HomepageActivity;
import com.example.pfe.JSONParser;
import com.example.pfe.Medicine;
import com.example.pfe.MyPatientsActivity;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private RecyclerView medicineRecyclerView;
    Toolbar toolbar;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success, number;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_inventory);

        List<Medicine> medicineList = new ArrayList<>();



        // Add medicine to the list
        medicineList.add(new Medicine("000000000", "Inflamil",5,"","12/3/2025"));
        medicineList.add(new Medicine("000000000", "Clamoxyl",5,"","12/3/2025"));
        medicineList.add(new Medicine("000000000", "Augmentin",5,"","12/3/2025"));
        medicineList.add(new Medicine("000000000", "Aspégic",5,"","12/3/2025"));

        RecyclerView medicineRecyclerView = findViewById(R.id.medicine_list);
        medicineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MedicineAdapter medicineAdapter = new MedicineAdapter(medicineList);
        medicineRecyclerView.setAdapter(medicineAdapter);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.home):
                Intent intent = new Intent(InventoryActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_patients):
                intent = new Intent(InventoryActivity.this, MyPatientsActivity.class);
                startActivity(intent);
                break;
            case (R.id.scan):
                intent = new Intent(InventoryActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            case (R.id.inventory):
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
    public void OpenAddMedicine(View view){
        Intent intent = new Intent(InventoryActivity.this, AddMedicineActivity.class);
        startActivity(intent);
    }
    @SuppressLint("StaticFieldLeak")
    class Select extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog dialog = new ProgressDialog(InventoryActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("user", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/addMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/medicine/selectMedicine.php", "GET", map);
                success = object.getInt("success");
                number = object.getInt("number");
                while (success == 1) {
                    JSONArray userJson = object.getJSONArray("medicine");
                    List<Medicine> medicineList = new ArrayList<>();
                    for(int i=0;i<number;i++) {
                        JSONObject jsonObject = userJson.getJSONObject(i);
                        Medicine medicine = new Medicine(
                                jsonObject.getString("barcode"),
                                jsonObject.getString("med_name"),
                                jsonObject.getInt("quantity"),
                                jsonObject.getString("description"),
                                jsonObject.getString("exp_date")
                        );
                        medicineList.add(medicine);
                    }
                    RecyclerView medicineRecyclerView = findViewById(R.id.medicine_list);
                    medicineRecyclerView.setLayoutManager(new LinearLayoutManager(InventoryActivity.this));
                    MedicineAdapter medicineAdapter = new MedicineAdapter(medicineList);
                    medicineRecyclerView.setAdapter(medicineAdapter);
                }
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if (success == 1) {
                Toast.makeText(InventoryActivity.this, "Selected successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(InventoryActivity.this, InventoryActivity.class));
            } else {
                Toast.makeText(InventoryActivity.this, "Error selecting medicine!", Toast.LENGTH_LONG).show();
            }
        }
    }
}