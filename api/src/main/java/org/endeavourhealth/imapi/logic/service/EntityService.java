package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.exporters.ExcelSetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.GraphDto.GraphType;
import org.endeavourhealth.imapi.model.dto.ParentDto;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class EntityService {
  public static final int MAX_CHILDREN = 200;
  private static final Logger LOG = LoggerFactory.getLogger(EntityService.class);
  private static final FilerService filerService = new FilerService();
  private static DataModelService dataModelService = new DataModelService();
  private static ConceptService conceptService = new ConceptService();
  private static EntityRepository entityRepository = new EntityRepository();
  private ConfigManager configManager = new ConfigManager();

  private static void filterOutSpecifiedPredicates(Set<String> excludePredicates, TTBundle bundle) {
    if (excludePredicates != null) {
      Map<String, String> filtered = bundle.getPredicates().entrySet().stream().filter(entry -> !entry.getKey().equals(RDFS.LABEL) && entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      bundle.setPredicates(filtered);
      if (excludePredicates.contains(RDFS.LABEL)) {
        bundle.getEntity().set(iri(RDFS.LABEL), (TTValue) null);
      }
    }
  }

  public static TTBundle getBundle(String iri, Set<String> predicates) {
    return entityRepository.getBundle(iri, predicates);
  }

  public TTBundle getBundleByPredicateExclusions(String iri, Set<String> excludePredicates) {
    TTBundle bundle = entityRepository.getBundle(iri, excludePredicates, true);
    filterOutSpecifiedPredicates(excludePredicates, bundle);
    filterOutInactiveTermCodes(bundle);
    return bundle;
  }

  public TTIriRef getEntityReference(String iri) {
    if (iri == null || iri.isEmpty()) return null;
    return entityRepository.getEntityReferenceByIri(iri);
  }

  public static List<EntityReferenceNode> getImmediateChildren(String iri, List<String> schemeIris, Integer pageIndex, Integer pageSize, boolean inactive) {
    if (iri == null || iri.isEmpty()) return Collections.emptyList();

    List<EntityReferenceNode> result = new ArrayList<>();
    int rowNumber = 0;
    if (pageIndex != null && pageSize != null) rowNumber = (pageIndex - 1) * pageSize;

    for (TTIriRef c : getChildren(iri, schemeIris, rowNumber, pageSize, inactive)) {
      result.add(getEntityAsEntityReferenceNode(c.getIri()));
    }

    result.sort(comparingInt(EntityReferenceNode::getOrderNumber).thenComparing(EntityReferenceNode::getName));

    return result;
  }

  public static EntityReferenceNode getEntityAsEntityReferenceNode(String iri) {
    return getEntityAsEntityReferenceNode(iri, null, false);
  }

  public static EntityReferenceNode getEntityAsEntityReferenceNode(String iri, List<String> schemeIris, boolean inactive) {
    if (null == iri) throw new IllegalArgumentException("Missing iri parameter");

    return entityRepository.getEntityReferenceNode(iri, schemeIris, inactive);
  }

  public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (iri == null || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (page != null && size != null) rowNumber = (page - 1) * size;

    Pageable<TTIriRef> childrenAndTotalCount = entityRepository.findImmediateChildrenPagedByIriWithTotalCount(iri, schemeIris, rowNumber, size, inactive);
    return iriRefPageableToEntityReferenceNodePageable(childrenAndTotalCount, schemeIris, inactive);
  }

  public Pageable<TTIriRef> getPartialWithTotalCount(String iri, String predicateList, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (iri == null || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (page != null && size != null) rowNumber = (page - 1) * size;

    return entityRepository.findPartialWithTotalCount(iri, predicateList, schemeIris, rowNumber, size, inactive);
  }

  private static List<TTIriRef> getChildren(String iri, List<String> schemeIris, int rowNumber, Integer pageSize, boolean inactive) {
    return entityRepository.findImmediateChildrenByIri(iri, schemeIris, rowNumber, pageSize, inactive);
  }

  public List<EntityReferenceNode> getImmediateParents(String iri, List<String> schemeIris, Integer pageIndex, Integer pageSize, boolean inactive) {

    if (iri == null || iri.isEmpty()) return Collections.emptyList();

    int rowNumber = 0;
    if (pageIndex != null && pageSize != null) rowNumber = (pageIndex - 1) * pageSize;

    List<EntityReferenceNode> parents = getParents(iri, schemeIris, rowNumber, pageSize, inactive).stream().map(p -> new EntityReferenceNode(p.getIri(), p.getName())).toList();

    for (EntityReferenceNode parent : parents)
      parent.setType(entityRepository.getEntityTypes(parent.getIri()));

    return parents;
  }

  private List<TTIriRef> getParents(String iri, List<String> schemeIris, int rowNumber, Integer pageSize, boolean inactive) {

    return entityRepository.findImmediateParentsByIri(iri, schemeIris, rowNumber, pageSize, inactive);
  }

  public List<TTIriRef> isWhichType(String iri, List<String> candidates) {
    if (iri == null || iri.isEmpty() || candidates == null || candidates.isEmpty()) return Collections.emptyList();
    return entityRepository.findAncestorsByType(iri, RDFS.SUBCLASS_OF, candidates).stream().sorted(Comparator.comparing(TTIriRef::getName)).toList();
  }

  public List<TTEntity> usages(String iri, Integer pageIndex, Integer pageSize) throws JsonProcessingException {
    ArrayList<TTEntity> usageEntities = new ArrayList<>();
    if (iri == null || iri.isEmpty()) return Collections.emptyList();

    List<String> xmlDataTypes = configManager.getConfig(CONFIG.XML_SCHEMA_DATA_TYPES, new TypeReference<>() {
    });
    if (xmlDataTypes != null && xmlDataTypes.contains(iri)) return Collections.emptyList();

    int rowNumber = 0;
    if (pageIndex != null && pageSize != null) rowNumber = pageIndex * pageSize;

    List<TTIriRef> usageRefs = entityRepository.getConceptUsages(iri, rowNumber, pageSize).stream().sorted(Comparator.comparing(TTIriRef::getName, Comparator.nullsLast(Comparator.naturalOrder()))).distinct().toList();

    usageRefs = usageRefs.stream().filter(usage -> !usage.getIri().equals(iri)).toList();
    for (TTIriRef usage : usageRefs) {
      TTArray type = getBundle(usage.getIri(), Collections.singleton(RDF.TYPE)).getEntity().getType();
      usageEntities.add(new TTEntity().setIri(usage.getIri()).setName(usage.getName()).setType(type));
    }

    return usageEntities;
  }

  public Integer totalRecords(String iri) throws JsonProcessingException {
    if (iri == null || iri.isEmpty()) return 0;

    List<String> xmlDataTypes = configManager.getConfig(CONFIG.XML_SCHEMA_DATA_TYPES, new TypeReference<>() {
    });
    if (xmlDataTypes != null && xmlDataTypes.contains(iri)) return 0;

    return entityRepository.getConceptUsagesCount(iri);
  }

  public TTEntity getSummaryFromConfig(String iri, List<ComponentLayoutItem> configs) {
    if (iri == null || iri.isEmpty() || configs == null || configs.isEmpty()) {
      return new TTEntity();
    }
    List<String> excludedForSummary = Arrays.asList("None", RDFS.SUBCLASS_OF, "subtypes", IM.IS_CHILD_OF, IM.HAS_CHILDREN, "termCodes", "semanticProperties", "dataModelProperties");
    List<ComponentLayoutItem> filteredConfigs = configs.stream().filter(config -> !excludedForSummary.contains(config.getPredicate())).toList();
    List<String> predicates = filteredConfigs.stream().map(ComponentLayoutItem::getPredicate).toList();
    return getBundle(iri, new HashSet<>(predicates)).getEntity();
  }

  public DownloadDto getJsonDownload(String iri, List<ComponentLayoutItem> configs, DownloadEntityOptions params) {
    if (iri == null || iri.isEmpty()) return null;

    DownloadDto downloadDto = new DownloadDto();

    downloadDto.setSummary(getSummaryFromConfig(iri, configs));

    if (params.includeHasSubtypes()) {
      downloadDto.setHasSubTypes(getImmediateChildren(iri, null, null, null, params.includeInactive()));
    }
    if (params.includeInferred()) {
      downloadDto.setInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP))).getEntity());
    }
    if (params.includeProperties()) {
      downloadDto.setDataModelProperties(dataModelService.getDataModelProperties(iri));
    }
    if (params.includeTerms()) {
      downloadDto.setTerms(conceptService.getEntityTermCodes(iri, false));
    }
    if (params.includeIsChildOf()) {
      downloadDto.setIsChildOf(getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF))).getEntity().get(iri(IM.IS_CHILD_OF)));
    }
    if (params.includeHasChildren()) {
      downloadDto.setHasChildren(getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN))).getEntity().get(iri(IM.HAS_CHILDREN)));
    }

    return downloadDto;
  }

  public XlsHelper getExcelDownload(String iri, List<ComponentLayoutItem> configs, DownloadEntityOptions params) {
    if (iri == null || iri.isEmpty()) return null;

    XlsHelper xls = new XlsHelper();

    xls.addSummary(getSummaryFromConfig(iri, configs));

    if (params.includeHasSubtypes()) {
      xls.addHasSubTypes(getImmediateChildren(iri, null, null, null, params.includeInactive()));
    }
    if (params.includeInferred()) {
      xls.addInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP))));
    }
    if (params.includeProperties()) {
      xls.addDataModelProperties(dataModelService.getDataModelProperties(iri));
    }
    if (params.includeTerms()) {
      xls.addTerms(conceptService.getEntityTermCodes(iri, false));
    }
    TTEntity isChildOfEntity = getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF))).getEntity();
    TTArray isChildOfData = isChildOfEntity.get(iri(IM.IS_CHILD_OF));
    if (params.includeIsChildOf() && isChildOfData != null) {
      xls.addIsChildOf(isChildOfData.getElements());
    }
    TTEntity hasChildrenEntity = getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN))).getEntity();
    TTArray hasChildrenData = hasChildrenEntity.get(iri(IM.HAS_CHILDREN));
    if (params.includeHasChildren() && hasChildrenData != null) {
      xls.addHasChildren(hasChildrenData.getElements());
    }

    return xls;
  }


  public GraphDto getGraphData(String iri) {
    TTEntity entity = getBundle(iri, Set.of(RDFS.SUBCLASS_OF, RDFS.LABEL)).getEntity();

    GraphDto graphData = new GraphDto().setKey("0").setIri(entity.getIri()).setName(entity.getName());
    GraphDto graphParents = new GraphDto().setKey("0_0").setName("Is a");
    GraphDto graphChildren = new GraphDto().setKey("0_1").setName("Subtypes");

    TTBundle axioms = getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP)));
    List<GraphDto> axiomGraph = new ArrayList<>();

    List<GraphDto> dataModelProps = dataModelService.getDataModelProperties(iri).stream().map(prop -> new GraphDto(prop.getProperty().getIri(), prop.getProperty().getName(), prop.getType() != null ? prop.getType().getIri() : "", prop.getType() != null ? prop.getType().getName() : "", prop.getInheritedFrom() != null ? prop.getInheritedFrom().getIri() : "", prop.getInheritedFrom() != null ? prop.getInheritedFrom().getName() : "")).toList();

    List<GraphDto> isas = getEntityDefinedParents(entity);

    List<GraphDto> subtypes = getDefinitionSubTypes(iri).stream().map(subtype -> new GraphDto().setName(subtype.getName()).setIri(subtype.getIri())).toList();

    GraphDto axiom = new GraphDto().setKey("0_2").setName("Axioms");
    GraphDto axiomWrapper = getWrapper(axiomGraph, "0_2_0");
    addWrapper(axiom, axiomWrapper, "0_2_0");

    GraphDto dataModel = new GraphDto().setKey("0_3").setName("Data model properties");

    GraphDto dataModelDirect = new GraphDto().setKey("0_3_0").setName("Direct");
    GraphDto dataModelDirectWrapper = getWrapper(dataModelProps, "0_3_0_0");
    addWrapper(dataModelDirect, dataModelDirectWrapper, "0_3_0_0");

    GraphDto dataModelInherited = new GraphDto().setKey("0_3_1").setName("Inherited");
    GraphDto dataModelInheritedWrapper = getDataModelInheritedWrapper(dataModelProps);
    addWrapper(dataModelInherited, dataModelInheritedWrapper, "0_3_1_0");

    if (!dataModelDirectWrapper.getLeafNodes().isEmpty()) {
      dataModel.getChildren().add(dataModelDirect);
    }
    if (!dataModelInheritedWrapper.getLeafNodes().isEmpty()) {
      dataModel.getChildren().add(dataModelInherited);
    }

    GraphDto childrenWrapper = new GraphDto().setKey("0_1_0").setType(!subtypes.isEmpty() ? GraphType.SUBTYPE : GraphType.NONE);
    childrenWrapper.getLeafNodes().addAll(subtypes);

    GraphDto parentsWrapper = new GraphDto().setKey("0_0_0").setType(!isas.isEmpty() ? GraphType.ISA : GraphType.NONE);
    parentsWrapper.getLeafNodes().addAll(isas);

    graphParents.getChildren().add(parentsWrapper);
    graphChildren.getChildren().add(childrenWrapper);

    graphData.getChildren().add(graphParents);
    graphData.getChildren().add(graphChildren);
    if (!(axiomWrapper.getLeafNodes().isEmpty())) {
      graphData.getChildren().add(axiom);
    }
    if (!(dataModelDirectWrapper.getLeafNodes().isEmpty() && dataModelInheritedWrapper.getLeafNodes().isEmpty())) {
      graphData.getChildren().add(dataModel);
    }
    return graphData;
  }

  private GraphDto getWrapper(List<GraphDto> props, String key) {
    GraphDto wrapper = new GraphDto().setKey(key).setType(GraphType.PROPERTIES);
    wrapper.getLeafNodes().addAll(props.stream().filter(prop -> prop.getInheritedFromIri() == null).toList());
    return wrapper;
  }

  private GraphDto getDataModelInheritedWrapper(List<GraphDto> dataModelProps) {
    GraphDto dataModelInheritedWrapper = new GraphDto().setKey("0_3_1_0").setType(GraphType.PROPERTIES);
    dataModelInheritedWrapper.getLeafNodes().addAll(dataModelProps.stream().filter(prop -> prop.getInheritedFromIri() != null).toList());
    return dataModelInheritedWrapper;
  }

  private void addWrapper(GraphDto dto, GraphDto wrapper, String key) {
    dto.getChildren().add(wrapper.getLeafNodes().isEmpty() ? new GraphDto().setKey(key).setType(GraphType.NONE) : wrapper);
  }

  private List<GraphDto> getEntityDefinedParents(TTEntity entity) {
    TTArray parent = entity.get(iri(RDFS.SUBCLASS_OF));
    if (parent == null) return Collections.emptyList();
    List<GraphDto> result = new ArrayList<>();
    parent.getElements().forEach(item -> {
      if (!OWL.THING.equals(item.asIriRef().getIri()))
        result.add(new GraphDto().setIri(item.asIriRef().getIri()).setName(item.asIriRef().getName()).setPropertyType(iri(RDFS.SUBCLASS_OF).getName()));
    });

    return result;
  }

  public List<TTIriRef> getDefinitionSubTypes(String iri) {
    return entityRepository.findImmediateChildrenByIri(iri, null, null, null, false).stream().map(t -> new TTIriRef(t.getIri(), t.getName())).toList();
  }

  public SearchResultSummary getSummary(String iri) {
    if (iri == null || iri.isEmpty()) return null;
    return entityRepository.getEntitySummaryByIri(iri);
  }

  public TTEntity getConceptShape(String iri) {
    if (iri == null || iri.isEmpty()) return null;
    TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY, SHACL.OR, RDF.TYPE)).getEntity();
    TTArray value = entity.get(iri(RDF.TYPE));
    if (!value.getElements().contains(iri(SHACL.NODESHAPE))) {
      return null;
    }
    return entity;
  }

  public List<Namespace> getNamespaces() {
    return entityRepository.findNamespaces();
  }

  public TTBundle getInferredBundle(String iri) {
    Set<String> predicates = null;
    try {
      predicates = configManager.getConfig(CONFIG.INFERRED_EXCLUDE_PREDICATES, new TypeReference<>() {
      });
      predicates.add(IM.HAS_MEMBER);
    } catch (Exception e) {
      LOG.warn("Error getting inferredPredicates config, reverting to default", e);
    }

    if (predicates == null) {
      LOG.warn("Config for inferredPredicates not set, reverting to default");
      predicates = new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP, IM.HAS_MEMBER));
    }

    return getBundleByPredicateExclusions(iri, predicates);
  }

  public TTDocument getConceptList(List<String> iris) {
    if (iris == null || iris.isEmpty()) {
      return null;
    }
    TTDocument document = new TTDocument();
    List<Namespace> namespaces = entityRepository.findNamespaces();
    TTContext context = new TTContext();
    for (Namespace namespace : namespaces) {
      context.add(namespace.getIri(), namespace.getPrefix(), namespace.getName());
    }
    document.setContext(context);
    for (String iri : iris) {
      TTBundle bundle = getBundle(iri, null);
      document.addEntity(bundle.getEntity());
    }
    return document;
  }

  public List<TTIriRef> getParentPath(String iri) {
    TTEntity entity = getBundle(iri, new HashSet<>(List.of(RDFS.LABEL))).getEntity();
    List<TTIriRef> parents = new ArrayList<>();
    getParentPathRecursive(iri, parents);
    Collections.reverse(parents);
    parents.add(new TTIriRef(iri, entity.getName()));
    return parents;
  }

  private void getParentPathRecursive(String iri, List<TTIriRef> parents) {
    TTIriRef parent = entityRepository.findParentFolderRef(iri);
    if (parent != null) {
      parents.add(parent);
      getParentPathRecursive(parent.getIri(), parents);
    }
  }

  public Set<TTIriRef> getNames(Set<String> iris) {
    Set<TTIriRef> result = iris.stream().map(TTIriRef::new).collect(Collectors.toSet());
    entityRepository.getNames(result);
    return result;
  }

  public List<List<TTIriRef>> getParentHierarchies(String iri) {
    ParentDto parentHierarchy = new ParentDto(iri, null, null);
    addParentHierarchiesRecursively(parentHierarchy);
    return getParentHierarchiesFlatLists(parentHierarchy);
  }

  public List<List<TTIriRef>> getParentHierarchiesFlatLists(ParentDto parent) {
    List<List<TTIriRef>> parentHierarchies = new ArrayList<>();
    parentHierarchies.add(new ArrayList<>());
    addParentHierarchiesRecursively(parentHierarchies, parentHierarchies.get(0), parent);
    return parentHierarchies;
  }

  private void addParentHierarchiesRecursively(List<List<TTIriRef>> parentHierarchies, List<TTIriRef> currentPath, ParentDto parent) {
    if (parent != null && parent.hasMultipleParents()) {
      parentHierarchies.remove(currentPath);
      for (ParentDto parentsParent : parent.getParents()) {
        List<TTIriRef> path = new ArrayList<>(currentPath);
        path.add(new TTIriRef(parentsParent.getIri(), parentsParent.getName()));
        parentHierarchies.add(path);
        addParentHierarchiesRecursively(parentHierarchies, path, parentsParent);
      }
    } else if (parent != null && parent.hasSingleParent()) {
      for (ParentDto parentsParent : parent.getParents()) {
        currentPath.add(new TTIriRef(parentsParent.getIri(), parentsParent.getName()));
        addParentHierarchiesRecursively(parentHierarchies, currentPath, parentsParent);
      }
    }
  }

  private void addParentHierarchiesRecursively(ParentDto parent) {
    List<ParentDto> parents = entityRepository.findParentHierarchies(parent.getIri());
    if (!parents.isEmpty()) {
      parent.setParents(parents);
      for (ParentDto parentsParent : parents) {
        addParentHierarchiesRecursively(parentsParent);
      }
    }
  }

  public List<TTIriRef> getShortestPathBetweenNodes(String ancestor, String descendant) {
    List<TTIriRef> shortestPath = new ArrayList<>();
    List<List<TTIriRef>> paths = getParentHierarchies(descendant);
    paths = paths.stream().filter(list -> indexOf(list, ancestor) != -1).collect(Collectors.toList());

    paths.sort((a1, a2) -> {
      return a2.size() - a1.size(); // biggest to smallest
    });

    if (!paths.isEmpty()) {
      shortestPath = paths.get(paths.size() - 1);
      int index = indexOf(shortestPath, ancestor);
      shortestPath = shortestPath.subList(0, index == shortestPath.size() ? index : index + 1);
    }
    return shortestPath;
  }

  private int indexOf(List<TTIriRef> iriRefs, String iri) {
    boolean found = false;
    int i = 0;
    while (!found && i < iriRefs.size()) {
      if (iriRefs.get(i).getIri().equals(iri)) {
        found = true;
      } else {
        i++;
      }
    }
    return found ? i : -1;
  }

  public static boolean iriExists(String iri) {
    return entityRepository.iriExists(iri);
  }

  public static TTEntity createEntity(TTEntity entity, String agentName) throws TTFilerException, JsonProcessingException {
    isValid(entity, "Create");
    TTIriRef graph = iri(GRAPH.DISCOVERY);
    entity.setCrud(iri(IM.ADD_QUADS)).setVersion(1);
    filerService.fileEntity(entity, graph, agentName, null);
    return entity;
  }

  public static TTEntity updateEntity(TTEntity entity, String agentName) throws TTFilerException, JsonProcessingException {
    isValid(entity, "Update");
    TTIriRef graph = iri(GRAPH.DISCOVERY);
    entity.setCrud(iri(IM.UPDATE_ALL));
    TTEntity usedEntity = getBundle(entity.getIri(), null).getEntity();
    entity.setVersion(usedEntity.getVersion() + 1);
    filerService.fileEntity(entity, graph, agentName, usedEntity);
    return entity;
  }

  protected static void filterOutInactiveTermCodes(TTBundle bundle) {
    if (bundle.getEntity().get(iri(IM.HAS_TERM_CODE)) != null) {
      List<TTValue> termCodes = bundle.getEntity().get(iri(IM.HAS_TERM_CODE)).getElements();
      TTArray activeTermCodes = new TTArray();
      for (TTValue value : termCodes) {
        if (value.asNode().get(iri(IM.HAS_STATUS)) != null) {
          if (value.asNode().get(iri(IM.HAS_STATUS)) != null && "Active".equals(value.asNode().get(iri(IM.HAS_STATUS)).asIriRef().getName())) {
            activeTermCodes.add(value);
          }
        } else activeTermCodes.add(value);
      }
      bundle.getEntity().set(iri(IM.HAS_TERM_CODE), activeTermCodes);
    }
  }

  public String getName(String iri) {
    return entityRepository.getEntityReferenceByIri(iri).getName();
  }

  public boolean isLinked(String subject, TTIriRef predicate, String object) {
    return entityRepository.predicatePathExists(subject, predicate, object);
  }

  public EntityDocument getOSDocument(String iri) {
    return entityRepository.getOSDocument(iri);
  }

  public Set<String> getPredicates(String iri) {
    return entityRepository.getPredicates(iri);
  }

  protected static Pageable<EntityReferenceNode> iriRefPageableToEntityReferenceNodePageable(Pageable<TTIriRef> iriRefPageable, List<String> schemeIris, boolean inactive) {
    Pageable<EntityReferenceNode> result = new Pageable<>();
    result.setTotalCount(iriRefPageable.getTotalCount());
    Set<String> iris = new HashSet<>();
    for (TTIriRef entity : iriRefPageable.getResult()) {
      iris.add(entity.getIri());
    }
    List<EntityReferenceNode> nodes = entityRepository.getEntityReferenceNodes(iris, schemeIris, inactive);
    nodes.sort(comparingInt(EntityReferenceNode::getOrderNumber).thenComparing(EntityReferenceNode::getName));

    result.setResult(nodes);
    return result;
  }

  public List<String> getIM1SchemeOptions() {
    return entityRepository.getIM1SchemeOptions();
  }


  public static void isValid(TTEntity entity, String mode) throws TTFilerException, JsonProcessingException {
    ArrayList<String> errorMessages = new ArrayList();
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      if (Boolean.TRUE.equals(!isValidIri(entity))) errorMessages.add("Missing iri.");
      if ("Create".equals(mode) && iriExists(entity.getIri())) errorMessages.add("Iri already exists.");
      if ("Update".equals(mode) && !iriExists(entity.getIri())) errorMessages.add("Iri doesn't exists.");
      if (Boolean.TRUE.equals(!isValidName(entity))) errorMessages.add("Name is invalid.");
      if (Boolean.TRUE.equals(!isValidType(entity))) errorMessages.add("Types are invalid.");
      if (Boolean.TRUE.equals(!isValidStatus(entity))) errorMessages.add("Status is invalid");
      if (Boolean.TRUE.equals(!hasParents(entity))) errorMessages.add("Parents are invalid");
      if (!errorMessages.isEmpty()) {
        String errorsAsString = String.join(",", errorMessages);
        throw new TTFilerException(mode + " entity errors: [" + errorsAsString + "] for entity " + om.writeValueAsString(entity));
      }
    }
  }

  private static Boolean isValidIri(TTEntity entity) {
    if (null == entity.getIri()) return false;
    return !"".equals(entity.getIri());
  }

  private static Boolean isValidName(TTEntity entity) {
    if (null == entity.getName()) return false;
    return !"".equals(entity.getName());
  }

  private static Boolean isValidType(TTEntity entity) {
    if (null == entity.getType()) return false;
    if (entity.getType().isEmpty()) return false;
    return entity.getType().getElements().stream().allMatch(TTValue::isIriRef);
  }

  private static Boolean isValidStatus(TTEntity entity) {
    if (null == entity.getStatus()) return false;
    return entity.getStatus().isIriRef();
  }

  private static Boolean hasParents(TTEntity entity) {
    if (null == entity.get(iri(IM.IS_A)) && null == entity.get(iri(IM.IS_CONTAINED_IN)) && null == entity.get(iri(RDFS.SUBCLASS_OF)) && null == entity.get(iri(IM.IS_SUBSET_OF)))
      return false;
    if (null != entity.get(iri(IM.IS_A)) && !entity.get(iri(IM.IS_A)).isEmpty()) {
      if (!entity.get(iri(IM.IS_A)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
    }
    if (null != entity.get(iri(IM.IS_CONTAINED_IN)) && !entity.get(iri(IM.IS_CONTAINED_IN)).isEmpty()) {
      if (!entity.get(iri(IM.IS_CONTAINED_IN)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
    }
    if (null != entity.get(iri(RDFS.SUBCLASS_OF)) && !entity.get(iri(RDFS.SUBCLASS_OF)).isEmpty()) {
      if (!entity.get(iri(RDFS.SUBCLASS_OF)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
    }
    if (null != entity.get(iri(IM.IS_SUBSET_OF)) && !entity.get(iri(IM.IS_SUBSET_OF)).isEmpty()) {
      if (!entity.get(iri(IM.IS_SUBSET_OF)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
    }
    return true;
  }
}

