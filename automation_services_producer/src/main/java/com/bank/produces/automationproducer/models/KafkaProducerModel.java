package com.bank.produces.automationproducer.models;

public class KafkaProducerModel {
    private String host;
    private int port;
    private String dbservicehost;
    private int dbserviceport;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbservicehost() {
        return dbservicehost;
    }

    public void setDbservicehost(String dbservicehost) {
        this.dbservicehost = dbservicehost;
    }

    public int getDbserviceport() {
        return dbserviceport;
    }

    public void setDbserviceport(int dbserviceport) {
        this.dbserviceport = dbserviceport;
    }
}
