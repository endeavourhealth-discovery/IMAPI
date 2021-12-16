package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeDeserializerV2;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializerV2;

import java.io.Serializable;
import java.util.*;

@JsonSerialize(using = TTNodeSerializerV2.class)
@JsonDeserialize(using = TTNodeDeserializerV2.class)
public class TTNode implements TTValue, Serializable {
    private Map<TTIriRef, TTArray> predicateValues = new HashMap<>();
    private TTIriRef[] predicateTemplate;

    public TTNode set(TTIriRef predicate, TTValue value) {
        if (value==null)
            predicateValues.remove(predicate);
        else
            predicateValues.put(predicate, new TTArray().add(value));
        return this;
    }

    public TTNode set(TTIriRef predicate, TTArray value) {
        predicateValues.put(predicate, value);
        return this;
    }

    public TTArray get(TTIriRef predicate) {
        return predicateValues.get(predicate);
    }

    public boolean has(TTIriRef predicate) {
        return predicateValues.containsKey(predicate);
    }

    public Map<TTIriRef, TTArray> getPredicateMap() {
        return this.predicateValues;
    }

    public TTNode setPredicateMap(Map<TTIriRef, TTArray> predicateMap) {
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
        TTArray vals = get(predicate);
        return (vals == null) ? null : vals.asLiteral();
    }

    public TTIriRef getAsIriRef(TTIriRef predicate) {
        TTArray vals = get(predicate);
        return (vals == null) ? null : vals.asIriRef();
    }

    public TTNode getAsNode(TTIriRef predicate) {
        TTArray vals = get(predicate);
        return (vals == null) ? null : vals.asNode();
    }


    /**
     * Adds an object to a predicate if necessary converting to an array if not already an array
     * @param predicate the predicate to add the object to. This may or may not already exist
     * @return the modified node with a predicate object as an array
     */

    public TTNode addObject(TTIriRef predicate, TTValue object){
        if (this.get(predicate)==null)
            this.set(predicate, new TTArray().add(object));
        else
            this.get(predicate).add(object);
        return this;
    }

    @JsonIgnore
    public TTIriRef[] getPredicateTemplate() {
        return predicateTemplate;
    }

    @JsonIgnore
    public TTNode setPredicateTemplate(TTIriRef[] predicateTemplate) {
        this.predicateTemplate = predicateTemplate;
        return this;
    }
}
