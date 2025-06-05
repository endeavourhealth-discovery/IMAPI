package org.endeavourhealth.imapi.logic.reasoner;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class DomainResolver {

  public void updateDomains() {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      Set<String> domains = getDomains(conn);
      for (String domain : domains) {
        updateDomain(conn,domain);
      }
    }
  }

  private void updateDomain(RepositoryConnection conn, String domain) {
    String sql= """
      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
      PREFIX im: <http://endhealth.info/im#>
      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
      select distinct ?extra
      where {
          Values ?domain {%s}
           ?subtype im:isA ?domain.
          ?subtype im:roleGroup ?rg.
          ?rg ?extra ?value.
          ?value rdf:type im:Concept.
          filter exists {
              ?extra im:isA ?property.
              ?property rdfs:domain ?domain.
          }
      }
      """.formatted("<"+domain+">");
    Set<String> missingProperties= new HashSet<>();
    TupleQuery missingPropertyQuery= conn.prepareTupleQuery(sql);
    try (TupleQueryResult missingResults = missingPropertyQuery.evaluate()) {
      while (missingResults.hasNext()) {
        BindingSet bs = missingResults.next();
        missingProperties.add(bs.getValue("extra").stringValue());
      }
    }
    StringJoiner insertSql= new StringJoiner("\n");
    insertSql.add("PREFIX im: <http://endhealth.info/im#>");
    insertSql.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
    insertSql.add("INSERT DATA{");
    missingProperties.forEach(p->insertSql.add(" <"+p+"> rdfs:domain <"+domain+">."));
    insertSql.add("}");
    String insertQuery= insertSql.toString();
    conn.prepareUpdate(insertQuery).execute();
  }

  private Set<String> getDomains(RepositoryConnection conn) {
    Set<String> domains = new HashSet<>();
    String sql= """
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        select distinct ?domain where {
        	?property rdfs:domain ?domain
        }
        """;
    TupleQuery domainQuery= conn.prepareTupleQuery(sql);
    try (TupleQueryResult domainResults = domainQuery.evaluate()) {
      while (domainResults.hasNext()) {
        BindingSet bs = domainResults.next();
        domains.add(bs.getValue("domain").stringValue());
      }
    }
    return domains;
  }
}
