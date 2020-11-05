package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"property","quantification","min","max","exact","dataType","value","oneOf","dataTypeRestriction"})
public class DPECardinalityRestriction extends DataRangeImpl {
    private String property;
    private String quantification;
    private Integer exact;
    private Integer min;
    private Integer max;



    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public DPECardinalityRestriction setProperty(String property) {
        this.property = property;
        return this;
    }
    @JsonProperty("Quantification")
    public String getQuantification() {
        return quantification;
    }

    public DPECardinalityRestriction setQuantification(String quantification) {
        this.quantification = quantification;
        return this;
    }
    @JsonProperty("Exact")
    public Integer getExact() {
        return exact;
    }

    public DPECardinalityRestriction setExact(Integer exact) {
        this.exact = exact;
        return this;
    }

    @JsonProperty("Min")
    public Integer getMin() {
        return min;
    }

    public DPECardinalityRestriction setMin(Integer min) {
        this.min = min;
        return this;
    }
    @JsonProperty("Max")
    public Integer getMax() {
        return max;
    }

    public DPECardinalityRestriction setMax(Integer max) {
        this.max = max;
        return this;
    }
}
