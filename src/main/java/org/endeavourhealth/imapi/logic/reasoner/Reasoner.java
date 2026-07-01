package org.endeavourhealth.imapi.logic.reasoner;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.tripletree.TTDocumentJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
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
  private TTDocumentJava inferred;
  @Getter
  private HashMap<String, TTEntityJava> entityMap;
  private TTManager manager;
  private Set<String> done;

  private static TTNodeJava copyNode(TTNodeJava node) {
    TTNodeJava result = new TTNodeJava();
    if (node.getPredicateMap() != null) {
      for (Map.Entry<TTIriRef, TTArrayJava> entry : node.getPredicateMap().entrySet()) {
        result.set(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }

  private static boolean hasProperty(TTArrayJava subProperties, TTIriRef path) {
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

  private boolean hasParameter(TTArrayJava subProperties, String parameterName) {
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

  public TTDocumentJava generateInferred(TTDocumentJava document) throws OWLOntologyCreationException {
    //Creates isas
    manager = new TTManager();
    manager.setDocument(document);
    inferred = new TTDocumentJava();
    inferred.setContext(document.getContext());
    inferred.setCrud(document.getCrud());
    classify(document);
    addDocumentRoles();
    for (TTEntityJava c : inferred.getEntities()) {
      if (c.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.CLASS))) {
        TTArrayJava types = c.getType();
        List<TTValue> oldTypes = types.getElements();
        oldTypes.remove(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.CLASS));
        oldTypes.add(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.CLASS));
        c.setType(new TTArrayJava());
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

  private void reformChains(TTEntityJava entity) {
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.PROPERTY_CHAIN)) != null) {
      int i = 1;
      TTNodeJava node = entity;
      for (TTValue property : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.PROPERTY_CHAIN)).iterator()) {
        if (i < entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.PROPERTY_CHAIN)).size()) {
          node.set(property.asIriRef(), new TTNodeJava());
          node = node.get(property.asIriRef()).asNode();
          i++;
        } else
          node.set(property.asIriRef(), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT));
      }
    }
  }

  private void simplifyDomains(TTEntityJava entity) {

    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)) == null)
      return;

    TTArrayJava newDomains = new TTArrayJava();
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
    for (TTEntityJava entity : inferred.getEntities()) {
      addEntityRoles(entity);
    }
  }

  private void addEntityRoles(TTEntityJava entity) {
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

  private void addExpression(TTNodeJava node, TTArrayJava expression) {
    for (TTValue subExp : expression.iterator()) {
      addExpression(node, subExp);
    }
  }

  private void addExpression(TTNodeJava node, TTValue expression) {
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

  private void addExpressionIntersection(TTNodeJava node, TTValue expression) {
    for (TTValue subExp : expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)).iterator()) {
      if (subExp.isNode()) {
        if (subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)) != null) {
          addRole(node, subExp.asNode());
        } else
          addExpression(node, subExp);
      } else if (subExp.isIriRef() && !node.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).contains(subExp) && !(node instanceof TTEntityJava)) {
        node.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), subExp);
      }
    }
  }

  private void addExpressionUnion(TTNodeJava node, TTValue expression) {
    node.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.OR), new TTArrayJava());
    TTNodeJava union = new TTNodeJava();
    node.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.OR), union);
    addExpression(union, expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.UNION_OF)));
  }

  private void addExpressionRoles(TTEntityJava entity, TTValue expression) {
    if (!expression.isNode() || expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)) == null)
      return;

    for (TTValue subExp : expression.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)).iterator()) {
      if (subExp.isNode() && subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)) != null) {
        TTIriRef property = subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)).asIriRef();
        TTArrayJava value = subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM));
        if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP)) == null) {
          TTNodeJava roleGroup = new TTNodeJava();
          roleGroup.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.GROUP_NUMBER), TTLiteral.literal(1));
          entity.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP), roleGroup);
        }
        if (value.isIriRef()) {
          entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP)).asNode().set(property, value);
        } else {
          TTNodeJava subGroup = new TTNodeJava();
          entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP)).asNode().set(property, subGroup);
          addSubRole(subGroup, value.asNode());
        }
      }
    }
  }

  private void addSubRole(TTNodeJava subGroup, TTNodeJava subExp) {
    if (subExp.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)) != null) {
      for (TTValue and : subExp.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)).getElements()) {
        if (and.isNode()) {
          addSubRole(subGroup, and.asNode());
        }
      }
    } else {

      TTIriRef property = subExp.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)).asIriRef();

      TTArrayJava value = subExp.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM));
      if (value.isIriRef()) {
        subGroup.set(property, value);
      } else {
        TTNodeJava subSub = new TTNodeJava();
        subGroup.set(property, subSub);
        addSubRole(subGroup, value.asNode());
      }
    }

  }

  private void addRole(TTNodeJava node, TTNodeJava restriction) {
    TTIriRef property = restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY)).asIriRef();
    if (restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)) != null) {
      if (restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)).isIriRef()) {
        node.set(property, restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)));
      } else if (restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)).isNode()) {
        TTNodeJava subRole = new TTNodeJava();
        subRole(subRole, restriction.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM)).asNode());
        node.set(property, subRole);
      } else {
        throw new IllegalArgumentException("unknown property construct");
      }
    } else
      throw new IllegalArgumentException("Only existential quantifications are supported");

  }

  private void subRole(TTNodeJava subRole, TTNodeJava node) {
    if (subRole.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)) != null) {
      for (TTValue subExp : subRole.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF)).iterator()) {
        if (subExp.isIriRef())
          node.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), subExp);
        else
          addRole(node, subExp.asNode());
      }
    }
    if (subRole.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.UNION_OF)) != null) {
      node.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.OR), new TTArrayJava());
      for (TTValue subExp : subRole.get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.UNION_OF)).iterator()) {
        if (subExp.isIriRef())
          node.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), subExp);
        else {
          TTNodeJava union = new TTNodeJava();
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

  public void classify(TTDocumentJava document) throws OWLOntologyCreationException {
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
    for (TTEntityJava c : document.getEntities()) {
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

  private void classifyObjectProperty(OpenlletReasoner owlReasoner, OWLDataFactory dataFactory, TTEntityJava c) {
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

  private void classifyDataProperty(OpenlletReasoner owlReasoner, OWLDataFactory dataFactory, TTEntityJava c) {
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

  private void classifySuperClasses(OpenlletReasoner owlReasoner, OWLDataFactory dataFactory, TTEntityJava c) {
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

  private void addSubClassOf(TTEntityJava entity, TTIriRef parent) {
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)) == null)
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), new TTArrayJava());
    entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).add(parent);
  }

  public void inheritDomRans(TTEntityJava property, TTEntityMap propertyMap) {
    inheritDomains(property, propertyMap);
    inheritRanges(property, propertyMap);

  }

  private void inheritRanges(TTEntityJava property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntityJava superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE)) != null)
        superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE)).getElements().forEach(dom -> property.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE), dom));
    }
  }

  private void inheritDomains(TTEntityJava property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntityJava superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)) != null)
        superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)).getElements().forEach(dom -> property.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN), dom));
    }
  }

  public TTDocumentJava inheritShapeProperties(TTDocumentJava document) {
    manager = new TTManager();
    done = new HashSet<>();
    manager.setDocument(document);
    for (TTEntityJava entity : document.getEntities()) {
      if (entity.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.FUNCTION)))
        inheritProperties(ShaclVocab.PARAMETER, entity);
      if (entity.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODESHAPE))) {
        inheritProperties(ShaclVocab.PROPERTY, entity);
        inheritTemplates(entity);
      }
    }
    return document;

  }

  private void inheritTemplates(TTEntityJava shape) {
    if (shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.FUNCTION_TEMPLATE)) != null)
      return;
    if (shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)) != null) {
      for (TTValue superIri : shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
        TTEntityJava superEntity = manager.getEntity(superIri.asIriRef().getIri());
        if (superEntity != null && superEntity.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODESHAPE))) {
          inheritTemplates(superEntity);
          if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.FUNCTION_TEMPLATE)) != null)
            shape.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.FUNCTION_TEMPLATE), superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.FUNCTION_TEMPLATE)));
        }
      }
    }
  }

  private void processSuperClasses(SHACL predicate, TTArrayJava properties, List<TTValue> mergedProperties, TTEntityJava shape) {
    for (TTValue superClass : shape.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTEntityJava superEntity = manager.getEntity(superClass.asIriRef().getIri());
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


  private void inheritProperties(SHACL predicate, TTEntityJava shape) {
    if (done.contains(shape.getIri()))
      return;
    TTArrayJava properties = null;
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
      TTArrayJava newValue = new TTArrayJava();
      mergedProperties.forEach(newValue::add);
      shape.set(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), newValue);
      done.add(shape.getIri());
    }
  }


  public void mergeInheritedProperties(SHACL predicate, TTArrayJava properties, List<TTValue> mergedProperties, TTValue superClass, TTEntityJava superEntity) {
    inheritProperties(predicate, superEntity);
    if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate)) != null) {
      for (TTValue superP : superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate)).getElements()) {
        if (predicate == ShaclVocab.PARAMETER) {
          if (!hasParameter(properties, superP.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)).asLiteral().getValue())) {
            TTNodeJava inherited = copyNode(superP.asNode());
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
            TTNodeJava inherited = copyNode(superP.asNode());
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
