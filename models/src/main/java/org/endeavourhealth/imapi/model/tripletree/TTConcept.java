package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

@JsonSerialize(using = TTConceptSerializer.class)
@JsonDeserialize(using = TTConceptDeserializer.class)
public class TTConcept extends TTNode {
    private String iri;
    private List<TTPrefix> prefixes = new ArrayList<>();

    public TTConcept() {}

    public TTConcept(String iri) {
        this.iri = iri;
    }

    public String getIri() {
        return iri;
    }

    public TTConcept setIri(String iri) {
        this.iri = iri;
        return this;
    }

    // Utility methods for common predicates
    public TTConcept setName (String name) {
        set(RDFS.LABEL, literal(name));
        return this;
    }

    public String getName() {
        TTLiteral literal = getAsLiteral(RDFS.LABEL);
        return (literal == null) ? null : literal.getValue();
    }

    public TTConcept setDescription (String description) {
        set(RDFS.COMMENT, literal(description));
        return this;
    }

    public String getDescription() {
        TTLiteral literal = getAsLiteral(RDFS.COMMENT);
        return (literal == null) ? null : literal.getValue();
    }

    public TTConcept setCode(String code) {
        set(IM.CODE, literal(code));
        return this;
    }

    public String getCode() {
        TTLiteral literal = getAsLiteral(IM.CODE);
        return (literal == null) ? null : literal.getValue();
    }

    public TTConcept setScheme(TTIriRef scheme) {
        set(IM.HAS_SCHEME, scheme);
        return this;
    }

    public TTIriRef getScheme() {
        return this.getAsIriRef(IM.HAS_SCHEME);
    }

    public TTConcept setType(TTArray type) {
        set(RDF.TYPE, type);
        return this;
    }

    public TTConcept addType(TTIriRef type) {
        TTArray types = getAsArray(RDF.TYPE);
        if (types == null) {
            types = new TTArray();
            setType(types);
        }
        types.add(type);
        return this;
    }

    public TTArray getType() {
        return getAsArray(RDF.TYPE);
    }

    public List<TTPrefix> getPrefixes() {
        return prefixes;
    }

    public TTConcept setPrefixes(List<TTPrefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    public TTConcept addPrefix(TTPrefix prefix) {
        if (prefix != null) {
            if (prefixes == null) {
                prefixes = new ArrayList<>();
            }
            prefixes.add(prefix);
        }
        return this;
    }

    public TTConcept addPrefix(String iri, String prefix) {
        return addPrefix(new TTPrefix(iri, prefix));
    }

    @Override
    public TTConcept set(TTIriRef predicate, TTValue value) {
        super.set(predicate, value);
        return this;
    }
}
