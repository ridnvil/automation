package com.bank.database.automationdatabaseservices.model;

import java.util.List;

public class DataBankModel {
    private String status;
    private int code;
    private String message;
    private List<BcaCrawlModel> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public List<BcaCrawlModel> getResult() {
        return result;
    }

    public void setResult(List<BcaCrawlModel> result) {
        this.result = result;
    }
}
