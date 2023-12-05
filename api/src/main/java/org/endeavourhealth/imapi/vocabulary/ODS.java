package org.endeavourhealth.imapi.vocabulary;

public class ODS {
    private static final String BASE_NAMESPACE = "https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-";

    public static final String ORGANISATION_ROLE_TYPE = BASE_NAMESPACE + "OrganizationRole-1#ODS_RoleType";
    public static final String ORGANISATION_RELATIONSHIP = BASE_NAMESPACE + "OrganizationRelationship-1#ODS_Relationship";
    public static final String ORGANISATION_RECORD_CLASS = BASE_NAMESPACE + "OrganizationRecordClass-1#ODS_RecordClass";

    private ODS() {
    }
}
