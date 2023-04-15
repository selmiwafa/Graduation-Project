package com.example.pfe.manage_prescriptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;

import java.util.ArrayList;

public class FragmentSummaryList extends Fragment {
    ArrayList<PresMedicine> summaryList;
    private LinearLayoutManager linearlayoutmanager;
    private SummaryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary_list, container, false);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.listSummaryMedicine);

        //add from previous activity

        adapter = new SummaryAdapter(getActivity(), summaryList);
        mRecyclerView.setAdapter(adapter);
        linearlayoutmanager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearlayoutmanager);
        return rootView;
    }


}