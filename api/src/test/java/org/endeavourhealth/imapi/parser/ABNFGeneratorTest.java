package org.endeavourhealth.imapi.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.sets.QueryRequest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ABNFGeneratorTest {

	@Test
	void generateTable() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		ABNFGenerator generator= new ABNFGenerator();
		generator.generateTable(QueryRequest.class);
	}
}