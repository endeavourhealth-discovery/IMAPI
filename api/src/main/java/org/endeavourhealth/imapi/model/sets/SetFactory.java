package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SetFactory {



	public static SetModel createSetModelFromJson(String json) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, SetModel.class);
	}

}
