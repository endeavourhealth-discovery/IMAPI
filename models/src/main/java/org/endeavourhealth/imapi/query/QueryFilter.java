package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type","expression"})
public class QueryFilter {
	private FilterType type;
	private String expression;

	public FilterType getType() {
		return type;
	}

	public QueryFilter setType(FilterType type) {
		this.type = type;
		return this;
	}

	public String getExpression() {
		return expression;
	}

	public QueryFilter setExpression(String expression) {
		this.expression = expression;
		return this;
	}
}
