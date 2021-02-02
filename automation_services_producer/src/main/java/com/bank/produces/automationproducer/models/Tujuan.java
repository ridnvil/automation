package com.bank.produces.automationproducer.models;

public class Tujuan {
    private String rekening;
    private double nominal;

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

    @Override
    public String toString() {
        return "{" +
                "rekening='" + rekening + '\'' +
                ", nominal=" + nominal +
                '}';
    }
}
