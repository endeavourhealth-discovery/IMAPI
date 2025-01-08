package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class DatasetRepository {
  public List<TTIriRef> searchAllowableDatamodelProperties(String datamodelIri, String searchTerm, int page, int size) throws Exception {
    if (page == 0) page = 1;
    if (size == 0) size = 10;
    int rowNumber = (page - 1) * size;
    List<TTIriRef> results = new ArrayList<>();

    String spql = """
      SELECT ?propertyLabel ?propertyPath WHERE {
        ?dataModel sh:property ?property .
        ?property sh:path ?propertyPath .
        ?propertyPath rdfs:label ?propertyLabel .
        filter contains(?propertyLabel, ?searchTerm)
      }
      LIMIT %s
      OFFSET %s
      """.formatted(size, rowNumber);

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, spql);
      qry.setBinding("dataModel", iri(datamodelIri));
      qry.setBinding("searchTerm", literal(searchTerm));
      qry.setBinding("size", literal(size));
      qry.setBinding("rowNumber", literal(rowNumber));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bindingSet = rs.next();
          results.add(TTIriRef.iri(bindingSet.getValue("propertyPath").stringValue(), bindingSet.getValue("propertyLabel").stringValue()));
        }
      }
    }
    return results;
  }
}
