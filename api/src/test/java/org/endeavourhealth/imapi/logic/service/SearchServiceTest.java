package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.SparqlConverter;
import org.endeavourhealth.imapi.model.iml.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

class SearchServiceTest {


	void queryIM() throws DataFormatException, IOException {

		//for (QueryRequest qr: List.of(TestQueries.query1(),TestQueries.query2(),
			//TestQueries.query4(),TestQueries.query5(),TestQueries.query6())){
			//output(qr);
//		}

		//QueryRequest qr;
		//qr== TestQueries.query1();
		//qr= TestQueries.query2();
		//qr= TestQueries.query4();
		//qr= TestQueries.query5();
		//qr= TestQueries.query6();
		//output(qr);

	}


	private static void output(QueryRequest dataSet) throws IOException, DataFormatException {
		SparqlConverter converter= new SparqlConverter(dataSet);
		String name= dataSet.getQuery().getName();
		String spq= converter.getSelectSparql();
		try (FileWriter wr = new FileWriter("c:\\\\TestQuerySpq-iml\\" + name + "_result.json")) {
			wr.write(spq);
		}

		SearchService searchService = new SearchService();
		if (dataSet.getQuery()!=null)
			name= dataSet.getQuery().getName();
		System.out.println("Testing "+ name);
		try (FileWriter wr = new FileWriter("C:\\Users\\david\\CloudStation\\EhealthTrust\\DiscoveryDataService\\IMAPI\\TestQueries\\Definitions\\" + name + ".json")) {
			wr.write(dataSet.getJson());
		}
		ObjectMapper om= new ObjectMapper();

		TTDocument result = searchService.queryIM(dataSet);
		try (FileWriter wr = new FileWriter("C:\\Users\\david\\CloudStation\\EhealthTrust\\DiscoveryDataService\\IMAPI\\TestQueries\\Results\\" + name + "_result.json")) {
			wr.write(om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(result));
		}



	}
}