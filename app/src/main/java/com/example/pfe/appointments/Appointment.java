package com.example.pfe.appointments;

public class Appointment {
    private String id;
    private String name;
    private String date;
    private String time;
    private String category;

    private String owner;

    public Appointment(String name, String date, String time,String category, String owner){
        this.name=name;
        this.id = name+date;
        this.date = date;
        this.time = time;
        this.category = category;
        this.owner=owner;
    }

    public Appointment() {
        this.name="";
        this.id = "";
        this.date = "";
        this.time = "";
        this.category = "";
        this.owner="";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
