package com.example.pfe.manage_medicine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder>  {
    private final List<Medicine> medicineList;
    int itemPosition;
    Medicine medicine;
    JSONParser parser = new JSONParser();
    private OnItemClickListener mListener;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success, number;
    Context mContext;
    ProgressDialog dialog;

    public MedicineAdapter(Context context, List<Medicine> medicineList)
    {
        this.medicineList = medicineList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);

        holder.medName.setText(medicine.getMed_name());
        holder.barcode.setText(medicine.getBarcode());
        holder.deleteBtn.setOnClickListener(v -> {
            itemPosition = holder.getAdapterPosition();
            new Delete().execute();
            deleteItem(medicine);
        });
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(medicine);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.activity_medicine_info, null);

                TextView medNameTv = dialogView.findViewById(R.id.medNameValueTextView);
                TextView barcodeTv = dialogView.findViewById(R.id.barcodeValueTextView);
                TextView quantityTv = dialogView.findViewById(R.id.quantityValueTextView);
                TextView expTv = dialogView.findViewById(R.id.ExpDateValueTextView);
                TextView descTv = dialogView.findViewById(R.id.DescValueTextView);

                // Use the current item instead of the global medicine object
                medNameTv.setText(medicine.getMed_name());
                barcodeTv.setText(medicine.getBarcode());
                quantityTv.setText(String.valueOf(medicine.getQuantity()));
                expTv.setText(medicine.getExp_date());
                descTv.setText(medicine.getDescription());

                builder.setView(dialogView)
                        .setTitle("Medicine Information")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setNeutralButton("Edit details", (dialog, which) -> {
                            Intent intent = new Intent(mContext, UpdateMedicineActivity.class);
                            intent.putExtra("barcode",medicine.getBarcode());
                            intent.putExtra("med_name",medicine.getMed_name());
                            intent.putExtra("quantity",medicine.getQuantity());
                            intent.putExtra("exp_date",medicine.getExp_date());
                            intent.putExtra("description",medicine.getDescription());
                            mContext.startActivity(intent);
                        })
                        .setIcon(R.drawable.help);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(Medicine medicine);
    }
    public void deleteItem(Medicine medicine) {
        if (medicineList != null){
            medicineList.remove(medicine);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (medicineList != null) {
            return medicineList.size();
        }
        else {
            return 0;
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView medName;
        private final TextView barcode;
        public ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            medName = itemView.findViewById(R.id.medName);
            barcode = itemView.findViewById(R.id.barcode);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(medicineList.get(position));
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
            map.put("barcode", medicine.getBarcode());
            map.put("user", SharedPrefManager.getInstance(mContext).getUser().getEmail());

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/deleteMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/medicine/deleteMedicine.php", "GET", map);
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
                Intent intent = new Intent(mContext, InventoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "Error deleting medicine!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

