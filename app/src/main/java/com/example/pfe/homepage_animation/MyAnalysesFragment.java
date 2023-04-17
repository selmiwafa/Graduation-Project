package com.example.pfe.homepage_animation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.pfe.R;
import com.example.pfe.manage_analyses.MyAnalysesActivity;

public class MyAnalysesFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_analyses, container, false);
        RelativeLayout myaFragment = view.findViewById(R.id.myaFragment);
        myaFragment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyAnalysesActivity.class);
            startActivity(intent);
        });
        return view;
    }
}