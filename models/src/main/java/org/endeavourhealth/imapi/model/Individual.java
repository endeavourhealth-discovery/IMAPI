package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({"id","status","version","iri","name","description",
    "code","scheme","annotations","isType","objectPropertyValue","dataPropertyValue"})
public class Individual extends Concept{
    private ConceptReference isType;
    private Set<ObjectPropertyValue> objectPropertyAssertion;
    private Set<DataPropertyValue> dataPropertyAssertion;


    public Individual(){
        this.setConceptType(ConceptType.INDIVIDUAL);
    }

    @JsonProperty("IsType")
    public ConceptReference getIsType() {
        return isType;
    }

    public Individual setIsType(ConceptReference isType) {
        this.isType = isType;
        return this;
    }


    @JsonProperty("ObjectPropertyAssertion")
    public Set<ObjectPropertyValue> getObjectPropertyAssertion() {
        return objectPropertyAssertion;
    }

    public Individual setObjectPropertyAssertion(Set<ObjectPropertyValue> propertyValue) {
        this.objectPropertyAssertion = propertyValue;
        return this;
    }
    public Individual addObjectPropertyAssertion(ObjectPropertyValue propertyValue){
        if (propertyValue ==null)
            this.objectPropertyAssertion= new HashSet<>();
        this.objectPropertyAssertion.add(propertyValue);
        return this;
    }

    @JsonProperty("DataPropertyAssertion")
    public Set<DataPropertyValue> getDataPropertyAssertion() {
        return dataPropertyAssertion;
    }

    public Individual setDataPropertyAssertion(Set<DataPropertyValue> propertyValue) {
        this.dataPropertyAssertion = propertyValue;
        return this;
    }
    public Individual addDataPropertyAssertion(DataPropertyValue propertyValue){
        if (dataPropertyAssertion ==null)
            this.dataPropertyAssertion= new HashSet<>();
        this.dataPropertyAssertion.add(propertyValue);
        return this;
    }


}
