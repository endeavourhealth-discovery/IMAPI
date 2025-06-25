package org.endeavourhealth.imapi.logic.reasoner;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.transforms.TTToOWLEL;
import org.endeavourhealth.imapi.vocabulary.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.*;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * Classifies an ontology using an owl reasoner, generating ISA relationships from a Discovery ontology document.
 * Generates inferred role groups (Snomed pattern) from the existential quntifiers and propogates them to subclasses
 */
@Slf4j
public class Reasoner {
  private TTDocument inferred;
  @Getter
  private HashMap<String, TTEntity> entityMap;
  private TTManager manager;
  private Set<String> done;

  private static TTNode copyNode(TTNode node) {
    TTNode result = new TTNode();
    if (node.getPredicateMap() != null) {
      for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
        result.set(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }

  private static boolean hasProperty(TTArray subProperties, TTIriRef path) {
    if (subProperties != null) {
      for (TTValue prop : subProperties.getElements()) {
        if (prop.asNode().get(iri(SHACL.PATH)) != null) {
          if (prop.asNode().get(iri(SHACL.PATH)).asIriRef().equals(path))
            return true;
        } else if (prop.asNode().get(iri(SHACL.INVERSEPATH)) != null && prop.asNode().get(iri(SHACL.INVERSEPATH)).asIriRef().equals(path))
          return true;
      }
    }
    return false;
  }

  public TTDocument generateInferred(TTDocument document) throws OWLOntologyCreationException {
    //Creates isas
    manager = new TTManager();
    manager.setDocument(document);
    inferred = new TTDocument();
    inferred.setContext(document.getContext());
    inferred.setNamespace(document.getNamespace());
    inferred.setCrud(document.getCrud());
    classify(document);
    addDocumentRoles();
    for (TTEntity c : inferred.getEntities()) {
      if (c.isType(iri(OWL.CLASS))) {
        TTArray types = c.getType();
        List<TTValue> oldTypes = types.getElements();
        oldTypes.remove(iri(OWL.CLASS));
        oldTypes.add(iri(RDFS.CLASS));
        c.setType(new TTArray());
        for (TTValue type : oldTypes)
          c.getType().add(type);
      }
      simplifyDomains(c);
      reformChains(c);
      if (c.isType(iri(OWL.OBJECT_PROPERTY))) {
        c.addType(iri(RDF.PROPERTY));
        c.getType().remove(iri(OWL.OBJECT_PROPERTY));
      }
      if (c.isType(iri(OWL.DATATYPE_PROPERTY))) {
        c.addType(iri(RDF.PROPERTY));
        c.getType().remove(iri(OWL.DATATYPE_PROPERTY));
      }

      c.getPredicateMap().remove(iri(OWL.EQUIVALENT_CLASS));
      c.getPredicateMap().remove(iri(OWL.PROPERTY_CHAIN));
    }

    return inferred;
  }

  private void reformChains(TTEntity entity) {
    if (entity.get(iri(OWL.PROPERTY_CHAIN)) != null) {
      int i = 1;
      TTNode node = entity;
      for (TTValue property : entity.get(iri(OWL.PROPERTY_CHAIN)).iterator()) {
        if (i < entity.get(iri(OWL.PROPERTY_CHAIN)).size()) {
          node.set(property.asIriRef(), new TTNode());
          node = node.get(property.asIriRef()).asNode();
          i++;
        } else
          node.set(property.asIriRef(), iri(IM.CONCEPT));
      }
    }
  }

  private void simplifyDomains(TTEntity entity) {

    if (entity.get(iri(RDFS.DOMAIN)) == null)
      return;

    TTArray newDomains = new TTArray();
    for (TTValue oldDomain : entity.get(iri(RDFS.DOMAIN)).iterator()) {
      if (oldDomain.isIriRef()) {
        newDomains.add(oldDomain);
      } else if (oldDomain.isNode() && oldDomain.asNode().get(iri(OWL.UNION_OF)) != null) {
        for (TTValue subDomain : oldDomain.asNode().get(iri(OWL.UNION_OF)).iterator()) {
          if (!subDomain.isIriRef()) {
            log.debug("Sub domains and ranges must be iris");
          } else {
            newDomains.add(subDomain);
          }
        }
      }
    }
    entity.set(iri(RDFS.DOMAIN), newDomains);
  }

  private void addDocumentRoles() {
    if (inferred.getEntities() == null)
      return;
    for (TTEntity entity : inferred.getEntities()) {
      addEntityRoles(entity);
    }
  }

  private void addEntityRoles(TTEntity entity) {
    if (entity.get(iri(RDFS.SUBCLASS_OF)) != null) {
      for (TTValue superClass : entity.get(iri(RDFS.SUBCLASS_OF)).iterator()) {
        if (!superClass.isIriRef()) {
          addExpression(entity, superClass);
        }
      }
    }
    if (entity.get(iri(OWL.EQUIVALENT_CLASS)) != null) {
      for (TTValue equClass : entity.get(iri(OWL.EQUIVALENT_CLASS)).iterator()) {
        if (!equClass.isIriRef()) {
          addExpressionRoles(entity, equClass);
        }
      }
    }
  }

  private void addExpression(TTNode node, TTArray expression) {
    for (TTValue subExp : expression.iterator()) {
      addExpression(node, subExp);
    }
  }

  private void addExpression(TTNode node, TTValue expression) {
    if (expression.isIriRef()) {
      node.addObject(iri(RDFS.SUBCLASS_OF), expression);
    } else if (expression.isNode()) {
      if (expression.asNode().get(iri(OWL.INTERSECTION_OF)) != null) {
        addExpressionIntersection(node, expression);
      } else if (expression.asNode().get(iri(OWL.UNION_OF)) != null) {
        addExpressionUnion(node, expression);
      } else if (expression.asNode().get(iri(OWL.ON_PROPERTY)) != null) {
        addRole(node, expression.asNode());
      } else
        log.debug("Only one level of nesting supported. ");
    } else
      throw new IllegalArgumentException("Unrecognised owl expression format");
  }

  private void addExpressionIntersection(TTNode node, TTValue expression) {
    for (TTValue subExp : expression.asNode().get(iri(OWL.INTERSECTION_OF)).iterator()) {
      if (subExp.isNode()) {
        if (subExp.asNode().get(iri(OWL.ON_PROPERTY)) != null) {
          addRole(node, subExp.asNode());
        } else
          addExpression(node, subExp);
      } else if (subExp.isIriRef() && !node.get(iri(RDFS.SUBCLASS_OF)).contains(subExp) && !(node instanceof TTEntity)) {
        node.addObject(iri(RDFS.SUBCLASS_OF), subExp);
      }
    }
  }

  private void addExpressionUnion(TTNode node, TTValue expression) {
    node.set(iri(SHACL.OR), new TTArray());
    TTNode union = new TTNode();
    node.addObject(iri(SHACL.OR), union);
    addExpression(union, expression.asNode().get(iri(OWL.UNION_OF)));
  }

  private void addExpressionRoles(TTEntity entity, TTValue expression) {
    if (!expression.isNode() || expression.asNode().get(iri(OWL.INTERSECTION_OF)) == null)
      return;

    for (TTValue subExp : expression.asNode().get(iri(OWL.INTERSECTION_OF)).iterator()) {
      if (subExp.isNode() && subExp.asNode().get(iri(OWL.ON_PROPERTY)) != null) {
        TTIriRef property = subExp.asNode().get(iri(OWL.ON_PROPERTY)).asIriRef();
        TTArray value = subExp.asNode().get(iri(OWL.SOME_VALUES_FROM));
        if (entity.get(iri(IM.ROLE_GROUP)) == null) {
          TTNode roleGroup = new TTNode();
          roleGroup.set(iri(IM.GROUP_NUMBER), TTLiteral.literal(1));
          entity.addObject(iri(IM.ROLE_GROUP), roleGroup);
        }
        if (value.isIriRef()) {
          entity.get(iri(IM.ROLE_GROUP)).asNode().set(property, value);
        } else {
          TTNode subGroup = new TTNode();
          entity.get(iri(IM.ROLE_GROUP)).asNode().set(property, subGroup);
          addSubRole(subGroup, value.asNode());
        }
      }
    }
  }

  private void addSubRole(TTNode subGroup, TTNode subExp) {
    if (subExp.get(iri(OWL.INTERSECTION_OF)) != null) {
      for (TTValue and : subExp.get(iri(OWL.INTERSECTION_OF)).getElements()) {
        if (and.isNode()) {
          addSubRole(subGroup, and.asNode());
        }
      }
    } else {

      TTIriRef property = subExp.get(iri(OWL.ON_PROPERTY)).asIriRef();

      TTArray value = subExp.asNode().get(iri(OWL.SOME_VALUES_FROM));
      if (value.isIriRef()) {
        subGroup.set(property, value);
      } else {
        TTNode subSub = new TTNode();
        subGroup.set(property, subSub);
        addSubRole(subGroup, value.asNode());
      }
    }

  }

  private void addRole(TTNode node, TTNode restriction) {
    TTIriRef property = restriction.get(iri(OWL.ON_PROPERTY)).asIriRef();
    if (restriction.get(iri(OWL.SOME_VALUES_FROM)) != null) {
      if (restriction.get(iri(OWL.SOME_VALUES_FROM)).isIriRef()) {
        node.set(property, restriction.get(iri(OWL.SOME_VALUES_FROM)));
      } else if (restriction.get(iri(OWL.SOME_VALUES_FROM)).isNode()) {
        TTNode subRole = new TTNode();
        subRole(subRole, restriction.get(iri(OWL.SOME_VALUES_FROM)).asNode());
        node.set(property, subRole);
      } else {
        throw new IllegalArgumentException("unknown property construct");
      }
    } else
      throw new IllegalArgumentException("Only existential quantifications are supported");

  }

  private void subRole(TTNode subRole, TTNode node) {
    if (subRole.asNode().get(iri(OWL.INTERSECTION_OF)) != null) {
      for (TTValue subExp : subRole.get(iri(OWL.INTERSECTION_OF)).iterator()) {
        if (subExp.isIriRef())
          node.addObject(iri(RDFS.SUBCLASS_OF), subExp);
        else
          addRole(node, subExp.asNode());
      }
    }
    if (subRole.asNode().get(iri(OWL.UNION_OF)) != null) {
      node.set(iri(SHACL.OR), new TTArray());
      for (TTValue subExp : subRole.get(iri(OWL.UNION_OF)).iterator()) {
        if (subExp.isIriRef())
          node.addObject(iri(RDFS.SUBCLASS_OF), subExp);
        else {
          TTNode union = new TTNode();
          node.get(iri(SHACL.OR)).add(union);
          addRole(union, subExp.asNode());
        }
      }
    }

  }

  /**
   * Classifies an ontology using an OWL Reasoner from concepts help in a TTDocument
   *
   * @param document The TTDocument to classify
   * @return set of child -  parent "isa" nodes
   * @throws OWLOntologyCreationException for invalid owl formats leading to inability to create ontology
   * @throws DataFormatException          for invalid owl content
   */

  public TTDocument classify(TTDocument document) throws OWLOntologyCreationException {
    manager = new TTManager();
    manager.setDocument(document);
    if (document.getEntities() == null)
      return document;
    entityMap = new HashMap<>();
    //builds entity map for later look up
    document.getEntities().forEach(c -> entityMap.put(c.getIri(), c));
    TTToOWLEL transformer = new TTToOWLEL();
    TTManager dmanager = new TTManager();
    dmanager.setDocument(document);
    OWLOntologyManager owlManager = transformer.transform(document, dmanager);
    Set<OWLOntology> owlOntologySet = owlManager.getOntologies();
    Optional<OWLOntology> owlOntology = owlOntologySet.stream().findFirst();

    if (owlOntology.isEmpty())
      return document;

    OWLReasonerConfiguration config = new SimpleConfiguration();
    OWLOntology o = owlOntology.get();
    OpenlletReasoner owlReasoner = OpenlletReasonerFactory.getInstance().createReasoner(o, config);
    owlReasoner.precomputeInferences();
    if (!owlReasoner.isConsistent())
      return null;

    OWLDataFactory dataFactory = new OWLDataFactoryImpl();
    for (TTEntity c : document.getEntities()) {
      inferred.addEntity(c);
      c.getPredicateMap().remove(iri(RDFS.SUBCLASS_OF));
      c.getPredicateMap().remove(iri(RDFS.SUB_PROPERTY_OF));
      if (c.get(iri(OWL.EQUIVALENT_CLASS)) != null)
        c.set(iri(IM.DEFINITIONAL_STATUS), iri(IM.SUFFICIENTLY_DEFINED));
      if (c.isType(iri(OWL.OBJECT_PROPERTY)) || c.isType(iri(RDF.PROPERTY)) || c.isType(iri(OWL.DATATYPE_PROPERTY))) {
        classifyObjectProperty(owlReasoner, dataFactory, c);
      } else if (c.isType(iri(RDF.PROPERTY)) || (c.isType(iri(OWL.DATATYPE_PROPERTY)))) {
        classifyDataProperty(owlReasoner, dataFactory, c);
      } else {
        classifySuperClasses(owlReasoner, dataFactory, c);
      }
    }
    return document;
  }

  private void classifyObjectProperty(OpenlletReasoner owlReasoner, OWLDataFactory dataFactory, TTEntity c) {
    OWLObjectPropertyExpression ope = dataFactory.getOWLObjectProperty(IRI.create(c.getIri()));
    NodeSet<OWLObjectPropertyExpression> superOb = owlReasoner.getSuperObjectProperties(ope, true);
    if (superOb != null) {
      superOb.forEach(sob -> {
        if (!sob.getRepresentativeElement().isAnonymous()) {
          String iriName = sob.getRepresentativeElement().asOWLObjectProperty()
            .getIRI().toString();
          if (!iriName.equals(OWL.NAMESPACE + "topObjectProperty") && (!iriName.contains("_TOP_"))) {
            addSubClassOf(c, TTIriRef
              .iri(iriName));
          } else {
            addSubClassOf(c, iri(RDF.PROPERTY));
          }
        }
      });
    }
  }

  private void classifyDataProperty(OpenlletReasoner owlReasoner, OWLDataFactory dataFactory, TTEntity c) {
    OWLDataProperty dpe = dataFactory.getOWLDataProperty(IRI.create(c.getIri()));
    NodeSet<OWLDataProperty> superP = owlReasoner.getSuperDataProperties(dpe, true);
    if (superP != null) {
      superP.forEach(sob -> {
        if (!sob.getRepresentativeElement().isAnonymous()) {
          String iriName = sob.getRepresentativeElement().asOWLDataProperty()
            .getIRI().toString();
          if (!iriName.equals(OWL.NAMESPACE + "topDataProperty") && (!iriName.contains("_TOP_"))) {
            addSubClassOf(c, TTIriRef.iri(iriName));
          } else {
            addSubClassOf(c, iri(RDF.PROPERTY));
          }
        }
      });
    }
  }

  private void classifySuperClasses(OpenlletReasoner owlReasoner, OWLDataFactory dataFactory, TTEntity c) {
    OWLClassExpression owlClass = dataFactory.getOWLClass(IRI.create(c.getIri()));
    NodeSet<OWLClass> superClasses = owlReasoner.getSuperClasses(owlClass, true);
    if (superClasses != null) {
      superClasses.forEach(sup -> {
          TTIriRef iri = TTIriRef.iri(sup.getRepresentativeElement()
            .asOWLClass()
            .getIRI()
            .toString());
          if (!iri.getIri().equals(OWL.THING))
            addSubClassOf(c, iri);
        }
      );
    }
  }

  private void addSubClassOf(TTEntity entity, TTIriRef parent) {
    if (entity.get(iri(RDFS.SUBCLASS_OF)) == null)
      entity.set(iri(RDFS.SUBCLASS_OF), new TTArray());
    entity.get(iri(RDFS.SUBCLASS_OF)).add(parent);
  }

  public void inheritDomRans(TTEntity property, TTEntityMap propertyMap) {
    inheritDomains(property, propertyMap);
    inheritRanges(property, propertyMap);

  }

  private void inheritRanges(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(iri(RDFS.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(iri(RDFS.RANGE)) != null)
        superEntity.get(iri(RDFS.RANGE)).getElements().forEach(dom -> property.addObject(iri(RDFS.RANGE), dom));
    }
  }

  private void inheritDomains(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(iri(RDFS.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(iri(RDFS.DOMAIN)) != null)
        superEntity.get(iri(RDFS.DOMAIN)).getElements().forEach(dom -> property.addObject(iri(RDFS.DOMAIN), dom));
    }
  }

  public TTDocument inheritShapeProperties(TTDocument document) {
    manager = new TTManager();
    done = new HashSet<>();
    manager.setDocument(document);
    for (TTEntity entity : document.getEntities()) {
      if (entity.isType(iri(SHACL.NODESHAPE))) {
        inheritProperties(entity);
        inheritTemplates(entity);
      }
    }
    return document;

  }

  private void inheritTemplates(TTEntity shape) {
    if (shape.get(iri(IM.FUNCTION_TEMPLATE)) != null)
      return;
    if (shape.get(iri(RDFS.SUBCLASS_OF)) != null) {
      for (TTValue superIri : shape.get(iri(RDFS.SUBCLASS_OF)).getElements()) {
        TTEntity superEntity = manager.getEntity(superIri.asIriRef().getIri());
        if (superEntity != null && superEntity.isType(iri(SHACL.NODESHAPE))) {
          inheritTemplates(superEntity);
          if (superEntity.get(iri(IM.FUNCTION_TEMPLATE)) != null)
            shape.set(iri(IM.FUNCTION_TEMPLATE), superEntity.get(iri(IM.FUNCTION_TEMPLATE)));
        }
      }
    }
  }

  private void processSuperClasses(TTArray properties, List<TTValue> mergedProperties, TTEntity shape) {
    for (TTValue superClass : shape.get(iri(RDFS.SUBCLASS_OF)).getElements()) {
      TTEntity superEntity = manager.getEntity(superClass.asIriRef().getIri());
      if (superEntity != null) {
        mergeInheritedProperties(properties, mergedProperties, superClass, superEntity);
        if (shape.get(iri(IM.CONCEPT)) == null && superEntity.get(iri(IM.CONCEPT)) != null) {
          shape.set(iri(IM.CONCEPT), superEntity.get(iri(IM.CONCEPT)));
        }
        if (shape.get(iri(SHACL.GROUP)) == null && superEntity.get(iri(SHACL.GROUP)) != null) {
          shape.set(iri(SHACL.GROUP), superEntity.get(iri(SHACL.GROUP)));
        }
      }
    }
  }

  private void inheritProperties(TTEntity shape) {
    if (done.contains(shape.getIri()))
      return;
    TTArray properties = null;
    if (shape.get(iri(SHACL.PROPERTY)) != null)
      properties = shape.get(iri(SHACL.PROPERTY));
    List<TTValue> mergedProperties = new ArrayList<>();
    if (shape.get(iri(RDFS.SUBCLASS_OF)) != null) {
      processSuperClasses(properties, mergedProperties, shape);
      if (properties != null) {
        for (TTValue p : properties.getElements()) {
          if (p.asNode().get(iri(SHACL.ORDER)) == null) {
            p.asNode().set(iri(SHACL.ORDER), TTLiteral.literal(1000));
          }
        }
        mergedProperties.addAll(properties.getElements());
      }
      TTArray newValue = new TTArray();
      mergedProperties.forEach(newValue::add);
      shape.set(iri(SHACL.PROPERTY), newValue);
      done.add(shape.getIri());
    }
  }

  public void mergeInheritedProperties(TTArray properties, List<TTValue> mergedProperties, TTValue superClass, TTEntity superEntity) {
    inheritProperties(superEntity);
    if (superEntity.get(iri(SHACL.PROPERTY)) != null) {
      for (TTValue superP : superEntity.get(iri(SHACL.PROPERTY)).getElements()) {
        if (superP.asNode().get(iri(SHACL.PATH)) != null) {
          if (!hasProperty(properties, superP.asNode().get(iri(SHACL.PATH)).asIriRef())
            && !hasPath(mergedProperties, superP.asNode().get(iri(SHACL.PATH)).asIriRef())) {
            if (superP.asNode().get(iri(SHACL.ORDER)) == null) {
              superP.asNode().set(iri(SHACL.ORDER), TTLiteral.literal(1000));
            }
            TTNode inherited = copyNode(superP.asNode());
            inherited.set(iri(IM.INHERITED_FROM), superClass);
            mergedProperties.add(inherited);
          }
        }
      }
    }
  }

  private boolean hasPath(List<TTValue> mergedProperties, TTIriRef iri) {
    if (mergedProperties.isEmpty()) {
      return false;
    }
    for (TTValue p : mergedProperties) {
      if (p.asNode().get(iri(SHACL.PATH)).asIriRef().equals(iri)) {
        return true;
      }
    }
    return false;
  }

}
