package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.model.tripletree.json.TTArrayDeserializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTArraySerializer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

@JsonSerialize(using = TTArraySerializer.class)
@JsonDeserialize(using = TTArrayDeserializer.class)
public class TTArray implements Serializable {
    private LinkedHashSet<TTValue> elements = new LinkedHashSet<>();
    private Boolean isList;

    public TTArray add(TTValue value) {
        if (elements != null && elements.contains(value))
            return this;

        if (elements == null)
            elements = new LinkedHashSet<>();

        elements.add(value);

        return this;
    }

    public boolean isEmpty() { return elements.isEmpty(); }
    public int size() {
        return elements.size();
    }
    public boolean contains(TTValue value) {
        return elements.contains(value);
    }

    // Single element helpers
    public boolean isLiteral() {
        return elements.size() == 1 && elements.stream().findFirst().map(TTValue::isLiteral).orElse(false);
    }
    public TTLiteral asLiteral() { return (TTLiteral) elements.stream().findFirst().orElse(null); }

    public boolean isIriRef() {
        return elements.size() == 1 && elements.stream().findFirst().map(TTValue::isIriRef).orElse(false);
    }
    public TTIriRef asIriRef() { return (TTIriRef) elements.stream().findFirst().orElse(null); }

    public boolean isNode() {
        return elements.size() == 1 && elements.stream().findFirst().map(TTValue::isNode).orElse(false);
    }
    public TTNode asNode() { return (TTNode) elements.stream().findFirst().orElse(null); }

    public TTValue asValue() { return elements.stream().findFirst().orElse(null); }

    public Iterable<TTValue> iterator() {
        return elements;
    }

    public TTValue get(int index) {
        return getElements().get(index);
    }

    public List<TTValue> getElements() {
        return new ArrayList<>(elements);
    }

    public void remove(TTValue remove) {
        elements.remove(remove);
    }

    public void clear() {
        elements.clear();
    }

    public Stream<TTValue> stream() {
        return elements.stream();
    }

    public Boolean getList() {
        return isList;
    }

    public TTArray setList(Boolean list) {
        isList = list;
        return this;
    }
    @Override
    public boolean equals(Object object){
        if (getElements().size()==1) {
            if (!(object instanceof TTArray))
            if (getElements().get(0).equals(object))
                return true;
        }
        return super.equals(object);
    }

}
