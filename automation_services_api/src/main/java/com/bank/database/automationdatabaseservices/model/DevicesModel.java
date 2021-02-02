package com.bank.database.automationdatabaseservices.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class DevicesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String udid;
    private String number;
    private String name;
    private String username;
    private String password;
    private String mpin;
    private String rekening;
    private int busy;
    private Double credit;
    private String active;
    private String status;
    private String provider;
    private Date createdAt;
    private String bank;
    private int destination;
    private int templogin;

    @Column(updatable = true)
    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getBusy() {
        return busy;
    }

    public void setBusy(int busy) {
        this.busy = busy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", udid='" + udid + '\'' +
                ", number='" + number + '\'' +
                ", busy=" + busy +
                ", credit=" + credit +
                ", active='" + active + '\'' +
                ", status='" + status + '\'' +
                ", provider='" + provider + '\'' +
                ", createdAt=" + createdAt +
                ", destination=" + destination +
                ", updateAt=" + updateAt +
                '}';
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public int getTemplogin() {
        return templogin;
    }

    public void setTemplogin(int templogin) {
        this.templogin = templogin;
    }
}
