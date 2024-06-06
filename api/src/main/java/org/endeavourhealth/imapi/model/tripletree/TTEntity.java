package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTEntityDeserializer;
import org.endeavourhealth.imapi.json.TTEntitySerializer;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

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
        set(iri(RDFS.LABEL), TTLiteral.literal(name));
        return this;
    }

    public String getName() {
        TTLiteral literal = getAsLiteral(iri(RDFS.LABEL));
        return (literal == null) ? null : literal.getValue();
    }

    public TTEntity setVersion (int version) {
        set(iri(IM.VERSION), TTLiteral.literal(version));
        return this;
    }

    public int getVersion() {
        TTLiteral literal = getAsLiteral(iri(IM.VERSION));
        return (literal == null) ? 1 : literal.intValue();
    }

    public TTEntity setDescription (String description) {
        if (description==null)
            getPredicateMap().remove(iri(RDFS.COMMENT));
        else
            set(iri(RDFS.COMMENT), TTLiteral.literal(description));
        return this;
    }

    public String getDescription() {
        TTLiteral literal = getAsLiteral(iri(RDFS.COMMENT));
        return (literal == null) ? null : literal.getValue();
    }

    public TTEntity setCode(String code) {
        set(iri(IM.CODE), TTLiteral.literal(code));
        return this;
    }

    public String getCode() {
        TTLiteral literal = getAsLiteral(iri(IM.CODE));
        return (literal == null) ? null : literal.getValue();
    }

    @JsonSetter
    public TTEntity setScheme(TTIriRef scheme) {
        set(iri(IM.HAS_SCHEME), scheme);
        return this;
    }

    public TTIriRef getScheme() {
        return this.getAsIriRef(iri(IM.HAS_SCHEME));
    }

    public TTEntity setType(TTArray type) {
        set(iri(RDF.TYPE), type);
        return this;
    }

    public TTEntity addType(TTIriRef type) {
        TTArray types;
        if (has(iri(RDF.TYPE))) {
            types = get(iri(RDF.TYPE));
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
        if (get(iri(RDF.TYPE))==null)
            return null;
        else
         return get(iri(RDF.TYPE));
    }

    public TTIriRef getStatus(){
        return this.getAsIriRef(iri(IM.HAS_STATUS));
    }

    @JsonSetter
    public TTEntity setStatus(TTIriRef status) {
        set(iri(IM.HAS_STATUS), status);
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

    @Override
    public TTEntity set(TTIriRef predicate, Integer value) {
        super.set(predicate, value);
        return this;
    }

    @Override
    public TTEntity set(TTIriRef predicate, Long value) {
        super.set(predicate, value);
        return this;
    }

    @Override
    public TTEntity set(TTIriRef predicate, boolean value) {
        super.set(predicate, value);
        return this;
    }

    @Override
    public TTEntity addObject(TTIriRef predicate, TTValue object) {
        super.set(predicate, object);
        return this;
    }

    public TTContext getContext() {
        return context;
    }

    public TTIriRef getCrud() {
        return crud;
    }

    @JsonSetter
    public TTEntity setCrud(TTIriRef crud) {
        this.crud = crud;
        return this;
    }

    public TTIriRef getGraph() {
        return graph;
    }

    @JsonSetter
    public TTEntity setGraph(TTIriRef graph) {
        this.graph = graph;
        return this;
    }
}
