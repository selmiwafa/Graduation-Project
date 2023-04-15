package com.example.pfe.manage_prescriptions;

import java.util.ArrayList;

public class PrescriptionDataSource {
    private ArrayList<PresMedicine> summaryList = new ArrayList<>();

    public ArrayList<PresMedicine> getSummaryList() {
        return summaryList;
    }

    public void addPrescription(PresMedicine prescription) {
        summaryList.add(prescription);
    }
}