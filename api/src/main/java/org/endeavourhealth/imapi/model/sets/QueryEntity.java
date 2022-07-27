package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * POJO equivalent of an entity of type query
 */
public class QueryEntity extends Heading{
	private Query query;

	public Query getQuery() {
		return query;
	}

	public QueryEntity setQuery(Query query) {
		this.query = query;
		return this;
	}

	@JsonIgnore
	public String getJson() throws JsonProcessingException {
		return Query.getJson(this);
	}
}
