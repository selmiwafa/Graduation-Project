package com.example.pfe.manage_prescriptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;

import java.util.ArrayList;

public class FragmentSummaryList extends Fragment {
    SummaryAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<PresMedicine> summaryList = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_summary_list, container, false);
        recyclerView = rootView.findViewById(R.id.listSummaryMedicine);
        summaryList.add(((PresMedListActivity) getActivity()).getPresMedicine());
        //add from previous activity

        adapter = new SummaryAdapter(getContext(), summaryList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(presMedicine -> {
            Toast.makeText(getContext(), "Summary tem selected", Toast.LENGTH_SHORT).show();
        });
        llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        return rootView;
    }


}