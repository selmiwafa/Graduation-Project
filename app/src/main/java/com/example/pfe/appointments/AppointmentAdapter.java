package com.example.pfe.appointments;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>{
    private final List<Appointment> appointments;
    private OnItemClickListener mListener;
    Context mContext;
    int itemPosition;
    String app_id, id;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success, number;
    String  owner;
    ProgressDialog dialog;

    public AppointmentAdapter(Context context, List<Appointment> appointments)
    {
        this.appointments = appointments;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        owner =appointment.getOwner();
        id = appointment.getId();

        holder.edDate.setText(appointment.getDate());
        holder.edName.setText(appointment.getName());
        holder.edTime.setText(appointment.getTime());
        holder.edType.setText(appointment.getCategory()+" :");

        holder.deleteBtn.setOnClickListener(v -> {
            itemPosition = holder.getAdapterPosition();
            new Delete().execute();
            deleteItem(appointment);
        });
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(appointment);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.appointement_info, null);

                TextView dateTv = dialogView.findViewById(R.id.app_dateValue);
                TextView nameTv = dialogView.findViewById(R.id.app_nameValue);
                TextView typeTv = dialogView.findViewById(R.id.app_typeValue);
                TextView timeTv = dialogView.findViewById(R.id.app_timeValue);

                dateTv.setText(appointment.getDate());
                nameTv.setText(appointment.getName());
                typeTv.setText(appointment.getCategory()+" :");
                timeTv.setText(appointment.getTime());

                builder.setView(dialogView)
                        .setTitle("Appointment Information")
                        .setPositiveButton("OK", (dialog, which) -> {dialog.dismiss();})
                        .setIcon(R.drawable.help);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }
    public void deleteItem(Appointment appointment) {
        if (appointments != null){
            appointments.remove(appointment);
            SharedPrefManager.getInstance(mContext).deleteAppointment(id);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        if (appointments != null) {
            return appointments.size();
        }
        else {
            return 0;
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView edDate;
        private final TextView edName, edTime, edType;
        private final ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edName = itemView.findViewById(R.id.app_name);
            edDate = itemView.findViewById(R.id.app_date);
            edTime = itemView.findViewById(R.id.app_time);
            edType = itemView.findViewById(R.id.app_type);
            deleteBtn = itemView.findViewById(R.id.DeleteBtn);
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(appointments.get(position));
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
            map.put("app_id", id);
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/deleteMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/Appointment/deleteAppointment.php", "GET", map);
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
                Intent intent = new Intent(mContext, MyAppointmentsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "Error deleting appointment!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

