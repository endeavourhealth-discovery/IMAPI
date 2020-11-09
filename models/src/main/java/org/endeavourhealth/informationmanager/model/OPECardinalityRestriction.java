package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonPropertyOrder({"property","inverseOf","quantification","exact","min","max","clazz","intersection","union","propertyObject"})
public class OPECardinalityRestriction extends ClassExpression {

    private ConceptReference property;
    private ConceptReference inverseOf;
    private String quantification;
    private Integer exact;
    private Integer min;
    private Integer max;
    private ConceptReference individual;
    // subClassExpression

    @JsonProperty("Property")
    public ConceptReference getProperty() {
        return property;
    }

    @JsonSetter
    public OPECardinalityRestriction setProperty(ConceptReference property) {
        this.property = property;
        return this;
    }

    public OPECardinalityRestriction setProperty(String property) {
        this.property = new ConceptReference(property);
        return this;
    }


    @JsonProperty("Quantification")
    public String getQuantification() {
        return quantification;
    }

    public OPECardinalityRestriction setQuantification(String quantification) {
        this.quantification = quantification;
        return this;
    }
    @JsonProperty("Exact")
    public Integer getExact() {
        return exact;
    }

    public OPECardinalityRestriction setExact(Integer exact) {
        this.exact = exact;
        return this;
    }
    @JsonProperty("Min")
    public Integer getMin() {
        return min;
    }

    public OPECardinalityRestriction setMin(Integer min) {
        this.min = min;
        return this;
    }
    @JsonProperty("Max")
    public Integer getMax() {
        return max;
    }

    public OPECardinalityRestriction setMax(Integer max) {
        this.max = max;
        return this;
    }

    @JsonProperty("Individual")
    public ConceptReference getIndividual() {
        return individual;
    }

    @JsonSetter
    public OPECardinalityRestriction setIndividual(ConceptReference individual) {
        this.individual = individual;
        return this;
    }

    public OPECardinalityRestriction setIndividual(String individual) {
        this.individual = new ConceptReference(individual);
        return this;
    }

    @JsonProperty("InverseOf")
    public ConceptReference getInverseOf() {
        return inverseOf;
    }

    @JsonSetter
    public OPECardinalityRestriction setInverseOf(ConceptReference inverseOf) {
        this.inverseOf = inverseOf;
        return this;
    }
    public OPECardinalityRestriction setInverseOf(String inverseOf) {
        this.inverseOf = new ConceptReference(inverseOf);
        return this;
    }


}
