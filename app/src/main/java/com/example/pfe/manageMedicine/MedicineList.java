package com.example.pfe.manageMedicine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicineList extends Fragment {
    private RecyclerView mRecyclerView;
    JSONParser parser = new JSONParser();
    List<Medicine> medicineList;
    MedicineAdapter adapter;
    LinearLayoutManager linearlayoutmanager;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success, number=0;
    ProgressDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.listMedicine);
        new Select().execute();
        return rootView;
    }


    @SuppressLint("StaticFieldLeak")
    class Select extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("user", SharedPrefManager.getInstance(getContext()).getUser().getEmail());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/addMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/medicine/selectMedicine.php", "GET", map);
                success = object.getInt("success");
                if (success == 1) {
                    number = object.getInt("number");
                    JSONArray userJson = object.getJSONArray("medicine");
                    medicineList = new ArrayList<>();
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
            adapter = new MedicineAdapter(getActivity(), medicineList);
            mRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(medicine -> {
                Toast.makeText(getContext(), "Item selected", Toast.LENGTH_SHORT).show();
            });
            linearlayoutmanager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearlayoutmanager);
            if (success == 1) {
                Toast.makeText(getContext(), "Selected successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Error selecting medicine!", Toast.LENGTH_LONG).show();
            }
        }
    }


}