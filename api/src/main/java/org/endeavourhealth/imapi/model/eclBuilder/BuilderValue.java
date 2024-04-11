package org.endeavourhealth.imapi.model.eclBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface BuilderValue {
    @JsonIgnore
    default boolean isConcept() {return false;}

    @JsonIgnore
    default boolean isRefinement() {return false;}

    @JsonIgnore
    default boolean isBoolGroup() {return false;}

    default Concept asConcept() {return null;}

    default Refinement asRefinement() {return null;}

    default BoolGroup asBoolGroup() {return null;}
}
