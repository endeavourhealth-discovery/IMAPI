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
    default boolean isNode() { return false; }

    default TTLiteral asLiteral() {return null; }
    default TTIriRef asIriRef() {return null; }
    default TTNode asNode() {return null; }
}
