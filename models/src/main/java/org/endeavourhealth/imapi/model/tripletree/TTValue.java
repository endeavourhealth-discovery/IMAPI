package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public interface TTValue {
    @JsonIgnore
    default boolean isLiteral() { return false; }

    @JsonIgnore
    default boolean isIriRef() {return false; }

    @JsonIgnore
    default boolean isList() { return false; }

    @JsonIgnore
    default boolean isNode() { return false; }

    @JsonIgnore
    default boolean isTypedIri() { return false;}

    @JsonIgnore
    default List<TTValue> getElements() {return new ArrayList<>(); }

    default TTLiteral asLiteral() {return null; }
    default TTIriRef asIriRef() {return null; }
    default TTArray asArray() {return null; }
    default TTNode asNode() {return null; }
    default TTIriRef asTypedIri() {return null;}
}
