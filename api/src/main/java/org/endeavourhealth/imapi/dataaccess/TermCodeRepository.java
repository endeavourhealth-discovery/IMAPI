package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.TermCode;

import java.util.ArrayList;
import java.util.List;

public class TermCodeRepository {

    public List<TermCode> findAllByIri(String iri) {
        // TODO: Term code implementation in graph?
        return new ArrayList<>();
    }
}
