package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.ValidationException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.exporters.ExcelSearchExporter;
import org.endeavourhealth.imapi.logic.exporters.SearchTextFileExporter;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.FilerService;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
  private final RequestObjectService reqObjService = new RequestObjectService();
  private final FilerService filerService;

  public EntityController(FilerService filerService) {
    this.filerService = filerService;
  }

  @GetMapping(value = "/public/partial", produces = "application/json")
  public TTEntity getPartialEntity(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Partial.GET")) {
      LOG.debug("getPartialEntity");
      return entityService.getBundle(iri, predicates).getEntity();
    }
  }

  @GetMapping(value = "/public/partials", produces = "application/json")
  public List<TTEntity> getPartialEntities(@RequestParam(name = "iris") Set<String> iris, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Partials.GET")) {
      LOG.debug("getPartialEntities");
      return entityService.getPartialEntities(iris, predicates);
    }
  }


  @GetMapping(value = "/fullEntity", produces = "application/json")
  public TTEntity getFullEntity(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.FullEntity.GET")) {
      LOG.debug("getFullEntity");
      return entityService.getBundleByPredicateExclusions(iri, null).getEntity();
    }
  }

  @GetMapping(value = "/public/partialBundle", produces = "application/json")
  public TTBundle getPartialEntityBundle(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PartialBundle.GET")) {
      LOG.debug("getPartialEntityBundle");
      return entityService.getBundle(iri, predicates);
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
  public EntityReferenceNode getEntityAsEntityReferenceNode(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.AsEntityReferenceNode.GET")) {
      LOG.debug("getEntityAsEntityReferenceNode");
      return entityService.getEntityAsEntityReferenceNode(iri);
    }
  }

  @GetMapping(value = "/public/childrenPaged")
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
  public List<EntityReferenceNode> getEntityParents(@RequestParam(name = "iri") String iri, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Parents.GET")) {
      LOG.debug("getEntityParents");
      return entityService.getImmediateParents(iri, schemeIris, page, size, false);
    }
  }

  @GetMapping(value = "/public/usages")
  public List<TTEntity> entityUsages(@RequestParam(name = "iri") String iri, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) throws IOException {
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

  @PostMapping(value = "/create")
  @PreAuthorize("hasAuthority('create')")
  public TTEntity createEntity(@RequestBody TTEntity entity, HttpServletRequest request) throws TTFilerException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Create.POST")) {
      LOG.debug("createEntity");
      String agentName = reqObjService.getRequestAgentName(request);
      return filerService.createEntity(entity, agentName);
    }
  }

  @PostMapping(value = "/update")
  @PreAuthorize("hasAuthority('edit')")
  public TTEntity updateEntity(@RequestBody TTEntity entity, HttpServletRequest request) throws TTFilerException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Update.POST")) {
      LOG.debug("updateEntity");
      String agentName = reqObjService.getRequestAgentName(request);
      return filerService.updateEntity(entity, agentName);
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

  @PostMapping("/public/downloadSearchResults")
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
  public List<TTIriRef> getFolderPath(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.FolderPath.GET")) {
      LOG.debug("getFolderPath");
      return entityService.getParentPath(iri);
    }
  }

  @PostMapping("/public/getNames")
  public Set<TTIriRef> getNames(@RequestBody Set<String> iris) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.GetNames.GET")) {
      LOG.debug("getNames");
      return entityService.getNames(iris);
    }
  }

  @GetMapping("/public/shortestParentHierarchy")
  public List<TTIriRef> getShortestPathBetweenNodes(@RequestParam(name = "ancestor") String ancestor, @RequestParam(name = "descendant") String descendant) throws IOException {
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
  public TTEntity getEntityByPredicateExclusions(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.EntityByPredicateExclusions.GET")) {
      LOG.debug("getEntityByPredicateExclusions");
      return entityService.getBundleByPredicateExclusions(iri, predicates).getEntity();
    }
  }

  @GetMapping("/public/bundleByPredicateExclusions")
  public TTBundle getBundleByPredicateExclusions(@RequestParam(name = "iri") String iri, @RequestParam(name = "predicates") Set<String> predicates) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.BundleByPredicateExclusions.GET")) {
      LOG.debug("getBundleByPredicateExclusions");
      return entityService.getBundleByPredicateExclusions(iri, predicates);
    }
  }

  @GetMapping(value = "/public/predicates")
  public Set<String> getPredicates(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Predicates.GET")) {
      LOG.debug("getPredicates");
      return entityService.getPredicates(iri);
    }
  }

  @PostMapping(value = "/public/validatedEntity")
  public List<ValidatedEntity> getValidatedEntitiesBySnomedCodes(@RequestBody List<String> codes) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ValidatedEntity.POST")) {
      LOG.debug("getValidatedEntitiesBySnomedCodes");
      return entityService.getValidatedEntitiesBySnomedCodes(codes);
    }
  }

  @GetMapping(value = "/public/detailsDisplay")
  public TTBundle getDetailsDisplay(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DetailsDisplay.GET")) {
      LOG.debug("getDetailsDisplay");
      return entityService.getDetailsDisplay(iri);
    }
  }

  @GetMapping(value = "/public/detailsDisplay/loadMore")
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

  @GetMapping(value = "/public/propertiesDisplay")
  public List<PropertyDisplay> getPropertiesDisplay(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PropertiesDisplay.GET")) {
      LOG.debug("getPropertiesDisplay");
      return entityService.getPropertiesDisplay(iri);
    }
  }

  @PostMapping(value = "/public/validate")
  public EntityValidationResponse validateEntity(@RequestBody EntityValidationRequest request) throws IOException, ValidationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.validate.POST")) {
      LOG.debug("validateEntity");
      return entityService.validate(request);
    }
  }
}
