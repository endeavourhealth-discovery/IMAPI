package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

@JsonPropertyOrder({"operator","fromGraphs","where","resultVariable","resultType"})
public class MatchClause {
	private QueryOperator operator =QueryOperator.AND;
	private WhereClause where;
	private List<TTIriRef> fromGraphs;
	private String resultVariable;
	private ResultType resultType;



	public List<TTIriRef> getFromGraphs() {
		return fromGraphs;
	}

	public MatchClause setFromGraphs(List<TTIriRef> fromGraphs) {
		this.fromGraphs = fromGraphs;
		return this;
	}

	public String getResultVariable() {
		return resultVariable;
	}

	public MatchClause setResultVariable(String resultVariable) {
		this.resultVariable = resultVariable;
		return this;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public MatchClause setResultType(ResultType resultType) {
		this.resultType = resultType;
		return this;
	}

	public WhereClause getWhere() {
		return where;
	}

	public MatchClause setWhere(WhereClause where) {
		this.where = where;
		return this;
	}

	public QueryOperator getOperator() {
		return operator;
	}

	public MatchClause setOperator(QueryOperator operator) {
		this.operator = operator;
		return this;
	}
}
