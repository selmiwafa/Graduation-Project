package com.example.pfe.donations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;
import com.example.pfe.manage_medicine.Medicine;

import java.util.List;

public class SelectProposalAdapter extends RecyclerView.Adapter<SelectProposalAdapter.ViewHolder> {
    private final List<Medicine> medicineList;
    private OnItemClickListener mListener;
    Context mContext;

    public SelectProposalAdapter(Context context, List<Medicine> medicineList)
    {
        this.medicineList = medicineList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);

        holder.medName.setText(medicine.getMed_name());
        holder.barcode.setText(medicine.getBarcode());
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
                        .setPositiveButton("Select", (dialog, which) -> {
                            Intent intent = new Intent(mContext, ProposeDonationActivity.class);
                            intent.putExtra("barcode",medicine.getBarcode());
                            mContext.startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setIcon(R.drawable.help);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(Medicine medicine);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.selectMedName);
            barcode = itemView.findViewById(R.id.selectBarcode);

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
}
