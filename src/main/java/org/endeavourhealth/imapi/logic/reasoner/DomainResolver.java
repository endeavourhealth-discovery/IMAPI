package org.endeavourhealth.imapi.logic.reasoner;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Slf4j
public class DomainResolver {

  public void updateDomains(Graph insertGraph) {
    try (IMDB conn = IMDB.getConnection()) {
      Set<String> domains = getDomains(conn);
      for (String domain : domains) {
        updateDomain(conn, domain, insertGraph);
      }
    }
  }

  private void updateDomain(IMDB conn, String domain, Graph insertGraph) {
    String sql = """
      select distinct ?property
      where {
         values ?domain {%s}
        ?property rdfs:domain ?domain
      }
      
      """.formatted("<" + domain + ">");
    Set<String> currentProperties = new HashSet<>();
    TupleQuery currentPropertyQuery = conn.prepareTupleSparql(sql);
    log.info("domain resolver adding missing properties for " + domain + "...");
    try (TupleQueryResult currentResults = currentPropertyQuery.evaluate()) {
      while (currentResults.hasNext()) {
        BindingSet bs = currentResults.next();
        currentProperties.add(bs.getValue("property").stringValue());
      }
    }
    String current = String.join(",", currentProperties.stream().map(p -> "<" + p + ">").toArray(String[]::new));
    sql = """
      select distinct ?property
      where {
          Values ?domain {%s}
           ?subtype im:isA ?domain.
          ?subtype im:roleGroup ?rg.
          ?rg ?property ?value.
          ?value rdf:type im:Concept.
      }
      """.formatted("<" + domain + ">", current);
    Set<String> allProperties = new HashSet<>();
    TupleQuery allPropertyQuery = conn.prepareTupleSparql(sql);
    try (TupleQueryResult allResults = allPropertyQuery.evaluate()) {
      while (allResults.hasNext()) {
        BindingSet bs = allResults.next();
        allProperties.add(bs.getValue("property").stringValue());
      }
    }
    Set<String> missingProperties = new HashSet<>(allProperties);
    missingProperties.removeAll(currentProperties);
    if (!missingProperties.isEmpty()) {
      StringJoiner insertSql = new StringJoiner("\n");
      insertSql.add("INSERT DATA{");
      missingProperties.forEach(p -> insertSql.add(" <" + p + "> rdfs:domain <" + domain + ">."));
      insertSql.add("}");
      String insertQuery = insertSql.toString();
      conn.prepareInsertSparql(insertQuery, insertGraph).execute();
    }
  }

  private Set<String> getDomains(IMDB conn) {
    Set<String> domains = new HashSet<>();
    String sql = """
      select distinct ?domain where {
      	?property rdfs:domain ?domain
      }
      """;
    TupleQuery domainQuery = conn.prepareTupleSparql(sql);
    try (TupleQueryResult domainResults = domainQuery.evaluate()) {
      while (domainResults.hasNext()) {
        BindingSet bs = domainResults.next();
        domains.add(bs.getValue("domain").stringValue());
      }
    }
    return domains;
  }
}
