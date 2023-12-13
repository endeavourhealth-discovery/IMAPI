package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum OWL implements Vocabulary {
    NAMESPACE("http://www.w3.org/2002/07/owl#"),
    PREFIX("owl"),
    THING(NAMESPACE.iri +"Thing"),
    CLASS(NAMESPACE.iri +"Class"),
    EQUIVALENTCLASS(NAMESPACE.iri + "equivalentClass"),
    INTERSECTIONOF(NAMESPACE.iri + "intersectionOf"),
    UNIONOF(NAMESPACE.iri + "unionOf"),
    RESTRICTION(NAMESPACE.iri + "Restriction"),
    ONPROPERTY(NAMESPACE.iri + "onProperty"),
    ONCLASS(NAMESPACE.iri + "onClass"),
    SOMEVALUESFROM(NAMESPACE.iri + "someValuesFrom"),
    ALLVALUESFROM(NAMESPACE.iri + "allValuesFrom"),
    OBJECTPROPERTY(NAMESPACE.iri + "ObjectProperty"),
    DATATYPEPROPERTY(NAMESPACE.iri + "DatatypeProperty"),
    ANNOTATIONPROPERTY(NAMESPACE.iri + "AnnotationProperty"),
    INVERSEOF(NAMESPACE.iri + "inverseOf"),
    INVERSEOBJECTPROPERTY(NAMESPACE.iri + "inverseOf"),
    PROPERTYCHAIN(NAMESPACE.iri + "propertyChainAxiom"),
    TRANSITIVE(NAMESPACE.iri + "TransitiveProperty"),
    FUNCTIONAL(NAMESPACE.iri + "FunctionalProperty"),
    SYMMETRIC(NAMESPACE.iri + "SymmetricProperty"),
    REFLEXIVE(NAMESPACE.iri + "ReflexiveProperty"),
    ONDATATYPE(NAMESPACE.iri + "onDatatype"),
    WITHRESTRICTIONS(NAMESPACE.iri + "withRestrictions"),
    MAXCARDINALITY(NAMESPACE.iri + "maxCardinality"),
    MINCARDINALITY(NAMESPACE.iri + "minCardinality"),
    ONDATARANGE(NAMESPACE.iri + "onDataRange"),
    HASVALUE(NAMESPACE.iri + "hasValue"),
    COMPLEMENTOF(NAMESPACE.iri + "complementOf"),
    ONEOF(NAMESPACE.iri + "oneOf"),
    NAMEDINDIVIDUAL(NAMESPACE.iri + "NamedIndividual");

    public final String iri;
    OWL(String iri) {
        this.iri = iri;
    }

    @Override
    public TTIriRef asTTIriRef() {
        return iri(this.iri);
    }

    @Override
    public String getIri() {
        return iri;
    }

    public static boolean contains(String iri) {
        try {
            OWL.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
