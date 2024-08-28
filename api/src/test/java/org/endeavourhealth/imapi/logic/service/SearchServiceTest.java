package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.controllers.EntityController;
import org.endeavourhealth.imapi.json.JsonLDMapper;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.PathDocument;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

class SearchServiceTest {

  private String succinctDefinitions;

  //@Test
    void testdataModelProperties() throws IOException {
    System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
      new EntityController().getDataModelProperties(IM.NAMESPACE+"Observation")));
  }


  //@Test
  void imq() throws DataFormatException, IOException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, QueryException {
    output(TestQueries.pathQuery());
    /*
    output(TestQueries.getAllowableSubtypes());
    output(TestQueries.query2());
    //output(TestQueries.pathQueryAtenolol3());

    output(TestQueries.getAllowableQueries());

    output(TestQueries.query6());
    output(TestQueries.dataModelPropertyRange());
    output(TestQueries.rangeSuggestion());
    output(TestQueries.getMembers());

    output(TestQueries.AllowablePropertiesForCovid());
    output(TestQueries.query1());
    output(TestQueries.query2());
    output(TestQueries.getShaclProperty());

    output(TestQueries.deleteSets());


    output(TestQueries.getAllowableProperties());
    output(TestQueries.subtypesParameterised());
    output(TestQueries.substanceTextSearch());
    output(TestQueries.rangeTextSearch());
    output(TestQueries.getAllowableRanges());
    output(TestQueries.oralNsaids());
    output(TestQueries.getAllowableProperties());
    output(TestQueries.getIsas());
    output(TestQueries.getConcepts());
    output(TestQueries.query4());

     */


  }

  private void output(QueryRequest dataSet) throws IOException, DataFormatException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, QueryException {
    String name = null;

    if (dataSet.getPathQuery() != null)
      name = dataSet.getPathQuery().getName();
    else if (dataSet.getQuery() != null)
      name = dataSet.getQuery().getName();
    else if (dataSet.getUpdate() != null)
      name = dataSet.getUpdate().getName();
    if (name != null)
      name = name.replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "");
    else
      name = "unnamed query";
    SearchService searchService = new SearchService();
    System.out.println("Testing " + name);
    dataSet.setContext(TTManager.createBasicContext());
    Path path = Paths.get("TestQueries/Definitions/" + name + "_definition.json").toAbsolutePath();

    try (FileWriter wr = new FileWriter(path.toString())) {
      wr.write(new JsonLDMapper().writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(dataSet));
    }
    ObjectMapper om = new ObjectMapper();

    Path of = Path.of("TestQueries/Results/" + name + "_results.json");
    if (dataSet.getQuery() != null) {
      JsonNode result = searchService.queryIM(dataSet);
      path = of.toAbsolutePath();
      try (FileWriter wr = new FileWriter(path.toString())) {
        wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
        System.out.println("Found " + result.get("entities").size() + " entities");
      }
    }
    else if (dataSet.getPathQuery() != null) {
      PathDocument result= searchService.pathQuery(dataSet.getPathQuery());
      path = of.toAbsolutePath();
      try (FileWriter wr = new FileWriter(path.toString())) {
        wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
      }
    } else if (dataSet.getUpdate() != null) {
      searchService.updateIM(dataSet);
    }


  }

  //@Test
  public void setTest() throws DataFormatException, JsonProcessingException, QueryException {
    EntityService es = new EntityService();
    TTEntity entity = es.getFullEntity(IM.NAMESPACE + "VSET_VitalSigns").getEntity();
    String json = entity.get(iri(IM.DEFINITION)).asLiteral().getValue();
    SetExporter exporter = new SetExporter();
    Set<Concept> concepts = exporter.getExpandedSetMembers(IM.NAMESPACE + "VSET_VitalSigns", true, false, true, List.of());
    System.out.println(concepts.size());
  }
}

