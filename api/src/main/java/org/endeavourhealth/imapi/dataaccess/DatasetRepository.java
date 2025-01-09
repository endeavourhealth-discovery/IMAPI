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
      SELECT DISTINCT ?label ?path WHERE {
         {
             ?dataModel sh:property ?property .
             ?property sh:path ?propertyPath .
             ?propertyPath rdfs:label ?propertyLabel .
             BIND(?propertyLabel AS ?label)
             BIND(?propertyPath AS ?path)
         }
         UNION {
             ?property sh:node ?subDataModel .
             ?subDataModel sh:property ?subProperty .
             ?subProperty sh:path ?subPropertyPath .
             ?subPropertyPath rdfs:label ?subPropertyLabel .
             BIND(?subPropertyLabel AS ?label)
             BIND(?subPropertyPath AS ?path)
         }
         UNION {
             ?subProperty sh:node ?sub2DataModel .
             ?sub2DataModel sh:property ?sub2Property .
             ?sub2Property sh:path ?sub2PropertyPath .
             ?sub2PropertyPath rdfs:label ?sub2PropertyLabel .
             BIND(?sub2PropertyLabel AS ?label)
             BIND(?sub2PropertyPath AS ?path)
         }
         FILTER CONTAINS(?label, ?searchTerm)
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, spql);
      qry.setBinding("dataModel", iri(datamodelIri));
      qry.setBinding("searchTerm", literal(searchTerm));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bindingSet = rs.next();
          results.add(TTIriRef.iri(bindingSet.getValue("path").stringValue(), bindingSet.getValue("label").stringValue()));
        }
      }
    }
    return results;
  }
}
