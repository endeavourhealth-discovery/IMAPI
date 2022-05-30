package org.endeavourhealth.imapi.model.config;

public class Config {

    private Integer dbid;
    private String name;
    private String data;

    public Integer getDbid() {
        return dbid;
    }

    public Config setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Config setName(String name) {
        this.name = name;
        return this;
    }

    public String getData() {
        return data;
    }

    public Config setData(String data) {
        this.data = data;
        return this;
    }
}
