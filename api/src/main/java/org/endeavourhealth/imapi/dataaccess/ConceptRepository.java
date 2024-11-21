package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.ConceptContextMap;
import org.endeavourhealth.imapi.model.Context;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.transforms.SnomedConcept;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM_FUNCTION;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.getString;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.valueList;

public class ConceptRepository {

  public Integer getLastInrementalFrom() {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sql = """
        select ?increment where {
          ?snomedGenerator ?hasIncrementalFrom ?increment
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(sql);
      qry.setBinding("snomedGenerator", iri(IM_FUNCTION.SNOMED_CONCEPT_GENERATOR));
      qry.setBinding("hasIncrementalFrom", iri(IM.HAS_INCREMENTAL_FROM));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          return Integer.parseInt(rs.next().getValue("increment").stringValue());
        }
      }
    }

    return 0;
  }

  public void updateIncrement(Integer from) throws QueryException, TTFilerException, JsonProcessingException {
    TTDocument document = new TTDocument()
      .setCrud(TTIriRef.iri(IM.UPDATE_PREDICATES))
      .setGraph(TTIriRef.iri(GRAPH.DISCOVERY))
      .addEntity(new TTEntity()
        .setCrud(TTIriRef.iri(IM.UPDATE_PREDICATES))
        .setIri(IM_FUNCTION.SNOMED_CONCEPT_GENERATOR)
        .set(TTIriRef.iri(IM.HAS_INCREMENTAL_FROM), TTLiteral.literal(from + 1)));
    try (TTTransactionFiler filer = new TTTransactionFiler()) {
      filer.fileDocument(document);
    }
  }

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

  public Pageable<TTIriRef> getSuperiorPropertiesByConceptPagedWithTotalCount(String conceptIri, Integer rowNumber, Integer pageSize, boolean inactive) {
    List<TTIriRef> properties = new ArrayList<>();
    Pageable<TTIriRef> result = new Pageable<>();

    StringJoiner sqlCount = new StringJoiner(System.lineSeparator()).add("""
      SELECT (COUNT(?a1) as ?count)
      WHERE {
        ?concept im:isA ?p .
        ?a1 rdfs:domain ?p .
        FILTER NOT EXISTS {
          ?a2 rdfs:domain ?p .
          ?a1 im:isA ?a2 .
          FILTER(?a1 != ?a2)
        }
      """);
    if (!inactive) {
      sqlCount.add("OPTIONAL {?a1 im:status ?a1s}").add("FILTER(?a1s != im:Inactive) .");
    }
    sqlCount.add("}");

    StringJoiner stringQuery = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?a1 ?attributeName
      WHERE {
        ?concept im:isA ?p .
        ?p rdfs:label ?parentName .
        ?a1 rdfs:domain ?p ;
        rdfs:label ?attributeName .
        FILTER NOT EXISTS {
          ?a2 rdfs:domain ?p .
          ?a1 im:isA ?a2 .
          FILTER(?a1 != ?a2)
        }
      """);
    if (!inactive) {
      stringQuery.add("OPTIONAL {?a1 im:status ?a1s}").add("FILTER(?a1s != im:Inactive) .");
    }
    stringQuery.add("}");
    stringQuery.add("ORDER BY ?attributeName");

    if (rowNumber != null && pageSize != null) {
      stringQuery.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
    }

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qryCount = prepareSparql(conn, sqlCount.toString());
      qryCount.setBinding("concept", iri(conceptIri));
      try (TupleQueryResult rsCount = qryCount.evaluate()) {
        BindingSet bsCount = rsCount.next();
        result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
      }

      TupleQuery qry = prepareSparql(conn, stringQuery.toString());
      qry.setBinding("concept", iri(conceptIri));
      try (TupleQueryResult rs = qry.evaluate()) {
        result.setPageSize(pageSize);
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          properties.add(new TTIriRef(bs.getValue("a1").stringValue(), bs.getValue("attributeName").stringValue()));
        }
        result.setResult(properties);
      }
    }
    return result;
  }

  public Pageable<TTIriRef> getSuperiorPropertiesByConceptBoolFocusPagedWithTotalCount(List<String> conceptIris, Integer rowNumber, Integer pageSize, boolean inactive) {
    List<TTIriRef> properties = new ArrayList<>();
    Pageable<TTIriRef> result = new Pageable<>();

    StringJoiner sqlCount = new StringJoiner(System.lineSeparator()).add("SELECT (COUNT(DISTINCT ?a1) as ?count)").add("WHERE {");
    if (conceptIris != null && !conceptIris.isEmpty()) {
      sqlCount.add(valueList("concept", conceptIris));
    }
    sqlCount.add("""
      ?concept im:isA ?p .
      ?a1 rdfs:domain ?p .
      FILTER NOT EXISTS {
        ?a2 rdfs:domain ?p .
        ?a1 im:isA ?a2 .
        FILTER(?a1 != ?a2)
      }
      """);
    if (!inactive) {
      sqlCount.add("OPTIONAL {?a1 im:status ?a1s}").add("FILTER(?a1s != im:Inactive) .");
    }
    sqlCount.add("}");

    StringJoiner stringQuery = new StringJoiner(System.lineSeparator()).add("SELECT DISTINCT ?a1 ?attributeName").add("WHERE {");
    if (conceptIris != null && !conceptIris.isEmpty()) {
      stringQuery.add(valueList("concept", conceptIris));
    }
    stringQuery.add("""
      ?concept im:isA ?p .
      ?p rdfs:label ?parentName .
      ?a1 rdfs:domain ?p ;
      rdfs:label ?attributeName .
      FILTER NOT EXISTS {
        ?a2 rdfs:domain ?p .
        ?a1 im:isA ?a2 .
        FILTER(?a1 != ?a2)
      }
      """);
    if (!inactive) {
      stringQuery.add("OPTIONAL {?a1 im:status ?a1s}").add("FILTER(?a1s != im:Inactive) .");
    }
    stringQuery.add("}");
    stringQuery.add("ORDER BY ?attributeName");

    if (rowNumber != null && pageSize != null) {
      stringQuery.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
    }

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qryCount = prepareSparql(conn, sqlCount.toString());
      try (TupleQueryResult rsCount = qryCount.evaluate()) {
        BindingSet bsCount = rsCount.next();
        result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
      }

      TupleQuery qry = prepareSparql(conn, stringQuery.toString());
      try (TupleQueryResult rs = qry.evaluate()) {
        result.setPageSize(pageSize);
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          properties.add(new TTIriRef(bs.getValue("a1").stringValue(), bs.getValue("attributeName").stringValue()));
        }
        result.setResult(properties);
      }
    }
    return result;
  }

  public Pageable<TTIriRef> getSuperiorPropertyValuesByPropertyPagedWithTotalCount(String propertyIri, Integer rowNumber, Integer pageSize, boolean inactive) {
    List<TTIriRef> values = new ArrayList<>();
    Pageable<TTIriRef> result = new Pageable<>();

    StringJoiner sqlCount = new StringJoiner(System.lineSeparator()).add("""
      SELECT (COUNT(?value) as ?count)
      WHERE {
        ?property rdfs:range ?value .
      """);
    if (!inactive) {
      sqlCount.add("OPTIONAL {?value im:status ?vs}").add("FILTER(?vs != im:Inactive) .");
    }
    sqlCount.add("}");

    StringJoiner stringQuery = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?value ?valueName
      WHERE {
        ?property rdfs:range ?value .
        ?value rdfs:label ?valueName .
      """);
    if (!inactive) {
      stringQuery.add("OPTIONAL {?value im:status ?vs}").add("FILTER(?vs != im:Inactive) .");
    }
    stringQuery.add("}");
    stringQuery.add("ORDER BY ?valueName");

    if (rowNumber != null && pageSize != null) {
      stringQuery.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
    }

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qryCount = prepareSparql(conn, sqlCount.toString());
      qryCount.setBinding("property", iri(propertyIri));
      try (TupleQueryResult rsCount = qryCount.evaluate()) {
        BindingSet bsCount = rsCount.next();
        result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
      }

      TupleQuery qry = prepareSparql(conn, stringQuery.toString());
      qry.setBinding("property", iri(propertyIri));
      try (TupleQueryResult rs = qry.evaluate()) {
        result.setPageSize(pageSize);
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          values.add(new TTIriRef(bs.getValue("value").stringValue(), bs.getValue("valueName").stringValue()));
        }
        result.setResult(values);
      }
    }
    return result;
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
