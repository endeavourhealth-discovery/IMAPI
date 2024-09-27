package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;

public class DataModelRepository {
  public List<TTIriRef> getProperties() {
    List<TTIriRef> result = new ArrayList<>();

    String spql = """
      SELECT ?s ?name
      WHERE {
        ?s rdf:type rdf:PropertyRef ;
        rdfs:label ?name .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, spql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
        }
      }
    }

    return result;
  }


  public List<TTIriRef> findDataModelsFromProperty(String propIri) {
    List<TTIriRef> dmList = new ArrayList<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sparql = """
        SELECT ?dm ?dmName
        WHERE {
          ?dm sh:property ?prop .
          ?dm rdfs:label ?dmName .
          ?prop sh:path ?propIri .
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sparql));
      qry.setBinding("propIri", iri(propIri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          dmList.add(new TTIriRef(bs.getValue("dm").stringValue(), bs.getValue("dmName").stringValue()));
        }
      }
    }
    return dmList;

  }

  public String checkPropertyType(String propIri) {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String query = """
        SELECT ?objectProperty ?dataProperty
        WHERE {"
          bind(exists{?propIri ?isA ?objProp} as ?objectProperty)
          bind(exists{?propIri ?isA ?dataProp} as ?dataProperty)
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(query);
      qry.setBinding("propIri", iri(propIri));
      qry.setBinding("isA", iri(IM.IS_A));
      qry.setBinding("objProp", iri(IM.DATAMODEL_OBJECTPROPERTY));
      qry.setBinding("dataProp", iri(IM.DATAMODEL_DATAPROPERTY));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (bs.hasBinding("objectProperty")) return IM.DATAMODEL_OBJECTPROPERTY;
          else if (bs.hasBinding("dataProperty")) return IM.DATAMODEL_DATAPROPERTY;
        }
      }
    }
    return null;
  }
}
