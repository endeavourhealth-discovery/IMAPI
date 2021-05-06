package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.entity.*;
import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTConceptVisitor;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
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
}
