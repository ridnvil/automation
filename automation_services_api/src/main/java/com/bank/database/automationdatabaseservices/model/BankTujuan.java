package com.bank.database.automationdatabaseservices.model;

public class BankTujuan {
    private String uniqueid;
    private String username;
    private String mpin;
    private String bank;
    private String rekening;
    private Double nominal;
    private String password;
    private String udid;
    private int port;

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public Double getNominal() {
        return nominal;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "{" +
                "uniqueid='" + uniqueid + '\'' +
                ", username='" + username + '\'' +
                ", mpin='" + mpin + '\'' +
                ", bank='" + bank + '\'' +
                ", rekening='" + rekening + '\'' +
                ", nominal=" + nominal +
                ", password='" + password + '\'' +
                ", udid='" + udid + '\'' +
                ", port=" + port +
                '}';
    }
}
