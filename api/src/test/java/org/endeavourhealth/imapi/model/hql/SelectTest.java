package org.endeavourhealth.imapi.model.hql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class SelectTest {

	@Test
	void setFields() throws JsonProcessingException {
		Hql hql= HqlFactory.createHql();
		Match match= new Match();
		hql.setWhere(match);

		ObjectMapper objectMapper= new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		String json= objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(hql);
		System.out.println(json);
		Hql q2= objectMapper.readValue(json,Hql.class);

	}
}