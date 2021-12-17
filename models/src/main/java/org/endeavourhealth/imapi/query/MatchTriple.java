package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;

import java.util.List;

@JsonPropertyOrder({"operator","fromGraphs","where","resultVariable","resultType"})
public class MatchTriple extends TTNode {
	private QueryOperator operator =QueryOperator.AND;
	private WhereClause where;
	private List<TTIriRef> fromGraphs;
	private String resultVariable;
	private ResultType resultType;



	public List<TTIriRef> getFromGraphs() {
		return fromGraphs;
	}

	public MatchTriple setFromGraphs(List<TTIriRef> fromGraphs) {
		this.fromGraphs = fromGraphs;
		return this;
	}

	public String getResultVariable() {
		return resultVariable;
	}

	public MatchTriple setResultVariable(String resultVariable) {
		this.resultVariable = resultVariable;
		return this;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public MatchTriple setResultType(ResultType resultType) {
		this.resultType = resultType;
		return this;
	}

	public WhereClause getWhere() {
		return where;
	}

	public MatchTriple setWhere(WhereClause where) {
		this.where = where;
		return this;
	}

	public QueryOperator getOperator() {
		return operator;
	}

	public MatchTriple setOperator(QueryOperator operator) {
		this.operator = operator;
		return this;
	}
}
