package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.ValidationException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.logic.validator.EntityValidator;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.ValidatedEntity;
import org.endeavourhealth.imapi.model.dto.FilterOptionsDto;
import org.endeavourhealth.imapi.model.dto.ParentDto;
import org.endeavourhealth.imapi.model.iml.EntityExtended;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EntityValidationRequest;
import org.endeavourhealth.imapi.model.responses.EntityValidationResponse;
import org.endeavourhealth.imapi.model.search.EntityDocumentExtended;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

@Component
public class EntityService {
  public static final int MAX_CHILDREN = 200;
  private final EntityRepository entityRepository;
  private final EntityValidator validator = new EntityValidator();
  private final ObjectMapper mapper = new ObjectMapper();

  public EntityService() {
    entityRepository = new EntityRepository();
  }

  public EntityService(EntityRepository entityRepository) {
    this.entityRepository = entityRepository;
  }

  private static void filterOutSpecifiedPredicates(Set<String> excludePredicates, TTBundle bundle) {
    if (excludePredicates != null) {
      Map<String, String> filtered = bundle.getPredicates().entrySet().stream().filter(entry -> !entry.getKey().equals(RdfsVocab.LABEL.toString()) && entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      bundle.setPredicates(filtered);
      if (excludePredicates.contains(RdfsVocab.LABEL.toString())) {
        bundle.getEntity().set(EnumUtils.asIri(RdfsVocab.LABEL), (TTValue) null);
      }
    }
  }

  protected static void filterOutInactiveTermCodes(TTBundle bundle) {
    if (bundle.getEntity().get(new TTIriRefExtended(ImVocab.HAS_TERM_CODE)) != null) {
      List<TTValue> termCodes = bundle.getEntity().get(new TTIriRefExtended(ImVocab.HAS_TERM_CODE)).getElements();
      TTArray activeTermCodes = new TTArray();
      for (TTValue value : termCodes) {
        if (value.asNode().get(new TTIriRefExtended(ImVocab.HAS_STATUS)) != null) {
          if (value.asNode().get(EnumUtils.asIri(ImVocab.HAS_STATUS)) != null && ImVocab.ACTIVE.toString().equals(value.asNode().get(new TTIriRefExtended(ImVocab.HAS_STATUS)).asIriRef().getIri())) {
            activeTermCodes.add(value);
          }
        } else activeTermCodes.add(value);
      }
      bundle.getEntity().set(new TTIriRefExtended(ImVocab.HAS_TERM_CODE), activeTermCodes);
    }
  }

  public void convertRuleToLogicalJson(TTBundle bundle) throws JsonProcessingException, QueryException {
    Query query = bundle.getEntity().get(new TTIriRefExtended(ImVocab.DEFINITION)).asLiteral().objectValue(Query.class);
    new LogicOptimizer().resolveLogic(query, DisplayMode.LOGICAL);
    bundle.getEntity().set(EnumUtils.asIri(ImVocab.DEFINITION), mapper.writeValueAsString(query));
  }

  public TTBundle getBundle(String iri, Set<String> predicates) {
    return entityRepository.getBundle(iri, predicates);
  }

  public TTEntity getPartialEntity(String iri, Set<String> predicates) {
    TTBundle bundle = getBundle(iri, predicates);
    if (bundle == null) return null;
    return bundle.getEntity();
  }

  public TTBundle getBundleByPredicateExclusions(String iri, Set<String> excludePredicates) throws JsonProcessingException {
    TTBundle bundle = entityRepository.getBundle(iri, excludePredicates, true);
    filterOutSpecifiedPredicates(excludePredicates, bundle);
    filterOutInactiveTermCodes(bundle);
    return bundle;
  }

  public List<TTEntity> getPartialEntities(Set<String> iris, Set<String> predicates) {
    List<TTEntity> entities = new ArrayList<>();
    if (iris.stream().anyMatch(str -> str == null || str.isEmpty())) {
      throw new IllegalArgumentException("Iri list contains an empty or null string");
    }

    for (String iri : iris) {
      TTEntity entity = getBundle(iri, predicates).getEntity();
      entities.add(entity);
    }
    return entities;
  }

  public TTIriRefExtended getEntityReference(String iri) {
    if (iri == null || iri.isEmpty()) return null;
    return entityRepository.getEntityReferenceByIri(iri);
  }


  public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(
    String iri,
    List<String> schemeIris,
    Integer page,
    Integer size,
    boolean inactive,
    List<String> entityTypes
  ) {
    if (iri == null || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (page != null && size != null) rowNumber = (page - 1) * size;

    Pageable<TTIriRefExtended> childrenAndTotalCount = entityRepository.findImmediateChildrenPagedByIriWithTotalCount(iri, schemeIris, rowNumber, size, inactive, entityTypes);
    return iriRefPageableToEntityReferenceNodePageable(childrenAndTotalCount, schemeIris, inactive, iri);
  }


  public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    return getEntityChildrenPagedWithTotalCount(iri, schemeIris, page, size, inactive, null);
  }

  public Pageable<TTIriRefExtended> getPartialWithTotalCount(String iri, String predicateList, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (iri == null || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (page != null && size != null) rowNumber = (page - 1) * size;

    return entityRepository.findPartialWithTotalCount(iri, predicateList, schemeIris, rowNumber, size, inactive);
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

  private List<TTIriRefExtended> getParents(String iri, List<String> schemeIris, int rowNumber, Integer pageSize, boolean inactive) {

    return entityRepository.findImmediateParentsByIri(iri, schemeIris, rowNumber, pageSize, inactive);
  }

  public List<TTIriRefExtended> isWhichType(String iri, List<String> candidates) {
    if (iri == null || iri.isEmpty() || candidates == null || candidates.isEmpty()) return Collections.emptyList();
    return entityRepository.findAncestorsByType(iri, RdfsVocab.SUBCLASS_OF, candidates).stream().sorted(Comparator.comparing(TTIriRefExtended::getName)).toList();
  }

  public List<TTEntity> usages(String iri, Integer pageIndex, Integer pageSize) {
    ArrayList<TTEntity> usageEntities = new ArrayList<>();
    if (iri == null || iri.isEmpty()) return Collections.emptyList();

    Set<String> xmlDataTypes = entityRepository.getByNamespace(NamespaceVocab.XSD);
    if (xmlDataTypes != null && xmlDataTypes.contains(iri)) return Collections.emptyList();

    int rowNumber = 0;
    if (pageIndex != null && pageSize != null) rowNumber = pageIndex * pageSize;

    List<TTIriRefExtended> usageRefs = entityRepository.getConceptUsages(iri, rowNumber, pageSize).stream().sorted(Comparator.comparing(TTIriRefExtended::getName, Comparator.nullsLast(Comparator.naturalOrder()))).distinct().toList();

    usageRefs = usageRefs.stream().filter(usage -> !usage.getIri().equals(iri)).toList();
    for (TTIriRefExtended usage : usageRefs) {
      TTArray type = getBundle(usage.getIri(), Collections.singleton(RdfVocab.TYPE.toString())).getEntity().getType();
      usageEntities.add(new TTEntity().setIri(usage.getIri()).setName(usage.getName()).setType(type));
    }

    return usageEntities;
  }

  public Integer totalRecords(String iri) {
    if (iri == null || iri.isEmpty()) return 0;

    Set<String> xmlDataTypes = entityRepository.getByNamespace(NamespaceVocab.XSD);
    if (xmlDataTypes != null && xmlDataTypes.contains(iri)) return 0;

    return entityRepository.getConceptUsagesCount(iri);
  }

  public TTEntity getSummaryFromConfig(String iri, List<String> configs) {
    if (iri == null || iri.isEmpty() || configs == null || configs.isEmpty()) {
      return new TTEntity();
    }
    List<String> excludedForSummary = Arrays.asList("None", RdfsVocab.SUBCLASS_OF.toString(), "subtypes", ImVocab.IS_CHILD_OF.toString(), ImVocab.HAS_CHILDREN.toString(), "termCodes", "semanticProperties", "dataModelProperties");
    List<String> predicates = configs.stream().filter(config -> !excludedForSummary.contains(config)).toList();
    return getBundle(iri, new HashSet<>(predicates)).getEntity();
  }

  public SearchResultSummary getSummary(String iri) {
    if (iri == null || iri.isEmpty()) return null;
    return entityRepository.getEntitySummaryByIri(iri);
  }

  public TTEntity getConceptShape(String iri) {
    if (iri == null || iri.isEmpty()) return null;
    TTEntity entity = getBundle(iri, Set.of(ShaclVocab.PROPERTY.toString(), ShaclVocab.OR.toString(), RdfVocab.TYPE.toString())).getEntity();
    TTArray value = entity.get(EnumUtils.asIri(RdfVocab.TYPE));
    if (!value.getElements().contains(EnumUtils.asIri(ShaclVocab.NODESHAPE))) {
      return null;
    }
    return entity;
  }

  public TTDocument getConceptList(List<String> iris) {
    if (iris == null || iris.isEmpty()) {
      return null;
    }
    TTDocument document = new TTDocument();
    List<Namespace> namespaces = getNamespaces();
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

  public List<TTIriRefExtended> getParentPath(String iri) {
    TTEntity entity = getBundle(iri, new HashSet<>(List.of(RdfsVocab.LABEL.toString()))).getEntity();
    List<TTIriRefExtended> parents = new ArrayList<>();
    getParentPathRecursive(iri, parents);
    Collections.reverse(parents);
    parents.add(new TTIriRefExtended(iri, entity.getName()));
    return parents;
  }

  private void getParentPathRecursive(String iri, List<TTIriRefExtended> parents) {
    TTIriRefExtended parent = entityRepository.findParentFolderRef(iri);
    if (parent != null) {
      parents.add(parent);
      getParentPathRecursive(parent.getIri(), parents);
    }
  }

  public Set<TTIriRefExtended> getNames(Set<String> iris) {
    Set<TTIriRefExtended> result = iris.stream().map(TTIriRefExtended::new).collect(Collectors.toSet());
    entityRepository.getNames(result);
    return result;
  }

  public List<List<TTIriRefExtended>> getParentHierarchies(String iri) {
    ParentDto parentHierarchy = new ParentDto(iri, null, null);
    addParentHierarchiesRecursively(parentHierarchy, new HashSet<>());
    return getParentHierarchiesFlatLists(parentHierarchy);
  }

  public List<List<TTIriRefExtended>> getParentHierarchiesFlatLists(ParentDto parent) {
    List<List<TTIriRefExtended>> parentHierarchies = new ArrayList<>();
    parentHierarchies.add(new ArrayList<>());
    addParentHierarchiesRecursively(parentHierarchies, parentHierarchies.getFirst(), parent);
    return parentHierarchies;
  }

  private void addParentHierarchiesRecursively(List<List<TTIriRefExtended>> parentHierarchies, List<TTIriRefExtended> currentPath, ParentDto parent) {
    if (parent != null && parent.hasMultipleParents()) {
      parentHierarchies.remove(currentPath);
      for (ParentDto parentsParent : parent.getParents()) {
        List<TTIriRefExtended> path = new ArrayList<>(currentPath);
        path.add(new TTIriRefExtended(parentsParent.getIri(), parentsParent.getName()));
        parentHierarchies.add(path);
        addParentHierarchiesRecursively(parentHierarchies, path, parentsParent);
      }
    } else if (parent != null && parent.hasSingleParent()) {
      for (ParentDto parentsParent : parent.getParents()) {
        currentPath.add(new TTIriRefExtended(parentsParent.getIri(), parentsParent.getName()));
        addParentHierarchiesRecursively(parentHierarchies, currentPath, parentsParent);
      }
    }
  }

  private void addParentHierarchiesRecursively(ParentDto parent, Set<String> done) {
    List<ParentDto> parents = entityRepository.findParentHierarchies(parent.getIri());
    if (!parents.isEmpty()) {
      parent.setParents(parents);
      for (ParentDto parentsParent : parents) {
        if (!done.contains(parentsParent.getIri())) {
          done.add(parentsParent.getIri());
          addParentHierarchiesRecursively(parentsParent, done);
        }
      }
    }
  }

  public List<TTIriRefExtended> getShortestPathBetweenNodes(String ancestor, String descendant) {
    List<TTIriRefExtended> shortestPath = new ArrayList<>();
    List<List<TTIriRefExtended>> paths = getParentHierarchies(descendant);
    paths = paths.stream().filter(list -> indexOf(list, ancestor) != -1).collect(Collectors.toList());

    paths.sort((a1, a2) ->
      a2.size() - a1.size() // biggest to smallest
    );

    if (!paths.isEmpty()) {
      shortestPath = paths.getLast();
      int index = indexOf(shortestPath, ancestor);
      shortestPath = shortestPath.subList(0, index == shortestPath.size() ? index : index + 1);
    }
    return shortestPath;
  }

  private int indexOf(List<TTIriRefExtended> iriRefs, String iri) {
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

  public boolean iriExists(String iri) {
    return entityRepository.iriExists(iri);
  }

  public String getName(String iri) {
    return entityRepository.getEntityReferenceByIri(iri).getName();
  }

  public boolean isLinked(String subject, TTIriRefExtended predicate, String object) {
    return entityRepository.predicatePathExists(subject, predicate, object);
  }

  public EntityDocumentExtended getOSDocument(String iri) {
    return entityRepository.getOSDocument(iri);
  }

  public Set<String> getPredicates(String iri) {
    return entityRepository.getPredicates(iri);
  }

  public List<String> getIM1SchemeOptions() {
    return entityRepository.getIM1SchemeOptions();
  }

  protected Pageable<EntityReferenceNode> iriRefPageableToEntityReferenceNodePageable(Pageable<TTIriRefExtended> iriRefPageable, List<String> schemeIris, boolean inactive, String parentContext) {
    Pageable<EntityReferenceNode> result = new Pageable<>();
    result.setTotalCount(iriRefPageable.getTotalCount());

    if (result.getTotalCount() > 0) {
      Set<String> iris = new HashSet<>();
      for (TTIriRefExtended entity : iriRefPageable.getResult()) {
        iris.add(entity.getIri());
      }
      List<EntityReferenceNode> nodes = entityRepository.getEntityReferenceNodes(iris, schemeIris, inactive, parentContext);
      nodes.sort(comparingInt(EntityReferenceNode::getOrderNumber).thenComparing(EntityReferenceNode::getName));

      result.setResult(nodes);
    } else {
      result.setResult(new ArrayList<>());
    }

    return result;
  }

  public List<EntityReferenceNode> getImmediateChildren(String iri, List<String> schemeIris, Integer pageIndex, Integer pageSize, boolean inactive) {
    if (iri == null || iri.isEmpty()) return Collections.emptyList();

    List<EntityReferenceNode> result = new ArrayList<>();
    int rowNumber = 0;
    if (pageIndex != null && pageSize != null) rowNumber = (pageIndex - 1) * pageSize;

    for (TTIriRefExtended c : getChildren(iri, schemeIris, rowNumber, pageSize, inactive)) {
      result.add(getEntityAsEntityReferenceNode(c.getIri()));
    }

    result.sort(comparingInt(EntityReferenceNode::getOrderNumber).thenComparing(EntityReferenceNode::getName));

    return result;
  }

  public List<TTIriRefExtended> getChildren(String iri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
    return entityRepository.findImmediateChildrenByIri(iri, schemeIris, rowNumber, pageSize, inactive);
  }


  public EntityReferenceNode getEntityAsEntityReferenceNode(String iri) {
    return getEntityAsEntityReferenceNode(iri, null, false);
  }

  public List<EntityReferenceNode> getAsEntityReferenceNodes(List<String> iris) {
    return entityRepository.getAsEntityReferenceNodes(iris);
  }

  public EntityReferenceNode getEntityAsEntityReferenceNode(String iri, List<String> schemeIris, boolean inactive) {
    if (null == iri) throw new IllegalArgumentException("Missing iri parameter");

    return entityRepository.getEntityReferenceNode(iri, schemeIris, inactive);
  }

  public List<ValidatedEntity> getValidatedEntitiesBySnomedCodes(List<String> codes) {
    List<String> snomedCodes = codes.stream().map(code -> NamespaceVocab.SNOMED + code).toList();
    List<TTEntity> entities = getPartialEntities(new HashSet<>(snomedCodes), Set.of(RdfsVocab.LABEL.toString(), ImVocab.CODE.toString()));
    SetService setService = new SetService();
    List<TTIriRefExtended> needed = setService.getDistillation(entities.stream().map(e -> new TTIriRefExtended(e.getIri())).toList());
    List<ValidatedEntity> validatedEntities = new ArrayList<>();
    for (TTEntity entity : entities) {
      ValidatedEntity validatedEntity = validateEntity(entity, needed);
      if (validatedEntities.stream().anyMatch(v -> v.getIri().equals(validatedEntity.getIri()))) {
        validatedEntity.setCode("Duplicate");
      }
      validatedEntities.add(validateEntity(entity, needed));
    }
    return validatedEntities;
  }

  private ValidatedEntity validateEntity(TTEntity entity, List<TTIriRefExtended> needed) {
    ValidatedEntity validatedEntity = new ValidatedEntity();
    validatedEntity
      .setIri(entity.getIri())
      .setName(entity.getName())
      .setCode(entity.getCode());
    boolean isInvalid = !entity.getIri().isEmpty() && !entity.getName().isEmpty() && !entity.getCode().isEmpty();
    TTIriRefExtended found = needed.stream().filter(n -> n.getIri().equals(validatedEntity.getIri())).findFirst().orElse(null);
    if (isInvalid) {
      validatedEntity.setValidationCode("Invalid");
      validatedEntity.setValidationLabel("Not an entity");
    } else if (null != found) {
      needed.remove(found);
      validatedEntity.setValidationCode("Valid");
    } else {
      validatedEntity.setValidationCode("Child");
    }
    if (validatedEntity.getCode().isEmpty() && !validatedEntity.getIri().isEmpty() && validatedEntity.getIri().contains("#")) {
      validatedEntity.setCode(validatedEntity.getIri().split("#")[1]);
    }
    return validatedEntity;
  }

  public TTBundle getDetailsDisplay(String iri) throws JsonProcessingException {
    Set<String> excludedPredicates = EnumUtils.asHashSet(ImVocab.CODE, RdfsVocab.LABEL, ImVocab.HAS_STATUS, RdfsVocab.COMMENT);
    Set<String> entityPredicates = getPredicates(iri);
    TTBundle response;
    if (entityPredicates.contains(ImVocab.HAS_MEMBER.toString())) {
      response = getBundleByPredicateExclusions(iri, excludedPredicates);
      excludedPredicates.add(ImVocab.HAS_MEMBER.toString());
      Pageable<TTIriRefExtended> partialAndCount = getPartialWithTotalCount(iri, ImVocab.HAS_MEMBER.toString(), null, 1, 10, false);
      TTArray partialAsTTArray = new TTArray();
      for (TTIriRefExtended partial : partialAndCount.getResult()) {
        partialAsTTArray.add(partial);
      }
      TTNode loadMoreNode = new TTNode()
        .setIri(ImVocab.LOAD_MORE.toString())
        .set(new TTIriRefExtended(RdfsVocab.LABEL), "Load more")
        .set(new TTIriRefExtended(NamespaceVocab.IM + "totalCount"), partialAndCount.getTotalCount());
      partialAsTTArray.add(loadMoreNode);
      response.addPredicate(new TTIriRefExtended(ImVocab.HAS_MEMBER));
      response.getEntity().set(new TTIriRefExtended(ImVocab.HAS_MEMBER), partialAsTTArray);
    } else {
      response = getBundleByPredicateExclusions(iri, excludedPredicates);
    }
    response.getEntity().removeObject(new TTIriRefExtended(RdfVocab.TYPE));
    return response;
  }

  public TTBundle loadMoreDetailsDisplay(String iri, String predicate, int pageIndex, int pageSize) {
    Pageable<TTIriRefExtended> response = getPartialWithTotalCount(iri, predicate, null, pageIndex, pageSize, false);
    TTEntity entity = new TTEntity();
    entity.addObject(new TTIriRefExtended(predicate), response.getTotalCount());
    TTBundle bundle = new TTBundle();
    bundle.setEntity(entity);
    return bundle;
  }

  public EntityValidationResponse validate(EntityValidationRequest request) throws ValidationException {
    if (request.getValidationIri().isEmpty()) throw new IllegalArgumentException("Missing validation iri");
    return validator.validate(request, this);
  }

  public List<TTIriRefExtended> getEntitiesByType(EntityTypeVocab typeIri) {
    return entityRepository.findEntitiesByType(typeIri);
  }

  public List<Namespace> getNamespaces() {
    return entityRepository.findNamespaces();
  }


  public List<TTIriRefExtended> getIsas(String iri) {
    return entityRepository.findInvertedIsas(iri);
  }


  public List<String> getOperatorOptions(String iri) {
    return entityRepository.findOperatorOptions(iri);
  }

  public Set<String> getXmlSchemaDataTypes() {
    return entityRepository.getByNamespace(NamespaceVocab.XSD);
  }

  public List<TTEntity> getEntitiesByType(String type, Integer offset, Integer limit, String... predicates) {
    return entityRepository.getEntitiesByType(type, offset, limit, predicates);
  }

  public FilterOptionsDto getFilterOptions() {
    FilterOptionsDto filterOptions = new FilterOptionsDto();
    filterOptions.setSchemes(getAllChildren(ImVocab.ROOT_NAMESPACE));
    filterOptions.setStatus(getAllChildren(ImVocab.STATUS));
    filterOptions.setTypes(getAllChildren(ImVocab.TYPE_FILTER_OPTIONS));
    filterOptions.setSortFields(getAllChildren(ImVocab.SORT_FIELD_FILTER_OPTIONS));
    filterOptions.setSortDirections(getAllChildren(ImVocab.SORT_DIRECTION_FILTER_OPTIONS));
    return filterOptions;
  }

  public FilterOptionsDto getFilterDefaults() {
    FilterOptionsDto filterOptions = new FilterOptionsDto();
    filterOptions.setStatus(getAllChildren(ImVocab.STATUS_FILTER_DEFAULTS));
    filterOptions.setTypes(getAllChildren(ImVocab.TYPE_FILTER_DEFAULTS));
    filterOptions.setTypeSchemes(getAllTypeSchemes());
    filterOptions.setSchemes(getDefaultSchemes(filterOptions));
    filterOptions.setSortFields(getAllChildren(ImVocab.SORT_FIELD_FILTER_DEFAULTS));
    filterOptions.setSortDirections(getAllChildren(ImVocab.SORT_DIRECTION_FILTER_DEFAULTS));
    return filterOptions;
  }

  private List<TTIriRefExtended> getDefaultSchemes(FilterOptionsDto filterOptions) {
    List<TTIriRefExtended> schemes = new ArrayList<>();
    filterOptions.getTypes().forEach(type -> {
      for (TTIriRefExtended iri : filterOptions.getTypeSchemes().get(type.getIri()))
        if (!schemes.contains(iri)) schemes.add(iri);
    });
    return schemes;
  }

  private Map<String, List<TTIriRefExtended>> getAllTypeSchemes() {
    return entityRepository.getTypeSchemeDefaults();
  }

  private List<TTIriRefExtended> getAllChildren(Enum<?> iri) {
    return getChildren(EnumUtils.asIri(iri).getIri(), null, null, null, false);
  }

  public List<TTEntity> getAllowableChildTypes(String iri) {
    return entityRepository.getAllowableChildTypes(iri);
  }

  public boolean entityExists(String iri) {
    return entityRepository.hasPredicates(iri, EnumUtils.asHashSet(RdfVocab.TYPE));
  }

  public List<String> getChildIris(String iri) {
    return entityRepository.getChildIris(iri);
  }

  public List<TTBundle> getEntityFromTerm(String term, Set<String> schemes) {
    return entityRepository.getEntityFromTerm(term, schemes);
  }

  public Map<String, EntityExtended> getIriDetails(Set<String> iris) {
    return entityRepository.getIriDetails(iris);
  }
}


