package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class XSD {
    public static final String NAMESPACE = "http://www.w3.org/2001/XMLSchema#";
    public static final String PREFIX = "xsd";
    public static final TTIriRef PATTERN= iri(NAMESPACE +"pattern");
    public static final TTIriRef MININCLUSIVE= iri(NAMESPACE +"minInclusive");
    public static final TTIriRef MINEXCLUSIVE= iri(NAMESPACE +"minExclusive");
    public static final TTIriRef MAXINCLUSIVE= iri(NAMESPACE +"maxInclusive");
    public static final TTIriRef MAXEXCLUSIVE= iri(NAMESPACE +"maxExclusive");
    public static final TTIriRef INTEGER= iri(NAMESPACE +"integer");
    public static final TTIriRef STRING= iri(NAMESPACE +"string");
    public static final TTIriRef BOOLEAN= iri(NAMESPACE +"boolean");
    public static final TTIriRef LONG= iri(NAMESPACE +"long");
}
