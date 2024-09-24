package org.endeavourhealth.imapi.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.USER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareUpdateSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Configuration
public class ConfigManager {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);

  public <T> T getConfig(String iri, TypeReference<T> resultType) throws JsonProcessingException {
    LOG.debug("getConfig<TypeReference>");

    try (CachedObjectMapper om = new CachedObjectMapper()) {
      Config config = getConfig(iri);
      if (config == null)
        return null;
      return om.readValue(config.getData(), resultType);
    }
  }

  public Config getConfig(String config) {
    String sql = """
      SELECT ?name ?data WHERE {
        ?s ?label   ?name ;
        ?config  ?data .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
      qry.setBinding("s", Values.iri(config));
      qry.setBinding("label", Values.iri(RDFS.LABEL));
      qry.setBinding("config", Values.iri(IM.HAS_CONFIG));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          return new Config()
            .setName(bs.getValue("name").stringValue())
            .setData(bs.getValue("data").stringValue());
        } else {
          return null;
        }
      }
    }
  }

  public void setConfig(String iri, Config config) {
    insert(iri, RDFS.LABEL, config.getName());
    insert(iri, RDFS.COMMENT, config.getComment());
    insert(iri, IM.HAS_CONFIG, config.getData());
  }

  private String getSparqlInsert() {
    StringJoiner sparql = new StringJoiner(System.lineSeparator()).add("INSERT {").add("  ?s ?p ?o ").add("}").add("WHERE { SELECT ?s ?p ?o {} }");
    return sparql.toString();
  }

  private void insert(String subject, String predicate, String object) {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      String sparql = getSparqlInsert();
      try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
        Update qry = prepareUpdateSparql(conn, sparql);
        qry.setBinding("s", Values.iri(subject));
        qry.setBinding("p", Values.iri(predicate));
        qry.setBinding("o", literal(object));
        qry.execute();
      }
    }
  }


}
