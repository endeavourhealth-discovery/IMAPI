package org.endeavourhealth.imapi.filer.rdf4j;

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
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.im.GRAPH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * Methods to create update and delete entities and generate a transaction log with the ability to refile
 * from the transaction log in order to process deltas after a bulk load
 * Methods may be called via CRUD instructions or file entities that contain the CRUD instructions.
 * <p>All entities must have a graph and a crud transation</p>
 */
public class TTTransactionFiler implements TTDocumentFiler,AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(TTTransactionFiler.class);
    private RepositoryConnection conn;
    private Set<String> entitiesFiled;
    private String logPath;
    private final String TTLog = "TTLog-";
    private Map<String,Set<String>> isAs;
    private TTManager manager;
    private Set<String> done;
    protected TTEntityFiler conceptFiler;
    protected TTEntityFiler instanceFiler;
    protected Map<String, String> prefixMap = new HashMap<>();


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

    public void fileDeltas(String deltaPath) throws Exception {
        this.logPath = deltaPath;
        Map<Integer, String> transactionLogs = new HashMap<>();
        LOG.debug("Filing deltas from [{}]", logPath);
        File directory = new File(logPath);
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (!file.isDirectory()) {
                String name = file.getName();
                if (name.startsWith(TTLog)) {
                    int counter = Integer.parseInt(name.split("-")[1].split("\\.")[0]);
                    transactionLogs.put(counter, file.getAbsolutePath());
                }
            }
        }
        try (TTManager manager = new TTManager()) {
            for (Integer logNumber : transactionLogs.keySet().stream().sorted().toList()) {
                LOG.info("Filing TTLog-{}.json", logNumber);
                manager.loadDocument(new File(transactionLogs.get(logNumber)));
                fileDocument(manager.getDocument());
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
    public void fileDocument(TTDocument document) throws TTFilerException {
        document.getEntities().removeIf(e -> null == e.getIri());

        checkDeletes(document);
        fileAsDocument(document);
    }

    private void checkDeletes(TTDocument transaction) throws TTFilerException {
        Map<String, Set<String>> toCheck = getEntitiesToCheckForUsage(transaction);
        if (!toCheck.isEmpty()) {
            checkIfEntitiesCurrentlyInUse(toCheck);
        }
    }

    private static Map<String, Set<String>> getEntitiesToCheckForUsage(TTDocument transaction) throws TTFilerException {
        Map<String, Set<String>> toCheck = new HashMap<>();
        for (TTEntity entity : transaction.getEntities()) {

            setEntityCrudOperation(transaction, entity);

            if (Objects.equals(entity.getCrud(), IM.UPDATE_ALL.asTTIriRef())) {
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
        if (entity.getCrud()==null) {
            if (transaction.getCrud() != null) {
                entity.setCrud(transaction.getCrud());
            }
            else
                entity.setCrud(IM.UPDATE_ALL);
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
                  "filter (?p!= <" + IM.IS_A.getIri() + ">) } }";
                TupleQuery qry = conn.prepareTupleQuery(sql);
                try (TupleQueryResult rs = qry.evaluate()) {
                    if (rs.hasNext())
                        throw new TTFilerException("Entities have been used as objects or predicates. These must be deleted first");
                }
            }
        }
    }

    private void fileAsDocument(TTDocument document) throws TTFilerException{
            try {
                startTransaction();
                LOG.info("Filing entities.... ");
                int i = 0;
                entitiesFiled = new HashSet<>();
                for (TTEntity entity : document.getEntities()) {
                    setEntityCrudOperation(document, entity);

                    TTIriRef entityGraph = processGraphs(document, entity);

                    if (entity.get(IM.PRIVACY_LEVEL) != null && (entity.get(IM.PRIVACY_LEVEL).asLiteral().intValue() > TTFilerFactory.getPrivacyLevel()))
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
            } catch (Exception e){
                rollback();
            }
    }
    private void fileEntity(TTEntity entity, TTIriRef graph) throws TTFilerException {
        if (GRAPH.ODS.iri.equals(graph.getIri()))
            instanceFiler.fileEntity(entity, graph);
        else
            conceptFiler.fileEntity(entity, graph);
    }

    private static TTIriRef processGraphs(TTDocument document, TTEntity entity) {
        TTIriRef entityGraph = entity.getGraph();
        if (entityGraph == null)
            entityGraph = document.getGraph();
        if (document.getGraph() == null)
            document.setGraph(entity.getGraph());
        return entityGraph;
    }

    public void updateTct(TTDocument document) throws TTFilerException {
        isAs = new HashMap<>();
        done = new HashSet<>();
        manager = new TTManager();
        manager.setDocument(document).createIndex();
        LOG.info("Generating isas.... ");
        int i = 0;
        LOG.info("Collecting known descendants");
        Set<TTEntity> descendants= conceptFiler.getDescendants(entitiesFiled);
        Set<TTEntity> toClose= new HashSet<>(document.getEntities());;
        toClose.addAll(descendants);
        //set external isas first i.e. top of the tree
        for (TTEntity entity : toClose) {
            String subclass = entity.getIri();
            if (entity.get(RDFS.SUBCLASS_OF) != null) {
                for (TTValue superClass : entity.get(RDFS.SUBCLASS_OF).getElements()) {
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
        //Now get next levels from top to bottom
        for (TTEntity entity : toClose) {
            getInternalIsAs(entity);
        }
        conceptFiler.deleteIsas(isAs.keySet());
        conceptFiler.fileIsAs(isAs);
    }

    private Set<String> getInternalIsAs(TTEntity entity) {
        String subclass = entity.getIri();
        if (!done.contains(subclass)) {
            isAs.computeIfAbsent(subclass, s -> new HashSet<>());
            isAs.get(subclass).add(subclass);
            if (entity.get(RDFS.SUBCLASS_OF) != null) {
                for (TTValue superClass : entity.get(RDFS.SUBCLASS_OF).getElements()) {
                    String iri = superClass.asIriRef().getIri();
                    if (!done.contains(iri)) {
                        isAs.get(subclass).add(iri);
                        Set<String> superIsas= getInternalIsAs(manager.getEntity(iri));
                        if (superIsas!=null)
                            isAs.get(subclass).addAll(superIsas);
                        done.add(iri);
                    }
                    else {
                        if (isAs.get(iri)!=null)
                            isAs.get(subclass).addAll(isAs.get(iri));
                    }
                }
            }
        }
        done.add(subclass);
        return isAs.get(subclass);
    }




    public void writeLog(TTDocument document) throws Exception {
        LOG.debug("Writing transaction to [{}]", logPath);
        File directory = new File(logPath);
        int logNumber = 0;
        for (File file : Objects.requireNonNull(directory.listFiles()))
            if (!file.isDirectory()) {
                String name = file.getName();
                if (name.startsWith(TTLog)) {
                    int counter = Integer.parseInt(name.split("-")[1].split("\\.")[0]);
                    if (counter > logNumber)
                        logNumber = counter;
                }
            }
        logNumber++;
        File logFile = new File(logPath + TTLog + logNumber + ".json");
        try(TTManager manager = new TTManager()) {
            manager.setDocument(document);
            manager.saveDocument(logFile);
        }
    }


    private TupleQueryResult runQuery(RepositoryConnection conn, String sql) {
        TupleQuery qr = conn.prepareTupleQuery(sql);
        return qr.evaluate();
    }

    private void validateGraph(Set<TTEntity> entities) throws TTFilerException {
        for (TTEntity entity : entities) {
            if (entity.getGraph() == null)
                throw new TTFilerException("Each entity must have a graph assigned");
            if (entity.getCrud() == null)
                throw new TTFilerException("All entities must have UpdateAll," +
                        "UpdatePredicates, AddTriple CRUD operations");
        }
    }

    public void fileEntities(Map<String, String> prefixMap, TTDocument document) throws TTFilerException {
        LOG.info("Filing entities.... ");

        TTIriRef defaultGraph = document.getGraph() != null ? document.getGraph() : GRAPH.DISCOVERY.asTTIriRef();

        startTransaction();
        try {
            if (document.getEntities() != null) {
                int i = 0;
                for (TTEntity entity : document.getEntities()) {
                    TTIriRef entityGraph = entity.getGraph() != null ? entity.getGraph() : defaultGraph;
                    if (entity.get(IM.PRIVACY_LEVEL) != null && (entity.get(IM.PRIVACY_LEVEL).asLiteral().intValue() > TTFilerFactory.getPrivacyLevel()))
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

