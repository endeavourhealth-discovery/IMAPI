package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.logic.exporters.ExcelSetExporter;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.GraphDto.GraphType;
import org.endeavourhealth.imapi.model.dto.ParentDto;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.dto.UnassignedEntity;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.*;
import org.endeavourhealth.imapi.validators.EntityValidator;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.imapi.transforms.TTToECL;
import org.endeavourhealth.imapi.transforms.TTToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    public static final int UNLIMITED = 0;
    public static final int MAX_CHILDREN = 200;

    private EntityRepository entityRepository = new EntityRepository();
    private EntityTctRepository entityTctRepository = new EntityTctRepository();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
    private SetRepository setRepository = new SetRepository();
    private TermCodeRepository termCodeRepository = new TermCodeRepository();
    private EntityTypeRepository entityTypeRepository = new EntityTypeRepository();
    private ConfigManager configManager = new ConfigManager();
    private EntityRepository2 entityRepository2 = new EntityRepository2();

    public TTBundle getBundle(String iri, Set<String> predicates, int limit) {
        return entityRepository2.getBundle(iri, predicates);
    }

    public TTBundle getEntityByPredicateExclusions(String iri, Set<String> excludePredicates) {
        TTBundle bundle = entityRepository2.getBundle(iri, excludePredicates, true);
        if (excludePredicates != null && excludePredicates.contains(RDFS.LABEL.getIri())) {
            Map<String, String> filtered = bundle.getPredicates().entrySet().stream()
                    .filter(entry -> !entry.getKey().equals(RDFS.LABEL.getIri()) && entry.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            bundle.setPredicates(filtered);
            bundle.getEntity().set(RDFS.LABEL, (TTValue) null);
        }
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
            EntityReferenceNode node = new EntityReferenceNode();
            node.setIri(c.getIri()).setName(c.getName());
            node.setType(entityTypeRepository.getEntityTypes(c.getIri()));
            node.setHasChildren(entityTripleRepository.hasChildren(c.getIri(), schemeIris, inactive));
            node.setHasGrandChildren(entityTripleRepository.hasGrandChildren(c.getIri(), schemeIris, inactive));
            result.add(node);
        }

        return result;
    }

    public Pageable<TTIriRef> getImmediateChildrenWithCount(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
        if (iri == null || iri.isEmpty())
            return null;

        int rowNumber = 0;
        if (page != null && size != null)
            rowNumber = (page - 1) * 10;

        return entityTripleRepository.findImmediateChildrenByIriWithCount(iri, schemeIris,rowNumber, size, inactive);
    }

    public Pageable<TTIriRef> getPartialWithTotalCount(String iri,String predicateList, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
        if (iri == null || iri.isEmpty())
            return null;

        int rowNumber = 0;
        if (page != null && size != null)
            rowNumber = (page - 1) * 10;

        return entityTripleRepository.findPartialWithTotalCount(iri,predicateList, schemeIris,rowNumber, size, inactive);
    }

    public ExportValueSet getHasMember(String iri,String predicateList, List<String> schemeIris, Integer page, Integer size, boolean inactive) {

        List<TTIriRef> hasMembers = getPartialWithTotalCount(iri,predicateList, schemeIris,page, size, inactive).getResult();
        TTArray array = new TTArray();
        for(TTIriRef member: hasMembers){
            array.add(member);
        }
        Set<ValueSetMember> members = new HashSet<>();
        members.add(getValueSetMemberFromArray(array, true));
        for(ValueSetMember valueSetMember:members){
            valueSetMember.setLabel("a_MemberIncluded");
            valueSetMember.setType(MemberType.INCLUDED_SELF);
            valueSetMember.setDirectParent(new TTIriRef().setIri(iri).setName(getEntityReference(iri).getName()));
        }
        ExportValueSet result = new ExportValueSet().setValueSet(getEntityReference(iri));

        Map<String, ValueSetMember> processedMembers = processMembers(members, false, 0, 2000);

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

        for (TTIriRef usage : usageRefs) {
            TTArray type = getBundle(usage.getIri(), Collections.singleton(RDF.TYPE.getIri()), 0).getEntity().getType();
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

    public List<SearchResultSummary> advancedSearch(SearchRequest request) throws URISyntaxException, IOException, InterruptedException, ExecutionException, OpenSearchException, DataFormatException {
        SearchService searchService = new SearchService();
        return searchService.getEntitiesByTerm(request);

    }

    public ExportValueSet getValueSetMembers(String iri, boolean expandMembers, boolean expandSets, Integer limit, boolean withHyperlinks) {
        return getValueSetMembers(iri, expandMembers, expandSets, limit, withHyperlinks, null, iri);
    }

    public ExportValueSet getValueSetMembers(String iri, boolean expandMembers, boolean expandSets, Integer limit, boolean withHyperlinks, String parentSetName, String originalParentIri) {
        if (iri == null || iri.isEmpty()) {
            return null;
        }
        ExportValueSet result = new ExportValueSet().setValueSet(getEntityReference(iri));
        int memberCount = 0;

        Set<ValueSetMember> definedMemberInclusions = getDefinedInclusions(iri, expandSets, withHyperlinks, parentSetName, originalParentIri);


        Map<String, ValueSetMember> evaluatedMemberInclusions = processMembers(definedMemberInclusions, expandMembers, memberCount, limit);

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
        TTArray included = getBundle(iri, definition, UNLIMITED)
                .getEntity()
                .get(IM.DEFINITION.asIriRef());
        result.setIri(valueSet.getIri());
        result.setName(valueSet.getName());
        result.setIncluded(included);
        return result;
    }

    private int processExpansions(boolean expandMembers, boolean expandSets, Integer limit, boolean withHyperlinks, String parentSetName, String originalParentIri, ExportValueSet result, int memberCount, Set<ValueSetMember> definedSetInclusions) {
        if (expandSets || expandMembers) {
            for (ValueSetMember set : definedSetInclusions) {
                ExportValueSet individualResults = getValueSetMembers(set.getEntity().getIri(), expandMembers, expandSets, limit, withHyperlinks, null, originalParentIri);
                memberCount += individualResults.getMembers().size();
                result.addAllMembers(individualResults.getMembers());
            }
        } else {
            for (ValueSetMember set : definedSetInclusions) {
                if (parentSetName == null) {
                    set.setLabel("Subset - " + set.getEntity().getName());
                } else {
                    set.setLabel("Subset - " + parentSetName);
                }
                ExportValueSet setMembers = getValueSetMembers(set.getEntity().getIri(), false, false, limit, withHyperlinks, set.getEntity().asIriRef().getName(), originalParentIri);
                memberCount += setMembers.getMembers().size();
                result.addAllMembers(setMembers.getMembers());
            }
        }
        return memberCount;
    }

    private Set<ValueSetMember> getDefinedInclusions(String iri, boolean expandSets, boolean withHyperlinks, String parentSetName, String originalParentIri) {
        Set<ValueSetMember> definedMemberInclusions = getMember(iri, Set.of(IM.DEFINITION.getIri()), withHyperlinks);
        for (ValueSetMember included : definedMemberInclusions) {
            if (originalParentIri.equals(iri)) {
                included.setLabel("a_MemberIncluded");
                if(direct){
                    included.setType(MemberType.INCLUDED_SELF);
                }else{
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
        return definedMemberInclusions;
    }


    private Set<ValueSetMember> getMember(String iri, Set<String> predicates, boolean withHyperlinks) {
        Set<ValueSetMember> members = new HashSet<>();

        TTBundle bundle = getBundle(iri, predicates, UNLIMITED);
        List<TTIriRef> hasMembers = entityTripleRepository.findPartialWithTotalCount(iri,IM.HAS_MEMBER.getIri(),null,0,10,false).getResult();
        TTArray result = new TTArray();

        if(hasMembers.size() != 0){
            for(TTIriRef member: hasMembers){
                result.add(member);
            }
            direct = true;
        } else {
            result = bundle.getEntity().get(IM.DEFINITION.asIriRef());
        }

        if (result != null) {
            if (result.isIriRef())
                members.add(getValueSetMemberFromIri(result.asIriRef(), withHyperlinks));
            else if (result.isNode())
                members.add(getValueSetMemberFromNode(result.asNode(), withHyperlinks));
            else {
                if(direct){
                    members.add(getValueSetMemberFromArray(result, withHyperlinks));
                }
                else{
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
        return members;
    }

    private ValueSetMember getValueSetMemberFromArray(TTArray result, boolean withHyperlinks) {
        ValueSetMember member = new ValueSetMember();
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

    private ValueSetMember getValueSetMemberFromNode(TTValue node, boolean withHyperlinks) {
        ValueSetMember member = new ValueSetMember();
        Map<String, String> defaultPredicates = getDefaultPredicateNames();
        List<String> blockedIris = getBlockedIris();
        String nodeAsString = TTToString.ttValueToString(node.asNode(), "object", defaultPredicates, 0, withHyperlinks, blockedIris);
        member.setEntity(iri("", nodeAsString));
        return member;
    }

    private ValueSetMember getValueSetMemberFromIri(TTIriRef iri, boolean withHyperlinks) {
        ValueSetMember member = new ValueSetMember();
        List<String> blockedIris = getBlockedIris();
        SearchResultSummary summary = entityRepository.getEntitySummaryByIri(iri.getIri());
        String iriAsString = TTToString.ttIriToString(iri, "object", 0, withHyperlinks, false, blockedIris);
        member.setEntity(iri(iri.getIri(), iriAsString));
        member.setCode(summary.getCode());
        member.setScheme(summary.getScheme());
        return member;
    }

    private Map<String, ValueSetMember> processMembers(Set<ValueSetMember> valueSetMembers, boolean expand, Integer memberCount, Integer limit) {
        Map<String, ValueSetMember> memberHashMap = new HashMap<>();
        for (ValueSetMember member : valueSetMembers) {

            if (limit != null && (memberCount + memberHashMap.size()) > limit) return memberHashMap;

            memberHashMap.put(member.getEntity().getIri() + "/" + member.getCode(), member);
            if (expand) {
                setRepository
                        .expandMember(member.getEntity().getIri(), limit)
                        .forEach(m -> {
                            m.setLabel("MemberExpanded");
                            m.setType(MemberType.EXPANDED);
                            memberHashMap.put(m.getEntity().getIri() + "/" + m.getCode(), m);
                        });
            }
        }
        return memberHashMap;
    }

    public ValueSetMembership isValuesetMember(String valueSetIri, String memberIri) {
        if (valueSetIri == null || valueSetIri.isEmpty() || memberIri == null || memberIri.isEmpty())
            return null;
        ValueSetMembership result = new ValueSetMembership();
        Set<TTIriRef> included = getMemberIriRefs(valueSetIri, IM.DEFINITION);

        for (TTIriRef m : included) {
            Optional<ValueSetMember> match = setRepository.expandMember(m.getIri()).stream()
                    .filter(em -> em.getEntity().asIriRef().getIri().equals(memberIri)).findFirst();
            if (match.isPresent()) {
                result.setIncludedBy(m);
                break;
            }
        }
        return result;
    }

    private Set<TTIriRef> getMemberIriRefs(String valueSetIri, TTIriRef predicate) {
        return entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(valueSetIri, predicate.getIri());

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
        return getBundle(iri, new HashSet<>(predicates), UNLIMITED).getEntity();
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
            downloadDto.setInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri())), UNLIMITED).getEntity());
        }
        if (params.includeProperties()) {
            downloadDto.setDataModelProperties(getDataModelProperties(iri));
        }
        if (params.includeMembers()) {
            downloadDto.setMembers(getValueSetMembers(iri, params.expandMembers(), params.expandSubsets(), null, false));
        }
        if (params.includeTerms()) {
            downloadDto.setTerms(getEntityTermCodes(iri));
        }
        if (params.includeIsChildOf()) {
            downloadDto.setIsChildOf(getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF.getIri())), UNLIMITED).getEntity().get(IM.IS_CHILD_OF));
        }
        if (params.includeHasChildren()) {
            downloadDto.setHasChildren(getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN.getIri())), UNLIMITED).getEntity().get(IM.HAS_CHILDREN));
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
            xls.addInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri())), UNLIMITED));
        }
        if (params.includeProperties()) {
            xls.addDataModelProperties(getDataModelProperties(iri));
        }
        if (params.includeMembers()) {
            xls.addMembersSheet(getValueSetMembers(iri, params.expandMembers(), params.expandSubsets(), null, false));
        }
        if (params.includeTerms()) {
            xls.addTerms(getEntityTermCodes(iri));
        }
        TTEntity isChildOfEntity = getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF.getIri())), UNLIMITED).getEntity();
        TTArray isChildOfData = isChildOfEntity.get(TTIriRef.iri(IM.IS_CHILD_OF.getIri(), IM.IS_CHILD_OF.getName()));
        if (params.includeIsChildOf() && isChildOfData != null) {
            xls.addIsChildOf(isChildOfData.getElements());
        }
        TTEntity hasChildrenEntity = getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN.getIri())), UNLIMITED).getEntity();
        TTArray hasChildrenData = hasChildrenEntity.get(TTIriRef.iri(IM.HAS_CHILDREN.getIri(), IM.HAS_CHILDREN.getName()));
        if (params.includeHasChildren() && hasChildrenData != null) {
            xls.addHasChildren(hasChildrenData.getElements());
        }

        return xls;
    }

    public List<DataModelProperty> getDataModelProperties(String iri) {
        TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY.getIri(), RDFS.LABEL.getIri()), UNLIMITED).getEntity();
        return getDataModelProperties(entity);
    }

    public List<DataModelProperty> getDataModelProperties(TTEntity entity) {
        List<DataModelProperty> properties = new ArrayList<>();
        if (entity == null)
            return Collections.emptyList();
        if (entity.has(SHACL.PROPERTY)) {
            getDataModelPropertyGroups(entity, properties);
        }
        return properties;
    }

    private void getDataModelPropertyGroups(TTEntity entity, List<DataModelProperty> properties) {
        for (TTValue propertyGroup : entity.get(SHACL.PROPERTY).iterator()) {
            if (propertyGroup.isNode()) {
                TTIriRef inheritedFrom = propertyGroup.asNode().has(RDFS.DOMAIN)
                        ? propertyGroup.asNode().get(RDFS.DOMAIN).asIriRef()
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
        if (property.asNode().has(SHACL.DATATYPE))
            pv.setType(property.asNode().get(SHACL.DATATYPE).asIriRef());
        if (property.asNode().has(SHACL.FUNCTION))
            pv.setType(property.asNode().get(SHACL.FUNCTION).asIriRef());
        if (property.asNode().has(SHACL.MAXCOUNT))
            pv.setMaxExclusive(property.asNode().get(SHACL.MAXCOUNT).asLiteral().getValue());
        if (property.asNode().has(SHACL.MINCOUNT))
            pv.setMinExclusive(property.asNode().get(SHACL.MINCOUNT).asLiteral().getValue());
        return pv;
    }

    public String valueSetMembersCSV(String iri, boolean expandMember, boolean expandSubset) {
        ExportValueSet exportValueSet = getValueSetMembers(iri, expandMember, expandSubset, null, false);
        StringBuilder valueSetMembers = new StringBuilder();
        valueSetMembers.append("Inc\\Exc\\IncSubset\tValueSetIri\tValueSetName\tMemberIri\tMemberTerm\tMemberCode\tMemberSchemeIri\tMemberSchemeName\n");

        if (exportValueSet == null)
            return valueSetMembers.toString();

        for (ValueSetMember inc : exportValueSet.getMembers()) {
            appendValueSet(exportValueSet, valueSetMembers, inc);
        }

        return valueSetMembers.toString();
    }

    private void appendValueSet(ExportValueSet exportValueSet, StringBuilder valueSetMembers, ValueSetMember setMember) {
        valueSetMembers.append("Inc").append("\t").append(exportValueSet.getValueSet().getIri()).append("\t")
                .append(exportValueSet.getValueSet().getName()).append("\t").append(setMember.getEntity().asIriRef().getIri())
                .append("\t").append(setMember.getEntity().asIriRef().getName()).append("\t").append(setMember.getCode())
                .append("\t");
        if (setMember.getScheme() != null)
            valueSetMembers.append(setMember.getScheme().getIri()).append("\t").append(setMember.getScheme().getName());

        valueSetMembers.append("\n");
    }

    public GraphDto getGraphData(String iri) {
        TTEntity entity = getBundle(iri, Set.of(RDFS.SUBCLASSOF.getIri(), RDFS.LABEL.getIri()), UNLIMITED).getEntity();

        GraphDto graphData = new GraphDto().setKey("0").setIri(entity.getIri()).setName(entity.getName());
        GraphDto graphParents = new GraphDto().setKey("0_0").setName("Is a");
        GraphDto graphChildren = new GraphDto().setKey("0_1").setName("Subtypes");

        TTBundle axioms = getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri())), UNLIMITED);
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
        TTEntity entity = getBundle(iri, Set.of(RDFS.SUBCLASSOF.getIri(), RDF.TYPE.getIri(), RDFS.LABEL.getIri(), RDFS.COMMENT.getIri(), IM.HAS_STATUS.getIri()), UNLIMITED).getEntity();
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
        TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY.getIri(), SHACL.OR.getIri(), RDF.TYPE.getIri()), UNLIMITED).getEntity();
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

        return getEntityByPredicateExclusions(iri, predicates);
    }

    public TTDocument getConcept(String iri) {
        TTBundle bundle = getBundle(iri, null, 0);
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
            TTBundle bundle = getBundle(iri, null, 0);
            document.addEntity(bundle.getEntity());
        }
        return document;
    }

    public TTDocument getConceptListByGraph(String iri) {
        List<String> conceptIris = entityTripleRepository.getConceptIrisByGraph(iri);
        return getConceptList(conceptIris);
    }

    public List<SimpleMap> getSimpleMaps(String iri) {
        if (iri == null || iri.equals("")) return new ArrayList<>();
        String scheme = iri.substring(0, iri.indexOf("#") + 1);
        List<Namespace> namespaces = getNamespaces();
        List<String> schemes = namespaces.stream().map(Namespace::getIri).collect(Collectors.toList());
        schemes.remove(scheme);
        return entityTripleRepository.findSimpleMapsByIri(iri, schemes);
    }

    public String getEcl(TTBundle inferred) throws DataFormatException {
        if (inferred == null) throw new DataFormatException("Missing data for ECL conversion");
        if (inferred.getEntity() == null || inferred.getEntity().asNode().getPredicateMap().isEmpty())
            throw new DataFormatException("Missing entity bundle definition for ECL conversion");
        return TTToECL.getExpressionConstraint(inferred.getEntity(), true);
    }

    public XSSFWorkbook getSetExport(String iri, boolean legacy) throws DataFormatException {
        if (iri == null || "".equals(iri)) {
            return null;
        }
        return new ExcelSetExporter().getSetAsExcel(iri, legacy);
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
        TTEntity entity = getBundle(iri, new HashSet<>(List.of(RDFS.LABEL.getIri())), 0).getEntity();
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

    public EntityReferenceNode getParentHierarchy(String iri) {
        TTEntity child = getBundle(iri, new HashSet<>(List.of(RDFS.LABEL.getIri(), RDF.TYPE.getIri(), IM.IS_CONTAINED_IN.getIri(), RDFS.SUBCLASSOF.getIri(), IM.IS_CHILD_OF.getIri(), RDFS.SUBPROPERTYOF.getIri())), 0).getEntity();
        EntityReferenceNode childNode = new EntityReferenceNode(child.getIri(), child.getName(), child.getType());

        if (child.has(IM.IS_CONTAINED_IN)) {
            for (TTValue ttvalue : child.get(IM.IS_CONTAINED_IN).getElements()) {
                childNode.addParent(new EntityReferenceNode(ttvalue.asIriRef().getIri()));
            }
        }

        if (child.has(RDFS.SUBCLASSOF)) {
            for (TTValue ttvalue : child.get(RDFS.SUBCLASSOF).getElements()) {
                childNode.addParent(new EntityReferenceNode(ttvalue.asIriRef().getIri()));
            }
        }

        if (child.has(IM.IS_CHILD_OF)) {
            for (TTValue ttvalue : child.get(IM.IS_CHILD_OF).getElements()) {
                childNode.addParent(new EntityReferenceNode(ttvalue.asIriRef().getIri()));
            }
        }

        if (child.has(RDFS.SUBPROPERTYOF)) {
            for (TTValue ttvalue : child.get(RDFS.SUBPROPERTYOF).getElements()) {
                childNode.addParent(new EntityReferenceNode(ttvalue.asIriRef().getIri()));
            }
        }


        getParentHierarchyRecursive(childNode);

        return childNode;
    }

    private void getParentHierarchyRecursive(EntityReferenceNode child) {
        if (child.getParents() != null && !child.getParents().isEmpty()) {
            for (EntityReferenceNode parent : child.getParents()) {
                TTEntity parentEntity = getBundle(parent.getIri(), new HashSet<>(List.of(RDFS.LABEL.getIri(), RDF.TYPE.getIri(), IM.IS_CONTAINED_IN.getIri())), 0).getEntity();
                EntityReferenceNode parentNode = new EntityReferenceNode(parentEntity.getIri(), parentEntity.getName(), parentEntity.getType());
                EntityReferenceNode parentNodeRef = child.getParents().stream().filter(childParent -> childParent.getIri().equals(parentNode.getIri())).findFirst().orElse(null);
                if (parentNodeRef != null) {
                    parentNodeRef.setType(parentNode.getType()).setParents(parentNode.getParents()).setName(parentNode.getName());
                } else {
                    child.addParent(parentNode);
                }
                if (parentEntity.get(IM.IS_CONTAINED_IN) != null) {
                    for (TTValue ttvalue : parentEntity.get(IM.IS_CONTAINED_IN).getElements()) {
                        parentNode.addParent(new EntityReferenceNode(ttvalue.asIriRef().getIri()));
                    }

                    getParentHierarchyRecursive(parentNode);
                }
            }
        }
    }

    public List<TTIriRef> getPathBetweenNodes(String descendant, String ancestor) {
        return entityRepository.getPathBetweenNodes(descendant, ancestor);
    }

    public List<UnassignedEntity> getUnassigned() {
        List<UnassignedEntity> unassignedList = new ArrayList<>();
        for (TTIriRef unassigned : entityRepository2.findUnassigned()) {
            unassignedList.add(new UnassignedEntity().setIri(unassigned.getIri()).setName(unassigned.getName()).setSuggestions(new ArrayList<>()));
        }
        return unassignedList;
    }

    public List<TTIriRef> getMappingSuggestions(String iri, String name) {
        List<TTIriRef> iriRefs = entityRepository.findEntitiesByName(name);
        iriRefs.removeIf(iriRef -> iriRef.getIri().equals(iri));
        return iriRefs;
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
                List<TTIriRef> path =  new ArrayList<>(currentPath);
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
        if (parents.size() != 0) {
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

        if (paths.size() != 0) {
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
        Boolean result = entityRepository.iriExists(iri);
        return result;
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
}

