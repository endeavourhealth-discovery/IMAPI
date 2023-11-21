package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class ORG {
    public static final String ORGANISATION_NAMESPACE = "http://org.endhealth.info/im#";
    public static final String LOCATION_NAMESPACE = "http://loc.endhealth.info/im#";
    public static final TTIriRef ORGANISATION_RECORD_CLASS = iri(IM.NAMESPACE + "organisationRecordClass");
    public static final TTIriRef RELATED_ORGANISATION = iri(IM.NAMESPACE + "organisationRelationship");
    public static final TTIriRef TARGET = iri(IM.NAMESPACE + "targetOrganisation");
    public static final TTIriRef ROLE = iri(IM.NAMESPACE + "organisationRole");
    public static final TTIriRef ODS_CODE = iri(IM.NAMESPACE + "odsCode");

    private ORG() {
    }
}

