package org.endeavourhealth.imapi.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.service.FilerService;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.dto.UnassignedEntity;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.SetAsObject;
import org.endeavourhealth.imapi.transforms.TTToTurtle;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/entity")
@CrossOrigin(origins = "*")
@Tag(name = "Entity Controller")
@RequestScope
public class EntityController {
    private static final Logger LOG = LoggerFactory.getLogger(EntityController.class);

    private final EntityService entityService = new EntityService();
	private final ConfigManager configManager = new ConfigManager();

	private static final String ATTACHMENT = "attachment;filename=\"";

	@PostMapping(value = "/public/search")
    @Operation(
        summary = "Advanced entity search",
        description = "Performs an advanced entity search with multiple filter options"
	)
	public List<SearchResultSummary> advancedSearch(@RequestBody SearchRequest request) throws OpenSearchException, URISyntaxException, IOException, ExecutionException, InterruptedException {
		LOG.debug("advancedSearch");
			return entityService.advancedSearch(request);

	}

    @GetMapping(value = "/public/partial", produces = "application/json")
    public TTEntity getPartialEntity(@RequestParam(name = "iri") String iri,
                                     @RequestParam(name = "predicate") Set<String> predicates,
                                     @RequestParam(name = "limit", required = false) Integer limit) {
        LOG.debug("getPartialEntity");
        if (limit == null)
            limit = EntityService.UNLIMITED;
        return entityService.getBundle(iri, predicates, limit).getEntity();
    }

	@GetMapping(value = "/fullEntity", produces = "application/json")
	@PreAuthorize("hasAuthority('IMAdmin')")
	public TTEntity getFullEntity(@RequestParam(name = "iri") String iri) {
		LOG.debug("getFullEntity");
		return entityService.getEntityByPredicateExclusions(iri, null, EntityService.UNLIMITED).getEntity();
	}

	@GetMapping(value = "/public/simpleMaps", produces = "application/json")
	public Collection<SimpleMap> getMatchedFrom(@RequestParam(name = "iri") String iri) {
		LOG.debug("getSimpleMaps");
		return entityService.getSimpleMaps(iri);
	}

    @GetMapping(value = "/public/partialBundle", produces = "application/json")
    public TTBundle getPartialEntityBundle(@RequestParam(name = "iri") String iri,
                                           @RequestParam(name = "predicate") Set<String> predicates,
                                           @RequestParam(name = "limit", required = false) Integer limit) {
        LOG.debug("getPartialEntityBundle");
        if (limit == null)
            limit = EntityService.UNLIMITED;
        return entityService.getBundle(iri, predicates, limit);
    }

    @GetMapping(value = "/public/inferredBundle", produces = "application/json")
    public TTBundle getInferredBundle(@RequestParam(name = "iri") String iri) {
        LOG.debug("getInferredBundle");
        return entityService.getInferredBundle(iri);
    }

	@GetMapping(value = "/public/children")
	public List<EntityReferenceNode> getEntityChildren(@RequestParam(name = "iri") String iri,
                                                       @RequestParam(name = "schemeIris", required = false) List<String> schemeIris,
                                                       @RequestParam(name = "page", required = false) Integer page,
                                                       @RequestParam(name = "size", required = false) Integer size) {
        LOG.debug("getEntityChildren");
        if (page == null && size == null) {
            page = 1;
            size = EntityService.MAX_CHILDREN;
        }
        return entityService.getImmediateChildren(iri, schemeIris, page, size, false);
	}

	@GetMapping("/public/exportConcept")
	public HttpEntity<Object> exportConcept(@RequestParam String iri, @RequestParam String format) throws JsonProcessingException {
		LOG.debug("exportConcept");
		if (iri == null || iri.isEmpty())
			return null;

		TTIriRef entity = entityService.getEntityReference(iri);

		String filename = entity.getName() + " " + LocalDate.now();
		HttpHeaders headers = new HttpHeaders();
		TTDocument document = entityService.getConcept(iri);
		return getObjectHttpEntity(format, filename, headers, document);
	}

	@GetMapping("/public/exportList")
	public HttpEntity<Object> exportList(@RequestParam List<String> iris, @RequestParam String format) throws JsonProcessingException {
		LOG.debug("exportList");
		String filename = "Concept List "+ LocalDate.now();
		HttpHeaders headers = new HttpHeaders();
		TTDocument document = entityService.getConceptList(iris);
		return getObjectHttpEntity(format, filename, headers, document);
	}

	@GetMapping("/public/exportGraph")
	public HttpEntity<Object> exportGraph(@RequestParam String iri, @RequestParam String format) throws JsonProcessingException {
		LOG.debug("exportGraph");
		TTIriRef entity = entityService.getEntityReference(iri);
		String filename = entity.getName() + " concept list "+ LocalDate.now();
		HttpHeaders headers = new HttpHeaders();
		TTDocument document = entityService.getConceptListByGraph(iri);
		return getObjectHttpEntity(format, filename, headers, document);
	}

	private HttpEntity<Object> getObjectHttpEntity(@RequestParam String format, String filename, HttpHeaders headers, TTDocument document) throws JsonProcessingException {
		if("turtle".equals(format)){

			TTToTurtle ttToTurtle = new TTToTurtle();
			String turtle = ttToTurtle.transformDocument(document);

			headers.setContentType(MediaType.TEXT_PLAIN);
			headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + filename + ".txt\"");

			return new HttpEntity<>(turtle, headers);

		}else{
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
			String json = objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);

			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + filename + ".json\"");

			return new HttpEntity<>(json, headers);
		}
	}

	@GetMapping(value = "/public/download")
	public HttpEntity<Object> download(
	    @RequestParam String iri,
        @RequestParam String format,
        @RequestParam(name = "hasSubTypes", required = false, defaultValue = "false") boolean hasSubTypes,
        @RequestParam(name = "inferred", required = false, defaultValue = "false") boolean inferred,
        @RequestParam(name = "dataModelProperties", required = false, defaultValue = "false") boolean dataModelProperties,
        @RequestParam(name = "members", required = false, defaultValue = "false") boolean members,
        @RequestParam(name = "expandMembers", required = false, defaultValue = "false") boolean expandMembers,
        @RequestParam(name = "expandSubsets", required = false, defaultValue = "false") boolean expandSubsets,
        @RequestParam(name = "terms", required = false, defaultValue = "false") boolean terms,
        @RequestParam(name = "isChildOf", required = false, defaultValue = "false") boolean isChildOf,
        @RequestParam(name = "hasChildren", required = false, defaultValue = "false") boolean hasChildren,
        @RequestParam(name = "inactive", required = false, defaultValue = "false") boolean inactive
    ) throws IOException {
        LOG.debug("download");
        if (iri == null || iri.isEmpty() || format == null || format.isEmpty())
            return null;

        TTIriRef entity = entityService.getEntityReference(iri);

        List<ComponentLayoutItem> configs = configManager.getConfig(CONFIG.DEFINITION, new TypeReference<>(){});

        String filename = entity.getName() + " " + LocalDate.now();
        HttpHeaders headers = new HttpHeaders();

		DownloadParams params = new DownloadParams();
		params.setIncludeHasSubtypes(hasSubTypes).setIncludeInferred(inferred).setIncludeProperties(dataModelProperties).setIncludeMembers(members).setExpandMembers(expandMembers).setExpandSubsets(expandSubsets).setIncludeTerms(terms).setIncludeIsChildOf(isChildOf).setIncludeHasChildren(hasChildren).setIncludeInactive(inactive);

        if ("excel".equals(format)) {
            XlsHelper xls = entityService.getExcelDownload(iri, configs, params);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                xls.getWorkbook().write(outputStream);
                xls.getWorkbook().close();
                headers.setContentType(new MediaType("application", "force-download"));
                headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + filename + ".xlsx\"");

                return new HttpEntity<>(outputStream.toByteArray(), headers);
            }
        } else {
            DownloadDto json = entityService.getJsonDownload(iri, configs, params);

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + filename + ".json\"");

            return new HttpEntity<>(json, headers);
        }
    }

	@GetMapping(value = "/public/parents")
	public List<EntityReferenceNode> getEntityParents(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "schemeIris", required = false) List<String> schemeIris,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size
    ) {
        LOG.debug("getEntityParents");
        return entityService.getImmediateParents(iri, schemeIris, page, size, false);
	}

	@GetMapping(value = "/public/usages")
	public List<TTEntity> entityUsages(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) throws JsonProcessingException {
        LOG.debug("entityUsages");

        return entityService.usages(iri,page,size);
	}

	@GetMapping("/public/usagesTotalRecords")
	public Integer totalRecords(@RequestParam(name = "iri") String iri) throws JsonProcessingException {
		LOG.debug("totalRecords");
		return entityService.totalRecords(iri);
	}

	@GetMapping(value = "/public/members")
	public ExportValueSet valueSetMembersJson(
	    @RequestParam(name = "iri") String iri,
		@RequestParam(name = "expandMembers", required = false) boolean expandMembers,
		@RequestParam(name = "expandSubsets", required = false) boolean expandSubsets,
        @RequestParam(name = "limit", required = false) Integer limit,
		@RequestParam(name = "withHyperlinks", required = false) boolean withHyperlinks
    ) {
        LOG.debug("valueSetMembersJson");
        return entityService.getValueSetMembers(iri, expandMembers,expandSubsets, limit, withHyperlinks);
	}

	@GetMapping(value = "/public/membersAsNode")
	public SetAsObject valueSetMembersAsNode(
			@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expandMembers", required = false) boolean expandMembers,
			@RequestParam(name = "expandSubsets", required = false) boolean expandSubsets,
			@RequestParam(name = "limit", required = false) Integer limit
	){
		LOG.debug("valueSetMembersNode");
		return entityService.getValueSetMembersAsNode(iri, expandMembers, expandSubsets, limit);
	}

	@GetMapping(value = "/public/members", produces = { "text/csv" })
	public String valueSetMembersCSV(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expandedMember", required = false) boolean expandedMember,
			@RequestParam(name = "expandedSubset", required = false) boolean expandedSubset) {
        LOG.debug("valueSetMembersCSV");
        return entityService.valueSetMembersCSV(iri, expandedMember, expandedSubset);
	}

	@GetMapping(value = "/public/referenceSuggestions")
	public List<TTIriRef> getSuggestions(@RequestParam String keyword, @RequestParam String word) {
	    LOG.debug("getSuggestions");
//    	TODO generate and return suggestions
		return new ArrayList<>(Arrays.asList(new TTIriRef(":961000252104", "method (attribute)"),
            new TTIriRef(":1271000252102", "Hospital inpatient admission"),
            new TTIriRef(":1911000252103", "Transfer event")));
	}

	@PostMapping(value = "/create")
	@PreAuthorize("hasAuthority('IMAdmin')")
	public TTEntity createEntity(@RequestBody TTEntity entity) throws TTFilerException, JsonProcessingException {
	    LOG.debug("createEntity");
		return entityService.createEntity(entity);
	}

	@PostMapping(value = "/update")
	@PreAuthorize("hasAuthority('IMAdmin')")
	public TTEntity updateEntity(@RequestBody TTEntity entity) throws TTFilerException, JsonProcessingException {
		LOG.debug("updateEntity");
		return entityService.updateEntity(entity);
	}

	@GetMapping(value = "/public/graph")
	public GraphDto getGraphData(@RequestParam(name = "iri") String iri) {
	    LOG.debug("getGraphData");
		return entityService.getGraphData(iri);
	}

	@GetMapping("/public/termCode")
	public List<TermCode> getTermCodes(@RequestParam(name = "iri") String iri) {
	    LOG.debug("getTermCodes");
		return entityService.getEntityTermCodes(iri);
	}

	@GetMapping("/public/dataModelProperties")
	public List<DataModelProperty> getDataModelProperties(@RequestParam(name = "iri") String iri) {
	    LOG.debug("getDataModelProperties");
		return entityService.getDataModelProperties(iri);
	}
	
	@GetMapping("/public/definition")
	public EntityDefinitionDto getEntityDefinitionDto(@RequestParam(name = "iri") String iri) {
	    LOG.debug("getEntityDefinitionDto");
		return entityService.getEntityDefinitionDto(iri);
	}

	@GetMapping("/public/summary")
	public SearchResultSummary getSummary(@RequestParam(name = "iri") String iri) {
	    LOG.debug("getSummary");
		return entityService.getSummary(iri);
	}

	@GetMapping("/public/namespaces")
	public List<Namespace> getNamespaces() {
		LOG.debug("getNamespaces");
		return entityService.getNamespaces();
	}

	@PostMapping("/public/ecl")
	public String getEcl(@RequestBody TTBundle inferred) throws DataFormatException {
		LOG.debug("getEcl");
		return entityService.getEcl(inferred);
	}

	@GetMapping("/public/setExport")
	public HttpEntity<Object> getSetExport(@RequestParam(name = "iri") String iri, @RequestParam(name = "legacy") boolean legacy) throws DataFormatException, IOException {
		LOG.debug("getSetExport");
		XSSFWorkbook workbook = entityService.getSetExport(iri,legacy);
		HttpHeaders headers = new HttpHeaders();

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			workbook.write(outputStream);
			workbook.close();
			headers.setContentType(new MediaType("application", "force-download"));
			headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + "setExport.xlsx\"");

			return new HttpEntity<>(outputStream.toByteArray(), headers);
		}
	}
	@GetMapping("/public/setExport/core")
	public HttpEntity<Object> getSetExportCore(@RequestParam(name = "iri") String iri) throws DataFormatException, IOException {
		LOG.debug("getSetExportCore");
		XSSFWorkbook workbook = entityService.getSetExport(iri,false);
		HttpHeaders headers = new HttpHeaders();

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			workbook.write(outputStream);
			workbook.close();
			headers.setContentType(new MediaType("application", "force-download"));
			headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + "setExport.xlsx\"");

			return new HttpEntity<>(outputStream.toByteArray(), headers);
		}
	}

	@GetMapping("/public/folderPath")
	public List<TTIriRef> getFolderPath(@RequestParam(name = "iri") String iri) {
		LOG.debug("getFolderPath");
		return entityService.getParentPath(iri);
	}

	@GetMapping("/public/parentHierarchy")
	public EntityReferenceNode getParentHierarchy(@RequestParam(name = "iri") String iri) {
		LOG.debug("getParentHierarchy");
		return entityService.getParentHierarchy(iri);
	}

    @GetMapping("/public/pathBetweenNodes")
    public List<TTIriRef> getPathBetweenNodes(@RequestParam(name = "descendant") String descendant, @RequestParam(name = "ancestor") String ancestor) {
        LOG.debug("getPathBetweenNodes");
        return entityService.getPathBetweenNodes(descendant, ancestor);
    }
	
	@GetMapping("/public/unmapped")
	public List<TTIriRef> getUnmapped() {
		LOG.debug("getUnassigned");
		return entityService.getUnmapped();
	}

	@GetMapping("/public/mappingSuggestions")
	public List<SearchResultSummary> getMappingSuggestions(@RequestBody SearchRequest request) throws OpenSearchException, URISyntaxException, IOException, ExecutionException, InterruptedException {
		LOG.debug("getMappingSuggestions");
		return entityService.advancedSearch(request);
	}

    @PostMapping("/public/getNames")
    public Set<TTIriRef> getNames(@RequestBody Set<String> iris) {
        LOG.debug("getNames");
        return entityService.getNames(iris);
    }
	@GetMapping("/public/parentHierarchies")
	public List<List<TTIriRef>> getParentHierarchies(@RequestParam(name = "iri") String iri) {
		LOG.debug("getParentHierarchies");
		return entityService.getParentHierarchies(iri);
	}

	@GetMapping("/public/shortestParentHierarchy")
	public List<TTIriRef> getShortestPathBetweenNodes(@RequestParam(name = "ancestor") String ancestor, @RequestParam(name = "descendant") String descendant) {
		LOG.debug("getShortestPathBetweenNodes");
		return entityService.getShortestPathBetweenNodes(ancestor, descendant);
	}

	@GetMapping("/public/iriExists")
	public Boolean iriExists(@RequestParam(name = "iri") String iri) {
		LOG.debug("iriExists");
		return entityService.iriExists(iri);
	}

	@PostMapping("/task")
	@PreAuthorize("isAuthenticated()")
	public TTEntity createTask(@RequestBody TTEntity entity) throws Exception {
		LOG.debug("createTask");
		return entityService.saveTask(entity);
	}

	@GetMapping("/task/action")
	@PreAuthorize("hasAuthority('IMAdmin')")
	public TTEntity addTaskAction(@RequestParam(name = "entityIri") String entityIri, @RequestParam(name = "taskIri") String taskIri) throws Exception {
		LOG.debug("addTaskAction");
		return entityService.addConceptToTask(entityIri, taskIri);
	}

	@DeleteMapping("/task/action")
	@PreAuthorize("hasAuthority('IMAdmin')")
	public TTEntity removeTaskAction(@RequestParam(name = "taskIri") String taskIri, @RequestParam(name = "removedActionIri") String removedActionIri) throws Exception {
		LOG.debug("removeTaskAction");
		return entityService.removeConceptFromTask(taskIri, removedActionIri);
	}

	@PostMapping("/mapping")
	@PreAuthorize("hasAuthority('IMAdmin')")
	public List<TTEntity> addMapping(@RequestBody Map<String, List<String>> mappings) throws Exception {
		LOG.debug("addMapping");
		return entityService.saveMapping(mappings);
	}
}
