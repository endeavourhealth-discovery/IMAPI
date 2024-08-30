package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;

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
      select ?where ?whereLabel ?recordType ?recordTypeLabel ?path ?pathLabel ?where1 ?where1Label
      where {
        {
          ?target sh:property ?property.
          ?property sh:path ?path.
          ?path rdfs:label ?pathLabel.
          ?property sh:node ?source.
          ?property ^sh:property ?recordType.
          ?recordType rdfs:label ?recordTypeLabel.
        } union {
          ?target ^sh:path ?property.
          ?property sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?property ^sh:property ?source.
        } union {
          ?target ^sh:path ?subProperty.
          ?subProperty sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?subProperty ^sh:property ?recordType.
          ?recordType rdfs:label ?recordTypeLabel.
          ?recordType sh:property ?recordProperty.
          ?recordProperty sh:path ?path.
          ?path rdfs:label ?pathLabel.
          ?recordProperty sh:node ?source.
        } union {
          ?target ^sh:path ?subProperty.
          ?subProperty sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?subProperty ^sh:property ?recordType.
          ?recordType rdfs:label ?recordTypeLabel.
          ?recordType ^sh:node ?recordProperty.
          ?recordProperty sh:path ?where1.
          ?where1 rdfs:label ?where1Label.
          ?recordProperty ^sh:property ?source.
        } union {
          ?target ^im:hasMember ?valueSet.
          ?valueSet ^sh:class ?property.
          ?property sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?property ^sh:property ?source.
        } union {
          ?target ^im:hasMember ?valueSet.
          ?valueSet ^sh:class ?property.
          ?property sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?property ^sh:property ?recordType.
          ?recordType rdfs:label ?recordTypeLabel.
          ?recordType sh:property ?recordProperty.
          ?recordProperty sh:path ?path.
          ?path rdfs:label ?pathLabel.
          ?recordProperty sh:node ?source.
        } union {
          ?target ^sh:class ?property.
          ?property sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?property ^sh:property ?recordType.
          ?recordType rdfs:label ?recordTypeLabel.
          ?recordType sh:property ?recordProperty.
          ?recordProperty sh:path ?path.
          ?path rdfs:label ?pathLabel.
          ?recordProperty sh:node ?source.
        } union {
          ?target im:binding ?datamodel.
          ?datamodel sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?datamodel sh:node ?recordType.
          ?recordType rdfs:label ?recordTypeLabel.
          ?recordType sh:property ?recordProperty.
          ?recordProperty sh:path ?path.
          ?path rdfs:label ?pathLabel.
          ?recordProperty sh:node ?source.
        }
      }
      group by ?where ?whereLabel  ?recordType ?recordTypeLabel ?path ?pathLabel ?where1 ?where1Label
      """;
    //The logic is to look for a target as a record types, properties, value sets or concepts linked to the source.
    TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
    qry.setBinding("target", iri(target));
    qry.setBinding("source", iri(source));
    try (TupleQueryResult rs = qry.evaluate()) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        Match match = new Match();
        if (bs.getValue("where1") != null) {
          Match superMatch = new Match();
          result.add(superMatch);
          superMatch.addWhere(new Where().setIri(bs.getValue("where1").stringValue())
            .setName(bs.getValue("where1Label").stringValue())
            .setMatch(match));
        } else
          result.add(match);
        if (bs.getValue("recordType") != null) {
          match.setTypeOf(new Node().setIri(bs.getValue("recordType").stringValue())
            .setName(bs.getValue("recordTypeLabel").stringValue()));
        }
        if (bs.getValue("where") != null) {
          match.addWhere(new Where()
            .setIri(bs.getValue("where").stringValue())
            .setName(bs.getValue("whereLabel").stringValue()));
        }
        if (bs.getValue("path") != null) {
          match.addPath(new IriLD().setIri(bs.getValue("path").stringValue())
            .setName(bs.getValue("pathLabel").stringValue()));
        }

      }
    }
    sortWherePaths(result);
    return result;
  }

  private void sortWherePaths(List<Match> result) {
    Set<Match> remove = new HashSet<>();
    for (int i = 0; i < result.size(); i++) {
      Match pathMatch = result.get(i);
      if (pathMatch.getPath() != null) {
        for (int q = i + 1; q < result.size(); q++) {
          Match hasWhere = result.get(q);
          if (hasWhere.getWhere() != null && hasWhere.getWhere().get(0).getMatch() != null && hasWhere.getWhere().get(0).getMatch().getTypeOf().equals(pathMatch.getTypeOf()))
            remove.add(pathMatch);
        }
      }
    }
    if (!remove.isEmpty())
      for (Match match : remove)
        result.remove(match);
  }
}