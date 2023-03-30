package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTContext;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"@context","iri","name","description","from","select","subQuery","groupBy","orderBy","direction","limit","having"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends TTAlias{

	private String description;
	private List<Select> select;
	private boolean activeOnly;
	private boolean usePrefixes;
	private From from;
	private List<Query> query;

	private List<OrderLimit> orderBy;
	private Integer limit;
	private String direction;
	private List<TTAlias> groupBy;
	private Having having;
	private List<Case> caze;



	public From getFrom() {
		return from;
	}

	public Query setFrom(From from) {
		this.from=from;
		return this;
	}


	public Query from(Consumer<From> builder){
		this.from= new From();
		builder.accept(this.from);
		return this;
	}



	public String getDescription() {
		return description;
	}

	public Query setDescription(String description) {
		this.description = description;
		return this;
	}

	public List<Case> getCaze() {
		return caze;
	}

	public Query setCaze(List<Case> caze) {
		this.caze = caze;
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


	@Override
	public Query setAlias(String alias) {
		super.setAlias(alias);
		return this;
	}


	@JsonProperty("case")
	public List<Case> getCase() {
		return caze;
	}

	public Query setCase(List<Case> caze) {
		this.caze = caze;
		return this;
	}

	public Query addCase(Case caze) {
		if (this.caze == null)
			this.caze = new ArrayList<>();
		this.caze.add(caze);
		return this;
	}

	public Having getHaving() {
		return having;
	}

	@JsonSetter
	public Query setHaving(Having having) {
		this.having = having;
		return this;
	}


	public Query having(Consumer<Having> builder) {
		this.having = new Having();
		builder.accept(this.having);
		return this;
	}



	public List<OrderLimit> getOrderBy() {
		return orderBy;
	}

	public Query setOrderBy(List<OrderLimit> orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Integer getLimit() {
		return limit;
	}

	public Query setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	public String getDirection() {
		return direction;
	}

	public Query setDirection(String direction) {
		this.direction = direction;
		return this;
	}

	public List<TTAlias> getGroupBy() {
		return groupBy;
	}

	public Query setGroupBy(List<TTAlias> groupBy) {
		this.groupBy = groupBy;
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
