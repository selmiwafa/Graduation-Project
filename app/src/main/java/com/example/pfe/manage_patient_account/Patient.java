package com.example.pfe.manage_patient_account;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String relationship;

    public Patient() {
        this.id="";
        this.name = "";
        this.relationship = "";
        this.age = 0;
    }

    public Patient(String name, int age, String relationship) {
        this.id=name+age+relationship;
        this.name = name;
        this.age = age;
        this.relationship = relationship;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

