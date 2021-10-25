package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.GraphDto.GraphType;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.MemberType;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.endeavourhealth.imapi.transforms.TTToHTML;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

@Component
public class EntityService {
    private static final Logger LOG = LoggerFactory.getLogger(EntityService.class);

    public static final int UNLIMITED = 0;
    public static final int MAX_CHILDREN = 100;

	EntityRepository entityRepository = new EntityRepository();

	EntityTctRepository entityTctRepository = new EntityTctRepository();

	EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

	ValueSetRepository valueSetRepository = new ValueSetRepository();

	TermCodeRepository termCodeRepository = new TermCodeRepository();

	EntitySearchRepository entitySearchRepository = new EntitySearchRepository();

	EntityTypeRepository entityTypeRepository = new EntityTypeRepository();

	@Autowired
	ConfigService configService;

	public TTBundle getEntityPredicates(String iri, Set<String> predicates, int limit) throws SQLException {
        List<Tpl> triples = entityTripleRepository.getTriplesRecursive(iri, predicates, limit);
        return buildEntityFromTriples(iri, triples);
    }

    private TTBundle buildEntityFromTriples(String iri, List<Tpl> triples) {
        TTEntity entity = new TTEntity(iri);
        TTBundle result = new TTBundle().setEntity(entity);

        // Reconstruct
        HashMap<Integer, TTNode> nodeMap = new HashMap<>();

        for (Tpl triple : triples) {
            result.addPredicate(triple.getPredicate());

            TTValue v = getValue(nodeMap, triple);

            if (triple.getParent() == null) {
                if (triple.isFunctional()) {
                    entity.set(triple.getPredicate(), v);
                } else {
                    entity.addObject(triple.getPredicate(), v);
                }
            } else {
                TTNode n = nodeMap.get(triple.getParent());
                if (n == null)
                    throw new IllegalStateException("Unknown parent node!");
                if (triple.isFunctional()) {
                    n.set(triple.getPredicate(), v);
                } else {
                    n.addObject(triple.getPredicate(), v);
                }
            }
        }

        return result;
    }

    private TTValue getValue(HashMap<Integer, TTNode> nodeMap, Tpl triple) {
        TTValue v;

        if (triple.getLiteral() != null)
            v = literal(triple.getLiteral(), triple.getObject());
        else if (triple.getObject() != null)
            v = triple.getObject();
        else {
            v = new TTNode();
            nodeMap.put(triple.getDbid(), (TTNode) v);
        }
        return v;
    }

    public TTIriRef getEntityReference(String iri) throws SQLException {
		if (iri == null || iri.isEmpty())
			return null;
		return entityRepository.getEntityReferenceByIri(iri);
	}

	public List<EntityReferenceNode> getImmediateChildren(String iri, Integer pageIndex, Integer pageSize,
			boolean inactive) throws SQLException {

		if (iri == null || iri.isEmpty())
			return Collections.emptyList();

		List<EntityReferenceNode> result = new ArrayList<>();
		int rowNumber = 0;
		if (pageIndex != null && pageSize != null)
			rowNumber = (pageIndex - 1) * pageSize;

		for (TTIriRef c: getChildren(iri, rowNumber, pageSize, inactive)) {
		    EntityReferenceNode node = new EntityReferenceNode();
		    node.setIri(c.getIri()).setName(c.getName());
		    node.setType(entityTypeRepository.getEntityTypes(c.getIri()));
		    node.setHasChildren(entityTripleRepository.hasChildren(c.getIri(), inactive));
		    result.add(node);
        }

		return result;
	}

	private List<TTIriRef> getChildren(String iri, int rowNumber, Integer pageSize, boolean inactive)
			throws SQLException {

		return entityTripleRepository.findImmediateChildrenByIri(iri, rowNumber, pageSize, inactive);
	}

	public List<EntityReferenceNode> getImmediateParents(String iri, Integer pageIndex, Integer pageSize,
			boolean inactive) throws SQLException {

		if (iri == null || iri.isEmpty())
			return Collections.emptyList();

		int rowNumber = 0;
		if (pageIndex != null && pageSize != null)
			rowNumber = (pageIndex - 1) * pageSize;

		List<EntityReferenceNode> parents = getParents(iri, rowNumber, pageSize, inactive).stream()
				.map(p -> new EntityReferenceNode(p.getIri(), p.getName())).collect(Collectors.toList());

		for (EntityReferenceNode parent : parents)
			parent.setType(entityTypeRepository.getEntityTypes(parent.getIri()));

		return parents;
	}

	private List<TTIriRef> getParents(String iri, int rowNumber, Integer pageSize, boolean inactive)
			throws SQLException {

		return entityTripleRepository.findImmediateParentsByIri(iri, rowNumber, pageSize, inactive);
	}

	public List<TTIriRef> isWhichType(String iri, List<String> candidates) throws SQLException {
		if (iri == null || iri.isEmpty() || candidates == null || candidates.isEmpty())
			return Collections.emptyList();
		return entityTctRepository
				.findAncestorsByType(iri, IM.IS_A.getIri(), candidates).stream()
				.sorted(Comparator.comparing(TTIriRef::getName)).collect(Collectors.toList());
	}

	public List<TTIriRef> usages(String iri, Integer pageIndex, Integer pageSize) throws SQLException {

		if (iri == null || iri.isEmpty())
			return Collections.emptyList();

		int rowNumber = 0;
		if (pageIndex != null && pageSize != null)
			rowNumber = pageIndex * pageSize;

		return entityTripleRepository.getActiveSubjectByObjectExcludeByPredicate(iri, rowNumber, pageSize, IM.IS_A.getIri()).stream()
				.sorted(Comparator.comparing(TTIriRef::getName, Comparator.nullsLast(Comparator.naturalOrder())))
				.distinct().collect(Collectors.toList());
	}

	public Integer totalRecords(String iri) throws SQLException {
		if (iri == null || iri.isEmpty())
			return 0;
		return entityTripleRepository.getActiveSubjectByObjectExcludeByPredicate(iri,null,null,IM.IS_A.getIri()).size();
	}

	public List<EntitySummary> advancedSearch(SearchRequest request) throws SQLException {
		if (request == null || request.getTermFilter() == null || request.getTermFilter().isEmpty())
			return Collections.emptyList();

		List<EntitySummary> matchingEntity = entitySearchRepository.advancedSearch(request);

		return matchingEntity.stream()
            .map(e -> e.setWeighting(Levenshtein.calculate(request.getTermFilter(), e.getMatch())))
            .sorted(Comparator.comparingInt(EntitySummary::getWeighting))
			.collect(Collectors.toList());
	}

	public ExportValueSet getValueSetMembers(String iri, boolean expandMembers, boolean expandSets, Integer limit) throws SQLException {
        return getValueSetMembers(iri, expandMembers, expandSets,  limit, null, iri);
    }

	public ExportValueSet getValueSetMembers(String iri, boolean expandMembers, boolean expandSets, Integer limit, String parentSetName, String originalParentIri) throws SQLException {
		if (iri == null || iri.isEmpty()) {
			return null;
		}
		ExportValueSet result = new ExportValueSet().setValueSet(getEntityReference(iri));
		int memberCount = 0;

		Set<ValueSetMember> definedMemberInclusions = getMember(iri, IM.DEFINITION);
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

        Set<ValueSetMember> definedMemberExclusions = getMember(iri, IM.NOT_MEMBER);
		for (ValueSetMember excluded : definedMemberExclusions) {
			if (originalParentIri.equals(iri)) {
				excluded.setLabel("b_MemberExcluded");
				excluded.setType(MemberType.EXCLUDED);
			} else {
				if (expandSets) {
					excluded.setLabel("Subset - expanded");
					excluded.setType(MemberType.SUBSET);
				} else if (excluded.getType() != MemberType.COMPLEX) {
					excluded.setLabel("Subset - " + parentSetName);
					excluded.setType(MemberType.SUBSET);
				}
			}
			excluded.setDirectParent(new TTIriRef().setIri(iri).setName(getEntityReference(iri).getName()));
		}

        Set<ValueSetMember> definedSetInclusions = getMember(iri, IM.HAS_SUBSET);
		if (expandSets || expandMembers) {
			for (ValueSetMember set : definedSetInclusions) {
				ExportValueSet individualResults = getValueSetMembers(set.getEntity().getIri(), expandMembers, expandSets, limit, null, originalParentIri);
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
				ExportValueSet setMembers = getValueSetMembers(set.getEntity().getIri(), expandMembers, expandSets, limit, set.getEntity().getName(), originalParentIri);
				memberCount += setMembers.getMembers().size();
				result.addAllMembers(setMembers.getMembers());
			}
		}

		Map<String, ValueSetMember> evaluatedMemberInclusions = processMembers(definedMemberInclusions, expandMembers, memberCount, limit);
		memberCount += evaluatedMemberInclusions.size();
		Map<String, ValueSetMember> evaluatedMemberExclusions = processMembers(definedMemberExclusions, expandMembers, memberCount, limit);
        memberCount += evaluatedMemberExclusions.size();

		if (expandMembers) {
			// Remove exclusions by key
			evaluatedMemberExclusions.forEach((k, v) -> evaluatedMemberInclusions.remove(k));
		}

		if (limit != null && memberCount > limit)
			return result.setLimited(true);

		result.addAllMembers(evaluatedMemberInclusions.values());
		if (!expandMembers)
			result.addAllMembers(evaluatedMemberExclusions.values());

		return result;
	}

	private Set<ValueSetMember> getMember(String iri, TTIriRef predicate) throws SQLException {
		Set<ValueSetMember> members = new HashSet<>();
		Set<String> predicates = new HashSet<>();
		predicates.add(predicate.getIri());
		List<TTValue> results = getEntityPredicates(iri, predicates, UNLIMITED)
            .getEntity()
            .getAsArray(predicate.asIriRef())
            .getElements();
		for (TTValue element : results) {
			if (element.isNode()) {
				members.add(getValueSetMemberFromNode(element));
            }
			if (element.isIriRef()) {
				ValueSetMember member = null;
				member = getValueSetMemberFromIri(element.asIriRef().getIri());
				members.add(member);
			}
		}
		return members;
	}

	private ValueSetMember getValueSetMemberFromNode(TTValue node) throws SQLException {
		ValueSetMember member = new ValueSetMember();
		String nodeAsString = TTToHTML.getExpressionText(node.asNode());
		member.setEntity(iri("", nodeAsString));
		return member;
	}

	private ValueSetMember getValueSetMemberFromIri(String iri) throws SQLException {
		ValueSetMember member = new ValueSetMember();
		EntitySummary summary = entityRepository.getEntitySummaryByIri(iri);
		member.setEntity(iri(summary.getIri(), summary.getName()));
		member.setCode(summary.getCode());
		member.setScheme(summary.getScheme());
		return member;
	}

	private Map<String, ValueSetMember> processMembers(Set<ValueSetMember> valueSetMembers, boolean expand, Integer memberCount, Integer limit)
			throws SQLException {
		Map<String, ValueSetMember> memberHashMap = new HashMap<>();
		for (ValueSetMember member : valueSetMembers) {

            if (limit != null && (memberCount + memberHashMap.size()) > limit)
                return memberHashMap;

			memberHashMap.put(member.getEntity().getIri() + "/" + member.getCode(), member);

			if (expand) {
                valueSetRepository
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

	public ValueSetMembership isValuesetMember(String valueSetIri, String memberIri) throws SQLException {
		if (valueSetIri == null || valueSetIri.isEmpty() || memberIri == null || memberIri.isEmpty())
			return null;
		ValueSetMembership result = new ValueSetMembership();
		Set<TTIriRef> included = getMemberIriRefs(valueSetIri, IM.DEFINITION);
		Set<TTIriRef> excluded = getMemberIriRefs(valueSetIri, IM.NOT_MEMBER);
		for (TTIriRef m : included) {
			Optional<ValueSetMember> match = valueSetRepository.expandMember(m.getIri()).stream()
					.filter(em -> em.getEntity().getIri().equals(memberIri)).findFirst();
			if (match.isPresent()) {
				result.setIncludedBy(m);
				break;
			}
		}
		for (TTIriRef m : excluded) {
			Optional<ValueSetMember> match = valueSetRepository.expandMember(m.getIri()).stream()
					.filter(em -> em.getEntity().getIri().equals(memberIri)).findFirst();
			if (match.isPresent()) {
				result.setExcludedBy(m);
				break;
			}
		}
		return result;
	}

	private Set<TTIriRef> getMemberIriRefs(String valueSetIri, TTIriRef predicate) throws SQLException {
		return entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(valueSetIri, predicate.getIri());

	}

	public List<TermCode> getEntityTermCodes(String iri) throws SQLException {
		if (iri == null || iri.isEmpty())
			return Collections.emptyList();
		return termCodeRepository.findAllByIri(iri);
	}

	public TTEntity getSummaryFromConfig(String iri, List<ComponentLayoutItem> configs) throws SQLException {
		if (iri == null || iri.isEmpty() || configs == null || configs.isEmpty()) {
			return new TTEntity();
		}
		List<String> excludedForSummary = Arrays.asList("None", IM.IS_A.getIri(), "subtypes", IM.IS_CHILD_OF.getIri(), IM.HAS_CHILDREN.getIri(), "termCodes", "semanticProperties", "dataModelProperties");
		List<ComponentLayoutItem> filteredConfigs = configs.stream().filter(config -> !excludedForSummary.contains(config.getPredicate())).collect(Collectors.toList());
		List<String> predicates = filteredConfigs.stream().map(config -> config.getPredicate()).collect(Collectors.toList());
		TTEntity entity = getEntityPredicates(iri, new HashSet<>(predicates), UNLIMITED).getEntity();
		return entity;
	}

    public DownloadDto getJsonDownload(String iri, List<ComponentLayoutItem> configs, boolean children, boolean inferred, boolean dataModelProperties,
									   boolean members, boolean expandMembers,boolean expandSubsets, boolean axioms, boolean terms, boolean isChildOf, boolean hasChildren, boolean inactive) throws SQLException {
        if (iri == null || iri.isEmpty())
            return null;

        DownloadDto downloadDto = new DownloadDto();

        downloadDto.setSummary(getSummaryFromConfig(iri, configs));

        if (children) downloadDto.setHasSubTypes(getImmediateChildren(iri, null, null, inactive));
        if (inferred) downloadDto.setInferred(getEntityPredicates(iri, new HashSet<>(Arrays.asList(IM.IS_A.getIri(), IM.ROLE_GROUP.getIri())), UNLIMITED).getEntity());
        if (axioms) downloadDto.setAxioms(getEntityPredicates(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), RDFS.SUBPROPERTYOF.getIri(), OWL.EQUIVALENTCLASS.getIri())), UNLIMITED).getEntity());
        if (dataModelProperties) downloadDto.setDataModelProperties(getDataModelProperties(iri));
        if (members) downloadDto.setMembers(getValueSetMembers(iri, expandMembers, expandSubsets, null));
        if (terms) downloadDto.setTerms(getEntityTermCodes(iri));
        if (isChildOf) downloadDto.setIsChildOf(getEntityPredicates(iri, new HashSet<String>(Arrays.asList(IM.IS_CHILD_OF.getIri())), UNLIMITED).getEntity().get(IM.IS_CHILD_OF));
		if (hasChildren) downloadDto.setHasChildren(getEntityPredicates(iri, new HashSet<String>(Arrays.asList(IM.HAS_CHILDREN.getIri())), UNLIMITED).getEntity().get(IM.HAS_CHILDREN));

        return downloadDto;
    }

    public XlsHelper getExcelDownload(String iri, List<ComponentLayoutItem> configs, boolean children, boolean inferred, boolean dataModelProperties,
									  boolean members, boolean expandMembers, boolean expandSubsets, boolean axioms, boolean terms, boolean isChildOf, boolean hasChildren, boolean inactive) throws SQLException {
        if (iri == null || iri.isEmpty())
            return null;

        XlsHelper xls = new XlsHelper();

        xls.addSummary(getSummaryFromConfig(iri, configs));

        if (children) xls.addHasSubTypes(getImmediateChildren(iri, null, null, inactive));
        if (inferred) xls.addInferred(getEntityPredicates(iri, new HashSet<>(Arrays.asList(IM.IS_A.getIri(), IM.ROLE_GROUP.getIri())), UNLIMITED));
        if (axioms) xls.addAxioms(getEntityPredicates(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), RDFS.SUBPROPERTYOF.getIri(), OWL.EQUIVALENTCLASS.getIri())), UNLIMITED));
        if (dataModelProperties) xls.addDataModelProperties(getDataModelProperties(iri));
        if (members) xls.addMembersSheet(getValueSetMembers(iri, expandMembers, expandSubsets, null));
		if (terms) xls.addTerms(getEntityTermCodes(iri));
		TTEntity isChildOfEntity = getEntityPredicates(iri, new HashSet<>(Arrays.asList(IM.IS_CHILD_OF.getIri())), UNLIMITED).getEntity();
		TTValue isChildOfData = isChildOfEntity.get(TTIriRef.iri(IM.IS_CHILD_OF.getIri(), IM.IS_CHILD_OF.getName()));
		if (isChildOf && isChildOfData != null) xls.addIsChildOf(isChildOfData.getElements());
		TTEntity hasChildrenEntity = getEntityPredicates(iri, new HashSet<>(Arrays.asList(IM.HAS_CHILDREN.getIri())), UNLIMITED).getEntity();
		TTValue hasChildrenData = hasChildrenEntity.get(TTIriRef.iri(IM.HAS_CHILDREN.getIri(), IM.HAS_CHILDREN.getName()));
		if (hasChildren && hasChildrenData != null) xls.addHasChildren(hasChildrenData.getElements());

        return xls;
    }

    public List<DataModelProperty> getDataModelProperties(String iri) throws SQLException {
		TTEntity entity = getEntityPredicates(iri, Set.of(IM.PROPERTY_GROUP.getIri()),UNLIMITED).getEntity();
		return getDataModelProperties(entity);
	}

	public List<DataModelProperty> getDataModelProperties(TTEntity entity) {
		List<DataModelProperty> properties = new ArrayList<>();
		if (entity == null)
			return Collections.emptyList();
		if (entity.has(IM.PROPERTY_GROUP)) {
            getDataModelPropertyGroups(entity, properties);
        }
		return properties;
	}

    private void getDataModelPropertyGroups(TTEntity entity, List<DataModelProperty> properties) {
        for (TTValue propertyGroup : entity.getAsArray(IM.PROPERTY_GROUP).getElements()) {
            if (propertyGroup.isNode()) {
                TTIriRef inheritedFrom = propertyGroup.asNode().has(IM.INHERITED_FROM)
                    ? propertyGroup.asNode().get(IM.INHERITED_FROM).asIriRef()
                    : null;
                if (propertyGroup.asNode().has(SHACL.PROPERTY)) {
                    getDataModelShaclProperties(properties, propertyGroup, inheritedFrom);
                }
            }
        }
    }

    private void getDataModelShaclProperties(List<DataModelProperty> properties, TTValue propertyGroup, TTIriRef inheritedFrom) {
        for (TTValue property : propertyGroup.asNode().get(SHACL.PROPERTY).asArray().getElements()) {
            TTIriRef propertyPath = property.asNode().get(SHACL.PATH).asIriRef();
            if (properties.stream()
                    .noneMatch(o -> o.getProperty().getIri().equals(propertyPath.getIri()))) {
                properties.add(getPropertyValue(inheritedFrom, property, propertyPath));
            }
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

	public String valueSetMembersCSV(String iri, boolean expandMember, boolean expandSubset) throws SQLException {
		ExportValueSet exportValueSet = getValueSetMembers(iri, expandMember, expandSubset, null);
		StringBuilder valueSetMembers = new StringBuilder();
		valueSetMembers.append(
				"Inc\\Exc\\IncSubset\tValueSetIri\tValueSetName\tMemberIri\tMemberTerm\tMemberCode\tMemberSchemeIri\tMemberSchemeName\n");
		if (exportValueSet == null)
			return valueSetMembers.toString();
		for (ValueSetMember inc : exportValueSet.getMembers()) {
			appendValueSet(exportValueSet, valueSetMembers, inc, "Inc");
		}
//		if(exportValueSet.getIncludedSubsets() != null){
//			for(ValueSetMember incSubset : exportValueSet.getIncludedSubsets()){
//				appendValueSet(exportValueSet, valueSetMembers, incSubset, "IncSubset");
//			}
//		}
//		if (exportValueSet.getExcludedMembers() != null) {
//			for (ValueSetMember exc : exportValueSet.getExcludedMembers()) {
//				appendValueSet(exportValueSet, valueSetMembers, exc, "Exc");
//			}
//		}
		return valueSetMembers.toString();
	}

	private void appendValueSet(ExportValueSet exportValueSet, StringBuilder valueSetMembers, ValueSetMember setMember,
			String text) {
		valueSetMembers.append(text).append("\t").append(exportValueSet.getValueSet().getIri()).append("\t")
				.append(exportValueSet.getValueSet().getName()).append("\t").append(setMember.getEntity().getIri())
				.append("\t").append(setMember.getEntity().getName()).append("\t").append(setMember.getCode())
				.append("\t");
		if (setMember.getScheme() != null)
			valueSetMembers.append(setMember.getScheme().getIri()).append("\t").append(setMember.getScheme().getName());

		valueSetMembers.append("\n");
	}

	public GraphDto getGraphData(String iri) throws SQLException {
		TTEntity entity = getEntityPredicates(iri, Set.of(IM.IS_A.getIri(), RDFS.LABEL.getIri()), UNLIMITED).getEntity();

		GraphDto graphData = new GraphDto().setKey("0").setIri(entity.getIri()).setName(entity.getName());
		GraphDto graphParents = new GraphDto().setKey("0_0").setName("Is a");
		GraphDto graphChildren = new GraphDto().setKey("0_1").setName("Subtypes");

		TTBundle axioms = getEntityPredicates(iri, new HashSet<>(Arrays.asList(IM.IS_A.getIri(), IM.ROLE_GROUP.getIri())), UNLIMITED);
		List<GraphDto> axiomGraph = bundleToGraphDtos(axioms);

		List<GraphDto> dataModelProps = getDataModelProperties(iri).stream()
				.map(prop -> new GraphDto(prop.getProperty().getIri(), prop.getProperty().getName(),
						prop.getType().getIri(), prop.getType().getName(), prop.getInheritedFrom().getIri(), prop.getInheritedFrom().getName()))
				.collect(Collectors.toList());

		List<GraphDto> isas = getEntityDefinedParents(entity, IM.IS_A);

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
	    List<GraphDto> result = new ArrayList<>();



	    return result;
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

	private List<GraphDto> getEntityDefinedParents(TTEntity entity, TTIriRef predicate) {
		TTValue parent = entity.get(predicate);
		if (parent == null)
			return Collections.emptyList();
		List<GraphDto> result = new ArrayList<>();
		if (parent.isList()) {
			parent.getElements().forEach(item -> {
				if (!OWL.THING.equals(item.asIriRef()))
					result.add(new GraphDto().setIri(item.asIriRef().getIri()).setName(item.asIriRef().getName())
							.setPropertyType(predicate.getName()));
			});
		} else {
			if (!OWL.THING.equals(parent.asIriRef()))
				result.add(new GraphDto().setIri(parent.asIriRef().getIri()).setName(parent.asIriRef().getName())
						.setPropertyType(predicate.getName()));
		}
		return result;
	}

	public List<TTIriRef> getDefinitionSubTypes(String iri) throws SQLException {

		return entityTripleRepository.findImmediateChildrenByIri(iri, null, null, false).stream()
				.map(t -> new TTIriRef(t.getIri(), t.getName())).collect(Collectors.toList());
	}

	public EntityDefinitionDto getEntityDefinitionDto(String iri) throws SQLException {
		TTEntity entity = getEntityPredicates(iri,Set.of(IM.IS_A.getIri(), RDF.TYPE.getIri(),RDFS.LABEL.getIri(),RDFS.COMMENT.getIri(),IM.HAS_STATUS.getIri()), UNLIMITED).getEntity();
		List<TTIriRef> types = entity.getType() == null ? new ArrayList<>()
				: entity.getType().getElements().stream()
						.map(t -> new TTIriRef(t.asIriRef().getIri(), t.asIriRef().getName()))
						.collect(Collectors.toList());

		List<TTIriRef> isa = !entity.has(IM.IS_A) ? new ArrayList<>()
				: entity.get(IM.IS_A).getElements().stream()
						.map(t -> new TTIriRef(t.asIriRef().getIri(), t.asIriRef().getName()))
						.collect(Collectors.toList());

		return new EntityDefinitionDto().setIri(entity.getIri()).setName(entity.getName())
				.setDescription(entity.getDescription())
				.setStatus(entity.getStatus() == null ? null : entity.getStatus().getName()).setTypes(types)
				.setSubtypes(getDefinitionSubTypes(iri)).setIsa(isa);
	}

	public EntitySummary getSummary(String iri) throws SQLException {
		return entitySearchRepository.getSummary(iri);
	}

	public TTEntity getConceptShape(String iri) throws SQLException {
		if(iri==null || iri.isEmpty())
			return null;
		TTEntity entity = getEntityPredicates(iri, Set.of(SHACL.PROPERTY.getIri(), SHACL.OR.getIri(), RDF.TYPE.getIri()), UNLIMITED).getEntity();
		TTValue value = entity.get(RDF.TYPE);
		if(!value.getElements().contains(SHACL.NODESHAPE)){
			return null;
		}
		return entity;
	}

	public List<Namespace> getNamespaces() throws SQLException {
//		List<Namespace> namespaces = entityTripleRepository.findNamespaces();
		return entityTripleRepository.findNamespaces();
	}

    public TTBundle getInferredBundle(String iri) throws SQLException {
        Set<String> predicates = null;
        try {
            predicates = configService.getConfig("inferredPredicates", new TypeReference<>() {
            });
        } catch (Exception e) {
            LOG.warn("Error getting inferredPredicates config, reverting to default", e);
        }

        if (predicates == null) {
            LOG.warn("Config for inferredPredicates not set, reverting to default");
            predicates = new HashSet<>(Arrays.asList(IM.IS_A.getIri(), IM.ROLE_GROUP.getIri()));
        }

        return getEntityPredicates(iri, predicates, UNLIMITED);
    }

    public TTBundle getAxiomBundle(String iri) throws SQLException {
        Set<String> predicates = null;
        try {
            predicates = configService.getConfig("axiomPredicates", new TypeReference<>() {
            });
        } catch (Exception e) {
            LOG.warn("Error getting axiomPredicates config, reverting to default", e);
        }

        if (predicates == null) {
            LOG.warn("Config for axiomPredicates not set, reverting to default");
            predicates = new HashSet<>(Arrays.asList(RDFS.SUBCLASSOF.getIri(), RDFS.SUBPROPERTYOF.getIri(), OWL.EQUIVALENTCLASS.getIri()));
        }

        return getEntityPredicates(iri, predicates, UNLIMITED);
    }

    public TTDocument getConcept(String iri) throws SQLException {
		TTBundle bundle = getEntityPredicates(iri, null, 0);
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

	public Collection<SimpleMap> getMatchedFrom(String iri) throws SQLException {
		return entityTripleRepository.getSubjectFromObject(iri, IM.MATCHED_TO);
	}

    public Set<String> getPredicateIris(Set<String> predicateIris) throws SQLException {
        return entityRepository.findAllByIriIn(predicateIris).stream().map(iriRef -> iriRef.getIri()).collect(Collectors.toSet());
    }
}
