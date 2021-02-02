package com.bank.database.automationdatabaseservices.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ResponseModel {
    private int code;
    private String uniqueid;
    private String message;
    private List<Map<String, String>> data;
    private Date time;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }
}
