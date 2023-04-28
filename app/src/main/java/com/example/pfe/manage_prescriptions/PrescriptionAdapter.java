package com.example.pfe.manage_prescriptions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.manage_patient_account.MyPatientsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder>{
    private final List<Prescription> prescriptions;
    private OnItemClickListener mListener;
    Context mContext;
    int itemPosition;
    String id;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success;
    String  owner;
    ProgressDialog dialog;
    private RecyclerView mRecyclerView;
    SummaryAdapter adapter;
    LinearLayoutManager linearlayoutmanager;

    public PrescriptionAdapter(Context context, List<Prescription> prescriptions)
    {
        this.prescriptions = prescriptions;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prescription prescription = prescriptions.get(position);
        owner =prescription.getOwner();
        id = prescription.getId();
        holder.edEnd.setVisibility(View.VISIBLE);
        holder.Hiphen.setVisibility(View.VISIBLE);
        holder.edStart.setText(prescription.getStart());
        holder.edName.setText(prescription.getName());
        if (prescription.getEnd().isEmpty()) {
            holder.edEnd.setVisibility(View.INVISIBLE);
            holder.Hiphen.setVisibility(View.INVISIBLE);
        } else {
            holder.edEnd.setText(prescription.getEnd());
        }

        holder.deleteBtn.setOnClickListener(v -> {
            itemPosition = holder.getAdapterPosition();
            new Delete().execute();
            deleteItem(prescription);
        });
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(prescription);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.prescription_info, null);

                TextView nameTv = dialogView.findViewById(R.id.pres_nameValue);
                TextView startTv = dialogView.findViewById(R.id.start_dateValue);
                TextView endTv = dialogView.findViewById(R.id.end_dateValue);

                ArrayList<PresMedicine> presMedicines = prescription.getMedicineArrayList();

                startTv.setText(prescription.getStart());
                endTv.setText(prescription.getEnd());
                nameTv.setText(prescription.getName());
                mRecyclerView = dialogView.findViewById(R.id.med_details_list);
                adapter = new SummaryAdapter(mContext, presMedicines);
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(medicine -> {
                    Toast.makeText(mContext, "Item selected", Toast.LENGTH_SHORT).show();
                });
                linearlayoutmanager = new LinearLayoutManager(mContext);
                mRecyclerView.setLayoutManager(linearlayoutmanager);

                builder.setView(dialogView)
                        .setTitle("Prescription Information")
                        .setPositiveButton("OK", (dialog, which) -> {dialog.dismiss();})
                        .setIcon(R.drawable.help);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(Prescription prescription);
    }
    public void deleteItem(Prescription prescription) {
        if (prescriptions != null){
            prescriptions.remove(prescription);
            SharedPrefManager.getInstance(mContext).deletePrescription(id);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        if (prescriptions != null) {
            return prescriptions.size();
        }
        else {
            return 0;
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView edStart, edEnd, Hiphen;
        private final TextView edName;
        private final ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edName = itemView.findViewById(R.id.pres_name);
            edStart = itemView.findViewById(R.id.pres_start);
            edEnd = itemView.findViewById(R.id.pres_end);
            Hiphen = itemView.findViewById(R.id.hiphen);
            deleteBtn = itemView.findViewById(R.id.DeleteBtn);
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(prescriptions.get(position));
                    }
                }
            });
        }
    }
    @SuppressLint("StaticFieldLeak")
    class Delete extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("owner_type", owner);
            map.put("owner_id", SharedPrefManager.getInstance(mContext).getUser().getEmail());
            map.put("pres_id", id);
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/deleteMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/Prescription/deletePrescription.php", "GET", map);
                success = object.getInt("success");
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
                Toast.makeText(mContext, "deleted successfully", Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
                if (Objects.equals(owner, "user")) {
                    Intent intent = new Intent(mContext, MyPrescriptionsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, MyPatientsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                }
            } else {
                Toast.makeText(mContext, "Error deleting prescription!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

