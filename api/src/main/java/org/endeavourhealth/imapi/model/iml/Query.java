package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"@context","iri","name","description","from","where","select","subQuery","groupBy","orderBy","direction","limit","having"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends TTIriRef{
	private String description;
	private List<TTAlias> from;
	private List<Select> select;
	private Where where;
	private List<TTAlias> orderBy;
	private String direction;
	private Integer limit;
	private List<TTAlias> groupBy;
	private Having having;
	private TTContext context;
	private List<Query> subQuery;
	private boolean activeOnly;
	private boolean usePrefixes;

	public Query(){
		this.context= new TTContext();
		this.context.add(IM.NAMESPACE,"");
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
		this.having= new Having();
		builder.accept(this.having);
		return this;
	}

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


	public List<TTAlias> getFrom() {
		return from;
	}

	@JsonSetter
	public Query setFrom(List<TTAlias> from) {
		this.from=from;
		return this;
	}

	public Query from(Consumer<TTAlias> builder){
		TTAlias from =new TTAlias();
		this.addFrom(from);
		builder.accept(from);
		return this;
	}

	public Query addFrom(TTAlias from){
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

	@JsonProperty("@context")
	public TTContext getContext() {
		return context;
	}

	public Query setContext(TTContext context) {
		this.context = context;
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
