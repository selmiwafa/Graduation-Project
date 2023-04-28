package com.example.pfe.manage_analyses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;

import java.util.ArrayList;
import java.util.Objects;

public class AnalysisListFragment extends Fragment {

    ArrayList<Analysis> analysisList;
    AnalysisAdapter adapter;
    LinearLayoutManager linearlayoutmanager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_analysis_list, container, false);
        analysisList = SharedPrefManager.getInstance(getContext()).getAnalysisList();
        analysisList.removeIf(analysis -> !Objects.equals(analysis.getOwner(), "user"));

        RecyclerView mRecyclerView = rootView.findViewById(R.id.listAnalysis);
        adapter = new AnalysisAdapter(getActivity(), analysisList);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(analysis -> {
            Toast.makeText(getContext(), "Item selected", Toast.LENGTH_SHORT).show();
        });
        linearlayoutmanager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearlayoutmanager);
        return rootView;
    }
}