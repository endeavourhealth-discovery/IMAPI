package org.endeavourhealth.dataaccess.graph.tripletree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;

import java.util.ArrayList;
import java.util.List;

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
    public TTConcept set(IRI predicate, IRI iri) {
        return set(predicate, new TTIriRef(iri));
    }

    @Override
    public TTConcept set(IRI predicate, IRI iri, String name) {
        return set(predicate, new TTIriRef(iri, name));
    }

    @Override
    public TTConcept set(IRI predicate, Literal literal) {
        return set(predicate, new TTLiteral(literal));
    }

    @Override
    public TTConcept set(IRI predicate, TTValue value) {
        super.set(predicate, value);
        return this;
    }
}
