package com.bank.database.automationdatabaseservices.model;

import java.util.Date;

public class BcaCrawlModel {
    private int id;
    private int id_account;
    private Date date_trans;
    private String description;
    private int branch;
    private String value_trans;
    private String type_trans;
    private String balance;
    private Date createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public Date getDate_trans() {
        return date_trans;
    }

    public void setDate_trans(Date date_trans) {
        this.date_trans = date_trans;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public String getValue_trans() {
        return value_trans;
    }

    public void setValue_trans(String value_trans) {
        this.value_trans = value_trans;
    }

    public String getType_trans() {
        return type_trans;
    }

    public void setType_trans(String type_trans) {
        this.type_trans = type_trans;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", id_account=" + id_account +
                ", date_trans=" + date_trans +
                ", description='" + description + '\'' +
                ", branch=" + branch +
                ", value_trans='" + value_trans + '\'' +
                ", type_trans='" + type_trans + '\'' +
                ", balance='" + balance + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
