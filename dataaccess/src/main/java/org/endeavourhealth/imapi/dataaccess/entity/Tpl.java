package org.endeavourhealth.imapi.dataaccess.entity;

import org.endeavourhealth.imapi.model.tripletree.*;

import java.util.HashMap;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class Tpl {
    private int dbid;
    private Integer parent;
    private TTIriRef predicate;
    private TTIriRef object;
    private String literal;
    private boolean functional = true;

    public Tpl() {}

    public Tpl(int dbid, Integer parent, TTIriRef predicate, boolean functional) {
        this.dbid = dbid;
        this.parent = parent;
        this.predicate = predicate;
        this.functional = functional;
    }

    public Tpl(int dbid, Integer parent, TTIriRef predicate, TTIriRef object, boolean functional) {
        this.dbid = dbid;
        this.parent = parent;
        this.predicate = predicate;
        this.object = object;
        this.functional = functional;
    }

    public Tpl(int dbid, Integer parent, TTIriRef predicate, TTIriRef object, String literal, boolean functional) {
        this.dbid = dbid;
        this.parent = parent;
        this.predicate = predicate;
        this.object = object;
        this.literal = literal;
        this.functional = functional;
    }

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

    public static TTBundle toBundle(String iri, List<Tpl> triples) {
        TTEntity entity = new TTEntity(iri);
        TTBundle result = new TTBundle().setEntity(entity);

        // Reconstruct
        HashMap<Integer, TTNode> nodeMap = new HashMap<>();

        for (Tpl triple : triples) {
            result.addPredicate(triple.getPredicate());

            TTValue v = getValue(nodeMap, triple);

            if (triple.getParent() == null) {
                if (triple.isFunctional()) {
                    entity.set(triple.getPredicate(), v);
                } else {
                    entity.addObject(triple.getPredicate(), v);
                }
            } else {
                TTNode n = nodeMap.get(triple.getParent());
                if (n == null)
                    throw new IllegalStateException("Unknown parent node!");
                if (triple.isFunctional()) {
                    n.set(triple.getPredicate(), v);
                } else {
                    n.addObject(triple.getPredicate(), v);
                }
            }
        }

        return result;
    }

    private static TTValue getValue(HashMap<Integer, TTNode> nodeMap, Tpl triple) {
        TTValue v;

        if (triple.getLiteral() != null)
            v = literal(triple.getLiteral(), triple.getObject());
        else if (triple.getObject() != null)
            v = triple.getObject();
        else {
            v = new TTNode();
            nodeMap.put(triple.getDbid(), (TTNode) v);
        }
        return v;
    }
}

