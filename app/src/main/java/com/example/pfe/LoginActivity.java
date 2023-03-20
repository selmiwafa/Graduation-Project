package com.example.pfe;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmailLogin;
    private EditText edPasswordLogin;
    ProgressDialog dialog;
    JSONParser parser=new JSONParser();
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent( LoginActivity.this, HomepageActivity.class));
            return;
        }
        this.initView();

    }
    public void login(View view) {
        String email = edEmailLogin.getText().toString();
        String password = edPasswordLogin.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
        }
        else if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Invalid e-mail format!", Toast.LENGTH_LONG).show();
        }
        else {
            new LoginActivity.Log().execute();
        }
    }
    @SuppressLint("StaticFieldLeak")
    class Log extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {

            HashMap<String,String> map= new HashMap<>();
            map.put("email",edEmailLogin.getText().toString());
            map.put("password",edPasswordLogin.getText().toString());

            JSONObject object =parser.makeHttpRequest("http://10.0.2.2/user/log.php","GET",map);
            try {
                success=object.getInt("success");
                JSONArray userJson = object.getJSONArray("user");
                JSONObject jsonObject = userJson.getJSONObject(0);
                User user = new User(
                        jsonObject.getString("email"),
                        jsonObject.getString("name"),
                        jsonObject.getString("birthdate"),
                        jsonObject.getString("password"),
                        jsonObject.getString("adress")
                );
                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            dialog.cancel();

            if(success==1)
            {
                Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_LONG).show();
                startActivity(new Intent( LoginActivity.this, HomepageActivity.class));


            }
            else
            {
                Toast.makeText(LoginActivity.this,"Error signing up!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initView() {
        edEmailLogin = findViewById(R.id.edEmailLogin);
        edPasswordLogin = findViewById(R.id.edPasswordLogin);
    }

    public boolean isValidEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
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
