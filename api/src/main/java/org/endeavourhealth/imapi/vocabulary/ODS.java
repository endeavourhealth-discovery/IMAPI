package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum ODS implements Vocabulary {
    BASE_NAMESPACE("https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-"),

    ORGANISATION_ROLE_TYPE(BASE_NAMESPACE.iri + "OrganizationRole-1#ODS_RoleType"),
    ORGANISATION_RELATIONSHIP(BASE_NAMESPACE.iri + "OrganizationRelationship-1#ODS_Relationship"),
    ORGANISATION_RECORD_CLASS(BASE_NAMESPACE.iri + "OrganizationRecordClass-1#ODS_RecordClass");

    public final String iri;
    ODS(String iri) {
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
            ODS.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

