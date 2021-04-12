package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SNOMED {
    public static final String NAMESPACE = "http://snomed.info/sct#";
    public static final String PREFIX = "sn";
    public static final TTIriRef GRAPH= iri("http://snomed.info/sct");
}
