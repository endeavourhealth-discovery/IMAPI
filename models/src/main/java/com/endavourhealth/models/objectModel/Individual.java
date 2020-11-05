package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Individual extends Concept{
    private String isType;
    private List<DataPropertyAssertionAxiom> propertyDataValue;


    @JsonProperty("IsType")
    public String getIsType() {
        return isType;
    }

    public Individual setIsType(String isType) {
        this.isType = isType;
        return this;
    }

    @JsonProperty("PropertyDataValue")
    public List<DataPropertyAssertionAxiom> getPropertyDataValue() {
        return propertyDataValue;
    }

    public Individual setPropertyDataValue(List<DataPropertyAssertionAxiom> propertyDataValue) {
        this.propertyDataValue = propertyDataValue;
        return this;
    }
    public Individual addPropertyDataValue(DataPropertyAssertionAxiom propertyValue){
        if (propertyDataValue ==null)
            this.propertyDataValue= new ArrayList<>();
        propertyDataValue.add(propertyValue);
        return this;
    }


}
