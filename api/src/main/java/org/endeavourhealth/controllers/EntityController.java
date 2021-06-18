package org.endeavourhealth.controllers;

import java.sql.SQLException;
import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.converters.EntityToImLang;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.logic.service.EntityService;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.DataModelPropertyDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.RecordStructureDto;
import org.endeavourhealth.imapi.model.dto.RecordStructureDto.EntityReference;
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

	@GetMapping(value = "", produces = "application/json")
	public TTEntity getEntity(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return entityService.getEntity(iri);
	}

	@GetMapping(value = "", produces = "application/imlang")
	public String getEntityImLang(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return entityToImLang.translateEntityToImLang(entityService.getEntity(iri));
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
			@RequestParam boolean parents, @RequestParam boolean properties, @RequestParam boolean members, @RequestParam boolean expandMembers,
			@RequestParam boolean roles, @RequestParam boolean inactive) throws SQLException, JsonProcessingException {
		return entityService.download(iri, format, children, parents, properties, members, expandMembers, roles, inactive);
	}

	@GetMapping(value = "/parents")
	public List<EntityReferenceNode> getEntityParents(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) throws SQLException {
		return entityService.getImmediateParents(iri, page, size, includeLegacy, false);
	}

	@GetMapping(value = "/parents/definitions")
	public List<TTEntity> getEntityAncestorDefinitions(@RequestParam(name = "iri") String iri) throws SQLException {
		return entityService.getAncestorDefinitions(iri);
	}

	@GetMapping(value = "/usages")
	public List<TTIriRef> entityUsages(@RequestParam(name = "iri") String iri) throws SQLException {
		return entityService.usages(iri);
	}

	@GetMapping(value = "/mappedFrom")
	public List<TTIriRef> getCoreMappedFromLegacy(@RequestParam(name = "iri") String legacyIri) throws SQLException {
		return entityService.getCoreMappedFromLegacy(legacyIri);
	}

	@GetMapping(value = "/mappedTo")
	public List<TTIriRef> getLegacyMappedToCore(@RequestParam(name = "iri") String coreIri) throws SQLException {
		return entityService.getLegacyMappedToCore(coreIri);
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
		return new ArrayList<TTIriRef>(Arrays.asList(new TTIriRef(":961000252104", "method (attribute)"),
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

	@GetMapping(value = "/roles")
	public List<PropertyValue> getRoles(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return entityService.getRoles(iri);
	}

	@GetMapping(value = "/properties")
	public List<PropertyValue> getAllProperties(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return entityService.getAllProperties(iri);
	}

	@GetMapping(value = "/graph")
	public GraphDto getGraphData(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return entityService.getGraphData(iri);
	}

	public List<String> getFlatParentHierarchy(String iri, List<String> flatParentIris) throws SQLException {
		List<EntityReferenceNode> parents = entityService.getImmediateParents(iri, null, null, false, false);

		if (parents == null) {
			return flatParentIris;
		}

		for (EntityReferenceNode parent : parents) {
			flatParentIris.add(parent.getIri());
			getFlatParentHierarchy(parent.getIri(), flatParentIris);
		}

		return flatParentIris;
	}

	@GetMapping("/termCode")
	public List<TermCode> getTermCodes(@RequestParam(name = "iri") String iri) throws SQLException {
		return entityService.getEntityTermCodes(iri);
	}
	
	@GetMapping("/recordStructure")
	public List<RecordStructureDto> getRecordStructure(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return entityService.getRecordStructure(iri);
	}
	
	@GetMapping("/definitionSubTypes")
	public List<EntityReference> getDefinitionSubTypes(@RequestParam(name = "iri") String iri) throws SQLException {
		return entityService.getDefinitionSubTypes(iri);
	}
	
	@GetMapping("/definition")
	public EntityDefinitionDto getEntityDefinitionDto(@RequestParam(name = "iri") String iri) throws JsonProcessingException, SQLException {
		return entityService.getEntityDefinitionDto(iri);
	}
	
	@GetMapping("/dataModelProperties")
	public List<DataModelPropertyDto> getDataModelProperties(@RequestParam(name = "iri") String iri) throws JsonProcessingException, SQLException {
		return entityService.getDataModelProperties(iri);
	}

	@GetMapping("/complexMappings")
	public TTArray getComplexMappings(@RequestParam(name = "iri") String iri) throws SQLException, JsonProcessingException {
		return entityService.getComplexMappings(iri);
	}

	@GetMapping("/summary")
	public EntitySummary getSummary(String iri) throws SQLException {
		return entityService.getSummary(iri);
	}

}
