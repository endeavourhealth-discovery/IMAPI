package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
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

  public List<SimpleMap> getMatchedFrom(String iri, List<String> schemeIris) {
    List<SimpleMap> simpleMaps = new ArrayList<>();
    String sql = """
      SELECT ?s ?code ?scheme ?name ?alternativeCode ?codeId
      WHERE {
        ?s im:matchedTo ?o .
        ?s im:code ?code .
        %s
        ?s im:scheme ?scheme ;
        rdfs:label ?name .
        optional {?s im:alternativeCode ?alternativeCode .}
        optional {?s im:codeId ?codeId .}
      }
      """.formatted(valueList("scheme", schemeIris));
    try (IMDB conn = IMDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("o", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          simpleMaps.add(new SimpleMap(getString(bs, "s"), getString(bs, "name"), getString(bs, "code"), getString(bs, "scheme")
          ,getString(bs,"alternativeCode"),getString(bs,"codeId")));
        }
      }
    }
    return simpleMaps;
  }

  public List<SimpleMap> getMatchedTo(String iri, List<String> schemeIris) {
    List<SimpleMap> simpleMaps = new ArrayList<>();
    String sql = """
      SELECT ?o ?code ?scheme ?name
      WHERE {
        ?s im:matchedTo ?o .
        ?o im:code ?code .
        %s
        ?o im:scheme ?scheme .
        ?o rdfs:label ?name .
      }
      """.formatted(valueList("scheme", schemeIris));

    try (IMDB conn = IMDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          simpleMaps.add(new SimpleMap(getString(bs, "o"), getString(bs, "name"), getString(bs, "code"), getString(bs, "scheme"),null,null));
        }
      }
    }
    return simpleMaps;
  }

  public Set<String> getPropertiesForDomains(Set<String> iris) {
    Set<String> properties = new HashSet<>();
    String sql = """
      select distinct ?property
      where {
        VALUES ?domains {%s}
        ?domains im:isA ?superDomains.
        ?property rdfs:domain ?superDomains
      }
      """.formatted(String.join(" ", iris.stream().map(iri -> "<" + iri + ">").toArray(String[]::new)));
    try (IMDB conn = IMDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
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
    String sql = """
      Select ?range
      where {
        VALUES ?superProperty {%s}
        ?property im:isA ?superProperty.
        ?property rdfs:range ?range.
      }
      """.formatted("<" + conceptIri + ">");

    try (IMDB conn = IMDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
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
    try (IMDB conn = IMDB.getConnection()) {
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
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("concept", iri(iri));
      qry.setBinding("imConcept", IM.CONCEPT.asDbIri());
      qry.setBinding("imHasMap", IM.HAS_MAP.asDbIri());
      qry.setBinding("rdfsLabel", RDFS.LABEL.asDbIri());
      qry.setBinding("imContextNode", IM.CONTEXT_NODE.asDbIri());
      qry.setBinding("imTargetProperty", IM.TARGET_PROPERTY.asDbIri());
      qry.setBinding("imSourcePublisher", IM.SOURCE_PUBLISHER.asDbIri());
      qry.setBinding("imSourceSystem", IM.SOURCE_SYSTEM.asDbIri());
      qry.setBinding("imSourceSchema", IM.SOURCE_SCHEMA.asDbIri());
      qry.setBinding("imSourceTable", IM.SOURCE_TABLE.asDbIri());
      qry.setBinding("imSourceField", IM.SOURCE_FIELD.asDbIri());
      qry.setBinding("imSourceValue", IM.SOURCE_VALUE.asDbIri());
      qry.setBinding("imSourceRegex", IM.SOURCE_REGEX.asDbIri());
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

  public String getShortestTerm(String iri) {
    String sql = """
      Select ?term
      where {
        values ?entity {%s}
         {
      ?entity im:hasTermCode ?termCode.
      ?termCode rdfs:label ?term.
      }
     
      }
      order by strlen(?term)
      limit 1
      """.formatted("<" + iri + ">");
    try (IMDB conn = IMDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          return bs.getValue("term").stringValue();
        }
      }
    }
    return null;
  }
}
