package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialised TTNode containing the model of a single feature for a query
 */
@JsonPropertyOrder ({"function","match","select","resultGraph","alias"})
public class QueryClause {
	private FunctionClause function;
	private MatchTriple match;
	private List<Select> select;
	private List<Triple> graph;
	private String alias;

	public FunctionClause getFunction() {
		return function;
	}

	public QueryClause setFunction(FunctionClause function) {
		this.function = function;
		return this;
	}

	public MatchTriple getMatch() {
		return match;
	}

	public QueryClause setMatch(MatchTriple match) {
		this.match = match;
		return this;
	}




	public List<Select> getSelect() {
		return select;
	}

	public QueryClause setSelect(List<Select> select) {
		this.select = select;
		return this;
	}

	public QueryClause addSelect(Select select) {
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(select);
		return this;
	}

	public List<Triple> getGraph() {
		return graph;
	}

	public QueryClause setGraph(List<Triple> graph) {
		this.graph = graph;
		return this;
	}

	public QueryClause addTriple(Triple triple){
		if (this.graph==null)
			this.graph= new ArrayList<>();
		this.graph.add(triple);
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public QueryClause setAlias(String alias) {
		this.alias = alias;
		return this;
	}
}











