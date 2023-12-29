package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTEntityDeserializer;
import org.endeavourhealth.imapi.json.TTEntitySerializer;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

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
        set(RDFS.LABEL.asTTIriRef(), TTLiteral.literal(name));
        return this;
    }

    public String getName() {
        TTLiteral literal = getAsLiteral(RDFS.LABEL.asTTIriRef());
        return (literal == null) ? null : literal.getValue();
    }

    public TTEntity setVersion (int version) {
        set(IM.VERSION.asTTIriRef(), TTLiteral.literal(version));
        return this;
    }

    public int getVersion() {
        TTLiteral literal = getAsLiteral(IM.VERSION.asTTIriRef());
        return (literal == null) ? 1 : literal.intValue();
    }

    public TTEntity setDescription (String description) {
        if (description==null)
            getPredicateMap().remove(RDFS.COMMENT.asTTIriRef());
        else
            set(RDFS.COMMENT.asTTIriRef(), TTLiteral.literal(description));
        return this;
    }

    public String getDescription() {
        TTLiteral literal = getAsLiteral(RDFS.COMMENT.asTTIriRef());
        return (literal == null) ? null : literal.getValue();
    }

    public TTEntity setCode(String code) {
        set(IM.CODE.asTTIriRef(), TTLiteral.literal(code));
        return this;
    }

    public String getCode() {
        TTLiteral literal = getAsLiteral(IM.CODE.asTTIriRef());
        return (literal == null) ? null : literal.getValue();
    }

    @JsonSetter
    public TTEntity setScheme(TTIriRef scheme) {
        set(IM.HAS_SCHEME.asTTIriRef(), scheme);
        return this;
    }

    public TTEntity setScheme(Vocabulary scheme) {
        return setScheme(scheme.asTTIriRef());
    }

    public TTIriRef getScheme() {
        return this.getAsIriRef(IM.HAS_SCHEME.asTTIriRef());
    }

    public TTEntity setType(TTArray type) {
        set(RDF.TYPE.asTTIriRef(), type);
        return this;
    }

    public TTEntity addType(TTIriRef type) {
        TTArray types;
        if (has(RDF.TYPE.asTTIriRef())) {
            types = get(RDF.TYPE.asTTIriRef());
        } else {
            types = new TTArray();
            setType(types);
        }
        types.add(type);
        return this;
    }

    public TTEntity addType(Vocabulary type) {
        return addType(type.asTTIriRef());
    }
    public boolean isType(TTIriRef type){
        if (this.getType()!=null){
            return this.getType().contains(type);
        }
        return false;
    }

    public boolean isType(Vocabulary type) {
        return isType(type.asTTIriRef());
    }

    public TTArray getType() {
        if (get(RDF.TYPE.asTTIriRef())==null)
            return null;
        else
         return get(RDF.TYPE.asTTIriRef());
    }

    public TTIriRef getStatus(){
        return this.getAsIriRef(IM.HAS_STATUS.asTTIriRef());
    }

    @JsonSetter
    public TTEntity setStatus(TTIriRef status) {
        set(IM.HAS_STATUS.asTTIriRef(), status);
        return this;
    }

    public TTEntity setStatus(Vocabulary status) {
        return setStatus(status.asTTIriRef());
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
    public TTEntity set(Vocabulary predicate, TTValue value) {
        return set(predicate.asTTIriRef(),value);
    }

    @Override
    public TTEntity set(Vocabulary predicate, TTArray value) {
        return set(predicate.asTTIriRef(),value);
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

    public TTEntity setCrud(Vocabulary crud) {
        return setCrud(crud.asTTIriRef());
    }

    public TTIriRef getGraph() {
        return graph;
    }

    @JsonSetter
    public TTEntity setGraph(TTIriRef graph) {
        this.graph = graph;
        return this;
    }

    public TTEntity setGraph(Vocabulary graph) {
        return setGraph(graph.asTTIriRef());
    }
}
