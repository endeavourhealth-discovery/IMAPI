package org.endeavourhealth.dataaccess.entity;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Tpl {
    private int dbid;
    private Integer parent;
    private TTIriRef predicate;
    private TTIriRef object;
    private String literal;

    public int getDbid() {
        return dbid;
    }

    public Tpl setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public Integer getParent() {
        return parent;
    }

    public Tpl setParent(Integer parent) {
        this.parent = parent;
        return this;
    }

    public TTIriRef getPredicate() {
        return predicate;
    }

    public Tpl setPredicate(TTIriRef predicate) {
        this.predicate = predicate;
        return this;
    }

    public TTIriRef getObject() {
        return object;
    }

    public Tpl setObject(TTIriRef object) {
        this.object = object;
        return this;
    }

    public String getLiteral() {
        return literal;
    }

    public Tpl setLiteral(String literal) {
        this.literal = literal;
        return this;
    }
}

