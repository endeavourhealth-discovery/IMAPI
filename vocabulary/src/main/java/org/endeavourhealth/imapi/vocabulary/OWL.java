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
    public static final TTIriRef RESTRICTION = iri(NAMESPACE + "Restriction");
    public static final TTIriRef ONPROPERTY = iri(NAMESPACE + "onProperty");
    public static final TTIriRef SOMEVALUESFROM = iri(NAMESPACE + "someValuesFrom");
}
