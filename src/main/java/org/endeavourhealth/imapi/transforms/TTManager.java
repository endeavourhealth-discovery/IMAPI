package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * Various utility functions to support triple tree entities and documents.
 * Create document creates a document with default common prefixes.
 */
@Slf4j
public class TTManager implements AutoCloseable {
  private static final TTIriRef[] jsonPredicates = {iri(IM.HAS_MAP)};
  private Map<String, TTEntity> entityMap;
  private Map<String, TTEntity> nameMap;
  private TTDocument document;
  private ModelDocument modelDocument;
  private TTContext context;

  public TTManager() {
    createDefaultContext();
  }

  public TTManager(TTDocument document) {
    createDefaultContext();
    this.document = document;
  }

  public static TTContext createBasicContext() {
    TTContext context = new TTContext();
    context.add(Namespace.IM, "im", "Discovery namespace");
    context.add(Namespace.SNOMED, "sn", "Snomed-CT namespace");
    context.add(Namespace.OWL, "owl", "OWL2 namespace");
    context.add(Namespace.RDF, "rdf", "RDF namespace");
    context.add(Namespace.RDFS, "rdfs", "RDFS namespace");
    context.add(Namespace.XSD, "xsd", "xsd namespace");
    context.add(Namespace.SHACL, "sh", "SHACL namespace");
    return context;
  }

  /**
   * Saves the Discovery TTDocument held by the manager
   *
   * @param document   the document to save.
   * @param outputFile file name to save ontology to
   * @param grammar    language to output in
   * @throws JsonProcessingException if deserialization fails
   */
  public static void saveDocument(TTDocument document, String outputFile, Grammar grammar) throws JsonProcessingException {
    String outputString;
    if (grammar == Grammar.JSON) {
      try (CachedObjectMapper om = new CachedObjectMapper()) {
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        outputString = om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);
      }
    } else {
      TTToTurtle converter = new TTToTurtle();
      outputString = converter.transformDocument(document);
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {
      writer.write(outputString);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public static TTEntity createInstance(TTIriRef iri, TTIriRef crud) {
    TTEntity result = new TTEntity();
    result.setIri(iri.getIri());
    result.setCrud(crud);
    return result;
  }

  public static void addChildOf(TTEntity c, TTIriRef parent) {
    if (c.get(iri(IM.IS_CHILD_OF)) == null)
      c.set(iri(IM.IS_CHILD_OF), new TTArray());
    c.get(iri(IM.IS_CHILD_OF)).add(parent);
  }

  public static void addSuperClass(TTEntity entity, TTIriRef andOr, TTValue superClass) {
    addESAxiom(entity, iri(RDFS.SUBCLASS_OF), andOr, superClass);

  }

  private static void addESAxiom(TTEntity entity, TTIriRef axiom,
                                 TTIriRef andOr, TTValue newExpression) {
    TTIriRef subType = entity.isType(iri(RDF.PROPERTY)) ? iri(RDFS.SUB_PROPERTY_OF) : iri(RDFS.SUBCLASS_OF);
    if (entity.get(axiom) == null)
      entity.set(axiom, new TTArray());
    TTValue oldExpression;
    TTArray expressions = entity.get(axiom);
    if (!expressions.isEmpty()) {
      oldExpression = expressions.getElements().getFirst();
      if (oldExpression.isIriRef() || oldExpression.isNode()) {
        TTNode intersection = new TTNode();
        intersection.set(andOr, new TTArray());
        intersection.get(andOr).add(oldExpression);
        intersection.get(andOr).add(newExpression);
        expressions.add(intersection);
      } else
        oldExpression.asNode().get(andOr).add(newExpression);
    } else
      expressions.add(newExpression);
    if (newExpression.isIriRef()) {
      if (entity.get(subType) == null)
        entity.set(subType, new TTArray());
      entity.addObject(subType, newExpression);
    }

  }

  public static void addSimpleMap(TTEntity c, String target) {
    c.addObject(iri(IM.MATCHED_TO), iri(target));
  }

  public static TTNode addComplexMap(TTEntity c) {
    TTNode map = new TTNode();
    c.addObject(iri(IM.HAS_MAP), map);
    return map;
  }

  public static TTEntity createTermCode(TTIriRef iri, TTIriRef crud,
                                        String term, String code) {
    TTEntity result = createInstance(iri, crud);
    addTermCode(result, term, code);
    return result;
  }

  public static boolean termUsed(TTEntity entity, String term) {
    if (entity.get(iri(IM.HAS_TERM_CODE)) != null) {
      for (TTValue val : entity.get(iri(IM.HAS_TERM_CODE)).getElements()) {
        if (val.asNode().get(iri(RDFS.LABEL)) != null && val.asNode().get(iri(RDFS.LABEL)).asLiteral().getValue().equals(term))
          return true;
      }
    }
    return false;
  }
  public static boolean termCodeUsed(TTEntity entity, String term,String code) {
    if (entity.get(iri(IM.HAS_TERM_CODE)) != null) {
      for (TTValue val : entity.get(iri(IM.HAS_TERM_CODE)).getElements()) {
        if (val.asNode().get(iri(RDFS.LABEL)) != null && val.asNode().get(iri(RDFS.LABEL)).asLiteral().getValue().equals(term)){
          if (code!=null){
            if (val.asNode().get(iri(IM.CODE)) != null && val.asNode().get(iri(IM.CODE)).asLiteral().getValue().equals(code)) {
              return true;
            }
          }
          else return true;
        }
      }
    }
    return false;
  }

  public static TTEntity addTermCode(TTEntity entity,
                                     String term, String code) {
    return addTermCode(entity, term, code, null);
  }

  public static TTEntity addTermCode(TTEntity entity,
                                     String term, String code, TTIriRef status) {
    if (!termCodeUsed(entity, term,code)) {
      TTNode termCode = new TTNode();
      if (status != null)
        termCode.set(iri(IM.HAS_STATUS), status);
      if (term != null) {
        termCode.set(iri(RDFS.LABEL), TTLiteral.literal(term));
      }
      if (code != null)
        termCode.set(iri(IM.CODE), TTLiteral.literal(code));
      entity.addObject(iri(IM.HAS_TERM_CODE), termCode);
    }
    return entity;
  }

  /**
   * Wraps a predicates object node into a json literal
   *
   * @param node the node whose predicate needs wrapping
   * @return the node wrapped
   * @throws JsonProcessingException when serialization problem with the ttnode
   */
  public static TTNode wrapRDFAsJson(TTNode node) throws JsonProcessingException {
    for (TTIriRef predicate : jsonPredicates) {
      if (node.get(predicate) != null) {
        TTArray jsons = new TTArray();
        try (CachedObjectMapper om = new CachedObjectMapper()) {
          om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
          om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
          om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
          for (TTValue value : node.get(predicate).getElements()) {
            String json = om.writeValueAsString(value.asNode());
            jsons.add(TTLiteral.literal(json));
          }
          node.set(predicate, jsons);
        }
      }
    }
    return node;
  }

  /**
   * Converts the object value literal representation of a node into a TTNode
   *
   * @param node the node or entity containing the predicate with the json data
   * @return the updated entity or node as full RDF
   * @throws IOException when problem with json literal
   */
  public static boolean unwrapRDFfromJson(TTNode node) throws IOException {
    boolean unwrapped = false;
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      for (TTIriRef predicate : jsonPredicates) {
        if (node.get(predicate) != null) {
          if (node.get(predicate).isLiteral()) {
            TTArray rdfNodes = new TTArray();
            for (TTValue value : node.get(predicate).getElements()) {
              rdfNodes.add(om.readValue(value.asLiteral().getValue(), TTNode.class));
            }
            node.set(predicate, rdfNodes);
            unwrapped = true;
          }
        }
      }
      return unwrapped;
    }
  }

  /**
   * Retrieves a set of IRIs from a node or array, including nested nodes
   *
   * @param node to retrieve the IRIs from
   * @return a set of iris
   */
  public static Set<TTIriRef> getIrisFromNode(TTNode node) {
    Set<TTIriRef> iris = new HashSet<>();
    return addToIrisFromNode(node, iris);
  }

  private static Set<TTIriRef> addToIrisFromNode(TTValue subject, Set<TTIriRef> iris) {
    if (subject.isIriRef() && (subject.asIriRef().getName() == null || subject.asIriRef().getName().isEmpty()))
      iris.add(subject.asIriRef());
    else if (subject.isNode() && subject.asNode().getPredicateMap() != null) {
      for (Map.Entry<TTIriRef, TTArray> entry : subject.asNode().getPredicateMap().entrySet()) {
        if (entry.getKey().getName() == null || entry.getKey().getName().isEmpty())
          iris.add(entry.getKey());
        for (TTValue v : entry.getValue().getElements()) {
          if (v.isIriRef() && (v.asIriRef().getName() == null || v.asIriRef().getName().isEmpty()))
            iris.add(v.asIriRef());
          else if (v.isNode())
            addToIrisFromNode(v, iris);
        }
      }
    }
    return iris;
  }

  /**
   * Populates a business object from an entity or node, the business object being a subclass of
   * a TTnode. Uses ontological properties and ranges to calculate the classes of the target
   * objects to populate
   *
   * @param source node containing the data
   * @param target node being the object to be populated
   * @param ranges A set of entities representing the properties and ranges used to calculate t target objects
   */
  public static void populateFromNode(TTNode source, TTNode target, Set<TTEntity> ranges) {
    Class<? extends TTNode> clazz = target.getClass();
    target.setPredicateMap(source.getPredicateMap());
  }

  public static TTContext getDefaultContext() {
    TTContext ctx = new TTContext();
    ctx.add(Namespace.IM, "");
    ctx.add(Namespace.IM, "im");
    ctx.add(Namespace.RDFS, "rdfs");
    ctx.add(Namespace.RDF, "rdf");
    ctx.add(Namespace.SNOMED, "sn");
    ctx.add(Namespace.SHACL, "sh");
    ctx.add(Namespace.XSD, "xsd");
    return ctx;
  }

  public ModelDocument getModelDocument() {
    return modelDocument;
  }

  public TTManager setModelDocument(ModelDocument modelDocument) {
    this.modelDocument = modelDocument;
    return this;
  }

  public TTDocument createDocument() {
    createDefaultContext();
    document = new TTDocument();
    document.setContext(context);
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
    context.add(Namespace.IM, "im", "Discovery namespace");
    context.add(Namespace.SNOMED, "sn", "Snomed-CT namespace");
    context.add(Namespace.OWL, "owl", "OWL2 namespace");
    context.add(Namespace.RDF, "rdf", "RDF namespace");
    context.add(Namespace.RDFS, "rdfs", "RDFS namespace");
    context.add(Namespace.XSD, "xsd", "xsd namespace");
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
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      document = om.readValue(inputFile, TTDocument.class);
      return document;
    }
  }

  public TTDocument loadDocument(String json) throws IOException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      document = om.readValue(json, TTDocument.class);
      return document;
    }
  }

  public ModelDocument loadModelDocument(File inputFile) throws IOException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      modelDocument = om.readValue(inputFile, ModelDocument.class);
      return modelDocument;

    }
  }

  /**
   * Saves an OWL ontology in functional syntax format
   *
   * @param manager    OWL ontology manager with at least one ontology
   * @param outputFile output fle name
   * @throws FileNotFoundException in the event of an IO file creation failure
   */

  public void saveOWLOntology(OWLOntologyManager manager, File outputFile) throws FileNotFoundException, OWLOntologyStorageException {
    for (OWLOntology ont : manager.getOntologies()) {
      OWLDocumentFormat format = manager.getOntologyFormat(ont);
      if (format != null) {
        format.setAddMissingTypes(false);
        ont.saveOntology(format, new FileOutputStream(outputFile));
      }
    }
  }

  /**
   * Indexes the entities held in the manager's TTDocument document so they can be quicly retrieced via their IRI.
   */
  public void createIndex() {
    entityMap = new HashMap<>();
    nameMap = new HashMap<>();

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
   * @param iri Iri to expand
   * @return Expanded iri, or the original iri if no expansion is required
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
   *
   * @param outputFile file to save ontology to
   * @throws JsonProcessingException if deserialization fails
   */
  public void saveDocument(File outputFile) throws JsonProcessingException {
    if (document == null)
      throw new NullPointerException("Manager has no ontology document assigned");
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      String json = om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {
        writer.write(json);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }

  public TTEntity createNamespaceEntity(Namespace namespace, String name, String description) {
    return createNamespaceEntity(namespace, name, description,null,false);

  }


  public TTEntity createNamespaceEntity(Namespace namespace, String name, String description,List<String> defaultTypes, boolean coreScheme) {
    TTEntity result = new TTEntity()
      .setIri(namespace.toString())
      .addType(RDFS.CLASS.asIri())
      .setName(name)
      .setDescription(description)
      .setScheme(namespace.asIri());
    if (defaultTypes!=null) {
      for (String defaultType : defaultTypes) {
        result.addObject(iri(IM.IS_CONTAINED_IN), iri(Namespace.IM + "DefaultSchemesFor"+ defaultType));
      }
    }
    if (coreScheme)
      result.addObject(iri(IM.IS_CONTAINED_IN), iri(Namespace.IM+"coreSchemes"));
    return result;
  }

  public void saveTurtleDocument(File outputFile) {
    TTToTurtle converter = new TTToTurtle();
    String ttl = converter.transformDocument(getDocument());
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
      writer.write(ttl);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

  }

  /**
   * Returns a string of JSON from a TTDocument instance
   *
   * @param document the TTDocument holding the ontology
   * @return the json serialization of the document
   */
  public String getJson(TTDocument document) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      return om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);
    }
  }

  /**
   * Returns a string of JSON from a TTEntity instance
   *
   * @param entity the TTEntity holding the entity
   * @return the json serialization of the document
   * @throws JsonProcessingException in on serialization failure
   */
  public String getJson(TTEntity entity) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      return om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true)
        .writeValueAsString(entity);
    }
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
    if (node.get(from) != null) {
      node.set(to, node.get(from));
      node.getPredicateMap().remove(from);
      return true;
    }
    if (node.getPredicateMap() != null) {
      for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
        replaceNodeValueChange(from, to, entry);
      }
    }
    return false;
  }

  private void replaceNodeValueChange(TTIriRef from, TTIriRef to, Map.Entry<TTIriRef, TTArray> entry) {
    TTArray value = entry.getValue();

    List<TTValue> toRemove = new ArrayList<>();
    for (TTValue arrayValue : value.iterator()) {
      if (arrayValue.isIriRef()) {
        if (arrayValue.asIriRef().equals(from)) {
          toRemove.add(arrayValue);
        }
      } else if (arrayValue.isNode()) {
        replaceNode(arrayValue.asNode(), from, to);
      }
    }
    if (!toRemove.isEmpty()) {
      for (TTValue remove : toRemove) {
        value.remove(remove);
      }
      value.add(to);
    }
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
    TTIriRef subType = descendant.isType(iri(RDF.PROPERTY)) ? iri(RDFS.SUB_PROPERTY_OF) : iri(RDFS.SUBCLASS_OF);
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

  public TTContext getContext() {
    return context;
  }

  @Override
  public void close() {
    if (entityMap != null) entityMap.clear();
    if (nameMap != null) nameMap.clear();
  }

  public enum Grammar {JSON, TURTLE}
}
