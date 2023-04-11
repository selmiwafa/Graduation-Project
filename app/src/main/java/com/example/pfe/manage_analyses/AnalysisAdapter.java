package com.example.pfe.manage_analyses;

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
import com.example.pfe.manage_medicine.InventoryActivity;
import com.example.pfe.manage_medicine.Medicine;
import com.example.pfe.manage_medicine.UpdateMedicineActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.ViewHolder> {
    private final List<Analysis> analysisList;
    int itemPosition;
    Analysis analysis;
    JSONParser parser = new JSONParser();
    private AnalysisAdapter.OnItemClickListener mListener;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    String name;
    int success, number;
    Context mContext;
    ProgressDialog dialog;
    public AnalysisAdapter(Context context, List<Analysis> analysisList)
    {
        this.analysisList = analysisList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Analysis analysis = analysisList.get(position);

        name = analysis.getAnalysis_name();
        holder.analysisName.setText(analysis.getAnalysis_name());
        holder.analysisDate.setText(analysis.getAnalysis_date());
        holder.deleteBtn.setOnClickListener(v -> {
            itemPosition = holder.getAdapterPosition();
            new Delete().execute();
            deleteItem(analysis);
        });
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(analysis);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.analysis_info, null);

                TextView analysisNameTv = dialogView.findViewById(R.id.analysisNameValueTextView);
                TextView analysisDateTv = dialogView.findViewById(R.id.AnalysisDateValueTextView);
                TextView ResultTv = dialogView.findViewById(R.id.ResultValueTextView);

                // Use the current item instead of the global medicine object
                analysisNameTv.setText(analysis.getAnalysis_name());
                analysisDateTv.setText(analysis.getAnalysis_date());
                ResultTv.setText(analysis.getResult());

                builder.setView(dialogView)
                        .setTitle("Analysis Information")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setNeutralButton("Edit details", (dialog, which) -> {
                            Intent intent = new Intent(mContext, UpdateAnalysisActivity.class);
                            intent.putExtra("analysis_name",analysis.getAnalysis_name());
                            intent.putExtra("analysis_date",analysis.getAnalysis_date());
                            intent.putExtra("result",analysis.getResult());
                            mContext.startActivity(intent);
                        })
                        .setIcon(R.drawable.help);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(Analysis analysis);
    }
    public void deleteItem(Analysis analysis) {
        if (analysisList != null){
            analysisList.remove(analysis);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (analysisList != null) {
            return analysisList.size();
        }
        else {
            return 0;
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView analysisName;
        private final TextView analysisDate;
        public ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            analysisName = itemView.findViewById(R.id.analysisName);
            analysisDate = itemView.findViewById(R.id.analysisDate);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(analysisList.get(position));
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

            map.put("analysis_name", name);
            map.put("user", SharedPrefManager.getInstance(mContext).getUser().getEmail());

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                //JSONObject object = parser.makeHttpRequest("http://192.168.1.16/healthbuddy/medicine/deleteMedicine.php", "GET", map);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/analysis/deleteAnalysis.php", "GET", map);
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
                Intent intent = new Intent(mContext, MyAnalysesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "Error deleting analysis!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

