package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.logic.exporters.ExcelSearchExporter;
import org.endeavourhealth.imapi.logic.exporters.SearchTextFileExporter;
import org.endeavourhealth.imapi.logic.service.*;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.ValidatedEntity;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.dto.FilterOptionsDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EditRequest;
import org.endeavourhealth.imapi.model.requests.EntityValidationRequest;
import org.endeavourhealth.imapi.model.requests.ValidatedEntitiesRequest;
import org.endeavourhealth.imapi.model.responses.EntityValidationResponse;
import org.endeavourhealth.imapi.model.search.DownloadByQueryOptions;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

@RestController
@RequestMapping("api/entity")
@CrossOrigin(origins = "*")
@Tag(name = "Entity Controller")
@RequestScope
@Slf4j
public class EntityController {
  private static final String ATTACHMENT = "attachment;filename=\"";
  private static final String FORCE_DOWNLOAD = "force-download";
  private static final String APPLICATION = "application";
  private final EntityService entityService = new EntityService();
  private final GraphDtoService graphDtoService = new GraphDtoService();
  private final RequestObjectService requestObjectService = new RequestObjectService();
  private final ProvService provService = new ProvService();
  private final FilerService filerService;

  public EntityController(FilerService filerService) {
    this.filerService = filerService;
  }

  @GetMapping(value = "/public/partial", produces = "application/json")
  @Operation(summary = "Get partial entity", description = "Fetches partial entity details using IRI and a set of predicates")
  public TTEntity getPartialEntity(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Partial.GET")) {
      log.debug("getPartialEntity");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getBundle(iri, predicates, graphs).getEntity();
    }
  }

  @PostMapping(value = "/public/partials")
  @Operation(summary = "Get partial entities", description = "Fetches partial details for multiple entities based on IRIs and predicates")
  public List<TTEntity> getPartialEntities(HttpServletRequest request, @RequestBody Map<String, Object> map) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Partials.POST")) {
      log.debug("getPartialEntities");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      Set<String> iris = new HashSet<>();
      Set<String> predicates = new HashSet<>();
      if (!map.get("iris").toString().isEmpty())
        iris = new HashSet<>(Arrays.asList(map.get("iris").toString().split(",")));
      if (!map.get("predicates").toString().isEmpty())
        predicates = new HashSet<>(Arrays.asList(map.get("predicates").toString().split(",")));
      return entityService.getPartialEntities(iris, predicates, graphs);
    }
  }

  @GetMapping(value = "/fullEntity", produces = "application/json")
  @Operation(summary = "Get full entity", description = "Fetches full entity details using IRI")
  public TTEntity getFullEntity(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.FullEntity.GET")) {
      log.debug("getFullEntity");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getBundleByPredicateExclusions(iri, null, graphs).getEntity();
    }
  }

  @GetMapping(value = "/entityTypes", produces = "application/json")
  @Operation(summary = "Get entity type", description = "Fetches entity types using IRI")
  public Set<String> getEntityType(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.FullEntity.GET")) {
      log.debug("getEntityTypes");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getBundle(iri, asHashSet(RDF.TYPE), graphs).getEntity()
        .getType().getElements().stream().map(e -> e.asIriRef().getIri()).collect(Collectors.toSet());
    }
  }


  @GetMapping(value = "/public/partialBundle", produces = "application/json")
  @Operation(summary = "Get partial entity bundle", description = "Fetches a partial entity bundle by IRI and a set of predicates")
  public TTBundle getPartialEntityBundle(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PartialBundle.GET")) {
      log.debug("getPartialEntityBundle");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getBundle(iri, predicates, graphs);
    }
  }

  @GetMapping(value = "/public/children")
  @Operation(summary = "Get entity children", description = "Fetches immediate child entities of the specified entity by IRI")
  public List<EntityReferenceNode> getEntityChildren(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "graph", required = false) Graph graph) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Children.GET")) {
      log.debug("getEntityChildren");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      if (page == null && size == null) {
        page = 1;
        size = EntityService.MAX_CHILDREN;
      }
      TTEntity entity = entityService.getBundle(iri, asHashSet(RDF.TYPE), graphs).getEntity();
      boolean inactive = entity.getType() != null && entity.getType().contains(iri(IM.TASK));

      return entityService.getImmediateChildren(iri, schemeIris, page, size, inactive, graphs);
    }
  }

  @GetMapping(value = "/public/asEntityReferenceNode")
  @Operation(summary = "Get entity as reference node", description = "Fetches the specified entity as an EntityReferenceNode by IRI")
  public EntityReferenceNode getEntityAsEntityReferenceNode(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "graph", defaultValue = "http://endhealth.info/im#") String graph) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.AsEntityReferenceNode.GET")) {
      log.debug("getEntityAsEntityReferenceNode");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getEntityAsEntityReferenceNode(iri, graphs);
    }
  }

  @GetMapping(value = "/public/asEntityReferenceNodes")
  @Operation(summary = "Get entity as reference node", description = "Fetches the specified entity iris as an EntityReferenceNode by IRI")
  public List<EntityReferenceNode> getAsEntityReferenceNodes(HttpServletRequest request, @RequestParam(name = "iris") List<String> iris) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.AsEntityReferenceNodes.GET")) {
      log.debug("getAsEntityReferenceNodes");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getAsEntityReferenceNodes(iris, graphs);
    }
  }

  @GetMapping(value = "/public/childrenPaged")
  @Operation(summary = "Get entity children with paging", description = "Fetches immediate children of the specified entity with pagination and total count")
  public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(
    HttpServletRequest request,
    @RequestParam(name = "iri") String iri,
    @RequestParam(name = "schemeIris", required = false) List<String> schemeIris,
    @RequestParam(name = "page", required = false) Integer page,
    @RequestParam(name = "size", required = false) Integer size,
    @RequestParam(name = "typeFilter", required = false) List<String> typeFilter
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ChildrenPaged.GET")) {
      log.debug("getEntityChildrenPagedWithTotalCount" + ((typeFilter != null) ? "entity types= " + typeFilter : ""));
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      if (page == null && size == null) {
        page = 1;
        size = 10;
      }
      return entityService.getEntityChildrenPagedWithTotalCount(iri, schemeIris, page, size, false, typeFilter, graphs);
    }
  }

  @GetMapping(value = "/public/partialAndTotalCount")
  @Operation(summary = "Get partial and total count", description = "Fetches partial results and provides total count for the given entity and predicate")
  public Pageable<TTIriRef> getPartialAndTotalCount(
    HttpServletRequest request,
    @RequestParam(name = "iri") String iri,
    @RequestParam(name = "predicate") String predicate,
    @RequestParam(name = "page", required = false) Integer page,
    @RequestParam(name = "size", required = false) Integer size,
    @RequestParam(name = "schemeIris", required = false) List<String> schemeIris
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PartialAndTotalCount.GET")) {
      log.debug("getPartialAndTotalCount");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      if (page == null && size == null) {
        page = 1;
        size = 10;
      }
      return entityService.getPartialWithTotalCount(iri, predicate, schemeIris, page, size, false, graphs);
    }
  }

  @GetMapping(value = "/public/downloadEntity")
  @Operation(summary = "Download entity", description = "Downloads the specified entity as a JSON file")
  public HttpEntity<Object> downloadEntity(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DownloadEntity.GET")) {
      log.debug("Download entity");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      TTBundle entity = entityService.getBundle(iri, null, graphs);
      TTManager manager = new TTManager();
      TTDocument document = manager.createDocument();
      List<TTEntity> entityList = new ArrayList<>();
      entityList.add(entity.getEntity());
      document.setEntities(entityList);
      String json = manager.getJson(document);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + entity.getEntity().get(iri(RDFS.LABEL)) + ".json\"");
      return new HttpEntity<>(json, headers);
    }
  }

  @GetMapping(value = "/public/parents")
  @Operation(summary = "Get entity parents", description = "Fetches immediate parent entities of the specified entity by IRI")
  public List<EntityReferenceNode> getEntityParents(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Parents.GET")) {
      log.debug("getEntityParents");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getImmediateParents(iri, schemeIris, page, size, false, graphs);
    }
  }

  @GetMapping(value = "/public/usages")
  @Operation(summary = "Get entity usages", description = "Fetches usage details of the specified entity using IRI with pagination options")
  public List<TTEntity> entityUsages(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Usages.GET")) {
      log.debug("entityUsages");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.usages(iri, page, size, graphs);
    }
  }

  @GetMapping("/public/usagesTotalRecords")
  @Operation(summary = "Get total records for usages", description = "Fetches the total number of records for the usages of a specified entity by IRI")
  public Integer totalRecords(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.UsagesTotalRecords.GET")) {
      log.debug("totalRecords");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.totalRecords(iri, graphs);
    }
  }

  @PostMapping(value = "/create")
  @PreAuthorize("hasAuthority('CREATOR')")
  @Operation(summary = "Create entity", description = "Creates a new entity in the system with the provided details")
  public TTEntity createEntity(@RequestBody EditRequest editRequest, HttpServletRequest request) throws TTFilerException, IOException, UserNotFoundException, TaskFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Create.POST")) {
      log.debug("createEntity");
      String agentName = requestObjectService.getRequestAgentName(request);
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return filerService.createEntity(editRequest, agentName, graphs, editRequest.getGraph());
    }
  }


  @GetMapping(value = "/checkExists")
  @Operation(summary = "Check entity exists", description = "Checks whether an entity exists. ")
  public boolean checkExists(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws TTFilerException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Exists.POST")) {
      log.debug("checkEntityExists");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.checkEntityExists(iri, graphs);
    }
  }

  @PostMapping(value = "/update")
  @PreAuthorize("hasAuthority('EDITOR')")
  @Operation(summary = "Update entity", description = "Updates an existing entity with the provided details")
  public TTEntity updateEntity(HttpServletRequest request, @RequestBody EditRequest editRequest) throws TTFilerException, IOException, UserNotFoundException, TaskFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Update.POST")) {
      log.debug("updateEntity");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      String agentName = requestObjectService.getRequestAgentName(request);
      return filerService.updateEntity(editRequest.getEntity(), agentName, graphs, editRequest.getGraph());
    }
  }

//  @PostMapping(value = "/createDraft")
//  @PreAuthorize("hasAuthority('EDITOR')")
//  @Operation(summary = "Save draft entity", description = "Add an entity to the users draft graph to await approval process")
//  public TTEntity saveDraft(@RequestBody EditRequest editRequest, HttpServletRequest request) throws TTFilerException, IOException, UserNotFoundException, TaskFilerException {
//    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.draft.POST")) {
//      log.debug("saveDraft");
//      String agentName = requestObjectService.getRequestAgentName(request);
//      String userGraph = requestObjectService.getUserDraftGraph(request);
//      return filerService.createEntity(editRequest, agentName, Graph.from(userGraph));
//    }
//  }

//  @GetMapping(value = "/draft")
//  @Operation(summary = "get draft entity", description = "Get a users draft entity by entity iri")
//  public TTEntity getDraft(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws TTFilerException, IOException, UserNotFoundException, TaskFilerException {
//    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.draft.GET")) {
//      log.debug("getDraft");
//      String userGraph = requestObjectService.getUserDraftGraph(request);
//      return entityService.getBundleByPredicateExclusions(iri, null, Graph.from(userGraph)).getEntity();
//    }
//  }

  @GetMapping("/public/summary")
  @Operation(summary = "Get entity summary", description = "Fetches a summary of the search results for the specified entity by IRI")
  public SearchResultSummary getSummary(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "graph", defaultValue = "http://endhealth.info/im#") String graph) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Summary.GET")) {
      log.debug("getSummary");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getSummary(iri, graphs);
    }
  }

  @PostMapping("/public/downloadSearchResults")
  @Operation(summary = "Download search results", description = "Downloads search results in specified format (e.g., xlsx, csv, tsv) for the given query options.")
  public HttpEntity<Object> downloadSearchResults(
    HttpServletRequest request,
    @RequestBody DownloadByQueryOptions downloadByQueryOptions
  ) throws IOException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, DownloadException, QueryException, DataFormatException {
    try (MetricsTimer t = MetricsHelper.recordTime("API/Entity.DownloadSearchResults.POST")) {
      log.debug("downloadSearchResults");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType(APPLICATION, FORCE_DOWNLOAD));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + "searchResults." + downloadByQueryOptions.getFormat() + "\"");

      try {
        switch (downloadByQueryOptions.getFormat()) {
          case "xlsx": {
            ExcelSearchExporter excelSearchExporter = new ExcelSearchExporter();
            XSSFWorkbook workbook = excelSearchExporter.getSearchAsExcel(downloadByQueryOptions, graphs);
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
              workbook.write(outputStream);
              workbook.close();
              return new HttpEntity<>(outputStream.toByteArray(), headers);
            } catch (IOException e) {
              throw new DownloadException("Failed to write to excel document");
            }
          }
          case "csv", "tsv": {
            SearchTextFileExporter searchTextFileExporter = new SearchTextFileExporter();
            String result = searchTextFileExporter.getSearchFile(downloadByQueryOptions, graphs);
            return new HttpEntity<>(result, headers);
          }
          default:
            throw new DownloadException("Unhandled format: " + downloadByQueryOptions.getFormat());
        }
      } catch (IOException e) {
        throw new DownloadException("Failed to write to excel document.");
      }
    }
  }

  @GetMapping("/public/folderPath")
  @Operation(summary = "Get folder path", description = "Fetches the folder path of an entity specified by its IRI")
  public List<TTIriRef> getFolderPath(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.FolderPath.GET")) {
      log.debug("getFolderPath");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getParentPath(iri, graphs);
    }
  }

  @GetMapping("/public/shortestParentHierarchy")
  @Operation(summary = "Get shortest parent hierarchy", description = "Fetches the shortest parent hierarchy between an ancestor and a descendant by their IRIs")
  public List<TTIriRef> getShortestPathBetweenNodes(HttpServletRequest request, @RequestParam(name = "ancestor") String ancestor, @RequestParam(name = "descendant") String descendant) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ShortestParentHierarchy.GET")) {
      log.debug("getShortestPathBetweenNodes");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getShortestPathBetweenNodes(ancestor, descendant, graphs);
    }
  }

  @GetMapping("/public/iriExists")
  @Operation(summary = "Check if IRI exists", description = "Checks if a specified IRI exists in the system")
  public Boolean iriExists(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.IriExists.GET")) {
      log.debug("iriExists");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.iriExists(iri, graphs);
    }
  }

  @GetMapping("/public/entityByPredicateExclusions")
  @Operation(summary = "Get entity by predicate exclusions", description = "Fetches an entity details using IRI, excluding specified predicates")
  public TTEntity getEntityByPredicateExclusions(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.EntityByPredicateExclusions.GET")) {
      log.debug("getEntityByPredicateExclusions");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getBundleByPredicateExclusions(iri, predicates, graphs).getEntity();
    }
  }

  @GetMapping("/public/bundleByPredicateExclusions")
  @Operation(summary = "Get bundle by predicate exclusions", description = "Fetches a bundle of entities identified by IRI, excluding specified predicates")
  public TTBundle getBundleByPredicateExclusions(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates, @RequestParam(name = "graph", defaultValue = "http://endhealth.info/im#") String graph) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.BundleByPredicateExclusions.GET")) {
      log.debug("getBundleByPredicateExclusions");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getBundleByPredicateExclusions(iri, predicates, graphs);
    }
  }

  @GetMapping(value = "/public/predicates")
  @Operation(summary = "Get predicates of an entity", description = "Fetches the predicates associated with a specified entity IRI")
  public Set<String> getPredicates(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "graph", defaultValue = "http://endhealth.info/im#") String graph) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Predicates.GET")) {
      log.debug("getPredicates");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getPredicates(iri, graphs);
    }
  }

  @PostMapping(value = "/public/validatedEntity")
  @Operation(summary = "Get validated entities by codes", description = "Fetches a list of validated entities for the provided SNOMED codes")
  public List<ValidatedEntity> getValidatedEntitiesBySnomedCodes(HttpServletRequest request, @RequestBody ValidatedEntitiesRequest validatedEntitiesRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ValidatedEntity.POST")) {
      log.debug("getValidatedEntitiesBySnomedCodes");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getValidatedEntitiesBySnomedCodes(validatedEntitiesRequest.getSnomedCodes(), graphs);
    }
  }

  @GetMapping(value = "/public/detailsDisplay")
  @Operation(summary = "Get entity details display", description = "Fetches the detailed display information for an entity specified by its IRI")
  public TTBundle getDetailsDisplay(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DetailsDisplay.GET")) {
      log.debug("getDetailsDisplay");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getDetailsDisplay(iri, graphs);
    }
  }

  @GetMapping(value = "/public/detailsDisplay/loadMore")
  @Operation(summary = "Load more details display", description = "Fetches additional details for an entity, based on a predicate and pagination options")
  public TTBundle getDetailsDisplayLoadMore(
    HttpServletRequest request,
    @RequestParam(name = "iri") String iri,
    @RequestParam(name = "predicate") String predicate,
    @RequestParam(name = "pageIndex") int pageIndex,
    @RequestParam(name = "pageSize") int pageSize
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DetailsDisplay.LOADMORE.GET")) {
      log.debug("getDetailsDisplayLoadMore");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.loadMoreDetailsDisplay(iri, predicate, pageIndex, pageSize, graphs);
    }
  }

  @PostMapping(value = "/public/validate")
  @Operation(summary = "Validate entity", description = "Validates an entity using the provided validation request details")
  public EntityValidationResponse validateEntity(
    HttpServletRequest request,
    @RequestBody EntityValidationRequest entityValidationRequest
  ) throws IOException, ValidationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.validate.POST")) {
      log.debug("validateEntity");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.validate(entityValidationRequest, graphs);
    }
  }

  @GetMapping(value = "/public/type/entities")
  @Operation(summary = "Get entities by type", description = "Fetches entities that match the specified type IRI")
  public List<TTIriRef> getEntitiesByType(HttpServletRequest request, @RequestParam(name = "iri") String typeIri, @RequestParam(name = "graph", defaultValue = "http://endhealth.info/im#") String graph) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Predicates.GET")) {
      log.debug("getEntitiesByType");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getEntitiesByType(EntityType.from(typeIri), graphs);
    }
  }

  @GetMapping(value = "/public/schemes")
  @Operation(summary = "Get schemes", description = "Fetches schemes and their prefixes available in the system")
  public Map<String, Namespace> getNamespacesWithPrefixes(HttpServletRequest request) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Namespaces.GET")) {
      log.debug("getNamespacesWithPrefixes (getSchemes)");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      List<Namespace> namespaces = entityService.getNamespaces(graphs);
      Map<String, Namespace> result = new HashMap<>();
      namespaces.forEach(namespace -> result.put(namespace.getIri(), namespace));
      return result;
    }
  }

  @GetMapping(value = "/public/namespaces")
  @Operation(summary = "Get schemes", description = "Fetches schemes and their prefixes available in the system")
  public List<Namespace> getNamespaces(HttpServletRequest request) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Namespaces.GET")) {
      log.debug("getNamespaces");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getNamespaces(graphs);
    }
  }

  @GetMapping(value = "/public/xmlSchemaDataTypes")
  @Operation(summary = "Get XML schema data types", description = "Fetches the XML schema data types supported by the system")
  public Set<String> getXmlSchemaDataTypes(HttpServletRequest request) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.XmlSchemaDataTypes.GET")) {
      log.debug("getXmlSchemaDataTypes");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getXmlSchemaDataTypes(graphs);
    }
  }

  @GetMapping(value = "/public/graph")
  @Operation(summary = "Get graph data", description = "Fetches graph data for an entity specified by its IRI")
  public GraphDto getGraphData(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Graph.Graph.GET")) {
      log.debug("getGraphData");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return graphDtoService.getGraphData(iri, graphs);
    }
  }

  @GetMapping("/public/history")
  @Operation(summary = "Get provenance history", description = "Fetches the provenance history of an entity specified by its IRI")
  public List<TTEntity> getProvHistory(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Prov.History.GET")) {
      log.debug("getProvHistory");
      return provService.getProvHistory(iri);
    }
  }

  @GetMapping("/public/filterOptions")
  public FilterOptionsDto getFilterOptions() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.History.GET")) {
      log.debug("getFilterOptions");
      return entityService.getFilterOptions();
    }
  }

  @GetMapping("/public/filterDefaults")
  public FilterOptionsDto getFilterDefaults() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.History.GET")) {
      log.debug("getFilterDefaults");
      return entityService.getFilterDefaults();
    }
  }

  @GetMapping("/public/allowableChildTypes")
  @Operation(summary = "Get allowable child types", description = "Fetches the allowable child types for an entity and the predicate that links them")
  public List<TTEntity> getAllowableChildTypes(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.AllowableChildTypes.GET")) {
      log.debug("get AllowableChildTypes for " + iri);
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getAllowableChildTypes(iri, graphs);
    }
  }


  @GetMapping(value = "/public/childIris")
  @Operation(summary = "Get entity children not paged", description = "Fetches immediate child iris of the specified entity by IRI")
  public List<String> getChildEntities(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Children.GET")) {
      log.debug("getChildIris");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return entityService.getChildIris(iri, graphs);
    }
  }


}