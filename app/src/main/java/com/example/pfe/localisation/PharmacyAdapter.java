package com.example.pfe.localisation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;

import java.util.List;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.ViewHolder>{
    private final List<Pharmacy> pharmacies;
    private OnItemClickListener mListener;
    Context mContext;
    String name;
    public PharmacyAdapter(Context context, List<Pharmacy> pharmacies)
    {
        this.pharmacies = pharmacies;
        mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pharmacy pharmacy = pharmacies.get(position);

        name = pharmacy.getName();
        holder.pharmacyName.setText(pharmacy.getName());
        holder.pharmacyAdress.setText(pharmacy.getAddress());
    }
    public interface OnItemClickListener {
        void onItemClick(Pharmacy pharmacy);
    }

    @Override
    public int getItemCount() {
        if (pharmacies != null) {
            return pharmacies.size();
        }
        else {
            return 0;
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView pharmacyName;
        private final TextView pharmacyAdress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pharmacyName = itemView.findViewById(R.id.nameTextView);
            pharmacyAdress = itemView.findViewById(R.id.addressTextView);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(pharmacies.get(position));
                    }
                }
            });
        }
    }
}
