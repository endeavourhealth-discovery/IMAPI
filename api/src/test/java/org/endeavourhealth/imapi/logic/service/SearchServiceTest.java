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


	@Test
	void queryIM() throws DataFormatException, IOException {

		for (QueryRequest qr: List.of(TestQueries.query2(),TestQueries.query1(),
			TestQueries.query4(),TestQueries.query5(),TestQueries.query6())){
			output(qr);
		}

		//QueryRequest qr;
		//qr== TestQueries.query1();
		//qr= TestQueries.query2();
		//qr= TestQueries.query4();
		//qr= TestQueries.query5();
		//qr= TestQueries.query6();
		//output(qr);



	}


	private static void output(QueryRequest dataSet) throws IOException, DataFormatException {
		
		String name= dataSet.getQuery().getName();
		SearchService searchService = new SearchService();
		System.out.println("Testing "+ name);
		try (FileWriter wr = new FileWriter("C:\\Users\\david\\CloudStation\\EhealthTrust\\DiscoveryDataService\\IMAPI\\TestQueries\\Definitions\\" + name + ".json")) {
			wr.write(dataSet.getJson());
		}
		ObjectMapper om= new ObjectMapper();

		QueryRequest redo= om.readValue(dataSet.getJson(),QueryRequest.class);

		SparqlConverter converter= new SparqlConverter(dataSet);

		String spq= converter.getSelectSparql();
		try (FileWriter wr = new FileWriter("c:\\\\TestQuerySpq-iml\\" + name + "_result.json")) {
			wr.write(spq);
		}

		TTDocument result = searchService.queryIM(dataSet);
		try (FileWriter wr = new FileWriter("C:\\Users\\david\\CloudStation\\EhealthTrust\\DiscoveryDataService\\IMAPI\\TestQueries\\Results\\" + name + "_result.json")) {
			wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
		}



	}
}