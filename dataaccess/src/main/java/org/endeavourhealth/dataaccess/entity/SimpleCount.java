package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SimpleCount {

    @Id
    private int dbid;
    private String label;
    private Integer count;

    public SimpleCount() {
    }

    public SimpleCount(int dbid, String label, Integer count) {
        this.dbid = dbid;
        this.label = label;
        this.count = count;
    }

    public SimpleCount(String label, Integer count) {
        this.label = label;
        this.count = count;
    }



    public String getLabel() {
        return label;
    }

    public SimpleCount setLabel(String label) {
        this.label = label;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public SimpleCount setCount(Integer count) {
        this.count = count;
        return this;
    }

    public int getDbid() {
        return dbid;
    }

    public SimpleCount setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }
}
