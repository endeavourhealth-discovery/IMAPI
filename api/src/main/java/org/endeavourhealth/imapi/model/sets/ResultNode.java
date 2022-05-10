package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder ({"@id"})
public class ResultNode extends HashMap<String,Object> {

	@JsonIgnore
	@SuppressWarnings("unchecked")
	public ResultNode add(String key, Object value) {
		computeIfAbsent(key, v-> new HashSet<>());
		((Set<Object>) get(key)).add(value);
		return this;
	}

	@JsonIgnore
	public String asJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);

	}
}
