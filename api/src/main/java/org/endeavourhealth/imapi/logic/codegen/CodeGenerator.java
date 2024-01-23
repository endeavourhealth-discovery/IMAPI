package org.endeavourhealth.imapi.logic.codegen;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.codegen.DataModel;
import org.endeavourhealth.imapi.model.codegen.DataModelProperty;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class CodeGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(CodeGenerator.class);
    final HashMap<String, DataModel> xxxxx = new HashMap<>();

    public Queue<String> getAllModelIrisList() {
        LOG.debug("getting model list");
        Queue<String> iris = new PriorityQueue<>();

        String sql = new StringJoiner(System.lineSeparator())
            .add("PREFIX im: <http://endhealth.info/im#>")
            .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
            .add("PREFIX shacl: <http://www.w3.org/ns/shacl#>")
            .add("select ?iri")
            .add("where { ")
            .add("    ?iri (im:isContainedIn|rdfs:subClassOf)* im:HealthDataModel ;")
            .add("        rdf:type shacl:NodeShape .")
            .add("}")
            .toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery query = conn.prepareTupleQuery(sql);
            try (TupleQueryResult result = query.evaluate()) {
                while (result.hasNext()) {
                    BindingSet bindSet = result.next();
                    String iri = bindSet.getValue("iri").stringValue();
                    LOG.trace("iri [{}]", iri);
                    iris.add(iri);
                }
            }
        }

        return iris;
    }

    public Collection<DataModel> getDataModelRecursively(Queue<String> iris) {
        LOG.debug("getting models");
        HashMap<String, DataModel> models = new HashMap<>();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            while (!iris.isEmpty()) {
                String iri = iris.remove();
                DataModel model = getDataModel(conn, iri);
                addMissingModelToQueue(iris, models, model);
                models.put(iri, model);
            }
        }

        return models.values();
    }

    private DataModel getDataModel(RepositoryConnection conn, String iri) {
        LOG.debug("get data model [{}]", iri);

        DataModel model = new DataModel().setIri(iri);

        String sql = new StringJoiner(System.lineSeparator())
            .add("PREFIX im: <http://endhealth.info/im#>")
            .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
            .add("PREFIX shacl: <http://www.w3.org/ns/shacl#>")
            .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
            .add("select ?iri ?model ?comment ?propname ?type ?typeName ?dm ?min ?max ?propcomment ?order")
            .add("where { ")
            .add("    ?iri shacl:property ?prop .")
            .add("    ?iri rdfs:label ?model .")
            .add("    ?iri rdfs:comment ?comment .")
            .add("    ?prop shacl:path ?propIri .")
            .add("    ?propIri rdfs:label ?propname .")
            .add("    optional { ?prop shacl:order ?order }")
            .add("    optional { ?prop shacl:class ?type }")
            .add("    optional { ?prop shacl:datatype ?type }")
            .add("    optional { ?prop shacl:node ?type }")
            .add("    optional { ?type rdfs:label ?typeName }")
            .add("    optional { ?prop rdfs:comment ?propcomment }")
            .add("    optional { ?prop shacl:maxCount ?max }")
            .add("    optional { ?prop shacl:minCount ?min }")
            .add("    bind( exists { ?type rdf:type shacl:NodeShape } as ?dm)")
            .add("    filter not exists { ?prop im:inversePath ?inverse }")
            .add("} order by ?order ")
            .toString();

            TupleQuery query = conn.prepareTupleQuery(sql);
            query.setBinding("iri", Values.iri(iri));
            try (TupleQueryResult result = query.evaluate()) {
                while (result.hasNext()) {

                    BindingSet bindSet = result.next();
                    model.setName(bindSet.getValue("model").stringValue());
                    model.setComment(bindSet.hasBinding("comment")
                        ? bindSet.getValue("comment").stringValue()
                        : null);

                    TTIriRef dataType = iri(
                        bindSet.getValue("type").stringValue(),
                        bindSet.hasBinding("typeName")
                            ? bindSet.getValue("typeName").stringValue()
                            : null);

                    DataModelProperty property = new DataModelProperty()
                        .setName(bindSet.getValue("propname").stringValue())
                        .setDataType(dataType)
                        .setModel(((Literal) bindSet.getValue("dm")).booleanValue())
                        .setComment(bindSet.hasBinding("propcomment")
                            ? bindSet.getValue("propcomment").stringValue()
                            : null)
                        .setMaxCount(bindSet.hasBinding("max")
                            ? ((Literal) bindSet.getValue("max")).intValue()
                            : null)
                        .setMinCount(bindSet.hasBinding("min")
                            ? ((Literal) bindSet.getValue("min")).intValue()
                            : null);

                    model.addProperty(property);
                    LOG.trace("iri [{}]", iri);
                }
        }
        return model;
    }

    private void addMissingModelToQueue(Queue<String> iris, HashMap<String, DataModel> models, DataModel model) {
        LOG.debug("add missing model to queue");

        for (DataModelProperty prop : model.getProperties()) {
            if (prop.isModel() && !iris.contains(prop.getDataType().getIri())
                && !model.getIri().equals(prop.getDataType().getIri())
                && !models.containsKey(prop.getDataType().getIri())) {
                iris.add(prop.getDataType().getIri());
            }
        }
    }
}
