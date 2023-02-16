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
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.dto.*;
import org.endeavourhealth.imapi.model.dto.GraphDto.GraphType;
import org.endeavourhealth.imapi.model.iml.FormGenerator;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.set.ExportSet;
import org.endeavourhealth.imapi.model.set.MemberType;
import org.endeavourhealth.imapi.model.set.SetAsObject;
import org.endeavourhealth.imapi.model.set.SetMember;
import org.endeavourhealth.imapi.transforms.TTToClassObject;
import org.endeavourhealth.imapi.transforms.TTToString;
import org.endeavourhealth.imapi.validators.EntityValidator;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class EntityService {
    private static final Logger LOG = LoggerFactory.getLogger(EntityService.class);
    private static final FilerService filerService = new FilerService();
    private boolean direct = false;
    private boolean desc = false;

    public static final int UNLIMITED = 0;
    public static final int MAX_CHILDREN = 200;

    private EntityRepository entityRepository = new EntityRepository();
    private EntityTctRepository entityTctRepository = new EntityTctRepository();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
    private TermCodeRepository termCodeRepository = new TermCodeRepository();
    private EntityTypeRepository entityTypeRepository = new EntityTypeRepository();
    private ConfigManager configManager = new ConfigManager();
    private EntityRepository2 entityRepository2 = new EntityRepository2();
    private SearchService searchService = new SearchService();

    public TTBundle getBundle(String iri, Set<String> predicates) {
        return entityRepository2.getBundle(iri, predicates);
    }

    /**
     * Returns the entity with local predicate names as plain json including json literals
     * <p> Works only for known POJO classes in order to resolve the RDF cardinality problem</p>
     *
     * @param iri   iri of the entity
     * @param depth maximum nesting depth
     * @return string of json
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws JsonProcessingException
     */
    public String getAsPlainJson(String iri, int depth) throws NoSuchMethodException, InstantiationException, IllegalAccessException, JsonProcessingException {
        TTBundle bundle = entityRepository2.getBundle(iri, null, false, depth);
        Class<?> cls;
        String entityType = bundle.getEntity().getType().get(0).asIriRef().getIri();
        switch (entityType) {
            case (IM.NAMESPACE + "FormGenerator"):
                cls = FormGenerator.class;
                break;
            default:
                throw new NoSuchMethodException(" entity type " + entityType + " is not supported as a POJO class");

        }
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            return om.writeValueAsString(new TTToClassObject().getObject(bundle.getEntity(), cls));
        }
    }

    public TTBundle getBundleByPredicateExclusions(String iri, Set<String> excludePredicates) {
        TTBundle bundle = entityRepository2.getBundle(iri, excludePredicates, true);
        filterOutSpecifiedPredicates(excludePredicates, bundle);
        filterOutInactiveTermCodes(bundle);
        return bundle;
    }

    private static void filterOutSpecifiedPredicates(Set<String> excludePredicates, TTBundle bundle) {
        if (excludePredicates != null) {
            Map<String, String> filtered = bundle.getPredicates().entrySet().stream()
                .filter(entry -> !entry.getKey().equals(RDFS.LABEL.getIri()) && entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            bundle.setPredicates(filtered);
            if (excludePredicates.contains(RDFS.LABEL.getIri())) {
                bundle.getEntity().set(RDFS.LABEL, (TTValue) null);
            }
        }
    }

    private static void filterOutInactiveTermCodes(TTBundle bundle) {
        if (bundle.getEntity().get(IM.HAS_TERM_CODE) != null) {
            List<TTValue> termCodes = bundle.getEntity().get(IM.HAS_TERM_CODE).getElements();
            TTArray activeTermCodes = new TTArray();
            for (TTValue value : termCodes) {
                if (value.asNode().get(IM.HAS_STATUS) != null) {
                    if (value.asNode().get(IM.HAS_STATUS) != null && "Active".equals(value.asNode().get(IM.HAS_STATUS).asIriRef().getName())) {
                        activeTermCodes.add(value);
                    }
                } else
                    activeTermCodes.add(value);
            }
            bundle.getEntity().set(IM.HAS_TERM_CODE, activeTermCodes);
        }
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
            EntityReferenceNode node = new EntityReferenceNode();
            node.setIri(c.getIri()).setName(c.getName());
            node.setType(entityTypeRepository.getEntityTypes(c.getIri()));
            node.setHasChildren(entityTripleRepository.hasChildren(c.getIri(), schemeIris, inactive));
            node.setHasGrandChildren(entityTripleRepository.hasGrandChildren(c.getIri(), schemeIris, inactive));
            node.setOrderNumber(entityTripleRepository.getOrderNumber(c.getIri()));
            result.add(node);
        }

        return result;
    }

    public EntityReferenceNode getEntityAsEntityReferenceNode(String iri) {
        if (null == iri) return new EntityReferenceNode();
        EntityReferenceNode node = new EntityReferenceNode();
        TTIriRef entityIriRef = getEntityReference(iri);
        node.setIri(entityIriRef.getIri());
        node.setName(entityIriRef.getName());
        node.setType(entityTypeRepository.getEntityTypes(entityIriRef.getIri()));
        node.setHasChildren(entityTripleRepository.hasChildren(entityIriRef.getIri(), null, false));
        node.setHasGrandChildren(entityTripleRepository.hasGrandChildren(entityIriRef.getIri(), null, false));
        return node;
    }

    public Pageable<EntityReferenceNode> getEntityChildrenPagedWithTotalCount(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
        if (iri == null || iri.isEmpty())
            return null;

        int rowNumber = 0;
        if (page != null && size != null)
            rowNumber = (page - 1) * size;

        Pageable<TTIriRef> childrenAndTotalCount = entityTripleRepository.findImmediateChildrenPagedByIriWithTotalCount(iri, schemeIris, rowNumber, size, inactive);
        return iriRefPageableToEntityReferenceNodePageable(childrenAndTotalCount,schemeIris,inactive);
    }

    public Pageable<TTIriRef> getPartialWithTotalCount(String iri, String predicateList, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
        if (iri == null || iri.isEmpty())
            return null;

        int rowNumber = 0;
        if (page != null && size != null)
            rowNumber = (page - 1) * size;

        return entityTripleRepository.findPartialWithTotalCount(iri, predicateList, schemeIris, rowNumber, size, inactive);
    }

    public ExportSet getHasMember(String iri, String predicateList, List<String> schemeIris, Integer page, Integer size, boolean inactive) {

        List<TTIriRef> hasMembers = getPartialWithTotalCount(iri, predicateList, schemeIris, page, size, inactive).getResult();
        TTArray array = new TTArray();
        for (TTIriRef member : hasMembers) {
            array.add(member);
        }
        Set<SetMember> members = new HashSet<>();
        members.add(getValueSetMemberFromArray(array, true));
        for (SetMember setMember : members) {
            setMember.setLabel("a_MemberIncluded");
            setMember.setType(MemberType.INCLUDED_SELF);
            setMember.setDirectParent(new TTIriRef().setIri(iri).setName(getEntityReference(iri).getName()));
        }
        ExportSet result = new ExportSet().setValueSet(getEntityReference(iri));

        Map<String, SetMember> processedMembers = processMembers(members, false, 0, 2000);

        result.addAllMembers(processedMembers.values());

        return result;
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
                .findAncestorsByType(iri, RDFS.SUBCLASSOF.getIri(), candidates).stream()
                .sorted(Comparator.comparing(TTIriRef::getName)).collect(Collectors.toList());
    }

    public List<TTEntity> usages(String iri, Integer pageIndex, Integer pageSize) throws JsonProcessingException {
        ArrayList<TTEntity> usageEntities = new ArrayList<>();
        if (iri == null || iri.isEmpty())
            return Collections.emptyList();

        List<String> xmlDataTypes = configManager.getConfig(CONFIG.XML_SCHEMA_DATATYPES, new TypeReference<>() {
        });
        if (xmlDataTypes != null && xmlDataTypes.contains(iri))
            return Collections.emptyList();

        int rowNumber = 0;
        if (pageIndex != null && pageSize != null)
            rowNumber = pageIndex * pageSize;

        List<TTIriRef> usageRefs = entityTripleRepository.getActiveSubjectByObjectExcludeByPredicate(iri, rowNumber, pageSize, RDFS.SUBCLASSOF.getIri()).stream()
                .sorted(Comparator.comparing(TTIriRef::getName, Comparator.nullsLast(Comparator.naturalOrder())))
                .distinct().collect(Collectors.toList());

        usageRefs = usageRefs.stream().filter(usage -> !usage.getIri().equals(iri)).collect(Collectors.toList());
        for (TTIriRef usage : usageRefs) {
            TTArray type = getBundle(usage.getIri(), Collections.singleton(RDF.TYPE.getIri())).getEntity().getType();
            usageEntities.add(new TTEntity().setIri(usage.getIri()).setName(usage.getName()).setType(type));
        }

        return usageEntities;
    }

    public Integer totalRecords(String iri) throws JsonProcessingException {
        if (iri == null || iri.isEmpty())
            return 0;

        List<String> xmlDataTypes = configManager.getConfig(CONFIG.XML_SCHEMA_DATATYPES, new TypeReference<>() {
        });
        if (xmlDataTypes != null && xmlDataTypes.contains(iri))
            return 0;

        return entityTripleRepository.getCountOfActiveSubjectByObjectExcludeByPredicate(iri, RDFS.SUBCLASSOF.getIri());
    }

    public List<SearchResultSummary> advancedSearch(SearchRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        return searchService.getEntitiesByTerm(request);
    }

    public ExportSet getValueSetMembers(String iri, boolean expandMembers, boolean expandSets, Integer limit, boolean withHyperlinks) {
        return getValueSetMembers(iri, expandMembers, expandSets, limit, withHyperlinks, null, iri);
    }

    public ExportSet getValueSetMembers(String iri, boolean expandMembers, boolean expandSets, Integer limit, boolean withHyperlinks, String parentSetName, String originalParentIri) {
        if (iri == null || iri.isEmpty()) {
            return null;
        }
        ExportSet result = new ExportSet().setValueSet(getEntityReference(iri));
        int memberCount = 0;

        Set<SetMember> definedMemberInclusions = getDefinedInclusions(iri, expandSets, withHyperlinks, parentSetName, originalParentIri, limit);


        Map<String, SetMember> evaluatedMemberInclusions = processMembers(definedMemberInclusions, expandMembers, memberCount, limit);

        if (limit != null && memberCount > limit)
            return result.setLimited(true);

        result.addAllMembers(evaluatedMemberInclusions.values());

        return result;
    }

    public SetAsObject getValueSetMembersAsNode(String iri, boolean expandMembers, boolean expandSubsets, Integer limit) {
        SetAsObject result = new SetAsObject();
        TTIriRef valueSet = entityRepository.getEntityReferenceByIri(iri);
        Set<String> definition = new HashSet<>();
        definition.add(IM.DEFINITION.getIri());
        TTArray included = getBundle(iri, definition)
                .getEntity()
                .get(IM.DEFINITION.asIriRef());
        result.setIri(valueSet.getIri());
        result.setName(valueSet.getName());
        result.setIncluded(included);
        return result;
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
        TTBundle bundle = getBundle(iri, Set.of(IM.DEFINITION.getIri(), IM.HAS_MEMBER.getIri()));
        if (bundle.getEntity().get(IM.DEFINITION.asIriRef()) != null) {
            result = bundle.getEntity().get(IM.DEFINITION.asIriRef());
            desc = true;
        } else if (bundle.getEntity().get(IM.HAS_MEMBER.asIriRef()) != null) {
            result = bundle.getEntity().get(IM.HAS_MEMBER.asIriRef());
            direct = true;
        }
        return result;
    }

    private TTArray getLimitedMembers(String iri, Integer limit, TTArray result) {
        TTBundle bundle = getBundle(iri, Set.of(IM.DEFINITION.getIri()));
        if (bundle.getEntity().get(IM.DEFINITION.asIriRef()) != null) {
            result = bundle.getEntity().get(IM.DEFINITION.asIriRef());
            desc = true;
        } else {
            List<TTIriRef> hasMembers = entityTripleRepository.findPartialWithTotalCount(iri, IM.HAS_MEMBER.getIri(), null, 0, limit, false).getResult();
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
            blockedIris = configManager.getConfig(CONFIG.XML_SCHEMA_DATATYPES, new TypeReference<>() {
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

    public List<TermCode> getEntityTermCodes(String iri) {
        if (iri == null || iri.isEmpty())
            return Collections.emptyList();
        return termCodeRepository.findAllByIri(iri);
    }

    public TTEntity getSummaryFromConfig(String iri, List<ComponentLayoutItem> configs) {
        if (iri == null || iri.isEmpty() || configs == null || configs.isEmpty()) {
            return new TTEntity();
        }
        List<String> excludedForSummary = Arrays.asList("None", RDFS.SUBCLASSOF.getIri(), "subtypes", IM.IS_CHILD_OF.getIri(), IM.HAS_CHILDREN.getIri(), "termCodes", "semanticProperties", "dataModelProperties");
        List<ComponentLayoutItem> filteredConfigs = configs.stream().filter(config -> !excludedForSummary.contains(config.getPredicate())).collect(Collectors.toList());
        List<String> predicates = filteredConfigs.stream().map(ComponentLayoutItem::getPredicate).collect(Collectors.toList());
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
            downloadDto.setInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri()))).getEntity());
        }
        if (params.includeProperties()) {
            downloadDto.setDataModelProperties(getDataModelProperties(iri));
        }
        if (params.includeMembers()) {
            // downloadDto.setMembers(getValueSetMembers(iri, params.expandMembers(), params.expandSubsets(), null, false));
        }
        if (params.includeTerms()) {
            downloadDto.setTerms(getEntityTermCodes(iri));
        }
        if (params.includeIsChildOf()) {
            downloadDto.setIsChildOf(getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF.getIri()))).getEntity().get(IM.IS_CHILD_OF));
        }
        if (params.includeHasChildren()) {
            downloadDto.setHasChildren(getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN.getIri()))).getEntity().get(IM.HAS_CHILDREN));
        }

        return downloadDto;
    }

    public boolean getHasChildren(String iri) {
        return entityRepository.getHasChildren(iri);
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
            xls.addInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri()))));
        }
        if (params.includeProperties()) {
            xls.addDataModelProperties(getDataModelProperties(iri));
        }
        if (params.includeMembers()) {
            // xls.addMembersSheet(getValueSetMembers(iri, params.expandMembers(), params.expandSubsets(), null, false));
        }
        if (params.includeTerms()) {
            xls.addTerms(getEntityTermCodes(iri));
        }
        TTEntity isChildOfEntity = getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF.getIri()))).getEntity();
        TTArray isChildOfData = isChildOfEntity.get(TTIriRef.iri(IM.IS_CHILD_OF.getIri(), IM.IS_CHILD_OF.getName()));
        if (params.includeIsChildOf() && isChildOfData != null) {
            xls.addIsChildOf(isChildOfData.getElements());
        }
        TTEntity hasChildrenEntity = getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN.getIri()))).getEntity();
        TTArray hasChildrenData = hasChildrenEntity.get(TTIriRef.iri(IM.HAS_CHILDREN.getIri(), IM.HAS_CHILDREN.getName()));
        if (params.includeHasChildren() && hasChildrenData != null) {
            xls.addHasChildren(hasChildrenData.getElements());
        }

        return xls;
    }

    public List<DataModelProperty> getDataModelProperties(String iri) {
        TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY.getIri(), RDFS.LABEL.getIri())).getEntity();
        return getDataModelProperties(entity);
    }

    public List<DataModelProperty> getDataModelProperties(TTEntity entity) {
        List<DataModelProperty> properties = new ArrayList<>();
        if (entity == null)
            return Collections.emptyList();
        if (entity.has(SHACL.PROPERTY)) {
            getDataModelPropertyGroups(entity, properties);
        }
        return properties.stream().sorted(Comparator.comparing(DataModelProperty::getOrder)).collect(Collectors.toList());
    }

    private void getDataModelPropertyGroups(TTEntity entity, List<DataModelProperty> properties) {
        for (TTValue propertyGroup : entity.get(SHACL.PROPERTY).iterator()) {
            if (propertyGroup.isNode()) {
                TTIriRef inheritedFrom = propertyGroup.asNode().has(IM.INHERITED_FROM)
                        ? propertyGroup.asNode().get(IM.INHERITED_FROM).asIriRef()
                        : null;
                if (propertyGroup.asNode().has(SHACL.PATH)) {
                    getDataModelShaclProperties(properties, propertyGroup, inheritedFrom);
                }
            }
        }
    }

    private void getDataModelShaclProperties(List<DataModelProperty> properties, TTValue propertyGroup, TTIriRef inheritedFrom) {
        TTIriRef propertyPath = propertyGroup.asNode().get(SHACL.PATH).asIriRef();
        if (properties.stream()
                .noneMatch(o -> o.getProperty().getIri().equals(propertyPath.getIri()))) {
            properties.add(getPropertyValue(inheritedFrom, propertyGroup, propertyPath));
        }
    }

    private DataModelProperty getPropertyValue(TTIriRef inheritedFrom, TTValue property, TTIriRef propertyPath) {
        DataModelProperty pv = new DataModelProperty().setInheritedFrom(inheritedFrom).setProperty(propertyPath);

        if (property.asNode().has(SHACL.CLASS))
            pv.setType(property.asNode().get(SHACL.CLASS).asIriRef());
        if (property.asNode().has(SHACL.NODE))
            pv.setType(property.asNode().get(SHACL.NODE).asIriRef());
        if (property.asNode().has(OWL.CLASS))
            pv.setType(property.asNode().get(OWL.CLASS).asIriRef());
        if (property.asNode().has(SHACL.DATATYPE))
            pv.setType(property.asNode().get(SHACL.DATATYPE).asIriRef());
        if (property.asNode().has(SHACL.FUNCTION))
            pv.setType(property.asNode().get(SHACL.FUNCTION).asIriRef());
        if (property.asNode().has(SHACL.MAXCOUNT))
            pv.setMaxExclusive(property.asNode().get(SHACL.MAXCOUNT).asLiteral().getValue());
        if (property.asNode().has(SHACL.MINCOUNT))
            pv.setMinExclusive(property.asNode().get(SHACL.MINCOUNT).asLiteral().getValue());
        pv.setOrder(property.asNode().has(SHACL.ORDER) ? property.asNode().get(SHACL.ORDER).asLiteral().intValue() : 0);

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
        TTEntity entity = getBundle(iri, Set.of(RDFS.SUBCLASSOF.getIri(), RDFS.LABEL.getIri())).getEntity();

        GraphDto graphData = new GraphDto().setKey("0").setIri(entity.getIri()).setName(entity.getName());
        GraphDto graphParents = new GraphDto().setKey("0_0").setName("Is a");
        GraphDto graphChildren = new GraphDto().setKey("0_1").setName("Subtypes");

        TTBundle axioms = getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri())));
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
                .collect(Collectors.toList());

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
                        .filter(prop -> prop.getInheritedFromIri() == null).collect(Collectors.toList()));
        return wrapper;
    }

    private GraphDto getDataModelInheritedWrapper(List<GraphDto> dataModelProps) {
        GraphDto dataModelInheritedWrapper = new GraphDto().setKey("0_3_1_0").setType(GraphType.PROPERTIES);
        dataModelInheritedWrapper.getLeafNodes()
                .addAll(dataModelProps.stream()
                        .filter(prop -> prop.getInheritedFromIri() != null).collect(Collectors.toList()));
        return dataModelInheritedWrapper;
    }

    private void addWrapper(GraphDto dto, GraphDto wrapper, String key) {
        dto.getChildren()
                .add(wrapper.getLeafNodes().isEmpty()
                        ? new GraphDto().setKey(key).setType(GraphType.NONE)
                        : wrapper);
    }

    private List<GraphDto> getEntityDefinedParents(TTEntity entity) {
        TTArray parent = entity.get(RDFS.SUBCLASSOF);
        if (parent == null)
            return Collections.emptyList();
        List<GraphDto> result = new ArrayList<>();
        parent.getElements().forEach(item -> {
            if (!OWL.THING.equals(item.asIriRef()))
                result.add(new GraphDto().setIri(item.asIriRef().getIri()).setName(item.asIriRef().getName())
                        .setPropertyType(RDFS.SUBCLASSOF.getName()));
        });

        return result;
    }

    public List<TTIriRef> getDefinitionSubTypes(String iri) {

        return entityTripleRepository.findImmediateChildrenByIri(iri, null, null, null, false).stream()
                .map(t -> new TTIriRef(t.getIri(), t.getName())).collect(Collectors.toList());
    }

    public EntityDefinitionDto getEntityDefinitionDto(String iri) {
        TTEntity entity = getBundle(iri, Set.of(RDFS.SUBCLASSOF.getIri(), RDF.TYPE.getIri(), RDFS.LABEL.getIri(), RDFS.COMMENT.getIri(), IM.HAS_STATUS.getIri())).getEntity();
        List<TTIriRef> types = entity.getType() == null ? new ArrayList<>()
                : entity.getType().getElements().stream()
                .map(t -> new TTIriRef(t.asIriRef().getIri(), t.asIriRef().getName()))
                .collect(Collectors.toList());

        List<TTIriRef> isa = !entity.has(RDFS.SUBCLASSOF) ? new ArrayList<>()
                : entity.get(RDFS.SUBCLASSOF).getElements().stream()
                .map(t -> new TTIriRef(t.asIriRef().getIri(), t.asIriRef().getName()))
                .collect(Collectors.toList());

        return new EntityDefinitionDto().setIri(entity.getIri()).setName(entity.getName())
                .setDescription(entity.getDescription())
                .setStatus(entity.getStatus() == null ? null : entity.getStatus().getName()).setTypes(types)
                .setSubtypes(getDefinitionSubTypes(iri)).setIsa(isa);
    }

    public SearchResultSummary getSummary(String iri) {
        return entityRepository.getEntitySummaryByIri(iri);
    }

    public TTEntity getConceptShape(String iri) {
        if (iri == null || iri.isEmpty())
            return null;
        TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY.getIri(), SHACL.OR.getIri(), RDF.TYPE.getIri())).getEntity();
        TTArray value = entity.get(RDF.TYPE);
        if (!value.getElements().contains(SHACL.NODESHAPE)) {
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
            predicates.add(IM.HAS_MEMBER.getIri());
        } catch (Exception e) {
            LOG.warn("Error getting inferredPredicates config, reverting to default", e);
        }

        if (predicates == null) {
            LOG.warn("Config for inferredPredicates not set, reverting to default");
            predicates = new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri(), IM.HAS_MEMBER.getIri()));
        }

        return getBundleByPredicateExclusions(iri, predicates);
    }

    public TTDocument getConcept(String iri) {
        TTBundle bundle = getBundle(iri, null);
        TTDocument document = new TTDocument();
        List<Namespace> namespaces = entityTripleRepository.findNamespaces();
        TTContext context = new TTContext();
        for (Namespace namespace : namespaces) {
            context.add(namespace.getIri(), namespace.getPrefix(), namespace.getName());
        }
        document.setContext(context);
        document.addEntity(bundle.getEntity());
        return document;
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

    public TTDocument getConceptListByGraph(String iri) {
        List<String> conceptIris = entityTripleRepository.getConceptIrisByGraph(iri);
        return getConceptList(conceptIris);
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

    public XSSFWorkbook getSetExport(String iri, boolean core, boolean legacy, boolean flat) throws DataFormatException, JsonProcessingException {
        if (iri == null || "".equals(iri)) {
            return null;
        }
        return new ExcelSetExporter().getSetAsExcel(iri, core, legacy, flat);
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
        TTEntity entity = getBundle(iri, new HashSet<>(List.of(RDFS.LABEL.getIri()))).getEntity();
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

    public List<TTIriRef> getPathBetweenNodes(String descendant, String ancestor) {
        return entityRepository.getPathBetweenNodes(descendant, ancestor);
    }

    public List<TTIriRef> getUnassigned() {
        List<TTIriRef> unassignedList = new ArrayList<>();
        for (TTIriRef unmapped : entityRepository2.findUnassigned()) {
            unassignedList.add(new TTIriRef().setIri(unmapped.getIri()).setName(unmapped.getName()));
        }
        return unassignedList;
    }

    public List<TTEntity> getUnmapped(String term, List<String> status, List<String> scheme, List<String> type, Integer usage, Integer limit) {
        List<TTEntity> unmappedList = new ArrayList<>();
        if (term.isBlank()) {
            return entityRepository2.findUnmapped(status, scheme, type, usage, limit);
        }
        return unmappedList;
    }

    public List<TTIriRef> getUnclassified() {
        List<TTIriRef> unclassifiedList = new ArrayList<>();
        for (TTIriRef unmapped : entityRepository2.findUnclassified()) {
            unclassifiedList.add(new TTIriRef().setIri(unmapped.getIri()).setName(unmapped.getName()));
        }
        return unclassifiedList;
    }

    public List<TTEntity> getMappingSuggestions(String iri, String name) {
        List<TTEntity> suggestions = entityRepository.findEntitiesByName(name);
        suggestions.removeIf(iriRef -> iriRef.getIri().equals(iri));
        return suggestions;
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
        TTIriRef graph = iri(IM.GRAPH_DISCOVERY.getIri(), IM.GRAPH_DISCOVERY.getName());
        entity.setCrud(IM.ADD_QUADS).setVersion(1);
        filerService.fileEntity(entity, graph, agentName, null);
        return entity;
    }

    public TTEntity updateEntity(TTEntity entity, String agentName) throws TTFilerException, JsonProcessingException {
        EntityValidator validator = new EntityValidator();
        validator.isValid(entity, this, "Update");
        TTIriRef graph = iri(IM.GRAPH_DISCOVERY.getIri(), IM.GRAPH_DISCOVERY.getName());
        entity.setCrud(IM.UPDATE_ALL);
        TTEntity usedEntity = getFullEntity(entity.getIri()).getEntity();
        entity.setVersion(usedEntity.getVersion() + 1);
        filerService.fileEntity(entity, graph, agentName, usedEntity);
        return entity;
    }

    public TTEntity addConceptToTask(String entityIri, String taskIri, String agentName) throws Exception {
        TTEntity entity = getBundleByPredicateExclusions(entityIri, null).getEntity();
        if (entity.get(IM.IN_TASK) == null) {
            entity.set(IM.IN_TASK, new TTArray());
        }
        entity.get(IM.IN_TASK).add(iri(taskIri));
        filerService.fileTransactionDocument(new TTDocument().addEntity(entity).setCrud(IM.UPDATE_ALL).setGraph(IM.GRAPH), agentName);
        return getBundleByPredicateExclusions(entity.getIri(), null).getEntity();
    }


    public TTEntity removeConceptFromTask(String taskIri, String removedActionIri, String agentName) throws Exception {
        TTEntity entity = getBundleByPredicateExclusions(removedActionIri, null).getEntity();
        entity.set(IM.IN_TASK, entityRepository2.findFilteredInTask(removedActionIri, taskIri));
        filerService.fileTransactionDocument(new TTDocument().addEntity(entity).setCrud(IM.UPDATE_ALL).setGraph(IM.GRAPH), agentName);
        return getBundleByPredicateExclusions(entity.getIri(), null).getEntity();
    }

    public List<TTEntity> saveMapping(Map<String, List<String>> mappings, String agentName) throws Exception {
        List<TTEntity> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : mappings.entrySet()) {
            TTEntity entity = getBundleByPredicateExclusions(entry.getKey(), null).getEntity();
            if (entity.has(IM.HAS_STATUS)) {
                entity.get(IM.HAS_STATUS).remove(IM.UNASSIGNED);
            }
            entity.set(IM.MATCHED_TO, new TTArray());
            for (String iri : entry.getValue()) {
                entity.get(IM.MATCHED_TO).add(iri(iri));
            }
            filerService.fileTransactionDocument(new TTDocument().addEntity(entity).setCrud(IM.UPDATE_ALL).setGraph(IM.GRAPH), agentName);
            result.add(getBundleByPredicateExclusions(entity.getIri(), null).getEntity());
        }

        return result;
    }

    public TTIriRef getShapeFromType(String iri) {
        if (null != iri) {
            if (iri.equals(IM.CONCEPT_SET.getIri()) || iri.equals(IM.VALUESET.getIri()))
                return entityTripleRepository.getShapeFromType(IM.SET.getIri());
            else return entityTripleRepository.getShapeFromType(iri);
        } else return null;
    }

    public List<TTEntity> getActions(String taskIri) {
        return entityRepository2.getActions(taskIri);
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

    public List<TTIriRef> getClasses() {
        return entityRepository.getClasses();
    }

    public List<TTIriRef> getStatuses() {
        return entityRepository.getStatuses();
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

    public Boolean isValidProperty(String entity, String property) {
        return entityRepository.isValidProperty(entity, property);
    }

    public Boolean isValidPropertyValue(String property, String value) {
        return entityRepository.isValidPropertyValue(property, value);
    }

    public Pageable<EntityReferenceNode> getSuperiorPropertiesPaged(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
        if (null == iri || iri.isEmpty()) return null;

        int rowNumber = 0;
        if (null != page && null != size) rowNumber = (page - 1) * size;


        Pageable<TTIriRef> propertiesAndCount = entityTripleRepository.getSuperiorPropertiesByConceptPagedWithTotalCount(iri,schemeIris,rowNumber,size,inactive);
        return iriRefPageableToEntityReferenceNodePageable(propertiesAndCount,schemeIris,inactive);
    }

    public Pageable<EntityReferenceNode> getSuperiorPropertyValuesPaged(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
        if (null == iri || iri.isEmpty()) return null;

        int rowNumber = 0;
        if (null != page && null != size) rowNumber = (page - 1) * size;


        Pageable<TTIriRef> propertiesAndCount = entityTripleRepository.getSuperiorPropertyValuesByPropertyPagedWithTotalCount(iri,schemeIris,rowNumber,size,inactive);
        return iriRefPageableToEntityReferenceNodePageable(propertiesAndCount,schemeIris,inactive);
    }

    private Pageable<EntityReferenceNode> iriRefPageableToEntityReferenceNodePageable(Pageable<TTIriRef> iriRefPageable,List<String> schemeIris,boolean inactive) {
        Pageable<EntityReferenceNode> result = new Pageable<>();
        result.setTotalCount(iriRefPageable.getTotalCount());
        List<EntityReferenceNode> nodes = new ArrayList<>();
        for (TTIriRef p : iriRefPageable.getResult()) {
            EntityReferenceNode node = new EntityReferenceNode();
            node.setIri(p.getIri());
            node.setName(p.getName());
            node.setType(entityTypeRepository.getEntityTypes(p.getIri()));
            node.setHasChildren(entityTripleRepository.hasChildren(p.getIri(),schemeIris,inactive));
            node.setHasGrandChildren(entityTripleRepository.hasGrandChildren(p.getIri(),schemeIris,inactive));
            nodes.add(node);
        }
        result.setResult(nodes);
        return result;
    }
}

