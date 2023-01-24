package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.SparqlConverter;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.*;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTTypedRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

class SearchServiceTest {

	private String testDefinitions;
	private String testResults;
	private String testSparql;


	//@Test
	void runOS() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
		SearchRequest request= new SearchRequest();
		request.setIndex("david");
		request.setTermFilter("hospital admission");
		List<String> schemes= Arrays.asList(SNOMED.NAMESPACE,IM.NAMESPACE);
		List<String> types= Arrays.asList(IM.CONCEPT.getIri());
		request.setSchemeFilter(schemes);
		request.setStatusFilter(Arrays.asList(IM.ACTIVE.getIri()));
		request.setTypeFilter(types);
		SearchService ss= new SearchService();
		List<SearchResultSummary> results= ss.getEntitiesByTerm(request);
	}

	//@Test
	void queryIM() throws DataFormatException, IOException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
		testDefinitions= System.getenv("folder")+"\\Definitions";
		testResults= System.getenv("folder")+"\\Results";
		testSparql = System.getenv("folder")+"\\Sparql";
		//QueryRequest qr= TestQueries.allowableChildTypes();
		for (QueryRequest qr1: List.of(TestQueries.pathToCSA(),TestQueries.pathToAtenolol(),TestQueries.pathDobQuery())) {
			output(qr1);
		}


		/*

		for (QueryRequest qr1: List.of(TestQueries.complexECL(),TestQueries.getLegPain(),TestQueries.getIsas(),TestQueries.oralNsaids(),TestQueries.getAllowableRanges(),TestQueries.getAllowableProperties(),TestQueries.getConcepts(),TestQueries.query2(),TestQueries.query1(),
			TestQueries.query4(),TestQueries.query5(),TestQueries.query6())){
			output(qr1);
		}

		 */

	}


	private void output(QueryRequest dataSet) throws IOException, DataFormatException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
		String name;
		if (dataSet.getPathQuery()!=null)
			name= dataSet.getPathQuery().getName();
		else
			name= dataSet.getQuery().getName();
		if (name!=null)
			name= name.replaceAll(" ","").replaceAll("\\(","").replaceAll("\\)","");
		else
			name="unnamed query";
		SearchService searchService = new SearchService();
		System.out.println("Testing "+ name);
		try (FileWriter wr = new FileWriter(testDefinitions+ "\\"  + name+ "_definition.json")) {
			wr.write(dataSet.getJson());
		}
		ObjectMapper om= new ObjectMapper();
		if (dataSet.getQuery()!=null) {
			SparqlConverter converter = new SparqlConverter(dataSet);
			String spq = converter.getSelectSparql(null);
			try (FileWriter wr = new FileWriter(testSparql + "\\" + name + "_sparql.json")) {
				wr.write(spq);
			}
			TTDocument result = searchService.queryIM(dataSet);
			try (FileWriter wr = new FileWriter(testResults + "\\" + name + "_result.json")) {
				wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
			}
			System.out.println("Found " + result.getEntities().size() + " entities");
		}
		else {
			PathDocument result = searchService.pathQuery(dataSet);
			try (FileWriter wr = new FileWriter(testResults + "\\" + name + "_result.json")) {
				wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
			}
		}

	}
}
