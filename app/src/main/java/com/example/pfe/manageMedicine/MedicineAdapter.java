package com.example.pfe.manageMedicine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.Medicine;
import com.example.pfe.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder>  {
    private final List<Medicine> medicineList;
    private ArrayList<Medicine> selectedItems = new ArrayList<>();

    public MedicineAdapter(List<Medicine> medicineList) {
        this.medicineList = medicineList;
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
        holder.checkbox.setChecked(medicine.isSelected());

        holder.checkbox.setOnClickListener(view -> {
            boolean isChecked = holder.checkbox.isChecked();
            medicine.setSelected(isChecked);

            if (isChecked) {
                selectedItems.add(medicine);
            } else {
                selectedItems.remove(medicine);
            }
        });
        holder.medName.setText(medicine.getMed_name());
        holder.barcode.setText(medicine.getBarcode());
        holder.quantity.setText(String.valueOf(medicine.getQuantity()));
    }
    public void deleteSelectedItems() {
        for (Medicine medicine : selectedItems) {
            medicineList.remove(medicine);

        }
        selectedItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView medName;
        private final TextView quantity;
        private final TextView barcode;
        public MaterialButton checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.medName);
            barcode = itemView.findViewById(R.id.barcode);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}

