package org.endeavourhealth.imapi.model.config;

public class MetricsGraphite {
    private String address;
    private int port;

    public String getAddress() {
        return address;
    }

    public MetricsGraphite setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getPort() {
        return port;
    }

    public MetricsGraphite setPort(int port) {
        this.port = port;
        return this;
    }
}
