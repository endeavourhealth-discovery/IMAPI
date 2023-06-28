package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"exclude","nodeRef","boolMatch","boolWhere","boolPath","description","graph","iri","set","type","name","path","descendantsOrSelfOf","descendantsOf",
	"ancestorsOf","description","match","where"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends Node implements Whereable{
	private Bool boolMatch;
	private Bool boolWhere;
	private Bool boolPath;
	private List<Match> match;
	private boolean exclude;
	private Element graph;
	private List<Where> where;
	private String description;
	private List<OrderLimit> orderBy;
	private String nodeRef;
	private List<Path> path;

	public String getNodeRef() {
		return nodeRef;
	}

	public Match setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
		return this;
	}

	public Bool getBoolWhere() {
		return boolWhere;
	}

	public Match setBoolWhere(Bool boolWhere) {
		this.boolWhere = boolWhere;
		return this;
	}

	public Bool getBoolPath() {
		return boolPath;
	}

	public Match setBoolPath(Bool boolPath) {
		this.boolPath = boolPath;
		return this;
	}

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



	public Match setPath(List<Path> path){
		this.path=path;
		return this;
	}

	public List<Path> getPath(){
		return this.path;
	}

	public Match addPath(Path path) {

		if (this.path==null)
			this.path= new ArrayList<>();
		this.path.add(path);
		return this;
	}

	public Match path(Consumer<Path> builder){
		Path path= new Path();
		addPath(path);
		builder.accept(path);
		return this;
	}

	public boolean isExclude() {
		return exclude;
	}

	public Match setExclude(boolean exclude) {
		this.exclude = exclude;
		return this;
	}

	public Bool getBoolMatch() {
		return boolMatch;
	}

	public Match setBoolMatch(Bool boolMatch) {
		this.boolMatch = boolMatch;
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

	public Match addMatch(Match match){
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
