package com.example.pfe;

import java.util.Date;

public class Reminder {
    private int id;
    private String text;
    private Date dueDate;
    private boolean shown;

    public Reminder(int id, String text, Date dueDate, boolean shown) {
        this.id = id;
        this.text = text;
        this.dueDate = dueDate;
        this.shown = shown;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}
