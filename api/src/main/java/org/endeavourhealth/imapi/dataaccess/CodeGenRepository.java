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
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;
import org.endeavourhealth.imapi.vocabulary.CODE_TEMPLATE;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareUpdateSparql;

@Slf4j
public class CodeGenRepository {

  public List<String> getCodeTemplateList() {
    List<String> result = new ArrayList<>();
    String sparql = """
      SELECT ?name WHERE {
        ?s ?type ?codeTemplate .
        ?s ?label ?name
      }
      """;
    try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
      qry.setBinding("type", iri(RDF.TYPE));
      qry.setBinding("codeTemplate", iri(IM.CODE_TEMPLATE));
      qry.setBinding("label", iri(RDFS.LABEL));

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
      SELECT ?p ?o WHERE {
        ?s ?p ?o .
      }
      """;
    try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
      qry.setBinding("s", iri(CODE_TEMPLATE.NAMESPACE + name));

      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          try (CachedObjectMapper om = new CachedObjectMapper()) {
            switch (bs.getValue("p").stringValue()) {
              case (CODE_TEMPLATE.DATATYPE_MAP) -> {
                ObjectNode map = (ObjectNode) om.readTree(bs.getValue("o").stringValue());
                for (Iterator<Map.Entry<String, JsonNode>> it = map.fields(); it.hasNext(); ) {
                  Map.Entry<String, JsonNode> ele = it.next();
                  result.getDatatypeMap().put(ele.getKey(), ele.getValue().textValue());
                }
              }
              case (CODE_TEMPLATE.WRAPPER) -> result.setCollectionWrapper(bs.getValue("o").stringValue());
              case (CODE_TEMPLATE.EXTENSION) -> result.setExtension(bs.getValue("o").stringValue());
              case (RDFS.LABEL) -> result.setName(bs.getValue("o").stringValue());
              case (IM.DEFINITION) -> result.setTemplate(bs.getValue("o").stringValue());
              case (CODE_TEMPLATE.INCLUDE_COMPLEX_TYPES) ->
                result.setComplexTypes(((Literal) bs.getValue("o")).booleanValue());
              default -> {
                break;
              }
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
    try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
      Update qry = conn.prepareUpdate(deleteSparql);
      qry.setBinding("s", iri(CODE_TEMPLATE.NAMESPACE + name));
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
    try (RepositoryConnection conn2 = ConnectionManager.getConfigConnection()) {
      try (CachedObjectMapper om = new CachedObjectMapper()) {
        Update qry2 = prepareUpdateSparql(conn2, insertSparql);
        qry2.setBinding("iri", iri(CODE_TEMPLATE.NAMESPACE + name));
        qry2.setBinding("label", iri(RDFS.LABEL));
        qry2.setBinding("name", literal(name));
        qry2.setBinding("extensionType", iri(CODE_TEMPLATE.EXTENSION));
        qry2.setBinding("extension", literal(extension));
        qry2.setBinding("type", iri(RDF.TYPE));
        qry2.setBinding("typeIri", iri(IM.CODE_TEMPLATE));
        qry2.setBinding("definition", iri(IM.DEFINITION));
        qry2.setBinding("template", literal(template));
        qry2.setBinding("typeMap", iri(CODE_TEMPLATE.DATATYPE_MAP));
        qry2.setBinding("datatypeMap", literal(om.writeValueAsString(dataTypeMap)));
        qry2.setBinding("wrapperType", iri(CODE_TEMPLATE.WRAPPER));
        qry2.setBinding("wrapper", literal(wrapper));
        qry2.setBinding("includeComplex", iri(CODE_TEMPLATE.INCLUDE_COMPLEX_TYPES));
        qry2.setBinding("complexTypes", literal(complexTypes));
        qry2.execute();
      } catch (JsonProcessingException err) {
        log.error("Error updating codeTemplate", err);
      }
    }
  }
}
