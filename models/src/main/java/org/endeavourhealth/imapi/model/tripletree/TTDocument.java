package org.endeavourhealth.imapi.model.tripletree;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.model.tripletree.json.TTDocumentDeserializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTDocumentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonSerialize(using = TTDocumentSerializer.class)
@JsonDeserialize(using = TTDocumentDeserializer.class)
public class TTDocument extends TTNode {
    private TTIriRef graph;
    private TTContext context = new TTContext();
    private List<TTConcept> concepts;
    private List<TTInstance> individuals;
    private TTIriRef crudOperation;
    private Map<Class, List<String>> predicateTemplate;

    public TTIriRef getGraph() {
        return graph;
    }

    public TTDocument(TTIriRef defaultGraph) {
        this.graph = defaultGraph;
    }

    public TTDocument() {
    }

    public TTDocument setContext(TTContext context) {
        this.context = context;
        return this;
    }

    public TTDocument(Map<Class, List<String>> predicateTemplate) {
        this.predicateTemplate = predicateTemplate;
    }

    public TTDocument setGraph(TTIriRef graph) {
        this.graph = graph;
        return this;
    }

    public List<TTPrefix> getPrefixes() {
        return context.getPrefixes();
    }

    public TTDocument addPrefix(TTPrefix directive) {
        addPrefix(directive.getIri(), directive.getPrefix());
        return this;
    }

    public TTDocument addPrefix(String iri, String prefix) {
        context.add(iri, prefix);
        return this;
    }

    public Map<Class, List<String>> getPredicateTemplate() {
        return predicateTemplate;
    }

    public TTDocument setPredicateTemplate(Map<Class, List<String>> predicateTemplate) {
        this.predicateTemplate = predicateTemplate;
        return this;
    }

    @Override
    public TTDocument set(TTIriRef predicate, TTValue value) {
        super.set(predicate, value);
        return this;
    }

    public List<TTConcept> getConcepts() {
        return concepts;
    }

    public TTDocument setConcepts(List<TTConcept> concepts) {
        this.concepts = concepts;
        return this;
    }

    public TTDocument addConcept(TTConcept concept) {
        if (this.concepts == null)
            this.concepts = new ArrayList<>();
        concept.setContext(this.context);
        this.concepts.add(concept);
        return this;
    }

    public List<TTInstance> getIndividuals() {
        return individuals;
    }

    public TTDocument setIndividuals(List<TTInstance> individuals) {
        this.individuals = individuals;
        return this;
    }

    public TTDocument addIndividual(TTInstance instance) {
        if (this.individuals == null)
            this.individuals = new ArrayList<>();
        instance.setContext(this.context);
        this.individuals.add(instance);
        return this;
    }

    public TTContext getContext() {
        return this.context;
    }

    public TTIriRef getCrudOperation() {
        return crudOperation;
    }

    public TTDocument setCrudOperation(TTIriRef crudOperation) {
        this.crudOperation = crudOperation;
        return this;
    }
}
