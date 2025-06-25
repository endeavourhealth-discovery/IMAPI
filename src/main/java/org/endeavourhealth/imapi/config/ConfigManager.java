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
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.context.annotation.Configuration;

import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareTupleSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareUpdateSparql;

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

  public <T> T getConfig(String iri, TypeReference<T> resultType, String graph) throws JsonProcessingException {
    log.debug("getConfig<TypeReference>");

    try (CachedObjectMapper om = new CachedObjectMapper()) {
      Config config = getConfig(iri, graph);
      if (config == null)
        return null;
      return om.readValue(config.getData(), resultType);
    }
  }

  public Config getConfig(String config, String graph) {
    String sql = """
      SELECT ?name ?data
      FROM ?g
      WHERE {
        ?s ?label ?name ;
        ?config ?data .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
      TupleQuery qry = prepareTupleSparql(conn, sql, graph);
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

  public void setConfig(String iri, Config config, String graph) {
    insert(iri, RDFS.LABEL, config.getName(), graph);
    insert(iri, RDFS.COMMENT, config.getComment(), graph);
    insert(iri, IM.HAS_CONFIG, config.getData(), graph);
  }

  private void insert(String subject, String predicate, String object, String graph) {
    if (null == subject || subject.isEmpty() || null == predicate || predicate.isEmpty())
      throw new IllegalArgumentException("Subject or Predicate cannot be null");
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
        String query = DELETE_INSERT_SPARQL;
        if (null == getConfig(subject, graph)) query = INSERT_SPARQL;
        Update qry = prepareUpdateSparql(conn, query, graph);
        qry.setBinding("s", Values.iri(subject));
        qry.setBinding("p", Values.iri(predicate));
        qry.setBinding("o", literal(object));
        qry.execute();
      }
    }
  }


}
