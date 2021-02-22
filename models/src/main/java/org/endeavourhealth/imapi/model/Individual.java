package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.Set;

public class Individual extends Concept{
    private ConceptReference isType;



    public Individual(){
        this.setConceptType(ConceptType.INDIVIDUAL);
    }

   
    public ConceptReference getIsType() {
        return isType;
    }

    public Individual setIsType(ConceptReference isType) {
        this.isType = isType;
        return this;
    }




}
