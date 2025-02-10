package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.ValidationException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.exporters.ExcelSearchExporter;
import org.endeavourhealth.imapi.logic.exporters.SearchTextFileExporter;
import org.endeavourhealth.imapi.logic.service.*;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.ValidatedEntity;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.DownloadByQueryOptions;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.validation.EntityValidationRequest;
import org.endeavourhealth.imapi.model.validation.EntityValidationResponse;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@RestController
@RequestMapping("api/entity")
@CrossOrigin(origins = "*")
@Tag(name = "Entity Controller")
@RequestScope
public class EntityController {
  private static final Logger LOG = LoggerFactory.getLogger(EntityController.class);
  private static final String ATTACHMENT = "attachment;filename=\"";
  private static final String FORCE_DOWNLOAD = "force-download";
  private static final String APPLICATION = "application";
  private final EntityService entityService = new EntityService();
  private final GraphDtoService graphDtoService = new GraphDtoService();
  private final RequestObjectService reqObjService = new RequestObjectService();
  private final ProvService provService = new ProvService();
  private final FilerService filerService;

  public EntityController(FilerService filerService) {
    this.filerService = filerService;
  }

  @GetMapping(value = "/public/partial", produces = "application/json")
  @Operation(summary = "Get partial entity", description = "Fetches partial entity details using IRI and a set of predicates")
  public TTEntity getPartialEntity(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Partial.GET")) {
      LOG.debug("getPartialEntity");
      return entityService.getBundle(iri, predicates).getEntity();
    }
  }

  @PostMapping(value = "/public/partials")
  @Operation(summary = "Get partial entities", description = "Fetches partial details for multiple entities based on IRIs and predicates")
  public List<TTEntity> getPartialEntities(@RequestBody Map<String, Object> map) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Partials.POST")) {
      LOG.debug("getPartialEntities");
      Set<String> iris = new HashSet<>();
      Set<String> predicates = new HashSet<>();
      if (!map.get("iris").toString().isEmpty())
        iris = new HashSet<>(Arrays.asList(map.get("iris").toString().split(",")));
      if (!map.get("predicates").toString().isEmpty())
        predicates = new HashSet<>(Arrays.asList(map.get("predicates").toString().split(",")));
      return entityService.getPartialEntities(iris, predicates);
    }
  }

  @GetMapping(value = "/fullEntity", produces = "application/json")
  @Operation(summary = "Get full entity", description = "Fetches full entity details using IRI")
  public TTEntity getFullEntity(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.FullEntity.GET")) {
      LOG.debug("getFullEntity");
      return entityService.getBundleByPredicateExclusions(iri, null).getEntity();
    }
  }

  @GetMapping(value = "/public/partialBundle", produces = "application/json")
  @Operation(summary = "Get partial entity bundle", description = "Fetches a partial entity bundle by IRI and a set of predicates")
  public TTBundle getPartialEntityBundle(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PartialBundle.GET")) {
      LOG.debug("getPartialEntityBundle");
      return entityService.getBundle(iri, predicates);
    }
  }

  @GetMapping(value = "/public/children")
  @Operation(summary = "Get entity children", description = "Fetches immediate child entities of the specified entity by IRI")
  public List<EntityReferenceNode> getEntityChildren(@RequestParam(name = "iri") String iri, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Children.GET")) {
      LOG.debug("getEntityChildren");
      if (page == null && size == null) {
        page = 1;
        size = EntityService.MAX_CHILDREN;
      }
      TTEntity entity = entityService.getBundle(iri, Set.of(RDF.TYPE)).getEntity();
      boolean inactive = entity.getType() != null && entity.getType().contains(iri(IM.TASK));
      return entityService.getImmediateChildren(iri, schemeIris, page, size, inactive);
    }
  }

  @GetMapping(value = "/public/asEntityReferenceNode")
  @Operation(summary = "Get entity as reference node", description = "Fetches the specified entity as an EntityReferenceNode by IRI")
  public EntityReferenceNode getEntityAsEntityReferenceNode(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.AsEntityReferenceNode.GET")) {
      LOG.debug("getEntityAsEntityReferenceNode");
      return entityService.getEntityAsEntityReferenceNode(iri);
    }
  }

  @GetMapping(value = "/public/childrenPaged")
  @Operation(summary = "Get entity children with paging", description = "Fetches immediate children of the specified entity with pagination and total count")
  public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(@RequestParam(name = "iri") String iri, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) throws IOException {
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
  @Operation(summary = "Get partial and total count", description = "Fetches partial results and provides total count for the given entity and predicate")
  public Pageable<TTIriRef> getPartialAndTotalCount(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicate") String predicate, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PartialAndTotalCount.GET")) {
      LOG.debug("getPartialAndTotalCount");
      if (page == null && size == null) {
        page = 1;
        size = 10;
      }
      return entityService.getPartialWithTotalCount(iri, predicate, schemeIris, page, size, false);
    }
  }

  @GetMapping(value = "/public/downloadEntity")
  @Operation(summary = "Download entity", description = "Downloads the specified entity as a JSON file")
  public HttpEntity<Object> downloadEntity(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DownloadEntity.GET")) {
      LOG.debug("Download entity");
      TTBundle entity = entityService.getBundle(iri, null);
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
  public List<EntityReferenceNode> getEntityParents(@RequestParam(name = "iri") String iri, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Parents.GET")) {
      LOG.debug("getEntityParents");
      return entityService.getImmediateParents(iri, schemeIris, page, size, false);
    }
  }

  @GetMapping(value = "/public/usages")
  @Operation(summary = "Get entity usages", description = "Fetches usage details of the specified entity using IRI with pagination options")
  public List<TTEntity> entityUsages(@RequestParam(name = "iri") String iri, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Usages.GET")) {
      LOG.debug("entityUsages");
      return entityService.usages(iri, page, size);
    }
  }

  @GetMapping("/public/usagesTotalRecords")
  @Operation(summary = "Get total records for usages", description = "Fetches the total number of records for the usages of a specified entity by IRI")
  public Integer totalRecords(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.UsagesTotalRecords.GET")) {
      LOG.debug("totalRecords");
      return entityService.totalRecords(iri);
    }
  }

  @PostMapping(value = "/create")
  @PreAuthorize("hasAuthority('create')")
  @Operation(summary = "Create entity", description = "Creates a new entity in the system with the provided details")
  public TTEntity createEntity(@RequestBody TTEntity entity, HttpServletRequest request) throws TTFilerException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Create.POST")) {
      LOG.debug("createEntity");
      String agentName = reqObjService.getRequestAgentName(request);
      return filerService.createEntity(entity, agentName);
    }
  }

  @PostMapping(value = "/update")
  @PreAuthorize("hasAuthority('edit')")
  @Operation(summary = "Update entity", description = "Updates an existing entity with the provided details")
  public TTEntity updateEntity(@RequestBody TTEntity entity, HttpServletRequest request) throws TTFilerException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Update.POST")) {
      LOG.debug("updateEntity");
      String agentName = reqObjService.getRequestAgentName(request);
      return filerService.updateEntity(entity, agentName);
    }
  }

  @GetMapping("/public/summary")
  @Operation(summary = "Get entity summary", description = "Fetches a summary of the search results for the specified entity by IRI")
  public SearchResultSummary getSummary(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Summary.GET")) {
      LOG.debug("getSummary");
      return entityService.getSummary(iri);
    }
  }

  @GetMapping("/public/namespaces")
  @Operation(summary = "Get all namespaces", description = "Fetches a list of namespaces available in the system")
  public List<Namespace> getNamespaces() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Namespaces.GET")) {
      LOG.debug("getNamespaces");
      return entityService.getNamespaces();
    }
  }

  @PostMapping("/public/downloadSearchResults")
  @Operation(summary = "Download search results", description = "Downloads search results in specified format (e.g., xlsx, csv, tsv) for the given query options.")
  public HttpEntity<Object> downloadSearchResults(@RequestBody DownloadByQueryOptions downloadByQueryOptions) throws IOException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, DownloadException, QueryException, DataFormatException {
    try (MetricsTimer t = MetricsHelper.recordTime("API/Entity.DownloadSearchResults.POST")) {
      LOG.debug("downloadSearchResults");
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType(APPLICATION, FORCE_DOWNLOAD));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + "searchResults." + downloadByQueryOptions.getFormat() + "\"");

      try {
        switch (downloadByQueryOptions.getFormat()) {
          case "xlsx": {
            ExcelSearchExporter excelSearchExporter = new ExcelSearchExporter();
            XSSFWorkbook workbook = excelSearchExporter.getSearchAsExcel(downloadByQueryOptions);
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
              workbook.write(outputStream);
              workbook.close();
              return new HttpEntity<>(outputStream.toByteArray(), headers);
            } catch (IOException e) {
              throw new DownloadException("Failed to write to excel document");
            }
          }
          case "csv":
          case "tsv": {
            SearchTextFileExporter searchTextFileExporter = new SearchTextFileExporter();
            String result = searchTextFileExporter.getSearchFile(downloadByQueryOptions);
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
  public List<TTIriRef> getFolderPath(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.FolderPath.GET")) {
      LOG.debug("getFolderPath");
      return entityService.getParentPath(iri);
    }
  }

  @GetMapping("/public/shortestParentHierarchy")
  @Operation(summary = "Get shortest parent hierarchy", description = "Fetches the shortest parent hierarchy between an ancestor and a descendant by their IRIs")
  public List<TTIriRef> getShortestPathBetweenNodes(@RequestParam(name = "ancestor") String ancestor, @RequestParam(name = "descendant") String descendant) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ShortestParentHierarchy.GET")) {
      LOG.debug("getShortestPathBetweenNodes");
      return entityService.getShortestPathBetweenNodes(ancestor, descendant);
    }
  }

  @GetMapping("/public/iriExists")
  @Operation(summary = "Check if IRI exists", description = "Checks if a specified IRI exists in the system")
  public Boolean iriExists(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.IriExists.GET")) {
      LOG.debug("iriExists");
      return entityService.iriExists(iri);
    }
  }

  @GetMapping("/public/entityByPredicateExclusions")
  @Operation(summary = "Get entity by predicate exclusions", description = "Fetches an entity details using IRI, excluding specified predicates")
  public TTEntity getEntityByPredicateExclusions(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.EntityByPredicateExclusions.GET")) {
      LOG.debug("getEntityByPredicateExclusions");
      return entityService.getBundleByPredicateExclusions(iri, predicates).getEntity();
    }
  }

  @GetMapping("/public/bundleByPredicateExclusions")
  @Operation(summary = "Get bundle by predicate exclusions", description = "Fetches a bundle of entities identified by IRI, excluding specified predicates")
  public TTBundle getBundleByPredicateExclusions(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.BundleByPredicateExclusions.GET")) {
      LOG.debug("getBundleByPredicateExclusions");
      return entityService.getBundleByPredicateExclusions(iri, predicates);
    }
  }

  @GetMapping(value = "/public/predicates")
  @Operation(summary = "Get predicates of an entity", description = "Fetches the predicates associated with a specified entity IRI")
  public Set<String> getPredicates(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Predicates.GET")) {
      LOG.debug("getPredicates");
      return entityService.getPredicates(iri);
    }
  }

  @PostMapping(value = "/public/validatedEntity")
  @Operation(summary = "Get validated entities by codes", description = "Fetches a list of validated entities for the provided SNOMED codes")
  public List<ValidatedEntity> getValidatedEntitiesBySnomedCodes(@RequestBody List<String> codes) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ValidatedEntity.POST")) {
      LOG.debug("getValidatedEntitiesBySnomedCodes");
      return entityService.getValidatedEntitiesBySnomedCodes(codes);
    }
  }

  @GetMapping(value = "/public/detailsDisplay")
  @Operation(summary = "Get entity details display", description = "Fetches the detailed display information for an entity specified by its IRI")
  public TTBundle getDetailsDisplay(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DetailsDisplay.GET")) {
      LOG.debug("getDetailsDisplay");
      return entityService.getDetailsDisplay(iri);
    }
  }

  @GetMapping(value = "/public/detailsDisplay/loadMore")
  @Operation(summary = "Load more details display", description = "Fetches additional details for an entity, based on a predicate and pagination options")
  public TTBundle getDetailsDisplayLoadMore(
    @RequestParam(name = "iri") String iri,
    @RequestParam(name = "predicate") String predicate,
    @RequestParam(name = "pageIndex") int pageIndex,
    @RequestParam(name = "pageSize") int pageSize
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DetailsDisplay.LOADMORE.GET")) {
      LOG.debug("getDetailsDisplayLoadMore");
      return entityService.loadMoreDetailsDisplay(iri, predicate, pageIndex, pageSize);
    }
  }

  @PostMapping(value = "/public/validate")
  @Operation(summary = "Validate entity", description = "Validates an entity using the provided validation request details")
  public EntityValidationResponse validateEntity(@RequestBody EntityValidationRequest request) throws IOException, ValidationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.validate.POST")) {
      LOG.debug("validateEntity");
      return entityService.validate(request);
    }
  }

  @GetMapping(value = "/public/type/entities")
  @Operation(summary = "Get entities by type", description = "Fetches entities that match the specified type IRI")
  public List<TTIriRef> getEntitiesByType(@RequestParam(name = "iri") String typeIri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Predicates.GET")) {
      LOG.debug("getEntitiesByType");
      return entityService.getEntitiesByType(typeIri);
    }
  }

  @GetMapping(value = "/public/schemes")
  @Operation(summary = "Get schemes with prefixes", description = "Fetches schemes and their prefixes available in the system")
  public Map<String, Namespace> getSchemesWithPrefixes() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SchemesWithPrefixes.GET")) {
      LOG.debug("getSchemesWithPrefixes");
      return entityService.getSchemesWithPrefixes();
    }
  }

  @GetMapping(value = "/public/xmlSchemaDataTypes")
  @Operation(summary = "Get XML schema data types", description = "Fetches the XML schema data types supported by the system")
  public Set<String> getXmlSchemaDataTypes() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.XmlSchemaDataTypes.GET")) {
      LOG.debug("getXmlSchemaDataTypes");
      return entityService.getXmlSchemaDataTypes();
    }
  }

  @GetMapping(value = "/public/graph")
  @Operation(summary = "Get graph data", description = "Fetches graph data for an entity specified by its IRI")
  public GraphDto getGraphData(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Graph.Graph.GET")) {
      LOG.debug("getGraphData");
      return graphDtoService.getGraphData(iri);
    }
  }

  @GetMapping("/public/history")
  @Operation(summary = "Get provenance history", description = "Fetches the provenance history of an entity specified by its IRI")
  public List<TTEntity> getProvHistory(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Prov.History.GET")) {
      LOG.debug("getProvHistory");
      return provService.getProvHistory(iri);
    }
  }
}