package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.PathDocument;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

class SearchServiceTest {

	private String testDefinitions;
	private String testResults;
	private String succinctDefinitions;


//@Test
	void runOS() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {

		SearchRequest request= new SearchRequest();
		request.setIndex("david");
		request.setTermFilter("^ESCTVE439120");
		List<String> schemes= Arrays.asList(IM.NAMESPACE,SNOMED.NAMESPACE);
		List<String> types= Arrays.asList(IM.CONCEPT.getIri());
		request.setSchemeFilter(schemes);
		request.setStatusFilter(Arrays.asList(IM.ACTIVE.getIri()));
		request.setTypeFilter(types);
		SearchService ss= new SearchService();
		List<SearchResultSummary> results= ss.getEntitiesByTerm(request);
		for (SearchResultSummary result:results) {
			System.out.println(result.getMatch()+" :"+ result.getCode());
		}

	}

	//@Test
	void imq() throws DataFormatException, IOException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
		testDefinitions = System.getenv("folder") + "\\Definitions";
		testResults = System.getenv("folder") + "\\Results";
		String testSparql = System.getenv("folder") + "\\Sparql";
		succinctDefinitions = System.getenv("folder") + "\\SuccinctSyntax";

		for (QueryRequest qr1 : List.of(
			TestQueries.pathDobQuery(),
			TestQueries.pathToPostCode(),
			TestQueries.deleteSets(),
			TestQueries.getAllowableSubtypes(),
			TestQueries.pathToCSA(),
			TestQueries.pathToAtenolol()
		)) {
			output(qr1);
		}

		for (QueryRequest qr1 : List.of(
			TestQueries.getAllowableProperties(),
			TestQueries.subtypesParameterised(), TestQueries.substanceTextSearch(),
			TestQueries.rangeTextSearch(), TestQueries.getAllowableRanges(), TestQueries.oralNsaids(),
			TestQueries.getAllowableProperties(), TestQueries.getIsas(),
			TestQueries.getConcepts(), TestQueries.query2(), TestQueries.query1(),
			TestQueries.query4(), TestQueries.query5(), TestQueries.query6())) {
			output(qr1);
		}
	}





	private void output(QueryRequest dataSet) throws IOException, DataFormatException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
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
		try (FileWriter wr = new FileWriter(testDefinitions + "\\" + name + "_definition.json")) {
			wr.write(new ObjectMapper().writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(dataSet));
		}
		ObjectMapper om = new ObjectMapper();
		if (dataSet.getQuery() != null) {
			TTDocument result = searchService.queryIM(dataSet);
			try (FileWriter wr = new FileWriter(testResults + "\\" + name + "_result.json")) {
				wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
			}
			System.out.println("Found " + result.getEntities().size() + " entities");
		}
		else if (dataSet.getUpdate() != null) {
			searchService.updateIM(dataSet);
		}
		else {
			PathDocument result = searchService.pathQuery(dataSet);
			try (FileWriter wr = new FileWriter(testResults + "\\" + name + "_result.json")) {
				wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
			}
		}

	}

	//@Test
	public void setTest() throws DataFormatException, JsonProcessingException {
		EntityService es= new EntityService();
		TTEntity entity= es.getFullEntity(IM.NAMESPACE+"VSET_VitalSigns").getEntity();
		String json = entity.get(IM.DEFINITION).asLiteral().getValue();
		SetExporter exporter = new SetExporter();
		Set<Concept> concepts = exporter.getExpandedSetMembers(IM.NAMESPACE + "VSET_VitalSigns", false);
		System.out.println(concepts.size());
	}
}

