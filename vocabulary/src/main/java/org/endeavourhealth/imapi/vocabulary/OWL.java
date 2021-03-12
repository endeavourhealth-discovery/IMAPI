package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class OWL {
    public static final String NAMESPACE = "http://www.w3.org/2002/07/owl#";
    public static final String PREFIX = "owl";
    public static final TTIriRef CLASS= iri(NAMESPACE +"Class");
    public static final TTIriRef EQUIVALENTCLASS = iri(NAMESPACE + "equivalentClass");
    public static final TTIriRef INTERSECTIONOF = iri(NAMESPACE + "intersectionOf");
    public static final TTIriRef UNIONOF = iri(NAMESPACE + "unionOf");
    public static final TTIriRef RESTRICTION = iri(NAMESPACE + "Restriction");
    public static final TTIriRef ONPROPERTY = iri(NAMESPACE + "onProperty");
    public static final TTIriRef ONCLASS = iri(NAMESPACE + "onClass");
    public static final TTIriRef SOMEVALUESFROM = iri(NAMESPACE + "someValuesFrom");
    public static final TTIriRef ALLVALUESFROM = iri(NAMESPACE + "allValuesFrom");
    public static final TTIriRef OBJECTPROPERTY = iri(NAMESPACE + "ObjectProperty");
    public static final TTIriRef DATAPROPERTY = iri(NAMESPACE + "DataProperty");
    public static final TTIriRef ANNOTATIONPROPERTY = iri(NAMESPACE + "AnnotationProperty");
    public static final TTIriRef INVERSEOF = iri(NAMESPACE + "inverseOf");
    public static final TTIriRef INVERSEOBJECTPROPERTY = iri(NAMESPACE + "inverseOf");
    public static final TTIriRef PROPERTYCHAIN = iri(NAMESPACE + "propertyChainAxiom");
    public static final TTIriRef TRANSITIVE = iri(NAMESPACE + "TransitiveProperty");
    public static final TTIriRef FUNCTIONAL = iri(NAMESPACE + "FunctionalProperty");
    public static final TTIriRef SYMMETRIC = iri(NAMESPACE + "SymmetricProperty");
    public static final TTIriRef REFLEXIVE = iri(NAMESPACE + "ReflexiveProperty");
    public static final TTIriRef ONDATATYPE = iri(NAMESPACE + "onDatatype");
    public static final TTIriRef WITHRESTRICTIONS = iri(NAMESPACE + "withRestrictions");
    public static final TTIriRef MAXCARDINALITY = iri(NAMESPACE + "maxQualifiedCardinality");
    public static final TTIriRef MINCARDINALITY = iri(NAMESPACE + "minQualifiedCardinality");
    public static final TTIriRef ONDATARANGE = iri(NAMESPACE + "onDataRange");
    public static final TTIriRef HASVALUE = iri(NAMESPACE + "hasValue");
    public static final TTIriRef COMPLEMENTOF = iri(NAMESPACE + "complementOf");
    public static final TTIriRef ONEOF = iri(NAMESPACE + "oneOf");



}
