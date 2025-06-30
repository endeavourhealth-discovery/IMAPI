package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.model.ConceptContextMap;
import org.endeavourhealth.imapi.model.Context;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.getString;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.valueList;

public class ConceptRepository {

  public List<SimpleMap> getMatchedFrom(String iri, List<String> schemeIris, String graph) {
    List<SimpleMap> simpleMaps = new ArrayList<>();
    String sql = """
      SELECT ?s ?code ?scheme ?name
      WHERE {
        GRAPH ?g {
          ?s im:matchedTo ?o .
          ?s im:code ?code .
          %s
          ?s im:scheme ?scheme ;
          rdfs:label ?name .
        }
      }
      """.formatted(valueList("scheme", schemeIris));
    try (RepositoryConnection conn = IMDB.getConnection()) {
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sql, graph);
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

  public List<SimpleMap> getMatchedTo(String iri, List<String> schemeIris, String graph) {
    List<SimpleMap> simpleMaps = new ArrayList<>();
    String sql = """
      SELECT ?o ?code ?scheme ?name
      WHERE {
        GRAPH ?g {
          ?s im:matchedTo ?o .
          ?o im:code ?code .
          %s
          ?o im:scheme ?scheme .
          GRAPH ?g { ?o rdfs:label ?name } .
        }
      }
      """.formatted(valueList("scheme", schemeIris));

    try (RepositoryConnection conn = IMDB.getConnection()) {
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sql, graph);
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

  public Set<String> getPropertiesForDomains(Set<String> iris, String graph) {
    Set<String> properties = new HashSet<>();
    String sql = """
      select distinct ?property
      where {
        GRAPH ?g {
          VALUES ?domains {%s}
          ?domains im:isA ?superDomains.
          ?property rdfs:domain ?superDomains
        }
      }
      """.formatted(String.join(" ", iris.stream().map(iri -> "<" + iri + ">").toArray(String[]::new)));
    try (RepositoryConnection conn = IMDB.getConnection()) {
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sql, graph);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          properties.add(bs.getValue("property").stringValue());
        }
      }
    }
    return properties;
  }

  public Set<String> getRangesForProperty(String conceptIri, String graph) {
    Set<String> ranges = new HashSet<>();
    String sql = """
      Select ?range
      where {
        GRAPH ?g {
          VALUES ?superProperty {%s}
          ?property im:isA ?superProperty.
          ?property rdfs:range ?range.
        }
      }
      """.formatted("<" + conceptIri + ">");

    try (RepositoryConnection conn = IMDB.getConnection()) {
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sql, graph);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          ranges.add(bs.getValue("range").stringValue());
        }
      }
    }
    return ranges;
  }


  public List<ConceptContextMap> getConceptContextMaps(String iri, String graph) {
    List<ConceptContextMap> result = new ArrayList<>();
    try (RepositoryConnection conn = IMDB.getConnection()) {
      String sparql = """
        SELECT ?nodeName ?sourceVal ?sourceRegex ?propertyName ?publisherName ?systemName ?schema ?table ?field
        WHERE {
          GRAPH ?g {
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
        }
        ORDER BY ?nodeName ?sourceVal ?publisherName
        """;
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sparql, graph);
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
