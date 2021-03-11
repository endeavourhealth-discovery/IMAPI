package org.endeavourhealth.imapi.model.tripletree;

import java.util.HashMap;

public class TTNode extends TTValue {
    private HashMap<TTIriRef, TTValue> predicateValues = new HashMap<>();

    public TTNode() {}

    public TTNode set(TTIriRef predicate, TTValue value) {
        predicateValues.put(predicate, value);
        return this;
    }

    public HashMap<TTIriRef, TTValue> getPredicateMap() {
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

    TTLiteral getAsLiteral(TTIriRef predicate) {
        return (TTLiteral) predicateValues.get(predicate);
    }

    TTIriRef getAsIriRef(TTIriRef predicate) {
        return (TTIriRef) predicateValues.get(predicate);
    }

    public TTArray getAsArray(TTIriRef predicate) {
        return (TTArray) predicateValues.get(predicate);
    }

}
