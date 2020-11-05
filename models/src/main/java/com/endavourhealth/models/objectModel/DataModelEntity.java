package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataModelEntity {
    private String iri;
    private List<DataModelAttribute> property;
    private List<DataModelAttribute> relationship;

    public String getIri() {
        return iri;
    }

    public DataModelEntity setIri(String iri) {
        this.iri = iri;
        return this;
    }

    @JsonProperty("Property")
    public List<DataModelAttribute> getProperty() {
        return property;
    }

    public DataModelEntity setProperty(List<DataModelAttribute> property) {
        this.property = property;
        return this;
    }

    public DataModelEntity addProperty(DataModelAttribute property) {
        if (this.property == null)
            this.property = new ArrayList<>();

        this.property.add(property);
        return this;
    }

    @JsonProperty("Relationship")
    public List<DataModelAttribute> getRelationship() {
        return relationship;
    }

    public DataModelEntity setRelationship(List<DataModelAttribute> relationship) {
        this.relationship = relationship;
        return this;
    }

    public DataModelEntity addRelationship(DataModelAttribute relationship) {
        if (this.relationship == null)
            this.relationship = new ArrayList<>();
        this.relationship.add(relationship);

        return this;
    }
}
