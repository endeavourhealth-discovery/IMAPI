package org.endeavourhealth.imapi.filer.rdf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TTFilerFactory;
import org.endeavourhealth.imapi.logic.reasoner.RangeInheritor;
import org.endeavourhealth.imapi.logic.reasoner.SetBinder;
import org.endeavourhealth.imapi.logic.reasoner.SetExpander;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * Methods to create update and delete entities and generate a transaction log with the ability to refile
 * from the transaction log in order to process deltas after a bulk load
 * Methods may be called via CRUD instructions or file entities that contain the CRUD instructions.
 * <p>All entities must have a graph and a crud transaction</p>
 */
public class TTTransactionFiler implements TTDocumentFiler, AutoCloseable {
  private static final Logger LOG = LoggerFactory.getLogger(TTTransactionFiler.class);
  private static final String TTLOG = "TTLog-";
  protected TTEntityFiler conceptFiler;
  protected TTEntityFiler instanceFiler;
  protected Map<String, String> prefixMap = new HashMap<>();
  private RepositoryConnection conn;
  private Set<String> entitiesFiled;
  private String logPath;
  private Map<String, Set<String>> isAs;
  private TTManager manager;
  private Set<String> done;


  /**
   * Destination folder for transaction log files must be set.
   */
  public TTTransactionFiler() {
    this(System.getenv("DELTA_PATH"));
    LOG.info("Connecting");
    conn = ConnectionManager.getIMConnection();

    LOG.info("Initializing");
    conceptFiler = new TTEntityFilerRdf4j(conn, prefixMap);
    instanceFiler = conceptFiler;   // Concepts & Instances filed in the same way
    LOG.info("Done");
  }

  /**
   * Destination folder for transaction log files must be set.
   *
   * @param logPath folder containing the transaction log files
   */
  public TTTransactionFiler(String logPath) {
    this.logPath = logPath;
  }

  private static Map<String, Set<String>> getEntitiesToCheckForUsage(TTDocument transaction) throws TTFilerException {
    Map<String, Set<String>> toCheck = new HashMap<>();
    for (TTEntity entity : transaction.getEntities()) {

      setEntityCrudOperation(transaction, entity);

      if (Objects.equals(entity.getCrud(), iri(IM.UPDATE_ALL))) {
        if (entity.getGraph() == null && transaction.getGraph() == null)
          throw new TTFilerException("Entity " + entity.getIri() + " must have a graph assigned, or the transaction must have a default graph");
        String graph = entity.getGraph() != null ? entity.getGraph().getIri() : transaction.getGraph().getIri();
        if (entity.getPredicateMap().isEmpty())
          toCheck.computeIfAbsent(graph, g -> new HashSet<>()).add("<" + entity.getIri() + ">");
      }

    }
    return toCheck;
  }

  private static void setEntityCrudOperation(TTDocument transaction, TTEntity entity) {
    if (entity.getCrud() == null) {
      if (transaction.getCrud() != null) {
        entity.setCrud(transaction.getCrud());
      } else
        entity.setCrud(iri(IM.UPDATE_ALL));
    }
  }

  private static void checkIfEntitiesCurrentlyInUse(Map<String, Set<String>> toCheck) throws TTFilerException {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      for (Map.Entry<String, Set<String>> entry : toCheck.entrySet()) {
        String graph = entry.getKey();
        Set<String> entities = entry.getValue();
        String sql = "select * where \n{ graph <" + graph + "> {" +
          "?s ?p ?o.\n" +
          "filter (?o in(" + String.join(",", entities) + "))" +
          "filter (?p!= <" + IM.IS_A + ">) } }";
        TupleQuery qry = conn.prepareTupleQuery(sql);
        try (TupleQueryResult rs = qry.evaluate()) {
          if (rs.hasNext())
            throw new TTFilerException("Entities have been used as objects or predicates. These must be deleted first");
        }
      }
    }
  }

  private static TTIriRef processGraphs(TTDocument document, TTEntity entity) {
    TTIriRef entityGraph = entity.getGraph();
    if (entityGraph == null)
      entityGraph = document.getGraph();
    if (document.getGraph() == null)
      document.setGraph(entity.getGraph());
    return entityGraph;
  }

  public void fileDeltas(String deltaPath) throws IOException, QueryException, TTFilerException {
    this.logPath = deltaPath;
    Map<Integer, String> transactionLogs = new HashMap<>();
    LOG.debug("Filing deltas from [{}]", logPath);
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
        LOG.info("Filing TTLog-{}.json", logNumber);
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
    LOG.info("Disconnecting");
    conn.close();
    LOG.info("Disconnected");
  }

  @Override
  public void fileDocument(TTDocument document) throws TTFilerException, JsonProcessingException, QueryException {
    if (document.getEntities() == null) {
      throw new TTFilerException("Document has no entities");
    }

    document.getEntities().removeIf(e -> null == e.getIri());

    checkDeletes(document);
    fileAsDocument(document);
  }

  @Override
  public void fileDocument(TTDocument document, String taskId, Map<String, Integer> progressMap) throws TTFilerException, JsonProcessingException, QueryException {
    if (document.getEntities() == null) {
      throw new TTFilerException("Document has no entities");
    }

    document.getEntities().removeIf(e -> null == e.getIri());

    checkDeletes(document);
    fileAsDocument(document, taskId, progressMap);
  }

  private void checkDeletes(TTDocument transaction) throws TTFilerException {
    Map<String, Set<String>> toCheck = getEntitiesToCheckForUsage(transaction);
    if (!toCheck.isEmpty()) {
      checkIfEntitiesCurrentlyInUse(toCheck);
    }
  }

  private void fileAsDocument(TTDocument document) throws TTFilerException, JsonProcessingException, QueryException {
    try {
      startTransaction();
      LOG.info("Filing entities.... ");
      int i = 0;
      entitiesFiled = new HashSet<>();
      for (TTEntity entity : document.getEntities()) {
        setEntityCrudOperation(document, entity);

        TTIriRef entityGraph = processGraphs(document, entity);

        if (entity.get(iri(IM.PRIVACY_LEVEL)) != null && (entity.get(iri(IM.PRIVACY_LEVEL)).asLiteral().intValue() > TTFilerFactory.getPrivacyLevel()))
          continue;

        fileEntity(entity, entityGraph);
        entitiesFiled.add(entity.getIri());
        i++;
        if (i % 100 == 0)
          LOG.info("Filed {}  entities in transaction from {} in graph {}", i, document.getEntities().size(), entityGraph.getIri());

      }
      if (logPath != null)
        writeLog(document);
      updateTct(document);
      LOG.info("Updating range inheritances");
      new RangeInheritor().inheritRanges(conn);
      commit();
    } catch (TTFilerException e) {
      rollback();
      throw e;

    } catch (Exception e) {
      throw new TTFilerException(e.getMessage());
    }
    updateSets(document);
  }

  private void fileAsDocument(TTDocument document, String taskId, Map<String, Integer> progressMap) throws TTFilerException, JsonProcessingException, QueryException {
//    new Thread(() -> {
//      try {
//        try {
//          startTransaction();
//          LOG.info("Filing entities.... ");
//          int i = 0;
//          entitiesFiled = new HashSet<>();
//          for (TTEntity entity : document.getEntities()) {
//            Integer previousProgress = progressMap.getOrDefault(taskId, 0);
//            progressMap.put(taskId, previousProgress++);
//            setEntityCrudOperation(document, entity);
//
//            TTIriRef entityGraph = processGraphs(document, entity);
//
//            if (entity.get(iri(IM.PRIVACY_LEVEL)) != null && (entity.get(iri(IM.PRIVACY_LEVEL)).asLiteral().intValue() > TTFilerFactory.getPrivacyLevel()))
//              continue;
//
//            fileEntity(entity, entityGraph);
//            entitiesFiled.add(entity.getIri());
//            i++;
//            if (i % 100 == 0)
//              LOG.info("Filed {}  entities in transaction from {} in graph {}", i, document.getEntities().size(), entityGraph.getIri());
//
//          }
//          if (logPath != null)
//            writeLog(document);
//          updateTct(document);
//          LOG.info("Updating range inheritances");
//          new RangeInheritor().inheritRanges(conn);
//          commit();
//        } catch (TTFilerException e) {
//          rollback();
//          throw e;
//
//        } catch (Exception e) {
//          throw new TTFilerException(e.getMessage());
//        }
//        updateSets(document);
//      } catch (TTFilerException e) {
//        throw new RuntimeException(e);
//      } catch (QueryException e) {
//        throw new RuntimeException(e);
//      } catch (JsonProcessingException e) {
//        throw new RuntimeException(e);
//      }
//    }).start();

    try {
      LOG.info("Filing entities.... ");
      int totalSteps = 100;
      for (int i = 1; i <= totalSteps; i++) {
        Thread.sleep(100);
        progressMap.put(taskId, i); // Update progress
//        System.out.println(progressMap.get(taskId));
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void fileEntity(TTEntity entity, TTIriRef graph) throws TTFilerException {
    if (GRAPH.ODS.equals(graph.getIri()))
      instanceFiler.fileEntity(entity, graph);
    else
      conceptFiler.fileEntity(entity, graph);
  }

  public void updateSets(TTDocument document) throws QueryException, JsonProcessingException {
    for (TTEntity entity : document.getEntities()) {
      if (entity.isType(iri(IM.CONCEPT_SET)) || entity.isType(iri(IM.VALUESET))) {
        LOG.info("Expanding set {}", entity.getIri());
        new SetExpander().expandSet(entity.getIri());
        LOG.info("Binding set {}", entity.getIri());
        new SetBinder().bindSet(entity.getIri());
      }
    }
  }

  public void updateTct(TTDocument document) throws TTFilerException {
    isAs = new HashMap<>();
    done = new HashSet<>();
    manager = new TTManager();
    manager.setDocument(document).createIndex();
    LOG.info("Generating isas.... ");
    LOG.info("Collecting known descendants");
    Set<TTEntity> descendants = conceptFiler.getDescendants(entitiesFiled);
    Set<TTEntity> toClose = new HashSet<>(document.getEntities());
    toClose.addAll(descendants);
    //set external isas first i.e. top of the tree
    setExternalIsas(toClose);
    //Now get next levels from top to bottom
    for (TTEntity entity : toClose) {
      getInternalIsAs(entity);
    }
    conceptFiler.deleteIsas(isAs.keySet());
    conceptFiler.fileIsAs(isAs);
  }

  private void setExternalIsas(Set<TTEntity> toClose) throws TTFilerException {
    for (TTEntity entity : toClose) {
      for (String iriRef : List.of(RDFS.SUBCLASS_OF, IM.LOCAL_SUBCLASS_OF)) {
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
      for (String iriRef : List.of(RDFS.SUBCLASS_OF, IM.LOCAL_SUBCLASS_OF)) {
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
    LOG.debug("Writing transaction to [{}]", logPath);
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

  public void fileEntities(TTDocument document) throws TTFilerException {
    LOG.info("Filing entities.... ");

    TTIriRef defaultGraph = document.getGraph() != null ? document.getGraph() : iri(GRAPH.DISCOVERY);

    startTransaction();
    try {
      if (document.getEntities() != null) {
        int i = 0;
        for (TTEntity entity : document.getEntities()) {
          TTIriRef entityGraph = entity.getGraph() != null ? entity.getGraph() : defaultGraph;
          if (entity.get(iri(IM.PRIVACY_LEVEL)) != null && (entity.get(iri(IM.PRIVACY_LEVEL)).asLiteral().intValue() > TTFilerFactory.getPrivacyLevel()))
            continue;
          setEntityCrudOperation(document, entity);
          fileEntity(entity, entityGraph);
          i++;
          if (i % 10000 == 0) {
            LOG.info("Filed {} entities from {} in graph {}", i, document.getEntities().size(), document.getGraph().getIri());
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

