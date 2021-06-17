package org.endeavourhealth.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.dto.ConceptDefinitionDto;
import org.endeavourhealth.imapi.model.dto.DataModelPropertyDto;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.RecordStructureDto;
import org.endeavourhealth.imapi.model.dto.GraphDto.GraphType;
import org.endeavourhealth.imapi.model.dto.RecordStructureDto.Cardinality;
import org.endeavourhealth.imapi.model.dto.RecordStructureDto.ConceptReference;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.SHACL;
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
import java.util.stream.Stream;

@Component
public class ConceptService {
	private static final Logger LOG = LoggerFactory.getLogger(ConceptService.class);

	ConceptRepository conceptRepository = new ConceptRepository();

	ConceptTctRepository conceptTctRepository = new ConceptTctRepository();

	ConceptTripleRepository conceptTripleRepository = new ConceptTripleRepository();

	ValueSetRepository valueSetRepository = new ValueSetRepository();

	TermCodeRepository termCodeRepository = new TermCodeRepository();

	ConceptSearchRepository conceptSearchRepository = new ConceptSearchRepository();

	ConceptTypeRepository conceptTypeRepository = new ConceptTypeRepository();

	private ObjectMapper om = new ObjectMapper();

	public TTConcept getConcept(String iri) throws SQLException, JsonProcessingException {

		if (iri == null || iri.isEmpty())
			return null;

		try {
			TTConcept result = conceptRepository.getConceptByIri(iri);
			populateMissingNames(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public TTIriRef getConceptReference(String iri) throws SQLException {
		if (iri == null || iri.isEmpty())
			return null;
		return conceptRepository.getConceptReferenceByIri(iri);
	}

	public List<ConceptReferenceNode> getImmediateChildren(String iri, Integer pageIndex, Integer pageSize,
			boolean includeLegacy, boolean inactive) throws SQLException {

		if (iri == null || iri.isEmpty())
			return Collections.emptyList();

		List<ConceptReferenceNode> result = new ArrayList<>();
		int rowNumber = 0;
		if (pageIndex != null && pageSize != null)
			rowNumber = (pageIndex - 1) * pageSize;
		List<ConceptReferenceNode> immediateChildren = getChildren(iri, rowNumber, pageSize, inactive).stream()
				.map(c -> new ConceptReferenceNode(c.getIri(), c.getName())).collect(Collectors.toList());
		for (ConceptReferenceNode child : immediateChildren)
			child.setType(conceptTypeRepository.getConceptTypes(child.getIri()));
		for (ConceptReferenceNode child : immediateChildren) {
			List<TTIriRef> grandChildren = getChildren(child.getIri(), 0, 1, inactive);
			child.setHasChildren(!grandChildren.isEmpty());
			result.add(child);
		}
		return result;
	}

	private List<TTIriRef> getChildren(String iri, int rowNumber, Integer pageSize, boolean inactive)
			throws SQLException {

		return conceptTripleRepository.findImmediateChildrenByIri(iri, rowNumber, pageSize, inactive);
	}

	public List<ConceptReferenceNode> getImmediateParents(String iri, Integer pageIndex, Integer pageSize,
			boolean includeLegacy, boolean inactive) throws SQLException {

		if (iri == null || iri.isEmpty())
			return Collections.emptyList();

		int rowNumber = 0;
		if (pageIndex != null && pageSize != null)
			rowNumber = (pageIndex - 1) * pageSize;

		List<ConceptReferenceNode> parents = getParents(iri, rowNumber, pageSize, inactive).stream()
				.map(p -> new ConceptReferenceNode(p.getIri(), p.getName())).collect(Collectors.toList());

		for (ConceptReferenceNode parent : parents)
			parent.setType(conceptTypeRepository.getConceptTypes(parent.getIri()));

		return parents;
	}

	private List<TTIriRef> getParents(String iri, int rowNumber, Integer pageSize, boolean inactive)
			throws SQLException {

		return conceptTripleRepository.findImmediateParentsByIri(iri, rowNumber, pageSize, inactive);
	}

	public List<TTIriRef> isWhichType(String iri, List<String> candidates) throws SQLException {
		if (iri == null || iri.isEmpty() || candidates == null || candidates.isEmpty())
			return Collections.emptyList();
		return conceptTctRepository
				.findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(iri, IM.IS_A.getIri(), candidates).stream()
				.sorted(Comparator.comparing(TTIriRef::getName)).collect(Collectors.toList());
	}

	public List<TTIriRef> usages(String iri) throws SQLException {

		if (iri == null || iri.isEmpty())
			return Collections.emptyList();

		return conceptTripleRepository.getActiveSubjectByObjectExcludeByPredicate(iri, IM.IS_A.getIri()).stream()
				.sorted(Comparator.comparing(TTIriRef::getName, Comparator.nullsLast(Comparator.naturalOrder())))
				.distinct().collect(Collectors.toList());
	}

	public List<ConceptSummary> advancedSearch(SearchRequest request) throws Exception {
		if (request == null || request.getTermFilter() == null || request.getTermFilter().isEmpty())
			return Collections.emptyList();

		List<ConceptSummary> matchingConcept = conceptSearchRepository.advancedSearch(request);

		return matchingConcept.stream().sorted(Comparator.comparingInt(ConceptSummary::getWeighting))
				.collect(Collectors.toList());
	}

	public List<TTConcept> getAncestorDefinitions(String iri) throws SQLException {
		if (iri == null || iri.isEmpty()) {
			return Collections.emptyList();
		}
		try {
			List<TTConcept> concepts = conceptTctRepository.findByDescendant_Iri_AndType_Iri_OrderByLevel(iri,
					IM.IS_A.getIri());
			if (concepts == null || concepts.isEmpty())
				return Collections.emptyList();
			List<TTConcept> result = new ArrayList<>();
			for (TTConcept concept : concepts) {
				if (!iri.equals(concept.getIri()))
					result.add(concept);
			}
			return result;
		} catch (JsonProcessingException | IllegalArgumentException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
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
		ExportValueSet result = new ExportValueSet().setValueSet(getConceptReference(iri))
				.addAllIncluded(inclusions.values());
		if (!expand)
			result.addAllExcluded(exclusions.values());
		return result;
	}

	private Set<ValueSetMember> getMember(String iri, TTIriRef predicate) throws SQLException {
		return conceptTripleRepository.getObjectBySubjectAndPredicate(iri, predicate.getIri());
	}

	private Map<String, ValueSetMember> expandMember(Set<ValueSetMember> valueSetMembers, boolean expand)
			throws SQLException {
		Map<String, ValueSetMember> memberHashMap = new HashMap<>();
		for (ValueSetMember member : valueSetMembers) {
			memberHashMap.put(member.getConcept().getIri() + "/" + member.getCode(), member);

			if (expand) {
				valueSetRepository.expandMember(member.getConcept().getIri())
						.forEach(m -> memberHashMap.put(m.getConcept().getIri() + "/" + m.getCode(), m));
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
					.filter(em -> em.getConcept().getIri().equals(memberIri)).findFirst();
			if (match.isPresent()) {
				result.setIncludedBy(m);
				break;
			}
		}
		for (TTIriRef m : excluded) {
			Optional<ValueSetMember> match = valueSetRepository.expandMember(m.getIri()).stream()
					.filter(em -> em.getConcept().getIri().equals(memberIri)).findFirst();
			if (match.isPresent()) {
				result.setExcludedBy(m);
				break;
			}
		}
		return result;
	}

	private Set<TTIriRef> getMemberIriRefs(String valueSetIri, TTIriRef predicate) throws SQLException {
		return conceptTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(valueSetIri, predicate.getIri());

	}

	public List<TTIriRef> getCoreMappedFromLegacy(String legacyIri) throws SQLException {
		if (legacyIri == null || legacyIri.isEmpty())
			return Collections.emptyList();
		return conceptTripleRepository.getCoreMappedFromLegacyBySubject_Iri_AndPredicate_Iri(legacyIri,
				IM.HAS_MAP.getIri());

	}

	public List<TTIriRef> getLegacyMappedToCore(String coreIri) throws SQLException {
		if (coreIri == null || coreIri.isEmpty())
			return Collections.emptyList();
		return conceptTripleRepository.findAllByObject_Iri_AndPredicate_Iri(coreIri, IM.MATCHED_TO.getIri());

	}

	private void populateMissingNames(TTConcept concept) throws SQLException {
		if (concept == null)
			return;

		// Get both predicate and object TTIriRefs
		List<TTIriRef> iriRefs = new ArrayList<>();
		List<TTIriRef> predicates = new ArrayList<>();

		TTVisitor visitor = new TTVisitor();
		visitor.IriRefVisitor = ((predicate, iriRef) -> iriRefs.add(iriRef));
		visitor.PredicateVisitor = (predicates::add);
		visitor.visit(concept);

		populateMissingNames(iriRefs);
		populateMissingNames(predicates);
	}

	private void populateMissingNames(List<TTIriRef> iriRefs) throws SQLException {
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
		Map<String, String> iriNameMap = new HashMap<>();
		for (TTIriRef concept1 : conceptRepository.findAllByIriIn(iris))
			iriNameMap.put(concept1.getIri(), concept1.getName());

		return iriNameMap;
	}

	public List<TermCode> getConceptTermCodes(String iri) throws SQLException {
		if (iri == null || iri.isEmpty())
			return Collections.emptyList();
		return termCodeRepository.findAllByConcept_Iri(iri);
	}

	public HttpEntity download(String iri, String format, boolean children, boolean parents, boolean properties,
			boolean members, boolean expandMembers, boolean roles, boolean inactive)
			throws SQLException, JsonProcessingException {
		if (iri == null || iri.isEmpty() || format == null || format.isEmpty())
			return null;
		TTConcept concept = getConcept(iri);
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

		String filename = concept.getName() + " " + LocalDate.now() + (format.equals("excel") ? ".xlsx" : ".json");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				format.equals("excel") ? new MediaType("application", "force-download") : MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");

		return new HttpEntity(format.equals("excel") ? outputStream.toByteArray() : downloadDto, headers);

	}

	private void addChildrenToDownload(String iri, String format, boolean inactive, XlsHelper xls,
			DownloadDto downloadDto) throws SQLException {
		List<ConceptReferenceNode> childrenList = getImmediateChildren(iri, null, null, false, inactive);
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
		List<ConceptReferenceNode> parentList = getImmediateParents(iri, null, null, false, inactive);
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
		TTConcept concept = getConcept(iri);
		return getAllProperties(concept);
	}

	public List<PropertyValue> getAllProperties(TTConcept concept) {
		List<PropertyValue> properties = new ArrayList<PropertyValue>();
		if (concept == null)
			return Collections.emptyList();
		if (concept.has(IM.PROPERTY_GROUP)) {
			for (TTValue propertyGroup : concept.getAsArray(IM.PROPERTY_GROUP).getElements()) {
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
		TTConcept concept = getConcept(iri);
		List<PropertyValue> roles = new ArrayList<PropertyValue>();
		if (concept == null)
			return Collections.emptyList();
		if (concept.has(IM.ROLE_GROUP)) {
			for (TTValue roleGroup : concept.getAsArray(IM.ROLE_GROUP).getElements()) {
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
				.append(exportValueSet.getValueSet().getName()).append("\t").append(setMember.getConcept().getIri())
				.append("\t").append(setMember.getConcept().getName()).append("\t").append(setMember.getCode())
				.append("\t");
		if (setMember.getScheme() != null)
			valueSetMembers.append(setMember.getScheme().getIri()).append("\t").append(setMember.getScheme().getName());

		valueSetMembers.append("\n");
	}

	public GraphDto getGraphData(String iri) throws SQLException, JsonProcessingException {
		TTConcept concept = getConcept(iri);

		if (concept == null) {
			LOG.error("Unable to find concept {}", iri);
			return null;
		}

		GraphDto graphData = new GraphDto().setKey("0").setIri(concept.getIri()).setName(concept.getName());

		GraphDto graphParents = new GraphDto().setKey("0_0").setName("Is a");
		GraphDto graphChildren = new GraphDto().setKey("0_1").setName("Subtypes");
		GraphDto graphProps = new GraphDto().setKey("0_2").setName("Properties");

		List<GraphDto> props = getRecordStructure(iri).stream()
				.map(prop -> new GraphDto(prop.getProperty().getIri(), prop.getProperty().getName(),
						prop.getType().getIri(), prop.getType().getName(), prop.getInherited().getIri(),
						prop.getInherited().getName()))
				.collect(Collectors.toList());

		List<GraphDto> isas = getConceptDefinedParents(concept, IM.IS_A);

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

	private List<GraphDto> getConceptDefinedParents(TTConcept concept, TTIriRef predicate) {
		TTValue parent = concept.get(predicate);
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
		TTConcept concept = getConcept(iri);
		for (PropertyValue prop : getAllProperties(iri)) {
			recordStructure.add(new RecordStructureDto()
					.setProperty(new ConceptReference(prop.getProperty().getIri(), prop.getProperty().getName()))
					.setType(new ConceptReference(prop.getValueType().getIri(), prop.getValueType().getName()))
					.setInherited(prop.getInheritedFrom() == null ? null
							: new ConceptReference(prop.getInheritedFrom().getIri(), prop.getInheritedFrom().getName()))
					.setCardinality(new Cardinality(prop.getMaxExclusive(), prop.getMaxInclusive(),
							prop.getMinExclusive(), prop.getMinInclusive())));
		}
		if (concept.has(IM.ROLE_GROUP)) {
			for (TTValue roleGroup : concept.asNode().get(IM.ROLE_GROUP).asArrayElements()) {
				if (roleGroup.asNode().has(OWL.ONPROPERTY)) {
					recordStructure.add(new RecordStructureDto()
							.setProperty(
									new ConceptReference(roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
							.setType(
									new ConceptReference(roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName())));
				} else {
					if (roleGroup.asNode().has(IM.ROLE)) {
						roleGroup.asNode().get(IM.ROLE).asArrayElements().forEach(role -> {
							recordStructure.add(new RecordStructureDto()
									.setProperty(
											new ConceptReference(role.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
													role.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
									.setType(new ConceptReference(
											role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
											role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName())));
						});
					}

				}
			}
		}
		return recordStructure;
	}

	public List<ConceptReference> getDefinitionSubTypes(String iri) throws SQLException {

		return conceptTripleRepository.findImmediateChildrenByIri(iri, null, null, false).stream()
				.map(t -> new ConceptReference(t.getIri(), t.getName())).collect(Collectors.toList());
	}

	public ConceptDefinitionDto getConceptDefinitionDto(String iri) throws JsonProcessingException, SQLException {
		TTConcept concept = getConcept(iri);
		List<ConceptReference> types = concept.getType() == null ? new ArrayList<>()
				: concept.getType().asArrayElements().stream()
						.map(t -> new ConceptReference(t.asIriRef().getIri(), t.asIriRef().getName()))
						.collect(Collectors.toList());

		List<ConceptReference> isa = !concept.has(IM.IS_A) ? new ArrayList<>()
				: concept.get(IM.IS_A).asArrayElements().stream()
						.map(t -> new ConceptReference(t.asIriRef().getIri(), t.asIriRef().getName()))
						.collect(Collectors.toList());

		return new ConceptDefinitionDto().setIri(concept.getIri()).setName(concept.getName())
				.setDescription(concept.getDescription())
				.setStatus(concept.getStatus() == null ? null : concept.getStatus().getName()).setTypes(types)
				.setSubtypes(getDefinitionSubTypes(iri)).setIsa(isa);
	}

	public List<DataModelPropertyDto> getDataModelProperties(String iri) throws JsonProcessingException, SQLException {
		List<DataModelPropertyDto> properties = new ArrayList<DataModelPropertyDto>();
		TTConcept concept = getConcept(iri);
		if (concept.has(SHACL.PROPERTY)) {
			for (TTValue property : concept.asNode().get(SHACL.PROPERTY).asArrayElements()) {
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
						.setProperty(new ConceptReference(property.asNode().get(SHACL.PATH).asIriRef().getIri(),
								property.asNode().get(SHACL.PATH).asIriRef().getName()))
						.setRange(new ConceptReference(rangeIri, rangeName)));
			}
		}
		if (concept.has(IM.ROLE_GROUP)) {
			for (TTValue roleGroup : concept.asNode().get(IM.ROLE_GROUP).asArrayElements()) {
				if (roleGroup.asNode().has(OWL.ONPROPERTY)) {
					properties.add(new DataModelPropertyDto()
							.setProperty(
									new ConceptReference(roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
							.setRange(
									new ConceptReference(roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
											roleGroup.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName())));
				} else {
					roleGroup.asNode().get(IM.ROLE).asArrayElements().forEach(role -> {
						properties.add(new DataModelPropertyDto()
								.setProperty(new ConceptReference(role.asNode().get(OWL.ONPROPERTY).asIriRef().getIri(),
										role.asNode().get(OWL.ONPROPERTY).asIriRef().getName()))
								.setRange(
										new ConceptReference(role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getIri(),
												role.asNode().get(OWL.SOMEVALUESFROM).asIriRef().getName())));
					});
				}
			}
		}
		return properties;
	}

	public TTArray getComplexMappings(String iri) throws JsonProcessingException, SQLException {
		TTConcept ttConcept = conceptRepository.getConceptByIri(iri);
		if (!ttConcept.has(IM.HAS_MAP)) {
			return new TTArray();
		}
		TTArray ttArray = ttConcept.getAsArray(IM.HAS_MAP);
		TTVisitor visitor = new TTVisitor();
		List<TTIriRef> iris = new ArrayList<>();
		visitor.IriRefVisitor = ((predicate, iriRef) -> {
			iris.add(iriRef);
		});
		visitor.visit(ttArray);
		populateMissingNames(iris);
		return ttArray;
	}
}
