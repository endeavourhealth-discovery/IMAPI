package org.endeavourhealth.imapi.dataaccess.entity;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Tpl {
    private int dbid;
    private Integer parent;
    private TTIriRef predicate;
    private TTIriRef object;
    private String literal;
    private boolean functional = true;

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

    public boolean isFunctional() {
        return functional;
    }

    public Tpl setFunctional(boolean functional) {
        this.functional = functional;
        return this;
    }
}

