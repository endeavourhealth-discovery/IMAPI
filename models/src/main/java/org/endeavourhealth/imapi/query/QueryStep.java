package org.endeavourhealth.imapi.query;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"include","description","match","or","sort","label"})
public class QueryStep {

	private String label;
	private String description;
	private QuerySort sort;
	private List<MatchClause> match;
	private Include include;
	private List<MatchClause> or;

	public String getLabel() {
		return label;
	}

	public QueryStep setLabel(String label) {
		this.label = label;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public QueryStep setDescription(String description) {
		this.description = description;
		return this;
	}

	public QuerySort getSort() {
		return sort;
	}

	public QueryStep setSort(QuerySort sort) {
		this.sort = sort;
		return this;
	}

	public List<MatchClause> getMatch() {
		return match;
	}

	public QueryStep setMatch(List<MatchClause> match) {
		this.match = match;
		return this;
	}

	public MatchClause addMatch(MatchClause clause){
		if (this.match==null)
			match= new ArrayList<>();
		match.add(clause);
		return clause;
	}
	public MatchClause addMatch(){
		return addMatch(new MatchClause());
	}

	public Include getInclude() {
		return include;
	}

	public QueryStep setInclude(Include include) {
		this.include = include;
		return this;
	}
	public List<MatchClause> getOr() {
		return or;
	}

	public QueryStep setOr(List<MatchClause> or) {
		this.or = or;
		return this;
	}

	public MatchClause addOr(){
		return addOr(new MatchClause());
	}

	public MatchClause addOr(MatchClause match){
		if (this.or==null)
			this.or= new ArrayList<>();
		or.add(match);
		return match;
	}
}
