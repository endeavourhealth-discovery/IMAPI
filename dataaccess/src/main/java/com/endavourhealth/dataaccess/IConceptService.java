package com.endavourhealth.dataaccess;

import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;

import java.util.List;
import java.util.Set;

public interface IConceptService {
    Concept getConcept(String iri);
    ConceptReference getConceptReference(String iri);
    Set<ConceptReference> findByNameLike(String term, String root);
    Set<ConceptReference> findByNameLike(String term, String root, Boolean includeLegacy);
    Set<ConceptReference> getImmediateChildren(String iri, Integer page, Integer size, Boolean includeLegacy);
    Set<ConceptReferenceNode> getParentHierarchy(String iri);

    Set<ConceptReference> isWhichType(String iri, List<String> candidates);

    ConceptReference create(Concept concept);
}
