package org.endeavourhealth.imapi.filer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.rdf4j.TTDocumentFilerRdf4j;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.File;
import java.util.*;

/**
 * Methods to create update and delete entities and generate a transaction log with the ability to refile
 * from the transaction log in order to process deltas after a bulk load
 * Methods may be called via CRUD instructions or file entities that contain the CRUD instructions.
 * <p>All entities must have a graph and a crud transation</p>
 */
public class TTTransactionFiler {
    private final String logPath;
    private final String pathDelimiter = "\\";

    /**
     * Destination folder for transaction log files must be set.
     */
    public TTTransactionFiler() {
        this(System.getenv("DELTA_PATH"));
    }

        /**
         * Destination folder for transaction log files must be set.
         *
         * @param logPath folder containing the transaction log files
         */
    public TTTransactionFiler(String logPath) {
        this.logPath = logPath;
    }

    /**
     * Files a set of entities with a crud transaction and generates transaction log
     *
     * @param transaction a TTDocyment with a crud setting and set of entities to file
     * @throws TTFilerException if invalid data content
     */
    public void fileTransaction(TTDocument transaction) throws Exception {
        checkDeletes(transaction);
        fileAsDocument(transaction);
    }

    /**
     * Files from the transaction logs in the delta folder
     *
     * @throws Exception
     */
    public void fileDeltas() throws Exception {
        Map<Integer, String> transactionLogs = new HashMap<>();
        File directory = new File(logPath + pathDelimiter);
        for (File file : Objects.requireNonNull(directory.listFiles()))
            if (!file.isDirectory()) {
                String name = file.getName();
                if (name.startsWith("TTLog-")) {
                    int counter = Integer.parseInt(name.split("-")[1].split("\\.")[0]);
                    transactionLogs.put(counter, file.getAbsolutePath());
                }
            }
        SortedSet<Integer> keys = new TreeSet<>(transactionLogs.keySet());
        TTManager manager = new TTManager();
        try (TTDocumentFiler filer = new TTDocumentFilerRdf4j()) { //only rdf4j supported at this point
            for (Integer logNumber : keys) {
                manager.loadDocument(new File(transactionLogs.get(logNumber)));
                filer.startTransaction();
                filer.fileInsideTraction(manager.getDocument());
                filer.commit();
            }
        }

    }

    private void checkDeletes(TTDocument transaction) throws TTFilerException {
        Map<String, Set<String>> toCheck = new HashMap<>();
            for (TTEntity entity : transaction.getEntities()) {
                if (entity.getCrud()==null) {
                    if (transaction.getCrud() != null) {
                        entity.setCrud(transaction.getCrud());
                    }
                    else
                        entity.setCrud(IM.UPDATE_ALL);
                }

                if (entity.getCrud()== IM.UPDATE_ALL) {
                if (entity.getGraph() == null && transaction.getGraph() == null)
                    throw new TTFilerException("Entity " + entity.getIri() + " must have a graph assigned, or the transaction must have a default graph");
                String graph = entity.getGraph() != null ? entity.getGraph().getIri() : transaction.getGraph().getIri();
                if (entity.getPredicateMap().isEmpty())
                    toCheck.computeIfAbsent(graph, g -> new HashSet<>()).add("<" + entity.getIri() + ">");
            }
            if (!toCheck.isEmpty()) {
                try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
                    for (Map.Entry<String, Set<String>> entry : toCheck.entrySet()) {
                        String graph = entry.getKey();
                        Set<String> entities = entry.getValue();
                        String sql = "select * where \n{ graph <" + graph + "> {" +
                                "?s ?p ?o.\n" +
                                "filter (?o in(" + String.join(",", entities) + "))" +
                                "filter (?p!= <" + IM.IS_A.getIri() + ">) } }";
                        TupleQuery qry = conn.prepareTupleQuery(sql);
                        TupleQueryResult rs = qry.evaluate();
                        if (rs.hasNext())
                            throw new TTFilerException("Entities have been used as objects or predicates. These must be deleted first");
                    }
                }
            }
        }
    }


    private void fileAsDocument(TTDocument document) throws Exception {
        try (TTDocumentFiler filer = new TTDocumentFilerRdf4j()) { //only rdf4j supported
            try {
               filer.startTransaction();
                filer.fileInsideTraction(document);
               if (logPath!=null)
                   writeLog(document);
                filer.commit();
            } catch (Exception e) {
                e.printStackTrace();
                filer.rollback();
            }
        }
    }


    private void writeLog(TTDocument document) throws JsonProcessingException {
        File directory = new File(logPath + pathDelimiter);
        int logNumber = 0;
        for (File file : Objects.requireNonNull(directory.listFiles()))
            if (!file.isDirectory()) {
                String name = file.getName();
                if (name.startsWith("TTLog-")) {
                    int counter = Integer.parseInt(name.split("-")[1].split("\\.")[0]);
                    if (counter > logNumber)
                        logNumber = counter;
                }
            }
        logNumber++;
        File logFile = new File(logPath + "\\TTLog-" + logNumber + ".json");
        TTManager manager = new TTManager();
        manager.setDocument(document);
        manager.saveDocument(logFile);
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
}

