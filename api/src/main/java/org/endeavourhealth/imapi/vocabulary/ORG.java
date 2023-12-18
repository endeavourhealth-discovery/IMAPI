package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum ORG implements Vocabulary {
    ORGANISATION_NAMESPACE("http://org.endhealth.info/im#"),
    LOCATION_NAMESPACE("http://loc.endhealth.info/im#"),
    ORGANISATION_RECORD_CLASS(IM.NAMESPACE.iri + "organisationRecordClass"),
    RELATED_ORGANISATION(IM.NAMESPACE.iri + "organisationRelationship"),
    TARGET(IM.NAMESPACE.iri + "targetOrganisation"),
    ROLE(IM.NAMESPACE.iri + "organisationRole"),
    ODS_CODE(IM.NAMESPACE.iri + "odsCode");

    public final String iri;
    ORG(String iri) {
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
            ORG.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

