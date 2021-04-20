package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SearchResultId implements Serializable {

    private int dbid;
    private String match;

    public int getDbid() {
        return dbid;
    }

    public SearchResultId setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getMatch() {
        return match;
    }

    public SearchResultId setMatch(String match) {
        this.match = match;
        return this;
    }
}
