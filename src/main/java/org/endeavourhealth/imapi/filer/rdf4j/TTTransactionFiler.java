package org.endeavourhealth.imapi.filer.rdf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TTFilerFactory;
import org.endeavourhealth.imapi.logic.reasoner.RangeInheritor;
import org.endeavourhealth.imapi.logic.reasoner.SetBinder;
import org.endeavourhealth.imapi.logic.reasoner.SetMemberGenerator;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArrayList;

/**
 * Methods to create update and delete entities and generate a transaction log with the ability to refile
 * from the transaction log in order to process deltas after a bulk load
 * Methods may be called via CRUD instructions or file entities that contain the CRUD instructions.
 * <p>All entities must have a graph and a crud transaction</p>
 */
@Slf4j
public class TTTransactionFiler implements TTDocumentFiler, AutoCloseable {
  private static final String TTLOG = "TTLog-";
  private static Integer filingProgress = null;
  protected TTEntityFiler conceptFiler;
  protected TTEntityFiler instanceFiler;
  protected Map<String, String> prefixMap = new HashMap<>();
  private IMDB conn;
  private Set<String> entitiesFiled;
  private String logPath;
  private Map<String, Set<String>> isAs;
  private TTManager manager;
  private Set<String> done;
  private Graph graph;


  /**
   * Destination folder for transaction log files must be set.
   */
  public TTTransactionFiler(Graph graph) {
    this.graph = graph;
    logPath = System.getenv("DELTA_PATH");
    log.info("Connecting");
    conn = IMDB.getConnection(graph);

    log.info("Initializing");
    conceptFiler = new TTEntityFilerRdf4j(conn, prefixMap, graph);
    instanceFiler = conceptFiler;   // Concepts & Instances filed in the same way
    log.info("Done");
  }

  private static Map<Graph, Set<String>> getEntitiesToCheckForUsage(TTDocument transaction) {
    Map<Graph, Set<String>> toCheck = new HashMap<>();
    for (TTEntity entity : transaction.getEntities()) {

      setEntityCrudOperation(transaction, entity);

      if (Objects.equals(entity.getCrud(), iri(IM.REPLACE_ALL_PREDICATES))) {
        if (entity.getPredicateMap().isEmpty())
          toCheck.computeIfAbsent(Graph.IM, g -> new HashSet<>()).add("<" + entity.getIri() + ">");
      }

    }
    return toCheck;
  }

  private static void setEntityCrudOperation(TTDocument transaction, TTEntity entity) {
    if (entity.getCrud() == null) {
      if (transaction.getCrud() != null) {
        entity.setCrud(transaction.getCrud());
      } else
        entity.setCrud(iri(IM.REPLACE_ALL_PREDICATES));
    }
  }

  private static void checkIfEntitiesCurrentlyInUse(Map<Graph, Set<String>> toCheck) throws TTFilerException {
      for (Map.Entry<Graph, Set<String>> entry : toCheck.entrySet()) {
        Graph graph = entry.getKey();
        try (IMDB conn = IMDB.getConnection(graph)) {
        Set<String> entities = entry.getValue();
        String sql = "select * where {" +
          "?s ?p ?o.\n" +
          "filter (?o in(" + String.join(",", entities) + "))" +
          "filter (?p!= <" + IM.IS_A + ">) } ";
        TupleQuery qry = conn.prepareTupleSparql(sql);
        try (TupleQueryResult rs = qry.evaluate()) {
          if (rs.hasNext())
            throw new TTFilerException("Entities have been used as objects or predicates. These must be deleted first");
        }
      }
    }
  }

  public void fileDeltas(String deltaPath) throws IOException, QueryException, TTFilerException {
    this.logPath = deltaPath;
    Map<Integer, String> transactionLogs = new HashMap<>();
    log.debug("Filing deltas from [{}]", logPath);
    File directory = new File(logPath);
    for (File file : Objects.requireNonNull(directory.listFiles())) {
      if (!file.isDirectory()) {
        String name = file.getName();
        if (name.startsWith(TTLOG)) {
          int counter = Integer.parseInt(name.split("-")[1].split("\\.")[0]);
          transactionLogs.put(counter, file.getAbsolutePath());
        }
      }
    }
    try (TTManager ttManager = new TTManager()) {
      for (Integer logNumber : transactionLogs.keySet().stream().sorted().toList()) {
        log.info("Filing TTLog-{}.json", logNumber);
        ttManager.loadDocument(new File(transactionLogs.get(logNumber)));
        fileDocument(ttManager.getDocument());
      }
    }

  }

  protected void startTransaction() throws TTFilerException {
    try {
      conn.begin();
    } catch (RepositoryException e) {
      throw new TTFilerException("Failed to start transaction", e);
    }
  }

  protected void commit() throws TTFilerException {
    try {
      conn.commit();
    } catch (RepositoryException e) {
      throw new TTFilerException("Failed to commit transaction", e);
    }
  }

  protected void rollback() throws TTFilerException {
    try {
      conn.rollback();
    } catch (RepositoryException e) {
      throw new TTFilerException("Failed to rollback transaction", e);
    }
  }

  @Override
  public void close() {
    log.info("Disconnecting");
    conn.close();
    log.info("Disconnected");
  }

  @Override
  public void fileDocument(TTDocument document) throws TTFilerException, JsonProcessingException, QueryException {
    if (document.getEntities() == null) {
      throw new TTFilerException("Document has no entities");
    }

    document.getEntities().removeIf(e -> null == e.getIri());

    checkDeletes(document);
    fileAsDocument(document, graph);
  }

  public void fileDocument(TTDocument document, String taskId) throws TTFilerException, JsonProcessingException, QueryException {
    if (document.getEntities() == null) {
      throw new TTFilerException("Document has no entities");
    }

    document.getEntities().removeIf(e -> null == e.getIri());

    checkDeletes(document);
    fileAsDocument(document, taskId, graph);
  }

  private void checkDeletes(TTDocument transaction) throws TTFilerException {
    Map<Graph, Set<String>> toCheck = getEntitiesToCheckForUsage(transaction);
    if (!toCheck.isEmpty()) {
      checkIfEntitiesCurrentlyInUse(toCheck);
    }
  }

  private void fileAsDocument(TTDocument document, Graph graph) throws TTFilerException, JsonProcessingException, QueryException {
    try {
      startTransaction();
      log.info("Filing entities.... ");
      int i = 0;
      entitiesFiled = new HashSet<>();
      for (TTEntity entity : document.getEntities()) {
        if (entity.getIri().equals("http://endhealth.info/emis#29711000033114")) {
          System.out.println("here");
        }
        setEntityCrudOperation(document, entity);

        if (entity.get(iri(IM.PRIVACY_LEVEL)) != null && (entity.get(iri(IM.PRIVACY_LEVEL)).asLiteral().intValue() > TTFilerFactory.getPrivacyLevel()))
          continue;

        fileEntity(entity);
        entitiesFiled.add(entity.getIri());
        i++;
        if (i % 100 == 0)
          log.info("Filed {}  entities in transaction from {}", i, document.getEntities().size());

      }
      if (logPath != null)
        writeLog(document);
      updateTct(document);
      log.info("Updating range inheritances");
      new RangeInheritor().inheritRanges(conn, graph);
      commit();

    } catch (Exception e) {
      rollback();
      throw new TTFilerException(e.getMessage());
    }
    updateSets(document);
  }

  public synchronized Integer getFilingProgress(String taskId) {
    return filingProgress;
  }

  private synchronized void fileAsDocument(TTDocument document, String taskId, Graph graph) throws TTFilerException, JsonProcessingException, QueryException {

    if (filingProgress != null)
      throw new TTFilerException("There is a document already filing, please try again later");
    filingProgress = 0;
    try {
      startTransaction();
      log.info("Filing entities.... ");
      int i = 0;
      int totalEntities = document.getEntities().size();
      entitiesFiled = new HashSet<>();
      for (TTEntity entity : document.getEntities()) {
        i++;
        filingProgress = Math.round((float) i / totalEntities * 100);
        setEntityCrudOperation(document, entity);

        if (entity.get(iri(IM.PRIVACY_LEVEL)) != null && (entity.get(iri(IM.PRIVACY_LEVEL)).asLiteral().intValue() > TTFilerFactory.getPrivacyLevel()))
          continue;

        fileEntity(entity);
        entitiesFiled.add(entity.getIri());

        if (i % 100 == 0)
          log.info("Filed {} entities in transaction from {}", i, document.getEntities().size());

      }
      if (logPath != null)
        writeLog(document);
      updateTct(document);
      log.info("Updating range inheritances");
      new RangeInheritor().inheritRanges(conn, Graph.IM);
      commit();
    } catch (TTFilerException e) {
      rollback();
      throw e;

    } catch (Exception e) {
      rollback();
      throw new TTFilerException(e.getMessage());
    }
    updateSets(document);
    filingProgress = null;
  }

  private void fileEntity(TTEntity entity) throws TTFilerException {
    if (entity.has(iri(IM.HAS_SCHEME)) && entity.get(iri(IM.HAS_SCHEME)).asIriRef().getIri().equals(Namespace.ODS.toString()))
      instanceFiler.fileEntity(entity);
    else
      conceptFiler.fileEntity(entity);
  }

  public void updateSets(TTDocument document) throws QueryException, JsonProcessingException {
    for (TTEntity entity : document.getEntities()) {
      if (entity.isType(iri(IM.CONCEPT_SET)) || entity.isType(iri(IM.VALUESET))) {
        log.info("Expanding set {}", entity.getIri());
        new SetMemberGenerator().generateMembers(entity.getIri(), graph);
        log.info("Binding set {}", entity.getIri());
        new SetBinder().bindSet(entity.getIri(), graph);
      }
    }
  }

  public void updateTct(TTDocument document) throws TTFilerException {
    isAs = new HashMap<>();
    done = new HashSet<>();
    manager = new TTManager();
    manager.setDocument(document).createIndex();
    log.info("Generating isas.... ");
    log.info("Collecting known descendants");
    Set<TTEntity> descendants = conceptFiler.getDescendants(entitiesFiled);
    Set<TTEntity> toClose = new HashSet<>(document.getEntities());
    toClose.addAll(descendants);
    //set external isas first i.e. top of the tree
    setExternalIsas(toClose);
    //Now get next levels from top to bottom
    for (TTEntity entity : toClose) {
      getInternalIsAs(entity);
    }
    conceptFiler.deleteIsAs(isAs.keySet());
    conceptFiler.fileIsAs(isAs);
  }

  private void setExternalIsas(Set<TTEntity> toClose) throws TTFilerException {
    for (TTEntity entity : toClose) {
      for (String iriRef : asArrayList(RDFS.SUBCLASS_OF, IM.LOCAL_SUBCLASS_OF)) {
        String subclass = entity.getIri();
        if (entity.get(iri(iriRef)) != null) {
          for (TTValue superClass : entity.get(iri(iriRef)).getElements()) {
            String iri = superClass.asIriRef().getIri();
            if (!entitiesFiled.contains(iri)) {
              Set<String> ancestors = conceptFiler.getIsAs(iri);
              ancestors.add(subclass);
              isAs.put(subclass, ancestors);
              done.add(iri);
            }
          }
        }
      }
    }
  }

  private Set<String> getInternalIsAs(TTEntity entity) {
    String subclass = entity.getIri();
    if (!done.contains(subclass)) {
      isAs.computeIfAbsent(subclass, s -> new HashSet<>());
      isAs.get(subclass).add(subclass);
      for (String iriRef : asArrayList(RDFS.SUBCLASS_OF, IM.LOCAL_SUBCLASS_OF)) {
        if (entity.get(iri(iriRef)) != null) {
          processSuperClass(entity, iriRef, subclass);
        }
      }
    }
    done.add(subclass);
    return isAs.get(subclass);
  }

  private void processSuperClass(TTEntity entity, String iriRef, String subclass) {
    for (TTValue superClass : entity.get(iri(iriRef)).getElements()) {
      String iri = superClass.asIriRef().getIri();
      if (!done.contains(iri)) {
        isAs.get(subclass).add(iri);
        Set<String> superIsas = getInternalIsAs(manager.getEntity(iri));
        if (superIsas != null)
          isAs.get(subclass).addAll(superIsas);
        done.add(iri);
      } else {
        if (isAs.get(iri) != null)
          isAs.get(subclass).addAll(isAs.get(iri));
      }
    }
  }


  public void writeLog(TTDocument document) throws JsonProcessingException {
    log.debug("Writing transaction to [{}]", logPath);
    File directory = new File(logPath);
    int logNumber = 0;
    for (File file : Objects.requireNonNull(directory.listFiles()))
      if (!file.isDirectory()) {
        String name = file.getName();
        if (name.startsWith(TTLOG)) {
          int counter = Integer.parseInt(name.split("-")[1].split("\\.")[0]);
          if (counter > logNumber)
            logNumber = counter;
        }
      }
    logNumber++;
    File logFile = new File(logPath + TTLOG + logNumber + ".json");
    try (TTManager ttManager = new TTManager()) {
      ttManager.setDocument(document);
      ttManager.saveDocument(logFile);
    }
  }

  public void fileEntities(TTDocument document, Graph graph) throws TTFilerException {
    log.info("Filing entities.... ");

    startTransaction();
    try {
      if (document.getEntities() != null) {
        int i = 0;
        for (TTEntity entity : document.getEntities()) {
          if (entity.get(iri(IM.PRIVACY_LEVEL)) != null && (entity.get(iri(IM.PRIVACY_LEVEL)).asLiteral().intValue() > TTFilerFactory.getPrivacyLevel()))
            continue;
          setEntityCrudOperation(document, entity);
          fileEntity(entity);
          i++;
          if (i % 10000 == 0) {
            log.info("Filed {} entities from {} in graph {}", i, document.getEntities().size(), Graph.IM);
            commit();
            startTransaction();
          }
        }
      }
      commit();
    } catch (Exception e) {
      rollback();
      throw e;
    }
  }


}

