package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.json.JsonLDMapper;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Order;
import org.endeavourhealth.imapi.model.imq.PathDocument;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

class SearchServiceTest {

	private String testDefinitions;
	private String testResults;
	private String succinctDefinitions;

	//@Test
	void runOS() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, IOException {
		testDefinitions = System.getenv("folder") + "\\Definitions";
		testResults = System.getenv("folder") + "\\Results";
	 Date startTime = new Date();
	 int count=0;
		 for (String input : List.of("Systolic bloo")) {
			 count++;
			 SearchRequest request = TestQueries.observationConcepts(input);
			 output(request, "observation entities starting with Systolic bl", true);
			 Long start=null;
			 for (Map<Long,String> entries: request.getTimings()){
				 for (Map.Entry<Long,String> entry:entries.entrySet()){
					 if (start==null){
						 System.out.println("0 "+ entry.getValue());
						 start= entry.getKey();
					 }
					 else {
						 System.out.println(entry.getKey() - start + " " + entry.getValue());
					 }
				 }
			 }
		 }
	 Date endTime = new Date();
	 System.out.println("average = "+ ((endTime.getTime()-startTime.getTime())/count)+" milliseconds");


	}
 //@Test
	void imq() throws DataFormatException, IOException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, QueryException {
		testDefinitions = System.getenv("folder") + "\\Definitions";
		testResults = System.getenv("folder") + "\\Results";
		String testSparql = System.getenv("folder") + "\\Sparql";
		succinctDefinitions = System.getenv("folder") + "\\SuccinctSyntax";
	 output(TestQueries.getAllowableSubtypes());
		output(TestQueries.query2());
		output(TestQueries.pathQueryAtenolol3());

	 output(TestQueries.getAllowableQueries());

		output(TestQueries.query6());
		output(TestQueries.dataModelPropertyRange());
	 output(TestQueries.rangeSuggestion());
		output(TestQueries.getMembers());

			output(TestQueries.AllowablePropertiesForCovid());
			output(TestQueries.query1());
			output(TestQueries.query2());
			output(TestQueries.getShaclProperty());

			output(TestQueries.pathToAtenolol());
			output(TestQueries.pathDobQuery());
			output(TestQueries.pathToPostCode());
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


	}



	private void output(SearchRequest request,String name,boolean write) throws IOException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {

		SearchService ss = new SearchService();
		List<SearchResultSummary> results = ss.getEntitiesByTerm(request);
		if (write) {
			try (FileWriter wr = new FileWriter(testDefinitions + "\\" + name + "_definition.json")) {
				wr.write(new ObjectMapper().writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(request));
			}
			System.out.println("Found "+ results.size()+" entities" );

		}

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

		try (FileWriter wr = new FileWriter(testDefinitions + "\\" + name + "_definition.json")) {
			wr.write(new JsonLDMapper().writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(dataSet));
		}
		ObjectMapper om= new ObjectMapper();

		if (dataSet.getQuery() != null) {
			JsonNode result = searchService.queryIM(dataSet);
			try (FileWriter wr = new FileWriter(testResults + "\\" + name + "_result.json")) {
 				wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
			}

			System.out.println("Found " + result.get("entities").size() + " entities");
		}
		else if (dataSet.getUpdate() != null) {
			searchService.updateIM(dataSet);
		}
		else {
			PathDocument result = searchService.pathQuery(dataSet);
			try (FileWriter wr = new FileWriter(testResults + "\\" + name + "_result.json")) {
				wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
			}
			System.out.println("found "+ result.getMatch().size()+" paths");
		}

	}

	//@Test
	public void setTest() throws DataFormatException, JsonProcessingException, QueryException {
		EntityService es= new EntityService();
		TTEntity entity= es.getFullEntity(IM.NAMESPACE+"VSET_VitalSigns").getEntity();
		String json = entity.get(IM.DEFINITION).asLiteral().getValue();
		SetExporter exporter = new SetExporter();
		Set<Concept> concepts = exporter.getExpandedSetMembers(IM.NAMESPACE + "VSET_VitalSigns", false, true, List.of());
		System.out.println(concepts.size());
	}
}

