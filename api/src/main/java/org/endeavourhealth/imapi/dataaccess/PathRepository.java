package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.imq.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;

public class PathRepository {
  private final PathDocument document = new PathDocument();
  private RepositoryConnection conn;

  public PathDocument pathQuery(PathQuery pathQuery) {
    try (RepositoryConnection connLocal = ConnectionManager.getIMConnection()) {
      this.conn = connLocal;
      String targetIri = pathQuery.getTarget().getIri();
      String source = pathQuery.getSource().getIri();
      List<Match> paths = getPaths(source, targetIri);
      document.setMatch(paths);
    }
    return document;
  }

  private List<Match> getPaths(String source, String target) {
    List<Match> result = new ArrayList<>();
    String sql = """
       select ?path ?pathLabel ?where ?whereLabel ?target ?targetName
            where {
            ?target rdfs:label ?targetName.
          {
              ?target ^sh:path ?property.
              ?where ^sh:path ?property.
              ?where rdfs:label ?whereLabel.
              ?property ^sh:property ?source.
          }
          union
          {
              ?target ^sh:path ?property.
              ?where ^sh:path ?property.
              ?where rdfs:label ?whereLabel.
              ?property ^sh:property ?recordType.
              ?recordType ^sh:node ?recordProperty.
              ?recordProperty sh:path ?path.
              ?path rdfs:label ?pathLabel.
              ?recordProperty ^sh:property ?source.
          }
          union
          {
                ?target ^im:hasMember ?valueSet.
                ?valueSet ^sh:class ?property.
                ?property sh:path ?where.
                ?where rdfs:label ?whereLabel.
                ?property ^sh:property ?source.
          }
          union {
                ?target ^im:hasMember ?valueSet.
                ?valueSet ^sh:class ?property.
                ?property sh:path ?where.
                ?where rdfs:label ?whereLabel.
                ?property ^sh:property ?recordType.
                ?recordType rdfs:label ?recordTypeLabel.
                ?recordType ^sh:node ?recordProperty.
                ?recordProperty sh:path ?path.
                ?path rdfs:label ?pathLabel.
                ?recordProperty ^sh:property ?source.
              }
           union
          {
                 ?target im:isA ?class.
                ?class ^sh:class ?property.
                ?property sh:path ?where.
                ?where rdfs:label ?whereLabel.
                ?property ^sh:property ?source.
          }
          union {
                ?target im:isA ?class.
                ?class ^sh:class ?property.
                ?property sh:path ?where.
                ?where rdfs:label ?whereLabel.
                ?property ^sh:property ?recordType.
                ?recordType rdfs:label ?recordTypeLabel.
                ?recordType ^sh:node ?recordProperty.
                ?recordProperty sh:path ?path.
                ?path rdfs:label ?pathLabel.
                ?recordProperty ^sh:property ?source.
              }
      }
      group by ?path ?pathLabel ?where ?whereLabel ?target  ?targetName    
      """;
    //The logic is to look for a target as a record types, properties, value sets or concepts linked to the source.
    TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
    qry.setBinding("target", iri(target));
    qry.setBinding("source", iri(source));
    try (TupleQueryResult rs = qry.evaluate()) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        Match match = new Match();
        result.add(match);
        String pathVariable = null;
        if (bs.getValue("path") != null) {
          String pathIri = bs.getValue("path").stringValue();
          pathVariable = pathIri.substring(pathIri.lastIndexOf("#") + 1);
          match.setPath(new Path().setIri(pathIri)
            .setName(bs.getValue("pathLabel").stringValue()));
        }
        if (bs.getValue("where") != null) {
          match.addWhere(new Where()
            .setIri(bs.getValue("where").stringValue())
            .setName(bs.getValue("whereLabel").stringValue())
            .addIs(new Node().setIri(target).setName(bs.getValue("targetName").stringValue())
              .setDescendantsOrSelfOf(true)));
          if (pathVariable != null && match.getWhere() != null)
            match.getWhere().get(0).setNodeRef(pathVariable);
        }
      }
    }
    return result;
  }
}

