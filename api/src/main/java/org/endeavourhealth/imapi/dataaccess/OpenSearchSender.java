package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class OpenSearchSender {
  private static final Logger LOG = LoggerFactory.getLogger(OpenSearchSender.class);
  private static final int BATCH_SIZE = 5000;
  private final Client client = ClientBuilder.newClient();
  private final ObjectMapper om = new ObjectMapper();
  // Env vars
  private final String osUrl = System.getenv("OPENSEARCH_URL");
  private final String osAuth = System.getenv("OPENSEARCH_AUTH");
  private final String server = System.getenv("GRAPH_SERVER");
  private final String repoId = System.getenv("GRAPH_REPO");
  private final String index = System.getenv("OPENSEARCH_INDEX");
  private final HTTPRepository repo = new HTTPRepository(server, repoId);
  private WebTarget target;
  private String cache;

  public String getCache() {
    return cache;
  }

  public OpenSearchSender setCache(String cache) {
    this.cache = cache;
    return this;
  }

  public void execute(boolean update) throws IOException, InterruptedException {
    om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    checkEnvs();
    checkIndexExists();


    int maxId = 0;
    if (!update)
      maxId = getMaxDocument();

    continueUpload(maxId);
  }


  private void continueUpload(int maxId) throws IOException, InterruptedException {
    target = client.target(osUrl).path("_bulk");
    Set<String> entityIris = new HashSet<>(2_000_000);
    if (maxId > 0 && (cache != null)) {
      getIriCache(entityIris);
    } else {

      String sql = """
        SELECT ?iri
        WHERE {
          ?iri rdfs:label ?name.
          FILTER(isIri(?iri))
        }
        ORDER BY ?iri
        """;
      try (RepositoryConnection conn = repo.getConnection()) {
        LOG.info("Fetching entity iris  ...");
        TupleQuery tupleQuery = conn.prepareTupleQuery(addSparqlPrefixes(sql));

        int count = 0;
        try (TupleQueryResult qr = tupleQuery.evaluate()) {
          while (qr.hasNext()) {
            BindingSet rs = qr.next();
            String iri = rs.getValue("iri").stringValue();
            // validate iri
            try {
              new URI(iri);
            } catch (URISyntaxException e) {
              LOG.error("Invalid IRI [{}]", iri);
              System.exit(-1);
            }
            if (++count % 50000 == 0)
              LOG.info("Loaded {}...", count);
            entityIris.add(iri);
          }
        }
      }
    }
    if (cache != null) {
      saveIriCache(entityIris);
    }

    LOG.info(" Found {} documents...", entityIris.size());

    int mapNumber = 0;
    Iterator<String> mapIterator = entityIris.iterator();

    // Skip to start point
    while (mapNumber < maxId && mapIterator.hasNext()) {
      mapNumber++;
      mapIterator.next();
    }

    // Start processing
    Map<String, EntityDocument> batch = new HashMap<>();

    while (mapIterator.hasNext()) {
      String iri = mapIterator.next();
      mapNumber++;
      EntityDocument doc = new EntityDocument()
        .setId(mapNumber)
        .setIri(iri)
        .setSubsumptionCount(0);
      batch.put(iri, doc);

      // Send every 5000
      if (batch.size() == BATCH_SIZE) {
        LOG.info(" Processing batch up to entity number {}... ", mapNumber);
        getEntityBatch(batch);
        index(batch);
        batch.clear();
      }
    }

    // Handle remaining / last partial batch
    if (!batch.isEmpty()) {
      LOG.info(" Processing final batch up to entity number {}...", mapNumber);
      getEntityBatch(batch);
      index(batch);
    }
  }

  private void saveIriCache(Set<String> entityIris) throws IOException {
    try (FileWriter wr = new FileWriter(cache)) {
      for (String iri : entityIris) {
        wr.write(iri + "\n");
      }
    }
  }

  private void getIriCache(Set<String> entityIris) {
    try (BufferedReader reader = new BufferedReader(new FileReader(cache))) {
      String line = reader.readLine();
      while (line != null && !line.isEmpty()) {
        entityIris.add(line);
        line = reader.readLine();
      }
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }
  }

  private void getEntityBatch(Map<String, EntityDocument> batch) {
    String inList = batch.keySet().stream().map(iri -> ("<" + iri + ">")).collect(Collectors.joining(" "));
    getCore(batch, inList);
    getTermCodes(batch, inList);
    getIsas(batch, inList);
    getBindings(batch, inList);
    getSubsumptions(batch, inList);
    getSetMembership(batch, inList);
  }

  private void getCore(Map<String, EntityDocument> batch, String inList) {
    String sql = getBatchCoreSql(inList);

    try (RepositoryConnection conn = repo.getConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(sql);
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          String iri = rs.getValue("iri").stringValue();
          EntityDocument blob = batch.get(iri);
          String name = rs.getValue("name").stringValue();
          blob.setName(name);
          blob.setUsageTotal(0);

          String code;
          if (rs.getValue("code") != null) {
            code = rs.getValue("code").stringValue();
            blob.setCode(code);
          }
          if (rs.getValue("preferredName") != null) {
            String preferred = rs.getValue("preferredName").stringValue();
            blob.setPreferredName(preferred);
          } else if (name.contains(" (")) {
            blob.setPreferredName(name.split(" \\(")[0]);
          } else
            blob.setPreferredName(name);
          if (rs.getValue("alternativeCode") != null) {
            String alternativeCode = rs.getValue("alternativeCode").stringValue();
            blob.setAlternativeCode(alternativeCode);
          }


          if (rs.hasBinding("scheme")) {
            TTIriRef scheme = iri(rs.getValue("scheme").stringValue());

            if (rs.getValue("schemeName") != null)
              scheme.setName(rs.getValue("schemeName").stringValue());
            blob.setScheme(scheme);
          } else {
            TTIriRef scheme = iri(rs.getValue("graph").stringValue());
            if (rs.getValue("graphName") != null)
              scheme.setName(rs.getValue("graphName").stringValue());
            blob.setScheme(scheme);
          }

          if (rs.getValue("status") != null) {
            TTIriRef status = iri(rs.getValue("status").stringValue());
            if (rs.getValue("statusName") != null)
              status.setName(rs.getValue("statusName").stringValue());
            blob.setStatus(status);
          }

          if (rs.getValue("type") != null) {
            TTIriRef type = iri(rs.getValue("type").stringValue());
            if (rs.getValue("typeName") != null)
              type.setName(rs.getValue("typeName").stringValue());
            blob.addType(type);
          }
          TTIriRef extraType;
          if (rs.getValue("extraType") != null) {
            extraType = iri(rs.getValue("extraType").stringValue());
            extraType.setName(rs.getValue("extraTypeName").stringValue());
            blob.addType(extraType);
            if (extraType.equals(iri(IM.NAMESPACE + "DataModelEntity"))) {
              int usageTotal = 2000000;
              blob.setUsageTotal(usageTotal);
            }
          }
          if (rs.getValue("usageTotal") != null) {
            blob.setUsageTotal(Integer.parseInt(rs.getValue("usageTotal").stringValue()));
          }

          String lengthKey = blob.getPreferredName() != null ? blob.getPreferredName() : name;
          lengthKey = getLengthKey(lengthKey);

          blob.setLength(lengthKey.length());
          blob.addTermCode(name, null, null);
          addMatchTerm(blob, name);
        }

      } catch (Exception e) {
        LOG.error("Bad Query \n{}", sql, e);
        System.exit(-1);
      }
    }
  }


  private String getLengthKey(String lengthKey) {
    if (lengthKey.endsWith(")")) {
      String[] words = lengthKey.split(" \\(");
      StringBuilder lengthKeyBuilder = new StringBuilder(words[0]);
      for (int i = 1; i < words.length - 1; i++) {
        lengthKeyBuilder.append(" (").append(words[i]);
      }
      lengthKey = lengthKeyBuilder.toString();
    }
    return lengthKey;
  }

  private String getBatchCoreSql(String inList) {
    return new StringJoiner(System.lineSeparator())
      .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
      .add("PREFIX im: <http://endhealth.info/im#>")
      .add("PREFIX sh: <http://www.w3.org/ns/shacl#>")
      .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
      .add("select ?iri ?name ?status ?statusName ?code ?scheme ?schemeName ?type ?typeName ?usageTotal")
      .add("?extraType ?extraTypeName ?preferredName ?alternativeCode ?graph ?graphName ?path ?node")
      .add("where {")
      .add("  graph ?graph {")
      .add("    ?iri rdfs:label ?name.")
      .add("    VALUES  ?iri  {" + inList + "}")
      .add("  }")
      .add("  Optional { ?graph rdfs:label ?graphName }")
      .add("  Optional { ?iri rdf:type ?type. Optional {?type rdfs:label ?typeName} }")
      .add("  Optional {?iri im:isA ?extraType.")
      .add("            ?extraType rdfs:label ?extraTypeName.")
      .add("            filter (?extraType in (im:dataModelProperty, im:DataModelEntity))}")
      .add("  Optional {?iri im:preferredName ?preferredName.}")
      .add("  Optional {?iri im:status ?status.")
      .add("  Optional {?status rdfs:label ?statusName} }")
      .add("  Optional {?iri im:scheme ?scheme.")
      .add("  Optional {?scheme rdfs:label ?schemeName } }")
      .add("  Optional {?iri im:code ?code.}")
      .add("  Optional {?iri im:usageTotal ?usageTotal.}")
      .add("  Optional {?iri im:alternativeCode ?alternativeCode.}")
      .add("}").toString();
  }

  private void getTermCodes(Map<String, EntityDocument> batch, String inList) {
    String sql = getBatchTermsSql(inList);
    try (RepositoryConnection conn = repo.getConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(sql);
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          String iri = rs.getValue("iri").stringValue();
          EntityDocument blob = batch.get(iri);
          String termCode = null;
          String synonym = null;
          TTIriRef status = null;
          if (rs.getValue("synonym") != null)
            synonym = rs.getValue("synonym").stringValue().toLowerCase();
          if (rs.getValue("termCode") != null)
            termCode = rs.getValue("termCode").stringValue();

          if (rs.getValue("termCodeStatus") != null)
            status = iri(rs.getValue("termCodeStatus").stringValue());
          if (synonym != null) {
            addMatchTerm(blob, synonym);
            SearchTermCode tc = getTermCode(blob, synonym);
            if (tc == null) {
              blob.addTermCode(synonym, termCode, status);
              addMatchTerm(blob, synonym);
            } else {
              if (termCode != null) {
                tc.setCode(termCode);
              }
            }
          } else if (termCode != null) {
            SearchTermCode tc = getTermCodeFromCode(blob, termCode);
            if (tc == null) {
              blob.addTermCode(null, termCode, status);
            }
          }

        }
      }
    }
  }

  private String getBatchTermsSql(String inList) {
    return new StringJoiner(System.lineSeparator())
      .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
      .add("PREFIX im: <http://endhealth.info/im#>")
      .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
      .add("select ?iri ?termCode ?synonym ?termCodeStatus")
      .add("where {")
      .add("?iri im:hasTermCode ?tc.")
      .add("      VALUES  ?iri  {" + inList + "}")
      .add("       Optional {?tc im:code ?termCode}")
      .add("       Optional  {?tc rdfs:label ?synonym}")
      .add("       Optional  {?tc im:status ?termCodeStatus}")
      .add("}").toString();
  }

  private void getIsas(Map<String, EntityDocument> batch, String inList) {
    String sql = getBatchIsaSql(inList);
    try (RepositoryConnection conn = repo.getConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(sql);
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          String iri = rs.getValue("iri").stringValue();
          EntityDocument blob = batch.get(iri);
          blob.getIsA().add(iri(rs.getValue("superType").stringValue()));
        }
      }
    }

  }

  private void getSubsumptions(Map<String, EntityDocument> batch, String inList) {
    String sql = getSubsumptionSql(inList);
    try (RepositoryConnection conn = repo.getConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(sql);
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          String iri = rs.getValue("iri").stringValue();
          EntityDocument blob = batch.get(iri);
          blob.setSubsumptionCount(Integer.parseInt(rs.getValue("subsumptions").stringValue()));
        }
      }
    }

  }


  private String getBatchIsaSql(String inList) {
    return new StringJoiner(System.lineSeparator())
      .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
      .add("PREFIX im: <http://endhealth.info/im#>")
      .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
      .add("select ?iri ?superType")
      .add("where {")
      .add(" ?iri im:isA ?superType.")
      .add("      VALUES  ?iri  {" + inList + "}")
      .add(" ?superType im:status im:Active.")
      .add("}").toString();
  }

  private String getSubsumptionSql(String inList) {
    return new StringJoiner(System.lineSeparator())
      .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
      .add("PREFIX im: <http://endhealth.info/im#>")
      .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
      .add("select distinct ?iri (count(?subType) as ?subsumptions)")
      .add("where {")
      .add(" ?iri ^im:isA ?subType.")
      .add("      VALUES  ?iri  {" + inList + "}")
      .add(" ?subType im:status im:Active.")
      .add("}")
      .add("group by ?iri").toString();
  }

  private void getSetMembership(Map<String, EntityDocument> batch, String inList) {
    String sql = getSetMembershipSql(inList);
    try (RepositoryConnection conn = repo.getConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(sql);
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          String iri = rs.getValue("iri").stringValue();
          EntityDocument blob = batch.get(iri);
          blob.getMemberOf().add(iri(rs.getValue("set").stringValue()));
        }

      }
    }

  }


  private void getBindings(Map<String, EntityDocument> batch, String inList) {
    String sql = getBindingsSql(inList);
    try (RepositoryConnection conn = repo.getConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(sql);
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          String iri = rs.getValue("iri").stringValue();
          EntityDocument blob = batch.get(iri);
          blob.addBinding(rs.getValue("path").stringValue(), rs.getValue("node").stringValue());
        }

      }
    }

  }

  private String getSetMembershipSql(String inList) {
    return new StringJoiner(System.lineSeparator())
      .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
      .add("PREFIX im: <http://endhealth.info/im#>")
      .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
      .add("select ?iri ?set")
      .add("where {")
      .add(" ?set im:hasMember ?iri.")
      .add("      VALUES  ?iri  {" + inList + "}")
      .add("}").toString();
  }

  private String getBindingsSql(String inList) {
    return new StringJoiner(System.lineSeparator())
      .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
      .add("PREFIX im: <http://endhealth.info/im#>")
      .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
      .add("PREFIX sh: <http://www.w3.org/ns/shacl#>")
      .add("select ?iri ?path ?node")
      .add("where {{")
      .add(" ?iri im:binding ?binding.")
      .add(" ?binding sh:path ?path.")
      .add(" ?binding sh:node ?node.}")
      .add("union {")
      .add("?iri ^im:hasMember ?vset.")
      .add("?vset im:binding ?binding.")
      .add(" ?binding sh:path ?path.")
      .add(" ?binding sh:node ?node.")
      .add("}")
      .add("VALUES  ?iri  {" + inList + "}")
      .add("}").toString();
  }


  private void addMatchTerm(EntityDocument blob, String term) {
    term = term.replaceAll("[ '()\\-_./,]", "").toLowerCase();
    if (term.length() > 30)
      term = term.substring(0, 30);
    blob.addMatchTerm(term);
  }

  private SearchTermCode getTermCode(EntityDocument blob, String term) {
    Optional<SearchTermCode> match = blob.getTermCode().stream().filter(tc -> tc.getTerm() != null && tc.getTerm().equals(term)).findFirst();

    return match.orElse(null);
  }

  private SearchTermCode getTermCodeFromCode(EntityDocument blob, String code) {
    Optional<SearchTermCode> match = blob.getTermCode().stream().filter(tc -> tc.getCode() != null && tc.getCode().contains(code)).findFirst();

    return match.orElse(null);
  }


  private void checkEnvs() {
    boolean missingEnvs = false;

    for (String env : Arrays.asList("OPENSEARCH_AUTH", "OPENSEARCH_URL", "OPENSEARCH_INDEX", "GRAPH_SERVER", "GRAPH_REPO")) {
      String envData = System.getenv(env);
      if (envData == null || envData.isEmpty()) {
        LOG.error("Environment variable {} not set", env);
        missingEnvs = true;
      }
    }

    if (missingEnvs)
      System.exit(-1);
  }

  private void index(Map<String, EntityDocument> docs) throws JsonProcessingException, InterruptedException {
    StringJoiner batch = new StringJoiner("\n");
    for (EntityDocument doc : docs.values()) {
      batch.add("{ \"create\" : { \"_index\": \"" + index + "\", \"_id\" : \"" + doc.getId() + "\" } }");
      batch.add(om.writeValueAsString(doc));
    }
    batch.add("\n");
    upload(batch.toString());
  }

  private void upload(String batch) throws InterruptedException {
    boolean retry;
    int retrySleep = 5;
    boolean done = false;
    int tries = 0;
    Entity<String> entity = Entity.entity(batch, MediaType.APPLICATION_JSON);
    Invocation.Builder req = target.request().header("Authorization", "Basic " + osAuth);
    while (!done && tries < 5) {
      try {
        tries++;
        do {
          LOG.info("Sending batch to Open search ...");
          retry = false;
          try (Response response = req.post(entity)) {

            if (response.getStatus() == 429) {
              retry = true;
              LOG.error("Queue full, retrying in {}s", retrySleep);
              TimeUnit.SECONDS.sleep(retrySleep);

              if (retrySleep < 60)
                retrySleep = retrySleep * 2;
            } else if (response.getStatus() == 403) {
              String responseData = response.readEntity(String.class);
              LOG.error(responseData);
              LOG.error("Potentially out of disk space");
              throw new IllegalStateException("Potential disk full");
            } else if (response.getStatus() != 200 /*&& response.getStatus() != 201*/) {
              String responseData = response.readEntity(String.class);
              LOG.error(responseData);
              throw new IllegalStateException("Error posting to OpenSearch");
            } else {
              retrySleep = 5;
              // Check for errors
              String responseData = response.readEntity(String.class);
              JsonNode responseJson = om.readTree(responseData);
              if (responseJson.has("errors") && responseJson.get("errors").asBoolean()) {
                LOG.error(responseData);
                throw new IllegalStateException("Error in batch");
              }
            }
          }
        } while (retry);
        LOG.info("Done.");
        done = true;
      } catch (Exception e) {
        LOG.error(e.getMessage());
        e.printStackTrace();
      }
    }
    if (tries == 5)
      throw new InterruptedException("Connectivity problem to open search");
  }

  private int getMaxDocument() throws IOException {
    target = client.target(osUrl).path(index + "/_search");

    try (Response response = target
      .request()
      .header("Authorization", "Basic " + osAuth)
      .post(Entity.entity("""
        {
            "aggs" : {
              "max_id" : {
                "max" : {
                  "field" : "id"
                }
              }
            },
            "size":0
          }""", MediaType.APPLICATION_JSON))) {

      if (response.getStatus() != 200) {
        String responseData = response.readEntity(String.class);
        if (responseData.contains("index_not_found_exception")) {
          LOG.info("No documents, starting from zero");
          return 0;
        } else {
          LOG.error(responseData);
          throw new IllegalStateException("Error getting max document id from OpenSearch");
        }
      } else {
        String responseData = response.readEntity(String.class);
        JsonNode root = om.readTree(responseData);
        int maxId = root.get("aggregations").get("max_id").get("value").asInt();
        if (maxId > 0)
          LOG.info("Continuing from {}", maxId);
        return maxId;
      }
    }
  }

  private void checkIndexExists() {

    target = client.target(osUrl).path(index);

    try (Response response = target
      .request()
      .header("Authorization", "Basic " + osAuth)
      .head()) {

      if (response.getStatus() != 200) {
        LOG.info("{} does not exist - creating index and default mappings", index);
        target = client.target(osUrl).path(index);
        String settings = "{\"settings\": {\n" +
          "    \"analysis\": {\n" +
          "      \"filter\": {\n" +
          "        \"edge_ngram_filter\": {\n" +
          "          \"type\": \"edge_ngram\",\n" +
          "          \"min_gram\": 1,\n" +
          "          \"max_gram\": 20\n" +
          "        }\n" +
          "      },\n" +
          "      \"analyzer\": {\n" +
          "        \"autocomplete\": {\n" +
          "          \"type\": \"custom\",\n" +
          "          \"tokenizer\": \"standard\",\n" +
          "          \"filter\": [\n" +
          "            \"lowercase\",\n" +
          "            \"edge_ngram_filter\"\n" +
          "          ]\n" +
          "        }\n" +
          "      }\n" +
          "    }\n" +
          "  },";
        String mappings = settings + """
                            
            "mappings": {
              "properties": {
                "scheme": {
                  "properties": {
                    "@id": {
                    "type": "keyword"
                      },
                    "name" : {
                      "type": "text"
                    }
                    }
                  },
                "matchTerm": {
                    "type" : "text"
                },
                "entityType" : {
                  "properties" : {
                    "@id": {
                      "type": "keyword"
                    },
                    "name" : {
                      "type" : "text"
                    }
                  }
                },
                "status": {
                  "properties" : {
                    "@id": {
                      "type": "keyword"
                    },
                    "name" : {
                      "type" : "text"
                    }
                  }
                },
                "isA": {
                  "properties" : {
                    "@id": {
                      "type": "keyword"
                    },
                    "name" : {
                      "type" : "text"
                    }
                  }
                },
                "usageTotal": {
                  "type" : "integer"
                },
                "memberOf": {
                  "properties" : {
                  "@id": {
                    "type": "keyword"
                  },
                  "name" : {
                    "type" : "text"
                  }
                  }
                },
                "code": {
                  "type": "keyword"
                },
                "iri": {
                  "type": "keyword"
                },
                "subsumptionCount" : {
                  "type" : "integer"
                },
                "length" : {
                  "type" : "integer"
                },
                "binding": {
                  "type": "keyword"
                },
                "termCode" : {
                  "properties" : {
                    "code" : {
                      "type" : "text"
                    },
                    "term" : {
                      "type" : "text",
                      "analyzer": "autocomplete"
                    },
                    "status" : {
                        "properties" : {
                          "@id" : {
                            "type" : "keyword"
                          },
                          "name" : {
                            "type" : "text"
                          }
                        }
                      }
                  }
                }
              }
            }
          }
                            
          """;

        try (Response createResponse = target
          .request()
          .header("Authorization", "Basic " + osAuth)
          .put(Entity.entity(mappings, MediaType.APPLICATION_JSON))) {
          LOG.info("Index check {}", createResponse.getStatus());
        }
      }
    }
  }


}
