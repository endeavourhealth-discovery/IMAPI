package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class EntityTctRepository {
  public List<TTIriRef> findAncestorsByType(String childIri, String relationshipIri, List<String> candidateAncestorIris) {
    List<TTIriRef> result = new ArrayList<>();
    String sql = """
      SELECT ?aname
      WHERE {
        ?c ?r ?a .
        ?a rdfs:label ?aname .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      qry.setBinding("c", Values.iri(childIri));
      qry.setBinding("r", Values.iri(relationshipIri));

      for (String candidate : candidateAncestorIris) {
        qry.setBinding("a", Values.iri(candidate));
        try (TupleQueryResult rs = qry.evaluate()) {
          if (rs.hasNext()) {
            BindingSet bs = rs.next();
            result.add(new TTIriRef()
              .setIri(candidate)
              .setName(bs.getValue("aname").stringValue())
            );
          }
        }
      }
    }
    return result;
  }
}
