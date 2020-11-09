package com.endavourhealth.dataaccess;

import org.endeavourhealth.informationmanager.model.Concept;
import org.endeavourhealth.informationmanager.model.ConceptReferenceNode;
import org.endeavourhealth.informationmanager.model.ConceptReference;

import java.util.Set;

public interface IConceptService {
    Concept getConcept(String iri);
    ConceptReference getConceptReference(String iri);
    Set<ConceptReference> findByNameLike(String term, String root);
    Set<ConceptReference> getImmediateChildren(String iri);
    Set<ConceptReferenceNode> getParentHierarchy(String iri);

    ConceptReference create(Concept concept);
}
