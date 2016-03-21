package com.placementoffice.hemanthreddy.login;

/**
 * Created by hemanthreddy on 3/13/2016.
 */
public class Notice {
    String title,date,id;

    public Notice() {
    }

    public Notice(String title, String date, String id) {
        this.title = title;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
