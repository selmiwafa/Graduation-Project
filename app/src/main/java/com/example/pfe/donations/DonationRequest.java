package com.example.pfe.donations;

public class DonationRequest {
    private String id, barcode, date;
    private int quantity;
    public DonationRequest(String id, String barcode, int quantity, String date) {
        this.id=id;
        this.barcode=barcode;
        this.quantity=quantity;
        this.date=date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
