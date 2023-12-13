package com.example.ktx_ute.model;

public class Message extends ChatModel {
    private int id;
    private String username;
    private String message;
    private String time;

    public Message(int id, String username, String message, String time) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
