package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.opensearch.*;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.imapi.transforms.TTToECL;
import org.endeavourhealth.imapi.transforms.TTToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class EntityService {
    private static final Logger LOG = LoggerFactory.getLogger(EntityService.class);
	private static final String XML_SCHEME_DATA_TYPES = "xmlSchemaDataTypes";

    public static final int UNLIMITED = 0;
    public static final int MAX_CHILDREN = 100;

	private EntityRepository entityRepository = new EntityRepository();
    private EntityTctRepository entityTctRepository = new EntityTctRepository();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
    private SetRepository setRepository = new SetRepository();
    private TermCodeRepository termCodeRepository = new TermCodeRepository();
    private EntityTypeRepository entityTypeRepository = new EntityTypeRepository();
    private ConfigService configService = new ConfigService();
    private EntityRepository2 entityRepository2 = new EntityRepository2();



	public TTBundle getBundle(String iri, Set<String> predicates,int limit) {
        return entityRepository2.getBundle(iri, predicates);
    }

	public TTBundle getEntityByPredicateExclusions(String iri, Set<String> excludePredicates,int limit) {
        return entityRepository2.getBundle(iri,excludePredicates, true);
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

		for (TTIriRef c: getChildren(iri, schemeIris, rowNumber, pageSize, inactive)) {
		    EntityReferenceNode node = new EntityReferenceNode();
		    node.setIri(c.getIri()).setName(c.getName());
		    node.setType(entityTypeRepository.getEntityTypes(c.getIri()));
		    node.setHasChildren(entityTripleRepository.hasChildren(c.getIri(), schemeIris, inactive));
		    result.add(node);
        }

		return result;
	}

	private List<TTIriRef> getChildren(String iri, List<String> schemeIris, int rowNumber, Integer pageSize, boolean inactive) {
		return entityTripleRepository.findImmediateChildrenByIri(iri,schemeIris, rowNumber, pageSize, inactive);
	}

	public List<EntityReferenceNode> getImmediateParents(String iri, List<String> schemeIris, Integer pageIndex, Integer pageSize,
			boolean inactive) {

		if (iri == null || iri.isEmpty())
			return Collections.emptyList();

		int rowNumber = 0;
		if (pageIndex != null && pageSize != null)
			rowNumber = (pageIndex - 1) * pageSize;

		List<EntityReferenceNode> parents = getParents(iri,schemeIris, rowNumber, pageSize, inactive).stream()
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

	public List<TTIriRef> usages(String iri, Integer pageIndex, Integer pageSize) throws JsonProcessingException {

		if (iri == null || iri.isEmpty())
			return Collections.emptyList();

		List<String> xmlDataTypes = configService.getConfig(XML_SCHEME_DATA_TYPES, new TypeReference<>() {});
		if (xmlDataTypes != null && xmlDataTypes.contains(iri))
			return Collections.emptyList();

		int rowNumber = 0;
		if (pageIndex != null && pageSize != null)
			rowNumber = pageIndex * pageSize;

		return entityTripleRepository.getActiveSubjectByObjectExcludeByPredicate(iri, rowNumber, pageSize, RDFS.SUBCLASSOF.getIri()).stream()
				.sorted(Comparator.comparing(TTIriRef::getName, Comparator.nullsLast(Comparator.naturalOrder())))
				.distinct().collect(Collectors.toList());
	}

	public Integer totalRecords(String iri) throws JsonProcessingException {
		if (iri == null || iri.isEmpty())
			return 0;

		List<String> xmlDataTypes = configService.getConfig(XML_SCHEME_DATA_TYPES, new TypeReference<>() {});
		if (xmlDataTypes != null && xmlDataTypes.contains(iri))
			return 0;

		return entityTripleRepository.getCountOfActiveSubjectByObjectExcludeByPredicate(iri,RDFS.SUBCLASSOF.getIri());
	}

	public List<SearchResultSummary> advancedSearch(SearchRequest request) throws URISyntaxException, IOException, InterruptedException, ExecutionException, OpenSearchException{

		if (request == null || request.getTermFilter() == null || request.getTermFilter().isEmpty())
			return Collections.emptyList();

		Query osQuery = new Query();
		Request osRequest = new Request(request.getSize());
		for (String term : request.getTermFilter().split(" ")) {
			osQuery.addMust(term);
		}

		Filter schemeFilter = new Filter(1);
		for (String scheme : request.getSchemeFilter()) {
			schemeFilter.addShould(new SchemeId(scheme));
		}
		osQuery.addFilter(schemeFilter);

		Filter statusFilter = new Filter(1);
		for (String status : request.getStatusFilter()) {
			statusFilter.addShould(new StatusId(status));
		}
		osQuery.addFilter(statusFilter);

		Filter typeFilter = new Filter(1);
		for (String type : request.getTypeFilter()) {
			typeFilter.addShould(new TypeId(type));
		}
		osQuery.addFilter(typeFilter);

		osRequest.setQuery(osQuery);
		ObjectMapper mapper = new ObjectMapper();
		String osRequestAsString = mapper.writeValueAsString(osRequest);

		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(new URI(System.getenv("OPENSEARCH_URL")))
//				.timeout(Duration.of(10, ChronoUnit.SECONDS))
				.header("Authorization", "Basic " + System.getenv("OPENSEARCH_AUTH"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(osRequestAsString))
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client
				.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
				.thenApply(res -> res)
				.get();

		if (299 < response.statusCode()) {
			LOG.debug("Open search request failed with code: " + response.statusCode());
			throw new OpenSearchException("Search request failed. Error connecting to opensearch.");
		}

		ObjectMapper resultMapper = new ObjectMapper();
		JsonNode root = resultMapper.readTree(response.body());
		List<SearchResultSummary> searchResults = new ArrayList<>();
		for (JsonNode hit : root.get("hits").get("hits")) {
			SearchResultSummary source = resultMapper.treeToValue(hit.get("_source"), SearchResultSummary.class);
			TTArray types = new TTArray();
			for (JsonNode type : hit.get("_source").get("entityType")) {
				types.add(new TTIriRef(type.get("@id").asText(), type.get("name").asText()));
			}
			source.setEntityType(types);
			searchResults.add(source);
		}

		return searchResults;
	}

	public ExportValueSet getValueSetMembers(String iri, boolean expandMembers, boolean expandSets, Integer limit, boolean withHyperlinks) {
		return getValueSetMembers(iri, expandMembers, expandSets,  limit, withHyperlinks, null, iri);
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
        Set<ValueSetMember> definedMemberInclusions = getMember(iri, IM.DEFINITION, withHyperlinks);
        for (ValueSetMember included : definedMemberInclusions) {
            if (originalParentIri.equals(iri)) {
                included.setLabel("a_MemberIncluded");
                included.setType(MemberType.INCLUDED);
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



    private Set<ValueSetMember> getMember(String iri, TTIriRef predicate, boolean withHyperlinks) {
		Set<ValueSetMember> members = new HashSet<>();
		Set<String> predicates = new HashSet<>();
		predicates.add(predicate.getIri());
		TTArray result = getBundle(iri, predicates, UNLIMITED)
            .getEntity()
            .get(predicate.asIriRef());

		if (result != null) {
            if (result.isIriRef())
                members.add(getValueSetMemberFromIri(result.asIriRef(), withHyperlinks));
            else if (result.isNode())
                members.add(getValueSetMemberFromNode(result.asNode(), withHyperlinks));
            else {
                for (TTValue element : result.iterator()) {
                    if (element.isNode()) {
                        members.add(getValueSetMemberFromNode(element, withHyperlinks));
                    } else if (element.isIriRef()) {
                        members.add(getValueSetMemberFromIri(element.asIriRef(), withHyperlinks));
                    }
                }
            }
        }
		return members;
	}

	private List<String> getBlockedIris() {
		List<String> blockedIris = new ArrayList<>();
		try {
			blockedIris = configService.getConfig("xmlSchemaDataTypes", new TypeReference<>(){});
		} catch (Exception e) {
			LOG.warn("Error getting xmlSchemaDataTypes config, reverting to default", e);
		}
		return blockedIris;
	}

	private Map<String, String> getDefaultPredicateNames() {
		Map<String, String> defaultPredicates = new HashMap<>();
		try {
			defaultPredicates = configService.getConfig("defaultPredicateNames", new TypeReference<>() {
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
		String iriAsString = TTToString.ttIriToString(iri,"object", 0, withHyperlinks, false, blockedIris);
		member.setEntity(iri(iri.getIri(), iriAsString));
		member.setCode(summary.getCode());
		member.setScheme(summary.getScheme());
		return member;
	}

	private Map<String, ValueSetMember> processMembers(Set<ValueSetMember> valueSetMembers, boolean expand, Integer memberCount, Integer limit)
			{
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

        if (params.includeHasSubtypes()) { downloadDto.setHasSubTypes(getImmediateChildren(iri,null, null, null, params.includeInactive()));}
        if (params.includeInferred()) { downloadDto.setInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri())), UNLIMITED).getEntity());}
        if (params.includeProperties()) { downloadDto.setDataModelProperties(getDataModelProperties(iri));}
        if (params.includeMembers()) { downloadDto.setMembers(getValueSetMembers(iri, params.expandMembers(), params.expandSubsets(), null, false));}
        if (params.includeTerms()) { downloadDto.setTerms(getEntityTermCodes(iri));}
        if (params.includeIsChildOf()) { downloadDto.setIsChildOf(getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF.getIri())), UNLIMITED).getEntity().get(IM.IS_CHILD_OF));}
		if (params.includeHasChildren()) { downloadDto.setHasChildren(getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN.getIri())), UNLIMITED).getEntity().get(IM.HAS_CHILDREN));}

        return downloadDto;
    }

    public XlsHelper getExcelDownload(String iri, List<ComponentLayoutItem> configs, DownloadParams params) {
        if (iri == null || iri.isEmpty())
            return null;

        XlsHelper xls = new XlsHelper();

        xls.addSummary(getSummaryFromConfig(iri, configs));

        if (params.includeHasSubtypes()) { xls.addHasSubTypes(getImmediateChildren(iri,null, null, null, params.includeInactive()));}
        if (params.includeInferred()) { xls.addInferred(getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri())), UNLIMITED));}
        if (params.includeProperties()) { xls.addDataModelProperties(getDataModelProperties(iri));}
        if (params.includeMembers()) { xls.addMembersSheet(getValueSetMembers(iri, params.expandMembers(), params.expandSubsets(), null, false));}
		if (params.includeTerms()) { xls.addTerms(getEntityTermCodes(iri));}
		TTEntity isChildOfEntity = getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF.getIri())), UNLIMITED).getEntity();
        TTArray isChildOfData = isChildOfEntity.get(TTIriRef.iri(IM.IS_CHILD_OF.getIri(), IM.IS_CHILD_OF.getName()));
		if (params.includeIsChildOf() && isChildOfData != null) { xls.addIsChildOf(isChildOfData.getElements());}
		TTEntity hasChildrenEntity = getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN.getIri())), UNLIMITED).getEntity();
        TTArray hasChildrenData = hasChildrenEntity.get(TTIriRef.iri(IM.HAS_CHILDREN.getIri(), IM.HAS_CHILDREN.getName()));
		if (params.includeHasChildren() && hasChildrenData != null) { xls.addHasChildren(hasChildrenData.getElements());}

        return xls;
    }

    public List<DataModelProperty> getDataModelProperties(String iri) {
		TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY.getIri(), RDFS.LABEL.getIri()),UNLIMITED).getEntity();
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
		if (property.asNode().has(SHACL.DATATYPE))
			pv.setType(property.asNode().get(SHACL.DATATYPE).asIriRef());
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
						prop.getType().getIri(), prop.getType().getName(), prop.getInheritedFrom().getIri(), prop.getInheritedFrom().getName()))
				.collect(Collectors.toList());

		List<GraphDto> isas = getEntityDefinedParents(entity);

		List<GraphDto> subtypes = getDefinitionSubTypes(iri).stream()
				.map(subtype -> new GraphDto().setName(subtype.getName()).setIri(subtype.getIri()))
				.collect(Collectors.toList());

		GraphDto axiom = new GraphDto().setKey("0_2").setName("Axioms");
		GraphDto axiomWrapper = getWrapper(axiomGraph,"0_2_0");
		addWrapper(axiom, axiomWrapper,"0_2_0");

		GraphDto dataModel = new GraphDto().setKey("0_3").setName("Data model properties");

		GraphDto dataModelDirect = new GraphDto().setKey("0_3_0").setName("Direct");
		GraphDto dataModelDirectWrapper = getWrapper(dataModelProps,"0_3_0_0");
		addWrapper(dataModelDirect,dataModelDirectWrapper,"0_3_0_0");

		GraphDto dataModelInherited = new GraphDto().setKey("0_3_1").setName("Inherited");
		GraphDto dataModelInheritedWrapper = getDataModelInheritedWrapper(dataModelProps);
		addWrapper(dataModelInherited,dataModelInheritedWrapper,"0_3_1_0");

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

    private GraphDto getWrapper(List<GraphDto> props,String key) {
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

		return entityTripleRepository.findImmediateChildrenByIri(iri,null, null, null, false).stream()
				.map(t -> new TTIriRef(t.getIri(), t.getName())).collect(Collectors.toList());
	}

	public EntityDefinitionDto getEntityDefinitionDto(String iri) {
		TTEntity entity = getBundle(iri,Set.of(RDFS.SUBCLASSOF.getIri(), RDF.TYPE.getIri(),RDFS.LABEL.getIri(),RDFS.COMMENT.getIri(),IM.HAS_STATUS.getIri()), UNLIMITED).getEntity();
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
		if(iri==null || iri.isEmpty())
			return null;
		TTEntity entity = getBundle(iri, Set.of(SHACL.PROPERTY.getIri(), SHACL.OR.getIri(), RDF.TYPE.getIri()), UNLIMITED).getEntity();
		TTArray value = entity.get(RDF.TYPE);
		if(!value.getElements().contains(SHACL.NODESHAPE)){
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
            predicates = configService.getConfig("inferredExcludePredicates", new TypeReference<>() {
            });
        } catch (Exception e) {
            LOG.warn("Error getting inferredPredicates config, reverting to default", e);
        }

        if (predicates == null) {
            LOG.warn("Config for inferredPredicates not set, reverting to default");
            predicates = new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), IM.ROLE_GROUP.getIri()));
        }

        return getEntityByPredicateExclusions(iri, predicates, UNLIMITED);
    }

    public TTDocument getConcept(String iri) {
		TTBundle bundle = getBundle(iri, null, 0);
		TTDocument document = new TTDocument();
		List<Namespace> namespaces = entityTripleRepository.findNamespaces();
		TTContext context = new TTContext();
		for(Namespace namespace : namespaces){
			context.add(namespace.getIri(), namespace.getPrefix(), namespace.getName());
		}
		document.setContext(context);
		document.addEntity(bundle.getEntity());
		return document;
	}

	public TTDocument getConceptList(List<String> iris){
		if(iris == null || iris.isEmpty()){
			return null;
		}
		TTDocument document = new TTDocument();
		List<Namespace> namespaces = entityTripleRepository.findNamespaces();
		TTContext context = new TTContext();
		for(Namespace namespace : namespaces){
			context.add(namespace.getIri(), namespace.getPrefix(), namespace.getName());
		}
		document.setContext(context);
		for(String iri: iris){
			TTBundle bundle = getBundle(iri, null, 0);
			document.addEntity(bundle.getEntity());
		}
		return document;
	}

	public TTDocument getConceptListByGraph(String iri){
		List<String> conceptIris = entityTripleRepository.getConceptIrisByGraph(iri);
		return getConceptList(conceptIris);
	}

	public List<SimpleMap> getSimpleMaps(String iri) {
		if (iri == null || iri.equals("")) return new ArrayList<>();
		String scheme = iri.substring(0,iri.indexOf("#") + 1);
		List<Namespace> namespaces = getNamespaces();
		List<String> schemes = namespaces.stream().map(Namespace::getIri).collect(Collectors.toList());
		schemes.remove(scheme);
		return entityTripleRepository.findSimpleMapsByIri(iri, schemes);
	}

	public String getEcl(TTBundle inferred) throws DataFormatException {
		if (inferred == null) throw new DataFormatException("Missing data for ECL conversion");
		if (inferred.getEntity() == null || inferred.getEntity().asNode().getPredicateMap().isEmpty()) throw new DataFormatException("Missing entity bundle definition for ECL conversion");
		return TTToECL.getExpressionConstraint(inferred.getEntity(), true);
	}

    public XSSFWorkbook getSetExport(String iri) throws DataFormatException {
		if(iri == null || "".equals(iri)){
			return null;
		}
        return new ExcelSetExporter().getSetAsExcel(iri);
    }

	/**
	 * Returns an entity and predicate names from an iri including all predicates and blank nodes recursively to a depth of 5
	 * @param iri the string representation of the absolute iri
	 * @return a bundle containing an entity and predicate iri to name map.
	 *
	 */
	public TTBundle getFullEntity(String iri){
		return entityRepository2.getBundle(iri);
	}





}