package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

/**
 * POJO equivalent of an entity of type query
 */
public class QueryEntity extends Heading{
	private Query select;
	private Query ask;

	public Query getSelect() {
		return select;
	}

	public QueryEntity setSelect(Query select) {
		this.select = select;
		return this;
	}

	public Query getAsk() {
		return ask;
	}

	public QueryEntity setAsk(Query ask) {
		this.ask = ask;
		return this;
	}


	@JsonIgnore
	public String getasJson() throws JsonProcessingException {
		return Query.getJson(this);
	}
}
