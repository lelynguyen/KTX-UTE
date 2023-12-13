package com.example.ktx_ute.model;

public class DataMessage {
    private String type = "-1", title = "", body = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public DataMessage(int type) {
        this.type = String.valueOf(type);
    }

    public DataMessage(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public DataMessage(int type, String title, String body) {
        this.type = String.valueOf(type);
        this.title = title;
        this.body = body;
    }
}
