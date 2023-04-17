package com.example.pfe.donations;

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

public class MyDonationsAdapter extends RecyclerView.Adapter<MyDonationsAdapter.ViewHolder>{
    private final List<Donation> donations;
    private OnItemClickListener mListener;
    Context mContext;
    int itemPosition;
    String barcode, id;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success, number;
    ProgressDialog dialog;

    public MyDonationsAdapter(Context context, List<Donation> donations)
    {
        this.donations = donations;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donation donation = donations.get(position);

        id = donation.getDate();
        barcode = donation.getBarcode();

        holder.edDate.setText(donation.getDate());
        holder.edBarcode.setText(donation.getBarcode());
        holder.deleteBtn.setOnClickListener(v -> {
            itemPosition = holder.getAdapterPosition();
            new Delete().execute();
            deleteItem(donation);
        });
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(donation);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.donation_info, null);

                TextView barcodeTv = dialogView.findViewById(R.id.barcodeValueTextView);
                TextView idTv = dialogView.findViewById(R.id.idValue);
                TextView quantityTv = dialogView.findViewById(R.id.quantityValue);
                TextView dateTv = dialogView.findViewById(R.id.dateValue);

                barcodeTv.setText(donation.getBarcode());
                quantityTv.setText(String.valueOf(donation.getQuantity()));
                idTv.setText(donation.getId());
                dateTv.setText(donation.getDate());

                builder.setView(dialogView)
                        .setTitle("Donation Information")
                        .setPositiveButton("OK", (dialog, which) -> {dialog.dismiss();})
                        .setIcon(R.drawable.help);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(Donation donation);
    }
    public void deleteItem(Donation donation) {
        if (donations != null){
            donations.remove(donation);
            SharedPrefManager.getInstance(mContext).deleteDonation(donation.getBarcode());
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        if (donations != null) {
            return donations.size();
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
        private final TextView edBarcode;
        private final ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edDate = itemView.findViewById(R.id.donation_date);
            edBarcode = itemView.findViewById(R.id.presBarcode);
            deleteBtn = itemView.findViewById(R.id.DeleteBtn);
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(donations.get(position));
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
            map.put("user", SharedPrefManager.getInstance(mContext).getUser().getEmail());

            map.put("id", id);

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/deleteMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/Donations/deleteDonation.php", "GET", map);
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
                Intent intent = new Intent(mContext, MyDonationsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "Error deleting donation!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

