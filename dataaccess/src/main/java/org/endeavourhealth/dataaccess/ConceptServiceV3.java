package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.Tct;
import org.endeavourhealth.dataaccess.entity.Tpl;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTctRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTripleRepository;
import org.endeavourhealth.dataaccess.repository.ValueSetRepository;
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
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
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

    public List<ConceptReferenceNode> getImmediateChildren(String iri, Integer pageIndex, Integer pageSize, boolean includeLegacy) {

        List<ConceptReferenceNode> immediateChildren = new ArrayList<>();

        Set<Tct> children = conceptTctRepository.findByAncestor_Iri_AndType_Iri_AndLevel(iri, IM.IS_A.getIri(), 0);

        for (Tct child: children) {
            if (IM.ACTIVE.getIri().equals(child.getDescendant().getStatus().getIri())) {
                if (includeLegacy || coreNamespacePrefixes.contains(getPath(child.getDescendant().getIri())))
                    immediateChildren.add(
                        new ConceptReferenceNode(child.getDescendant().getIri(), child.getDescendant().getName())
                    );
            }
        }
        return immediateChildren;
    }

    public List<ConceptReferenceNode> getImmediateParents(String iri, Integer page, Integer size, boolean includeLegacy) {
        List<ConceptReferenceNode> immediateParents = new ArrayList<>();

        Set<Tct> children = conceptTctRepository.findByDescendant_Iri_AndType_Iri_AndLevel(iri, IM.IS_A.getIri(), 0);

        for (Tct child: children) {
            if (IM.ACTIVE.getIri().equals(child.getDescendant().getStatus().getIri())) {
                if (includeLegacy || coreNamespacePrefixes.contains(getPath(child.getDescendant().getIri())))
                    immediateParents.add(
                        new ConceptReferenceNode(child.getAncestor().getIri(), child.getAncestor().getName())
                    );
            }
        }
        return immediateParents;
    }

    public List<ConceptReferenceNode> getParentHierarchy(String iri) {
        if (OWL.THING.equals(iri))
            return null;

        List<ConceptReferenceNode> parents = getImmediateParents(iri, null, null, false);

        // Recurse parents' parents
        for(ConceptReferenceNode parent: parents) {
            List<ConceptReferenceNode> grandParents = getParentHierarchy(parent.getIri());

            parent.setParents(grandParents);
        }

        return parents;
    }

    public List<TTIriRef> isWhichType(String iri, List<String> candidates) {
        return conceptTctRepository.findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(iri, IM.IS_A.getIri(), candidates)
            .stream().map(tct -> new TTIriRef(tct.getAncestor().getIri(), tct.getAncestor().getName()))
            .sorted(Comparator.comparing(TTIriRef::getName))
            .collect(Collectors.toList());
    }

    public List<ConceptSummary> usages(String iri) {
        Set<String> children = conceptTctRepository.findByAncestor_Iri_AndType_Iri(iri, IM.IS_A.getIri()).stream()
            .map(t -> t.getDescendant().getIri())
            .collect(Collectors.toSet());

        return conceptTripleRepository.findByObject_Iri(iri).stream()
            .map(Tpl::getSubject)
            .filter(subject -> !children.contains(subject.getIri()))
            .distinct()
            .map(c -> new ConceptSummary()
                .setIri(c.getIri())
                .setName(c.getName())
                .setCode(c.getCode())
                .setScheme(c.getScheme() == null ? null : new TTIriRef(c.getScheme().getIri(), c.getScheme().getName()))
            )
            // .sorted(Comparator.comparing(ConceptSummary::getName))
            .collect(Collectors.toList());
    }

    public List<ConceptSummary> advancedSearch(SearchRequest request) {
        List<org.endeavourhealth.dataaccess.entity.Concept> result;

        String full = request.getTermFilter();
        String terms = Arrays.stream(full.split(" "))
            .filter(t -> t.trim().length() >= 3)
            .map(w -> "+" + w + "*")
            .collect(Collectors.joining(" "));

        if (request.getSchemeFilter() == null || request.getSchemeFilter().isEmpty())
            result = conceptRepository.searchLegacy(terms, full, request.getStatusFilter(), request.getTypeFilter(), request.getSize());
        else {
            result = conceptRepository.searchLegacySchemes(terms, full, request.getSchemeFilter(), request.getTypeFilter(), request.getStatusFilter(), request.getSize());
        }

        List<ConceptSummary> src = result.stream()
            .map(r -> new ConceptSummary()
                .setName(r.getName())
                .setIri(r.getIri())
                // .setConceptType(new TTIriRef(r.getTypes()))
                // .setWeighting(r.getWeighting())
                .setCode(r.getCode())
                .setDescription(r.getDescription())
                .setStatus(new TTIriRef(r.getStatus().getIri(), r.getStatus().getName()))
                .setScheme(
                    r.getScheme() == null
                        ? null
                        : new TTIriRef(r.getScheme().getIri(), r.getScheme().getName())

                ))
            .collect(Collectors.toList());

        List<String> types = request.getMarkIfDescendentOf();
        if (types != null && !types.isEmpty()) {
            src.forEach(s -> s.setIsDescendentOf(this.isWhichType(s.getIri(), types)));
        }

        return src;
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
        Set<ValueSetMember> included = conceptTripleRepository.findBySubject_Iri_AndPredicate_Iri(iri, IM.HAS_MEMBER.getIri()).stream()
            .map(Tpl::getObject)
            .map(inc -> new ValueSetMember()
                .setConcept(new TTIriRef(inc.getIri(), inc.getName()))
                .setCode(inc.getCode())
                .setScheme(inc.getScheme() == null ? null : new TTIriRef(inc.getScheme().getIri(), inc.getScheme().getName()))
            )
            .collect(Collectors.toSet());

        Set<ValueSetMember> excluded = conceptTripleRepository.findBySubject_Iri_AndPredicate_Iri(iri, IM.NOT_MEMBER.getIri()).stream()
            .map(Tpl::getObject)
            .map(inc -> new ValueSetMember()
                .setConcept(new TTIriRef(inc.getIri(), inc.getName()))
                .setCode(inc.getCode())
                .setScheme(inc.getScheme() == null ? null : new TTIriRef(inc.getScheme().getIri(), inc.getScheme().getName()))
            )
            .collect(Collectors.toSet());


        Map<String, ValueSetMember> inclusions = new HashMap<>();
        for(ValueSetMember inc: included) {
            inclusions.put(inc.getConcept().getIri(), inc);

            if (expand) {
                valueSetRepository.expandMember(inc.getConcept().getIri())
                    .forEach(m -> inclusions.put(m.getConceptIri(), new ValueSetMember()
                        .setConcept(new TTIriRef(m.getConceptIri(), m.getConceptName()))
                        .setCode(m.getCode())
                        .setScheme(new TTIriRef(m.getSchemeIri(), m.getSchemeName()))
                    ));
            }
        }

        Map<String, ValueSetMember> exclusions = new HashMap<>();
        for(ValueSetMember inc: excluded) {
            exclusions.put(inc.getConcept().getIri(), inc);

            if (expand) {
                valueSetRepository.expandMember(inc.getConcept().getIri())
                    .forEach(m -> exclusions.put(m.getConceptIri(), new ValueSetMember()
                        .setConcept(new TTIriRef(m.getConceptIri(), m.getConceptName()))
                        .setCode(m.getCode())
                        .setScheme(new TTIriRef(m.getSchemeIri(), m.getSchemeName()))
                    ));
            }
        }

        if (expand) {
            // Remove exclusions by key
            exclusions.forEach((k,v) -> inclusions.remove(k));
        }

        ExportValueSet result = new ExportValueSet()
            .setValueSet(getConceptReference(iri))
            .addAllIncluded(inclusions.values());

        if (!expand)
            result.addAllExcluded(exclusions.values());

        return result;
    }

    public ValueSetMembership isValuesetMember(String valueSetIri, String memberIri) {
        ValueSetMembership result = new ValueSetMembership();

        Set<TTIriRef> included = conceptTripleRepository.findBySubject_Iri_AndPredicate_Iri(valueSetIri, IM.HAS_MEMBER.getIri()).stream()
            .map(Tpl::getObject)
            .map(inc -> new TTIriRef(inc.getIri(), inc.getName()))
            .collect(Collectors.toSet());

        Set<TTIriRef> excluded = conceptTripleRepository.findBySubject_Iri_AndPredicate_Iri(valueSetIri, IM.NOT_MEMBER.getIri()).stream()
            .map(Tpl::getObject)
            .map(inc -> new TTIriRef(inc.getIri(), inc.getName()))
            .collect(Collectors.toSet());

            for (TTIriRef m : included) {
                Optional<org.endeavourhealth.dataaccess.entity.ValueSetMember> match = valueSetRepository.expandMember(m.getIri()).stream().filter(em -> em.getConceptIri().equals(memberIri)).findFirst();
                if (match.isPresent()) {
                    result.setIncludedBy(m);
                    break;
                }
            }

            for (TTIriRef m : excluded) {
                Optional<org.endeavourhealth.dataaccess.entity.ValueSetMember> match = valueSetRepository.expandMember(m.getIri()).stream().filter(em -> em.getConceptIri().equals(memberIri)).findFirst();
                if (match.isPresent()) {
                    result.setExcludedBy(m);
                    break;
                }
            }

        return result;
    }

    public List<TTIriRef> getCoreMappedFromLegacy(String legacyIri) {
        return conceptTripleRepository.findBySubject_Iri_AndPredicate_Iri(legacyIri, IM.MAPPED_FROM.getIri()).stream()
            .map(t -> new TTIriRef(t.getObject().getIri(), t.getObject().getName()))
            .collect(Collectors.toList());
    }

    public List<TTIriRef> getLegacyMappedToCore(String coreIri) {
        return conceptTripleRepository.findByObject_Iri_AndPredicate_Iri(coreIri, IM.MAPPED_FROM.getIri()).stream()
            .map(t -> new TTIriRef(t.getSubject().getIri(), t.getSubject().getName()))
            .collect(Collectors.toList());
    }

    private Pageable getPage(Integer pageIndex, Integer pageSize) {
        Pageable page = null;

        // defaults
        if (pageIndex != null && pageIndex <= 0) pageIndex = 1;
        if (pageSize != null && pageSize <= 0) pageSize = 20;

        if(pageIndex != null && pageSize != null) {
            page = PageRequest.of(pageIndex - 1, pageSize);
        }

        return page;
    }

    private String getPath(String iri) {
        return iri.substring(0, Math.max(iri.lastIndexOf("/"), iri.lastIndexOf('#'))+1);
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
        Set<String> nameless = Stream.of(iriRefs, predicates)
            .flatMap(Collection::stream)
            .map(TTIriRef::getIri)
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
