package org.endeavourhealth.imapi.model.tripletree;

import java.util.HashMap;
import java.util.List;

public class TTNode extends TTValue {
    private HashMap<TTIriRef, TTValue> predicateValues = new HashMap<>();

    public TTNode() {}

    public TTNode set(TTIriRef predicate, TTValue value) {
        if (value==null)
            predicateValues.remove(predicate);
        else
            predicateValues.put(predicate, value);
        return this;
    }

    public TTValue get(TTIriRef predicate) {
        return predicateValues.get(predicate);
    }

    public boolean has(TTIriRef predicate) { return predicateValues.containsKey(predicate); }

    public HashMap<TTIriRef, TTValue> getPredicateMap() {
        return this.predicateValues;
    }

    public TTNode setPredicateMap(HashMap<TTIriRef,TTValue> predicateMap) {
        this.predicateValues= predicateMap;
        return this;
    }

    @Override
    public TTNode asNode() {
        return this;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    public TTLiteral getAsLiteral(TTIriRef predicate) {
        return (TTLiteral) predicateValues.get(predicate);
    }

    public TTIriRef getAsIriRef(TTIriRef predicate) {
        return (TTIriRef) predicateValues.get(predicate);
    }

    public TTArray getAsArray(TTIriRef predicate) {
        return predicateValues.get(predicate).asArray();
    }

    public List<TTValue> getAsArrayElements(TTIriRef predicate) {
        return predicateValues.get(predicate).asArrayElements();
    }



    public TTNode getAsNode(TTIriRef predicate) {
        return (TTNode) predicateValues.get(predicate);
    }

}
