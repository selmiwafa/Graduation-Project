package com.example.pfe.links;
public class Card {
    private final String title;
    private int imageResId;
    private final Class<?> activityClass;

    public Card(String title, Class<?> activityClass) {
        this.title = title;
        this.imageResId = imageResId;
        this.activityClass = activityClass;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }
    public void settImageResId(int imageResId) {
        this.imageResId= imageResId;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }
}
