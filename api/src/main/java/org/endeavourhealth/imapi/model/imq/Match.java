package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"exclude","boolMatch","description","graph","iri","set","type","name","path","descendantOrSelfOf","descendantOf",
	"ancestorOf","description","bool","match","where"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends Node implements Whereable{
	private Bool boolMatch;
	private List<Match> match;
	private Bool bool;
	private boolean exclude;
	private Element graph;
	private List<Where> where;
	private String description;
	private List<OrderLimit> orderBy;

	public List<OrderLimit> getOrderBy() {
		return orderBy;
	}

	@JsonSetter
	public Match setOrderBy(List<OrderLimit> orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Match setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public Match setParameter(String parameter){
		super.setParameter(parameter);
		return this;
	}


	public Match addOrderBy(OrderLimit orderBy) {
		if (this.orderBy == null)
			this.orderBy = new ArrayList<>();
		this.orderBy.add(orderBy);
		return this;
	}

	public Match orderBy(Consumer<OrderLimit> builder) {
		OrderLimit orderBy = new OrderLimit();
		addOrderBy(orderBy);
		builder.accept(orderBy);
		return this;
	}



	public Match setPath(Path path) {
		super.setPath(path);
		return this;
	}

	public Match path(Consumer<Path> builder){
		if (getPath()!=null)
			throw new IllegalArgumentException("Builder should not be used to overwrite properties ");
		super.path(builder);
		return this;
	}

	public boolean isExclude() {
		return exclude;
	}

	public Match setExclude(boolean exclude) {
		this.exclude = exclude;
		return this;
	}

	public Bool getBool() {
		return bool;
	}

	public Match setBool(Bool bool) {
		this.bool = bool;
		return this;
	}

	public Element getGraph() {
		return graph;
	}

	public Match setGraph(Element graph) {
		this.graph = graph;
		return this;
	}


	public Match setType(String type){
		super.setType(type);
		return this;
	}

	public Match setSet(String set){
		super.setSet(set);
		return this;
	}


	public Bool getBoolMatch() {
		return boolMatch;
	}

	public Match setBoolMatch(Bool boolMatch) {
		this.boolMatch = boolMatch;
		return this;
	}


	public List<Match> getMatch() {
		return match;
	}

	@JsonSetter
	public Match setMatch(List<Match> match) {
		this.match = match;
		return this;
	}
	public Match match(Consumer<Match> builder){
		Match match = new Match();
		addMatch(match);
		builder.accept(match);
		return this;
	}

	public Match addMatch(Match match) {
		if (this.match ==null)
			this.match = new ArrayList<>();
		this.match.add(match);
		return this;
	}





	public Match setVariable(String variable){
		super.setVariable(variable);
		return this;
	}


	public Match setDescendantsOrSelfOf(boolean include){
		super.setDescendantsOrSelfOf(include);
		return this;
	}

	public Match setAncestorsOf(boolean include){
		super.setAncestorsOf(include);
		return this;
	}

	public Match setName(String name) {
		super.setName(name);
		return this;
	}




	public Match setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public List<Where> getWhere() {
		return where;
	}

 @JsonSetter
	public Match setWhere(List<Where> where) {
		this.where = where;
		return this;
	}

	public Match addWhere(Where where){
		if (this.where==null)
			this.where= new ArrayList<>();
		this.where.add(where);
		return this;
	}

	public Match where(Consumer<Where> builder){
		Where where= new Where();
		addWhere(where);
		builder.accept(where);
		return this;
	}





}
