package org.endeavourhealth.dataaccess;

import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;

import java.util.List;

public interface IConceptService {
    Concept getConcept(String iri);
    ConceptReference getConceptReference(String iri);
    List<ConceptReferenceNode> getImmediateChildren(String iri, Integer page, Integer size, boolean includeLegacy);
    List<ConceptReferenceNode> getImmediateParents(String iri, Integer page, Integer size, boolean includeLegacy);
    List<ConceptReferenceNode> getParentHierarchy(String iri);

    List<ConceptReference> isWhichType(String iri, List<String> candidates);

    List<ConceptSummary> usages(String iri);

    List<ConceptSummary> advancedSearch(SearchRequest request);

    List<Concept> getAncestorDefinitions(String iri);

    ExportValueSet getValueSetMembers(String iri, boolean expand);

    ValueSetMembership isValuesetMember(String valueSetIri, String memberIri);

    List<ConceptReference> getCoreMappedFromLegacy(String legacyIri);
    List<ConceptReference> getLegacyMappedToCore(String coreIri);

}
