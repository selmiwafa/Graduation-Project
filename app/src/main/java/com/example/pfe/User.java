package com.example.pfe;

import java.util.ArrayList;
import java.util.Random;

public class User {
    private String email;
    private String name;
    private String birthdate;
    private String password;
    private String adress;
    private String code;
    private ArrayList<Patient> PatientList = new ArrayList<>();

    public User(String email, String name, String birthdate, String password, String adress) {
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.password = password;
        this.adress = adress;
    }

    public void setArray(ArrayList<Patient> Array) {
        this.PatientList = Array;
    }

    public String getCode() {
        return code;
    }

    public String setCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getBirthdate() {
        return this.birthdate;
    }

    public String getPassword() {
        return this.password;
    }
    public String getAdress() {
        return this.adress;
    }

}