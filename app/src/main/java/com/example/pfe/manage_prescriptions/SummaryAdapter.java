package com.example.pfe.manage_prescriptions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder>{
    private final List<PresMedicine> summaryList;
    int itemPosition;
    PresMedicine presMedicine;
    private OnItemClickListener mListener;
    String name;
    Context mContext;
    String barcode ;
    String medName;
    int quantity;
    String exp_date;
    String description;
    Intent intent;
    String dose;
    String frequency;
    String period;
    String tpw;
    String other;
    public SummaryAdapter(Context context, List<PresMedicine> summaryList)
    {
        this.summaryList = summaryList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        presMedicine = summaryList.get(position);

        name = presMedicine.getMed_name();
        holder.medName.setText(presMedicine.getMed_name());
        holder.barcode.setText(presMedicine.getBarcode());
        holder.deleteBtn.setOnClickListener(v -> {
            itemPosition = holder.getAdapterPosition();
            deleteItem(presMedicine);
        });
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(presMedicine);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogViewSummary = inflater.inflate(R.layout.summary_med_info, null);

                TextView medNameTv = dialogViewSummary.findViewById(R.id.presMedNameValueTextView);
                TextView medBarcodeTv = dialogViewSummary.findViewById(R.id.presBarcodeValueTextView);
                TextView doseTv = dialogViewSummary.findViewById(R.id.doseValueTextView);
                TextView frequencyTv = dialogViewSummary.findViewById(R.id.frequencyValueTextView);
                TextView periodTv = dialogViewSummary.findViewById(R.id.periodValueTextView);
                TextView tpwTv = dialogViewSummary.findViewById(R.id.tpwValueTextView);
                TextView otherTv = dialogViewSummary.findViewById(R.id.otherValueTextView);

                // Use the current item instead of the global PresMedicine object
                medNameTv.setText(presMedicine.getMed_name());
                medBarcodeTv.setText(presMedicine.getBarcode());
                doseTv.setText(String.valueOf(presMedicine.getDose()));
                frequencyTv.setText(String.valueOf(presMedicine.getFrequency()));
                periodTv.setText(String.valueOf(presMedicine.getPeriod()));
                tpwTv.setText(String.valueOf(presMedicine.getTpw()));
                otherTv.setText(presMedicine.getOther());

                builder.setView(dialogViewSummary)
                        .setTitle("Prescription medicine Information")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setIcon(R.drawable.help);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(PresMedicine presMedicine);
    }
    public void deleteItem(PresMedicine presMedicine) {
        if (summaryList != null){
            summaryList.remove(presMedicine);
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
            deleteBtn = itemView.findViewById(R.id.summaryDeleteBtn);
            medName = itemView.findViewById(R.id.presMedName);
            barcode = itemView.findViewById(R.id.presBarcode);

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

