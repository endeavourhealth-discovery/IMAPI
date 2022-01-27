package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public interface EntityRepository {
    TTIriRef getEntityReferenceByIri(String iri);

    SearchResultSummary getEntitySummaryByIri(String iri);

}
