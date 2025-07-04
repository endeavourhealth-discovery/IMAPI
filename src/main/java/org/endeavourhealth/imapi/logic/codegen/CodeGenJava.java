package org.endeavourhealth.imapi.logic.codegen;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.model.codegen.DataModel;
import org.endeavourhealth.imapi.model.codegen.DataModelProperty;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Slf4j
public class CodeGenJava {
  private final Queue<String> iris = new PriorityQueue<>();
  private final HashMap<String, DataModel> models = new HashMap<>();

  public void generate(ZipOutputStream os, Graph graph) throws IOException {
    getModelList(graph);
    getDataModelRecursively(graph);
    generateJavaCode(os);
  }

  private void getModelList(Graph graph) {
    log.debug("getting model list");

    String sql = """
      SELECT ?iri
      WHERE {
        ?iri (im:isContainedIn|rdfs:subClassOf)* im:HealthDataModel ;
        rdf:type shacl:NodeShape .
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery query = conn.prepareTupleSparql(sql);
      try (TupleQueryResult result = query.evaluate()) {
        while (result.hasNext()) {
          BindingSet bindSet = result.next();
          String iri = bindSet.getValue("iri").stringValue();
          log.trace("iri [{}]", iri);
          iris.add(iri);
        }
      }
    }
  }

  private void getDataModelRecursively(Graph graph) {
    log.debug("getting models");

    while (!iris.isEmpty()) {
      String iri = iris.remove();
      DataModel model = getDataModel(iri, graph);
      addMissingModelToQueue(model);
      models.put(iri, model);
    }
  }

  private DataModel getDataModel(String iri, Graph graph) {
    log.debug("get data model [{}]", iri);

    DataModel model = new DataModel().setIri(iri);

    String sql = """      
      SELECT ?iri ?model ?comment ?propname ?type ?typeName ?dm ?min ?max ?propcomment ?order
      WHERE {
        ?iri shacl:property ?prop .
        ?iri rdfs:label ?model .
        ?iri rdfs:comment ?comment .
        ?prop shacl:path ?propIri .
        ?propIri rdfs:label ?propname .
        OPTIONAL { ?prop shacl:order ?order }
        # OPTIONAL { ?prop shacl:class ?type }
        OPTIONAL { ?prop shacl:datatype ?type }
        # OPTIONAL { ?prop shacl:node ?type }
        OPTIONAL { ?type rdfs:label ?typeName }
        OPTIONAL { ?prop rdfs:comment ?propcomment }
        OPTIONAL { ?prop shacl:maxCount ?max }
        OPTIONAL { ?prop shacl:minCount ?min }
        bind( exists { ?type rdf:type shacl:NodeShape } as ?dm)
        FILTER not exists { ?prop im:inversePath ?inverse }
        } ORDER BY ?order
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery query = conn.prepareTupleSparql(sql);
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
          log.trace("iri [{}]", iri);
        }
      }
    }
    return model;
  }

  private void addMissingModelToQueue(DataModel model) {
    log.debug("add missing model to queue");

    for (DataModelProperty prop : model.getProperties()) {
      if (prop.isModel() && !iris.contains(prop.getDataType().getIri())
        && !model.getIri().equals(prop.getDataType().getIri())
        && !models.containsKey(prop.getDataType().getIri())) {
        iris.add(prop.getDataType().getIri());
      }
    }
  }

  private void generateJavaCode(ZipOutputStream zs) throws IOException {
    log.debug("generating code");

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

  private String capitalizeFirstCharacter(String input) {
    return input.substring(0, 1).toUpperCase() + separate(input).substring(1);
  }

  private String generateJavaCodeForModel(DataModel model, String modelName) throws IOException {
    try (StringWriter os = new StringWriter()) {

      String modelNameSeparated = separate(modelName);
      String modelComment = model.getComment();

      os.write("""
        package org.endeavourhealth.imapi.logic.codegen;
        
        import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
        
        import java.util.UUID;
        
        /**
        * Represents %s.
        """.formatted(modelNameSeparated));

      if (modelComment != null)
        os.write("* " + modelComment + "\n");
      os.write("""
        */
        public class %s extends IMDMBase<%s> {
        
        
          /**
          * %s constructor with identifier
          */
          public %s (UUID id) {
            super(%s, id);
          }
        """.formatted(modelName, modelName, capitalizeFirstCharacter(modelName), modelName, modelName));

      for (DataModelProperty property : model.getProperties()) {
        String propertyName = property.getName();
        String propertyNameCapitalised = capitalise(property.getName());
        String propertyNameCamelCase = propertyNameCapitalised.substring(0, 1).toLowerCase() + propertyNameCapitalised.substring(1);

        boolean isArray = property.getMaxCount() != null && property.getMaxCount() > 1;
        String propertyType = getDataType(property.getDataType(), property.isModel(), isArray);
        String propertyTypeName = getDataType(property.getDataType(), property.isModel(), false);

        os.write("""
          
          
            /**
            * Gets the %s of this %s
          """.formatted(propertyName, modelNameSeparated));
        if (property.getComment() != null) os.write("""
            * %s
          """.formatted(property.getComment()));
        os.write("""
              * @return %s
              */
              public %s get%s() {
                return getProperty("%s");
              }
            
              /**
              * Changes the %s of this %s
              * @param %s The new %s to set
              * @return %s
              */
              public %s set%s(%s %s) {
                setProperty("%s", %s);
                return this;
              }
            """.formatted(
            propertyNameCamelCase,
            propertyType,
            propertyNameCapitalised,
            propertyNameCamelCase,
            propertyName,
            modelName,
            propertyNameCamelCase,
            propertyName,
            modelName,
            modelName,
            propertyNameCapitalised,
            propertyType,
            propertyNameCamelCase,
            propertyNameCamelCase,
            propertyNameCamelCase
          )
        );
        if (isArray) {
          os.write("""
              
                /**
                * Adds the given %s to this %s
                * @param %s The %s to add
                @return %s
                */
                public %s add%s(%s %s) {
                  %s %s = this.get%s();
                  if (%s == null) {
                    %s = new ArrayList();
                    this.set%s(%s)
                  }
                  %s.add(%s);
                  return this;
                }
              """.formatted(
              propertyName,
              modelName,
              propertyNameCamelCase.charAt(0),
              propertyName,
              modelName,
              modelName,
              propertyNameCapitalised,
              propertyTypeName,
              propertyNameCamelCase.charAt(0),
              propertyType,
              propertyNameCamelCase,
              propertyNameCapitalised,
              propertyNameCamelCase,
              propertyNameCamelCase,
              propertyNameCapitalised,
              propertyNameCamelCase,
              propertyNameCamelCase,
              propertyNameCamelCase.charAt(0)
            )
          );
        }
      }
      os.write("}\n\n");
      return os.toString();
    }
  }

  String capitalise(String name) {
    if (null == name) {
      throw new IllegalArgumentException("Name cannot be null");
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
      throw new IllegalArgumentException("Name cannot be null");
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
    if (dataType.getIri().startsWith(XSD.NAMESPACE.toString())) {
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
      log.error("Unknown data type [{} - {}], defaulting to String", dataType.getIri(), dataType.getName());
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
