package com.example.pfe.homepage_animation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.pfe.R;
import com.example.pfe.manage_medicine.InventoryActivity;

public class MyMedFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_med, container, false);
        RelativeLayout mymedFragment = view.findViewById(R.id.mymedFragment);
        mymedFragment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), InventoryActivity.class);
            startActivity(intent);
        });
        return view;
    }

}