package com.endavourhealth.dataaccess;

import com.endavourhealth.dataaccess.entity.*;
import com.endavourhealth.dataaccess.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.Axiom;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("ConceptServiceV3")
public class ConceptServiceV3 implements IConceptService {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptServiceV3.class);

    private static final String IS_A = "sn:116680003";

    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    ExpressionRepository expressionRepository;

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

        List<Expression> expressions = expressionRepository.findByIri(iri);

        reconstructConcept(c, expressions);

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

    private void reconstructConcept(Concept c, List<Expression> expressions) {
        for (Expression e: expressions) {
            AxiomType at = AxiomType.byValue(e.getAxiom().getType());
            if (e.getParent() == null) {
                switch (at) {
                    case SUBCLASSOF:
                        c.addSubClassOf((ClassAxiom) setClassExpression(e, expressions, new ClassAxiom()));
                        break;
                    case EQUIVALENTTO:
                        c.addEquivalentTo((ClassAxiom) setClassExpression(e, expressions, new ClassAxiom()));
                        break;
                    case INVERSEPROPERTYOF:
                        ConceptReference inverse = new ConceptReference(e.getTargetConcept().getIri(), e.getTargetConcept().getName());
                        ((ObjectProperty)c).setInversePropertyOf(new PropertyAxiom().setProperty(inverse));
                        break;
                    case OBJECTPROPERTYRANGE:
                        ((ObjectProperty)c).addObjectPropertyRange((ClassAxiom) setClassExpression(e, expressions, new ClassAxiom()));
                        break;
                    case DATAPROPERTYRANGE:
                        ConceptReference dataType = new ConceptReference(e.getTargetConcept().getIri(), e.getTargetConcept().getName());
                        ((DataProperty)c).addDataPropertyRange(new DataPropertyRange().setDataType(dataType));
                        break;
                    case PROPERTYDOMAIN:
                        ClassAxiom cex = (ClassAxiom)setClassExpression(e, expressions, new ClassAxiom());
                        if (c instanceof ObjectProperty)
                            ((ObjectProperty)c).addPropertyDomain(cex);
                        else if (c instanceof DataProperty)
                            ((DataProperty)c).addPropertyDomain(cex);
                        else
                            throw new IllegalStateException("Property domain on non-property");
                        break;
                    default:
                        throw new IllegalStateException("Unknown axiom type [" + at.getName() + "]");
                }
            }
        }
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

    private void addAxiomToConcept(Concept c, ConceptAxiom ax, ObjectMapper om) {
        AxiomType at = AxiomType.byValue(ax.getType());

        try {
            switch (at) {
                case SUBCLASSOF:
                    c.addSubClassOf(om.readValue(ax.getDefinition(), ClassAxiom.class));
                    break;
                case EQUIVALENTTO:
                    c.addEquivalentTo(om.readValue(ax.getDefinition(), ClassAxiom.class));
                    break;
                case SUBOBJECTPROPERTY:
                    ((ObjectProperty) c).addSubObjectPropertyOf(om.readValue(ax.getDefinition(), PropertyAxiom.class));
                    break;
                case SUBDATAPROPERTY:
                    ((DataProperty) c).addSubDataPropertyOf(om.readValue(ax.getDefinition(), PropertyAxiom.class));
                    break;
                case OBJECTPROPERTYRANGE:
                    ((ObjectProperty) c).addObjectPropertyRange(om.readValue(ax.getDefinition(), ClassAxiom.class));
                    break;
                case PROPERTYDOMAIN:
                    if (c instanceof ObjectProperty)
                        ((ObjectProperty) c).addPropertyDomain(om.readValue(ax.getDefinition(), ClassAxiom.class));
                    else
                        ((DataProperty) c).addPropertyDomain(om.readValue(ax.getDefinition(), ClassAxiom.class));
                    break;
                case SUBANNOTATIONPROPERTY:
                    ((AnnotationProperty) c).addSubAnnotationPropertyOf(om.readValue(ax.getDefinition(), PropertyAxiom.class));
                    break;
                case DISJOINTWITH:
                    c.addDisjointWith(om.readValue(ax.getDefinition(), ConceptReference.class));
                    break;
                case SUBPROPERTYCHAIN:
                    ((ObjectProperty) c).addSubPropertyChain(om.readValue(ax.getDefinition(), SubPropertyChain.class));
                    break;
                case INVERSEPROPERTYOF:
                    ((ObjectProperty) c).setInversePropertyOf(om.readValue(ax.getDefinition(), PropertyAxiom.class));
                    break;
                case ISFUNCTIONAL:
                    if (c instanceof ObjectProperty)
                        ((ObjectProperty) c).setIsFunctional(om.readValue(ax.getDefinition(), Axiom.class));
                    else
                        ((DataProperty) c).setIsFunctional(om.readValue(ax.getDefinition(), Axiom.class));
                    break;
                case ISTRANSITIVE:
                    ((ObjectProperty) c).setIsTransitive(om.readValue(ax.getDefinition(), Axiom.class));
                    break;
                case ISSYMMETRIC:
                    ((ObjectProperty) c).setIsSymmetric(om.readValue(ax.getDefinition(), Axiom.class));
                    break;
                case DATAPROPERTYASSERTION:
                    ((Individual) c).addDataPropertyAssertion(om.readValue(ax.getDefinition(), DataPropertyValue.class));
                    break;
                case DATAPROPERTYRANGE:
                    ((DataProperty) c).addDataPropertyRange(om.readValue(ax.getDefinition(), DataPropertyRange.class));
                    break;
                case ISREFLEXIVE:
                    ((ObjectProperty) c).setIsReflexive(om.readValue(ax.getDefinition(), Axiom.class));
                    break;
            }
        } catch (Exception e) {
            LOG.error("Unable to deserialise axiom definition");
        }
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
