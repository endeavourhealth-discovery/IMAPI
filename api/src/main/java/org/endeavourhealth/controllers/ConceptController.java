package org.endeavourhealth.controllers;

import java.sql.SQLException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.converters.ConceptToImLang;
import org.endeavourhealth.dataaccess.ConceptService;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.definition.ConceptDefinitionDto;
import org.endeavourhealth.imapi.model.graph.GraphDto;
import org.endeavourhealth.imapi.model.recordstructure.RecordStructureDto;
import org.endeavourhealth.imapi.model.recordstructure.RecordStructureDto.ConceptReference;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/concept")
@CrossOrigin(origins = "*")
public class ConceptController {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptController.class);

	@Autowired
    ConceptService conceptService;

	@Autowired
	ConceptToImLang conceptToImLang;

	@PostMapping(value = "/search")
	public SearchResponse advancedSearch(@RequestBody SearchRequest request) throws Exception {
        return new SearchResponse().setConcepts(conceptService.advancedSearch(request));
	}

	@GetMapping(value = "", produces = "application/json")
	public TTConcept getConcept(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return conceptService.getConcept(iri);
	}

	@GetMapping(value = "", produces = "application/imlang")
	public String getConceptImLang(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return conceptToImLang.translateConceptToImLang(conceptService.getConcept(iri));
	}

	@GetMapping(value = "/children")
	public List<ConceptReferenceNode> getConceptChildren(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) throws SQLException {
		return conceptService.getImmediateChildren(iri, page, size, includeLegacy, false);
	}

	@GetMapping(value = "/download")
	public HttpEntity download(@RequestParam String iri, @RequestParam String format, @RequestParam boolean children,
			@RequestParam boolean parents, @RequestParam boolean properties, @RequestParam boolean members,
			@RequestParam boolean roles, @RequestParam boolean inactive) throws SQLException, JsonProcessingException {
		return conceptService.download(iri, format, children, parents, properties, members, roles, inactive);
	}

	@GetMapping(value = "/parents")
	public List<ConceptReferenceNode> getConceptParents(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) throws SQLException {
		return conceptService.getImmediateParents(iri, page, size, includeLegacy, false);
	}

	@GetMapping(value = "/parents/definitions")
	public List<TTConcept> getConceptAncestorDefinitions(@RequestParam(name = "iri") String iri) throws SQLException {
		return conceptService.getAncestorDefinitions(iri);
	}

	@GetMapping(value = "/usages")
	public List<TTIriRef> conceptUsages(@RequestParam(name = "iri") String iri) throws SQLException {
		return conceptService.usages(iri);
	}

	@GetMapping(value = "/mappedFrom")
	public List<TTIriRef> getCoreMappedFromLegacy(@RequestParam(name = "iri") String legacyIri) throws SQLException {
		return conceptService.getCoreMappedFromLegacy(legacyIri);
	}

	@GetMapping(value = "/mappedTo")
	public List<TTIriRef> getLegacyMappedToCore(@RequestParam(name = "iri") String coreIri) throws SQLException {
		return conceptService.getLegacyMappedToCore(coreIri);
	}

	@PostMapping(value = "/isWhichType")
	public List<TTIriRef> conceptIsWhichType(@RequestParam(name = "iri") String iri,
			@RequestBody List<String> candidates) throws SQLException {
		return conceptService.isWhichType(iri, candidates);
	}

	@GetMapping(value = "/members")
	public ExportValueSet valueSetMembersJson(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expanded", required = false) boolean expanded) throws SQLException {
		return conceptService.getValueSetMembers(iri, expanded);
	}

	@GetMapping(value = "/members", produces = { "text/csv" })
	public String valueSetMembersCSV(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expanded", required = false) boolean expanded) throws SQLException {
		return conceptService.valueSetMembersCSV(iri, expanded);
	}

	@GetMapping(value = "/isMemberOf")
	public ValueSetMembership isMemberOfValueSet(@RequestParam(name = "iri") String conceptIri,
			@RequestParam("valueSetIri") String valueSetIri) throws SQLException {
		return conceptService.isValuesetMember(valueSetIri, conceptIri);
	}

	@GetMapping(value = "/referenceSuggestions")
	public List<TTIriRef> getSuggestions(@RequestParam String keyword, @RequestParam String word) {
//    	TODO generate and return suggestions
		return new ArrayList<TTIriRef>(Arrays.asList(new TTIriRef(":961000252104", "method (attribute)"),
				new TTIriRef(":1271000252102", "Hospital inpatient admission"),
				new TTIriRef(":1911000252103", "Transfer event")));
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public TTConcept createConcept(@RequestBody ConceptDefinitionDto conceptDto) {
//    	TODO convert conceptDto to concept
//    	TODO save concept
		return new TTConcept();
	}

	@GetMapping(value = "/roles")
	public List<PropertyValue> getRoles(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return conceptService.getRoles(iri);
	}

	@GetMapping(value = "/properties")
	public List<PropertyValue> getAllProperties(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return conceptService.getAllProperties(iri);
	}

	@GetMapping(value = "/graph")
	public GraphDto getGraphData(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return conceptService.getGraphData(iri);
	}

	public List<String> getFlatParentHierarchy(String iri, List<String> flatParentIris) throws SQLException {
		List<ConceptReferenceNode> parents = conceptService.getImmediateParents(iri, null, null, false, false);

		if (parents == null) {
			return flatParentIris;
		}

		for (ConceptReferenceNode parent : parents) {
			flatParentIris.add(parent.getIri());
			getFlatParentHierarchy(parent.getIri(), flatParentIris);
		}

		return flatParentIris;
	}

	@GetMapping("/termCode")
	public List<TermCode> getTermCodes(@RequestParam(name = "iri") String iri) throws SQLException {
		return conceptService.getConceptTermCodes(iri);
	}
	
	@GetMapping("/recordStructure")
	public List<RecordStructureDto> getRecordStructure(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return conceptService.getRecordStructure(iri);
	}
	
	@GetMapping("/definitionSubTypes")
	public List<ConceptReference> getDefinitionSubTypes(@RequestParam(name = "iri") String iri) throws SQLException {
		return conceptService.getDefinitionSubTypes(iri);
	}
	
	@GetMapping("/definition")
	public ConceptDefinitionDto getConceptDefinitionDto(@RequestParam(name = "iri") String iri) throws JsonProcessingException, SQLException {
		return conceptService.getConceptDefinitionDto(iri);
	}
	
}
