package com.example.pfe.manage_prescriptions;

import java.util.ArrayList;

public class Prescription {
    private String id,name, start, end, owner;
    private ArrayList<PresMedicine> medicineArrayList;
    public Prescription (String name, String start, String end, String owner){
        this.id=name+start;
        this.name=name;
        this.start=start;
        this.end=end;
        this.owner=owner;
    }
    public Prescription (String name, String start, String end, String owner, ArrayList<PresMedicine> medicineArrayList){
        this.id=name+start;
        this.name=name;
        this.start=start;
        this.end=end;
        this.owner=owner;
        this.medicineArrayList=medicineArrayList;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
