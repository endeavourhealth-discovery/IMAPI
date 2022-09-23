package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.SparqlConverter;
import org.endeavourhealth.imapi.model.iml.*;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

class SearchServiceTest {

	private String testDefinitions;
	private String testResults;
	private String testSparql;



	//@Test
	void queryIM() throws DataFormatException, IOException {
		testDefinitions= System.getenv("testDefinitions");
		testResults= System.getenv("testResults");
		testSparql = System.getenv("testSparql");

		for (QueryRequest qr: List.of(TestQueries.getIsas(),TestQueries.oralNsaids(),TestQueries.getAllowableRanges(),TestQueries.getAllowableProperties(),TestQueries.getConcepts(),TestQueries.query2(),TestQueries.query1(),
			TestQueries.query4(),TestQueries.query5(),TestQueries.query6())){
			output(qr);
		}

	}


	private void output(QueryRequest dataSet) throws IOException, DataFormatException {
		
		String name= dataSet.getQuery().getName();
		SearchService searchService = new SearchService();
		System.out.println("Testing "+ name);
		try (FileWriter wr = new FileWriter(testDefinitions+ "\\" + name + ".json")) {
			wr.write(dataSet.getJson());
		}
		ObjectMapper om= new ObjectMapper();

		QueryRequest redo= om.readValue(dataSet.getJson(),QueryRequest.class);

		SparqlConverter converter= new SparqlConverter(dataSet);

		String spq= converter.getSelectSparql();
		try (FileWriter wr = new FileWriter(testSparql+"\\" + name + "_result.json")) {
			wr.write(spq);
		}

		TTDocument result = searchService.queryIM(dataSet);
		try (FileWriter wr = new FileWriter(testResults+"\\" + name + "_result.json")) {
			wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
		}
		System.out.println("Found "+ result.getEntities().size()+ " entities");


	}
}
