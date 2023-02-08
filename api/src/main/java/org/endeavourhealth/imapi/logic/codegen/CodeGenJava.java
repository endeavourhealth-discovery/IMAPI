package org.endeavourhealth.imapi.logic.codegen;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.XSD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class CodeGenJava {
    private static final Logger LOG = LoggerFactory.getLogger(CodeGenJava.class);
    private HTTPRepository repo;
    private final Queue<String> iris = new PriorityQueue<>();
    private final HashMap<String, DataModel> models = new HashMap<>();

    public void generate(Writer os) throws IOException {
        connectToDatabase();
        getModelList();
        getDataModelRecursively();
        generateJavaCode(os);
    }
    private void connectToDatabase() {
        LOG.debug("connecting to database");

        String server = System.getenv("GRAPH_SERVER") != null
                ? System.getenv("GRAPH_SERVER")
                : "HTTP://localhost:7200";
        String repoID = System.getenv("GRAPH_REPO") != null
                ? System.getenv("GRAPH_REPO")
                : "im";

        repo = new HTTPRepository(server, repoID);

    }

    private void getModelList() {
        LOG.debug("getting model list");

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

        try(RepositoryConnection con = repo.getConnection()) {
            TupleQuery query = con.prepareTupleQuery(sql);
            try(TupleQueryResult result = query.evaluate()) {
                while(result.hasNext()) {
                    BindingSet bindSet = result.next();
                    String iri = bindSet.getValue("iri").stringValue();
                    LOG.trace("iri [{}]", iri);
                    iris.add(iri);
                }
            }
        }
    }

    private void getDataModelRecursively() {
        LOG.debug("getting models");

        while(!iris.isEmpty()) {
            String iri = iris.remove();
            DataModel model = getDataModel(iri);
            addMissingModelToQueue(model);
            models.put(iri, model);
        }
    }

    private DataModel getDataModel(String iri) {
        LOG.debug("get data model [{}]", iri);

        DataModel model = new DataModel().setIri(iri);

        String sql = new StringJoiner(System.lineSeparator())
                .add("PREFIX im: <http://endhealth.info/im#>")
                .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
                .add("PREFIX shacl: <http://www.w3.org/ns/shacl#>")
                .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
                .add("select ?iri ?model ?name ?type ?typeName ?dm ?min ?max ?comment ?order")
                .add("where { ")
                .add("    ?iri shacl:property ?prop .")
                .add("    ?iri rdfs:label ?model .")
                .add("    ?prop shacl:path ?propIri .")
                .add("    ?propIri rdfs:label ?name .")
                .add("    optional { ?prop shacl:order ?order }")
                .add("    optional { ?prop shacl:class ?type }")
                .add("    optional { ?prop shacl:datatype ?type }")
                .add("    optional { ?prop shacl:node ?type }")
                .add("    optional { ?type rdfs:label ?typeName }")
                .add("    optional { ?prop rdfs:comment ?comment }")
                .add("    optional { ?prop shacl:maxCount ?max }")
                .add("    optional { ?prop shacl:minCount ?min }")
                .add("    bind( exists { ?type rdf:type shacl:NodeShape } as ?dm)")
                .add("} order by ?order ")
                .toString();

        try (RepositoryConnection con = repo.getConnection()) {
            TupleQuery query = con.prepareTupleQuery(sql);
            query.setBinding("iri", Values.iri(iri));
            try (TupleQueryResult result = query.evaluate()) {
                while (result.hasNext()) {

                    BindingSet bindSet = result.next();
                    model.setName(bindSet.getValue("model").stringValue());

                    TTIriRef dataType = iri(
                            bindSet.getValue("type").stringValue(),
                            bindSet.hasBinding("typeName")
                                    ? bindSet.getValue("typeName").stringValue()
                                    : null);

                    DataModelProperty property = new DataModelProperty()
                            .setName(bindSet.getValue("name").stringValue())
                            .setDataType(dataType)
                            .setModel(((Literal) bindSet.getValue("dm")).booleanValue())
                            .setComment(bindSet.hasBinding("comment")
                                    ? bindSet.getValue("comment").stringValue()
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
        }
        return model;
    }

    private void addMissingModelToQueue(DataModel model) {
        LOG.debug("add missing model to queue");

        for (DataModelProperty prop : model.getProperties()) {
            if (prop.isModel() && !iris.contains(prop.getDataType().getIri())
                    && !model.getIri().equals(prop.getDataType().getIri())
                    && !models.containsKey(prop.getDataType().getIri())) {
                iris.add(prop.getDataType().getIri());
            }
        }
    }

    private void generateJavaCode(Writer os) throws IOException {
        LOG.debug("generating code");

        os.write("package org.endeavourhealth.informationmanager.utils.codegen;\n" +
                "\nimport org.endeavourhealth.imapi.model.tripletree.TTIriRef;\n" +
                "\nimport java.time.LocalDateTime;");

        for (DataModel model : models.values()) {
            String modelName = capitalise(model.getName());
            os.write("\npublic class " + modelName + " extends IMDMBase<" + modelName + "> {\n" +
                    "\n\tpublic " + modelName + "() {\n" +
                    "\t\tsuper(\"" + modelName + "\");\n" +
                    "\t}");
            for (DataModelProperty property : model.getProperties()) {
                String propertyName = capitalise(property.getName());
                String propertyNameCamelCase = (null == propertyName) ? null : propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
                String propertyType = getDataType(property.getDataType(), property.isModel());

                os.write("\n\tpublic " + propertyType + " get" + propertyName + "() {\n" +
                            "\t\treturn getProperty" + "(\"" + propertyNameCamelCase + "\");\n" +
                        "\t}\n" +
                        "\n\tpublic " + modelName + " set" + propertyName + "(" + propertyType + " " + propertyNameCamelCase + ") {\n" +
                            "\t\tsetProperty(\"" + propertyNameCamelCase + "\", " + propertyNameCamelCase + ");\n" +
                            "\t\treturn this;\n" +
                        "\t}\n");
            }
            os.write("}\n\n");
        }
        os.close();
    }

    String capitalise(String name) {
        if (null == name) {
            return null;
        }

        //name as name
        StringBuilder output = new StringBuilder();

        String[] words = name.toLowerCase().replace("\"", "")
                .replaceAll("[^\\p{L}]", " ").split("\\s+");

        for (String word : words) {
            String camelCase = Character.toUpperCase(word.charAt(0)) + word.substring(1);
            output.append(camelCase);
        }
        return output.toString();
    }

    private String getDataType(TTIriRef dataType, boolean dataModel) {
        if (dataType.getIri().startsWith(XSD.NAMESPACE)) {
            return capitalise(getSuffix(dataType.getIri()));
        } else if (dataModel) {
            return capitalise(dataType.getName());
        } else if ("http://endhealth.info/im#DateTime".equals(dataType.getIri())) {
            return "LocalDateTime";
        } else if (dataType.getIri().startsWith("http://endhealth.info/im#VSET_")
                || "http://endhealth.info/im#Status".equals(dataType.getIri())
                || "http://endhealth.info/im#Graph".equals(dataType.getIri())
                || "http://www.w3.org/2000/01/rdf-schema#Resource".equals(dataType.getIri())
                || "http://snomed.info/sct#999002991000000109".equals(dataType.getIri())
                || "http://snomed.info/sct#999002981000000107".equals(dataType.getIri())
                || "http://hl7.org/fhir/ValueSet/administrative-gender".equals(dataType.getIri())
                || "http://endhealth.info/im#1731000252106".equals(dataType.getIri())) {
            return "TTIriRef";
        } else {
            LOG.error("Unknown data type [{} - {}]", dataType.getIri(), dataType.getName());
            return "UNK " + capitalise(dataType.getName());
        }
    }

    String getSuffix(String iri) {
        return iri.substring(iri.lastIndexOf("#") + 1);
    }
}
