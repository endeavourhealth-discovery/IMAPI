package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.model.tripletree.json.TTArrayDeserializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTArraySerializer;

import java.io.Serializable;
import java.util.*;

@JsonSerialize(using = TTArraySerializer.class)
@JsonDeserialize(using = TTArrayDeserializer.class)
public class TTArray implements TTValue, Serializable {
    private LinkedHashSet<TTValue> elements = new LinkedHashSet<>();

    public TTArray add(TTValue value) {
        if (elements != null && elements.contains(value))
            return this;

        if (elements == null)
            elements = new LinkedHashSet<>();

        elements.add(value);

        return this;
    }

    public TTNode getAsNode(int index) {
        return (TTNode)get(index);
    }
    public TTLiteral getAsLiteral(int index) {
        return (TTLiteral)get(index);
    }
    public TTIriRef getAsIriRef(int index) {
        return (TTIriRef)get(index);
    }
    public TTArray getAsArray(int index) {
        return (TTArray)get(index);
    }

    public TTValue get(int index) {
        return getElements().get(index);
    }

    public int size() {
        return elements.size();
    }
    public void remove(TTValue value){
        elements.remove(value);
    }


    public boolean contains(TTValue value) {
        return elements.contains(value);
    }

    @Override
    public TTArray asArray() {
        return this;
    }

    @Override
    public List<TTValue> getElements() {
        return new ArrayList<>(elements);
    }

    @Override
    @JsonIgnore
    public boolean isList() {
        return true;
    }
}
