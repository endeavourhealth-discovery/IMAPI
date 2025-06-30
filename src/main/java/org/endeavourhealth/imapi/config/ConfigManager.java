package org.endeavourhealth.imapi.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.databases.ConfigDB;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.context.annotation.Configuration;

import static org.eclipse.rdf4j.model.util.Values.literal;

@Slf4j
@Configuration
public class ConfigManager {

  private String DELETE_INSERT_SPARQL = """
    DELETE {
      GRAPH ?g {
        ?s ?p ?oAny
      }
    }
    INSERT {
      GRAPH ?g {
        ?s ?p ?o
      }
    }
    WHERE {
      GRAPH ?g {
        ?s ?p ?oAny
      }
    }
    """;
  private String INSERT_SPARQL = """
    DELETE {
      GRAPH ?g {
        ?s ?p ?oAny
      }
    }
    INSERT {
      GRAPH ?g {
        ?s ?p ?o
      }
    }
    WHERE {}
    """;

  public <T> T getConfig(String iri, TypeReference<T> resultType) throws JsonProcessingException {
    log.debug("getConfig<TypeReference>");

    try (CachedObjectMapper om = new CachedObjectMapper()) {
      Config config = getConfig(iri);
      if (config == null)
        return null;
      return om.readValue(config.getData(), resultType);
    }
  }

  public Config getConfig(String config) {
    String sql = """
      SELECT ?name ?data
      FROM ?g
      WHERE {
        ?s ?label ?name ;
        ?config ?data .
      }
      """;

    try (RepositoryConnection conn = ConfigDB.getConnection()) {
      TupleQuery qry = ConfigDB.prepareTupleSparql(conn, sql);
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

  private void insert(String subject, String predicate, String object) {
    if (null == subject || subject.isEmpty() || null == predicate || predicate.isEmpty())
      throw new IllegalArgumentException("Subject or Predicate cannot be null");
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      try (RepositoryConnection conn = ConfigDB.getConnection()) {
        String query = DELETE_INSERT_SPARQL;
        if (null == getConfig(subject)) query = INSERT_SPARQL;
        Update qry = ConfigDB.prepareUpdateSparql(conn, query);
        qry.setBinding("s", Values.iri(subject));
        qry.setBinding("p", Values.iri(predicate));
        qry.setBinding("o", literal(object));
        qry.execute();
      }
    }
  }


}
