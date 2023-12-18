package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTNodeDeserializerV2;
import org.endeavourhealth.imapi.json.TTNodeSerializerV2;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

import java.io.Serializable;
import java.util.*;

@JsonSerialize(using = TTNodeSerializerV2.class)
@JsonDeserialize(using = TTNodeDeserializerV2.class)
public class TTNode implements TTValue, Serializable {
    private Map<TTIriRef, TTArray> predicateValues = new HashMap<>();
    private String iri;

    @JsonProperty("@id")
    public String getIri() {
        return iri;
    }

    @JsonProperty("@id")
    public TTNode setIri(String iri) {
        this.iri = iri;
        return this;
    }

    @JsonSetter
    public TTNode set(TTIriRef predicate, TTValue value) {
        if (value==null)
            predicateValues.remove(predicate);
        else
            predicateValues.put(predicate, new TTArray().add(value));
        return this;
    }
    @JsonIgnore
    public TTNode set(Vocabulary predicate, TTValue value) {
        return set(predicate.asTTIriRef(),value);
    }
    @JsonIgnore
    public TTNode set(Vocabulary predicate, Vocabulary value) {
        return set(predicate.asTTIriRef(),value.asTTIriRef());
    }
    @JsonIgnore
    public TTNode set(TTIriRef predicate,String value){
        if (value.startsWith("http:"))
            this.set(predicate,TTIriRef.iri(value));
        else
            this.set(predicate,TTLiteral.literal(value));
        return this;
    }
    @JsonIgnore
    public TTNode set(Vocabulary predicate, String value){
        return set(predicate.asTTIriRef(),value);
    }
    @JsonIgnore
    public TTNode set(TTIriRef predicate,Integer value){
            this.set(predicate,TTLiteral.literal(value));
        return this;
    }
    @JsonIgnore
    public TTNode set(Vocabulary predicate,Integer value){
        return set(predicate.asTTIriRef(),TTLiteral.literal(value));
    }

    @JsonIgnore
    public TTNode set(TTIriRef predicate,boolean value){
        this.set(predicate,TTLiteral.literal(value));
        return this;
    }
    @JsonIgnore
    public TTNode set(Vocabulary predicate,boolean value){
        return set(predicate.asTTIriRef(),TTLiteral.literal(value));
    }

    @JsonIgnore
    public TTNode set(TTIriRef predicate,Long value){
        this.set(predicate,TTLiteral.literal(value));
        return this;
    }
    @JsonIgnore
    public TTNode set(Vocabulary predicate,Long value){
        return set(predicate.asTTIriRef(),TTLiteral.literal(value));
    }


    @JsonSetter
    public TTNode set(TTIriRef predicate, TTArray value) {
        predicateValues.put(predicate, value);
        return this;
    }
    public TTNode set(Vocabulary predicate, TTArray value) {
        return set(predicate.asTTIriRef(), value);
    }

    @JsonGetter
    public TTArray get(TTIriRef predicate) {
        return predicateValues.get(predicate);
    }
    public TTArray get(Vocabulary predicate) {
        return get(predicate.asTTIriRef());
    }

    public boolean has(TTIriRef predicate) {
        return predicateValues.containsKey(predicate);
    }
    public boolean has(Vocabulary predicate) {
        return has(predicate.asTTIriRef());
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

    @JsonGetter
    public TTLiteral getAsLiteral(TTIriRef predicate) {
        TTArray vals = get(predicate);
        return (vals == null) ? null : vals.asLiteral();
    }
    public TTLiteral getAsLiteral(Vocabulary predicate) {
        return getAsLiteral(predicate.asTTIriRef());
    }

    @JsonGetter
    public TTIriRef getAsIriRef(TTIriRef predicate) {
        TTArray vals = get(predicate);
        return (vals == null) ? null : vals.asIriRef();
    }
    public TTIriRef getAsIriRef(Vocabulary predicate) {
        return getAsIriRef(predicate.asTTIriRef());
    }

    @JsonGetter
    public TTNode getAsNode(TTIriRef predicate) {
        TTArray vals = get(predicate);
        return (vals == null) ? null : vals.asNode();
    }
    public TTNode getAsNode(Vocabulary predicate) {
        return getAsNode(predicate.asTTIriRef());
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
    public TTNode addObject(Vocabulary predicate, TTValue object){
        return addObject(predicate.asTTIriRef(),object);
    }
    public TTNode addObject(Vocabulary predicate, Vocabulary object){
        return addObject(predicate.asTTIriRef(),object.asTTIriRef());
    }

    /**
     * Adds a String or string iri to a predicate if necessary converting to an array if not already an array
     * @param predicate the predicate to add the object to. This may or may not already exist
     * @return the modified node with a predicate object as an array
     */

    public TTNode addObject(TTIriRef predicate, String value){
        if (value.startsWith("http:"))
            this.addObject(predicate,TTIriRef.iri(value));
        else
            this.addObject(predicate,TTLiteral.literal(value));
        return this;
    }
    public TTNode addObject(Vocabulary predicate, String value){
        return addObject(predicate.asTTIriRef(),value);
    }

    /**
     * Adds an integer value to a predicate if necessary converting to an array if not already an array
     * @param predicate the predicate to add the object to. This may or may not already exist
     * @return the modified node with a predicate object as an array
     */

    public TTNode addObject(TTIriRef predicate, Integer value){
        this.addObject(predicate,TTLiteral.literal(value));
        return this;
    }
    public TTNode addObject(Vocabulary predicate, Integer value){
        return addObject(predicate.asTTIriRef(),value);
    }
    /**
     * Adds an integer value to a predicate if necessary converting to an array if not already an array
     * @param predicate the predicate to add the object to. This may or may not already exist
     * @return the modified node with a predicate object as an array
     */

    public TTNode addObject(TTIriRef predicate, boolean value){
        this.addObject(predicate,TTLiteral.literal(value));
        return this;
    }
    public TTNode addObject(Vocabulary predicate, boolean value){
        return addObject(predicate.asTTIriRef(),value);
    }

    /**
     * Adds an integer value to a predicate if necessary converting to an array if not already an array
     * @param predicate the predicate to add the object to. This may or may not already exist
     * @return the modified node with a predicate object as an array
     */

    public TTNode addObject(TTIriRef predicate, Long value){
        this.addObject(predicate,TTLiteral.literal(value));
        return this;
    }
    public TTNode addObject(Vocabulary predicate, Long value){
        return addObject(predicate.asTTIriRef(),value);
    }


}
