package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@JsonPropertyOrder({"iri","name","description","from","match","select","subQuery"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends Heading {
	private From from;
	private Match match;
	private List<Select> select;
	private List<Query> subQuery;

	public List<Query> getSubQuery() {
		return subQuery;
	}

	public Query setSubQuery(List<Query> subQuery) {
		this.subQuery = subQuery;
		return this;
	}

	public Query addSubQuery(Query query){
		if (this.subQuery==null)
			this.subQuery= new ArrayList<>();
		this.subQuery.add(query);
		return this;
	}

	public From getFrom() {
		return from;
	}

	public Query setFrom(From from) {
		this.from = from;
		return this;
	}
	@JsonIgnore
	public Query from (Consumer<From> builder){
		this.from= new From();
		builder.accept(this.from);
		return this;
	}

	public Match getMatch() {
		return match;
	}

	public Query setMatch(Match match) {
		this.match = match;
		return this;
	}



	public Query match(Consumer<Match> builder){
		Match match= new Match();
		this.match=match;
		builder.accept(match);
		return this;
	}

	public List<Select> getSelect() {
		return select;
	}

	public Query setSelect(List<Select> select) {
		this.select = select;
		return this;
	}

	public Query addSelect(Select select){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(select);
		return this;
	}

	@JsonIgnore
	public Query select(Consumer<Select> builder){
		if (this.select==null)
			this.select= new ArrayList<>();
		Select select= new Select();
		this.select.add(select);
		builder.accept(select);
		return this;
	}

}
