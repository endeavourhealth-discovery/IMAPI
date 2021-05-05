package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public boolean has(TTIriRef predicate) {
        if (predicateValues.containsKey(predicate))
            return true;
        return false;
    }

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
    @JsonIgnore
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

    /**
     * Adds an object to a predicate if necessary converting to an array if not already an array
     * @param predicate the predicate to add the object to. This may or may not already exist
     * @return the modified node with a predicate object as an array
     */

    public TTNode addObject(TTIriRef predicate, TTValue object){
        if (this.get(predicate)==null)
            this.set(predicate,new TTArray().add(object));
        else if (!this.get(predicate).isList()){
            TTValue oldObject= this.get(predicate);
            this.set(predicate,new TTArray().add(oldObject).add(object));
        } else
            this.get(predicate).asArray().add(object);
        return this;
    }

}
