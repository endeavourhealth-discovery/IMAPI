package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareUpdateSparql;

public class CodeGenRepository {
    private static final Logger LOG = LoggerFactory.getLogger(CodeGenRepository.class);

    public List<String> getCodeTemplateList() throws JsonProcessingException  {
        List<String> result = new ArrayList<>();
        StringJoiner sparql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?name WHERE {")
                .add("  ?s ?type ?codeTemplate .")
                .add("  ?s ?label ?name")
                .add("}");
        try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql.toString());
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

    public CodeGenDto getCodeTemplate(String name) throws JsonProcessingException  {
        CodeGenDto result = new CodeGenDto();
        List<String> stuff = new ArrayList<>();
        StringJoiner sparql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?p ?o WHERE {")
                .add("  ?s ?p ?o .")
                .add("}");
        try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql.toString());
            qry.setBinding("s", iri(CODE_TEMPLATE.NAMESPACE + name));

            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    try (CachedObjectMapper om = new CachedObjectMapper()) {
                        LOG.debug(bs.getValue("o").stringValue());
                        LOG.debug(bs.getValue("p").stringValue());
                        switch (bs.getValue("p").stringValue()) {
                            case (CODE_TEMPLATE.DATATYPE_MAP) -> result.setDatatypeMap(bs.getValue("o").stringValue());
                            case (CODE_TEMPLATE.WRAPPER) -> result.setCollectionWrapper(bs.getValue("o").stringValue());
                            case (CODE_TEMPLATE.EXTENSION) -> result.setExtension(bs.getValue("o").stringValue());
                            case (RDFS.LABEL) -> result.setName(bs.getValue("o").stringValue());
                            case (IM.DEFINITION) -> result.setTemplate(bs.getValue("o").stringValue());
                            default -> {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public void saveCodeTemplate(String name, String extension, String wrapper, String dataTypeMap, String template) {
        StringJoiner deleteSparql = new StringJoiner(System.lineSeparator()).add("DELETE WHERE {").add("  ?s ?p ?o").add("}");
        try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
            Update qry = conn.prepareUpdate(deleteSparql.toString());
            qry.setBinding("s", iri(CODE_TEMPLATE.NAMESPACE + name));
            qry.execute();
        }
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            StringJoiner insertSparql = new StringJoiner(System.lineSeparator())
                    .add("INSERT {")
                    .add("  ?iri ?label ?name .")
                    .add("  ?iri ?extensionType ?extension .")
                    .add("  ?iri ?type ?typeIri .")
                    .add("  ?iri ?definition ?template .")
                    .add("  ?iri ?typeMap ?datatypeMap .")
                    .add("  ?iri ?wrapperType ?wrapper .")
                    .add("}")
                    .add("WHERE { SELECT ?iri ?label ?extension {} }");
            try (RepositoryConnection conn2 = ConnectionManager.getConfigConnection()) {
                Update qry2 = prepareUpdateSparql(conn2, insertSparql.toString());
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
                qry2.setBinding("datatypeMap", literal(dataTypeMap));
                qry2.setBinding("wrapperType", iri(CODE_TEMPLATE.WRAPPER));
                qry2.setBinding("wrapper", literal(wrapper));
                qry2.execute();
            }
        }
    }
}
