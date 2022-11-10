package com.example.notesapp;

public class Model {
    String title;
    String desciption;
    String id;

    public Model(String id, String title, String desciption) {
        this.title = title;
        this.desciption = desciption;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
