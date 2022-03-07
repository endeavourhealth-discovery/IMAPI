package org.endeavourhealth.imapi.model.hql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HqlFactory {

		public static Profile createProfileFromJson(String json) throws JsonProcessingException {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(json,Profile.class);
	}

	public static HqlDocument createHqlDocument(){
			return new HqlDocument();
	}
}
