package org.endeavourhealth.imapi.logic.reasoner;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.transforms.TTToOWLEL;
import org.endeavourhealth.interfacemanager.model.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.*;

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
        if (prop.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)) != null) {
          if (prop.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)).asIriRef().equals(path))
            return true;
        } else if (prop.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.INVERSEPATH)) != null && prop.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.INVERSEPATH)).asIriRef().equals(path))
          return true;
      }
    }
    return false;
  }

  private boolean hasParameter(TTArray subProperties, String parameterName) {
    if (subProperties != null) {
      for (TTValue prop : subProperties.getElements()) {
        if (prop.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)) != null) {
          if (prop.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)).asLiteral().getValue().equals(parameterName))
            return true;
        }
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
    inferred.setCrud(document.getCrud());
    classify(document);
    addDocumentRoles();
    for (TTEntity c : inferred.getEntities()) {
      if (c.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.CLASS))) {
        TTArray types = c.getType();
        List<TTValue> oldTypes = types.getElements();
        oldTypes.remove(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.CLASS));
        oldTypes.add(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.CLASS));
        c.setType(new TTArray());
        for (TTValue type : oldTypes)
          c.getType().add(type);
      }
      simplifyDomains(c);
      reformChains(c);
      if (c.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.OBJECT_PROPERTY))) {
        c.addType(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.PROPERTY));
        c.getType().remove(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.OBJECT_PROPERTY));
      }
      if (c.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.DATATYPE_PROPERTY))) {
        c.addType(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.PROPERTY));
        c.getType().remove(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.DATATYPE_PROPERTY));
      }

      c.getPredicateMap().remove(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.EQUIVALENT_CLASS));
      c.getPredicateMap().remove(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.PROPERTY_CHAIN));
    }

    return inferred;
  }

  private void reformChains(TTEntity entity) {
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.PROPERTY_CHAIN)) != null) {
      int i = 1;
      TTNode node = entity;
      for (TTValue property : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.PROPERTY_CHAIN)).iterator()) {
        if (i < entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.PROPERTY_CHAIN)).size()) {
          node.set(property.asIriRef(), new TTNode());
          node = node.get(property.asIriRef()).asNode();
          i++;
        } else
          node.set(property.asIriRef(), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT));
      }
    }
  }

  private void simplifyDomains(TTEntity entity) {

    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)) == null)
      return;

    TTArray newDomains = new TTArray();
    for (TTValue oldDomain : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)).iterator()) {
      if (oldDomain.isIriRef()) {
        newDomains.add(oldDomain);
      } else if (oldDomain.isNode() && oldDomain.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.UNION_OF)) != null) {
        for (TTValue subDomain : oldDomain.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.UNION_OF)).iterator()) {
          if (!subDomain.isIriRef()) {
            log.debug("Sub domains and ranges must be iris");
          } else {
            newDomains.add(subDomain);
          }
        }
      }
    }
    entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN), newDomains);
  }

  private void addDocumentRoles() {
    if (inferred.getEntities() == null)
      return;
    for (TTEntity entity : inferred.getEntities()) {
      addEntityRoles(entity);
    }
  }

  private void addEntityRoles(TTEntity entity) {
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)) != null) {
      for (TTValue superClass : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).iterator()) {
        if (!superClass.isIriRef()) {
          addExpression(entity, superClass);
        }
      }
    }
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.EQUIVALENT_CLASS)) != null) {
      for (TTValue equClass : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.EQUIVALENT_CLASS)).iterator()) {
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
      node.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), expression);
    } else if (expression.isNode()) {
      if (expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)) != null) {
        addExpressionIntersection(node, expression);
      } else if (expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.UNION_OF)) != null) {
        addExpressionUnion(node, expression);
      } else if (expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)) != null) {
        addRole(node, expression.asNode());
      } else
        log.debug("Only one level of nesting supported. ");
    } else
      throw new IllegalArgumentException("Unrecognised owl expression format");
  }

  private void addExpressionIntersection(TTNode node, TTValue expression) {
    for (TTValue subExp : expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)).iterator()) {
      if (subExp.isNode()) {
        if (subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)) != null) {
          addRole(node, subExp.asNode());
        } else
          addExpression(node, subExp);
      } else if (subExp.isIriRef() && !node.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).contains(subExp) && !(node instanceof TTEntity)) {
        node.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), subExp);
      }
    }
  }

  private void addExpressionUnion(TTNode node, TTValue expression) {
    node.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.OR), new TTArray());
    TTNode union = new TTNode();
    node.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.OR), union);
    addExpression(union, expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.UNION_OF)));
  }

  private void addExpressionRoles(TTEntity entity, TTValue expression) {
    if (!expression.isNode() || expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)) == null)
      return;

    for (TTValue subExp : expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)).iterator()) {
      if (subExp.isNode() && subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)) != null) {
        TTIriRef property = subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)).asIriRef();
        TTArray value = subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM));
        if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP)) == null) {
          TTNode roleGroup = new TTNode();
          roleGroup.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.GROUP_NUMBER), TTLiteral.literal(1));
          entity.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP), roleGroup);
        }
        if (value.isIriRef()) {
          entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP)).asNode().set(property, value);
        } else {
          TTNode subGroup = new TTNode();
          entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP)).asNode().set(property, subGroup);
          addSubRole(subGroup, value.asNode());
        }
      }
    }
  }

  private void addSubRole(TTNode subGroup, TTNode subExp) {
    if (subExp.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)) != null) {
      for (TTValue and : subExp.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)).getElements()) {
        if (and.isNode()) {
          addSubRole(subGroup, and.asNode());
        }
      }
    } else {

      TTIriRef property = subExp.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)).asIriRef();

      TTArray value = subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM));
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
    TTIriRef property = restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)).asIriRef();
    if (restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)) != null) {
      if (restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)).isIriRef()) {
        node.set(property, restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)));
      } else if (restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)).isNode()) {
        TTNode subRole = new TTNode();
        subRole(subRole, restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)).asNode());
        node.set(property, subRole);
      } else {
        throw new IllegalArgumentException("unknown property construct");
      }
    } else
      throw new IllegalArgumentException("Only existential quantifications are supported");

  }

  private void subRole(TTNode subRole, TTNode node) {
    if (subRole.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)) != null) {
      for (TTValue subExp : subRole.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)).iterator()) {
        if (subExp.isIriRef())
          node.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), subExp);
        else
          addRole(node, subExp.asNode());
      }
    }
    if (subRole.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.UNION_OF)) != null) {
      node.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.OR), new TTArray());
      for (TTValue subExp : subRole.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.UNION_OF)).iterator()) {
        if (subExp.isIriRef())
          node.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), subExp);
        else {
          TTNode union = new TTNode();
          node.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.OR)).add(union);
          addRole(union, subExp.asNode());
        }
      }
    }

  }

  /**
   * Classifies an ontology using an OWL Reasoner from concepts help in a TTDocument
   *
   * @param document The TTDocument to classify
   * @throws OWLOntologyCreationException for invalid owl formats leading to inability to create ontology
   */

  public void classify(TTDocument document) throws OWLOntologyCreationException {
    manager = new TTManager();
    manager.setDocument(document);

    if (document.getEntities() == null)
      return;

    entityMap = new HashMap<>();
    //builds entity map for later look up
    document.getEntities().forEach(c -> entityMap.put(c.getIri(), c));
    TTToOWLEL transformer = new TTToOWLEL();
    TTManager dmanager = new TTManager();
    dmanager.setDocument(document);
    OWLOntologyManager owlManager = transformer.transform(document, dmanager, GraphVocab.IM);
    Set<OWLOntology> owlOntologySet = owlManager.getOntologies();
    Optional<OWLOntology> owlOntology = owlOntologySet.stream().findFirst();

    if (owlOntology.isEmpty())
      return;

    OWLReasonerConfiguration config = new SimpleConfiguration();
    OWLOntology o = owlOntology.get();
    OpenlletReasoner owlReasoner = OpenlletReasonerFactory.getInstance().createReasoner(o, config);
    owlReasoner.precomputeInferences();

    if (!owlReasoner.isConsistent())
      return;

    OWLDataFactory dataFactory = new OWLDataFactoryImpl();
    for (TTEntity c : document.getEntities()) {
      inferred.addEntity(c);
      c.getPredicateMap().remove(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF));
      c.getPredicateMap().remove(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUB_PROPERTY_OF));
      if (c.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.EQUIVALENT_CLASS)) != null)
        c.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITIONAL_STATUS), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.SUFFICIENTLY_DEFINED));
      if (c.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.OBJECT_PROPERTY)) || c.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.PROPERTY)) || c.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.DATATYPE_PROPERTY))) {
        classifyObjectProperty(owlReasoner, dataFactory, c);
      } else if (c.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.PROPERTY)) || (c.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.DATATYPE_PROPERTY)))) {
        classifyDataProperty(owlReasoner, dataFactory, c);
      } else {
        classifySuperClasses(owlReasoner, dataFactory, c);
      }
    }
  }

  private void classifyObjectProperty(OpenlletReasoner owlReasoner, OWLDataFactory dataFactory, TTEntity c) {
    OWLObjectPropertyExpression ope = dataFactory.getOWLObjectProperty(IRI.create(c.getIri()));
    NodeSet<OWLObjectPropertyExpression> superOb = owlReasoner.getSuperObjectProperties(ope, true);
    if (superOb != null) {
      superOb.forEach(sob -> {
        if (!sob.getRepresentativeElement().isAnonymous()) {
          String iriName = sob.getRepresentativeElement().asOWLObjectProperty()
            .getIRI().toString();
          if (!iriName.equals(NamespaceVocab.OWL + "topObjectProperty") && (!iriName.contains("_TOP_"))) {
            addSubClassOf(c, TTIriRefExtensionsKt.iri(new TTIriRef(), iriName));
          } else {
            addSubClassOf(c, TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.PROPERTY));
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
          if (!iriName.equals(NamespaceVocab.OWL + "topDataProperty") && (!iriName.contains("_TOP_"))) {
            addSubClassOf(c, TTIriRefExtensionsKt.iri(new TTIriRef(), iriName));
          } else {
            addSubClassOf(c, TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.PROPERTY));
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
          TTIriRef iri = TTIriRefExtensionsKt.iri(new TTIriRef(), sup.getRepresentativeElement()
            .asOWLClass()
            .getIRI()
            .toString());
          if (!iri.getIri().equals(OwlVocab.THING.toString()))
            addSubClassOf(c, iri);
        }
      );
    }
  }

  private void addSubClassOf(TTEntity entity, TTIriRef parent) {
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)) == null)
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), new TTArray());
    entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).add(parent);
  }

  public void inheritDomRans(TTEntity property, TTEntityMap propertyMap) {
    inheritDomains(property, propertyMap);
    inheritRanges(property, propertyMap);

  }

  private void inheritRanges(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE)) != null)
        superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE)).getElements().forEach(dom -> property.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE), dom));
    }
  }

  private void inheritDomains(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)) != null)
        superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)).getElements().forEach(dom -> property.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN), dom));
    }
  }

  public TTDocument inheritShapeProperties(TTDocument document) {
    manager = new TTManager();
    done = new HashSet<>();
    manager.setDocument(document);
    for (TTEntity entity : document.getEntities()) {
      if (entity.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.FUNCTION)))
        inheritProperties(ShaclVocab.PARAMETER, entity);
      if (entity.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODESHAPE))) {
        inheritProperties(ShaclVocab.PROPERTY, entity);
        inheritTemplates(entity);
      }
    }
    return document;

  }

  private void inheritTemplates(TTEntity shape) {
    if (shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.FUNCTION_TEMPLATE)) != null)
      return;
    if (shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)) != null) {
      for (TTValue superIri : shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
        TTEntity superEntity = manager.getEntity(superIri.asIriRef().getIri());
        if (superEntity != null && superEntity.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODESHAPE))) {
          inheritTemplates(superEntity);
          if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.FUNCTION_TEMPLATE)) != null)
            shape.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.FUNCTION_TEMPLATE), superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.FUNCTION_TEMPLATE)));
        }
      }
    }
  }

  private void processSuperClasses(SHACL predicate, TTArray properties, List<TTValue> mergedProperties, TTEntity shape) {
    for (TTValue superClass : shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTEntity superEntity = manager.getEntity(superClass.asIriRef().getIri());
      if (superEntity != null) {
        mergeInheritedProperties(predicate, properties, mergedProperties, superClass, superEntity);
        if (shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT)) == null && superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT)) != null) {
          shape.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT), superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT)));
        }
        if (shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.GROUP)) == null && superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.GROUP)) != null) {
          shape.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.GROUP), superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.GROUP)));
        }
      }
    }
  }


  private void inheritProperties(SHACL predicate, TTEntity shape) {
    if (done.contains(shape.getIri()))
      return;
    TTArray properties = null;
    if (shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate)) != null)
      properties = shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate));
    List<TTValue> mergedProperties = new ArrayList<>();
    if (shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)) != null) {
      processSuperClasses(predicate, properties, mergedProperties, shape);
      if (properties != null) {
        for (TTValue p : properties.getElements()) {
          if (p.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER)) == null) {
            p.asNode().set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER), TTLiteral.literal(1000));
          }
        }
        mergedProperties.addAll(properties.getElements());
      }
      TTArray newValue = new TTArray();
      mergedProperties.forEach(newValue::add);
      shape.set(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), newValue);
      done.add(shape.getIri());
    }
  }


  public void mergeInheritedProperties(SHACL predicate, TTArray properties, List<TTValue> mergedProperties, TTValue superClass, TTEntity superEntity) {
    inheritProperties(predicate, superEntity);
    if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate)) != null) {
      for (TTValue superP : superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate)).getElements()) {
        if (predicate == ShaclVocab.PARAMETER) {
          if (!hasParameter(properties, superP.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)).asLiteral().getValue())) {
            TTNode inherited = copyNode(superP.asNode());
            inherited.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.INHERITED_FROM), superClass);
            mergedProperties.add(inherited);
          }
        }
        if (superP.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)) != null) {
          if (!hasProperty(properties, superP.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)).asIriRef())
            && !hasPath(mergedProperties, superP.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)).asIriRef())) {
            if (superP.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER)) == null) {
              superP.asNode().set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER), TTLiteral.literal(1000));
            }
            TTNode inherited = copyNode(superP.asNode());
            inherited.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.INHERITED_FROM), superClass);
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
      if (p.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)).asIriRef().equals(iri)) {
        return true;
      }
    }
    return false;
  }


}
