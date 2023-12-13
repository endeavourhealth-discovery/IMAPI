package org.endeavourhealth.imapi.logic.reasoner;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

/**
 * Classifies an ontology using an owl reasoner, generating ISA relationships from a Discovery ontology document.
 * Generates inferred role groups (Snomed pattern) from the existential quntifiers and propogates them to subclasses
 */
public class Reasoner {
   private TTDocument inferred;
   private HashMap<String, TTEntity> entityMap;
   private TTManager manager;
   private Set<String> done ;

   private static final Logger LOG = LoggerFactory.getLogger(Reasoner.class);




   public TTDocument generateInferred(TTDocument document) throws OWLOntologyCreationException,DataFormatException {
      //Creates isas
      manager= new TTManager();
      manager.setDocument(document);
      inferred= new TTDocument();
      inferred.setContext(document.getContext());
      inferred.setGraph(document.getGraph());
      inferred.setCrud(document.getCrud());
      classify(document);
      addDocumentRoles();
      for (TTEntity c:inferred.getEntities()){
         if (c.isType(OWL.CLASS)){
            TTArray types= c.getType();
            List<TTValue> oldTypes= types.getElements();
            oldTypes.remove(OWL.CLASS.asTTIriRef());
            oldTypes.add(RDFS.CLASS.asTTIriRef());
            c.setType(new TTArray());
            for (TTValue type:oldTypes)
               c.getType().add(type);
         }
         simplifyDomains(c);
         reformChains(c);
         if (c.isType(OWL.OBJECTPROPERTY))
            c.setType(new TTArray().add(RDF.PROPERTY.asTTIriRef()));
         if (c.isType(OWL.DATATYPEPROPERTY))
            c.setType(new TTArray().add(RDF.PROPERTY.asTTIriRef()));

         c.getPredicateMap().remove(OWL.EQUIVALENTCLASS.asTTIriRef());
         c.getPredicateMap().remove(OWL.PROPERTYCHAIN.asTTIriRef());
      }

      return inferred;
   }


   private void reformChains(TTEntity entity) {
      if (entity.get(OWL.PROPERTYCHAIN) != null) {
         int i = 1;
         TTNode node = entity;
         for (TTValue property : entity.get(OWL.PROPERTYCHAIN).iterator()) {
            if (i < entity.get(OWL.PROPERTYCHAIN).size()) {
               node.set(property.asIriRef(), new TTNode());
               node = node.get(property.asIriRef()).asNode();
               i++;
            } else
               node.set(property.asIriRef(), IM.CONCEPT.asTTIriRef());
         }
      }
   }


   private void simplifyDomains(TTEntity entity) {

      if (entity.get(RDFS.DOMAIN.asTTIriRef()) == null)
         return;

      TTArray newDomains = new TTArray();
      for (TTValue oldDomain : entity.get(RDFS.DOMAIN.asTTIriRef()).iterator()) {
         if (oldDomain.isIriRef()) {
            newDomains.add(oldDomain);
         } else if (oldDomain.isNode() && oldDomain.asNode().get(OWL.UNIONOF) != null) {
            for (TTValue subDomain : oldDomain.asNode().get(OWL.UNIONOF).iterator()) {
               if (!subDomain.isIriRef()) {
                  LOG.debug("Sub domains and ranges must be iris");
               } else {
                  newDomains.add(subDomain);
               }
            }
         }
      }
      entity.set(RDFS.DOMAIN.asTTIriRef(), newDomains);
   }


   private void addDocumentRoles() throws DataFormatException {
      if (inferred.getEntities() == null)
         return;
      for (TTEntity entity:inferred.getEntities()) {
         addEntityRoles(entity);
      }
   }

   private void addEntityRoles(TTEntity entity) throws DataFormatException {
      if (entity.get(RDFS.SUBCLASSOF.asTTIriRef()) != null) {
         for (TTValue superClass : entity.get(RDFS.SUBCLASSOF.asTTIriRef()).iterator()) {
            if (!superClass.isIriRef()) {
               addExpression(entity, superClass);
            }
         }
      }
      if (entity.get(OWL.EQUIVALENTCLASS) != null) {
         for (TTValue equClass : entity.get(OWL.EQUIVALENTCLASS).iterator()) {
            if (!equClass.isIriRef()) {
               addExpressionRoles(entity, equClass);
            }
         }
      }
   }

   private void addExpression(TTNode node, TTArray expression) throws DataFormatException {
       for (TTValue subExp:expression.iterator()) {
           addExpression(node, subExp);
       }
   }

   private void addExpression(TTNode node,TTValue expression) throws DataFormatException {
      if (expression.isIriRef()) {
         node.addObject(RDFS.SUBCLASSOF.asTTIriRef(), expression);
      } else if (expression.isNode()) {
         if (expression.asNode().get(OWL.INTERSECTIONOF) != null) {
            addExpressionIntersection(node, expression);
         } else if (expression.asNode().get(OWL.UNIONOF) != null) {
            addExpressionUnion(node, expression);
         } else if (expression.asNode().get(OWL.ONPROPERTY) != null) {
            addRole(node, expression.asNode());
         } else
            LOG.debug("Only one level of nesting supported. ");
      } else
         throw new DataFormatException("Unrecognised owl expression format");
   }

   private void addExpressionIntersection(TTNode node, TTValue expression) throws DataFormatException {
      for (TTValue subExp : expression.asNode().get(OWL.INTERSECTIONOF).iterator()) {
         if (subExp.isNode()) {
            if (subExp.asNode().get(OWL.ONPROPERTY) != null) {
               addRole(node, subExp.asNode());
            } else
               addExpression(node, subExp);
         } else if (subExp.isIriRef() && !node.get(RDFS.SUBCLASSOF.asTTIriRef()).contains(subExp) && !(node instanceof TTEntity)) {
            node.addObject(RDFS.SUBCLASSOF.asTTIriRef(), subExp);
         }
      }
   }

   private void addExpressionUnion(TTNode node, TTValue expression) throws DataFormatException {
      node.set(SHACL.OR, new TTArray());
      TTNode union = new TTNode();
      node.addObject(SHACL.OR, union);
      addExpression(union, expression.asNode().get(OWL.UNIONOF));
   }


   private void addExpressionRoles(TTEntity entity,TTValue expression) throws DataFormatException {
      if (!expression.isNode() || expression.asNode().get(OWL.INTERSECTIONOF) == null)
         return;

      for (TTValue subExp : expression.asNode().get(OWL.INTERSECTIONOF).iterator()) {
         if (subExp.isNode() && subExp.asNode().get(OWL.ONPROPERTY) != null) {
            TTIriRef property = subExp.asNode().get(OWL.ONPROPERTY).asIriRef();
            TTArray value = subExp.asNode().get(OWL.SOMEVALUESFROM);
            if (entity.get(IM.ROLE_GROUP.asTTIriRef()) == null) {
               TTNode roleGroup = new TTNode();
               roleGroup.set(IM.GROUP_NUMBER.asTTIriRef(), TTLiteral.literal(1));
               entity.addObject(IM.ROLE_GROUP.asTTIriRef(), roleGroup);
            }
            if (value.isIriRef()) {
               entity.get(IM.ROLE_GROUP.asTTIriRef()).asNode().set(property, value);
            } else {
               TTNode subGroup = new TTNode();
               entity.get(IM.ROLE_GROUP.asTTIriRef()).asNode().set(property, subGroup);
               addSubRole(subGroup, value.asNode());
            }
         }
      }
   }

   private void addSubRole(TTNode subGroup,TTNode subExp) {
      if (subExp.get(OWL.INTERSECTIONOF)!=null) {
         for (TTValue and:subExp.get(OWL.INTERSECTIONOF).getElements()){
            if (and.isNode()){
               addSubRole(subGroup,and.asNode());
            }
         }
      }
      else {

         TTIriRef property = subExp.get(OWL.ONPROPERTY).asIriRef();

         TTArray value = subExp.asNode().get(OWL.SOMEVALUESFROM);
         if (value.isIriRef()) {
            subGroup.set(property, value);
         } else {
            TTNode subSub = new TTNode();
            subGroup.set(property, subSub);
            addSubRole(subGroup, value.asNode());
         }
      }

   }

   private void addRole(TTNode node, TTNode restriction) throws DataFormatException {
      TTIriRef property = restriction.get(OWL.ONPROPERTY).asIriRef();
      if (restriction.get(OWL.SOMEVALUESFROM) != null) {
         if (restriction.get(OWL.SOMEVALUESFROM).isIriRef()) {
            node.set(property, restriction.get(OWL.SOMEVALUESFROM));
         } else if (restriction.get(OWL.SOMEVALUESFROM).isNode()) {
            TTNode subRole= new TTNode();
            subRole(subRole,restriction.get(OWL.SOMEVALUESFROM).asNode());
            node.set(property,subRole);
         } else {
            throw new DataFormatException("unknown property construct");
         }
      } else
         throw new DataFormatException("Only existential quantifications are supported");

   }

   private void subRole(TTNode subRole, TTNode node) throws DataFormatException {
      if (subRole.asNode().get(OWL.INTERSECTIONOF) != null) {
         for (TTValue subExp : subRole.get(OWL.INTERSECTIONOF).iterator()) {
            if (subExp.isIriRef())
               node.addObject(RDFS.SUBCLASSOF.asTTIriRef(),subExp);
            else
               addRole(node,subExp.asNode());
            }
         }
      if (subRole.asNode().get(OWL.UNIONOF) != null) {
         node.set(SHACL.OR,new TTArray());
         for (TTValue subExp : subRole.get(OWL.UNIONOF).iterator()) {
            if (subExp.isIriRef())
               node.addObject(RDFS.SUBCLASSOF.asTTIriRef(),subExp);
            else {
               TTNode union= new TTNode();
               node.get(SHACL.OR).add(union);
               addRole(union, subExp.asNode());
            }
         }
      }

   }


   /**
    * Classifies an ontology using an OWL Reasoner from concepts help in a TTDocument
    * @return set of child -  parent "isa" nodes
    * @param document The TTDocument to classify
    * @throws  OWLOntologyCreationException for invalid owl formats leading to inability to create ontology
    * @throws DataFormatException for invalid owl content
    */

   public TTDocument classify(TTDocument document) throws OWLOntologyCreationException, DataFormatException {
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
         c.getPredicateMap().remove(RDFS.SUBCLASSOF.asTTIriRef());
         c.getPredicateMap().remove(RDFS.SUBPROPERTYOF.asTTIriRef());
         if (c.get(OWL.EQUIVALENTCLASS) != null)
            c.set(IM.DEFINITIONAL_STATUS.asTTIriRef(), IM.SUFFICIENTLY_DEFINED.asTTIriRef());
         if (c.isType(OWL.OBJECTPROPERTY) || c.isType(RDF.PROPERTY.asTTIriRef()) || c.isType(OWL.DATATYPEPROPERTY)) {
            classifyObjectProperty(owlReasoner, dataFactory, c);
         } else if (c.isType(RDF.PROPERTY.asTTIriRef()) || (c.isType(OWL.DATATYPEPROPERTY))) {
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
               if (!iriName.equals(OWL.NAMESPACE.iri + "topObjectProperty") && (!iriName.contains("_TOP_"))) {
                  addSubClassOf(c, TTIriRef
                          .iri(iriName));
               } else {
                  addSubClassOf(c, RDF.PROPERTY.asTTIriRef());
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
               if (!iriName.equals(OWL.NAMESPACE.iri + "topDataProperty") && (!iriName.contains("_TOP_"))) {
                  addSubClassOf(c, TTIriRef.iri(iriName));
               } else {
                  addSubClassOf(c, RDF.PROPERTY.asTTIriRef());
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
                    if (!iri.getIri().equals(OWL.THING.iri))
                       addSubClassOf(c, iri);
                 }
         );
      }
                  /*
                  Node<OWLClass> equClasses= owlReasoner.getEquivalentClasses(owlClass);
               equClasses.forEach(sup -> {if (sup.isOWLClass()){
                  TTIriRef superIri= TTIriRef.iri(sup.getIRI().toString());
                  if (!superIri.equals(TTIriRef.iri(c.getIri())))
                     addSubClassOf(c,superIri);}
               });
               */
   }

   private void addSubClassOf(TTEntity entity,TTIriRef parent){
      if (entity.get(RDFS.SUBCLASSOF.asTTIriRef())==null)
         entity.set(RDFS.SUBCLASSOF.asTTIriRef(),new TTArray());
      entity.get(RDFS.SUBCLASSOF.asTTIriRef()).add(parent);
   }


   public void inheritDomRans(TTEntity property,TTEntityMap propertyMap){
      inheritDomains(property,propertyMap);
      inheritRanges(property,propertyMap);

   }

   private void inheritRanges(TTEntity property, TTEntityMap propertyMap) {
      for (TTValue superProp:property.get(RDFS.SUBCLASSOF.asTTIriRef()).getElements()){
         TTIriRef superIri= superProp.asIriRef();
         TTEntity superEntity= propertyMap.getEntity(superIri.getIri());
         inheritDomains(superEntity, propertyMap);
         if (superEntity.get(RDFS.RANGE.asTTIriRef())!=null)
            superEntity.get(RDFS.RANGE.asTTIriRef()).getElements().forEach(dom-> property.addObject(RDFS.RANGE.asTTIriRef(),dom));
      }
   }

   private void inheritDomains(TTEntity property, TTEntityMap propertyMap) {
      for (TTValue superProp:property.get(RDFS.SUBCLASSOF.asTTIriRef()).getElements()){
         TTIriRef superIri= superProp.asIriRef();
         TTEntity superEntity= propertyMap.getEntity(superIri.getIri());
         inheritDomains(superEntity, propertyMap);
         if (superEntity.get(RDFS.DOMAIN.asTTIriRef())!=null)
            superEntity.get(RDFS.DOMAIN.asTTIriRef()).getElements().forEach(dom-> property.addObject(RDFS.DOMAIN.asTTIriRef(),dom));
      }
   }
   public TTDocument inheritShapeProperties(TTDocument document){
      manager= new TTManager();
      done= new HashSet<>();
      manager.setDocument(document);
      for (TTEntity entity:document.getEntities()){
         if (entity.isType(SHACL.NODESHAPE)) {
            inheritProperties(entity);
         }
      }
      return document;

   }

   private void inheritProperties(TTEntity shape) {
      if (done.contains(shape.getIri()))
         return;
      List<TTValue> properties = getOrderedProperties(shape);
      int order = 0;
      List<TTValue> mergedProperties = new ArrayList<>();
      if (shape.get(RDFS.SUBCLASSOF.asTTIriRef()) != null) {
         for (TTValue superClass : shape.get(RDFS.SUBCLASSOF.asTTIriRef()).getElements()) {
            TTEntity superEntity = manager.getEntity(superClass.asIriRef().getIri());
            if (superEntity != null) {
               mergeInheritedProperties(properties, order, mergedProperties, superClass, superEntity);
            }
         }
         if (properties != null) {
            for (TTValue prop : properties) {
               order++;
               prop.asNode().set(SHACL.ORDER, TTLiteral.literal(order));
               mergedProperties.add(prop);
            }
         }
         TTArray newValue = new TTArray();
         mergedProperties.forEach(newValue::add);
         shape.set(SHACL.PROPERTY, newValue);
         done.add(shape.getIri());
      }
   }

   public void mergeInheritedProperties(List<TTValue> properties, int order, List<TTValue> mergedProperties,TTValue superClass, TTEntity superEntity) {
      inheritProperties(superEntity);
      if (superEntity.get(SHACL.PROPERTY) != null) {
         for (TTValue superP : superEntity.get(SHACL.PROPERTY).getElements()) {
            if (superP.asNode().get(SHACL.PATH)==null){
               order++;
               TTNode inherited= copyNode(superP.asNode());
               inherited.set(SHACL.ORDER, TTLiteral.literal(order));
               inherited.set(IM.INHERITED_FROM.asTTIriRef(), superClass);
               mergedProperties.add(inherited);
            }
            else {
               if (!hasProperty(properties, superP.asNode().get(SHACL.PATH).asIriRef())) {
                  order++;
                  TTNode inherited= copyNode(superP.asNode());
                  inherited.set(SHACL.ORDER, TTLiteral.literal(order));
                  inherited.set(IM.INHERITED_FROM.asTTIriRef(), superClass);
                  mergedProperties.add(inherited);
               }
            }
         }
      }
   }

   private static TTNode copyNode(TTNode node){
      TTNode result= new TTNode();
      if (node.getPredicateMap()!=null){
         for (Map.Entry<TTIriRef,TTArray> entry:node.getPredicateMap().entrySet()){
            result.set(entry.getKey(),entry.getValue());
         }
      }
      return result;
   }


   private static List<TTValue> getOrderedProperties(TTEntity shape) {
      if (shape.get(SHACL.PROPERTY) != null) {
         List<TTValue> properties = new ArrayList<>(shape.get(SHACL.PROPERTY).getElements());
         assignMissingOrder(properties);
         return properties.stream()
           .sorted(Comparator.comparingInt((TTValue p) -> p.asNode().get(SHACL.ORDER).asLiteral().intValue()))
           .collect(Collectors.toList());
      }
      return Collections.emptyList();
   }


   private static void assignMissingOrder(List<TTValue> properties){
      int order=0;
      for (TTValue node:properties){
         if (node.asNode().get(SHACL.ORDER)==null){
            order++;
            node.asNode().set(SHACL.ORDER,TTLiteral.literal(order));
         }
      }
   }

   private static boolean hasProperty(List<TTValue> subProperties, TTIriRef path) {
      if (subProperties!=null){
         for (TTValue prop: subProperties) {
            if (prop.asNode().get(SHACL.PATH)!=null) {
               if (prop.asNode().get(SHACL.PATH).asIriRef().equals(path))
                  return true;
            }
            else if (prop.asNode().get(SHACL.INVERSEPATH)!=null && prop.asNode().get(SHACL.INVERSEPATH).asIriRef().equals(path))
               return true;
         }
      }
      return false;
   }

}
