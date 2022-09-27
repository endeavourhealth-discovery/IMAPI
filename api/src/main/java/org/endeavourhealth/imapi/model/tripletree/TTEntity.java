package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.model.tripletree.json.TTEntityDeserializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTEntitySerializer;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.Serializable;
import java.util.List;

@JsonSerialize(using = TTEntitySerializer.class)
@JsonDeserialize(using = TTEntityDeserializer.class)
public class TTEntity extends TTNode implements Serializable {
    private TTContext context = new TTContext();
    private TTIriRef crud;
    private TTIriRef graph;

    public TTEntity() {}

    public TTEntity(String iri) {

        super.setIri(iri);
    }

    public TTEntity setIri(String iri) {
        super.setIri(iri);
        return this;
    }

    // Utility methods for common predicates
    public TTEntity setName (String name) {
        set(RDFS.LABEL, TTLiteral.literal(name));
        return this;
    }

    public String getName() {
        TTLiteral literal = getAsLiteral(RDFS.LABEL);
        return (literal == null) ? null : literal.getValue();
    }

    public TTEntity setVersion (int version) {
        set(IM.VERSION, TTLiteral.literal(version));
        return this;
    }

    public int getVersion() {
        TTLiteral literal = getAsLiteral(IM.VERSION);
        return (literal == null) ? 1 : literal.intValue();
    }

    public TTEntity setDescription (String description) {
        set(RDFS.COMMENT, TTLiteral.literal(description));
        return this;
    }

    public String getDescription() {
        TTLiteral literal = getAsLiteral(RDFS.COMMENT);
        return (literal == null) ? null : literal.getValue();
    }

    public TTEntity setCode(String code) {
        set(IM.CODE, TTLiteral.literal(code));
        return this;
    }

    public String getCode() {
        TTLiteral literal = getAsLiteral(IM.CODE);
        return (literal == null) ? null : literal.getValue();
    }

    public TTEntity setScheme(TTIriRef scheme) {
        set(IM.HAS_SCHEME, scheme);
        return this;
    }

    public TTIriRef getScheme() {
        return this.getAsIriRef(IM.HAS_SCHEME);
    }

    public TTEntity setType(TTArray type) {
        set(RDF.TYPE, type);
        return this;
    }

    public TTEntity addType(TTIriRef type) {
        TTArray types;
        if (has(RDF.TYPE)) {
            types = get(RDF.TYPE);
        } else {
            types = new TTArray();
            setType(types);
        }
        types.add(type);
        return this;
    }
    public boolean isType(TTIriRef type){
        if (this.getType()!=null){
            return this.getType().contains(type);
        }
        return false;
    }

    public TTArray getType() {
        if (get(RDF.TYPE)==null)
            return null;
        else
         return get(RDF.TYPE);
    }

    public TTIriRef getStatus(){
        return this.getAsIriRef(IM.HAS_STATUS);
    }

    public TTEntity setStatus(TTIriRef status) {
        set(IM.HAS_STATUS, status);
        return this;
    }

    public TTEntity setContext(TTContext context) {
        this.context = context;
        return this;
    }

    public List<TTPrefix> getPrefixes() {
        return context.getPrefixes();
    }

    public TTEntity addPrefix(String iri, String prefix) {
        context.add(iri, prefix);
        return this;
    }

    @Override
    public TTEntity set(TTIriRef predicate, TTValue value) {
        super.set(predicate, value);
        return this;
    }

    @Override
    public TTEntity set(TTIriRef predicate, TTArray value) {
        super.set(predicate, value);
        return this;
    }

    public TTContext getContext() {
        return context;
    }
    public TTIriRef getCrud() {
        return crud;
    }

    public TTEntity setCrud(TTIriRef crud) {
        this.crud = crud;
        return this;
    }

    public TTIriRef getGraph() {
        return graph;
    }

    public TTEntity setGraph(TTIriRef graph) {
        this.graph = graph;
        return this;
    }
}
