package com.example.pfe.localisation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;

import java.util.ArrayList;

public class PharmacyListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    ArrayList<Pharmacy> pharmacies;
    PharmacyAdapter adapter;
    LinearLayoutManager linearlayoutmanager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pharmacies_list, container, false);



        mRecyclerView = rootView.findViewById(R.id.listPharmacies);
        Bundle bundle = getArguments();
        Log.d("bund",bundle +"");
        if (bundle != null) {
            pharmacies=bundle.getParcelableArrayList("phar");
            Log.d("phar",pharmacies.get(0).getLatitude()+" "+pharmacies.get(0).getName());

        }

        adapter = new PharmacyAdapter(getActivity(), pharmacies);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(analysis -> {
            Toast.makeText(getContext(), "Item selected", Toast.LENGTH_SHORT).show();
        });
        linearlayoutmanager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearlayoutmanager);
        return rootView;
    }
}