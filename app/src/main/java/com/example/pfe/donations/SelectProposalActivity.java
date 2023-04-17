package com.example.pfe.donations;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.manage_medicine.Medicine;

import java.util.List;

public class SelectProposalActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    List<Medicine> medicineList;
    SelectProposalAdapter adapter;
    LinearLayoutManager linearlayoutmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_select_proposal);
        mRecyclerView = findViewById(R.id.selectlistProposal);


        medicineList = SharedPrefManager.getInstance(getApplicationContext()).getMedicineList();
        adapter = new SelectProposalAdapter(SelectProposalActivity.this, medicineList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(medicine -> {
            Toast.makeText(SelectProposalActivity.this, "Item selected", Toast.LENGTH_SHORT).show();
        });
        linearlayoutmanager = new LinearLayoutManager(SelectProposalActivity.this);
        mRecyclerView.setLayoutManager(linearlayoutmanager);
    }

    public void back(View view) {
        this.finish();
    }
}