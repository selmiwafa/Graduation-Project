package com.example.pfe.manage_prescriptions;

public class PresMedicine {
    private String barcode;
    private String med_name;
    private int  dose, period, frequency, tpw;
    private String other;

    public PresMedicine (
            String barcode,
            String med_name,
            int dose,
            int frequency,
            int period,
            int tpw,
            String other
    ) {
        this.barcode=barcode;
        this.med_name=med_name;
        this.dose=dose;
        this.frequency=frequency;
        this.period=period;
        this.tpw=tpw;
        this.other=other;
    }
    public PresMedicine (
            String barcode,
            int dose,
            int frequency,
            int period,
            int tpw,
            String other
    ) {
        this.barcode=barcode;
        this.dose=dose;
        this.frequency=frequency;
        this.period=period;
        this.tpw=tpw;
        this.other=other;
    }

    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getTpw() {
        return tpw;
    }

    public void setTpw(int tpw) {
        this.tpw = tpw;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
