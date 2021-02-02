package com.bank.database.automationdatabaseservices.model;

import java.util.Date;

public class APIKeyModel {
    private String username;
    private String key;
    private String date;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", key='" + key + '\'' +
                ", date='" + date + '\''+
                '}';
    }
}
