package org.endeavourhealth.imapi.logic.service;

import jakarta.xml.bind.ValidationException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.logic.validator.EntityValidator;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.ValidatedEntity;
import org.endeavourhealth.imapi.model.dto.FilterOptionsDto;
import org.endeavourhealth.imapi.model.dto.ParentDto;
import org.endeavourhealth.imapi.model.requests.EntityValidationRequest;
import org.endeavourhealth.imapi.model.responses.EntityValidationResponse;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

@Component
public class EntityService {
  public static final int MAX_CHILDREN = 200;
  private EntityRepository entityRepository = new EntityRepository();
  private EntityValidator validator = new EntityValidator();

  private static void filterOutSpecifiedPredicates(Set<String> excludePredicates, TTBundle bundle) {
    if (excludePredicates != null) {
      Map<String, String> filtered = bundle.getPredicates().entrySet().stream().filter(entry -> !entry.getKey().equals(RDFS.LABEL.toString()) && entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      bundle.setPredicates(filtered);
      if (excludePredicates.contains(RDFS.LABEL.toString())) {
        bundle.getEntity().set(RDFS.LABEL.asIri(), (TTValue) null);
      }
    }
  }

  protected static void filterOutInactiveTermCodes(TTBundle bundle) {
    if (bundle.getEntity().get(iri(IM.HAS_TERM_CODE)) != null) {
      List<TTValue> termCodes = bundle.getEntity().get(iri(IM.HAS_TERM_CODE)).getElements();
      TTArray activeTermCodes = new TTArray();
      for (TTValue value : termCodes) {
        if (value.asNode().get(iri(IM.HAS_STATUS)) != null) {
          if (value.asNode().get(IM.HAS_STATUS.asIri()) != null && IM.ACTIVE.toString().equals(value.asNode().get(iri(IM.HAS_STATUS)).asIriRef().getIri())) {
            activeTermCodes.add(value);
          }
        } else activeTermCodes.add(value);
      }
      bundle.getEntity().set(iri(IM.HAS_TERM_CODE), activeTermCodes);
    }
  }

  public TTBundle getBundle(String iri, Set<String> predicates) {
    return entityRepository.getBundle(iri, predicates);
  }

  public TTBundle getBundleByPredicateExclusions(String iri, Set<String> excludePredicates, Graph graph) {
    TTBundle bundle = entityRepository.getBundle(iri, excludePredicates, true, graph);
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

  public TTIriRef getEntityReference(String iri, Graph graph) {
    if (iri == null || iri.isEmpty()) return null;
    return entityRepository.getEntityReferenceByIri(iri, graph);
  }


  public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(String iri, List<String> schemeIris, Integer page,
                                                                            Integer size, boolean inactive,
                                                                            List<String> entityTypes, Graph graph) {
    if (iri == null || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (page != null && size != null) rowNumber = (page - 1) * size;

    Pageable<TTIriRef> childrenAndTotalCount = entityRepository.findImmediateChildrenPagedByIriWithTotalCount(iri, schemeIris, rowNumber, size, inactive, entityTypes, graph);
    return iriRefPageableToEntityReferenceNodePageable(childrenAndTotalCount, schemeIris, inactive, graph);
  }


  public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive, Graph graph) {
    return getEntityChildrenPagedWithTotalCount(iri, schemeIris, page, size, inactive, null, graph);
  }

  public Pageable<TTIriRef> getPartialWithTotalCount(String iri, String predicateList, List<String> schemeIris, Integer page, Integer size, boolean inactive, Graph graph) {
    if (iri == null || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (page != null && size != null) rowNumber = (page - 1) * size;

    return entityRepository.findPartialWithTotalCount(iri, predicateList, schemeIris, rowNumber, size, inactive, graph);
  }


  public List<EntityReferenceNode> getImmediateParents(String iri, List<String> schemeIris, Integer pageIndex, Integer pageSize, boolean inactive, Graph graph) {

    if (iri == null || iri.isEmpty()) return Collections.emptyList();

    int rowNumber = 0;
    if (pageIndex != null && pageSize != null) rowNumber = (pageIndex - 1) * pageSize;

    List<EntityReferenceNode> parents = getParents(iri, schemeIris, rowNumber, pageSize, inactive, graph).stream().map(p -> new EntityReferenceNode(p.getIri(), p.getName())).toList();

    for (EntityReferenceNode parent : parents)
      parent.setType(entityRepository.getEntityTypes(parent.getIri(), graph));

    return parents;
  }

  private List<TTIriRef> getParents(String iri, List<String> schemeIris, int rowNumber, Integer pageSize, boolean inactive, Graph graph) {

    return entityRepository.findImmediateParentsByIri(iri, schemeIris, rowNumber, pageSize, inactive, graph);
  }

  public List<TTIriRef> isWhichType(String iri, List<String> candidates, Graph graph) {
    if (iri == null || iri.isEmpty() || candidates == null || candidates.isEmpty()) return Collections.emptyList();
    return entityRepository.findAncestorsByType(iri, RDFS.SUBCLASS_OF, candidates, graph).stream().sorted(Comparator.comparing(TTIriRef::getName)).toList();
  }

  public List<TTEntity> usages(String iri, Integer pageIndex, Integer pageSize, Graph graph) {
    ArrayList<TTEntity> usageEntities = new ArrayList<>();
    if (iri == null || iri.isEmpty()) return Collections.emptyList();

    Set<String> xmlDataTypes = entityRepository.getByNamespace(org.endeavourhealth.imapi.vocabulary.Namespace.XSD, Graph.IM);
    if (xmlDataTypes != null && xmlDataTypes.contains(iri)) return Collections.emptyList();

    int rowNumber = 0;
    if (pageIndex != null && pageSize != null) rowNumber = pageIndex * pageSize;

    List<TTIriRef> usageRefs = entityRepository.getConceptUsages(iri, rowNumber, pageSize, graph).stream().sorted(Comparator.comparing(TTIriRef::getName, Comparator.nullsLast(Comparator.naturalOrder()))).distinct().toList();

    usageRefs = usageRefs.stream().filter(usage -> !usage.getIri().equals(iri)).toList();
    for (TTIriRef usage : usageRefs) {
      TTArray type = getBundle(usage.getIri(), Collections.singleton(RDF.TYPE.toString())).getEntity().getType();
      usageEntities.add(new TTEntity().setIri(usage.getIri()).setName(usage.getName()).setType(type));
    }

    return usageEntities;
  }

  public Integer totalRecords(String iri, Graph graph) {
    if (iri == null || iri.isEmpty()) return 0;

    Set<String> xmlDataTypes = entityRepository.getByNamespace(org.endeavourhealth.imapi.vocabulary.Namespace.XSD, Graph.IM);
    if (xmlDataTypes != null && xmlDataTypes.contains(iri)) return 0;

    return entityRepository.getConceptUsagesCount(iri, graph);
  }

  public TTEntity getSummaryFromConfig(String iri, List<String> configs) {
    if (iri == null || iri.isEmpty() || configs == null || configs.isEmpty()) {
      return new TTEntity();
    }
    List<String> excludedForSummary = Arrays.asList("None", RDFS.SUBCLASS_OF.toString(), "subtypes", IM.IS_CHILD_OF.toString(), IM.HAS_CHILDREN.toString(), "termCodes", "semanticProperties", "dataModelProperties");
    List<String> predicates = configs.stream().filter(config -> !excludedForSummary.contains(config)).toList();
    return getBundle(iri, new HashSet<>(predicates)).getEntity();
  }

  public SearchResultSummary getSummary(String iri, Graph graph) {
    if (iri == null || iri.isEmpty()) return null;
    return entityRepository.getEntitySummaryByIri(iri, graph);
  }

  public TTEntity getConceptShape(String iri) {
    if (iri == null || iri.isEmpty()) return null;
    TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY.toString(), SHACL.OR.toString(), RDF.TYPE.toString())).getEntity();
    TTArray value = entity.get(RDF.TYPE.asIri());
    if (!value.getElements().contains(SHACL.NODESHAPE.asIri())) {
      return null;
    }
    return entity;
  }

  public TTDocument getConceptList(List<String> iris, Graph graph) {
    if (iris == null || iris.isEmpty()) {
      return null;
    }
    TTDocument document = new TTDocument();
    List<Namespace> namespaces = getNamespaces(graph);
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

  public List<TTIriRef> getParentPath(String iri, Graph graph) {
    TTEntity entity = getBundle(iri, new HashSet<>(List.of(RDFS.LABEL.toString()))).getEntity();
    List<TTIriRef> parents = new ArrayList<>();
    getParentPathRecursive(iri, parents, graph);
    Collections.reverse(parents);
    parents.add(new TTIriRef(iri, entity.getName()));
    return parents;
  }

  private void getParentPathRecursive(String iri, List<TTIriRef> parents, Graph graph) {
    TTIriRef parent = entityRepository.findParentFolderRef(iri, graph);
    if (parent != null) {
      parents.add(parent);
      getParentPathRecursive(parent.getIri(), parents, graph);
    }
  }

  public Set<TTIriRef> getNames(Set<String> iris, Graph graph) {
    Set<TTIriRef> result = iris.stream().map(TTIriRef::new).collect(Collectors.toSet());
    entityRepository.getNames(result, graph);
    return result;
  }

  public List<List<TTIriRef>> getParentHierarchies(String iri, Graph graph) {
    ParentDto parentHierarchy = new ParentDto(iri, null, null);
    addParentHierarchiesRecursively(parentHierarchy, graph);
    return getParentHierarchiesFlatLists(parentHierarchy);
  }

  public List<List<TTIriRef>> getParentHierarchiesFlatLists(ParentDto parent) {
    List<List<TTIriRef>> parentHierarchies = new ArrayList<>();
    parentHierarchies.add(new ArrayList<>());
    addParentHierarchiesRecursively(parentHierarchies, parentHierarchies.getFirst(), parent);
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

  private void addParentHierarchiesRecursively(ParentDto parent, Graph graph) {
    List<ParentDto> parents = entityRepository.findParentHierarchies(parent.getIri(), graph);
    if (!parents.isEmpty()) {
      parent.setParents(parents);
      for (ParentDto parentsParent : parents) {
        addParentHierarchiesRecursively(parentsParent, graph);
      }
    }
  }

  public List<TTIriRef> getShortestPathBetweenNodes(String ancestor, String descendant, Graph graph) {
    List<TTIriRef> shortestPath = new ArrayList<>();
    List<List<TTIriRef>> paths = getParentHierarchies(descendant, graph);
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

  public boolean iriExists(String iri, Graph graph) {
    return entityRepository.iriExists(iri, graph);
  }

  public String getName(String iri, Graph graph) {
    return entityRepository.getEntityReferenceByIri(iri, graph).getName();
  }

  public boolean isLinked(String subject, TTIriRef predicate, String object, Graph graph) {
    return entityRepository.predicatePathExists(subject, predicate, object, graph);
  }

  public EntityDocument getOSDocument(String iri, Graph graph) {
    return entityRepository.getOSDocument(iri, graph);
  }

  public Set<String> getPredicates(String iri, Graph graph) {
    return entityRepository.getPredicates(iri, graph);
  }

  public List<String> getIM1SchemeOptions(Graph graph) {
    return entityRepository.getIM1SchemeOptions(graph);
  }

  protected Pageable<EntityReferenceNode> iriRefPageableToEntityReferenceNodePageable(Pageable<TTIriRef> iriRefPageable, List<String> schemeIris, boolean inactive, Graph graph) {
    Pageable<EntityReferenceNode> result = new Pageable<>();
    result.setTotalCount(iriRefPageable.getTotalCount());

    if (result.getTotalCount() > 0) {
      Set<String> iris = new HashSet<>();
      for (TTIriRef entity : iriRefPageable.getResult()) {
        iris.add(entity.getIri());
      }
      List<EntityReferenceNode> nodes = entityRepository.getEntityReferenceNodes(iris, schemeIris, inactive, graph);
      nodes.sort(comparingInt(EntityReferenceNode::getOrderNumber).thenComparing(EntityReferenceNode::getName));

      result.setResult(nodes);
    } else {
      result.setResult(new ArrayList<>());
    }

    return result;
  }

  public List<EntityReferenceNode> getImmediateChildren(String iri, List<String> schemeIris, Integer pageIndex, Integer pageSize, boolean inactive, Graph graph) {
    if (iri == null || iri.isEmpty()) return Collections.emptyList();

    List<EntityReferenceNode> result = new ArrayList<>();
    int rowNumber = 0;
    if (pageIndex != null && pageSize != null) rowNumber = (pageIndex - 1) * pageSize;

    for (TTIriRef c : getChildren(iri, schemeIris, rowNumber, pageSize, inactive, graph)) {
      result.add(getEntityAsEntityReferenceNode(c.getIri(), graph));
    }

    result.sort(comparingInt(EntityReferenceNode::getOrderNumber).thenComparing(EntityReferenceNode::getName));

    return result;
  }

  public List<TTIriRef> getChildren(String iri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive, Graph graph) {
    return entityRepository.findImmediateChildrenByIri(iri, schemeIris, rowNumber, pageSize, inactive, graph);
  }


  public EntityReferenceNode getEntityAsEntityReferenceNode(String iri, Graph graph) {
    return getEntityAsEntityReferenceNode(iri, null, false, graph);
  }

  public List<EntityReferenceNode> getAsEntityReferenceNodes(Set<String> iris, Graph graph) {
    return entityRepository.getAsEntityReferenceNodes(iris, graph);
  }

  public EntityReferenceNode getEntityAsEntityReferenceNode(String iri, List<String> schemeIris, boolean inactive, Graph graph) {
    if (null == iri) throw new IllegalArgumentException("Missing iri parameter");

    return entityRepository.getEntityReferenceNode(iri, schemeIris, inactive, graph);
  }

  public List<ValidatedEntity> getValidatedEntitiesBySnomedCodes(List<String> codes, Graph graph) {
    List<String> snomedCodes = codes.stream().map(code -> org.endeavourhealth.imapi.vocabulary.Namespace.SNOMED + code).toList();
    List<TTEntity> entities = getPartialEntities(new HashSet<>(snomedCodes), Set.of(RDFS.LABEL.toString(), IM.CODE.toString()));
    SetService setService = new SetService();
    List<TTIriRef> needed = setService.getDistillation(entities.stream().map(e -> iri(e.getIri())).toList(), graph);
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

  private ValidatedEntity validateEntity(TTEntity entity, List<TTIriRef> needed) {
    ValidatedEntity validatedEntity = new ValidatedEntity();
    validatedEntity
      .setIri(entity.getIri())
      .setName(entity.getName())
      .setCode(entity.getCode());
    boolean isInvalid = !entity.getIri().isEmpty() && !entity.getName().isEmpty() && !entity.getCode().isEmpty();
    TTIriRef found = needed.stream().filter(n -> n.getIri().equals(validatedEntity.getIri())).findFirst().orElse(null);
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

  public TTBundle getDetailsDisplay(String iri, Graph graph) {
    Set<String> excludedPredicates = asHashSet(IM.CODE, RDFS.LABEL, IM.HAS_STATUS, RDFS.COMMENT);
    Set<String> entityPredicates = getPredicates(iri, graph);
    TTBundle response;
    if (entityPredicates.contains(IM.HAS_MEMBER.toString())) {
      response = getBundleByPredicateExclusions(iri, excludedPredicates, null);
      excludedPredicates.add(IM.HAS_MEMBER.toString());
      Pageable<TTIriRef> partialAndCount = getPartialWithTotalCount(iri, IM.HAS_MEMBER.toString(), null, 1, 10, false, graph);
      TTArray partialAsTTArray = new TTArray();
      for (TTIriRef partial : partialAndCount.getResult()) {
        partialAsTTArray.add(partial);
      }
      TTNode loadMoreNode = new TTNode()
        .setIri(IM.LOAD_MORE.toString())
        .set(iri(RDFS.LABEL), "Load more")
        .set(iri(org.endeavourhealth.imapi.vocabulary.Namespace.IM + "totalCount"), partialAndCount.getTotalCount());
      partialAsTTArray.add(loadMoreNode);
      response.addPredicate(iri(IM.HAS_MEMBER));
      response.getEntity().set(iri(IM.HAS_MEMBER), partialAsTTArray);
    } else {
      response = getBundleByPredicateExclusions(iri, excludedPredicates, null);
    }
    response.getEntity().removeObject(iri(RDF.TYPE));
    return response;
  }

  public TTBundle loadMoreDetailsDisplay(String iri, String predicate, int pageIndex, int pageSize, Graph graph) {
    Pageable<TTIriRef> response = getPartialWithTotalCount(iri, predicate, null, pageIndex, pageSize, false, graph);
    TTEntity entity = new TTEntity();
    entity.addObject(iri(predicate), response.getTotalCount());
    TTBundle bundle = new TTBundle();
    bundle.setEntity(entity);
    return bundle;
  }

  public EntityValidationResponse validate(EntityValidationRequest request) throws ValidationException {
    if (request.getValidationIri().isEmpty()) throw new IllegalArgumentException("Missing validation iri");
    return validator.validate(request, this);
  }

  public List<TTIriRef> getEntitiesByType(EntityType typeIri, Graph graph) {
    return entityRepository.findEntitiesByType(typeIri, graph);
  }

  public List<Namespace> getNamespaces(Graph graph) {
    return entityRepository.findNamespaces(graph);
  }


  public List<TTIriRef> getIsas(String iri, Graph graph) {
    return entityRepository.findInvertedIsas(iri, graph);
  }


  public List<String> getOperatorOptions(String iri, Graph graph) {
    return entityRepository.findOperatorOptions(iri, graph);
  }

  public Set<String> getXmlSchemaDataTypes() {
    return entityRepository.getByNamespace(org.endeavourhealth.imapi.vocabulary.Namespace.XSD, Graph.IM);
  }

  public List<TTEntity> getEntitiesByType(String type, Integer offset, Integer limit, Graph graph, String... predicates) {
    return entityRepository.getEntitiesByType(type, offset, limit, graph, predicates);
  }

  public FilterOptionsDto getFilterOptions() {
    FilterOptionsDto filterOptions = new FilterOptionsDto();
    filterOptions.setSchemes(getAllChildren(IM.NAMESPACE, Graph.IM));
    filterOptions.setStatus(getAllChildren(IM.STATUS, Graph.IM));
    filterOptions.setTypes(getAllChildren(IM.TYPE_FILTER_OPTIONS, Graph.IM));
    filterOptions.setSortFields(getAllChildren(IM.SORT_FIELD_FILTER_OPTIONS, Graph.IM));
    filterOptions.setSortDirections(getAllChildren(IM.SORT_DIRECTION_FILTER_OPTIONS, Graph.IM));
    return filterOptions;
  }

  public FilterOptionsDto getFilterDefaults() {
    FilterOptionsDto filterOptions = new FilterOptionsDto();
    filterOptions.setSchemes(getAllChildren(IM.SCHEME_FILTER_DEFAULTS, Graph.IM));
    filterOptions.setStatus(getAllChildren(IM.STATUS_FILTER_DEFAULTS, Graph.IM));
    filterOptions.setTypes(getAllChildren(IM.TYPE_FILTER_DEFAULTS, Graph.IM));
    filterOptions.setSortFields(getAllChildren(IM.SORT_FIELD_FILTER_DEFAULTS, Graph.IM));
    filterOptions.setSortDirections(getAllChildren(IM.SORT_DIRECTION_FILTER_DEFAULTS, Graph.IM));
    return filterOptions;
  }

  private List<TTIriRef> getAllChildren(IM iri, Graph graph) {
    return getChildren(iri.toString(), null, null, null, false, graph);
  }

  public List<TTEntity> getAllowableChildTypes(String iri, Graph graph) {
    return entityRepository.getAllowableChildTypes(iri, graph);
  }

  public boolean checkEntityExists(String iri, Graph graph) {
    return entityRepository.hasPredicates(iri, asHashSet(RDF.TYPE), graph);
  }
}


