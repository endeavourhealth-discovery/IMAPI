package org.endeavourhealth.dataaccess;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.endeavourhealth.dataaccess.entity.Classification;
import org.endeavourhealth.dataaccess.entity.ConceptTct;
import org.endeavourhealth.dataaccess.entity.Expression;
import org.endeavourhealth.dataaccess.entity.PropertyValue;
import org.endeavourhealth.dataaccess.entity.Axiom;
import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Qualifier("ConceptServiceV3")
public class ConceptServiceV3 implements IConceptService {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptServiceV3.class);
    private static final Integer DEFAULT_LIMIT = 20;
    private static final String DEFAULT_CONCEPT_REFERENCE_NAME = "";
    
    @Value("#{'${discovery.dataaccess.core-namespace-prefixes}'.split(',')}")
    List<String> coreNamespacePrefixes;
    
    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    ExpressionRepository expressionRepository;

    @Autowired
    AxiomRepository axiomRepository;

    @Autowired
    ClassificationRepository classificationRepository;

    @Autowired
    ConceptTctRepository conceptTctRepository;
    
    @Autowired
    ClassificationNativeQueries classificationNativeQueries;

    @Autowired
    ValueSetRepository valueSetRepository;

    @Autowired
    MemberRepository memberRepository;


    @Override
    public ConceptReference getConceptReference(String iri) {
        org.endeavourhealth.dataaccess.entity.Concept concept = conceptRepository.findByIri(iri);
        if (concept == null) {
            LOG.error("Unable to load concept reference [{}]\n", iri);
            return null;
        } else {
            return new ConceptReference(iri, concept.getName());
        }
    }

    @Override
    public Concept getConcept(String iri) {
        org.endeavourhealth.dataaccess.entity.Concept concept = conceptRepository.findByIri(iri);

        if (concept == null) {
            LOG.error("Unable to load concept [{}]\n", iri);
            return null;
        }

        return hydrateConcept(concept);
    }

    @Override
    public List<ConceptReference> findByNameLike(String full, String root, boolean includeLegacy, Integer limit) {
        if (limit == null)
            limit = DEFAULT_LIMIT;

        List<Byte> status = Arrays.asList(ConceptStatus.DRAFT.getValue(),ConceptStatus.ACTIVE.getValue());

        String terms = Arrays.stream(full.split(" "))
            .filter(t -> t.trim().length() >= 3)
            .map(w -> "+" + w)
            .collect(Collectors.joining(" "));

        List<org.endeavourhealth.dataaccess.entity.Concept> result;
        if (root == null || root.isEmpty())
            result = (includeLegacy)
                ? conceptRepository.searchLegacy(terms, full, status, limit)
                : conceptRepository.search(terms, full, status, limit);
        else
            result = (includeLegacy)
                ? conceptRepository.searchType(terms, full, root, status, limit)
                : conceptRepository.searchLegacyType(terms, full, root, status, limit);

        return result.stream()
            .map(r -> new ConceptReference(r.getIri(), r.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public Boolean getHasChildren(String iri, boolean includeLegacy) {
        if(includeLegacy) {
            return classificationRepository.findFirstByParent_Iri(iri) != null;
        }
        else {
            return !classificationNativeQueries.findClassificationByParentIriAndChildNamespace(iri, coreNamespacePrefixes).isEmpty();
        }
    }

    @Override
    public List<String> getHaveChildren(List<String> iris, boolean includeLegacy) {
        List<String> result = new ArrayList<>();

        for(String iri : iris) {
            if(includeLegacy && classificationRepository.findFirstByParent_Iri(iri) != null) {
                result.add(iri);
            }
            else if (!includeLegacy && !classificationNativeQueries.findClassificationByParentIriAndChildNamespace(iri, coreNamespacePrefixes).isEmpty()) {
                result.add(iri);
            }
        }

        return result;
    }

    @Override
    public List<ConceptSummary> advancedSearch(SearchRequest request) {
        List<org.endeavourhealth.dataaccess.entity.Concept> result;

        String full = request.getTerms();
        String terms = Arrays.stream(full.split(" "))
            .filter(t -> t.trim().length() >= 3)
            .map(w -> "+" + w + "*")
            .collect(Collectors.joining(" "));

        List<Byte> status = request.getStatuses() == null || request.getStatuses().isEmpty()
            ? Arrays.stream(ConceptStatus.values()).map(ConceptStatus::getValue).collect(Collectors.toList())
            : request.getStatuses();

        if (request.getCodeSchemes() == null || request.getCodeSchemes().isEmpty())
            result = conceptRepository.searchLegacy(terms, full, status, request.getSize());
        else {
            List<String> schemeIris = request.getCodeSchemes().stream().map(ConceptReference::getIri).collect(Collectors.toList());
            result = conceptRepository.searchLegacySchemes(terms, full, schemeIris, status, request.getSize());
        }

        List<ConceptSummary> src = result.stream()
            .map(r -> new ConceptSummary()
                .setName(r.getName())
                .setIri(r.getIri())
                .setConceptType(ConceptType.byValue(r.getType()))
                .setWeighting(r.getWeighting())
                .setCode(r.getCode())
                .setScheme(
                    r.getScheme() == null
                        ? null
                        : new ConceptReference(r.getScheme().getIri(), r.getScheme().getName())

                ))
            .collect(Collectors.toList());

        List<String> types = request.getTypes();
        if (types != null && !types.isEmpty()) {
            src.forEach(s -> s.setTypes(this.isWhichType(s.getIri(), types)));
        }

        return src;
    }
    
    @Override
    public List<Concept> getAncestorDefinitions(String iri) {
        Set<ConceptTct> result = conceptTctRepository.findBySource_IriOrderByLevel(iri);

        return result.stream()
            .filter(t -> !iri.equals(t.getTarget().getIri()))
            .map(t -> hydrateConcept(t.getTarget()))
            .collect(Collectors.toList());
    }

    public List<ConceptReferenceNode> getImmediateChildren(String iri, Integer pageIndex, Integer pageSize, boolean includeLegacy) {
    	List<ConceptReferenceNode> immediateChildren;
               
        Pageable page = getPage(pageIndex, pageSize);
        
        // get the data
        List<Object[]> rows;
        if(page != null) {
        	// TODO - adapt for paging
        	rows = getClassificationsPage(page, iri, getNamespacePrefixes(includeLegacy));
        }
        else {
        	rows = getClassifications(iri, getNamespacePrefixes(includeLegacy));
        }
        
        // transform the data
        if(rows != null) {
        	immediateChildren = rows.stream()
                .filter(row -> classificationNativeQueries.getClassificaton(row).getChild().getStatus().getDbid() != ConceptStatus.INACTIVE.getValue())
                .map(row -> toConceptReferenceNode(classificationNativeQueries.getClassificaton(row).getChild(), classificationNativeQueries.getChildHasChildren(row)))
	            .sorted(Comparator.comparing(ConceptReferenceNode::getName))
	            .collect(Collectors.toList());        	
        }
        else {
        	immediateChildren = new ArrayList<>();
        }
        
        return immediateChildren;
    }

    public List<ConceptReferenceNode> getImmediateParents(String iri, Integer pageIndex, Integer pageSize, boolean includeLegacy) {
        Set<Classification> classifications = classificationRepository.findByChild_Iri(iri);

        return classifications.stream()
            .filter(c -> c.getParent().getStatus().getDbid() != ConceptStatus.INACTIVE.getValue())
            .map(row -> new ConceptReferenceNode(row.getParent().getIri(), row.getParent().getName()))
            .sorted(Comparator.comparing(ConceptReferenceNode::getName))
            .collect(Collectors.toList());
    }

    public List<ConceptReferenceNode> getParentHierarchy(String iri) {
        // TODO : Optimize via TCT and/or ancestor map
        Set<Classification> classifications = classificationRepository.findByChild_Iri(iri);

        List<ConceptReferenceNode> parents = classifications.stream()
                .map(classification -> toConceptReferenceNode(classification.getParent(), true))
                .sorted(Comparator.comparing(ConceptReferenceNode::getName))
                .collect(Collectors.toList());

        // Recurse parents' parents
        for(ConceptReferenceNode parent: parents) {
            List<ConceptReferenceNode> grandParents = getParentHierarchy(parent.getIri());
            
			parent.setParents(grandParents);
        }

        return parents;   
    }

    @Override
    public List<ConceptReference> isWhichType(String iri, List<String> candidates) {
        return conceptTctRepository.findBySource_Iri_AndTarget_IriIn(iri, candidates)
            .stream().map(tct -> new ConceptReference(tct.getTarget().getIri(), tct.getTarget().getName()))
            .sorted(Comparator.comparing(ConceptReference::getName))
            .collect(Collectors.toList());
    }

    @Override
    public ConceptReference create(Concept concept) {
        return new ConceptReference(concept.getIri(), concept.getName());
    }

    @Override
    public List<ConceptSummary> usages(String iri) {
        Set<String> children = classificationRepository.findByParent_Iri(iri).stream()
            .map(c -> c.getChild().getIri())
            .collect(Collectors.toSet());

        return expressionRepository.findByTargetConcept_Iri(iri).stream()
            .map(exp -> exp.getAxiom().getConcept())
            .filter(c -> !children.contains(c.getIri()))
            .distinct()
            .map(c -> new ConceptSummary()
                .setIri(c.getIri())
                .setName(c.getName())
                .setCode(c.getCode())
                .setScheme(c.getScheme() == null ? null : new ConceptReference(c.getScheme().getIri(), c.getName()))
                .setConceptType(ConceptType.byValue(c.getType()))
                .setWeighting(c.getWeighting())
            )
            .sorted(Comparator.comparing(ConceptSummary::getName))
            .collect(Collectors.toList());
    }

    @Override
    public ExportValueSet getValueSetMembers(String iri, boolean expand) {
        Concept concept = getConcept(iri);

        if (!(concept instanceof ValueSet))
            return null;

        List<ConceptReference> included = new ArrayList<>();
        List<ConceptReference> excluded = new ArrayList<>();
        ((ValueSet)concept).getMember().forEach(m -> {
            if (m.isExclude())
                excluded.add(m.getClazz());
            else
                included.add(m.getClazz());
        });


        Map<String, ValueSetMember> inclusions = new HashMap<>();

        for(ConceptReference cr: included) {
            org.endeavourhealth.dataaccess.entity.Concept c = conceptRepository.findByIri(cr.getIri());

            ValueSetMember vsm = new ValueSetMember()
                .setConcept(new ConceptReference(c.getIri(), c.getName()))
                .setCode(c.getCode());
            if (c.getScheme() != null)
                vsm.setScheme(new ConceptReference(c.getScheme().getIri(), c.getScheme().getName()));

            inclusions.put(c.getIri(), vsm);

            if (expand) {
                valueSetRepository.expandMember(cr.getIri())
                    .forEach(m -> inclusions.put(m.getConceptIri(), new ValueSetMember()
                        .setConcept(new ConceptReference(m.getConceptIri(), m.getConceptName()))
                        .setCode(m.getCode())
                        .setScheme(new ConceptReference(m.getSchemeIri(), m.getSchemeName()))
                    ));
            }
        }

        Map<String, ValueSetMember> exclusions = new HashMap<>();
        for(ConceptReference cr: excluded) {
            org.endeavourhealth.dataaccess.entity.Concept c = conceptRepository.findByIri(cr.getIri());

            ValueSetMember vsm = new ValueSetMember()
                .setConcept(new ConceptReference(c.getIri(), c.getName()))
                .setCode(c.getCode());
            if (c.getScheme() != null)
                vsm.setScheme(new ConceptReference(c.getScheme().getIri(), c.getScheme().getName()));

            exclusions.put(c.getIri(), vsm);

            if (expand) {
                valueSetRepository.expandMember(cr.getIri())
                    .forEach(m -> exclusions.put(m.getConceptIri(), new ValueSetMember()
                        .setConcept(new ConceptReference(m.getConceptIri(), m.getConceptName()))
                        .setCode(m.getCode())
                        .setScheme(new ConceptReference(m.getSchemeIri(), m.getSchemeName()))
                    ));
            }
        }

        if (expand) {
            // Remove exclusions by key
            exclusions.forEach((k,v) -> inclusions.remove(k));
        }

        ExportValueSet result = new ExportValueSet()
            .setValueSet(new ConceptReference(concept.getIri(), concept.getName()))
            .addAllIncluded(inclusions.values());

        if (!expand)
            result.addAllExcluded(exclusions.values());

        return result;
    }

    @Override
    public ValueSetMembership isValuesetMember(String valueSetIri, String memberIri) {
        ValueSetMembership result = new ValueSetMembership();

        Concept valueSet = getConcept(valueSetIri);

        if (valueSet instanceof ValueSet) {
            List<ConceptReference> included = new ArrayList<>();
            List<ConceptReference> excluded = new ArrayList<>();
            ((ValueSet) valueSet).getMember().forEach(m -> {
                if (m.isExclude())
                    excluded.add(m.getClazz());
                else
                    included.add(m.getClazz());
            });

            for (ConceptReference m : included) {
                Optional<org.endeavourhealth.dataaccess.entity.ValueSetMember> match = valueSetRepository.expandMember(m.getIri()).stream().filter(em -> em.getConceptIri().equals(memberIri)).findFirst();
                if (match.isPresent()) {
                    result.setIncludedBy(m);
                    break;
                }
            }

            for (ConceptReference m : excluded) {
                Optional<org.endeavourhealth.dataaccess.entity.ValueSetMember> match = valueSetRepository.expandMember(m.getIri()).stream().filter(em -> em.getConceptIri().equals(memberIri)).findFirst();
                if (match.isPresent()) {
                    result.setExcludedBy(m);
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public List<ConceptReference> getCoreMappedFromLegacy(String legacyIri) {
        return memberRepository.getCoreMappedFromLegacy(legacyIri)
            .stream()
            .map(c -> new ConceptReference(c.getIri(), c.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public List<ConceptReference> getLegacyMappedToCore(String coreIri) {
        return memberRepository.getLegacyMappedToCore(coreIri)
            .stream()
            .map(c -> new ConceptReference(c.getIri(), c.getName()))
            .collect(Collectors.toList());
    }


    // PRIVATE METHODS ----------------------------------------------------------------------------------------------------

    private Concept hydrateConcept(org.endeavourhealth.dataaccess.entity.Concept concept) {
        ConceptType ct = ConceptType.byValue(concept.getType());
        Concept c = getConceptInstance(ct);

        c.setIri(concept.getIri())
            .setName(concept.getName())
            .setDescription(concept.getDescription())
            .setCode(concept.getCode())
            .setStatus(ConceptStatus.byValue(concept.getStatus().getDbid()));

        if (concept.getScheme() != null)
            c.setScheme(new ConceptReference(concept.getScheme().getIri(), concept.getScheme().getName()));

        // Check for concept expression
        if (concept.getExpression() != null)
            c.setExpression(setClassExpression(concept.getExpression(), null, new ClassExpression()));

        // remainder of properties
        List<Axiom> axioms = axiomRepository.findByIri(concept.getIri());

        reconstructConcept(c, axioms);

        return c;
    }

    private Concept getConceptInstance(ConceptType ct) {
        switch (ct) {
            case CLASSONLY:
                return new Concept();
            case OBJECTPROPERTY:
                return new ObjectProperty();
            case DATAPROPERTY:
                return new DataProperty();
            case DATATYPE:
                return new DataType();
            case ANNOTATION:
                return new AnnotationProperty();
            case INDIVIDUAL:
                return new Individual();
            case RECORD:
                return new Record();
            case VALUESET:
                return new ValueSet();
            case LEGACY:
                return new LegacyConcept();
            default:
                throw new IllegalStateException("Unhandled concept type [" + ct.getName() + "]");
        }
    }

    private void reconstructConcept(Concept c, List<Axiom> axioms) {
        for (Axiom a : axioms) {
            AxiomType at = AxiomType.byValue(a.getType());
            switch (at) {
                case SUBCLASSOF:
                    getExpressionsAsStream(a.getExpressions())
                        .forEach(c::addSubClassOf);
                    break;
                case EQUIVALENTTO:
                    getExpressionsAsStream(a.getExpressions())
                        .forEach(c::addEquivalentTo);
                    break;
                case SUBOBJECTPROPERTY:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .map(ref -> new PropertyAxiom().setProperty(ref))
                        .forEach(((ObjectProperty) c)::addSubObjectPropertyOf);
                    break;
                case SUBDATAPROPERTY:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .map(ref -> new PropertyAxiom().setProperty(ref))
                        .forEach(((DataProperty) c)::addSubDataPropertyOf);
                    break;
                case SUBANNOTATIONPROPERTY:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .map(ref -> new PropertyAxiom().setProperty(ref))
                        .forEach(((AnnotationProperty) c)::addSubAnnotationPropertyOf);
                    break;
                case OBJECTPROPERTYRANGE:
                    getExpressionsAsStream(a.getExpressions())
                        .forEach(((ObjectProperty) c)::addObjectPropertyRange);
                    break;
                case DATAPROPERTYRANGE:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .map(ref -> new DataPropertyRange().setDataType(ref))
                        .forEach(((DataProperty) c)::addDataPropertyRange);
                    break;
                case PROPERTYDOMAIN:
                    if (c instanceof ObjectProperty)
                        getExpressionsAsStream(a.getExpressions())
                            .forEach(((ObjectProperty) c)::addPropertyDomain);
                    else if (c instanceof DataProperty)
                        getExpressionsAsStream(a.getExpressions())
                            .forEach(((DataProperty) c)::addPropertyDomain);
                    else
                        throw new IllegalStateException("Property domain on non-property");
                    break;
                case DISJOINTWITH:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .forEach(c::addDisjointWith);
                    break;
                case SUBPROPERTYCHAIN:
                    SubPropertyChain chain = new SubPropertyChain();
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .forEach(chain::addProperty);

                    ((ObjectProperty) c).addSubPropertyChain(chain);
                    break;
                case INVERSEPROPERTYOF:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .map(ref -> new PropertyAxiom().setProperty(ref))
                        .findFirst()
                        .ifPresent(((ObjectProperty) c)::setInversePropertyOf);
                    break;
                case ISFUNCTIONAL:
                    if (c instanceof ObjectProperty)
                        ((ObjectProperty) c).setIsFunctional(new org.endeavourhealth.imapi.model.Axiom());
                    else if (c instanceof DataProperty)
                        ((DataProperty) c).setIsFunctional(new org.endeavourhealth.imapi.model.Axiom());
                    else
                        throw new IllegalStateException("IsFunctional on non-property");
                    break;
                case ISTRANSITIVE:
                    if (c instanceof ObjectProperty)
                        ((ObjectProperty) c).setIsTransitive(new org.endeavourhealth.imapi.model.Axiom());
                    else
                        throw new IllegalStateException("IsTransitive on non-object-property");
                    break;
                case ISSYMMETRIC:
                    if (c instanceof ObjectProperty)
                        ((ObjectProperty) c).setIsSymmetric(new org.endeavourhealth.imapi.model.Axiom());
                    else
                        throw new IllegalStateException("IsSymmetric on non-object-property");
                    break;
                case ISREFLEXIVE:
                    if (c instanceof ObjectProperty)
                        ((ObjectProperty) c).setIsReflexive(new org.endeavourhealth.imapi.model.Axiom());
                    else
                        throw new IllegalStateException("IsReflexive on non-object-property");
                    break;
                case PROPERTY:
                    if (c instanceof Record)
                        getExpressionsAsStream(a.getExpressions())
                            .findFirst()
                            .ifPresent(cex -> c.setProperty(cex.getPropertyConstraint()));
                    else
                        throw new IllegalStateException("Property on non-record");
                    break;
                case MEMBER:
                    if (c instanceof ValueSet)
                        getExpressionsAsStream(a.getExpressions())
                        .forEach(((ValueSet) c)::addMember);
                    else
                        throw new IllegalStateException("Member on non-valueset");
                    break;
                case MAPPED_FROM:
                    getExpressionsAsStream(a.getExpressions())
                        .map(ClassExpression::getClazz)
                        .forEach(((LegacyConcept)c)::addMappedFrom);
                    break;
                default:
                    throw new IllegalStateException("Unknown axiom type [" + at.getName() + "]");

            }
        }
    }

    private Stream<ClassExpression> getExpressionsAsStream(List<Expression> expressions) {
        return expressions.stream()
            .filter(exp -> exp.getParent() == null)
            .map(exp -> setClassExpression(exp, expressions, new ClassExpression()));
    }

    private ClassExpression setClassExpression(Expression exp, List<Expression> expressions, ClassExpression cex) {
        ExpressionType et = ExpressionType.byValue(exp.getType());
        switch (et) {
            case CLASS:
                org.endeavourhealth.dataaccess.entity.Concept c = exp.getTargetConcept();
                cex.setClazz(new ConceptReference(c.getIri(), c.getName()));
                cex.setExclude(exp.getExclude());
                return cex;
            case INTERSECTION:
                cex.setIntersection(
                    expressions.stream()
                        .filter(ex -> exp.getDbid().equals(ex.getParent()))
                        .map(ex -> setClassExpression(ex, expressions, new ClassExpression()))
                        .collect(Collectors.toList())
                );
                return cex;
            case UNION:
                cex.setUnion(
                    expressions.stream()
                        .filter(ex -> exp.getDbid().equals(ex.getParent()))
                        .map(ex -> setClassExpression(ex, expressions, new ClassExpression()))
                        .collect(Collectors.toList())
                );
                return cex;
            case OBJECTONEOF:
                cex.setObjectOneOf(
                    expressions.stream()
                        .filter(ex -> exp.getDbid().equals(ex.getParent()))
                        .map(ex -> new ConceptReference().setIri(ex.getTargetConcept().getIri()).setName(ex.getTargetConcept().getName()))
                        .collect(Collectors.toList())
                );
                return cex;
            case OBJECTPROPERTYVALUE:
                PropertyValue pv1 = exp.getPropertyValue().get(0);
                ObjectPropertyValue opv = new ObjectPropertyValue();

                opv.setProperty(new ConceptReference(pv1.getProperty().getIri(), pv1.getProperty().getName()))
                    .setValueData(pv1.getValueData())
                    .setMin(pv1.getMinCardinality())
                    .setMax(pv1.getMaxCardinality());
                if (pv1.getValueType() != null)
                    opv.setValueType(new ConceptReference(pv1.getValueType().getIri(), pv1.getValueType().getName()));

                if (pv1.getValueExpression() != null)
                    opv.setExpression(setClassExpression(pv1.getValueExpression(), expressions, new ClassExpression()));

                cex.setObjectPropertyValue(opv);
                return cex;
            case DATAPROPERTYVALUE:
                PropertyValue pv2 = exp.getPropertyValue().get(0);
                DataPropertyValue dpv = new DataPropertyValue();
                dpv.setProperty(new ConceptReference(pv2.getProperty().getIri(), pv2.getProperty().getName()))
                    .setValueData(pv2.getValueData())
                    .setMin(pv2.getMinCardinality())
                    .setMax(pv2.getMaxCardinality());
                if (pv2.getValueType() != null)
                    dpv.setDataType(new ConceptReference(pv2.getValueType().getIri(), pv2.getValueType().getName()));

                cex.setDataPropertyValue(dpv);
                return cex;
            case COMPLEMENTOF:
                expressions.stream()
                    .filter(ex -> exp.getDbid().equals(ex.getParent()))
                    .map(ex -> setClassExpression(ex, expressions, new ClassExpression()))
                    .findFirst()
                    .ifPresent(cex::setComplementOf);
                return cex;
            case PROPERTY_CONSTRAINT:
                cex.setPropertyConstraint(
                    exp.getPropertyValue().stream()
                        .map(this::toPropertyConstraint)
                        .collect(Collectors.toList())
                );
                return cex;
            default:
                throw new IllegalStateException("Unknown expression type [" + et.getName() + "]");
        }
    }

    private Stream<ConceptReference> getExpressionsAsConceptReferenceStream(List<Expression> expressions) {
        return expressions.stream()
            .filter(exp -> exp.getTargetConcept() != null)
            .map(exp -> new ConceptReference()
                .setIri(exp.getTargetConcept().getIri())
                .setName(exp.getTargetConcept().getName())
            );
    }

    private PropertyConstraint toPropertyConstraint(PropertyValue pv) {
        return new PropertyConstraint()
            .setProperty(new ConceptReference(pv.getProperty().getIri(), pv.getProperty().getName()))
            .setValueClass(new ConceptReference(pv.getValueType().getIri(), pv.getValueType().getName()))
            .setMin(pv.getMinCardinality())
            .setMax(pv.getMaxCardinality());
    }

    private ConceptReferenceNode toConceptReferenceNode(org.endeavourhealth.dataaccess.entity.Concept concept, boolean hasChildren) {
    	ConceptReferenceNode conceptReferenceNode = new ConceptReferenceNode(concept.getIri());
    	
    	String name = concept.getName();
    	if(name == null) {
    		name = DEFAULT_CONCEPT_REFERENCE_NAME;
    	}
    	
    	conceptReferenceNode.setName(name);
    	conceptReferenceNode.setHasChildren(hasChildren);
    	
    	return conceptReferenceNode;
    }
    
    private List<Object[]> getClassifications(String parentIri, List<String> namespacePrefixes) {
    	List<Object[]> rawResult;
    	
    	if(namespacePrefixes != null && namespacePrefixes.size() > 0) { 	
    		rawResult = classificationNativeQueries.findClassificationByParentIriAndChildNamespace(parentIri, namespacePrefixes);
    	}
    	else {
    		rawResult = classificationNativeQueries.findClassificationByParentIri(parentIri);
    	}
    	
    	return rawResult;
    }
    
    // TODO - adapt for paging
    private List<Object[]> getClassificationsPage(Pageable page, String parentIri, List<String> namespacePrefixes) {
    	List<Object[]> rawResult;
    	
    	if(namespacePrefixes != null && namespacePrefixes.size() > 0) { 	
    		rawResult = classificationNativeQueries.findClassificationByParentIriAndChildNamespace(parentIri, namespacePrefixes);
    	}
    	else {
    		rawResult = classificationNativeQueries.findClassificationByParentIri(parentIri);
    	}
    	
    	return rawResult;
    }
    
	private List<String> getNamespacePrefixes(boolean includeLegacy) {
		// null means everything

        return (includeLegacy) ? null : coreNamespacePrefixes;
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
}
