package org.endeavourhealth.imapi.dataaccess.helpers;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;

@Slf4j
public class ConnectionManager {
  private static final Map<String, Repository> repos = new HashMap<>();
  private static final String DEFAULT_PREFIXES = """
    PREFIX bc: <http://endhealth.info/bc#>
    PREFIX reports: <http://endhealth.info/reports#>
    PREFIX owl: <http://www.w3.org/2002/07/owl#>
    PREFIX ceg13: <http://endhealth.info/ceg16#>
    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
    PREFIX fn: <http://www.w3.org/2005/xpath-functions#>
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    PREFIX imq: <http://endhealth.info/imq#>
    PREFIX path: <http://www.ontotext.com/path#>
    PREFIX opcs4: <http://endhealth.info/opcs4#>
    PREFIX sh: <http://www.w3.org/ns/shacl#>
    PREFIX xml: <http://www.w3.org/XML/1998/namespace#>
    PREFIX sn: <http://snomed.info/sct#>
    PREFIX prov: <http://www.w3.org/ns/prov#>
    PREFIX icd10: <http://endhealth.info/icd10#>
    PREFIX rdf4j: <http://rdf4j.org/schema/rdf4j#>
    PREFIX vis: <http://endhealth.info/vision#>
    PREFIX wgs: <http://www.w3.org/2003/01/geo/wgs84_pos#>
    PREFIX kchapex: <http://endhealth.info/kchapex#>
    PREFIX im: <http://endhealth.info/im#>
    PREFIX prsb: <http://endhealth.info/prsb#>
    PREFIX cfg: <http://endhealth.info/config#>
    PREFIX nhse2001: <http://endhealth.info/nhsethnic2001#>
    PREFIX gn: <http://www.geonames.org/ontology#>
    PREFIX kchwinpath: <http://endhealth.info/kchwinpath#>
    PREFIX orole: <https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-OrganizationRole-1#>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX tpp: <http://endhealth.info/tpp#>
    PREFIX sesame: <http://www.openrdf.org/schema/sesame#>
    PREFIX emis: <http://endhealth.info/emis#>
    PREFIX ods: <http://endhealth.info/ods#>
    """;

  private ConnectionManager() {
    throw new IllegalStateException("Utility class");
  }

  public static RepositoryConnection getIMConnection() {
    return getRepository("im").getConnection();
  }

  public static RepositoryConnection getConfigConnection() {
    return getRepository("config").getConnection();
  }

  public static RepositoryConnection getUserConnection() {
    return getRepository("user").getConnection();
  }

  public static RepositoryConnection getWorkflowConnection() {
    return getRepository("workflow").getConnection();
  }


  public static TupleQuery prepareTupleSparql(RepositoryConnection conn, String sparql, String graph) {
    if (!sparql.contains("FROM ?g")) {
      throw new DALException("Query must contain 'FROM ?g' to specify graph");
    }
    try {
      StringJoiner sj = new StringJoiner(System.lineSeparator());
      sj.add(DEFAULT_PREFIXES);
      sj.add(sparql);
      TupleQuery tq = conn.prepareTupleQuery(sj.toString());
      tq.setBinding("g", iri(graph));
      return tq;
    } catch (Exception e) {
      throw new DALException("Failed to prepare SPARQL query", e);
    }
  }

  public static Update prepareUpdateSparql(RepositoryConnection conn, String sparql, String graph) {
    if (!sparql.contains("GRAPH ?g {")) {
      throw new DALException("Query must contain 'GRAPH ?g {' to specify graph");
    }
    try {
      StringJoiner sj = new StringJoiner(System.lineSeparator());
      sj.add(DEFAULT_PREFIXES);
      sj.add(sparql);
      Update uq = conn.prepareUpdate(sj.toString());
      uq.setBinding("g", iri(graph));
      return uq;
    } catch (Exception e) {
      throw new DALException("Failed to prepare SPARQL query", e);
    }
  }

  public static GraphQuery prepareGraphSparql(RepositoryConnection conn, String sparql, String graph) {
    if (!sparql.contains("FROM ?g")) {
      throw new DALException("Query must contain 'FROM ?g' to specify graph");
    }
    try {
      StringJoiner sj = new StringJoiner(System.lineSeparator());
      sj.add(DEFAULT_PREFIXES);
      sj.add(sparql);
      GraphQuery gq = conn.prepareGraphQuery(sj.toString());
      gq.setBinding("g", iri(graph));
      return gq;
    } catch (Exception e) {
      throw new DALException("Failed to prepare SPARQL query", e);
    }
  }

  public static BooleanQuery prepareBooleanSparql(RepositoryConnection conn, String sparql, String graph) {
    if (!sparql.contains("GRAPH ?g {")) {
      throw new DALException("Query must contain 'GRAPH ?g {' to specify graph");
    }
    try {
      StringJoiner sj = new StringJoiner(System.lineSeparator());
      sj.add(DEFAULT_PREFIXES);
      sj.add(sparql);
      BooleanQuery bq = conn.prepareBooleanQuery(sj.toString());
      bq.setBinding("g", iri(graph));
      return bq;
    } catch (Exception e) {
      throw new DALException("Failed to prepare SPARQL query", e);
    }
  }

  private static synchronized Repository getRepository(String repoId) {
    Repository repo = repos.get(repoId);
    if (repo == null) {
      log.debug("Connecting to repository [{}]", repoId);
      String type = System.getenv("GRAPH_TYPE");
      if (type == null || type.isEmpty())
        type = "http";

      repo = switch (type.toLowerCase()) {
        case "http" -> getHttpRepo(repoId);
        case "file" -> getFileRepo(repoId);
        case "sparql" -> getSparqlRepo(repoId);
        default -> throw new DALException("Invalid graph connection parameters");
      };
      repos.put(repoId, repo);
    }

    return repo;
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
}
