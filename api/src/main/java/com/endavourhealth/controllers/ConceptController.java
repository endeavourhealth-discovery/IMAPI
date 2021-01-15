package com.endavourhealth.controllers;

import java.util.List;

import com.endavourhealth.dataaccess.IConceptService;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.valuset.ValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/concept")
@CrossOrigin
public class ConceptController {

    @Autowired
    @Qualifier("ConceptServiceV3")
    IConceptService conceptService;

    @GetMapping(value = "/")
    public List<ConceptReference> search(@RequestParam(name = "nameTerm") String nameTerm,
                                        @RequestParam(name = "root", required = false) String root,
                                        @RequestParam(name = "includeLegacy", required = false) boolean includeLegacy,
                                        @RequestParam(name = "limit", required = false) Integer limit) {
        return conceptService.findByNameLike(nameTerm, root, includeLegacy, limit);
    }

    @PostMapping(value = "/search")
    public SearchResponse advancedSearch(@RequestBody SearchRequest request) {
        return new SearchResponse()
            .setConcepts(
                conceptService.advancedSearch(request)
            );
    }

    @GetMapping(value = "/{iri}")
    public Concept getConcept(@PathVariable("iri") String iri) {
        return conceptService.getConcept(iri);
    }

    @GetMapping(value = "/{iri}/reference")
    public ConceptReference getConceptReference(@PathVariable("iri") String iri) {
        return conceptService.getConceptReference(iri);
    }

    @GetMapping(value = "/{iri}/parents")
    public List<ConceptReferenceNode> getConceptParents(@PathVariable("iri") String iri) {
    	List<ConceptReferenceNode> parents = conceptService.getParentHierarchy(iri);
    	
    	return parents;
    }

    @GetMapping(value = "/{iri}/parents/definitions")
    public List<Concept> getConceptAncestorDefinitions(@PathVariable("iri") String iri) {
        return conceptService.getAncestorDefinitions(iri);
    }

    @GetMapping(value = "/{iri}/children")
    public List<ConceptReferenceNode> getConceptChildren(@PathVariable("iri") String iri,
                                                    @RequestParam(name = "page", required = false) Integer page,
                                                    @RequestParam(name = "size", required = false) Integer size,
                                                    @RequestParam(name = "includeLegacy", required = false) boolean includeLegacy
    ) {
        return conceptService.getImmediateChildren(iri, page, size, includeLegacy);
    }

    @GetMapping(value = "/{iri}/usages")
    public List<ConceptReference> conceptUsages(@PathVariable("iri") String iri) {
        return conceptService.usages(iri);
    }

    @PostMapping(value = "/{iri}/isWhichType")
    public List<ConceptReference> conceptIsWhichType(@PathVariable("iri") String iri,
                                                    @RequestBody List<String> candidates) {
        return conceptService.isWhichType(iri, candidates);
    }

    @GetMapping(value = "/{iri}/members")
    public ValueSet valueSetMembersJson(@PathVariable("iri") String iri, @RequestParam(name="expanded", required = false) boolean expanded) {
        return conceptService.getValueSetMembers(iri, expanded);
    }

    @GetMapping(value = "/{iri}/members", produces = {"text/csv"})
    public String valueSetMembersCSV(@PathVariable("iri") String iri, @RequestParam(name="expanded", required = false) boolean expanded) {
        ValueSet valueSet = conceptService.getValueSetMembers(iri, expanded);

        StringBuilder sb = new StringBuilder();

        sb.append("Inc\\Exc\tValueSetIri\tValueSetName\tRelationshipIri\tRelationshipName\tMemberIri\tMemberTerm\tMemberCode\tMemberSchemeIri\tMemberSchemeName\n");

        for(ValueSetMember c : valueSet.getIncluded()) {
            sb.append("Inc\t")
                .append(valueSet.getValueSet().getIri()).append("\t")
                .append(valueSet.getValueSet().getName()).append("\t")
                .append(valueSet.getRelationship().getIri()).append("\t")
                .append(valueSet.getRelationship().getName()).append("\t")
                .append(c.getConcept().getIri()).append("\t")
                .append(c.getConcept().getName()).append("\t")
                .append(c.getCode()).append("\t");
            if (c.getScheme() != null)
                sb.append(c.getScheme().getIri()).append("\t")
                    .append(c.getScheme().getName());

            sb.append("\n");
        }

        if (valueSet.getExcluded() != null) {
            for (ValueSetMember c : valueSet.getExcluded()) {
                sb.append("Exc\t")
                    .append(valueSet.getValueSet().getIri()).append("\t")
                    .append(valueSet.getValueSet().getName()).append("\t")
                    .append(valueSet.getRelationship().getIri()).append("\t")
                    .append(valueSet.getRelationship().getName()).append("\t")
                    .append(c.getConcept().getIri()).append("\t")
                    .append(c.getConcept().getName()).append("\t")
                    .append(c.getCode()).append("\t");
                if (c.getScheme() != null)
                    sb.append(c.getScheme().getIri()).append("\t")
                        .append(c.getScheme().getName());

                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
