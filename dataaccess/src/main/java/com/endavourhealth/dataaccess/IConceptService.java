package com.endavourhealth.dataaccess;

import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;

import java.util.Set;

public interface IConceptService {
    Concept getConcept(String iri);
    ConceptReference getConceptReference(String iri);
    Set<ConceptReference> findByNameLike(String term, String root);
    Set<ConceptReference> findByNameLike(String term, String root, Boolean includeLegacy);
    Set<ConceptReference> getImmediateChildren(String iri);
    Set<ConceptReferenceNode> getParentHierarchy(String iri);

    ConceptReference create(Concept concept);
}
