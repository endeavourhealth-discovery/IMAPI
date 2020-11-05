package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"property","inverseOf","quantification","exact","min","max","clazz","intersection","union","propertyObject"})
public class OPECardinalityRestriction extends ClassExpression {
    private String property;
    private String inverseOf;
    private String quantification;
    private Integer exact;
    private Integer min;
    private Integer max;
    private String individual;
    // subClassExpression

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public OPECardinalityRestriction setProperty(String property) {
        this.property = property;
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
    public String getIndividual() {
        return individual;
    }

    public OPECardinalityRestriction setIndividual(String individual) {
        this.individual = individual;
        return this;
    }

    @JsonProperty("InverseOf")
    public String getInverseOf() {
        return inverseOf;
    }

    public OPECardinalityRestriction setInverseOf(String inverseOf) {
        this.inverseOf = inverseOf;
        return this;
    }
}
