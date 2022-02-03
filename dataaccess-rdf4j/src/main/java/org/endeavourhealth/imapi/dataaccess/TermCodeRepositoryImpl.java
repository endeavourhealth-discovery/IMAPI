package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.TermCode;

import java.util.ArrayList;
import java.util.List;

public class TermCodeRepositoryImpl {

    public List<TermCode> findAllByIri(String iri) {
        List<TermCode> terms = new ArrayList<>();

        // TODO: Term code implementation in graph?

        return terms;
    }
}
