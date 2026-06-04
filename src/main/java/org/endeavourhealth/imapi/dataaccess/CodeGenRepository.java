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
import org.endeavourhealth.imapi.model.dto.CodeGenDtoExtended;
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
      qry.setBinding("type", EnumUtils.asDbIri(RdfVocab.TYPE));
      qry.setBinding("codeTemplate", EnumUtils.asDbIri(ImVocab.CODE_TEMPLATE));
      qry.setBinding("label", EnumUtils.asDbIri(RdfsVocab.LABEL));

      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(bs.getValue("name").stringValue());
        }
      }
    }
    return result;
  }

  public CodeGenDtoExtended getCodeTemplate(String name) {
    CodeGenDtoExtended result = new CodeGenDtoExtended();
    String sparql = """
      SELECT ?p ?o
      WHERE {
        ?s ?p ?o .
      }
      """;
    try (ConfigDB conn = ConfigDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("s", iri(NamespaceVocab.IM_CODE_TEMPLATE + name));

      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          try (CachedObjectMapper om = new CachedObjectMapper()) {


            switch (CodeTemplateVocab.fromValue(bs.getValue("p").stringValue())) {
              case CodeTemplateVocab.DATATYPE_MAP -> {
                ObjectNode map = (ObjectNode) om.readTree(bs.getValue("o").stringValue());
                for (Iterator<Map.Entry<String, JsonNode>> it = map.fields(); it.hasNext(); ) {
                  Map.Entry<String, JsonNode> ele = it.next();
                  result.getDatatypeMap().put(ele.getKey(), ele.getValue().textValue());
                }
              }
              case CodeTemplateVocab.WRAPPER -> result.setCollectionWrapper(bs.getValue("o").stringValue());
              case CodeTemplateVocab.EXTENSION -> result.setExtension(bs.getValue("o").stringValue());
              case CodeTemplateVocab.LABEL -> result.setName(bs.getValue("o").stringValue());
              case CodeTemplateVocab.DEFINITION -> result.setTemplate(bs.getValue("o").stringValue());
              case CodeTemplateVocab.INCLUDE_COMPLEX_TYPES ->
                result.setComplexTypes(((Literal) bs.getValue("o")).booleanValue());
              case null -> throw new IllegalArgumentException("Failed to decode into CODETEMPLATE enum");
              default ->
                throw new IllegalArgumentException("Invalid CODETEMPLATE found" + CodeTemplateVocab.fromValue(bs.getValue("p").stringValue()));
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
      qry.setBinding("s", iri(NamespaceVocab.IM_CODE_TEMPLATE + name));
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
        qry2.setBinding("iri", iri(NamespaceVocab.IM_CODE_TEMPLATE + name));
        qry2.setBinding("label", EnumUtils.asDbIri(RdfsVocab.LABEL));
        qry2.setBinding("name", literal(name));
        qry2.setBinding("extensionType", EnumUtils.asDbIri(CodeTemplateVocab.EXTENSION));
        qry2.setBinding("extension", literal(extension));
        qry2.setBinding("type", EnumUtils.asDbIri(RdfVocab.TYPE));
        qry2.setBinding("typeIri", EnumUtils.asDbIri(ImVocab.CODE_TEMPLATE));
        qry2.setBinding("definition", EnumUtils.asDbIri(CodeTemplateVocab.DEFINITION));
        qry2.setBinding("template", literal(template));
        qry2.setBinding("typeMap", EnumUtils.asDbIri(CodeTemplateVocab.DATATYPE_MAP));
        qry2.setBinding("datatypeMap", literal(om.writeValueAsString(dataTypeMap)));
        qry2.setBinding("wrapperType", EnumUtils.asDbIri(CodeTemplateVocab.WRAPPER));
        qry2.setBinding("wrapper", literal(wrapper));
        qry2.setBinding("includeComplex", EnumUtils.asDbIri(CodeTemplateVocab.INCLUDE_COMPLEX_TYPES));
        qry2.setBinding("complexTypes", literal(complexTypes));
        qry2.execute();
      } catch (JsonProcessingException err) {
        log.error("Error updating codeTemplate", err);
      }
    }
  }
}
