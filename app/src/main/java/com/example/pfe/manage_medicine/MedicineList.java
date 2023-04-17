package com.example.pfe.manage_medicine;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import java.util.ArrayList;

public class MedicineList extends Fragment {
    private RecyclerView mRecyclerView;
    JSONParser parser = new JSONParser();
    ArrayList<Medicine> medicineList;
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
        medicineList = SharedPrefManager.getInstance(getContext()).getMedicineList();
        mRecyclerView = rootView.findViewById(R.id.listMedicine);
        adapter = new MedicineAdapter(getActivity(), medicineList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(medicine -> {
            Toast.makeText(getContext(), "Item selected", Toast.LENGTH_SHORT).show();
        });
        linearlayoutmanager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearlayoutmanager);
        //new Select().execute();
        return rootView;
    }


    /*@SuppressLint("StaticFieldLeak")
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
                    ArrayList<String> myList = new ArrayList<>();
                    for(int i=0;i<number;i++) {
                        JSONObject jsonObject = userJson.getJSONObject(i);
                        myList.add(jsonObject.getString("barcode"));
                        Medicine medicine = new Medicine(
                                jsonObject.getString("barcode"),
                                jsonObject.getString("med_name"),
                                jsonObject.getInt("quantity"),
                                jsonObject.getString("description"),
                                jsonObject.getString("exp_date")
                        );
                        medicineList.add(medicine);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("medicineList", myList);
                    Intent intent = new Intent(getContext(), BarcodeActivity.class);
                    intent.putExtras(bundle);
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
    }*/


}