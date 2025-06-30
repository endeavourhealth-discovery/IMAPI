package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.model.imq.*;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class PathRepository {
  private final PathDocument document = new PathDocument();

  public PathDocument pathQuery(PathQuery pathQuery) {
    try (RepositoryConnection conn = IMDB.getConnection()) {
      String targetIri = pathQuery.getTarget().getIri();
      String source = pathQuery.getSource().getIri();
      List<Match> paths = getPaths(conn, source, targetIri, pathQuery.getGraph());
      document.setMatch(paths);
    }
    return document;
  }

  private List<Match> getPaths(RepositoryConnection conn, String source, String target, String graph) {
    List<Match> result = new ArrayList<>();
    String sql = """
      select ?path ?pathLabel ?path2 ?path2Label ?where ?whereLabel ?target ?targetName
      FROM ?g
      where {
        ?target rdfs:label ?targetName.
        {
          ?target ^sh:path ?property.
          ?path2 ^sh:path ?property.
          ?path2 rdfs:label ?path2Label.
          ?property ^sh:property ?source.
        }
        union {
          ?target ^sh:path ?property.
          ?path2 ^sh:path ?property.
          ?path2 rdfs:label ?path2Label.
          ?property ^sh:property ?recordType.
          ?recordType ^sh:node ?recordProperty.
          ?recordProperty sh:path ?path.
          ?path rdfs:label ?pathLabel.
          ?recordProperty ^sh:property ?source.
        }
        union {
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
        union {
          ?target im:isA ?class.
          ?class ^sh:class ?property.
          ?property sh:path ?path2.
          ?path2 rdfs:label ?path2Label.
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
      group by ?path ?pathLabel ?path2 ?path2Label ?where ?whereLabel ?target ?targetName
      """;
    //The logic is to look for a target as a record types, properties, value sets or concepts linked to the source.
    TupleQuery qry = IMDB.prepareTupleSparql(conn, sql, graph);
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
          Path pathMatch = new Path().setIri(pathIri).setName(bs.getValue("pathLabel").stringValue());
          match.addPath(pathMatch);
        }
        if (bs.getValue("path2") != null) {
          String pathIri = bs.getValue("path2").stringValue();
          match.getPath().getFirst().addPath(new Path().setIri(pathIri).setName(bs.getValue("path2Label").stringValue()));
          pathVariable = pathIri.substring(pathIri.lastIndexOf("#") + 1);
        }
        if (bs.getValue("where") != null) {
          match.setWhere(new Where()
            .setIri(bs.getValue("where").stringValue())
            .setName(bs.getValue("whereLabel").stringValue())
            .addIs(new Node().setIri(target).setName(bs.getValue("targetName").stringValue())
              .setDescendantsOrSelfOf(true)));
          if (pathVariable != null && match.getWhere() != null)
            match.getWhere().setNodeRef(pathVariable);
        }
      }
    }
    return result;
  }
}

