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

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>{
    private final List<Doctor> doctors;
    private OnItemClickListener mListener;
    Context mContext;
    String name;
    public DoctorAdapter(Context context, List<Doctor> doctors)
    {
        this.doctors = doctors;
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
        Doctor doctor = doctors.get(position);

        name = doctor.getName();
        holder.doctorName.setText(doctor.getName());
        holder.doctorAddress.setText(doctor.getAddress());
    }
    public interface OnItemClickListener {
        void onItemClick(Doctor doctor);
    }

    @Override
    public int getItemCount() {
        if (doctors != null) {
            return doctors.size();
        }
        else {
            return 0;
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView doctorName;
        private final TextView doctorAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.nameTextView);
            doctorAddress = itemView.findViewById(R.id.addressTextView);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(doctors.get(position));
                    }
                }
            });
        }
    }
}
