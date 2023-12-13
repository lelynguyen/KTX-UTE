package com.example.ktx_ute.model;

public class PushNotificationWrapper {
    private PushNotification message;

    public PushNotification getMessage() {
        return message;
    }

    public void setMessage(PushNotification message) {
        this.message = message;
    }

    public PushNotificationWrapper(PushNotification message) {
        this.message = message;
    }
}
