package com.bank.database.automationdatabaseservices.model;

import java.util.List;

public class TransferModel {
    private String uniqueid;
    private String udid;
    private String bank;
    private List<Tujuan> tujuanList;

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public List<Tujuan> getTujuan() {
        return tujuanList;
    }

    public void setTujuan(List<Tujuan> tujuanList) {
        this.tujuanList = tujuanList;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
