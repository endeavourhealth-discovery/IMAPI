package org.endeavourhealth.imapi.model.tripletree;

import java.util.HashMap;

public class TTNode extends TTValue {
    private HashMap<String, TTValue> predicateValues = new HashMap<>();

    public TTNode() {}

    public TTNode set(TTIriRef predicate, TTValue value) {
        predicateValues.put(predicate.getIri(), value);
        return this;
    }

    public TTValue get(TTIriRef predicate) {
        return predicateValues.get(predicate.getIri());
    }

    public HashMap<String, TTValue> getPredicateMap() {
        return this.predicateValues;
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
        return (TTLiteral) predicateValues.get(predicate.getIri());
    }

    public TTIriRef getAsIriRef(TTIriRef predicate) {
        return (TTIriRef) predicateValues.get(predicate.getIri());
    }

    public TTArray getAsArray(TTIriRef predicate) {
        return (TTArray) predicateValues.get(predicate.getIri());
    }

    public TTNode getAsNode(TTIriRef predicate) {
        return (TTNode) predicateValues.get(predicate.getIri());
    }

}
