package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.filer.TTFilerException;
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
import org.endeavourhealth.imapi.model.set.ExportSet;
import org.endeavourhealth.imapi.model.set.MemberType;
import org.endeavourhealth.imapi.model.set.SetMember;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTToString;
import org.endeavourhealth.imapi.validators.EntityValidator;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

import static java.util.Comparator.comparingInt;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class EntityService {
  public static final int MAX_CHILDREN = 200;
  private static final Logger LOG = LoggerFactory.getLogger(EntityService.class);
  private static final FilerService filerService = new FilerService();
  private boolean direct = false;
  private boolean desc = false;
  private EntityRepository entityRepository = new EntityRepository();
  private EntityTctRepository entityTctRepository = new EntityTctRepository();
  private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
  private EntityTypeRepository entityTypeRepository = new EntityTypeRepository();
  private ConfigManager configManager = new ConfigManager();
  private EntityRepository2 entityRepository2 = new EntityRepository2();
  private SearchService searchService = new SearchService();

  private static void filterOutSpecifiedPredicates(Set<String> excludePredicates, TTBundle bundle) {
    if (excludePredicates != null) {
      Map<String, String> filtered = bundle.getPredicates().entrySet().stream()
        .filter(entry -> !entry.getKey().equals(RDFS.LABEL) && entry.getValue() != null)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      bundle.setPredicates(filtered);
      if (excludePredicates.contains(RDFS.LABEL)) {
        bundle.getEntity().set(iri(RDFS.LABEL), (TTValue) null);
      }
    }
  }

  private static void filterOutInactiveTermCodes(TTBundle bundle) {
    if (bundle.getEntity().get(iri(IM.HAS_TERM_CODE)) != null) {
      List<TTValue> termCodes = bundle.getEntity().get(iri(IM.HAS_TERM_CODE)).getElements();
      TTArray activeTermCodes = new TTArray();
      for (TTValue value : termCodes) {
        if (value.asNode().get(iri(IM.HAS_STATUS)) != null) {
          if (value.asNode().get(iri(IM.HAS_STATUS)) != null && "Active".equals(value.asNode().get(iri(IM.HAS_STATUS)).asIriRef().getName())) {
            activeTermCodes.add(value);
          }
        } else
          activeTermCodes.add(value);
      }
      bundle.getEntity().set(iri(IM.HAS_TERM_CODE), activeTermCodes);
    }
  }

  public TTBundle getBundle(String iri, Set<String> predicates) {
    return entityRepository2.getBundle(iri, predicates);
  }

  public TTBundle getBundleByPredicateExclusions(String iri, Set<String> excludePredicates) {
    TTBundle bundle = entityRepository2.getBundle(iri, excludePredicates, true);
    filterOutSpecifiedPredicates(excludePredicates, bundle);
    filterOutInactiveTermCodes(bundle);
    return bundle;
  }

  public TTIriRef getEntityReference(String iri) {
    if (iri == null || iri.isEmpty())
      return null;
    return entityRepository.getEntityReferenceByIri(iri);
  }

  public List<EntityReferenceNode> getImmediateChildren(String iri, List<String> schemeIris, Integer pageIndex, Integer pageSize,
                                                        boolean inactive) {
    if (iri == null || iri.isEmpty())
      return Collections.emptyList();

    List<EntityReferenceNode> result = new ArrayList<>();
    int rowNumber = 0;
    if (pageIndex != null && pageSize != null)
      rowNumber = (pageIndex - 1) * pageSize;

    for (TTIriRef c : getChildren(iri, schemeIris, rowNumber, pageSize, inactive)) {
      result.add(getEntityAsEntityReferenceNode(c.getIri()));
    }

    result.sort(comparingInt(EntityReferenceNode::getOrderNumber).thenComparing(EntityReferenceNode::getName));

    return result;
  }

  public EntityReferenceNode getEntityAsEntityReferenceNode(String iri) {
    return getEntityAsEntityReferenceNode(iri, null, false);
  }

  public EntityReferenceNode getEntityAsEntityReferenceNode(String iri, List<String> schemeIris, boolean inactive) {
    if (null == iri) throw new IllegalArgumentException("Missing iri parameter");

    return entityTripleRepository.getEntityReferenceNode(iri, schemeIris, inactive);
  }

  public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (iri == null || iri.isEmpty())
      return null;

    int rowNumber = 0;
    if (page != null && size != null)
      rowNumber = (page - 1) * size;

    Pageable<TTIriRef> childrenAndTotalCount = entityTripleRepository.findImmediateChildrenPagedByIriWithTotalCount(iri, schemeIris, rowNumber, size, inactive);
    return iriRefPageableToEntityReferenceNodePageable(childrenAndTotalCount, schemeIris, inactive);
  }

  public Pageable<TTIriRef> getPartialWithTotalCount(String iri, String predicateList, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (iri == null || iri.isEmpty())
      return null;

    int rowNumber = 0;
    if (page != null && size != null)
      rowNumber = (page - 1) * size;

    return entityTripleRepository.findPartialWithTotalCount(iri, predicateList, schemeIris, rowNumber, size, inactive);
  }

  private List<TTIriRef> getChildren(String iri, List<String> schemeIris, int rowNumber, Integer pageSize, boolean inactive) {
    return entityTripleRepository.findImmediateChildrenByIri(iri, schemeIris, rowNumber, pageSize, inactive);
  }

  public List<EntityReferenceNode> getImmediateParents(String iri, List<String> schemeIris, Integer pageIndex, Integer pageSize,
                                                       boolean inactive) {

    if (iri == null || iri.isEmpty())
      return Collections.emptyList();

    int rowNumber = 0;
    if (pageIndex != null && pageSize != null)
      rowNumber = (pageIndex - 1) * pageSize;

    List<EntityReferenceNode> parents = getParents(iri, schemeIris, rowNumber, pageSize, inactive).stream()
      .map(p -> new EntityReferenceNode(p.getIri(), p.getName())).collect(Collectors.toList());

    for (EntityReferenceNode parent : parents)
      parent.setType(entityTypeRepository.getEntityTypes(parent.getIri()));

    return parents;
  }

  private List<TTIriRef> getParents(String iri, List<String> schemeIris, int rowNumber, Integer pageSize, boolean inactive) {

    return entityTripleRepository.findImmediateParentsByIri(iri, schemeIris, rowNumber, pageSize, inactive);
  }

  public List<TTIriRef> isWhichType(String iri, List<String> candidates) {
    if (iri == null || iri.isEmpty() || candidates == null || candidates.isEmpty())
      return Collections.emptyList();
    return entityTctRepository
      .findAncestorsByType(iri, RDFS.SUBCLASS_OF, candidates).stream()
      .sorted(Comparator.comparing(TTIriRef::getName)).collect(Collectors.toList());
  }

  public List<TTEntity> usages(String iri, Integer pageIndex, Integer pageSize) throws JsonProcessingException {
    ArrayList<TTEntity> usageEntities = new ArrayList<>();
    if (iri == null || iri.isEmpty())
      return Collections.emptyList();

    List<String> xmlDataTypes = configManager.getConfig(CONFIG.XML_SCHEMA_DATA_TYPES, new TypeReference<>() {
    });
    if (xmlDataTypes != null && xmlDataTypes.contains(iri))
      return Collections.emptyList();

    int rowNumber = 0;
    if (pageIndex != null && pageSize != null)
      rowNumber = pageIndex * pageSize;

    List<TTIriRef> usageRefs = entityTripleRepository.getConceptUsages(iri, rowNumber, pageSize).stream()
      .sorted(Comparator.comparing(TTIriRef::getName, Comparator.nullsLast(Comparator.naturalOrder())))
      .distinct().collect(Collectors.toList());

    usageRefs = usageRefs.stream().filter(usage -> !usage.getIri().equals(iri)).collect(Collectors.toList());
    for (TTIriRef usage : usageRefs) {
      TTArray type = getBundle(usage.getIri(), Collections.singleton(RDF.TYPE)).getEntity().getType();
      usageEntities.add(new TTEntity().setIri(usage.getIri()).setName(usage.getName()).setType(type));
    }

    return usageEntities;
  }

  public Integer totalRecords(String iri) throws JsonProcessingException {
    if (iri == null || iri.isEmpty())
      return 0;

    List<String> xmlDataTypes = configManager.getConfig(CONFIG.XML_SCHEMA_DATA_TYPES, new TypeReference<>() {
    });
    if (xmlDataTypes != null && xmlDataTypes.contains(iri))
      return 0;

    return entityTripleRepository.getConceptUsagesCount(iri);
  }

  public SearchResponse advancedSearch(QueryRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException, DataFormatException {
    SearchResponse searchResults = searchService.getEntitiesByTerm(request);
    searchResults.setPage(request.getPage().getPageNumber());
    return searchResults;
  }

  private Set<SetMember> getDefinedInclusions(String iri, boolean expandSets, boolean withHyperlinks, String parentSetName, String originalParentIri, Integer limit) {
    Set<SetMember> definedMemberInclusions = getMembers(iri, withHyperlinks, limit);

    for (SetMember included : definedMemberInclusions) {
      if (originalParentIri.equals(iri)) {
        included.setLabel("a_MemberIncluded");
        if (direct) {
          included.setType(MemberType.INCLUDED_SELF);
        } else if (desc) {
          included.setType(MemberType.INCLUDED_DESC);
        }
      } else {
        if (expandSets) {
          included.setLabel("Subset - expanded");
          included.setType(MemberType.SUBSET);
        } else if (included.getType() != MemberType.COMPLEX) {
          included.setLabel("Subset - " + parentSetName);
          included.setType(MemberType.SUBSET);
        }
      }
      included.setDirectParent(new TTIriRef().setIri(iri).setName(getEntityReference(iri).getName()));
    }

    Set<SetMember> sets = getIsSubsetOf(iri);

    if (!sets.isEmpty()) {
      for (SetMember set : sets) {
        set.setLabel("isSubsetOf");
        set.setType(MemberType.IS_SUBSET_OF);
        definedMemberInclusions.add(set);
      }
    }

    return definedMemberInclusions;
  }

  private Set<SetMember> getIsSubsetOf(String iri) {
    Set<TTIriRef> isSubsetOf = entityRepository2.getIsSubsetOf(iri);
    Set<SetMember> sets = new HashSet<>();
    TTArray result = new TTArray();

    if (!isSubsetOf.isEmpty()) {
      for (TTIriRef set : isSubsetOf) {
        result.add(new TTIriRef(set.getIri(), set.getName()));
      }
    }
    getValueSetMember(true, sets, result);
    return sets;
  }


  private Set<SetMember> getMembers(String iri, boolean withHyperlinks, Integer limit) {
    Set<SetMember> members = new HashSet<>();

    TTArray result = new TTArray();
    if (limit == null || limit == 0) {
      result = getAllMembers(iri, result);
    } else {
      result = getLimitedMembers(iri, limit, result);
    }
    getValueSetMember(withHyperlinks, members, result);
    return members;
  }

  private TTArray getAllMembers(String iri, TTArray result) {
    TTBundle bundle = getBundle(iri, Set.of(IM.DEFINITION, IM.HAS_MEMBER));
    if (bundle.getEntity().get(iri(IM.DEFINITION)) != null) {
      result = bundle.getEntity().get(iri(IM.DEFINITION));
      desc = true;
    } else if (bundle.getEntity().get(iri(IM.HAS_MEMBER)) != null) {
      result = bundle.getEntity().get(iri(IM.HAS_MEMBER));
      direct = true;
    }
    return result;
  }

  private TTArray getLimitedMembers(String iri, Integer limit, TTArray result) {
    TTBundle bundle = getBundle(iri, Set.of(IM.DEFINITION));
    if (bundle.getEntity().get(iri(IM.DEFINITION)) != null) {
      result = bundle.getEntity().get(iri(IM.DEFINITION));
      desc = true;
    } else {
      List<TTIriRef> hasMembers = entityTripleRepository.findPartialWithTotalCount(iri, IM.HAS_MEMBER, null, 0, limit, false).getResult();
      if (!hasMembers.isEmpty()) {
        for (TTIriRef member : hasMembers) {
          result.add(member);
        }
        direct = true;
      }
    }
    return result;
  }

  private void getValueSetMember(boolean withHyperlinks, Set<SetMember> members, TTArray result) {
    if (result != null && !result.isEmpty()) {
      if (direct) {
        members.add(getValueSetMemberFromArray(result, withHyperlinks));
      } else {
        for (TTValue element : result.iterator()) {
          if (element.isNode()) {
            members.add(getValueSetMemberFromNode(element, withHyperlinks));
          } else if (element.isIriRef()) {
            members.add(getValueSetMemberFromIri(element.asIriRef(), withHyperlinks));
          }
        }
      }
    }
  }

  private SetMember getValueSetMemberFromArray(TTArray result, boolean withHyperlinks) {
    SetMember member = new SetMember();
    Map<String, String> defaultPredicates = getDefaultPredicateNames();
    List<String> blockedIris = getBlockedIris();
    String arrayAsString = TTToString.ttValueToString(result, "object", defaultPredicates, 0, withHyperlinks, blockedIris);
    member.setEntity(iri("", arrayAsString));
    return member;
  }

  private List<String> getBlockedIris() {
    List<String> blockedIris = new ArrayList<>();
    try {
      blockedIris = configManager.getConfig(CONFIG.XML_SCHEMA_DATA_TYPES, new TypeReference<>() {
      });
    } catch (Exception e) {
      LOG.warn("Error getting xmlSchemaDataTypes config, reverting to default", e);
    }
    return blockedIris;
  }

  private Map<String, String> getDefaultPredicateNames() {
    Map<String, String> defaultPredicates = new HashMap<>();
    try {
      defaultPredicates = configManager.getConfig(CONFIG.DEFAULT_PREDICATE_NAMES, new TypeReference<>() {
      });
    } catch (Exception e) {
      LOG.warn("Error getting defaultPredicateNames config, reverting to default", e);
    }
    return defaultPredicates;
  }

  private SetMember getValueSetMemberFromNode(TTValue node, boolean withHyperlinks) {
    SetMember member = new SetMember();
    Map<String, String> defaultPredicates = getDefaultPredicateNames();
    List<String> blockedIris = getBlockedIris();
    String nodeAsString = TTToString.ttValueToString(node.asNode(), "object", defaultPredicates, 0, withHyperlinks, blockedIris);
    member.setEntity(iri("", nodeAsString));
    return member;
  }

  private SetMember getValueSetMemberFromIri(TTIriRef iri, boolean withHyperlinks) {
    SetMember member = new SetMember();
    List<String> blockedIris = getBlockedIris();
    SearchResultSummary summary = entityRepository.getEntitySummaryByIri(iri.getIri());
    String iriAsString = TTToString.ttIriToString(iri, "object", 0, withHyperlinks, false, blockedIris);
    member.setEntity(iri(iri.getIri(), iriAsString));
    member.setCode(summary.getCode());
    member.setScheme(summary.getScheme());
    return member;
  }

  private Map<String, SetMember> processMembers(Set<SetMember> setMembers, boolean expand, Integer memberCount, Integer limit) {
    Map<String, SetMember> memberHashMap = new HashMap<>();
    for (SetMember member : setMembers) {

      if (limit != null && (memberCount + memberHashMap.size()) > limit) return memberHashMap;

      memberHashMap.put(member.getEntity().getIri() + "/" + member.getCode(), member);
            /*
            if (expand) {
                setRepository
                    .expandMember(member.getEntity().getIri(), limit)
                    .forEach(m -> {
                        m.setLabel("MemberExpanded");
                        m.setType(MemberType.EXPANDED);
                        memberHashMap.put(m.getEntity().getIri() + "/" + m.getCode(), m);
                    });
            }

             */
    }
    return memberHashMap;
  }

  public List<SearchTermCode> getEntityTermCodes(String iri, boolean includeInactive) {
    if (iri == null || iri.isEmpty())
      return Collections.emptyList();
    TTBundle termsBundle = getBundle(iri, Stream.of(IM.HAS_TERM_CODE).collect(Collectors.toSet()));
    if (!includeInactive) filterOutInactiveTermCodes(termsBundle);
    TTArray terms = termsBundle.getEntity().get(iri(IM.HAS_TERM_CODE));
    if (null == terms) return Collections.emptyList();
    List<SearchTermCode> termsSummary = new ArrayList<>();
    for (TTValue term : terms.getElements()) {
      if (null != term.asNode().get(iri(IM.CODE)) && null == termsSummary.stream().filter(t -> term.asNode().get(iri(IM.CODE)).get(0).asLiteral().getValue().equals(t.getCode())).findAny().orElse(null)) {
        SearchTermCode newTerm = new SearchTermCode();
        if (term.asNode().has(iri(IM.CODE)))
          newTerm.setCode(term.asNode().get(iri(IM.CODE)).get(0).asLiteral().getValue());
        if (term.asNode().has(iri(RDFS.LABEL)))
          newTerm.setTerm(term.asNode().get(iri(RDFS.LABEL)).get(0).asLiteral().getValue());
        if (term.asNode().has(iri(IM.HAS_STATUS)))
          newTerm.setStatus(term.asNode().get(iri(IM.HAS_STATUS)).get(0).asIriRef());
        termsSummary.add(
          newTerm
        );
      }
    }
    return termsSummary;
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

  public DownloadDto getJsonDownload(String iri, List<ComponentLayoutItem> configs, DownloadParams params) {
    if (iri == null || iri.isEmpty())
      return null;

    DownloadDto downloadDto = new DownloadDto();

    downloadDto.setSummary(getSummaryFromConfig(iri, configs));

    if (params.includeHasSubtypes()) {
      downloadDto.setHasSubTypes(getImmediateChildren(iri, null, null, null, params.includeInactive()));
    }
    if (params.includeInferred()) {
      downloadDto.setInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP))).getEntity());
    }
    if (params.includeProperties()) {
      downloadDto.setDataModelProperties(getDataModelProperties(iri));
    }
    if (params.includeMembers()) {
      // downloadDto.setMembers(getValueSetMembers(iri, params.expandMembers(), params.expandSubsets(), null, false));
    }
    if (params.includeTerms()) {
      downloadDto.setTerms(getEntityTermCodes(iri, false));
    }
    if (params.includeIsChildOf()) {
      downloadDto.setIsChildOf(getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF))).getEntity().get(iri(IM.IS_CHILD_OF)));
    }
    if (params.includeHasChildren()) {
      downloadDto.setHasChildren(getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN))).getEntity().get(iri(IM.HAS_CHILDREN)));
    }

    return downloadDto;
  }

  public XlsHelper getExcelDownload(String iri, List<ComponentLayoutItem> configs, DownloadParams params) {
    if (iri == null || iri.isEmpty())
      return null;

    XlsHelper xls = new XlsHelper();

    xls.addSummary(getSummaryFromConfig(iri, configs));

    if (params.includeHasSubtypes()) {
      xls.addHasSubTypes(getImmediateChildren(iri, null, null, null, params.includeInactive()));
    }
    if (params.includeInferred()) {
      xls.addInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP))));
    }
    if (params.includeProperties()) {
      xls.addDataModelProperties(getDataModelProperties(iri));
    }
    if (params.includeMembers()) {
      // xls.addMembersSheet(getValueSetMembers(iri, params.expandMembers(), params.expandSubsets(), null, false));
    }
    if (params.includeTerms()) {
      xls.addTerms(getEntityTermCodes(iri, false));
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

  public List<DataModelProperty> getDataModelProperties(String iri) {
    TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY, RDFS.LABEL)).getEntity();
    return getDataModelProperties(entity);
  }

  public List<DataModelProperty> getDataModelProperties(TTEntity entity) {
    List<DataModelProperty> properties = new ArrayList<>();
    if (entity == null)
      return Collections.emptyList();
    if (entity.has(iri(SHACL.PROPERTY))) {
      getDataModelPropertyGroups(entity, properties);
    }
    return properties.stream().sorted(Comparator.comparing(DataModelProperty::getOrder)).collect(Collectors.toList());
  }

  private void getDataModelPropertyGroups(TTEntity entity, List<DataModelProperty> properties) {
    for (TTValue propertyGroup : entity.get(iri(SHACL.PROPERTY)).iterator()) {
      if (propertyGroup.isNode()) {
        TTIriRef inheritedFrom = propertyGroup.asNode().has(iri(IM.INHERITED_FROM))
          ? propertyGroup.asNode().get(iri(IM.INHERITED_FROM)).asIriRef()
          : null;
        if (propertyGroup.asNode().has(iri(SHACL.PATH))) {
          getDataModelShaclProperties(properties, propertyGroup, inheritedFrom);
        }
      }
    }
  }

  private void getDataModelShaclProperties(List<DataModelProperty> properties, TTValue propertyGroup, TTIriRef inheritedFrom) {
    TTIriRef propertyPath = propertyGroup.asNode().get(iri(SHACL.PATH)).asIriRef();
    if (properties.stream()
      .noneMatch(o -> o.getProperty().getIri().equals(propertyPath.getIri()))) {
      properties.add(getPropertyValue(inheritedFrom, propertyGroup, propertyPath));
    }
  }

  private DataModelProperty getPropertyValue(TTIriRef inheritedFrom, TTValue property, TTIriRef propertyPath) {
    DataModelProperty pv = new DataModelProperty().setInheritedFrom(inheritedFrom).setProperty(propertyPath);

    if (property.asNode().has(iri(SHACL.CLASS)))
      pv.setType(property.asNode().get(iri(SHACL.CLASS)).asIriRef());
    if (property.asNode().has(iri(SHACL.NODE)))
      pv.setType(property.asNode().get(iri(SHACL.NODE)).asIriRef());
    if (property.asNode().has(iri(OWL.CLASS)))
      pv.setType(property.asNode().get(iri(OWL.CLASS)).asIriRef());
    if (property.asNode().has(iri(SHACL.DATATYPE)))
      pv.setType(property.asNode().get(iri(SHACL.DATATYPE)).asIriRef());
    if (property.asNode().has(iri(SHACL.FUNCTION)))
      pv.setType(property.asNode().get(iri(SHACL.FUNCTION)).asIriRef());
    if (property.asNode().has(iri(SHACL.MAXCOUNT)))
      pv.setMaxExclusive(property.asNode().get(iri(SHACL.MAXCOUNT)).asLiteral().getValue());
    if (property.asNode().has(iri(SHACL.MINCOUNT)))
      pv.setMinExclusive(property.asNode().get(iri(SHACL.MINCOUNT)).asLiteral().getValue());
    pv.setOrder(property.asNode().has(iri(SHACL.ORDER)) ? property.asNode().get(iri(SHACL.ORDER)).asLiteral().intValue() : 0);

    return pv;
  }

  private void appendValueSet(ExportSet exportSet, StringBuilder valueSetMembers, SetMember setMember) {
    valueSetMembers.append("Inc").append("\t").append(exportSet.getValueSet().getIri()).append("\t")
      .append(exportSet.getValueSet().getName()).append("\t").append(setMember.getEntity().asIriRef().getIri())
      .append("\t").append(setMember.getEntity().asIriRef().getName()).append("\t").append(setMember.getCode())
      .append("\t");
    if (setMember.getScheme() != null)
      valueSetMembers.append(setMember.getScheme().getIri()).append("\t").append(setMember.getScheme().getName());

    valueSetMembers.append("\n");
  }

  public GraphDto getGraphData(String iri) {
    TTEntity entity = getBundle(iri, Set.of(RDFS.SUBCLASS_OF, RDFS.LABEL)).getEntity();

    GraphDto graphData = new GraphDto().setKey("0").setIri(entity.getIri()).setName(entity.getName());
    GraphDto graphParents = new GraphDto().setKey("0_0").setName("Is a");
    GraphDto graphChildren = new GraphDto().setKey("0_1").setName("Subtypes");

    TTBundle axioms = getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP)));
    List<GraphDto> axiomGraph = bundleToGraphDtos(axioms);

    List<GraphDto> dataModelProps = getDataModelProperties(iri).stream()
      .map(prop -> new GraphDto(prop.getProperty().getIri(), prop.getProperty().getName(),
        prop.getType() != null ? prop.getType().getIri() : "",
        prop.getType() != null ? prop.getType().getName() : "",
        prop.getInheritedFrom() != null ? prop.getInheritedFrom().getIri() : "",
        prop.getInheritedFrom() != null ? prop.getInheritedFrom().getName() : ""))
      .collect(Collectors.toList());

    List<GraphDto> isas = getEntityDefinedParents(entity);

    List<GraphDto> subtypes = getDefinitionSubTypes(iri).stream()
      .map(subtype -> new GraphDto().setName(subtype.getName()).setIri(subtype.getIri()))
      .toList();

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

    GraphDto childrenWrapper = new GraphDto().setKey("0_1_0")
      .setType(!subtypes.isEmpty() ? GraphType.SUBTYPE : GraphType.NONE);
    childrenWrapper.getLeafNodes().addAll(subtypes);

    GraphDto parentsWrapper = new GraphDto().setKey("0_0_0")
      .setType(!isas.isEmpty() ? GraphType.ISA : GraphType.NONE);
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

  private List<GraphDto> bundleToGraphDtos(TTBundle axioms) {
    return new ArrayList<>();
  }

  private GraphDto getWrapper(List<GraphDto> props, String key) {
    GraphDto wrapper = new GraphDto().setKey(key).setType(GraphType.PROPERTIES);
    wrapper.getLeafNodes()
      .addAll(props.stream()
        .filter(prop -> prop.getInheritedFromIri() == null).toList());
    return wrapper;
  }

  private GraphDto getDataModelInheritedWrapper(List<GraphDto> dataModelProps) {
    GraphDto dataModelInheritedWrapper = new GraphDto().setKey("0_3_1_0").setType(GraphType.PROPERTIES);
    dataModelInheritedWrapper.getLeafNodes()
      .addAll(dataModelProps.stream()
        .filter(prop -> prop.getInheritedFromIri() != null).toList());
    return dataModelInheritedWrapper;
  }

  private void addWrapper(GraphDto dto, GraphDto wrapper, String key) {
    dto.getChildren()
      .add(wrapper.getLeafNodes().isEmpty()
        ? new GraphDto().setKey(key).setType(GraphType.NONE)
        : wrapper);
  }

  private List<GraphDto> getEntityDefinedParents(TTEntity entity) {
    TTArray parent = entity.get(iri(RDFS.SUBCLASS_OF));
    if (parent == null)
      return Collections.emptyList();
    List<GraphDto> result = new ArrayList<>();
    parent.getElements().forEach(item -> {
      if (!OWL.THING.equals(item.asIriRef().getIri()))
        result.add(new GraphDto().setIri(item.asIriRef().getIri()).setName(item.asIriRef().getName())
          .setPropertyType(iri(RDFS.SUBCLASS_OF).getName()));
    });

    return result;
  }

  public List<TTIriRef> getDefinitionSubTypes(String iri) {

    return entityTripleRepository.findImmediateChildrenByIri(iri, null, null, null, false).stream()
      .map(t -> new TTIriRef(t.getIri(), t.getName())).collect(Collectors.toList());
  }

  public SearchResultSummary getSummary(String iri) {
    if (iri == null || iri.isEmpty())
      return null;
    return entityRepository.getEntitySummaryByIri(iri);
  }

  public TTEntity getConceptShape(String iri) {
    if (iri == null || iri.isEmpty())
      return null;
    TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY, SHACL.OR, RDF.TYPE)).getEntity();
    TTArray value = entity.get(iri(RDF.TYPE));
    if (!value.getElements().contains(iri(SHACL.NODESHAPE))) {
      return null;
    }
    return entity;
  }

  public List<Namespace> getNamespaces() {
    return entityTripleRepository.findNamespaces();
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
    List<Namespace> namespaces = entityTripleRepository.findNamespaces();
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

  public List<SimpleMap> getMatchedFrom(String iri) {
    if (iri == null || iri.equals("")) return new ArrayList<>();
    String scheme = iri.substring(0, iri.indexOf("#") + 1);
    List<Namespace> namespaces = getNamespaces();
    List<String> schemes = namespaces.stream().map(Namespace::getIri).collect(Collectors.toList());
    schemes.remove(scheme);
    return entityTripleRepository.getMatchedFrom(iri, schemes);
  }

  public List<SimpleMap> getMatchedTo(String iri) {
    if (iri == null || iri.equals("")) return new ArrayList<>();
    String scheme = iri.substring(0, iri.indexOf("#") + 1);
    List<Namespace> namespaces = getNamespaces();
    List<String> schemes = namespaces.stream().map(Namespace::getIri).collect(Collectors.toList());
    schemes.remove(scheme);
    return entityTripleRepository.getMatchedTo(iri, schemes);
  }

  public XSSFWorkbook getSetExport(String iri, boolean definition, boolean core, boolean legacy, boolean includeSubsets,
                                   boolean inlineLegacy, boolean im1id, List<String> schemes) throws JsonProcessingException, QueryException {
    if (iri == null || "".equals(iri)) {
      return null;
    }
    return new ExcelSetExporter().getSetAsExcel(new SetExporterOptions(iri, definition, core, legacy, includeSubsets, inlineLegacy, im1id, schemes));
  }

  /**
   * Returns an entity and predicate names from an iri including all predicates and blank nodes recursively to a depth of 5
   *
   * @param iri the string representation of the absolute iri
   * @return a bundle containing an entity and predicate iri to name map.
   */
  public TTBundle getFullEntity(String iri) {
    return entityRepository2.getBundle(iri);
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
    TTIriRef parent = entityTripleRepository.findParentFolderRef(iri);
    if (parent != null) {
      parents.add(parent);
      getParentPathRecursive(parent.getIri(), parents);
    }
  }

  public Set<TTIriRef> getNames(Set<String> iris) {
    Set<TTIriRef> result = iris.stream().map(TTIriRef::new).collect(Collectors.toSet());
    entityRepository2.getNames(result);
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

    paths.sort(new Comparator<List<TTIriRef>>() {
      @Override
      public int compare(List<TTIriRef> a1, List<TTIriRef> a2) {
        return a2.size() - a1.size(); // biggest to smallest
      }
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

  public TTEntity createEntity(TTEntity entity, String agentName) throws TTFilerException, JsonProcessingException {
    EntityValidator validator = new EntityValidator();
    validator.isValid(entity, this, "Create");
    TTIriRef graph = iri(GRAPH.DISCOVERY);
    entity.setCrud(iri(IM.ADD_QUADS)).setVersion(1);
    filerService.fileEntity(entity, graph, agentName, null);
    return entity;
  }

  public TTEntity updateEntity(TTEntity entity, String agentName) throws TTFilerException, JsonProcessingException {
    EntityValidator validator = new EntityValidator();
    validator.isValid(entity, this, "Update");
    TTIriRef graph = iri(GRAPH.DISCOVERY);
    entity.setCrud(iri(IM.UPDATE_ALL));
    TTEntity usedEntity = getFullEntity(entity.getIri()).getEntity();
    entity.setVersion(usedEntity.getVersion() + 1);
    filerService.fileEntity(entity, graph, agentName, usedEntity);
    return entity;
  }

  public void updateSubsetsFromSuper(String agentName, TTEntity entity) throws TTFilerException, JsonProcessingException {
    TTArray subsets = entity.get(iri(IM.HAS_SUBSET));
    String entityIri = entity.getIri();
    Set<TTIriRef> subsetsOriginal = getSubsets(entityIri);
    List<TTIriRef> subsetsArray = subsets.stream().map(TTValue::asIriRef).toList();
    for (TTIriRef subset : subsetsArray) {
      TTEntity subsetEntity = getFullEntity(subset.getIri()).getEntity();
      if (null != subsetEntity) {
        if (!(subsetEntity.isType(iri(IM.VALUESET)) || subsetEntity.isType(iri(IM.CONCEPT_SET))))
          throw new TTFilerException("Subsets must be of type valueSet or conceptSet. Type: " + subsetEntity.getType());
        TTArray isSubsetOf = subsetEntity.get(iri(IM.IS_SUBSET_OF));
        if (null == isSubsetOf) {
          subsetEntity.set(iri(IM.IS_SUBSET_OF), new TTArray().add(iri(entityIri)));
          updateEntity(subsetEntity, agentName);
        } else if (isSubsetOf.getElements().stream().noneMatch(i -> Objects.equals(i.asIriRef().getIri(), entityIri))) {
          isSubsetOf.add(iri(entityIri));
          updateEntity(subsetEntity, agentName);
        }
      }
    }
    for (TTIriRef subsetOriginal : subsetsOriginal) {
      if (subsetsArray.stream().noneMatch(s -> s.getIri().equals(subsetOriginal.getIri()))) {
        TTEntity subsetEntity = getFullEntity(subsetOriginal.getIri()).getEntity();
        TTArray isSubsetOf = subsetEntity.get(iri(IM.IS_SUBSET_OF));
        isSubsetOf.remove(iri(entityIri));
        updateEntity(subsetEntity, agentName);
      }
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

  public List<TTIriRef> getProperties() {
    return entityRepository.getProperties();
  }

  public List<TTIriRef> getDistillation(List<TTIriRef> conceptList) {
    List<String> iriList = conceptList.stream().map(c -> "<" + c.getIri() + ">").collect(Collectors.toList());
    String iris = String.join(" ", iriList);
    Set<String> isas = entityRepository.getDistillation(iris);
    conceptList.removeIf(c -> isas.contains(c.getIri()));
    return conceptList;
  }

  public Set<String> getPredicates(String iri) {
    return entityRepository.getPredicates(iri);
  }

  public Pageable<EntityReferenceNode> getSuperiorPropertiesPaged(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (null == iri || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (null != page && null != size) rowNumber = (page - 1) * size;

    Pageable<TTIriRef> propertiesAndCount = entityTripleRepository.getSuperiorPropertiesByConceptPagedWithTotalCount(iri, rowNumber, size, inactive);
    return iriRefPageableToEntityReferenceNodePageable(propertiesAndCount, schemeIris, inactive);
  }

  public Pageable<EntityReferenceNode> getSuperiorPropertiesBoolFocusPaged(List<String> conceptIris, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (null == conceptIris || conceptIris.isEmpty()) return null;

    int rowNumber = 0;
    if (null != page && null != size) rowNumber = (page - 1) * size;

    Pageable<TTIriRef> propertiesAndCount = entityTripleRepository.getSuperiorPropertiesByConceptBoolFocusPagedWithTotalCount(conceptIris, rowNumber, size, inactive);
    return iriRefPageableToEntityReferenceNodePageable(propertiesAndCount, schemeIris, inactive);
  }

  public Pageable<EntityReferenceNode> getSuperiorPropertyValuesPaged(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (null == iri || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (null != page && null != size) rowNumber = (page - 1) * size;


    Pageable<TTIriRef> propertiesAndCount = entityTripleRepository.getSuperiorPropertyValuesByPropertyPagedWithTotalCount(iri, rowNumber, size, inactive);
    return iriRefPageableToEntityReferenceNodePageable(propertiesAndCount, schemeIris, inactive);
  }

  private Pageable<EntityReferenceNode> iriRefPageableToEntityReferenceNodePageable(Pageable<TTIriRef> iriRefPageable, List<String> schemeIris, boolean inactive) {
    Pageable<EntityReferenceNode> result = new Pageable<>();
    result.setTotalCount(iriRefPageable.getTotalCount());
    Set<String> iris = new HashSet<>();
    for (TTIriRef entity : iriRefPageable.getResult()) {
      iris.add(entity.getIri());
    }
    List<EntityReferenceNode> nodes = entityTripleRepository.getEntityReferenceNodes(iris, schemeIris, inactive);
    nodes.sort(comparingInt(EntityReferenceNode::getOrderNumber).thenComparing(EntityReferenceNode::getName));

    result.setResult(nodes);
    return result;
  }

  public List<String> getIM1SchemeOptions() {
    return entityRepository.getIM1SchemeOptions();
  }

  public Set<Concept> getFullyExpandedMembers(String iri, boolean includeLegacy, boolean includeSubset, List<String> schemes) throws QueryException, JsonProcessingException {
    SetExporter setExporter = new SetExporter();
    Set<Concept> members = setExporter.getExpandedSetMembers(iri, true, includeLegacy, includeSubset, schemes);
    return members;
  }

  public Set<TTIriRef> getSubsets(String iri) {
    SetExporter setExporter = new SetExporter();
    return setExporter.getSubsetIrisWithNames(iri);
  }

  public List<TTIriRef> getDataModelsFromProperty(String propIri) {
    return entityRepository.findDataModelsFromProperty(propIri);

  }

  public List<ConceptContextMap> getConceptContextMaps(String iri) {
    return entityRepository.getConceptContextMaps(iri);
  }

  public String checkPropertyType(String iri) {
    return entityRepository.checkPropertyType(iri);
  }
}

