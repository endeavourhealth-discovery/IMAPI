package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Classifies an ontology using an owl reasoner, generating ISA relationships from a Discovery ontology document.
 * Generates inferred role groups (Snomed pattern) from the existential quntifiers and propogates them to subclasses
 * Generatesi inferred Property groups from the SHACL property definitions and propogates them to subclasses
 */
public class ReasonerPlus {
   private TTDocument document;
   private TTDocument inferred;
   private HashMap<String, TTEntity> entityMap;
   private boolean consistent;
   private OWLReasoner owlReasoner;
   private TTManager manager;
   private Set<String> done;

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
      generatePropertyGroups(document);
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
         if (c.isType(OWL.OBJECTPROPERTY))
            c.setType(new TTArray().add(RDF.PROPERTY));
         if (c.isType(OWL.DATATYPEPROPERTY))
            c.setType(new TTArray().add(RDF.PROPERTY));
         c.getPredicateMap().remove(RDFS.SUBCLASSOF);
         c.getPredicateMap().remove(RDFS.SUBPROPERTYOF);
         c.getPredicateMap().remove(OWL.EQUIVALENTCLASS);
      }

      return inferred;
   }





   private void generatePropertyGroups(TTDocument document) throws DataFormatException {
      if (inferred.getEntities() == null)
         return;
      for (TTEntity entity:inferred.getEntities()) {
         setPropertyGroups(entity);


      }
   }



   private void setPropertyGroups(TTEntity entity) throws DataFormatException {
      if (entity.get(IM.PROPERTY_GROUP)==null) {
         if (entity.get(RDFS.SUBCLASSOF) != null) {
            for (TTValue superClass : entity.get(RDFS.SUBCLASSOF).asArray().getElements()) {
               setExpression(entity, superClass);

            }

         }
         if (entity.get(OWL.EQUIVALENTCLASS) != null) {
            if (entity.get(OWL.EQUIVALENTCLASS).isList()) {
               for (TTValue equClass : entity.get(OWL.EQUIVALENTCLASS).asArray().getElements()) {
                  setExpression(entity, equClass);
               }
            }
         }
      }
   }

   private void setExpression(TTEntity entity, TTValue exp) throws DataFormatException {
      if (exp.isIriRef())
         return;
      if (exp.asNode().get(OWL.INTERSECTIONOF) != null) {
         for (TTValue subExp : exp.asNode().get(OWL.INTERSECTIONOF).asArray().getElements()) {
            setExpression(entity, subExp);
         }
      } else if (exp.asNode().get(OWL.UNIONOF)!=null) {
         for (TTValue subExp : exp.asNode().get(OWL.UNIONOF).asArray().getElements()) {
            setExpression(entity, subExp);
         }
      }else
         addRole(entity,exp.asNode());
   }

   private void addRole(TTEntity entity, TTNode attribute) throws DataFormatException {


      TTValue roleGroups= entity.get(IM.ROLE_GROUP);
      if (roleGroups==null) {
         roleGroups = new TTArray();
         entity.set(IM.PROPERTY_GROUP, roleGroups);
         TTNode roleGroup= new TTNode();
         roleGroups.asArray().add(roleGroup);
         roleGroup.set(SHACL.PROPERTY,new TTArray());
      }
      TTNode property= new TTNode();
      TTNode roleGroup= roleGroups.asArray().getElements().stream().findFirst().get().asNode();
      roleGroup.get(SHACL.PROPERTY).asArray().add(property);
      if (attribute.get(OWL.ONPROPERTY) != null) {
         TTIriRef path= attribute.get(OWL.ONPROPERTY).asIriRef();
         property.set(SHACL.PATH,path);
         if (attribute.get(OWL.ONCLASS) != null)
            property.set(SHACL.CLASS,attribute.get(OWL.ONCLASS));
         else if (attribute.get(OWL.SOMEVALUESFROM) != null)
               property.set(SHACL.CLASS,attribute.get(OWL.SOMEVALUESFROM));
         else if (attribute.get(OWL.ALLVALUESFROM) != null)
               property.set(SHACL.CLASS,attribute.get(OWL.ALLVALUESFROM));
         else if (attribute.get(OWL.ONDATATYPE) != null)

               roleGroup.set(attribute.get(OWL.ONPROPERTY).asIriRef(),attribute.get(OWL.ONDATATYPE));
         else if (attribute.get(OWL.HASVALUE) != null)
               roleGroup.set(attribute.get(OWL.ONPROPERTY).asIriRef(),attribute.get(OWL.HASVALUE));
            else
               throw new DataFormatException("unknown property construct");
         } else
            if (attribute.get(OWL.WITHRESTRICTIONS)==null)
               throw new DataFormatException("unknown class expression format");
   }



   /**
    * Classifies an ontology using an OWL Reasoner
    *
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
            if (c.get(OWL.EQUIVALENTCLASS)!=null)
               c.set(IM.DEFINITIONAL_STATUS,IM.SUFFICIENTLY_DEFINED);
            if (c.isType(OWL.OBJECTPROPERTY)) {
               OWLObjectPropertyExpression ope = dataFactory.getOWLObjectProperty(IRI.create(c.getIri()));
               NodeSet<OWLObjectPropertyExpression> superOb = owlReasoner.getSuperObjectProperties(ope, true);
               if (superOb != null) {
                  TTArray parents = new TTArray();
                  c.set(IM.IS_A, parents);
                  superOb.forEach(sob -> {
                     if (!sob.getRepresentativeElement().isAnonymous())
                        parents.add(TTIriRef
                       .iri(sob.getRepresentativeElement()
                         .asOWLObjectProperty()
                         .getIRI()
                         .toString()));});
                  }
            } else if (c.isType(RDF.PROPERTY)|(c.isType(OWL.DATATYPEPROPERTY))) {
               OWLDataProperty dpe = dataFactory.getOWLDataProperty(IRI.create(c.getIri()));
               NodeSet<OWLDataProperty> superP = owlReasoner.getSuperDataProperties(dpe, true);
               if (superP != null) {
                  TTArray parents = new TTArray();
                  c.set(IM.IS_A, parents);
                  superP.forEach(sob -> {
                     if (!sob.getRepresentativeElement().isAnonymous())
                        parents.add(TTIriRef
                       .iri(sob
                         .getRepresentativeElement().asOWLDataProperty()
                         .getIRI()
                         .toString()));});
                  };
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
                           ;});
                  }

               }

            }
         }
      return document;
   }
   private void addIsa(TTEntity entity,TTIriRef parent){
      if (entity.get(IM.IS_A)==null)
         entity.set(IM.IS_A,new TTArray());
      entity.get(IM.IS_A).asArray().add(parent);
   }

}
