package com.bank.produces.automationproducer.models;

public class BankTujuan {
    private String uniqueid;
    private String username;
    private String mpin;
    private String bank;
    private String rekening;
    private String nominal;
    private String password;
    private String udid;
    private int port;

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

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
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
    public String toString(){
        return "{'bank': '" + bank + "', 'username': '" + username + "', 'mpin': '" + mpin + "', 'rekening': '"+ rekening + "', 'nominal': "+ nominal + ", 'password': '"+ password + "', 'udid': '"+ udid + "', 'port': "+ port + "}";
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }
}
