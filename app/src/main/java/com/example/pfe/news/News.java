package com.example.pfe.news;

import java.util.Date;

public class News {

    private String  title, content;
    private int id;
    private Date date;
    public News (int id, String title, String content, Date date) {
        this.id=id;
        this.title=title;
        this.content=content;
        this.date=date;
    }
    public News (String title, String content, Date date) {
        this.title=title;
        this.content=content;
        this.date=date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
