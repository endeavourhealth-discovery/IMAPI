package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Annotation {
    private ConceptReference property;
    private String value;

    @JsonProperty("Property")
    public ConceptReference getProperty() {
        return property;
    }

    public Annotation setProperty(ConceptReference property) {
        this.property = property;
        return this;
    }

    public Annotation setProperty(String property) {
        this.property = new ConceptReference(property);
        return this;
    }


    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    public Annotation setValue(String value) {
        this.value = value;
        return this;
    }
}
