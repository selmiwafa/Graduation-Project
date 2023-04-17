package com.example.pfe.appointments;

public class Appointment {
    private String id;
    private String name;
    private String date;
    private String time;
    private String category;

    public Appointment(String name, String date, String time,String category){
        this.name=name;
        this.id = name+date;
        this.date = date;
        this.time = time;
        this.category = category;
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
}
