package com.bank.database.automationdatabaseservices.model;

import java.util.Date;

public class ADBDevicesModel {
    private String udid;
    private Date timestamp;

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
