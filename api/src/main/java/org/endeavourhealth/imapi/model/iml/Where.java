package org.endeavourhealth.imapi.model.iml;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@JsonPropertyOrder({"alias","from","graph","path","notExist","not","inverse","property","in","range","and","or","compare","function","argument","value"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Where {
	private String alias;
	private String description;
	private List<TTAlias> from;
	private String graph;
	private boolean not;
	private String path;
	private TTAlias property;
	private List<TTAlias> in;
	private TTAlias is;
	private Range range;
	private List<Argument> argument;
	private List<Where> and;
	private List<Where> or;
	private Where notExist;
	private Function function;
	private Where where;
	private Value value;
	private List<OrderBy> orderBy;
	private Integer limit;

	public String getPath() {
		return path;
	}

	public Where setPath(String path) {
		this.path = path;
		return this;
	}

	public List<OrderBy> getOrderBy() {
		return orderBy;
	}

	public Where setOrderBy(List<OrderBy> orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Where addOrderBy(OrderBy orderBy){
		if (this.orderBy==null)
			this.orderBy= new ArrayList<>();
		this.orderBy.add(orderBy);
		return this;
	}
	public Where orderBy(Consumer<OrderBy> builder){
		OrderBy orderBy= new OrderBy();
		addOrderBy(orderBy);
		builder.accept(orderBy);
		return this;
	}

	public Integer getLimit() {
		return limit;
	}

	public Where setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	public List<TTAlias> getFrom() {
		return from;
	}

	@JsonSetter
	public Where setFrom(List<TTAlias> from) {
		this.from = from;
		return this;
	}

	public Where addFrom(TTAlias from){
		if (this.from==null)
			this.from= new ArrayList<>();
		this.from.add(from);
		return this;
	}

	public Where from(Consumer<TTAlias> builder) {
		TTAlias from= new TTAlias();
		this.addFrom(from);
		builder.accept(from);
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Where setDescription(String description) {
		this.description = description;
		return this;
	}

	@JsonSetter
	public Where setProperty(TTAlias property) {
		this.property = property;
		return this;
	}


	public Where setProperty(String property) {
			this.property = new TTAlias().setIri(property);
		return this;
	}


	public Where setProperty(TTIriRef property) {
		this.property = new TTAlias().setIri(property.getIri());
		return this;
	}

	public Where property(Consumer<TTAlias> builder){
		this.property= new TTAlias();
		builder.accept(this.property);
		return this;
	}


	public Value getValue() {
		return value;
	}

	@JsonSetter
	public Where setValue(Value value) {
		this.value = value;
		return this;
	}

	public Where value(Consumer<Value> builder){
		this.value= new Value();
		builder.accept(value);
		return this;
	}




	public String getAlias() {
		return alias;
	}

	public Where setAlias(String alias) {
		this.alias = alias;
		return this;
	}



	public Where getWhere() {
		return where;
	}

	@JsonSetter
	public Where setWhere(Where where) {
		this.where = where;
		return this;
	}


	public Where where(Consumer<Where> builder){
		this.where= new Where();
		builder.accept(this.where);
		return this;
	}

	public String getGraph() {
		return graph;
	}

	public Where setGraph(String graph) {
		this.graph = graph;
		return this;
	}


	public Where getNotExist() {
		return notExist;
	}

	@JsonSetter
	public Where setNotExist(Where notExist) {
		this.notExist = notExist;
		return this;
	}


	public Where notExist(Consumer<Where> builder){
		this.notExist= new Where();
		builder.accept(this.notExist);
		return this;
	}





	public Function getFunction() {
		return function;
	}

	public Where setFunction(Function function) {
		this.function = function;
		return this;
	}

	public boolean isNot() {
		return not;
	}

	public Where setNot(boolean not) {
		this.not = not;
		return this;
	}

	public TTAlias getProperty() {
		return property;
	}




	public List<TTAlias> getIn() {
		return in;
	}

	public Where setIn(List<TTAlias> in) {
		this.in = in;
		return this;
	}

	public Where addIn(TTAlias in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(in);
		return this;
	}
	public TTAlias getIs() {
		return is;
	}

	@JsonSetter
	public Where setIs(TTAlias is) {
		this.is = is;
		return this;
	}

	public Where setIs(String is) {
		this.is = new TTAlias().setIri(is);
		return this;
	}


	public Range getRange() {
		return range;
	}

	public Where setRange(Range range) {
		this.range = range;
		return this;
	}

	public List<Argument> getArgument() {
		return argument;
	}

	@JsonSetter
	public Where setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}
	public Where addArgument(Argument argument){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}


	public Where argument(Consumer<Argument> builder){
		Argument argument= new Argument();
		addArgument(argument);
		builder.accept(argument);
		return this;
	}

	public List<Where> getOr() {
		return or;
	}

	public Where setOr(List<Where> or) {
		this.or = or;
		return this;
	}
	
	public Where addOr(Where or){
		if (this.or== null)
			this.or= new ArrayList<>();
		this.or.add(or);
		return this;
	}
	public Where or(Consumer<Where> builder){
		Where or = new Where();
		addOr(or);
		builder.accept(or);
		return this;
		
	}
	public List<Where> getAnd() {
		return and;
	}

	public Where setAnd(List<Where> and) {
		this.and = and;
		return this;
	}

	public Where addAnd(Where and){
		if (this.and== null)
			this.and= new ArrayList<>();
		this.and.add(and);
		return this;
	}
	public Where and(Consumer<Where> builder){
		Where and = new Where();
		addAnd(and);
		builder.accept(and);
		return this;

	}
}
