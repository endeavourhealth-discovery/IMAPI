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
public class Match extends Node{
	private Bool bool;
	private List<Match> match;
	private List<Node> in;
	private boolean exclude;
	private Element graph;
	private List<Property> property;
	private String description;
	private List<OrderLimit> orderBy;
	private String nodeRef;


	public List<Node> getIn() {
		return in;
	}

	@JsonSetter
	public Match setIn(List<Node> in) {
		this.in = in;
		return this;
	}




	public Match addIn(Node in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(in);
		return this;
	}

	public Match in(Consumer<Node> builder){
		Node in = new Node();
		addIn(in);
		builder.accept(in);
		return this;
	}

	public String getNodeRef() {
		return nodeRef;
	}

	public Match setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
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

	public List<Property> getProperty() {
		return property;
	}

 @JsonSetter
	public Match setProperty(List<Property> property) {
		this.property = property;
		return this;
	}

	public Match addProperty(Property prop){
		if (this.property ==null)
			this.property = new ArrayList<>();
		this.property.add(prop);
		return this;
	}

	public Match property(Consumer<Property> builder){
		Property prop= new Property();
		addProperty(prop);
		builder.accept(prop);
		return this;
	}





}
