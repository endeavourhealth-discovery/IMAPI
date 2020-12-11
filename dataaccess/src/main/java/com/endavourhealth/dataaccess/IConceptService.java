package com.endavourhealth.dataaccess;

import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponseConcept;

import java.util.List;

public interface IConceptService {
    Concept getConcept(String iri);
    ConceptReference getConceptReference(String iri);
    List<ConceptReference> findByNameLike(String term, String root, Boolean includeLegacy, Integer limit);
    List<ConceptReference> getImmediateChildren(String iri, Integer page, Integer size, Boolean includeLegacy);
    List<ConceptReferenceNode> getParentHierarchy(String iri);

    List<ConceptReference> isWhichType(String iri, List<String> candidates);

    ConceptReference create(Concept concept);

    List<ConceptReference> usages(String iri);

    List<SearchResponseConcept> advancedSearch(SearchRequest request);
}
