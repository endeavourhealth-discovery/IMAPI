package org.endeavourhealth.controllers;

import java.util.List;

import org.endeavourhealth.converters.ConceptToImLang;
import org.endeavourhealth.converters.ImLangToConcept;
import org.endeavourhealth.dataaccess.IConceptService;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
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
//    @Qualifier("ConceptServiceRDF4J")
    IConceptService conceptService;

    @Autowired
    ConceptToImLang conceptToImLang;
    
    @Autowired
    ImLangToConcept imLangToConcept;



    @PostMapping(value = "/search")
    public SearchResponse advancedSearch(@RequestBody SearchRequest request) {
        return new SearchResponse()
            .setConcepts(
                conceptService.advancedSearch(request)
            );
    }

    @GetMapping(value = "/{iri}", produces = "application/json")
    public Concept getConcept(@PathVariable("iri") String iri) {
        return conceptService.getConcept(iri);
    }
    
    @GetMapping(value = "/{iri}", produces = "application/imlang")
    public String getConceptImLang(@PathVariable("iri") String iri) {
    	
    	String conceptDefinition = conceptToImLang.translateConceptToImLang(conceptService.getConcept(iri));
    	imLangToConcept.translateDefinitionToConcept(conceptDefinition);
    	
        return conceptDefinition;
    }
    
    @PostMapping()
    public Concept saveConcept(@RequestBody String conceptDefinition) {
        return imLangToConcept.translateDefinitionToConcept(conceptDefinition);
    }

    @GetMapping(value = "/{iri}/children")
    public List<ConceptReferenceNode> getConceptChildren(@PathVariable("iri") String iri,
                                                         @RequestParam(name = "page", required = false) Integer page,
                                                         @RequestParam(name = "size", required = false) Integer size,
                                                         @RequestParam(name = "includeLegacy", required = false) boolean includeLegacy
    ) {
        return conceptService.getImmediateChildren(iri, page, size, includeLegacy);
    }

    @GetMapping(value = "/{iri}/parentHierarchy")
    public List<ConceptReferenceNode> getConceptParentHierarchy(@PathVariable("iri") String iri) {
        List<ConceptReferenceNode> parents = conceptService.getParentHierarchy(iri);

        return parents;
    }

    @GetMapping(value = "/{iri}/parents")
    public List<ConceptReferenceNode> getConceptParents(@PathVariable("iri") String iri,
                                                        @RequestParam(name = "page", required = false) Integer page,
                                                        @RequestParam(name = "size", required = false) Integer size,
                                                        @RequestParam(name = "includeLegacy", required = false) boolean includeLegacy
    ) {
        return conceptService.getImmediateParents(iri, page, size, includeLegacy);
    }

    @GetMapping(value = "/{iri}/parents/definitions")
    public List<Concept> getConceptAncestorDefinitions(@PathVariable("iri") String iri) {
        return conceptService.getAncestorDefinitions(iri);
    }

    @GetMapping(value = "/{iri}/usages")
    public List<ConceptSummary> conceptUsages(@PathVariable("iri") String iri) {
        return conceptService.usages(iri);
    }

    @GetMapping(value = "/{iri}/mappedFrom")
    public List<ConceptReference> getCoreMappedFromLegacy(@PathVariable("iri") String legacyIri) {
        return conceptService.getCoreMappedFromLegacy(legacyIri);
    }

    @GetMapping(value = "/{iri}/mappedTo")
    public List<ConceptReference> getLegacyMappedToCore(@PathVariable("iri") String coreIri) {
        return conceptService.getLegacyMappedToCore(coreIri);
    }

    @PostMapping(value = "/{iri}/isWhichType")
    public List<ConceptReference> conceptIsWhichType(@PathVariable("iri") String iri,
                                                     @RequestBody List<String> candidates) {
        return conceptService.isWhichType(iri, candidates);
    }

    @GetMapping(value = "/{iri}/members")
    public ExportValueSet valueSetMembersJson(@PathVariable("iri") String iri, @RequestParam(name="expanded", required = false) boolean expanded) {
        return conceptService.getValueSetMembers(iri, expanded);
    }

    @GetMapping(value = "/{iri}/members", produces = {"text/csv"})
    public String valueSetMembersCSV(@PathVariable("iri") String iri, @RequestParam(name="expanded", required = false) boolean expanded) {
        ExportValueSet exportValueSet = conceptService.getValueSetMembers(iri, expanded);

        StringBuilder sb = new StringBuilder();

        sb.append("Inc\\Exc\tValueSetIri\tValueSetName\tMemberIri\tMemberTerm\tMemberCode\tMemberSchemeIri\tMemberSchemeName\n");

        for(ValueSetMember c : exportValueSet.getIncluded()) {
            sb.append("Inc\t")
                .append(exportValueSet.getValueSet().getIri()).append("\t")
                .append(exportValueSet.getValueSet().getName()).append("\t")
                .append(c.getConcept().getIri()).append("\t")
                .append(c.getConcept().getName()).append("\t")
                .append(c.getCode()).append("\t");
            if (c.getScheme() != null)
                sb.append(c.getScheme().getIri()).append("\t")
                    .append(c.getScheme().getName());

            sb.append("\n");
        }

        if (exportValueSet.getExcluded() != null) {
            for (ValueSetMember c : exportValueSet.getExcluded()) {
                sb.append("Exc\t")
                    .append(exportValueSet.getValueSet().getIri()).append("\t")
                    .append(exportValueSet.getValueSet().getName()).append("\t")
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

    @GetMapping(value = "/{iri}/isMemberOf/{valueSetIri}")
    public ValueSetMembership isMemberOfValueSet(@PathVariable("iri") String conceptIri, @PathVariable("valueSetIri") String valueSetIri) {
        return conceptService.isValuesetMember(valueSetIri, conceptIri);
    }
}
