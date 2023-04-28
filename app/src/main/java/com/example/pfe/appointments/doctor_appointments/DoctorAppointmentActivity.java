package com.example.pfe.appointments.doctor_appointments;

import static com.example.pfe.R.layout.user_details;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.HomepageActivity;
import com.example.pfe.JSONParser;
import com.example.pfe.MyReminderService;
import com.example.pfe.R;
import com.example.pfe.Reminder;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.appointments.Appointment;
import com.example.pfe.appointments.MyAppointmentsActivity;
import com.example.pfe.appointments.analysis_appointments.AnalysisAppointmentActivity;
import com.example.pfe.diet.DietActivity;
import com.example.pfe.donations.MyDonationsActivity;
import com.example.pfe.donations.ProposeDonationActivity;
import com.example.pfe.donations.RequestDonationActivity;
import com.example.pfe.localisation.LocateDoctorsActivity;
import com.example.pfe.localisation.LocatePharmaciesActivity;
import com.example.pfe.manage_analyses.AddAnalysisActivity;
import com.example.pfe.manage_analyses.MyAnalysesActivity;
import com.example.pfe.manage_medicine.AddMedicineActivity;
import com.example.pfe.manage_medicine.BarcodeActivity;
import com.example.pfe.manage_medicine.InventoryActivity;
import com.example.pfe.manage_patient_account.MyPatientsActivity;
import com.example.pfe.manage_prescriptions.AddPrescriptionActivity;
import com.example.pfe.manage_prescriptions.MyPrescriptionsActivity;
import com.example.pfe.manage_user_account.ManageAccountActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DoctorAppointmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    DrawerLayout drawerLayout;
    NotificationManagerCompat notificationManager;
    NavigationView navigationView;
    Toolbar toolbar;
    EditText edDoctorName, edDate, edTime;
    Spinner edOwner;
    ProgressDialog dialog;
    Button saveBtn;
    ImageButton topSaveBtn;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success;
    private RecyclerView mRecyclerView;
    private AlertDialog Userdialog;

    TextView username, detail_email;
    TimePickerDialog.OnTimeSetListener setTimeListener;
    DatePickerDialog.OnDateSetListener setListener;
    String owner;
    ArrayList<Reminder> reminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_doctor_appointment);
        createNavbar();
        initView();
        createSpinner();

        Button btnScheduleNotification = findViewById(R.id.btn_schedule_notification);
        btnScheduleNotification.setOnClickListener(v -> {
            // set the desired notification time
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 22);
            calendar.set(Calendar.MINUTE, 29);

            // create intent for ReminderService and add the notification time as an extra
            Intent intent = new Intent(DoctorAppointmentActivity.this, MyReminderService.class);
            intent.putExtra("notification_time", calendar.getTimeInMillis());

            // start the ReminderService
            startService(intent);
        });


        edDate.setOnClickListener(v -> {
            int y = Calendar.getInstance().get(Calendar.YEAR) ;
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    DoctorAppointmentActivity.this
                    , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    , setListener, y, m, d);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener = (view, year, month, day) -> {
            String date = day + "/" + month + "/" + year;
            edDate.setText(date);
            int y = (Calendar.getInstance().get(Calendar.YEAR));
            int m = Calendar.getInstance().get(Calendar.MONTH);
            int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (year < y && month < m && day < d) {
                Toast.makeText(DoctorAppointmentActivity.this, "Already passed!", Toast.LENGTH_LONG).show();
                edDate.setText("");
            }
        };
        edTime.setOnClickListener(v -> {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    DoctorAppointmentActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setTimeListener,
                    hour,
                    minute,
                    false
            );
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.show();
        });

        setTimeListener = (view, hourOfDay, minute) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute);
            edTime.setText(time);
        };
        saveBtn.setOnClickListener(v -> addApp());


    }
    public void createSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.owner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edOwner.setAdapter(adapter);
        edOwner.setOnItemSelectedListener(this);
        edOwner.setPrompt("Relationship");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                owner="";
                break;
            case 1:
                owner = "user";
                break;
            case 2:
                owner = SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getName() +
                        SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getAge()+
                        SharedPrefManager.getInstance(getApplicationContext()).getPatient1().getRelationship();
                break;
            case 3:
                owner = SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getName() +
                        SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getAge()+
                        SharedPrefManager.getInstance(getApplicationContext()).getPatient2().getRelationship();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void logout(View view) {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
    }

    public void OpenManageAccount(View view) {
        startActivity(new Intent(DoctorAppointmentActivity.this, ManageAccountActivity.class));
    }
    public void createUserDialog(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(user_details, null);
        dialogBuilder.setView(contactPopupView);
        Userdialog = dialogBuilder.create();
        Userdialog.show();
        username = Userdialog.findViewById(R.id.username);
        detail_email = Userdialog.findViewById(R.id.detail_email);
        showInfo(username, detail_email);

    }
    public void showInfo(TextView username, TextView email) {
        username.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName()));
        email.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail()));
    }
    public void cancel(View view) {
        dialog.dismiss();
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
                Intent intent = new Intent(DoctorAppointmentActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_patients):
                intent = new Intent(DoctorAppointmentActivity.this, MyPatientsActivity.class);
                startActivity(intent);
                break;
            case (R.id.scan):
                intent = new Intent(DoctorAppointmentActivity.this, BarcodeActivity.class);
                startActivity(intent);
                break;
            case (R.id.inventory):
                intent = new Intent(DoctorAppointmentActivity.this, InventoryActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_med):
                intent = new Intent(DoctorAppointmentActivity.this, AddMedicineActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_prescriptions):
                intent = new Intent(DoctorAppointmentActivity.this, MyPrescriptionsActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_prescription):
                intent = new Intent(DoctorAppointmentActivity.this, AddPrescriptionActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_analyses):
                intent = new Intent(DoctorAppointmentActivity.this, MyAnalysesActivity.class);
                startActivity(intent);
                break;
            case (R.id.add_analysis):
                intent = new Intent(DoctorAppointmentActivity.this, AddAnalysisActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_doctors):
                intent = new Intent(DoctorAppointmentActivity.this, LocateDoctorsActivity.class);
                startActivity(intent);
                break;
            case (R.id.locate_pharmacies):
                intent = new Intent(DoctorAppointmentActivity.this, LocatePharmaciesActivity.class);
                startActivity(intent);
                break;

            case (R.id.make_doctor_appointment):
                break;
            case (R.id.make_analysis_appointment):
                intent = new Intent(DoctorAppointmentActivity.this, AnalysisAppointmentActivity.class);
                startActivity(intent);
                break;
            case (R.id.request_donation):
                intent = new Intent(DoctorAppointmentActivity.this, RequestDonationActivity.class);
                startActivity(intent);
                break;
            case (R.id.my_donations):
                intent = new Intent(DoctorAppointmentActivity.this, MyDonationsActivity.class);
                startActivity(intent);
                break;
            case (R.id.propose_donation):
                intent = new Intent(DoctorAppointmentActivity.this, ProposeDonationActivity.class);
                startActivity(intent);
                break;
            case (R.id.propose_diet):
                intent = new Intent(DoctorAppointmentActivity.this, DietActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.home);
        return true;
    }
    public void initView(){
        edDate = findViewById(R.id.edDate);
        edDoctorName=findViewById(R.id.DocName);
        edTime=findViewById(R.id.edTime);
        saveBtn=findViewById(R.id.saveBtn);
        edOwner=findViewById(R.id.owner);
        owner="";
    }
    void addApp() {
        String a_name = edDoctorName.getText().toString();
        String a_date = edDate.getText().toString();
        String a_time = edTime.getText().toString();
        if (a_name.isEmpty() || a_date.isEmpty() || a_time.isEmpty()|| owner=="") {
            Toast.makeText(getApplicationContext().getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
        }
        new Add().execute();
    }
    @SuppressLint("StaticFieldLeak")
    class Add extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DoctorAppointmentActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("app_id", edDoctorName.getText().toString()+edDate.getText().toString());
            map.put("app_name", edDoctorName.getText().toString());
            map.put("app_date", edDate.getText().toString());
            map.put("app_time", edTime.getText().toString());
            map.put("app_type", "Doctor");
            map.put("owner_type", owner);
            map.put("owner_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/analysis/addAnalysis.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/appointment/addAppointment.php", "GET", map);
                success = object.getInt("success");
                connection.close();
                ArrayList<Appointment> appointments;
                if (SharedPrefManager.getInstance(getApplicationContext()).getAppointmentList() == null){
                    appointments = new ArrayList<>();
                }
                else {
                    appointments = SharedPrefManager.getInstance(getApplicationContext()).getAppointmentList();
                }
                appointments.add(new Appointment(edDoctorName.getText().toString(),
                        edDate.getText().toString(),
                        edTime.getText().toString(),
                        "Doctor",
                        owner));
                SharedPrefManager.saveAppointmentList(appointments);
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
                Toast.makeText(DoctorAppointmentActivity.this, "Added successfully", Toast.LENGTH_LONG).show();

                startActivity(new Intent(DoctorAppointmentActivity.this, MyAppointmentsActivity.class));
            } else {
                Toast.makeText(DoctorAppointmentActivity.this, "Error adding appointment!", Toast.LENGTH_LONG).show();
            }
        }
    }
}