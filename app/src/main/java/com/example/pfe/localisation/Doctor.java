package com.example.pfe.localisation;

public class Doctor {
    private String name, address;
    private double longitude,latitude;
    public Doctor (String n, String a, double la, double lon){
        this.name = n;
        this.address = a;
        this.longitude = lon;
        this.latitude = la;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }
}
