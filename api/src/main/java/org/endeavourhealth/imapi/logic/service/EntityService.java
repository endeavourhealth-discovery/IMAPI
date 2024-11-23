package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.xml.bind.ValidationException;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.logic.validator.EntityValidator;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.dto.ParentDto;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.validation.EntityValidationRequest;
import org.endeavourhealth.imapi.model.validation.EntityValidationResponse;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class EntityService {
  public static final int MAX_CHILDREN = 200;
  private static final Logger LOG = LoggerFactory.getLogger(EntityService.class);
  private EntityRepository entityRepository = new EntityRepository();
  private ConfigManager configManager = new ConfigManager();
  private EntityValidator validator = new EntityValidator();

  private static void filterOutSpecifiedPredicates(Set<String> excludePredicates, TTBundle bundle) {
    if (excludePredicates != null) {
      Map<String, String> filtered = bundle.getPredicates().entrySet().stream().filter(entry -> !entry.getKey().equals(RDFS.LABEL) && entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      bundle.setPredicates(filtered);
      if (excludePredicates.contains(RDFS.LABEL)) {
        bundle.getEntity().set(iri(RDFS.LABEL), (TTValue) null);
      }
    }
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

  public TTBundle getBundle(String iri, Set<String> predicates) {
    return entityRepository.getBundle(iri, predicates);
  }

  public TTBundle getBundleByPredicateExclusions(String iri, Set<String> excludePredicates) {
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

  public TTIriRef getEntityReference(String iri) {
    if (iri == null || iri.isEmpty()) return null;
    return entityRepository.getEntityReferenceByIri(iri);
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

    Set<String> xmlDataTypes = entityRepository.getByGraph(XSD.NAMESPACE);
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

    Set<String> xmlDataTypes = entityRepository.getByGraph(XSD.NAMESPACE);
    if (xmlDataTypes != null && xmlDataTypes.contains(iri)) return 0;

    return entityRepository.getConceptUsagesCount(iri);
  }

  public TTEntity getSummaryFromConfig(String iri, List<String> configs) {
    if (iri == null || iri.isEmpty() || configs == null || configs.isEmpty()) {
      return new TTEntity();
    }
    List<String> excludedForSummary = Arrays.asList("None", RDFS.SUBCLASS_OF, "subtypes", IM.IS_CHILD_OF, IM.HAS_CHILDREN, "termCodes", "semanticProperties", "dataModelProperties");
    List<String> predicates = configs.stream().filter(config -> !excludedForSummary.contains(config)).toList();
    return getBundle(iri, new HashSet<>(predicates)).getEntity();
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
      List<TTIriRef> inferredExcludePredicates = getChildren(IM.INFERRED_EXCLUDE_PREDICATES, null, 0, 0, false);
      predicates = inferredExcludePredicates.stream().map(TTIriRef::getIri).collect(Collectors.toSet());
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

  public boolean iriExists(String iri) {
    return entityRepository.iriExists(iri);
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

  public List<String> getIM1SchemeOptions() {
    return entityRepository.getIM1SchemeOptions();
  }

  protected Pageable<EntityReferenceNode> iriRefPageableToEntityReferenceNodePageable(Pageable<TTIriRef> iriRefPageable, List<String> schemeIris, boolean inactive) {
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

  public List<EntityReferenceNode> getImmediateChildren(String iri, List<String> schemeIris, Integer pageIndex, Integer pageSize, boolean inactive) {
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

  public List<TTIriRef> getChildren(String iri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
    return entityRepository.findImmediateChildrenByIri(iri, schemeIris, rowNumber, pageSize, inactive);
  }


  public EntityReferenceNode getEntityAsEntityReferenceNode(String iri) {
    return getEntityAsEntityReferenceNode(iri, null, false);
  }

  public EntityReferenceNode getEntityAsEntityReferenceNode(String iri, List<String> schemeIris, boolean inactive) {
    if (null == iri) throw new IllegalArgumentException("Missing iri parameter");

    return entityRepository.getEntityReferenceNode(iri, schemeIris, inactive);
  }

  public List<ValidatedEntity> getValidatedEntitiesBySnomedCodes(List<String> codes) {
    List<String> snomedCodes = codes.stream().map(code -> SNOMED.NAMESPACE + code).toList();
    List<TTEntity> entities = getPartialEntities(new HashSet<>(snomedCodes), Stream.of(RDFS.LABEL, IM.CODE).collect(Collectors.toSet()));
    SetService setService = new SetService();
    List<TTIriRef> needed = setService.getDistillation(entities.stream().map(e -> iri(e.getIri())).toList());
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

  public TTBundle getDetailsDisplay(String iri) {
    Set<String> excludedPredicates = new HashSet<>(List.of(IM.CODE, RDFS.LABEL, IM.HAS_STATUS, RDFS.COMMENT));
    Set<String> entityPredicates = getPredicates(iri);
    TTBundle response;
    if (entityPredicates.contains(IM.HAS_MEMBER)) {
      response = getBundleByPredicateExclusions(iri, excludedPredicates);
      excludedPredicates.add(IM.HAS_MEMBER);
      Pageable<TTIriRef> partialAndCount = getPartialWithTotalCount(iri, IM.HAS_MEMBER, null, 1, 10, false);
      TTArray partialAsTTArray = new TTArray();
      for (TTIriRef partial : partialAndCount.getResult()) {
        partialAsTTArray.add(partial);
      }
      TTNode loadMoreNode = new TTNode()
        .setIri(IM.LOAD_MORE)
        .set(iri(RDFS.LABEL), "Load more")
        .set(iri(IM.NAMESPACE + "totalCount"), partialAndCount.getTotalCount());
      partialAsTTArray.add(loadMoreNode);
      response.addPredicate(iri(IM.HAS_MEMBER));
      response.getEntity().set(iri(IM.HAS_MEMBER), partialAsTTArray);
    } else {
      response = getBundleByPredicateExclusions(iri, excludedPredicates);
    }
    response.getEntity().removeObject(iri(RDF.TYPE));
    return response;
  }

  public TTBundle loadMoreDetailsDisplay(String iri, String predicate, int pageIndex, int pageSize) {
    Pageable<TTIriRef> response = getPartialWithTotalCount(iri, predicate, null, pageIndex, pageSize, false);
    TTEntity entity = new TTEntity();
    entity.addObject(iri(predicate), response.getTotalCount());
    TTBundle bundle = new TTBundle();
    bundle.setEntity(entity);
    return bundle;
  }

  public List<PropertyDisplay> getPropertiesDisplay(String iri) {
    Set<String> predicates = new HashSet<>();
    predicates.add(SHACL.PROPERTY);
    TTEntity entity = getBundle(iri, predicates).getEntity();
    List<PropertyDisplay> propertyList = new ArrayList<>();
    TTArray ttProperties = entity.get(iri(SHACL.PROPERTY));
    if (null != ttProperties) {
      for (TTValue ttProperty : ttProperties.getElements()) {
        int minCount = 0;
        if (ttProperty.asNode().has(iri(SHACL.MINCOUNT))) {
          minCount = ttProperty.asNode().get(iri(SHACL.MINCOUNT)).asLiteral().intValue();
        }
        int maxCount = 0;
        if (ttProperty.asNode().has(iri(SHACL.MAXCOUNT))) {
          maxCount = ttProperty.asNode().get(iri(SHACL.MAXCOUNT)).asLiteral().intValue();
        }
        String cardinality = minCount + " : " + (maxCount == 0 ? "*" : maxCount);
        if (ttProperty.asNode().has(iri(SHACL.OR))) {
          handleOr(ttProperty, cardinality, propertyList);
        } else {
          handleNotOr(ttProperty, cardinality, propertyList);
        }
      }
    }
    return propertyList;
  }

  private void handleOr(TTValue ttProperty, String cardinality, List<PropertyDisplay> propertyList) {
    PropertyDisplay propertyDisplay = new PropertyDisplay();
    propertyDisplay.setOrder(ttProperty.asNode().get(iri(SHACL.ORDER)).asLiteral().intValue());
    propertyDisplay.setCardinality(cardinality);
    propertyDisplay.setOr(true);
    for (TTValue orProperty : ttProperty.asNode().get(iri(SHACL.OR)).getElements()) {
      TTArray type;
      if (orProperty.asNode().has(iri(SHACL.CLASS))) type = orProperty.asNode().get(iri(SHACL.CLASS));
      else if (orProperty.asNode().has(iri(SHACL.NODE))) type = orProperty.asNode().get(iri(SHACL.NODE));
      else if (orProperty.asNode().has(iri(SHACL.DATATYPE))) type = orProperty.asNode().get(iri(SHACL.DATATYPE));
      else type = new TTArray();
      String name = "";
      if (orProperty.asNode().has(iri(SHACL.PATH))) {
        name += orProperty.asNode().get(iri(SHACL.PATH)).get(0).asIriRef().getIri() + " (";
        if (!type.isEmpty() && !type.get(0).asIriRef().getName().isEmpty()) name += type.get(0).asIriRef().getName();
        else if (!type.isEmpty() && !type.get(0).asIriRef().getIri().isEmpty())
          name += " (" + type.get(0).asIriRef().getIri().split("#")[1];
        name += ")";
        propertyDisplay.addProperty(iri(orProperty.asNode().get(iri(SHACL.PATH)).get(0).asIriRef().getIri(), name));
        propertyDisplay.addType(type.get(0).asIriRef());
      }
      propertyList.add(propertyDisplay);
    }
  }

  private void handleNotOr(TTValue ttProperty, String cardinality, List<PropertyDisplay> propertyList) {
    TTArray type;
    if (ttProperty.asNode().has(iri(SHACL.CLASS))) type = ttProperty.asNode().get(iri(SHACL.CLASS));
    else if (ttProperty.asNode().has(iri(SHACL.NODE))) type = ttProperty.asNode().get(iri(SHACL.NODE));
    else if (ttProperty.asNode().has(iri(SHACL.DATATYPE))) type = ttProperty.asNode().get(iri(SHACL.DATATYPE));
    else type = new TTArray();
    TTValue group = null;
    if (ttProperty.asNode().has(iri(SHACL.GROUP))) {
      group = ttProperty.asNode().get(iri(SHACL.GROUP)).get(0);
    }
    String name = "";
    if (ttProperty.asNode().has(iri(SHACL.PATH))) {
      name += ttProperty.asNode().get(iri(SHACL.PATH)).get(0).asIriRef().getName() + " (";
      if (!type.isEmpty() && !type.get(0).asIriRef().getName().isEmpty()) name += type.get(0).asIriRef().getName();
      else if (!type.isEmpty() && !type.get(0).asIriRef().getIri().isEmpty()) name += type.get(0).asIriRef().getIri();
      name += ")";
    }
    PropertyDisplay propertyDisplay = new PropertyDisplay();
    propertyDisplay.setOrder(ttProperty.asNode().get(iri(SHACL.ORDER)).asLiteral().intValue());
    propertyDisplay.addProperty(iri(ttProperty.asNode().get(iri(SHACL.PATH)).get(0).asIriRef().getIri(), name));
    propertyDisplay.addType(type.get(0).asIriRef());
    propertyDisplay.setCardinality(cardinality);
    propertyDisplay.setOr(false);
    if (null != group) propertyDisplay.setGroup(group.asIriRef());
    propertyList.add(propertyDisplay);
  }

  public EntityValidationResponse validate(EntityValidationRequest request) throws ValidationException {
    if (request.getValidationIri().isEmpty()) throw new IllegalArgumentException("Missing validation iri");
    return validator.validate(request, this);
  }

  public List<TTIriRef> getEntitiesByType(String typeIri) {
    return entityRepository.findEntitiesByType(typeIri);
  }

  public Map<String, org.endeavourhealth.imapi.model.Namespace> getSchemesWithPrefixes() {
    Map<String, org.endeavourhealth.imapi.model.Namespace> result = entityRepository.findAllSchemesWithPrefixes();
    result.values().forEach(value -> value.setPrefix(getPrefixFromIri(value.getIri())));
    return result;
  }

  private String getPrefixFromIri(String iri) {
    int lastSlashIndex = iri.lastIndexOf('/');
    int hashIndex = iri.lastIndexOf('#');
    if (lastSlashIndex != -1 && hashIndex != -1 && hashIndex > lastSlashIndex) {
      return iri.substring(lastSlashIndex + 1, hashIndex);
    } else if (iri.equals(FHIR.DOMAIN)) {
      return FHIR.PREFIX;
    }
    return iri;
  }

  public Set<String> getXmlSchemaDataTypes() {
    return entityRepository.getByGraph(XSD.NAMESPACE);
  }

  public List<List<TTIriRef>> getAllParentHierarchies(String ancestor, String descendant) {
    List<List<TTIriRef>> paths = getParentHierarchies(descendant);
    paths = paths.stream().filter(list -> indexOf(list, ancestor) != -1).collect(Collectors.toList());
    paths.sort((a1, a2) -> {
      return a2.size() - a1.size(); // biggest to smallest
    });

    List<List<TTIriRef>> filteredPaths = new ArrayList<>();
    if (!paths.isEmpty()) {
      filteredPaths = paths.stream().map(path -> {
        int index = indexOf(path, ancestor);
        return path.subList(0, index == path.size() ? index : index + 1);
      }).toList();
    }
    return filteredPaths;
  }

  public Map<String, List<List<String>>> getMultipleSnomedParentHierarchies(List<String> descendantCodes) {
    HashMap<String, List<List<String>>> returnMap = new HashMap<>();
    for (String descendantCode : descendantCodes) {
      List<List<TTIriRef>> refPaths = getAllParentHierarchies(SNOMED.NAMESPACE + "138875005", SNOMED.NAMESPACE + descendantCode);
      List<List<String>> stringPath = refPaths.stream().map(path -> path.stream().map(ref -> {
        String[] splits = ref.getIri().split("#");
        if (splits.length == 2) {
          String code = splits[1];
          return ref.getName() + " | " + code;
        }
        return ref.getName();
      }).toList()).toList();
      returnMap.put(descendantCode, stringPath);
    }
    return returnMap;
  }

  public Map<String, Set<String>> getBNFs(List<String> codes) {
    return entityRepository.findBNFs(codes);
  }

  public Map<String, Set<String>> getSubclassPaths(List<String> codes) {
    return entityRepository.findSubClassPaths(codes);
  }

  public Map<String, Set<String>> getTargetRelatives(List<String> codes, List<String> targetCodes) {
    return entityRepository.findTargetRelatives(codes, targetCodes);
  }
}


