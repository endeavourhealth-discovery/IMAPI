package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"exclude","nodeRef","boolMatch","boolWhere","boolPath","description","graph","iri","set","type","name","path","descendantsOrSelfOf","descendantsOf",
	"ancestorsOf","description","match","where"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends IriLD {
	private Bool bool;
	private List<Match> match;
	private List<Node> inSet;
	private boolean exclude;
	private Element graph;
	private List<Property> property;
	private String description;
	private List<OrderLimit> orderBy;
	private String nodeRef;
	private boolean optional;
	private FunctionClause aggregate;
	private Node instanceOf;
	private Node typeOf;
	private String variable;
	private String name;


	@Override
	public Match setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	public String getVariable() {
		return variable;
	}

	public Match setVariable(String variable) {
		this.variable = variable;
		return this;
	}

	public String getName() {
		return name;
	}

	public Node getInstanceOf() {
		return instanceOf;
	}

	public Match setInstanceOf(Node instanceOf) {
		this.instanceOf = instanceOf;
		return this;
	}

	public Node getTypeOf() {
		return typeOf;
	}

	@JsonSetter
	public Match setTypeOf(Node typeOf) {
		this.typeOf = typeOf;
		return this;
	}

	public FunctionClause getAggregate() {
		return aggregate;
	}

	public Match setAggregate(FunctionClause aggregate) {
		this.aggregate = aggregate;
		return this;
	}

	public Match aggregate(Consumer<FunctionClause> builder){
		FunctionClause function= new FunctionClause();
		this.aggregate= function;
		builder.accept(function);
		return this;
	}

	public boolean isOptional() {
		return optional;
	}

	public Match setOptional(boolean optional) {
		this.optional = optional;
		return this;
	}

	public List<Node> getInSet() {
		return inSet;
	}

	@JsonSetter
	public Match setInSet(List<Node> inSet) {
		this.inSet = inSet;
		return this;
	}




	public Match addInSet(Node in){
		if (this.inSet ==null)
			this.inSet = new ArrayList<>();
		this.inSet.add(in);
		return this;
	}

	public Match inSet(Consumer<Node> builder){
		Node in = new Node();
		addInSet(in);
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


	public Match setTypeOf(String type){
		this.typeOf= new Node().setIri(type);
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




	public Match setName(String name) {
		this.name=name;
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
