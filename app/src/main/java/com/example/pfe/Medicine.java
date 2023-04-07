package com.example.pfe;

public class Medicine {
    private String barcode;
    private String med_name;
    private int quantity;
    private String description;
    private String exp_date;
    private boolean isSelected;
    public Medicine (
            String barcode,
            String med_name,
            int quantity,
            String description,
            String exp_date
    ) {
        this.barcode=barcode;
        this.med_name=med_name;
        this.quantity=quantity;
        this.description=description;
        this.exp_date=exp_date;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
