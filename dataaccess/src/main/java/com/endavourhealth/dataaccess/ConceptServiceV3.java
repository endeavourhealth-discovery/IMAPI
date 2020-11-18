package com.endavourhealth.dataaccess;

import com.endavourhealth.dataaccess.entity.*;
import com.endavourhealth.dataaccess.entity.Axiom;
import com.endavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Qualifier("ConceptServiceV3")
public class ConceptServiceV3 implements IConceptService {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptServiceV3.class);

    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    ExpressionRepository expressionRepository;

    @Autowired
    AxiomRepository axiomRepository;

    @Autowired
    ClassificationRepository classificationRepository;


    @Override
    public ConceptReference getConceptReference(String iri) {
        com.endavourhealth.dataaccess.entity.Concept concept = conceptRepository.findByIri(iri);
        if (concept == null) {
            LOG.error("Unable to load concept reference [{}]\n", iri);
            return null;
        } else {
            return new ConceptReference(iri, concept.getName());
        }
    }

    @Override
    public Concept getConcept(String iri) {
        com.endavourhealth.dataaccess.entity.Concept concept = conceptRepository.findByIri(iri);

        if (concept == null) {
            LOG.error("Unable to load concept [{}]\n", iri);
            return null;
        }

        ConceptType ct = ConceptType.byValue(concept.getType());
        Concept c = getConceptInstance(ct);

        c.setIri(concept.getIri())
            .setName(concept.getName())
            .setDescription(concept.getDescription())
            .setCode(concept.getCode())
            .setStatus(ConceptStatus.byValue(concept.getStatus().getDbid()));

        if (concept.getScheme() != null)
            c.setScheme(new ConceptReference(concept.getScheme().getIri(), concept.getScheme().getName()));

        // remainder of properties
        List<Axiom> axioms = axiomRepository.findByIri(iri);

        reconstructConcept(c, axioms);

        return c;
    }

    @Override
    public Set<ConceptReference> findByNameLike(String term, String root) {
        List<com.endavourhealth.dataaccess.entity.Concept> result;
        if (root == null || root.isEmpty())
            result = conceptRepository.search(term);
        else
            result = conceptRepository.search(term, root);

        return result.stream()
            .map(r -> new ConceptReference(r.getIri(), r.getName()))
            .collect(Collectors.toSet());
    }

    @Override
    public Set<ConceptReference> getImmediateChildren(String iri) {
        Set<Classification> children = classificationRepository.findByParent_Iri(iri);
        return children
            .stream()
            .map(i -> new ConceptReference(
                i.getChild().getIri(),
                i.getChild().getName()
                )
            )
            .collect(Collectors.toSet());
    }

    @Override
    public Set<ConceptReferenceNode> getParentHierarchy(String iri) {
        return getParentHierarchy(iri, new HashMap<>());
    }

    @Override
    public ConceptReference create(Concept concept) {
        return new ConceptReference(concept.getIri(), concept.getName());
    }

    // PRIVATE METHODS ----------------------------------------------------------------------------------------------------

    private Concept getConceptInstance(ConceptType ct) {
        Concept c = null;
        switch (ct) {
            case CLASSONLY:
                c = new Concept();
                break;
            case OBJECTPROPERTY:
                c = new ObjectProperty();
                break;
            case DATAPROPERTY:
                c = new DataProperty();
                break;
            case DATATYPE:
                c = new DataType();
                break;
            case ANNOTATION:
                c = new AnnotationProperty();
                break;
            case INDIVIDUAL:
                c = new Individual();
                break;
            default:
                LOG.error("Unknown concept type");
                break;
        }
        return c;
    }

    private void reconstructConcept(Concept c, List<Axiom> axioms) {
        for (Axiom a: axioms) {
            AxiomType at = AxiomType.byValue(a.getType());
            switch (at) {
                case SUBCLASSOF:
                    getExpressionsAsClassAxiomStream(a.getExpressions())
                        .forEach(c::addSubClassOf);
                    break;
                case EQUIVALENTTO:
                    getExpressionsAsClassAxiomStream(a.getExpressions())
                        .forEach(c::addEquivalentTo);
                    break;
                case SUBOBJECTPROPERTY:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .map(ref -> new PropertyAxiom().setProperty(ref))
                        .forEach(((ObjectProperty)c)::addSubObjectPropertyOf);
                    break;
                case SUBDATAPROPERTY:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .map(ref -> new PropertyAxiom().setProperty(ref))
                        .forEach(((DataProperty)c)::addSubDataPropertyOf);
                    break;
                case SUBANNOTATIONPROPERTY:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .map(ref -> new PropertyAxiom().setProperty(ref))
                        .forEach(((AnnotationProperty)c)::addSubAnnotationPropertyOf);
                    break;
                case OBJECTPROPERTYRANGE:
                    getExpressionsAsClassAxiomStream(a.getExpressions())
                        .forEach(((ObjectProperty) c)::addObjectPropertyRange);
                    break;
                case DATAPROPERTYRANGE:
                    getExpressionsAsConceptReferenceStream(a.getExpressions())
                        .map(ref -> new DataPropertyRange().setDataType(ref))
                        .forEach(((DataProperty)c)::addDataPropertyRange);
                    break;
                case PROPERTYDOMAIN:
                    if (c instanceof ObjectProperty)
                        getExpressionsAsClassAxiomStream(a.getExpressions())
                            .forEach(((ObjectProperty) c)::addPropertyDomain);
                    else if (c instanceof DataProperty)
                        getExpressionsAsClassAxiomStream(a.getExpressions())
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

                default:
                    throw new IllegalStateException("Unknown axiom type [" + at.getName() + "]");

            }
        }
    }

    private Stream<ClassAxiom> getExpressionsAsClassAxiomStream(List<Expression> expressions) {
        return expressions.stream()
            .filter(exp -> exp.getParent() == null)
            .map(exp -> (ClassAxiom)setClassExpression(exp, expressions, new ClassAxiom()));
    }

    private ClassExpression setClassExpression(Expression exp, List<Expression> expressions, ClassExpression cex) {
        ExpressionType et = ExpressionType.byValue(exp.getType());
        switch (et) {
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
            case CLASS:
                com.endavourhealth.dataaccess.entity.Concept c = exp.getTargetConcept();
                cex.setClazz(new ConceptReference(c.getIri(), c.getName()));
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

    private Set<ConceptReferenceNode> getParentHierarchy(String iri, Map<String, ConceptReference> ancestors) {
        // TODO : Optimize via TCT and/or ancestor map
        Set<Classification> cpo = classificationRepository.findByChild_Iri(iri);

        Set<ConceptReferenceNode> parents = cpo
            .stream()
            .map(i -> new ConceptReferenceNode(i.getParent().getIri(), i.getParent().getName())
            )
            .collect(Collectors.toSet());

        // Recurse parents' parents
        for(ConceptReferenceNode n: parents) {
            n.setParents(getParentHierarchy(n.getIri(), ancestors));
        }

        return parents;
    }
}
