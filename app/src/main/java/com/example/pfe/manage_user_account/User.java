package com.example.pfe.manage_user_account;

import com.example.pfe.manage_patient_account.Patient;

import java.util.ArrayList;
import java.util.Random;

public class User {
    private final String email;
    private final String name;
    private final String birthdate;
    private final String password;
    private final String adress;
    private String number;
    private String code;
    private ArrayList<Patient> PatientList = new ArrayList<>();

    public User(String email, String name, String birthdate, String password, String adress, String number) {
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.password = password;
        this.adress = adress;
        this.number=number;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}