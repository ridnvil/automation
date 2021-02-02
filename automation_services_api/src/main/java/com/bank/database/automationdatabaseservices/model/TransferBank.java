package com.bank.database.automationdatabaseservices.model;

import com.sun.istack.Nullable;
import org.hibernate.annotations.NotFound;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TransferBank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uniqueid;
    private String bank;
    @Nullable
    @NotFound
    private String username;
    @Nullable
    @NotFound
    private String mpin;
    private String rekening;
    private double nominal;
    @Nullable
    @NotFound
    private String password;
    private String udid;
    private int port;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
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

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
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

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createat) {
        this.createat = createat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", uniqueid='" + uniqueid + '\'' +
                ", bank='" + bank + '\'' +
                ", username='" + username + '\'' +
                ", mpin='" + mpin + '\'' +
                ", rekening='" + rekening + '\'' +
                ", nominal=" + nominal +
                ", password='" + password + '\'' +
                ", udid='" + udid + '\'' +
                ", port=" + port +
                ", status='" + status + '\'' +
                ", createat=" + createat +
                '}';
    }
}
