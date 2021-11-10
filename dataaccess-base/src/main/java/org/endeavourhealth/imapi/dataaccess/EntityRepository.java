package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public interface EntityRepository {
    TTIriRef getEntityReferenceByIri(String iri);

    SearchResultSummary getEntitySummaryByIri(String iri);

    String findCodeByDbid(Integer dbid);

    String isCoreCodeSchemeIncludedInVSet(String code, String scheme, String vSet);

    String isLegacyCodeSchemeIncludedInVSet(String code, String scheme, String vSet);

    String isCoreCodeSchemeExcludedInVSet(String code, String scheme, String vSet);

    String isLegacyCodeSchemeExcludedInVSet(String code, String scheme, String vSet);
}
