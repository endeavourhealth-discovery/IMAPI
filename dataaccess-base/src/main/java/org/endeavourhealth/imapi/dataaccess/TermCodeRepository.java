package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.TermCode;

import java.util.List;

public interface TermCodeRepository {
    List<TermCode> findAllByIri(String iri);
}
