package com.example.pfe.manage_patient_account;

public class Patient {
    private String name;
    private int age;
    private String relationship;

    public Patient() {
        this.name = "";
        this.relationship = "";
        this.age = 0;
    }

    public Patient(String name, int age, String relationship) {
        this.name = name;
        this.age = age;
        this.relationship = relationship;
    }


    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public String getRelationship() {
        return this.relationship;
    }

}

