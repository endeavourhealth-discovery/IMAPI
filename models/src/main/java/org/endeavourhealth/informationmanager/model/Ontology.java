package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class Ontology {
    private String iri;
    private String module;
    private Set<String> imports;
    private Set<Namespace> namespace;
    private DocumentInfo documentInfo;
    private Set<Clazz> clazz;
    private Set<ObjectProperty> objectProperty;
    private Set<DataProperty> dataProperty;
    private Set<DataType> dataType;
    private Set<AnnotationProperty> annotationProperty;
    private Set<Individual> individual;

    @JsonProperty("Individual")
    public Set<Individual> getIndividual() {
        return individual;
    }

    public Ontology setIndividual(Set<Individual> individual) {
        this.individual = individual;
        return this;
    }

    public Ontology addIndividual(Individual individual) {
        if (this.individual == null)
            this.individual = new HashSet<>();
        this.individual.add(individual);

        return this;
    }


    @JsonProperty("Namespace")
    public Set<Namespace> getNamespace() {
        return namespace;
    }

    public Ontology setNamespace(Set<Namespace> namespace) {
        this.namespace = namespace;
        return this;
    }

    public Ontology addNamespace(Namespace namespace) {
        if (this.namespace == null)
            this.namespace = new HashSet<>();

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
    public Set<Clazz> getClazz() {
        return clazz;
    }

    public Ontology setClazz(Set<Clazz> clazz) {
        this.clazz = clazz;
        return this;
    }

    public Ontology addClazz(Clazz clazz) {
        if (this.clazz == null)
            this.clazz = new HashSet<>();

        this.clazz.add(clazz);
        return this;
    }

    public Ontology deleteClazz(Clazz clazz) {
        if (this.clazz != null)
            this.clazz.remove(clazz);

        return this;
    }

    @JsonProperty("ObjectProperty")
    public Set<ObjectProperty> getObjectProperty() {
        return objectProperty;
    }

    public Ontology setObjectProperty(Set<ObjectProperty> objectProperty) {
        this.objectProperty = objectProperty;
        return this;
    }

    public Ontology addObjectProperty(ObjectProperty objectProperty) {
        if (this.objectProperty == null)
            this.objectProperty = new HashSet<>();

        this.objectProperty.add(objectProperty);
        return this;
    }

    @JsonProperty("DataProperty")
    public Set<DataProperty> getDataProperty() {
        return dataProperty;
    }

    public Ontology setDataProperty(Set<DataProperty> dataProperty) {
        this.dataProperty = dataProperty;
        return this;
    }

    public Ontology addDataProperty(DataProperty dataProperty) {
        if (this.dataProperty == null)
            this.dataProperty = new HashSet<>();
        this.dataProperty.add(dataProperty);
        return this;
    }

    @JsonProperty("DataType")
    public Set<DataType> getDataType() {
        return dataType;
    }

    public Ontology setDataType(Set<DataType> dataType) {
        this.dataType = dataType;
        return this;
    }
    @JsonProperty("Import")
    public Set<String> getImports() {
        return imports;
    }

    public Ontology setImports(Set<String> imports) {
        this.imports = imports;
        return this;
    }
    public Ontology addImport(String newimport) {
        if (this.imports == null)
            this.imports = new HashSet<String>();
        this.imports.add(newimport);

        return this;
    }

    public Ontology addDataType(DataType dataType) {
        if (this.dataType == null)
            this.dataType = new HashSet<>();
        this.dataType.add(dataType);
        return this;
    }

    @JsonProperty("AnnotationProperty")
    public Set<AnnotationProperty> getAnnotationProperty() {
        return annotationProperty;
    }

    public Ontology setAnnotationProperty(Set<AnnotationProperty> annotationProperty) {
        this.annotationProperty = annotationProperty;
        return this;
    }

    public Ontology addAnnotationProperty(AnnotationProperty annotationProperty) {
        if (this.annotationProperty == null)
            this.annotationProperty = new HashSet<>();

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
