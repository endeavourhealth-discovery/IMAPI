package com.endavourhealth.controllers;

import java.util.List;

import com.endavourhealth.dataaccess.IConceptService;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
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
                                        @RequestParam(name = "includeLegacy", required = false) Boolean includeLegacy,
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
        return conceptService.getParentHierarchy(iri);
    }

    @GetMapping(value = "/{iri}/parents/definitions")
    public List<Concept> getConceptAncestorDefinitions(@PathVariable("iri") String iri) {
        return conceptService.getAncestorDefinitions(iri);
    }

    @GetMapping(value = "/{iri}/children")
    public List<ConceptReference> getConceptChildren(@PathVariable("iri") String iri,
                                                    @RequestParam(name = "page", required = false) Integer page,
                                                    @RequestParam(name = "size", required = false) Integer size,
                                                    @RequestParam(name = "includeLegacy", required = false) Boolean includeLegacy
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
}
