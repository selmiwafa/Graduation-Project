package com.example.pfe.localisation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;

import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    List<Doctor> doctors;
    DoctorAdapter adapter;
    LinearLayoutManager linearlayoutmanager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctors_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.listAnalysis);

        adapter = new DoctorAdapter(getActivity(), doctors);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(analysis -> {
            Toast.makeText(getContext(), "Item selected", Toast.LENGTH_SHORT).show();
        });
        linearlayoutmanager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearlayoutmanager);
        return rootView;
    }
}