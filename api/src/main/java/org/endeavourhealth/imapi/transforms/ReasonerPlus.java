package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Classifies an ontology using an owl reasoner, generating ISA relationships from a Discovery ontology document.
 * Generates inferred role groups (Snomed pattern) from the existential quntifiers and propogates them to subclasses
 */
public class ReasonerPlus {
   private TTDocument document;
   private TTDocument inferred;
   private HashMap<String, TTEntity> entityMap;
   private boolean consistent;
   private OWLReasoner owlReasoner;
   private TTManager manager;
   private Set<String> done;

   private static final Logger LOG = LoggerFactory.getLogger(ReasonerPlus.class);

   public ReasonerPlus(){
      manager= new TTManager();
   }


   public TTDocument generateInferred(TTDocument document) throws OWLOntologyCreationException,DataFormatException {
      //Creates isas
      manager= new TTManager();
      manager.setDocument(document);
      inferred= new TTDocument();
      inferred.setContext(document.getContext());
      inferred.setGraph(document.getGraph());
      classify(document);
      addDocumentRoles(document);
      for (TTEntity c:inferred.getEntities()){
         if (c.isType(OWL.CLASS)){
            TTArray types= c.getType();
            List<TTValue> oldTypes= types.getElements();
            oldTypes.remove(OWL.CLASS);
            oldTypes.add(RDFS.CLASS);
            c.setType(new TTArray());
            for (TTValue type:oldTypes)
               c.getType().add(type);
         }
         simplifyDomains(c);
         reformChains(c);
         if (c.isType(OWL.OBJECTPROPERTY))
            c.setType(new TTArray().add(RDF.PROPERTY));
         if (c.isType(OWL.DATATYPEPROPERTY))
            c.setType(new TTArray().add(RDF.PROPERTY));
         if (c.get(IM.IS_A)!=null) {
            if (c.isType(RDF.PROPERTY)) {
               c.set(RDFS.SUBPROPERTYOF, c.get(IM.IS_A));
            } else {
               c.set(RDFS.SUBCLASSOF, c.get(IM.IS_A));
            }
            c.getPredicateMap().remove(IM.IS_A);
         }
         c.getPredicateMap().remove(OWL.EQUIVALENTCLASS);
         c.getPredicateMap().remove(OWL.PROPERTYCHAIN);
      }

      return inferred;
   }


   private void reformChains(TTEntity entity) {
         if (entity.get(OWL.PROPERTYCHAIN)!=null){
            int i=1;
            TTNode node=entity;
            for (TTValue property:entity.get(OWL.PROPERTYCHAIN).iterator()){
               if (i<entity.get(OWL.PROPERTYCHAIN).size()){
                  node.set(property.asIriRef(),new TTNode());
                  node= node.get(property.asIriRef()).asNode();
                  i++;
               } else
                  node.set(property.asIriRef(),IM.CONCEPT);
            }
         }
      }


   private void simplifyDomains(TTEntity entity) {

         if (entity.get(RDFS.DOMAIN)!=null){
            TTArray newDomains= new TTArray();
            for (TTValue oldDomain: entity.get(RDFS.DOMAIN).iterator()){
               if (oldDomain.isIriRef()) {
                  newDomains.add(oldDomain);
               } else if (oldDomain.isNode() && oldDomain.asNode().get(OWL.UNIONOF) != null){
                  for (TTValue subDomain: oldDomain.asNode().get(OWL.UNIONOF).iterator()){
                     if (!subDomain.isIriRef()) {
                        LOG.debug("Sub domains and ranges must be iris");
                     } else {
                        newDomains.add(subDomain);
                     }
                  }
               }
            }
            entity.set(RDFS.DOMAIN,newDomains);
         }
   }


   private void addDocumentRoles(TTDocument document) throws DataFormatException {
      if (inferred.getEntities() == null)
         return;
      for (TTEntity entity:inferred.getEntities()) {
         addEntityRoles(entity);
      }
   }

   private void addEntityRoles(TTEntity entity) throws DataFormatException {
      if (entity.get(RDFS.SUBCLASSOF) != null) {
         for (TTValue superClass : entity.get(RDFS.SUBCLASSOF).iterator()) {
            if (!superClass.isIriRef()) {
               addExpression(entity, superClass);
            }
         }
      } else if (entity.get(OWL.EQUIVALENTCLASS) != null) {
         for (TTValue superClass : entity.get(OWL.EQUIVALENTCLASS).iterator()) {
            if (!superClass.isIriRef()) {
               addExpression(entity, superClass);
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
           node.addObject(IM.IS_A, expression);
       } else if (expression.isNode()) {
           if (expression.asNode().get(OWL.INTERSECTIONOF) != null) {
               for (TTValue subExp : expression.asNode().get(OWL.INTERSECTIONOF).iterator()) {
                   if (subExp.isNode()) {
                       if (subExp.asNode().get(OWL.ONPROPERTY) != null) {
                           addRole(node, subExp.asNode());
                       } else
                           addExpression(node, subExp);
                   } else if (subExp.isIriRef() && !node.get(IM.IS_A).contains(subExp) && !(node instanceof  TTEntity)) {
                      node.addObject(IM.IS_A, subExp);
                   }
               }
           } else if (expression.asNode().get(OWL.UNIONOF) != null) {
               node.set(SHACL.OR, new TTArray());
               TTNode union = new TTNode();
               node.addObject(SHACL.OR, union);
               addExpression(union, expression.asNode().get(OWL.UNIONOF));
           } else if (expression.asNode().get(OWL.ONPROPERTY) != null) {
               addRole(node, expression.asNode());
           } else
               LOG.debug("Only one level of nesting supported. ");
       } else
           throw new DataFormatException("Unrecognised owl expression format");
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
               node.addObject(IM.IS_A,subExp);
            else
               addRole(node,subExp.asNode());
            }
         }
      if (subRole.asNode().get(OWL.UNIONOF) != null) {
         node.set(SHACL.OR,new TTArray());
         for (TTValue subExp : subRole.get(OWL.UNIONOF).iterator()) {
            if (subExp.isIriRef())
               node.addObject(IM.IS_A,subExp);
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
      this.document = document;
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
      if (owlOntology.isPresent()) {
         OWLReasonerConfiguration config = new SimpleConfiguration();
         OWLOntology o = owlOntology.get();
         owlReasoner = new FaCTPlusPlusReasonerFactory().createReasoner(o, config);
         owlReasoner.precomputeInferences();
         if (!owlReasoner.isConsistent()) {
            consistent = false;
            return null;
         }
         consistent = true;
         OWLDataFactory dataFactory = new OWLDataFactoryImpl();
         for (TTEntity c : document.getEntities()) {
            inferred.addEntity(c);
            c.getPredicateMap().remove(RDFS.SUBCLASSOF);
            c.getPredicateMap().remove(RDFS.SUBPROPERTYOF);
            if (c.get(OWL.EQUIVALENTCLASS)!=null)
               c.set(IM.DEFINITIONAL_STATUS,IM.SUFFICIENTLY_DEFINED);
            if (c.isType(OWL.OBJECTPROPERTY)) {
               OWLObjectPropertyExpression ope = dataFactory.getOWLObjectProperty(IRI.create(c.getIri()));
               NodeSet<OWLObjectPropertyExpression> superOb = owlReasoner.getSuperObjectProperties(ope, true);
               if (superOb != null) {
                  superOb.forEach(sob -> {
                     if (!sob.getRepresentativeElement().isAnonymous()) {
                        if (!sob.getRepresentativeElement().asOWLObjectProperty()
                                .getIRI()
                                .toString().equals(OWL.NAMESPACE + "topObjectProperty")) {
                           addIsa(c, TTIriRef
                                   .iri(sob
                                           .getRepresentativeElement().asOWLObjectProperty()
                                           .getIRI()
                                           .toString()));
                        } else {
                           addIsa(c, RDF.PROPERTY);
                        }
                     }
                  });
               }
            }
            else if (c.isType(RDF.PROPERTY) || (c.isType(OWL.DATATYPEPROPERTY))) {
               OWLDataProperty dpe = dataFactory.getOWLDataProperty(IRI.create(c.getIri()));
               NodeSet<OWLDataProperty> superP = owlReasoner.getSuperDataProperties(dpe, true);
               if (superP != null) {
                  superP.forEach(sob -> {
                     if (!sob.getRepresentativeElement().isAnonymous()) {
                        if (!sob.getRepresentativeElement().asOWLDataProperty()
                                .getIRI()
                                .toString().equals(OWL.NAMESPACE + "topDataProperty")) {
                           addIsa(c, TTIriRef
                                   .iri(sob
                                           .getRepresentativeElement().asOWLDataProperty()
                                           .getIRI()
                                           .toString()));
                        } else {
                           addIsa(c, RDF.PROPERTY);
                        }
                     }
                  });
               }
            } else {
                  OWLClassExpression owlClass = dataFactory.getOWLClass(IRI.create(c.getIri()));
                  NodeSet<OWLClass> superClasses = owlReasoner.getSuperClasses(owlClass, true);
                  if (superClasses != null) {
                     superClasses.forEach(sup -> {TTIriRef iri= TTIriRef.iri(sup.getRepresentativeElement()
                         .asOWLClass()
                         .getIRI()
                         .toString());
                     if (!iri.equals(OWL.THING))
                        addIsa(c,iri);}
                     );
                  }
                  Node<OWLClass> equClasses= owlReasoner.getEquivalentClasses(owlClass);
                  if (equClasses!=null){
                     equClasses.forEach(sup -> {if (sup.isOWLClass()){
                        TTIriRef superIri= TTIriRef.iri(sup.getIRI().toString());
                        if (!superIri.equals(TTIriRef.iri(c.getIri())))
                           addIsa(c,superIri);}
                     });
                  }

               }

            }
         }
      return document;
   }
   private void addIsa(TTEntity entity,TTIriRef parent){
      if (entity.get(IM.IS_A)==null)
         entity.set(IM.IS_A,new TTArray());
      entity.get(IM.IS_A).add(parent);
   }

}
