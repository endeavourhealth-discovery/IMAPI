package org.endeavourhealth.imapi.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.exporters.ExcelSearchExporter;
import org.endeavourhealth.imapi.logic.exporters.SearchTextFileExporter;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.SetContent;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.*;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.set.ExportSet;
import org.endeavourhealth.imapi.model.set.SetAsObject;
import org.endeavourhealth.imapi.transforms.TTToTurtle;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;


import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/entity")
@CrossOrigin(origins = "*")
@Tag(name = "Entity Controller")
@RequestScope
public class EntityController {
    private static final Logger LOG = LoggerFactory.getLogger(EntityController.class);

    private final EntityService entityService = new EntityService();
    private final SetService setService = new SetService();
    private final ConfigManager configManager = new ConfigManager();
    private final RequestObjectService reqObjService = new RequestObjectService();

    private static final String ATTACHMENT = "attachment;filename=\"";
    private static final String FORCE_DOWNLOAD = "force-download";
    private static final String APPLICATION = "application";

    @PostMapping(value = "/public/search")
    @Operation(
            summary = "Advanced entity search",
            description = "Performs an advanced entity search with multiple filter options"
    )
    public SearchResponse advancedSearch(@RequestBody QueryRequest request) throws OpenSearchException, URISyntaxException, IOException, ExecutionException, InterruptedException, QueryException, DataFormatException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Search.POST")) {
            LOG.debug("advancedSearch");
            return entityService.advancedSearch(request);
        }
    }

    @GetMapping(value = "/public/partial", produces = "application/json")
    public TTEntity getPartialEntity(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "predicates") Set<String> predicates
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Partial.GET")) {
            LOG.debug("getPartialEntity");
            return entityService.getBundle(iri, predicates).getEntity();
        }
    }

    @GetMapping(value = "/public/partials", produces = "application/json")
    public List<TTEntity> getPartialEntities(
            @RequestParam(name = "iris") Set<String> iris,
            @RequestParam(name = "predicates") Set<String> predicates
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Partials.GET")) {
            LOG.debug("getPartialEntities");
            List<TTEntity> entities = new ArrayList<>();
            for (String iri : iris) {
                TTEntity entity = entityService.getBundle(iri, predicates).getEntity();
                entities.add(entity);
            }
            return entities;
        }
    }

    @GetMapping(value = "/fullEntity", produces = "application/json")
    public TTEntity getFullEntity(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.FullEntity.GET")) {
            LOG.debug("getFullEntity");
            return entityService.getBundleByPredicateExclusions(iri, null).getEntity();
        }
    }

    @GetMapping(value = "/public/matchedFrom", produces = "application/json")
    public Collection<SimpleMap> getMatchedFrom(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.MatchedFrom.GET")) {
            LOG.debug("getMatchedFrom");
            return entityService.getMatchedFrom(iri);
        }
    }

    @GetMapping(value = "/public/matchedTo", produces = "application/json")
    public Collection<SimpleMap> getMatchedTo(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.MatchedTo.GET")) {
            LOG.debug("getMatchedTo");
            return entityService.getMatchedTo(iri);
        }
    }

    @GetMapping(value = "/public/partialBundle", produces = "application/json")
    public TTBundle getPartialEntityBundle(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "predicates") Set<String> predicates
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PartialBundle.GET")) {
            LOG.debug("getPartialEntityBundle");
            return entityService.getBundle(iri, predicates);
        }
    }

    @GetMapping(value = "/public/entityAsPlainJson", produces = "application/json")
    public String getForm(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "depth", required = false) Integer depth
    ) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.EntityAsPlainJson.GET")) {
            LOG.debug("getEntityAsPlainJson");
            return entityService.getAsPlainJson(iri, depth == null ? 5 : depth);
        }
    }

    @GetMapping(value = "/public/inferredBundle", produces = "application/json")
    public TTBundle getInferredBundle(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.InferredBundle.GET")) {
            LOG.debug("getInferredBundle");
            return entityService.getInferredBundle(iri);
        }
    }

    @GetMapping(value = "/public/children")
    public List<EntityReferenceNode> getEntityChildren(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "schemeIris", required = false) List<String> schemeIris,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Children.GET")) {
            LOG.debug("getEntityChildren");
            if (page == null && size == null) {
                page = 1;
                size = EntityService.MAX_CHILDREN;
            }
            TTEntity entity = entityService.getBundle(iri, Set.of(RDF.TYPE)).getEntity();
            boolean inactive = entity.getType() != null && entity.getType().contains(TTIriRef.iri(IM.TASK));
            return entityService.getImmediateChildren(iri, schemeIris, page, size, inactive);
        }
    }

    @GetMapping(value = "/public/asEntityReferenceNode")
    public EntityReferenceNode getEntityAsEntityReferenceNode(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.AsEntityReferenceNode.GET")) {
            LOG.debug("getEntityAsEntityReferenceNode");
            return entityService.getEntityAsEntityReferenceNode(iri);
        }
    }

    @GetMapping(value = "/public/childrenPaged")
    public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "schemeIris", required = false) List<String> schemeIris,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ChildrenPaged.GET")) {

            LOG.debug("getEntityChildrenPagedWithTotalCount");
            if (page == null && size == null) {
                page = 1;
                size = 10;
            }
            return entityService.getEntityChildrenPagedWithTotalCount(iri, schemeIris, page, size, false);
        }
    }

    @GetMapping(value = "/public/partialAndTotalCount")
    public Pageable<TTIriRef> getPartialAndTotalCount(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "predicate") String predicate,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "schemeIris", required = false) List<String> schemeIris
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PartialAndTotalCount.GET")) {
            LOG.debug("getPartialAndTotalCount");
            if (page == null && size == null) {
                page = 1;
                size = 10;
            }
            return entityService.getPartialWithTotalCount(iri, predicate, schemeIris, page, size, false);
        }
    }

    @GetMapping("/public/exportList")
    public HttpEntity<Object> exportList(
            @RequestParam(name = "iris") List<String> iris,
            @RequestParam(name = "format") String format
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ExportList.GET")) {
            LOG.debug("exportList");
            String filename = "Concept List " + LocalDate.now();
            HttpHeaders headers = new HttpHeaders();
            TTDocument document = entityService.getConceptList(iris);
            return getObjectHttpEntity(format, filename, headers, document);
        }
    }

    @GetMapping("/public/exportGraph")
    public HttpEntity<Object> exportGraph(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "format") String format
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ExportGraph.GET")) {
            LOG.debug("exportGraph");
            TTIriRef entity = entityService.getEntityReference(iri);
            String filename = entity.getName() + " concept list " + LocalDate.now();
            HttpHeaders headers = new HttpHeaders();
            TTDocument document = entityService.getConceptListByGraph(iri);
            return getObjectHttpEntity(format, filename, headers, document);
        }
    }

    private HttpEntity<Object> getObjectHttpEntity(String format, String filename, HttpHeaders headers, TTDocument document) throws JsonProcessingException {
        String ATTACHMENT = "attachment";
        if ("turtle".equals(format)) {
            TTToTurtle ttToTurtle = new TTToTurtle();
            String turtle = ttToTurtle.transformDocument(document);
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + filename + ".txt\"");
            return new HttpEntity<>(turtle, headers);
        } else {
            try (CachedObjectMapper objectMapper = new CachedObjectMapper()) {
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
                String json = objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + filename + ".json\"");
                return new HttpEntity<>(json, headers);
            }
        }
    }

    private HttpEntity<Object> getSetHttpEntity(HttpHeaders headers, SetContent set) throws JsonProcessingException {

        try (CachedObjectMapper objectMapper = new CachedObjectMapper()) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            String json = objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(set);
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new HttpEntity<>(json, headers);
        }
    }

    @GetMapping(value = "/public/download")
    public HttpEntity<Object> download(
            @RequestParam("iri") String iri,
            @RequestParam("format") String format,
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
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Download.GET")) {
            LOG.debug("download");
            if (iri == null || iri.isEmpty() || format == null || format.isEmpty())
                return null;
            TTIriRef entity = entityService.getEntityReference(iri);
            List<ComponentLayoutItem> configs = configManager.getConfig(CONFIG.DEFINITION, new TypeReference<>() {
            });
            String filename = entity.getName() + " " + LocalDate.now();
            HttpHeaders headers = new HttpHeaders();
            DownloadParams params = new DownloadParams();
            params.setIncludeHasSubtypes(hasSubTypes).setIncludeInferred(inferred).setIncludeProperties(dataModelProperties).setIncludeMembers(members).setExpandMembers(expandMembers).setExpandSubsets(expandSubsets).setIncludeTerms(terms).setIncludeIsChildOf(isChildOf).setIncludeHasChildren(hasChildren).setIncludeInactive(inactive);
            if ("excel".equals(format)) {
                XlsHelper xls = entityService.getExcelDownload(iri, configs, params);

                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    xls.getWorkbook().write(outputStream);
                    xls.getWorkbook().close();
                    headers.setContentType(new MediaType(APPLICATION, FORCE_DOWNLOAD));
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
    }

    @GetMapping(value = "/public/parents")
    public List<EntityReferenceNode> getEntityParents(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "schemeIris", required = false) List<String> schemeIris,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Parents.GET")) {
            LOG.debug("getEntityParents");
            return entityService.getImmediateParents(iri, schemeIris, page, size, false);
        }
    }

    @GetMapping(value = "/public/usages")
    public List<TTEntity> entityUsages(@RequestParam(name = "iri") String iri,
                                       @RequestParam(name = "page", required = false) Integer page,
                                       @RequestParam(name = "size", required = false) Integer size
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Usages.GET")) {
            LOG.debug("entityUsages");
            return entityService.usages(iri, page, size);
        }
    }

    @GetMapping("/public/usagesTotalRecords")
    public Integer totalRecords(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.UsagesTotalRecords.GET")) {
            LOG.debug("totalRecords");
            return entityService.totalRecords(iri);
        }
    }

    @GetMapping(value = "/public/membersAsNode")
    public SetAsObject valueSetMembersAsNode(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "expandMembers", required = false) boolean expandMembers,
            @RequestParam(name = "expandSubsets", required = false) boolean expandSubsets,
            @RequestParam(name = "limit", required = false) Integer limit
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.MembersAsNode.GET")) {
            LOG.debug("valueSetMembersNode");
            return entityService.getValueSetMembersAsNode(iri, expandMembers, expandSubsets, limit);
        }
    }

    @GetMapping(value = "/public/referenceSuggestions")
    public List<TTIriRef> getSuggestions(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "word") String word
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ReferenceSuggestions.GET")) {
            LOG.debug("getSuggestions");
//    	TODO generate and return suggestions
            return new ArrayList<>(
                Arrays.asList(new TTIriRef(":961000252104", "method (attribute)"),
                    new TTIriRef(":1271000252102", "Hospital inpatient admission"),
                    new TTIriRef(":1911000252103", "Transfer event"))
            );
        }
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('create')")
    public TTEntity createEntity(@RequestBody TTEntity entity, HttpServletRequest request) throws TTFilerException, IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Create.POST")) {
            LOG.debug("createEntity");
            String agentName = reqObjService.getRequestAgentName(request);
            return entityService.createEntity(entity, agentName);
        }
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAuthority('edit')")
    public TTEntity updateEntity(@RequestBody TTEntity entity, HttpServletRequest request) throws TTFilerException, IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Update.POST")) {
            LOG.debug("updateEntity");
            String agentName = reqObjService.getRequestAgentName(request);
            return entityService.updateEntity(entity, agentName);
        }
    }

    @GetMapping(value = "/public/graph")
    public GraphDto getGraphData(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Graph.GET")) {
            LOG.debug("getGraphData");
            return entityService.getGraphData(iri);
        }
    }

    @GetMapping("/public/termCode")
    public List<SearchTermCode> getTermCodes(@RequestParam(name = "iri") String iri, @RequestParam(name = "includeInactive") Optional<Boolean> includeInactive) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.TermCode.GET")) {
            LOG.debug("getTermCodes");
            return entityService.getEntityTermCodes(iri, includeInactive.orElseGet(() -> false));
        }
    }

    @GetMapping("/public/dataModelProperties")
    public List<DataModelProperty> getDataModelProperties(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DataModelProperties.GET")) {
            LOG.debug("getDataModelProperties");
            return entityService.getDataModelProperties(iri);
        }
    }

    @GetMapping("/public/definition")
    public EntityDefinitionDto getEntityDefinitionDto(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Definition.GET")) {
            LOG.debug("getEntityDefinitionDto");
            return entityService.getEntityDefinitionDto(iri);
        }
    }

    @GetMapping("/public/summary")
    public SearchResultSummary getSummary(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Summary.GET")) {
            LOG.debug("getSummary");
            return entityService.getSummary(iri);
        }
    }

    @GetMapping("/public/namespaces")
    public List<Namespace> getNamespaces() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Namespaces.GET")) {
            LOG.debug("getNamespaces");
            return entityService.getNamespaces();
        }
    }

    @GetMapping("/public/setExport")
    public HttpEntity<Object> getSetExport(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "definition", defaultValue = "false") boolean definition,
            @RequestParam(name = "core", defaultValue = "false") boolean core,
            @RequestParam(name = "legacy", defaultValue = "false") boolean legacy,
            @RequestParam(name = "includeSubsets",defaultValue = "false") boolean subsets,
            @RequestParam(name = "ownRow", defaultValue = "false") boolean ownRow,
            @RequestParam(name = "im1id", defaultValue = "false") boolean im1id,
            @RequestParam(name = "format") String format,
            @RequestParam(name = "schemes", defaultValue = "") List<String> schemes
    ) throws DownloadException, IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SetExport.GET")) {
            LOG.debug("getSetExport");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType(APPLICATION, FORCE_DOWNLOAD));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + "setExport." + format + "\"");

            try {
                if ("xlsx".equals(format)) {
                    XSSFWorkbook workbook = entityService.getSetExport(iri, definition, core, legacy, subsets, ownRow, im1id, schemes);
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        workbook.write(outputStream);
                        workbook.close();
                        return new HttpEntity<>(outputStream.toByteArray(), headers);
                    } catch (IOException e) {
                        throw new DownloadException("Failed to write to excel document");
                    }
                } else if ("csv".equals(format)) {
                    String result = setService.getCSVSetExport(iri, definition, core, legacy, subsets, ownRow, im1id, schemes);
                    return new HttpEntity<>(result, headers);
                } else if ("tsv".equals(format)) {
                    String result = setService.getTSVSetExport(iri, definition, core, legacy, subsets, ownRow, im1id, schemes);
                    return new HttpEntity<>(result, headers);
                } else if ("object".equals(format)) {
                    SetContent result = setService.getSetContent(iri, definition, core, legacy, subsets, schemes);
                    return getSetHttpEntity(headers, result);
                } else {
                    return null;
                }
            } catch (IOException e) {
                throw new DownloadException("Failed to write to excel document.");
            } catch (QueryException e) {
                throw new DownloadException("Failed to get set details for download.");
            }
        }
    }

    @PostMapping("/public/downloadSearchResults")
    public HttpEntity<Object> downloadSearchResults(@RequestBody DownloadOptions downloadOptions) throws IOException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, DownloadException, QueryException, DataFormatException {
        try (MetricsTimer t = MetricsHelper.recordTime("API/Entity.DownloadSearchResults.POST")) {
            LOG.debug("downloadSearchResults");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType(APPLICATION, FORCE_DOWNLOAD));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + "searchResults." + downloadOptions.getFormat() + "\"");

            try {
                switch (downloadOptions.getFormat()) {
                    case "xlsx": {
                        ExcelSearchExporter excelSearchExporter = new ExcelSearchExporter();
                        XSSFWorkbook workbook = excelSearchExporter.getSearchAsExcel(downloadOptions);
                        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                            workbook.write(outputStream);
                            workbook.close();
                            return new HttpEntity<>(outputStream.toByteArray(), headers);
                        } catch (IOException e) {
                            throw new DownloadException("Failed to write to excel document");
                        }
                    }
                    case "csv":
                    case "tsv":
                    {
                        SearchTextFileExporter searchTextFileExporter = new SearchTextFileExporter();
                        String result = searchTextFileExporter.getSearchFile(downloadOptions);
                        return new HttpEntity<>(result, headers);
                    }
                    default:throw new DownloadException("Unhandled format: " + downloadOptions.getFormat());
                }
            } catch (IOException e) {
                throw new DownloadException("Failed to write to excel document.");
            }
        }
    }

    @GetMapping("/public/expandedMembers")
    public Set<Concept> getFullyExpandedMembers(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "legacy", required = false) boolean legacy,
            @RequestParam(name = "includeSubsets", required = false) boolean includeSubsets,
            @RequestParam(name = "schemes", required = false) List<String> schemes) throws QueryException, IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ExpandedMembers.GET")) {
            LOG.debug("getFullyExpandedMembers");
            return entityService.getFullyExpandedMembers(iri, legacy, includeSubsets, schemes);
        }
    }

    @GetMapping("/public/subsets")
    public Set<TTIriRef> getSubsets(
            @RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Subsets.GET")) {
            LOG.debug("getSubsets");
            return entityService.getSubsets(iri);
        }
    }

    @GetMapping("/public/folderPath")
    public List<TTIriRef> getFolderPath(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.FolderPath.GET")) {
            LOG.debug("getFolderPath");
            return entityService.getParentPath(iri);
        }
    }

    @GetMapping("/public/pathBetweenNodes")
    public List<TTIriRef> getPathBetweenNodes(
            @RequestParam(name = "descendant") String descendant,
            @RequestParam(name = "ancestor") String ancestor
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PathBetweenNodes.GET")) {
            LOG.debug("getPathBetweenNodes");
            return entityService.getPathBetweenNodes(descendant, ancestor);
        }
    }

    @GetMapping("/public/unassigned")
    public List<TTIriRef> getUnassigned() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Unassigned.GET")) {
            LOG.debug("getUnassigned");
            return entityService.getUnassigned();
        }
    }

    @GetMapping("/public/unclassified")
    public List<TTIriRef> getUnclassified() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Unclassified.GET")) {
            LOG.debug("getUnclassified");
            return entityService.getUnclassified();
        }
    }

    @PostMapping("/public/getNames")
    public Set<TTIriRef> getNames(@RequestBody Set<String> iris) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.GetNames.GET")) {
            LOG.debug("getNames");
            return entityService.getNames(iris);
        }
    }

    @GetMapping("/public/parentHierarchies")
    public List<List<TTIriRef>> getParentHierarchies(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ParentHierarchies.GET")) {
            LOG.debug("getParentHierarchies");
            return entityService.getParentHierarchies(iri);
        }
    }

    @GetMapping("/public/shortestParentHierarchy")
    public List<TTIriRef> getShortestPathBetweenNodes(
            @RequestParam(name = "ancestor") String ancestor,
            @RequestParam(name = "descendant") String descendant
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ShortestParentHierarchy.GET")) {
            LOG.debug("getShortestPathBetweenNodes");
            return entityService.getShortestPathBetweenNodes(ancestor, descendant);
        }
    }

    @GetMapping("/public/iriExists")
    public Boolean iriExists(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.IriExists.GET")) {
            LOG.debug("iriExists");
            return entityService.iriExists(iri);
        }
    }

    @GetMapping("/public/entityByPredicateExclusions")
    public TTEntity getEntityByPredicateExclusions(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.EntityByPredicateExclusions.GET")) {
            LOG.debug("getEntityByPredicateExclusions");
            return entityService.getBundleByPredicateExclusions(iri, predicates).getEntity();
        }
    }

    @GetMapping("/public/bundleByPredicateExclusions")
    public TTBundle getBundleByPredicateExclusions(
            @RequestParam(name = "iri") String iri,
            @RequestParam(name = "predicates") Set<String> predicates
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.BundleByPredicateExclusions.GET")) {
            LOG.debug("getBundleByPredicateExclusions");
            return entityService.getBundleByPredicateExclusions(iri, predicates);
        }
    }

    @GetMapping("/public/shapeFromType")
    public TTIriRef getShapeFromType(
            @RequestParam(name = "iri") String iri
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ShapeFromType.GET")) {
            LOG.debug("getShapeFromType");
            return entityService.getShapeFromType(iri);
        }
    }

    @GetMapping("/public/properties")
    public List<TTIriRef> getProperties() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Properties.GET")) {
            LOG.debug("getProperties");
            return entityService.getProperties();
        }
    }

    @GetMapping("/public/classes")
    public List<TTIriRef> getClasses() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Classes.GET")) {
            LOG.debug("getClasses");
            return entityService.getClasses();
        }
    }

    @GetMapping("/public/statuses")
    public List<TTIriRef> getStatuses() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Statuses.GET")) {
            LOG.debug("getStatuses");
            return entityService.getStatuses();
        }
    }

    @PostMapping(value = "public/distillation")
    public List<TTIriRef> getDistillation(@RequestBody List<TTIriRef> conceptList) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Distillation.POST")) {
            LOG.debug("getDistillation");
            return entityService.getDistillation(conceptList);
        }
    }

    @GetMapping(value = "/public/predicates")
    public Set<String> getPredicates(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Predicates.GET")) {
            LOG.debug("getPredicates");
            return entityService.getPredicates(iri);
        }
    }

    @GetMapping(value = "/public/hasChildren")
    public Boolean getHasChildren(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.HasChildren.GET")) {
            LOG.debug("getHasChildren");
            return entityService.getHasChildren(iri);
        }
    }

    @GetMapping(value = "/public/superiorPropertiesPaged")
    @Operation(
            summary = "Get top level properties for an entity as a tree node",
            description = "Finds the highest parent (superior) properties for an entity and returns then in a tree node format for use in a hierarchy tree"
    )
    public Pageable<EntityReferenceNode> getSuperiorPropertiesPaged(
            @RequestParam(name = "conceptIri") String iri,
            @RequestParam(name = "schemeIris", required = false) List<String> schemeIris,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "inactive", required = false) boolean inactive
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SuperiorPropertiesPaged.GET")) {
            LOG.debug("getSuperiorPropertiesPaged");
            if (null == page) page = 1;
            if (null == size) size = EntityService.MAX_CHILDREN;
            if (null == schemeIris) schemeIris = new ArrayList<>(Arrays.asList(IM.NAMESPACE, SNOMED.NAMESPACE));
            return entityService.getSuperiorPropertiesPaged(iri, schemeIris, page, size, inactive);
        }
    }

    @GetMapping(value = "/public/superiorPropertiesBoolFocusPaged")
    @Operation(
            summary = "Get top level properties for an entity as a tree node",
            description = "Finds the highest parent (superior) properties for an entity and returns then in a tree node format for use in a hierarchy tree"
    )
    public Pageable<EntityReferenceNode> getSuperiorPropertiesBoolFocusPaged(
            @RequestParam(name = "conceptIris") List<String> conceptIris,
            @RequestParam(name = "schemeIris", required = false) List<String> schemeIris,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "inactive", required = false) boolean inactive
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SuperiorPropertiesBoolFocusPaged.GET")) {
            LOG.debug("getSuperiorPropertiesBoolFocusPaged");
            if (null == page) page = 1;
            if (null == size) size = EntityService.MAX_CHILDREN;
            if (null == schemeIris) schemeIris = new ArrayList<>(Arrays.asList(IM.NAMESPACE, SNOMED.NAMESPACE));
            return entityService.getSuperiorPropertiesBoolFocusPaged(conceptIris, schemeIris, page, size, inactive);
        }
    }

    @GetMapping(value = "/public/superiorPropertyValuesPaged")
    @Operation(
            summary = "Get top level property values for an entity as a tree node",
            description = "Finds the highest parent (superior) property value for an entity and returns then in a tree node format for use in a hierarchy tree"
    )
    public Pageable<EntityReferenceNode> getSuperiorPropertyValuesPaged(
            @RequestParam(name = "propertyIri") String iri,
            @RequestParam(name = "schemeIris", required = false) List<String> schemeIris,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "inactive", required = false) boolean inactive
    ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SuperiorPropertyValuesPaged.GET")) {
            LOG.debug("getSuperiorPropertyValuesPaged");
            if (null == page) page = 1;
            if (null == size) size = EntityService.MAX_CHILDREN;
            if (null == schemeIris) schemeIris = new ArrayList<>(Arrays.asList(IM.NAMESPACE, SNOMED.NAMESPACE));
            return entityService.getSuperiorPropertyValuesPaged(iri, schemeIris, page, size, inactive);
        }
    }

    @GetMapping(value = "/public/isAncestor")
    @Operation(summary = "check if subject isa object")
    public Boolean isInverseIsa(@RequestParam(name = "objectIri") String objectIri, @RequestParam(name = "subjectIri") String subjectIri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.IsAncestor.GET")) {
            LOG.debug("isAncestor");
            return entityService.isAncestor(objectIri, subjectIri);
        }
    }

    @PostMapping(value="/updateSubsetsFromSuper")
    @PreAuthorize("hasAuthority('edit') or hasAuthority('create')")
    public void updateSubsetsFromSuper(@RequestBody TTEntity entity,HttpServletRequest request) throws IOException, TTFilerException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.UpdateSubsetsFromSuper.POST")) {
            LOG.debug("updateSubsetsFromSuper");
            String agentName = reqObjService.getRequestAgentName(request);
            entityService.updateSubsetsFromSuper(agentName, entity);
        }
    }

    @GetMapping(value="/public/dataModels")
    public List<TTIriRef> getDataModelsFromProperty(@RequestParam(name = "propIri") String propIri) {
        LOG.debug("getDataModelsFromProperty");
        return entityService.getDataModelsFromProperty(propIri);
    }
}
