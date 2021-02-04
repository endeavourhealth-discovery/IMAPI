package org.endeavourhealth.dataaccess;

import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponseConcept;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;

import java.util.List;

public interface IConceptService {
    Concept getConcept(String iri);
    ConceptReference getConceptReference(String iri);
    List<ConceptReference> findByNameLike(String term, String root, boolean includeLegacy, Integer limit);
    Boolean getHasChildren(String iri, boolean includeLegacy);
    List<String> getHaveChildren(List<String> iris, boolean includeLegacy);
    List<ConceptReferenceNode> getImmediateChildren(String iri, Integer page, Integer size, boolean includeLegacy);
    List<ConceptReferenceNode> getImmediateParents(String iri, Integer page, Integer size, boolean includeLegacy);
    List<ConceptReferenceNode> getParentHierarchy(String iri);

    List<ConceptReference> isWhichType(String iri, List<String> candidates);

    ConceptReference create(Concept concept);

    List<ConceptReference> usages(String iri);

    List<SearchResponseConcept> advancedSearch(SearchRequest request);

    List<Concept> getAncestorDefinitions(String iri);

    ExportValueSet getValueSetMembers(String iri, boolean expand);

    List<ConceptReference> getCoreMappedFromLegacy(String legacyIri);
    List<ConceptReference> getLegacyMappedToCore(String coreIri);

}
