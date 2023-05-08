package com.example.pfe;

import static com.example.pfe.R.layout.user_details;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.pfe.appointments.Appointment;
import com.example.pfe.appointments.MyAppointmentsActivity;
import com.example.pfe.appointments.analysis_appointments.AnalysisAppointmentActivity;
import com.example.pfe.appointments.doctor_appointments.DoctorAppointmentActivity;
import com.example.pfe.diet.DietActivity;
import com.example.pfe.donations.Donation;
import com.example.pfe.donations.DonationRequest;
import com.example.pfe.donations.MyDonationsActivity;
import com.example.pfe.donations.ProposeDonationActivity;
import com.example.pfe.donations.RequestDonationActivity;
import com.example.pfe.links.Card;
import com.example.pfe.links.CardAdapter;
import com.example.pfe.localisation.LocateDoctorsActivity;
import com.example.pfe.localisation.LocatePharmaciesActivity;
import com.example.pfe.manage_analyses.AddAnalysisActivity;
import com.example.pfe.manage_analyses.Analysis;
import com.example.pfe.manage_analyses.MyAnalysesActivity;
import com.example.pfe.manage_medicine.AddMedicineActivity;
import com.example.pfe.manage_medicine.BarcodeActivity;
import com.example.pfe.manage_medicine.InventoryActivity;
import com.example.pfe.manage_medicine.Medicine;
import com.example.pfe.manage_patient_account.MyPatientsActivity;
import com.example.pfe.manage_patient_account.Patient;
import com.example.pfe.manage_prescriptions.AddPrescriptionActivity;
import com.example.pfe.manage_prescriptions.MyPrescriptionsActivity;
import com.example.pfe.manage_prescriptions.PresMedicine;
import com.example.pfe.manage_prescriptions.Prescription;
import com.example.pfe.manage_user_account.ManageAccountActivity;
import com.example.pfe.manage_user_account.User;
import com.example.pfe.news.News;
import com.example.pfe.news.NewsAdapter;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AlertDialog dialog;
    ProgressDialog dialogg;
    JSONParser parser = new JSONParser();
    int success, number;
    String message;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    TextView username, detail_email;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_home_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        new Login().execute();

        createNavbar();
        ImageView imageView = findViewById(R.id.gif_imageview);

        Glide.with(this)
                .load(R.raw.hello)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource instanceof GifDrawable) {
                            GifDrawable gifDrawable = (GifDrawable) resource;
                            gifDrawable.setLoopCount(GifDrawable.LOOP_FOREVER);
                            gifDrawable.start();
                        }
                        return false;
                    }
                })
                .into(imageView);
        TextView userTv = findViewById(R.id.nameTv);
        userTv.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName());

        RecyclerView mRecyclerView;
        mRecyclerView = findViewById(R.id.news_list);
        LinearLayoutManager linearlayoutmanager = new LinearLayoutManager(HomepageActivity.this);
        mRecyclerView.setLayoutManager(linearlayoutmanager);

        ArrayList<News> news = SharedPrefManager.getInstance(getApplicationContext()).getNews();
        NewsAdapter adapter = new NewsAdapter(HomepageActivity.this, news);
        mRecyclerView.setAdapter(adapter);



        RecyclerView myRecyclerView = findViewById(R.id.recycler_view);
        myRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize card data
        ArrayList mCardList = new ArrayList<>();
        mCardList.add(new Card("My patients", MyPatientsActivity.class));

        mCardList.add(new Card("My Prescriptions", MyPrescriptionsActivity.class));

        mCardList.add(new Card("Inventory", InventoryActivity.class));
        mCardList.add(new Card("My Analyses", MyAnalysesActivity.class));
        mCardList.add(new Card("Nearby doctors", LocateDoctorsActivity.class));
        mCardList.add(new Card("Nearby pharmacies", LocatePharmaciesActivity.class));

        // add more cards here

        // Initialize adapter
        CardAdapter mCardAdapter = new CardAdapter(mCardList);
        mRecyclerView.setAdapter(mCardAdapter);



    }


    public void logout(View view) {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
    }


    public void OpenManageAccount(View view) {
        startActivity(new Intent(HomepageActivity.this, ManageAccountActivity.class));
    }
    public void createUserDialog(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(user_details, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        username = dialog.findViewById(R.id.username);
        detail_email = dialog.findViewById(R.id.detail_email);
        showInfo(username, detail_email);

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



    public void cancel(View view) {
        dialog.dismiss();
    }

    public void showInfo(TextView username, TextView email) {
        username.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName()));
        email.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail()));
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
                break;
            case (R.id.my_patients):
                Intent intent = new Intent(HomepageActivity.this, MyPatientsActivity.class);
                startActivity(intent);
                break;
            case (R.id.scan):
                intent = new Intent(HomepageActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            case (R.id.inventory):
                intent = new Intent(HomepageActivity.this, InventoryActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_med):
                intent = new Intent(HomepageActivity.this, AddMedicineActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_prescriptions):
                intent = new Intent(HomepageActivity.this, MyPrescriptionsActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_prescription):
                intent = new Intent(HomepageActivity.this, AddPrescriptionActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_analyses):
                intent = new Intent(HomepageActivity.this, MyAnalysesActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_analysis):
                intent = new Intent(HomepageActivity.this, AddAnalysisActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_doctors):
                intent = new Intent(HomepageActivity.this, LocateDoctorsActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_pharmacies):
                intent = new Intent(HomepageActivity.this, LocatePharmaciesActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_appointments):
                intent = new Intent(HomepageActivity.this, MyAppointmentsActivity.class);
                startActivity(intent);
                break;
            case (R.id.make_doctor_appointment):
                intent = new Intent(HomepageActivity.this, DoctorAppointmentActivity.class);
                startActivity(intent);
                break;
            case (R.id.make_analysis_appointment):
                intent = new Intent(HomepageActivity.this, AnalysisAppointmentActivity.class);
                startActivity(intent);
                break;
            case (R.id.request_donation):
                intent = new Intent(HomepageActivity.this, RequestDonationActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_donations):
                intent = new Intent(HomepageActivity.this, MyDonationsActivity.class);
                startActivity(intent);
                break;
            case (R.id.propose_donation):
                intent = new Intent(HomepageActivity.this, ProposeDonationActivity.class);
                startActivity(intent);
                break;
            case (R.id.propose_diet):
                intent = new Intent(HomepageActivity.this, DietActivity.class);
                startActivity(intent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
    @SuppressLint("StaticFieldLeak")
    class Login extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            number = 0;
            HashMap<String, String> map = new HashMap<>();
            map.put("email", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            map.put("password", SharedPrefManager.getInstance(getApplicationContext()).getUser().getPassword());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/user/log.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/user/log.php", "GET", map);
                success = object.getInt("success");
                message = object.getString("message");
                number = object.getInt("number_patients");
                while (success == 1) {
                    JSONArray userJson = object.getJSONArray("user");
                    JSONObject jsonObject = userJson.getJSONObject(0);
                    User user = new User(
                            jsonObject.getString("email"),
                            jsonObject.getString("name"),
                            jsonObject.getString("birthdate"),
                            jsonObject.getString("password"),
                            jsonObject.getString("adress"),
                            jsonObject.getString("number")
                    );
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                    if (number == 1) {
                        SharedPrefManager.getInstance(getApplicationContext()).setKeyNumberPatients(1);
                        JSONArray patientsJson = object.getJSONArray("patients");
                        JSONObject patientJson = patientsJson.getJSONObject(0);
                        Patient patient = new Patient(
                                patientJson.getString("patient_name"),
                                patientJson.getInt("patient_age"),
                                patientJson.getString("relationship")
                        );
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient1(patient);
                    } else if (number >= 2) {
                        SharedPrefManager.getInstance(getApplicationContext()).setKeyNumberPatients(2);
                        JSONArray patientsJson = object.getJSONArray("patients");
                        JSONObject patient1Json = patientsJson.getJSONObject(0);
                        Patient patient1 = new Patient(
                                patient1Json.getString("patient_name"),
                                patient1Json.getInt("patient_age"),
                                patient1Json.getString("relationship")
                        );
                        JSONObject patient2Json = patientsJson.getJSONObject(1);
                        Patient patient2 = new Patient(
                                patient2Json.getString("patient_name"),
                                patient2Json.getInt("patient_age"),
                                patient2Json.getString("relationship")
                        );
                        int number2 = object.getInt("number_medicine");
                        ArrayList<Medicine> medicineList = new ArrayList<>();
                        JSONArray medicineJson = object.getJSONArray("medicine");
                        for (int i = 0; i < number2; i++) {
                            JSONObject jsonObject2 = medicineJson.getJSONObject(i);
                            Medicine medicine = new Medicine(
                                    jsonObject2.getString("barcode"),
                                    jsonObject2.getString("med_name"),
                                    jsonObject2.getInt("quantity"),
                                    jsonObject2.getString("description"),
                                    jsonObject2.getString("exp_date")
                            );
                            medicineList.add(medicine);
                        }
                        int number3 = object.getInt("number_analyses");
                        ArrayList<Analysis> analysisArrayList = new ArrayList<>();
                        JSONArray analysisJson = object.getJSONArray("user_analyses");
                        for (int i = 0; i < number3; i++) {
                            JSONObject jsonObject2 = analysisJson.getJSONObject(i);
                            Analysis analysis = new Analysis(
                                    jsonObject2.getString("analysis_name"),
                                    jsonObject2.getString("analysis_date"),
                                    jsonObject2.getString("result"),
                                    jsonObject2.getString("owner")
                            );
                            analysisArrayList.add(analysis);
                        }

                        int number4 = object.getInt("number_donations");
                        ArrayList<Donation> donations = new ArrayList<>();
                        JSONArray donationJson = object.getJSONArray("donations");
                        for (int i = 0; i < number4; i++) {
                            JSONObject jsonObject3 = donationJson.getJSONObject(i);
                            Donation donation = new Donation(
                                    jsonObject3.getString("id"),
                                    jsonObject3.getString("barcode"),
                                    Integer.parseInt(jsonObject3.getString("quantity")),
                                    jsonObject3.getString("date"));
                            donations.add(donation);
                        }

                        int number5 = object.getInt("number_apps");
                        ArrayList<Appointment> appointments = new ArrayList<>();
                        JSONArray appointmentJson = object.getJSONArray("appointments");
                        for (int i = 0; i < number5; i++) {
                            JSONObject jsonObject4 = appointmentJson.getJSONObject(i);
                            Appointment appointment = new Appointment(
                                    jsonObject4.getString("app_name"),
                                    jsonObject4.getString("app_date"),
                                    jsonObject4.getString("app_time"),
                                    jsonObject4.getString("app_type"),
                                    jsonObject4.getString("owner")
                            );
                            appointments.add(appointment);
                        }

                        int number6 = object.getInt("number_prescriptions");
                        ArrayList<Prescription> prescriptions = new ArrayList<>();
                        JSONArray prescriptionJson = object.getJSONArray("prescriptions");

                        HashSet<String> uniquePresIds = new HashSet<>();

                        for (int i = 0; i < number6; i++) {
                            JSONObject jsonObject4 = prescriptionJson.getJSONObject(i);
                            String presId = jsonObject4.getString("pres_name");
                            ArrayList<PresMedicine> presMedicines = new ArrayList<>();
                            HashSet<String> uniqueBarcodes = new HashSet<>();

                            for (int j = 0; j < number6; j++) {
                                JSONObject presMed = prescriptionJson.getJSONObject(j);
                                if (presMed.getString("pres_name").equals(presId)) {
                                    String barcode = presMed.getString("barcode");
                                    String med_name = presMed.getString("med_name");
                                    // Check if barcode has already been added
                                    if (uniqueBarcodes.contains(barcode)) {
                                        continue;  // Skip this PresMedicine object
                                    } else {
                                        uniqueBarcodes.add(barcode);  // Add new barcode to set
                                    }

                                    PresMedicine presMedecine = new PresMedicine(
                                            barcode,
                                            med_name,
                                            Integer.parseInt(presMed.getString("dose")),
                                            Integer.parseInt(presMed.getString("frequency")),
                                            Integer.parseInt(presMed.getString("period")),
                                            Integer.parseInt(presMed.getString("times_per_week")),
                                            presMed.getString("other_details")
                                    );
                                    presMedicines.add(presMedecine);
                                }
                            }

                            // Check if prescription ID has already been added
                            if (uniquePresIds.contains(presId)) {
                                continue;  // Skip this Prescription object
                            } else {
                                uniquePresIds.add(presId);  // Add new prescription ID to set
                            }

                            Prescription prescription = new Prescription(
                                    presId,
                                    jsonObject4.getString("start_date"),
                                    jsonObject4.getString("end_date"),
                                    jsonObject4.getString("owner"),
                                    presMedicines
                            );

                            prescriptions.add(prescription);
                        }

                        int number = object.getInt("number_requests");
                        ArrayList<DonationRequest> requests = new ArrayList<>();
                        JSONArray requestJson = object.getJSONArray("requests");
                        for (int i = 0; i < number; i++) {
                            JSONObject jsonObject7 = requestJson.getJSONObject(i);
                            DonationRequest donation = new DonationRequest(
                                    jsonObject7.getString("id"),
                                    jsonObject7.getString("barcode"),
                                    Integer.parseInt(jsonObject7.getString("quantity")),
                                    jsonObject7.getString("date"));
                            requests.add(donation);
                        }

                        int number8 = object.getInt("number_news");
                        ArrayList<News> news = new ArrayList<>();
                        JSONArray newsJson = object.getJSONArray("news");
                        for (int i = 0; i < number8; i++) {
                            JSONObject jsonObject6 = newsJson.getJSONObject(i);
                            String dateStr = jsonObject6.getString("date");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            News news1 = new News(
                                    jsonObject6.getInt("id"),
                                    jsonObject6.getString("title"),
                                    jsonObject6.getString("content"),
                                    sdf.parse(dateStr)
                            );
                            news.add(news1);
                        }

                        SharedPrefManager.saveNews(news);
                        SharedPrefManager.savePrescriptionList(prescriptions);
                        SharedPrefManager.saveAppointmentList(appointments);
                        SharedPrefManager.saveDonationList(donations);
                        SharedPrefManager.saveRequestList(requests);
                        SharedPrefManager.saveAnalysisList(analysisArrayList);
                        SharedPrefManager.saveMedicineList(medicineList);
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient1(patient1);
                        SharedPrefManager.getInstance(getApplicationContext()).addPatient2(patient2);
                    }
                    break;
                }
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } catch (JSONException | ParseException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
}
