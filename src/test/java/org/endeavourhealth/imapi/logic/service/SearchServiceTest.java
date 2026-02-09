package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.json.JsonLDMapper;
import org.endeavourhealth.imapi.logic.reasoner.SetMemberGenerator;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.PathDocument;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

class SearchServiceTest {

  EntityService entityService = new EntityService();
  private String succinctDefinitions;
//@Test
  void os() throws QueryException, OpenSearchException {
  Set<String> entities= getResults(TestQueries.getAllowableProperties());
  QueryRequest osSearch= TestQueries.entityFilter(entities);
  SearchResponse results= new SearchService().queryIMSearch(osSearch);
  System.out.println(results.getEntities().size());


}
  //@Test
  void imq() throws Exception {
    output(TestQueries.getAllowableProperties());
    /*
    ask(TestQueries.isValidProperty());


    output(TestQueries.AllowablePropertiesForCovid());
    output(TestQueries.subtypesParameterised());
    output(TestQueries.dataModelPropertyRange());
    output(TestQueries.getAllowableSubtypes());

    output(TestQueries.AllowablePropertiesForCovid());
    output(TestQueries.getAllowableProperties());
    output(TestQueries.pathQuery());
    output(TestQueries.rangeSuggestion());
    output(TestQueries.getShaclProperty());
    output(TestQueries.deleteSets());
    output(TestQueries.substanceTextSearch());
    output(TestQueries.oralNsaids());

    output(TestQueries.getIsas());
    output(TestQueries.getConcepts());
    output(TestQueries.query4());

     */


  }
  private Set<String> getResults(QueryRequest request) throws OpenSearchException, QueryException {
    SearchService searchService = new SearchService();
    JsonNode results = searchService.queryIM(request);
    Set<String> iris = new HashSet<>();
    JsonNode entities = results.get("entities");

    if (entities != null && entities.isArray()) {
      for (JsonNode entity : entities) {
        JsonNode iriNode = entity.get("iri");
        if (iriNode != null && iriNode.isTextual()) {
          iris.add(iriNode.asText());
        }
      }
    }
    return iris;
  }

  private void output(QueryRequest dataSet) throws IOException, OpenSearchException, QueryException {
    String name = null;
    String originalRequest = new ObjectMapper().writeValueAsString(dataSet);

    if (dataSet.getPathQuery() != null)
      name = dataSet.getPathQuery().getName();
    else if (dataSet.getQuery() != null)
      name = dataSet.getQuery().getName();
    else if (dataSet.getUpdate() != null)
      name = dataSet.getUpdate().getName();
    if (name != null)
      name = name.replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "");
    else if (dataSet.getName() != null)
      name = dataSet.getName();
    else name = "unnamed query";
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
        if (result.get("entities").isEmpty()) {
          dataSet = new ObjectMapper().readValue(originalRequest, QueryRequest.class);
          searchService.queryIM(dataSet);
          throw new RuntimeException("No results found for query " + name);
        }
      }
    } else if (dataSet.getPathQuery() != null) {
      PathDocument result = searchService.pathQuery(dataSet.getPathQuery());
      path = of.toAbsolutePath();
      try (FileWriter wr = new FileWriter(path.toString())) {
        wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
      }
    } else if (dataSet.getUpdate() != null) {
      searchService.updateIM(dataSet, Graph.IM);
    }


  }

  private void ask(QueryRequest askQuery) throws Exception {
    SearchService searchService = new SearchService();
    if (searchService.askQueryIM(askQuery)) {
      System.out.println("ask query Valid");
    } else
      throw new Exception("invalid ask query");
  }

  //@Test
  public void setTest() throws JsonProcessingException, QueryException {
    new SetMemberGenerator().generateMembers("http://apiqcodes.org/qcodes#QCodeGroup_713", Graph.IM);
  }
}

