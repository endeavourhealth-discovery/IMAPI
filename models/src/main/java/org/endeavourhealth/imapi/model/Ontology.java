package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({"iri","module","imports","namespace","documentInfo","concept","individual"})
public class Ontology {
    private String iri;
    private String module;
    private Set<String> imports;
    private Set<Namespace> namespace;
    private DocumentInfo documentInfo;
    private Set<Concept> concept;
    private Set<Individual> individual;



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

    @JsonProperty("Concept")
    public Set<Concept> getConcept() {
        return concept;
    }

    public Ontology setConcept(Set<Concept> concept) {
        this.concept = concept;
        return this;
    }

    public Ontology addConcept(Concept concept) {
        if (this.concept == null)
            this.concept = new HashSet<>();

        this.concept.add(concept);
        return this;
    }

    public Ontology deleteConcept(Concept concept) {
        if (this.concept != null)
            this.concept.remove(concept);

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
            this.imports = new HashSet<>();
        this.imports.add(newimport);

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

    @JsonProperty("Individual")
    public Set<Individual> getIndividual() {
        return individual;
    }

    public Ontology setIndividual(Set<Individual> individual) {
        this.individual = individual;
        return this;
    }
    public Ontology addIndividual(Individual individual){
        if (this.individual==null)
            this.individual = new HashSet<>();
        this.individual.add(individual);
        return this;
    }
}
