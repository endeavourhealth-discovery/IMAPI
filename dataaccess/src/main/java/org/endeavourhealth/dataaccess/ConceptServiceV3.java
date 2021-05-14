package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.entity.*;
import org.endeavourhealth.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.download.DownloadDto;
import org.endeavourhealth.imapi.model.graph.GraphDto;
import org.endeavourhealth.imapi.model.TermCode;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ConceptServiceV3 {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptServiceV3.class);

    @Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptTctRepository conceptTctRepository;

	@Autowired
	ConceptTripleRepository conceptTripleRepository;

	@Autowired
	ValueSetRepository valueSetRepository;

	@Autowired
    TermCodeRepository termCodeRepository;

	@Autowired
	SearchRepository searchRepository;

	@Autowired
	ConceptSearchRepository conceptSearchRepository;

	@Autowired
	ConceptTypeRepository conceptTypeRepository;

	private ObjectMapper om = new ObjectMapper();

	public TTConcept getConcept(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		if (concept == null)
			return null;

		if (concept.getJson() == null || concept.getJson().isEmpty()) {
		    LOG.error("Concept is missing definition {}", iri);
		    return null;
        }

		try {
			TTConcept result = om.readValue(concept.getJson(), TTConcept.class);
			populateMissingNames(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public TTIriRef getConceptReference(String iri) {
		if(iri==null||iri.isEmpty())
			return null;
		Concept concept = conceptRepository.findByIri(iri);
		if (concept == null)
			return null;

		return new TTIriRef(concept.getIri(), concept.getName());
	}

	public List<ConceptReferenceNode> getImmediateChildren(String iri, Integer pageIndex, Integer pageSize,
			boolean includeLegacy, boolean inactive) {

		if(iri == null || iri.isEmpty())
			return Collections.emptyList();

        List<ConceptReferenceNode> result = new ArrayList<>();

        Pageable pageable = null;
        if (pageIndex != null && pageSize!=null){
            pageable = PageRequest.of(pageIndex - 1, pageSize);
        }

        List<ConceptReferenceNode> immediateChildren = inactive ? getAllStatusChildren(iri, pageable) : getActiveChildren(iri, pageable);
        for (ConceptReferenceNode child : immediateChildren) {
                List<ConceptReferenceNode> grandChildren = inactive ? getAllStatusChildren(child.getIri(), pageable)
						: getActiveChildren(child.getIri(), pageable);
                child.setHasChildren(!grandChildren.isEmpty());
                result.add(child);
        }
        return result;
    }

	private List<ConceptReferenceNode> getActiveChildren(String iri, Pageable pageable) {
		return conceptTripleRepository
				.findImmediateChildrenByIri(iri, pageable).stream()
				.filter(t -> IM.ACTIVE.getIri().equals(t.getSubject().getStatus().getIri()))
				.map(t -> new ConceptReferenceNode(t.getSubject().getIri(), t.getSubject().getName())
						.setType(getConcept(t.getSubject().getIri()).getType())).collect(Collectors.toList());
	}
	
	private List<ConceptReferenceNode> getAllStatusChildren(String iri, Pageable pageable) {
		return conceptTripleRepository
				.findImmediateChildrenByIri(iri, pageable).stream()
				.map(t -> new ConceptReferenceNode(t.getSubject().getIri(), t.getSubject().getName())
						.setType(getConcept(t.getSubject().getIri()).getType())).collect(Collectors.toList());
	}

	public List<ConceptReferenceNode> getImmediateParents(String iri, Integer pageIndex, Integer pageSize,
			boolean includeLegacy, boolean inactive) {

		if(iri == null || iri.isEmpty())
			return Collections.emptyList();

        Pageable pageable = null;
        if (pageIndex != null && pageSize!=null){
            pageable = PageRequest.of(pageIndex - 1, pageSize);
        }

		return inactive ? getAllStatusParents(iri, pageable) : getActiveParents(iri, pageable);
	}
	
	private List<ConceptReferenceNode> getAllStatusParents(String iri, Pageable pageable) {
		return conceptTripleRepository
				.findImmediateParentsByIri(iri, pageable).stream()
				.filter(t -> !OWL.THING.getIri().equals(t.getObject().getIri()))
				.map(t -> new ConceptReferenceNode(t.getObject().getIri(), t.getObject().getName())
						.setType(getConcept(t.getObject().getIri()).getType())).collect(Collectors.toList());
	}


	private List<ConceptReferenceNode> getActiveParents(String iri, Pageable pageable) {
		return conceptTripleRepository
				.findImmediateParentsByIri(iri, pageable).stream()
				.filter(t -> IM.ACTIVE.getIri().equals(t.getObject().getStatus().getIri()))
				.filter(t -> !OWL.THING.getIri().equals(t.getObject().getIri()))
				.map(t -> new ConceptReferenceNode(t.getObject().getIri(), t.getObject().getName())
						.setType(getConcept(t.getObject().getIri()).getType())).collect(Collectors.toList());
	}

	public List<TTIriRef> isWhichType(String iri, List<String> candidates) {
		if(iri == null || iri.isEmpty() || candidates == null || candidates.isEmpty())
			return Collections.emptyList();
		return conceptTctRepository
				.findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(iri, IM.IS_A.getIri(), candidates).stream()
				.map(tct -> new TTIriRef(tct.getAncestor().getIri(), tct.getAncestor().getName()))
				.sorted(Comparator.comparing(TTIriRef::getName)).collect(Collectors.toList());
	}

	public List<TTIriRef> usages(String iri) {

		if(iri == null || iri.isEmpty())
			return Collections.emptyList();

        return conceptTripleRepository.findDistinctByObject_IriAndPredicate_IriNot(iri, IM.IS_A.getIri()).stream()
            .map(Tpl::getSubject)
            .map(c -> new TTIriRef().setIri(c.getIri()).setName(c.getName()))
            .sorted(Comparator.comparing(TTIriRef::getName, Comparator.nullsLast(Comparator.naturalOrder())))
            .distinct()
            .collect(Collectors.toList());
	}

	public List<ConceptSummary> advancedSearch(SearchRequest request) {

		if(request==null || request.getTermFilter()==null || request.getTermFilter().isEmpty())
			return Collections.emptyList();

		List<ConceptSearch> matchingConcept;

		String full = request.getTermFilter();
		String terms = Arrays.stream(full.split(" ")).filter(t -> t.trim().length() >= 3).map(w -> "+" + w + "*")
				.collect(Collectors.joining(" "));

		if (request.getSchemeFilter() == null || request.getSchemeFilter().isEmpty())
			matchingConcept = conceptSearchRepository.findLegacyByTerm(terms,request.getTypeFilter(), request.getStatusFilter(),
					request.getSize());
		else {
			matchingConcept = conceptSearchRepository.findLegacySchemesByTerm(terms,request.getSchemeFilter(),
					request.getTypeFilter(), request.getStatusFilter(), request.getSize());
		}

		List<ConceptSummary> result = matchingConcept.stream()
				.map(c -> convertConceptToConceptSummary(c, full))
				.sorted(Comparator.comparingInt(ConceptSummary::getWeighting))
				.collect(Collectors.toList());

		List<String> types = request.getMarkIfDescendentOf();
		if (types != null && !types.isEmpty()) {
			result.forEach(s -> s.setIsDescendentOf(this.isWhichType(s.getIri(), types)));
		}

		return result;
	}

	private ConceptSummary convertConceptToConceptSummary(ConceptSearch r,String full) {
		TTArray types = new TTArray();
		conceptTypeRepository.findAllByConcept_Dbid(r.getConcept().getDbid()).forEach(ct -> types.add(
		    new TTIriRef().setIri(ct.getType().getIri()).setName(ct.getType().getName()))
        );
		return new ConceptSummary()
				.setName(r.getConcept().getName())
				.setMatch(r.getTerm())
				.setIri(r.getConcept().getIri())
				.setWeighting(Levenshtein.calculate(full, r.getTerm()))
				// .setWeighting(r.getWeighting())
				.setCode(r.getConcept().getCode())
				.setDescription(r.getConcept().getDescription())
				.setConceptType(types)
				.setStatus(new TTIriRef(r.getConcept().getStatus().getIri(), r.getConcept().getStatus().getName()))
				.setScheme(r.getConcept().getScheme() == null ? null
						: new TTIriRef(r.getConcept().getScheme().getIri(), r.getConcept().getScheme().getName()));

	}

	public List<TTConcept> getAncestorDefinitions(String iri) {
		if(iri == null || iri.isEmpty()){
			return  Collections.emptyList();
		}
		try {
			List<TTConcept> result = new ArrayList<>();
			for (Tct tct : conceptTctRepository.findByDescendant_Iri_AndType_OrderByLevel(iri, IM.IS_A.getIri())) {
				Concept t = tct.getAncestor();
				if (!iri.equals(t.getIri())) {
				    if (t.getJson() == null || t.getJson().isEmpty()) {
				        LOG.error("Concept has no definition {}", t.getIri());
                    } else {
                        TTConcept concept = om.readValue(t.getJson(), TTConcept.class);
                        result.add(concept);
                    }
				}
			}
			return result;
		} catch (JsonProcessingException | IllegalArgumentException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public ExportValueSet getValueSetMembers(String iri, boolean expand) {
		if(iri == null || iri.isEmpty()){
			return  null;
		}

		Set<ValueSetMember> included =  getMember(iri, IM.HAS_MEMBER);
		Set<ValueSetMember> excluded = getMember(iri, IM.NOT_MEMBER);
		Map<String, ValueSetMember> inclusions = expandMember(included,expand);
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

	private Set<ValueSetMember> getMember(String iri, TTIriRef predicate){
		return conceptTripleRepository
				.findAllBySubject_Iri_AndPredicate_Iri(iri, predicate.getIri()).stream().map(Tpl::getObject)
				.map(inc -> new ValueSetMember().setConcept(new TTIriRef(inc.getIri(), inc.getName()))
						.setCode(inc.getCode())
						.setScheme(inc.getScheme() == null ? null
								: new TTIriRef(inc.getScheme().getIri(), inc.getScheme().getName())))
				.collect(Collectors.toSet());
	}

	private Map<String, ValueSetMember> expandMember(Set<ValueSetMember> valueSetMembers, boolean expand){
		Map<String, ValueSetMember> memberHashMap = new HashMap<>();
		for (ValueSetMember member : valueSetMembers) {
			memberHashMap.put(member.getConcept().getIri(), member);
			if (expand) {
				valueSetRepository.expandMember(member.getConcept().getIri())
						.forEach(m -> memberHashMap.put(m.getConceptIri(),
								new ValueSetMember().setConcept(new TTIriRef(m.getConceptIri(), m.getConceptName()))
										.setCode(m.getCode())
										.setScheme(new TTIriRef(m.getSchemeIri(), m.getSchemeName()))));
			}
		}
		return memberHashMap;
	}

	public ValueSetMembership isValuesetMember(String valueSetIri, String memberIri) {
		if(valueSetIri==null || valueSetIri.isEmpty() || memberIri==null || memberIri.isEmpty())
			return null;
		ValueSetMembership result = new ValueSetMembership();
		Set<TTIriRef> included = getMemberIriRefs(valueSetIri, IM.HAS_MEMBER);
		Set<TTIriRef> excluded = getMemberIriRefs(valueSetIri, IM.NOT_MEMBER);
		for (TTIriRef m : included) {
			Optional<org.endeavourhealth.dataaccess.entity.ValueSetMember> match = valueSetRepository
					.expandMember(m.getIri()).stream().filter(em -> em.getConceptIri().equals(memberIri)).findFirst();
			if (match.isPresent()) {
				result.setIncludedBy(m);
				break;
			}
		}
		for (TTIriRef m : excluded) {
			Optional<org.endeavourhealth.dataaccess.entity.ValueSetMember> match = valueSetRepository
					.expandMember(m.getIri()).stream().filter(em -> em.getConceptIri().equals(memberIri)).findFirst();
			if (match.isPresent()) {
				result.setExcludedBy(m);
				break;
			}
		}
		return result;
	}

	private Set<TTIriRef> getMemberIriRefs(String valueSetIri, TTIriRef predicate){
		return conceptTripleRepository
				.findAllBySubject_Iri_AndPredicate_Iri(valueSetIri, predicate.getIri()).stream().map(Tpl::getObject)
				.map(inc -> new TTIriRef(inc.getIri(), inc.getName())).collect(Collectors.toSet());
	}

	public List<TTIriRef> getCoreMappedFromLegacy(String legacyIri) {
		if(legacyIri == null || legacyIri.isEmpty())
			return Collections.emptyList();
		return conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(legacyIri, IM.HAS_MAP.getIri()).stream()
				.map(t -> new TTIriRef(t.getObject().getIri(), t.getObject().getName())).collect(Collectors.toList());
	}

	public List<TTIriRef> getLegacyMappedToCore(String coreIri) {
		if(coreIri == null || coreIri.isEmpty())
			return Collections.emptyList();
		return conceptTripleRepository.findAllByObject_Iri_AndPredicate_Iri(coreIri, IM.MATCHED_TO.getIri()).stream()
				.map(t -> new TTIriRef(t.getSubject().getIri(), t.getSubject().getName())).collect(Collectors.toList());
	}

	public List<String> getSynonyms(String iri) {
		if(iri==null||iri.isEmpty())
			return Collections.emptyList();
		return termCodeRepository.getSynonyms(iri);
	}

	private Set<TTIriRef> populateMissingNames(TTConcept concept) {
		// Get both predicate and object TTIriRefs
		List<TTIriRef> iriRefs = new ArrayList<>();
		Set<TTIriRef> predicates = new HashSet<>();

		TTConceptVisitor visitor = new TTConceptVisitor();
		visitor.IriRefVisitor = ((predicate, iriRef) -> {
			if (iriRef.getName() == null || iriRef.getName().isEmpty())
				iriRefs.add(iriRef);
		});
		visitor.PredicateVisitor = (predicates::add);
		visitor.visit(concept);

		// Get the list of iris to lookup
		Set<String> nameless = Stream.of(iriRefs, predicates).flatMap(Collection::stream).map(TTIriRef::getIri)
				.collect(Collectors.toSet());

		// Lookup and generate map
		Map<String, String> iriNameMap = new HashMap<>();
		for (Concept concept1 : conceptRepository.findAllByIriIn(nameless))
			iriNameMap.put(concept1.getIri(), concept1.getName());

		// Populate names
		iriRefs.forEach(i -> i.setName(iriNameMap.get(i.getIri())));
		predicates.forEach(i -> i.setName(iriNameMap.get(i.getIri())));

		// Return named predicate list
		return predicates;
	}

	public List<TermCode> getConceptTermCodes(String iri) {
		if(iri == null || iri.isEmpty())
			return Collections.emptyList();
		return termCodeRepository.findAllByConcept_Iri(iri)
				.stream()
				.map(termCode ->
						new TermCode()
								.setCode(termCode.getCode())
								.setTerm(termCode.getTerm())
								.setScheme(new TTIriRef(termCode.getScheme().getIri(), termCode.getScheme().getName()))
								.setConcept_term_code(termCode.getConceptTermCode())
				).collect(Collectors.toList());
	}

	public HttpEntity download(String iri, String format, boolean children, boolean parents, boolean properties, boolean members,
							   boolean roles, boolean inactive){
		if(iri==null || iri.isEmpty() || format== null || format.isEmpty())
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
			addMembersToDownload(iri, format, xls, downloadDto);
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

	private void addChildrenToDownload(String iri, String format, boolean inactive, XlsHelper xls, DownloadDto downloadDto) {
		List<ConceptReferenceNode> childrenList = getImmediateChildren(iri, null, null, false,
				inactive);
		switch (format) {
			case "excel":
				xls.addChildren(childrenList);
				break;
			case "json":
				downloadDto.setChildren(childrenList);
				break;
		}
	}

	private void addParentsToDownload(String iri, String format, boolean inactive, XlsHelper xls, DownloadDto downloadDto) {
		List<ConceptReferenceNode> parentList = getImmediateParents(iri, null, null, false,
				inactive);
		switch (format) {
			case "excel":
				xls.addParents(parentList);
				break;
			case "json":
				downloadDto.setParents(parentList);
				break;
		}
	}

	private void addPropertiesToDownload(String iri, String format, XlsHelper xls, DownloadDto downloadDto) {
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

	private void addMembersToDownload(String iri, String format, XlsHelper xls, DownloadDto downloadDto) {
		ExportValueSet exportValueSet = getValueSetMembers(iri, false);
		switch (format) {
			case "excel":
				xls.addMembers(exportValueSet);
				break;
			case "json":
				downloadDto.setMembers(exportValueSet);
				break;
		}
	}

	private void addRolesToDownload(String iri, String format, XlsHelper xls, DownloadDto downloadDto) {
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

	public List<PropertyValue> getAllProperties(String iri) {
		TTConcept concept = getConcept(iri);
		return getAllProperties(concept);
	}
	public List<PropertyValue> getAllProperties(TTConcept concept){
		List<PropertyValue> properties = new ArrayList<PropertyValue>();
		if(concept==null)
			return  Collections.emptyList();
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
		PropertyValue pv = new PropertyValue().setInheritedFrom(inheritedFrom)
				.setProperty(propertyPath);

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

	public List<PropertyValue> getRoles(String iri) {
		TTConcept concept = getConcept(iri);
		List<PropertyValue> roles = new ArrayList<PropertyValue>();
		if(concept==null)
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

	public String valueSetMembersCSV(String iri, boolean expanded) {
		ExportValueSet exportValueSet = getValueSetMembers(iri, expanded);
		StringBuilder valueSetMembers = new StringBuilder();
		valueSetMembers.append("Inc\\Exc\tValueSetIri\tValueSetName\tMemberIri\tMemberTerm\tMemberCode\tMemberSchemeIri\tMemberSchemeName\n");
		if(exportValueSet == null)
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

	private void appendValueSet(ExportValueSet exportValueSet, StringBuilder valueSetMembers, ValueSetMember setMember, String text) {
		valueSetMembers.append(text).append("\t").append(exportValueSet.getValueSet().getIri()).append("\t")
				.append(exportValueSet.getValueSet().getName()).append("\t").append(setMember.getConcept().getIri())
				.append("\t").append(setMember.getConcept().getName()).append("\t").append(setMember.getCode()).append("\t");
		if (setMember.getScheme() != null)
			valueSetMembers.append(setMember.getScheme().getIri()).append("\t").append(setMember.getScheme().getName());

		valueSetMembers.append("\n");
	}

	public GraphDto getGraphData(String iri) {
		TTConcept concept = getConcept(iri);

		if (concept == null) {
			LOG.error("Unable to find concept {}", iri);
			return null;
		}

		GraphDto graphData = new GraphDto().setIri(concept.getIri()).setName(concept.getName());

		GraphDto graphParents = new GraphDto().setName("Parents");
		GraphDto graphChildren = new GraphDto().setName("Children");
		GraphDto graphProps = new GraphDto().setName("Properties");
		GraphDto graphInheritedProps = new GraphDto().setName("Inherited");
		GraphDto graphDirectProps = new GraphDto().setName("Direct");
		GraphDto graphRoles = new GraphDto().setName("Roles");

		graphParents.getChildren().addAll(getConceptDefinedParents(concept, IM.IS_A));
		graphParents.getChildren().addAll(getConceptDefinedParents(concept, IM.IS_CONTAINED_IN));

		List<ConceptReferenceNode> children = getImmediateChildren(iri, null, null, false, false);
		List<PropertyValue> properties = getAllProperties(concept);
		List<PropertyValue> roles = getRoles(iri);

		addChildrenToGraph(graphChildren, children);
		addPropertiesToGraph(graphInheritedProps, graphDirectProps, properties);
		addRolesToGraph(graphRoles, roles);

		if (graphDirectProps.getChildren().size() > 0 || graphInheritedProps.getChildren().size() > 0) {
			graphProps.getChildren().add(graphDirectProps);
			graphProps.getChildren().add(graphInheritedProps);
		}
		graphData.getChildren().add(graphParents);
		graphData.getChildren().add(graphChildren);
		graphData.getChildren().add(graphProps);
		graphData.getChildren().add(graphRoles);

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
					result.add(
							new GraphDto().setIri(item.asIriRef().getIri())
									.setName(item.asIriRef().getName()).setPropertyType(predicate.getName())
					);
			});
		} else {
			if (!OWL.THING.equals(parent.asIriRef()))
				result.add(
						new GraphDto().setIri(parent.asIriRef().getIri())
								.setName(parent.asIriRef().getName()).setPropertyType(predicate.getName())
				);
		}
		return result;
	}

	private void addChildrenToGraph(GraphDto graphChildren, List<ConceptReferenceNode> children) {
		children.forEach(child -> {
			GraphDto graphChild = new GraphDto().setIri(child.getIri()).setName(child.getName());
			graphChildren.getChildren().add(graphChild);
		});
	}

	private void addPropertiesToGraph(GraphDto graphInheritedProps, GraphDto graphDirectProps, List<PropertyValue> properties) {
		properties.forEach(prop -> {
			if (null != prop.getInheritedFrom()) {
				GraphDto graphProp = new GraphDto().setIri(prop.getProperty().getIri())
						.setName(prop.getProperty().getName())
						.setInheritedFromName(prop.getInheritedFrom().getName())
						.setInheritedFromIri(prop.getInheritedFrom().getIri())
						.setPropertyType(prop.getProperty().getName())
						.setValueTypeIri(prop.getValueType().getIri())
						.setValueTypeName(prop.getValueType().getName())
						.setMax(prop.getMaxExclusive())
						.setMin(prop.getMinExclusive());
				graphInheritedProps.getChildren().add(graphProp);
			} else {
				GraphDto graphProp = new GraphDto().setIri(prop.getProperty().getIri())
						.setName(prop.getProperty().getName())
						.setPropertyType(prop.getProperty().getName())
						.setValueTypeIri(prop.getValueType().getIri())
						.setValueTypeName(prop.getValueType().getName())
						.setMax(prop.getMaxExclusive())
						.setMin(prop.getMinExclusive());
				graphDirectProps.getChildren().add(graphProp);
			}
		});
	}

	private void addRolesToGraph(GraphDto graphRoles, List<PropertyValue> roles) {
		for (PropertyValue role : roles) {
			GraphDto graphRole = new GraphDto().setIri(role.getProperty().getIri())
					.setName(role.getProperty().getName()).setPropertyType(role.getProperty().getName());
			if (role.getValueType() != null) {
				graphRole.setValueTypeIri(role.getValueType().getIri()).setValueTypeName(role.getValueType().getName());
			}
			graphRoles.getChildren().add(graphRole);
		}
	}
}
