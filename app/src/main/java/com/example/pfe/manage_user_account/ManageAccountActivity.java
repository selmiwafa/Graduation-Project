package com.example.pfe.manage_user_account;

import static com.example.pfe.R.layout.delete_dialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pfe.HomepageActivity;
import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class ManageAccountActivity extends AppCompatActivity {
    TextView username, detail_email;
    Button Update;
    ProgressDialog dialog;
    int success;
    JSONParser parser = new JSONParser();

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog delDialog;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";

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

    public void deleteUser(View view) {
        new Delete().execute();
    }

    public void deleteUserDialog(View view) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(delete_dialog, null);
        dialogBuilder.setView(contactPopupView);
        delDialog = dialogBuilder.create();
        delDialog.show();
    }

    public void backHomepage(View view) {
        startActivity(new Intent(ManageAccountActivity.this, HomepageActivity.class));
    }

    public void openPageUpdate(View view) {
        startActivity(new Intent(ManageAccountActivity.this, UpdateInfoActivity.class));
    }

    @SuppressLint("StaticFieldLeak")
    class Delete extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ManageAccountActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();


        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("email", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/user/delete.php", "GET", map);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/user/delete.php", "GET", map);
                success = object.getInt("success");
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                connection.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if (success == 1) {
                Toast.makeText(ManageAccountActivity.this, "Account deleted successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ManageAccountActivity.this, LoginActivity.class));
            } else {
                Toast.makeText(ManageAccountActivity.this, "Error deleting!", Toast.LENGTH_LONG).show();
            }
        }
    }

}