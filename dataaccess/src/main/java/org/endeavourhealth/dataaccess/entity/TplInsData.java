package org.endeavourhealth.dataaccess.entity;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class TplInsData {
    private int dbid;
    private int group;
    private Integer bnode;
    private TTIriRef predicate;
    private TTIriRef dataType;
    private String literal;

    public int getDbid() {
        return dbid;
    }

    public TplInsData setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public int getGroup() {
        return group;
    }

    public TplInsData setGroup(int group) {
        this.group = group;
        return this;
    }

    public Integer getBnode() {
        return bnode;
    }

    public TplInsData setBnode(Integer bnode) {
        this.bnode = bnode;
        return this;
    }

    public TTIriRef getPredicate() {
        return predicate;
    }

    public TplInsData setPredicate(TTIriRef predicate) {
        this.predicate = predicate;
        return this;
    }

    public TTIriRef getDataType() {
        return dataType;
    }

    public TplInsData setDataType(TTIriRef dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getLiteral() {
        return literal;
    }

    public TplInsData setLiteral(String literal) {
        this.literal = literal;
        return this;
    }
}
