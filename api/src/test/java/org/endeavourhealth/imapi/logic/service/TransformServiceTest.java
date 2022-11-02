package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.iml.TransformMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransformServiceTest {

	@Test
	void transform() throws JsonProcessingException {
		TransformMap map= TestMaps.fhirPatient();
		System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map));
	}
}