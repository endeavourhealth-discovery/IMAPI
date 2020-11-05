package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

public class Ontology {
    private String iri;
    private String module;
    private List<String> imports;
    private List<Namespace> namespace;
    private DocumentInfo documentInfo;
    private List<Clazz> clazz;
    private List<ObjectProperty> objectProperty;
    private List<DataProperty> dataProperty;
    private List<DataType> dataType;
    private List<AnnotationProperty> annotationProperty;
    private List<Individual> individual;

    @JsonProperty("Individual")
    public List<Individual> getIndividual() {
        return individual;
    }

    public Ontology setIndividual(List<Individual> individual) {
        this.individual = individual;
        return this;
    }

    public Ontology addIndividual(Individual individual) {
        if (this.individual == null)
            this.individual = new ArrayList<>();
        this.individual.add(individual);

        return this;
    }


    @JsonProperty("Namespace")
    public List<Namespace> getNamespace() {
        return namespace;
    }

    public Ontology setNamespace(List<Namespace> namespace) {
        this.namespace = namespace;
        return this;
    }

    public Ontology addNamespace(Namespace namespace) {
        if (this.namespace == null)
            this.namespace = new ArrayList<>();

        this.namespace.add(namespace);
        return this;
    }

    @JsonProperty("DocumentInfo")
    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public Ontology setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
        return this;
    }

    @JsonProperty("Class")
    public List<Clazz> getClazz() {
        return clazz;
    }

    public Ontology setClazz(List<Clazz> clazz) {
        this.clazz = clazz;
        return this;
    }

    public Ontology addClazz(Clazz clazz) {
        if (this.clazz == null)
            this.clazz = new ArrayList<>();

        this.clazz.add(clazz);
        return this;
    }

    public Ontology deleteClazz(Clazz clazz) {
        if (this.clazz != null)
            this.clazz.remove(clazz);

        return this;
    }

    @JsonProperty("ObjectProperty")
    public List<ObjectProperty> getObjectProperty() {
        return objectProperty;
    }

    public Ontology setObjectProperty(List<ObjectProperty> objectProperty) {
        this.objectProperty = objectProperty;
        return this;
    }

    public Ontology addObjectProperty(ObjectProperty objectProperty) {
        if (this.objectProperty == null)
            this.objectProperty = new ArrayList<>();

        this.objectProperty.add(objectProperty);
        return this;
    }

    @JsonProperty("DataProperty")
    public List<DataProperty> getDataProperty() {
        return dataProperty;
    }

    public Ontology setDataProperty(List<DataProperty> dataProperty) {
        this.dataProperty = dataProperty;
        return this;
    }

    public Ontology addDataProperty(DataProperty dataProperty) {
        if (this.dataProperty == null)
            this.dataProperty = new ArrayList<>();
        this.dataProperty.add(dataProperty);
        return this;
    }

    @JsonProperty("DataType")
    public List<DataType> getDataType() {
        return dataType;
    }

    public Ontology setDataType(List<DataType> dataType) {
        this.dataType = dataType;
        return this;
    }
    @JsonProperty("Import")
    public List<String> getImports() {
        return imports;
    }

    public Ontology setImports(List<String> imports) {
        this.imports = imports;
        return this;
    }
    public Ontology addImport(String newimport) {
        if (this.imports == null)
            this.imports = new ArrayList<String>();
        this.imports.add(newimport);

        return this;
    }

    public Ontology addDataType(DataType dataType) {
        if (this.dataType == null)
            this.dataType = new ArrayList<>();
        this.dataType.add(dataType);
        return this;
    }

    @JsonProperty("AnnotationProperty")
    public List<AnnotationProperty> getAnnotationProperty() {
        return annotationProperty;
    }

    public Ontology setAnnotationProperty(List<AnnotationProperty> annotationProperty) {
        this.annotationProperty = annotationProperty;
        return this;
    }

    public Ontology addAnnotationProperty(AnnotationProperty annotationProperty) {
        if (this.annotationProperty == null)
            this.annotationProperty = new ArrayList<>();

        this.annotationProperty.add(annotationProperty);
        return this;
    }

    @JsonProperty("iri")
    public String getIri() {
        return iri;
    }

    public Ontology setIri(String iri) {
        this.iri = iri;
        return this;
    }

    @JsonProperty("module")
    public String getModule() {
        return module;
    }

    public Ontology setModule(String module) {
        this.module = module;
        return this;
    }
}
