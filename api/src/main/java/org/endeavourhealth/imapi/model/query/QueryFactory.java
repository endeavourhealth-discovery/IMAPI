package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryFactory {

		public static Profile createProfileFromJson(String json) throws JsonProcessingException {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(json, Profile.class);
	}

	public static QueryDocument createQueryDocument(){
			return new QueryDocument();
	}

	public static Query createQuery(){
		return new Query();
	}
}
