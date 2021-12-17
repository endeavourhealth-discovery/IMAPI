package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialised TTNode containing the model of a single feature for a query
 */
@JsonPropertyOrder ({"function","match","select","resultGraph","alias"})
public class QueryStep extends TTNode {
	private FunctionClause function;
	private MatchClause match;
	private List<Select> select;
	private List<Triple> graph;
	private String alias;

	public FunctionClause getFunction() {
		return function;
	}

	public QueryStep setFunction(FunctionClause function) {
		this.function = function;
		return this;
	}

	public MatchClause getMatch() {
		return match;
	}

	public QueryStep setMatch(MatchClause match) {
		this.match = match;
		return this;
	}




	public List<Select> getSelect() {
		return select;
	}

	public QueryStep setSelect(List<Select> select) {
		this.select = select;
		return this;
	}

	public QueryStep addSelect(Select select) {
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(select);
		return this;
	}

	public List<Triple> getGraph() {
		return graph;
	}

	public QueryStep setGraph(List<Triple> graph) {
		this.graph = graph;
		return this;
	}

	public QueryStep addTriple(Triple triple){
		if (this.graph==null)
			this.graph= new ArrayList<>();
		this.graph.add(triple);
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public QueryStep setAlias(String alias) {
		this.alias = alias;
		return this;
	}

}











