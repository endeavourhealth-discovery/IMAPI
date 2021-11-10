package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public interface TermCodeRepository {
    List<TermCode> findAllByIri(String iri);

    TTIriRef findByCodeAndScheme(String code, String scheme);

    Integer findDbidByCodeAndScheme(String code, String scheme);
}
