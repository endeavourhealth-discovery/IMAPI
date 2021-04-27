package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Config {

    @Id
    private Integer dbid;
    private String name;
    private String config;

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

    public String getConfig() {
        return config;
    }

    public Config setConfig(String config) {
        this.config = config;
        return this;
    }
}
