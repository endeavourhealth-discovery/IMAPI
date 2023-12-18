package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTDocumentDeserializer;
import org.endeavourhealth.imapi.json.TTDocumentSerializer;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonSerialize(using = TTDocumentSerializer.class)
@JsonDeserialize(using = TTDocumentDeserializer.class)
public class TTDocument extends TTNode {
    private TTIriRef graph;
    private TTContext context = new TTContext();
    private List<TTEntity> entities;
    private TTIriRef crud;
    private Map<String,String> predicates = new HashMap<>();

    public Map<String, String> getPredicates() {
        return predicates;
    }

    public TTDocument setPredicates(Map<String, String> predicates) {
        this.predicates = predicates;
        return this;
    }

    public TTIriRef getGraph() {
        return graph;
    }

    public TTDocument(TTIriRef defaultGraph) {
        this.graph = defaultGraph;
    }
    public TTDocument(Vocabulary defaultGraph) {
        this.graph = defaultGraph.asTTIriRef();
    }

    public TTDocument() {
    }

    public TTDocument setContext(TTContext context) {
        this.context = context;
        return this;
    }

    public TTDocument setGraph(TTIriRef graph) {
        this.graph = graph;
        return this;
    }
    public TTDocument setGraph(Vocabulary graph) {
        return setGraph(graph.asTTIriRef());
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

    @Override
    public TTDocument set(TTIriRef predicate, TTValue value) {
        super.set(predicate, value);
        return this;
    }

    public List<TTEntity> getEntities() {
        return entities;
    }

    public TTDocument setEntities(List<TTEntity> entities) {
        this.entities = entities;
        return this;
    }

    public TTDocument addEntity(TTEntity entity) {
        if (this.entities == null)
            this.entities = new ArrayList<>();
        entity.setContext(this.context);
        this.entities.add(entity);
        return this;
    }


    public TTContext getContext() {
        return this.context;
    }

    public TTIriRef getCrud() {
        return crud;
    }

    public TTDocument setCrud(TTIriRef crud) {
        this.crud = crud;
        return this;
    }

    public TTDocument setCrud(Vocabulary crud) {
        return setCrud(crud.asTTIriRef());
    }

}
