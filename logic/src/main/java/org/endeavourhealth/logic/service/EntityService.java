package org.endeavourhealth.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.entity.Tpl;
import org.endeavourhealth.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.DataModelPropertyDto;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.RecordStructureDto;
import org.endeavourhealth.imapi.model.dto.GraphDto.GraphType;
import org.endeavourhealth.imapi.model.dto.RecordStructureDto.Cardinality;
import org.endeavourhealth.imapi.model.dto.RecordStructureDto.EntityReference;
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

	private ObjectMapper om = new ObjectMapper();

	public TTEntity getEntityPredicates(String iri, Set<String> predicates) throws SQLException {
        TTEntity result = new TTEntity(iri);

        // Temp fix for type array
        result.set(RDF.TYPE, new TTArray());
        result.set(IM.IS_A, new TTArray());
        result.set(IM.HAS_MAP, new TTArray());

        List<Tpl> triples = entityTripleRepository.getTriplesRecursive(iri, predicates);

        // Reconstruct
        HashMap<Integer, TTNode> nodeMap = new HashMap<>();

        for (Tpl triple : triples) {
            TTValue v;

            if (triple.getLiteral() != null)
                v = literal(triple.getLiteral(), triple.getObject());
            else if (triple.getObject() != null)
                v = triple.getObject();
            else {
                v = new TTNode();
                nodeMap.put(triple.getDbid(), (TTNode) v);
            }


            if (triple.getParent() == null)
                if (result.has(triple.getPredicate()))
                    result.addObject(triple.getPredicate(), v);
                else
                    result.set(triple.getPredicate(), v);
            else {
                TTNode n = nodeMap.get(triple.getParent());
                if (n == null)
                    throw new IllegalStateException("Unknown parent node!");

                if (n.has(triple.getPredicate()))
                    n.addObject(triple.getPredicate(), v);
                else
                    n.set(triple.getPredicate(), v);
            }
        }

        return result;
    }

//	public TTEntity getEntity(String iri) throws SQLException, JsonProcessingException {
//
//		if (iri == null || iri.isEmpty())
//			return null;
//
//		try {
//			TTEntity result = entityRepository.getEntityByIri(iri);
//			populateMissingNames(result);
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}

	public TTIriRef getEntityReference(String iri) throws SQLException {
		if (iri == null || iri.isEmpty())
			return null;
		return entityRepository.getEntityReferenceByIri(iri);
	}

	public List<EntityReferenceNode> getImmediateChildren(String iri, Integer pageIndex, Integer pageSize,
			boolean includeLegacy, boolean inactive) throws SQLException {

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
				.findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(iri, IM.IS_A.getIri(), candidates).stream()
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
		if (iri == null || iri.isEmpty()) {
			return null;
		}

		Set<ValueSetMember> included = getMember(iri, IM.HAS_MEMBER);
		Set<ValueSetMember> excluded = getMember(iri, IM.NOT_MEMBER);
		Map<String, ValueSetMember> inclusions = expandMember(included, expand);
		Map<String, ValueSetMember> exclusions = expandMember(excluded, expand);
		if (expand) {
			// Remove exclusions by key
			exclusions.forEach((k, v) -> inclusions.remove(k));
		}
		ExportValueSet result = new ExportValueSet().setValueSet(getEntityReference(iri))
				.addAllIncluded(inclusions.values());
		if (!expand)
			result.addAllExcluded(exclusions.values());
		return result;
	}

	private Set<ValueSetMember> getMember(String iri, TTIriRef predicate) throws SQLException {
		return entityTripleRepository.getObjectBySubjectAndPredicate(iri, predicate.getIri());
	}

	private Map<String, ValueSetMember> expandMember(Set<ValueSetMember> valueSetMembers, boolean expand)
			throws SQLException {
		Map<String, ValueSetMember> memberHashMap = new HashMap<>();
		for (ValueSetMember member : valueSetMembers) {
			memberHashMap.put(member.getEntity().getIri() + "/" + member.getCode(), member);

			if (expand) {
				valueSetRepository.expandMember(member.getEntity().getIri())
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
		return entityTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(valueSetIri, predicate.getIri());

	}

	private void populateMissingNames(TTEntity entity) throws SQLException {
		if (entity == null)
			return;

		// Get both predicate and object TTIriRefs
		List<TTIriRef> iriRefs = new ArrayList<>();
		List<TTIriRef> predicates = new ArrayList<>();

		TTVisitor visitor = new TTVisitor();
		visitor.IriRefVisitor = ((predicate, iriRef) -> iriRefs.add(iriRef));
		visitor.PredicateVisitor = (predicates::add);
		visitor.visit(entity);

		populateMissingNames(iriRefs);
		populateMissingNames(predicates);
	}

	private void populateMissingNames(List<TTIriRef> iriRefs) throws SQLException {
		if(iriRefs==null || iriRefs.isEmpty())
			return;
		List<TTIriRef> unNamed = new ArrayList<>();
		Set<String> iris = new HashSet<>();

		iriRefs.forEach(r -> {
			if (r.getName() == null || r.getName().isEmpty()) {
				unNamed.add(r);
				iris.add(r.getIri());
			}
		});

		Map<String, String> iriNameMap = getIriNameMap(iris);

		for (TTIriRef iriRef : unNamed) {
			iriRef.setName(iriNameMap.get(iriRef.getIri()));
		}
	}

	private Map<String, String> getIriNameMap(Set<String> iris) throws SQLException {
        if (iris.isEmpty())
            return new HashMap<>();

        Map<String, String> iriNameMap = new HashMap<>();
        for (TTIriRef entity1 : entityRepository.findAllByIriIn(iris))
            iriNameMap.put(entity1.getIri(), entity1.getName());
        return iriNameMap;
    }

	public List<TermCode> getEntityTermCodes(String iri) throws SQLException {
		if (iri == null || iri.isEmpty())
			return Collections.emptyList();
		return termCodeRepository.findAllByEntity_Iri(iri);
	}

	public HttpEntity download(String iri, String format, boolean children, boolean parents, boolean properties,
			boolean members, boolean expandMembers, boolean roles, boolean inactive)
			throws SQLException, JsonProcessingException {
		if (iri == null || iri.isEmpty() || format == null || format.isEmpty())
			return null;
		TTIriRef entity = getEntityReference(iri);
		XlsHelper xls = new XlsHelper();
		DownloadDto downloadDto = new DownloadDto();

		if (children)
			addChildrenToDownload(iri, format, inactive, xls, downloadDto);
		if (parents)
			addParentsToDownload(iri, format, inactive, xls, downloadDto);
		if (properties)
			addPropertiesToDownload(iri, format, xls, downloadDto);
		if (members)
			addMembersToDownload(iri, expandMembers, format, xls, downloadDto);
		if (roles)
			addRolesToDownload(iri, format, xls, downloadDto);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			xls.getWorkbook().write(outputStream);
			xls.getWorkbook().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String filename = entity.getName() + " " + LocalDate.now() + (format.equals("excel") ? ".xlsx" : ".json");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				format.equals("excel") ? new MediaType("application", "force-download") : MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");

		return new HttpEntity(format.equals("excel") ? outputStream.toByteArray() : downloadDto, headers);

	}

	private void addChildrenToDownload(String iri, String format, boolean inactive, XlsHelper xls,
			DownloadDto downloadDto) throws SQLException {
		List<EntityReferenceNode> childrenList = getImmediateChildren(iri, null, null, false, inactive);
		switch (format) {
		case "excel":
			xls.addChildren(childrenList);
			break;
		case "json":
			downloadDto.setChildren(childrenList);
			break;
		}
	}

	private void addParentsToDownload(String iri, String format, boolean inactive, XlsHelper xls,
			DownloadDto downloadDto) throws SQLException {
		List<EntityReferenceNode> parentList = getImmediateParents(iri, null, null, false, inactive);
		switch (format) {
		case "excel":
			xls.addParents(parentList);
			break;
		case "json":
			downloadDto.setParents(parentList);
			break;
		}
	}

	private void addPropertiesToDownload(String iri, String format, XlsHelper xls, DownloadDto downloadDto)
			throws SQLException, JsonProcessingException {
		List<PropertyValue> propertyList = getAllProperties(iri);
		switch (format) {
		case "excel":
			xls.addProperties(propertyList);
			break;
		case "json":
			downloadDto.setProperties(propertyList);
			break;
		}
	}

	private void addMembersToDownload(String iri, boolean expandMembers, String format, XlsHelper xls,
			DownloadDto downloadDto) throws SQLException {
		ExportValueSet exportValueSet = getValueSetMembers(iri, expandMembers);
		switch (format) {
		case "excel":
			xls.addMembers(exportValueSet);
			break;
		case "json":
			downloadDto.setMembers(exportValueSet);
			break;
		}
	}

	private void addRolesToDownload(String iri, String format, XlsHelper xls, DownloadDto downloadDto)
			throws SQLException, JsonProcessingException {
		List<PropertyValue> roleList = getRoles(iri);
		switch (format) {
		case "excel":
			xls.addRoles(roleList);
			break;
		case "json":
			downloadDto.setRoles(roleList);
			break;
		}
	}

	public List<PropertyValue> getAllProperties(String iri) throws SQLException, JsonProcessingException {
		TTEntity entity = getEntityPredicates(iri, Set.of(IM.PROPERTY_GROUP.getIri()));
		return getAllProperties(entity);
	}

	public List<PropertyValue> getAllProperties(TTEntity entity) {
		List<PropertyValue> properties = new ArrayList<PropertyValue>();
		if (entity == null)
			return Collections.emptyList();
		if (entity.has(IM.PROPERTY_GROUP)) {
			for (TTValue propertyGroup : entity.getAsArray(IM.PROPERTY_GROUP).getElements()) {
				if (propertyGroup.isNode()) {
					TTIriRef inheritedFrom = propertyGroup.asNode().has(IM.INHERITED_FROM)
							? propertyGroup.asNode().get(IM.INHERITED_FROM).asIriRef()
							: null;
					if (propertyGroup.asNode().has(SHACL.PROPERTY)) {
						for (TTValue property : propertyGroup.asNode().get(SHACL.PROPERTY).asArray().getElements()) {
							TTIriRef propertyPath = property.asNode().get(SHACL.PATH).asIriRef();
							if (properties.stream()
									.noneMatch(o -> o.getProperty().getIri().equals(propertyPath.getIri()))) {
								properties.add(getPropertyValue(inheritedFrom, property, propertyPath));
							}
						}
					}
				}
			}
		}
		return properties;
	}

	private PropertyValue getPropertyValue(TTIriRef inheritedFrom, TTValue property, TTIriRef propertyPath) {
		PropertyValue pv = new PropertyValue().setInheritedFrom(inheritedFrom).setProperty(propertyPath);

		if (property.asNode().has(SHACL.CLASS))
			pv.setValueType(property.asNode().get(SHACL.CLASS).asIriRef());
		if (property.asNode().has(SHACL.DATATYPE))
			pv.setValueType(property.asNode().get(SHACL.DATATYPE).asIriRef());
		if (property.asNode().has(SHACL.MAXCOUNT))
			pv.setMaxExclusive(property.asNode().get(SHACL.MAXCOUNT).asLiteral().getValue());
		if (property.asNode().has(SHACL.MINCOUNT))
			pv.setMinExclusive(property.asNode().get(SHACL.MINCOUNT).asLiteral().getValue());
		return pv;
	}

	public List<PropertyValue> getRoles(String iri) throws SQLException, JsonProcessingException {
		TTEntity entity = getEntityPredicates(iri,Set.of(IM.ROLE_GROUP.getIri()));
		List<PropertyValue> roles = new ArrayList<PropertyValue>();
		if (entity == null)
			return Collections.emptyList();
		if (entity.has(IM.ROLE_GROUP)) {
			for (TTValue roleGroup : entity.getAsArray(IM.ROLE_GROUP).getElements()) {
				if (roleGroup.isNode()) {
					HashMap<TTIriRef, TTValue> role = roleGroup.asNode().getPredicateMap();
					role.forEach((key, value) -> {
						if (!IM.COUNTER.equals(key)) {
							PropertyValue pv = new PropertyValue().setProperty(key).setValueType(value.asIriRef());
							roles.add(pv);
						}
					});
				}
			}
		}
		return roles;
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

	public GraphDto getGraphData(String iri) throws SQLException, JsonProcessingException {
		TTEntity entity = getEntityPredicates(iri, Set.of(IM.IS_A.getIri()));

		if (entity == null) {
			LOG.error("Unable to find entity {}", iri);
			return null;
		}

		GraphDto graphData = new GraphDto().setKey("0").setIri(entity.getIri()).setName(entity.getName());

		GraphDto graphParents = new GraphDto().setKey("0_0").setName("Is a");
		GraphDto graphChildren = new GraphDto().setKey("0_1").setName("Subtypes");
		GraphDto graphProps = new GraphDto().setKey("0_2").setName("Properties");

		List<GraphDto> props = getRecordStructure(iri).stream()
				.map(prop -> new GraphDto(prop.getProperty().getIri(), prop.getProperty().getName(),
						prop.getType().getIri(), prop.getType().getName(), prop.getInherited().getIri(),
						prop.getInherited().getName()))
				.collect(Collectors.toList());

		List<GraphDto> isas = getEntityDefinedParents(entity, IM.IS_A);

		List<GraphDto> subtypes = getDefinitionSubTypes(iri).stream()
				.map(subtype -> new GraphDto().setName(subtype.getName()).setIri(subtype.getIri()))
				.collect(Collectors.toList());

		GraphDto direct = new GraphDto().setKey("0_2_0").setName("Direct");
		GraphDto directWrapper = new GraphDto().setKey("0_2_0_0").setType(GraphType.PROPERTIES);
		directWrapper.getLeafNodes()
				.addAll(props.stream().filter(prop -> prop.getInheritedFromIri() == null).collect(Collectors.toList()));
		direct.getChildren()
				.add(directWrapper.getLeafNodes().isEmpty() ? new GraphDto().setKey("0_2_0_0").setType(GraphType.NONE)
						: directWrapper);

		GraphDto inherited = new GraphDto().setKey("0_2_1").setName("Inherited");
		GraphDto inheritedWrapper = new GraphDto().setKey("0_2_1_0").setType(GraphType.PROPERTIES);
		inheritedWrapper.getLeafNodes()
				.addAll(props.stream().filter(prop -> prop.getInheritedFromIri() != null).collect(Collectors.toList()));
		inherited.getChildren()
				.add(inheritedWrapper.getLeafNodes().isEmpty()
						? new GraphDto().setKey("0_2_1_0").setType(GraphType.NONE)
						: inheritedWrapper);

		GraphDto childrenWrapper = new GraphDto().setKey("0_1_0")
				.setType(!subtypes.isEmpty() ? GraphType.SUBTYPE : GraphType.NONE);
		childrenWrapper.getLeafNodes().addAll(subtypes);

		GraphDto parentsWrapper = new GraphDto().setKey("0_0_0")
				.setType(!isas.isEmpty() ? GraphType.ISA : GraphType.NONE);
		parentsWrapper.getLeafNodes().addAll(isas);

		graphParents.getChildren().add(parentsWrapper);
		graphChildren.getChildren().add(childrenWrapper);
		graphProps.getChildren().add(direct);
		graphProps.getChildren().add(inherited);

		graphData.getChildren().add(graphParents);
		graphData.getChildren().add(graphChildren);
		graphData.getChildren().add(graphProps);

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

	public List<RecordStructureDto> getRecordStructure(String iri) throws SQLException, JsonProcessingException {
		List<RecordStructureDto> recordStructure = new ArrayList<RecordStructureDto>();
		TTEntity entity = getEntityPredicates(iri,Set.of(IM.ROLE_GROUP.getIri()));
		for (PropertyValue prop : getAllProperties(iri)) {
			recordStructure.add(new RecordStructureDto()
					.setProperty(new EntityReference(prop.getProperty().getIri(), prop.getProperty().getName()))
					.setType(new EntityReference(prop.getValueType().getIri(), prop.getValueType().getName()))
					.setInherited(prop.getInheritedFrom() == null ? null
							: new EntityReference(prop.getInheritedFrom().getIri(), prop.getInheritedFrom().getName()))
					.setCardinality(new Cardinality(prop.getMaxExclusive(), prop.getMaxInclusive(),
							prop.getMinExclusive(), prop.getMinInclusive())));
		}
		if (entity.has(IM.ROLE_GROUP)) {
			for (TTValue roleGroup : entity.asNode().get(IM.ROLE_GROUP).asArrayElements()) {
				if (roleGroup.asNode().has(OWL.ONPROPERTY)) {
					recordStructure.add(new RecordStructureDto()
							.setProperty(
									new EntityReference(roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
							.setType(
									new EntityReference(roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName())));
				} else {
					if (roleGroup.asNode().has(IM.ROLE)) {
						roleGroup.asNode().get(IM.ROLE).asArrayElements().forEach(role -> {
							recordStructure.add(new RecordStructureDto()
									.setProperty(
											new EntityReference(role.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
													role.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
									.setType(new EntityReference(
											role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
											role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName())));
						});
					}

				}
			}
		}
		return recordStructure;
	}

	public List<EntityReference> getDefinitionSubTypes(String iri) throws SQLException {

		return entityTripleRepository.findImmediateChildrenByIri(iri, null, null, false).stream()
				.map(t -> new EntityReference(t.getIri(), t.getName())).collect(Collectors.toList());
	}

	public EntityDefinitionDto getEntityDefinitionDto(String iri) throws JsonProcessingException, SQLException {
		TTEntity entity = getEntityPredicates(iri,Set.of(IM.IS_A.getIri(), RDF.TYPE.getIri(),RDFS.LABEL.getIri(),RDFS.COMMENT.getIri(),IM.STATUS.getIri()));
		List<EntityReference> types = entity.getType() == null ? new ArrayList<>()
				: entity.getType().asArrayElements().stream()
						.map(t -> new EntityReference(t.asIriRef().getIri(), t.asIriRef().getName()))
						.collect(Collectors.toList());

		List<EntityReference> isa = !entity.has(IM.IS_A) ? new ArrayList<>()
				: entity.get(IM.IS_A).asArrayElements().stream()
						.map(t -> new EntityReference(t.asIriRef().getIri(), t.asIriRef().getName()))
						.collect(Collectors.toList());

		return new EntityDefinitionDto().setIri(entity.getIri()).setName(entity.getName())
				.setDescription(entity.getDescription())
				.setStatus(entity.getStatus() == null ? null : entity.getStatus().getName()).setTypes(types)
				.setSubtypes(getDefinitionSubTypes(iri)).setIsa(isa);
	}

	public List<DataModelPropertyDto> getDataModelProperties(String iri) throws JsonProcessingException, SQLException {
		List<DataModelPropertyDto> properties = new ArrayList<DataModelPropertyDto>();
		TTEntity entity = getEntityPredicates(iri,Set.of(SHACL.PROPERTY.getIri(),IM.ROLE_GROUP.getIri()));
		if (entity.has(SHACL.PROPERTY)) {
			for (TTValue property : entity.asNode().get(SHACL.PROPERTY).asArrayElements()) {
				String rangeIri = "";
				String rangeName = "";
				if (property.asNode().has(SHACL.CLASS)) {
					rangeIri = property.asNode().get(SHACL.CLASS).asIriRef().getIri();
					rangeName = property.asNode().get(SHACL.CLASS).asIriRef().getName();
				}

				if (property.asNode().has(SHACL.DATATYPE)) {
					rangeIri = property.asNode().get(SHACL.DATATYPE).asIriRef().getIri();
					rangeName = property.asNode().get(SHACL.DATATYPE).asIriRef().getName();
				}
				properties.add(new DataModelPropertyDto()
						.setProperty(new EntityReference(property.asNode().get(SHACL.PATH).asIriRef().getIri(),
								property.asNode().get(SHACL.PATH).asIriRef().getName()))
						.setRange(new EntityReference(rangeIri, rangeName)));
			}
		}
		if (entity.has(IM.ROLE_GROUP)) {
			for (TTValue roleGroup : entity.asNode().get(IM.ROLE_GROUP).asArrayElements()) {
				if (roleGroup.asNode().has(OWL.ONPROPERTY)) {
					properties.add(new DataModelPropertyDto()
							.setProperty(
									new EntityReference(roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
							.setRange(
									new EntityReference(roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName())));
				} else {
					roleGroup.asNode().get(IM.ROLE).asArrayElements().forEach(role -> {
						properties.add(new DataModelPropertyDto()
								.setProperty(new EntityReference(role.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
										role.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
								.setRange(
										new EntityReference(role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
												role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName())));
					});
				}
			}
		}
		return properties;
	}

	public EntitySummary getSummary(String iri) throws SQLException {
		return entitySearchRepository.getSummary(iri);
	}
}
