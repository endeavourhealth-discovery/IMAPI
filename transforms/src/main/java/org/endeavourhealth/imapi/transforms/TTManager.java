package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.*;
import java.util.*;


import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * Various utility functions to support triple tree entities and documents.
 * Create document creates a document with default common prefixes.
 */
public class TTManager {
   private Map<String, TTEntity> entityMap;
   private Map<String, TTEntity> nameMap;
   private TTDocument document;
   private TTContext context;
   private Set<TTIriRef> templatedPredicates;

   public enum Grammar {JSON,TURTLE}

   public TTManager() {
      createDefaultContext();
   }

   public TTManager(TTDocument document) {
      createDefaultContext();
      this.document = document;
   }

   public TTDocument createDocument(String graph) {
      createDefaultContext();
      document = new TTDocument();
      document.setContext(context);
      document.setGraph(TTIriRef.iri(graph));
      return document;
   }
   public TTDocument createDocument() {
      TTContext context= new TTContext();
      context.add(IM.NAMESPACE,"im");
      context.add(RDF.NAMESPACE, "rdf","RDF namespace");
      context.add(RDFS.NAMESPACE, "rdfs","RDFS namespace");
      document = new TTDocument();
      document.setContext(context);
      document.setGraph(TTIriRef.iri(IM.GRAPH_DISCOVERY.getIri()));
      return document;
   }

   /**
    * Gets a entity from an iri or null if not found
    *
    * @param searchKey the iri or name of the entity you are looking for
    * @return entity, which may be a subtype that may be downcasted
    */
   public TTEntity getEntity(String searchKey) {
      if (entityMap == null)
         createIndex();
      TTEntity result = entityMap.get(searchKey);
      if (result != null)
         return result;
      else {
         if (searchKey.contains(":")) {
            result = entityMap.get(expand(searchKey));
            if (result != null)
               return result;
         }

         return nameMap.get(searchKey.toLowerCase());
      }
   }



   public TTContext createDefaultContext() {
      context = new TTContext();
      context.add(IM.NAMESPACE, "im","Discovery namespace");
      context.add(SNOMED.NAMESPACE, "sn","Snomed-CT namespace");
      context.add(OWL.NAMESPACE, "owl","OWL2 namespace");
      context.add(RDF.NAMESPACE, "rdf","RDF namespace");
      context.add(RDFS.NAMESPACE, "rdfs","RDFS namespace");
      context.add(XSD.NAMESPACE, "xsd","xsd namespace");
      context.add("http://endhealth.info/icd10#", "icd10","ICD10 namespace");
      context.add("http://endhealth.info/opcs4#", "opcs4","OPCS4 namespace");
      context.add("http://endhealth.info/emis#", "emis","EMIS (inc. Read2 like) namespace");
      context.add("http://endhealth.info/tpp#", "tpp","TPP (inc.CTV3) namespace");
      context.add("http://endhealth.info/bc#", "bc","Barts Cerner namespace");
      context.add(SHACL.NAMESPACE, "sh","SHACL namespace");
      context.add("http://www.w3.org/ns/prov#", "prov","PROV namespace");
      context.add("http://endhealth.info/reports#","reports","IM internal reports");
      context.add("https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-OrganizationRole-1#", "orole","OPS roles namespace");
      context.add("http://endhealth.info/ods#","ods","ODS code scheme");
      context.add("http://endhealth.info/prsb#","prsb","PRSB namespace");
      context.add("http://endhealth.info/kchwinpath#","kchwinpath","KCH Winpath codes");
      context.add("http://endhealth.info/kchapex#","kchapex","KCH Apex codes");
      context.add("http://endhealth.info/ceg16#","ceg13","CEG ethnicity 16+ category");
      context.add("http://endhealth.info/nhsethnic2001#","nhse2001","NHS Ethnicitity categories 2001 census");
      context.add("http://endhealth.info/vision#", "vis","Vision (incl. Read2) namespace");
      return context;
   }

   /**
    * Loads an information model document file in  JSON-LD/RDF syntax
    *
    * @param inputFile the file name to load
    * @return the IM triple tree document
    * @throws IOException covering file format exceptions and content exceptions of various kinds
    */
   public TTDocument loadDocument(File inputFile) throws IOException {
      ObjectMapper objectMapper = new ObjectMapper();
      document = objectMapper.readValue(inputFile, TTDocument.class);
      return document;
   }

   public TTDocument loadDocument(String json) throws IOException {
      ObjectMapper objectMapper = new ObjectMapper();
      document = objectMapper.readValue(json, TTDocument.class);
      return document;

   }


   /**
    * Saves an OWL ontology in functional syntax format
    *
    * @param manager    OWL ontology manager with at least one ontology
    * @param outputFile output fle name
    * @throws IOException in the event of an IO file creation failure
    */

   public void saveOWLOntology(OWLOntologyManager manager, File outputFile) throws IOException {
      manager.getOntologies().forEach(o -> {
         try {
            OWLDocumentFormat format = manager.getOntologyFormat(o);
            format.setAddMissingTypes(false);
            o.saveOntology(format, new FileOutputStream(outputFile));

         } catch (IOException | OWLOntologyStorageException e) {
            e.printStackTrace();
         }

      });
   }


   /**
    * Indexes the entities held in the manager's TTDocument document so they can be quicly retrieced via their IRI.
    */
   public void createIndex() {
      entityMap = new HashMap();
      nameMap = new HashMap();

      //Loops through the 3 main entity types and add them to the IRI map
      //Note that an IRI may be both a class and a property so both are added
      if (document.getEntities() != null)
         document.getEntities().forEach(p -> {
            entityMap.put(p.getIri(), p);
            if (p.getName() != null)
               nameMap.put(p.getName().toLowerCase(), p);
         });
   }

   /**
    * Expands a prefixed iri string to a full iri
    *
    * @param iri
    * @return
    */
   public String expand(String iri) {
      if (context == null)
         context = createDefaultContext();
      if (iri == null)
         return null;
      return context.expand(iri);
   }

   public TTDocument getDocument() {
      return document;
   }

   public TTManager setDocument(TTDocument document) {
      this.document = document;
      return this;
   }

   /**
    * Saves the Discovery ontology held by the manager
    * @param outputFile file to save ontology to
    * @throws JsonProcessingException if deserialization fails
    */
   public void saveDocument(File outputFile) throws JsonProcessingException {
      if (document == null)
         throw new NullPointerException("Manager has no ontology document assigned");
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      String json = objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
         writer.write(json);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * Saves the Discovery TTDocument held by the manager
    * @param document the document to save.
    * @param outputFile file name to save ontology to
    * @param grammar    language to output in
    * @throws JsonProcessingException if deserialization fails
    */
   public static void saveDocument(TTDocument document, String outputFile,Grammar grammar) throws JsonProcessingException {
      String outputString;
      if (grammar== Grammar.JSON) {
         ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
         objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
         objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
         outputString = objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);
      }
      else {
         TTToTurtle converter= new TTToTurtle();
         outputString= converter.transformDocument(document);
      }
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
         writer.write(outputString);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public TTEntity createGraph(String iri,String name, String description){
      TTEntity graph= new TTEntity()
        .setIri(iri)
        .addType(RDFS.RESOURCE)
        .setName(name)
        .setDescription(description)
        .addType(IM.GRAPH);
      graph.addObject(RDFS.SUBCLASSOF,IM.GRAPH);
      return graph;
   }


   public void saveTurtleDocument(File outputFile) {
      TTToTurtle converter= new TTToTurtle();
     String ttl= converter.transformDocument(getDocument());
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
         writer.write(ttl);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }


   /**
    * Returns a string of JSON from a TTDocument instance
    *
    * @param document the TTDocument holding the ontology
    * @return the json serialization of the document
    */
   public String getJson(TTDocument document) throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      return objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);
   }
   /**
    * Returns a string of JSON from a TTEntity instance
    *
    * @param entity the TTEntity holding the entity
    * @return the json serialization of the document
    * @throws JsonProcessingException
    */
   public String getJson(TTEntity entity) throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      return objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true)
        .writeValueAsString(entity);
   }

   public TTDocument replaceIri(TTDocument document, TTIriRef from, TTIriRef to) {
      if (document.getEntities() != null) {
         for (TTEntity entity : document.getEntities()) {
            if (entity.getIri().equals(from.getIri()))
               entity.setIri(to.getIri());
            boolean replacedPredicate = true;
            while (replacedPredicate) {
               replacedPredicate = replaceNode(entity, from, to);
            }
         }
      }

      return document;

   }

   private boolean replaceNode(TTNode node, TTIriRef from, TTIriRef to) {
      boolean replaced = false;
      if (node.get(from) != null) {
         node.set(to, node.get(from));
         node.getPredicateMap().remove(from);
         return true;
      }
      if (node.getPredicateMap() != null) {
         HashMap<TTIriRef, TTValue> newPredicates = new HashMap<>();
         for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
             TTIriRef predicate = entry.getKey();
             TTArray value = entry.getValue();

             List<TTValue> toRemove = new ArrayList<>();
             for (TTValue arrayValue : value.iterator()) {
                 if (arrayValue.isIriRef()) {
                     if (arrayValue.asIriRef().equals(from)) {
                         toRemove.add(arrayValue);
                     }
                 } else if (arrayValue.isNode()) {
                     replaced = replaceNode(arrayValue.asNode(), from, to);
                 }
             }
             if (!toRemove.isEmpty()) {
                 for (TTValue remove : toRemove) {
                     value.remove(remove);
                 }
                 value.add(to);
             }
         }
         if (!newPredicates.isEmpty()) {
            for (Map.Entry<TTIriRef, TTValue> entry : newPredicates.entrySet()) {
               node.getPredicateMap().remove(entry.getKey());
               node.set(entry.getKey(), entry.getValue());
            }
         }
      }
      return false;
   }

   private TTArray replaceArray(TTArray array, TTIriRef from, TTIriRef to) {
      TTArray newArray = new TTArray();
      for (TTValue value : array.iterator()) {
         if (value.isIriRef()) {
            if (value.asIriRef().equals(from))
               newArray.add(to);
            else
               newArray.add(value);
         } else {
            newArray.add(value);
            if (value.isNode()) {
               replaceNode(value.asNode(), from, to);
            } else {
               newArray.add(value);
            }
         }
      }
      return newArray;
   }

   /**
    * Tests whether a entity is a descendant of an ancestor, entity test against iri
    * uses standard prefixes in this version
    *
    * @param descendant the descendant entity
    * @param ancestor   the ancestor IRI
    * @return true if found false if not a descendant
    */
   public boolean isA(TTEntity descendant, TTIriRef ancestor) {
      Set<TTIriRef> done = new HashSet<>();
      if (entityMap == null)
         createIndex();
      if (entityMap.get(ancestor.getIri()) == null)
         throw new NoSuchElementException("ancestor not found in this module");
      return isA1(descendant, ancestor, done);
   }

   /**
    * tests isa relationship between two iris. Isa rerlationships must have previosuly been inferred.
    * This is not an entailment test using DL reasoning
    *
    * @param descendant the subtype that is being tested
    * @param ancestor   the supertype that is being tested against
    * @return true if descendent is a subtype of supertype
    */
   public boolean isA(TTIriRef descendant, TTIriRef ancestor) {
      if (descendant.equals(ancestor))
         return true;
      Set<TTIriRef> done = new HashSet<>();
      if (entityMap == null)
         createIndex();
      TTEntity descendantEntity = entityMap.get(descendant.getIri());
      if (descendantEntity == null)
         return false;
      if (entityMap.get(ancestor.getIri()) == null)
         return false;
      return isA1(descendantEntity, ancestor, done);
   }

   private boolean isA1(TTEntity descendant, TTIriRef ancestor, Set<TTIriRef> done) {
      if (TTIriRef.iri(descendant.getIri()).equals(ancestor))
         return true;
      TTIriRef subType= descendant.isType(RDF.PROPERTY) ? RDFS.SUBPROPERTYOF : RDFS.SUBCLASSOF;
      boolean isa = false;
      if (descendant.get(subType) != null)
         for (TTValue ref : descendant.get(subType).iterator())
            if (ref.equals(ancestor))
               return true;
            else {
               TTIriRef parent = ref.asIriRef();
               if (!done.contains(parent)) {
                  done.add(parent);
                  TTEntity parentEntity = entityMap.get(parent.getIri());
                  if (parentEntity != null)
                     isa = isA1(parentEntity, ancestor, done);
                  if (isa)
                     return true;
               }
            }
      return false;
   }


   public static TTEntity createInstance(TTIriRef iri,TTIriRef crud){
      TTEntity result= new TTEntity();
      result.setIri(iri.getIri());
      result.setCrud(crud);
      return result;
   }

   public static void addChildOf(TTEntity c, TTIriRef parent) {
      if (c.get(IM.IS_CHILD_OF) == null)
         c.set(IM.IS_CHILD_OF, new TTArray());
      c.get(IM.IS_CHILD_OF).add(parent);
   }

   public static void addSuperClass(TTEntity entity, TTIriRef andOr,TTValue superClass) {
      addESAxiom(entity,RDFS.SUBCLASSOF,andOr,superClass);

   }


   private static void addESAxiom(TTEntity entity, TTIriRef axiom,
                                  TTIriRef andOr, TTValue newExpression) {
      TTIriRef subType= entity.isType(RDF.PROPERTY) ? RDFS.SUBPROPERTYOF : RDFS.SUBCLASSOF;
      if (entity.get(axiom) == null)
         entity.set(axiom, new TTArray());
      TTValue oldExpression;
      TTArray expressions = entity.get(axiom);
      if (expressions.size() > 0) {
         oldExpression = expressions.getElements().get(0);
         if (oldExpression.isIriRef()||oldExpression.isNode()) {
            TTNode intersection = new TTNode();
            intersection.set(andOr, new TTArray());
            intersection.get(andOr).add(oldExpression);
            intersection.get(andOr).add(newExpression);
            expressions.add(intersection);
         } else
            oldExpression.asNode().get(andOr).add(newExpression);
      } else
         expressions.add(newExpression);
      if (newExpression.isIriRef()){
         if (entity.get(subType)==null)
            entity.set(subType, new TTArray());
         entity.addObject(subType,newExpression);
      }

   }


   public static void addSimpleMap(TTEntity c,String target) {
      c.addObject(IM.MATCHED_TO,iri(target));
   }
   public static TTNode addComplexMap(TTEntity c){
      TTNode map= new TTNode();
      c.addObject(IM.HAS_MAP,map);
      return map;
   }






   public static TTEntity createTermCode(TTIriRef iri,TTIriRef crud,
                                              String term,String code){
      TTEntity result= createInstance(iri,crud);
      addTermCode(result,term,code);
      return result;
   }



   public static TTEntity addTermCode(TTEntity entity,
                                       String term,String code){
      TTNode termCode= new TTNode();
      termCode.set(RDFS.LABEL,TTLiteral.literal(term));
      if (code!=null)
         termCode.set(IM.CODE,TTLiteral.literal(code));
      entity.addObject(IM.HAS_TERM_CODE,termCode);
      return entity;
   }

   public TTContext getContext() {
      return context;
   }
}
