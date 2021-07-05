package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.SemanticProperty;
import org.endeavourhealth.imapi.model.dto.GraphDto.GraphType;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

@Component
public class EntityService {
	private static final Logger LOG = LoggerFactory.getLogger(EntityService.class);

	EntityRepository entityRepository = new EntityRepository();

	EntityTctRepository entityTctRepository = new EntityTctRepository();

	EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

	ValueSetRepository valueSetRepository = new ValueSetRepository();

	TermCodeRepository termCodeRepository = new TermCodeRepository();

	EntitySearchRepository entitySearchRepository = new EntitySearchRepository();

	EntityTypeRepository entityTypeRepository = new EntityTypeRepository();

	public TTEntity getEntityPredicates(String iri, Set<String> predicates) throws SQLException {
        TTEntity result = new TTEntity(iri);

        List<Tpl> triples = entityTripleRepository.getTriplesRecursive(iri, predicates);

        // Reconstruct
        HashMap<Integer, TTNode> nodeMap = new HashMap<>();

        for (Tpl triple : triples) {
            TTValue v = getValue(nodeMap, triple);

            if (triple.getParent() == null)
                if (triple.isFunctional())
                    result.set(triple.getPredicate(), v);
                else
                    result.addObject(triple.getPredicate(), v);
            else {
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
		List<EntityReferenceNode> immediateChildren = getChildren(iri, rowNumber, pageSize, inactive).stream()
				.map(c -> new EntityReferenceNode(c.getIri(), c.getName())).collect(Collectors.toList());
		for (EntityReferenceNode child : immediateChildren)
			child.setType(entityTypeRepository.getEntityTypes(child.getIri()));
		for (EntityReferenceNode child : immediateChildren) {
			List<TTIriRef> grandChildren = getChildren(child.getIri(), 0, 1, inactive);
			child.setHasChildren(!grandChildren.isEmpty());
			result.add(child);
		}
		return result;
	}

	private List<TTIriRef> getChildren(String iri, int rowNumber, Integer pageSize, boolean inactive)
			throws SQLException {

		return entityTripleRepository.findImmediateChildrenByIri(iri, rowNumber, pageSize, inactive);
	}

	public List<EntityReferenceNode> getImmediateParents(String iri, Integer pageIndex, Integer pageSize,
			boolean includeLegacy, boolean inactive) throws SQLException {

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

	public List<TTIriRef> usages(String iri) throws SQLException {

		if (iri == null || iri.isEmpty())
			return Collections.emptyList();

		return entityTripleRepository.getActiveSubjectByObjectExcludeByPredicate(iri, IM.IS_A.getIri()).stream()
				.sorted(Comparator.comparing(TTIriRef::getName, Comparator.nullsLast(Comparator.naturalOrder())))
				.distinct().collect(Collectors.toList());
	}

	public List<EntitySummary> advancedSearch(SearchRequest request) throws Exception {
		if (request == null || request.getTermFilter() == null || request.getTermFilter().isEmpty())
			return Collections.emptyList();

		List<EntitySummary> matchingEntity = entitySearchRepository.advancedSearch(request);

		return matchingEntity.stream().sorted(Comparator.comparingInt(EntitySummary::getWeighting))
				.collect(Collectors.toList());
	}

	public ExportValueSet getValueSetMembers(String iri, boolean expand) throws SQLException {
        return getValueSetMembers(iri, expand, null);
    }

	public ExportValueSet getValueSetMembers(String iri, boolean expand, Integer limit) throws SQLException {
		if (iri == null || iri.isEmpty()) {
			return null;
		}
		ExportValueSet result = new ExportValueSet().setValueSet(getEntityReference(iri));

		Set<ValueSetMember> definedInclusions = getMember(iri, IM.HAS_MEMBER);
		Set<ValueSetMember> definedExclusions = getMember(iri, IM.NOT_MEMBER);

		int memberCount = 0;
		Map<String, ValueSetMember> evaluatedInclusions = processMembers(definedInclusions, expand, memberCount, limit);
		memberCount += evaluatedInclusions.size();
		Map<String, ValueSetMember> evaluatedExclusions = processMembers(definedExclusions, expand, memberCount, limit);
        memberCount += evaluatedExclusions.size();

		if (limit != null && memberCount > limit)
		    return result.setLimited(true);

		if (expand) {
			// Remove exclusions by key
			evaluatedExclusions.forEach((k, v) -> evaluatedInclusions.remove(k));
		}

		result.addAllIncluded(evaluatedInclusions.values());
		if (!expand)
			result.addAllExcluded(evaluatedExclusions.values());

		return result;
	}

	private Set<ValueSetMember> getMember(String iri, TTIriRef predicate) throws SQLException {
		return entityTripleRepository.getObjectBySubjectAndPredicate(iri, predicate.getIri());
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
                    .forEach(m -> memberHashMap.put(m.getEntity().getIri() + "/" + m.getCode(), m));
			}
		}
		return memberHashMap;
	}

	public ValueSetMembership isValuesetMember(String valueSetIri, String memberIri) throws SQLException {
		if (valueSetIri == null || valueSetIri.isEmpty() || memberIri == null || memberIri.isEmpty())
			return null;
		ValueSetMembership result = new ValueSetMembership();
		Set<TTIriRef> included = getMemberIriRefs(valueSetIri, IM.HAS_MEMBER);
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

	public HttpEntity download(String iri, String format, boolean children, boolean parents, boolean dataModelProperties,
			boolean members, boolean expandMembers, boolean semanticProperties, boolean inactive)
        throws SQLException, IOException {
		if (iri == null || iri.isEmpty() || format == null || format.isEmpty())
			return null;
		TTIriRef entity = getEntityReference(iri);

        String filename = entity.getName() + " " + LocalDate.now() + ("excel".equals(format) ? ".xlsx" : ".json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
            "excel".equals(format) ? new MediaType("application", "force-download") : MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");

		if ("excel".equals(format)) {
            XlsHelper xls = new XlsHelper();

            if (children) xls.addChildren(getImmediateChildren(iri, null, null, inactive));
            if (parents) xls.addParents(getImmediateParents(iri, null, null, false, inactive));
            if (semanticProperties) xls.addSemanticProperties(getSemanticProperties(iri));
            if (dataModelProperties) xls.addDataModelProperties(getDataModelProperties(iri));
            if (members) xls.addMembers(getValueSetMembers(iri, expandMembers));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                xls.getWorkbook().write(outputStream);
                xls.getWorkbook().close();
            } catch (IOException e) {
                LOG.error("Error writing file: " + e.getMessage());
                throw e;
            }

            return new HttpEntity<>(outputStream.toByteArray(), headers);
        } else {
            DownloadDto downloadDto = new DownloadDto();

            if (children) downloadDto.setChildren(getImmediateChildren(iri, null, null, inactive));
            if (parents) downloadDto.setParents(getImmediateParents(iri, null, null, false, inactive));
            if (semanticProperties) downloadDto.setSemanticProperties(getSemanticProperties(iri));
            if (dataModelProperties) downloadDto.setDataModelProperties(getDataModelProperties(iri));
            if (members) downloadDto.setMembers(getValueSetMembers(iri, expandMembers));

            return new HttpEntity<>(downloadDto, headers);
        }
	}

	public List<DataModelProperty> getDataModelProperties(String iri) throws SQLException {
		TTEntity entity = getEntityPredicates(iri, Set.of(IM.PROPERTY_GROUP.getIri()));
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

	public String valueSetMembersCSV(String iri, boolean expanded) throws SQLException {
		ExportValueSet exportValueSet = getValueSetMembers(iri, expanded);
		StringBuilder valueSetMembers = new StringBuilder();
		valueSetMembers.append(
				"Inc\\Exc\tValueSetIri\tValueSetName\tMemberIri\tMemberTerm\tMemberCode\tMemberSchemeIri\tMemberSchemeName\n");
		if (exportValueSet == null)
			return valueSetMembers.toString();
		for (ValueSetMember inc : exportValueSet.getIncluded()) {
			appendValueSet(exportValueSet, valueSetMembers, inc, "Inc");
		}
		if (exportValueSet.getExcluded() != null) {
			for (ValueSetMember exc : exportValueSet.getExcluded()) {
				appendValueSet(exportValueSet, valueSetMembers, exc, "Exc");
			}
		}
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
		TTEntity entity = getEntityPredicates(iri, Set.of(IM.IS_A.getIri(), RDFS.LABEL.getIri()));

		GraphDto graphData = new GraphDto().setKey("0").setIri(entity.getIri()).setName(entity.getName());
		GraphDto graphParents = new GraphDto().setKey("0_0").setName("Is a");
		GraphDto graphChildren = new GraphDto().setKey("0_1").setName("Subtypes");

		List<GraphDto> semanticProps = getSemanticProperties(iri).stream()
				.map(prop -> new GraphDto(prop.getProperty().getIri(), prop.getProperty().getName(),
						prop.getType().getIri(), prop.getType().getName()))
				.collect(Collectors.toList());

		List<GraphDto> dataModelProps = getDataModelProperties(iri).stream()
				.map(prop -> new GraphDto(prop.getProperty().getIri(), prop.getProperty().getName(),
						prop.getType().getIri(), prop.getType().getName(), prop.getInheritedFrom().getIri(), prop.getInheritedFrom().getName()))
				.collect(Collectors.toList());

		List<GraphDto> isas = getEntityDefinedParents(entity, IM.IS_A);

		List<GraphDto> subtypes = getDefinitionSubTypes(iri).stream()
				.map(subtype -> new GraphDto().setName(subtype.getName()).setIri(subtype.getIri()))
				.collect(Collectors.toList());

		GraphDto semantic = new GraphDto().setKey("0_2").setName("Semantic properties");
		GraphDto semanticWrapper = new GraphDto().setKey("0_2_0").setType(GraphType.PROPERTIES);
		semanticWrapper.getLeafNodes()
				.addAll(semanticProps.stream().filter(prop -> prop.getInheritedFromIri() == null).collect(Collectors.toList()));
		semantic.getChildren()
				.add(semanticWrapper.getLeafNodes().isEmpty() ? new GraphDto().setKey("0_2_0").setType(GraphType.NONE)
						: semanticWrapper);

		GraphDto dataModel = new GraphDto().setKey("0_3").setName("Data model properties");
		GraphDto dataModelDirect = new GraphDto().setKey("0_3_0").setName("Direct");
		GraphDto dataModelDirectWrapper = new GraphDto().setKey("0_3_0_0").setType(GraphType.PROPERTIES);
		dataModelDirectWrapper.getLeafNodes()
				.addAll(dataModelProps.stream().filter(prop -> prop.getInheritedFromIri() == null).collect(Collectors.toList()));
		dataModelDirect.getChildren()
				.add(dataModelDirectWrapper.getLeafNodes().isEmpty() ? new GraphDto().setKey("0_3_0_0").setType(GraphType.NONE)
						: dataModelDirectWrapper);

		GraphDto dataModelInherited = new GraphDto().setKey("0_3_1").setName("Inherited");
		GraphDto dataModelInheritedWrapper = new GraphDto().setKey("0_3_1_0").setType(GraphType.PROPERTIES);
		dataModelInheritedWrapper.getLeafNodes()
				.addAll(dataModelProps.stream().filter(prop -> prop.getInheritedFromIri() != null).collect(Collectors.toList()));
		dataModelInherited.getChildren()
				.add(dataModelInheritedWrapper.getLeafNodes().isEmpty()
						? new GraphDto().setKey("0_3_1_0").setType(GraphType.NONE)
						: dataModelInheritedWrapper);
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
		if (!(semanticWrapper.getLeafNodes().isEmpty())) {
			graphData.getChildren().add(semantic);
		}
		if (!(dataModelDirectWrapper.getLeafNodes().isEmpty() && dataModelInheritedWrapper.getLeafNodes().isEmpty())) {
			graphData.getChildren().add(dataModel);
		}
		return graphData;
	}

	private List<GraphDto> getEntityDefinedParents(TTEntity entity, TTIriRef predicate) {
		TTValue parent = entity.get(predicate);
		if (parent == null)
			return Collections.emptyList();
		List<GraphDto> result = new ArrayList<>();
		if (parent.isList()) {
			parent.asArrayElements().forEach(item -> {
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

	public List<SemanticProperty> getSemanticProperties(String iri) throws SQLException {
		List<SemanticProperty> recordStructure = new ArrayList<>();
		TTEntity entity = getEntityPredicates(iri,Set.of(IM.ROLE_GROUP.getIri()));
		if (entity.has(IM.ROLE_GROUP)) {
			for (TTValue roleGroup : entity.asNode().get(IM.ROLE_GROUP).asArrayElements()) {
				if (roleGroup.asNode().has(OWL.ONPROPERTY)) {
					recordStructure.add(new SemanticProperty()
							.setProperty(
									new TTIriRef(roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
							.setType(
									new TTIriRef(roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName())));
				} else {
					if (roleGroup.asNode().has(IM.ROLE)) {
						roleGroup.asNode().get(IM.ROLE).asArrayElements().forEach(role -> recordStructure.add(new SemanticProperty()
                                .setProperty(
                                        new TTIriRef(role.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
                                                role.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
                                .setType(new TTIriRef(
                                        role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
                                        role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName()))));
					}
				}
			}
		}
		return recordStructure;
	}

	public List<TTIriRef> getDefinitionSubTypes(String iri) throws SQLException {

		return entityTripleRepository.findImmediateChildrenByIri(iri, null, null, false).stream()
				.map(t -> new TTIriRef(t.getIri(), t.getName())).collect(Collectors.toList());
	}

	public EntityDefinitionDto getEntityDefinitionDto(String iri) throws SQLException {
		TTEntity entity = getEntityPredicates(iri,Set.of(IM.IS_A.getIri(), RDF.TYPE.getIri(),RDFS.LABEL.getIri(),RDFS.COMMENT.getIri(),IM.STATUS.getIri()));
		List<TTIriRef> types = entity.getType() == null ? new ArrayList<>()
				: entity.getType().asArrayElements().stream()
						.map(t -> new TTIriRef(t.asIriRef().getIri(), t.asIriRef().getName()))
						.collect(Collectors.toList());

		List<TTIriRef> isa = !entity.has(IM.IS_A) ? new ArrayList<>()
				: entity.get(IM.IS_A).asArrayElements().stream()
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
}
