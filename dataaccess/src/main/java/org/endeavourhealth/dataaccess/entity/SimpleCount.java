package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SimpleCount {

    @Id
    private int dbid;
    private String iri;
    private String label;
    private Integer count;

    public int getDbid() {
        return dbid;
    }

    public SimpleCount setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public SimpleCount setIri(String iri) {
        this.iri = iri;
        return this;
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
}
