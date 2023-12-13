package com.example.ktx_ute.model;

public class UserToken {
    private String userID;
    private String roomNumber;
    private String token;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserToken() {}

    public UserToken(String userID, String roomNumber, String token) {
        this.userID = userID;
        this.roomNumber = roomNumber;
        this.token = token;
    }
}
