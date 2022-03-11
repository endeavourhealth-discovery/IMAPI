package org.endeavourhealth.imapi.model.hql;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder ({"match","return"})
public class Hql {

	private Match match;
	private Return retn;

	public Match getMatch() {
		return match;
	}

	public Hql setMatch(Match match) {
		this.match = match;
		return this;
	}

	@JsonProperty("return")
	public Return getRetn() {
		return retn;
	}

	@JsonProperty("return")
	public Hql setRetn(Return retn) {
		this.retn = retn;
		return this;
	}
}
