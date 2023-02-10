package org.endeavourhealth.imapi.logic.codegen;


import java.util.ArrayList;
import java.util.List;

public class DataModel {

    private String iri;
    private String name;
    private String description;

    private List<DataModelProperty> properties = new ArrayList<>();

    public String getIri() {
        return iri;
    }

    public DataModel setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DataModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<DataModelProperty> getProperties() {
        return properties;
    }

    public DataModel setProperties(List<DataModelProperty> properties) {
        this.properties = properties;
        return this;
    }

    public DataModel addProperty(DataModelProperty property) {
        this.properties.add(property);
        return this;
    }
}
