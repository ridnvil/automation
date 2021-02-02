package com.devices.automation.model;

import java.util.List;

public class Transfer {
    private String uniqueid;
    private String udid;
    private String bank;
    private String username;
    private String password;
    private String mpin;
    private int port;
    private List<Tujuan> tujuanList;

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public List<Tujuan> getTujuan() {
        return tujuanList;
    }

    public void setTujuan(List<Tujuan> tujuanList) {
        this.tujuanList = tujuanList;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }
}
