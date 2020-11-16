package com.endavourhealth.dataaccess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.endeavourhealth.imapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.ConceptAxiom;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.repository.ConceptAxiomRepository;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Qualifier("ConceptServiceV2")
public class ConceptServiceV2 implements IConceptService {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptServiceV2.class);

    private static final String IS_A = "sn:116680003";

    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    ConceptAxiomRepository conceptAxiomRepository;

    @Autowired
    ConceptPropertyObjectRepository conceptPropertyObjectRepository;

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
        Concept c = null;

        com.endavourhealth.dataaccess.entity.Concept concept = conceptRepository.findByIri(iri);
        ConceptType ct = ConceptType.byValue(concept.getType());
        c = getConceptInstance(ct);

        c.setIri(concept.getIri())
            .setName(concept.getName())
        .setDescription(concept.getDescription())
        .setCode(concept.getCode())
            .setScheme(new ConceptReference(concept.getScheme().getIri(), concept.getScheme().getName()))
        .setStatus(ConceptStatus.byValue(concept.getStatus().getDbid()));
        // remainder of properties

        // Build definition
        List<ConceptAxiom> conceptAxioms = conceptAxiomRepository.findByConceptDbid(concept.getDbid());
        ObjectMapper om = new ObjectMapper();
        for (ConceptAxiom ax : conceptAxioms) {
            addAxiomToConcept(c, ax, om);
        }

        return c;
    }

    @Override
    public Set<ConceptReference> findByNameLike(String term, String root) {
        Set<ConceptReference> result = new HashSet<>();

        for(com.endavourhealth.dataaccess.entity.Concept entity : conceptRepository.search(term, root)) {
            result.add(new ConceptReference(entity.getIri()).setName(entity.getName()));
        }

        return result;
    }

    @Override
    public Set<ConceptReference> getImmediateChildren(String iri) {
        Set<ConceptPropertyObject> cpo = conceptPropertyObjectRepository.findByProperty_IriAndObject_Iri(IS_A, iri);
        return cpo
            .stream()
            .map(i -> new ConceptReference(
                i.getConcept().getIri(),
                i.getConcept().getName()
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
        Set<ConceptPropertyObject> cpo = conceptPropertyObjectRepository.findByConcept_IriAndProperty_Iri(iri, IS_A);

        Set<ConceptReferenceNode> parents = cpo
            .stream()
            .map(i -> (ConceptReferenceNode) new ConceptReferenceNode(i.getObject().getIri(), i.getObject().getName())
            )
            .collect(Collectors.toSet());

        // Recurse parents' parents
        for(ConceptReferenceNode n: parents) {
            n.setParents(getParentHierarchy(n.getIri(), ancestors));
        }

        return parents;
    }
}
