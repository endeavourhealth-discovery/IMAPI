package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@JsonPropertyOrder({"alias","path","property","filtered","sum","average","name","inverseOf","alias","argument","function","select","where","orderBy","direction","limit","groupBy","having"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Select  extends TTAlias{
	private TTAlias property;
	private String path;
	private Aggregate aggregate;
	List<Argument> argument;
	Function function;
	private List<Select> select;
	private Where where;
	private List<OrderBy> orderBy;
	private Integer limit;
	private String direction;
	private List<TTAlias> groupBy;
	private Having having;



	@JsonSetter
	public Select setAlias(String alias) {
		super.setAlias(alias);
		return this;
	}


	public Having getHaving() {
		return having;
	}

	@JsonSetter
	public Select setHaving(Having having) {
		this.having = having;
		return this;
	}


	public Select having(Consumer<Having> builder) {
		this.having= new Having();
		builder.accept(this.having);
		return this;
	}

	public String getDirection() {
		return direction;
	}

	public Select setDirection(String direction) {
		this.direction = direction;
		return this;
	}

	public Aggregate getAggregate() {
		return aggregate;
	}

	public Select setAggregate(Aggregate aggregate) {
		this.aggregate = aggregate;
		return this;
	}


	public List<TTAlias> getGroupBy() {
		return groupBy;
	}

	@JsonSetter
	public Select setGroupBy(List<TTAlias> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Select addGroupBy(TTAlias groupBy){
		if (this.groupBy==null)
			this.groupBy= new ArrayList<>();
		this.groupBy.add(groupBy);
		return this;
	}


	public List<Select> getSelect() {
		return select;
	}

	public Select setSelect(List<Select> select) {
		this.select = select;
		return this;
	}

	public Where getWhere() {
		return where;
	}

	@JsonSetter
	public Select setWhere(Where where) {
		this.where = where;
		return this;
	}
	public Select where(Consumer<Where> builder){
		Where where= new Where();
		this.where=where;
		builder.accept(where);
		return this;
	}

	public Select select(Consumer<Select> builder){
		if (this.select==null)
			this.select= new ArrayList<>();
		Select select= new Select();
		this.select.add(select);
		builder.accept(select);
		return this;
	}



	public Select addSelect(Select select){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(select);
		return this;
	}

	public Select addSelect(String property){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(new Select().setProperty(property));
		return this;
	}

	public List<OrderBy> getOrderBy() {
		return orderBy;
	}

	@JsonSetter
	public Select setOrderBy(List<OrderBy> orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Select addOrderBy(OrderBy orderBy){
		if (this.orderBy==null)
			this.orderBy= new ArrayList<>();
		this.orderBy.add(orderBy);
		return this;
	}

	public Select orderBy(Consumer<OrderBy> builder){
		OrderBy orderBy= new OrderBy();
		addOrderBy(orderBy);
		builder.accept(orderBy);
		return this;
	}



	public Integer getLimit() {
		return limit;
	}

	public Select setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	public String getPath() {
		return path;
	}

	public Select setPath(String path) {
		this.path = path;
		return this;
	}

	@JsonSetter
	public Select setProperty(TTAlias property) {
		this.property = property;
		return this;
	}

	public Select property(Consumer<TTAlias> builder){
		this.property= new TTAlias();
		builder.accept(this.property);
		return this;
	}

	public TTAlias getProperty() {
		return property;
	}


	public Select setProperty(String propertyIri) {
		this.property = new TTAlias().setIri(propertyIri);
		return this;
	}

	public Select setProperty(TTIriRef property) {
		this.property = new TTAlias(property);
		return this;
	}


	public List<Argument> getArgument() {
		return argument;
	}

	@JsonSetter
	public Select setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}

	public Select addArgument(Argument argument){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}

	public Select argument(Consumer<Argument> builder){
		Argument arg= new Argument();
		this.addArgument(arg);
		builder.accept(arg);
		return this;
	}





	public Select function(Consumer<Function> builder){
		this.function= new Function();
		builder.accept(this.function);
		return this;
	}

	public Function getFunction() {
		return function;
	}

	public Select setFunction(Function function) {
		this.function = function;
		return this;
	}




}
