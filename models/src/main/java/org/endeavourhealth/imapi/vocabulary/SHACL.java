package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SHACL {
   public static final String NAMESPACE = "http://www.w3.org/ns/shacl#";
   public static final String PREFIX = "sh";
   public static final TTIriRef MININCLUSIVE= iri(NAMESPACE +"minInclusive");
   public static final TTIriRef MINEXCLUSIVE= iri(NAMESPACE +"minExclusive");
   public static final TTIriRef MAXINCLUSIVE= iri(NAMESPACE +"maxInclusive");
   public static final TTIriRef MAXEXCLUSIVE= iri(NAMESPACE +"maxExclusive");
   public static final TTIriRef PROPERTY= iri(NAMESPACE +"property");
   public static final TTIriRef MINCOUNT= iri(NAMESPACE +"minCount");
   public static final TTIriRef MAXCOUNT= iri(NAMESPACE +"maxCount");
   public static final TTIriRef HASVALUE= iri(NAMESPACE +"hasValue");
   public static final TTIriRef PATTERN= iri(NAMESPACE +"pattern");
   public static final TTIriRef PATH= iri(NAMESPACE +"path");
   public static final TTIriRef INVERSEPATH= iri(NAMESPACE +"inversePath");
   public static final TTIriRef CLASS= iri(NAMESPACE +"class");
   public static final TTIriRef DATATYPE= iri(NAMESPACE +"datatype");
}

