package com.example.pfe.manage_prescriptions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.JSONParser;
import com.example.pfe.R;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder>{
    private final List<PresMedicine> summaryList;
    int itemPosition;
    PresMedicine PresMedicine;
    JSONParser parser = new JSONParser();
    private OnItemClickListener mListener;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    String name;
    int success, number;
    Context mContext;
    ProgressDialog dialog;
    public SummaryAdapter(Context context, List<PresMedicine> summaryList)
    {
        this.summaryList = summaryList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pres_med_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PresMedicine = summaryList.get(position);

        name = PresMedicine.getMed_name();
        holder.medName.setText(PresMedicine.getMed_name());
        holder.barcode.setText(PresMedicine.getBarcode());
        holder.deleteBtn.setOnClickListener(v -> {
            itemPosition = holder.getAdapterPosition();
            deleteItem(PresMedicine);
        });
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(PresMedicine);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.summary_med_info, null);

                TextView medNameTv = dialogView.findViewById(R.id.medNameValueTextView);
                TextView medBarcodeTv = dialogView.findViewById(R.id.barcodeValueTextView);
                TextView doseTv = dialogView.findViewById(R.id.doseValueTextView);
                TextView frequencyTv = dialogView.findViewById(R.id.frequencyValueTextView);
                TextView periodTv = dialogView.findViewById(R.id.periodValueTextView);
                TextView tpwTv = dialogView.findViewById(R.id.tpwValueTextView);
                TextView otherTv = dialogView.findViewById(R.id.otherValueTextView);

                // Use the current item instead of the global PresMedicine object
                medNameTv.setText(PresMedicine.getMed_name());
                medBarcodeTv.setText(PresMedicine.getBarcode());
                doseTv.setText(PresMedicine.getDose());
                frequencyTv.setText(PresMedicine.getFrequency());
                periodTv.setText(PresMedicine.getPeriod());
                tpwTv.setText(PresMedicine.getTpw());
                otherTv.setText(PresMedicine.getOther());

                builder.setView(dialogView)
                        .setTitle("Medicine Information")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setIcon(R.drawable.help);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(PresMedicine PresMedicine);
    }
    public void deleteItem(PresMedicine PresMedicine) {
        if (summaryList != null){
            summaryList.remove(PresMedicine);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (summaryList != null) {
            return summaryList.size();
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
                        mListener.onItemClick(summaryList.get(position));
                    }
                }
            });
        }
    }
}

