package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"prefix","iri","name","description","from","where","select","subQuery"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends TTAlias {
	private String description;
	private List<From> from;
	private List<Select> select;
	private Where where;
	private List<TTAlias> orderBy;
	private String direction;
	private Integer limit;
	private List<TTAlias> groupBy;
	private TTContext prefix;
	private List<Query> subQuery;
	private boolean activeOnly;
	private boolean usePrefixes;

	public String getDescription() {
		return description;
	}

	public Query setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDirection() {
		return direction;
	}

	public Query setDirection(String direction) {
		this.direction = direction;
		return this;
	}


	public List<From> getFrom() {
		return from;
	}

	@JsonSetter
	public Query setFrom(List<From> from) {
		this.from=from;
		return this;
	}

	public Query from(Consumer<From> builder){
		From from =new From();
		this.addFrom(from);
		builder.accept(from);
		return this;
	}

	public Query addFrom(From from){
		if (this.from==null)
			this.from= new ArrayList<>();
		this.from.add(from);
		return this;
	}



	public Query where(Consumer<Where> builder){
		Where where= new Where();
		this.where=where;
		builder.accept(where);
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

	public Query addSelect(String property){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(new Select().setProperty(property));
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


	public Query select(String property){
		if (this.select==null)
			this.select= new ArrayList<>();
		Select select= new Select();
		this.select.add(select);
		select.setProperty(property);
		return this;
	}


	public Query select(TTIriRef property){
		if (this.select==null)
			this.select= new ArrayList<>();
		Select select= new Select();
		this.select.add(select);
		select.setProperty(property);
		return this;
	}

	public Where getWhere() {
		return where;
	}

	@JsonSetter
	public Query setWhere(Where where) {
		this.where = where;
		return this;
	}

	public List<TTAlias> getOrderBy() {
		return orderBy;
	}

	public Query setOrderBy(List<TTAlias> orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Query addOrderBy(TTAlias orderBy){
		if (this.orderBy==null)
			this.orderBy= new ArrayList<>();
		this.orderBy.add(orderBy);
		return this;
	}

	public Integer getLimit() {
		return limit;
	}

	public Query setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	public List<TTAlias> getGroupBy() {
		return groupBy;
	}

	public Query setGroupBy(List<TTAlias> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Query addGroupBy(TTAlias groupBy){
		if (this.groupBy==null)
			this.groupBy= new ArrayList<>();
		this.groupBy.add(groupBy);
		return this;
	}


	public boolean isUsePrefixes() {
		return usePrefixes;
	}

	public Query setUsePrefixes(boolean usePrefixes) {
		this.usePrefixes = usePrefixes;
		return this;
	}

	public boolean isActiveOnly() {
		return activeOnly;
	}

	public Query setActiveOnly(boolean activeOnly) {
		this.activeOnly = activeOnly;
		return this;
	}

	public TTContext getPrefix() {
		return prefix;
	}

	public Query setPrefix(TTContext prefix) {
		this.prefix = prefix;
		return this;
	}

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


	public Query setName(String name) {
		super.setName(name);
		return this;
	}





	@JsonIgnore
	public String getJson() throws JsonProcessingException {
		return Query.getJson(this);
	}

	public static String getJson(Object object) throws JsonProcessingException {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        }
	}

}
