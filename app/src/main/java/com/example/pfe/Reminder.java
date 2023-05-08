package com.example.pfe;

import java.util.Calendar;

public class Reminder {
    private int id;
    private String text;
    private Calendar dueDate;
    private boolean shown;

    public Reminder(int id, String text, Calendar dueDate, boolean shown) {
        this.id = id;
        this.text = text;
        this.dueDate = dueDate;
        this.shown = shown;
    }
    public Reminder(int id, String text, Calendar dueDate) {
        this.id = id;
        this.text = text;
        this.dueDate = dueDate;
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

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}
