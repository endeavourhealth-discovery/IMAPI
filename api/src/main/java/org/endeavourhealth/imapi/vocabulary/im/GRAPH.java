package org.endeavourhealth.imapi.vocabulary.im;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum GRAPH implements Vocabulary {
    //Graphs
    DISCOVERY(IM.NAMESPACE.iri),
    ICD10(IM.DOMAIN.iri + "icd10#"),
    EMIS(IM.DOMAIN.iri + "emis#"),
    EMIS_CORE(IM.DOMAIN.iri + "emisc"),
    CPRD_MED(IM.DOMAIN.iri + "cprdm#"),
    CPRD_PROD(IM.DOMAIN.iri + "cprdp#"),
    OPCS4(IM.DOMAIN.iri + "opcs4#"),
    TPP(IM.DOMAIN.iri + "tpp#"),
    ODS(IM.DOMAIN.iri + "ods#"),
    PRSB(IM.DOMAIN.iri + "prsb#"),
    KINGS_APEX(IM.DOMAIN.iri + "kpax#"),
    KINGS_WINPATH(IM.DOMAIN.iri + "kwp#"),
    VISION(IM.DOMAIN.iri + "vis#"),
    READ2(IM.DOMAIN.iri + "read2#"),
    BARTS_CERNER(IM.DOMAIN.iri + "bc#"),
    NHSDD_ETHNIC_2001(IM.DOMAIN.iri + "nhsethnic2001#"),
    IM1(IM.DOMAIN.iri + "im1#"),
    ENCOUNTERS(IM.DOMAIN.iri + "enc#"),
    CONFIG(IM.DOMAIN.iri + "config#"),
    CEG_QUERY(IM.DOMAIN.iri + "ceg/qry#"),
    NHS_TFC(IM.DOMAIN.iri + "nhstfc#"),
    STATS(IM.DOMAIN.iri + "stats#"),
    DELTAS(IM.DOMAIN.iri + "deltas#"),
    PROV(IM.DOMAIN.iri + "prov#"),
    QUERY(IM.DOMAIN.iri + "query#"),
    CEG16(IM.DOMAIN.iri + "ceg16#"),
    REPORTS(IM.DOMAIN.iri + "reports#"),
    OPS_ROLES("https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-OrganizationRole-1#");

    public final String iri;

    GRAPH(String url) {
        this.iri = url;
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
            GRAPH.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
