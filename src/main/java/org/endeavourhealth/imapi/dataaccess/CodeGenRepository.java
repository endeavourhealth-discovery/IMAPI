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
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.*;

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
        ?s ?type ?codeTemplate .
        ?s ?label ?name
      }
      """;
    try (ConfigDB conn = ConfigDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("type", EnumUtils.asDbIri(RDF.TYPE));
      qry.setBinding("codeTemplate", EnumUtils.asDbIri(IM.CODE_TEMPLATE));
      qry.setBinding("label", EnumUtils.asDbIri(RDFS.LABEL));

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
        ?s ?p ?o .
      }
      """;
    try (ConfigDB conn = ConfigDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("s", iri(NAMESPACE.IM_CODE_TEMPLATE + name));

      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          try (CachedObjectMapper om = new CachedObjectMapper()) {


            switch (CODETEMPLATE.Companion.decode(bs.getValue("p").stringValue())) {
              case CODETEMPLATE.DATATYPE_MAP -> {
                ObjectNode map = (ObjectNode) om.readTree(bs.getValue("o").stringValue());
                for (Iterator<Map.Entry<String, JsonNode>> it = map.fields(); it.hasNext(); ) {
                  Map.Entry<String, JsonNode> ele = it.next();
                  result.getDatatypeMap().put(ele.getKey(), ele.getValue().textValue());
                }
              }
              case CODETEMPLATE.WRAPPER -> result.setCollectionWrapper(bs.getValue("o").stringValue());
              case CODETEMPLATE.EXTENSION -> result.setExtension(bs.getValue("o").stringValue());
              case CODETEMPLATE.LABEL -> result.setName(bs.getValue("o").stringValue());
              case CODETEMPLATE.DEFINITION -> result.setTemplate(bs.getValue("o").stringValue());
              case CODETEMPLATE.INCLUDE_COMPLEX_TYPES ->
                result.setComplexTypes(((Literal) bs.getValue("o")).booleanValue());
              case null -> throw new IllegalArgumentException("Failed to decode into CODETEMPLATE enum");
              default ->
                throw new IllegalArgumentException("Invalid CODETEMPLATE found" + CODETEMPLATE.Companion.decode(bs.getValue("p").stringValue()));
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
        ?s ?p ?o
      }
      """;
    try (ConfigDB conn = ConfigDB.getConnection()) {
      Update qry = conn.prepareDeleteSparql(deleteSparql);
      qry.setBinding("s", iri(NAMESPACE.IM_CODE_TEMPLATE + name));
      qry.execute();
    }
    String insertSparql = """
      INSERT {
        ?iri ?label ?name .
        ?iri ?extensionType ?extension .
        ?iri ?type ?typeIri .
        ?iri ?definition ?template .
        ?iri ?typeMap ?datatypeMap .
        ?iri ?wrapperType ?wrapper .
        ?iri ?includeComplex ?complexTypes .
      }
      WHERE {
        SELECT ?iri ?label ?extension {}
      }
      """;
    try (ConfigDB conn = ConfigDB.getConnection()) {
      try (CachedObjectMapper om = new CachedObjectMapper()) {
        Update qry2 = conn.prepareInsertSparql(insertSparql);
        qry2.setBinding("iri", iri(NAMESPACE.IM_CODE_TEMPLATE + name));
        qry2.setBinding("label", EnumUtils.asDbIri(RDFS.LABEL));
        qry2.setBinding("name", literal(name));
        qry2.setBinding("extensionType", EnumUtils.asDbIri(CODETEMPLATE.EXTENSION));
        qry2.setBinding("extension", literal(extension));
        qry2.setBinding("type", EnumUtils.asDbIri(RDF.TYPE));
        qry2.setBinding("typeIri", EnumUtils.asDbIri(IM.CODE_TEMPLATE));
        qry2.setBinding("definition", EnumUtils.asDbIri(CODETEMPLATE.DEFINITION));
        qry2.setBinding("template", literal(template));
        qry2.setBinding("typeMap", EnumUtils.asDbIri(CODETEMPLATE.DATATYPE_MAP));
        qry2.setBinding("datatypeMap", literal(om.writeValueAsString(dataTypeMap)));
        qry2.setBinding("wrapperType", EnumUtils.asDbIri(CODETEMPLATE.WRAPPER));
        qry2.setBinding("wrapper", literal(wrapper));
        qry2.setBinding("includeComplex", EnumUtils.asDbIri(CODETEMPLATE.INCLUDE_COMPLEX_TYPES));
        qry2.setBinding("complexTypes", literal(complexTypes));
        qry2.execute();
      } catch (JsonProcessingException err) {
        log.error("Error updating codeTemplate", err);
      }
    }
  }
}
