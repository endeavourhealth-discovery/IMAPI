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
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class CodeGenJava {
    private static final Logger LOG = LoggerFactory.getLogger(CodeGenJava.class);
    private HTTPRepository repo;
    private final Queue<String> iris = new PriorityQueue<>();
    private final HashMap<String, DataModel> models = new HashMap<>();

    public void generate(ZipOutputStream os) throws IOException {
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

        try (RepositoryConnection con = repo.getConnection()) {
            TupleQuery query = con.prepareTupleQuery(sql);
            try (TupleQueryResult result = query.evaluate()) {
                while (result.hasNext()) {
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

        while (!iris.isEmpty()) {
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

        try (RepositoryConnection con = repo.getConnection()) {
            TupleQuery query = con.prepareTupleQuery(sql);
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

    private void generateJavaCode(ZipOutputStream zs) throws IOException {
        LOG.debug("generating code");

        for (DataModel model : models.values()) {
            String modelName = capitalise(model.getName());
            zs.putNextEntry(new ZipEntry(modelName + ".java"));
            String java = generateJavaCodeForModel(model, modelName);
            zs.write(java.getBytes());
            zs.closeEntry();
        }
        zs.finish();
        zs.flush();
    }

    private String generateJavaCodeForModel(DataModel model, String modelName) throws IOException {
        try (StringWriter os = new StringWriter()) {

            String modelNameSeparated = separate(modelName);
            String modelComment = model.getComment();

            os.write("package org.endeavourhealth.imapi.logic.codegen;\n" +
                "\nimport org.endeavourhealth.imapi.model.tripletree.TTIriRef;\n" +
                "\nimport java.util.UUID;");

            os.write("\n\n/**\n" +
                "* Represents " + modelNameSeparated + ".\n");
            if (modelComment != null)
                os.write("* " + modelComment + "\n");
            os.write("*/\n");
            os.write("public class " + modelName + " extends IMDMBase<" + modelName + "> {\n");
            //os.write("\n\n\t/**\n" +
                //"\t* " + modelName.substring(0, 1).toUpperCase() + modelNameSeparated.substring(1) + " constructor \n" +
                //"\t*/");
            //os.write("\n\tpublic " + modelName + "() {\n" +
                //"\t\tsuper(\"" + modelName + "\");\n" +
                //"\t}");
            os.write("\n\n\t/**\n" +
                "\t* " + modelName.substring(0, 1).toUpperCase() + modelNameSeparated.substring(1) + " constructor with identifier\n" +
                "\t*/");
            os.write("\n\tpublic " + modelName + "(UUID id) {\n" +
                "\t\tsuper(\"" + modelName + "\", id);\n" +
                "\t}");

            for (DataModelProperty property : model.getProperties()) {
                String propertyName = property.getName();
                String propertyNameCapitalised = capitalise(property.getName());
                String propertyNameCamelCase = (null == propertyNameCapitalised) ? null : propertyNameCapitalised.substring(0, 1).toLowerCase() + propertyNameCapitalised.substring(1);

                boolean isArray = property.getMaxCount() != null && property.getMaxCount() > 1;
                String propertyType = getDataType(property.getDataType(), property.isModel(), isArray);
                String propertyTypeName = getDataType(property.getDataType(), property.isModel(), false);

                os.write("\n\n\t/**\n" +
                    "\t* Gets the " + propertyName + " of this " + modelNameSeparated + "\n");
                if (property.getComment() != null)
                    os.write("\t* " + property.getComment() + "\n");
                os.write("\t* @return " + propertyNameCamelCase + "\n" +
                    "\t*/\n");
                os.write("\tpublic " + propertyType + " get" + propertyNameCapitalised + "() {\n" +
                    "\t\treturn getProperty" + "(\"" + propertyNameCamelCase + "\");\n" +
                    "\t}\n");
                os.write("\n\n\t/**\n" +
                    "\t* Changes the " + propertyName + " of this " + modelName + "\n" +
                    "\t* @param " + propertyNameCamelCase + " The new " + propertyName + " to set\n" +
                    "\t* @return " + modelName + "\n" +
                    "\t*/\n");
                os.write("\tpublic " + modelName + " set" + propertyNameCapitalised + "(" + propertyType + " " + propertyNameCamelCase + ") {\n" +
                    "\t\tsetProperty(\"" + propertyNameCamelCase + "\", " + propertyNameCamelCase + ");\n" +
                    "\t\treturn this;\n" +
                    "\t}\n");
                if (isArray) {
                    os.write("\n\n\t/**\n" +
                        "\t* Adds the given " + propertyName + " to this " + modelName + "\n" +
                        "\t* @param " + propertyNameCamelCase.substring(0, 1) + " The " + propertyName + " to add\n" +
                        "\t* @return " + modelName + "\n" +
                        "\t*/\n");
                    os.write("\tpublic " + modelName + " add" + propertyNameCapitalised + "(" + propertyTypeName + " " + propertyNameCamelCase.substring(0, 1) + ") {\n" +
                        "\t\t" + propertyType + " " + propertyNameCamelCase + " = this.get" + propertyNameCapitalised + "();\n" +
                        "\t\tif (" + propertyNameCamelCase + " == null) {\n" +
                        "\t\t\t" + propertyNameCamelCase + " = new ArrayList();\n" +
                        "\t\t\tthis.set" + propertyNameCapitalised + "(" + propertyNameCamelCase + "); \n\t\t}\n" +
                        "\t\t" + propertyNameCamelCase + ".add(" + propertyNameCamelCase.substring(0, 1) + ");\n" +
                        "\t\treturn this;\n" +
                        "\t}\n");
                }
            }
            os.write("}\n\n");
            return os.toString();
        }
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

    String separate(String name) {
        if (null == name) {
            return null;
        }
        //name as name
        StringBuilder output = new StringBuilder();

        String[] words = name.split("(?=\\p{Upper})");

        for (String word : words) {
            String separate = word.toLowerCase();
            output.append(separate);
            output.append(" ");
        }

        output.deleteCharAt(output.length() - 1);

        return output.toString();
    }

    private String getDataType(TTIriRef dataType, boolean dataModel, boolean isArray) {
        String dataTypeName = null;
        if (dataType.getIri().startsWith(XSD.NAMESPACE.iri)) {
            dataTypeName = capitalise(getSuffix(dataType.getIri()));
        } else if (dataModel) {
            dataTypeName = "UUID";
        } else if (dataType.getIri().startsWith("http://endhealth.info/im#VSET_")
            || "http://endhealth.info/im#Status".equals(dataType.getIri())
            || "http://endhealth.info/im#Graph".equals(dataType.getIri())
            || "http://www.w3.org/2000/01/rdf-schema#Resource".equals(dataType.getIri())
            || "http://snomed.info/sct#999002991000000109".equals(dataType.getIri())
            || "http://snomed.info/sct#999002981000000107".equals(dataType.getIri())
            || "http://hl7.org/fhir/ValueSet/administrative-gender".equals(dataType.getIri())
            || "http://endhealth.info/im#1731000252106".equals(dataType.getIri())) {
            dataTypeName = "String";
        } else if ("http://endhealth.info/im#DateTime".equals(dataType.getIri())) {
            dataTypeName = "PartialDateTime";
        } else {
            LOG.error("Unknown data type [{} - {}], defaulting to String", dataType.getIri(), dataType.getName());
            dataTypeName = "String /* UNKNOWN " + capitalise(dataType.getName()) + "*/";
        }
        if (isArray)
            dataTypeName = "List<" + dataTypeName + ">";

        return dataTypeName;
    }

    String getSuffix(String iri) {
        return iri.substring(iri.lastIndexOf("#") + 1);
    }
}
