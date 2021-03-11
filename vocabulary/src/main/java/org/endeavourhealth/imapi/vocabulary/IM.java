package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IM {
    public static final String NAMESPACE = "http://endhealth.info/im#";
    public static final String PREFIX = "im";
    public static final TTIriRef MODULE = iri(NAMESPACE + "module");
    public static final TTIriRef ONTOLOGY = iri(NAMESPACE + "ontology");
    public static final TTIriRef CODE = iri(NAMESPACE + "code");
    public static final TTIriRef HAS_SCHEME = iri(NAMESPACE + "scheme");
    public static final TTIriRef STATUS = iri(NAMESPACE + "status");
    public static final TTIriRef SYNONYM = iri(NAMESPACE + "synonym");
    public static final TTIriRef RECORD = iri(NAMESPACE + "Record");
    public static final TTIriRef VALUESET = iri(NAMESPACE + "ValueSet");
    public static final TTIriRef HAS_MEMBERS = iri(NAMESPACE + "hasMembers");
    public static final TTIriRef HAS_EXPANSION = iri(NAMESPACE + "hasExpansion");
    public static final TTIriRef IS_CONTAINED_IN = iri(NAMESPACE +"isContainedIn");
    public static final TTIriRef LEGACY = iri(NAMESPACE +"Legacy");
    public static final TTIriRef IS_A= iri(NAMESPACE +"isA");
    public static final TTIriRef ROLE_GROUP= iri(NAMESPACE +"roleGroup");
    public static final TTIriRef IN_ROLE_GROUP_OF= iri(NAMESPACE +"inrRoleGroupOf");
    public static final TTIriRef IS_INSTANCE_OF = iri(NAMESPACE +"isInstanceOf");
    public static final TTIriRef DM_OP = iri(NAMESPACE +"modelObjectProperty");
    public static final TTIriRef DM_DP = iri(NAMESPACE +"modelDataProperty");


}
