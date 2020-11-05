package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubPropertyChain extends Axiom{
    private List<String> property;

    @JsonProperty("Property")
    public List<String> getProperty() {
        return property;
    }

    public SubPropertyChain setProperty(List<String> property) {
        this.property = property;
        return this;
    }
}
