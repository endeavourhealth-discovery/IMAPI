package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class Individual extends Concept{
    private ConceptReference isType;
    private Set<DataPropertyAssertionAxiom> propertyDataValue;


    @JsonProperty("IsType")
    public ConceptReference getIsType() {
        return isType;
    }

    public Individual setIsType(ConceptReference isType) {
        this.isType = isType;
        return this;
    }
    public Individual setIsType(String isType) {
        this.isType = new ConceptReference(isType);
        return this;
    }

    @JsonProperty("PropertyDataValue")
    public Set<DataPropertyAssertionAxiom> getPropertyDataValue() {
        return propertyDataValue;
    }

    public Individual setPropertyDataValue(Set<DataPropertyAssertionAxiom> propertyDataValue) {
        this.propertyDataValue = propertyDataValue;
        return this;
    }
    public Individual addPropertyDataValue(DataPropertyAssertionAxiom propertyValue){
        if (propertyDataValue ==null)
            this.propertyDataValue= new HashSet<>();
        propertyDataValue.add(propertyValue);
        return this;
    }


}
