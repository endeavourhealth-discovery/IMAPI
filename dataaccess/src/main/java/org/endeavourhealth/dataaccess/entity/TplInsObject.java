package org.endeavourhealth.dataaccess.entity;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class TplInsObject {
    private int dbid;
    private int group;
    private Integer bnode;
    private TTIriRef predicate;
    private TTIriRef object;

    public int getDbid() {
        return dbid;
    }

    public TplInsObject setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public int getGroup() {
        return group;
    }

    public TplInsObject setGroup(int group) {
        this.group = group;
        return this;
    }

    public Integer getBnode() {
        return bnode;
    }

    public TplInsObject setBnode(Integer bnode) {
        this.bnode = bnode;
        return this;
    }

    public TTIriRef getPredicate() {
        return predicate;
    }

    public TplInsObject setPredicate(TTIriRef predicate) {
        this.predicate = predicate;
        return this;
    }

    public TTIriRef getObject() {
        return object;
    }

    public TplInsObject setObject(TTIriRef object) {
        this.object = object;
        return this;
    }
}
