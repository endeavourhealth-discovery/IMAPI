package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class SelectTest {

	@Test
	void setFields() throws JsonProcessingException {
		Query query = QueryFactory.createQuery();
		Match match= new Match();
		query.setWhere(match);

		ObjectMapper objectMapper= new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		String json= objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(query);
		System.out.println(json);
		Query q2= objectMapper.readValue(json, Query.class);

	}
}