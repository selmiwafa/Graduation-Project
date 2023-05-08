package com.example.pfe.manage_patient_account.analysis;

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
import com.example.pfe.manage_analyses.Analysis;
import com.example.pfe.manage_patient_account.MyPatientsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class PatientAnalysisAdapter extends RecyclerView.Adapter<PatientAnalysisAdapter.ViewHolder>{
    private final List<Analysis> analyses;
    private OnItemClickListener mListener;
    Context mContext;
    int itemPosition;
    String name, owner;
    JSONParser parser = new JSONParser();
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success;
    ProgressDialog dialog;

    public PatientAnalysisAdapter(Context context, List<Analysis> analyses)
    {
        this.analyses = analyses;
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
        Analysis analysis = analyses.get(position);

        name = analysis.getAnalysis_name();
        owner = analysis.getOwner();
        holder.edDate.setText(analysis.getAnalysis_date());
        holder.edName.setText(analysis.getAnalysis_name());


        holder.DeleteBtn.setOnClickListener(v -> {
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

                TextView nameTv = dialogView.findViewById(R.id.analysisNameValueTextView);
                TextView dateTv = dialogView.findViewById(R.id.AnalysisDateValueTextView);
                TextView resultTv = dialogView.findViewById(R.id.ResultValueTextView);

                dateTv.setText(analysis.getAnalysis_date());
                nameTv.setText(analysis.getAnalysis_name());
                resultTv.setText(analysis.getResult());

                builder.setView(dialogView)
                        .setTitle("Analysis Information")
                        .setPositiveButton("OK", (dialog, which) -> {dialog.dismiss();})
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
        if (analyses != null){
            analyses.remove(analysis);
            SharedPrefManager.getInstance(mContext).deleteAnalysis(name);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        if (analyses != null) {
            return analyses.size();
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
        private final TextView edName;
        private final ImageButton DeleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edName = itemView.findViewById(R.id.analysisName);
            edDate = itemView.findViewById(R.id.analysisDate);
            DeleteBtn = itemView.findViewById(R.id.deleteBtn);
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(analyses.get(position));
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
            map.put("analysis_name", name);
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, user, password);
                JSONObject object = parser.makeHttpRequest("http://192.168.43.205/healthbuddy/Analysis/deleteAnalysis.php", "GET", map);
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
                Intent intent = new Intent(mContext, MyPatientsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "Error deleting appointment!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
