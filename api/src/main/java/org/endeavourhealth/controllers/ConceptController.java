package org.endeavourhealth.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.graph.ConceptServiceRDF4J;
import org.endeavourhealth.dto.ConceptDto;
import org.endeavourhealth.converters.ImLangConverter;
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
    ImLangConverter imLangConverter;



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
        return imLangConverter.convertToImLang(conceptService.getConcept(iri));
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
    
    @GetMapping(value = "/referenceSuggestions")
    public List<ConceptReference> getSuggestions(@RequestParam String keyword, @RequestParam String word) {
//    	TODO generate and return suggestions
    	return new ArrayList<ConceptReference>(Arrays. asList(
	    			new ConceptReference(":961000252104", "method (attribute)"), 
	    			new ConceptReference(":1271000252102", "Hospital inpatient admission"),
	    			new ConceptReference(":1911000252103", "Transfer event")
    			));
    }
    
    @PostMapping
    public Concept createConcept(@RequestBody ConceptDto conceptDto) {
//    	TODO convert conceptDto to concept
//    	TODO save concept
    	return new Concept();
    }

    @GetMapping(value = "/{iri}/test")
    public JsonNode getTTConcept(@PathVariable("iri") String conceptIri) throws JsonProcessingException {
        // return conceptService.getTTConcept(conceptIri);
        return new ObjectMapper().readTree("{      \"iri\": \"http://envhealth.info/im#25451000252115\",\n" +
                "      \"http://envhealth.info/im#code\": \"25451000252115\",\n" +
                "      \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\",\n" +
                "      \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\n" +
                "        \"http://www.w3.org/2002/07/owl#intersectionOf\": [{\n" +
                "          \"iri\": \"http://snomed.info/sct#62014003\",\n" +
                "          \"name\": \"Drug reaction\"\n" +
                "        }, {\n" +
                "          \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\n" +
                "            \"iri\": \"http://snomed.info/sct#384976003\",\n" +
                "            \"name\": \"Amlodipine besilate (substance)\"\n" +
                "          },\n" +
                "          \"http://www.w3.org/2002/07/owl#onProperty\": {\n" +
                "            \"iri\": \"http://snomed.info/sct#246075003\",\n" +
                "            \"name\": \"Causative agent (attribute)\"\n" +
                "          },\n" +
                "          \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\n" +
                "            \"iri\": \"http://www.w3.org/2002/07/owl#Restriction\",\n" +
                "            \"name\": \"Restriction\"\n" +
                "          }\n" +
                "        }]\n" +
                "      }],\n" +
                "      \"http://envhealth.info/im#scheme\": {\n" +
                "        \"iri\": \"http://envhealth.info/im#891071000252105\",\n" +
                "        \"iri\": \"Snomed code\"\n" +
                "      },\n" +
                "      \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\n" +
                "        \"iri\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                "        \"name\": \"Class\"\n" +
                "      }\n" +
                "    };\n");
    }

    @GetMapping(value = "/formSchema")
    public JsonNode getTTFormSchema() throws JsonProcessingException {

        return new ObjectMapper().readTree("[\n" +
                "  {\n" +
                "\t\"predicate\": \"http://www.w3.org/2000/01/rdf-schema#label\",\n" +
                "\t\"fieldType\": \"TTEdit\",\n" +
                "\t\"label\": \"Label\",\n" +
                "\t\"size\": 12\n" +
                "  },\n" +
                "  {\n" +
                "\t\"predicate\": \"http://envhealth.info/im#scheme\",\n" +
                "\t\"fieldType\": \"TTIRIEdit\",\n" +
                "\t\"label\": \"Scheme\",\n" +
                "\t\"size\": 4\n" +
                "  },\n" +
                "  {\n" +
                "\t\"predicate\": \"http://envhealth.info/im#code\",\n" +
                "\t\"fieldType\": \"TTEdit\",\n" +
                "\t\"label\": \"Code\",\n" +
                "\t\"size\": 8\n" +
                "  },\n" +
                "  {\n" +
                "\t\"predicate\": \"http://www.w3.org/2002/07/owl#equivalentClass\",\n" +
                "\t\"fieldType\": \"TTExpressionList\",\n" +
                "\t\"label\": \"Equivalent to\",\n" +
                "\t\"size\": 12\n" +
                "  },\n" +
                "  {\n" +
                "\t\"predicate\": \"http://www.w3.org/2002/07/owl#intersectionOf\",\n" +
                "\t\"fieldType\": \"TTExpressionList\",\n" +
                "\t\"label\": \"Intersection\",\n" +
                "\t\"size\": 12\n" +
                "  }\n" +
                "]");
    }

    @GetMapping(value = "/namespaces")
    public JsonNode getNamespaces() throws JsonProcessingException {

        return new ObjectMapper().readTree("[\n" +
                "  {\n" +
                "\t\"uri\": \"http://www.w3.org/2000/01/rdf-schema#\",\n" +
                "\t\"prefix\": \"rdfs:\"\n" +
                "  },\n" +
                "  {\n" +
                "\t\"uri\": \"http://envhealth.info/im#\",\n" +
                "\t\"prefix\": \"im:\"\n" +
                "  },\n" +
                "  {\n" +
                "\t\"uri\": \"http://www.w3.org/2002/07/owl#\",\n" +
                "\t\"prefix\": \"owl:\"\n" +
                "  },\n" +
                "  {\n" +
                "\t\"uri\": \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",\n" +
                "\t\"prefix\": \"rdf:\"\n" +
                "  }\n" +
                "]");
    }
}
