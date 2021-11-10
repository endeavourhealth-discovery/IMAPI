package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public interface EntityTctRepository {
    List<TTIriRef> findAncestorsByType(String iri, String type, List<String> candidates);
}
