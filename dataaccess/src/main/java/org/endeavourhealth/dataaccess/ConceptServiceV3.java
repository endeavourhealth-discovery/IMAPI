package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.SearchResult;
import org.endeavourhealth.dataaccess.entity.Tct;
import org.endeavourhealth.dataaccess.entity.Tpl;
import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTConceptVisitor;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ConceptServiceV3 {
	@Value("#{'${discovery.dataaccess.core-namespace-prefixes}'.split(',')}")
	List<String> coreNamespacePrefixes;

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptTctRepository conceptTctRepository;

	@Autowired
	ConceptTripleRepository conceptTripleRepository;

	@Autowired
	ValueSetRepository valueSetRepository;

	@Autowired
	ConceptTermRepository conceptTermRepository;

	@Autowired
	SearchRepository searchRepository;

	private ObjectMapper om = new ObjectMapper();

	public TTConcept getConcept(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		if (concept == null)
			return null;

		try {
			TTConcept result = om.readValue(concept.getDefinition(), TTConcept.class);
			populateMissingNames(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public TTIriRef getConceptReference(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		if (concept == null)
			return null;

		return new TTIriRef(concept.getIri(), concept.getName());
	}

	public List<ConceptReferenceNode> getImmediateChildren(String iri, Integer pageIndex, Integer pageSize,
			boolean includeLegacy) {

        List<ConceptReferenceNode> result = new ArrayList<>();

        Pageable pageable = null;
        if (pageIndex != null && pageSize!=null){
            pageable = PageRequest.of(pageIndex - 1, pageSize);
        }

        List<ConceptReferenceNode> immediateChildren = getActiveChildren(iri, pageable);
        for (ConceptReferenceNode child : immediateChildren) {
                List<Tpl> grandChildren = conceptTripleRepository
                        .findImmediateChildrenByIri(child.getIri(), pageable);
                child.setHasChildren(grandChildren.size() != 0);
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

	public List<ConceptReferenceNode> getDescendants(String iri) {
		return getChildren(iri);
	}

	private List<ConceptReferenceNode> getChildren(String iri){
		List<ConceptReferenceNode> descendant =conceptTripleRepository
				.findImmediateChildrenByIri(iri).stream()
				.map(t -> new ConceptReferenceNode(t.getSubject().getIri(), t.getSubject().getName())).collect(Collectors.toList());
		for (ConceptReferenceNode child : descendant) {
			if(child.isHasChildren()) {
				descendant.addAll(getChildren(child.getIri()));
			}
		}
		return descendant;
	}

	public List<ConceptReferenceNode> getImmediateParents(String iri, Integer pageIndex, Integer pageSize,
			boolean includeLegacy) {

        Pageable pageable = null;
        if (pageIndex != null && pageSize!=null){
            pageable = PageRequest.of(pageIndex - 1, pageSize);
        }

		return getActiveParents(iri, pageable);
	}

	private List<ConceptReferenceNode> getActiveParents(String iri, Pageable pageable) {
		return conceptTripleRepository
				.findImmediateParentsByIri(iri, pageable).stream()
				.filter(t -> IM.ACTIVE.getIri().equals(t.getObject().getStatus().getIri()))
				.map(t -> new ConceptReferenceNode(t.getObject().getIri(), t.getObject().getName())
						.setType(getConcept(t.getObject().getIri()).getType())).collect(Collectors.toList());
	}

	public List<TTIriRef> isWhichType(String iri, List<String> candidates) {
		return conceptTctRepository
				.findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(iri, IM.IS_A.getIri(), candidates).stream()
				.map(tct -> new TTIriRef(tct.getAncestor().getIri(), tct.getAncestor().getName()))
				.sorted(Comparator.comparing(TTIriRef::getName)).collect(Collectors.toList());
	}

	public List<ConceptSummary> usages(String iri) {
		Set<String> children = conceptTctRepository.findByAncestor_Iri_AndType_Iri(iri, IM.IS_A.getIri()).stream()
				.map(t -> t.getDescendant().getIri()).collect(Collectors.toSet());

		return conceptTripleRepository.findAllByObject_Iri(iri).stream().map(Tpl::getSubject)
				.filter(subject -> !children.contains(subject.getIri())).distinct()
				.map(c -> new ConceptSummary().setIri(c.getIri()).setName(c.getName()).setCode(c.getCode()).setScheme(
						c.getScheme() == null ? null : new TTIriRef(c.getScheme().getIri(), c.getScheme().getName())))
				// .sorted(Comparator.comparing(ConceptSummary::getName))
				.collect(Collectors.toList());
	}

	public List<ConceptSummary> advancedSearch(SearchRequest request) {
		List<SearchResult> matchingConcept;

		String full = request.getTermFilter();
		String terms = Arrays.stream(full.split(" ")).filter(t -> t.trim().length() >= 3).map(w -> "+" + w + "*")
				.collect(Collectors.joining(" "));

		if (request.getSchemeFilter() == null || request.getSchemeFilter().isEmpty())
			matchingConcept = searchLegacy(terms, full, request.getTypeFilter(), request.getStatusFilter(),
					request.getSize());
		else {
			matchingConcept = searchLegacyScheme(terms, full, request.getSchemeFilter(),
					request.getTypeFilter(), request.getStatusFilter(), request.getSize());
		}

		List<ConceptSummary> result = matchingConcept.stream()
				.map(c -> convertConceptToConceptSummary(c, full))
				.sorted(Comparator.comparingInt(ConceptSummary::getWeighting))
				.filter(distinctByKey(ConceptSummary::getIri))
				.collect(Collectors.toList());

		List<String> types = request.getMarkIfDescendentOf();
		if (types != null && !types.isEmpty()) {
			result.forEach(s -> s.setIsDescendentOf(this.isWhichType(s.getIri(), types)));
		}

		return result;
	}

	public static <T> Predicate<T> distinctByKey(
			Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}



	private List<SearchResult> searchLegacy(String terms, String full, List<String> conceptType, List<String> status, Integer limit) {
		List<SearchResult> concepts = new ArrayList<>();

		concepts.addAll(searchRepository.findLegacyByCode(full, conceptType, status, limit));
		concepts.addAll(searchRepository.findLegacyByIri(full, conceptType, status, limit));
		concepts.addAll(searchRepository.findLegacyByName(terms, conceptType, status, limit));
		concepts.addAll(searchRepository.findLegacyByTerm(terms, conceptType, status, limit));

		return concepts;
	}

	private List<SearchResult> searchLegacyScheme(String terms, String full, List<String> schemes, List<String> conceptType, List<String> status, Integer limit){
		List<SearchResult> concepts = new ArrayList<>();

		concepts.addAll(searchRepository.findLegacySchemesByCode(full, schemes, conceptType, status, limit));
		concepts.addAll(searchRepository.findLegacySchemesByIri(full, schemes, conceptType, status, limit));
		concepts.addAll(searchRepository.findLegacySchemesByName(terms, schemes, conceptType, status, limit));
		concepts.addAll(searchRepository.findLegacySchemesByTerm(terms, schemes, conceptType, status, limit));

		return concepts;
	}

	private ConceptSummary convertConceptToConceptSummary(SearchResult r,String full) {
		return new ConceptSummary()
				.setName(r.getName())
				.setIri(r.getIri())
				.setConceptType(getConcept(r.getIri()).getType())
				.setWeighting(Levenshtein.calculate(full, r.getId().getMatch()))
				.setCode(r.getCode())
				.setDescription(r.getDescription())
				.setStatus(new TTIriRef(r.getStatus().getIri(), r.getStatus().getName()))
				.setScheme(r.getScheme() == null ? null : new TTIriRef(r.getScheme().getIri(), r.getScheme().getName()));
	}

	public List<TTConcept> getAncestorDefinitions(String iri) {
		try {
			List<TTConcept> result = new ArrayList<>();
			for (Tct tct : conceptTctRepository.findByDescendant_Iri_AndType_OrderByLevel(iri, IM.IS_A.getIri())) {
				Concept t = tct.getAncestor();
				if (!iri.equals(t.getIri())) {
					TTConcept concept = om.readValue(t.getDefinition(), TTConcept.class);
					result.add(concept);
				}
			}
			return result;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ExportValueSet getValueSetMembers(String iri, boolean expand) {
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
		return conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(legacyIri, IM.HAS_MAP.getIri()).stream()
				.map(t -> new TTIriRef(t.getObject().getIri(), t.getObject().getName())).collect(Collectors.toList());
	}

	public List<TTIriRef> getLegacyMappedToCore(String coreIri) {
		return conceptTripleRepository.findAllByObject_Iri_AndPredicate_Iri(coreIri, IM.MATCHED_TO.getIri()).stream()
				.map(t -> new TTIriRef(t.getSubject().getIri(), t.getSubject().getName())).collect(Collectors.toList());
	}

	public List<String> getSynonyms(String iri) {
		return conceptTermRepository.getSynonyms(iri);
	}

	private Pageable getPage(Integer pageIndex, Integer pageSize) {
		Pageable page = null;

		// defaults
		if (pageIndex != null && pageIndex <= 0)
			pageIndex = 1;
		if (pageSize != null && pageSize <= 0)
			pageSize = 20;

		if (pageIndex != null && pageSize != null) {
			page = PageRequest.of(pageIndex - 1, pageSize);
		}

		return page;
	}

	private String getPath(String iri) {
		return iri.substring(0, Math.max(iri.lastIndexOf("/"), iri.lastIndexOf('#')) + 1);
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

	private void stripIriRefNames(TTConcept concept) {
		TTConceptVisitor visitor = new TTConceptVisitor();
		visitor.IriRefVisitor = ((predicate, iriRef) -> iriRef.setName(null));
		visitor.visit(concept);
	}


}
