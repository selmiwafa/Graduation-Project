package com.example.pfe.manageMedicine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.Medicine;
import com.example.pfe.R;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder>  {
    private final List<Medicine> medicineList;

    public MedicineAdapter(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecine_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);

        holder.medName.setText(medicine.getMed_name());
        holder.barcode.setText(medicine.getBarcode());
        holder.quantity.setText(medicine.getQuantity());
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView medName;
        private final TextView quantity;
        private final TextView barcode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.medName);
            barcode = itemView.findViewById(R.id.barcode);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}

