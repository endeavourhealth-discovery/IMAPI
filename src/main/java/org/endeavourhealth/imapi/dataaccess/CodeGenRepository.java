package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.endeavourhealth.imapi.dataaccess.databases.ConfigDB;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

@Slf4j
public class CodeGenRepository {

  public List<String> getCodeTemplateList() {
    List<String> result = new ArrayList<>();
    String sparql = """
      SELECT ?name
      WHERE {
        GRAPH ?g {
          ?s ?type ?codeTemplate .
          ?s ?label ?name
        }
      }
      """;
    try (ConfigDB conn = ConfigDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("type", RDF.TYPE.asDbIri());
      qry.setBinding("codeTemplate", IM.CODE_TEMPLATE.asDbIri());
      qry.setBinding("label", RDFS.LABEL.asDbIri());

      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(bs.getValue("name").stringValue());
        }
      }
    }
    return result;
  }

  public CodeGenDto getCodeTemplate(String name) {
    CodeGenDto result = new CodeGenDto();
    String sparql = """
      SELECT ?p ?o
      WHERE {
        GRAPH ?g {
          ?s ?p ?o .
        }
      }
      """;
    try (ConfigDB conn = ConfigDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("s", iri(CodeTemplate.NAMESPACE + name));

      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          try (CachedObjectMapper om = new CachedObjectMapper()) {


            switch (CodeTemplate.from(bs.getValue("p").stringValue())) {
              case CodeTemplate.DATATYPE_MAP -> {
                ObjectNode map = (ObjectNode) om.readTree(bs.getValue("o").stringValue());
                for (Iterator<Map.Entry<String, JsonNode>> it = map.fields(); it.hasNext(); ) {
                  Map.Entry<String, JsonNode> ele = it.next();
                  result.getDatatypeMap().put(ele.getKey(), ele.getValue().textValue());
                }
              }
              case CodeTemplate.WRAPPER -> result.setCollectionWrapper(bs.getValue("o").stringValue());
              case CodeTemplate.EXTENSION -> result.setExtension(bs.getValue("o").stringValue());
              case CodeTemplate.LABEL -> result.setName(bs.getValue("o").stringValue());
              case CodeTemplate.DEFINITION -> result.setTemplate(bs.getValue("o").stringValue());
              case CodeTemplate.INCLUDE_COMPLEX_TYPES ->
                result.setComplexTypes(((Literal) bs.getValue("o")).booleanValue());
            }
          } catch (JsonProcessingException e) {
            log.error("Unable to parse codeTemplate", e);
          }
        }
      }
    }
    return result;
  }

  public void updateCodeTemplate(String name, String extension, String wrapper, Map<String, String> dataTypeMap, String template, Boolean complexTypes) {
    if (null == complexTypes)
      complexTypes = false;

    String deleteSparql = """
      DELETE WHERE {
      GRAPH ?g {
          ?s ?p ?o
        }
      }
      """;
    try (ConfigDB conn = ConfigDB.getConnection()) {
      Update qry = conn.prepareInsertSparql(deleteSparql);
      qry.setBinding("s", iri(CodeTemplate.NAMESPACE + name));
      qry.execute();
    }
    String insertSparql = """
      INSERT {
        GRAPH ?g {
          ?iri ?label ?name .
          ?iri ?extensionType ?extension .
          ?iri ?type ?typeIri .
          ?iri ?definition ?template .
          ?iri ?typeMap ?datatypeMap .
          ?iri ?wrapperType ?wrapper .
          ?iri ?includeComplex ?complexTypes .
        }
      }
      WHERE {
        SELECT ?iri ?label ?extension {}
      }
      """;
    try (ConfigDB conn = ConfigDB.getConnection()) {
      try (CachedObjectMapper om = new CachedObjectMapper()) {
        Update qry2 = conn.prepareInsertSparql(insertSparql);
        qry2.setBinding("iri", iri(CodeTemplate.NAMESPACE + name));
        qry2.setBinding("label", RDFS.LABEL.asDbIri());
        qry2.setBinding("name", literal(name));
        qry2.setBinding("extensionType", CodeTemplate.EXTENSION.asDbIri());
        qry2.setBinding("extension", literal(extension));
        qry2.setBinding("type", RDF.TYPE.asDbIri());
        qry2.setBinding("typeIri", IM.CODE_TEMPLATE.asDbIri());
        qry2.setBinding("definition", CodeTemplate.DEFINITION.asDbIri());
        qry2.setBinding("template", literal(template));
        qry2.setBinding("typeMap", CodeTemplate.DATATYPE_MAP.asDbIri());
        qry2.setBinding("datatypeMap", literal(om.writeValueAsString(dataTypeMap)));
        qry2.setBinding("wrapperType", CodeTemplate.WRAPPER.asDbIri());
        qry2.setBinding("wrapper", literal(wrapper));
        qry2.setBinding("includeComplex", CodeTemplate.INCLUDE_COMPLEX_TYPES.asDbIri());
        qry2.setBinding("complexTypes", literal(complexTypes));
        qry2.execute();
      } catch (JsonProcessingException err) {
        log.error("Error updating codeTemplate", err);
      }
    }
  }
}
