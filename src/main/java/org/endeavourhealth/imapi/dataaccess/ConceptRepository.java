package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.ConceptContextMap;
import org.endeavourhealth.imapi.model.Context;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.getString;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.valueList;

public class ConceptRepository {

  public List<SimpleMap> getMatchedFrom(String iri, List<String> schemeIris) {
    List<SimpleMap> simpleMaps = new ArrayList<>();
    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?s ?code ?scheme ?name
      WHERE{
        ?s im:matchedTo ?o .
        ?s im:code ?code .
        ?s im:scheme ?scheme .
        GRAPH ?g { ?s rdfs:label ?name } .
      """);

    if (schemeIris != null && !schemeIris.isEmpty()) {
      sql.add(valueList("g", schemeIris));
    }
    sql.add("}");
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql.toString());
      qry.setBinding("o", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          simpleMaps.add(new SimpleMap(getString(bs, "s"), getString(bs, "name"), getString(bs, "code"), getString(bs, "scheme")));
        }
      }
    }
    return simpleMaps;
  }

  public List<SimpleMap> getMatchedTo(String iri, List<String> schemeIris) {
    List<SimpleMap> simpleMaps = new ArrayList<>();
    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?o ?code ?scheme ?name
      WHERE {
        ?s im:matchedTo ?o .
        ?o im:code ?code .
        ?o im:scheme ?scheme .
        GRAPH ?g { ?o rdfs:label ?name } .
      """);

    if (schemeIris != null && !schemeIris.isEmpty()) {
      sql.add(valueList("g", schemeIris));
    }
    sql.add("}");
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql.toString());
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          simpleMaps.add(new SimpleMap(getString(bs, "o"), getString(bs, "name"), getString(bs, "code"), getString(bs, "scheme")));
        }
      }
    }
    return simpleMaps;
  }

  public Set<String> getPropertiesForDomains(Set<String> iris) {
   Set<String> properties = new HashSet<>();
   String sql= """
     select distinct ?property
     where {
       VALUES ?domains {%s}
       ?domains im:isA ?superDomains.
       ?property rdfs:domain ?superDomains
      }
     """.formatted(String.join(" ",iris.stream().map(iri->"<"+iri+">").toArray(String[]::new)));
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          properties.add(bs.getValue("property").stringValue());
        }
      }
    }
    return properties;
  }

  public Set<String> getRangesForProperty(String conceptIri) {
    Set<String> ranges = new HashSet<>();
    String sql= """
      Select ?range
      where {
        VALUES ?superProperty {%s}
        ?property im:isA ?superProperty.
        ?property rdfs:range ?range.
      }
      """.formatted("<"+conceptIri+">");

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          ranges.add(bs.getValue("range").stringValue());
        }
      }
    }
    return ranges;
  }



  public List<ConceptContextMap> getConceptContextMaps(String iri) {
    List<ConceptContextMap> result = new ArrayList<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sparql = """
        SELECT ?nodeName ?sourceVal ?sourceRegex ?propertyName ?publisherName ?systemName ?schema ?table ?field
        WHERE {
          ?map ?imConcept ?concept .
          ?node ?imHasMap ?map ;
          ?imTargetProperty ?property ;
          ?rdfsLabel ?nodeName .
          ?property ?rdfsLabel ?propertyName .
          ?context ?imContextNode ?node ;
          ?imSourcePublisher ?publisher .
          ?publisher rdfs:label ?publisherName .
          ?map ?imSourceValue ?sourceVal .
          OPTIONAL {
            ?context ?imSourceSystem ?system .
            ?system ?rdfsLabel ?systemName
          }
          OPTIONAL { ?context ?imSourceSchema ?schema }
          OPTIONAL { ?context ?imSourceTable ?table }
          OPTIONAL { ?context ?imSourceField ?field }
          OPTIONAL { ?context ?imSourceConcept ?concept }
        }
        ORDER BY ?nodeName ?sourceVal ?publisherName
        """;
      TupleQuery qry = conn.prepareTupleQuery(sparql);
      qry.setBinding("concept", iri(iri));
      qry.setBinding("imConcept", iri(IM.CONCEPT));
      qry.setBinding("imHasMap", iri(IM.HAS_MAP));
      qry.setBinding("rdfsLabel", iri(RDFS.LABEL));
      qry.setBinding("imContextNode", iri(IM.CONTEXT_NODE));
      qry.setBinding("imTargetProperty", iri(IM.TARGET_PROPERTY));
      qry.setBinding("imSourcePublisher", iri(IM.SOURCE_PUBLISHER));
      qry.setBinding("imSourceSystem", iri(IM.SOURCE_SYSTEM));
      qry.setBinding("imSourceSchema", iri(IM.SOURCE_SCHEMA));
      qry.setBinding("imSourceTable", iri(IM.SOURCE_TABLE));
      qry.setBinding("imSourceField", iri(IM.SOURCE_FIELD));
      qry.setBinding("imSourceValue", iri(IM.SOURCE_VALUE));
      qry.setBinding("imSourceRegex", iri(IM.SOURCE_REGEX));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (result.stream().noneMatch(r -> r.getNode().equals(bs.getValue("nodeName").stringValue()) || r.getValue().equals(bs.getValue("sourceVal").stringValue()) || (bs.getValue("sourceRegex") != null && r.getRegex().equals(bs.getValue("sourceRegex").stringValue())))) {
            ConceptContextMap conceptContextMap = new ConceptContextMap();
            conceptContextMap.setId(UUID.randomUUID().toString());
            conceptContextMap.setNode(bs.getValue("nodeName").stringValue());
            conceptContextMap.setValue(bs.getValue("sourceVal").stringValue());
            conceptContextMap.setProperty(bs.getValue("propertyName").stringValue());
            if (bs.getValue("sourceRegex") != null)
              conceptContextMap.setRegex(bs.getValue("sourceRegex").stringValue());
            Context context = new Context();
            context.setPublisher(bs.getValue("publisherName").stringValue());
            context.setSystem(bs.getValue("systemName").stringValue());
            context.setSchema(bs.getValue("schema").stringValue());
            context.setTable(bs.getValue("table").stringValue());
            context.setField(bs.getValue("field").stringValue());
            List<Context> contexts = new ArrayList<>();
            contexts.add(context);
            conceptContextMap.setContext(contexts);
            result.add(conceptContextMap);
          }
        }
      }
    }
    return result;
  }

}
