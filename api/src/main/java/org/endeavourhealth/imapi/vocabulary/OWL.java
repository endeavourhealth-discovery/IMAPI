package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum OWL implements Vocabulary {
    NAMESPACE("http://www.w3.org/2002/07/owl#"),
    PREFIX("owl"),
    THING(NAMESPACE.iri +"Thing"),
    CLASS(NAMESPACE.iri +"Class"),
    EQUIVALENT_CLASS(NAMESPACE.iri + "equivalentClass"),
    INTERSECTION_OF(NAMESPACE.iri + "intersectionOf"),
    UNION_OF(NAMESPACE.iri + "unionOf"),
    RESTRICTION(NAMESPACE.iri + "Restriction"),
    ON_PROPERTY(NAMESPACE.iri + "onProperty"),
    ON_CLASS(NAMESPACE.iri + "onClass"),
    SOME_VALUES_FROM(NAMESPACE.iri + "someValuesFrom"),
    ALL_VALUES_FROM(NAMESPACE.iri + "allValuesFrom"),
    OBJECT_PROPERTY(NAMESPACE.iri + "ObjectProperty"),
    DATATYPE_PROPERTY(NAMESPACE.iri + "DatatypeProperty"),
    ANNOTATION_PROPERTY(NAMESPACE.iri + "AnnotationProperty"),
    INVERSE_OF(NAMESPACE.iri + "inverseOf"),
    INVERSE_OBJECT_PROPERTY(NAMESPACE.iri + "inverseOf"),
    PROPERTY_CHAIN(NAMESPACE.iri + "propertyChainAxiom"),
    TRANSITIVE(NAMESPACE.iri + "TransitiveProperty"),
    FUNCTIONAL(NAMESPACE.iri + "FunctionalProperty"),
    SYMMETRIC(NAMESPACE.iri + "SymmetricProperty"),
    REFLEXIVE(NAMESPACE.iri + "ReflexiveProperty"),
    ON_DATATYPE(NAMESPACE.iri + "onDatatype"),
    WITH_RESTRICTIONS(NAMESPACE.iri + "withRestrictions"),
    MAX_CARDINALITY(NAMESPACE.iri + "maxCardinality"),
    MIN_CARDINALITY(NAMESPACE.iri + "minCardinality"),
    ON_DATA_RANGE(NAMESPACE.iri + "onDataRange"),
    HAS_VALUE(NAMESPACE.iri + "hasValue"),
    COMPLEMENT_OF(NAMESPACE.iri + "complementOf"),
    ONE_OF(NAMESPACE.iri + "oneOf"),
    NAMED_INDIVIDUAL(NAMESPACE.iri + "NamedIndividual");

    public final String iri;
    OWL(String iri) {
        this.iri = iri;
    }

    @Override
    public TTIriRef asTTIriRef() {
        return iri(this.iri);
    }

    @Override
    @JsonValue
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

    @Override
    public String toString() {
        return iri;
    }
}
