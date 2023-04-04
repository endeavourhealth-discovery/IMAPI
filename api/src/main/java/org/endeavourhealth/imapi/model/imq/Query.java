package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"@context","iri","name","description","match","select","subQuery","groupBy","orderBy","direction","limit","having"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends TTIriRef {

	private String description;
	private List<Select> select;
	private boolean activeOnly;
	private boolean usePrefixes;
	private List<Query> query;
	private List<Match> match;
	private List<OrderLimit> orderBy;
	private List<Element> groupBy;


	public List<Match> getMatch() {
		return match;
	}

	public Query setMatch(List<Match> from) {
		this.match = from;
		return this;
	}

	public Query addMatch(Match match){
		if (this.match ==null)
			this.match = new ArrayList<>();
		this.match.add(match);
		return this;
	}

	public Query match(Consumer<Match> builder){
		Match match= new Match();
		addMatch(match);
		builder.accept(match);
		return this;
	}




	public String getDescription() {
		return description;
	}

	public Query setDescription(String description) {
		this.description = description;
		return this;
	}



	@Override
	public Query setIri(String iri) {
		super.setIri(iri);
		return this;
	}



	@Override
	public Query setName(String name) {
		super.setName(name);
		return this;
	}






	public List<OrderLimit> getOrderBy() {
		return orderBy;
	}

	public Query setOrderBy(List<OrderLimit> orderBy) {
		this.orderBy = orderBy;
		return this;
	}


	public List<Element> getGroupBy() {
		return groupBy;
	}

	public Query setGroupBy(List<Element> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Query addGroupBy(Element group){
		if (this.groupBy==null)
			this.groupBy= new ArrayList<>();
		this.groupBy.add(group);
		return this;
	}

	public Query groupBy(Consumer<Element> builder){
		Element group= new Element();
		addGroupBy(group);
		builder.accept(group);
		return this;
	}





	public List<Query> getQuery() {
		return query;
	}

	@JsonSetter
	public Query setQuery(List<Query> query) {
		this.query = query;
		return this;
	}

	public Query addQuery(Query query){
		if (this.query==null)
			this.query= new ArrayList<>();
		this.query.add(query);
		return this;
	}

	public Query query(Consumer<Query> builder){
		Query query= new Query();
		addQuery(query);
		builder.accept(query);
		return this;
	}

	public boolean isActiveOnly() {
		return activeOnly;
	}

	public Query setActiveOnly(boolean activeOnly) {
		this.activeOnly = activeOnly;
		return this;
	}

	public boolean isUsePrefixes() {
		return usePrefixes;
	}

	public Query setUsePrefixes(boolean usePrefixes) {
		this.usePrefixes = usePrefixes;
		return this;
	}

	public List<Select> getSelect() {
		return select;
	}

	@JsonSetter
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



	public Query select(Consumer<Select> builder){
		if (this.select==null)
			this.select= new ArrayList<>();
		Select select= new Select();
		this.select.add(select);
		builder.accept(select);
		return this;
	}




}
