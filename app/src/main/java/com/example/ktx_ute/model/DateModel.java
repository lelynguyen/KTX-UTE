package com.example.ktx_ute.model;

import android.widget.ArrayAdapter;

import com.example.ktx_ute.adapter.MessageAdapter;

public class DateModel extends ChatModel {
    private String datetime;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public DateModel(String dateTime) {
        this.datetime = dateTime;
        setType(MessageAdapter.TYPE_DATE);
    }
}
