package org.endeavourhealth.imapi.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.DataFormatException;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.logic.service.ConfigService;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.SemanticProperty;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
@Api(value="EntityController")
@SwaggerDefinition(tags = {
    @Tag(name = "Entity Controller", description = "Main Entity endpoint")
})
public class EntityController {
    private static final Logger LOG = LoggerFactory.getLogger(EntityController.class);

	@Autowired
    EntityService entityService;

	@Autowired
	ConfigService configService;

	@PostMapping(value = "/search")
    @ApiOperation(
        value = "Advanced entity search",
        notes = "Performs an advanced entity search with multiple filter options",
        response = SearchResponse.class
    )
	public SearchResponse advancedSearch(@RequestBody SearchRequest request) throws SQLException {
	    LOG.debug("advancedSearch");
        return new SearchResponse().setEntities(entityService.advancedSearch(request));
	}

    @GetMapping(value = "/partial", produces = "application/json")
    public TTEntity getPartialEntity(@RequestParam(name = "iri") String iri,
                                     @RequestParam(name = "predicate") Set<String> predicates,
                                     @RequestParam(name = "limit", required = false) Integer limit) throws SQLException {
        LOG.debug("getPartialEntity");
        if (limit == null)
            limit = EntityService.UNLIMITED;
        return entityService.getEntityPredicates(iri, predicates, limit).getEntity();
    }

	@GetMapping(value = "/matchedFrom", produces = "application/json")
	public Collection<SimpleMap> getMatchedFrom(@RequestParam(name = "iri") String iri) throws SQLException {
		LOG.debug("getMatchedFrom");
		return entityService.getMatchedFrom(iri);
	}

    @GetMapping(value = "/partialBundle", produces = "application/json")
    public TTBundle getPartialEntityBundle(@RequestParam(name = "iri") String iri,
                                     @RequestParam(name = "predicate") Set<String> predicates,
                                     @RequestParam(name = "limit", required = false) Integer limit) throws SQLException {
        LOG.debug("getPartialEntityBundle");
        if (limit == null)
            limit = EntityService.UNLIMITED;
        return entityService.getEntityPredicates(iri, predicates, limit);
    }

    @GetMapping(value = "/inferredBundle", produces = "application/json")
    public TTBundle getInferredBundle(@RequestParam(name = "iri") String iri) throws SQLException {
        LOG.debug("getInferredBundle");
        return entityService.getInferredBundle(iri);
    }

    @GetMapping(value = "/axiomBundle", produces = "application/json")
    public TTBundle getAxiomBundle(@RequestParam(name = "iri") String iri) throws SQLException {
        LOG.debug("getAxiomBundle");
        return entityService.getAxiomBundle(iri);
    }

	@GetMapping(value = "/children")
	public List<EntityReferenceNode> getEntityChildren(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) throws SQLException {
        LOG.debug("getEntityChildren");
        if (page == null & size == null) {
            page = 1;
            size = EntityService.MAX_CHILDREN;
        }
        return entityService.getImmediateChildren(iri, page, size, false);
	}

	@GetMapping(value = "/download")
	public HttpEntity<Object> download(
	    @RequestParam String iri,
        @RequestParam String format,
        @RequestParam(name = "hasSubTypes", required = false, defaultValue = "false") boolean hasSubTypes,
        @RequestParam(name = "inferred", required = false, defaultValue = "false") boolean inferred,
        @RequestParam(name = "dataModelProperties", required = false, defaultValue = "false") boolean dataModelProperties,
        @RequestParam(name = "members", required = false, defaultValue = "false") boolean members,
        @RequestParam(name = "expandMembers", required = false, defaultValue = "false") boolean expandMembers,
        @RequestParam(name = "expandSubsets", required = false, defaultValue = "false") boolean expandSubsets,
        @RequestParam(name = "axioms", required = false, defaultValue = "false") boolean axioms,
        @RequestParam(name = "terms", required = false, defaultValue = "false") boolean terms,
        @RequestParam(name = "isChildOf", required = false, defaultValue = "false") boolean isChildOf,
        @RequestParam(name = "hasChildren", required = false, defaultValue = "false") boolean hasChildren,
        @RequestParam(name = "inactive", required = false, defaultValue = "false") boolean inactive
    ) throws SQLException, IOException {
        LOG.debug("download");
        if (iri == null || iri.isEmpty() || format == null || format.isEmpty())
            return null;

        TTIriRef entity = entityService.getEntityReference(iri);

        List<ComponentLayoutItem> configs = configService.getConfig("definition", new TypeReference<List<ComponentLayoutItem>>(){});

        String filename = entity.getName() + " " + LocalDate.now();
        HttpHeaders headers = new HttpHeaders();

        if ("excel".equals(format)) {
            XlsHelper xls = entityService.getExcelDownload(iri, configs, hasSubTypes, inferred, dataModelProperties, members, expandMembers,expandSubsets, axioms, terms, isChildOf, hasChildren, inactive);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                xls.getWorkbook().write(outputStream);
                xls.getWorkbook().close();
                headers.setContentType(new MediaType("application", "force-download"));
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + ".xlsx\"");

                return new HttpEntity<>(outputStream.toByteArray(), headers);
            }
        } else {
            DownloadDto json = entityService.getJsonDownload(iri, configs, hasSubTypes, inferred, dataModelProperties, members, expandMembers,expandSubsets, axioms, terms, isChildOf, hasChildren, inactive);

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + ".json\"");

            return new HttpEntity<>(json, headers);
        }
    }

	@GetMapping(value = "/parents")
	public List<EntityReferenceNode> getEntityParents(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) throws SQLException {
        LOG.debug("getEntityParents");
        return entityService.getImmediateParents(iri, page, size, false);
	}

	@GetMapping(value = "/usages")
	public List<TTIriRef> entityUsages(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) throws SQLException {
        LOG.debug("entityUsages");
        return entityService.usages(iri,page,size);
	}

	@GetMapping("/usagesTotalRecords")
	public Integer totalRecords(@RequestParam(name = "iri") String iri) throws SQLException {
		LOG.debug("totalRecords");
		return entityService.totalRecords(iri);
	}

	@PostMapping(value = "/isWhichType")
	public List<TTIriRef> entityIsWhichType(@RequestParam(name = "iri") String iri,
			@RequestBody List<String> candidates) throws SQLException {
        LOG.debug("entityIsWhichType");
        return entityService.isWhichType(iri, candidates);
	}

	@GetMapping(value = "/members")
	public ExportValueSet valueSetMembersJson(
	    @RequestParam(name = "iri") String iri,
		@RequestParam(name = "expandMembers", required = false) boolean expandMembers,
		@RequestParam(name = "expandSubsets", required = false) boolean expandSubsets,
        @RequestParam(name = "limit", required = false) Integer limit
    ) throws SQLException, DataFormatException {
        LOG.debug("valueSetMembersJson");
        return entityService.getValueSetMembers(iri, expandMembers,expandSubsets, limit);
	}

	@GetMapping(value = "/members", produces = { "text/csv" })
	public String valueSetMembersCSV(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expandedMember", required = false) boolean expandedMember,
			@RequestParam(name = "expandedSubset", required = false) boolean expandedSubset) throws SQLException {
        LOG.debug("valueSetMembersCSV");
        return entityService.valueSetMembersCSV(iri, expandedMember, expandedSubset);
	}

	@GetMapping(value = "/isMemberOf")
	public ValueSetMembership isMemberOfValueSet(@RequestParam(name = "iri") String entityIri,
			@RequestParam("valueSetIri") String valueSetIri) throws SQLException {
        LOG.debug("isMemberOfValueSet");
		return entityService.isValuesetMember(valueSetIri, entityIri);
	}

	@GetMapping(value = "/referenceSuggestions")
	public List<TTIriRef> getSuggestions(@RequestParam String keyword, @RequestParam String word) {
	    LOG.debug("getSuggestions");
//    	TODO generate and return suggestions
		return new ArrayList<>(Arrays.asList(new TTIriRef(":961000252104", "method (attribute)"),
            new TTIriRef(":1271000252102", "Hospital inpatient admission"),
            new TTIriRef(":1911000252103", "Transfer event")));
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public TTEntity createEntity(@RequestBody EntityDefinitionDto entityDto) {
	    LOG.debug("createEntity");
//    	TODO convert entityDto to entity and save
		return new TTEntity();
	}

	@GetMapping(value = "/graph")
	public GraphDto getGraphData(@RequestParam(name = "iri") String iri) throws SQLException {
	    LOG.debug("getGraphData");
		return entityService.getGraphData(iri);
	}

	@GetMapping("/termCode")
	public List<TermCode> getTermCodes(@RequestParam(name = "iri") String iri) throws SQLException {
	    LOG.debug("getTermCodes");
		return entityService.getEntityTermCodes(iri);
	}

	@GetMapping("/dataModelProperties")
	public List<DataModelProperty> getDataModelProperties(@RequestParam(name = "iri") String iri) throws SQLException {
	    LOG.debug("getDataModelProperties");
		return entityService.getDataModelProperties(iri);
	}
	
	@GetMapping("/definition")
	public EntityDefinitionDto getEntityDefinitionDto(@RequestParam(name = "iri") String iri) throws SQLException {
	    LOG.debug("getEntityDefinitionDto");
		return entityService.getEntityDefinitionDto(iri);
	}

	@GetMapping("/summary")
	public EntitySummary getSummary(@RequestParam(name = "iri") String iri) throws SQLException {
	    LOG.debug("getSummary");
		return entityService.getSummary(iri);
	}

	@GetMapping("/shape")
	public TTEntity getConceptShape(@RequestParam(name = "iri") String iri) throws SQLException {
		LOG.debug("getConceptShape");
		return entityService.getConceptShape(iri);
	}

	@GetMapping("/namespaces")
	public List<Namespace> getNamespaces() throws SQLException {
		LOG.debug("getNamespaces");
		return entityService.getNamespaces();
	}

}
