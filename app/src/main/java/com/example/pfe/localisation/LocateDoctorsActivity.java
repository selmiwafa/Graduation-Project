package com.example.pfe.localisation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pfe.HomepageActivity;
import com.example.pfe.R;
import com.example.pfe.manage_analyses.AddAnalysisActivity;
import com.example.pfe.manage_medicine.AddMedicineActivity;
import com.example.pfe.manage_medicine.BarcodeActivity;
import com.example.pfe.manage_medicine.InventoryActivity;
import com.example.pfe.manage_patient_account.MyPatientsActivity;
import com.example.pfe.manage_prescriptions.AddPrescriptionActivity;
import com.example.pfe.manage_prescriptions.MyPrescriptionsActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LocateDoctorsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Response response;
    String json;
    JSONObject jsonObject;
    String url;
    double latitude;
    double longitude;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_locate_doctors);
        createNavbar();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new ListFragment())
                .commit();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                // Do something with the location, such as updating a map view or sending the coordinates to a server.
                constructOverpassQuery();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            startLocationUpdates();
        }


    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }




    private void constructOverpassQuery() {
        // Construct the Overpass query
        String query = "[out:json];" +
                "(" +
                "  node[\"amenity\"=\"doctors\"](around:1000," + latitude + "," + longitude + ");" +
                "  node[\"amenity\"=\"hospital\"](around:1000," + latitude + "," + longitude + ");" +
                ");" +
                "out;";

// Send the query to the Overpass API
        OkHttpClient client = new OkHttpClient();
        try {
            url = "http://overpass-api.de/api/interpreter?data=" + URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            response = client.newCall(request).execute();
            json = response.body().string();
            jsonObject = new JSONObject(json);
            JSONArray elements = jsonObject.getJSONArray("elements");
            List<Doctor> doctors = new ArrayList<>();
            for (int i = 0; i < elements.length(); i++) {
                JSONObject element = elements.getJSONObject(i);
                if (!element.has("tags")) {
                    continue;
                }
                JSONObject tags = element.getJSONObject("tags");
                if (!tags.has("amenity")) {
                    continue;
                }
                String amenity = tags.getString("amenity");
                if (!"doctors".equals(amenity) && !"hospital".equals(amenity)) {
                    continue;
                }
                String name = tags.optString("name", "Unnamed");
                String address = tags.optString("addr:full", "");
                double elementLat = element.getDouble("lat");
                double elementLon = element.getDouble("lon");
                Doctor doctor = new Doctor(name, address, elementLat, elementLon);
                doctors.add(doctor);
            }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void createNavbar() {
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.home):
                Intent intent = new Intent(LocateDoctorsActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_patients):
                intent = new Intent(LocateDoctorsActivity.this, MyPatientsActivity.class);
                startActivity(intent);
                break;
            case (R.id.scan):
                intent = new Intent(LocateDoctorsActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            case (R.id.inventory):
                intent = new Intent(LocateDoctorsActivity.this, InventoryActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_med):
                intent = new Intent(LocateDoctorsActivity.this, AddMedicineActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_prescriptions):
                intent = new Intent(LocateDoctorsActivity.this, MyPrescriptionsActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_prescription):
                intent = new Intent(LocateDoctorsActivity.this, AddPrescriptionActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_analyses):
                break;
            case (R.id.add_analysis):
                intent = new Intent(LocateDoctorsActivity.this, AddAnalysisActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_doctors):
                break;
            case (R.id.locate_pharmacies):
                intent = new Intent(LocateDoctorsActivity.this, LocatePharmaciesActivity.class);
                startActivity(intent);
                break;

            case (R.id.make_doctor_appointment):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
            case (R.id.make_analysis_appointment):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
            case (R.id.request_donation):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
            case (R.id.propose_donation):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
            case (R.id.propose_diet):
                //intent = new Intent(InventoryActivity.this, AddPrescriptionActivity.class);
                //startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
}
