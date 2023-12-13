package com.example.ktx_ute.model;

public class PushNotification {
    private String token;
    private DataMessage data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DataMessage getData() {
        return data;
    }

    public void setData(DataMessage data) {
        this.data = data;
    }

    public PushNotification(String token, DataMessage notification) {
        this.token = token;
        this.data = notification;
    }

    public PushNotification(DataMessage notification) {
        this.token = token;
        this.data = notification;
    }
}
