package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"prefix","iri","name","description","activeOnly","bool","match","return","construct","query","groupBy","orderBy"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends Match{
	private Prefixes prefixes;
	private String description;
	private boolean activeOnly;
	private List<Query> query;
	private List<Match> match;
	private OrderLimit orderBy;
	private List<PropertyRef> groupBy;
	private List<Return> returx;
	private String iri;
	private String name;

	public Prefixes getPrefixes() {
		return prefixes;
	}

	public Query setPrefixes(Prefixes prefixes) {
		this.prefixes = prefixes;
		return this;
	}

	public Query addPrefix(String prefix, String namespace){
		Prefix newPrefix= new Prefix().setPrefix(prefix).setNamespace(namespace);
		if (this.prefixes==null){
			this.prefixes= new Prefixes();
		}
		prefixes.add(newPrefix);
		return this;
	}

	@Override
	public Query setBool(Bool bool) {
		super.setBool(bool);
		return this;
	}


	public Query setBoolMatch(Bool boolMatch) {
		super.setBoolMatch(boolMatch);
		return this;
	}

	public Query setBoolWhere(Bool boolWhere) {
		super.setBoolWhere(boolWhere);
		return this;
	}


	public Query setTypeOf(String type) {
		super.setTypeOf(type);
		return this;
	}



	@JsonProperty("return")
	public List<Return> getReturn() {
		return returx;
	}

	public Query setReturn(List<Return> returx) {
		this.returx = returx;
		return this;
	}

	public Query addReturn(Return aReturn){
		if (this.returx==null)
			this.returx= new ArrayList<>();
		this.returx.add(aReturn);
		return this;
	}

	public Query return_(Consumer<Return> builder){
		Return ret= new Return();
		addReturn(ret);
		builder.accept(ret);
		return this;
	}


	public List<Match> getMatch() {
		return match;
	}

	public Query setMatch(List<Match> from) {
		this.match = from;
		return this;
	}

	public Query addMatch(Match match) {
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

	public String getIri() {
		return iri;
	}

	public Query setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public String getName() {
		return name;
	}

	public Query setName(String name) {
		this.name = name;
		return this;
	}

	public OrderLimit getOrderBy() {
		return orderBy;
	}

	public Query setOrderBy(OrderLimit orderBy) {
		this.orderBy = orderBy;
		return this;
	}


	public List<PropertyRef> getGroupBy() {
		return groupBy;
	}

	public Query setGroupBy(List<PropertyRef> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Query addGroupBy(PropertyRef group){
		if (this.groupBy==null)
			this.groupBy= new ArrayList<>();
		this.groupBy.add(group);
		return this;
	}

	public Query groupBy(Consumer<PropertyRef> builder){
		PropertyRef group= new PropertyRef();
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






}
