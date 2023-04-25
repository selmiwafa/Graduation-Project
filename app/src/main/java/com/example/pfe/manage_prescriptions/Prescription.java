package com.example.pfe.manage_prescriptions;

import java.util.ArrayList;

public class Prescription {
    private String id,name, start, end;
    private ArrayList<PresMedicine> medicineArrayList;
    public Prescription (String name, String start, String end){
        this.id=name+start;
        this.name=name;
        this.start=start;
        this.end=end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public ArrayList<PresMedicine> getMedicineArrayList() {
        return medicineArrayList;
    }

    public void setMedicineArrayList(ArrayList<PresMedicine> medicineArrayList) {
        this.medicineArrayList = medicineArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
