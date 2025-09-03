package org.endeavourhealth.imapi.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.endeavourhealth.imapi.dataaccess.databases.ConfigDB;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.VocabEnum;
import org.springframework.context.annotation.Configuration;

import static org.eclipse.rdf4j.model.util.Values.literal;

@Slf4j
@Configuration
public class ConfigManager {
  public <T> T getConfig(CONFIG iri, TypeReference<T> resultType) throws JsonProcessingException {
    log.debug("getConfig<TypeReference>");

    try (CachedObjectMapper om = new CachedObjectMapper()) {
      Config config = getConfig(iri);
      if (config == null)
        return null;
      return om.readValue(config.getData(), resultType);
    }
  }

  public Config getConfig(CONFIG config) {
    String sql = """
      SELECT ?name ?data
      WHERE {
        ?s ?label ?name ;
        ?config ?data .
      }
      """;

    try (ConfigDB conn = ConfigDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("s", config.asDbIri());
      qry.setBinding("label", CONFIG.LABEL.asDbIri());
      qry.setBinding("config", CONFIG.HAS_CONFIG.asDbIri());
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

  public void setConfig(CONFIG iri, Config config) {
    insert(iri, CONFIG.LABEL, config.getName());
    insert(iri, CONFIG.COMMENT, config.getComment());
    insert(iri, CONFIG.HAS_CONFIG, config.getData());
  }

  private void insert(CONFIG subject, VocabEnum predicate, String object) {
    if (null == subject || null == predicate)
      throw new IllegalArgumentException("Subject or Predicate cannot be null");
    try (CachedObjectMapper om = new CachedObjectMapper();
         ConfigDB conn = ConfigDB.getConnection()) {

      String query = """
        DELETE {
            ?s ?p ?oAny
        }
        INSERT {
            ?s ?p ?o
        }
        """;

      query += (null == getConfig(subject))
        ? "WHERE {}"
        : "WHERE { ?s ?p ?oAny }";

      Update qry = conn.prepareUpdateSparql(query, Graph.CONFIG);
      qry.setBinding("s", subject.asDbIri());
      qry.setBinding("p", predicate.asDbIri());
      qry.setBinding("o", literal(object));
      qry.execute();
    }
  }
}
