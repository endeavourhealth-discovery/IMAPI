package org.endeavourhealth.imapi.dataaccess.databases;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.impl.SimpleDataset;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.io.File;
import java.util.StringJoiner;

@Slf4j
public abstract class BaseDB implements AutoCloseable {
  private static final String DEFAULT_PREFIXES = """
    PREFIX bc: <http://endhealth.info/bc#>
    PREFIX owl: <http://www.w3.org/2002/07/owl#>
    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    PREFIX sh: <http://www.w3.org/ns/shacl#>
    PREFIX xml: <http://www.w3.org/XML/1998/namespace#>
    PREFIX sn: <http://snomed.info/sct#>
    PREFIX im: <http://endhealth.info/im#>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    """;

  protected RepositoryConnection conn;
  private final SimpleDataset dataset= new SimpleDataset();

  protected BaseDB(Graph... graphs) {
    for (Graph graph : graphs) {
      if (graph == null)
        dataset.addDefaultGraph(null);
      else
        dataset.addDefaultGraph(graph.asDbIri());
    }
  }

  public TupleQuery prepareTupleSparql(String sparql) {
    if (sparql.toUpperCase().contains("INSERT"))
      throw new DALException("This appears to be an INSERT statement, use `prepareInsertSparql` instead");

    if (sparql.toUpperCase().contains("DELETE"))
      throw new DALException("This appears to be an DELETE statement, use `prepareDeleteSparql` instead");

    try {
      StringJoiner sj = new StringJoiner(System.lineSeparator());
      sj.add(DEFAULT_PREFIXES);
      sj.add(sparql);
      TupleQuery tq = conn.prepareTupleQuery(sj.toString());
      tq.setDataset(dataset);
      return tq;
    } catch (Exception e) {
      throw new DALException("Failed to prepare SPARQL query", e);
    }
  }

  public Update prepareDeleteSparql(String sparql) {
    if (!sparql.toUpperCase().contains("DELETE"))
      throw new DALException("This doesnt appear to be an DELETE statement");

    return prepareSparql(sparql);
  }

  public Update prepareInsertSparql(String sparql, Graph graph) {
    if (!sparql.toUpperCase().contains("INSERT"))
      throw new DALException("This doesnt appear to be an INSERT statement");

    if (graph == null)
      throw new DALException("A graph MUST be specified for an insert statement");

    Update insert = prepareSparql(sparql);
    dataset.setDefaultInsertGraph(graph.asDbIri());

    return insert;
  }

  public Update prepareUpdateSparql(String sparql, Graph graph) {
    if (!sparql.toUpperCase().contains("DELETE") || !sparql.toUpperCase().contains("INSERT"))
      throw new DALException("This doesnt appear to be an UPDATE statement");

    if (graph == null)
      throw new DALException("A graph MUST be specified for an update statement");

    Update update = prepareSparql(sparql);
    dataset.setDefaultInsertGraph(graph.asDbIri());

    return update;
  }

  private Update prepareSparql(String sparql) {
    try {
      StringJoiner sj = new StringJoiner(System.lineSeparator());
      sj.add(DEFAULT_PREFIXES);
      sj.add(sparql);
      Update uq = conn.prepareUpdate(sj.toString());
      uq.setDataset(dataset);
      return uq;
    } catch (Exception e) {
      throw new DALException("Failed to prepare SPARQL query", e);
    }
  }

  public GraphQuery prepareGraphSparql(String sparql) {

    try {
      StringJoiner sj = new StringJoiner(System.lineSeparator());
      sj.add(DEFAULT_PREFIXES);
      sj.add(sparql);
      GraphQuery gq = conn.prepareGraphQuery(sj.toString());
      gq.setDataset(dataset);
      return gq;
    } catch (Exception e) {
      throw new DALException("Failed to prepare SPARQL query", e);
    }
  }

  public BooleanQuery prepareBooleanSparql(String sparql) {
    if (!sparql.toUpperCase().contains("ASK"))
      throw new DALException("This doesnt appear to be an ASK statement");

    try {
      StringJoiner sj = new StringJoiner(System.lineSeparator());
      sj.add(DEFAULT_PREFIXES);
      sj.add(sparql);
      BooleanQuery bq = conn.prepareBooleanQuery(sj.toString());
      bq.setDataset(dataset);
      return bq;
    } catch (Exception e) {
      throw new DALException("Failed to prepare SPARQL query", e);
    }
  }

  @Override
  public void close() {
    if (conn != null) {
      conn.close();
      conn = null;
    }
  }

  protected static synchronized Repository getRepository(String repoId) {
    log.debug("Connecting to repository [{}]", repoId);
    String type = System.getenv("GRAPH_TYPE");
    if (type == null || type.isEmpty())
      type = "http";

    return switch (type.toLowerCase()) {
      case "http" -> getHttpRepo(repoId);
      case "file" -> getFileRepo(repoId);
      case "sparql" -> getSparqlRepo(repoId);
      default -> throw new DALException("Invalid graph connection parameters");
    };
  }

  private static HTTPRepository getHttpRepo(String repoId) {
    String server = System.getenv("GRAPH_SERVER");
    if (server == null || server.isEmpty()) {
      server = "http://localhost:7200/";
      log.warn("GRAPH_SERVER not set, defaulting to '{}'", server);
    }

    HTTPRepository repo = new HTTPRepository(server, repoId);

    String user = System.getenv("GRAPH_USER");
    String pass = System.getenv("GRAPH_PASS");

    if (user != null && pass != null)
      repo.setUsernameAndPassword(user, pass);

    return repo;
  }

  private static SailRepository getFileRepo(String repoId) {
    String server = System.getenv("GRAPH_SERVER");
    if (server == null || server.isEmpty()) {
      server = "C:\\rdf4j\\";
      log.warn("GRAPH_FILENAME not set, defaulting to '{}'", server);
    }

    String indexes = System.getenv("GRAPH_INDEXES");
    if (indexes == null || indexes.isEmpty()) {
      indexes = "spoc,posc,opsc";
      log.warn("GRAPH_INDEXES not set, defaulting to '{}'", indexes);
    }

    return new SailRepository(new NativeStore(new File(server + repoId), indexes));
  }

  private static SPARQLRepository getSparqlRepo(String repoId) {
    String query = System.getenv("GRAPH_QUERY");
    if (query == null || query.isEmpty()) {
      query = "cluster-cwwwbkhdusnw.eu-west-2.neptune.amazonaws.com";
      log.warn("GRAPH_QUERY not set, defaulting to '{}'", query);
    }

    String update = System.getenv("GRAPH_UPDATE");
    if (update == null || update.isEmpty()) {
      update = "cluster-ro-cwwwbkhdusnw.eu-west-2.neptune.amazonaws.com";
      log.warn("GRAPH_UPDATE not set, defaulting to '{}'", update);
    }

    return new SPARQLRepository(repoId + "." + query, repoId + "." + update);
  }

  public void add(Model build) {
    conn.add(build);
  }

  public void begin() {
    conn.begin();
  }

  public void commit() {
    conn.commit();
  }

  public void rollback() {
    conn.rollback();
  }
}

