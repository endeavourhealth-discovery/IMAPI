package org.endeavourhealth.controllers;

import java.sql.SQLException;
import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.converters.EntityToImLang;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.logic.service.EntityService;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.SemanticProperty;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
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
@RequestMapping("api/entity")
@CrossOrigin(origins = "*")
@Api(value="EntityController", description = "Main Entity endpoint")
public class EntityController {
    private static final Logger LOG = LoggerFactory.getLogger(EntityController.class);

	@Autowired
    EntityService entityService;

	@Autowired
	EntityToImLang entityToImLang;

	@PostMapping(value = "/search")
    @ApiOperation(
        value = "Advanced entity search",
        notes = "Performs an advanced entity search with multiple filter options",
        response = SearchResponse.class
    )
	public SearchResponse advancedSearch(@RequestBody SearchRequest request) throws Exception {
        return new SearchResponse().setEntities(entityService.advancedSearch(request));
	}

    @GetMapping(value = "/partial", produces = "application/json")
    public TTEntity getPartialEntity(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicate") Set<String> predicates) throws SQLException, JsonProcessingException {
        return entityService.getEntityPredicates(iri, predicates);
    }

	@GetMapping(value = "", produces = "application/imlang")
	public String getEntityImLang(@RequestParam(name = "iri") String iri) throws SQLException {
		return entityToImLang.translateEntityToImLang(entityService.getEntityPredicates(iri,null));
	}

	@GetMapping(value = "/children")
	public List<EntityReferenceNode> getEntityChildren(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) throws SQLException {
		return entityService.getImmediateChildren(iri, page, size, includeLegacy, false);
	}

	@GetMapping(value = "/download")
	public HttpEntity download(@RequestParam String iri, @RequestParam String format, @RequestParam boolean children,
			@RequestParam boolean parents, @RequestParam boolean dataModelProperties, @RequestParam boolean members, @RequestParam boolean expandMembers,
			@RequestParam boolean semanticProperties, @RequestParam boolean inactive) throws SQLException, JsonProcessingException {
		return entityService.download(iri, format, children, parents, dataModelProperties, members, expandMembers, semanticProperties, inactive);
	}

	@GetMapping(value = "/parents")
	public List<EntityReferenceNode> getEntityParents(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) throws SQLException {
		return entityService.getImmediateParents(iri, page, size, includeLegacy, false);
	}

	@GetMapping(value = "/usages")
	public List<TTIriRef> entityUsages(@RequestParam(name = "iri") String iri) throws SQLException {
		return entityService.usages(iri);
	}

	@PostMapping(value = "/isWhichType")
	public List<TTIriRef> entityIsWhichType(@RequestParam(name = "iri") String iri,
			@RequestBody List<String> candidates) throws SQLException {
		return entityService.isWhichType(iri, candidates);
	}

	@GetMapping(value = "/members")
	public ExportValueSet valueSetMembersJson(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expanded", required = false) boolean expanded) throws SQLException {
		return entityService.getValueSetMembers(iri, expanded);
	}

	@GetMapping(value = "/members", produces = { "text/csv" })
	public String valueSetMembersCSV(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expanded", required = false) boolean expanded) throws SQLException {
		return entityService.valueSetMembersCSV(iri, expanded);
	}

	@GetMapping(value = "/isMemberOf")
	public ValueSetMembership isMemberOfValueSet(@RequestParam(name = "iri") String entityIri,
			@RequestParam("valueSetIri") String valueSetIri) throws SQLException {
		return entityService.isValuesetMember(valueSetIri, entityIri);
	}

	@GetMapping(value = "/referenceSuggestions")
	public List<TTIriRef> getSuggestions(@RequestParam String keyword, @RequestParam String word) {
//    	TODO generate and return suggestions
		return new ArrayList<>(Arrays.asList(new TTIriRef(":961000252104", "method (attribute)"),
            new TTIriRef(":1271000252102", "Hospital inpatient admission"),
            new TTIriRef(":1911000252103", "Transfer event")));
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public TTEntity createEntity(@RequestBody EntityDefinitionDto entityDto) {
//    	TODO convert entityDto to entity
//    	TODO save entity
		return new TTEntity();
	}

	@GetMapping(value = "/graph")
	public GraphDto getGraphData(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return entityService.getGraphData(iri);
	}

	@GetMapping("/termCode")
	public List<TermCode> getTermCodes(@RequestParam(name = "iri") String iri) throws SQLException {
		return entityService.getEntityTermCodes(iri);
	}
	
	@GetMapping("/semanticProperties")
	public List<SemanticProperty> getSemanticProperties(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return entityService.getSemanticProperties(iri);
	}

	@GetMapping("/dataModelProperties")
	public List<DataModelProperty> getDataModelProperties(@RequestParam(name = "iri") String iri) throws JsonProcessingException, SQLException {
		return entityService.getDataModelProperties(iri);
	}
	
	@GetMapping("/definition")
	public EntityDefinitionDto getEntityDefinitionDto(@RequestParam(name = "iri") String iri) throws JsonProcessingException, SQLException {
		return entityService.getEntityDefinitionDto(iri);
	}

	@GetMapping("/summary")
	public EntitySummary getSummary(String iri) throws SQLException {
		return entityService.getSummary(iri);
	}

}
