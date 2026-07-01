package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.extensions.TTIriRefExtensionsKt;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.*;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Various utility functions to support triple tree entities and documents.
 * Create document creates a document with default common prefixes.
 */
@Slf4j
public class TTManager implements AutoCloseable {
  private static final TTIriRef[] jsonPredicates = {TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_MAP)};
  private Map<String, TTEntityJava> entityMap;
  private Map<String, TTEntityJava> nameMap;
  private TTDocumentJava document;
  private ModelDocument modelDocument;
  private TTContextJava context;

  public TTManager() {
    createDefaultContext();
  }

  public TTManager(TTDocumentJava document) {
    createDefaultContext();
    this.document = document;
  }

  public static TTContextJava createBasicContext() {
    TTContextJava context = new TTContextJava();
    context.add(NamespaceVocab.IM, "im", "Discovery namespace");
    context.add(NamespaceVocab.SNOMED, "sn", "Snomed-CT namespace");
    context.add(NamespaceVocab.OWL, "owl", "OWL2 namespace");
    context.add(NamespaceVocab.RDF, "rdf", "RDF namespace");
    context.add(NamespaceVocab.RDFS, "rdfs", "RDFS namespace");
    context.add(NamespaceVocab.XSD, "xsd", "xsd namespace");
    context.add(NamespaceVocab.SHACL, "sh", "SHACL namespace");
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
  public static void saveDocument(TTDocumentJava document, String outputFile, Grammar grammar) throws JsonProcessingException {
    String outputString;
    if (grammar == Grammar.JSON) {
      try (CachedObjectMapper om = new CachedObjectMapper()) {
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        outputString = om.writerWithDefaultPrettyPrinter().withAttribute(TTContextJava.OUTPUT_CONTEXT, true).writeValueAsString(document);
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

  public static TTEntityJava createInstance(TTIriRef iri, TTIriRef crud) {
    TTEntityJava result = new TTEntityJava();
    result.setIri(iri.getIri());
    result.setCrud(crud);
    return result;
  }

  public static void addChildOf(TTEntityJava c, TTIriRef parent) {
    if (c.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_CHILD_OF)) == null)
      c.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_CHILD_OF), new TTArrayJava());
    c.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_CHILD_OF)).add(parent);
  }

  public static void addSuperClass(TTEntityJava entity, TTIriRef andOr, TTValue superClass) {
    addESAxiom(entity, TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), andOr, superClass);

  }

  private static void addESAxiom(TTEntityJava entity, TTIriRef axiom,
                                 TTIriRef andOr, TTValue newExpression) {
    TTIriRef subType = entity.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.PROPERTY)) ? TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUB_PROPERTY_OF) : TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF);
    if (entity.get(axiom) == null)
      entity.set(axiom, new TTArrayJava());
    TTValue oldExpression;
    TTArrayJava expressions = entity.get(axiom);
    if (!expressions.isEmpty()) {
      oldExpression = expressions.getElements().getFirst();
      if (oldExpression.isIriRef() || oldExpression.isNode()) {
        TTNodeJava intersection = new TTNodeJava();
        intersection.set(andOr, new TTArrayJava());
        intersection.get(andOr).add(oldExpression);
        intersection.get(andOr).add(newExpression);
        expressions.add(intersection);
      } else
        oldExpression.asNode().get(andOr).add(newExpression);
    } else
      expressions.add(newExpression);
    if (newExpression.isIriRef()) {
      if (entity.get(subType) == null)
        entity.set(subType, new TTArrayJava());
      entity.addObject(subType, newExpression);
    }

  }

  public static void addSimpleMap(TTEntityJava c, String target) {
    c.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.MATCHED_TO), TTIriRefExtensionsKt.iri(new TTIriRef(), target));
  }

  public static TTNodeJava addComplexMap(TTEntityJava c) {
    TTNodeJava map = new TTNodeJava();
    c.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_MAP), map);
    return map;
  }

  public static TTEntityJava createTermCode(TTIriRef iri, TTIriRef crud,
                                            String term, String code) {
    TTEntityJava result = createInstance(iri, crud);
    addTermCode(result, term, code);
    return result;
  }

  public static boolean termUsed(TTEntityJava entity, String term) {
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE)) != null) {
      for (TTValue val : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE)).getElements()) {
        if (val.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)) != null && val.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)).asLiteral().getValue().equals(term))
          return true;
      }
    }
    return false;
  }

  public static boolean termCodeUsed(TTEntityJava entity, String term, String code) {
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE)) != null) {
      for (TTValue val : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE)).getElements()) {
        if (val.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)) != null && val.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)).asLiteral().getValue().equals(term)) {
          if (code != null) {
            if (val.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE)) != null && val.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE)).asLiteral().getValue().equals(code)) {
              return true;
            }
          } else return true;
        }
      }
    }
    return false;
  }

  public static TTEntityJava addTermCode(TTEntityJava entity,
                                         String term, String code) {
    return addTermCode(entity, term, code, null);
  }

  public static TTEntityJava addTermCode(TTEntityJava entity,
                                         String term, String code, TTIriRef status) {
    if (!termCodeUsed(entity, term, code)) {
      TTNodeJava termCode = new TTNodeJava();
      if (status != null)
        termCode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS), status);
      if (term != null) {
        termCode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL), TTLiteralJava.literal(term));
      }
      if (code != null)
        termCode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE), TTLiteralJava.literal(code));
      entity.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE), termCode);
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
  public static TTNodeJava wrapRDFAsJson(TTNodeJava node) throws JsonProcessingException {
    for (TTIriRef predicate : jsonPredicates) {
      if (node.get(predicate) != null) {
        TTArrayJava jsons = new TTArrayJava();
        try (CachedObjectMapper om = new CachedObjectMapper()) {
          om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
          om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
          om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
          for (TTValue value : node.get(predicate).getElements()) {
            String json = om.writeValueAsString(value.asNode());
            jsons.add(TTLiteralJava.literal(json));
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
  public static boolean unwrapRDFfromJson(TTNodeJava node) throws IOException {
    boolean unwrapped = false;
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      for (TTIriRef predicate : jsonPredicates) {
        if (node.get(predicate) != null) {
          if (node.get(predicate).isLiteral()) {
            TTArrayJava rdfNodes = new TTArrayJava();
            for (TTValue value : node.get(predicate).getElements()) {
              rdfNodes.add(om.readValue(value.asLiteral().getValue(), TTNodeJava.class));
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
  public static Set<TTIriRef> getIrisFromNode(TTNodeJava node) {
    Set<TTIriRef> iris = new HashSet<>();
    return addToIrisFromNode(node, iris);
  }

  private static Set<TTIriRef> addToIrisFromNode(TTValue subject, Set<TTIriRef> iris) {
    if (subject.isIriRef() && (subject.asIriRef().getName() == null || subject.asIriRef().getName().isEmpty()))
      iris.add(subject.asIriRef());
    else if (subject.isNode() && subject.asNode().getPredicateMap() != null) {
      for (Map.Entry<TTIriRef, TTArrayJava> entry : subject.asNode().getPredicateMap().entrySet()) {
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
  public static void populateFromNode(TTNodeJava source, TTNodeJava target, Set<TTEntityJava> ranges) {
    Class<? extends TTNodeJava> clazz = target.getClass();
    target.setPredicateMap(source.getPredicateMap());
  }

  public static TTContextJava getDefaultContext() {
    TTContextJava ctx = new TTContextJava();
    ctx.add(NamespaceVocab.IM, "");
    ctx.add(NamespaceVocab.IM, "im");
    ctx.add(NamespaceVocab.RDFS, "rdfs");
    ctx.add(NamespaceVocab.RDF, "rdf");
    ctx.add(NamespaceVocab.SNOMED, "sn");
    ctx.add(NamespaceVocab.SHACL, "sh");
    ctx.add(NamespaceVocab.XSD, "xsd");
    return ctx;
  }

  public ModelDocument getModelDocument() {
    return modelDocument;
  }

  public TTManager setModelDocument(ModelDocument modelDocument) {
    this.modelDocument = modelDocument;
    return this;
  }

  public TTDocumentJava createDocument() {
    createDefaultContext();
    document = new TTDocumentJava();
    document.setContext(context);
    return document;
  }

  /**
   * Gets a entity from an iri or null if not found
   *
   * @param searchKey the iri or name of the entity you are looking for
   * @return entity, which may be a subtype that may be downcasted
   */
  public TTEntityJava getEntity(String searchKey) {
    if (entityMap == null)
      createIndex();
    TTEntityJava result = entityMap.get(searchKey);
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

  public TTContextJava createDefaultContext() {
    context = new TTContextJava();
    context.add(NamespaceVocab.IM, "im", "Discovery namespace");
    context.add(NamespaceVocab.SNOMED, "sn", "Snomed-CT namespace");
    context.add(NamespaceVocab.OWL, "owl", "OWL2 namespace");
    context.add(NamespaceVocab.RDF, "rdf", "RDF namespace");
    context.add(NamespaceVocab.RDFS, "rdfs", "RDFS namespace");
    context.add(NamespaceVocab.XSD, "xsd", "xsd namespace");
    return context;
  }

  /**
   * Loads an information model document file in  JSON-LD/RDF syntax
   *
   * @param inputFile the file name to load
   * @return the IM triple tree document
   * @throws IOException covering file format exceptions and content exceptions of various kinds
   */
  public TTDocumentJava loadDocument(File inputFile) throws IOException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      document = om.readValue(inputFile, TTDocumentJava.class);
      return document;
    }
  }

  public TTDocumentJava loadDocument(String json) throws IOException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      document = om.readValue(json, TTDocumentJava.class);
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

  public TTDocumentJava getDocument() {
    return document;
  }

  public TTManager setDocument(TTDocumentJava document) {
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
      String json = om.writerWithDefaultPrettyPrinter().withAttribute(TTContextJava.OUTPUT_CONTEXT, true).writeValueAsString(document);
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {
        writer.write(json);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }


  public TTEntityJava createNamespaceEntity(NamespaceVocab namespace, String name, String description) {
    TTEntityJava result = new TTEntityJava()
      .setIri(namespace.toString())
      .addType(EnumUtils.asIri(RdfsVocab.CLASS))
      .setName(name)
      .setDescription(description)
      .setScheme(EnumUtils.asIri(namespace));
    result.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), EnumUtils.asIri(ImVocab.ROOT_NAMESPACE));
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
  public String getJson(TTDocumentJava document) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      return om.writerWithDefaultPrettyPrinter().withAttribute(TTContextJava.OUTPUT_CONTEXT, true).writeValueAsString(document);
    }
  }

  /**
   * Returns a string of JSON from a TTEntity instance
   *
   * @param entity the TTEntity holding the entity
   * @return the json serialization of the document
   * @throws JsonProcessingException in on serialization failure
   */
  public String getJson(TTEntityJava entity) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      return om.writerWithDefaultPrettyPrinter().withAttribute(TTContextJava.OUTPUT_CONTEXT, true)
        .writeValueAsString(entity);
    }
  }

  public TTDocumentJava replaceIri(TTDocumentJava document, TTIriRef from, TTIriRef to) {
    if (document.getEntities() != null) {
      for (TTEntityJava entity : document.getEntities()) {
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

  private boolean replaceNode(TTNodeJava node, TTIriRef from, TTIriRef to) {
    if (node.get(from) != null) {
      node.set(to, node.get(from));
      node.getPredicateMap().remove(from);
      return true;
    }
    if (node.getPredicateMap() != null) {
      for (Map.Entry<TTIriRef, TTArrayJava> entry : node.getPredicateMap().entrySet()) {
        replaceNodeValueChange(from, to, entry);
      }
    }
    return false;
  }

  private void replaceNodeValueChange(TTIriRef from, TTIriRef to, Map.Entry<TTIriRef, TTArrayJava> entry) {
    TTArrayJava value = entry.getValue();

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
  public boolean isA(TTEntityJava descendant, TTIriRef ancestor) {
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
    TTEntityJava descendantEntity = entityMap.get(descendant.getIri());
    if (descendantEntity == null)
      return false;
    if (entityMap.get(ancestor.getIri()) == null)
      return false;
    return isA1(descendantEntity, ancestor, done);
  }

  private boolean isA1(TTEntityJava descendant, TTIriRef ancestor, Set<TTIriRef> done) {
    if (TTIriRefExtensionsKt.iri(new TTIriRef(), descendant.getIri()).equals(ancestor))
      return true;
    TTIriRef subType = descendant.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.PROPERTY)) ? TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUB_PROPERTY_OF) : TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF);
    boolean isa = false;
    if (descendant.get(subType) != null)
      for (TTValue ref : descendant.get(subType).iterator())
        if (ref.equals(ancestor))
          return true;
        else {
          TTIriRef parent = ref.asIriRef();
          if (!done.contains(parent)) {
            done.add(parent);
            TTEntityJava parentEntity = entityMap.get(parent.getIri());
            if (parentEntity != null)
              isa = isA1(parentEntity, ancestor, done);
            if (isa)
              return true;
          }
        }
    return false;
  }

  public TTContextJava getContext() {
    return context;
  }

  @Override
  public void close() {
    if (entityMap != null) entityMap.clear();
    if (nameMap != null) nameMap.clear();
  }

  public enum Grammar {JSON, TURTLE}
}
