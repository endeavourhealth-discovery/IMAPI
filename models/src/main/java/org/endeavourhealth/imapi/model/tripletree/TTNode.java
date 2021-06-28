package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.model.tripletree.json.TTLiteralSerializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeDeserializerV2;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializerV2;

import java.util.*;

@JsonSerialize(using = TTNodeSerializerV2.class)
@JsonDeserialize(using = TTNodeDeserializerV2.class)
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

    @Override
    public TTArray asArray(){
        return new TTArray().add(this);
    }

    @Override
    public List<TTValue> asArrayElements(){
        return new ArrayList<>(Collections.singletonList(this));
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
